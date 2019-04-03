package ch.hslu.swt.wikilenium.core;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.LinkedList;

class ExcelFileHelper {

    static String[] readHeader(File excelFile) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(excelFile))) {
            Sheet sheet = workbook.getSheetAt(0);
            LinkedList<String> header = new LinkedList<>();
            Row row = sheet.getRow(0);
            if (row == null) {
                return new String[0];
            }
            int lastCellNumber = row.getLastCellNum();
            for(int columnIndex = 0; columnIndex < lastCellNumber; columnIndex++) {
                Cell cell = row.getCell(columnIndex);
                if (cell != null) {
                    header.add(cell.toString());
                }
                else {
                    header.add("");
                }
            }
            return header.toArray(new String[0]);
        }
    }

    static String[] readColumn(int index, boolean readHeader, File excelFile) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(excelFile))) {
            Sheet sheet = workbook.getSheetAt(0);
            LinkedList<String> entries = new LinkedList<>();
            int numberOfFilledRows = sheet.getPhysicalNumberOfRows();
            int rowStartIndex = 0;
            if (!readHeader) {
                rowStartIndex = 1;
            }
            for(int rowIndex = rowStartIndex; rowIndex < numberOfFilledRows; rowIndex++) {
                Cell cell = sheet.getRow(rowIndex).getCell(index);
                if (cell != null) {
                    entries.add(cell.toString());
                }
            }
            return entries.toArray(new String[0]);
        }
    }

    static void writeColumn(int index, boolean writeHeader, String[] values, File excelFile) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(excelFile))) {
            Sheet sheet = workbook.getSheetAt(0);
            int valuesStartIndex = 0;
            if (writeHeader) {
                sheet.getRow(0).createCell(index).setCellValue(values[0]);
                Cell cell = sheet.getRow(0).createCell(0);
                cell.setCellValue(values[0]);

                XSSFFont font = workbook.createFont();
                font.setBold(true);
                XSSFCellStyle style = workbook.createCellStyle();
                style.setFont(font);
                cell.setCellValue(values[0]);
                cell.setCellStyle(style);

                valuesStartIndex = 1;
            }
            for (int valueIndex = valuesStartIndex, rowIndex = 1; valueIndex < values.length; valueIndex++, rowIndex++) {
                sheet.getRow(rowIndex).createCell(index).setCellValue(values[valueIndex]);
            }
            try(FileOutputStream outputStream = new FileOutputStream(excelFile)) {
                workbook.write(outputStream);
            }
        }
    }
}
