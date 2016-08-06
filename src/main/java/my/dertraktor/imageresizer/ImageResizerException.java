package my.dertraktor.imageresizer;

/**
 * Image resizing exception
 */
public class ImageResizerException extends Exception {
    public ImageResizerException(String message) {
        super(message);
    }

    public ImageResizerException(String message, Throwable cause) {
        super(message, cause);
    }
}
