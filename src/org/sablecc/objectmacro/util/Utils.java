package org.sablecc.objectmacro.util;

import org.sablecc.objectmacro.syntax3.node.TVariable;

/**
 * Created by lam on 12/07/17.
 */
public class Utils {

    public static String getVariableName(
            TVariable variable){

        String variableText = variable.getText();
        int length = variableText.length();

        return variableText.substring(1, length - 1);
    }
}
