package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Parser;

/**
 * the basic worksheet builder.
 */
public class BasicWorksheetBuilder implements WorksheetReader.WorksheetBuilder<IWorksheet> {

  private IWorksheet worksheet;

  public BasicWorksheetBuilder(IWorksheet worksheet) {
    this.worksheet = worksheet;
  }

  /**
   * Creates a new cell at the given coordinates and fills in its raw contents.
   *
   * @param col      the column of the new cell (1-indexed)
   * @param row      the row of the new cell (1-indexed)
   * @param contents the raw contents of the new cell: may be {@code null}, or any string. Strings
   *                 beginning with an {@code =} character should be treated as formulas; all other
   *                 strings should be treated as number or boolean values if possible, and string
   *                 values otherwise.
   * @return this
   */
  @Override
  public WorksheetReader.WorksheetBuilder<IWorksheet> createCell(int col, int row,
      String contents) {
    Coord coord = new Coord(col, row);
    Cell cell = new Cell(Parser.parse(remEquals(contents)));
    this.worksheet.addCell(coord, cell);
    return this;
  }

  /**
   * Finalizes the construction of the worksheet and returns it.
   *
   * @return the fully-filled-in worksheet
   */
  @Override
  public IWorksheet createWorksheet() {
    return this.worksheet;
  }

  private String remEquals(String contents) {
    if (contents.charAt(0) == '=') {
      return contents.replaceFirst("=", "");
    } else {
      return contents;
    }
  }
}
