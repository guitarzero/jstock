/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.yccheok.jstock.engine;

import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yccheok.jstock.gui.JStockOptions;
import org.yccheok.jstock.gui.JStock;

/**
 *
 * @author yccheok
 */
public class UtilsTest extends TestCase {
    
    private static Logger log = LoggerFactory.getLogger(UtilsTest.class);
    
    public UtilsTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        JStock.instance().initJStockOptions(new JStockOptions());
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of toCompleteUnitedStatesGoogleFormat method, of class Utils.
     */
    public void testToCompleteUnitedStatesGoogleFormat() {
        log.debug("toCompleteUnitedStatesGoogleFormat");
        
        Code code = Code.newInstance("MCD");
        String result = Utils.toCompleteUnitedStatesGoogleFormat(code);
        assertEquals("NYSE:MCD", result);
        
        // Test for cache.
        code = Code.newInstance("MCD");
        result = Utils.toCompleteUnitedStatesGoogleFormat(code);
        assertEquals("NYSE:MCD", result);        
        
        code = Code.newInstance("ENVS");
        result = Utils.toCompleteUnitedStatesGoogleFormat(code);
        // Not LON:ENVS
        assertEquals("OTCMKTS:ENVS", result);        
        
    }
    
    /**
     * Test of toGoogleFormat method, of class Utils.
     */
    public void testToGoogleFormat() {
        log.debug("toGoogleFormat");

        Code code = Code.newInstance("^DJI");
        String expResult = "INDEXDJX:.DJI";
        String result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);
        
        code = Code.newInstance("^GSPC");
        expResult = "INDEXSP:.INX";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("^IXIC");
        expResult = "INDEXNASDAQ:.IXIC";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);
        
        code = Code.newInstance("^BSESN");
        expResult = "INDEXBOM:SENSEX";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("^NSEI");
        expResult = "NSE:NIFTY";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("^NSEBANK");
        expResult = "NSE:BANKNIFTY";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("^BVSP");
        expResult = "INDEXBVMF:IBOV";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("^ATX");
        expResult = "INDEXVIE:ATX";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("^FTSE");
        expResult = "INDEXFTSE:UKX";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("^TWII");
        expResult = "TPE:TAIEX";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("^NZ50");
        expResult = "NZE:NZ50G";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("^OMX");
        expResult = "INDEXNASDAQ:OMXS30";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("^AXJO");
        expResult = "INDEXASX:XJO";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("^AORD");
        expResult = "INDEXASX:XAO";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("^AEX");
        expResult = "INDEXEURO:AEX";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);
        
        code = Code.newInstance("USDMYR=X");
        expResult = "CURRENCY:USDMYR";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);
        
        code = Code.newInstance("RDS-B");
        expResult = "RDS.B";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("a.ss");
        expResult = "SHA:A";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);
        
        code = Code.newInstance("b.SZ");
        expResult = "SHE:B";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("c.sa");
        expResult = "BVMF:C";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("c.vi");
        expResult = "VIE:C";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);
        
        code = Code.newInstance("c.SI");
        expResult = "SGX:C";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("c.tw");
        expResult = "TPE:C";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("c.nz");
        expResult = "NZE:C";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("c.st");
        expResult = "STO:C";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("c.n");
        expResult = "NSE:C";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);
        
        code = Code.newInstance("c.b");
        expResult = "BOM:C";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("c.l");
        expResult = "LON:C";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);

        code = Code.newInstance("tatamotor.ns");
        expResult = "NSE:TATAMOTORS";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);
        
        code = Code.newInstance("8ih.ax");
        expResult = "ASX:8IH";
        result = Utils.toGoogleFormat(code);
        assertEquals(expResult, result);
    }
}
