package eu.ibagroup.easyrpa.examples.excel.excel_file_editing;

import eu.ibagroup.easyrpa.engine.annotation.ApModuleEntry;
import eu.ibagroup.easyrpa.engine.apflow.ApModule;
import eu.ibagroup.easyrpa.engine.apflow.TaskOutput;
import eu.ibagroup.easyrpa.examples.excel.excel_file_editing.tasks.EditCellsOnSheet;
import eu.ibagroup.easyrpa.examples.excel.excel_file_editing.tasks.EditRecordsOnSheet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApModuleEntry(name = "Excel File Editing")
public class ExcelFileEditingModule extends ApModule {

    public TaskOutput run() throws Exception {
        return execute(getInput(), EditCellsOnSheet.class)
                .thenCompose(execute(EditRecordsOnSheet.class))
                .get();
    }
}