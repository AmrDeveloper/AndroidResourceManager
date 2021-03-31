package arm.utils;

import java.util.HashSet;
import java.util.Set;

public class FileUtils {

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
}
