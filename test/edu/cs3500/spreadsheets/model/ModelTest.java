package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.SBoolean;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SString;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.Assert.assertEquals;

/**
 * the test class for the model and its related functions.
 */
public class ModelTest {

  FileReader fr;
  FileReader fr2;

  {
    try {
      fr = new FileReader("../resources/testfile.txt");
      fr2 = new FileReader("../resources/errors.txt");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  BasicWorksheet bwk = new BasicWorksheet();
  BasicWorksheet bwk2 = new BasicWorksheet();
  WorksheetReader.WorksheetBuilder<IWorksheet> worksheet = new BasicWorksheetBuilder(bwk);
  WorksheetReader.WorksheetBuilder<IWorksheet> worksheet2 = new BasicWorksheetBuilder(bwk2);
  IWorksheet wk = WorksheetReader.read(worksheet, fr);
  IWorksheet wk2 = WorksheetReader.read(worksheet2, fr2);


  // tests evaluating a single number (cell with just a number in it)
  @Test
  public void applyNumberTest() {
    assertEquals(wk.evaluate(new Coord("A1")), new SNumber(3));
  }

  // tests the sum function
  @Test
  public void applySumTest() {
    assertEquals(wk.evaluate(new Coord("C2")), new SNumber(7));
  }

  // tests the sum function given a loop
  @Test(expected = IllegalArgumentException.class)
  public void applySumTest_Error() {
    wk2.evaluate(new Coord("B2"));
  }

  // tests the product function
  @Test
  public void applyProductTest() {
    assertEquals(wk.evaluate(new Coord("A2")), new SNumber(36));
    assertEquals(wk.evaluate(new Coord("B2")), new SNumber(64));
  }

  // tests the square root funtion
  @Test
  public void applySqrtTest() {
    assertEquals(wk.evaluate(new Coord("A4")), new SNumber(10));
    assertEquals(wk.evaluate(new Coord("A3")), new SNumber(10));
  }

  // tests the square root function given more than one cell to evaluate
  @Test(expected = IllegalArgumentException.class)
  public void applySqrtTest_Error() {
    wk2.evaluate(new Coord("A5"));
  }

  // tests the less than function
  @Test
  public void applyLessThanTest() {
    assertEquals(wk.evaluate(new Coord("B3")), new SBoolean(false));
    assertEquals(wk.evaluate(new Coord("C3")), new SBoolean(true));
  }

  // tests the less than function given the same cell twice
  @Test
  public void applyLessThanTest_SameCell() {
    assertEquals(wk.evaluate(new Coord("C4")), new SBoolean(false));
  }

  // tests the less than function given not 2 cells
  @Test(expected = IllegalArgumentException.class)
  public void applyLessThanTest_Error() {
    wk.evaluate(new Coord("C5"));
    wk.evaluate(new Coord("C6"));
  }

  // tests all cases of the string append function
  @Test
  public void applyStringAppendTest() {
    assertEquals(wk.evaluate(new Coord("D2")), new SString("3.04.0"));
    assertEquals(wk.evaluate(new Coord("D3")), new SString("3.03.04.0"));
    assertEquals(wk.evaluate(new Coord("D4")), new SString("9.03.04.0"));
  }

  // tests string append if one of the cells is appended in the other
  @Test
  public void applyStringAppendTest_CellUsedTwice() {
    assertEquals(wk.evaluate(new Coord("D5")), new SString("3.04.03.03.04.0"));
  }

  // tests string append if one argument is itself
  @Test(expected = IllegalArgumentException.class)
  public void applyStringAppendTest_UsingItself() {
    assertEquals(wk.evaluate(new Coord("D6")), new SString("3.03.04.0"));
  }

  // tests the addCell function on the model
  @Test
  public void addCellTest() {
    assertEquals(wk.evaluate(new Coord("E1")), new SSymbol(""));
    wk.addCell(new Coord("E1"), new Cell(new SNumber(10)));
    assertEquals(wk.evaluate(new Coord("E1")), new SNumber(10));
  }

  // tests the getCells function
  @Test
  public void getCellsTest() {
    assertEquals(wk.getCells().toString(), "{A2=(PRODUCT (SUB C1 A1) (SUB C1 A1)), "
        + "B3=(< A3 10.0), C4=(< A1 A1), D5=(STRING_APPEND D2 D3), A1=3.0, B2=(PRODUCT "
        + "(SUB D1 B1) (SUB D1 B1)), C3=(< A1 C1), D4=(STRING_APPEND C1 D2), B1=4.0, "
        + "C2=(SUM A1 B1), D3=(STRING_APPEND A1 D2), C1=9.0, D2=(STRING_APPEND A1 B1), "
        + "D1=12.0, C6=(< A1 B1 C1), A4=(SQRT (SUM (PRODUCT (SUB C1 A1) (SUB C1 A1)) (PRODUCT "
        + "(SUB D1 B1) (SUB D1 B1)))), A3=(SQRT (SUM A2:B2)), C5=(< D1), D6=(STRING_APPEND D1 "
        + "D6)}");
  }

}