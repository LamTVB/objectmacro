package org.sablecc.objectmacro.structure;

import org.sablecc.objectmacro.exception.CompilerException;
import org.sablecc.objectmacro.exception.InternalException;
import org.sablecc.objectmacro.syntax3.node.AOption;
import org.sablecc.objectmacro.syntax3.node.TIdentifier;

import java.util.*;

/**
 * Created by lam on 06/06/17.
 */
public class Param {

    private GlobalIndex globalIndex;

    private TIdentifier name;

    private List<Macro> macroReferences = new LinkedList<>();

    private Map<String, Macro> macroReferencesName = new HashMap<>();

    private Map<String, StaticValue> options = new HashMap<>();

    private boolean isUsed;

    public Param(
            GlobalIndex globalIndex,
            TIdentifier name) {

        this.globalIndex = globalIndex;
        this.name = name;
    }

    public StaticValue newOption(
            AOption option) {

        String optionName = option.getIdentifier().getText();
        if (this.options.containsKey(optionName)) {
            throw new CompilerException(
                    "Directive '" + optionName + "' is already defined for this parameter", option.getIdentifier());
        }

        StaticValue newValue = new StaticValue();
        this.options.put(
                optionName, newValue);

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

    public void setUsed() {
        isUsed = true;
    }

    public TIdentifier getName(){

        return this.name;
    }

    public void addMacroReference(
            TIdentifier macroName){

        String name = macroName.getText();
        if(this.macroReferencesName.containsKey(name)){
            throw new CompilerException(
                    "This parameter already references macro of name '" + macroName + "'", macroName);
        }

        Macro macro = this.globalIndex.getMacro(macroName);

        this.macroReferences.add(macro);
        this.macroReferencesName.put(name, macro);
    }

    public Macro getMacroReference(
            String macroName){

        return this.macroReferencesName.get(macroName);
    }
}
