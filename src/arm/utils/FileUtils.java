package arm.utils;

public class FileUtils {

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
        return (extension.equalsIgnoreCase("png")
                || extension.equalsIgnoreCase("jpeg"));
    }
}
