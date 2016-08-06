package my.dertraktor.imageresizer.bot.commands;

import my.dertraktor.imageresizer.bot.BotException;

public class BotCommandException extends BotException {
    public BotCommandException(String message) {
        super(message);
    }

    public BotCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
