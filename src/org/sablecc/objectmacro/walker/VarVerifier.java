package org.sablecc.objectmacro.walker;

import org.sablecc.objectmacro.exception.CompilerException;
import org.sablecc.objectmacro.structure.GlobalIndex;
import org.sablecc.objectmacro.structure.Macro;
import org.sablecc.objectmacro.structure.Param;
import org.sablecc.objectmacro.syntax3.analysis.DepthFirstAdapter;
import org.sablecc.objectmacro.syntax3.node.*;
import org.sablecc.objectmacro.util.Utils;

/**
 * Created by lam on 12/07/17.
 */
public class VarVerifier
        extends DepthFirstAdapter{

    private final GlobalIndex globalIndex;

    private Macro currentMacro;

    private Param currentParam;

    private Macro tempMacro = null;

    public VarVerifier(
            GlobalIndex globalIndex) {

        this.globalIndex = globalIndex;
    }

    private Macro getMacroReference(
            PMacroReference node){

        node.apply(this);
        Macro referencedMacro = this.tempMacro;
        this.tempMacro = null;
        return referencedMacro;
    }

    @Override
    public void inAMacro(
            AMacro node) {

        this.currentMacro = this.globalIndex.getMacro(node.getName());
    }

    @Override
    public void outAMacro(
            AMacro node) {

        this.currentMacro = null;
    }

    @Override
    public void caseAInsertMacroBodyPart(
            AInsertMacroBodyPart node) {

        Macro referencedMacro = getMacroReference(node.getMacroReference());
        this.currentMacro.newInsert(referencedMacro);
    }

    @Override
    public void caseAMacroCallMacroReference(
            AMacroCallMacroReference node) {

        this.tempMacro = this.globalIndex.getMacro(node.getIdentifier());
    }

    @Override
    public void caseANameMacroReference(
            ANameMacroReference node) {

        this.tempMacro = this.globalIndex.getMacro(node.getIdentifier());
    }

    @Override
    public void caseAVarMacroBodyPart(
            AVarMacroBodyPart node) {

        String paramName = Utils.getVariableName(node.getVariable());
        TIdentifier varName = new TIdentifier(paramName);

        this.currentMacro.setParamUsed(varName);
    }

    @Override
    public void inAParam(
            AParam node) {

        this.currentParam = this.currentMacro.getParam(node.getName());
    }

    @Override
    public void outAParam(
            AParam node) {

        this.currentParam = null;
    }

    @Override
    public void caseAParam(
            AParam node) {

        if(node.getType() instanceof AStringType){
            return;
        }

        AMacrosType macros = (AMacrosType) node.getType();

        for(PMacroReference macro_reference_node : macros.getMacroReference()){
            Macro referencedMacro = getMacroReference(macro_reference_node);

            if(referencedMacro == this.currentMacro){
                throw new CompilerException("Cannot self reference macro", node.getName());
            }else if(referencedMacro.isUsing(this.currentMacro)) {
                throw new CompilerException("Cyclic reference of macros", node.getName());
            }
        }
    }
}
