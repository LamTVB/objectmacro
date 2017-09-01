package test.org.sablecc.objectmacro;

import org.sablecc.objectmacro.intermediate.syntax3.node.AContextParam;
import org.sablecc.objectmacro.syntax3.node.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lam on 30/08/17.
 */
public class NodeFactory {

    public static AMacro createSimpleMacroWithoutBody(String name){

        return new AMacro(
                new TMacroKw(0,0),
                new TIdentifier(name, 0 ,0),
                new LinkedList<>(),
                new LinkedList<>(),
                new TBegin(0,0), new LinkedList<>());
    }

    public static AParam createStringParam(String paramName){

        AParam param = new AParam();
        param.setName(new TIdentifier(paramName));
        param.setType(new AStringType());

        return param;
    }

    public static AParam createMacroParam(String paramName, AMacro referenced){

        List<PMacroReference> macroReferences = new LinkedList<>();
        macroReferences.add(
                new AMacroReference(
                        new TIdentifier(referenced.getName().getText()), new LinkedList<>()));

        AParam param = new AParam();
        param.setName(new TIdentifier(paramName));
        param.setType(new AMacrosType(macroReferences));

        return param;
    }

    public static AMacroReference createMacroReferenceNoValues(
            AMacro referenced){

        return new AMacroReference(
                new TIdentifier(referenced.getName().getText()),
                new LinkedList<>());
    }
}
