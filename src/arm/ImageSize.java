package arm;

import java.util.Objects;

public class ImageSize {

    private final int height;
    private final int width;
    private String hint = "";

    public ImageSize(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public ImageSize(int height, int width, String hint) {
        this.height = height;
        this.width = width;
        this.hint = "(" + hint + ")";
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getHint() {
        return hint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageSize imageSize = (ImageSize) o;
        return height == imageSize.height &&  width == imageSize.width;
    }

    @Override
    public int hashCode() {
        return Objects.hash(height, width);
    }

    @Override
    public String toString() {
        return height + "x" + width + " pixels" + hint;
    }

}
