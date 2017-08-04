package org.sablecc.objectmacro.walker;

import org.sablecc.objectmacro.exception.CompilerException;
import org.sablecc.objectmacro.structure.GlobalIndex;
import org.sablecc.objectmacro.structure.Macro;
import org.sablecc.objectmacro.structure.Param;
import org.sablecc.objectmacro.syntax3.analysis.DepthFirstAdapter;
import org.sablecc.objectmacro.syntax3.node.*;

import java.util.List;

/**
 * Created by lam on 10/07/17.
 */
public class DefinitionCollector
        extends DepthFirstAdapter {

    private final GlobalIndex globalIndex;

    private Macro currentMacro;

    private Param currentParam;

    public DefinitionCollector(
            GlobalIndex globalIndex) {

        this.globalIndex = globalIndex;
    }

    @Override
    public void inAMacro(
            AMacro node) {

        String macroName = node.getName().getText();
        this.currentMacro = this.globalIndex.getMacroOrNull(macroName);
    }

    @Override
    public void outAMacro(
            AMacro node) {

        this.currentMacro = null;
    }

    @Override
    public void inAParam(
            AParam node) {

        this.currentParam = this.currentMacro
                .getParam(node.getName());
    }

    @Override
    public void outAParam(
            AParam node) {

        this.currentParam = null;
    }

    @Override
    public void caseAMacroReference(
            AMacroReference node) {

        //In case of Insert in a macro
        if(this.currentParam == null){
            return;
        }

        Macro referencedMacro = this.globalIndex.getMacro(node.getIdentifier());
        List<PStaticValue> staticValues = node.getValues();

        if(staticValues.size() > 0 && referencedMacro.getAllContexts().size() == 0){
            throw new CompilerException(
                    "Cannot call a macro without any context", node.getIdentifier());

        }else if(staticValues.size() > 0){
            if(staticValues.size() != referencedMacro.getAllContexts().size()){
                throw new CompilerException(
                        "Incorrect number of arguments", node.getIdentifier());
            }
            else if(referencedMacro.nbStringContexts() != staticValues.size()){
                throw new CompilerException(
                        "Incorrect number of string arguments", node.getIdentifier());
            }
        }

        this.currentParam.addMacroReference(node);
    }

    @Override
    public void caseAStringType(
            AStringType node) {

        this.currentParam.setString();
    }
}
