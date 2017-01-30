package net.eulerframework.web.core.exception.api;

@SuppressWarnings("serial")
public class ResourceExistsException extends ApiException {

    public ResourceExistsException() {
        super();
    }

    public ResourceExistsException(String message) {
        super(message);
    }

    public ResourceExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceExistsException(Throwable cause) {
        super(cause);
    }
    
    protected ResourceExistsException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
