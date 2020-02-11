package edu.cs3500.spreadsheets.model.functions;

import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.EvaluateVisitor;
import edu.cs3500.spreadsheets.model.GetValueVisitor;
import edu.cs3500.spreadsheets.model.IFunc;
import edu.cs3500.spreadsheets.sexp.SBoolean;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.Sexp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * the less than function - compares two given integers.
 */
public class LessThanFunc implements IFunc<List<Sexp>, Sexp> {

  private BasicWorksheet wk;
  private HashMap<Coord, Boolean> usedCells;

  /**
   * the constructor.
   *
   * @param wk        the worksheet
   * @param usedCells list of used cells
   */
  public LessThanFunc(BasicWorksheet wk, HashMap<Coord, Boolean> usedCells) {
    this.wk = wk;
    this.usedCells = usedCells;
  }

  /**
   * visitor for a list of Sexp that returns a single Sexp.
   *
   * @param arg the argument
   * @return
   */
  @Override
  public Sexp apply(List<Sexp> arg) {
    ArrayList<Double> list = new ArrayList();
    GetValueVisitor gvv = new GetValueVisitor();

    for (int i = 1; i < arg.size(); i++) {
      Sexp curr = arg.get(i).accept(new EvaluateVisitor(this.wk, this.usedCells));
      if (curr instanceof SNumber) {
        list.add((double) curr.accept(gvv));
      }
    }

    if (list.size() != 2) {
      throw new IllegalArgumentException("incorrect amount of values to evaluate");
    }

    return new SBoolean(list.get(0) < list.get(1));
  }
}
