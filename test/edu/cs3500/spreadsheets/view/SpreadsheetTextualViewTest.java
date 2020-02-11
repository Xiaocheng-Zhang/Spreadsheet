package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.controller.ISpreadsheetController;
import edu.cs3500.spreadsheets.controller.SpreadsheetController;
import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.BasicWorksheetBuilder;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IWorksheet;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * the test class for the textual view.
 */
public class SpreadsheetTextualViewTest {

  FileReader fr;
  FileReader fr2;

  {
    try {
      fr = new FileReader("resources/testfile.txt");
      fr2 = new FileReader("resources/errors.txt");
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
  ISpreadsheetController c1 = new SpreadsheetController(wk);
  ISpreadsheetController c2 = new SpreadsheetController(wk2);
  StringBuilder out = new StringBuilder("");
  SpreadsheetTextualView view = new SpreadsheetTextualView(c1, out);

  @Test
  public void renderTest() throws IOException {
    view.render();
    assertEquals(out.toString(), "A2 =(PRODUCT (SUB C1 A1) (SUB C1 A1))\n"
        + "B3 =(< A3 10.0)\n"
        + "C4 =(< A1 A1)\n"
        + "D5 =(STRING_APPEND D2 D3)\n"
        + "A1 3.0\n"
        + "B2 =(PRODUCT (SUB D1 B1) (SUB D1 B1))\n"
        + "C3 =(< A1 C1)\n"
        + "D4 =(STRING_APPEND C1 D2)\n"
        + "B1 4.0\n"
        + "C2 =(SUM A1 B1)\n"
        + "D3 =(STRING_APPEND A1 D2)\n"
        + "C1 9.0\n"
        + "D2 =(STRING_APPEND A1 B1)\n"
        + "D1 12.0\n"
        + "C6 =(< A1 B1 C1)\n"
        + "A4 =(SQRT (SUM (PRODUCT (SUB C1 A1) (SUB C1 A1)) (PRODUCT (SUB D1 B1) (SUB D1 B1))))\n"
        + "A3 =(SQRT (SUM A2:B2))\n"
        + "C5 =(< D1)\n"
        + "D6 =(STRING_APPEND D1 D6)\n");
  }

  @Test
  public void roundTripTest() throws IOException {
    PrintWriter pw = new PrintWriter("roundTrip.txt");
    SpreadsheetTextualView printView = new SpreadsheetTextualView(c1, pw);

    printView.render();
    pw.close();
    FileReader fr = null;
    try {
      fr = new FileReader("roundTrip.txt");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    IWorksheet reread = WorksheetReader.read(new BasicWorksheetBuilder(new BasicWorksheet()), fr);
    Map<Coord, Cell> rereadCells = reread.getCells();
    Map<Coord, Cell> originalCells = wk.getCells();
    assertEquals(rereadCells.size(), originalCells.size());
    for (Entry e : rereadCells.entrySet()) {
      assertEquals(originalCells.get(e.getKey()), e.getValue());
    }
  }

  @Test
  public void roundTripTest__Errors() throws IOException {
    PrintWriter pw = new PrintWriter("roundTrip2.txt");
    SpreadsheetTextualView printView = new SpreadsheetTextualView(c2, pw);

    printView.render();
    pw.close();
    FileReader fr = null;
    try {
      fr = new FileReader("roundTrip2.txt");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    IWorksheet reread = WorksheetReader.read(new BasicWorksheetBuilder(new BasicWorksheet()), fr);
    Map<Coord, Cell> rereadCells = reread.getCells();
    Map<Coord, Cell> originalCells = wk2.getCells();
    assertEquals(rereadCells.size(), originalCells.size());
    for (Entry e : rereadCells.entrySet()) {
      assertEquals(originalCells.get(e.getKey()), e.getValue());
    }
  }
}