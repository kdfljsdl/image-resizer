package my.dertraktor.imageresizer.bot.commands;

import my.dertraktor.mq.QueueConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Scheduler
 * Accepts a path to the directory with images and schedule them for resize, i.e. adds to resize queue.
 * $ bot.sh schedule ./images
 * Directory images contains only images in different formats:
 * $ ls imagesfirst.png second.jpg third.png 5.jpg
 */
@Service
public class SchedulerCommand extends BotCommand {
    private static final Logger log = LogManager.getLogger(SchedulerCommand.class);
    private static final String PROPER_COMMAND_LINE = "schedule <images directory>";
    private static final String COMMAND_DESCRIPTION = "Add filenames to resize queue";

    /**
     * Connection to resize queue
     */
    @Autowired
    private QueueConnector resizeQueueConnector;

    /**
     * Director, which contains files to be scheduled
     */
    private String sourceDirectory;

    /**
     * @see BotCommand#execute()
     */
    @Override
    public void execute() throws Exception {
        log.trace("enter");

        File sourceDir = new File(sourceDirectory);

        File[] files = sourceDir.listFiles();

        log.trace(String.format("\tsourceDir:%s, imageFileCount:%d", sourceDir.getAbsolutePath(), files.length));

        int scheduledFileCount = 0;
        for (File file : files) {
            log.trace(String.format("\tsending file \"%s\" to resize queue", file.getAbsolutePath()));
            try {
                resizeQueueConnector.sendStringToQueue(file.getAbsolutePath());
                scheduledFileCount++;
            } catch (Exception e) {
                log.trace(String.format("\terror sending file \"%s\" to resize queue", file.getAbsolutePath()), e);
            }
        }
        log.info(String.format("%d files scheduled to resize", scheduledFileCount));
    }

    /**
     * @see BotCommand#parseArgs(String[])
     */
    @Override
    public void parseArgs(String[] args) throws InvalidCommandLineException {
        if (args.length != 2)
            throw new InvalidCommandLineException(PROPER_COMMAND_LINE, args);
        sourceDirectory = args[1];
    }

    /**
     * @see BotCommand#getCommandDescription()
     */
    @Override
    public String getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }

    /**
     * @see BotCommand#getUsageInfo()
     */
    @Override
    public String getUsageInfo() {
        return PROPER_COMMAND_LINE;
    }

    /**
     * @see SchedulerCommand#sourceDirectory
     */
    public String getSourceDirectory() {
        return sourceDirectory;
    }

    /**
     * @see SchedulerCommand#sourceDirectory
     */
    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }
}
