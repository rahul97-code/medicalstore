package hms.patient.slippdf;

import hms.admin.gui.StoreInfoDBConnection;
import hms.main.GeneralDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.database.PODBConnection;
import hms.store.database.SuppliersDBConnection;
import hms.store.gui.StoreMain;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Bill_PDF {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 5);
	private static Font speratorLine = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD, BaseColor.RED);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.DARK_GRAY);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 19,
			Font.BOLD);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 14,
			Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 9f);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD);
	private static Font font5 = new Font(Font.FontFamily.HELVETICA, 11,
			Font.BOLD);

	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 7f,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "";
	public static String serverFile = "";
	private static Font Namefont = new Font(Font.FontFamily.HELVETICA, 8,
			Font.ITALIC);

	Vector<String> doctorname = new Vector<String>();
	Vector<String> achievements = new Vector<String>();
	Vector<String> specialization = new Vector<String>();
	boolean IsPriceChange=false;
	static String OS,reason="";
	String mainDir = "";
	Font font;
	String[] open = new String[4];

	int index = 1;

	String po_number,ReturnAmount="",vendorName="",entry_date_time="";
	String PO_NUMBER,PO_DATE,REF_NO,SUPPLIER_ID,SUPPLIER_NAME,SUPPLIER_ADDRESS,ODERED_BY,TOTAL_AMOUNT,TAX_AMOUNT,FINAL_AMOUNT,user_entry;
	/**
	 * @param exam_id
	 * @param examnames
	 * @param examRates
	 * @param totalAmountSTR
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void main(String[] argh) {
		try {
			new Bill_PDF("15096","");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Bill_PDF(String po_number,String vendorName) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		this.po_number=po_number;
		
		readFile();
		makeDirectory("" + index);
		PODetail();
		Document document = new Document();
		OS = System.getProperty("os.name").toLowerCase();
		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		document.setPageSize(PageSize.A4.rotate());
		document.setMargins(10, 10, 10, 10);
		document.open();

		BaseFont base = BaseFont.createFont("indian.ttf", BaseFont.WINANSI,
				BaseFont.EMBEDDED);
		font = new Font(base, 9f);
		float[] table1Cell = { 1.5f, 0.4f, 0.5f, 0.5f,0.5f };

		PdfPTable table = new PdfPTable(table1Cell);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(100);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		java.net.URL imgURL = Bill_PDF.class.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		java.net.URL imgURL2 = Bill_PDF.class.getResource("/icons/footer.PNG");
		Image image2 = Image.getInstance(imgURL2);

		image.scalePercent(30);
		image.setAbsolutePosition(40, 750);

		
		PdfPTable t=new PdfPTable(1);
		t.getDefaultCell().setBorder(0);
		PdfPCell namecell = new PdfPCell(new Phrase(
				"DR. JAI DEV MEMORIAL ROTARY MEDICAL STORE", font1));
		namecell.setHorizontalAlignment(Element.ALIGN_CENTER);
		namecell.setPaddingBottom(-1);
		namecell.setBorder(PdfPCell.NO_BORDER);
//		namecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		t.addCell(namecell);
		
		namecell = new PdfPCell(new Phrase(
				"( FORMERLY : ROTARY AMBALA MEDICAL STORE )" , Namefont));
		namecell.setHorizontalAlignment(Element.ALIGN_CENTER);
		namecell.setPaddingBottom(3);

//		namecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		namecell.setBorder(PdfPCell.NO_BORDER);
		t.addCell(namecell);
		
		namecell = new PdfPCell(new Phrase("VENDOR NAME : "+vendorName.toUpperCase(), font1));
		namecell.setRowspan(2);
		namecell.setHorizontalAlignment(Element.ALIGN_CENTER);
		namecell.setColspan(4);
//		namecell.setBorder(PdfPCell.NO_BORDER);
		t.addCell(namecell);

		PdfPCell nameTable = new PdfPCell(t);
		nameTable.setRowspan(2);
//		nameTable.setBorder(Rectangle.NO_BORDER);
		nameTable.setHorizontalAlignment(Element.ALIGN_CENTER);
//		nameTable.setPaddingLeft(3);
		nameTable.setColspan(4);
		table.addCell(nameTable);
		PdfPCell cell;
		

		cell = new PdfPCell(new Phrase("Invoice",font2));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setRowspan(2);
		table.addCell(cell);
	

		cell = new PdfPCell(
				new Phrase(
						"GROUND FLOOR ROTARY AMBALA CANCER & GENERAL HOSPITAL AMBALA CANTT-133001 (Haryana)\nMobile No. : +91-9034056793,+91-7496956703,+91-7082840888\nGSTIN No. : "+getInfo()));
		cell.setRowspan(2);
		cell.setColspan(2);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("Invoice No.", font2));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Invoice Date", font2));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Order No.", font2));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(""+PO_NUMBER, font2));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

//		cell = new PdfPCell(new Phrase(
//				SUPPLIER_NAME+"\n"+SUPPLIER_ADDRESS));
//		cell.setRowspan(3);
//		table.addCell(cell);

		

		cell = new PdfPCell(new Phrase(""+PO_DATE, font2));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

	

		cell = new PdfPCell(new Phrase(""+REF_NO, font2));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);


		//table.addCell(cell);

		cell = new PdfPCell(
				new Phrase(
						"GROUND FLOOR ROTARY AMBALA CANCER & GENERAL HOSPITAL AMBALA CANTT-133001 (Haryana)\nTelephone No. : 0171-2690009, Mobile No. : 09034056793"));
		cell.setColspan(2);
		//table.addCell(cell);

		cell = new PdfPCell(
				new Phrase(
						"GROUND FLOOR ROTARY AMBALA CANCER & GENERAL HOSPITAL AMBALA CANTT-133001 (Haryana)\nTelephone No. : 0171-2690009, Mobile No. : 09034056793"));
		cell.setColspan(2);
		//ble.addCell(cell);

//		float[] table2Cell = { 0.07f, 0.4f,0.1f,0.1f, 0.1f,0.1f, 0.1f, 0.1f,0.1f,0.3f};
		float[] table2Cell = { 0.07f,0.08f, 0.4f,0.1f,0.1f, 0.1f,0.1f, 0.1f, 0.1f,0.1f,0.1f,0.3f};
		PdfPTable table2 = new PdfPTable(table2Cell);
		table2.getDefaultCell().setBorder(0);
		table2.setWidthPercentage(100);
		table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		cell = new PdfPCell(new Phrase("S.No.", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Item ID", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Description of Goods", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);
		cell = new PdfPCell(new Phrase("Batch No.", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);
		cell = new PdfPCell(new Phrase("MRP", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);
		cell = new PdfPCell(new Phrase("Pack Size", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);
		cell = new PdfPCell(new Phrase("Expiry Date", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);
		cell = new PdfPCell(new Phrase("HSN Code", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("PO Qty.", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);
		cell = new PdfPCell(new Phrase("Received Qty", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);
		cell = new PdfPCell(new Phrase("Selling Price", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);
		cell = new PdfPCell(new Phrase("Remarks", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);
//		cell = new PdfPCell(new Phrase("Unit Price", font3));
//		cell.setRowspan(2);
//		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		table2.addCell(cell);
//
//		cell = new PdfPCell(new Phrase("Total", font3));
//		cell.setRowspan(2);
//		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		table2.addCell(cell);
//
//		cell = new PdfPCell(new Phrase("Discount", font3));
//		cell.setRowspan(2);
//		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		table2.addCell(cell);
//
//		cell = new PdfPCell(new Phrase("Taxable Amt", font3));
//		cell.setRowspan(2);
//		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		table2.addCell(cell);
//
//		cell = new PdfPCell(new Phrase("CGST", font3));
//		cell.setColspan(2);
//		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		table2.addCell(cell);
//
//		cell = new PdfPCell(new Phrase("SGST", font3));
//		cell.setColspan(2);
//		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		table2.addCell(cell);
//
//		cell = new PdfPCell(new Phrase("Rate", font3));
//		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		table2.addCell(cell);
//
//		cell = new PdfPCell(new Phrase("Amt", font3));
//		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		table2.addCell(cell);
//
//		cell = new PdfPCell(new Phrase("Rate", font3));
//		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		table2.addCell(cell);
//
//		cell = new PdfPCell(new Phrase("Amt", font3));
//		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		table2.addCell(cell);

		PODBConnection podbConnection = new PODBConnection();
		ResultSet resultSet = podbConnection.retrieveItemsInvoice(po_number);
		int counter=0;
		try {
			while (resultSet.next()) {
				counter++;
				cell = new PdfPCell(new Phrase(counter+"", font3));
				
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.addCell(cell);
				
                cell = new PdfPCell(new Phrase(resultSet.getObject(18).toString(), font3));			
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.addCell(cell);
				
				cell = new PdfPCell(new Phrase(resultSet.getObject(1).toString(), font3));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table2.addCell(cell);

			
				cell = new PdfPCell(new Phrase(resultSet.getObject(14).toString(), font3));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table2.addCell(cell);

				String mtext="";
				if(resultSet.getObject(13).toString().equals("null")){
					mtext=" ";
				}else{
					mtext=resultSet.getObject(13).toString();
				}
				cell = new PdfPCell(new Phrase(mtext, font3));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table2.addCell(cell);
				cell = new PdfPCell(new Phrase(resultSet.getObject(15).toString(), font3));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table2.addCell(cell);
				cell = new PdfPCell(new Phrase(resultSet.getObject(16).toString(), font3));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table2.addCell(cell);
				cell = new PdfPCell(new Phrase(resultSet.getObject(2).toString(), font3));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table2.addCell(cell);
				
				cell = new PdfPCell(new Phrase(resultSet.getObject(3).toString(), font3));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.addCell(cell);
				
				cell = new PdfPCell(new Phrase(resultSet.getObject(12).toString(), font3));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table2.addCell(cell);
				double price1=getItemBatchDetail(resultSet.getObject(19).toString());
				cell = new PdfPCell(new Phrase(""+price1, font3));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				if(resultSet.getObject(20).toString().equals("1"))
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				table2.addCell(cell);
				cell = new PdfPCell(new Phrase("", font3));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.addCell(cell);
				
//				cell = new PdfPCell(new Phrase(resultSet.getObject(6).toString(), font3));
//				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				table2.addCell(cell);
//				
//				cell = new PdfPCell(new Phrase(resultSet.getObject(7).toString(), font3));
//				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				table2.addCell(cell);
//				
//				cell = new PdfPCell(new Phrase(resultSet.getObject(8).toString(), font3));
//				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				table2.addCell(cell);
//				
//				cell = new PdfPCell(new Phrase(resultSet.getObject(9).toString(), font3));
//				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				table2.addCell(cell);
//				
//				cell = new PdfPCell(new Phrase(resultSet.getObject(10).toString(), font3));
//				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				table2.addCell(cell);
//				
//				cell = new PdfPCell(new Phrase(resultSet.getObject(11).toString(), font3));
//				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				table2.addCell(cell);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		podbConnection.closeConnection();
		
//		float[] table3Cell = { 0.5F, 2f, 0.5f, 0.5f };
//
//		PdfPTable table3 = new PdfPTable(table3Cell);
//		table3.getDefaultCell().setBorder(PdfPCell.LEFT | PdfPCell.RIGHT);
//		table3.setWidthPercentage(100);
//		table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//
//		cell = new PdfPCell(new Phrase("Terms & Conditions :", font3));
//	
//		table3.addCell(cell);
//
//		cell = new PdfPCell(new Phrase("", font3));
//		table3.addCell(cell);
//		cell = new PdfPCell(new Phrase("", font3));
//		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		table3.addCell(cell);
//		cell = new PdfPCell(new Phrase("", font3));
//		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		table3.addCell(cell);
//		
//		
//		cell = new PdfPCell(new Phrase("Warranty/ Guarantee:", font3));
//		table3.addCell(cell);
//		cell = new PdfPCell(new Phrase("N.A.", font3));
//		table3.addCell(cell);
//		cell = new PdfPCell(new Phrase("Sub Total", font3));
//		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		table3.addCell(cell);
//		cell = new PdfPCell(new Phrase(""+TOTAL_AMOUNT, font3));
//		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		table3.addCell(cell);
//		
//		
//		
//		cell = new PdfPCell(new Phrase("Transport Terms:", font3));
//		table3.addCell(cell);
//		cell = new PdfPCell(new Phrase("", font3));
//		table3.addCell(cell);
//		cell = new PdfPCell(new Phrase("Total Tax", font3));
//		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		table3.addCell(cell);
//		cell = new PdfPCell(new Phrase(""+TAX_AMOUNT, font3));
//		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		table3.addCell(cell);
//		
//		
//		cell = new PdfPCell(new Phrase("Payment Terms:", font3));
//		table3.addCell(cell);
//		cell = new PdfPCell(new Phrase("", font3));
//		table3.addCell(cell);
//		cell = new PdfPCell(new Phrase("Total", font3));
//		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		table3.addCell(cell);
//		cell = new PdfPCell(new Phrase(""+FINAL_AMOUNT, font3));
//		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		table3.addCell(cell);
		
		
		float[] table4Cell = { 1f };

		PdfPTable table4 = new PdfPTable(table4Cell);
		table4.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
//		table4.getDefaultCell().setBorder(PdfPCell.LEFT | PdfPCell.RIGHT);
		table4.setWidthPercentage(20);
		table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

//		cell = new PdfPCell(new Phrase("Importan :\nPlease mention the following information on invoice:\n1) Our P.O. No. & Date\n2) Our GSTIN No. ", font3));
//		table4.addCell(cell);
//		cell = new PdfPCell(new Phrase("Order By", font3));
//		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		table4.addCell(cell);
//		cell = new PdfPCell(new Phrase("Checked By", font3));
//		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		table4.addCell(cell);
		PdfPCell cell2 = new PdfPCell(new Phrase("Generated By : "+ODERED_BY.toUpperCase() +" [ "+entry_date_time+" ] ", font3));
		cell2.setBorder(PdfPCell.NO_BORDER);
		cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
//		cell2.setPaddingTop(-20);
		

		PdfPCell cell3 = new PdfPCell(new Phrase("Updated Reason : "+reason, font3));
		cell3.setBorder(PdfPCell.NO_BORDER);
		cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
//		cell2.setPaddingTop(-20);
		cell3.setPaddingBottom(-10);
		
		
		PdfPCell cell1 = new PdfPCell(new Phrase("Return Amount : "+ReturnAmount+"      Total Amount : "+FINAL_AMOUNT+"\n\n", font5));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		
		cell = new PdfPCell(new Phrase("Approved By", font3));
//		cell.setColspan(4);
		cell.setBorder(Rectangle.OUT_BOTTOM);
		cell.setBorder(Rectangle.OUT_LEFT);
		cell.setBorder(Rectangle.OUT_RIGHT);
		cell.setBorder(Rectangle.OUT_TOP);
		
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		
		table4.addCell(cell1);
		table4.addCell(cell2);
		if(reason.equals("") || reason==null || reason.contains("null"))
		{
			cell2.setPaddingBottom(-10);
			
		}else
		{   
			cell2.setPaddingBottom(0);
			table4.addCell(cell3);	
				
		}
		table4.addCell(cell);
		
		
		document.add(table);
		document.add(table2);
			
		PdfPTable footer = new PdfPTable(1);
		footer.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		footer.setWidthPercentage(100);
		footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

//		footer.addCell(table3);
		footer.addCell(table4);
		writeFooterTable(wr,document,footer);
		document.close();

		if (isWindows()) {
			OPenFileWindows("MISCSlips/" + index + ".pdf");
			System.out.println("This is Windows");
		}else if (isUnix()) {
			if (System.getProperty("os.version").equals("3.11.0-12-generic")) {
				Run(new String[] { "/bin/bash", "-c",
						open[0] + " MISCSlips/" + index + ".pdf" });
			} else {
				Run(new String[] { "/bin/bash", "-c",
						open[1] + " MISCSlips/" + index + ".pdf" });
			}
			
		}

	}
	private void writeFooterTable(PdfWriter writer, Document document, PdfPTable table) {
        final int FIRST_ROW = 0;
        final int LAST_ROW = -1;
        //Table must have absolute width set.
        if(table.getTotalWidth()==0)
            table.setTotalWidth((document.right()-document.left())*table.getWidthPercentage()/100f);
        table.writeSelectedRows(FIRST_ROW, LAST_ROW, document.left(), document.bottom()+table.getTotalHeight(),writer.getDirectContent());
    }
	public void makeDirectory(String slipNo) {

		RESULT = "MISCSlips/" + slipNo + ".pdf";
	}
	public double getItemBatchDetail(String index) {
		GeneralDBConnection formula = new GeneralDBConnection();
		double value = Double.parseDouble(formula.retrieveFormula1());
		formula.closeConnection();
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetailBatch2(index);
//		ResultSet resultSet = itemsDBConnection.itemDetailBatchNew(index,batchname);
		double mrp = 0,mrptotal = 0;
		double purchase = 0;
		double tot = 0, tot1 = 0;
		double sp = 0;
		int quantity = 0;
		double price = 0;
		int packSize = 1;
		double taxValue=0,surchargeValue=0;
		String formulaActive = "";
		// int packSize = 1;
		try {
			while (resultSet.next()) {

				taxValue = Double
						.parseDouble(resultSet.getObject(4).toString());
				purchase = Double
						.parseDouble(resultSet.getObject(1).toString());
				System.out.println("purchase" + purchase);
				quantity = Integer.parseInt(resultSet.getObject(8).toString());

				surchargeValue = Double.parseDouble(resultSet.getObject(5)
						.toString());
				// System.out.println(taxValue + "surcharge  " +
				// surchargeValue);
				// itemHSNSTR = resultSet.getObject(14).toString();
				try {
					packSize = Integer.parseInt(resultSet.getObject(3)
							.toString().trim());
				} catch (Exception e) {
					// TODO: handle exception
				}

				mrp = Double.parseDouble(resultSet.getObject(2).toString());
				mrptotal = mrp;
				if (formulaActive.equals("1")) {
					sp = purchase;
				} else {
					double tempvar1 = mrp / packSize;
					System.out.println("tempvar1"+tempvar1);
					double tempvar2 = tempvar1
							* ((taxValue + surchargeValue) / 100);
					System.out.println("tempvar2"+tempvar2);
					double mrpwithouttax = tempvar1 - tempvar2;
					System.out.println("mrpwithouttax" + mrpwithouttax);
					double seventyprcntmrpwithouttax = 0.7 * mrpwithouttax;

					double margin = (mrpwithouttax - purchase) / purchase * 100;
					System.out.println("margin" + margin);
//					double value = 0;
					if (purchase >= 10000 && purchase <= 20000) {
						sp = purchase * 1.15;
					} else if (purchase > 20000 && purchase <= 30000) {
						sp = purchase * 1.10;
					} else if (purchase >= 30000) {
						sp = purchase * 1.05;
					} else {
						if (margin > 70.0) {
							System.out.println("70");
							value = 1.5;
							sp = (double) Math.round(purchase * value * 100) / 100;
							System.out.println("sp" + sp);
						} else if (margin <= 70.0 && margin > 60.0) {
							System.out.println("60");
							value = 1.4;
							sp = (double) Math.round(purchase * value * 100) / 100;
							System.out.println("sp" + sp);
						} else if (margin <= 60.0 && margin > 50.0) {
							System.out.println("50");
							value = 1.3;
							sp = (double) Math.round(purchase * value * 100) / 100;
							System.out.println("sp" + sp);
						} else if (margin <= 50.0 && margin > 40.0) {
							System.out.println("40");
							value = 1.2;
							sp = (double) Math.round(purchase * value * 100) / 100;
							System.out.println("sp" + sp);
						} else if (margin <= 40.0 && margin > 30.0) {
							System.out.println("30");
							value = 1.15;
							sp = (double) Math.round(purchase * value * 100) / 100;
							System.out.println("sp" + sp);
						} else if (margin <= 30.0) {
							System.out.println("30less");

							value = 1.15;
							sp = (double) Math.round(purchase * value * 100) / 100;
							double mrpless1prcnt = mrpwithouttax
									- (mrpwithouttax * 0.01);
							if (sp < mrpless1prcnt) {
								sp = sp;
							} else {
								sp = mrpless1prcnt;
							}
							System.out.println("sp" + sp);
						}

					}
				}
			}
			sp = Math.round(sp * 100.00) / 100.00;
//			price = sp;
			
			price = (double) Math.round(sp * 100) / 100;
			double k = price * (taxValue / 100.0f);
			k = Math.round(k * 100.000) / 100.000;
			double s = price * (surchargeValue / 100.0f);
			s = Math.round(s * 100.000) / 100.000;
		
			price = price + k;
			price = price + s;

			price = Math.round(price * 100.000) / 100.000;
			
			
			System.out.println("priceeeeeeeeee" + price);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return price;
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
	public void PODetail()
	{
		PODBConnection podbConnection = new PODBConnection();
		ResultSet resultSet = podbConnection.retrieveWithInvoiceID(po_number);
	
		try {
			while (resultSet.next()) {
				PO_NUMBER=(resultSet.getObject(2).toString());
				REF_NO=(resultSet.getObject(3).toString());
				SUPPLIER_ID=(resultSet.getObject(4).toString());
				
				
				SUPPLIER_NAME=(resultSet.getObject(5).toString());
				PO_DATE=(resultSet.getObject(6).toString());
				ODERED_BY=(resultSet.getObject(8).toString());
				TOTAL_AMOUNT=(resultSet.getObject(9).toString());
				TAX_AMOUNT=(resultSet.getObject(10).toString());
				FINAL_AMOUNT=(resultSet.getObject(12).toString());
				ReturnAmount=(resultSet.getObject(13).toString());
				entry_date_time=(resultSet.getObject(14).equals("null")?"":resultSet.getObject(14).toString());
				reason=(resultSet.getObject(14).equals("null")?"":resultSet.getObject(15).toString());
				System.out.println(PO_NUMBER+"  "+SUPPLIER_NAME);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getSupplierDetail(SUPPLIER_ID);
	}	
	public void getSupplierDetail(String index) {

		SuppliersDBConnection suppliersDBConnection = new SuppliersDBConnection();
		ResultSet resultSet = suppliersDBConnection
				.searchSupplierWithId(index);
		try {
			while (resultSet.next()) {

				
				SUPPLIER_NAME = resultSet.getObject(2).toString();
				SUPPLIER_ADDRESS = (resultSet.getObject(6).toString() + ", "
						+ resultSet.getObject(7).toString() + ", " + resultSet
						.getObject(8).toString())+"\n"+(resultSet.getObject(4).toString());;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		suppliersDBConnection.closeConnection();
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
	public String getInfo() {

		StoreInfoDBConnection dbConnection=new StoreInfoDBConnection();
		ResultSet resultSet = dbConnection
				.retrieveAllData();
		String gstin="";
		try {
			while (resultSet.next()) {
				
				gstin=resultSet.getObject(3).toString();
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gstin;

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