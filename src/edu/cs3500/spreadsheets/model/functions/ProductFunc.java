package edu.cs3500.spreadsheets.model.functions;

import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.EvaluateVisitor;
import edu.cs3500.spreadsheets.model.GetValueVisitor;
import edu.cs3500.spreadsheets.model.IFunc;
import edu.cs3500.spreadsheets.sexp.SList;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.Sexp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * the product function - multiplies all arguments and returns the total.
 */
public class ProductFunc implements IFunc<List<Sexp>, Sexp> {

  private BasicWorksheet wk;
  private HashMap<Coord, Boolean> usedCells;

  /**
   * the constructor.
   *
   * @param wk        the worksheet
   * @param usedCells list of used cells
   */
  public ProductFunc(BasicWorksheet wk, HashMap<Coord, Boolean> usedCells) {
    this.wk = wk;
    this.usedCells = usedCells;
  }

  /**
   * visitor for a list of Sexp that returns a single Sexp.
   *
   * @param arg arguments
   * @return
   */
  @Override
  public Sexp apply(List<Sexp> arg) {
    ArrayList<Double> list = new ArrayList();
    GetValueVisitor gvv = new GetValueVisitor();

    for (int i = 0; i < arg.size(); i++) {
      Sexp curr = arg.get(i).accept(new EvaluateVisitor(this.wk, this.usedCells));
      if (curr instanceof SList) {
        list.add(
            (double) ((new ProductFunc(this.wk, this.usedCells).apply((List) curr.accept(gvv))))
                .accept(gvv));
      } else if (curr instanceof SNumber) {
        list.add((double) curr.accept(gvv));
      }
    }

    if (list.isEmpty()) {
      return new SNumber(0);
    }

    double total = 1;
    for (double d : list) {
      total = total * d;
    }

    return new SNumber(total);
  }
}