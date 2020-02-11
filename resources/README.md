# Assignment 7 README
## Design Changes from Assignment 6
- Reformatted SpreadsheetTable code to be much cleaner and more modularized.
- Added a controller interface and class to serve model data and interact with the model.

## Design decisions for Assignment 7
- Reusing the same Table component from the read-only view.
- Added a new SpreadsheetInput component to deal with editing the raw data in a cell.
- Cell selections are dispatched to the SpreadsheetInput component, which then populates the text in the text input.
- On committing an edit, the edit is first sent to the model through the controller, then it informs the Table that it should refresh its data.
- Added a different constructor to the Table component where if lacking certain fields, the listeners will not activate.
- The state of which cell is selected should not need to be stored in the model. Thus we decided to keep that management in the view.
- Using the controller helps to further decouple the view from the model.

## Stretch goals achieved
- Can Open other files from the MenuBar. File -> Open
- Can Save to a file from the MenuBar. File -> Save
- Can press delete/backspace with a cell selected to clear the cell's data.
- Also added a New option to the MenuBar for opening a blank spreadsheet. File -> New