package hms.store.gui;

import hms.doctor.database.DoctorDBConnection;
import hms.gl.database.GLAccountDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.main.GeneralDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.patient.database.PatientDBConnection;
import hms.patient.slippdf.ReturnBillSlippdf;
import hms.patient.slippdf.MedicalStoreBillSlippdf;
import hms.patient.slippdf.PO_PDF;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.BillingDBConnection;
import hms.store.database.CancelBillDBConnection;
import hms.store.database.CancelRestockFeeDB;
import hms.store.database.ItemsDBConnection;
import hms.store.database.PurchaseOrderDBConnection;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.CellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

import javax.swing.JTextArea;

public class ReturnApprovedBrowser extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private Timer timer;
	Date currentDate =null;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	private double GovtTax=0,RestockFee=0,cuttingFee=0;

	final DefaultComboBoxModel departmentName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemBatchName = new DefaultComboBoxModel();
	final DefaultComboBoxModel measUnit = new DefaultComboBoxModel();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel insuranceModel = new DefaultComboBoxModel();
	Vector<String> cancelIDV = new Vector<String>();
	Vector<String> userNameV = new Vector<String>();
	Vector<String> dateV = new Vector<String>();
	double amt=0;
	Vector<String> LastIssuedV = new Vector<String>();
	Vector<String> LastItemIDV = new Vector<String>();
	String departmentNameSTR, departmentID, personname, supplierID;
	String itemIDSTR, itemNameSTR, itemDescSTR, taxTypeSTR, expiryDateSTR = "",
			issuedDateSTR = "", dueDateSTR = "", previouseStock = "",
			itemBatchNameSTR = "", itemLocationSTR = "", payableSTR = "";
	double qtyIssued = 0, afterIssued = 0, discountValue = 0, taxValue = 0,
			itemValue, surchargeValue = 0, finalTaxValue = 0,
			finalDiscountValue = 0, finalTotalValue = 0, price = 0,
			batchQty = 0, taxAmountValue = 0, surchargeAmountValue = 0,
			taxAmountValue2, totalTaxAmount = 0, totalSurchargeAmount = 0,
			completeTaxAmount;
	int quantity = 0;
	String batchIDSTR = "0";
	Object[][] ObjectArray_ListOfexamsSpecs;
	private JScrollPane scrollPane_2;
	private JTable table1;
	private Object[][] ObjectArray_ListOfexamsSpecs1;
	private JLabel totalcancelTF;
	private JLabel lblTotalCancellation_1;
	private JLabel totalcancelTF_1;

	public static void main(String[] arg) {
		new ReturnApprovedBrowser().setVisible(true);
	}

	/**
	 * Create the dialog.
	 */
	public ReturnApprovedBrowser() {
		currentDate=new Date(System.currentTimeMillis());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Cancel Billing Requests");

		setIconImage(Toolkit.getDefaultToolkit().getImage(
				ReturnApprovedBrowser.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setModal(true);
		setBounds(100, 40, 1031, 505);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JSeparator separator = new JSeparator();
		separator.setBounds(33, 52, 966, 2);
		contentPanel.add(separator);

		JButton btnNewButton_1 = new JButton("Approved");
		btnNewButton_1.addActionListener(new ActionListener() {
		
			
			public void actionPerformed(ActionEvent e) {
				CancelBillDBConnection db= new CancelBillDBConnection();
				System.out.println("arun "+cancelIDV.size());
				if (!(cancelIDV.size()>0)) {
					JOptionPane.showMessageDialog(null, "Please Select bill",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				for(int i=0;i<cancelIDV.size();i++)
				{
					try {
						db.updateReturnBillStatus(cancelIDV.get(i));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}	
					JOptionPane.showMessageDialog(null, "Data successfully Updated.",
							"Success", JOptionPane.INFORMATION_MESSAGE);
					db.closeConnection();
					loadDataToTable1();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_1.setBounds(666, 431, 153, 25);
		contentPanel.add(btnNewButton_1);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(829, 431, 153, 25);
		contentPanel.add(btnCancel);

		

		JLabel lblBillCancellation = new JLabel("Bill Cancellation Request");
		lblBillCancellation.setHorizontalAlignment(SwingConstants.CENTER);
		lblBillCancellation.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblBillCancellation.setBounds(12, 23, 327, 31);
		contentPanel.add(lblBillCancellation);

		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(22, 66, 984, 353);
		table1 = new JTable();
		table1.setToolTipText("press enter to save");
		table1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table1.getTableHeader().setReorderingAllowed(false);
		table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table1.setFont(new Font("Tahoma", Font.BOLD, 11));
		table1.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Requester Name", "Bill No.", "Insurance", "Payable Amount", "Date", "Time", "Status", "Reason", "Bill Type"
			}
		));
		table1.getColumnModel().getColumn(0).setPreferredWidth(150);
		table1.getColumnModel().getColumn(0).setMinWidth(140);
		table1.getColumnModel().getColumn(1).setPreferredWidth(100);
		table1.getColumnModel().getColumn(1).setMinWidth(100);
		table1.getColumnModel().getColumn(2).setPreferredWidth(150);
		table1.getColumnModel().getColumn(2).setMinWidth(150);
		table1.getColumnModel().getColumn(3).setPreferredWidth(120);
		table1.getColumnModel().getColumn(3).setMinWidth(120);
		table1.getColumnModel().getColumn(4).setPreferredWidth(100);
		table1.getColumnModel().getColumn(4).setMinWidth(100);
		table1.getColumnModel().getColumn(5).setPreferredWidth(100);
		table1.getColumnModel().getColumn(5).setMinWidth(100);
		table1.getColumnModel().getColumn(6).setPreferredWidth(100);
		table1.getColumnModel().getColumn(6).setMinWidth(100);
		table1.getColumnModel().getColumn(7).setPreferredWidth(180);
		table1.getColumnModel().getColumn(7).setMinWidth(100);
		table1.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

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
				if (arg0.getClickCount() == 1) {
					//JTable target = (JTable) arg0.getSource();
					int row = table1.getSelectedRow();
					int column = table1.getSelectedColumn();
					// do some action
					amt=0;
					totalcancelTF_1.setText("0");
					cancelIDV.removeAllElements();
					userNameV.removeAllElements();
					dateV.removeAllElements();
					EditableTableModel model1 = (EditableTableModel) table1.getModel();
					double amount1 = 0;

					for (int count = 0; count < model1.getRowCount(); count++) {
						Boolean b = Boolean.valueOf(model1.getValueAt(count, 9).toString());
						if (b) {
							cancelIDV.add(model1.getValueAt(count, 1)+"");
							userNameV.add(model1.getValueAt(count, 0)+"");
							dateV.add(model1.getValueAt(count, 4)+"");
							amt+=Double.parseDouble(model1.getValueAt(count, 3)+"");
						}
					}
					totalcancelTF_1.setText(""+amt);
				}
				if (arg0.getClickCount() == 2) {
					//JTable target = (JTable) arg0.getSource();
					int row = table1.getSelectedRow();
					int column = table1.getSelectedColumn();
					// do some action
					String str=table1.getValueAt(row, 1)+"";
					try {
						new ReturnBillSlippdf(str);
					} catch (DocumentException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});

		scrollPane_2.setViewportView(table1);
		contentPanel.add(scrollPane_2);
		
		JLabel lblTotalCancellation = new JLabel("Total Cancellation : ");
		lblTotalCancellation.setBounds(12, 441, 145, 15);
		contentPanel.add(lblTotalCancellation);
		
		totalcancelTF = new JLabel("");
		totalcancelTF.setBounds(161, 441, 70, 15);
		contentPanel.add(totalcancelTF);
		
		lblTotalCancellation_1 = new JLabel("Selected Total : ");
		lblTotalCancellation_1.setBounds(387, 441, 145, 15);
		contentPanel.add(lblTotalCancellation_1);
		
		totalcancelTF_1 = new JLabel("0");
		totalcancelTF_1.setBounds(536, 441, 70, 15);
		contentPanel.add(totalcancelTF_1);
		
		loadDataToTable1();
	}
	private void loadDataToTable1() {
		// get size of the hashmap
		// to set rows in this array
		Object Rows_Object_Array[][];
		try {
			CancelBillDBConnection CDB= new CancelBillDBConnection();
			ResultSet rs=CDB.retrieveCancelBills();
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				NumberOfRows++;
			}
			rs.beforeFirst();
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns+1];
			double t=0;
			int R = 0;
			while (rs.next()) {
				for (int C = 1; C < NumberOfColumns+1; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
				}
				t+=Double.parseDouble(rs.getObject(4)+"");
				Rows_Object_Array[R][9]=new Boolean(false);
				R++;
			}
			totalcancelTF.setText(t+"");
			// Finally load data to the table
            TableModel model = new EditableTableModel(
					new String[] {"Requester Name","Bill No.", "Insurance", "Payable Amount", "Date", "Time",
							"Status","Reason","Bill Type","select" },Rows_Object_Array);
			
			table1.setModel(model);
	
			CDB.closeConnection();

			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getCancelitemData(String receiptID) {
		BillingDBConnection db1 = new BillingDBConnection();
		ResultSet resultSet = db1.retrieveCancelitemData(receiptID);
		LastIssuedV.removeAllElements();
		LastItemIDV.removeAllElements();
		try {
			while (resultSet.next()) {

				LastIssuedV.add(resultSet.getObject(1)+"");
				LastItemIDV.add(resultSet.getObject(2)+"");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db1.closeConnection();
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
			if(column==9){
				return true;
		    }else{
		    	return false;
		    }
			
		}
		public void setValueAt(Object value, int row, int column) {
			dataEntries[row][column] = value;
		}
	
}	
}