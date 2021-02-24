package arm;

import java.io.File;

public class SearchPosition {

    private final File file;
    private final int line;
    private final int start;
    private final int end;

    public SearchPosition(File file, int line, int start, int end) {
        this.file = file;
        this.line = line;
        this.start = start;
        this.end = end;
    }

    public File getFile() {
        return file;
    }

    public int getLine() {
        return line;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return file.getPath() + " -> " + line + ":" + start + ":" + end;
    }
}
