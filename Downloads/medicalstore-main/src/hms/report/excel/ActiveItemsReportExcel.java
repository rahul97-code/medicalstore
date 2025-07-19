package hms.report.excel;

import hms.admin.gui.ActiveItemsStatusReport;
import hms.main.DateFormatChange;
import hms.store.database.BillingDBConnection;
import hms.store.database.InvoiceDBConnection;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;

public class ActiveItemsReportExcel {

	
	Object Rows_Object_Array[][];
	int NumberOfColumns = 0, NumberOfRows = 0;

	static String OS;
	String mainDir = "", str = "";
	Font font;
	double totalAmount = 0, cancelledAmount = 0;
	float[] opdTablCellWidth = { 2.0f, 0.7f, 1.1f };
	String fromDateSTR,toDateSTR;
	
	public ActiveItemsReportExcel(String fromDate,String toDate,String fileName) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		fromDateSTR=fromDate;
		toDateSTR=toDate;
		
		try {
			
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Active Items List");
			doctorWiseReport( sheet);

			FileOutputStream fileOut = new FileOutputStream(fileName);
			workbook.write(fileOut);
			fileOut.close();
			JOptionPane
					.showMessageDialog(
							null,
							"Excel File Generated Successfully",
							"Data Saved", JOptionPane.INFORMATION_MESSAGE);
			OPenFileWindows(fileName);
		} catch (Exception ex) {
			Logger.getLogger(ActiveItemsStatusReport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void doctorWiseReport( HSSFSheet sheet) {
		
		int k = 1;
		try {
			BillingDBConnection db = new BillingDBConnection();
			
			ResultSet rs = db.retrieveItemsStatus(fromDateSTR, toDateSTR);
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			HSSFRow rowhead = sheet.createRow((short) 0);
			for (int i = 0; i < columnsNumber; i++) {
				rowhead.createCell(i).setCellValue(rsmd.getColumnName(i+1));
			}
				while (rs.next()) {
					HSSFRow rowhead1 = sheet.createRow((short) k);
					for (int i = 0; i < columnsNumber; i++) {
						rowhead1.createCell(i).setCellValue(rs.getObject(i+1).toString());
					}
					k++;
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