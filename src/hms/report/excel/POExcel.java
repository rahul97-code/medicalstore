package hms.report.excel;

import hms.main.DateFormatChange;
import hms.store.database.PODBConnection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;

public class POExcel {

	
	Object Rows_Object_Array[][];
	int NumberOfColumns = 0, NumberOfRows = 0;
	
	static String OS;
	String mainDir = "", str = "";
	Font font;
	double totalAmount = 0, cancelledAmount = 0;
	float[] opdTablCellWidth = { 2.0f, 0.7f, 1.1f };
	String fromDateSTR,toDateSTR;
	public static void main(String[] arg) {
		try {
			new POExcel("2018-09-09","2018-10-10");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public POExcel(String fromDateSTR,String toDateSTR) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		
		this.fromDateSTR=fromDateSTR;
		this.toDateSTR=toDateSTR;
		try {
			File desktop = new File(System.getProperty("user.home"), "Desktop");
			String filename = desktop + "/" + "Purchase Order" + "-"
					+ DateFormatChange.StringToMysqlDate(new Date()) + ".xls";
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Item Rate List");
			issuedRegister( sheet);

			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			JOptionPane
					.showMessageDialog(
							null,
							"Excel File Generated Successfully on your Desktop with Purchase Order -"
									+ DateFormatChange.StringToMysqlDate(new Date())+" Name",
							"Data Saved", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void issuedRegister( HSSFSheet sheet) {
		
		HSSFRow rowhead0 = sheet.createRow((short) 0);
		
		int k = 0;
		try {
			PODBConnection db = new PODBConnection();
			ResultSet rs = db.retrievePOItemsData(fromDateSTR, toDateSTR);
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			HSSFRow rowhead = sheet.createRow((short) k);
			for (int i = 0; i < columnsNumber; i++) {
				rowhead.createCell(i).setCellValue(rsmd.getColumnName(i+1));
			}
			k++;
				while (rs.next()) {
				
						HSSFRow rowhead1 = sheet.createRow((short) k);
						for (int i = 0; i < columnsNumber; i++) {
							rowhead1.createCell(i).setCellValue(rs.getObject(i+1).toString());
						}
						k++;
					
				}
			db.closeConnection();
		} catch (SQLException ex) {
			System.out.print(""+ex);
		}
	}


}