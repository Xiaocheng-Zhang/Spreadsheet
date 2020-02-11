package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.SBoolean;
import edu.cs3500.spreadsheets.sexp.SList;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SString;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;
import java.util.List;

/**
 * returns an sexp based on given sexp.
 */
public class GetSexpVisitor implements SexpVisitor<Sexp> {

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
    return new SSymbol(s);
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
  public Sexp visitSList(List<Sexp> l) {
    return new SList(l);
  }
}
