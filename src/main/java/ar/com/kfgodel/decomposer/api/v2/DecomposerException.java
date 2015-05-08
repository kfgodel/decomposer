package ar.com.kfgodel.decomposer.api.v2;

/**
 * This type represents an error in the execution of a task with the decomposer processor
 * Created by kfgodel on 07/05/2015.
 */
public class DecomposerException extends RuntimeException {

    public DecomposerException(String message) {
        super(message);
    }

    public DecomposerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecomposerException(Throwable cause) {
        super(cause);
    }
}


