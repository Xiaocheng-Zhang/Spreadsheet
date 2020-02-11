package edu.cs3500.spreadsheets.model.functions;

import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.EvaluateVisitor;
import edu.cs3500.spreadsheets.model.GetValueVisitor;
import edu.cs3500.spreadsheets.model.IFunc;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.Sexp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * the square root function - finds the square root of the given argument.
 */
public class SqrtFunc implements IFunc<List<Sexp>, Sexp> {

  private BasicWorksheet wk;
  private HashMap<Coord, Boolean> usedCells;

  /**
   * the constructor.
   *
   * @param wk        the worksheet
   * @param usedCells list of used cells
   */
  public SqrtFunc(BasicWorksheet wk, HashMap<Coord, Boolean> usedCells) {
    this.wk = wk;
    this.usedCells = usedCells;
  }

  /**
   * visitor for a list of Sexp that returns a single Sexp.
   *
   * @param arg the arguments
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

    if (list.size() != 1) {
      throw new IllegalArgumentException("incorrect number of arguments");
    }

    return new SNumber(Math.sqrt(list.get(0)));
  }
}