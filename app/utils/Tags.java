package utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Utility class for template tags
 * Created by taylorak on 4/29/14.
 */
public class Tags{

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String slugify(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    public static String formatTimes(DateTime start_time, DateTime end_time) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("h:mm");
        DateTimeFormatter fmt2 = DateTimeFormat.forPattern("h:mm a");
        String first = fmt.print(start_time);
        String second = fmt2.print(end_time);

        return first + "-" + second;

         //return String.format("%d:%02d", time.getHourOfDay(), time.getMinuteOfHour());
    }
}
