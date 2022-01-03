package eu.ibagroup.easyrpa.openframework.googlesheets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SheetDK {
    private final Sheet sheet;
    private final String spreadsheetId;
    private final Sheets service;

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    public SheetDK(String spreadsheetId, Sheet sheet, Sheets service) {
        this.sheet = sheet;
        this.spreadsheetId = spreadsheetId;
        this.service = service;
    }

    public <T> List<T> read(Class<T> tClass) throws IOException {
        List<ValueRange> valueRanges = readRange(sheet.getProperties().getTitle());
        if (Objects.isNull(valueRanges)) {
            return null;
        }

        List<List<Object>> values = valueRanges.get(0).getValues();
        if (values.isEmpty()) {
            return null;
        }

        List<String> tFields = Stream.of(tClass.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());

        List<T> entities = new ArrayList<>();
        for (List<Object> value : values) {
            Map<String, Object> fieldMap = new HashMap<>();
            for (int fieldIndex = 0; fieldIndex < tFields.size(); fieldIndex++) {
                fieldMap.put(tFields.get(fieldIndex), value.get(fieldIndex));
            }
            entities.add(OBJECT_MAPPER.convertValue(fieldMap, tClass));
        }
        return entities;
    }

    public List<List<Object>> read() throws IOException {
        return readRange(sheet.getProperties().getTitle()).get(0).getValues();
    }

    public void writeValues(List<List<Object>> values) throws IOException {
        writeObjects(sheet.getProperties().getTitle(), values);
    }

    public <T> void writeEntities(List<T> entities) throws IOException {
        writeToRange(sheet.getProperties().getTitle(), entities);
    }

    public <T> void write(T entity) throws IOException {
        writeToRange(sheet.getProperties().getTitle(), entity);
    }

    private List<ValueRange> readRange(String range) throws IOException {
        return service.spreadsheets().values()
                .batchGet(spreadsheetId)
                .setRanges(Collections.singletonList(range))
                .execute().getValueRanges();
    }

    private <T> UpdateValuesResponse writeToRange(String range, T entity) throws IOException {
        List<Object> values = new ArrayList<Object>(OBJECT_MAPPER.convertValue(entity, Map.class).values());
        return writeObjects(range, Collections.singletonList(values));
    }

    private <T> UpdateValuesResponse writeToRange(String range, List<T> entities) throws IOException {
        List<List<Object>> values = new ArrayList<>();
        for (T entity: entities) {
            List<Object> value = new ArrayList<Object>(OBJECT_MAPPER.convertValue(entity, Map.class).values());
            values.add(value);
        }
        return writeObjects(range, values);
    }

    private UpdateValuesResponse writeObjects(String range, List<List<Object>> values) throws IOException {
        ValueRange body = new ValueRange().setValues(values);
        return service.spreadsheets().values()
                .update(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .execute();
    }
}
