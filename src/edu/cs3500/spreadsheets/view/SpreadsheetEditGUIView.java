package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.controller.ISpreadsheetController;
import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.BasicWorksheetBuilder;
import edu.cs3500.spreadsheets.model.Coord;

import edu.cs3500.spreadsheets.model.IFunc;
import edu.cs3500.spreadsheets.model.IWorksheet;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.view.components.SpreadsheetInput;
import edu.cs3500.spreadsheets.view.components.SpreadsheetTable;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * A class representing the GUI view with edit functionality.
 * Features:
 *  cell selection
 *  table editing through editor at the top (with commit and reset actions)
 *  cell clearing with backspace key when selected in table
 *  saving to a file
 *  opening a different file
 */
public class SpreadsheetEditGUIView extends JFrame implements ISpreadsheetView {

  private static final int INITIAL_WIDTH = 520;
  private static final int INITIAL_HEIGHT = 520;
  private ISpreadsheetController c;
  private Coord selected;
  private SpreadsheetInput input;
  private SpreadsheetTable table;

  public SpreadsheetEditGUIView(ISpreadsheetController c) {
    this.c = c;
    this.selected = null;
  }

  @Override
  public void render() throws IOException {
    this.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);

    // Build menu bar
    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("File");

    JMenuItem newItem = new JMenuItem("New");
    newItem.addActionListener(new NewFile());
    JMenuItem openItem = new JMenuItem("Open");
    openItem.addActionListener(new OpenFile());
    JMenuItem saveItem = new JMenuItem("Save");
    saveItem.addActionListener(new SaveFile());

    fileMenu.add(newItem);
    fileMenu.add(openItem);
    fileMenu.add(saveItem);

    menuBar.add(fileMenu);
    this.setJMenuBar(menuBar);

    // Build content
    JPanel content = new JPanel();
    content.setLayout(new BorderLayout(0, 10));

    table = new SpreadsheetTable(c, new SetSelected());

    input = new SpreadsheetInput(c, selected, table);

    content.add(input, BorderLayout.NORTH);
    content.add(table, BorderLayout.CENTER);
    this.setContentPane(content);

    pack();

    this.setVisible(true);
  }

  /**
   * Listener for New option click to open a blank spreadsheet.
   */
  private class NewFile implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      IWorksheet wk = new BasicWorksheet();
      c.setModel(wk);
      table.updateTable();
    }
  }

  /**
   * Listener for Open option click to open a different file.
   */
  private class OpenFile implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      JFileChooser fc = new JFileChooser();
      int returnVal = fc.showOpenDialog((JMenuItem) e.getSource());
      if (returnVal == 0) {
        try {
          FileReader fr = new FileReader(fc.getSelectedFile());
          BasicWorksheet bwk = new BasicWorksheet();
          WorksheetReader.WorksheetBuilder<IWorksheet> worksheet = new BasicWorksheetBuilder(bwk);
          IWorksheet wk = WorksheetReader.read(worksheet, fr);
          c.setModel(wk);
          table.updateTable();
        } catch (IOException f) {
          System.out.println("Error opening file: " + f.getMessage());
        }
      }
    }
  }

  /**
   * Listener for Open option click to save a file.
   */
  private class SaveFile implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      JFileChooser fc = new JFileChooser();
      int returnVal = fc.showSaveDialog((JMenuItem) e.getSource());
      if (returnVal == 0) {
        try {
          PrintWriter pw = new PrintWriter(fc.getSelectedFile());
          SpreadsheetTextualView view = new SpreadsheetTextualView(c, pw);
          view.render();
          pw.close();
        } catch (IOException f) {
          System.out.println("Error saving file: " + f.getMessage());
        }
      }
    }
  }

  /**
   * Functional class for setting selected state.
   */
  private class SetSelected implements IFunc<Coord, Object> {

    @Override
    public Object apply(Coord coord) {
      selected = coord;
      input.setSelected(coord);
      return null;
    }
  }
}
