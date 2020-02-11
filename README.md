# Spreadsheet
## Author: Abigail Ko, Xiaocheng Zhang

- A small application that save datas and read datas.
- This spreadsheet works similar as *Office Excel* with methods built in.
- Files in *resources* are tests data.

- User can run the program from terminal:
  1. find out the *Spreadsheet.jar* in resources folder
  2. We provided several ways to open a sheet with input file data:
    1. -in <file-name> -edit                      will provide an editable spreadsheet with the data <file-name> in *resouces* folder
    2. -in <file-name> -gui                       will provide a gui only for user to view the data
    3. -in <file-name> -save <new-file-name>      will allow user to save the data into a other file called <new-file-name>        
    4. -in <file-name> -execute <row> <column>    will allow user to execute a cell's value in spreadsheet
  Mutation of data in opened application will not change the data directly. If users want to save the data, they must click the *menu* in the corner, then click *save*
  We also provided open new files by clicking *open*
  
