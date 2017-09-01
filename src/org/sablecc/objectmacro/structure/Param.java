package org.sablecc.objectmacro.structure;

import org.sablecc.objectmacro.exception.CompilerException;
import org.sablecc.objectmacro.exception.InternalException;
import org.sablecc.objectmacro.syntax3.node.*;

import java.util.*;

/**
 * Created by lam on 06/06/17.
 */
public class Param {

    private final GlobalIndex globalIndex;

    private final AParam declaration;

    private TIdentifier name;

    private List<AMacroReference> macroReferences = new LinkedList<>();

    private Map<String, AMacroReference> macroReferencesName = new HashMap<>();

    private Map<String, Option> options = new HashMap<>();

    private List<Option> allOptions = new LinkedList<>();

    private boolean isUsed;

    private boolean isString;

    public Param(
            GlobalIndex globalIndex,
            AParam declaration) {

        this.globalIndex = globalIndex;
        this.name = declaration.getName();
        this.declaration = declaration;
    }

    public Option newOption(
            AOption option) {

        String optionName = option.getName().getText();
        if (this.options.containsKey(optionName)) {
            throw new CompilerException(
                    "Directive '" + optionName + "' is already defined for this parameter", option.getName());
        }

        Option newOption = new Option(option);
        this.options.put(
                optionName, newOption);
        this.allOptions.add(newOption);

        return newOption;
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
            AMacroReference macroRef){

        if(macroRef == null){
            throw new InternalException("Macro reference cannot be null");
        }

        String name = macroRef.getIdentifier().getText();
        if(this.macroReferencesName.containsKey(name)){
            throw new CompilerException(
                    "This parameter already references macro of name '" + name + "'", macroRef.getIdentifier());
        }else if(this.isString){
            throw new CompilerException(
                    "Cannot reference a macro with a string", macroRef.getIdentifier());
        }

        this.macroReferences.add(macroRef);
        this.macroReferencesName.put(name, macroRef);

    }

    public PMacroReference getMacroReference(
            String macroName){

        return this.macroReferencesName.get(macroName);
    }

    public List<AMacroReference> getMacroReferences(){

        return this.macroReferences;
    }

    public boolean isString(){

        return this.isString;
    }

    public void setString(){

        this.isString = true;
    }

    public List<Option> getAllOptions(){

        return this.allOptions;
    }

    public AParam getDeclaration(){

        return this.declaration;
    }
}
