package hms.report.excel;

import hms.main.DateFormatChange;
import hms.store.database.BillingDBConnection;
import hms.store.database.InvoiceDBConnection;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;

public class FinalSalesTaxReportExcel {

	Object Rows_Object_Array[][];
	int NumberOfColumns = 0, NumberOfRows = 0;
	Vector<String> id = new Vector<String>();
	static String OS;
	String mainDir = "", str = "";
	Font font;
	double totalAmount = 0, cancelledAmount = 0;
	float[] opdTablCellWidth = { 2.0f, 0.7f, 1.1f };
	String fromDateSTR, toDateSTR;
	Vector<String> specialization = new Vector<String>();
	HSSFFont font1 ,font2;
	HSSFCellStyle style,style2;

	public static void main(String[] arg) {
		try {
			new FinalSalesTaxReportExcel(
					"C:/Users/MDI/Desktop/Sales_Reports-2013-03-24 TO 2015-08-24.xls");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FinalSalesTaxReportExcel(
			String filePath) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		
		readXLSFile(filePath);
		try {
			File desktop = new File(System.getProperty("user.home"), "Desktop");
			String filename = desktop + "/" + "Final_Purchase_Reports" + "-" + DateFormatChange.StringToMysqlDate(new Date()) + ".xls";
			HSSFWorkbook workbook = new HSSFWorkbook();
			
			HSSFSheet sheet = workbook.createSheet("Final_Purchase_Reports");
			
			style = workbook.createCellStyle();
			style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			font1 = workbook.createFont();
			//font1.setColor(HSSFColor.RED.index);
			style.setFont(font1);
			
			style2 = workbook.createCellStyle();
			style2.setFillForegroundColor(HSSFColor.AQUA.index);
			style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			font2 = workbook.createFont();
			//font2.setColor(HSSFColor.RED.index);
			style2.setFont(font2);
			doctorWiseReport(sheet);

			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			JOptionPane.showMessageDialog(null,
					"Excel File Generated Successfully on your Desktop with Final_Purchase_Reports"
							+ "-" +DateFormatChange.StringToMysqlDate(new Date()) + ".xls"
							+ " Name", "Data Saved",
					JOptionPane.INFORMATION_MESSAGE);
			OPenFileWindows(filename);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void readXLSFile(String filePath) throws IOException {
		id.clear();
		InputStream ExcelFileToRead = new FileInputStream(filePath);
		HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);

		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row;
		HSSFCell cell;

		Iterator rows = sheet.rowIterator();
		row = (HSSFRow) rows.next();
		while (rows.hasNext()) {
			row = (HSSFRow) rows.next();

			Iterator cells = row.cellIterator();

			// while (cells.hasNext())
			// {
			// cell=(HSSFCell) cells.next();
			cell = (HSSFCell) cells.next();

			if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
				id.add(cell.getStringCellValue() + "");
				System.out.print(cell.getStringCellValue() + "");
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				id.add(cell.getNumericCellValue() + "");
				System.out.print(cell.getNumericCellValue() + "");
			} else {
				// U Can Handel Boolean, Formula, Errors
			}
			// }
			System.out.println();
		}

	}

	public void doctorWiseReport(HSSFSheet sheet) {
		HSSFRow rowhead = sheet.createRow((short) 0);
		
		int k = 1;
		try {
			InvoiceDBConnection db = new InvoiceDBConnection();
			for (int i = 0; i < id.size(); i++) {
				ResultSet rs = db.retrieveSalesTaxReport(id.get(i));
				java.sql.ResultSetMetaData rsmd = rs.getMetaData();
				int columnsNumber = rsmd.getColumnCount();

				for (int i1 = 0; i1 < columnsNumber; i1++) {
					if (k == 1)
						rowhead.createCell(i1).setCellValue(
								rsmd.getColumnName(i1 + 1));
				}
				while (rs.next()) {
					HSSFRow rowhead1 = sheet.createRow((short) k);
					for (int i1 = 0; i1 < columnsNumber; i1++) {
						
						rowhead1.createCell(i1).setCellValue(
								rs.getObject(i1 + 1).toString());
						 Cell cell1 = rowhead1.getCell(i1);
						 if(i%2==0)
						 {
							 cell1.setCellStyle(style);
						 }
						 else {
							// cell1.setCellStyle(style2);
						}
						
					}
					k++;
				}
			}

			db.closeConnection();
		} catch (SQLException ex) {

		}
	}

	public void OPenFileWindows(String path) {

		try {

			File f = new File(path);
			if (f.exists()) {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(f);
				} else {
					System.out.println("File does not exists!");
				}
			}
		} catch (Exception ert) {
		}
	}

}