package hms.patient.slippdf;

import hms.admin.gui.CancelledDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.opd.database.OPDDBConnection;
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

public class NewSummaryReportPDF {

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
	Vector<String> reportCategory = new Vector<String>();
	Vector<String> userName = new Vector<String>();
	Vector<String> miscType = new Vector<String>();
	String opd_no, patient_id, patient_name, patient_age, doctor_name,
	amt_received, date, token_no;
	static String OS;
	String mainDir = "", str = "";
	Font font;
	double totalAmount = 0, cancelledAmount = 0,cashAmount=0,onlineAmount=0,Cancelamount=0;
	float[] opdTablCellWidth = { 2.0f, 0.7f, 1.1f };

	public static void main(String[] arg) {
		try {
			new NewSummaryReportPDF("2024-03-17", "2024-03-17");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public NewSummaryReportPDF(String dateFrom, String dateTo)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH_mm_ss");
		System.out.println( sdf.format(cal.getTime()) );
		readFile();
		str = "SummeryReport" + DateFormatChange.StringToMysqlDate(new Date())+"_"+sdf.format(cal.getTime()) ;
		//		readFile();
		//		str = "SummeryReport" + DateFormatChange.StringToMysqlDate(new Date());
		System.out.print(str);
		makeDirectory(str);

		getAllinsurance();
		getUserName(dateFrom,dateTo);
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
		font = new Font(base, 13f, Font.BOLD);

		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(90);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		float[] tiltelTablCellWidth = { 0.1f, 1f, 0.1f };
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);

		java.net.URL imgURL = NewSummaryReportPDF.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = NewSummaryReportPDF.class
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

		table.addCell(new Phrase("Daily Cash Collection Report ", font3));
		String timeStamp = new SimpleDateFormat(
				"EEEEEE, d MMMMMM, YYYY        hh:mm:ss a").format(Calendar
						.getInstance().getTime());
		System.out.println(timeStamp);
		table.addCell(new Phrase("Date : From     "
				+ DateFormatChange.StringToDateFormat(dateFrom)
				+ "     to     " + DateFormatChange.StringToDateFormat(dateTo),
				font3));
		table.addCell(new Phrase("As on : " + timeStamp, font3));

		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		OPDDBConnection db = new OPDDBConnection();

		opdTable.addCell(new Phrase("Services", font3));
		opdTable.addCell(new Phrase("No.", font3));

		PdfPCell amount = new PdfPCell(new Phrase("Amount", font3));
		amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		opdTable.addCell(amount);

		table.addCell(opdTable);

		for (int i = 0; i < reportCategory.size(); i++) {

			PdfPCell header = new PdfPCell(new Phrase(""
					+ reportCategory.get(i), font3));
			header.setBorder(Rectangle.NO_BORDER);
			header.setPaddingBottom(3);
			header.setHorizontalAlignment(Element.ALIGN_CENTER);
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);

			table.addCell(header);
			if(reportCategory.get(i).toString().equals("OPD Unknown"))
			{

				table.addCell(opdAmount(dateFrom, dateTo,"Unknown","Cash","OPD"));
			}
			else if(reportCategory.get(i).toString().equals("IPD Unknown")) {
				table.addCell(opdAmount(dateFrom, dateTo,"Unknown","NA","IPD"));	
			}

			else if(reportCategory.get(i).toString().equals("Rotary")) {
				table.addCell(opdAmount(dateFrom, dateTo,reportCategory.get(i),"CAsh","OPD"));
			}

			else {
				table.addCell(opdAmount(dateFrom, dateTo,reportCategory.get(i),"NA","IPD"));
			}

			table.addCell(new Phrase(" "
					, font3));
			table.addCell(new Phrase(" "
					, font3));
		}
		BillingDBConnection db1 = new BillingDBConnection();
		ResultSet rs = db1.retrieveAllSummeryUserWiseNew(dateFrom, dateTo, "Unknown","","Cash","OPD");

		//			
		try {
			while (rs.next()) {

				try {
					cashAmount=rs.getDouble(2)  +cashAmount;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//CashTotal=rs.getDouble(2)  +CashTotal;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		totalAmount = totalAmount - cancelledAmount;
		PdfPCell footer2 = new PdfPCell(new Phrase("Cash Amount : `"
				+ cashAmount, font));
		footer2.setBorder(Rectangle.NO_BORDER);
		footer2.setPaddingBottom(5);
		footer2.setHorizontalAlignment(Element.ALIGN_RIGHT);
		totalAmount = totalAmount - cancelledAmount;
		PdfPCell footer5 = new PdfPCell(new Phrase("Cancel Amount : `"
				+ Cancelamount, font));
		footer5.setBorder(Rectangle.NO_BORDER);
		footer5.setPaddingBottom(5);
		footer5.setHorizontalAlignment(Element.ALIGN_RIGHT);
		// footer2.setBackgroundColor(BaseColor.LIGHT_GRAY);

		table.addCell(footer2);
		table.addCell(footer5);
		PdfPCell footer3 = new PdfPCell(new Phrase("Online Amount : `"
				+ onlineAmount, font));
		footer3.setBorder(Rectangle.NO_BORDER);
		footer3.setPaddingBottom(5);
		footer3.setHorizontalAlignment(Element.ALIGN_RIGHT);
		// footer2.setBackgroundColor(BaseColor.LIGHT_GRAY);

		table.addCell(footer3);

		//		PdfPCell footer4 = new PdfPCell(new Phrase("Grand Total Amount : `"
		//				+ totalAmount, font));
		//		footer4.setBorder(Rectangle.NO_BORDER);
		//		footer4.setPaddingBottom(5);
		//		footer4.setHorizontalAlignment(Element.ALIGN_RIGHT);
		//		// footer2.setBackgroundColor(BaseColor.LIGHT_GRAY);
		//
		//		table.addCell(footer4);



		//table.addCell(footer4);
		table.addCell(new Phrase(" ", font3));
		table.addCell(new Phrase(" ", font3));
		table.addCell(new Phrase("User Wise Cash Collection ", font3));
		table.addCell(new Phrase(" ", font3));
		for (int i = 0; i < userName.size(); i++) {

			PdfPCell header = new PdfPCell(new Phrase(""
					+ userName.get(i), font3));
			header.setBorder(Rectangle.NO_BORDER);
			header.setPaddingBottom(3);
			header.setHorizontalAlignment(Element.ALIGN_LEFT);
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);

			table.addCell(header);

			table.addCell(opdAmountNewUserwise(dateFrom, dateTo,userName.get(i)));
			table.addCell(new Phrase(" "
					, font3));
			table.addCell(new Phrase(" "
					, font3));
		}

		document.add(table);
		document.close();
		int patient_code=10000002;
		if(patient_code>=9999999)
		{
			patient_code=patient_code%9999999;
		}
		System.out.println(patient_code+"");
		StringBuffer ss = new StringBuffer(patient_code + "");
		while (ss.length() < 7) {
			ss.insert(0, '0');
		}
		System.out.println(ss+"");
		if (isWindows()) {
			OPenFileWindows("DailySlip/" + str + ".pdf");
			System.out.println("This is Windows");
		} else if (isMac()) {
			System.out.println("This is Mac");
		} else if (isUnix()) {
			if (System.getProperty("os.version").equals("3.11.0-12-generic")) {
				Run(new String[] { "/bin/bash", "-c",
						"exo-open DailySlip/" + str + ".pdf" });
			} else {
				Run(new String[] { "/bin/bash", "-c",
						"xdg-open DailySlip/" + str + ".pdf" });
			}
			System.out.println("This is Unix or Linux");
		} else if (isSolaris()) {
			System.out.println("This is Solaris");
		} else {
			System.out.println("Your OS is not support!!");
		}
		//		try {
		//			//copyFileUsingJava7Files("DailySlip/" + str + ".pdf", serverFile);
		//		} catch (IOException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
	}
	public PdfPTable opdAmountNewUserwise(String dateFrom, String dateTo,String username) {

		double headdTotal=0;
		double OnlineTotal=0;
		double CashTotal=0;
		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		try {
			BillingDBConnection db = new BillingDBConnection();


			ResultSet rs = db.retrieveAllSummeryUserwiseinAdmin(dateFrom, dateTo, ""
					+ username,"Cash");

			while (rs.next()) {

				opdTable.addCell(new Phrase("Bills", font3));
				opdTable.addCell(new Phrase("" + rs.getObject(1).toString()
						, font3));

				PdfPCell amount = new PdfPCell(new Phrase(""
						+ rs.getDouble(2), font3));
				amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(amount);
				//totalAmount = rs.getDouble(2)  + totalAmount;
				headdTotal=rs.getDouble(2)  +headdTotal;
				CashTotal=rs.getDouble(2)  +CashTotal;
			}



			db.closeConnection();
		} catch (SQLException ex) {

			System.out.print("error"+ex);
		}
		try {
			BillingDBConnection db = new BillingDBConnection();


			ResultSet rs = db.retrieveAllSummeryUserwiseinAdmin(dateFrom, dateTo, ""
					+ username,"Online");
			//				ResultSet rs1 = db.retrieveAllSummeryUserWiseNewBillsCount(dateFrom, dateTo, ""
			//						+ type,userName);
			while (rs.next()) {

				opdTable.addCell(new Phrase("Online Bills", font3));
				opdTable.addCell(new Phrase("" + rs.getObject(1).toString()
						, font3));

				PdfPCell amount = new PdfPCell(new Phrase(""
						+ rs.getDouble(2), font3));
				amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(amount);
				//totalAmount = rs.getDouble(2)  + totalAmount;
				headdTotal=rs.getDouble(2)  +headdTotal;
				OnlineTotal=rs.getDouble(2)  +OnlineTotal;
			}



			db.closeConnection();
		} catch (SQLException ex) {

			System.out.print("error"+ex);
		}
		try {
			BillingDBConnection db = new BillingDBConnection();
			ResultSet rs = db.retrieveAllSummeryUserwiseinAdmin(dateFrom, dateTo, ""
					+ username,"IPD");
			while (rs.next()) {

				opdTable.addCell(new Phrase("IPD Bills", font3));
				opdTable.addCell(new Phrase("" + rs.getObject(1).toString()
						, font3));
				PdfPCell amount = new PdfPCell(new Phrase(""
						+ rs.getDouble(2), font3));
				amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(amount);

				//					headdTotal=headdTotal-rs.getDouble(2);
				//					CashTotal=CashTotal-rs.getDouble(2);
			}

			db.closeConnection();
		} catch (SQLException ex) {

			System.out.print("error"+ex);
		}
		try {
			CancelBillDBConnection db = new CancelBillDBConnection();
			ResultSet rs = db.retrieveAllSummeryAdmin(dateFrom, dateTo, ""
					+ username,"IPD");
			while (rs.next()) {

				opdTable.addCell(new Phrase("IPD Cancel Bills", font3));
				opdTable.addCell(new Phrase("" + rs.getObject(1).toString()
						, font3));
				PdfPCell amount = new PdfPCell(new Phrase(""
						+ rs.getDouble(2), font3));
				amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(amount);

				headdTotal=headdTotal-rs.getDouble(2);
				CashTotal=CashTotal-rs.getDouble(2);
			}

			db.closeConnection();
		} catch (SQLException ex) {

			System.out.print("error"+ex);
		}
		try {
			CancelBillDBConnection db = new CancelBillDBConnection();
			ResultSet rs = db.retrieveAllSummeryAdmin(dateFrom, dateTo, ""
					+ username,"OPD");
			while (rs.next()) {

				opdTable.addCell(new Phrase("OPD Cancel Bills", font3));
				opdTable.addCell(new Phrase("" + rs.getObject(1).toString()
						, font3));
				PdfPCell amount = new PdfPCell(new Phrase(""
						+ rs.getDouble(2), font3));
				amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(amount);

				headdTotal=headdTotal-rs.getDouble(2);
				CashTotal=CashTotal-rs.getDouble(2);
			}

			db.closeConnection();
		} catch (SQLException ex) {

			System.out.print("error"+ex);
		}
		totalAmount = headdTotal  + totalAmount;
		cashAmount=CashTotal+cashAmount;
		onlineAmount=OnlineTotal+onlineAmount;
		//		PdfPCell amount = new PdfPCell(new Phrase("Head Amount : `"
		//				+ headdTotal, font));
		//		amount.setColspan(3);
		//		amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		//		opdTable.addCell(amount);
		return opdTable;
	}
	public PdfPTable opdAmount(String dateFrom, String dateTo,String type,String bill_type,String t) {

		double headdTotal=0;
		double CashTotal=0;
		double OnlineTotal=0;
		double headdTotal1=0;
		double cancelTotal=0;
		double cashTotal1=0;
		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		try {
			BillingDBConnection db = new BillingDBConnection();


			ResultSet rs = db.retrieveAllSummerycash(dateFrom, dateTo, ""
					+ type,bill_type,t);

			while (rs.next()) {

				opdTable.addCell(new Phrase("Bills", font3));
				opdTable.addCell(new Phrase("" + rs.getObject(1).toString()
						, font3));

				PdfPCell amount = new PdfPCell(new Phrase(""
						+ rs.getDouble(2), font3));
				amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(amount);
				//totalAmount = rs.getDouble(2)  + totalAmount;
				headdTotal=rs.getDouble(2)  +headdTotal;
				CashTotal=rs.getDouble(2)  +CashTotal;
			}


			db.closeConnection();
		} catch (SQLException ex) {

			System.out.print("error"+ex);
		}
		try {
			BillingDBConnection db = new BillingDBConnection();


			ResultSet rs = db.retrieveAllSummery(dateFrom, dateTo, ""
					+ type,"Online",t);
			//				ResultSet rs1 = db.retrieveAllSummeryUserWiseNewBillsCount(dateFrom, dateTo, ""
			//						+ type,userName);
			while (rs.next()) {

				opdTable.addCell(new Phrase("Online Bills", font3));
				opdTable.addCell(new Phrase("" + rs.getObject(1).toString()
						, font3));

				PdfPCell amount = new PdfPCell(new Phrase(""
						+ rs.getDouble(2), font3));
				amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(amount);
				//totalAmount = rs.getDouble(2)  + totalAmount;
				headdTotal=rs.getDouble(2)  +headdTotal;
				OnlineTotal=rs.getDouble(2)  +OnlineTotal;
			}



			db.closeConnection();
		} catch (SQLException ex) {

			System.out.print("error"+ex);
		}
		try {
			CancelBillDBConnection db = new CancelBillDBConnection();
			ResultSet rs = db.retrieveAllSummery(dateFrom, dateTo, ""
					+ type,bill_type,t);
			while (rs.next()) {

				opdTable.addCell(new Phrase("Cancel Items", font3));
				opdTable.addCell(new Phrase("" + rs.getObject(1).toString()
						, font3));
				PdfPCell amount = new PdfPCell(new Phrase(""
						+ rs.getDouble(2), font3));
				amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(amount);

				headdTotal1=headdTotal1+rs.getDouble(2);

				cancelTotal=cancelTotal+rs.getDouble(2);
			}

			db.closeConnection();
		} catch (SQLException ex) {

			System.out.print("error"+ex);
		}
		totalAmount = headdTotal  + totalAmount;
		cashAmount=cashTotal1+cashAmount;
		onlineAmount=OnlineTotal+onlineAmount;
		Cancelamount=cancelTotal+Cancelamount;
		PdfPCell amount = new PdfPCell(new Phrase("Head Amount : `"
				+ headdTotal, font));
		amount.setColspan(3);
		amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		PdfPCell amount1 = new PdfPCell(new Phrase("Cancelled Amount : `"
				+ headdTotal1, font));
		amount1.setColspan(3);
		amount1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		opdTable.addCell(amount);
		opdTable.addCell(amount1);
		return opdTable;
	}

	//	public PdfPTable opdAmount(String dateFrom, String dateTo,String type) {
	//
	//		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
	//		try {
	//			CancelBillDBConnection db = new CancelBillDBConnection();
	//			ResultSet rs = db.retrieveAllSummery(dateFrom, dateTo, ""
	//						+ type);
	//				while (rs.next()) {
	//					
	//					opdTable.addCell(new Phrase("" + type, font3));
	//					opdTable.addCell(new Phrase("" + rs.getObject(1).toString()
	//							, font3));
	//					PdfPCell amount = new PdfPCell(new Phrase(""
	//							+ rs.getDouble(2), font3));
	//					amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
	//					opdTable.addCell(amount);
	//					totalAmount = rs.getDouble(2)  + totalAmount;
	//				}
	//
	//			db.closeConnection();
	//		} catch (SQLException ex) {
	//		
	//			System.out.print("error"+ex);
	//		}
	//		return opdTable;
	//	}
	public void makeDirectory(String name) {
		try {
			SmbFile dir = new SmbFile(mainDir + "/HMS/DailySlip");
			if (!dir.exists())
				dir.mkdirs();

		} catch (SmbException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new File("DailySlip").mkdir();
		serverFile = mainDir + "/HMS/DailySlip/" + name + ".pdf";
		RESULT = "DailySlip/" + name + ".pdf";
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
	public void getAllinsurance() {
		reportCategory.add("OPD Unknown");
		reportCategory.add("IPD Unknown");
		InsuranceDBConnection dbConnection = new InsuranceDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				reportCategory.add(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();

	}
	public void getUserName(String dateFrom,String dateTo) {
		//		reportCategory.add("Unknown");
		BillingDBConnection dbConnection = new BillingDBConnection();
		ResultSet resultSet = dbConnection.retrieveUserName(dateFrom,dateTo);
		try {
			while (resultSet.next()) {
				userName.add(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();

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
}