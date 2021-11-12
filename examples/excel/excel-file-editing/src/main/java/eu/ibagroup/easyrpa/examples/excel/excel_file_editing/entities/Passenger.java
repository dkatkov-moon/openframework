package eu.ibagroup.easyrpa.examples.excel.excel_file_editing.entities;

import eu.ibagroup.easyrpa.openframework.excel.annotations.ExcelColumn;
import lombok.Data;

@Data
public class Passenger {

    @ExcelColumn(name = "Passenger Id", identifier = true)
    private Integer passengerId;

    @ExcelColumn(name = "Name")
    private String name;

    @ExcelColumn(name = "Sex")
    private String sex;

    @ExcelColumn(name = "Age")
    private Integer age;

    @ExcelColumn(name = "Survived")
    private boolean survived;

    @ExcelColumn(name = "Class")
    private Integer pClass;

    @ExcelColumn(name = "Siblings on board")
    private Integer SibSp;

    @ExcelColumn(name = "Parch")
    private Integer parch;

    @ExcelColumn(name = "Ticket")
    private String ticket;

    @ExcelColumn(name = "Fare")
    private Double fare;

    @ExcelColumn(name = "Cabin")
    private String cabin;

    @ExcelColumn(name = "Embarked")
    private String embarked;
}