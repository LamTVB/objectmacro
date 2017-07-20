package org.sablecc.objectmacro.structure;

import org.sablecc.objectmacro.exception.InternalException;
import org.sablecc.objectmacro.syntax3.node.*;

import java.util.*;

/**
 * Created by lam on 05/06/17.
 */
public class Macro {

    private GlobalIndex globalIndex;

    private TIdentifier name;

    private List<Param> allParams = new LinkedList<>();

    private SortedMap<String, Param> namedParams = new TreeMap<>();

    private Map<TIdentifier, Param> namedContext = new HashMap<>();

    private List<Insert> inserts = new LinkedList<>();

    public Macro(
            GlobalIndex globalIndex,
            TIdentifier name){

        this.globalIndex = globalIndex;
        this.name = name;
    }

    public Param newParam(
            TIdentifier name){

        Param newParam = new Param(this.globalIndex, name);

        this.namedParams.put(name.getText(), newParam);
        this.allParams.add(newParam);

        return newParam;
    }

    public Param newContext(
            TIdentifier name){

        Param newContext = new Param(this.globalIndex, name);
        this.namedContext.put(name, newContext);

        return newContext;
    }

    public Insert newInsert(
            Macro referencedMacro){

        Insert newInsert = new Insert(referencedMacro, this);

        this.inserts.add(newInsert);

        return newInsert;
    }

    public TIdentifier getName() {
        return name;
    }

    public Param getParam(
            String name){

        if(!this.namedParams.containsKey(name)){
            throw new InternalException("Param of name " + name + " is undefined in macro '" + this.name.getText()+ "'.");
        }

        throw new CompilerException("Param of name '" + name + "' is undefined in macro '" + this.name.getText()+ "'.", name);
    }

    public void setParamUsed(
            String name){

        if(!this.namedParams.containsKey(name)){
        }

        this.namedParams.get(name).setUsed();
            throw new CompilerException("Param of name '" + name + "' is undefined", name);
    }

    public List<Param> getAllParams(){
        return this.allParams;
    }

    public boolean isUsing(
            Macro macro){

        for(Param parameter : this.getAllParams()){
            if(parameter.getMacroReference(macro.getName().getText()) != null){
                return true;
            }
        }

        return false;
    }
}
