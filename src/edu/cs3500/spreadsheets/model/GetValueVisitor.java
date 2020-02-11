package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.SexpVisitor;

import java.util.List;

/**
 * returns value of given sexp.
 */
public class GetValueVisitor implements SexpVisitor {

  /**
   * Process a boolean value.
   *
   * @param b the value
   * @return the desired result
   */
  @Override
  public Object visitBoolean(boolean b) {
    return b;
  }

  /**
   * Process a numeric value.
   *
   * @param d the value
   * @return the desired result
   */
  @Override
  public Object visitNumber(double d) {
    return d;
  }

  /**
   * Process a symbol.
   *
   * @param s the value
   * @return the desired result
   */
  @Override
  public Object visitSymbol(String s) {
    return s;
  }

  /**
   * Process a string value.
   *
   * @param s the value
   * @return the desired result
   */
  @Override
  public Object visitString(String s) {
    return s;
  }

  /**
   * Process a list value.
   *
   * @param l the contents of the list (not yet visited)
   * @return the desired result
   */
  @Override
  public Object visitSList(List l) {
    return l;
  }
}
