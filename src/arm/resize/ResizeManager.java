package arm.resize;

import arm.utils.DeviceInfo;
import arm.utils.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ResizeManager {

    private volatile static ResizeManager mInstance = null;

    public static ResizeManager getInstance() {
        if (mInstance == null) {
            mInstance = new ResizeManager();
        }
        return mInstance;
    }

    public void resize(ResizeOrder order, OnProgressListener listener) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(DeviceInfo.CORE_NUM);
        Map<ImageSize, File> outputDirectories = generateOutputDirectories(
                order.getOutputDirectory(),
                order.getSizeSet(),
                order.getImageType());

        final float[] progress = {0};
        int orderSize = order.getImageList().size();
        for (File image : order.getImageList()) {
            executor.execute(() -> {
                resizeImage(image, order.getSizeSet(), outputDirectories);
                progress[0] += 1;
                listener.onProcessChange(progress[0] / orderSize);
            });
        }
        executor.shutdown();
    }

    private void resizeImage(File image, Set<ImageSize> sizes, Map<ImageSize, File> directories) {
        for (ImageSize size : sizes) {
            try {
                BufferedImage originalImage = ImageIO.read(image);
                BufferedImage resizedImage = imageBufferedResize(originalImage, size);
                File outputDirectory = directories.get(size);
                String extension = FileUtils.extensionName(image.getName());
                String imagePath = outputDirectory + File.separator + imageNameParser(image.getName()) + "." + extension;
                ImageIO.write(resizedImage, extension, new File(imagePath));
            } catch (IOException e) {
                System.err.println("Can't create this directory : " + e.getMessage());
            }
        }
    }

    private BufferedImage imageBufferedResize(BufferedImage originalImage, ImageSize size) {
        final BufferedImage bufferedImage = new BufferedImage(size.getWidth(), size.getHeight(), BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(originalImage, 0, 0, size.getWidth(), size.getHeight(), null);
        graphics2D.dispose();
        return bufferedImage;
    }

    private Map<ImageSize, File> generateOutputDirectories(File mainDirectory, Set<ImageSize> sizes, ImageType type) {
        String mainPath = mainDirectory.getAbsolutePath();
        Map<ImageSize, File> outputDirectories = new HashMap<>();
        for (ImageSize imageSize : sizes) {
            String sizeName = imageSize.getHint().isEmpty() ? imageSize.sizeString() : imageSize.getHint();
            String path = mainPath + File.separator + type.getName().toLowerCase() + "-" + sizeName;
            File directory = new File(path);
            if (directory.exists()) {
                outputDirectories.put(imageSize, directory);
            } else {
                boolean success = directory.mkdir();
                if (success) {
                    outputDirectories.put(imageSize, directory);
                } else {
                    System.err.println("Can't create this directory : " + directory.getAbsolutePath());
                }
            }
        }
        return outputDirectories;
    }

    private String imageNameParser(String fullName) {
        int dotIndex = fullName.indexOf('.');
        return fullName.substring(0, dotIndex);
    }
}
