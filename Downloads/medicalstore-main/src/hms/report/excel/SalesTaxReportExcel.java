package hms.report.excel;

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

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;

public class SalesTaxReportExcel {

	
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
			new SalesTaxReportExcel("2013-03-24", "2015-08-24");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SalesTaxReportExcel(String fromDate,String toDate) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		fromDateSTR=fromDate;
		toDateSTR=toDate;
		
		try {
			
			JFrame parentFrame = new JFrame();
			 JFileChooser c = new JFileChooser();
			 c.setDialogTitle("Specify a file to save");   
			 File fileToSave = null;
			 int userSelection = c.showSaveDialog(parentFrame);
			 if (userSelection == JFileChooser.APPROVE_OPTION) {
				     fileToSave = c.getSelectedFile();
				    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
				}
//			 int i=c.showSaveDialog(this);  
//			 int rVal = c.showSaveDialog(sa.this);
//		      if (rVal == JFileChooser.APPROVE_OPTION) {
//		        filename.setText(c.getSelectedFile().getName());
//		        dir.setText(c.getCurrentDirectory().toString());
//		      }
//		      if (rVal == JFileChooser.CANCEL_OPTION) {
//		        filename.setText("You pressed cancel");
//		        dir.setText("");
//		      }
//		    }
			File desktop = new File(System.getProperty("user.home"), "Desktop");
			String filename = fileToSave.getAbsolutePath() + "_" + "Sales_Tax_Reports" + "-"
					+ fromDate +" TO "+toDate+ ".xls";
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Sales_Tax_Reports");
			doctorWiseReport( sheet);

			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
//			JOptionPane
//					.showMessageDialog(
//							null,
//							"Excel File Generated Successfully on your Desktop with Sales_Tax_Reports" + "-"
//					+ fromDate +" TO "+toDate+ ".xls"+" Name",
//							"Data Saved", JOptionPane.INFORMATION_MESSAGE);
			OPenFileWindows(filename);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void doctorWiseReport( HSSFSheet sheet) {
		
		int k = 1;
		try {
			BillingDBConnection db = new BillingDBConnection();
			ResultSet rs = db.retrieveSalesTaxReport(fromDateSTR,toDateSTR);
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