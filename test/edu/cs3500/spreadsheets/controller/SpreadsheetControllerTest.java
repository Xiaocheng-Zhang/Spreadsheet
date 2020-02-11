package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.BasicWorksheetBuilder;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IWorksheet;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * A test class for the controller class and its interactions with the model.
 */
public class SpreadsheetControllerTest {

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
  ISpreadsheetController c = new SpreadsheetController(wk);
  ISpreadsheetController c2 = new SpreadsheetController(wk2);

  // Test getCells function. Does not return the original object from the model
  @Test
  public void testGetCells() {
    Map<Coord, Cell> cells = c.getCells();
    Map<Coord, Cell> modelCells = wk.getCells();
    assertFalse(cells == modelCells);
    assertEquals(cells.size(), modelCells.size());
    for (Entry<Coord, Cell> e : cells.entrySet()) {
      assertEquals(modelCells.get(e.getKey()), e.getValue());
    }
  }

  // Test getEvaluatedCells function, returns all evaluated cells and #VALUE for errored cells
  @Test
  public void testGetEvaluatedCells() {
    Map<Coord, String> cells = c.getEvaluatedCells();
    Map<Coord, Cell> modelCells = wk.getCells();
    assertEquals(cells.size(), modelCells.size());
    for (Entry<Coord, String> e : cells.entrySet()) {
      String s = "";
      try {
        s = wk.evaluate(e.getKey()).toString();
      } catch (IllegalArgumentException f) {
        s = "#VALUE";
      }
      assertEquals(s, e.getValue());
    }
  }

  // Test editCell function, cell is updated, and dependent cells are updated as well
  @Test
  public void testEditCell() {
    Map<Coord, String> cells = c.getEvaluatedCells();
    Coord coord = new Coord(4, 1);
    Coord dependentCoord = new Coord(2, 2);
    String original = cells.get(coord);
    String dependentOriginal = cells.get(dependentCoord);
    assertEquals(original, "12.0");
    assertEquals(dependentOriginal, "64.0");
    c.editCell(coord, "24");
    cells = c.getEvaluatedCells();
    assertNotEquals(original, cells.get(coord));
    assertNotEquals(dependentOriginal, cells.get(dependentCoord));
    assertEquals(cells.get(coord), "24.0");
    assertEquals(cells.get(dependentCoord), "400.0");
  }

  // Test editCell function, inserting data into a formerly blank cell
  @Test
  public void testEditCell__InsertNewCell() {
    Map<Coord, String> cells = c.getEvaluatedCells();
    Coord coord = new Coord(5, 1);
    String original = cells.get(coord);
    assertEquals(original, null);
    c.editCell(coord, "50");
    cells = c.getEvaluatedCells();
    assertNotEquals(original, cells.get(coord));
    assertEquals(cells.get(coord), "50.0");
  }

  // Test clearCell, cell data is removed, and dependent cells are updated
  @Test
  public void testClearCell() {
    Map<Coord, String> cells = c.getEvaluatedCells();
    Coord coord = new Coord(4, 1);
    Coord dependentCoord = new Coord(2, 2);
    String original = cells.get(coord);
    String dependentOriginal = cells.get(dependentCoord);
    assertEquals(original, "12.0");
    c.clearCell(coord);
    cells = c.getEvaluatedCells();
    assertNotEquals(original, cells.get(coord));
    assertEquals(cells.get(coord), null);
  }

  // Test clearCell function when cell is already empty, no change
  @Test
  public void testClearCell__NoCell() {
    Map<Coord, String> cells = c.getEvaluatedCells();
    Coord coord = new Coord(5, 1);
    String original = cells.get(coord);
    assertEquals(original, null);
    c.clearCell(coord);
    cells = c.getEvaluatedCells();
    assertEquals(original, cells.get(coord));
  }

  // Test setModel function
  @Test
  public void testSetModel() {
    Map<Coord, String> cells = c.getEvaluatedCells();
    c.setModel(wk2);
    Map<Coord, String> cells2 = c.getEvaluatedCells();
    assertFalse(cells == cells2);
    assertNotEquals(cells.size(), cells2.size());
    Map<Coord, String> cells3 = c.getEvaluatedCells();
    assertEquals(cells2.size(), cells3.size());
    for (Entry e : cells2.entrySet()) {
      assertEquals(cells3.get(e.getKey()), e.getValue());
    }
  }
}