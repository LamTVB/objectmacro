package test.org.sablecc.objectmacro.structure;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sablecc.objectmacro.structure.GlobalIndex;
import org.sablecc.objectmacro.structure.Macro;
import org.sablecc.objectmacro.syntax3.node.AMacro;
import org.sablecc.objectmacro.syntax3.node.TIdentifier;
import test.org.sablecc.objectmacro.NodeFactory;

/**
 * Created by lam on 01/09/17.
 */
public class MacroTest {

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
    public void testNewMacro(){
        AMacro macro1 = NodeFactory.createSimpleMacroWithoutBody("testNewMacro");
        this.globalIndex.newMacro(macro1);

        Assert.assertNotNull("Param added to Macro",
                this.globalIndex.getMacroOrNull("testNewMacro"));
    }
}
