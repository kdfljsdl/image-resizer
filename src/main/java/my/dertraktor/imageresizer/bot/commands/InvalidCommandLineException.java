package my.dertraktor.imageresizer.bot.commands;

import my.dertraktor.imageresizer.bot.BotException;

/**
 * Bad command parameters exception
 */
public class InvalidCommandLineException extends BotException {
    /**
     * Usage information
     */
    private String commandUsageInfo;
    /**
     * Actual parameters passed to a command
     */
    private String actualCommandLine;


    /**
     * Constructor
     * @param commandUsageInfo - command usage information
     * @param args - actual command line arguments
     */
    public InvalidCommandLineException(String commandUsageInfo, String[] args) {
        this.commandUsageInfo = commandUsageInfo;
        StringBuilder actualCommandLineBuilder = new StringBuilder();
        for (String arg : args) actualCommandLineBuilder.append(arg).append(" ");
        actualCommandLine = actualCommandLineBuilder.toString();
    }

    public String getActualCommandLine() {
        return actualCommandLine;
    }

    public String getCommandUsageInfo() {
        return commandUsageInfo;
    }

}
