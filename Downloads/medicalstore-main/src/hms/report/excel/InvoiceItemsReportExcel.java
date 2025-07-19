package hms.report.excel;

import hms.main.DateFormatChange;
import hms.store.database.InvoiceDBConnection;

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

public class InvoiceItemsReportExcel {

	String fromDateSTR,toDateSTR;
	public static void main(String[] arg) {
		try {
			new InvoiceItemsReportExcel("2013-03-24", "2015-04-24");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public InvoiceItemsReportExcel(String fromDate,String toDate) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		fromDateSTR=fromDate;
		toDateSTR=toDate;
		
		try {
			File desktop = new File(System.getProperty("user.home"), "Desktop");
			String filename = desktop + "/" + "InvoiceItemsSummery" + "-"
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
							"Excel File Generated Successfully on your Desktop with InvoiceItemsSummery -"
									+ DateFormatChange.StringToMysqlDate(new Date())+" Name",
							"Data Saved", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void issuedRegister( HSSFSheet sheet) {
		
		int k = 1;
		try {

			InvoiceDBConnection db = new InvoiceDBConnection();
			ResultSet rs = db.retrieveAllItemsData(fromDateSTR,toDateSTR);
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
//			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
//					ex);
		}
	}


}