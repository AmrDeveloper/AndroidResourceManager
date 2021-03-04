package arm.analysis;

public class Source {

    private final String type;
    private final int linesNum;

    public Source(String type, int linesNum) {
        this.type = type;
        this.linesNum = linesNum;
    }

    public String getType() {
        return type;
    }

    public int getLinesNum() {
        return linesNum;
    }
}
