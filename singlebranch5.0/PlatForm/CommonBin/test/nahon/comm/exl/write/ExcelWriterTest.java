/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.exl.write;

import nahon.comm.exl.ExcelWriter;
import nahon.comm.exl.DefaultExcelTable;
import nahon.comm.exl.ExcelRead;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jiche
 */
public class ExcelWriterTest {

    public ExcelWriterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of InsertOneRow method, of class ExcelWriter.
     */
    @Test
    public void testInsertOneRow() throws Exception {
        System.out.println("InsertOneRow");
        DefaultExcelTable table = DefaultExcelTable.createTable("test", new String[]{"A", "B"}, new Object[][]{{1, 2}, {4, 5}, {"a", "b"}, {"d", "e"}});

        ExcelWriter instance = ExcelWriter.CreateExcel("D:\\ttt.xls");
        instance.CreateSheet("test", 0);
        instance.WriteTable(table, ExcelWriter.Direction.Vertical);
        instance.Close();

        ExcelRead ReadExcel = ExcelRead.ReadExcel("D:\\ttt.xls");
        DefaultExcelTable[] tables = ReadExcel.ReadExcelTable();
        DefaultExcelTable rt = tables[0];
        for (int i = 0; i < rt.getColumnCount(); i++) {
            System.out.print(rt.getColumnName(i) + " | ");
        }
        System.out.println();
        for (int i = 0; i < rt.getRowCount(); i++) {
            for (int j = 0; j < rt.getColumnCount(); j++) {
                System.out.print(rt.getValueAt(i, j) + " | ");
            }
            System.out.println();
        }
        ReadExcel.Close();
    }

}
