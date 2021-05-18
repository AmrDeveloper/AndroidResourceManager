package arm.utils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class FileUtils {

    private static final double KILOBYTES = 1_024;
    private static final double MEGABYTES = 1_048_576;
    private static final double GIGABYTES = 1_073_741_824;

    private static final Set<String> mImageExtensionsSet = new HashSet<>();

    static {
        mImageExtensionsSet.add("png");
        mImageExtensionsSet.add("jpeg");
        mImageExtensionsSet.add("jpg");
    }

    public static String extensionName(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

    public static boolean isImageExtension(String fileName) {
        String extension = extensionName(fileName);
        extension = extension.toLowerCase();
        return mImageExtensionsSet.contains(extension);
    }

    public static String getFormattedFileSize(File file) {
        double lengthInBytes = file.length();
        if (lengthInBytes >= GIGABYTES) return String.format("%.2f", lengthInBytes / GIGABYTES) + " gb";
        if (lengthInBytes >= MEGABYTES) return String.format("%.2f", lengthInBytes / MEGABYTES) + " mb";
        if (lengthInBytes >= KILOBYTES) return String.format("%.2f", lengthInBytes / KILOBYTES) + " kb";
        return String.format("%.2f", lengthInBytes) + " bytes";
    }
}
