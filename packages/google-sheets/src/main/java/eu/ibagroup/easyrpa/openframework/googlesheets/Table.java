package eu.ibagroup.easyrpa.openframework.googlesheets;

import eu.ibagroup.easyrpa.openframework.googlesheets.constants.InsertMethod;
import eu.ibagroup.easyrpa.openframework.googlesheets.internal.RecordTypeHelper;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//TODO implement multi line header support. Currently only one line header is supported.
public class Table<T> implements Iterable<T> {

    /**
     * Reference to parent sheet.
     */
    private final Sheet parent;

    /**
     * Index of the header top row.
     */
    private int hTopRow;

    /**
     * Index of the header left column.
     */
    private int hLeftCol;

    /**
     * Index of the header bottom row.
     */
    private int hBottomRow;

    /**
     * Index of the header right column.
     */
    private int hRightCol;

    /**
     * Index of the last row of this table.
     */
    private int bottomRow = -1;

    /**
     * Cached map that maps column titles to its 0-based ordering number.
     */
    private Map<String, Integer> columnNameToIndexMap;

    /**
     * Cached list of table records.
     */
    private List<T> records;

    /**
     * Helper that converts records to corresponding row data and vice versa. Also it's responsible for providing
     * contained in record type meta information that necessary to build the table or its rows.
     */
    private RecordTypeHelper<T> typeHelper;

    /**
     * Builds a new table on the given sheet at position defined by <code>topRow</code> and <code>leftCol</code>
     * (top-left cell of the table).
     *
     * @param parent  parent sheet where the table should be placed.
     * @param topRow  0-based index of row that defines top-left cell of the table.
     * @param leftCol 0-based index of column that defines top-left cell of the table.
     * @param records list of records to insert into the table after creation. Also this list provides information
     *                about type of records with meta-information is necessary for table construction.
     *                That's why this list should not be empty.
     */
    @SuppressWarnings("unchecked")
    protected Table(Sheet parent, int topRow, int leftCol, List<T> records) throws IOException {
        this.parent = parent;
        if (records != null && records.size() > 0) {
            this.typeHelper = RecordTypeHelper.getFor((Class<T>) records.get(0).getClass());
            buildTable(topRow, leftCol, records);
        } else {
            this.hTopRow = topRow;
            this.hLeftCol = leftCol;
            this.hBottomRow = topRow;
            this.hRightCol = parent.getLastColumnIndex();
        }
    }

    public Table(Sheet parent, int headerTopRow, int headerLeftCol, int headerBottomRow, int headerRightCol, Class<T> recordType) {
        this.parent = parent;
        this.hTopRow = headerTopRow;
        this.hLeftCol = headerLeftCol;
        this.hBottomRow = headerBottomRow;
        this.hRightCol = headerRightCol;
        this.typeHelper = RecordTypeHelper.getFor(recordType);
    }

    public Table(Sheet parent, int headerTopRow, int headerLeftCol, int headerBottomRow, int headerRightCol, int bottomRow, Class<T> recordType) {
        this.parent = parent;
        this.hTopRow = headerTopRow;
        this.hLeftCol = headerLeftCol;
        this.hBottomRow = headerBottomRow;
        this.hRightCol = headerRightCol;
        this.typeHelper = RecordTypeHelper.getFor(recordType);
        this.bottomRow = bottomRow;
    }

//DELETED will not support
//    public ExcelDocument getDocument() {
//        return parent.getDocument();
//    }

    /**
     * Gets parent sheet.
     *
     * @return parent Excel document.
     */
    public Sheet getSheet() {
        return parent;
    }

    /**
     * Gets index of the top row of this table header.
     *
     * @return 0-based index of the top row of this table header.
     */
    public int getHeaderTopRow() {
        return hTopRow;
    }

    /**
     * Sets new index of the top row of this table header.
     *
     * @param topRowIndex the new index of the top row to set.
     */
    public void setHeaderTopRow(int topRowIndex) {
        this.hTopRow = topRowIndex;
        columnNameToIndexMap = null;
    }

    /**
     * Gets index of the left column of this table header.
     *
     * @return 0-based index of the left column  of this table header.
     */
    public int getHeaderLeftCol() {
        return hLeftCol;
    }

    /**
     * Sets new index of the left column of this table header.
     *
     * @param leftColIndex the new index of the left column to set.
     */
    public void setHeaderLeftCol(int leftColIndex) {
        this.hLeftCol = leftColIndex;
        columnNameToIndexMap = null;
    }

    /**
     * Gets index of the bottom row of this table header.
     *
     * @return 0-based index of the bottom row of this table header.
     */
    public int getHeaderBottomRow() {
        return hBottomRow;
    }

    /**
     * Sets new index of the bottom row of this table header.
     *
     * @param bottomRowIndex the new index of the bottom row to set.
     */
    public void setHeaderBottomRow(int bottomRowIndex) {
        this.hBottomRow = bottomRowIndex;
        columnNameToIndexMap = null;
    }

    /**
     * Gets index of the right column of this table header.
     *
     * @return 0-based index of the right column  of this table header.
     */
    public int getHeaderRightCol() {
        return hRightCol;
    }

    /**
     * Sets new index of the right column of this table header.
     *
     * @param rightColIndex the new index of the right column to set.
     */
    public void setHeaderRightCol(int rightColIndex) {
        this.hRightCol = rightColIndex;
        columnNameToIndexMap = null;
    }

    /**
     * Gets index of the last row of this table.
     * <p>
     * If the bottom row is not specified explicitly this method returns the actual index of the last row
     * of parent sheet.
     *
     * @return an actual index of the last row of this table.
     */
    public int getBottomRow() {
        if (bottomRow < 0) {
            return parent.getLastRowIndex();
        }
        return bottomRow;
    }

    /**
     * Sets index of the last row of this table explicitly.
     * <p>
     * It's necessary to use when the table ends earlier than the last row of the parent sheet. Once this bottom row
     * index is specified it will be automatically corrected during adding, inserting or removing records.
     *
     * @param bottomRowIndex 0-based index of the row that should be the last row of this table.
     */
    public void setBottomRow(int bottomRowIndex) {
        this.bottomRow = bottomRowIndex;
    }

    /**
     * Gets map that maps column titles to its ordering number (0-based).
     * <p>
     * It's necessary to properly map table records to corresponding row data and vice versa.
     *
     * @return map that maps column titles to its ordering number.
     */
    public Map<String, Integer> getColumnNameToIndexMap() throws IOException {
        if (columnNameToIndexMap == null) {
            this.columnNameToIndexMap = getColumnNameToIndexMap(this.hTopRow, this.hLeftCol, this.hRightCol);
        }
        return columnNameToIndexMap;
    }

    /**
     * Gets full list of records that are contained in this table.
     *
     * @return list of table records.
     */
    public List<T> getRecords() throws IOException {
        if (records == null) {
            Map<String, Integer> columnsIndexMap = getColumnNameToIndexMap();
            if (columnsIndexMap != null) {
                List<List<Object>> data = parent.getRange(hBottomRow + 1, hLeftCol + 1, getBottomRow(), hRightCol+1);
                records = data.stream().map(values -> typeHelper.mapToRecord(values, columnsIndexMap)).collect(Collectors.toList());
            }
        } else {
            //Make sure that all records have been loaded into cache
            for (int i = 0; i < records.size(); i++) {
                if (records.get(i) == null) {
                    getRecord(i);
                }
            }
        }
        return new ArrayList<>(records);
    }

    /**
     * Gets specific record by its index.
     * <p>
     * Records are indexed starting with the row right below the header bottom row.
     *
     * @param index 0-based index of the record to get.
     * @return instance of corresponding record or <code>null</code> if record at such index is not exist.
     */
    public T getRecord(int index) throws IOException {
        int recordsCount = getRecordsCount();
        if (index < 0 || index >= recordsCount) {
            return null;
        }
        if (records == null) {
            records = new ArrayList<>(Collections.nCopies(recordsCount, null));
        }
        T record = records.get(index);
        if (record == null) {
            Row row = parent.getRow(index + hBottomRow + 1);
            List<Object> values = row.getRange(hLeftCol, hRightCol);
            record = typeHelper.mapToRecord(values, getColumnNameToIndexMap());
            records.set(index, record);
        }
        return record;
    }

    public T findRecord(Predicate<T> isSatisfy) throws IOException {
        int index = findRecordIndex(isSatisfy);
        return index >= 0 ? records.get(index) : null;
    }

    public int findRecordIndex(Predicate<T> isSatisfy) throws IOException {
        if (isSatisfy != null) {
            int recordsCount = getRecordsCount();
            for (int i = 0; i < recordsCount; i++) {
                T record = getRecord(i);
                if (isSatisfy.test(record)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int indexOf(T record) throws IOException {
        return record != null ? findRecordIndex(record::equals) : -1;
    }

    public int getRecordsCount() {
        return getBottomRow() - hBottomRow;
    }

    public void addRecord(T record) throws IOException {
        insertRecord(InsertMethod.BEFORE, getRecordsCount(), record);
    }

    public void addRecords(List<T> records) throws IOException {
        insertRecords(InsertMethod.BEFORE, getRecordsCount(), records);
    }

    public void insertRecord(InsertMethod method, T relatedRecord, T record) throws IOException {
        insertRecords(method, indexOf(relatedRecord), Collections.singletonList(record));
    }

    public void insertRecord(InsertMethod method, int recordIndex, T record) throws IOException {
        insertRecords(method, recordIndex, Collections.singletonList(record));
    }

    public void insertRecords(InsertMethod method, T relatedRecord, List<T> records) throws IOException {
        insertRecords(method, indexOf(relatedRecord), records);
    }

    public void insertRecords(InsertMethod method, int recordIndex, List<T> records) throws IOException {
        if (recordIndex < 0 || records == null || records.isEmpty()) {
            return;
        }
        Map<String, Integer> columnsIndexMap = getColumnNameToIndexMap();
        //TODO think to replace map with list of column names
        Map<Integer, String> columnNamesMap = columnsIndexMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        List<List<Object>> data = records.stream().map(r -> typeHelper.mapToValues(r, columnsIndexMap)).collect(Collectors.toList());
        parent.insertRows(method, recordIndex + hBottomRow + 1, hLeftCol, data);

        int insertPos = method == null || method == InsertMethod.BEFORE ? recordIndex : recordIndex + 1;
        if (this.records == null) {
            this.records = new ArrayList<>(Collections.nCopies(getRecordsCount(), null));
        }
        this.records.addAll(insertPos, records);
        if (bottomRow >= 0) {
            bottomRow += records.size();
        }

        int rowsCount = data.size();
        int startRow = insertPos + hBottomRow + 1;
        for (int i = startRow; i < rowsCount + startRow; i++) {
            for (int j = hLeftCol; j < hRightCol; j++) {
                typeHelper.formatCell(parent.getCell(i, j), columnNamesMap.get(j - hLeftCol), i - hBottomRow - 1, this.records);
            }
        }
    }

    public void updateRecord(T record) throws IOException {
        updateRecords(Collections.singletonList(record));
    }

    public void updateRecords(List<T> records) throws IOException {
        if (records == null) {
            return;
        }
        Map<String, Integer> columnsIndexMap = getColumnNameToIndexMap();
        //TODO think to replace map with list of column names
        Map<Integer, String> columnNamesMap = columnsIndexMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        for (T record : records) {
            int index = indexOf(record);
            if (index >= 0) {
                int rowNum = index + hBottomRow + 1;
                List<Object> values = typeHelper.mapToValues(record, columnsIndexMap);
                parent.putRange(rowNum, hLeftCol, values);
                for (int j = hLeftCol; j <= hRightCol; j++) {
                    typeHelper.formatCell(parent.getCell(rowNum, j), columnNamesMap.get(j - hLeftCol), index, this.records);
                }
                this.records.set(index, record);
            }
        }
    }

    public void removeRecord(T record) throws IOException {
        if (record != null) {
            int index = indexOf(record);
            if (index >= 0) {
                parent.removeRow(index + hBottomRow + 1);
                this.records.remove(index);
                if (bottomRow >= 0) {
                    bottomRow--;
                }
            }
        }
    }

    public void removeRecords(List<T> records) throws IOException {
        if (records != null) {
            for (T record : records) {
                removeRecord(record);
            }
        }
    }

    public void clearCache() {
        records = null;
    }

    public void trimLeadingAndTrailingSpaces() {
        while (parent.getColumn(getHeaderLeftCol()).isEmpty()) {
            setHeaderLeftCol(getHeaderLeftCol() + 1);
        }

        while (parent.getColumn(getHeaderRightCol()).isEmpty()) {
            setHeaderRightCol(getHeaderRightCol() - 1);
        }
    }
/* TODO decide will we implement*/

/*    public Table<T> filter(int columnIndex, String filterPattern) {
        int lastColumnIndex = hRightCol - hLeftCol;
        if (columnIndex < 0 || columnIndex > lastColumnIndex) {
            return this;
        }
        CellRange tableRange = new CellRange(getSheet().getName(), hTopRow, hLeftCol, getBottomRow(), hRightCol);
        CellRange valuesRange = new CellRange(hBottomRow + 1, hLeftCol + columnIndex,
                getBottomRow(), hLeftCol + columnIndex);
        getDocument().runScript(new Filter(tableRange, columnIndex + 1, filterPattern, valuesRange));
        return this;
    }

    public Table<T> sort(int columnIndex, SortDirection direction) {
        int lastColumnIndex = hRightCol - hLeftCol;
        if (columnIndex < 0 || columnIndex > lastColumnIndex) {
            return this;
        }
        direction = direction != null ? direction : SortDirection.ASC;
        CellRange columnRange = new CellRange(getSheet().getName(), hTopRow, hLeftCol + columnIndex,
                getBottomRow(), hLeftCol + columnIndex);
        getDocument().runScript(new Sorter(columnRange, direction));
        return this;
    }*/

    @Override
    public Iterator<T> iterator() {
        return new RecordIterator();
    }

    private Map<String, Integer> getColumnNameToIndexMap(int tableHeaderRow, int headerLeftCol, int headerRightCol) throws IOException {
        Row headerRow = parent.getRow(tableHeaderRow);
        if (headerRow == null) {
            return null;
        }
        Map<String, Integer> columnsIndex = new HashMap<>();
        List<String> columns = headerRow.getRange(headerLeftCol, headerRightCol, String.class);
        for (int i = 0; i < columns.size(); i++) {
            String columnName = columns.get(i);
            columnsIndex.put(columnName != null ? columnName : "", i);
        }
        return columnsIndex;
    }

    private void buildTable(int startRow, int startCol, List<T> records) throws IOException {
        List<String> columnNames = typeHelper.getColumnNames();
        int columnsCount = columnNames.size();

        hTopRow = startRow;
        hLeftCol = startCol;
        hBottomRow = startRow;
        hRightCol = hLeftCol + columnsCount;

        parent.insertRows(InsertMethod.BEFORE, hTopRow, hLeftCol, columnNames);
        for (int j = hLeftCol; j < hRightCol; j++) {
            typeHelper.formatHeaderCell(parent.getCell(hTopRow, j), columnNames.get(j - hLeftCol));
        }

        insertRecords(InsertMethod.BEFORE, 0, records);
    }

    private class RecordIterator implements Iterator<T> {

        private int index = 0;
        private int recordsCount;

        public RecordIterator() {
            recordsCount = getBottomRow() - hBottomRow;
        }

        @Override
        public boolean hasNext() {
            return index < recordsCount;
        }

        @Override
        public T next() {
            try {
                return getRecord(index++);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
