package tools.sqllogformat;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.pp.para.GFmtOpt;
import gudusoft.gsqlparser.pp.para.GFmtOptFactory;
import gudusoft.gsqlparser.pp.para.GOutputFmt;
import gudusoft.gsqlparser.pp.stmtformatter.FormatterFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Service;

@Service
public class SqlLogFormatService {
    public List<FormattedSql> formatting(String sqlLog) throws IOException {

        // convert String into InputStream
        InputStream is = new ByteArrayInputStream(sqlLog.getBytes());

        // read it with BufferedReader
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        List<SqlLogLineSet> SqlLogLineSets = new ArrayList<>();
        SqlLogLineSet logSet = new SqlLogLineSet();
        while ((line = br.readLine()) != null) {
            int idx = 0;
            if ((idx = line.indexOf("Preparing:", idx)) > -1) {
                idx = idx + 10;
                String sqlLine = line.substring(idx);
                logSet.setSqlLine(sqlLine);
            } else if ((idx = line.indexOf("Parameters:", idx)) > -1) {
                idx = idx + 11;
                String parameterLine = line.substring(idx);
                logSet.setParameterLine(parameterLine);
            }

            if (logSet.getSqlLine() != null && logSet.getParameterLine() != null) {
                SqlLogLineSets.add(logSet);
                logSet = new SqlLogLineSet();
            }

        }
        List<FormattedSql> resultSql = new ArrayList<>();
        for (SqlLogLineSet sqlLogLineSet : SqlLogLineSets) {
            String bindSql = bindingParameter(sqlLogLineSet);

            FormattedSql result = new FormattedSql();

            result.setSql(bindSql);
            result.setParameters(sqlLogLineSet.getParameterLine());
            resultSql.add(result);
        }
        return resultSql;
    }

    private String bindingParameter(SqlLogLineSet sqlLog) {
        StringBuffer mappedQuerySb = new StringBuffer();
        int q1 = 0;
        int q2 = 0;
        int parameterIdx = 0;
        String inputQuery = sqlLog.getSqlLine();
        inputQuery = formattingSql(inputQuery);
        List<String> parameters = extractParameter(sqlLog.getParameterLine());
        while (true) {
            q2 = inputQuery.indexOf("?", q1);
            if (q2 < 0) {
                break;
            }
            mappedQuerySb.append(inputQuery.substring(q1, q2));

            mappedQuerySb.append("<span style='font-weight:bold;color:black'>" + parameters.get(parameterIdx).trim()
                    + "</span>");
            q1 = q2 + 1;
            parameterIdx++;
        }
        mappedQuerySb.append(inputQuery.substring(q1));

        return mappedQuerySb.toString();
    }

    private String formattingSql(String result) {
        System.out.println("extracted sql : " + result);
        TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvmysql);
        sqlparser.setSqltext(result);
        int ret = sqlparser.parse();
        String result2 = null;
        if (ret == 0) {
            GFmtOpt option = GFmtOptFactory.newInstance();
            option.outputFmt = GOutputFmt.ofhtml;
            result2 = FormatterFactory.pp(sqlparser, option);
            System.out.println(result2);
        }
        System.out.println("parsed sql : " + result2);

        return result2;
    }

    private List<String> extractParameter(String parameterLine) {

        List<String> parameters = Arrays.asList(parameterLine.split("[,]"));
        //parameters.replaceAll(String::trim);

        //parameters.stream().map(s -> replaceTypeValue(s));
        parameters.replaceAll(SqlLogFormatService::replaceTypeValue);
        return parameters;
    }

    private static String replaceTypeValue(String name) {
        name = name.trim();
        int idx = 0;
        if ((idx = name.indexOf("String")) > -1) {
            name = "'" + name.substring(0, idx - 1) + "'";
        } else if ((idx = name.indexOf("Integer")) > -1) {
            name = name.substring(0, idx - 1);
        } else if ((idx = name.indexOf("Long")) > -1) {
            name = name.substring(0, idx - 1);
        }
        //name = name.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        name = StringEscapeUtils.escapeHtml(name);
        return name;
    }
}
