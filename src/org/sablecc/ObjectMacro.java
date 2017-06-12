package org.sablecc;

import org.sablecc.objectmacro.structure.GlobalIndex;
import org.sablecc.objectmacro.syntax3.node.Start;
import org.sablecc.objectmacro.syntax3.parser.Parser;
import org.sablecc.objectmacro.walker.MacroCollector;
import org.sablecc.objectmacro.walker.Printer;

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
                                            new FileReader(args[0]), 1024)));
            // Parse the input.
            Start tree = p.parse();
            Printer printer = new Printer();
            printer.print(tree);
//            GlobalIndex globalIndex = new GlobalIndex();
//            MacroCollector walker = new MacroCollector(globalIndex);
//            walker.visit(tree);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}