package generic;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Utils {
	
	public static String getXLData(String path, String sheet, int rowNum, int cellNum) {
		String value = "";
		try {
		Workbook wb = WorkbookFactory.create(new FileInputStream(path));
		value = wb.getSheet(sheet).getRow(rowNum).getCell(cellNum).toString(); 
		}catch(Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	public static String getProperty(String path, String key) throws Exception, IOException {
		String val="";
		Properties p = new Properties();
		p.load(new FileInputStream(path));
		val = p.getProperty(key);
		return val;
	}

}
