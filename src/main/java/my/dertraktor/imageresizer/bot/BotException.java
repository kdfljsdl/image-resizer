package my.dertraktor.imageresizer.bot;

/**
 * Basic class for bot.sh commad exceptions
 */
public class BotException extends Exception {
    public BotException() {
    }

    public BotException(String message) {
        super(message);
    }

    public BotException(String message, Throwable cause) {
        super(message, cause);
    }
}
