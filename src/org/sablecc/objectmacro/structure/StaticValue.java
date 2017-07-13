package org.sablecc.objectmacro.structure;

import org.sablecc.objectmacro.exception.InternalException;
import org.sablecc.objectmacro.syntax3.node.TIdentifier;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lam on 10/07/17.
 */
public class StaticValue {

    //Use to check if param exist
    public Map<TIdentifier, Param> referencedParams = new LinkedHashMap<>();

    public void addReferencedParam(
            Param referencedParam){

        if(this.referencedParams.containsKey(referencedParam.getName())){
            throw new InternalException("Param of name "+ referencedParam.getName().getText() + " cannot be referenced multiple times");
        }

        this.referencedParams.put(referencedParam.getName(), referencedParam);
    }
}
