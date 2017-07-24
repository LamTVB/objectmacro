package org.sablecc.objectmacro.walker;

import org.sablecc.objectmacro.exception.CompilerException;
import org.sablecc.objectmacro.structure.GlobalIndex;
import org.sablecc.objectmacro.structure.Macro;
import org.sablecc.objectmacro.structure.Param;
import org.sablecc.objectmacro.syntax3.analysis.DepthFirstAdapter;
import org.sablecc.objectmacro.syntax3.node.*;

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
    public void caseAMacrosType(AMacrosType node) {

        for(PMacroReference pMacroReference : node.getMacroReference()){

            if(pMacroReference instanceof ANameMacroReference){
                ANameMacroReference aNameMacroReference = (ANameMacroReference) pMacroReference;
                this.currentParam.addMacroReference(aNameMacroReference.getIdentifier());

            }else if(pMacroReference instanceof AMacroCallMacroReference){
                AMacroCallMacroReference aMacroCallMacroReference = (AMacroCallMacroReference) pMacroReference;

                Macro referencedMacro = this.globalIndex.getMacro(aMacroCallMacroReference.getIdentifier());
                if(referencedMacro.getAllContexts().size() == 0){
                    throw new CompilerException("Cannot call a macro without any context", aMacroCallMacroReference.getIdentifier());
                }

                this.currentParam.addMacroReference(aMacroCallMacroReference.getIdentifier());
            }

        }
    }

    @Override
    public void caseAStringType(
            AStringType node) {

        this.currentParam.setString();
    }
}
