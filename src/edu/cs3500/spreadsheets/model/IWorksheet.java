package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Sexp;

import java.util.HashMap;

/**
 * interface for worksheets so that different kinds can later be created.
 */
public interface IWorksheet {

  void addCell(Coord coord, Cell cell);

  void removeCell(Coord coord);

  Sexp evaluate(Coord coord);

  HashMap<Coord, Cell> getCells();
}