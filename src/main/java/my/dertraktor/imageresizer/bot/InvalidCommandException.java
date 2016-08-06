package my.dertraktor.imageresizer.bot;

public class InvalidCommandException extends BotException {
    /**
     * Unkwnown command exception
     * @param command command
     */
    public InvalidCommandException(String command) {
        super(String.format("Invalid command \"%s\".", command));
    }
}
