package test.org.sablecc.objectmacro.structure;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sablecc.objectmacro.structure.GlobalIndex;
import org.sablecc.objectmacro.structure.Macro;
import org.sablecc.objectmacro.structure.Param;
import org.sablecc.objectmacro.syntax3.node.*;
import test.org.sablecc.objectmacro.NodeFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lam on 30/08/17.
 */
public class SemanticTest {

    private GlobalIndex globalIndex;

    @Before
    public void initGlobalIndex(){

        this.globalIndex = new GlobalIndex();
    }

    @Test
    public void testNewSameMacro(){
        AMacro aMacro1 = NodeFactory.createSimpleMacroWithoutBody("name");

        try{
            this.globalIndex.newMacro(aMacro1);
            this.globalIndex.newMacro(aMacro1);
            Assert.fail("It should fail here cause two same macro is added");
        }catch(Exception e){
            Assert.assertTrue(e.getMessage(), true);
        }
    }

    @Test
    public void testParamCyclicReference(){
        AMacro aMacro1 = NodeFactory.createSimpleMacroWithoutBody("testCyclic1");
        AMacro aMacro2 = NodeFactory.createSimpleMacroWithoutBody("testCyclic2");

        AParam aParam1 = NodeFactory.createMacroParam("macro2", aMacro2);
        AParam aParam2 = NodeFactory.createMacroParam("macro1", aMacro1);

        Macro macro1 = this.globalIndex.newMacro(aMacro1);
        Macro macro2 = this.globalIndex.newMacro(aMacro2);

        Param param1 = macro1.newParam(aParam1);
        Param param2 = macro2.newParam(aParam2);

        param1.addMacroReference(NodeFactory.createMacroReferenceNoValues(aMacro2));
        param2.addMacroReference(NodeFactory.createMacroReferenceNoValues(aMacro1));

        try{
            this.globalIndex.checkReferencedMacro(macro1, macro2, aParam1.getName());
            Assert.fail("There should be a cyclic reference");
        }catch (Exception e){
            Assert.assertTrue(e.getMessage(), true);
        }
    }

    @Test
    public void testNbArgsMacroReference(){
        AMacro aMacro1 = NodeFactory.createSimpleMacroWithoutBody("testArgsMacroRef1");
        AMacro aMacro2 = NodeFactory.createSimpleMacroWithoutBody("testArgsMacroRef2");

        AParam aParam1 = NodeFactory.createMacroParam("macro2", aMacro2);
        AParam aParam2 = NodeFactory.createStringParam("string1");
        AParam aParam3 = NodeFactory.createStringParam("string2");
        AParam aParam4 = NodeFactory.createStringParam("string3");

        Macro macro1 = this.globalIndex.newMacro(aMacro1);
        Macro macro2 = this.globalIndex.newMacro(aMacro2);

        Param param1 = macro1.newParam(aParam1);
        macro2.newContext(aParam2);
        macro2.newContext(aParam3);
        macro2.newContext(aParam4);

        AMacroReference macroRef1 = NodeFactory.createMacroReferenceNoValues(aMacro2);

        List<PStaticValue> values1 = new LinkedList<>();
        values1.add(new AStringStaticValue(new LinkedList<>()));
        values1.add(new AStringStaticValue(new LinkedList<>()));

        macroRef1.setValues(values1);

        param1.addMacroReference(macroRef1);

        try{
            this.globalIndex.checkArgsMacroReference(macroRef1);
            Assert.fail("It should fail because there are not enough args");
        }catch (Exception e){
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testContainContextsArgsMacroReference(){
        AMacro aMacro1 = NodeFactory.createSimpleMacroWithoutBody("testArgsMacroRef1");
        AMacro aMacro2 = NodeFactory.createSimpleMacroWithoutBody("testArgsMacroRef2");

        AParam aParam1 = NodeFactory.createMacroParam("macro2", aMacro2);

        Macro macro1 = this.globalIndex.newMacro(aMacro1);
        this.globalIndex.newMacro(aMacro2);

        Param param1 = macro1.newParam(aParam1);

        AMacroReference macroRef1 = NodeFactory.createMacroReferenceNoValues(aMacro2);

        List<PStaticValue> values1 = new LinkedList<>();
        values1.add(new AStringStaticValue(new LinkedList<>()));
        values1.add(new AStringStaticValue(new LinkedList<>()));

        macroRef1.setValues(values1);

        param1.addMacroReference(macroRef1);

        try{
            this.globalIndex.checkArgsMacroReference(macroRef1);
            Assert.fail("It should fail because there is no context defined");
        }catch (Exception e){
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testStringArgsMacroReference(){
        AMacro aMacro1 = NodeFactory.createSimpleMacroWithoutBody("testArgsMacroRef1");
        AMacro aMacro2 = NodeFactory.createSimpleMacroWithoutBody("testArgsMacroRef2");
        AMacro aMacro3 = NodeFactory.createSimpleMacroWithoutBody("testArgsMacroRef3");

        AParam aParam1 = NodeFactory.createMacroParam("macro2", aMacro2);
        AParam aParam2 = NodeFactory.createMacroParam("macro3", aMacro3);
        AParam aParam3 = NodeFactory.createStringParam("string2");
        AParam aParam4 = NodeFactory.createStringParam("string3");

        Macro macro1 = this.globalIndex.newMacro(aMacro1);
        Macro macro2 = this.globalIndex.newMacro(aMacro2);

        Param param1 = macro1.newParam(aParam1);
        macro2.newContext(aParam2);
        macro2.newContext(aParam3);
        macro2.newContext(aParam4);

        AMacroReference macroRef1 = NodeFactory.createMacroReferenceNoValues(aMacro2);

        List<PStaticValue> values1 = new LinkedList<>();
        values1.add(new AStringStaticValue(new LinkedList<>()));
        values1.add(new AStringStaticValue(new LinkedList<>()));
        values1.add(new AStringStaticValue(new LinkedList<>()));

        macroRef1.setValues(values1);

        param1.addMacroReference(macroRef1);

        try{
            this.globalIndex.checkArgsMacroReference(macroRef1);
            Assert.fail("It should fail because there are not enough args");
        }catch (Exception e){
            Assert.assertTrue(true);
        }
    }
}
