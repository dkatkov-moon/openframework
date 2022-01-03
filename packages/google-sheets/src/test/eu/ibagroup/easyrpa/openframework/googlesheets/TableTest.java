package eu.ibagroup.easyrpa.openframework.googlesheets;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TableTest {

    private UserEntity DENIS = new UserEntity("Denis", 36);
    private UserEntity BOB = new UserEntity("Bob", 29);

    private GoogleSheets googleSheets;

    @Before
    public void setUp() throws GeneralSecurityException, IOException {
        googleSheets = new GoogleSheets(new File("c:\\work\\openframework\\tokens"), Paths.get("c:\\work\\openframework\\credentials.json"));
    }

    /**
     * name	age
     * Denis	36
     * Bob	29
     */
    @Test
    public void read() throws IOException {
        Spreadsheet spreadsheet = googleSheets.getSpreadsheet("1jqlsw9Afzm4dA5PWJn6wWXKH7BiC91PL92U9ogOFp4U");
        SheetDK sheet = spreadsheet.getSheetDk("Tab One").get();
        List<List<Object>> values = sheet.read();
        Assert.assertEquals(3, values.size());
        Assert.assertEquals("name", String.valueOf(values.get(0).get(0)));
        Assert.assertEquals("age", String.valueOf(values.get(0).get(1)));
        Assert.assertEquals("Denis", String.valueOf(values.get(1).get(0)));
        Assert.assertEquals("36", String.valueOf(values.get(1).get(1)));
        Assert.assertEquals("Bob", String.valueOf(values.get(2).get(0)));
        Assert.assertEquals("29", String.valueOf(values.get(2).get(1)));
    }

    @Test
    public void writeAndRead() throws IOException {
        Spreadsheet spreadsheet = googleSheets.getSpreadsheet("1jqlsw9Afzm4dA5PWJn6wWXKH7BiC91PL92U9ogOFp4U");
        SheetDK sheet = spreadsheet.getSheetDk("Blank Tab").get();
        sheet.write(Collections.singletonList(
                Arrays.asList("Peter", 28)
        ));

        List<List<Object>> values = sheet.read();
        Assert.assertEquals("Peter", String.valueOf(values.get(0).get(0)));
        Assert.assertEquals("28", String.valueOf(values.get(0).get(1)));
    }

    @Test
    public void writeAndReadEntity() throws IOException {
        Spreadsheet spreadsheet = googleSheets.getSpreadsheet("1jqlsw9Afzm4dA5PWJn6wWXKH7BiC91PL92U9ogOFp4U");
        SheetDK sheet = spreadsheet.getSheetDk("Blank Tab").get();
        sheet.writeEntities(Arrays.asList(DENIS, BOB));

        List<UserEntity> values = sheet.read(UserEntity.class);
        Assert.assertEquals(DENIS.getName(), values.get(0).getName());
        Assert.assertEquals(DENIS.getAge(), values.get(0).getAge());

        Assert.assertEquals(BOB.getName(), values.get(1).getName());
        Assert.assertEquals(BOB.getAge(), values.get(1).getAge());
    }
}