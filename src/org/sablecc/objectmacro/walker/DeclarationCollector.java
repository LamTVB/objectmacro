package org.sablecc.objectmacro.walker;

import org.sablecc.objectmacro.exception.InternalException;
import org.sablecc.objectmacro.structure.GlobalIndex;
import org.sablecc.objectmacro.structure.Macro;
import org.sablecc.objectmacro.structure.Param;
import org.sablecc.objectmacro.syntax3.analysis.DepthFirstAdapter;
import org.sablecc.objectmacro.syntax3.node.*;

import java.lang.reflect.Method;
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
            throw new InternalException(
                    "Token {Begin} must be at the beginning of the line");
        }

        Macro newMacro = this.globalIndex.newMacro(node);

        List<PParam> params = node.getParams();
        List<PParam> contexts = node.getContexts();

        for (PParam param_production : params) {
            AParam param_node = (AParam) param_production;
            newMacro.newParam(param_node.getIdentifier());
        }

        for (PParam param_production : contexts) {

            AParam param_node = (AParam) param_production;
            newMacro.newContext(param_node.getIdentifier());
        }
    }

    @Override
    public void caseAIgnoreMacro(
            AIgnoreMacro node) {

        if (node.getIgnoreMacroStart().getPos() > 1) {
            throw new InternalException("'Start Ignoring' must start at the beginning of the line");
        }
        //TODO get macros ignored
    }

}
