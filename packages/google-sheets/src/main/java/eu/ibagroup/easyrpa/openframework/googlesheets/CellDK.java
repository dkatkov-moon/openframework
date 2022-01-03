package eu.ibagroup.easyrpa.openframework.googlesheets;

import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.ExtendedValue;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class CellDK {
    private CellData cellData;

    public CellDK(CellData cellData) {
        this.cellData = cellData;
    }

    public Optional<String> getValue() {
        Optional<String> stringValue = getStringValue();
        if (stringValue.isPresent()) {
            return stringValue;
        }

        Optional<Double> numberValue = getNumberValue();
        if (numberValue.isPresent()) {
            return Optional.of(numberValue.get().toString());
        }

        return Optional.empty();
    }

    public Optional<String> getStringValue() {
        Optional<String> effectiveValue = getEffectiveValue()
                .map(ExtendedValue::getStringValue);
        Optional<String> userEnteredValue = getUserEnteredValue()
                .map(ExtendedValue::getStringValue);
        return Stream.of(effectiveValue, userEnteredValue)
                .filter(Optional::isPresent)
                .findFirst()
                .map(Optional::get);
    }

    public Optional<Double> getNumberValue() {
        Optional<Double> effectiveValue = getEffectiveValue()
                .map(ExtendedValue::getNumberValue);
        Optional<Double> userEnteredValue = getUserEnteredValue()
                .map(ExtendedValue::getNumberValue);
        return Stream.of(effectiveValue, userEnteredValue)
                .filter(Optional::isPresent)
                .findFirst()
                .map(Optional::get);
    }

    public void setValue(String stringValue) {
        ExtendedValue userEnteredValue = getUserEnteredValue()
                .orElse(new ExtendedValue());
        userEnteredValue.setStringValue(stringValue);
        cellData.setUserEnteredValue(userEnteredValue);
    }

    public void setValue(double doubleValue) {
        ExtendedValue userEnteredValue = getUserEnteredValue()
                .orElse(new ExtendedValue());
        userEnteredValue.setNumberValue(doubleValue);
        cellData.setUserEnteredValue(userEnteredValue);
    }

    private Optional<ExtendedValue> getEffectiveValue() {
        ExtendedValue extendedValue = cellData.getEffectiveValue();
        if (Objects.isNull(extendedValue)) {
            return Optional.empty();
        } else {
            return Optional.of(extendedValue);
        }
    }

    private Optional<ExtendedValue> getUserEnteredValue() {
        ExtendedValue extendedValue = cellData.getUserEnteredValue();
        if (Objects.isNull(extendedValue)) {
            return Optional.empty();
        } else {
            return Optional.of(extendedValue);
        }
    }
}
