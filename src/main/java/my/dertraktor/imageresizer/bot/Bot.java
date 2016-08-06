package my.dertraktor.imageresizer.bot;

import my.dertraktor.imageresizer.bot.commands.BotCommand;
import my.dertraktor.imageresizer.bot.commands.InvalidCommandLineException;
import my.dertraktor.imageresizer.bot.commands.MonitoringCommand;
import my.dertraktor.imageresizer.bot.commands.ReschedulerCommand;
import my.dertraktor.imageresizer.bot.commands.ResizerCommand;
import my.dertraktor.imageresizer.bot.commands.SchedulerCommand;
import my.dertraktor.imageresizer.bot.commands.UploaderCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Main class to run the bot.sh
 */
public class Bot {
    private static final Logger log = LogManager.getLogger(Bot.class);

    /**
     * Bot name
     */
    public static final String BOT_NAME = "Images Processor Bot";

    /**
     * Command processors
     */
    private Map<String, Class> commands = new HashMap();

    private ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/context-root.xml");

    public Bot() {
        commands.put("schedule", SchedulerCommand.class);
        commands.put("resize", ResizerCommand.class);
        commands.put("status", MonitoringCommand.class);
        commands.put("upload", UploaderCommand.class);
        commands.put("retry", ReschedulerCommand.class);

    }

    /**
     * main function
     * @param args command like parameters
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new Bot().execute(args);
    }

    /**
     * Find command processor form specified command
     * @param commandName
     * @return command processor
     * @throws InvalidCommandException if commandName is unknown
     */
    private BotCommand getCommand(String commandName) throws InvalidCommandException {
        Class<BotCommand> botCommandClass = commands.get(commandName);
        if (botCommandClass == null) {
            throw new InvalidCommandException(commandName);
        }
        BotCommand command = context.getBean(botCommandClass);
        return command;
    }

    /**
     * Bot execution.
     * Find command processors.
     * Parses parameters.
     * Shows imessages.
     * @param args command line arguments
     * @return return code 0- command was executed successfully, 1- the command is unknown, 2- bad command parameters, 3- error during command execution
     * @throws Exception
     */
    private int execute(String[] args) throws Exception {
        log.trace("start");
        showInfoMessage();
        if (args.length == 0) {
            showUsageMessage();
            return 0;
        }
        String commandName = args[0];

        log.trace(String.format("executing command %s", commandName));

        try {
            BotCommand command = getCommand(commandName);

            command.parseArgs(args);

            command.execute();

            log.trace(String.format("command %s execution finished successfully", commandName));
        } catch (InvalidCommandException e) {
            log.error(e);
            showInvalidCommandMessage(e.getMessage());
            return 1;

        } catch (InvalidCommandLineException e) {
            log.error(String.format("invalid command parameters %s. Usage: %s",  e.getActualCommandLine(), e.getCommandUsageInfo()));
            showBadCommandParametersMessage(e.getCommandUsageInfo());
            return 2;
        } catch (Exception e) {
            log.error(String.format("error executing command %s", commandName), e);
            showErrorMessage(e.getMessage());
            return 3;
        }

        context.destroy();

        return 0;
    }

    /**
     * write error message to console out
     * @param message message
     */
    private void showErrorMessage(String message) {
        System.out.println(String.format("Error: %s", message));
    }

    private void showInvalidCommandMessage(String message) throws Exception {
        System.out.println(message);
        showUsageMessage();
    }

    /**
     * writes invalid command parameters message to console out
     * @param properCommand command usage info
     * @throws Exception
     */
    private void showBadCommandParametersMessage(String properCommand) {
        System.out.println(String.format("Bad parameters.\nUsage: %s", properCommand));
    }

    /**
     * writes bot.sh name to console output
     */
    private void showInfoMessage() {
        System.out.println(BOT_NAME);
    }

    /**
     * writes usage information to console output
     */
    private void showUsageMessage() throws Exception {
        StringBuilder infoMessageBuilder = new StringBuilder(200);
        infoMessageBuilder.append("Usage:\ncommand [arguments]\nAvailable commands:\n");
        for (String commandName : commands.keySet()) {
            BotCommand command = getCommand(commandName);
            infoMessageBuilder.append("\t").append(commandName).append("\t\t");
            infoMessageBuilder.append(command.getCommandDescription()).append("\n");
        }
        System.out.println(infoMessageBuilder.toString());
    }

}
