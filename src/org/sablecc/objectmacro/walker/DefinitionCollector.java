package org.sablecc.objectmacro.walker;

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
                .getParam(node.getName().getText());
    }

    @Override
    public void outAParam(
            AParam node) {

        this.currentParam = null;
    }

    @Override
    public void caseAMacroCallMacroReference(
            AMacroCallMacroReference node) {

        //To avoid verifying anything else than a parameter definition
        if (this.currentParam == null) {
            return;
        }

        this.currentParam.addMacroReference(node.getIdentifier());
    }

    @Override
    public void caseANameMacroReference(
            ANameMacroReference node) {

        //To avoid verifying anything else than a parameter definition
        if (this.currentParam == null) {
            return;
        }

        this.currentParam.addMacroReference(node.getIdentifier());
    }
}
