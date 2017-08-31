package org.sablecc.objectmacro.structure;

import org.sablecc.objectmacro.exception.CompilerException;
import org.sablecc.objectmacro.exception.InternalException;
import org.sablecc.objectmacro.syntax3.node.*;

import java.util.*;

/**
 * Created by lam on 05/06/17.
 */
public class Macro {

    private GlobalIndex globalIndex;

    private AMacro declaration;

    private TIdentifier name;

    private List<Param> allParams = new LinkedList<>();

    private Map<String, Param> namedParams = new LinkedHashMap<>();

    private List<Param> allContexts = new LinkedList<>();

    private Map<String, Param> namedContexts = new LinkedHashMap<>();

    private List<Insert> inserts = new LinkedList<>();

    Macro(
            GlobalIndex globalIndex,
            AMacro declaration){

        this.globalIndex = globalIndex;
        this.declaration = declaration;
        this.name = declaration.getName();
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

    public List<Insert> getInserts() { return this.inserts; }

    public boolean isUsing(
            Macro macro){

        return isReferencedInParams(macro) || isReferencedInInserts(macro);
    }

    private boolean isReferencedInParams(
            Macro macro){

        for(Param parameter : getAllParams()){
            if(parameter.getMacroReference(macro.getName().getText()) != null){
                return true;
            }
        }

        return false;
    }

    private boolean isReferencedInInserts(
            Macro macro){

        for(Insert insert : getInserts()){
            if(insert.getReferencedMacro() == macro){
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

        if(name == null){
            throw new InternalException("Name should not be null");
        }

        return this.namedParams.containsKey(name.getText());
    }

    public List<Param> getAllContexts(){

        return this.allContexts;
    }

    public AMacro getDeclaration(){
        return this.declaration;
    }

    public int nbStringContexts(){

        int nbString = 0;

        for(Param context : this.getAllContexts()){
            if(context.getDeclaration().getType() instanceof AStringType){
                nbString++;
            }
        }

        return nbString;


    }
}
