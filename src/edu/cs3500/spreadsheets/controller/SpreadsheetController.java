package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IWorksheet;
import edu.cs3500.spreadsheets.sexp.Parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of the ISpreadsheetController interface, exposing model
 * interactions to the user.
 */
public class SpreadsheetController implements ISpreadsheetController {
  private IWorksheet wk;

  public SpreadsheetController(IWorksheet wk) {
    this.wk = wk;
  }

  @Override
  public Map<Coord, Cell> getCells() {
    return Collections.unmodifiableMap(wk.getCells());
  }

  @Override
  public Map<Coord, String> getEvaluatedCells() {
    Map<Coord, Cell> data = wk.getCells();
    Map<Coord, String> cells = new HashMap<Coord, String>();
    for (Map.Entry<Coord, Cell> e : data.entrySet()) {
      try {
        cells.put(e.getKey(), wk.evaluate(e.getKey()).toString());
      } catch (IllegalArgumentException | NullPointerException i) {
        cells.put(e.getKey(), "#VALUE");
      }
    }
    return cells;
  }

  @Override
  public void editCell(Coord c, String s) {
    s = s.trim();
    wk.addCell(c, new Cell(Parser.parse(s)));
  }

  @Override
  public void clearCell(Coord c) {
    wk.removeCell(c);
  }

  @Override
  public void setModel(IWorksheet wk) {
    this.wk = wk;
  }
}
