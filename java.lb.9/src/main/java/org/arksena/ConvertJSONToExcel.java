package org.arksena;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class ConvertJSONToExcel {
    private static final String EXCEL_EXTENSION = ".xlsx";

    public static void convert(String jsonInput, String filename, String dataArrayKey, String sheetName) throws IOException {
        Workbook workbook = getOrCreateWorkbook(filename);

        try {
            verifySheetDoesNotExist(workbook, sheetName);

            Sheet sheet = workbook.createSheet(sheetName);
            ArrayNode dataArray = extractDataArrayFromJson(jsonInput, dataArrayKey);

            if (!dataArray.isEmpty()) {
                createAndFillHeaderRow(sheet, dataArray.get(0));
                populateSheetWithData(sheet, dataArray);
                adjustColumnsWidth(sheet, dataArray.get(0).size());
            }

            writeFile(workbook, filename + EXCEL_EXTENSION);
        } finally {
            workbook.close();
        }
    }

    private static Workbook getOrCreateWorkbook(String filename) throws IOException {
        File file = new File(filename + EXCEL_EXTENSION);
        if (file.exists()) {
            return new XSSFWorkbook(new FileInputStream(file));
        }
        return new XSSFWorkbook();
    }

    private static void verifySheetDoesNotExist(Workbook workbook, String sheetName) {
        if (workbook.getSheet(sheetName) != null) {
            throw new IllegalArgumentException("Sheet with name " + sheetName + " already exists.");
        }
    }

    private static ArrayNode extractDataArrayFromJson(String jsonInput, String dataArrayKey) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonInput);

        return (ArrayNode) rootNode.get(dataArrayKey);
    }

    private static void createAndFillHeaderRow(Sheet sheet, JsonNode firstItem) {
        Row headerRow = sheet.createRow(0);
        int columnIndex = 0;

        Iterator<Map.Entry<String, JsonNode>> fields = firstItem.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            Cell headerCell = headerRow.createCell(columnIndex++);
            headerCell.setCellValue(field.getKey());
        }
    }

    private static void populateSheetWithData(Sheet sheet, ArrayNode dataArray) {
        for (int i = 0; i < dataArray.size(); i++) {
            ObjectNode dataItem = (ObjectNode) dataArray.get(i);
            Row row = sheet.createRow(i + 1);
            int columnIndex = 0;
            for (JsonNode field : dataItem) {
                Cell cell = row.createCell(columnIndex++);
                cell.setCellValue(field.asText());
            }
        }
    }

    private static void adjustColumnsWidth(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private static void writeFile(Workbook workbook, String filePath) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
    }
}
