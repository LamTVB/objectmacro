package org.sablecc.objectmacro.exception;

import org.sablecc.objectmacro.syntax3.node.Token;

/**
 * Created by lam on 19/07/17.
 */
public class CompilerException
        extends RuntimeException {

    private String message;

    private Token token;

    public CompilerException(
            String message,
            Token token){

        this.message = message;
        this.token = token;
    }

    @Override
    public String getMessage() {

        if(token != null){
            return this.message.concat(" at line " + token.getLine() + ".");
        }

        return message;
    }
}
