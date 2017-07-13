package org.sablecc.objectmacro.structure;

import org.sablecc.objectmacro.exception.InternalException;
import org.sablecc.objectmacro.syntax3.node.AMacro;
import org.sablecc.objectmacro.syntax3.node.TIdentifier;

import java.util.*;

/**
 * Created by lam on 07/06/17.
 */
public class GlobalIndex {

    private List<Macro> allMacros = new LinkedList<>();

    private SortedMap<String, Macro> macrosMap = new TreeMap<>();

    public Macro getMacroOrNull(
            String name){

        if(name == null){
            throw new InternalException("name cannot be null");
        }

        return this.macrosMap.get(name);
    }

    public Macro newMacro(
            AMacro node){

        if(node == null){
            throw new InternalException("macro node cannot be null ");
        }

        TIdentifier name = node.getName();

        Macro firstMacro = getMacroOrNull(name.getText());
        if(firstMacro != null){
            throw new InternalException("Macro of name " + firstMacro.getName().getText() + " is already created");
        }

        Macro newMacro = new Macro(this, node.getName());
        this.allMacros.add(newMacro);
        this.macrosMap.put(name.getText(), newMacro);

        return newMacro;
    }

    public Macro getMacro(
            String macroName){

        if(macroName == null){
            throw new InternalException("Name must not be null");
        }

        Macro macro = this.getMacroOrNull(macroName);
        if(macro == null){
            throw new InternalException("Macro '" + macroName + "' is undefined");
        }

        return macro;
    }
}
