package hms.patient.slippdf;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import hms.Printer.PrintFile;
import hms.admin.gui.StoreInfoDBConnection;
import hms.main.DateFormatChange;
import hms.main.NumberToWordConverter;
import hms.store.database.BillingDBConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MedicalStoreIPDReturnPdf {
	private String DRUGLIC="",GSTIN="";
	private String patient_name="";
	private String patient_mobile="";
	private String doctor_name="";
	private String insurance_type="";
	private String payable="";
	private double totalAmount=0;
	private double payableAmount=0;
	private String date="";
	private String time="";
	private String patient_address="";
	private String user_name="";
	private String karuna_discount="";
	private String ipd_id="",billno="";
	//	private double totalMRP=0;
	int R = 1;
	String mainDir = "";
	String[] open = new String[4];
	public static String serverFile = "ipdslip.pdf";
	private static Font itemFont = new Font(Font.FontFamily.HELVETICA, 6f);
	private static Font itemBoldFont = new Font(Font.FontFamily.HELVETICA, 6f,Font.BOLD, BaseColor.BLACK);
	private static Font itemHeaderFont = new Font(Font.FontFamily.HELVETICA, 7f,Font.BOLD);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 17,
			Font.BOLD, BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD);
	private static Font fontAddress = new Font(Font.FontFamily.HELVETICA, 7.8f,
			Font.BOLD);
	private static Font font2Notbold = new Font(Font.FontFamily.HELVETICA, 8);
	private static Font font0 = new Font(Font.FontFamily.HELVETICA, 7,
			Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 8.5f,
			Font.BOLD);
	//	private static Font font3Notbold = new Font(Font.FontFamily.HELVETICA, 8f,
	//			Font.BOLD);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.BLACK);
	private static Font font5 = new Font(Font.FontFamily.HELVETICA, 6f,
			Font.BOLD);
	private static Font font6 = new Font(Font.FontFamily.HELVETICA, 8.5f);
	// Output file path
	public static  String RESULT = "ipdslip.pdf";

	public static void main(String[] args) {

		try {
			new MedicalStoreIPDReturnPdf("521775");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public  MedicalStoreIPDReturnPdf(String billno) throws DocumentException, IOException {
		// Step 1: Create a Document object
		this.billno=billno;

		readFile();
		makeDirectory(billno);
		getDrugAndGstinInfo();
		getPatientBillData(billno);
		Document document = new Document();

		// Step 2: Initialize PdfWriter instance
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
		document.setPageSize(PageSize.LETTER);
		document.setMargins(36, 36, 36, 36);
		document.setMarginMirroring(false);

		// Step 3: Open the document
		document.open();
		Rectangle pageSize = document.getPageSize();
		float pageWidth = pageSize.getWidth();
		float pageHeight = pageSize.getHeight();
		Rectangle OutterRect = getRectangle(pageWidth-10, pageHeight-10,10,10,2);
		Rectangle InnerRect = getRectangle(pageWidth-25, pageHeight-25,25,25,1);
		// Step 4: Create a rectangle and add it to the document
		document.add(OutterRect);
		document.add(InnerRect);

		PdfPTable table = new PdfPTable(new float[] {1,1});
		table.getDefaultCell().setBorder(1);
		table.setWidthPercentage(100);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		PdfPTable nestedTable1 = new PdfPTable(new float[]{1, 1,1});
		nestedTable1.getDefaultCell().setBorder(0);
		PdfPCell gstIn = new PdfPCell(new Phrase(
				"GSTIN : "+GSTIN, font2));
		gstIn.setPaddingBottom(5);
		gstIn.setBorder(Rectangle.NO_BORDER);
		gstIn.setHorizontalAlignment(Element.ALIGN_LEFT);
		nestedTable1.addCell(gstIn);

		PdfPCell invoiceDesc = new PdfPCell(new Paragraph(
				"GST INVOICE, CREDIT", font4));
		invoiceDesc.setBorder(Rectangle.NO_BORDER);
		invoiceDesc.setHorizontalAlignment(Element.ALIGN_CENTER);
		nestedTable1.addCell(invoiceDesc);

		invoiceDesc = new PdfPCell(new Paragraph(
				"Original for Buyer", font4));
		invoiceDesc.setBorder(Rectangle.NO_BORDER);
		invoiceDesc.setHorizontalAlignment(Element.ALIGN_CENTER);
		invoiceDesc = new PdfPCell(invoiceDesc);
		invoiceDesc.setBorder(Rectangle.NO_BORDER);
		nestedTable1.addCell(invoiceDesc);
		PdfPCell cell= new PdfPCell(nestedTable1);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		table.addCell(cell);


		table.addCell(getStoreInfo());
		table.addCell(getBillInfo());

		cell = new PdfPCell(new Paragraph(
				"BUYER INFORMATION :- ", font4));
		cell.setPaddingLeft(5);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph(
				"PATIENT DETAIL :- ", font4));
		cell.setPaddingLeft(5);
		table.addCell(cell);

		table.addCell(getBuyerInfo());
		table.addCell(getPatientInfo());

		cell = new PdfPCell(table);
		document.add(getItemsData(billno,cell,document));

		PdfPTable CalTable=getTotalCalculation();
		PdfPTable FootTable=getFooter();

		PdfPTable footer = new PdfPTable(1);
		footer.setWidthPercentage(100);
		footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		footer.addCell(CalTable);
		footer.addCell(FootTable);
		writeFooterTable(writer,document,footer);

		document.add(OutterRect);
		document.add(InnerRect);
		System.out.println(document.getPageNumber());

		document.close();

		new OpenFile(RESULT);
		//		new PrintFile("MedicalSlip/" + billno + ".pdf", 1);

	}
	public void makeDirectory(String bill_no) {
		new File("MedicalSlip").mkdir();
		serverFile = mainDir + "/HMS/MedicalSlip/" + bill_no + ".pdf";
		RESULT = "MedicalSlip/" + bill_no + ".pdf";
	}
	private void writeFooterTable(PdfWriter writer, Document document, PdfPTable table) {
		final int FIRST_ROW = 0;
		final int LAST_ROW = -1;
		//Table must have absolute width set.
		if(table.getTotalWidth()==0)
			table.setTotalWidth((document.right()-document.left())*table.getWidthPercentage()/100f);
		table.writeSelectedRows(FIRST_ROW, LAST_ROW, document.left(), document.bottom()+table.getTotalHeight(),writer.getDirectContent());
	}
	public PdfPCell getStoreInfo() throws BadElementException, MalformedURLException, IOException {
		// Create the main table that will hold all the information
		PdfPTable finalTable = new PdfPTable(1);

		// Create a nested table to hold the logo and title
		PdfPTable nestedTable = new PdfPTable(new float[]{0.4f, 2.5f});
		nestedTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

		// Load the image from resources
		java.net.URL imgURL = MedicalStoreBillSlippdf.class.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);
		image.scalePercent(50);

		// Create the image cell
		PdfPCell imageCell = new PdfPCell(image);
		imageCell.setBorder(Rectangle.NO_BORDER);
		imageCell.setPaddingLeft(2);
		imageCell.setPaddingTop(3);
		imageCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		nestedTable.addCell(imageCell);

		// Create the title cell
		Phrase titlePhrase = new Phrase("DR. JAI DEV MEMORIAL ROTARY MEDICAL STORE\n", font1);
		PdfPCell titleCell = new PdfPCell(titlePhrase);
		titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		titleCell.setPaddingBottom(5);
		imageCell.setPaddingLeft(2);
		titleCell.setBorder(Rectangle.NO_BORDER);
		nestedTable.addCell(titleCell);

		// Add the nested table to the main table
		PdfPCell combinedCell = new PdfPCell(nestedTable);
		combinedCell.setBorder(Rectangle.NO_BORDER);
		finalTable.addCell(combinedCell);

		// Add the store address cell
		PdfPCell addressCell = new PdfPCell(new Phrase(
				"GROUND FLOOR ROTARY AMBALA CANCER & GENERAL HOSPITAL AMBALA CANTT-133001 (Haryana)",
				fontAddress
				));
		addressCell.setPaddingBottom(2);
		addressCell.setBorder(Rectangle.NO_BORDER);
		addressCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		finalTable.addCell(addressCell);

		// Add the drug license number cell
		PdfPCell drugCell = new PdfPCell(new Phrase(
				"D.L.No. : " + DRUGLIC,
				font2
				));
		drugCell.setPaddingBottom(2);
		drugCell.setBorder(Rectangle.NO_BORDER);
		drugCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		finalTable.addCell(drugCell);

		// Add the mobile number cell
		PdfPCell addressCell2 = new PdfPCell(new Phrase(
				"Mobile No. : +91-7496956702, +91-7496956703, +91-7082840888", 
				font2
				));
		addressCell2.setPaddingBottom(5);
		addressCell2.setBorder(Rectangle.NO_BORDER);
		addressCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
		finalTable.addCell(addressCell2);

		// Return the final cell containing all the information
		return new PdfPCell(finalTable);
	}

	public PdfPCell getBillInfo() throws BadElementException, MalformedURLException, IOException {
		// Create the main table that will hold all the information
		PdfPTable table = new PdfPTable(1);
		table.addCell(getNoBorderCell("Invoice No. : ",billno));
		table.addCell(getNoBorderCell("Invoice Date : ", date));
		table.addCell(getNoBorderCell("S.O No. : ", ""));
		table.addCell(getNoBorderCell("S.O Date : ",""));
		table.addCell(getNoBorderCell("Transport Name : ",""));
		table.addCell(getNoBorderCell("Issue Date/Time : ", date+"/"+time));
		table.addCell(getNoBorderCell("Generated By : ", user_name));

		PdfPCell cell=new PdfPCell(table);
		return cell;
	}

	public PdfPCell getBuyerInfo() throws BadElementException, MalformedURLException, IOException {
		// Create the main table that will hold all the information
		PdfPTable table = new PdfPTable(1);
		PdfPCell cell = new PdfPCell(new Paragraph("ROTARY AMBALA CANCER & GENERAL HOSPITAL", font4));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("NEAR ROTARY SCHOOL FOR DEAF,RAM BAGH ROAD,AMBALA \n 06-HARYANA \n PH.NO. :", font2Notbold));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell=new PdfPCell(table);

		return cell;
	}
	public PdfPCell getPatientInfo() throws BadElementException, MalformedURLException, IOException {
		// Create the main table that will hold all the information
		PdfPTable table = new PdfPTable(1);
		PdfPCell cell = new PdfPCell(new Paragraph("Name : "+patient_name, font2));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("IPD ID : "+ipd_id, font2Notbold));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);	
		
		cell = new PdfPCell(new Paragraph("Doctor : "+doctor_name, font2Notbold));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		
		cell = new PdfPCell(new Paragraph("PH.NO. : "+patient_mobile, font2Notbold));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell=new PdfPCell(table);
		cell.setPaddingBottom(3);
		return cell;
	}

	public PdfPTable getItemsData(String bill_no, PdfPCell cell, Document document) throws DocumentException {
		float[] TablCellWidth = new float[15];
		TablCellWidth = new float[] { 0.24f, 1.0f,0.4f, 0.35f, 0.3f,
				0.3f, 0.3f,0.3f,0.38f, 0.38f, 0.35f };
		PdfPTable Table = new PdfPTable(TablCellWidth);
		Table.setWidthPercentage(100);
		Table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		cell.setColspan(11);
		Table.addCell(cell);
		Table.setHeaderRows(1);
		Table.addCell(new Phrase("S.No.", itemHeaderFont));
		Table.addCell(new Phrase("Item Name", itemHeaderFont));
		Table.addCell(new Phrase("Batch No.", itemHeaderFont));
		Table.addCell(new Phrase("Expiry", itemHeaderFont));
		Table.addCell(new Phrase("Pack Size", itemHeaderFont));
		PdfPCell quantity = new PdfPCell(new Phrase("Qnty.", itemHeaderFont));
		quantity.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(quantity);
		PdfPCell mrp = new PdfPCell(new Phrase("MRP", itemHeaderFont));
		mrp.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(mrp);
		PdfPCell price = new PdfPCell(new Phrase("Unit Price", itemHeaderFont));
		price.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(price);


		PdfPCell cgst = new PdfPCell(new Phrase("CGST", itemHeaderFont));
		cgst.setHorizontalAlignment(Element.ALIGN_CENTER);
		PdfPCell sgst = new PdfPCell(new Phrase("SGST", itemHeaderFont));
		sgst.setHorizontalAlignment(Element.ALIGN_CENTER);
		PdfPCell gstAmt = new PdfPCell(new Phrase("Amt", itemBoldFont));
		cgst.setHorizontalAlignment(Element.ALIGN_CENTER);
		PdfPCell gstPer = new PdfPCell(new Phrase("Per%", itemBoldFont));
		cgst.setHorizontalAlignment(Element.ALIGN_CENTER);

		PdfPTable tempCell = new PdfPTable(new float[] {1,1});
		//finally add all cell using common cell
		cgst.setColspan(2);
		tempCell.addCell(cgst);
		tempCell.addCell(gstAmt);tempCell.addCell(gstPer);
		Table.addCell(new PdfPCell(tempCell));

		tempCell = new PdfPTable(new float[] {1,1});
		sgst.setColspan(2);
		tempCell.addCell(sgst);
		tempCell.addCell(gstAmt);tempCell.addCell(gstPer);
		Table.addCell(new PdfPCell(tempCell));



		PdfPCell amount = new PdfPCell(new Phrase("Amount", itemHeaderFont));
		amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(amount);


		try {
			BillingDBConnection db1 = new BillingDBConnection();
			ResultSet rs = db1.retrieveBillItemsData(bill_no);
			while (rs.next()) {

				Table.addCell(new Phrase("" + R, itemFont));
				String salt = rs.getObject(12).toString();
				salt = salt.trim();
				if (salt.length() <= 4) {
					salt = "";
				} else {
					salt = " (" + salt + ")";
				}
				Table.addCell(new Phrase("" + rs.getObject(1).toString() + ""+ salt, itemFont));
				Table.addCell(new Phrase("" +rs.getObject(3).toString() ,itemFont));
				Table.addCell(new Phrase(""+ DateFormatChange.StringToDateFormat(rs.getObject(4).toString()).toString(),itemFont));

				Double packsize = Double.parseDouble(rs.getObject(14).toString());
				Table.addCell(new Phrase("" + packsize,	itemFont));
				PdfPCell quantity1 = new PdfPCell(new Phrase(""	+ rs.getObject(6).toString(), itemFont));
				quantity1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				Table.addCell(quantity1);
				Double amnt1 = Double.parseDouble(rs.getObject(11).toString());
				double amnt =  Math.round(amnt1  * 100.0) / 100.0;
				Double mrpl = Double.parseDouble(rs.getObject(13).toString());
				PdfPCell mrpprice1 = new PdfPCell(new Phrase("" + (String.format("%.2f",mrpl)),
						itemFont));
				mrpprice1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				Table.addCell(mrpprice1);
				PdfPCell price1 = new PdfPCell(new Phrase(""
						+ rs.getObject(5).toString(), itemFont));
				price1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				Table.addCell(price1);

				PdfPCell tax1 = new PdfPCell(new Phrase(""+ rs.getObject(7).toString() +" | "+ "("+ rs.getObject(8).toString() + ")", itemFont));
				tax1.setHorizontalAlignment(Element.ALIGN_CENTER);
				Table.addCell(tax1);
				PdfPCell tax2 = new PdfPCell(new Phrase(""+ rs.getObject(9).toString() +" | "+"("+ rs.getObject(10).toString() + ")", itemFont));
				tax2.setHorizontalAlignment(Element.ALIGN_CENTER);
				Table.addCell(tax2);
				PdfPCell amount1 = new PdfPCell(new Phrase(""
						+ rs.getObject(11).toString(), font5));
				amount1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				Table.addCell(amount1);
				R++;
			}


		} catch (SQLException ex) {

		}
		return Table;
	}

	public PdfPTable getTotalCalculation() throws BadElementException, MalformedURLException, IOException {
		// Create the main table that will hold all the information
		PdfPTable table = new PdfPTable(new float[] {4,1,1});
		PdfPCell cell = new PdfPCell(new Paragraph("*GET WELL SOON,SAVE GIRL CHILD*", font4));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(" ", font4));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph(" ", font4));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("Total Items : "+(R-1), font4));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingLeft(5);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("TOTAL : ", font4));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);	

		cell = new PdfPCell(new Paragraph(totalAmount+"", font4));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setPaddingRight(8);
		table.addCell(cell);


		cell = new PdfPCell(new Paragraph("Computer generated invoice,thanks for your business.", font4));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingBottom(5);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("Coin Adjustment : ", font4));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);

		table.addCell(cell);

		cell = new PdfPCell(new Paragraph((""+ Math.round((payableAmount - totalAmount) * 100.00) / 100.00),
				font4));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setPaddingRight(8);
		table.addCell(cell);


		PdfPCell payableInWords = new PdfPCell(new Paragraph("In Words : "
				+ NumberToWordConverter.convert((int) payableAmount)+ " Only",
				font3));
		payableInWords.setBorder(Rectangle.TOP | Rectangle.LEFT);        
		payableInWords.setBorderWidthTop(1f);                            
		payableInWords.setBorderWidthLeft(0f); 
		payableInWords.setBorderWidthRight(0f); 
		payableInWords.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(payableInWords);

		PdfPCell payable = new PdfPCell(new Paragraph("GRAND TOTAL : ", font3));
		payable.setBorder(Rectangle.TOP | Rectangle.LEFT | Rectangle.RIGHT); 
		payable.setBorderWidthTop(1f);                             
		payable.setBorderWidthLeft(0f);
		payable.setBorderWidthRight(0f); 
		payable.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(payable);

		payable = new PdfPCell(new Paragraph(""+ payableAmount, font3));
		payable.setBorder(Rectangle.TOP | Rectangle.LEFT | Rectangle.RIGHT);    
		payable.setBorderWidthTop(1f);                            
		payable.setBorderWidthLeft(0f); 
		payable.setBorderWidthRight(0f); 
		payable.setHorizontalAlignment(Element.ALIGN_RIGHT);	
		table.addCell(payable);

		return table;
	}
	public PdfPTable getFooter() throws BadElementException, MalformedURLException, IOException {
		// Create the main table that will hold all the information
		PdfPTable table = new PdfPTable(1);
		PdfPCell cell = new PdfPCell(new Paragraph("TERMS AND CONDITIONS:-", font4));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);


		cell = new PdfPCell(new Paragraph("All Disputes subject to Ambala Jurisdiction only.                                                                          "
				+ "                                               "
				+ "For Dr. Jai Dev Memorial Rotary Medical Store", font2Notbold));
		cell.setBorder(Rectangle.NO_BORDER);
		
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("Goods once sold will not be taken back.", font2Notbold));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingTop(-8);
		cell.setPaddingLeft(5);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("BANK DETAILS :- YES BANK LTD.,GROUND FLOOR 4307,SHANTI COMPLEX,\n JAGADHARI ROAD,AMBALA CANTT 133001"
				+ "\n A/C NO. 012088700000155,IFSC-YESB0000120", font4));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingLeft(5);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("Checked By _______________________Receiver's seal and sign_E. & O.E. ", itemHeaderFont));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);	

		//		cell=new PdfPCell(table);
		//		cell.setPaddingBottom(3);
		return table;
	}

	public PdfPCell getNoBorderCell(String key,String value) {
		PdfPCell cell = new PdfPCell(new Paragraph(
				key+value, font0));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		return cell;
	}

	public static Rectangle getRectangle(float pageWidth,float pageHeight,float x,float y,float thick) {
		Rectangle rect = new Rectangle(pageWidth, pageHeight);

		rect.setLeft(x); // X coordinate (left position)
		rect.setBottom(y);// Y coordinate (right position)

		rect.enableBorderSide(Rectangle.LEFT);
		rect.enableBorderSide(Rectangle.RIGHT);
		rect.enableBorderSide(Rectangle.TOP);
		rect.enableBorderSide(Rectangle.BOTTOM);
		rect.setBorder(Rectangle.BOX);
		rect.setBorderWidth(thick);
		rect.setBorderColor(BaseColor.BLACK);

		return rect;
	}

	public  void getDrugAndGstinInfo() {

		StoreInfoDBConnection db = new StoreInfoDBConnection();
		ResultSet resultSet = db.retrieveAllData();

		try {
			while (resultSet.next()) {
				DRUGLIC = resultSet.getObject(2).toString();
				GSTIN = resultSet.getObject(3).toString();

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			db.closeConnection();
		}

	}

	public void getPatientBillData(String bill_no) {
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
				user_name = rs.getObject(12).toString();
				karuna_discount = rs.getObject(13).toString();
				ipd_id = rs.getObject(14).toString();


			}
			db.closeConnection();
		} catch (SQLException ex) {

		}

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

}
