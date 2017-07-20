package org.sablecc.objectmacro.walker;

import org.sablecc.objectmacro.exception.CompilerException;
import org.sablecc.objectmacro.structure.GlobalIndex;
import org.sablecc.objectmacro.structure.Macro;
import org.sablecc.objectmacro.syntax3.analysis.DepthFirstAdapter;
import org.sablecc.objectmacro.syntax3.node.*;

import java.util.*;

/**
 * Created by lam on 05/06/17.
 */
public class DeclarationCollector
        extends DepthFirstAdapter {

    private final GlobalIndex globalIndex;

    private Macro currentMacro;

    public void visit(Node node) {

        node.apply(this);
    }

    public DeclarationCollector(
            GlobalIndex globalIndex) {

        this.globalIndex = globalIndex;
    }

    @Override
    public void caseAMacro(
            AMacro node) {

        if (node.getBegin().getPos() != 1) {
            throw new CompilerException(
                    "Token {Begin} must be at the beginning of the line", node.getBegin());
        }

        Macro newMacro = this.globalIndex.newMacro(node);

        List<PParam> params = node.getParams();
        List<PParam> contexts = node.getContexts();

        for (PParam param_production : params) {

            AParam param_node = (AParam) param_production;
            newMacro.newParam(param_node.getName());
        }

        for (PParam param_production : contexts) {

            AParam param_node = (AParam) param_production;
            newMacro.newContext(param_node.getName());
        }
    }

    @Override
    public void caseAIgnoreMacro(
            AIgnoreMacro node) {

        if (node.getIgnoreMacroStart().getPos() > 1) {
            throw new CompilerException("'Start Ignoring' must start at the beginning of the line", node.getIgnoreMacroStart());
        }
        //TODO get macros ignored
    }

}
