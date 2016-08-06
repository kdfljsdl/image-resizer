package my.dertraktor.imageresizer;

import org.apache.logging.log4j.Logger;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.LogManager;

/**
 * Image resizer. Resizes images to 640x640 JPEG format.
 */
@Service
public class ImageResizer {
    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(ImageResizer.class);

    private static final String OUTPUT_IMAGE_FORMAT = "JPG";

    /**
     * Resizes image
     *
     * @param sourceImageFile source image file
     * @param deleteSource delete source if it was actually resized.
     * @throws IOException
     */
    public  File resizeImage(File sourceImageFile, File destImageDirectory, boolean deleteSource) throws IOException, ImageResizerException {
        log.trace(String.format("enter, image \"%s\".", sourceImageFile.getAbsolutePath()));

        String resizedImageFileName = sourceImageFile.getName().replaceAll("\\.[A-Za-z]{3,4}$", ".jpg");

        File resizedImageFile = new File(destImageDirectory, resizedImageFileName);

        // checking if resized file already exists
        if (resizedImageFile.getAbsoluteFile().equals(sourceImageFile.getAbsoluteFile())) {
            // assume that all the files in resized folder are already resized
            log.trace(String.format("Image \"%s\" is already resized.", resizedImageFile.getAbsolutePath()));
            return sourceImageFile;
        }

        if (resizedImageFile.exists()) {
            log.trace(String.format("Resized image \"%s\" already exists.", resizedImageFile.getAbsolutePath()));
            throw new ImageResizerException(String.format("Resized image file with this name (%s) already exists", resizedImageFile.getName()));
        }


        BufferedImage resizedImg;
        BufferedImage img = ImageIO.read(sourceImageFile);
        try {
            Scalr.Mode mode;
            // calculate dimentions
            if (img.getWidth() > img.getHeight()) mode = Scalr.Mode.FIT_TO_WIDTH;
            else mode = Scalr.Mode.FIT_TO_WIDTH;
            // resize image
            resizedImg = Scalr.resize(img, mode, 640, 640);
        } finally {
            img.flush();
        }
        try {
            if (resizedImg.getWidth() == 640 && resizedImg.getHeight() == 640) {
                log.trace(String.format("\tno need to reshape. Saving resized image to file \"%s\".", resizedImageFile.getAbsolutePath()));
                ImageIO.write(resizedImg, OUTPUT_IMAGE_FORMAT, resizedImageFile);
                sourceImageFile.delete();
                return resizedImageFile;
            }

            // resize image
            BufferedImage canvasImg = new BufferedImage(640, 640, img.getType());
            try {
                log.trace(String.format("\tresizing image \"%s\".", resizedImageFile.getAbsolutePath()));
                canvasImg.getGraphics().setColor(Color.WHITE);
                canvasImg.getGraphics().fillRect(0, 0, 640, 640);
                int x = (640 - resizedImg.getWidth()) / 2;
                int y = (640 - resizedImg.getHeight()) / 2;
                canvasImg.getGraphics().drawImage(resizedImg, x, y, null);
                log.trace(String.format("\tsaving resized image to file \"%s\".", resizedImageFile.getAbsolutePath()));
                ImageIO.write(canvasImg, OUTPUT_IMAGE_FORMAT, resizedImageFile);
                sourceImageFile.delete();
                return resizedImageFile;
            } finally {
                canvasImg.flush();
            }
        } finally {
            resizedImg.flush();
        }
    }
}