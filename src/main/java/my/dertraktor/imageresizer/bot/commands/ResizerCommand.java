package my.dertraktor.imageresizer.bot.commands;

import my.dertraktor.imageresizer.ImageResizer;
import my.dertraktor.mq.QueueConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Takes next count of images from resize queue and resizes them to 640x640 pixels in jpg format. If
 * image is not a square shape resizer should make it square by means of adding a white background. If
 * there is an error URL should be moved to failed queue.
 * $ bot.sh resize [-n <count>]
 * If parameter -n is omitted resize should work on all images from resize queue.
 * Resized images should be stored in directory called images_resized. If resize goes well original image
 * should be removed from images directory.
 */
@Service
public class ResizerCommand extends ImageCountAwareBotCommand {
    private static final Logger log = LogManager.getLogger(ResizerCommand.class);
    private static final String PROPER_COMMAND_LINE = "resize [-n <count>]";
    private static final String COMMAND_DESCRIPTION = "Resize next images from the queue";

    /**
     * resize queue connection
     */
    @Autowired
    private QueueConnector resizeQueueConnector;

    /**
     * upload queue connection
     */
    @Autowired
    private QueueConnector uploadQueueConnector;

    /**
     * failed queue connection
     */
    @Autowired
    private QueueConnector failedQueueConnector;

    /**
     * The Tool to resize images
     */
    @Autowired
    private ImageResizer imageResizer;

    /**
     * The directory where resized images are stored
     */
    @Autowired
    private String resizedImagesDirectory;

    /**
     * @see my.dertraktor.imageresizer.bot.commands.BotCommand#execute()
     */
    @Override
    public void execute() throws Exception {
        log.trace("enter");
        File resizedImagesDir = new File(resizedImagesDirectory);

        // creating resized images directory
        resizedImagesDir.mkdirs();

        if (!resizedImagesDir.exists()) {
            log.error(String.format("resized images directory \"%s\" does not exists and cannot be created.", resizedImagesDir.getAbsolutePath()));
            return;
        }

        int processedFileCount = 0;
        String fileName = null;
        while ((fileName = resizeQueueConnector.getStringFromQueue()) != null
                && (getFileCountToProcess() == -1 || processedFileCount < getFileCountToProcess())) {
            File sourceFile = new File(fileName);
            File destinationDirectory = new File(resizedImagesDirectory);
            try {
                log.trace(String.format("resizing file \"%s\".", sourceFile.getAbsolutePath()));
                File resizedFile = imageResizer.resizeImage(sourceFile, destinationDirectory, true);
                log.trace(String.format("scheduling upload \"%s\".", resizedFile.getAbsolutePath()));
                uploadQueueConnector.sendStringToQueue(resizedFile.getAbsolutePath());
                processedFileCount++;
            } catch (Exception e) {
                log.error(String.format("error resizing file \"%s\".", fileName), e);
                failedQueueConnector.sendStringToQueue(sourceFile.getAbsolutePath());
            }
        }
        log.trace(String.format("leave. %d images processed", processedFileCount));
    }

    /**
     * @see BotCommand#getUsageInfo()
     */
    @Override
    public String getUsageInfo() {
        return PROPER_COMMAND_LINE;
    }

    /**
     * @see BotCommand#getCommandDescription()
     */
    @Override
    public String getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }
}
