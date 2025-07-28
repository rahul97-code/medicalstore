package hms.store.gui;

import hms.gl.database.GLAccountDBConnection;
import hms.main.DateFormatChange;
import hms.store.database.BillingDBConnection;
import hms.store.database.InvoiceDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.database.SuppliersDBConnection;
import hms.store.gui.PurchaseOrder.EditableTableModel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.barcodelib.barcode.a.g.o;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import javax.swing.ImageIcon;
import java.awt.Color;

public class SupplierGLPayments extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField searchTF;
	private JTextField mobileTF;
	private JTextField addressTF;
	final DefaultComboBoxModel supplierName = new DefaultComboBoxModel();
	private JComboBox suplierCB;
	String supplierDisplaySTR, mobileSTR, addressSTR, supplierID,
			supplierNameSTR,dateFromSTR="",dateToSTR="";
	private JTable table;
	double totalAmount=0;
	int paymetID=0;
	private JDateChooser dateFrom;
	private JDateChooser dateTo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SupplierGLPayments dialog = new SupplierGLPayments();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SupplierGLPayments() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(SupplierGLPayments.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setTitle("Supplier Payment");
		setBounds(100, 100, 957, 463);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel label = new JLabel("Search Supplier :");
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label.setBounds(35, 11, 126, 25);
		contentPanel.add(label);

		searchTF = new JTextField();
		searchTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		searchTF.setColumns(10);
		searchTF.setBounds(171, 11, 218, 25);
		contentPanel.add(searchTF);
		searchTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = searchTF.getText() + "";
				if (!str.equals("")) {
					getSupplierName(str);
				} else {

					addressTF.setText("");
					mobileTF.setText("");
					supplierName.removeAllElements();
					suplierCB.setModel(supplierName);

				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = searchTF.getText() + "";
				if (!str.equals("")) {
					getSupplierName(str);
				} else {
					addressTF.setText("");
					mobileTF.setText("");
					supplierName.removeAllElements();
					suplierCB.setModel(supplierName);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = searchTF.getText() + "";
				if (!str.equals("")) {
					getSupplierName(str);
				} else {
					addressTF.setText("");
					mobileTF.setText("");
					supplierName.removeAllElements();
					suplierCB.setModel(supplierName);
				}
			}
		});

		suplierCB = new JComboBox();
		suplierCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		suplierCB.setBounds(171, 44, 218, 25);
		contentPanel.add(suplierCB);
		suplierCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					supplierDisplaySTR = suplierCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}
				addressTF.setText("");
				mobileTF.setText("");
				
				totalAmount=0;
				getSupplierDetail(supplierDisplaySTR);
				if (supplierName.getSize() > 0) {
					addressTF.setText("" + addressSTR);
					mobileTF.setText("" + mobileSTR);
					if(!dateFromSTR.equals("")&&!dateToSTR.equals(""))
						populateExpensesTable(dateFromSTR,dateToSTR);
				}
			}
		});

		JLabel label_1 = new JLabel("Select Supplier");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_1.setBounds(35, 44, 126, 25);
		contentPanel.add(label_1);

		JLabel label_2 = new JLabel("Mobile :");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_2.setBounds(35, 90, 126, 25);
		contentPanel.add(label_2);

		mobileTF = new JTextField();
		mobileTF.setEditable(false);
		mobileTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mobileTF.setColumns(10);
		mobileTF.setBounds(171, 90, 218, 25);
		contentPanel.add(mobileTF);

		addressTF = new JTextField();
		addressTF.setEditable(false);
		addressTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		addressTF.setColumns(10);
		addressTF.setBounds(171, 126, 218, 25);
		contentPanel.add(addressTF);

		JLabel label_3 = new JLabel("Address :");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_3.setBounds(35, 126, 126, 25);
		contentPanel.add(label_3);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(399, 11, 542, 380);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setForeground(Color.BLUE);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
	//	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Credit", "Debit", "Balance", "Date", "Time", "Desc."
			}
		) );
		table.getColumnModel().getColumn(0).setMinWidth(50);
		table.getColumnModel().getColumn(1).setMinWidth(75);
		table.getColumnModel().getColumn(2).setMinWidth(75);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setMinWidth(100);
		table.getColumnModel().getColumn(5).setMinWidth(75);
		table.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				 JTable target = (JTable)arg0.getSource();
		         int row = target.getSelectedRow();
		         int column = target.getSelectedColumn();
		         // do some action
		         
		         Object selectedObject = table.getModel().getValueAt(row, 0);
		        		         

			        OrderPayments orderPayments=new OrderPayments(selectedObject+"");
			        orderPayments.setModal(true);
			        orderPayments.setVisible(true);
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				  if (arg0.getClickCount() == 2) {
				         JTable target = (JTable)arg0.getSource();
				         int row = target.getSelectedRow();
				         int column = target.getSelectedColumn();
				         // do some action
				         
				        
							
						
				  }
			}
		});
		scrollPane.setViewportView(table);
		
		dateFrom = new JDateChooser();
		dateFrom.setBounds(171, 174, 218, 25);
		contentPanel.add(dateFrom);
		dateFrom.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateFromSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							if(!dateFromSTR.equals("")&&!dateToSTR.equals(""))
								populateExpensesTable(dateFromSTR,dateToSTR);
						}
					}
				});
		
		dateTo = new JDateChooser();
		dateTo.setBounds(171, 205, 218, 25);
		contentPanel.add(dateTo);
		dateTo.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateToSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							if(!dateFromSTR.equals("")&&!dateToSTR.equals(""))
								populateExpensesTable(dateFromSTR,dateToSTR);
						}
					}
				});
		
		JLabel lblDateFrom = new JLabel("Date From :");
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDateFrom.setBounds(35, 174, 126, 25);
		contentPanel.add(lblDateFrom);
		
		JLabel lblDateTo = new JLabel("Date To :");
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDateTo.setBounds(35, 205, 126, 25);
		contentPanel.add(lblDateTo);
		
		JButton btnExcelFile = new JButton("Excel File");
		btnExcelFile.setIcon(new ImageIcon(SupplierGLPayments.class.getResource("/icons/1BL.PNG")));
		btnExcelFile.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnExcelFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					new ExcelFile("Supplier Payments", table);
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
			}
		});
		btnExcelFile.setBounds(251, 257, 138, 36);
		contentPanel.add(btnExcelFile);
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {

						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	public void populateExpensesTable(String dateFrom,String dateTo) {

		try {
			GLAccountDBConnection db = new GLAccountDBConnection();
			ResultSet rs = db.retrieveAllGLDetail(supplierDisplaySTR, "SUPPLIER", dateFromSTR, dateToSTR);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs =  db.retrieveAllGLDetail(supplierDisplaySTR, "SUPPLIER", dateFromSTR, dateToSTR);
		
			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns +2];
		
			int R = 0;
			while (rs.next()) {
				Rows_Object_Array[R][0] = rs.getObject(1).toString();
				Rows_Object_Array[R][1] = rs.getObject(2).toString();
				Rows_Object_Array[R][2] = rs.getObject(3).toString();
				Rows_Object_Array[R][3] = rs.getObject(4).toString();
				Rows_Object_Array[R][4] = rs.getObject(5).toString();
				Rows_Object_Array[R][5] = rs.getObject(6).toString();
				Rows_Object_Array[R][6] = rs.getObject(7).toString();
				R++;
			}
			DefaultTableModel model = new DefaultTableModel( Rows_Object_Array, new String[] {
					"ID", "Credit", "Debit", "Balance", "Date", "Time", "Desc." });
			
			table.setModel(model);
			table.getColumnModel().getColumn(0).setMinWidth(50);
			table.getColumnModel().getColumn(1).setMinWidth(75);
			table.getColumnModel().getColumn(2).setMinWidth(75);
			table.getColumnModel().getColumn(4).setPreferredWidth(100);
			table.getColumnModel().getColumn(4).setMinWidth(100);
			table.getColumnModel().getColumn(5).setMinWidth(75);
		//	table.setEnabled(false);
		} catch (SQLException ex) {

			System.out.println(ex);
		}
	}
	
	public void getSupplierName(String index) {

		SuppliersDBConnection suppliersDBConnection = new SuppliersDBConnection();
		ResultSet resultSet = suppliersDBConnection
				.searchSupplierWithIdOrNmae(index);
		supplierName.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {
				supplierName.addElement(resultSet.getObject(3).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		suppliersDBConnection.closeConnection();
		suplierCB.setModel(supplierName);
		if (i > 0) {
			suplierCB.setSelectedIndex(0);
		}
	}
	public void getSupplierDetail(String index) {

		SuppliersDBConnection suppliersDBConnection = new SuppliersDBConnection();
		ResultSet resultSet = suppliersDBConnection
				.searchSupplierWithIdOrNmae(index);
		try {
			while (resultSet.next()) {

				supplierID = resultSet.getObject(1).toString();
				supplierNameSTR = resultSet.getObject(2).toString();
				mobileSTR = (resultSet.getObject(4).toString());
				addressSTR = (resultSet.getObject(6).toString() + ", "
						+ resultSet.getObject(7).toString() + ", " + resultSet
						.getObject(8).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		suppliersDBConnection.closeConnection();
	}
}
