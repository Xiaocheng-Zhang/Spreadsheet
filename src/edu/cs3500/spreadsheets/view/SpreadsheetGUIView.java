package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.controller.ISpreadsheetController;
import edu.cs3500.spreadsheets.view.components.SpreadsheetTable;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.BorderLayout;

import java.io.IOException;

/**
 * A class representing the view-only GUI view.
 */
public class SpreadsheetGUIView extends JFrame implements ISpreadsheetView {

  private static final int INITIAL_WIDTH = 520;
  private static final int INITIAL_HEIGHT = 440;
  private ISpreadsheetController c;

  public SpreadsheetGUIView(ISpreadsheetController c) {
    this.c = c;
  }

  @Override
  public void render() throws IOException {
    this.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
    this.setLayout(new BorderLayout());
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Container container = getContentPane();

    SpreadsheetTable table = new SpreadsheetTable(c);
    table.setPreferredSize(new Dimension(INITIAL_WIDTH, INITIAL_HEIGHT));

    container.add(table, BorderLayout.CENTER);
    this.pack();
    this.setVisible(true);
  }
}
