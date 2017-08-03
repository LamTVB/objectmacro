package org.sablecc.objectmacro.structure;

import org.sablecc.objectmacro.exception.CompilerException;
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
            throw new InternalException("Name must not be null");
        }

        return this.macrosMap.get(name);
    }

    public Macro newMacro(
            AMacro node){

        if(node == null){
            throw new InternalException("Macro must not be null.");
        }

        TIdentifier name = node.getName();

        Macro firstMacro = getMacroOrNull(name.getText());
        if(firstMacro != null){
            throw new CompilerException("Macro of name '" + firstMacro.getName().getText() + "' is already defined.", node.getName());
        }

        Macro newMacro = new Macro(this, node);
        this.allMacros.add(newMacro);
        this.macrosMap.put(name.getText(), newMacro);

        return newMacro;
    }

    public Macro getMacro(
            TIdentifier macroName){

        if(macroName == null){
            throw new InternalException("Name must not be null");
        }

        String stringName = macroName.getText();
        Macro macro = this.getMacroOrNull(stringName);
        if(macro == null){
            throw new CompilerException("Macro '" + stringName + "' is undefined", macroName);
        }

        return macro;
    }

    public List<Macro> getAllMacros(){

        return this.allMacros;
    }
}
