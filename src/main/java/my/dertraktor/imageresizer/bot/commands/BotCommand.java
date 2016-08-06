package my.dertraktor.imageresizer.bot.commands;

/**
 * Basic class for bot.sh command
 */
public abstract class BotCommand {

    /**
     * Execute the command
     * @throws Exception exception during execution
     */
    public abstract void execute() throws Exception;

    /**
     * Parse command line arguments and initialize command
     * @param args - command line arguments, passed to the bot.sh
     * @throws InvalidCommandLineException raise if commad parameters are invalid
     */
    public abstract void parseArgs(String[] args) throws InvalidCommandLineException;

    /**
     * Returns description which is used in info messages
     * @return description of the command
     */
    public abstract String getCommandDescription();

    /**
     * @return Command parameters description
     */
    public abstract String getUsageInfo();

}
