package tools.sqllogformat;

import lombok.Data;

public @Data class SqlLogLineSet {
    private String sqlLine;
    private String parameterLine;
    
    
}
