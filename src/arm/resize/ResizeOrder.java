package arm.resize;

import java.io.File;
import java.util.Set;

public class ResizeOrder {

    private final Set<File> imageList;
    private final Set<ImageSize> sizeSet;
    private final ImageType imageType;
    private final File outputDirectory;

    public ResizeOrder(Set<File> images,
                       Set<ImageSize> sizes,
                       ImageType imageType,
                       File outputDirectory) {
        this.imageList = images;
        this.sizeSet = sizes;
        this.imageType = imageType;
        this.outputDirectory = outputDirectory;
    }

    public Set<File> getImageList() {
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
