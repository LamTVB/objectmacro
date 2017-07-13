package org.sablecc.objectmacro.structure;

import org.sablecc.objectmacro.exception.InternalException;
import org.sablecc.objectmacro.syntax3.node.AOption;
import org.sablecc.objectmacro.syntax3.node.TIdentifier;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by lam on 06/06/17.
 */
public class Param {

    private GlobalIndex globalIndex;

    private TIdentifier name;

    private List<Macro> macroReferences = new LinkedList<>();

    private Map<TIdentifier, StaticValue> options;

    private boolean isUsed;

    public Param(
            GlobalIndex globalIndex,
            TIdentifier name) {

        this.globalIndex = globalIndex;
        this.name = name;
        this.options = new LinkedHashMap<>();
    }

    public StaticValue newOption(
            AOption option) {

        String optionName = option.getIdentifier().getText();
        if (this.options.containsKey(option.getIdentifier())) {
            throw new InternalException(
                    "Directive '" + optionName + "' is already defined for this parameter");
        }

        StaticValue newValue = new StaticValue();
        this.options.put(
                option.getIdentifier(), newValue);

        return newValue;
    }

    public StaticValue getOptionValue(
            TIdentifier name){

        if(name == null){
            throw new InternalException("name cannot be null");
        }else if(!this.options.containsKey(name)){
            throw new InternalException("no option of this name");
        }

        return this.options.get(name);
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public TIdentifier getName(){

        return this.name;
    }

    public void addMacroReference(
            TIdentifier macroName){

        String name = macroName.getText();
        Macro macro = this.globalIndex.getMacro(name);

        this.macroReferences.add(macro);
    }
}
