package org.sablecc.objectmacro.walker;

import org.sablecc.objectmacro.structure.*;
import org.sablecc.objectmacro.syntax3.analysis.DepthFirstAdapter;
import org.sablecc.objectmacro.syntax3.node.*;
import org.sablecc.objectmacro.util.Utils;

/**
 * Created by lam on 12/07/17.
 */
public class OptionCollector
        extends DepthFirstAdapter{

    private GlobalIndex globalIndex;

    private Macro currentMacro;

    private Param currentParam;

    private StaticValue optionValue;

    public OptionCollector(
            GlobalIndex globalIndex){

        this.globalIndex = globalIndex;
    }

    @Override
    public void inAMacro(
            AMacro node) {

        this.currentMacro = this.globalIndex.getMacroOrNull(node.getName().getText());
    }

    @Override
    public void outAMacro(
            AMacro node) {

        this.currentMacro = null;
    }

    @Override
    public void inAParam(
            AParam node) {

        this.currentParam = this.currentMacro.getParam(node.getName().getText());
    }

    @Override
    public void outAParam(
            AParam node) {

        this.currentParam = null;
    }

    @Override
    public void inAOption(
            AOption node) {

        this.optionValue = this.currentParam.newOption(node);
    }

    @Override
    public void outAOption(
            AOption node) {

        this.optionValue = null;
    }

    @Override
    public void caseAVarStaticValue(
            AVarStaticValue node) {

        if(this.optionValue == null){
            return;
        }

        Param param = this.currentMacro.getParam(node.getIdentifier().getText());
        this.optionValue.addReferencedParam(param);
    }

    @Override
    public void caseAVarStringPart(
            AVarStringPart node) {

        if(this.optionValue == null){
            return;
        }

        String name = Utils.getVariableName(node.getVariable());
        Param param = this.currentMacro.getParam(name);

        this.optionValue.addReferencedParam(param);
    }
}
