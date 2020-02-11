package edu.cs3500.spreadsheets;

import edu.cs3500.spreadsheets.controller.ISpreadsheetController;
import edu.cs3500.spreadsheets.controller.SpreadsheetController;
import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.BasicWorksheetBuilder;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IWorksheet;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.view.SpreadsheetEditGUIView;
import edu.cs3500.spreadsheets.view.SpreadsheetGUIView;
import edu.cs3500.spreadsheets.view.SpreadsheetTextualView;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The main class for our program.
 */
public class BeyondGood {

  /**
   * The main entry point.
   *
   * @param args any command-line arguments
   */
  public static void main(String[] args) {
    try {
      FileReader fr = new FileReader(args[1]);
      BasicWorksheet bwk = new BasicWorksheet();
      WorksheetReader.WorksheetBuilder<IWorksheet> worksheet = new BasicWorksheetBuilder(bwk);
      IWorksheet wk = WorksheetReader.read(worksheet, fr);
      ISpreadsheetController c = new SpreadsheetController(wk);

      if (args.length >= 3) {
        switch (args[2]) {
          case "-eval":
            System.out.println(wk.evaluate(new Coord(args[3])));
            break;
          case "-save":
            PrintWriter pw = new PrintWriter(args[3]);
            SpreadsheetTextualView view = new SpreadsheetTextualView(c, pw);
            view.render();
            pw.close();
            break;
          case "-gui":
            SpreadsheetGUIView gui = new SpreadsheetGUIView(c);
            gui.render();
            break;
          case "-edit":
            SpreadsheetEditGUIView editGUI = new SpreadsheetEditGUIView(c);
            editGUI.render();
            break;
          default:
            throw new IllegalArgumentException("not a supported action");
        }
      }
    } catch (IllegalArgumentException | IOException e) {
      System.out.println("Something went wrong: " + e.getMessage());
    }
  }
}
