package arm.dimens;

import arm.utils.SearchPosition;

import java.io.File;

public class DimensionPosition extends SearchPosition {

    private final String value;
    private static final String DIMENSION_FORMAT = "<dimen name=\"%s\">%s</dimen>";

    public DimensionPosition(File file, String value, int line, int start, int end) {
        super(file, line, start, end);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getFormattedValue() {
        return String.format(DIMENSION_FORMAT, value, value);
    }

    @Override
    public String toString() {
        return getFile().getPath() + " " + getValue() + " -> " + getLine() + ":" + getStart() + ":" + getEnd();
    }
}
