package hms.store.gui;
import hms.doctor.database.DoctorDBConnection;
import hms.gl.database.GLAccountDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.main.GeneralDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.patient.database.PatientDBConnection;
import hms.patient.slippdf.MedicalStoreBillSlippdf;
import hms.patient.slippdf.MedicalStoreIPDPdf;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.BillingDBConnection;
import hms.store.database.CancelRestockFeeDB;
import hms.store.database.ItemsDBConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.text.WordUtils;

import com.itextpdf.text.DocumentException;

public class NewBillForm2 extends JDialog implements KeyListener {

	private final JPanel contentPanel = new JPanel();
	private JTextField patientNameTF;
	private JTextField MobileNoTF;
	private JComboBox doctorNameCB;
	public JTextField searchItemTF;
	private JTextField itemDescTF;
	private JTextField qtyInHandTF;
	private JTextField qtyIssuedTF;
	JTextField mrpTF, packSizeET;
	JButton btnEditItem;
	private JTable table;
	
	private double cutting_chrg_1=0,cutting_chrg_2=0,cutting_chrg_3=0,newUnitTax=0;
	final DefaultComboBoxModel departmentName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemBatchName1 = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemBatchName = new DefaultComboBoxModel();
	final DefaultComboBoxModel measUnit = new DefaultComboBoxModel();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel insuranceModel = new DefaultComboBoxModel();
	Vector<String> itemID = new Vector<String>();
	Vector<String> cutting_chrgV = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> itemCategoryV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> itemHSN_CODEV = new Vector<String>();
	Vector<String> itemRiskValueV = new Vector<String>();
	Vector<String> issuedQtyV = new Vector<String>();
	Vector<String> itemBatchIDV = new Vector<String>();
	Vector<String> itemBatchV = new Vector<String>();
	Vector<String> unitPriceV = new Vector<String>();
	Vector<String> totalValueV = new Vector<String>();
	Vector<String> mrpPriceV = new Vector<String>();
	Vector<String> measUnitV = new Vector<String>();
	Vector<String> taxPercentageV = new Vector<String>();
	Vector<String> DiscountV = new Vector<String>();
	Vector<String> taxAmountV = new Vector<String>();
	Vector<String> surchargeV = new Vector<String>();
	Vector<String> surchargeAmountValueV = new Vector<String>();
	Vector<String> batchID = new Vector<String>();
	Vector<String> checkBatch = new Vector<String>();
	double mrp = 0, mrptotal = 0;
	double purchase = 0;
	double value = 0;
	int packSize = 1;
	double totalChargesIPD = 0;
	String item_risk_type = "";
	String p_name, p_id, p_address, ipd_id;
	Vector<String> expiryDateV = new Vector<String>();
	String departmentNameSTR, departmentID, personname, supplierID;
	String itemIDSTR = "", itemNameSTR, itemDescSTR, itemHSNSTR, taxTypeSTR,
			expiryDateSTR = "", issuedDateSTR = "", dueDateSTR = "",
			previouseStock = "", itemBatchNameSTR = "", itemLocationSTR = "",
			payableSTR = "", formulaActive = "",p_insurance="";
	double afterIssued = 0, discountValue = 0, taxValue = 0, itemValue,cutting_chrg=0,
			surchargeValue = 0, finalTaxValue = 0, finalDiscountValue = 0,
			finalTotalValue = 0, price = 0, batchQty = 0, taxAmountValue = 0,
			surchargeAmountValue = 0, taxAmountValue2, totalTaxAmount = 0,
			totalSurchargeAmount = 0, completeTaxAmount;
	int quantity = 0, qtyIssued = 0;
	String batchIDSTR = "0";
	Object[][] ObjectArray_ListOfexamsSpecs;
	private JComboBox itemNameCB;
	private JTextField serachIPDNoTF;
	private JTextField batchQtyTF;
	private JTextField taxTF;
	private JTextField discountTF;
	private JTextField itemPriceTF;
	private JTextField totalValueTF;
	private JTextField expiryDateTF;
	private JTextField finalDiscountTF;
	private JTextField finalTaxTF;
	private JTextField totalAmountTF;
	private JTextField cashRecievedTF;
	private Timer timer;
	private JTextField balanceAmountTF;
	private JComboBox batchNameCB;
	private JComboBox insuranceCB;
	private JTextField itemLocationTF;
	private JTextField totalPayableAmountTF;
	private JTextField surchargeTF;
	BillBrowser billBrowser;
	private JTextField insuranceIDTB;
	private JTextField enterBatchNO;
	private JTextField patientIDTF;
	private String itemCategory="";

	public static void main(String[] args) {
		new NewBillForm2(null).setVisible(true);;
	}
	/**
	 * Create the dialog.
	 */
	public NewBillForm2(final BillBrowser billBrowser) {
		setTitle("Billing Form");
		addKeyListener(this);
		this.billBrowser = billBrowser;
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				NewBillForm2.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 40, 1031, 646);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblCredit = new JLabel("Enter IPD No. :");
		lblCredit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCredit.setBounds(14, 45, 126, 25);
		contentPanel.add(lblCredit);

		patientNameTF = new JTextField();
		patientNameTF.setEditable(false);
		patientNameTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		patientNameTF.setColumns(10);
		patientNameTF.setBounds(150, 120, 218, 25);
		contentPanel.add(patientNameTF);

		JLabel lblDebit = new JLabel("Patient Name :");
		lblDebit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDebit.setBounds(14, 120, 126, 25);
		contentPanel.add(lblDebit);

		MobileNoTF = new JTextField();
		MobileNoTF.setEditable(false);
		MobileNoTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		MobileNoTF.setColumns(10);
		MobileNoTF.setBounds(150, 156, 218, 25);
		contentPanel.add(MobileNoTF);

		JLabel lblBalance = new JLabel("Mobile No. :");
		lblBalance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBalance.setBounds(14, 156, 126, 25);
		contentPanel.add(lblBalance);

		doctorNameCB = new JComboBox();
		doctorNameCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		doctorNameCB.setBounds(751, 9, 218, 25);
		contentPanel.add(doctorNameCB);

		JLabel lblInvoiceNo = new JLabel("Select Doctor :");
		lblInvoiceNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInvoiceNo.setBounds(615, 9, 126, 25);
		contentPanel.add(lblInvoiceNo);

		searchItemTF = new JTextField();
		searchItemTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		searchItemTF.setBounds(14, 239, 119, 20);
		contentPanel.add(searchItemTF);
		searchItemTF.setColumns(10);
		searchItemTF.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();
					}
				});
		
		timer = new Timer(500, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// highlightAll();
				timer.stop();
				String str = searchItemTF.getText() + "";
				if(patientNameTF.getText().equals("") && !searchItemTF.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Please Enter Patient IPD ID First",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					searchItemTF.setText("");
					return;
				}
				item_risk_type = "";
				if (!str.equals("")) {
					getItemName(str);
				} else {

					itemDescTF.setText("");
					qtyIssuedTF.setText("");
					mrpTF.setText("");
					packSizeET.setText("");
					item_risk_type = "";
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					itemBatchName.removeAllElements();
					batchNameCB.setModel(itemBatchName);
					btnEditItem.setText("");
					batchQtyTF.setText("");
					btnEditItem.setBackground(Color.WHITE);
					measUnit.removeAllElements();
					itemIDSTR = "";
				}
				
			}
		});
         

		itemDescTF = new JTextField();
		itemDescTF.setEditable(false);
		itemDescTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemDescTF.setBounds(14, 270, 188, 20);
		contentPanel.add(itemDescTF);
		itemDescTF.setColumns(10);

		itemNameCB = new JComboBox();
		itemNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					itemNameSTR = itemNameCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}

				if (itemNameCB.getSelectedIndex() > -1) {
					itemIDSTR = itemID.get(itemNameCB.getSelectedIndex());
				}
				itemBatchName.removeAllElements();
				batchNameCB.setModel(itemBatchName);
				itemDescTF.setText("");
				qtyInHandTF.setText("");
				itemPriceTF.setText("");
				itemLocationTF.setText("");
				mrpTF.setText("");
				packSizeET.setText("");
				taxTF.setText("");
				batchQtyTF.setText("");
				surchargeTF.setText("");
				getrisktype(itemIDSTR);
				getItemDetail(itemIDSTR);
				if (itemName.getSize() > 0) {
					getItemBatchName(itemIDSTR);
					itemDescTF.setText("" + itemHSNSTR);
					qtyInHandTF.setText("" + quantity);
					itemPriceTF.setText("" + price);
					afterIssued = quantity - qtyIssued;
					itemLocationTF.setText(itemLocationSTR);
					taxTF.setText("" + taxValue);
					surchargeTF.setText("" + surchargeValue);
				}
			}
		});
		itemNameCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemNameCB.setBounds(143, 239, 210, 20);
		contentPanel.add(itemNameCB);

		qtyInHandTF = new JTextField();
		qtyInHandTF.setEditable(false);
		qtyInHandTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyInHandTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		qtyInHandTF.setBounds(321, 269, 92, 20);
		contentPanel.add(qtyInHandTF);
		qtyInHandTF.setColumns(10);

		qtyIssuedTF = new FocusTextField();
		qtyIssuedTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyIssuedTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		qtyIssuedTF.setBounds(590, 239, 68, 20);
		contentPanel.add(qtyIssuedTF);
		qtyIssuedTF.setColumns(10);
		qtyIssuedTF.addKeyListener(new KeyAdapter() {
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
		qtyIssuedTF.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					Toolkit.getDefaultToolkit().beep();
					System.out.println("ENTER pressed");
					addButton();
				}
			}
		});

		qtyIssuedTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = qtyIssuedTF.getText() + "";
				if (!str.equals("")) {

					qtyIssued = Integer.parseInt(str);

				} else {

					qtyIssued = 0;

				}
				afterIssued = quantity - qtyIssued;
				if (qtyIssued > batchQty) {
					batchQtyTF.setForeground(Color.RED);
				} else {
					batchQtyTF.setForeground(Color.BLACK);
				}
				itemValue();

			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = qtyIssuedTF.getText() + "";
				if (!str.equals("")) {

					qtyIssued = Integer.parseInt(str);

				} else {

					qtyIssued = 0;

				}
				afterIssued = quantity - qtyIssued;
				if (qtyIssued > batchQty) {
					batchQtyTF.setForeground(Color.RED);
				} else {
					batchQtyTF.setForeground(Color.BLACK);
				}
				itemValue();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = qtyIssuedTF.getText() + "";
				if (!str.equals("")) {

					qtyIssued = Integer.parseInt(str);

				} else {

					qtyIssued = 0;

				}
				afterIssued = quantity - qtyIssued;
				if (qtyIssued > batchQty) {
					batchQtyTF.setForeground(Color.RED);
				} else {
					batchQtyTF.setForeground(Color.BLACK);
				}
				itemValue();
			}
		});

		JLabel lblSearch = new JLabel("Search Item");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSearch.setBounds(14, 214, 119, 14);
		contentPanel.add(lblSearch);

		JLabel lblQty = new JLabel("Total Qty. in Hand");
		lblQty.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblQty.setBounds(205, 273, 119, 14);
		contentPanel.add(lblQty);

		JLabel lblqtyIssued = new JLabel("Enter Qty.");
		lblqtyIssued.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblqtyIssued.setBounds(590, 214, 88, 14);
		contentPanel.add(lblqtyIssued);

		JButton btnNewButton = new JButton("Add Line");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addButton();
			}
		});
		btnNewButton.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					Toolkit.getDefaultToolkit().beep();
					System.out.println("ENTER pressed");
				}
			}
		});

		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(913, 205, 106, 31);
		contentPanel.add(btnNewButton);

		JLabel lblExpireDate = new JLabel("Expire Date :");
		lblExpireDate.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExpireDate.setBounds(639, 273, 86, 14);
		contentPanel.add(lblExpireDate);

		final JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int cur_selectedRow;
				cur_selectedRow = table.getSelectedRow();
				cur_selectedRow = table.convertRowIndexToModel(cur_selectedRow);
				String toDelete = table.getModel()
						.getValueAt(cur_selectedRow, 0).toString();
				itemIDV.remove(cur_selectedRow);
				itemCategoryV.remove(cur_selectedRow);
				itemNameV.remove(cur_selectedRow);
				itemDescV.remove(cur_selectedRow);
				itemHSN_CODEV.remove(cur_selectedRow);
				issuedQtyV.remove(cur_selectedRow);
				expiryDateV.remove(cur_selectedRow);
				itemBatchIDV.remove(cur_selectedRow);
				itemBatchV.remove(cur_selectedRow);
				unitPriceV.remove(cur_selectedRow);
				mrpPriceV.remove(cur_selectedRow);
				measUnitV.remove(cur_selectedRow);
				totalValueV.remove(cur_selectedRow);
				taxPercentageV.remove(cur_selectedRow);
				DiscountV.remove(cur_selectedRow);
				taxAmountV.remove(cur_selectedRow);
				surchargeV.remove(cur_selectedRow);
				itemRiskValueV.remove(cur_selectedRow);
				surchargeAmountValueV.remove(cur_selectedRow);
				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRemove.setBounds(913, 243, 106, 31);
		contentPanel.add(btnRemove);
		btnRemove.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 327, 1002, 183);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] {
				"Item ID", "Item Name", "Item Batch", "Unit Price", "Quantity",
				"tax", "Discount", "Total Value", "expiry" }));
		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setMinWidth(150);
		table.getColumnModel().getColumn(4).setPreferredWidth(150);
		table.getColumnModel().getColumn(4).setMinWidth(150);
		table.getColumnModel().getColumn(5).setPreferredWidth(150);
		table.getColumnModel().getColumn(5).setMinWidth(150);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setMinWidth(100);
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						int selectedRowIndex = table.getSelectedRow();
						selectedRowIndex = table
								.convertRowIndexToModel(selectedRowIndex);
						int selectedColumnIndex = table.getSelectedColumn();
						btnRemove.setEnabled(true);
					}
				});
		table.addMouseListener(new MouseListener() {

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
				if (arg0.getClickCount() == 2) {
					int cur_selectedRow;
					cur_selectedRow = table.getSelectedRow();
					cur_selectedRow = table
							.convertRowIndexToModel(cur_selectedRow);
					String toDelete = table.getModel()
							.getValueAt(cur_selectedRow, 0).toString();

					searchItemTF.setText(itemNameV.get(cur_selectedRow));
					batchNameCB.setSelectedItem(itemBatchV.get(cur_selectedRow));
					qtyIssuedTF.setText((issuedQtyV.get(cur_selectedRow)));
					taxTF.setText(taxPercentageV.get(cur_selectedRow));
					discountTF.setText(DiscountV.get(cur_selectedRow));
					surchargeTF.setText(surchargeV.get(cur_selectedRow));

					itemIDV.remove(cur_selectedRow);
					itemNameV.remove(cur_selectedRow);
					itemCategoryV.remove(cur_selectedRow);
					itemDescV.remove(cur_selectedRow);
					itemHSN_CODEV.remove(cur_selectedRow);
					issuedQtyV.remove(cur_selectedRow);
					expiryDateV.remove(cur_selectedRow);
					itemBatchIDV.remove(cur_selectedRow);
					itemBatchV.remove(cur_selectedRow);
					unitPriceV.remove(cur_selectedRow);
					mrpPriceV.remove(cur_selectedRow);
					measUnitV.remove(cur_selectedRow);
					totalValueV.remove(cur_selectedRow);
					taxPercentageV.remove(cur_selectedRow);
					DiscountV.remove(cur_selectedRow);
					itemRiskValueV.remove(cur_selectedRow);
					taxAmountV.remove(cur_selectedRow);
					surchargeV.remove(cur_selectedRow);
					surchargeAmountValueV.remove(cur_selectedRow);
					loadDataToTable();
					btnRemove.setEnabled(false);
					qtyIssuedTF.requestFocusInWindow();
				}
			}
		});
		scrollPane.setViewportView(table);

		JSeparator separator = new JSeparator();
		separator.setBounds(14, 192, 966, 2);
		contentPanel.add(separator);

		JButton btnNewButton_1 = new JButton("Generate Bill");
		btnNewButton_1.addActionListener(new ActionListener() {
			private String item_batch;

			@Override
			public void actionPerformed(ActionEvent e) {

				if (patientNameTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter person name", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (MobileNoTF.getText().toString().equals("")) {
					MobileNoTF.setText("N/A");
				}
				if (itemIDV.size() <= 0) {
					JOptionPane.showMessageDialog(null,
							"Please enter atleast one item", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					doctorNameCB.getSelectedItem().toString();
				} catch (Exception e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null,
							"Please select doctor name", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				BillingDBConnection billingDBConnection = new BillingDBConnection();
				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				int index = 0;
				double totalTaxPayabble = taxAmountValue2 + totalTaxAmount;
				String[] data = new String[30];
				data[0] = serachIPDNoTF.getText() + "";
				data[1] = WordUtils.capitalize(patientNameTF.getText()
						.toString());
				data[2] = MobileNoTF.getText().toString();
				data[3] = doctorNameCB.getSelectedItem().toString();
				data[4] = insuranceCB.getSelectedItem().toString();
				data[5] = insuranceIDTB.getText().toString();
				data[6] = payableSTR; // payable
				data[7] = "" + totalAmountTF.getText().toString();
				data[8] = "" + totalPayableAmountTF.getText().toString();
				data[9] = "" + finalTaxValue;
				data[10] = "" + finalDiscountValue; // user
				data[11] = totalTaxPayabble + "";
				data[12] = totalSurchargeAmount + "";
				data[13] = DateFormatChange.StringToMysqlDate(new Date());
				data[14] = "" + timeFormat.format(cal1.getTime());
				data[15] = StoreMain.userID;
				data[16] = StoreMain.userName;
				data[17] = "NA";
				data[18] = "NA";
				data[19] = "NA";
				data[20] ="IPD";

				try {
					index = billingDBConnection.inserBillEntry(data);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
				BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
				data[0] = index + "";
				data[1] = patientNameTF.getText().toString();
				data[2] = doctorNameCB.getSelectedItem().toString();
				data[3] = insuranceCB.getSelectedItem().toString();
				data[4] = payableSTR;

				for (int i = 0; i < itemIDV.size(); i++) {
					data[5] = itemIDV.get(i);
					data[6] = itemNameV.get(i);
					data[7] = itemDescV.get(i);
					data[8] = itemHSN_CODEV.get(i);
					data[9] = itemBatchIDV.get(i);
					// data[10] = itemBatchV.get(i);
					data[11] = unitPriceV.get(i);
					data[12] = issuedQtyV.get(i);
					data[13] = taxPercentageV.get(i);
					data[14] = DiscountV.get(i);
					data[15] = taxAmountV.get(i);
					data[16] = totalValueV.get(i);
					data[17] = surchargeV.get(i);
					data[18] = surchargeAmountValueV.get(i);
					data[19] = expiryDateV.get(i);
					data[20] = DateFormatChange.StringToMysqlDate(new Date());
					data[21] = "" + timeFormat.format(cal1.getTime());
					data[22] = StoreMain.userID;
					data[23] = StoreMain.userName;
					data[24]=mrpPriceV.get(i)+""+"";
					data[25]=measUnitV.get(i)+"";
					data[26]=itemRiskValueV.get(i)+"";
					ResultSet rs1 = billingDBConnection
							.retrieveBatchName(itemBatchIDV.get(i));
					int counter = 0;
					// try {
					// counter = rs1.getRow();
					// } catch (SQLException e2) {
					// // TODO Auto-generated catch block
					// e2.printStackTrace();
					// }

					// ResultSet rs = db.retrievePOItemData(ID);
					 item_batch = "";
					try {
						while (rs1.next()) {
							item_batch = rs1.getString("item_batch");
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					data[10] = item_batch;
					data[27] ="0";
					data[28] ="0";
					try {
						billingDBConnection.insertBillingItems(data);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						itemsDBConnection.subtractStock(itemIDV.get(i),
								issuedQtyV.get(i));
						batchTrackingDBConnection.subtractStock(
								itemBatchIDV.get(i), issuedQtyV.get(i),
								DateFormatChange.StringToMysqlDate(new Date()),
								StoreMain.userName);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				data[0] = "" + (insuranceCB.getSelectedIndex() + 1);
				data[1] = insuranceCB.getSelectedItem().toString();
				data[2] = "INSURANCE";
				data[3] = "0";
				data[4] = "" + totalPayableAmountTF.getText().toString();
				data[5] = "0";
				data[6] = "0";
				data[7] = DateFormatChange.StringToMysqlDate(new Date());
				data[8] = "" + timeFormat.format(cal1.getTime());
				data[9] = "" + StoreMain.userName;
				data[10] = "Bill No. " + index;

				GLAccountDBConnection glAccountDBConnection = new GLAccountDBConnection();

				try {
					glAccountDBConnection.inserGLAccountInsurance(data);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				glAccountDBConnection.closeConnection();
				
				batchTrackingDBConnection.closeConnection();
				itemsDBConnection.closeConnection();
				OPDDBConnection ipddbConnection = new OPDDBConnection();
				String[] data1 = new String[17];
				for (int i = 0; i < itemIDV.size(); i++) {
					data1[0] = ipd_id;
					data1[1] = "" + itemNameV.get(i);

					data1[2] = itemDescV.get(i);
					data1[3] = totalValueV.get(i);

					data1[4] = ""
							+ DateFormatChange.StringToMysqlDate(new Date());
					data1[5] = "" + timeFormat.format(cal1.getTime());
					data1[6] = itemIDV.get(i);
					data1[7] = "" + p_id;
					data1[8] = "" + p_name;
					data1[9] = Math.round((Double.parseDouble(totalValueV.get(i))/Double.parseDouble(issuedQtyV.get(i)))*100.0)/100.0+"";
					data1[10] = issuedQtyV.get(i);
					data1[11] =itemCategoryV.get(i);
					data1[12] = "" + StoreMain.userName;
					data1[13] = "Medical Store";
					data1[14] =mrpPriceV.get(i);
					data1[15] =itemBatchIDV.get(i);
					ResultSet rs1 = billingDBConnection
							.retrieveBatchName(itemBatchIDV.get(i));
					 item_batch = "";
					try {
						while (rs1.next()) {
							item_batch = rs1.getString(5);
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					data1[16] = item_batch;
					try {
						ipddbConnection.inserDataIpdExpense(data1);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						
					}
				}
				billingDBConnection.closeConnection();
				ipddbConnection.closeConnection();
				ipddbConnection = new OPDDBConnection();
				double tot = totalChargesIPD + finalTotalValue;
				double finalValue = Math.round(tot * 100.0) / 100.0;
				try {
					ipddbConnection.updateTotalAmount(ipd_id, finalValue + "");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					ipddbConnection.closeConnection();
					e1.printStackTrace();
				}
				ipddbConnection.closeConnection();

				// JOptionPane.showMessageDialog(null,
				// "Item Issued Successfully",
				// "Issued Items", JOptionPane.INFORMATION_MESSAGE);
				if (billBrowser != null)
					billBrowser.populateTable1(
							DateFormatChange.StringToMysqlDate(new Date()),
							DateFormatChange.StringToMysqlDate(new Date()),"");
				dispose();
				try {
					new MedicalStoreIPDPdf(index+"" );
				} catch (DocumentException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_1.setBounds(862, 521, 153, 39);
		contentPanel.add(btnNewButton_1);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(862, 568, 153, 39);
		contentPanel.add(btnCancel);

		serachIPDNoTF = new JTextField();
		serachIPDNoTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		serachIPDNoTF.setColumns(10);
		serachIPDNoTF.setBounds(150, 45, 218, 25);
		contentPanel.add(serachIPDNoTF);
		serachIPDNoTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = serachIPDNoTF.getText() + "";
				if (!str.equals("")) {
					IPD(str);
				} else {
					patientNameTF.setText("");
					MobileNoTF.setText("");
					doctorNameCB.setSelectedIndex(0);
					insuranceCB.setSelectedIndex(0);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = serachIPDNoTF.getText() + "";
				if (!str.equals("")) {
					IPD(str);
				} else {
					patientNameTF.setText("");
					MobileNoTF.setText("");
					doctorNameCB.setSelectedIndex(0);
					insuranceCB.setSelectedIndex(0);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = serachIPDNoTF.getText() + "";
				if (!str.equals("")) {
					IPD(str);
				} else {

					patientNameTF.setText("");
					MobileNoTF.setText("");
					doctorNameCB.setSelectedIndex(0);
					insuranceCB.setSelectedIndex(0);
				}
			}
		});

		JLabel lblBillNo = new JLabel("Bill No.  :");
		lblBillNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBillNo.setBounds(14, 9, 126, 25);
		contentPanel.add(lblBillNo);

		JTextField billNoTF = new JTextField("Bill No.");
		billNoTF.setEditable(false);
		billNoTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		billNoTF.setBounds(150, 9, 218, 25);
		contentPanel.add(billNoTF);
		BillingDBConnection billingDBConnection = new BillingDBConnection();
		billNoTF.setText(billingDBConnection.retrieveCounterData() + "");
		billingDBConnection.closeConnection();

		batchNameCB = new JComboBox();
		batchNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (batchNameCB.getSelectedIndex() > 0) {
					batchIDSTR = batchID.get(batchNameCB.getSelectedIndex());
				} else {
					return;
				}
				try {
					itemBatchNameSTR = batchNameCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}

				batchQtyTF.setText("");
				
				expiryDateTF.setText("");
				mrpTF.setText("");
				packSizeET.setText("");
				getItemBatch(batchIDSTR);
				getItemBatchDetail(batchIDSTR);
				getItemDetail(itemIDSTR);
				if (itemBatchName.getSize() > 0) {

					int addDays = 20;
					itemPriceTF.setText("" + price);
					batchQtyTF.setText("" + batchQty);
					expiryDateTF.setText("" + expiryDateSTR);
					mrpTF.setText("" + mrptotal);
					packSizeET.setText(packSize + "");
					taxTF.setText("" + taxValue);
					surchargeTF.setText(surchargeValue + "");
					String untildate = DateFormatChange
							.StringToMysqlDate(new Date());
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd");
					Calendar cal = Calendar.getInstance();
					try {
						cal.setTime((Date) dateFormat.parse(untildate));

						cal.add(Calendar.DATE, addDays);
						String convertedDate = dateFormat.format(cal.getTime());
						Date date1 = (Date) dateFormat.parse(convertedDate);
						Date date2 = (Date) dateFormat.parse(expiryDateSTR);
						if (date1.compareTo(date2) < 0)
							expiryDateTF.setForeground(Color.BLACK);
						else if (date1.compareTo(date2) > 0)
							expiryDateTF.setForeground(Color.RED);
						else
							expiryDateTF.setForeground(Color.BLACK);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		batchNameCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		batchNameCB.setBounds(361, 216, 219, 20);
		contentPanel.add(batchNameCB);
		batchNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (batchNameCB.getSelectedIndex() > 0) {
					itemBatchNameSTR = (String) batchNameCB.getSelectedItem();

				}
			}
		});
		JLabel lblSelectBatch = new JLabel("Select Batch");
		lblSelectBatch.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSelectBatch.setBounds(361, 202, 92, 14);
		contentPanel.add(lblSelectBatch);

		JLabel lblSelectItem = new JLabel("Select Item");
		lblSelectItem.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSelectItem.setBounds(144, 214, 92, 14);
		contentPanel.add(lblSelectItem);

		batchQtyTF = new JTextField();
		batchQtyTF.setHorizontalAlignment(SwingConstants.RIGHT);
		batchQtyTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		batchQtyTF.setEditable(false);
		batchQtyTF.setColumns(10);
		batchQtyTF.setBounds(537, 269, 92, 20);
		contentPanel.add(batchQtyTF);

		JLabel lblBatchQtyIn = new JLabel("Batch Qty. in Hand");
		lblBatchQtyIn.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblBatchQtyIn.setBounds(421, 273, 119, 14);
		contentPanel.add(lblBatchQtyIn);

		taxTF = new FocusTextField();
		taxTF.setEditable(false);
		taxTF.setHorizontalAlignment(SwingConstants.RIGHT);
		taxTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		taxTF.setColumns(10);
		taxTF.setBounds(668, 239, 68, 20);
		contentPanel.add(taxTF);
		taxTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE)
						|| (vChar == KeyEvent.VK_DELETE) || vChar == '.')) {
					e.consume();

					// ||vChar== '.'
				}
			}
		});
		taxTF.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					Toolkit.getDefaultToolkit().beep();
					System.out.println("ENTER pressed");
					addButton();
				}
			}
		});

		taxTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = taxTF.getText() + "";
				if (!str.equals("")) {

					taxValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					taxValue = 0;
					itemValue();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = taxTF.getText() + "";
				if (!str.equals("")) {

					taxValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					taxValue = 0;
					itemValue();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = taxTF.getText() + "";
				if (!str.equals("")) {

					taxValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					taxValue = 0;
					itemValue();
				}
			}
		});

		JLabel lblTax = new JLabel("Tax");
		lblTax.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTax.setBounds(668, 214, 88, 14);
		contentPanel.add(lblTax);

		discountTF = new FocusTextField();
		discountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		discountTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		discountTF.setColumns(10);
		discountTF.setBounds(825, 239, 78, 20);
		contentPanel.add(discountTF);
		discountTF.setVisible(false);
		discountTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE)
						|| (vChar == KeyEvent.VK_DELETE) || vChar == '.')) {
					e.consume();
				}
			}
		});
		discountTF.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					Toolkit.getDefaultToolkit().beep();
					System.out.println("ENTER pressed");
					addButton();
				}
			}
		});
		discountTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = discountTF.getText() + "";
				if (!str.equals("")) {

					discountValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					discountValue = 0;
					itemValue();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = discountTF.getText() + "";
				if (!str.equals("")) {

					discountValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					discountValue = 0;
					itemValue();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = discountTF.getText() + "";
				if (!str.equals("")) {

					discountValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					discountValue = 0;
					itemValue();
				}
			}
		});

		JLabel lblDiscount = new JLabel("Discount");
		lblDiscount.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDiscount.setBounds(825, 214, 88, 14);
		contentPanel.add(lblDiscount);
		lblDiscount.setVisible(false);
		itemPriceTF = new JTextField();
		itemPriceTF.setHorizontalAlignment(SwingConstants.RIGHT);
		itemPriceTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		itemPriceTF.setEditable(false);
		itemPriceTF.setColumns(10);
		itemPriceTF.setBounds(96, 296, 106, 20);
		contentPanel.add(itemPriceTF);

		JLabel lblItemPrice = new JLabel("Item Price");
		lblItemPrice.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblItemPrice.setBounds(18, 301, 78, 14);
		contentPanel.add(lblItemPrice);

		totalValueTF = new JTextField();
		totalValueTF.setHorizontalAlignment(SwingConstants.RIGHT);
		totalValueTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		totalValueTF.setEditable(false);
		totalValueTF.setColumns(10);
		totalValueTF.setBounds(321, 296, 92, 20);
		contentPanel.add(totalValueTF);

		JLabel lblTotalValue = new JLabel("Total Value");
		lblTotalValue.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTotalValue.setBounds(243, 300, 68, 14);
		contentPanel.add(lblTotalValue);

		expiryDateTF = new JTextField();
		expiryDateTF.setHorizontalAlignment(SwingConstants.RIGHT);
		expiryDateTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		expiryDateTF.setEditable(false);
		expiryDateTF.setColumns(10);
		expiryDateTF.setBounds(719, 270, 155, 20);
		contentPanel.add(expiryDateTF);

		finalDiscountTF = new JTextField();
		finalDiscountTF.setEditable(false);
		finalDiscountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		finalDiscountTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		finalDiscountTF.setColumns(10);
		finalDiscountTF.setBounds(110, 568, 146, 25);
		contentPanel.add(finalDiscountTF);
		finalDiscountTF.setVisible(false);
		finalDiscountTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE)
						|| (vChar == KeyEvent.VK_DELETE) || vChar == '.')) {
					e.consume();

					// ||vChar== '.'
				}
			}
		});

		finalDiscountTF.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = finalDiscountTF.getText() + "";
						if (!str.equals("")) {

							finalDiscountValue = Double.parseDouble("0" + str);
							FinalAmount();

						} else {

							finalDiscountValue = 0;
							FinalAmount();
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = finalDiscountTF.getText() + "";
						if (!str.equals("")) {

							finalDiscountValue = Double.parseDouble("0" + str);
							FinalAmount();

						} else {

							finalDiscountValue = 0;
							FinalAmount();
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = finalDiscountTF.getText() + "";
						if (!str.equals("")) {

							finalDiscountValue = Double.parseDouble("0" + str);
							FinalAmount();

						} else {

							finalDiscountValue = 0;
							FinalAmount();
						}
					}
				});

		JLabel lblDicount = new JLabel("Discount :");
		lblDicount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDicount.setBounds(30, 568, 78, 25);
		contentPanel.add(lblDicount);
		lblDicount.setVisible(false);
		finalTaxTF = new JTextField();
		finalTaxTF.setEditable(false);
		finalTaxTF.setHorizontalAlignment(SwingConstants.RIGHT);
		finalTaxTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		finalTaxTF.setColumns(10);
		finalTaxTF.setBounds(110, 532, 146, 25);
		contentPanel.add(finalTaxTF);
		finalTaxTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE)
						|| (vChar == KeyEvent.VK_DELETE) || vChar == '.')) {
					e.consume();

					// ||vChar== '.'
				}
			}
		});

		finalTaxTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = finalTaxTF.getText() + "";
				if (!str.equals("")) {

					finalTaxValue = Double.parseDouble("0" + str);
					FinalAmount();

				} else {

					finalTaxValue = 0;
					FinalAmount();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = finalTaxTF.getText() + "";
				if (!str.equals("")) {

					finalTaxValue = Double.parseDouble("0" + str);
					FinalAmount();

				} else {

					finalTaxValue = 0;
					FinalAmount();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = finalTaxTF.getText() + "";
				if (!str.equals("")) {

					finalTaxValue = Double.parseDouble("0" + str);
					FinalAmount();

				} else {

					finalTaxValue = 0;
					FinalAmount();
				}
			}
		});

		JLabel lblTax_1 = new JLabel("Tax :");
		lblTax_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTax_1.setBounds(30, 532, 78, 25);
		contentPanel.add(lblTax_1);

		JLabel lblTotalPayablet = new JLabel("Total Payable :");
		lblTotalPayablet.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotalPayablet.setBounds(288, 568, 98, 25);
		contentPanel.add(lblTotalPayablet);

		totalAmountTF = new JTextField();
		totalAmountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		totalAmountTF.setEditable(false);
		totalAmountTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		totalAmountTF.setColumns(10);
		totalAmountTF.setBounds(390, 528, 146, 25);
		contentPanel.add(totalAmountTF);

		JLabel lblCashRecieved = new JLabel("Cash Recieved :");
		lblCashRecieved.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCashRecieved.setBounds(561, 532, 98, 25);
		contentPanel.add(lblCashRecieved);

		cashRecievedTF = new JTextField();
		cashRecievedTF.setHorizontalAlignment(SwingConstants.RIGHT);
		cashRecievedTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cashRecievedTF.setColumns(10);
		cashRecievedTF.setBounds(661, 532, 146, 25);
		contentPanel.add(cashRecievedTF);
		cashRecievedTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE)
						|| (vChar == KeyEvent.VK_DELETE) || vChar == '.')) {
					e.consume();

					// ||vChar== '.'
				}
			}
		});

		cashRecievedTF.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = cashRecievedTF.getText() + "";
						if (!str.equals("")) {

							double amount = Double.parseDouble("0" + str);

							double balance = amount
									- Double.parseDouble("0"
											+ totalPayableAmountTF.getText()
													.toString());
							balance = Math.round(balance * 100.000) / 100.000;
							balanceAmountTF.setText("" + balance);
						} else {

							balanceAmountTF.setText("");
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = cashRecievedTF.getText() + "";
						if (!str.equals("")) {

							double amount = Double.parseDouble("0" + str);
							double balance = amount
									- Double.parseDouble("0"
											+ totalPayableAmountTF.getText()
													.toString());
							balance = Math.round(balance * 100.000) / 100.000;
							balanceAmountTF.setText("" + balance);
						} else {

							balanceAmountTF.setText("");
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = cashRecievedTF.getText() + "";
						if (!str.equals("")) {

							double amount = Double.parseDouble("0" + str);
							double balance = amount
									- Double.parseDouble("0"
											+ totalPayableAmountTF.getText()
													.toString());
							balance = Math.round(balance * 100.000) / 100.000;
							balanceAmountTF.setText("" + balance);
						} else {

							balanceAmountTF.setText("");
						}
					}
				});
		totalAmountTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = totalAmountTF.getText() + "";
				if (!str.equals("")) {

					double amount = Double.parseDouble("0" + str);
					if (amount - Math.floor(amount) > 0.5) {
						totalPayableAmountTF.setText("" + Math.ceil(amount));
					} else {
						totalPayableAmountTF.setText("" + Math.floor(amount));
					}

				} else {

					totalPayableAmountTF.setText("");
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = totalAmountTF.getText() + "";
				if (!str.equals("")) {

					double amount = Double.parseDouble("0" + str);

					if (amount - Math.floor(amount) > 0.5) {
						totalPayableAmountTF.setText("" + Math.ceil(amount));
					} else {
						totalPayableAmountTF.setText("" + Math.floor(amount));
					}
				} else {

					totalPayableAmountTF.setText("");
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = totalAmountTF.getText() + "";
				if (!str.equals("")) {

					double amount = Double.parseDouble("0" + str);

					if (amount - Math.floor(amount) > 0.5) {
						totalPayableAmountTF.setText("" + Math.ceil(amount));
					} else {
						totalPayableAmountTF.setText("" + Math.floor(amount));
					}
				} else {

					totalPayableAmountTF.setText("");
				}
			}
		});

		JLabel lblBalance_1 = new JLabel("Balance :");
		lblBalance_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBalance_1.setBounds(561, 568, 98, 25);
		contentPanel.add(lblBalance_1);

		balanceAmountTF = new JTextField();
		balanceAmountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		balanceAmountTF.setEditable(false);
		balanceAmountTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		balanceAmountTF.setColumns(10);
		balanceAmountTF.setBounds(661, 568, 146, 25);
		contentPanel.add(balanceAmountTF);

		JLabel lblSelectInsurance = new JLabel("Select Head :");
		lblSelectInsurance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSelectInsurance.setBounds(615, 56, 126, 25);
		contentPanel.add(lblSelectInsurance);

		insuranceCB = new JComboBox();
		insuranceCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		insuranceCB.setBounds(751, 56, 218, 25);
		contentPanel.add(insuranceCB);

		JLabel lblItemLocation = new JLabel("Item Location");
		lblItemLocation.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblItemLocation.setBounds(447, 300, 92, 14);
		contentPanel.add(lblItemLocation);

		itemLocationTF = new JTextField();
		itemLocationTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		itemLocationTF.setEditable(false);
		itemLocationTF.setColumns(10);
		itemLocationTF.setBounds(536, 296, 126, 20);
		contentPanel.add(itemLocationTF);

		totalPayableAmountTF = new JTextField();
		totalPayableAmountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		totalPayableAmountTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		totalPayableAmountTF.setEditable(false);
		totalPayableAmountTF.setColumns(10);
		totalPayableAmountTF.setBounds(390, 568, 146, 25);
		contentPanel.add(totalPayableAmountTF);

		JLabel lblTotalAmount = new JLabel("Total Amount :");
		lblTotalAmount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotalAmount.setBounds(288, 532, 98, 25);
		contentPanel.add(lblTotalAmount);

		surchargeTF = new FocusTextField();
		surchargeTF.setEditable(false);
		surchargeTF.setHorizontalAlignment(SwingConstants.RIGHT);
		surchargeTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		surchargeTF.setColumns(10);
		surchargeTF.setBounds(746, 239, 69, 20);
		contentPanel.add(surchargeTF);
		surchargeTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE)
						|| (vChar == KeyEvent.VK_DELETE) || vChar == '.')) {
					e.consume();

					// ||vChar== '.'
				}
			}
		});
		surchargeTF.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					Toolkit.getDefaultToolkit().beep();
					System.out.println("ENTER pressed");
					addButton();
				}
			}
		});
		surchargeTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = surchargeTF.getText() + "";
				if (!str.equals("")) {

					surchargeValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					surchargeValue = 0;
					itemValue();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = surchargeTF.getText() + "";
				if (!str.equals("")) {

					surchargeValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					surchargeValue = 0;
					itemValue();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = surchargeTF.getText() + "";
				if (!str.equals("")) {

					surchargeValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					surchargeValue = 0;
					itemValue();
				}
			}
		});

		JLabel lblSurChg = new JLabel("Sur. Chg.");
		lblSurChg.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSurChg.setBounds(746, 214, 69, 14);
		contentPanel.add(lblSurChg);

		JLabel lblInsuranceId = new JLabel("Insurance ID *:");
		lblInsuranceId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInsuranceId.setBounds(615, 103, 126, 25);
		contentPanel.add(lblInsuranceId);

		insuranceIDTB = new JTextField();
		insuranceIDTB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		insuranceIDTB.setColumns(10);
		insuranceIDTB.setBounds(751, 103, 218, 25);
		contentPanel.add(insuranceIDTB);

		 btnEditItem = new JButton("Edit Item");
		btnEditItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (!itemIDSTR.equals("") || itemIDSTR.equals("0")) {
					// EditItem editItem = new EditItem(itemIDSTR);
					// editItem.newBillInstatnce(NewBillForm1.this);
					// editItem.setModal(true);
					// editItem.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "Please select item",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		btnEditItem.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnEditItem.setBounds(913, 285, 106, 31);
		contentPanel.add(btnEditItem);
		btnEditItem.setEnabled(false);
		enterBatchNO = new JTextField();
		enterBatchNO.setFont(new Font("Tahoma", Font.BOLD, 11));
		enterBatchNO.setBounds(363, 239, 219, 20);
		contentPanel.add(enterBatchNO);
		enterBatchNO.setColumns(10);

		JLabel lblPatientId = new JLabel("Patient ID :");
		lblPatientId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPatientId.setBounds(14, 84, 126, 25);
		contentPanel.add(lblPatientId);

		patientIDTF = new JTextField();
		patientIDTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		patientIDTF.setEditable(false);
		patientIDTF.setColumns(10);
		patientIDTF.setBounds(150, 84, 218, 25);
		contentPanel.add(patientIDTF);

		JLabel lblMrp = new JLabel("MRP");
		lblMrp.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMrp.setBounds(668, 298, 45, 14);
		contentPanel.add(lblMrp);

		mrpTF = new JTextField();
		mrpTF.setEditable(false);
		mrpTF.setHorizontalAlignment(SwingConstants.RIGHT);
		mrpTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		mrpTF.setColumns(10);
		mrpTF.setBounds(703, 296, 68, 20);
		contentPanel.add(mrpTF);

		JLabel lblPackSize = new JLabel("Pack Size");
		lblPackSize.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPackSize.setBounds(780, 300, 62, 14);
		contentPanel.add(lblPackSize);

		packSizeET = new JTextField();
		packSizeET.setEditable(false);
		packSizeET.setHorizontalAlignment(SwingConstants.RIGHT);
		packSizeET.setFont(new Font("Tahoma", Font.BOLD, 11));
		packSizeET.setColumns(10);
		packSizeET.setBounds(835, 297, 68, 20);
		contentPanel.add(packSizeET);

		enterBatchNO.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = enterBatchNO.getText().toUpperCase() + "";
				batchQtyTF.setText("");
				expiryDateTF.setText("");
				quantity = 0;
				if (batchID.size() > 0) {

					if (!str.equals("")) {

						getItemBatchName(itemIDSTR, str);
						System.out.println(str + "  " + checkBatch.size()
								+ "   " + itemIDSTR);
						if (checkBatch.size() > 0) {
							if (checkBatch.size() == 1) {
								batchNameCB.setSelectedItem(checkBatch.get(0));
							} else {
								if (checkBatch.get(0).equals(str)) {
									batchNameCB.setSelectedItem(checkBatch
											.get(0));
								} else {
									batchNameCB.setSelectedIndex(0);
								}
							}
						} else {
							batchNameCB.setSelectedIndex(0);
						}

					} else {

						batchNameCB.setSelectedIndex(0);
					}
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = enterBatchNO.getText().toUpperCase() + "";
				batchQtyTF.setText("");
				expiryDateTF.setText("");
				quantity = 0;
				if (batchID.size() > 0) {
					if (!str.equals("")) {

						getItemBatchName(itemIDSTR, str);

						if (checkBatch.size() > 0) {
							if (checkBatch.size() == 1) {
								batchNameCB.setSelectedItem(checkBatch.get(0));
							} else {
								if (checkBatch.get(0).equals(str)) {
									batchNameCB.setSelectedItem(checkBatch
											.get(0));
								} else {
									batchNameCB.setSelectedIndex(0);
								}
							}
						} else {
							batchNameCB.setSelectedIndex(0);
						}

					} else {

						batchNameCB.setSelectedIndex(0);
					}
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = enterBatchNO.getText().toUpperCase() + "";
				batchQtyTF.setText("");
				expiryDateTF.setText("");
				quantity = 0;
				if (batchID.size() > 0) {
					if (!str.equals("")) {

						getItemBatchName(itemIDSTR, str);

						if (checkBatch.size() > 0) {
							if (checkBatch.size() == 1) {
								batchNameCB.setSelectedItem(checkBatch.get(0));
							} else {
								if (checkBatch.get(0).equals(str)) {
									batchNameCB.setSelectedItem(checkBatch
											.get(0));
								} else {
									batchNameCB.setSelectedIndex(0);
								}
							}
						} else {
							batchNameCB.setSelectedIndex(0);
						}

					} else {

						batchNameCB.setSelectedIndex(0);
					}
				}
			}
		});

		getAllDoctors();
		getAllinsurance();
	}

	public void getItemName(String index) {

		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.searchItemWithIdOrNmae(index);
		itemName.removeAllElements();
		itemID.clear();
		int i = 0;
		try {
			while (resultSet.next()) {
				itemID.add(resultSet.getObject(1).toString());
				itemName.addElement(resultSet.getObject(2).toString());
				item_risk_type=resultSet.getObject(11).toString();
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		itemNameCB.setModel(itemName);
		if(item_risk_type.equals("General")){
			btnEditItem.setText(item_risk_type);
		}else{
			btnEditItem.setText(item_risk_type);
		}
		
		if(item_risk_type.contains("High Risk")){
			btnEditItem.setBackground(Color.RED);
			btnEditItem.setForeground(Color.white);
		}else if(item_risk_type.equals("SHC-H1")){
			btnEditItem.setBackground(Color.ORANGE);
			btnEditItem.setForeground(Color.white);
		}else{
			btnEditItem.setBackground(Color.WHITE);
		}
		if (i > 0) {
			itemNameCB.setSelectedIndex(0);
		}
	}

	public void getItemBatchName(String index) {

		BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
		ResultSet resultSet = batchTrackingDBConnection.itemBatch(index);
		itemBatchName.removeAllElements();
		itemBatchName1.removeAllElements();
		itemBatchName.addElement("Please Select");
		itemBatchName1.addElement("Please Select");
		batchID.clear();
		batchID.add("0");
		int i = 0;
		try {
			while (resultSet.next()) {
				batchID.add(resultSet.getObject(1).toString());
				itemBatchName1.addElement(resultSet.getObject(2).toString()
						+ "(Batch" + (i + 1) + ")");

				// itemBatchName1.addItem(new ComboItem("Visible String 1",
				// "Value 1"));
				itemBatchName.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		batchTrackingDBConnection.closeConnection();
		batchNameCB.setModel(itemBatchName1);
		if (i > 0) {
			enterBatchNO.setText("");
			batchNameCB.setSelectedIndex(0);
		}

	}

	public void getItemBatchName(String index, String batch) {

		BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
		ResultSet resultSet = batchTrackingDBConnection.itemBatch(index, batch);
		checkBatch.clear();
		int i = 0;
		try {
			while (resultSet.next()) {
				checkBatch.add(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		batchTrackingDBConnection.closeConnection();
	}

	public void addButton() {

		if (itemDescSTR.equals("")) {
			JOptionPane.showMessageDialog(null, "Please select item",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (qtyIssuedTF.getText().toString().equals("") || qtyIssuedTF.getText().toString().equals("0")) {
			JOptionPane.showMessageDialog(null, "Please enter issued qty.",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (batchQty - qtyIssued < 0) {

			JOptionPane.showMessageDialog(null,
					"Issued Quantity is greater than Batch Quantity",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			qtyIssuedTF.requestFocus();
			return;
		}
		if (expiryDateSTR.equals("")) {
			JOptionPane.showMessageDialog(null, "Please enter expiry date",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		else if (itemBatchIDV.indexOf(batchIDSTR) != -1) {
			JOptionPane.showMessageDialog(null, "this item is already entered",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return;
		} else if (batchQtyTF.getText().toString().equals("")) {
			JOptionPane.showMessageDialog(null,
					"Issued Quantity is greater than Batch Quantity",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int addDays = 20;
		String untildate = DateFormatChange.StringToMysqlDate(new Date());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime((Date) dateFormat.parse(untildate));

			cal.add(Calendar.DATE, addDays);
			String convertedDate = dateFormat.format(cal.getTime());
			Date date1 = (Date) dateFormat.parse(convertedDate);
			Date date2 = (Date) dateFormat.parse(expiryDateSTR);
			if (date1.compareTo(date2) < 0)
				expiryDateTF.setForeground(Color.BLACK);
			else if (date1.compareTo(date2) > 0) {
				expiryDateTF.setForeground(Color.RED);
				// JOptionPane.showMessageDialog(null,
				// "Please Check Batch Expiry Date", "Input Error",
				// JOptionPane.ERROR_MESSAGE);
				// return;
			} else
				expiryDateTF.setForeground(Color.BLACK);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		itemIDV.add(itemIDSTR);
		itemCategoryV.add(itemCategory);
		itemNameV.add(itemNameSTR);
		itemDescV.add(itemDescSTR);
		itemHSN_CODEV.add(itemHSNSTR);
		issuedQtyV.add(qtyIssued + "");
		expiryDateV.add(expiryDateSTR);
		itemBatchIDV.add(batchIDSTR);
		itemBatchV.add(itemBatchNameSTR);
		unitPriceV.add(price + "");
		mrpPriceV.add(mrptotal+"");
		measUnitV.add(packSize+"");
		totalValueV.add("" + itemValue);
		taxPercentageV.add(taxValue + "");
		DiscountV.add(discountValue + "");
		taxAmountV.add(taxAmountValue + "");
		surchargeV.add(surchargeValue + ""); 
		getrisktype(itemIDSTR);
		itemRiskValueV.add(item_risk_type+"");
		surchargeAmountValueV.add(surchargeAmountValue + "");
		loadDataToTable();
		searchItemTF.setText("");
		searchItemTF.requestFocusInWindow();
		enterBatchNO.setText("");
	}

	public void getItemDetail(String index) {
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail2(index);
		try {
			while (resultSet.next()) {
				itemDescSTR = resultSet.getObject(3).toString();	
				quantity = Integer.parseInt(resultSet.getObject(8).toString());
				itemLocationSTR = resultSet.getObject(12).toString();
				itemHSNSTR = resultSet.getObject(14).toString();
				formulaActive = resultSet.getObject(17).toString();
				itemCategory = resultSet.getObject(18).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();

	}

	public void getItemBatchDetail(String index) {
		GeneralDBConnection formula = new GeneralDBConnection();
		value = Double.parseDouble(formula.retrieveFormula1());
		formula.closeConnection();
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetailBatch2(index);
		mrp = 0;
		purchase = 0;
		double tot = 0, tot1 = 0;
		double sp = 0;
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
					if (purchase >= 10000 && purchase <= 20000) {
						sp = purchase * 1.15;
					} else if (purchase > 20000 && purchase <= 30000) {
						sp = purchase * 1.10;
					} else if(purchase>=30000){
						sp = purchase * 1.05;
					}else {
						double tempvar1 = mrp / packSize;
						double mrpwithouttax = (tempvar1 *100)
								/(100 + taxValue + surchargeValue);
						//double mrpwithouttax = tempvar1 - tempvar2;
						double temp = 2.5 * purchase;
						if (mrpwithouttax > temp) {
							sp = temp;
							
						} else {
							double mrpless1prcnt = mrpwithouttax
									- (mrpwithouttax * 0.01);
							sp = mrpless1prcnt;
						}
					}
				}
			}
			
			sp = Math.round(sp * 100.00) / 100.00;
			price = sp;
			if(insuranceCB.getSelectedIndex()!=0) {
				sp=((mrptotal*100)/(100+(taxValue+surchargeValue)))/packSize;
				sp = Math.round(sp * 100.00) / 100.00;
				price = sp; 
			}else if(itemCategory.toLowerCase().contains("implant")) {
				price = purchase; 
			}
			System.out.println("priceeeeeeeeee" + price);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getItemBatch(String index) {

		batchQty = 0;
		BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
		ResultSet resultSet = batchTrackingDBConnection.itemBatchDetail(index);
		int i = 0;
		try {
			while (resultSet.next()) {

				batchQty = Integer.parseInt(resultSet.getObject(1).toString());
				expiryDateSTR = resultSet.getObject(2).toString();

				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		batchTrackingDBConnection.closeConnection();

	}

	private void loadDataToTable() {
		// get size of the hashmap
		int size = itemIDV.size();
		finalTotalValue = 0;
		totalTaxAmount = 0;
		totalSurchargeAmount = 0;
		ObjectArray_ListOfexamsSpecs = new Object[size][11];

		for (int i = 0; i < itemIDV.size(); i++) {

			ObjectArray_ListOfexamsSpecs[i][0] = itemIDV.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = itemNameV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = itemBatchV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = unitPriceV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = issuedQtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = taxPercentageV.get(i) + "%("
					+ taxAmountV.get(i) + ")";
			ObjectArray_ListOfexamsSpecs[i][6] = surchargeV.get(i) + "%("
					+ surchargeAmountValueV.get(i) + ")";
			ObjectArray_ListOfexamsSpecs[i][7] = DiscountV.get(i);
			ObjectArray_ListOfexamsSpecs[i][8] = totalValueV.get(i);
			ObjectArray_ListOfexamsSpecs[i][9] = expiryDateV.get(i);
			finalTotalValue = finalTotalValue
					+ Double.parseDouble(totalValueV.get(i));
			totalTaxAmount = totalTaxAmount
					+ Double.parseDouble(taxAmountV.get(i));
			totalSurchargeAmount = totalSurchargeAmount
					+ Double.parseDouble(surchargeAmountValueV.get(i));
		}
		DefaultTableModel model = new DefaultTableModel(
				ObjectArray_ListOfexamsSpecs,
				new String[] { "Item ID", "Item Name", "Item Batch",
						"Unit Price", "Quantity", "tax", "Surcharge",
						"Discount", "Total Value", "expiry" }) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;// This causes all cells to be not editable
			}
		};
		table.setModel(model);
		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setMinWidth(75);

		table.getColumnModel().getColumn(4).setPreferredWidth(75);
		table.getColumnModel().getColumn(4).setMinWidth(75);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setMinWidth(100);

		table.getColumnModel().getColumn(9).setPreferredWidth(100);
		table.getColumnModel().getColumn(9).setMinWidth(100);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
		finalTotalValue = Math.round(finalTotalValue * 100.000) / 100.000;
		totalTaxAmount = Math.round(totalTaxAmount * 100.000) / 100.000;
		totalAmountTF.setText(finalTotalValue + "");
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

	public void IPD(String opdID) {
		patientNameTF.setText("");
		MobileNoTF.setText("");
		doctorNameCB.setSelectedIndex(0);
		insuranceCB.setSelectedIndex(0);
		try {
			OPDDBConnection db = new OPDDBConnection();
			ResultSet rs = db.retrieveAllDataWithIPDId(opdID);
			while (rs.next()) {

				p_id = rs.getObject(1).toString().toString();
				setPatientDetail(p_id);
				patientIDTF.setText(p_id);
				p_name = rs.getObject(2).toString();
				patientNameTF.setText("" + rs.getObject(2).toString());

				totalChargesIPD = Double.parseDouble(""
						+ rs.getObject(5).toString());
				doctorNameCB.setSelectedIndex(doctors.getIndexOf(rs
						.getObject(3).toString()));
				ipd_id = rs.getObject(6).toString();
				p_insurance=rs.getObject(7).toString();
			}
			db.closeConnection();
		} catch (SQLException ex) {

		}
	}

	public void setPatientDetail(String indexId) {
		PatientDBConnection patientDBConnection = new PatientDBConnection();
		ResultSet resultSet = patientDBConnection
				.retrieveDataWithIndex(indexId);
		try {
			while (resultSet.next()) {

				MobileNoTF.setText(resultSet.getObject(6).toString());
				insuranceCB.setSelectedIndex(insuranceModel
						.getIndexOf(resultSet.getObject(7).toString()));
				p_address = (resultSet.getObject(4).toString() + "," + resultSet
						.getObject(5).toString());
				insuranceIDTB.setText("" + resultSet.getObject(8).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		patientDBConnection.closeConnection();
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

	public void itemValue() {
		System.out.println(itemCategory);
		if(insuranceCB.getSelectedIndex()!=0) {
			double gst=mrptotal-((mrptotal*100)/(100+(taxValue+surchargeValue)));
			gst=gst/2;
			gst = Math.round(gst * 100.00) / 100.00;
			taxAmountValue = gst;
			surchargeAmountValue = gst;
			
			double sp=((mrptotal*100)/(100+(taxValue+surchargeValue)))/packSize;
			sp = Math.round(sp * 100.00) / 100.00;
			price = sp; 
			itemValue = qtyIssued * (mrptotal/packSize);
			itemValue = Math.round(itemValue * 100.00) / 100.00;
		}else if(itemCategory.toLowerCase().contains("implant")) {
			double gst=((purchase*(100+taxValue+surchargeValue))/100)-purchase;
			gst=gst/2;
			gst = Math.round(gst * 100.00) / 100.00;
			taxAmountValue = gst;
			surchargeAmountValue = gst;
			
			double sp=((purchase*(100+taxValue+surchargeValue))/100);
			sp = Math.round(sp * 100.00) / 100.00;
			price = purchase; 
			itemValue = qtyIssued * sp;
			itemValue = Math.round(itemValue * 100.00) / 100.00;	
		}
		else {
		getCuttingCharges();

		double mrpRate = mrp * qtyIssued;
		itemValue = qtyIssued * price;
		newUnitTax=0;
		int pack=packSize;
		double t=0;
		System.out.print(itemValue);
		if(!(pack<=3)) {

			int pck=pack/3;
			if(((qtyIssued%pack) > 0 && (qtyIssued%pack) <= pck))
			{  
				t = itemValue * (cutting_chrg_1 / 100.0f);
				newUnitTax=t/qtyIssued;
				t = Math.round(t * 100.000) / 100.000;
				System.out.print(t);
				cutting_chrg = cutting_chrg_1;
				System.out.println("cutting_chrg 1:"+cutting_chrg);
			}	
			if(((qtyIssued%pack) > pck && (qtyIssued%pack) <= pck*2)) 
			{
				t = itemValue * (cutting_chrg_2 / 100.0f);
				newUnitTax=t/qtyIssued;
				t = Math.round(t * 100.000) / 100.000;
				System.out.print(t);
				cutting_chrg = cutting_chrg_2; 
				System.out.println("cutting_chrg 2:"+cutting_chrg);
			}
			if(((qtyIssued%pack) > pck*2  && (qtyIssued%pack)< pack ))
			{
				t = itemValue * (cutting_chrg_3 / 100.0f);
				newUnitTax=t/qtyIssued;
				t = Math.round(t * 100.000) / 100.000;
				System.out.print(t);
				cutting_chrg = cutting_chrg_3;
				System.out.println("cutting_chrg 3:"+cutting_chrg);
			}
			if((qtyIssued%pack)==0)
			{
				t=0;
				cutting_chrg=0;
				newUnitTax=0;
				System.out.println("cutting_chrg 4:"+cutting_chrg);
				
			}
		}
		else 
		{
			t=0;
			cutting_chrg=0;
			newUnitTax=0;
			System.out.println("cutting_chrg 5:"+cutting_chrg);
		}
		
		itemPriceTF.setText(""+Math.round((price+newUnitTax) * 100.0) / 100.0);
		itemValue = itemValue + t;
		
		double k = itemValue * (taxValue / 100.0f);
		k = Math.round(k * 100.000) / 100.000;
		double s = itemValue * (surchargeValue / 100.0f);
		s = Math.round(s * 100.000) / 100.000;
		taxAmountValue = k;
		surchargeAmountValue = s;
		
		itemValue = itemValue + k;
		itemValue = itemValue + s;
		itemValue = itemValue - discountValue;
		itemValue = Math.round(itemValue * 100.000) / 100.000;
		mrpRate = Math.round(mrpRate * 100.000) / 100.000;

		if (itemValue >= mrpRate) {
			itemValue = mrpRate;
		}
		
		}
		
		 
		totalValueTF.setText("" + itemValue);
	}
	
	private void getCuttingCharges() {
		// TODO Auto-generated method stub
		CancelRestockFeeDB db=new CancelRestockFeeDB();
		ResultSet rs=db.retrieveDataNew("IPD");
		cutting_chrg_1=0;
		cutting_chrg_2=0;
		cutting_chrg_3=0;
		cutting_chrgV.clear();
		try {
			while(rs.next())
			{
				cutting_chrgV.add(rs.getObject(2).toString());	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cutting_chrg_3=Double.parseDouble(cutting_chrgV.get(0));
		cutting_chrg_2=Double.parseDouble(cutting_chrgV.get(1));
		cutting_chrg_1=Double.parseDouble(cutting_chrgV.get(2));
		
		db.closeConnection();
	}
	
	public void FinalAmount() {
		double finalTotalValue = this.finalTotalValue;
		finalTotalValue = finalTotalValue - finalDiscountValue;
		double k = finalTotalValue * (finalTaxValue / 100.0f);
		k = Math.round(k * 100.000) / 100.000;
		taxAmountValue2 = k;
		finalTotalValue = finalTotalValue + k;
		finalTotalValue = Math.round(finalTotalValue * 100.000) / 100.000;
		totalAmountTF.setText("" + finalTotalValue);
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("KeyReleased: ");
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("KeyReleased: ");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("KeyReleased: ");
	}

	public JTextField getPatientIDTF() {
		return patientIDTF;
	}
	public void getrisktype(String index) {
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetailNew(index);
		// mrp = 0;
		// purchase = 0;
		// double tot = 0, tot1 = 0;
		// int packSize = 1;
		try {
			while (resultSet.next()) {

				item_risk_type = resultSet.getObject(1).toString();

			}
		} catch (SQLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
		}
		if (item_risk_type.equals("General")) {
			btnEditItem.setText(item_risk_type);
		} else {
			btnEditItem.setText(item_risk_type);
		}

		if (item_risk_type.equals("High Risk")) {
			btnEditItem.setBackground(Color.RED);
			btnEditItem.setForeground(Color.RED);
		} else if (item_risk_type.equals("SHC-H1")) {
			btnEditItem.setBackground(Color.ORANGE);
			btnEditItem.setForeground(Color.ORANGE);
		} else {
			btnEditItem.setBackground(Color.WHITE);
		}
	}
}
