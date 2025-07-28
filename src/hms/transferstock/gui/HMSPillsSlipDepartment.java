package hms.transferstock.gui;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import hms.patient.slippdf.OpenFile;
import hms.store.gui.StoreMain;

public class HMSPillsSlipDepartment {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 5);
	private static Font speratorLine = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD, BaseColor.RED);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.DARK_GRAY);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.WHITE);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 7,
			Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 7f,
			Font.BOLD);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.WHITE);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 7f,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "opdslip1.pdf";

	Vector<String> doctorname = new Vector<String>();
	Vector<String> achievements = new Vector<String>();
	Vector<String> specialization = new Vector<String>();

	String mainDir = "";
	Font font;
	String[] open=new String[4];

	/** 
	 * @param expiryDateV 
	 * @param itemBatchV 
	 * @param itemPriceV 
	 * @param person 
	 * @param exam_id
	 * @param examnames
	 * @param examRates
	 * @param totalAmountSTR
	 * @throws DocumentException
	 * @throws IOException
	 */
	
	
	public HMSPillsSlipDepartment(Vector<String> itemIDV,Vector<String> itemIDVHMS,String deptName,Vector<String> itemName,Vector<String> itemNameVHMS,Vector<String> quantity,int index, Vector<String> itemBatchV, Vector<String> expiryDateV, Vector<String> itemPriceV, String person) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
	
		readFile();
		makeDirectory(""+index);
		Document document = new Document();
		
		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		document.setPageSize(PageSize.LETTER);
		document.setMargins(0, 0, 50, 0);
		document.open();
		
	     BaseFont base = BaseFont.createFont("indian.ttf", BaseFont.WINANSI,
					BaseFont.EMBEDDED);
			font = new Font(base, 9f);
		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(40);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		float[] tiltelTablCellWidth = { 0.1f, 1f };
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);

		java.net.URL imgURL = HMSPillsSlipDepartment.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		java.net.URL imgURL2 = HMSPillsSlipDepartment.class
				.getResource("/icons/footer.PNG");
		Image image2 = Image.getInstance(imgURL2);
	
		image.scalePercent(30);
		image.setAbsolutePosition(40, 750);

		PdfPCell logocell = new PdfPCell(image);
		logocell.setRowspan(3);
		logocell.setBorder(Rectangle.NO_BORDER);
		logocell.setHorizontalAlignment(Element.ALIGN_CENTER);
		logocell.setPaddingRight(5);
		TitleTable.addCell(logocell);

		PdfPCell namecell = new PdfPCell(new Phrase(
				"ROTARY AMBALA CANCER AND GENERAL HOSPITAL" + "\n", font1));
		namecell.setHorizontalAlignment(Element.ALIGN_CENTER);
		namecell.setPaddingBottom(3);
		namecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		TitleTable.addCell(namecell);

		PdfPCell addressCell = new PdfPCell(new Phrase(
				"GROUND FLOOR ROTARY AMBALA CANCER & GENERAL HOSPITAL AMBALA CANTT-133001 (Haryana)",
				font2));
		addressCell.setPaddingBottom(1);
		addressCell.setBorder(Rectangle.NO_BORDER);
		addressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell);

		PdfPCell addressCell2 = new PdfPCell(
				new Phrase(
						"Telephone No. : 0171-2690009, Mobile No. : 09034056793",
						font2));
		addressCell2.setPaddingBottom(2);
		addressCell2.setBorder(Rectangle.NO_BORDER);
		addressCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell2);

		table.addCell(TitleTable);

		PdfPCell cashReceipt = new PdfPCell(new Paragraph("MS++ TO DEPARTMENT RECEIPT ",
				spaceFont));
		cashReceipt.setBorder(Rectangle.NO_BORDER);
		cashReceipt.setHorizontalAlignment(Element.ALIGN_CENTER);
		cashReceipt.setPaddingBottom(3);
		table.addCell(cashReceipt);
		//
		float[] opdTablCellWidth = { 1f, 2f, 1.2f, 1.2f };
		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		opdTable.getDefaultCell().setBorder(0);

		PdfPCell tokencell = new PdfPCell(new Phrase("Receipt No. :" + "\n",
				font3));
		tokencell.setBorder(Rectangle.NO_BORDER);
		tokencell.setPaddingBottom(4);

		PdfPCell tokenNocell = new PdfPCell(new Phrase( ""+index, font3));
		tokenNocell.setBorder(Rectangle.NO_BORDER);
		tokenNocell.setPaddingBottom(4);

		opdTable.addCell(tokencell);
		opdTable.addCell(tokenNocell);

		opdTable.addCell(new Phrase("Dept Name : ", font3));
		opdTable.addCell(new Phrase("" + deptName, font3));
		

		

		PdfPCell opdCell = new PdfPCell(opdTable);
		opdCell.setBorderWidth(0.8f);
		table.addCell(opdCell);

		long timeInMillis = System.currentTimeMillis();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(timeInMillis);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		PdfPCell space = new PdfPCell(new Paragraph("Date : "
				+ dateFormat.format(cal1.getTime()), font3));
		space.setBorder(Rectangle.NO_BORDER);
		space.setHorizontalAlignment(Element.ALIGN_RIGHT);
		space.setPaddingBottom(6);
		space.setPaddingTop(4);
		table.addCell(space);

		float[] examTableWidth = { 0.7f,0.7f, 1.7f,1.7f,0.8f,0.8f, 0.7f,1.0f};
		PdfPTable examtable = new PdfPTable(examTableWidth);

		examtable.addCell(new Phrase("MS++ ID", font3));
		examtable.addCell(new Phrase("HMS ID", font3));
		examtable.addCell(new Phrase("MS ++ Description", font3));
		examtable.addCell(new Phrase("HMS Description", font3));
		examtable.addCell(new Phrase("Item Batch", font3));
		examtable.addCell(new Phrase("Item Exp", font3));
		examtable.addCell(new Phrase("Qty", font3));
		examtable.addCell(new Phrase("Amount", font3));
		
		for (int i = 0; i < itemName.size(); i++) {
			examtable.addCell(new Phrase(""+itemIDV.get(i), font3));
			examtable.addCell(new Phrase(""+itemIDVHMS.get(i), font3));
			examtable.addCell(new Phrase(""+itemName.get(i), font3));
			examtable.addCell(new Phrase(""+itemNameVHMS.get(i), font3));
			String str = itemBatchV.get(i);
			String[] arrOfStr = str.split("\\(", 2);
			for (String a : arrOfStr)
			{
				examtable.addCell(new Phrase(""+a, font3));
				break;
			}
			
			examtable.addCell(new Phrase(""+expiryDateV.get(i), font3));
			examtable.addCell(new Phrase(""+quantity.get(i), font3));
			examtable.addCell(new Phrase(""+itemPriceV.get(i), font3));
		}
		table.addCell(examtable);

		

		PdfPCell username = new PdfPCell(new Paragraph("Generated By : "+StoreMain.userName, font3));
		username.setBorder(Rectangle.NO_BORDER);
		username.setHorizontalAlignment(Element.ALIGN_LEFT);
		username.setPaddingTop(15);
		PdfPCell person1 = new PdfPCell(new Paragraph("Received By : "+person, font3));
		person1.setBorder(Rectangle.NO_BORDER);
		person1.setHorizontalAlignment(Element.ALIGN_LEFT);
		person1.setPaddingTop(1);
		PdfPCell signature = new PdfPCell(new Paragraph("Signature", font3));
		signature.setBorder(Rectangle.NO_BORDER);
		signature.setHorizontalAlignment(Element.ALIGN_RIGHT);
		signature.setPaddingTop(15);
		table.addCell(username);
		table.addCell(person1);
		table.addCell(signature);
		table.addCell(image2);

		float[] table2w = { 1f, 0.1f, 1f };
		PdfPTable table2 = new PdfPTable(table2w);
		table2.getDefaultCell().setBorder(0);
		table2.setWidthPercentage(94);

		PdfPCell call1 = new PdfPCell(table);
		call1.setBorder(Rectangle.NO_BORDER);
		call1.setHorizontalAlignment(Element.ALIGN_CENTER);

		table2.addCell(call1);

		PdfPCell call3 = new PdfPCell(
				new Paragraph(
						"   - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - - - - - - - - - -   ",
						speratorLine));
		call3.setBorder(Rectangle.NO_BORDER);
		call3.setRotation(270);
		call3.setPaddingRight(7);
		call3.setPaddingLeft(5);
		call3.setHorizontalAlignment(Element.ALIGN_CENTER);

		table2.addCell(call3);

		PdfPCell cell2 = new PdfPCell(table);
		cell2.setBorder(Rectangle.NO_BORDER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell2);

		document.add(table2);
		document.close();

		new OpenFile(RESULT);
	}


	public void makeDirectory(String slipNo) {

		new File("DeptIssuePillSlips").mkdir();
		RESULT = "DeptIssuePillSlips/" + slipNo + ".pdf";
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
			open[0]=data[2];
			open[1]=data[3];
			open[2]=data[4];
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