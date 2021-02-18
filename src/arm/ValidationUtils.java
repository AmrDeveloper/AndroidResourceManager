package arm;

import java.util.regex.Pattern;

public class ValidationUtils {

    private final static Pattern INTEGER_PATTERN = Pattern.compile("[0-9]+");

    public static boolean isInteger(String value) {
        return INTEGER_PATTERN.matcher(value).matches();
    }
}