package arm.search;

import arm.utils.SearchPosition;

import java.io.File;

public class KeywordPosition extends SearchPosition {

    public KeywordPosition(File file, int line, int start, int end) {
        super(file, line, start, end);
    }

    @Override
    public String toString() {
        return getFile().getPath() + " -> " + getLine() + ":" + getStart() + ":" + getEnd();
    }
}
