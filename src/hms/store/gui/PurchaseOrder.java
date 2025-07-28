package hms.store.gui;

import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.BillingDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.database.PurchaseOrderDBConnection;
import hms.store.database.SuppliersDBConnection;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.itextpdf.text.DocumentException;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class PurchaseOrder extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable purchaseItemsTable;
	String fromDateSTR1,toDateSTR1;
	String fromDateSTR3,toDateSTR3;
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel supplier = new DefaultComboBoxModel();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PurchaseOrder dialog = new PurchaseOrder();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PurchaseOrder() {
		setBounds(100, 100, 1148, 556);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		fromDateSTR1=DateFormatChange.StringToMysqlDate(DateFormatChange.addMonths(new Date(),-1));
		toDateSTR1=DateFormatChange.StringToMysqlDate(new Date());
		fromDateSTR3=DateFormatChange.StringToMysqlDate(DateFormatChange.addMonths(new Date(),-3));
		toDateSTR3=DateFormatChange.StringToMysqlDate(new Date());
		JPanel panel = new JPanel();
		panel.setBounds(10, 57, 1112, 399);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 1092, 377);
		panel.add(scrollPane);
		
		purchaseItemsTable = new JTable();
		purchaseItemsTable.getTableHeader().setReorderingAllowed(false);
		purchaseItemsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		purchaseItemsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		purchaseItemsTable.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, "", null, null, null, null, null, "", null, null},
			},
			new String[] {
				"S.N.", "Item ID", "Item Name", "Item Desc", "Brand Name", "Doctor", "Vendor", "Total Stock", "Last Month", "Last Three Month", "Reorder Level", "Order Qty.", "Order Status"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, true, true, false, false, false, false, false, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		purchaseItemsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		purchaseItemsTable.getColumnModel().getColumn(0).setMinWidth(50);
		purchaseItemsTable.getColumnModel().getColumn(1).setResizable(false);
		purchaseItemsTable.getColumnModel().getColumn(1).setPreferredWidth(50);
		purchaseItemsTable.getColumnModel().getColumn(1).setMinWidth(50);
		purchaseItemsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
		purchaseItemsTable.getColumnModel().getColumn(2).setMinWidth(150);
		purchaseItemsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		purchaseItemsTable.getColumnModel().getColumn(3).setMinWidth(100);
		purchaseItemsTable.getColumnModel().getColumn(4).setResizable(false);
		purchaseItemsTable.getColumnModel().getColumn(4).setPreferredWidth(90);
		purchaseItemsTable.getColumnModel().getColumn(4).setMinWidth(90);
		purchaseItemsTable.getColumnModel().getColumn(5).setMinWidth(100);
		purchaseItemsTable.getColumnModel().getColumn(6).setMinWidth(100);
		purchaseItemsTable.getColumnModel().getColumn(7).setResizable(false);
		purchaseItemsTable.getColumnModel().getColumn(8).setResizable(false);
		purchaseItemsTable.getColumnModel().getColumn(9).setResizable(false);
		purchaseItemsTable.getColumnModel().getColumn(10).setResizable(false);
		purchaseItemsTable.getColumnModel().getColumn(11).setResizable(false);
		purchaseItemsTable.getColumnModel().getColumn(12).setResizable(false);
		purchaseItemsTable.setFont(new Font("Tahoma", Font.BOLD, 13));
		scrollPane.setViewportView(purchaseItemsTable);
		
		JLabel lblPurchaseOreder = new JLabel("Purchase Order");
		lblPurchaseOreder.setFont(new Font("Tahoma", Font.BOLD, 33));
		lblPurchaseOreder.setBounds(10, 11, 369, 35);
		contentPanel.add(lblPurchaseOreder);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnCancel.setBounds(947, 460, 175, 47);
		contentPanel.add(btnCancel);
		
		JButton btnNewButton_1 = new JButton("Save & Generate");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				insertPurchaseOrder();
				try {
					PurchaseOrderExcel();
				} catch (DocumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton_1.setBounds(762, 460, 175, 47);
		contentPanel.add(btnNewButton_1);
		getAllDoctors();
		getAllSuppliers();
		populateExpensesTable();
	}
	

public void populateExpensesTable() {

	try {
		BillingDBConnection db = new BillingDBConnection();
		ResultSet rs = db.retrieveAllIssuedPillsNew(fromDateSTR3,toDateSTR3,fromDateSTR1,toDateSTR1);

		// System.out.println("Table: " + rs.getMetaData().getTableName(1));
		int NumberOfColumns = 0, NumberOfRows = 0;
		NumberOfColumns = rs.getMetaData().getColumnCount();

		while (rs.next()) {
			NumberOfRows++;
		}
		rs = db.retrieveAllIssuedPillsNew(fromDateSTR3,toDateSTR3,fromDateSTR1,toDateSTR1);
		System.out.println(NumberOfColumns);
		// to set rows in this array
		Object Rows_Object_Array[][];
		Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns+4];

		int R = 0;
		while (rs.next()) {
			Rows_Object_Array[R][0]= R+1+"" ;
			Rows_Object_Array[R][1] = rs.getObject(1).toString();
			Rows_Object_Array[R][2] = rs.getObject(2).toString();
			Rows_Object_Array[R][3] = rs.getObject(3).toString();
			Rows_Object_Array[R][4] = rs.getObject(4).toString();
			Rows_Object_Array[R][5]= "Store Stock" ;
			Rows_Object_Array[R][6]= "Select Supplier" ;
			Rows_Object_Array[R][7] = rs.getObject(5).toString();
			Rows_Object_Array[R][8] = rs.getObject(6).toString();
			Rows_Object_Array[R][9] = rs.getObject(7).toString();
			Rows_Object_Array[R][10] = rs.getObject(8).toString();
			Rows_Object_Array[R][11] = rs.getObject(9).toString();
		
			Rows_Object_Array[R][12]= new Boolean(true) ;
			R++;
		}
		
		TableModel model1 = new EditableTableModel( new String[] { "S.N.", "Item ID", "Item Name", "Item Desc", "Brand Name", "Doctor", "Vendor", "Total Stock", "Last Month", "Last Three Month", "Reorder Level", "Order Qty.", "Order Status"},Rows_Object_Array);
		purchaseItemsTable.setModel(model1);
		purchaseItemsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		purchaseItemsTable.getColumnModel().getColumn(0).setMinWidth(50);
		purchaseItemsTable.getColumnModel().getColumn(1).setResizable(false);
		purchaseItemsTable.getColumnModel().getColumn(1).setPreferredWidth(50);
		purchaseItemsTable.getColumnModel().getColumn(1).setMinWidth(50);
		purchaseItemsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
		purchaseItemsTable.getColumnModel().getColumn(2).setMinWidth(150);
		purchaseItemsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		purchaseItemsTable.getColumnModel().getColumn(3).setMinWidth(100);
		purchaseItemsTable.getColumnModel().getColumn(4).setResizable(false);
		purchaseItemsTable.getColumnModel().getColumn(4).setPreferredWidth(90);
		purchaseItemsTable.getColumnModel().getColumn(4).setMinWidth(90);
		purchaseItemsTable.getColumnModel().getColumn(5).setMinWidth(100);
		purchaseItemsTable.getColumnModel().getColumn(6).setMinWidth(100);
		purchaseItemsTable.getColumnModel().getColumn(7).setResizable(false);
		purchaseItemsTable.getColumnModel().getColumn(8).setResizable(false);
		purchaseItemsTable.getColumnModel().getColumn(9).setResizable(false);
		purchaseItemsTable.getColumnModel().getColumn(10).setResizable(false);
		purchaseItemsTable.getColumnModel().getColumn(11).setResizable(false);
		purchaseItemsTable.getColumnModel().getColumn(12).setResizable(false);
		JComboBox comboBox = new JComboBox(doctors);
		purchaseItemsTable.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(comboBox));
		
		JComboBox comboBox2 = new JComboBox(supplier);
		purchaseItemsTable.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(comboBox2));
		purchaseItemsTable.setFont(new Font("Tahoma", Font.BOLD, 13));
		purchaseItemsTable.setFont(new Font("Tahoma", Font.BOLD, 13));
		
	} catch (SQLException ex) {
		
	}
}
public void PurchaseOrderExcel() throws DocumentException, IOException {
	// TODO Auto-generated constructor stub
	

	try {
		File desktop = new File(System.getProperty("user.home"), "Desktop");
		String filename = desktop + "/" + "Purchase Order" + "-"
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
						"Excel File Generated Successfully on your Desktop with IssuedRegister -"
								+ DateFormatChange.StringToMysqlDate(new Date())+" Name",
						"Data Saved", JOptionPane.INFORMATION_MESSAGE);
		dispose();
	} catch (Exception ex) {
		System.out.println(ex);
	}
}

public void issuedRegister( HSSFSheet sheet) {
	EditableTableModel model1 = (EditableTableModel) purchaseItemsTable.getModel();
	ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
	HSSFRow rowhead0 = sheet.createRow((short) 0);
	
		rowhead0.createCell(0).setCellValue("Purchase Order");
		rowhead0.createCell(1).setCellValue("");
		rowhead0.createCell(2).setCellValue("-----");
		rowhead0.createCell(3).setCellValue("Generated Date");
		rowhead0.createCell(4).setCellValue(""+toDateSTR1);
	int k = 2;
	try {
		
		HSSFRow rowhead = sheet.createRow((short) 1);
		for (int i = 0; i <  12; i++) {
			rowhead.createCell(i).setCellValue(model1.getColumnName(i).toString());
		}
		List<String> numdata = new ArrayList<String>();
        for (int count = 0; count < model1.getRowCount(); count++){
              numdata.add(model1.getValueAt(count, 12).toString());
            
              Boolean b = Boolean.valueOf(model1.getValueAt(count, 12).toString());
              if(b)
              {
            	HSSFRow rowhead1 = sheet.createRow((short) k);
  				for (int i = 0; i < 12; i++) {
  					rowhead1.createCell(i).setCellValue(model1.getValueAt(count,i).toString());
  				}
  				k++;  
  				itemsDBConnection.updateOrderStaus(model1.getValueAt(count,1).toString(), "YES", model1.getValueAt(count,11).toString());
              }
            
        }
	} catch (Exception ex) {
		System.out.print(""+ex);
	}
	itemsDBConnection.closeConnection();
}
public void insertPurchaseOrder()
{
	EditableTableModel model1 = (EditableTableModel) purchaseItemsTable.getModel();
	PurchaseOrderDBConnection purchaseOrderDBConnection=new PurchaseOrderDBConnection();
	String[] data = new String[30];
	long timeInMillis = System.currentTimeMillis();
	Calendar cal1 = Calendar.getInstance();
	cal1.setTimeInMillis(timeInMillis);
	SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
	 for (int count = 0; count < model1.getRowCount(); count++){
         
       
         Boolean b = Boolean.valueOf(model1.getValueAt(count, 12).toString());
         if(b)
         {
				for (int i = 1; i < 12; i++) {
					data[i-1] = model1.getValueAt(count,i).toString();
				}
				data[11] = "" + DateFormatChange.StringToMysqlDate(new Date());
				data[12] = "" + timeFormat.format(cal1.getTime());
				data[13]=""+StoreMain.userID;
				data[14]=""+StoreMain.userName;
				try {
					purchaseOrderDBConnection.insertPurchaseOrder(data);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

         }
       
   }
	purchaseOrderDBConnection.closeConnection();
}
public void getAllDoctors() {
	doctors.addElement("Store Stock");
	DoctorDBConnection dbConnection = new DoctorDBConnection();
	ResultSet resultSet = dbConnection.retrieveAllData();
	try {
		while (resultSet.next()) {
			doctors.addElement(resultSet.getObject(2).toString());
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	dbConnection.closeConnection();

}
public void getAllSuppliers() {
	supplier.addElement("Select Supplier");
	SuppliersDBConnection db = new SuppliersDBConnection();
	ResultSet rs = db.retrieveAllData2();
	try {
		while (rs.next()) {
			supplier.addElement(rs.getObject(2).toString());
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	db.closeConnection();

}


class EditableTableModel extends AbstractTableModel {
	  String[] columnTitles;

	  Object[][] dataEntries;

	  int rowCount;

	  public EditableTableModel(String[] columnTitles, Object[][] dataEntries) {
	    this.columnTitles = columnTitles;
	    this.dataEntries = dataEntries;
	  }

	  public int getRowCount() {
	    return dataEntries.length;
	  }

	  public int getColumnCount() {
	    return columnTitles.length;
	  }

	  public Object getValueAt(int row, int column) {
	    return dataEntries[row][column];
	  }

	  public String getColumnName(int column) {
	    return columnTitles[column];
	  }

	  public Class getColumnClass(int column) {
	    return getValueAt(0, column).getClass();
	  }

	  public boolean isCellEditable(int row, int column) {
	    return true;
	  }

	  public void setValueAt(Object value, int row, int column) {
	    dataEntries[row][column] = value;
	  }
}

}
