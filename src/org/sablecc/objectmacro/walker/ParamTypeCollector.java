package org.sablecc.objectmacro.walker;

import org.sablecc.objectmacro.structure.*;
import org.sablecc.objectmacro.syntax3.analysis.DepthFirstAdapter;
import org.sablecc.objectmacro.syntax3.node.*;

/**
 * Created by lam on 07/06/17.
 */
public class ParamValueCollector
        extends DepthFirstAdapter{

    private final GlobalIndex globalIndex;

    private Macro currentMacro;

    private Param currentParam;

    private Value tempValue;

    public ParamValueCollector(
            GlobalIndex globalIndex){

        this.globalIndex = globalIndex;
    }

    private Value getParamValue(
            Node node){


    }

    @Override
    public void inAMacro(
            AMacro node) {

        AMacroDefinition macroDefinition = (AMacroDefinition)node.getMacroDefinition();
        this.currentMacro = this.globalIndex.getMacro(macroDefinition.getIdentifier());
    }

    @Override
    public void outAMacro(
            AMacro node) {

        this.currentMacro = null;
    }

    @Override
    public void inAParam(
            AParam node) {

        this.currentParam = this.currentMacro.getParam(node.getIdentifier());
    }

    @Override
    public void outAParam(
            AParam node) {

        this.currentParam = null;
    }

    @Override
    public void caseAParam(
            AParam node) {

        node.getType().apply(this);
    }

    @Override
    public void caseAStringType(
            AStringType node) {

//        this.tempValue = new StringValue(node.get)
    }
}
