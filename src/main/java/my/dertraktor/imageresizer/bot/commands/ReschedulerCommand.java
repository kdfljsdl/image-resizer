package my.dertraktor.imageresizer.bot.commands;


import my.dertraktor.mq.QueueConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReschedulerCommand extends ImageCountAwareBotCommand {
    private static final Logger log = LogManager.getLogger(ReschedulerCommand.class);
    private static final String PROPER_COMMAND_LINE = "retry [-n <count>]";
    private static final String COMMAND_DESCRIPTION = "Moves all URLs from failed queue back to resize queue";

    /**
     * resize queue connection
     */
    @Autowired
    private QueueConnector resizeQueueConnector;

    /**
     * failed queue connection
     */
    @Autowired
    private QueueConnector failedQueueConnector;


    @Override
    public void execute() throws Exception {
        log.trace("enter");

        String fileNameFromFailedQueue;

        int processedFileCount = 0;

        while ((fileNameFromFailedQueue = failedQueueConnector.getStringFromQueue()) != null && (getFileCountToProcess() == -1 || processedFileCount < getFileCountToProcess())) {
            log.trace(String.format("moving image from failed to resize queue \"%s\"", fileNameFromFailedQueue));

            resizeQueueConnector.sendStringToQueue(fileNameFromFailedQueue);

            processedFileCount++;
        }

        log.info(String.format("leave. %d images moved to resize queue", processedFileCount));
    }


    @Override
    public String getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }

    @Override
    public String getUsageInfo() {
        return PROPER_COMMAND_LINE;
    }
}
