package org.sablecc.objectmacro.exception;

/**
 * Created by lam on 10/07/17.
 */
public class InternalException
        extends RuntimeException {

    public InternalException(
            String message){

        super(message);
    }

    public InternalException(
            String message,
            Throwable cause){

        super(message, cause);

        if (message == null) {
            throw new InternalException("message may not be null");
        }

        if (cause == null) {
            throw new InternalException("cause may not be null");
        }
    }
}
