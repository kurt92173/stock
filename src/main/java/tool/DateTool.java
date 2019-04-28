package tool;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTool {
    public static String getCurrentDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String date = now.format(formatter);
        System.out.println("[info] Today:" + date);

        return date;
    }
}
