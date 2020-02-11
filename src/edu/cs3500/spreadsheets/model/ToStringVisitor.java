package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;

import java.util.List;

/**
 * returns the string of a given sexp.
 */
public class ToStringVisitor implements SexpVisitor<String> {

  /**
   * Process a boolean value.
   *
   * @param b the value
   * @return the desired result
   */
  @Override
  public String visitBoolean(boolean b) {
    return String.valueOf(b);
  }

  /**
   * Process a numeric value.
   *
   * @param d the value
   * @return the desired result
   */
  @Override
  public String visitNumber(double d) {
    return String.valueOf(d);
  }

  /**
   * Process a list value.
   *
   * @param l the contents of the list (not yet visited)
   * @return the desired result
   */
  @Override
  public String visitSList(List<Sexp> l) {
    String combined = "";
    for (Sexp sexp : l) {
      combined = combined + sexp.accept(this);
    }
    return combined;
  }

  /**
   * Process a symbol.
   *
   * @param s the value
   * @return the desired result
   */
  @Override
  public String visitSymbol(String s) {
    return s;
  }

  /**
   * Process a string value.
   *
   * @param s the value
   * @return the desired result
   */
  @Override
  public String visitString(String s) {
    return s;
  }
}
