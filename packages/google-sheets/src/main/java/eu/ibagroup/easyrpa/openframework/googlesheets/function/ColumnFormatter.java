package eu.ibagroup.easyrpa.openframework.googlesheets.function;

import com.google.api.services.sheets.v4.model.CellData;

@FunctionalInterface
public interface ColumnFormatter<T> {

    /**
     * Formats specific cells based on some specific logic.
     *
     * @param cell   - instance of cell to which formatting is applied.
     * @param column - name of column to which the cell belongs.
     * @param record - instance of current record. If value is null then cell is a header cell.
     */
    void format(CellData cell, String column, T record);
}