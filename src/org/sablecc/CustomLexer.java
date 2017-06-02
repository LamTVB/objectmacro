package org.sablecc;

import org.sablecc.objectmacro.syntax3.lexer.Lexer;
import org.sablecc.objectmacro.syntax3.lexer.LexerException;
import org.sablecc.objectmacro.syntax3.node.TDquote;

import java.io.IOException;
import java.io.PushbackReader;

/**
 * Created by lam on 02/06/17.
 */
public class CustomLexer
                extends Lexer{

    private State previousState;

    private int textDepth = 0;

    public CustomLexer(PushbackReader in) {
        super(in);
    }

    @Override
    protected void filter() throws
            LexerException, IOException {

        if(this.token instanceof TDquote){

            if(this.textDepth == 0){
                this.previousState = this.state;
                this.state = State.STRING;
                this.textDepth++;

            }
            else{
                this.textDepth--;
                this.state = this.previousState;

            }
        }
    }
}
