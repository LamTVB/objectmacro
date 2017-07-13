package org.sablecc.objectmacro.walker;

import org.sablecc.objectmacro.syntax3.analysis.DepthFirstAdapter;
import org.sablecc.objectmacro.syntax3.node.*;

/**
 * Created by lam on 12/06/17.
 */
public class Printer extends DepthFirstAdapter {

    private StringBuilder sBuilder = new StringBuilder();

    @Override
    public void defaultCase(Node node) {
        sBuilder.append(((Token)node).getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    public void print(Node node){
        node.apply(this);
        System.out.println(this.sBuilder.toString());
    }
}
