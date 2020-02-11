package edu.cs3500.spreadsheets.view;

import java.io.IOException;

/**
 * the interface for the view.
 */
interface ISpreadsheetView {

  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   *
   * @throws IOException if the rendering fails for some reason
   */
  void render() throws IOException;
}