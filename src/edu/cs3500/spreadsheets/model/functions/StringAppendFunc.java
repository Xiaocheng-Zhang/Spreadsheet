package edu.cs3500.spreadsheets.model.functions;

import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.EvaluateVisitor;
import edu.cs3500.spreadsheets.model.IFunc;
import edu.cs3500.spreadsheets.model.ToStringVisitor;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * the string append function - appends all given strings.
 */
public class StringAppendFunc implements IFunc<List<Sexp>, Sexp> {

  private BasicWorksheet wk;
  private HashMap<Coord, Boolean> usedCells;

  /**
   * the constructor.
   *
   * @param wk        worksheet
   * @param usedCells list of used cells
   */
  public StringAppendFunc(BasicWorksheet wk, HashMap<Coord, Boolean> usedCells) {
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
    ArrayList<String> list = new ArrayList();
    ToStringVisitor tsv = new ToStringVisitor();

    for (int i = 1; i < arg.size(); i++) {
      Sexp curr = arg.get(i).accept(new EvaluateVisitor(this.wk, this.usedCells));
      list.add(curr.accept(tsv));
    }

    String str = "";
    for (String s : list) {
      str = str + s;
    }

    return new SString(str);
  }
}