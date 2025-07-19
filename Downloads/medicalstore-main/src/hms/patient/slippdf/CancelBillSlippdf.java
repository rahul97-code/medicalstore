package hms.patient.slippdf;

import hms.Printer.PrintFile;
import hms.admin.gui.StoreInfoDBConnection;
import hms.main.DateFormatChange;
import hms.main.NumberToWordConverter;
import hms.store.database.BillingDBConnection;
import hms.store.database.CancelBillDBConnection;

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
import java.util.Date;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class CancelBillSlippdf {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 9,Font.BOLD);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 2);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 17,
			Font.BOLD, BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 8.5f,
			Font.BOLD);
	private static Font notefont = new Font(Font.FontFamily.HELVETICA, 8.5f,
			Font.ITALIC);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.BLACK);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 11,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "opdslip1.pdf";
	public static String serverFile = "opdslip1.pdf";
	double totalCharges = 0, ipd_advance = 0, ipd_balance = 0,BilltotalAmount=0,
			payableAmount = 0,totalAmount=0;
	String  patient_name, patient_mobile, doctor_name,
			amt_received, date, bill_no,time,insurance_type,payable,remarks="";
	String ipd_doctor_id = "", ipd_doctorname = "", ipd_date = "",
			ipd_time = "", ipd_date_dis = "", ipd_time_dis = "", ipd_note = "",
			ipd_id = "", ward_name = "", building_name = "",
			bed_no = "Select Bed No", ward_incharge = "", ward_room = "",
			ward_code = "", descriptionSTR = "", charges = "", ipd_days,
			ipd_hours, ipd_minutes, ipd_expenses_id,licNoSTR="",tinNoSTR="";
	static String OS;
	String mainDir = "";
	Font font;
	float[] TablCellWidth = {  0.24f,1.0f,0.7f,0.5f,0.5f,0.5f,0.5f,0.5f };
	String[] open = new String[4];
	private String username="";
	private String CancelDateTime="";
	private String CanceluserName="";
  
	public static void main(String[] argh) {
		try {
			new CancelBillSlippdf("521291","CANCEL");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public CancelBillSlippdf(String medical_bill, String status)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		bill_no=medical_bill;
		
		readFile();
		makeDirectory(bill_no);
		getBillData(bill_no);
		getInfo();
		Document document = new Document();
		OS = System.getProperty("os.name").toLowerCase();
		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		wr.setBoxSize("art", new Rectangle(36, 54, 559, 788));
		document.setPageSize(PageSize.LETTER);
		document.setMargins(0, 0, 10, 0);
		document.open();
		BaseFont base = BaseFont.createFont("indian.ttf", BaseFont.WINANSI,
				BaseFont.EMBEDDED);
		font = new Font(base, 8f);

		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(90);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		float[] tiltelTablCellWidth = { 0.1f, 1f, 0.1f };
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);

		java.net.URL imgURL = CancelBillSlippdf.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = CancelBillSlippdf.class
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
		addressCell2.setPaddingBottom(5);
		addressCell2.setBorder(Rectangle.NO_BORDER);
		addressCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell2);

		table.addCell(TitleTable);

		
		PdfPCell spaceCell = new PdfPCell(new Paragraph(" Cancel Bill Reciept ", font4));
		spaceCell.setBorder(Rectangle.NO_BORDER);
		spaceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		spaceCell.setPaddingTop(1);
		spaceCell.setPaddingBottom(5);
		table.addCell(spaceCell);
		//
		float[] opdTablCellWidth = { 0.5f, 1f, 0.5f, 1f };
		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		opdTable.getDefaultCell().setBorder(0);

		PdfPCell tokencell = new PdfPCell(new Phrase("Bill No. :" + "\n", font3));
		tokencell.setBorder(Rectangle.NO_BORDER);

		PdfPCell tokenNocell = new PdfPCell(new Phrase(bill_no + "\n", font3));
		tokenNocell.setBorder(Rectangle.NO_BORDER);

		opdTable.addCell(tokencell);
		opdTable.addCell(tokenNocell);
		
		opdTable.addCell(new Phrase("Drug Lic. No. : ", font3));
		opdTable.addCell(new Phrase("" + licNoSTR, font3));



		opdTable.addCell(new Phrase("Patient Name : ", font3));
		opdTable.addCell(new Phrase("" + patient_name, font3));

		opdTable.addCell(new Phrase("TIN No. : ", font3));
		opdTable.addCell(new Phrase("" + tinNoSTR, font3));
		
		
		opdTable.addCell(new Phrase("Contact No. :", font3));
		PdfPCell ContactCell = new PdfPCell(new Phrase("" + patient_mobile,
				font3));
		ContactCell.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(ContactCell);
		
		opdTable.addCell(new Phrase("Bill Date : ", font3));
		opdTable.addCell(new Phrase("" + DateFormatChange.StringToDateFormat(date)+" "+time, font3));

		opdTable.addCell(new Phrase("Doctor Name : ", font3));
		opdTable.addCell(new Phrase("" + doctor_name, font3));
		
		

//		opdTable.addCell(new Phrase("Time : ", font3));
//		PdfPCell timeCell = new PdfPCell(new Phrase(""+time, font3));
//		timeCell.setBorder(Rectangle.NO_BORDER);
//		opdTable.addCell(timeCell);
		getUserDATA(bill_no,status);
		
		opdTable.addCell(new Phrase("Generated by : ", font3));
		PdfPCell genrateby = new PdfPCell(new Phrase(""+username, font3));
		genrateby.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(genrateby);
		
		opdTable.addCell(new Phrase("Cancelled by : ", font3));
		PdfPCell cancelledby = new PdfPCell(new Phrase(""+CanceluserName, font3));
		cancelledby.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(cancelledby);
		
		opdTable.addCell(new Phrase("Cancelled Date : ", font3));
		PdfPCell cancelledDatetime = new PdfPCell(new Phrase(""+CancelDateTime, font3));
		cancelledDatetime.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(cancelledDatetime);


		PdfPCell opdCell = new PdfPCell(opdTable);
		opdCell.setBorderWidth(0.8f);
		opdCell.setPaddingBottom(2);
		table.addCell(opdCell);

		table.addCell(new Phrase("  ", font3));
		table.addCell(billItems(bill_no,status));
		
		totalAmount=Math.round(totalAmount*100.000)/100.000;
		PdfPCell total = new PdfPCell(new Paragraph("Total :     "+totalAmount, smallBold));
		total.setBorder(Rectangle.NO_BORDER);
		total.setHorizontalAlignment(Element.ALIGN_RIGHT);
		total.setPaddingTop(10);
		total.setPaddingBottom(1);
		table.addCell(total);
		
		
		
		PdfPCell payableInWords = new PdfPCell(new Paragraph("Remarks : "+remarks+".", font3));
		payableInWords.setBorder(Rectangle.NO_BORDER);
		payableInWords.setHorizontalAlignment(Element.ALIGN_LEFT);
		payableInWords.setPaddingTop(5);
		payableInWords.setPaddingBottom(10);
		table.addCell(payableInWords);
		
		PdfPCell note = new PdfPCell(new Paragraph("Restocking fee is applicable", notefont));
		note.setBorder(Rectangle.NO_BORDER);
		note.setHorizontalAlignment(Element.ALIGN_LEFT);
		note.setPaddingTop(0);
		note.setPaddingBottom(2);
		table.addCell(note);
		
//
//		PdfPCell footer2 = new PdfPCell(
//				new Phrase(
//						"Reciept Cancelled on "+DateFormatChange.StringToMysqlDate(new Date())+".",
//						font3));
//		footer2.setBorder(Rectangle.NO_BORDER);
//		footer2.setPaddingBottom(5);
//		footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
//		//footer2.setBackgroundColor(BaseColor.LIGHT_GRAY);
//
//		table.addCell(footer2);
	
		document.add(table);
		// onEndPage(wr,document);
		document.close();

		if (isWindows()) {
			OPenFileWindows("CancelSlip/" + bill_no + ".pdf");
			System.out.println("This is Windows");
		} else if (isMac()) {
			System.out.println("This is Mac");
		} else if (isUnix()) {
			if (System.getProperty("os.version").equals("3.11.0-12-generic")) {
				Run(new String[] { "/bin/bash", "-c",
						open[0] + " CancelSlip/" + bill_no + ".pdf" });
			} else {
				Run(new String[] { "/bin/bash", "-c",
						open[1] + " CancelSlip/" + bill_no + ".pdf" });
			}
			System.out.println("This is Unix or Linux");
		} else if (isSolaris()) {
			System.out.println("This is Solaris");
		} else {
			System.out.println("Your OS is not support!!");
		}
		
//		new PrintFile("CancelSlip/" + bill_no + ".pdf",3);
//		try {
//			copyFileUsingJava7Files("IPDSlip/" + ipd_no + ".pdf", serverFile);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	public void getUserDATA(String bill_no,String status) {
	CancelBillDBConnection db1 = new CancelBillDBConnection();
	ResultSet rs = db1.retrieveCancelBillItemsData(bill_no,status);
	int r = 1;
	try {
		while (rs.next()) {
			CancelDateTime=rs.getObject(9)+" "+rs.getObject(10);
			CanceluserName=rs.getObject(11).toString();
			break;
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	db1.closeConnection();
	}
	public PdfPTable billItems(String bill_no,String status) {

		PdfPTable Table = new PdfPTable(TablCellWidth);
		Table.addCell(new Phrase("S.No.", font3));
		Table.addCell(new Phrase("Item Name", font3));
		
		Table.addCell(new Phrase("Batch No.", font3));
		Table.addCell(new Phrase("Expiry", font3));
		Table.addCell(new Phrase("Date", font3));

		PdfPCell price = new PdfPCell(new Phrase("Unit Price", font3));
		price.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(price);
		PdfPCell quantity = new PdfPCell(new Phrase("Qnty.", font3));
		quantity.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(quantity);
		PdfPCell amount = new PdfPCell(new Phrase("Amount", font3));
		amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(amount);
		try {
			CancelBillDBConnection db1 = new CancelBillDBConnection();
			ResultSet rs = db1.retrieveCancelBillItemsData(bill_no,status);
			int r = 1;
			while (rs.next()) {
				double amount2 = Math.round(Double.parseDouble(rs.getObject(6).toString()) * 100.000) / 100.000;

				Table.addCell(new Phrase("" + r, font3));
				Table.addCell(new Phrase("" + rs.getObject(1).toString(), font3));
				Table.addCell(new Phrase("" + rs.getObject(2).toString(), font3));
				Table.addCell(new Phrase("" + DateFormatChange.StringToDateFormat(rs.getObject(7).toString()).toString(), font3));
				Table.addCell(new Phrase("" + rs.getObject(9).toString(), font3));//date
				double dUnitPrice= Math.round((amount2/rs.getInt(4)) * 100.000) / 100.000;
				PdfPCell price1 = new PdfPCell(new Phrase("" + dUnitPrice+"", font3));
				price1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				Table.addCell(price1);
				PdfPCell quantity1 = new PdfPCell(new Phrase("" + rs.getObject(4).toString(), font3));
				quantity1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				Table.addCell(quantity1);
				totalAmount=totalAmount+Double.parseDouble(rs.getObject(6).toString());
				PdfPCell amount1 = new PdfPCell(new Phrase("" + amount2, font3));
				amount1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				Table.addCell(amount1);
				remarks=rs.getObject(8).toString();
				r++;
			//	System.out.println(""+rs.getObject(3).toString());
			}
			db1.closeConnection();
		} catch (SQLException ex) {
			
		}
	
		return Table;
	}

	public void getBillData(String bill_no) {
		try {
			BillingDBConnection db = new BillingDBConnection();
			ResultSet rs = db.retrieveBillData(bill_no);
			while (rs.next()) {
				
				patient_name = rs.getObject(1).toString();
				patient_mobile = rs.getObject(2).toString();
				doctor_name = rs.getObject(3).toString();
				insurance_type = rs.getObject(4).toString();
//				payable = rs.getObject(5).toString();
				BilltotalAmount = Double.parseDouble(rs.getObject(6).toString());
//				payableAmount = Double.parseDouble(rs.getObject(7).toString());
				date =rs.getObject(8).toString();
				time = rs.getObject(9).toString();
				username=rs.getObject(12).toString();
				
			}
			db.closeConnection();
		} catch (SQLException ex) {
			
		}
		
	}

	public void getInfo() {

		StoreInfoDBConnection dbConnection=new StoreInfoDBConnection();
		ResultSet resultSet = dbConnection
				.retrieveAllData();
		
		try {
			while (resultSet.next()) {
				licNoSTR=resultSet.getObject(2).toString();
				tinNoSTR=resultSet.getObject(3).toString();
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
	public void makeDirectory(String bill_no) {

		try {
			SmbFile dir = new SmbFile(mainDir + "/HMS/CancelSlip");
			if (!dir.exists())
				dir.mkdirs();
			
		} catch (SmbException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new File("CancelSlip").mkdir();
		serverFile = mainDir + "/HMS/CancelSlip/" + bill_no + ".pdf";
		RESULT = "CancelSlip/" + bill_no + ".pdf";
	}

	private void copyFileUsingJava7Files(String source, String dest)
			throws IOException {
		SmbFile remoteFile = new SmbFile(dest);
		OutputStream os = remoteFile.getOutputStream();
		InputStream is = new FileInputStream(new File(source));

		int bufferSize = 5096;

		byte[] b = new byte[bufferSize];
		int noOfBytes = 0;
		while ((noOfBytes = is.read(b)) != -1) {
			os.write(b, 0, noOfBytes);
		}
		os.close();
		is.close();

	}

	public void readFile() {
		// The name of the file to open.
		String fileName = "data.mdi";

		// This will reference one line at a time
		String line = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String str = null;
			boolean fetch=true;
			while ((line = bufferedReader.readLine()) != null&&fetch) {
				// System.out.println(line);
				str = line;
				fetch=false;
			}
			String data[] = new String[22];
			int i = 0;
			for (String retval : str.split("@")) {
				data[i] = retval;
				i++;
			}
			mainDir = data[1];
			open[0] = data[2];
			open[1] = data[3];
			open[2] = data[4];
			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
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

	public void Run(String[] cmd) {
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			int processComplete = process.waitFor();
			if (processComplete == 0) {
				System.out.println("successfully");
			} else {
				System.out.println("Failed");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS
				.indexOf("aix") > 0);

	}

	public static boolean isSolaris() {

		return (OS.indexOf("sunos") >= 0);

	}

	public void onEndPage(PdfWriter writer, Document document) {
		Rectangle rect = writer.getBoxSize("art");
		switch (writer.getPageNumber() % 2) {
		case 0:
			ColumnText
					.showTextAligned(
							writer.getDirectContent(),
							Element.ALIGN_RIGHT,
							new Phrase(
									"Saturday : General OPD Closed, Sunday : Working, EMERGENCY : 24x7",
									font4), rect.getRight(), rect.getTop(), 0);
			break;
		case 1:
			ColumnText
					.showTextAligned(
							writer.getDirectContent(),
							Element.ALIGN_LEFT,
							new Phrase(
									"Saturday : General OPD Closed, Sunday : Working, EMERGENCY : 24x7",
									font4), rect.getLeft(), rect.getTop(), 0);
			break;
		}
		ColumnText.showTextAligned(writer.getDirectContent(),
				Element.ALIGN_CENTER, new Phrase(String.format("page %d", 1)),
				(rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18,
				0);
	}
}