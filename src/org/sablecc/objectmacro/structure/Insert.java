package org.sablecc.objectmacro.structure;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lam on 06/06/17.
 */
public class Insert {

    private GlobalIndex globalIndex;

    private Macro referencedMacro;

    private Macro parentMacro;

    private List<StaticValue> values;

    public Insert(
            Macro referencedMacro,
            Macro parentMacro){

        this.referencedMacro = referencedMacro;
        this.parentMacro = parentMacro;
        this.values = new LinkedList<>();
    }

    public Macro getReferencedMacro(){

        return this.referencedMacro;
    }

    public Macro getParentMacro(){

        return this.parentMacro;
    }

    public void addValue(
            List<Param> referencedParams){

        StaticValue newValue = new StaticValue();

        for(Param param : referencedParams){
            newValue.addReferencedParam(param);
        }

        this.values.add(newValue);
    }
}
