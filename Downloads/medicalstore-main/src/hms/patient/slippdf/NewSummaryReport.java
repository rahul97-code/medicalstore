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
import java.util.HashMap;
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
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class NewSummaryReport {

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
	private static Font fontHeader = new Font(Font.FontFamily.HELVETICA, 11,
			Font.BOLD, BaseColor.BLACK);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 11,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "opdslip1.pdf";
	public static String serverFile = "opdslip1.pdf";
	Object Rows_Object_Array[][];
	int NumberOfColumns = 0, NumberOfRows = 0;
	Vector<String> TypeV = new Vector<String>();
	Vector<String> ReportInsuranceV= new Vector<String>();
	Vector<Integer> BillsCountV = new Vector<Integer>();
	Vector<Double> BillsAmountV= new Vector<Double>();
	Vector<Integer> OnlineCountV = new Vector<Integer>();
	Vector<Double> OnlineAmountV = new Vector<Double>();
	Vector<Integer> CancelCountV = new Vector<Integer>();
	Vector<Double> CancelAmountV = new Vector<Double>();
	Vector<Double> IPDTotalAmountV = new Vector<Double>();
	Vector<Double> OPDTotalAmountV = new Vector<Double>();
	Vector<Double> CashAmountV = new Vector<Double>();
	Vector<String> AllUsers = new Vector<String>();

	Vector<String> InsuranceCategory = new Vector<String>();
	Vector<String> miscType = new Vector<String>();
	String opd_no, patient_id, patient_name, patient_age, doctor_name,
	amt_received, date, token_no;
	static String OS;
	String mainDir = "", str = "";
	String dateFrom="",dateTo="";
	Font font;
	double IPDtotalAmount = 0, OPDtotalAmount = 0,CashtotalAmount=0,CanceltotalAmount=0,OnlinetotalAmount=0,IpdCancelAmount=0,CashCancelAmount=0;
	double GrandIPDtotalAmount = 0, GrandOPDtotalAmount = 0,GrandCashtotalAmount=0,GrandCashCanceltotalAmount=0,GrandIPDCanceltotalAmount=0,GrandOnlinetotalAmount=0;
	float[] opdTablCellWidth = { 2.0f, 0.7f, 1.1f };

	public static void main(String[] arg) {
		try {
			new NewSummaryReport("2021-03-01", "2021-03-01","","");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public NewSummaryReport(String dateFrom, String dateTo,String username,String id)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		if(username.equals("All")) {
			getAllUsers(dateFrom,dateTo);
		}else { 	
			AllUsers.add(username);
		}
StoreAccountDBConnection storeAccountDBConnection = new StoreAccountDBConnection();
		
		storeAccountDBConnection.closeActivity1(id);
		storeAccountDBConnection.closeConnection();
		this.dateFrom=dateFrom;
		this.dateTo=dateTo;
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

		Document document = new Document();
		OS = System.getProperty("os.name").toLowerCase();
		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		HeaderAndFooter event = new HeaderAndFooter();
		wr.setPageEvent(event);
		document.setPageSize(PageSize.A4.rotate());
		document.setMargins(0,0, 50,80);
		document.open();
		BaseFont base = BaseFont.createFont("indian.ttf", BaseFont.WINANSI,
				BaseFont.EMBEDDED);
		font = new Font(base, 13f, Font.BOLD);

		for(int i=0;i<AllUsers.size();i++) {
		float[] tiltelTablCellWidth = { 0.1f, 1f, 0.1f };
		
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);

		java.net.URL imgURL = NewSummaryReport.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = NewSummaryReport.class
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
				fontHeader));
		addressCell.setBorder(Rectangle.NO_BORDER);
		addressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell);

		PdfPCell addressCell2 = new PdfPCell(new Phrase("Mobile No. : 09034056793",	font2));
		addressCell2.setBorder(Rectangle.NO_BORDER);
		addressCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell2);
		document.add(TitleTable);
		 PdfContentByte cb1 = wr.getDirectContent();
	        BaseFont bf = BaseFont.createFont();
	        
	        Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 59);
	        f1.setColor(BaseColor.LIGHT_GRAY);
	        Phrase phrase3 = new Phrase(AllUsers.get(i)+" Summary Report", f1);
	        	ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase3,450,380,30);
	        cb1.saveState();
	       // cb1.setColorStroke(BaseColor.BLACK);
	        //cb1.rectangle(501, 657,80, 70);
	        cb1.stroke();
	        cb1.restoreState();
			GetAllReportDATA(dateFrom,dateTo,AllUsers.get(i));
			document.add(UserData(AllUsers.get(i)));
			
			GrandIPDtotalAmount+=IPDtotalAmount;
			GrandOPDtotalAmount+=OPDtotalAmount;
			GrandOnlinetotalAmount+=OnlinetotalAmount;
			GrandCashCanceltotalAmount+=CashCancelAmount;
			GrandIPDCanceltotalAmount+=IpdCancelAmount;
			GrandCashtotalAmount+=CashtotalAmount;
			
		}
		if(AllUsers.size()>1) {
			
			PdfPTable table1 = new PdfPTable(1);
			table1.getDefaultCell().setBorder(10);
			table1.setWidthPercentage(90);
			
			PdfPTable table = new PdfPTable(1);
			table.getDefaultCell().setBorder(0);
			table.setWidthPercentage(90);
			
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			
			PdfPCell footer2 = new PdfPCell(new Phrase(" Total IPD Amount : `"
					+ Math.round(GrandIPDtotalAmount+GrandIPDCanceltotalAmount), font));
			footer2.setBorder(Rectangle.NO_BORDER);
			//footer2.setPaddingBottom(-90);
			footer2.setPaddingTop(5);
			footer2.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(footer2);
			PdfPCell footer6 = new PdfPCell(new Phrase(" Total Cancel IPD Amount : `"
					+ Math.round(GrandIPDCanceltotalAmount), font));
			footer6.setBorder(Rectangle.NO_BORDER);
			//footer6.setPaddingBottom(-90);
			footer6.setPaddingTop(5);
			footer6.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(footer6);
			
			PdfPCell footer7 = new PdfPCell(new Phrase(" Grand Total IPD Amount : `"
					+ Math.round(GrandIPDtotalAmount), font));
			footer7.setBorder(Rectangle.NO_BORDER);
			//footer7.setPaddingBottom(-90);
			footer7.setPaddingTop(5);
			footer7.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(footer7);
			
			PdfPCell footer3 = new PdfPCell(new Phrase("Grand Total OPD Amount : `"
					+ Math.round(GrandOPDtotalAmount), font));
			footer3.setBorder(Rectangle.NO_BORDER);
			footer3.setPaddingTop(-55);
			footer3.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(footer3);
			
			PdfPCell footer5 = new PdfPCell(new Phrase("Grand Total Online Amount : `"
					+ Math.round(GrandOnlinetotalAmount), font));
			footer5.setBorder(Rectangle.NO_BORDER);
			footer5.setPaddingTop(-36);
			//footer5.setPaddingBottom(2);
			footer5.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(footer5);
			
			PdfPCell footer1 = new PdfPCell(new Phrase("Grand Total Cash Cancel Amount : `"
					+ Math.round(GrandCashCanceltotalAmount), font));
			footer1.setBorder(Rectangle.NO_BORDER);
			footer1.setPaddingTop(-18);
			//footer1.setPaddingBottom(2);
			footer1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(footer1);
			
			PdfPCell footer4 = new PdfPCell(new Phrase("Grand Total Cash Amount : `"
					+ Math.round(GrandCashtotalAmount), font));
			footer4.setBorder(Rectangle.NO_BORDER);
			footer4.setPaddingTop(0);
		//	footer4.setPaddingBottom(2);
			footer4.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(footer4);	
			
			PdfPCell t = new PdfPCell(table);
			t.setPaddingTop(3);
		    t.setPaddingBottom(3);
			table1.addCell(t);		
			document.add(table1);

		}
		
		document.close();

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
	}

	public void GetAllReportDATA(String FromDate,String EndDate,String userName) {
		IpdCancelAmount=CashCancelAmount=0;
		BillsCountV.clear();
		BillsAmountV.clear();
		OnlineAmountV.clear();
		OnlineCountV.clear();
		CancelAmountV.clear();
		CancelCountV.clear();
		IPDTotalAmountV.clear();
		OPDTotalAmountV.clear();
		CashAmountV.clear();
		ReportInsuranceV.clear();
		TypeV.clear();
		IPDtotalAmount =OPDtotalAmount = CashtotalAmount=OnlinetotalAmount=CanceltotalAmount=0;
		BillingDBConnection db = new BillingDBConnection();
		ResultSet rs=db.retrieveReportData(FromDate, EndDate,userName);
		try {
			while(rs.next()) {
				TypeV.add(rs.getString(1));
				ReportInsuranceV.add(rs.getString(2));
				BillsCountV.add(rs.getInt(3));
				BillsAmountV.add(rs.getDouble(4));
				OnlineCountV.add(rs.getInt(5));
				OnlineAmountV.add(rs.getDouble(6));
				CancelCountV.add(rs.getInt(7));
				CancelAmountV.add(rs.getDouble(8));
				IPDTotalAmountV.add(rs.getDouble(9));
				OPDTotalAmountV.add(rs.getDouble(10));
				CashAmountV.add(rs.getDouble(11));	
				IPDtotalAmount+=rs.getDouble(9);
				OPDtotalAmount+=rs.getDouble(10);
				CashtotalAmount+=rs.getDouble(11);
				CanceltotalAmount+=rs.getDouble(8);
				OnlinetotalAmount+=rs.getDouble(6);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
	}




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
		InsuranceCategory.removeAllElements();
		InsuranceCategory.add("Unknown");
		InsuranceDBConnection dbConnection = new InsuranceDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllInsurance();
		try {
			while (resultSet.next()) {
				InsuranceCategory.add(resultSet.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
	
	public PdfPTable UserData(String u_name) {
		float[] tiltelTablCellWidth = { 0.1f, 1f, 0.1f };
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(90);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		
		table.addCell(TitleTable);
		String timeStamp = new SimpleDateFormat(
				"EEEEEE, d MMMMMM, YYYY        hh:mm:ss a").format(Calendar
						.getInstance().getTime());
		
		PdfPCell HeaderName = new PdfPCell(new Phrase("My Cash :- "+u_name+"                   Date : ["
				+ DateFormatChange.StringToDateFormat(dateFrom)
				+ "] to [" + DateFormatChange.StringToDateFormat(dateTo)+"]  As on : " + timeStamp,	fontHeader));
		HeaderName.setBorder(Rectangle.NO_BORDER);
		HeaderName.setHorizontalAlignment(Element.ALIGN_LEFT);
		HeaderName.setPaddingTop(2);
		HeaderName.setPaddingBottom(3);
		table.addCell(HeaderName);
		System.out.println("Arun---");

		float[] TablCellWidth = {0.3f,0.5f,0.3f,0.5f,0.4f,0.5f,0.4f,0.5f,0.6f,0.6f,0.5f};
		PdfPTable opdTable = new PdfPTable(TablCellWidth);

		opdTable.addCell(new Phrase( "Type", font3));
		opdTable.addCell(new Phrase("Insurance", font3));
				opdTable.addCell(new Phrase("Bills", font3));
				opdTable.addCell(new Phrase("Bills Amount", font3));
				opdTable.addCell(new Phrase("Online Bills", font3));
				opdTable.addCell(new Phrase("Online Amount", font3));
				opdTable.addCell(new Phrase("Cancel Bills", font3));
				opdTable.addCell(new Phrase("Cancel Amount", font3));
				opdTable.addCell(new Phrase("IPD Total Amount", font3));
				opdTable.addCell(new Phrase("OPD Total Amount", font3));
				opdTable.addCell(new Phrase("Cash Amount", font3));

		for(int a=0;a<2;a++) {
			
			String type=a==0?"OPD":"IPD";
			PdfPCell typeCell = new PdfPCell(new Phrase(type,font3));
			typeCell.setRowspan(InsuranceCategory.size());
			typeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			typeCell.setPaddingTop(InsuranceCategory.size()+50);
			opdTable.addCell(typeCell);
			for(int i=0;i<InsuranceCategory.size();i++) {
				boolean f=false;
				opdTable.addCell(new Phrase(InsuranceCategory.get(i), font3));
				for (int k = 0; k < TypeV.size(); k++) {
					
					if (TypeV.get(k).equals(type) && InsuranceCategory.get(i).equals(ReportInsuranceV.get(k))) {
						opdTable.addCell(new Phrase(""+BillsCountV.get(k), font3));
						opdTable.addCell(new Phrase(""+BillsAmountV.get(k), font3));
						opdTable.addCell(new Phrase(""+OnlineCountV.get(k), font3));
						opdTable.addCell(new Phrase(""+OnlineAmountV.get(k), font3));
						opdTable.addCell(new Phrase(""+CancelCountV.get(k), font3));
						opdTable.addCell(new Phrase(""+CancelAmountV.get(k), font3));
						opdTable.addCell(new Phrase(""+IPDTotalAmountV.get(k), font3));
						opdTable.addCell(new Phrase(""+OPDTotalAmountV.get(k), font3));
						opdTable.addCell(new Phrase(""+CashAmountV.get(k), font3));
						f=true;
						if(type.equals("IPD"))
					     	IpdCancelAmount+=CancelAmountV.get(k);
						else
							CashCancelAmount+=CancelAmountV.get(k);
						break;
					} 
					
				}
				if(!f) {
					opdTable.addCell(new Phrase("0", font3));
					opdTable.addCell(new Phrase("0.0", font3));
					opdTable.addCell(new Phrase("0", font3));
					opdTable.addCell(new Phrase("0.0", font3));
					opdTable.addCell(new Phrase("0", font3));
					opdTable.addCell(new Phrase("0.0", font3));
					opdTable.addCell(new Phrase("0.0", font3));
					opdTable.addCell(new Phrase("0.0", font3));
					opdTable.addCell(new Phrase("0.0", font3));
				}
				
			}

		}

		table.addCell(opdTable);
		
		
		PdfPCell footer2 = new PdfPCell(new Phrase("Total IPD Amount : `"
				+ Math.round(IPDtotalAmount+IpdCancelAmount), font));
		footer2.setBorder(Rectangle.NO_BORDER);
		footer2.setPaddingBottom(5);
		footer2.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(footer2);
		PdfPCell footer5 = new PdfPCell(new Phrase("Total Cancel IPD Amount : `"
				+ Math.round(IpdCancelAmount), font));
		footer5.setBorder(Rectangle.NO_BORDER);
		footer5.setPaddingBottom(5);
		footer5.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(footer5);
		PdfPCell footer6 = new PdfPCell(new Phrase("Grand Total IPD Amount : `"
				+ Math.round(IPDtotalAmount), font));
		footer6.setBorder(Rectangle.NO_BORDER);
		footer6.setPaddingBottom(-20);
		footer6.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(footer6);
		
		PdfPCell footer3 = new PdfPCell(new Phrase("Total OPD Amount : `"
				+ Math.round(OPDtotalAmount), font));
		footer3.setBorder(Rectangle.NO_BORDER);
		footer3.setPaddingTop(-32);
		footer3.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(footer3);
		
		PdfPCell footer8 = new PdfPCell(new Phrase("Total Online Amount : `"
				+ Math.round(OnlinetotalAmount), font));
		footer8.setBorder(Rectangle.NO_BORDER);
		footer8.setPaddingTop(-15);
		//footer8.setPaddingBottom(2);
		footer8.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(footer8);
		
		PdfPCell footer1 = new PdfPCell(new Phrase("Total Cash Cancel Amount : `"
				+ Math.round(CashCancelAmount), font));
		footer1.setBorder(Rectangle.NO_BORDER);
		footer1.setPaddingTop(5);
		//footer1.setPaddingBottom(2);
		footer1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(footer1);
		
		PdfPCell footer4 = new PdfPCell(new Phrase("Total Cash Amount : `"
				+ Math.round(CashtotalAmount), font));
		footer4.setBorder(Rectangle.NO_BORDER);
		footer4.setPaddingTop(5);
		//footer4.setPaddingBottom(90);
		footer4.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(footer4);
		
		return table;
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
	public void getAllUsers(String dateFrom,String dateTo) {
		AllUsers.removeAllElements();
		StoreAccountDBConnection dbConnection = new StoreAccountDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllReceptionName1(dateFrom, dateTo);
		try {
			while (resultSet.next()) {
				AllUsers.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//dbConnection.closeConnection();
		//PurchaseOrderDBConnection db = new PurchaseOrderDBConnection();
//		ResultSet rs = dbConnection.retrieveAllData21();
//		try {
//			while (rs.next()) {
//				AllUsers.addElement(rs.getObject(1).toString());
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		dbConnection.closeConnection();
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