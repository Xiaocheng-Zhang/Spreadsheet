package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A value type representing coordinates in a {@link Worksheet}.
 */
public class Coord {
  public final int row;
  public final int col;

  /**
   * the constructor.
   *
   * @param col the column
   * @param row the row.
   */
  public Coord(int col, int row) {
    if (row < 1 || col < 1) {
      throw new IllegalArgumentException("Coordinates should be strictly positive");
    }
    this.row = row;
    this.col = col;
  }

  /**
   * the constructor that takes a string.
   *
   * @param coord the coordinate of the cell
   */
  public Coord(String coord) {
    String[] list = coord.split("(?<=\\D)(?=\\d)");
    this.col = colNameToIndex(list[0]);
    this.row = Integer.parseInt(list[1]);
  }

  /**
   * Converts from the A-Z column naming system to a 1-indexed numeric value.
   * @param name the column name
   * @return the corresponding column index
   */
  public static int colNameToIndex(String name) {
    name = name.toUpperCase();
    int ans = 0;
    for (int i = 0; i < name.length(); i++) {
      ans *= 26;
      ans += (name.charAt(i) - 'A' + 1);
    }
    return ans;
  }

  /**
   * Converts a 1-based column index into the A-Z column naming system.
   * @param index the column index
   * @return the corresponding column name
   */
  public static String colIndexToName(int index) {
    StringBuilder ans = new StringBuilder();
    while (index > 0) {
      int colNum = (index - 1) % 26;
      ans.insert(0, Character.toChars('A' + colNum));
      index = (index - colNum) / 26;
    }
    return ans.toString();
  }

  @Override
  public String toString() {
    return colIndexToName(this.col) + this.row;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coord coord = (Coord) o;
    return row == coord.row
        && col == coord.col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }

  /**
   * returns the list of coordinates as references.
   *
   * @param c1 first coordinate
   * @param c2 second coordinate
   * @return
   */
  public static List<Coord> references(Coord c1, Coord c2) {
    ArrayList<Coord> coords = new ArrayList();

    for (int i = c1.col; i <= c2.col; i++) {
      for (int j = c1.row; j <= c2.row; j++) {
        coords.add(new Coord(i, j));
      }
    }

    return coords;
  }
}
