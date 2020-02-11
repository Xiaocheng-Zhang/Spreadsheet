package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.controller.ISpreadsheetController;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.sexp.SList;

import java.io.IOException;
import java.util.Map;

/**
 * the textual view class.
 */
public class SpreadsheetTextualView implements ISpreadsheetView {

  private final ISpreadsheetController c;
  private Appendable ap;

  /**
   * the constructor.
   *
   * @param c  a controller to pull information from the model
   * @param ap the appendable
   */
  public SpreadsheetTextualView(ISpreadsheetController c, Appendable ap) {
    this.c = c;
    this.ap = ap;
  }

  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   *
   * @throws IOException if the rendering fails for some reason
   */
  @Override
  public void render() throws IOException {
    Map<Coord, Cell> cells = c.getCells();
    for (Map.Entry<Coord, Cell> element : cells.entrySet()) {
      String key = element.getKey().toString();
      String value = element.getValue().getValue().toString();
      if (element.getValue().getValue() instanceof SList) {
        ap.append(key + " " + "=" + value + "\n");
      } else {
        ap.append(key + " " + value + "\n");
      }
    }
  }
}