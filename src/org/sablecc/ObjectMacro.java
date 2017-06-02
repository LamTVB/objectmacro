package org.sablecc;

import org.sablecc.objectmacro.syntax3.node.Start;
import org.sablecc.objectmacro.syntax3.parser.Parser;

import java.io.FileReader;
import java.io.PushbackReader;

public class ObjectMacro{

    public static void main(String args[]){

        try
        {
            // Create a Parser instance.
            Parser p =
                    new Parser(
                            new CustomLexer(
                                    new PushbackReader(
                                            new FileReader(args[0]))));
            // Parse the input.
            Start tree = p.parse();

        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}