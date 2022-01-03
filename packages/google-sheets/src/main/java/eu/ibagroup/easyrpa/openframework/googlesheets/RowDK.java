package eu.ibagroup.easyrpa.openframework.googlesheets;

import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.RowData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RowDK {
    private RowData rowData;
    private List<CellDK> cells;

    public RowDK(RowData rowData) {
        this.rowData = rowData;
        this.cells = new ArrayList<>();
        List<CellData> cellDataList = rowData.getValues();
        if (Objects.nonNull(cellDataList) && !cellDataList.isEmpty()) {
            cells.addAll(cellDataList.stream()
                    .map(CellDK::new).collect(Collectors.toList()));
        }
    }

    public List<CellDK> getCells() {
        return cells;
    }
}
