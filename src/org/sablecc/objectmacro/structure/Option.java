package org.sablecc.objectmacro.structure;

import org.sablecc.objectmacro.exception.InternalException;
import org.sablecc.objectmacro.syntax3.node.AOption;
import org.sablecc.objectmacro.syntax3.node.TIdentifier;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lam on 10/07/17.
 */
public class Option {

    private AOption option;

    //Use to check if param exist
    private Map<TIdentifier, Param> referencedParams = new LinkedHashMap<>();

    public Option(
            AOption option){

        this.option = option;
    }

    public void addReferencedParam(
            Param referencedParam){

        if(this.referencedParams.containsKey(referencedParam.getName())){
            throw new InternalException("Param of name "+ referencedParam.getName().getText() + " cannot be referenced multiple times");
        }

        this.referencedParams.put(referencedParam.getName(), referencedParam);
    }

    public Map<TIdentifier, Param> getReferencedParams(){

        return this.referencedParams;
    }

    public AOption getDeclaration(){

        return this.option;
    }
}
