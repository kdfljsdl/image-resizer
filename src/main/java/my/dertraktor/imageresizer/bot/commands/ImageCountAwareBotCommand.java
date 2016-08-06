package my.dertraktor.imageresizer.bot.commands;

/**
 * Basic class for command which uses image count limit parameter
 */
public abstract class ImageCountAwareBotCommand extends BotCommand {
    private int fileCountToProcess = -1;

    /**
     * @return "-n" parameter value or -1 if "-n" parameter is omitted
     */
    protected int getFileCountToProcess() {
        return fileCountToProcess;
    }

    /**
     * checks whether "-n" parameter has proper count value
     * @param sCount
     * @return
     */
    private boolean isProperCount(String sCount){
        return sCount.matches("[0-9]+");
    }

    /**
     * @see my.dertraktor.imageresizer.bot.commands.BotCommand#parseArgs(String[])
     */
    @Override
    public void parseArgs(String[] args) throws InvalidCommandLineException {
        if (args.length == 3 && "-n".equals(args[1]) && isProperCount(args[2])) {
            fileCountToProcess = Integer.parseInt(args[2]);
        } else if (args.length != 1)
            throw new InvalidCommandLineException(getUsageInfo(), args);

    }

}
