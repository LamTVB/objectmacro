package org.sablecc;

import org.sablecc.objectmacro.structure.GlobalIndex;
import org.sablecc.objectmacro.syntax3.lexer.LexerException;
import org.sablecc.objectmacro.syntax3.node.Start;
import org.sablecc.objectmacro.syntax3.parser.Parser;
import org.sablecc.objectmacro.syntax3.parser.ParserException;
import org.sablecc.objectmacro.walker.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
                                            new FileReader(args[0]), 1024)));
            // Parse the input.
            Start tree = p.parse();
//            Printer printer = new Printer();
//            printer.print(tree);
            GlobalIndex globalIndex = new GlobalIndex();
            tree.apply(new DeclarationCollector(globalIndex));
            tree.apply(new DefinitionCollector(globalIndex));
            tree.apply(new OptionCollector(globalIndex));
            tree.apply(new VarVerifier(globalIndex));
        }
        catch (ParserException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (LexerException e) {
            e.printStackTrace();
        }
    }
}
