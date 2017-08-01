package org.sablecc.objectmacro.walker;

import org.sablecc.objectmacro.exception.CompilerException;
import org.sablecc.objectmacro.structure.GlobalIndex;
import org.sablecc.objectmacro.structure.Macro;
import org.sablecc.objectmacro.syntax3.analysis.DepthFirstAdapter;
import org.sablecc.objectmacro.syntax3.node.*;

import java.util.*;

/* This file is part of SableCC ( http://sablecc.org ).
 *
 * See the NOTICE file distributed with this work for copyright information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
            newMacro.newParam(param_node);
        }

        for (PParam param_production : contexts) {

            AParam param_node = (AParam) param_production;
            newMacro.newContext(param_node);
        }
    }

    @Override
    public void caseAIgnoreMacro(
            AIgnoreMacro node) {

        if (node.getIgnoreMacroStart().getPos() > 1) {
            throw new CompilerException("'Start Ignoring' must start at the beginning of the line", node.getIgnoreMacroStart());
        }

        if(node.getIgnoreMacroEnd().getPos() > 1){
            throw new CompilerException("'Stop Ignoring' must start at the beginning of the line", node.getIgnoreMacroEnd());
        }
        //TODO get macros ignored
    }

}
