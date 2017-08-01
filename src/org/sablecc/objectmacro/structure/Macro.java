package org.sablecc.objectmacro.structure;

import org.sablecc.objectmacro.exception.CompilerException;
import org.sablecc.objectmacro.syntax3.node.*;

import java.util.*;

/**
 * Created by lam on 05/06/17.
 */
public class Macro {

    private GlobalIndex globalIndex;

    private TIdentifier name;

    private List<Param> allParams = new LinkedList<>();

    private Map<String, Param> namedParams = new LinkedHashMap<>();

    private List<Param> allContexts = new LinkedList<>();

    private Map<String, Param> namedContexts = new LinkedHashMap<>();

    private List<Insert> inserts = new LinkedList<>();

    public Macro(
            GlobalIndex globalIndex,
            TIdentifier name){

        this.globalIndex = globalIndex;
        this.name = name;
    }

    public Param newParam(
            AParam param){

        TIdentifier name = param.getName();
        Param newParam = new Param(this.globalIndex, param);

        if(containsKeyInContexts(name) || containsKeyInParams(name)){
            throw new CompilerException(
                    "Parameter '" + name.getText() + "' is already defined in '" + getName().getText() + "'", name);
        }
        this.namedParams.put(name.getText(), newParam);
        this.allParams.add(newParam);

        return newParam;
    }

    public Param newContext(
            AParam param){

        TIdentifier name = param.getName();
        Param newContext = new Param(this.globalIndex, param);

        if(containsKeyInContexts(name) || containsKeyInParams(name)){
            throw new CompilerException(
                    "Parameter '" + name.getText() + "' is already defined in '" + getName().getText() + "'", name);
        }
        this.allContexts.add(newContext);
        this.namedContexts.put(name.getText(), newContext);

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
            TIdentifier name){

        String stringName = name.getText();
        if(containsKeyInParams(name)){
            return this.namedParams.get(stringName);

        }else if(containsKeyInContexts(name)){
            return this.namedContexts.get(stringName);
        }

        throw new CompilerException("Param of name '" + stringName + "' is undefined in macro '" + this.name.getText()+ "'.", name);
    }

    public void setParamUsed(
            TIdentifier name){

        String stringName = name.getText();
        if(containsKeyInParams(name)){
            this.namedParams.get(stringName).setUsed();

        }else if(containsKeyInContexts(name)){
            this.namedContexts.get(stringName).setUsed();

        }else{
            throw new CompilerException("Param of name '" + stringName + "' is undefined", name);
        }
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

    public boolean containsKeyInContexts(
            TIdentifier name){

        return this.namedContexts.containsKey(name.getText());
    }

    public boolean containsKeyInParams(
            TIdentifier name){

        return this.namedParams.containsKey(name.getText());
    }

    public List<Param> getAllContexts(){

        return this.allContexts;
    }
}
