package io.github.rahulojha07.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellType.*;

public class ExcelToJsonServiceImpl implements ExcelToJsonService {
    static Logger logger = LogManager.getLogger(ExcelToJsonServiceImpl.class);
    @Override
     public Boolean createJsonFile(String filePath) {
        try {
            // create a excel workbook from the Excel File Path
            Workbook excelWorkBook = WorkbookFactory.create(new File(filePath));
            // find out the number of sheets on the excel workbook
            int totalSheetNumber = excelWorkBook.getNumberOfSheets();
            // iterate for each sheet
            for (int i = 0; i < totalSheetNumber; i++) {
                // get sheet at index i
                Sheet sheet = excelWorkBook.getSheetAt(i);
                // get sheet name
                String sheetName = sheet.getSheetName();
                if (sheetName != null && sheetName.length() > 0) {
                    // get each row as a list inside one list
                    List<List<String>> sheetDataTable = getSheetDataList(sheet);
                    // convert sheetDataTable to a JSON String
                    String jsonString = getJSONStringFromList(sheetDataTable);
                    // create the name of the sheet
                    String jsonFileName = sheet.getSheetName() + ".json";
                    // create a file and save it with the sheet name
                    writeStringToFile(jsonString, jsonFileName);
                }
            }
            // close whole workbook
            excelWorkBook.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

     @Override
     public Boolean createTextFile(String filePath) {
        try {
            // create a excel workbook from the Excel File Path
            Workbook excelWorkBook = WorkbookFactory.create(new File(filePath));
            // find out the number of sheets on the excel workbook
            int totalSheetNumber = excelWorkBook.getNumberOfSheets();
            // iterate for each sheet
            for (int i = 0; i < totalSheetNumber; i++) {
                // get sheet at index i
                Sheet sheet = excelWorkBook.getSheetAt(i);
                // get sheet name
                String sheetName = sheet.getSheetName();
                if (sheetName != null && sheetName.length() > 0) {
                    // get each row as a list inside one list
                    List<List<String>> sheetDataTable = getSheetDataList(sheet);
                    // create the sheetDataTable to a text table
                    String textTableString = getTextTableStringFromList(sheetDataTable);
                    // create the name of the sheet
                    String textTableFileName = sheet.getSheetName() + ".txt";
                    // create a file and save it with the sheet name
                    writeStringToFile(textTableString, textTableFileName);
                }
            }
            // close whole workbook
            excelWorkBook.close();
        } catch (Exception ex) {
            logger.error(ex);
            return false;
        }
         return true;
    }

     public Boolean creteJSONAndTextFileFromExcel(String filePath) {
        try {
            Workbook excelWorkBook = WorkbookFactory.create(new File(filePath));
            int totalSheetNumber = excelWorkBook.getNumberOfSheets();
            for (int i = 0; i < 1; i++) {
                Sheet sheet = excelWorkBook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                if (sheetName != null && sheetName.length() > 0) {
                    List<List<String>> sheetDataTable = getSheetDataList(sheet);
                    String jsonString = getJSONStringFromList(sheetDataTable);
                    String jsonFileName = sheet.getSheetName() + ".json";
                    writeStringToFile(jsonString, jsonFileName);
                    String textTableString = getTextTableStringFromList(sheetDataTable);
                    String textTableFileName = sheet.getSheetName() + ".txt";
                    writeStringToFile(textTableString, textTableFileName);
                }
            }
            // close whole workbook
            excelWorkBook.close();
        } catch (Exception ex) {
            logger.error(ex);
            return false;
        }
         return true;
    }

     @Override
     public List<List<String>> getSheetDataList(Sheet sheet) {
        List<List<String>> sheetDataList = new ArrayList<>();
        //find the first row position
        int firstRowIndex = sheet.getFirstRowNum();
         //find the last row position
        int lastRowIndex = sheet.getLastRowNum();
        // iterate for each row of a sheet
         for (int i = firstRowIndex; i <= lastRowIndex ; i++) {
             logger.info("working on line: " + i);
             // get i-th row from sheet
             Row row = sheet.getRow(i);
             // get start index of row
             if (row == null) {
                 continue;
             }
             int firstCellIndex = row.getFirstCellNum();
             // get end index of row
             int lastCellIndex = row.getLastCellNum();
             // list to store row data
             List<String> rowDataList = new ArrayList<>();
             // iterate on each cell of a row
             for (int j = firstCellIndex; j < lastCellIndex; j++) {
                 // get i-th cell data
                 Cell cell = row.getCell(j);
                 if (cell == null) {
                     continue;
                 }
                 // get the data type of the cell
                 CellType cellType = cell.getCellType();
                 // to store the string value of the cell
                 String stringCellValue ;
                 if (cellType == NUMERIC){
                     // get the numeric value of the cell
                     double numberValue = cell.getNumericCellValue();
                     if (DateUtil.isCellDateFormatted(cell)) {
                         // if the cell is DateFormatted cell then store the LocalDateTime cell value
                         LocalDateTime date = cell.getLocalDateTimeCellValue();
                         // get the date in specific format
                         stringCellValue = date.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
                     } else {
                         // get the correct value of the numeric data
                         stringCellValue = getNumericValueAsString(numberValue);
                     }
                 }
                 else if (cellType == STRING) {
                     // if the cell has string value get it
                     stringCellValue = cell.getStringCellValue();
                 } else if (cellType == BOOLEAN) {
                     // if the cell has boolean value get it convert it to string value
                     boolean boolValue = cell.getBooleanCellValue();
                     stringCellValue = String.valueOf(boolValue);
                 } else if (cellType == BLANK) {
                     // if the cell is empty
                     stringCellValue = "";
                 } else if (cellType == FORMULA) {
                     // if the cell has some formula
                     // find out the cached result type of the cell
                     CellType formulaResultType = cell.getCachedFormulaResultType();
                     if (formulaResultType== NUMERIC) {
                         // if the result type is numeric get correct data
                         stringCellValue = getNumericValueAsString(cell.getNumericCellValue());
                     } else if (formulaResultType==STRING) {
                         // if the result type is string get data
                         stringCellValue = cell.getStringCellValue();
                     }else{
                         // TODO : Need to work on this sometime!!
//                       // if the result type is something else put Err there
                         stringCellValue = "Err!!";
                     }
                 }
                 else {
                     stringCellValue = "";
                 }
                 rowDataList.add(stringCellValue);
             }
             sheetDataList.add(rowDataList);
         }
        return sheetDataList;
    }

    private String getNumericValueAsString(Double numberValue) {
        if (numberValue == Math.round(numberValue)){
            // if the numberValue is a integral value
            return Math.round(numberValue) + "" ;
        }
        // if the numberValue is double value
        return String.format("%.2f", numberValue);
    }

     @Override
     public String getJSONStringFromList(List<List<String>> dataTable) {
        // to store data as jsonString
        String jsonString = "";
        // if dataTable is not null
        if (dataTable != null) {
            // find the size of dataTable
            int rowCount = dataTable.size();
            // create main json object
            JsonObject jsonObject = new JsonObject();
            // add total property
            jsonObject.add("total",getAsJsonElement(rowCount));
            // create an array of json objects (row data)
            JsonArray dataArray = new JsonArray();
            // if there is more than 1 row in dataTable
            if (rowCount > 1) {
                // get the first row as header row
                List<String> headerRow = dataTable.get(0);
                // find the header size
                int columnCount = headerRow.size();
                // for each row in dataTable
                for (int i = 1; i < rowCount; i++) {
                    // get the row
                    List<String> dataRow = dataTable.get(i);
                    // create a json object to store row data
                    JsonObject rowJsonObject = new JsonObject();
                    // add id for the row
                    rowJsonObject.add("id", new JsonPrimitive(i));
                    // for each data in a row
                    for (int j = 0; j < columnCount; j++) {
                        // get the column header
                        String columnName = headerRow.get(j);
                        // get the column data value
                        String columnValue = "";
                        try {
                            columnValue = dataRow.get(j);
                        } catch (Exception e) {
                            logger.error("got error on i=" + i + " j =" +j);
                        }

                        // add it in the row as a Json object
                        rowJsonObject.add(columnName, getAsJsonElement(columnValue));
                    }
                    // add the row to the json data array
                    dataArray.add(rowJsonObject);
                }
            }
            // add the data array to the main json object
            jsonObject.add("data",dataArray);
            // get the string value of the json object
            jsonString = jsonObject.toString();
        }
        // return the string value for json data
        return jsonString;
    }

    JsonElement getAsJsonElement(Object data) {
        // return data by converting it to jsonElement
        return new JsonPrimitive(String.valueOf(data));
    }

     @Override
     public String getTextTableStringFromList(List<List<String>> dataTable) {
        StringBuilder strBuf = new StringBuilder();
        if (dataTable != null) {
            for (List<String> row : dataTable) {
                for (String column : row) {
                    strBuf.append(column);
                    strBuf.append("    ");
                }
                strBuf.append("\r\n");
            }
        }
        return strBuf.toString();
    }

     @Override
     public void writeStringToFile(String data, String fileName) {
        try {
            // get current working folder
            String currentWorkingFolder = System.getProperty("user.dir");
            String filePathSeparator = System.getProperty("file.separator");
            String filePath = currentWorkingFolder + filePathSeparator + "JsonFiles" + filePathSeparator + fileName;
            File file = new File(filePath);
            FileWriter fw = new FileWriter(file);
            BufferedWriter buffWriter = new BufferedWriter(fw);
            buffWriter.write(data);
            buffWriter.flush();
            buffWriter.close();
            logger.info(filePath + " has been created.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}