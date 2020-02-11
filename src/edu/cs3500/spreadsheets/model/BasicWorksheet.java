package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.Sexp;

import java.util.HashMap;
import java.util.Map;

/**
 * the basic worksheet class, keeps track of all cells.
 */
public class BasicWorksheet implements IWorksheet {

  private HashMap<Coord, Cell> cells;

  /**
   * the constructor.
   */
  public BasicWorksheet() {
    this.cells = new HashMap<Coord, Cell>();
  }

  /**
   * adds a cell to the hashmap.
   *
   * @param coord the coordinate of the cell to be added
   * @param cell  the cell (with its given value)
   */
  public void addCell(Coord coord, Cell cell) {
    cells.put(coord, cell);
  }

  @Override
  public void removeCell(Coord coord) {
    cells.remove(coord);
  }

  /**
   * evaluates a given cell based on its coordinate.
   *
   * @param coord the cell's coordinate
   * @return
   */
  public Sexp evaluate(Coord coord) {
    if (cells.get(coord) == null) {
      return new SSymbol("");
    }

    EvaluateVisitor evisit = new EvaluateVisitor(this, coord);
    return cells.get(coord).getValue().accept(evisit);
  }

  /**
   * the toString method for the hashmap.
   *
   * @return
   */
  public String toString() {
    String str = "";
    for (Map.Entry element : cells.entrySet()) {
      String key = element.getKey().toString();
      String value = element.getValue().toString();
      str += key + ", " + value + "\n";
    }
    return str;
  }

  /**
   * returns a copy of the existing hashmap.
   *
   * @return
   */
  public HashMap<Coord, Cell> getCells() {
    HashMap<Coord, Cell> newList = new HashMap<>();
    for (Map.Entry<Coord, Cell> element : cells.entrySet()) {
      newList.put(new Coord(element.getKey().toString()), new Cell(element.getValue().getValue()));
    }
    return newList;
  }
}
