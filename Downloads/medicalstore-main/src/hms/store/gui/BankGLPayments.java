package hms.store.gui;

import hms.gl.database.GLAccountDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
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

public class BankGLPayments extends JDialog {

	private final JPanel contentPanel = new JPanel();
	final DefaultComboBoxModel bankModel = new DefaultComboBoxModel();
	private JComboBox bankCB;
	String insuranceDisplaySTR, mobileSTR, addressSTR, supplierID,
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
			BankGLPayments dialog = new BankGLPayments();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public BankGLPayments() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(BankGLPayments.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setTitle("Bank Payment");
		setBounds(100, 100, 583, 484);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		bankCB = new JComboBox();
		bankCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bankCB.setBounds(170, 11, 218, 25);
		contentPanel.add(bankCB);
		bankCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					insuranceDisplaySTR = bankCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}
				if(!dateFromSTR.equals("")&&!dateToSTR.equals(""))
					populateExpensesTable(dateFromSTR,dateToSTR);
				
			}
		});

		JLabel lblSelectInsurance = new JLabel("Select Bank");
		lblSelectInsurance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSelectInsurance.setBounds(34, 11, 126, 25);
		contentPanel.add(lblSelectInsurance);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(29, 127, 538, 288);
		contentPanel.add(scrollPane);

		table = new JTable();
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
		        		         
//
//			        OrderPayments orderPayments=new OrderPayments(selectedObject+"");
//			        orderPayments.setModal(true);
//			        orderPayments.setVisible(true);
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
		dateFrom.setBounds(170, 52, 218, 20);
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
		dateTo.setBounds(170, 83, 218, 20);
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
		lblDateFrom.setBounds(34, 47, 126, 25);
		contentPanel.add(lblDateFrom);
		
		JLabel lblDateTo = new JLabel("Date To :");
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDateTo.setBounds(34, 78, 126, 25);
		contentPanel.add(lblDateTo);
		
		JButton btnNewButton = new JButton("Excel File");
		btnNewButton.setIcon(new ImageIcon(BankGLPayments.class.getResource("/icons/1BL.PNG")));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new ExcelFile("Bank Payments", table);
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
			}
		});
		btnNewButton.setBounds(431, 75, 115, 35);
		contentPanel.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Report");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setIcon(new ImageIcon(BankGLPayments.class.getResource("/icons/list_dialog.png")));
		lblNewLabel.setBounds(431, 11, 115, 59);
		contentPanel.add(lblNewLabel);
		
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
		getBank();
	}
	public void populateExpensesTable(String dateFrom,String dateTo) {

		try {
			GLAccountDBConnection db = new GLAccountDBConnection();
			ResultSet rs = db.retrieveAllGLDetail(insuranceDisplaySTR, "BANK", dateFromSTR, dateToSTR);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs =  db.retrieveAllGLDetail(insuranceDisplaySTR, "BANK", dateFromSTR, dateToSTR);
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
	
	public void getBank() {
		bankModel.addElement("Yes Bank");
//		InsuranceDBConnection dbConnection = new InsuranceDBConnection();
//		ResultSet resultSet = dbConnection.retrieveAllData();
//		try {
//			while (resultSet.next()) {
//				bankModel.addElement(resultSet.getObject(2).toString());
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		dbConnection.closeConnection();
		bankCB.setModel(bankModel);
		bankCB.setSelectedIndex(0);
	}
}
