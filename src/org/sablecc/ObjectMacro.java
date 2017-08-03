package org.sablecc;

import org.sablecc.objectmacro.exception.CompilerException;
import org.sablecc.objectmacro.exception.InternalException;
import org.sablecc.objectmacro.intermediate.syntax3.node.*;
import org.sablecc.objectmacro.intermediate.syntax3.node.AMacro;
import org.sablecc.objectmacro.intermediate.syntax3.node.AOption;
import org.sablecc.objectmacro.intermediate.syntax3.node.POption;
import org.sablecc.objectmacro.intermediate.syntax3.node.PParam;
import org.sablecc.objectmacro.intermediate.syntax3.node.PType;
import org.sablecc.objectmacro.intermediate.walker.CodeGenerator;
import org.sablecc.objectmacro.structure.GlobalIndex;
import org.sablecc.objectmacro.structure.Macro;
import org.sablecc.objectmacro.structure.Option;
import org.sablecc.objectmacro.structure.Param;
import org.sablecc.objectmacro.syntax3.lexer.LexerException;
import org.sablecc.objectmacro.syntax3.node.*;
import org.sablecc.objectmacro.syntax3.node.AStringType;
import org.sablecc.objectmacro.syntax3.node.Start;
import org.sablecc.objectmacro.syntax3.parser.Parser;
import org.sablecc.objectmacro.syntax3.parser.ParserException;
import org.sablecc.objectmacro.util.Utils;
import org.sablecc.objectmacro.walker.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.LinkedList;
import java.util.List;

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
            GlobalIndex globalIndex = new GlobalIndex();
            tree.apply(new DeclarationCollector(globalIndex));
            tree.apply(new DefinitionCollector(globalIndex));
            tree.apply(new OptionCollector(globalIndex));
            tree.apply(new VarVerifier(globalIndex));

            List<AMacro> macros = new LinkedList<>();
            for(Macro macro : globalIndex.getAllMacros()){
                macros.add(createMacro(macro));

            }

            AIntermediateRepresentation intermediateRepresentation =
                    new AIntermediateRepresentation(macros);
        }
        catch (ParserException | IOException | CompilerException | LexerException e) {
            e.printStackTrace();
        }
    }

    private static AMacro createMacro(Macro macro){

        TString name = new TString(macro.getName().getText());
        List<PParam> params = new LinkedList<>();
        List<Param> macro_contexts = macro.getAllContexts();
        List<Param> macro_params = macro.getAllParams();
        List<PMacroPart> macroParts = new LinkedList<>();
        List<PMacroBodyPart> macroBodyParts = macro.getDeclaration().getMacroBodyParts();

        for(Param context : macro_contexts){
            params.add(createContext(context));
        }

        for(Param param : macro_params){
            params.add(createParam(param));
        }

        StringBuilder macroBuilder = null;

        for(PMacroBodyPart bodyPart : macroBodyParts){
            if(bodyPart instanceof AEolMacroBodyPart){

                if(macroBuilder != null){
                    macroParts.add(
                            new AStringMacroPart(
                                    new TString(macroBuilder.toString())));

                    macroBuilder = null;
                }

                macroParts.add(new AEolMacroPart());

            }else if(bodyPart instanceof AEscapeMacroBodyPart){

            }else if(bodyPart instanceof AInsertMacroBodyPart){

                if(macroBuilder != null){
                    macroParts.add(
                            new AStringMacroPart(
                                    new TString(macroBuilder.toString())));

                    macroBuilder = null;
                }

                AInsertMacroBodyPart insertMacroBodyPart = (AInsertMacroBodyPart) bodyPart;
                AMacroRef macroRef = createMacroRef((AMacroReference)insertMacroBodyPart.getMacroReference());
                AInsertMacroPart insertMacroPart = new AInsertMacroPart(macroRef);
                macroParts.add(insertMacroPart);

            }else if(bodyPart instanceof ATextMacroBodyPart){

                if(macroBuilder == null){
                    macroBuilder = new StringBuilder();

                }

                macroBuilder.append(((ATextMacroBodyPart) bodyPart).getTextPart().getText());

            }else if(bodyPart instanceof AVarMacroBodyPart){

                if(macroBuilder != null){
                    macroParts.add(
                            new AStringMacroPart(
                                    new TString(macroBuilder.toString())));

                    macroBuilder = null;
                }

                AVarMacroBodyPart varBodyPart = (AVarMacroBodyPart) bodyPart;
                String varName = Utils.getVariableName(varBodyPart.getVariable());
                macroParts.add(new AVarMacroPart(new TString(varName)));

            }else{
                throw new InternalException("case not handled");
            }
        }

        if(macroBuilder != null){
            macroParts.add(
                    new AStringMacroPart(
                            new TString(macroBuilder.toString())));
        }

        return new AMacro(name, params, macroParts);
    }

    private static AParamParam createParam(
            Param param){

        List<POption> options = createOptions(param.getAllOptions());
        TString pName = new TString(param.getDeclaration().getName().getText());
        PType type = createParamType(param);

        return new AParamParam(pName, type, options);
    }

    private static PParam createContext(
            Param param){

        List<POption> options = createOptions(param.getAllOptions());
        TString pName = new TString(param.getDeclaration().getName().getText());
        PType type = createParamType(param);

        return new AContextParam(pName, type, options);
    }

    private static List<POption> createOptions(List<Option> options){

        List<POption> options_node = new LinkedList<>();

        for(Option l_option : options){
            TString optionName = new TString(l_option.getDeclaration().getName().getText());
            List<PTextPart> text_parts = createTextParts(l_option.getDeclaration().getParts());
            AStringValue value = new AStringValue(text_parts);
            options_node.add(new AOption(optionName, value));
        }

        return options_node;
    }

    private static PType createParamType(
            Param param){

        PType type = null;
        if(param.getDeclaration().getType() instanceof AStringType){
            type = new org.sablecc.objectmacro.intermediate.syntax3.node.AStringType();

        }else if(param.getDeclaration().getType() instanceof AMacrosType){
            List<AMacroReference> macroReferences = param.getMacroReferences();
            List<AMacroRef> macroRefs = new LinkedList<>();

            for(AMacroReference l_macroRef : macroReferences){

                macroRefs.add(createMacroRef(l_macroRef));
            }

            type = new AMacroRefsType(macroRefs);

        }

        return type;
    }

    private static List<PValue> createValues(
            AMacroReference macroReference){

        List<PValue> values = new LinkedList<>();
        List<PStaticValue> args = macroReference.getValues();

        for(PStaticValue argument : args){
            if(argument instanceof AStringStaticValue){
                AStringStaticValue stringStaticValue = (AStringStaticValue) argument;
                List<PTextPart> text_parts = createTextParts(stringStaticValue.getParts());
                AStringValue value = new AStringValue(text_parts);
                values.add(value);
            }else if(argument instanceof AVarStaticValue){
                AVarStaticValue aVarStaticValue = (AVarStaticValue) argument;
                AVarValue value = new AVarValue(
                        new TString(aVarStaticValue.getIdentifier().getText()));
                values.add(value);
            }
        }

        return values;
    }

    private static AMacroRef createMacroRef(
            AMacroReference macroReference){

        List<PValue> values = createValues(macroReference);
        TString macroRefName = new TString(macroReference.getIdentifier().getText());

        return new AMacroRef(macroRefName, values);
    }

    private static List<PTextPart> createTextParts(
            List<PStringPart> stringParts){

        List<PTextPart> text_parts = new LinkedList<>();
        {
            StringBuilder text_builder = null;
            for(PStringPart stringPart : stringParts){

                if(stringPart instanceof AEscapeStringPart){

                    if(text_builder == null){
                        text_builder = new StringBuilder();
                    }

                    AEscapeStringPart aEscapeStringPart = (AEscapeStringPart) stringPart;
                    //TODO escaped string
                    System.out.println(aEscapeStringPart.getStringEscape().getText());
//                        optionValue.append(aEscapeStringPart)
                }else if(stringPart instanceof ATextStringPart){

                    if(text_builder == null){
                        text_builder = new StringBuilder();
                    }

                    ATextStringPart aTextStringPart = (ATextStringPart) stringPart;
                    text_builder.append(aTextStringPart.getText().getText());

                }else if(stringPart instanceof AVarStringPart){
                    AVarStringPart aVarStringPart = (AVarStringPart) stringPart;

                    if(text_builder != null){
                        text_parts.add(new AStringTextPart(
                                new TString(text_builder.toString())
                        ));

                        text_builder = null;
                    }

                    text_parts.add(new AVarTextPart(
                            new TString(aVarStringPart.getVariable().getText())));
                }else {
                    throw new InternalException("case not handled");
                }
            }

            if(text_builder != null){
                text_parts.add(new AStringTextPart(
                        new TString(text_builder.toString())
                ));

                text_builder = null;
            }
        }

        return text_parts;
    }
}
