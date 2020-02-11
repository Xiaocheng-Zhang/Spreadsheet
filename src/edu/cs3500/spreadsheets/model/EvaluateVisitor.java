package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.functions.LessThanFunc;
import edu.cs3500.spreadsheets.model.functions.ProductFunc;
import edu.cs3500.spreadsheets.model.functions.SqrtFunc;
import edu.cs3500.spreadsheets.model.functions.StringAppendFunc;
import edu.cs3500.spreadsheets.model.functions.SubFunc;
import edu.cs3500.spreadsheets.model.functions.SumFunc;

import edu.cs3500.spreadsheets.sexp.SBoolean;
import edu.cs3500.spreadsheets.sexp.SList;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SString;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * the evaluate visitor that returns a cells value in basic form.
 */
public class EvaluateVisitor implements SexpVisitor<Sexp> {

  private BasicWorksheet wk;
  private HashMap<Coord, Boolean> usedCells;

  EvaluateVisitor(BasicWorksheet wk) {
    this.wk = wk;
    this.usedCells = new HashMap<Coord, Boolean>();
  }

  EvaluateVisitor(BasicWorksheet wk, Coord coord) {
    this.wk = wk;
    this.usedCells = new HashMap<Coord, Boolean>();
    usedCells.put(coord, true);
  }

  /**
   * the constructor.
   *
   * @param wk worksheet
   * @param usedCells list of used cells
   */
  public EvaluateVisitor(BasicWorksheet wk, HashMap<Coord, Boolean> usedCells) {
    this.wk = wk;
    this.usedCells = new HashMap<Coord, Boolean>(usedCells);
  }

  /**
   * Process a boolean value.
   *
   * @param b the value
   * @return the desired result
   */
  @Override
  public Sexp visitBoolean(boolean b) {
    return new SBoolean(b);
  }

  /**
   * Process a numeric value.
   *
   * @param d the value
   * @return the desired result
   */
  @Override
  public Sexp visitNumber(double d) {
    return new SNumber(d);
  }

  /**
   * Process a symbol.
   *
   * @param s the value
   * @return the desired result
   */
  @Override
  public Sexp visitSymbol(String s) {
    if (s.contains(":")) {
      String[] cells = s.split(":");

      if (cells.length != 2) {
        throw new IllegalArgumentException("this reference was written incorrectly");
      }

      List<Coord> coords = Coord.references(new Coord(cells[0]), new Coord(cells[1]));
      List<Sexp> sexpList = new ArrayList();

      for (Coord c : coords) {
        if (usedCells.get(c) == null) {
          sexpList.add(wk.evaluate(c));
          usedCells.put(c, true);
        } else {
          throw new IllegalArgumentException("there's a loop");
        }
      }

      return new SList(sexpList);
    } else {
      Coord c = new Coord(s);
      if (usedCells.get(c) == null) {
        usedCells.put(c, true);
        return wk.getCells().get(c).getValue().accept(this);
      } else {
        throw new IllegalArgumentException("there's a loop");
      }
    }
  }

  /**
   * Process a string value.
   *
   * @param s the value
   * @return the desired result
   */
  @Override
  public Sexp visitString(String s) {
    return new SString(s);
  }

  /**
   * Process a list value.
   *
   * @param l the contents of the list (not yet visited)
   * @return the desired result
   */
  @Override
  public Sexp visitSList(List l) {
    switch (l.get(0).toString()) {
      case "PRODUCT":
        return new ProductFunc(this.wk, this.usedCells).apply(l.subList(1, l.size()));
      case "SUM":
        return new SumFunc(this.wk, this.usedCells).apply(l.subList(1, l.size()));
      case "SUB":
        return new SubFunc(this.wk, this.usedCells).apply(l.subList(1, l.size()));
      case "<":
        return new LessThanFunc(this.wk, this.usedCells).apply(l);
      case "SQRT":
        return new SqrtFunc(this.wk, this.usedCells).apply(l);
      case "STRING_APPEND":
        return new StringAppendFunc(this.wk, this.usedCells).apply(l);
      default:
        throw new IllegalArgumentException("not a valid function");
    }
  }
}
