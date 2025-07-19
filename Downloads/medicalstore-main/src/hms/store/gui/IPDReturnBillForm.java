package hms.store.gui;

import hms.doctor.database.DoctorDBConnection;
import hms.formula.MSIPDReturnPriceFormula;
import hms.gl.database.GLAccountDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.main.GeneralDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.patient.database.PatientDBConnection;
import hms.patient.slippdf.ReturnBillSlippdf;
import hms.patient.slippdf.MedicalStoreBillSlippdf;
import hms.patient.slippdf.MedicalStoreIPDReturnPdf;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.BillingDBConnection;
import hms.store.database.CancelBillDBConnection;
import hms.store.database.CancelRestockFeeDB;
import hms.store.database.ItemsDBConnection;
import hms.store.database.PurchaseOrderDBConnection;
import hms.store.gui.PurchaseOrder.EditableTableModel;

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
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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

import org.apache.commons.lang3.text.WordUtils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import javax.swing.JTextArea;

public class IPDReturnBillForm extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField patientNameTF;
	private JTextField MobileNoTF;
	private JComboBox doctorNameCB;
	private JTable table;
	private Timer timer;
	private double GovtTax=0,RestockFee=0,cuttingFee=0;

	final DefaultComboBoxModel departmentName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemBatchName = new DefaultComboBoxModel();
	final DefaultComboBoxModel measUnit = new DefaultComboBoxModel();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel insuranceModel = new DefaultComboBoxModel();
	//	Vector<String> ID = new Vector<String>();
	//	Vector<String> itemID = new Vector<String>();
	//	Vector<String> itemIdV = new Vector<String>();
	//	Vector<String> expense_desc = new Vector<String>();
	//	Vector<String> item_meas_unit = new Vector<String>();
	//	Vector<String> item_risk_type = new Vector<String>();
	//	Vector<String> expense_text3 = new Vector<String>();
	//	Vector<String> mrp = new Vector<String>();
	//	Vector<String> issuedQtyV = new Vector<String>();
	//	Vector<String> QtyV = new Vector<String>();
	//	Vector<String> itemBatchIDV = new Vector<String>();
	//	Vector<String> ValueV = new Vector<String>();
	//	Vector<String> CancelValueV = new Vector<String>();
	//	Vector<String> DiscountV = new Vector<String>();
	//	Vector<String> surchargeV = new Vector<String>();
	//	Vector<String> surchargeAmountValueV = new Vector<String>();
	//	Vector<String> batchID = new Vector<String>();
	//	Vector<String> issuedDateV = new Vector<String>();
	//	Vector<String> cuttingV = new Vector<String>();
	String departmentNameSTR, departmentID, personname, supplierID,p_id,check;
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
	double ipdTotalChrge=0;
	String batchIDSTR = "0";
	Object[][] ObjectArray_ListOfexamsSpecs;
	private JTextField serachBillNoTF;
	private JTextField totalAmountTF;
	private JTextField balanceCashTF;
	private JComboBox insuranceCB;
	private JTextField cancelAmountTF;
	private JTextField insuranceIDTB;
	private JTextField opdNoTF;
	private JTextArea cancelReasonTA;
	private JTextField tfpatientid;



	/***new data***/
	Vector<String> billItemIdV = new Vector<String>();
	Vector<String> payableV = new Vector<String>();
	Vector<String> itemIdV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> itemBatchIdV = new Vector<String>();
	Vector<String> itemHsnCodeV = new Vector<String>();
	Vector<String> itemBatchV = new Vector<String>();
	Vector<String> itemCategoryV = new Vector<String>();
	Vector<String> unitPriceV = new Vector<String>();
	Vector<String> returnUnitPriceV = new Vector<String>();
	Vector<String> quantityV = new Vector<String>();
	Vector<String> returnQuantityV = new Vector<String>();
	Vector<Double> taxPercentageV = new Vector<Double>();
	Vector<String> discountV = new Vector<String>();
	Vector<String> taxAmountV = new Vector<String>();
	Vector<String> returnTaxAmountV = new Vector<String>();
	Vector<Double> surchargePercentageV = new Vector<Double>();
	Vector<String> surchargeValueV = new Vector<String>();
	Vector<String> returnSurchargeValueV = new Vector<String>();
	Vector<String> totalValueV = new Vector<String>();
	Vector<String> returnTotalValueV = new Vector<String>();
	Vector<String> expiryDateV = new Vector<String>();
	Vector<String> dateV = new Vector<String>();
	Vector<String> timeV = new Vector<String>();
	Vector<String> userIdV = new Vector<String>();
	Vector<String> userNameV = new Vector<String>();
	Vector<String> mrpV = new Vector<String>();
	Vector<String> packSizeV = new Vector<String>();
	Vector<String> itemRiskTypeV = new Vector<String>();
	Vector<String> karunaDiscountV = new Vector<String>();
	Vector<String> newUnitPriceV = new Vector<String>();

	public static void main(String[] arg) {
		new IPDReturnBillForm().setVisible(true);
	}

	/**
	 * Create the dialog.
	 */
	public IPDReturnBillForm() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("IPD Cancel Billing Form");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				IPDReturnBillForm.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 40, 1031, 510);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblCredit = new JLabel("Enter OPD No. :");
		lblCredit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCredit.setBounds(14, 45, 126, 25);
		contentPanel.add(lblCredit);

		patientNameTF = new JTextField();
		patientNameTF.setEditable(false);
		patientNameTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		patientNameTF.setColumns(10);
		patientNameTF.setBounds(150, 81, 218, 25);
		contentPanel.add(patientNameTF);

		JLabel lblDebit = new JLabel("Patient Name :");
		lblDebit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDebit.setBounds(14, 81, 126, 25);
		contentPanel.add(lblDebit);

		MobileNoTF = new JTextField();
		MobileNoTF.setEditable(false);
		MobileNoTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		MobileNoTF.setColumns(10);
		MobileNoTF.setBounds(150, 117, 218, 25);
		contentPanel.add(MobileNoTF);

		JLabel lblBalance = new JLabel("Mobile No. :");
		lblBalance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBalance.setBounds(14, 117, 126, 25);
		contentPanel.add(lblBalance);

		doctorNameCB = new JComboBox();
		doctorNameCB.setEnabled(false);
		doctorNameCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		doctorNameCB.setBounds(751, 9, 218, 25);
		contentPanel.add(doctorNameCB);

		JLabel lblInvoiceNo = new JLabel("Select Doctor :");
		lblInvoiceNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInvoiceNo.setBounds(615, 9, 126, 25);
		contentPanel.add(lblInvoiceNo);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 161, 1002, 189);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setToolTipText("press enter to save");
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] {
				"Item ID", "Item Name", "Item Batch", "Unit Price", "Quantity",
				"tax", "Total Value", "Issue Date", "Select","Enter Qty","Expiry" }));
		table.getColumnModel().getColumn(0).setMinWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setMinWidth(80);
		table.getColumnModel().getColumn(4).setPreferredWidth(80);
		table.getColumnModel().getColumn(4).setMinWidth(80);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setMinWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setMinWidth(100);
		table.getColumnModel().getColumn(7).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setMinWidth(100);
		table.getColumnModel().getColumn(8).setPreferredWidth(75);
		table.getColumnModel().getColumn(8).setMinWidth(75);
		table.getColumnModel().getColumn(9).setPreferredWidth(90);
		table.getColumnModel().getColumn(9).setMinWidth(90);
		table.getColumnModel().getColumn(10).setPreferredWidth(100);
		table.getColumnModel().getColumn(10).setMinWidth(100);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					JTable target = (JTable) e.getSource();
					final int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					// do some action
					table.requestFocus();
					table.editCellAt(row, 9);


				}
			}
		});
		table.getModel().addTableModelListener(new TableModelListener() {


			@Override
			public void tableChanged(TableModelEvent e) {
				// TODO Auto-generated method stub
				table.requestFocus();
			}
		});
		table.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
			}

			// this function successfully provides cell editing stop
			// on cell losts focus (but another cell doesn't gain focus)
			public void focusLost(FocusEvent e) {
				CellEditor cellEditor = table.getCellEditor();
				if (cellEditor != null)

					cellEditor.stopCellEditing();

			}
		});
		table.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyChar()==KeyEvent.VK_ENTER)
				{
					CellEditor cellEditor = table.getCellEditor();
					if (cellEditor != null)

						cellEditor.stopCellEditing();

					calculate();

				}
			}
		});

		scrollPane.setViewportView(table);

		JSeparator separator = new JSeparator();
		separator.setBounds(14, 152, 966, 2);
		contentPanel.add(separator);

		JButton btnNewButton_1 = new JButton("Return Bill");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if(!calculate())
				{
					return;
				}


				if (patientNameTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please search bill",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (cancelAmountTF.getText().toString().equals("0.0")) {
					JOptionPane.showMessageDialog(null, "Please select item !",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (cancelReasonTA.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter remarks",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				insertCancel();
			}


		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_1.setBounds(666, 431, 153, 39);
		contentPanel.add(btnNewButton_1);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(829, 431, 153, 39);
		contentPanel.add(btnCancel);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(IPDReturnBillForm.class
				.getResource("/icons/delete_dialog.png")));
		lblNewLabel.setBounds(409, 56, 171, 65);
		contentPanel.add(lblNewLabel);

		serachBillNoTF = new JTextField();
		serachBillNoTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		serachBillNoTF.setColumns(10);
		serachBillNoTF.setBounds(150, 9, 218, 25);
		serachBillNoTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				int key = e.getKeyCode();

				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {

					e.consume();
				}

			}
		});

		timer = new Timer(700, new ActionListener() {



			public void actionPerformed(ActionEvent e) {
				// highlightAll();
				timer.stop();
				String str = serachBillNoTF.getText() + "";
				if (!str.equals("")) {
					BillNo(str);
				} else {
					patientNameTF.setText("");
					MobileNoTF.setText("");
					doctorNameCB.setSelectedIndex(0);
					insuranceCB.setSelectedIndex(0);
					opdNoTF.setText("");
					insuranceIDTB.setText("");
					clearAllVectors();
					loadDataToTable();
				}

			}



		});
		contentPanel.add(serachBillNoTF);
		serachBillNoTF.getDocument().addDocumentListener( 
				new DocumentListener() {

					@Override
					public void removeUpdate(DocumentEvent e) {
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();

					}
					@Override
					public void changedUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();
					}

				});
		JLabel lblBillNo = new JLabel("Bill No.  :");
		lblBillNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBillNo.setBounds(14, 9, 126, 25);
		contentPanel.add(lblBillNo);

		opdNoTF = new JTextField("");
		opdNoTF.setEditable(false);
		opdNoTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		opdNoTF.setBounds(150, 45, 218, 25);
		contentPanel.add(opdNoTF);

		totalAmountTF = new JTextField();
		totalAmountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		totalAmountTF.setEditable(false);
		totalAmountTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		totalAmountTF.setColumns(10);
		totalAmountTF.setBounds(542, 383, 114, 25);
		contentPanel.add(totalAmountTF);

		balanceCashTF = new JTextField();
		balanceCashTF.setEditable(false);
		balanceCashTF.setHorizontalAlignment(SwingConstants.RIGHT);
		balanceCashTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		balanceCashTF.setColumns(10);
		balanceCashTF.setBounds(674, 383, 145, 25);
		contentPanel.add(balanceCashTF);

		JLabel lblSelectInsurance = new JLabel("Select Head :");
		lblSelectInsurance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSelectInsurance.setBounds(625, 45, 126, 25);
		contentPanel.add(lblSelectInsurance);

		insuranceCB = new JComboBox();
		insuranceCB.setEnabled(false);
		insuranceCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		insuranceCB.setBounds(751, 45, 218, 25);
		contentPanel.add(insuranceCB);

		cancelAmountTF = new JTextField();
		cancelAmountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		cancelAmountTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cancelAmountTF.setEditable(false);
		cancelAmountTF.setColumns(10);
		cancelAmountTF.setBounds(843, 383, 126, 25);
		cancelAmountTF.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = cancelAmountTF.getText() + "";
						if (!str.equals("")) {
							double cancleAmount = Double.parseDouble("0" + str);
							double amount = Double.parseDouble("0"
									+ totalAmountTF.getText().toString());
							double balance = (amount - cancleAmount);
							balance = Math.round(balance * 100.000) / 100.000;
							balanceCashTF.setText("" + balance);
						} else {

							balanceCashTF.setText("");
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = cancelAmountTF.getText() + "";
						if (!str.equals("")) {

							double cancleAmount = Double.parseDouble("0" + str);
							double amount = Double.parseDouble("0"
									+ totalAmountTF.getText().toString());
							double balance = (amount - cancleAmount);
							balance = Math.round(balance * 100.000) / 100.000;
							balanceCashTF.setText("" + balance);
						} else {

							balanceCashTF.setText("");
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = cancelAmountTF.getText() + "";
						if (!str.equals("")) {

							double cancleAmount = Double.parseDouble("0" + str);
							double amount = Double.parseDouble("0"
									+ totalAmountTF.getText().toString());
							double balance = (amount - cancleAmount);
							balance = Math.round(balance * 100.000) / 100.000;
							balanceCashTF.setText("" + balance);
						} else {

							balanceCashTF.setText("");
						}
					}
				});
		contentPanel.add(cancelAmountTF);

		JLabel lblInsuranceId = new JLabel("Insurance ID *:");
		lblInsuranceId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInsuranceId.setBounds(615, 81, 126, 25);
		contentPanel.add(lblInsuranceId);

		insuranceIDTB = new JTextField();
		insuranceIDTB.setEditable(false);
		insuranceIDTB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		insuranceIDTB.setColumns(10);
		insuranceIDTB.setBounds(751, 81, 218, 25);
		contentPanel.add(insuranceIDTB);

		JLabel lblBillCancellation = new JLabel("IPD Bill Return.");
		lblBillCancellation.setHorizontalAlignment(SwingConstants.CENTER);
		lblBillCancellation.setFont(new Font("Dialog", Font.BOLD, 18));
		lblBillCancellation.setBounds(386, 39, 219, 31);
		contentPanel.add(lblBillCancellation);

		JLabel lblReason = new JLabel("Reason :");
		lblReason.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblReason.setBounds(14, 361, 71, 25);
		contentPanel.add(lblReason);



		cancelReasonTA = new JTextArea();
		cancelReasonTA.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cancelReasonTA.setLineWrap(true);
		//		scrollPane_1.setViewportView(cancelReasonTA);
		cancelReasonTA.setRows(5);
		JScrollPane scrollPane_1 = new JScrollPane(cancelReasonTA);
		scrollPane_1.setBounds(115, 360, 400, 54);
		contentPanel.add(scrollPane_1);

		JLabel lblTotalAmount = new JLabel(" Total Amount ");
		lblTotalAmount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotalAmount.setBounds(543, 359, 113, 25);
		contentPanel.add(lblTotalAmount);

		JLabel lblRemainingAmount = new JLabel(" Remaining Amount ");
		lblRemainingAmount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblRemainingAmount.setBounds(674, 359, 145, 25);
		contentPanel.add(lblRemainingAmount);

		JLabel lblCancelAmount = new JLabel("  Payable Amount ");
		lblCancelAmount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCancelAmount.setBounds(843, 359, 150, 25);
		contentPanel.add(lblCancelAmount);

		JButton btnNewButton_1_1 = new JButton("Calculate");
		btnNewButton_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculate();
			}
		});
		btnNewButton_1_1.setFont(new Font("Dialog", Font.BOLD, 15));
		btnNewButton_1_1.setBounds(113, 431, 153, 39);
		contentPanel.add(btnNewButton_1_1);

		JLabel lblPatie = new JLabel("Patient ID *:");
		lblPatie.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblPatie.setBounds(615, 117, 126, 25);
		contentPanel.add(lblPatie);

		tfpatientid = new JTextField();
		tfpatientid.setFont(new Font("Dialog", Font.PLAIN, 14));
		tfpatientid.setEditable(false);
		tfpatientid.setColumns(10);
		tfpatientid.setBounds(751, 117, 218, 25);
		contentPanel.add(tfpatientid);


		getAllDoctors();
		getAllinsurance();
	}

	private void loadDataToTable() {
		// get size of the hashmap
		int size = itemIdV.size();
		finalTotalValue = 0;
		ObjectArray_ListOfexamsSpecs = new Object[size][12];

		for (int i = 0; i < itemIdV.size(); i++) {

			ObjectArray_ListOfexamsSpecs[i][0] = itemIdV.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = itemNameV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = itemBatchV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = unitPriceV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = quantityV.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = (taxPercentageV.get(i)+surchargePercentageV.get(i));
			ObjectArray_ListOfexamsSpecs[i][6] = totalValueV.get(i);
			ObjectArray_ListOfexamsSpecs[i][7] = dateV.get(i);
			ObjectArray_ListOfexamsSpecs[i][8] = new Boolean(false);
			ObjectArray_ListOfexamsSpecs[i][9] = "";
			ObjectArray_ListOfexamsSpecs[i][10] = expiryDateV.get(i);

		}
		TableModel model1 = new EditableTableModel(new String[] { "Item ID",
				"Item Name", "Item Batch", "Unit Price", "Quantity", "tax(%)",
				"Total Value","Issue Date" , "Select","Enter Qty","Expiry" },
				ObjectArray_ListOfexamsSpecs);
		table.setModel(model1);

		table.getColumnModel().getColumn(0).setMinWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setMinWidth(80);
		table.getColumnModel().getColumn(4).setPreferredWidth(80);
		table.getColumnModel().getColumn(4).setMinWidth(80);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setMinWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setMinWidth(100);
		table.getColumnModel().getColumn(7).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setMinWidth(100);
		table.getColumnModel().getColumn(8).setPreferredWidth(75);
		table.getColumnModel().getColumn(8).setMinWidth(75);
		table.getColumnModel().getColumn(9).setPreferredWidth(90);
		table.getColumnModel().getColumn(9).setMinWidth(90);
		table.getColumnModel().getColumn(10).setPreferredWidth(100);
		table.getColumnModel().getColumn(10).setMinWidth(100);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
	}

	public void getAllDoctors() {
		doctors.addElement("Other Doctor");
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
		doctorNameCB.setModel(doctors);
		doctorNameCB.setSelectedIndex(0);
	}

	public void BillNo(String billNo) {
		patientNameTF.setText("");
		MobileNoTF.setText("");
		doctorNameCB.setSelectedIndex(0);
		insuranceCB.setSelectedIndex(0);
		try {
			BillingDBConnection db = new BillingDBConnection();
			ResultSet rs = db.retrieveIPDBillDataDetail(billNo);
			while (rs.next()) {

				opdNoTF.setText("" + rs.getObject(1).toString());
				patientNameTF.setText("" + rs.getObject(2).toString());
				MobileNoTF.setText("" + rs.getObject(3).toString());
				System.out.println(rs.getObject(3).toString() + " "
						+ rs.getObject(4).toString()
						+ rs.getObject(7).toString());
				doctorNameCB.setSelectedIndex(doctors.getIndexOf(rs
						.getObject(4).toString()));
				insuranceCB.setSelectedIndex(insuranceModel.getIndexOf(rs
						.getObject(5).toString()));
				insuranceIDTB.setText("" + rs.getObject(6).toString());
				//	totalAmountTF.setText("" + rs.getObject(8).toString());
				BillData(billNo);
				patientID(opdNoTF.getText()+"");
				IpdTotalCharge(opdNoTF.getText()+"");

			}
			db.closeConnection();
		} catch (SQLException ex) {

		}
	}
	private void patientID(String ipd) {
		// TODO Auto-generated method stub
		OPDDBConnection ipddbConnection = new OPDDBConnection();
		ResultSet rs=ipddbConnection.retrieveipdid(ipd);
		p_id="";
		check="";
		try {
			while(rs.next()) {
				p_id=rs.getObject(1).toString();
				check=rs.getObject(2).toString();
			}
			ipddbConnection.closeConnection();
			if(check.equals("No")) {
				tfpatientid.setText(p_id);
			}
			else {
				JOptionPane.showMessageDialog(null, "These paient is discharged you can`t return ",
						"Input Error", JOptionPane.ERROR_MESSAGE);
				dispose();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void IpdTotalCharge(String ipd) {
		// TODO Auto-generated method stub
		OPDDBConnection ipddbConnection = new OPDDBConnection();
		ResultSet rs=ipddbConnection.retrieveipdid(ipd);
		ipdTotalChrge=0;
		try {
			while(rs.next()) {
				ipdTotalChrge=rs.getDouble(1);
			}		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ipddbConnection.closeConnection();
	}

	public void getAllinsurance() {
		insuranceModel.addElement("Unknown");
		InsuranceDBConnection dbConnection = new InsuranceDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				insuranceModel.addElement(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		insuranceCB.setModel(insuranceModel);
		insuranceCB.setSelectedIndex(0);
	}

	public boolean calculate() {
		MSIPDReturnPriceFormula formula=new MSIPDReturnPriceFormula();
		EditableTableModel model1 = (EditableTableModel) table.getModel();
		totalSurchargeAmount=totalTaxAmount=0;
		double bal=0; 

		for (int count = 0; count < model1.getRowCount(); count++) {
			Boolean b = Boolean.valueOf(model1.getValueAt(count, 8).toString());
			if (b) {
				if (b && !(model1.getValueAt(count, 9).equals(""))) {
					double issuedQty = Double.parseDouble(model1.getValueAt(count, 9).toString());
					double qty = Double.parseDouble(model1.getValueAt(count, 4).toString());
					String itemID  = model1.getValueAt(count, 0).toString();
					itemCategoryV.set(count, getItemCategory(itemID));
					String issuedDate  = model1.getValueAt(count, 7).toString();
					double unitPrice = Double.parseDouble(model1.getValueAt(count, 3).toString());
					if(qty<issuedQty)
					{
						JOptionPane.showMessageDialog(null, "Qunatity exceeded for item "+model1.getValueAt(count, 1).toString()+"!", "Input Error",
								JOptionPane.ERROR_MESSAGE);
						return false;
					}

					double rp=unitPrice*issuedQty;
					rp=formula.addGst(rp, taxPercentageV.get(count), surchargePercentageV.get(count));
					totalSurchargeAmount=totalSurchargeAmount+formula.getSgstValue();
					totalTaxAmount=totalTaxAmount+formula.getCgstValue();
					returnSurchargeValueV.set(count,String.valueOf(formula.getSgstValue()));
					returnTaxAmountV.set(count,String.valueOf(formula.getCgstValue()));
					bal=bal+rp; 
					returnTotalValueV.set(count,String.valueOf(roundPrice(-rp)));
					returnQuantityV.set(count, -issuedQty + ""); 
				} else {

					JOptionPane.showMessageDialog(null, "Please check Qunatity and Checkboxes!", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return false;

				} 
			}
		}
		cancelAmountTF.setText("" + roundPrice(bal));
		return true;
	}

	public void insertCancel() {
		EditableTableModel model1 = (EditableTableModel) table.getModel();
		CancelBillDBConnection cancelBillDBConnection = new CancelBillDBConnection();
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
		BillingDBConnection billingDBConnection = new BillingDBConnection();
		OPDDBConnection ipddbConnection = new OPDDBConnection();
		int index=0;
		String[] data = new String[31];
		String[] data1=new String[5];
		long timeInMillis = System.currentTimeMillis();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(timeInMillis);
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");

		data[0] = opdNoTF.getText() + "";
		data[1] = WordUtils.capitalize(patientNameTF.getText()
				.toString());
		data[2] = MobileNoTF.getText().toString();
		data[3] = doctorNameCB.getSelectedItem().toString();
		data[4] = insuranceCB.getSelectedItem().toString();
		data[5] = insuranceIDTB.getText().toString();
		data[6] = "0.0"; 
		data[7] = "-" + cancelAmountTF.getText().toString();
		data[8] = "-" + getPaybleRoundValue(cancelAmountTF.getText().toString());
		data[9] = "0.0" ;
		data[10] = "0.0"; 
		data[11] = "-"+totalTaxAmount + "";
		data[12] = "-"+totalSurchargeAmount + "";
		data[13] = DateFormatChange.StringToMysqlDate(new Date());
		data[14] = "" + timeFormat.format(cal1.getTime());
		data[15] = StoreMain.userID;
		data[16] = StoreMain.userName;
		data[17] = "NA";
		data[18] = "NA";
		data[19] = "NA";
		data[20] ="IPD RETURN";
		data[21] = serachBillNoTF.getText();
		data[22] = cancelReasonTA.getText();

		try {
			System.out.println(Arrays.toString(data));
			index = billingDBConnection.inserBillEntry(data);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (int count = 0; count < model1.getRowCount(); count++) {
			Boolean b = Boolean.valueOf(model1.getValueAt(count, 8).toString());
			if (b) {

				data[0] = index + "";
				data[1] = patientNameTF.getText().toString();
				data[2] = doctorNameCB.getSelectedItem().toString();
				data[3] = insuranceCB.getSelectedItem().toString();
				data[4] = "0";	
				data[5] = itemIdV.get(count);
				data[6] = itemNameV.get(count);
				data[7] = itemDescV.get(count);
				data[8] = itemHsnCodeV.get(count);
				data[9] = itemBatchIdV.get(count);
				data[10] = itemBatchV.get(count);
				data[11] = returnUnitPriceV.get(count);
				data[12] = returnQuantityV.get(count);
				data[13] = taxPercentageV.get(count)+"";
				data[14] = discountV.get(count); 
				data[15] = "-"+returnTaxAmountV.get(count);
				data[16] = returnTotalValueV.get(count);
				data[17] = surchargePercentageV.get(count)+""; 
				data[18] = "-"+returnSurchargeValueV.get(count);
				data[19] = expiryDateV.get(count);
				data[20] = DateFormatChange.StringToMysqlDate(new Date());
				data[21] = "" + timeFormat.format(cal1.getTime());
				data[22] = StoreMain.userID;
				data[23] = StoreMain.userName;
				data[24] = mrpV.get(count)+""+"";
				data[25] = packSizeV.get(count)+"";
				data[26] = itemRiskTypeV.get(count)+"";
				data[27] ="0";
				data[28] ="0";
				data[29] ="RETURN";
				data[30] = serachBillNoTF.getText();
				try {
					System.out.println(Arrays.toString(data));
					billingDBConnection.insertBillingItems(data);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


				data[0] =  opdNoTF.getText().toString();
				data[1] = itemNameV.get(count).toString();
				data[2] = itemDescV.get(count).toString();
				data[3] = ""+returnTotalValueV.get(count).toString();
				data[4] = DateFormatChange.StringToMysqlDate(new Date());
				data[5] = "" + timeFormat.format(cal1.getTime());
				data[6] = itemIdV.get(count).toString();
				data[7] = tfpatientid.getText().toString();
				data[8] = patientNameTF.getText().toString();
				data[9] = returnUnitPriceV.get(count).toString();
				data[10] = ""+returnQuantityV.get(count).toString();
				data[11] = itemCategoryV.get(count).toString()+" RETURNED";
				data[12] = "" + StoreMain.userName;
				data[13] = "Medical Store";
				data[14] = mrpV.get(count).toString();
				data[15] = itemBatchIdV.get(count).toString();
				data[16] = itemBatchV.get(count).toString();
				data[17] = "MS";
				try {
					System.out.println(Arrays.toString(data));
					int d=ipddbConnection.inserDataIpd(data);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				try {
					itemsDBConnection.subtractStock(itemIdV.get(count),
							returnQuantityV.get(count));
					batchTrackingDBConnection.returnStock1(
							itemBatchIdV.get(count), returnQuantityV.get(count));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		data[0] = "" + (insuranceCB.getSelectedIndex() + 1);
		data[1] = insuranceCB.getSelectedItem().toString();
		data[2] = "INSURANCE";
		data[3] = "" + balanceCashTF.getText().toString();
		data[4] = "0";
		data[5] = "0";
		data[6] = "0";
		data[7] = DateFormatChange.StringToMysqlDate(new Date());
		data[8] = "" + timeFormat.format(cal1.getTime());
		data[9] = "" + StoreMain.userName;
		data[10] = "Cancel Bill Items";
		GLAccountDBConnection glAccountDBConnection = new GLAccountDBConnection();
		try {
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		glAccountDBConnection.closeConnection();
		ipddbConnection = new OPDDBConnection();
		double tot = ipdTotalChrge - Double.parseDouble(cancelAmountTF.getText()+"")+0;
		double finalValue = Math.round(tot * 100.0) / 100.0;
		try {
			ipddbConnection.updateTotalAmount(opdNoTF.getText().toString(), finalValue + "");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			ipddbConnection.closeConnection();
			e1.printStackTrace();
		}
		ipddbConnection.closeConnection();
		billingDBConnection.closeConnection();
		itemsDBConnection.closeConnection();
		batchTrackingDBConnection.closeConnection();
		cancelBillDBConnection.closeConnection();

		dispose();
		try {
			new ReturnBillSlippdf(index+"");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Data successfully Updated.",
				"Success", JOptionPane.INFORMATION_MESSAGE);

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
			if(column==9 || column==8)
			{
				return true;	
			}else {
				return false;
			}

		}

		public void setValueAt(Object value, int row, int column) {
			dataEntries[row][column] = value;
		}
	}


	public void BillData(String bill_no) {
		clearAllVectors();
		double tot=0;
		try {
			BillingDBConnection db1 = new BillingDBConnection();
			ResultSet rs = db1.retrieveBillItemsDataDetailNew(bill_no);
			while (rs.next()) {
				billItemIdV.add(rs.getString("bill_item_id"));
				payableV.add(rs.getString("payable"));
				itemIdV.add(rs.getString("item_id"));
				itemNameV.add(rs.getString("item_name"));
				itemDescV.add(rs.getString("item_desc"));
				itemBatchIdV.add(rs.getString("item_batch_id"));
				itemHsnCodeV.add(rs.getString("item_hsn_code"));
				itemBatchV.add(rs.getString("item_batch"));
				unitPriceV.add(rs.getString("unit_price"));
				returnUnitPriceV.add(rs.getString("unit_price"));
				quantityV.add(rs.getString("quantity"));
				returnQuantityV.add(rs.getString("quantity"));
				taxPercentageV.add(rs.getDouble("tax_percentage"));
				discountV.add(rs.getString("discount"));
				taxAmountV.add(rs.getString("tax_amount"));
				returnTaxAmountV.add(rs.getString("tax_amount"));
				surchargePercentageV.add(rs.getDouble("surchargePercentage"));
				surchargeValueV.add(rs.getString("surchargeValue"));
				returnSurchargeValueV.add(rs.getString("surchargeValue"));
				totalValueV.add(rs.getString("total_value"));
				returnTotalValueV.add(rs.getString("total_value"));
				tot=tot+rs.getDouble("total_value");
				expiryDateV.add(rs.getString("expiry_date"));
				dateV.add(rs.getString("date"));
				timeV.add(rs.getString("time"));
				userIdV.add(rs.getString("user_id"));
				userNameV.add(rs.getString("user_name"));
				mrpV.add(rs.getString("mrp"));
				packSizeV.add(rs.getString("pack_size"));
				itemRiskTypeV.add(rs.getString("item_risk_type"));
				karunaDiscountV.add(String.valueOf(rs.getDouble("karuna_discount")));
				newUnitPriceV.add(String.valueOf(rs.getDouble("new_unit_price")));
				itemCategoryV.add("");
			}
			db1.closeConnection();
		} catch (SQLException ex) {

		}

		if (itemBatchIdV.size() > 0) {
			tot = Math.round(tot * 100.000) / 100.000;
			totalAmountTF.setText("" + tot);
			loadDataToTable();	
		}

	}

	public void clearAllVectors() {
		billItemIdV.clear();
		payableV.clear();
		itemIdV.clear();
		itemNameV.clear();
		itemDescV.clear();
		itemBatchIdV.clear();
		itemHsnCodeV.clear();
		itemBatchV.clear();
		unitPriceV.clear();
		quantityV.clear();
		taxPercentageV.clear();
		discountV.clear();
		taxAmountV.clear();
		surchargePercentageV.clear();
		surchargeValueV.clear();
		totalValueV.clear();
		expiryDateV.clear();
		dateV.clear();
		timeV.clear();
		userIdV.clear();
		userNameV.clear();
		mrpV.clear();
		packSizeV.clear();
		itemRiskTypeV.clear();
		karunaDiscountV.clear();
		newUnitPriceV.clear();
	}

	public String getItemCategory(String index) {
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail2(index);
		try {
			while (resultSet.next()) {
				return resultSet.getObject(18).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		return "";

	}

	public double getPaybleRoundValue(String str) {
		double amount = Double.parseDouble("0" + str);
		if (amount - Math.floor(amount) > 0.5) {
			return Math.ceil(amount);
		} else {
			return Math.floor(amount);
		}
	}

	private double roundPrice(double price) {
		return Math.round(price * 100.0) / 100.0;
	}

}