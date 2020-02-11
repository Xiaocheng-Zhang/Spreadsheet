package edu.cs3500.spreadsheets.view.components;

import edu.cs3500.spreadsheets.controller.ISpreadsheetController;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.sexp.SList;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.Sexp;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;

/**
 * A class representing the Input at the top that will show the raw Sexp behind each table cell, and
 * allows changes to be committed or reset.
 */
public class SpreadsheetInput extends JPanel {

  private final int TEXT_INPUT_SIZE = 35;
  private final int BUTTON_SIZE = 20;
  private JButton accept;
  private JButton reject;
  private JTextField textField;
  private Coord selected;
  private ISpreadsheetController c;
  private SpreadsheetTable table;

  /**
   * Constructor for a SpreadsheetInput.
   *
   * @param c        Controller instance for the view
   * @param selected the currently selected cell
   * @param table    a reference to the table instance
   */
  public SpreadsheetInput(ISpreadsheetController c, Coord selected, SpreadsheetTable table) {
    this.c = c;
    this.selected = selected;
    this.table = table;
    this.setLayout(new GridBagLayout());

    accept = new JButton("Y");
    accept.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
    accept.addActionListener(new AcceptListener());
    reject = new JButton("X");
    reject.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
    reject.addActionListener(new RejectListener());

    this.add(accept);
    this.add(reject);
    textField = new JTextField();
    textField.setColumns(TEXT_INPUT_SIZE);
    if (selected == null) {
      accept.setEnabled(false);
      reject.setEnabled(false);
      textField.setEditable(false);
    }
    this.add(textField);
    this.setVisible(true);
  }

  /**
   * Set the selected coordinate (to pull from the controller).
   *
   * @param selected the selected Coord
   */
  public void setSelected(Coord selected) {
    this.selected = selected;
    boolean state = selected != null;
    accept.setEnabled(state);
    reject.setEnabled(state);
    textField.setEditable(state);
    if (state && c.getCells().get(selected) != null) {
      Sexp s = c.getCells().get(selected).getValue();
      if (s instanceof SList || s instanceof SSymbol) {
        textField.setText("=" + s.toString());
      } else {
        textField.setText(s.toString());
      }
    } else {
      textField.setText("");
    }
  }

  /**
   * Listener for an accept action to accept changes and commit to model.
   */
  private class AcceptListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      String text = textField.getText();
      if (text.length() <= 0) {
        c.clearCell(selected);
        table.updateTable();
        return;
      }
      if (text.charAt(0) == '=') {
        text = text.replaceFirst("=", "");
      }
      c.editCell(selected, text);
      table.updateTable();
    }
  }

  /**
   * Listener for a reject action to reject changes and reset input.
   */
  private class RejectListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      setSelected(selected);
    }
  }
}
