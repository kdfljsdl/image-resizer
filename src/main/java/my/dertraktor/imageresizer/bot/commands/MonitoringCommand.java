package my.dertraktor.imageresizer.bot.commands;

import my.dertraktor.imageresizer.bot.Bot;
import my.dertraktor.mq.QueueConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Outputs all queues with a count of URLs in each of them
 */
@Service
public class MonitoringCommand extends BotCommand {
    private static final Logger log = LogManager.getLogger(MonitoringCommand.class);

    private final static String INFO_MESSAGE = Bot.BOT_NAME + "\nQueue\tCount\nresize\t%d\nupload\t%d\ndone\t%d\nfailed\t%d\n";
    private static final String PROPER_COMMAND_LINE = "status";
    private static final String COMMAND_DESCRIPTION = "Output current status in format %queue%:%number_of_images%" ;

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
     * done queue connection
     */
    @Autowired
    private QueueConnector doneQueueConnector;


    /**
     * @see BotCommand#execute()
     */
    @Override
    public void execute() throws Exception {
        log.trace("enter");

        System.out.println(String.format(INFO_MESSAGE,
                resizeQueueConnector.getQueueMessageCount(),
                uploadQueueConnector.getQueueMessageCount(),
                doneQueueConnector.getQueueMessageCount(),
                failedQueueConnector.getQueueMessageCount()));


    }

    /**
     * @see my.dertraktor.imageresizer.bot.commands.BotCommand#parseArgs(String[])
     */
    @Override
    public void parseArgs(String[] args) throws InvalidCommandLineException {
        if (args.length>1)
            throw new InvalidCommandLineException(PROPER_COMMAND_LINE, args);
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
     * @return
     */
    @Override
    public String getUsageInfo() {
        return PROPER_COMMAND_LINE;
    }
}
