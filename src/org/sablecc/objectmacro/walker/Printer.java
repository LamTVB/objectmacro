package org.sablecc.objectmacro.walker;

import org.sablecc.objectmacro.syntax3.analysis.DepthFirstAdapter;
import org.sablecc.objectmacro.syntax3.node.*;

/**
 * Created by lam on 12/06/17.
 */
public class Printer extends DepthFirstAdapter {

    private StringBuilder sBuilder = new StringBuilder();

    @Override
    public void caseTAssign(TAssign node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().toGenericString() + ')');
    }

    @Override
    public void caseTBegin(TBegin node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTBlank(TBlank node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTColon(TColon node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTComma(TComma node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTContextKw(TContextKw node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTDquote(TDquote node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTEnd(TEnd node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTEol(TEol node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTEscape(TEscape node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTIdentifier(TIdentifier node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTIgnoreMacroEnd(TIgnoreMacroEnd node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTIgnoreMacroStart(TIgnoreMacroStart node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTInsertCommand(TInsertCommand node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTInvalidIdentifier(TInvalidIdentifier node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTLongMacroComment(TLongMacroComment node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTLPar(TLPar node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTMacroKw(TMacroKw node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTParamsKw(TParamsKw node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTRBrace(TRBrace node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTRPar(TRPar node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTSemiColon(TSemiColon node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTShortMacroComment(TShortMacroComment node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTStringEscape(TStringEscape node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTStringKw(TStringKw node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTStringText(TStringText node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTTextChar(TTextChar node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTUnknownCommand(TUnknownCommand node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTVariable(TVariable node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    @Override
    public void caseTLBrace(TLBrace node) {
        sBuilder.append(node.getText());
        sBuilder.append('(');
        sBuilder.append(node.getClass().getSimpleName() + ')');
    }

    public void print(Node node){
        node.apply(this);
        System.out.println(this.sBuilder.toString());
    }
}
