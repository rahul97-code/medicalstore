package hms.report.excel;

import hms.main.DateFormatChange;
import hms.store.database.BillingDBConnection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;

public class PurchaseOrderExcel {

	
	Object Rows_Object_Array[][];
	int NumberOfColumns = 0, NumberOfRows = 0;
	Vector<String> doctorname = new Vector<String>();
	Vector<String> ipdID = new Vector<String>();
	Vector<String> opdtypes = new Vector<String>();
	Vector<String> exams = new Vector<String>();
	Vector<String> examsSubCat = new Vector<String>();
	Vector<String> achievements = new Vector<String>();
	Vector<String> specialization = new Vector<String>();
	Vector<String> reportCategory = new Vector<String>();
	Vector<String> miscType = new Vector<String>();
	static String OS;
	String mainDir = "", str = "", DoctorName = "Dr. Karan Sobti";
	Font font;
	double totalAmount = 0, cancelledAmount = 0;
	float[] opdTablCellWidth = { 2.0f, 0.7f, 1.1f };
	String fromDateSTR1,toDateSTR1;
	String fromDateSTR3,toDateSTR3;
	public static void main(String[] arg) {
		try {
			new PurchaseOrderExcel();
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public PurchaseOrderExcel() throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		
		fromDateSTR1=DateFormatChange.StringToMysqlDate(DateFormatChange.addMonths(new Date(),-1));
		toDateSTR1=DateFormatChange.StringToMysqlDate(new Date());
		fromDateSTR3=DateFormatChange.StringToMysqlDate(DateFormatChange.addMonths(new Date(),-3));
		toDateSTR3=DateFormatChange.StringToMysqlDate(new Date());
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
							"Excel File Generated Successfully on your Desktop with IssuedRegister -"
									+ DateFormatChange.StringToMysqlDate(new Date())+" Name",
							"Data Saved", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void issuedRegister( HSSFSheet sheet) {
		
		HSSFRow rowhead0 = sheet.createRow((short) 0);
		
			rowhead0.createCell(0).setCellValue("Purchase Order");
			rowhead0.createCell(1).setCellValue("");
			rowhead0.createCell(2).setCellValue("-----");
			rowhead0.createCell(3).setCellValue("Generated Date");
			rowhead0.createCell(4).setCellValue(""+toDateSTR1);
		int k = 2;
		try {
			BillingDBConnection db = new BillingDBConnection();
			ResultSet rs = db.retrieveAllIssuedPillsNew(fromDateSTR3,toDateSTR3,fromDateSTR1,toDateSTR1);
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			HSSFRow rowhead = sheet.createRow((short) 1);
			for (int i = 0; i < columnsNumber; i++) {
				rowhead.createCell(i).setCellValue(rsmd.getColumnName(i+1));
			}
				while (rs.next()) {
					if(Double.parseDouble(rs.getObject(5).toString())>0)
					{
						HSSFRow rowhead1 = sheet.createRow((short) k);
						for (int i = 0; i < columnsNumber; i++) {
							rowhead1.createCell(i).setCellValue(rs.getObject(i+1).toString());
						}
						k++;
					}
					
				}
			db.closeConnection();
		} catch (SQLException ex) {
			System.out.print(""+ex);
		}
	}


}