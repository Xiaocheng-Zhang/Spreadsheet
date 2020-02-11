package edu.cs3500.spreadsheets.view.components;

import edu.cs3500.spreadsheets.controller.ISpreadsheetController;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IFunc;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Vector;

/**
 * A class representing the Table GUI, allows cell selection, clearing, and "infinite" scroll
 * features.
 */
public class SpreadsheetTable extends JPanel {

  private ISpreadsheetController c;
  private JTable table;
  private JScrollPane scrollPane;
  private IFunc<Coord, Object> onCellSelect;
  private int rows;
  private int cols;

  /**
   * Constructor for a view-only Table.
   *
   * @param c Controller instance from view
   */
  public SpreadsheetTable(ISpreadsheetController c) {
    this.c = c;
    this.rows = 50;
    this.cols = 50;
    this.onCellSelect = null;

    setup();
  }

  /**
   * Constructor for an editable Table.
   *
   * @param c            Controller instance from view
   * @param onCellSelect IFunc for select action
   */
  public SpreadsheetTable(ISpreadsheetController c, IFunc<Coord, Object> onCellSelect) {
    this.c = c;
    this.rows = 26;
    this.cols = 26;
    this.onCellSelect = onCellSelect;

    setup();
  }

  private void setup() {
    TableModel dataModel = new GUITableModel(rows, cols, c.getEvaluatedCells());
    table = new JTable(dataModel);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    table.setCellSelectionEnabled(true);

    if (onCellSelect != null) {
      table.addKeyListener(new ClearCell());
      TableSelectionListener selectionListener = new TableSelectionListener();
      table.getSelectionModel().addListSelectionListener(selectionListener);
      table.getColumnModel().getSelectionModel()
          .addListSelectionListener(selectionListener);
    }

    scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    this.add(scrollPane);

    scrollPane.getVerticalScrollBar()
        .addAdjustmentListener(new ScrollBarAdjustmentListener("vertical"));

    scrollPane.getHorizontalScrollBar()
        .addAdjustmentListener(new ScrollBarAdjustmentListener("horizontal"));
    scrollPane.setRowHeaderView(buildRowHeader(table));
  }

  /**
   * Trigger an update so the table will reset its TableModel.
   */
  public void updateTable() {
    table.setModel(new GUITableModel(rows, cols, c.getEvaluatedCells()));
  }

  /**
   * Key listener for backspace when a cell is selected, clear the row.
   */
  private class ClearCell extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == 8
          && table.getSelectedRow() >= 0
          && table.getSelectedColumn() >= 0) {
        int row = table.getSelectedRow();
        int col = table.getSelectedColumn();
        c.clearCell(new Coord(col + 1, row + 1));
        table.setModel(new GUITableModel(rows, cols, c.getEvaluatedCells()));
        table.changeSelection(row, col, false, false);
      }
    }
  }

  /**
   * Listener for scroll bar hitting its end so that the table can be grown.
   */
  private class ScrollBarAdjustmentListener implements AdjustmentListener {

    private String type;

    ScrollBarAdjustmentListener(String type) {
      this.type = type;
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
      if (!e.getValueIsAdjusting()) {
        JScrollBar scrollBar = (JScrollBar) e.getAdjustable();
        if (scrollBar.getModel().getExtent() + e.getValue() == scrollBar.getModel()
            .getMaximum()) {
          if (type.equals("horizontal")) {
            cols++;
          } else {
            rows++;
          }
          table.setModel(new GUITableModel(rows, cols, c.getEvaluatedCells()));
          scrollPane.setRowHeaderView(buildRowHeader(table));
        }
      }
    }
  }

  /**
   * Listener for Select actions on the table.
   */
  private class TableSelectionListener implements ListSelectionListener {

    @Override
    public void valueChanged(ListSelectionEvent e) {
      if (table.getSelectedColumn() >= 0 && table.getSelectedRow() >= 0) {
        onCellSelect
            .apply(new Coord(table.getSelectedColumn() + 1, table.getSelectedRow() + 1));
      }
    }
  }

  /**
   * This next section of code was taken from a stack overflow post for the purposes of adding row
   * headers to the JTable. (https://stackoverflow.com/questions/1434933/jtable-row-header-implementation)
   */
  private static Component buildRowHeader(final JTable table) {
    final Vector<Integer> headers = new Vector<Integer>();
    for (int i = 0; i < table.getRowCount(); i++) {
      headers.add(i + 1);
    }
    ListModel lm = new AbstractListModel() {

      public int getSize() {
        return headers.size();
      }

      public Object getElementAt(int index) {
        return headers.get(index);
      }
    };

    final JList rowHeader = new JList(lm);
    rowHeader.setOpaque(false);
    rowHeader.setFixedCellWidth(50);

    rowHeader.setCellRenderer(new RowHeaderRenderer(table));
    rowHeader.setBackground(table.getBackground());
    rowHeader.setForeground(table.getForeground());
    return rowHeader;
  }

  static class RowHeaderRenderer extends JLabel implements ListCellRenderer {

    private JTable table;

    RowHeaderRenderer(JTable table) {
      this.table = table;
      JTableHeader header = this.table.getTableHeader();
      setOpaque(true);
      setBorder(UIManager.getBorder("TableHeader.cellBorder"));
      setHorizontalAlignment(CENTER);
      setForeground(header.getForeground());
      setBackground(header.getBackground());
      setFont(header.getFont());
      setDoubleBuffered(true);
    }

    public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean cellHasFocus) {
      setText((value == null) ? "" : value.toString());
      setPreferredSize(null);
      setPreferredSize(
          new Dimension((int) getPreferredSize().getWidth(), table.getRowHeight(index)));
      //trick to force repaint on JList (set updateLayoutStateNeeded = true) on BasicListUI
      list.firePropertyChange("cellRenderer", 0, 1);
      return this;
    }
  }

  /**
   * Class representing the TableModel for the JTable.
   */
  private class GUITableModel extends AbstractTableModel {

    private int rows;
    private int cols;
    Map<Coord, String> cells;

    GUITableModel(int rows, int cols, Map<Coord, String> cells) {
      this.rows = rows;
      this.cols = cols;
      this.cells = cells;
    }

    @Override
    public int getRowCount() {
      return rows;
    }

    @Override
    public int getColumnCount() {
      return cols;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      String value = cells.get(new Coord(columnIndex + 1, rowIndex + 1));
      return value == null ? "" : value;
    }
  }

}