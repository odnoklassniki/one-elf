package one.pe;

import java.io.IOException;

public class PeException extends IOException {

    public PeException() {
    }

    public PeException(String message) {
        super(message);
    }

    public PeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PeException(Throwable cause) {
        super(cause);
    }
}
