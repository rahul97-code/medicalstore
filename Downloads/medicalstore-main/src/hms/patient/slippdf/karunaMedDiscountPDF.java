package hms.patient.slippdf;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.itextpdf.text.BadElementException;
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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import hms.store.database.BillingDBConnection;

public class karunaMedDiscountPDF {

    private static final String RESULT_FILE_PATH = "opdslip1.pdf";
    private static final Font FONT_HELVETICA_11_BOLD = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
    private static final Font FONT_HELVETICA_10_BOLD = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    private static final Font FONT_HELVETICA_9 = new Font(Font.FontFamily.HELVETICA, 9);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        try {
            generateReport("2024-06-24", "2021-06-15");
        } catch (DocumentException | IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void generateReport(String fromDate, String toDate) throws DocumentException, IOException, SQLException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(RESULT_FILE_PATH));
        document.open();

        addHeader(document);
        addDateRange(document, fromDate, toDate);
        space(document);
        addItemsNotSoldTable(document, fromDate, toDate);

        document.close();
        openPdfFile(RESULT_FILE_PATH);
    }

    private static void addHeader(Document document) throws DocumentException {
        Paragraph header = new Paragraph("Karuna Medicine Discount Report", FONT_HELVETICA_11_BOLD);
        header.setAlignment(Element.ALIGN_CENTER);
        PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(90);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        float[] tiltelTablCellWidth = { 0.1f, 1f, 0.1f };
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);

		java.net.URL imgURL = HighRiskItemPDF.class
				.getResource("/icons/rotaryLogo.png");
		Image image = null;
		try {
			image = Image.getInstance(imgURL);
		} catch (BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = HighRiskItemPDF.class
				.getResource("/icons/Rotary-Club-logo.jpg");
		Image imageRotaryClub = null;
		try {
			imageRotaryClub = Image.getInstance(imgURLRotaryClub);
		} catch (BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// imageRotaryClub.scalePercent(60);
		// imageRotaryClub.setAbsolutePosition(40, 750);

		PdfPCell logocell2 = new PdfPCell(imageRotaryClub);
		logocell2.setRowspan(3);
		logocell2.setBorder(Rectangle.NO_BORDER);
		logocell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		logocell2.setPaddingRight(5);
		TitleTable.addCell(logocell2);

		PdfPCell namecell = new PdfPCell(new Phrase(
				"DR. JAI DEV MEMORIAL ROTARY MEDICAL STORE" + "\n", FONT_HELVETICA_9));
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
				FONT_HELVETICA_9));
		addressCell.setPaddingBottom(2);
		addressCell.setBorder(Rectangle.NO_BORDER);
		addressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell);

		PdfPCell addressCell2 = new PdfPCell(
				new Phrase(
						"Mobile No. : 09034056793",
						FONT_HELVETICA_9));
		addressCell2.setPaddingBottom(1);
		addressCell2.setBorder(Rectangle.NO_BORDER);
		addressCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell2);

		table.addCell(TitleTable);
		table.addCell(header);
		document.add(table);
       // document.add(header);
    }

    private static void addDateRange(Document document, String fromDate, String toDate) throws DocumentException {
        Paragraph dateRange = new Paragraph("Date Range: " + formatDate(fromDate) + " to " + formatDate(toDate), FONT_HELVETICA_11_BOLD);
        dateRange.setAlignment(Element.ALIGN_CENTER);
        document.add(dateRange);
        
    }
    private static void space(Document document) throws DocumentException {
    	Paragraph dateRange = new Paragraph("-----------------------------------------------------------------------", FONT_HELVETICA_11_BOLD);
         dateRange.setAlignment(Element.ALIGN_CENTER);
         document.add(dateRange);
    }

    private static String formatDate(String dateStr) {
        try {
            Date date = DATE_FORMAT.parse(dateStr);
            return new SimpleDateFormat("dd MMMM, yyyy").format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return dateStr; // Return original string if parsing fails
        }
    }

    private static void addItemsNotSoldTable(Document document, String fromDate, String toDate) throws DocumentException, SQLException {
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 2, 2, 2, 2, 2, 2});
        table.addCell(new PdfPCell(new Phrase("S.No", FONT_HELVETICA_10_BOLD)));
        table.addCell(new PdfPCell(new Phrase("OPD ID", FONT_HELVETICA_10_BOLD)));
        table.addCell(new PdfPCell(new Phrase("Patient Name", FONT_HELVETICA_10_BOLD)));
        table.addCell(new PdfPCell(new Phrase("Doctor Name", FONT_HELVETICA_10_BOLD)));
        table.addCell(new PdfPCell(new Phrase("Total Amount", FONT_HELVETICA_10_BOLD)));
        table.addCell(new PdfPCell(new Phrase("Date", FONT_HELVETICA_10_BOLD)));
        table.addCell(new PdfPCell(new Phrase("Discount ", FONT_HELVETICA_10_BOLD)));
//        table.addCell(new PdfPCell(new Phrase("Item Date", FONT_HELVETICA_10_BOLD)));

        try  {
        	BillingDBConnection con= new BillingDBConnection();
            ResultSet resultSet = con.karunaMeddiscountreport(fromDate, toDate);
            int s=0;
            while (resultSet.next()) {
                table.addCell(new PdfPCell(new Phrase(String.valueOf(++s),FONT_HELVETICA_9)));
                table.addCell(new PdfPCell(new Phrase(resultSet.getObject(1).toString(), FONT_HELVETICA_9)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(resultSet.getObject(2).toString()), FONT_HELVETICA_9)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(resultSet.getString(3)), FONT_HELVETICA_9)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(resultSet.getString(4)), FONT_HELVETICA_9)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(resultSet.getString(5)), FONT_HELVETICA_9)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(resultSet.getString(6)), FONT_HELVETICA_9)));
//             table.addCell(new PdfPCell(new Phrase(String.valueOf(resultSet.getString("date")), FONT_HELVETICA_9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception to handle at higher level
        }

        document.add(table);
    }

    private static void openPdfFile(String filePath) {
        try {
            Desktop.getDesktop().open(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
