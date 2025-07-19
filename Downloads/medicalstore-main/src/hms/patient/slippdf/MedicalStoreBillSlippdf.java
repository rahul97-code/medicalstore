package hms.patient.slippdf;

import hms.Printer.PrintFile;
import hms.admin.gui.StoreInfoDBConnection;
import hms.main.DateFormatChange;
import hms.main.NumberToWordConverter;
import hms.store.database.BillingDBConnection;

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

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class MedicalStoreBillSlippdf {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 8);
	private static Font smallBold1 = new Font(Font.FontFamily.HELVETICA, 8,Font.BOLD);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 2);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 17,
			Font.BOLD, BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD);
	private static Font Namefont = new Font(Font.FontFamily.HELVETICA, 8,
			Font.ITALIC);
	private static Font font5 = new Font(Font.FontFamily.HELVETICA, 6f,
			Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 8.5f,
			Font.BOLD);
	private static Font font6 = new Font(Font.FontFamily.HELVETICA, 8.5f);

	private static Font fontForTable = new Font(Font.FontFamily.HELVETICA, 6f);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.BLACK);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 11,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "opdslip1.pdf";
	public static String serverFile = "opdslip1.pdf";
	double totalCharges = 0, ipd_advance = 0, ipd_balance = 0,
			payableAmount = 0, totalAmount = 0,totalMRP=0;
	String patient_name, patient_mobile, patient_address, doctor_name,
	doctor_address, amt_received, date, bill_no, time, insurance_type,
	payable;
	String ipd_doctor_id = "", ipd_doctorname = "", ipd_date = "",
			ipd_time = "", ipd_date_dis = "", ipd_time_dis = "", ipd_note = "",
			ipd_id = "", ward_name = "", building_name = "",
			bed_no = "Select Bed No", ward_incharge = "", ward_room = "",
			ward_code = "", descriptionSTR = "", charges = "", ipd_days,
			ipd_hours, ipd_minutes, ipd_expenses_id, licNoSTR = "",
			tinNoSTR = "",user_name="",karuna_discount="";
	static String OS;
	static int flag=0;
	static	double	amntchk,mrpchk;
	String mainDir = "";
	Font font;
	// float[] TablCellWidth ;
	// float TablCellWidth[];
	// float[] TablCellWidth =
	// {0.24f,1.5f,0.4f,0.4f,0.4f,0.3f,0.3f,0.35f,0.35f,0.35f,0.35f};
	// float[] TablCellWidth = { 0.24f,1.0f,0.5f,0.5f,0.5f, 0.7f,0.5f };
	String[] open = new String[4];

	public static void main(String[] argh) {
		try {
			new MedicalStoreBillSlippdf("2334647","OPD","Unknown",false,false);
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public MedicalStoreBillSlippdf(String medical_bill,String billtype,String insurancetype, boolean b, boolean iskaruna)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		bill_no = medical_bill;

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
		
		
		document.add(createBarCode(wr,medical_bill));

		
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

		java.net.URL imgURL = MedicalStoreBillSlippdf.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = MedicalStoreBillSlippdf.class
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
		
		
		PdfPTable t=new PdfPTable(1);
		t.getDefaultCell().setBorder(0);
		PdfPCell namecell = new PdfPCell(new Phrase(
				"DR. JAI DEV MEMORIAL ROTARY MEDICAL STORE", font1));
		namecell.setHorizontalAlignment(Element.ALIGN_CENTER);
		namecell.setPaddingBottom(-1);
		namecell.setBorder(Rectangle.NO_BORDER);
		namecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		t.addCell(namecell);
		
		namecell = new PdfPCell(new Phrase(
				"( FORMERLY : ROTARY AMBALA MEDICAL STORE )" , Namefont));
		namecell.setHorizontalAlignment(Element.ALIGN_CENTER);
		namecell.setPaddingBottom(3);
		namecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		namecell.setBorder(Rectangle.NO_BORDER);
		t.addCell(namecell);

		PdfPCell nameTable = new PdfPCell(t);
		nameTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(nameTable);
		
		PdfPCell logocell = new PdfPCell(image);
		logocell.setRowspan(3);
		logocell.setBorder(Rectangle.NO_BORDER);
		logocell.setHorizontalAlignment(Element.ALIGN_CENTER);
		logocell.setPaddingLeft(3);
		TitleTable.addCell(logocell);
		PdfPCell addressCell = new PdfPCell(new Phrase(
				"GROUND FLOOR ROTARY AMBALA CANCER & GENERAL HOSPITAL AMBALA CANTT-133001 (Haryana)",
				font2));
		addressCell.setPaddingBottom(1);
		addressCell.setBorder(Rectangle.NO_BORDER);
		addressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell);

		PdfPCell addressCell2 = new PdfPCell(new Phrase(
				"Mobile No. : +91-7496956702 ,+91-7496956703,+91-7082840888", font2));
		addressCell2.setPaddingBottom(5);
		addressCell2.setBorder(Rectangle.NO_BORDER);
		addressCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell2);

		table.addCell(TitleTable);

		PdfPCell spaceCell = new PdfPCell(new Paragraph(
				" Bill cum Cash Receipt ", font4));
		spaceCell.setBorder(Rectangle.NO_BORDER);
		spaceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		spaceCell.setPaddingTop(1);
		spaceCell.setPaddingBottom(5);
		table.addCell(spaceCell);
		//
		float[] opdTablCellWidth = { 0.5f, 1f, 0.5f, 1f };
		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		opdTable.getDefaultCell().setBorder(0);

		PdfPCell tokencell = new PdfPCell(
				new Phrase("Bill No. :" + "\n", font3));
		tokencell.setBorder(Rectangle.NO_BORDER);

		PdfPCell tokenNocell = new PdfPCell(new Phrase(bill_no + "\n", font3));
		tokenNocell.setBorder(Rectangle.NO_BORDER);

		opdTable.addCell(tokencell);
		opdTable.addCell(tokenNocell);

		opdTable.addCell(new Phrase("Drug Lic. No. : ", font3));
		opdTable.addCell(new Phrase("" + licNoSTR, font3));

		opdTable.addCell(new Phrase("Patient Name : ", font3));
		opdTable.addCell(new Phrase("" + patient_name, font3));

		opdTable.addCell(new Phrase("GSTIN No. : ", font3));
		opdTable.addCell(new Phrase("" + tinNoSTR, font3));

		opdTable.addCell(new Phrase("Patient Address :", font3));
		PdfPCell pAddressCell = new PdfPCell(new Phrase("" + patient_address,
				font3));
		pAddressCell.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(pAddressCell);

		opdTable.addCell(new Phrase("Date : ", font3));
		opdTable.addCell(new Phrase(""
				+ DateFormatChange.StringToDateFormat(date), font3));

		opdTable.addCell(new Phrase("Contact No. :", font3));
		PdfPCell ContactCell = new PdfPCell(new Phrase("" + patient_mobile,
				font3));
		ContactCell.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(ContactCell);

		opdTable.addCell(new Phrase("Time : ", font3));
		PdfPCell timeCell = new PdfPCell(new Phrase("" + time, font3));
		timeCell.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(timeCell);

		opdTable.addCell(new Phrase("Doctor Name : ", font3));
		opdTable.addCell(new Phrase("" + doctor_name, font3));

		opdTable.addCell(new Phrase("Doctor Address : ", font3));
		opdTable.addCell(new Phrase("" + doctor_address, font3));
		opdTable.addCell(new Phrase("Bill Type : ", font3));
		opdTable.addCell(new Phrase("" + billtype, font3));

		opdTable.addCell(new Phrase("Insurance Type : ", font3));
		opdTable.addCell(new Phrase("" + insurancetype, font3));
		opdTable.addCell(new Phrase("Generated By: ", font3));
		opdTable.addCell(new Phrase("" + user_name, font3));

		opdTable.addCell(new Phrase(" ", font3));
		opdTable.addCell(new Phrase("" , font3));
		PdfPCell opdCell = new PdfPCell(opdTable);
		opdCell.setBorderWidth(0.8f);
		opdCell.setPaddingBottom(5);
		table.addCell(opdCell);

		table.addCell(new Phrase("  ", font3));
		table.addCell(billItems(bill_no));
		double savedAmount=totalMRP-totalAmount;

	
		if (billtype.equals("OPD") && !iskaruna) {
			String totalMRPAmount1 = String.format("%.2f", totalMRP);
			PdfPCell totalmrp = new PdfPCell(new Paragraph("Total MRP Amount : " + totalMRPAmount1, smallBold));
			totalmrp.setBorder(Rectangle.NO_BORDER);
			totalmrp.setHorizontalAlignment(Element.ALIGN_LEFT);
			totalmrp.setPaddingTop(8);
			totalmrp.setPaddingBottom(-30);
			table.addCell(totalmrp);
			String MRPSaving = String.format("%.2f", savedAmount);
			PdfPCell totalmrpsave = new PdfPCell(new Paragraph("Save On MRP : " + MRPSaving, smallBold1));
			totalmrpsave.setBorder(Rectangle.NO_BORDER);
			totalmrpsave.setHorizontalAlignment(Element.ALIGN_LEFT);
			totalmrpsave.setPaddingTop(19);
			totalmrpsave.setPaddingBottom(-20);
			table.addCell(totalmrpsave);
		}
		String totalAmount1=String.format("%.2f",totalAmount);
		PdfPCell total = new PdfPCell(new Paragraph("Total Amount : "+totalAmount1, smallBold));
		total.setBorder(Rectangle.NO_BORDER);
		total.setHorizontalAlignment(Element.ALIGN_RIGHT);
		total.setPaddingBottom(1);
		table.addCell(total);

		
		PdfPCell advance = new PdfPCell(new Paragraph(("Coin Adjustment : "
				+ Math.round((payableAmount - totalAmount) * 100.00) / 100.00),
				smallBold));
		advance.setBorder(Rectangle.NO_BORDER);
		advance.setHorizontalAlignment(Element.ALIGN_RIGHT);
		advance.setPaddingBottom(1);
		table.addCell(advance);
		PdfPCell karunadiscount = new PdfPCell(new Paragraph(iskaruna?"Karuna Relief : "
				+ karuna_discount:"", font3));
		karunadiscount.setBorder(Rectangle.NO_BORDER);
		karunadiscount.setHorizontalAlignment(Element.ALIGN_LEFT);
		karunadiscount.setPaddingTop(2);
		karunadiscount.setPaddingBottom(-10);
		table.addCell(karunadiscount);
		PdfPCell payable = new PdfPCell(new Paragraph("Amount Payable : "
				+ payableAmount, font3));
		payable.setBorder(Rectangle.NO_BORDER);
		payable.setHorizontalAlignment(Element.ALIGN_RIGHT);
		payable.setPaddingTop(2);
		payable.setPaddingBottom(1);

		table.addCell(payable);

		String sg="";
	    double cashTotal2=0;
	    cashTotal2=payableAmount;
	    if(cashTotal2<0)
	    {
	    	cashTotal2=Math.abs(cashTotal2);
	        sg="-";
	    }


		PdfPCell payableInWords = new PdfPCell(new Paragraph("In Words : "
				+sg+ NumberToWordConverter.convert((int) cashTotal2)+ " Only",
				font3));
		payableInWords.setBorder(Rectangle.NO_BORDER);
		payableInWords.setHorizontalAlignment(Element.ALIGN_RIGHT);
		payableInWords.setPaddingTop(2);
		payableInWords.setPaddingBottom(10);
		table.addCell(payableInWords);

		PdfPCell footer2 = new PdfPCell(new Phrase("Received Rs : "
				+ payableAmount+ " by cash on date "
				+ DateFormatChange.StringToDateFormat(date)
				+ " toward settlement of the above bill.", font3));
		footer2.setBorder(Rectangle.NO_BORDER);
		footer2.setPaddingBottom(5);
		footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
		// footer2.setBackgroundColor(BaseColor.LIGHT_GRAY);

		table.addCell(footer2);
//		if(flag!=1) {
//			String savedamount=String.format("%.2f",savedAmount);
//			PdfPCell footer3 = new PdfPCell(new Phrase(new Chunk("Amount saved : "+savedamount,font3).setUnderline(1f, -2f)));
//			footer3.setBorder(Rectangle.NO_BORDER);
//			footer3.setPaddingTop(1);
//			footer3.setPaddingBottom(5);
//			footer3.setHorizontalAlignment(Element.ALIGN_CENTER);
//			// footer2.setBackgroundColor(BaseColor.LIGHT_GRAY);
//
//			table.addCell(footer3);
//		}


		PdfPCell sign = new PdfPCell(new Paragraph("Authorised Signatory-Hospital", font4));
		PdfPCell sign2 = new PdfPCell(new Paragraph(new Chunk("NOTE:- No Returns or Exchanges.", font4).setUnderline(1f, -2f)));
		//		PdfPCell sign3 = new PdfPCell(new Paragraph(new Chunk("NOTE:- All Sales Final.No Returns or Exchanges.                                                                                                        "
		//				+ "Authorised Signatory-Hospital   ", font4).setUnderline(0.1f, -2f)));

		sign.setBorder(Rectangle.NO_BORDER);
		sign2.setBorder(Rectangle.NO_BORDER);
		sign2.setHorizontalAlignment(Element.ALIGN_LEFT);
		sign.setHorizontalAlignment(Element.ALIGN_RIGHT);
		sign.setPaddingTop(22);
		sign.setPaddingBottom(1);
		sign2.setPaddingTop(-18);
		sign2.setPaddingBottom(1);
		table.addCell(sign);
		table.addCell(sign2);
		document.add(table);
		// onEndPage(wr,document);
		document.close();

		if (isWindows()) {
			OPenFileWindows("MedicalSlip/" + bill_no + ".pdf");
			System.out.println("This is Windows");
		} else if (isMac()) {
			System.out.println("This is Mac");
		} else if (isUnix()) {
			if (System.getProperty("os.version").equals("3.11.0-12-generic")) {
				Run(new String[] { "/bin/bash", "-c",
						open[0] + " MedicalSlip/" + bill_no + ".pdf" });
			} else {
				Run(new String[] { "/bin/bash", "-c",
						open[1] + " MedicalSlip/" + bill_no + ".pdf" });
			}
			System.out.println("This is Unix or Linux");
		} else if (isSolaris()) {
			System.out.println("This is Solaris");
		} else {
			System.out.println("Your OS is not support!!");
		}
		
		if (b) {
			if (billtype.equals("IPD"))
				new PrintFile("MedicalSlip/" + bill_no + ".pdf", 1);
			else
				new PrintFile("MedicalSlip/" + bill_no + ".pdf", 0);
		}
		
		
		// try {
		// copyFileUsingJava7Files("IPDSlip/" + ipd_no + ".pdf", serverFile);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	private PdfPTable createBarCode(PdfWriter wr,String medical_bill) {

		PdfContentByte cb1 = wr.getDirectContent();
		Barcode128 code128 = new Barcode128();
		code128.setCode(medical_bill);
		code128.setFont(null);  // no text
		Image barcodeImage = code128.createImageWithBarcode(cb1, null, null);

		// OPTIONAL: scale barcode image if desired
		barcodeImage.scaleAbsolute(120f, 13f);

		PdfPTable table = new PdfPTable(1); 
		table.setWidthPercentage(90);
		PdfPCell barcodeCell = new PdfPCell(barcodeImage); 
		barcodeCell.setPaddingBottom(4f);
		barcodeCell.setBorder(PdfPCell.NO_BORDER);
		barcodeCell.setHorizontalAlignment(Element.ALIGN_LEFT);

		table.addCell(barcodeCell);
		return table;
	}

	public PdfPTable billItems(String bill_no) {
		float[] TablCellWidth = new float[15];
		TablCellWidth = new float[] { 0.24f, 1.0f,0.4f, 0.35f, 0.3f,
				0.3f, 0.3f,0.3f,0.4f,0.35f, 0.35f, 0.35f };
		PdfPTable Table = new PdfPTable(TablCellWidth);
		
		
		Table.addCell(new Phrase("S.No.", fontForTable));
		Table.addCell(new Phrase("Item Name", fontForTable));
		Table.addCell(new Phrase("Batch No.", fontForTable));
		Table.addCell(new Phrase("Expiry", fontForTable));
		Table.addCell(new Phrase("Pack Size", fontForTable));
		PdfPCell quantity = new PdfPCell(new Phrase("Qnty.", fontForTable));
		quantity.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(quantity);
		PdfPCell mrp = new PdfPCell(new Phrase("MRP", fontForTable));
		mrp.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(mrp);
		PdfPCell price = new PdfPCell(new Phrase("Unit Price", fontForTable));
		price.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(price);
		PdfPCell newUnit = new PdfPCell(new Phrase("Disc Unit Price", fontForTable));
		price.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(newUnit);
		PdfPCell cgst = new PdfPCell(new Phrase("CGST(%)", fontForTable));
		cgst.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(cgst);
		PdfPCell sgst = new PdfPCell(new Phrase("SGST(%)", fontForTable));
		sgst.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(sgst);
		PdfPCell amount = new PdfPCell(new Phrase("Amount", font5));
		amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(amount);
		
		
		try {
			BillingDBConnection db1 = new BillingDBConnection();
			ResultSet rs = db1.retrieveBillItemsData(bill_no);
			int r = 1;
			while (rs.next()) {
				Table.addCell(new Phrase("" + r, fontForTable));
				String salt = rs.getObject(12).toString();
				salt = salt.trim();
				if (salt.length() <= 4) {
					salt = "";
				} else {
					salt = " (" + salt + ")";
				}
				Table.addCell(new Phrase("" + rs.getObject(1).toString() + ""+ salt, fontForTable));
				Table.addCell(new Phrase("" +rs.getObject(3).toString() ,fontForTable));
				Table.addCell(new Phrase(""+ DateFormatChange.StringToDateFormat(rs.getObject(4).toString()).toString(),fontForTable));

				Double packsize = Double.parseDouble(rs.getObject(14).toString());
				Table.addCell(new Phrase("" + packsize,	fontForTable));
				PdfPCell quantity1 = new PdfPCell(new Phrase(""	+ rs.getObject(6).toString(), fontForTable));
				quantity1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				Table.addCell(quantity1);
				Double amnt1 = Double.parseDouble(rs.getObject(11).toString());
				double amnt =  Math.round(amnt1  * 100.0) / 100.0;
				Double mrpl = Double.parseDouble(rs.getObject(13).toString()) / packsize;
				Double fmrp = Double.parseDouble(rs.getObject(6).toString())*mrpl;
				double roundOffmrp =  Math.round(fmrp  * 100.0) / 100.0;
				totalMRP+=roundOffmrp;
				PdfPCell mrpprice1 = new PdfPCell(new Phrase("" + (roundOffmrp<amnt?"":String.format("%.2f",mrpl)),fontForTable));
				mrpprice1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				Table.addCell(mrpprice1);
				PdfPCell price1 = new PdfPCell(new Phrase(""+ rs.getObject(5).toString(), fontForTable));
				price1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				Table.addCell(price1);
				PdfPCell newunitprice = new PdfPCell(new Phrase(""
						+ rs.getObject(16).toString(), fontForTable));
				newunitprice.setHorizontalAlignment(Element.ALIGN_RIGHT);
				Table.addCell(newunitprice);
				PdfPCell tax1 = new PdfPCell(new Phrase(""+ rs.getObject(7).toString() + "("+ rs.getObject(8).toString() + ")", fontForTable));
				tax1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				Table.addCell(tax1);
				PdfPCell tax2 = new PdfPCell(new Phrase(""+ rs.getObject(9).toString() + "("+ rs.getObject(10).toString() + ")", fontForTable));
				tax2.setHorizontalAlignment(Element.ALIGN_RIGHT);
				Table.addCell(tax2);
				PdfPCell amount1 = new PdfPCell(new Phrase(""
						+ rs.getObject(11).toString(), font5));
				amount1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				Table.addCell(amount1);
				r++;
			}


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
				payable = rs.getObject(5).toString();
				totalAmount = Double.parseDouble(rs.getObject(6).toString());
				payableAmount = Double.parseDouble(rs.getObject(7).toString());
				date = rs.getObject(8).toString();
				time = rs.getObject(9).toString();
				patient_address = rs.getObject(10).toString();
				doctor_address = rs.getObject(11).toString();
				user_name = rs.getObject(12).toString();
				karuna_discount = rs.getObject(13).toString();
			}
			db.closeConnection();
		} catch (SQLException ex) {

		}

	}

	public void getInfo() {

		StoreInfoDBConnection dbConnection = new StoreInfoDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();

		try {
			while (resultSet.next()) {
				licNoSTR = resultSet.getObject(2).toString();
				tinNoSTR = resultSet.getObject(3).toString();

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void makeDirectory(String bill_no) {
		new File("MedicalSlip").mkdir();
		serverFile = mainDir + "/HMS/MedicalSlip/" + bill_no + ".pdf";
		RESULT = "MedicalSlip/" + bill_no + ".pdf";
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
			boolean fetch = true;
			while ((line = bufferedReader.readLine()) != null && fetch) {
				// System.out.println(line);
				str = line;
				fetch = false;
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