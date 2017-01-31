package vsoc;

@SuppressWarnings("serial")
public class VsocInvalidDataException extends RuntimeException {

/**
 * Indicates a programming or configuration problem.
 *
 */
    
    public VsocInvalidDataException() {
        super();
    }

    public VsocInvalidDataException(String message) {
        super(message);
    }

    public VsocInvalidDataException(Throwable cause) {
        super(cause);
    }

    public VsocInvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }

}
