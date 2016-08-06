package my.dertraktor.imageresizer.bot.commands;

import my.dertraktor.cloudstorage.FileUploader;
import my.dertraktor.mq.QueueConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * Uploader
 * Uploads next count of images from upload queue to one of the remote storages. Type of cloud
 * storage and corresponding credentials should be set in config file.
 * Dropbox = remote storage is supported
 * After image is uploaded move its filename to done queue. In case of any error move filename to
 * failed queue.
 * $ bot.sh upload [-n <count>]
 * If parameter -n is omitted upload should work on all images from the queue.
 */
@Service
public class UploaderCommand extends ImageCountAwareBotCommand {
    private static final Logger log = LogManager.getLogger(UploaderCommand.class);
    private static final String PROPER_COMMAND_LINE = "upload [-n <count>]";
    private static final String COMMAND_DESCRIPTION = "Upload next images to remote storage";

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
     * done queue connection
     */
    @Autowired
    private QueueConnector doneQueueConnector;


    /**
     * A tool to upload images to remote storage
     */
    @Autowired
    private FileUploader uploader;

    /**
     * @see BotCommand#execute()
     */
    @Override
    public void execute() throws Exception {
        log.trace("enter execute");
        int processedImageCount = 0;
        String fileName = null;
        while ((fileName = uploadQueueConnector.getStringFromQueue()) != null &&
                (getFileCountToProcess() == -1 || processedImageCount < getFileCountToProcess())) {
            File resizedFile = new File(fileName);
            log.trace(String.format("\tuploading file \"%s\".", resizedFile.getAbsolutePath()));
            try {
                uploader.uploadFile(resizedFile);
                resizedFile.delete();
                doneQueueConnector.sendStringToQueue(fileName);
                processedImageCount++;
            } catch (Exception e) {
                log.error(String.format("\terror processing file \"%s\".", fileName), e);
                failedQueueConnector.sendStringToQueue(fileName);
            }
        }

        log.trace(String.format("leave execute. %n files uploaded successfully.", processedImageCount));
    }

    /**
     * @see BotCommand#getCommandDescription()
     */
    @Override
    public String getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }

    /**
     * @see my.dertraktor.imageresizer.bot.commands.BotCommand#getUsageInfo()
     */
    @Override
    public String getUsageInfo() {
        return PROPER_COMMAND_LINE;
    }

}
