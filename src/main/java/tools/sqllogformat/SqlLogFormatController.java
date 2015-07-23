package tools.sqllogformat;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SqlLogFormatController {

    @Autowired
    private SqlLogFormatService sqlLogFormatService;

    @RequestMapping("/")
    public String input(ModelAndView modelAndView) {
        return "sqllogformat/input";
    }

    @RequestMapping("/sqllogformat/result")
    public String result(@RequestParam(value = "sqlLog") String sqlLog, Model model) throws IOException {
        List<FormattedSql> result = sqlLogFormatService.formatting(sqlLog);
        model.addAttribute("formattedSqls", result);
        return "sqllogformat/result";
    }
}
