package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Sexp;

/**
 * the cell class, represents an individual rectangle on the spreadsheet.
 */
public class Cell {

  private Sexp value;

  /**
   * the constructor.
   *
   * @param value the cell's value
   */
  public Cell(Sexp value) {
    this.value = value;
  }

  /**
   * the toString method.
   *
   * @return
   */
  public String toString() {
    return value.toString();
  }

  /**
   * returns the value of the cell in the form of an Sexp.
   *
   * @return
   */
  public Sexp getValue() {
    return this.value.accept(new GetSexpVisitor());
  }

  /**
   * returns if the given object is equal to this Cell.
   *
   * @return
   */
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Cell other = (Cell) o;
    return value.equals(other.value);
  }

  /**
   * returns a hashCode representing the Cell.
   *
   * @return
   */
  public int hashCode() {
    return this.value.toString().hashCode();
  }
}