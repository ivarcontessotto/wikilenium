package ch.hslu.swt.wikilenium.core;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

class ExcelFileHelper {

    static String[] readColumn(int index, File excelFile) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(excelFile))) {
            Sheet sheet = workbook.getSheetAt(0);
            LinkedList<String> entries = new LinkedList<>();
            sheet.forEach(row -> {
                Cell cell = row.getCell(index);
                if (cell != null) {
                    entries.add(cell.toString());
                }
            });
            return entries.toArray(new String[0]);
        }
    }

    static void writeColumn(int index, String[] cellEntries, File excelFile) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(excelFile))) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i < cellEntries.length; i++) {
                sheet.getRow(i).createCell(index).setCellValue(cellEntries[i]);
                System.out.println(sheet.getRow(i).getCell(index).toString());
            }
            try(FileOutputStream outputStream = new FileOutputStream(excelFile)) {
                workbook.write(outputStream);
            }
        }
    }
}
