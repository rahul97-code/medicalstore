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

import com.toedter.calendar.JDateChooser;

public class SupplierPayments extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField searchTF;
	private JTextField mobileTF;
	private JTextField addressTF;
	final DefaultComboBoxModel supplierName = new DefaultComboBoxModel();
	private JComboBox suplierCB;
	String supplierDisplaySTR, mobileSTR, addressSTR, supplierID,
			supplierNameSTR,dateSTR="";
	private JTextField paymentTF;
	private JTable table;
	private JLabel totalAmountLB;
	double totalAmount = 0, amountAdjustmentDouble = 0,finalAmount=0;
	int paymetID = 0;
	private JTextField amountAdjustment;
	private JDateChooser selectDate;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SupplierPayments dialog = new SupplierPayments();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SupplierPayments() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				SupplierPayments.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setTitle("Supplier Payment");
		setBounds(100, 100, 826, 463);
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
				table.setModel(new DefaultTableModel(new Object[][] { { null,
						null, null, null, null, null, null }, }, new String[] {
						"ID", "Invoice No.", "Order No.", "Tax", "Amount",
						"Date", "Select" }) {
					boolean[] columnEditables = new boolean[] { false, false,
							false, false, false, false, true };

					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
				});
				totalAmount = 0;
				totalAmountLB.setText(totalAmount + "");
				getSupplierDetail(supplierDisplaySTR);
				if (supplierName.getSize() > 0) {
					addressTF.setText("" + addressSTR);
					mobileTF.setText("" + mobileSTR);
					populateExpensesTable();
				}
			}
		});

		JLabel label_1 = new JLabel("Select Supplier");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_1.setBounds(35, 44, 126, 25);
		contentPanel.add(label_1);

		JLabel label_2 = new JLabel("Mobile :");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_2.setBounds(418, 8, 126, 25);
		contentPanel.add(label_2);

		mobileTF = new JTextField();
		mobileTF.setEditable(false);
		mobileTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mobileTF.setColumns(10);
		mobileTF.setBounds(554, 8, 218, 25);
		contentPanel.add(mobileTF);

		addressTF = new JTextField();
		addressTF.setEditable(false);
		addressTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		addressTF.setColumns(10);
		addressTF.setBounds(554, 44, 218, 25);
		contentPanel.add(addressTF);

		JLabel label_3 = new JLabel("Address :");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_3.setBounds(418, 44, 126, 25);
		contentPanel.add(label_3);

		JLabel lblPayment = new JLabel("Payment Description");
		lblPayment.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPayment.setBounds(171, 80, 207, 25);
		contentPanel.add(lblPayment);

		paymentTF = new JTextField();
		paymentTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		paymentTF.setColumns(10);
		paymentTF.setBounds(388, 80, 384, 25);
		contentPanel.add(paymentTF);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 138, 790, 204);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(new Object[][] { { null, null,
				null, null, null, null, null }, }, new String[] { "ID",
				"Invoice No.", "Order No.", "Tax", "Amount", "Date", "Select" }) {
			boolean[] columnEditables = new boolean[] { false, false, false,
					false, false, false, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				JTable target = (JTable) arg0.getSource();
				int row = target.getSelectedRow();
				int column = target.getSelectedColumn();
				// do some action

				Object selectedObject = table.getModel().getValueAt(row, 0);

				amount();
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
					JTable target = (JTable) arg0.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					// do some action

					Object selectedObject = table.getModel().getValueAt(row, 0);

				}
			}
		});
		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(4).setMinWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setMinWidth(150);
		scrollPane.setViewportView(table);

		JLabel lblTotalAmount = new JLabel("Total Amount");
		lblTotalAmount.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTotalAmount.setBounds(543, 348, 119, 33);
		contentPanel.add(lblTotalAmount);

		totalAmountLB = new JLabel("");
		totalAmountLB.setFont(new Font("Tahoma", Font.BOLD, 14));
		totalAmountLB.setBounds(670, 348, 119, 33);
		contentPanel.add(totalAmountLB);

		amountAdjustment = new JTextField();
		amountAdjustment.setBounds(266, 356, 176, 25);
		contentPanel.add(amountAdjustment);
		amountAdjustment.setColumns(10);
		amountAdjustment.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE)
						|| (vChar == KeyEvent.VK_DELETE) || vChar == '.'|| vChar == '-')) {
					e.consume();

					// ||vChar== '.'
				}
			}
		});

		amountAdjustment.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = amountAdjustment.getText() + "";
						if (!str.equals("")&&!str.equals("-")) {

							amountAdjustmentDouble = Double.parseDouble(
									str);
							finalAmount = totalAmount + amountAdjustmentDouble;
							finalAmount = Math.round(finalAmount * 100.000) / 100.000;
							totalAmountLB.setText(finalAmount + "");

						} else {

							amountAdjustmentDouble = 0;
							finalAmount = totalAmount + amountAdjustmentDouble;
							finalAmount = Math.round(finalAmount * 100.000) / 100.000;
							totalAmountLB.setText(finalAmount + "");
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = amountAdjustment.getText() + "";
						if (!str.equals("")&&!str.equals("-")) {


							amountAdjustmentDouble = Double.parseDouble(str);
							finalAmount = totalAmount + amountAdjustmentDouble;
							finalAmount = Math.round(finalAmount * 100.000) / 100.000;
							totalAmountLB.setText(finalAmount + "");

						} else {

							amountAdjustmentDouble = 0;
							finalAmount = totalAmount + amountAdjustmentDouble;
							finalAmount = Math.round(finalAmount * 100.000) / 100.000;
							totalAmountLB.setText(finalAmount + "");
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = amountAdjustment.getText() + "";
						if (!str.equals("")&&!str.equals("-")) {

							amountAdjustmentDouble = Double.parseDouble( str);
							finalAmount = totalAmount + amountAdjustmentDouble;
							finalAmount = Math.round(finalAmount * 100.000) / 100.000;
							totalAmountLB.setText(finalAmount + "");

						} else {

							amountAdjustmentDouble = 0;
							finalAmount = totalAmount + amountAdjustmentDouble;
							finalAmount = Math.round(finalAmount * 100.000) / 100.000;
							totalAmountLB.setText(finalAmount + "");
						}
					}
				});
		JLabel lblAmountAdujstment = new JLabel("Amount Adujstment");
		lblAmountAdujstment.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAmountAdujstment.setBounds(71, 353, 169, 33);
		contentPanel.add(lblAmountAdujstment);
		
		selectDate = new JDateChooser();
		selectDate.setBounds(388, 116, 218, 20);
		contentPanel.add(selectDate);
		
		selectDate.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							
						}
					}
				});
		
		JLabel label_4 = new JLabel("Select Date");
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_4.setBounds(171, 114, 126, 25);
		contentPanel.add(label_4);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {

						if (addressTF.getText().toString().equals("")
								|| paymentTF.getText().toString().equals("")||dateSTR.equals("")) {
							JOptionPane.showMessageDialog(null,
									"Please fill all fileds properlyy",
									"Input Error", JOptionPane.ERROR_MESSAGE);
						} else {
							long timeInMillis = System.currentTimeMillis();
							Calendar cal1 = Calendar.getInstance();
							cal1.setTimeInMillis(timeInMillis);
							SimpleDateFormat timeFormat = new SimpleDateFormat(
									"hh:mm:ss a");
							String[] data = new String[30];
							data[0] = supplierID;
							data[1] = supplierDisplaySTR;
							data[2] = "SUPPLIER";
							data[3] = "0";
							data[4] = "" + finalAmount;
							data[5] = "0";
							data[6] = "0";
							data[7] = dateSTR;
							data[8] = "" + timeFormat.format(cal1.getTime());
							data[9] = "" + StoreMain.userName;
							data[10] = "Amount Adjustment : "+amountAdjustmentDouble+"  Desc. : "+ paymentTF.getText().toString();

							GLAccountDBConnection glAccountDBConnection = new GLAccountDBConnection();

							try {
								paymetID = glAccountDBConnection
										.inserGLAccountSupplier(data);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();

							}

							glAccountDBConnection.closeConnection();
							updateInvoice();
							JOptionPane.showMessageDialog(null,
									"Payment saved successfully",
									"Payment Save",
									JOptionPane.INFORMATION_MESSAGE);
							dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
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

	public void populateExpensesTable() {

		try {
			InvoiceDBConnection db = new InvoiceDBConnection();
			ResultSet rs = db.retrieveAllPending(supplierID);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = db.retrieveAllPending(supplierID);

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns + 2];

			int R = 0;
			while (rs.next()) {
				Rows_Object_Array[R][0] = rs.getObject(1).toString();
				Rows_Object_Array[R][1] = rs.getObject(2).toString();
				Rows_Object_Array[R][2] = rs.getObject(3).toString();
				Rows_Object_Array[R][3] = rs.getObject(4).toString();
				Rows_Object_Array[R][4] = rs.getObject(5).toString();
				Rows_Object_Array[R][5] = rs.getObject(6).toString();
				Rows_Object_Array[R][6] = new Boolean(true);
				R++;
			}
			TableModel model1 = new EditableTableModel(new String[] { "ID",
					"Invoice No.", "Order No.", "Tax", "Amount", "Date",
					"Select" }, Rows_Object_Array);
			table.setModel(model1);
			table.getColumnModel().getColumn(0).setMinWidth(100);
			table.getColumnModel().getColumn(1).setPreferredWidth(100);
			table.getColumnModel().getColumn(1).setMinWidth(150);
			table.getColumnModel().getColumn(2).setPreferredWidth(100);
			table.getColumnModel().getColumn(2).setMinWidth(150);
			table.getColumnModel().getColumn(4).setMinWidth(100);
			table.getColumnModel().getColumn(5).setPreferredWidth(100);
			table.getColumnModel().getColumn(5).setMinWidth(150);
			amount();
		} catch (SQLException ex) {

			System.out.println(ex);
		}
	}

	public void amount() {
		EditableTableModel model1 = (EditableTableModel) table.getModel();
		totalAmount = 0;

		for (int count = 0; count < model1.getRowCount(); count++) {

			Boolean b = Boolean.valueOf(model1.getValueAt(count, 6).toString());
			if (b) {
				totalAmount = totalAmount
						+ Double.parseDouble(model1.getValueAt(count, 4)
								.toString());
			}
		}
		finalAmount = totalAmount + amountAdjustmentDouble;
		finalAmount = Math.round(finalAmount * 100.000) / 100.000;
		totalAmountLB.setText(finalAmount + "");
	}

	public void updateInvoice() {
		InvoiceDBConnection connection = new InvoiceDBConnection();
		EditableTableModel model1 = (EditableTableModel) table.getModel();
		for (int count = 0; count < model1.getRowCount(); count++) {

			Boolean b = Boolean.valueOf(model1.getValueAt(count, 6).toString());
			if (b) {
			
				try {
					connection.updateInvoiceStatus(model1.getValueAt(count, 0)
							.toString(), "" + paymetID);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		connection.closeConnection();
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
	public JDateChooser getSelectDate() {
		return selectDate;
	}
}
