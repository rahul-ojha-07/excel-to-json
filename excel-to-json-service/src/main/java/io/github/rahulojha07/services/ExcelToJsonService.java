package io.github.rahulojha07.services;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface ExcelToJsonService {
    Boolean createJsonFile(String filePath);

    Boolean createTextFile(String filePath);

    List<List<String>> getSheetDataList(Sheet sheet);

    String getJSONStringFromList(List<List<String>> dataTable);

    String getTextTableStringFromList(List<List<String>> dataTable);

    void writeStringToFile(String data, String fileName);
}
