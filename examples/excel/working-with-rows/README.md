# Working with sheet rows

This process example show what is possible to do with sheet rows of spreadsheet document using Excel package 
functionality.  

## Configuration
All necessary configuration files can be found in <code>src/main/resources</code> directory.

**apm_run.properties**

| Parameter     | Value         |
| ------------- |---------------|
| `source.spreadsheet.file` | Path to the source spreadsheet file. It can be path on local file system or within resources of this project. |
| `output.spreadsheet.file` | Path on local file system where all changes are going to be saved. |

## Running

Run `main()` method of `LocalRunner` class.