package com.example.absencesessect.utils;

import android.util.Log;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    public interface ExcelCallback {
        void onExcelRead(List<String> data);
    }

    public static void readExcel(String filePath, ExcelCallback callback) {
        List<String> data = new ArrayList<>();
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);

            Workbook workbook;


            if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else {
                workbook = new HSSFWorkbook(fis);
            }

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                StringBuilder rowData = new StringBuilder();
                row.forEach(cell -> rowData.append(cell.toString()).append(" | "));
                data.add(rowData.toString());
            }

            fis.close();
            callback.onExcelRead(data);

        } catch (IOException e) {
            Log.e("ExcelReader", "Error reading Excel file", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Log.e("ExcelReader", "Error closing file input stream", e);
                }
            }
        }
    }
}
