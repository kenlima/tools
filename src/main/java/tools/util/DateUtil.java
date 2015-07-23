package tools.util;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static String getCurrentDate() {
        LocalDate today = LocalDate.now();
        return today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static String getBeforeDay(long days) {
        LocalDate today = LocalDate.now();
        today = today.minusDays(days);
        return today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
