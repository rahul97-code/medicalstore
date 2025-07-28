package hms.patient.slippdf;

import hms.admin.gui.CancelledDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.opd.database.OPDDBConnection;


import hms.store.database.BillingDBConnection;
import hms.store.database.CancelBillDBConnection;
import hms.store.database.StoreAccountDBConnection;
import hms.store.gui.StoreMain;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class HighRiskItemPDF {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 8);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 2);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 15,
			Font.BOLD, BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 9.5f,
			Font.BOLD);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.BLACK);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 11,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "opdslip1.pdf";
	public static String serverFile = "opdslip1.pdf";
	Object Rows_Object_Array[][];
	int NumberOfColumns = 0, NumberOfRows = 0;
	Vector<String> doctorname = new Vector<String>();
	Vector<String> opdtypes = new Vector<String>();
	Vector<String> exams = new Vector<String>();
	Vector<String> examsSubCat = new Vector<String>();

	Vector<String> achievements = new Vector<String>();
	Vector<String> specialization = new Vector<String>();
	Vector<String> U_id= new Vector<String>();
	Vector<String> U_name = new Vector<String>();
	Vector<String> miscType = new Vector<String>();
	String opd_no, patient_id, patient_name, patient_age, doctor_name,
			amt_received, date, token_no;
	static String OS;
	String mainDir = "", str = "";
	String userName = "";
	Font font;
	
	double totalAmount = 0, cancelledAmount = 0,cashAmount=0,onlineAmount=0;
	float[] opdTablCellWidth = { 2.0f, 0.7f, 1.1f };

	public static void main(String[] arg) {
		try {
			new HighRiskItemPDF("2021-08-23", "2021-08-25");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HighRiskItemPDF(String dateFrom, String dateTo)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH_mm_ss");
	str = "SummeryReport" + DateFormatChange.StringToMysqlDate(new Date())+"_"+sdf.format(cal.getTime()) ;
//		readFile();
//		str = "SummeryReport" + DateFormatChange.StringToMysqlDate(new Date());
		System.out.print(str);
		
		
		getAllinsurance();

		Document document = new Document();
		OS = System.getProperty("os.name").toLowerCase();
		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		HeaderAndFooter event = new HeaderAndFooter();
		wr.setPageEvent(event);
		document.setPageSize(PageSize.A4);
		document.setMargins(0,0, 50,80);
		document.open();
		BaseFont base = BaseFont.createFont("indian.ttf", BaseFont.WINANSI,
				BaseFont.EMBEDDED);
		font = new Font(base, 11f, Font.BOLD);

		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(90);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		float[] tiltelTablCellWidth = { 0.1f, 1f, 0.1f };
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);

		java.net.URL imgURL = HighRiskItemPDF.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = HighRiskItemPDF.class
				.getResource("/icons/Rotary-Club-logo.jpg");
		Image imageRotaryClub = Image.getInstance(imgURLRotaryClub);

		// imageRotaryClub.scalePercent(60);
		// imageRotaryClub.setAbsolutePosition(40, 750);

		PdfPCell logocell2 = new PdfPCell(imageRotaryClub);
		logocell2.setRowspan(3);
		logocell2.setBorder(Rectangle.NO_BORDER);
		logocell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		logocell2.setPaddingRight(5);
		TitleTable.addCell(logocell2);

		PdfPCell namecell = new PdfPCell(new Phrase(
				"DR. JAI DEV MEMORIAL ROTARY MEDICAL STORE" + "\n", font1));
		namecell.setHorizontalAlignment(Element.ALIGN_CENTER);
		namecell.setPaddingBottom(5);
		namecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		TitleTable.addCell(namecell);

		PdfPCell logocell = new PdfPCell(image);
		logocell.setRowspan(3);
		logocell.setBorder(Rectangle.NO_BORDER);
		logocell.setHorizontalAlignment(Element.ALIGN_CENTER);
		logocell.setPaddingLeft(3);
		TitleTable.addCell(logocell);
		PdfPCell addressCell = new PdfPCell(new Phrase(
				"GROUND FLOOR ROTARY AMBALA CANCER & GENERAL HOSPITAL AMBALA CANTT-133001 (Haryana)",
				font2));
		addressCell.setPaddingBottom(2);
		addressCell.setBorder(Rectangle.NO_BORDER);
		addressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell);

		PdfPCell addressCell2 = new PdfPCell(
				new Phrase(
						"Mobile No. : 09034056793",
						font2));
		addressCell2.setPaddingBottom(1);
		addressCell2.setBorder(Rectangle.NO_BORDER);
		addressCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell2);

		table.addCell(TitleTable);
//		table.addCell(new Phrase("Daily Cash Collection Report ", font3));
		String timeStamp = new SimpleDateFormat(
				"EEEEEE, d MMMMMM, YYYY        hh:mm:ss a").format(Calendar
				.getInstance().getTime());
		System.out.println(timeStamp);
		table.addCell(new Phrase("Date : From     "
				+ DateFormatChange.StringToDateFormat(dateFrom)
				+ "     to     " + DateFormatChange.StringToDateFormat(dateTo),
				font3));
		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		//OPDDBConnection db = new OPDDBConnection();
		table.addCell(new Phrase("High Risk Item Report ",font1));
		table.addCell(new Phrase(" "));
		table.addCell(opdTable);
		userName = StoreMain.userName;
		for (int i = 0; i < U_id.size(); i++) {

			PdfPCell header = new PdfPCell(new Phrase(""
					+ U_name.get(i), font3));
			header.setBorder(Rectangle.NO_BORDER);
			header.setPaddingBottom(3);
			header.setHorizontalAlignment(Element.ALIGN_CENTER);
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(header);
			
			table.addCell(opdAmount(dateFrom, dateTo,U_id.get(i)));
			
			cashAmount=Math.round(cashAmount*100.000)/100.000;
			PdfPCell footer3 = new PdfPCell(new Phrase("Total Amount : `"
					+ cashAmount, font));
			footer3.setBorder(Rectangle.NO_BORDER);
			footer3.setPaddingBottom(5);
			footer3.setHorizontalAlignment(Element.ALIGN_RIGHT);
	;
			table.addCell(footer3);
			table.addCell(new Phrase(" "
					, font3));
			table.addCell(new Phrase(" "
					, font3));
			
		}

	
		document.add(table);
		document.close();

       new OpenFile(RESULT);
	}

	public PdfPTable opdAmount(String dateFrom, String dateTo,String id) {

		cashAmount=0;
		float[] opdTablCellWidth = { 1.0f, 2.0f, 1.1f ,1.0f};
		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		opdTable.addCell(new Phrase("Item ID", font3));
		opdTable.addCell(new Phrase("Item Name", font3));
		opdTable.addCell(new Phrase("Quantity", font3));
		opdTable.addCell(new Phrase("Price", font3));
	
		
		try {
			BillingDBConnection db = new BillingDBConnection();

			
				ResultSet rs = db.retrieveAllSummeryHighRiskItem(dateFrom, dateTo,id);
//				ResultSet rs1 = db.retrieveAllSummeryUserWiseNewBillsCount(dateFrom, dateTo, ""
//						+ type,userName);
				while (rs.next()) {
					opdTable.addCell(new Phrase("" + rs.getObject(1).toString()
							, font3));
					opdTable.addCell(new Phrase("" + rs.getObject(2).toString()
							, font3));
					opdTable.addCell(new Phrase("" + rs.getObject(3).toString()
							, font3));
					double a=Math.round(Double.parseDouble(rs.getObject(4).toString())*100.000)/100.000;
					opdTable.addCell(new Phrase("" + a
							, font3));
			
					cashAmount = rs.getDouble(4)  + cashAmount;
					
				}

				

			db.closeConnection();
		} catch (SQLException ex) {
		
			System.out.print("error"+ex);
		}
	
		
		return opdTable;
	}

	public void getAllinsurance() {

		StoreAccountDBConnection dbConnection = new StoreAccountDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				U_id.add(resultSet.getObject(1).toString());
				U_name.add(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	
	}
	

	
		
}