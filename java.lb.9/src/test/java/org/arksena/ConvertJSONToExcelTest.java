package org.arksena;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ConvertJSONToExcelTest {

    private String tempFilename;

    @BeforeEach
    public void setUp() {
        tempFilename = System.getProperty("java.io.tmpdir") + "/testExcel";
        File file = new File(tempFilename + ".xlsx");
        file.delete();
    }

    @AfterEach
    public void tearDown() {
        File file = new File(tempFilename + ".xlsx");
        file.delete();
    }

    @Test
    public void testConvertWithNewFile() throws IOException {
        String sampleJson = "{ \"data\": [ { \"name\": \"John\", \"age\": 30 }, { \"name\": \"Doe\", \"age\": 25 } ] }";

        ConvertJSONToExcel.convert(sampleJson, tempFilename, "data", "testSheet");

        assertTrue(new File(tempFilename + ".xlsx").exists());

        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(tempFilename + ".xlsx"))) {
            Sheet sheet = workbook.getSheet("testSheet");
            assertNotNull(sheet);

            Row headerRow = sheet.getRow(0);
            assertEquals("name", headerRow.getCell(0).getStringCellValue());
            assertEquals("age", headerRow.getCell(1).getStringCellValue());

            Row firstDataRow = sheet.getRow(1);
            assertEquals("John", firstDataRow.getCell(0).getStringCellValue());
            assertEquals("30", firstDataRow.getCell(1).getStringCellValue());
        }
    }

    @Test
    public void testConvertWithExistingSheetName() throws IOException {
        String sampleJson = "{ \"data\": [ { \"name\": \"John\" } ] }";

        ConvertJSONToExcel.convert(sampleJson, tempFilename, "data", "testSheet");
        assertThrows(IllegalArgumentException.class, () -> ConvertJSONToExcel.convert(sampleJson, tempFilename, "data", "testSheet"));
    }
}
