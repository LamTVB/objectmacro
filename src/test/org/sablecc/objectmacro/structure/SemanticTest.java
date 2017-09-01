package test.org.sablecc.objectmacro.structure;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sablecc.objectmacro.structure.GlobalIndex;
import org.sablecc.objectmacro.structure.Macro;
import org.sablecc.objectmacro.structure.Param;
import org.sablecc.objectmacro.syntax3.node.*;
import test.org.sablecc.objectmacro.NodeFactory;

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
    public void testNewParam(){
        AMacro macro1 = NodeFactory.createSimpleMacroWithoutBody("testNewParam");
        AMacro macro2 = NodeFactory.createSimpleMacroWithoutBody("testNewParam2");
        Macro macro = this.globalIndex.newMacro(macro1);

        macro.newParam(NodeFactory.createMacroParam("macroparam1", macro2));
        Assert.assertTrue("Param added to Macro",
                macro.containsKeyInParams(new TIdentifier("macroparam1")));
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
    public void testArgsMacroReference(){

    }
}
