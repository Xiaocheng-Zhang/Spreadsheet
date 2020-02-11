package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IWorksheet;

import java.util.Map;

/**
 * A controller interface for spreadsheets. Exposes interactions with the model for the view
 */
public interface ISpreadsheetController {

  /**
   * Get a map of Coordinates to cell data in a spreadsheet model.
   *
   * @return
   */
  Map<Coord, String> getEvaluatedCells();

  /**
   * Get a map of raw cell data in a spreadsheet model.
   *
   * @return
   */
  Map<Coord, Cell> getCells();

  /**
   * Edit a cell in the model.
   *
   * @param c Coord at which to insert/update a Cell
   * @param s data to insert into the cell
   */
  void editCell(Coord c, String s);

  /**
   * Clear data in a cell.
   *
   * @param c Coord at which to clear data
   */
  void clearCell(Coord c);

  /**
   * set a new Model (used for opening different files from the view).
   */
  void setModel(IWorksheet wk);
}
