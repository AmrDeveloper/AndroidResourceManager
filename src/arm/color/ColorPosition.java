package arm.color;

import arm.utils.SearchPosition;

import java.io.File;

public class ColorPosition extends SearchPosition {

    private final String value;

    public ColorPosition(File file, String value, int line, int start, int end) {
        super(file, line, start, end);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getFile().getPath() + " " + getValue() + " -> " + getLine() + ":" + getStart() + ":" + getEnd();
    }
}
