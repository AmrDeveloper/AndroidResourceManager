package arm;

import java.io.File;
import java.util.List;
import java.util.Set;

public class ResizeOrder {

    private final List<File> imageList;
    private final Set<ImageSize> sizeSet;
    private final ImageType imageType;
    private final File outputDirectory;

    public ResizeOrder(List<File> images,
                       Set<ImageSize> sizes,
                       ImageType imageType,
                       File outputDirectory) {
        this.imageList = images;
        this.sizeSet = sizes;
        this.imageType = imageType;
        this.outputDirectory = outputDirectory;
    }

    public List<File> getImageList() {
        return imageList;
    }

    public Set<ImageSize> getSizeSet() {
        return sizeSet;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }
}
