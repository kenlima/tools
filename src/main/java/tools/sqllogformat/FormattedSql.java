package tools.sqllogformat;

import lombok.Data;

public @Data class FormattedSql {
    private String sql;
    private String parameters;

}
