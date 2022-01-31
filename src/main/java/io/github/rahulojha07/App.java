package io.github.rahulojha07;

import io.github.rahulojha07.services.ExcelToJsonService;
import io.github.rahulojha07.services.ExcelToJsonServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hello world!
 *
 */
public class App 
{
    static Logger logger = LogManager.getLogger(App.class);
    public static void main( String[] args )
    {
        logger.info( "Hello World!" );
        String excelFilePath = "src/main/resources/AnimeData_Top Anime.xlsx";
        ExcelToJsonService service = new ExcelToJsonServiceImpl();
        service.createJsonFile(excelFilePath);
    }
}
