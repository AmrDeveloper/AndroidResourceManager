package arm;

public enum ImageType {

    DRAWABLE("Drawable"),
    MIPMAP("Mipmap"),
    OTHER("Other");

    private final String name;

    ImageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
