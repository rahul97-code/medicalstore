package hms.store.gui;

import hms.admin.gui.AdminMain;
import hms.doc.scanning.DocScanning;
import hms.doctor.database.DoctorDBConnection;
import hms.formula.MSOPDPriceFormula;
import hms.formula.manipulation.CuttingCharges;
import hms.formula.manipulation.KarunaDiscount;
import hms.formula.manipulation.OnlineDiscount;
import hms.gl.database.GLAccountDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.main.GeneralDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.patient.database.PatientDBConnection;
import hms.patient.slippdf.MedicalStoreBillSlippdf;
//import hms.payments.PaymentMain;
//import hms.payments.database.PaymentsDBConnection;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.text.WordUtils;

import ca.odell.glazedlists.UndoRedoSupport.Edit;

import com.itextpdf.text.DocumentException;
import javax.swing.JTextArea;

public class OPDBillEntry extends JDialog implements KeyListener {

	private final JPanel contentPanel = new JPanel();
	private JTextField patientNameTF;
	private JTextField MobileNoTF;
	private JComboBox doctorNameCB; 
	public JTextField searchItemTF;
	private JTextField itemDescTF;
	private JTextField qtyInHandTF;
	private JTextField qtyIssuedTF;
	JComboBox paymentModeCB;
	private JTable table;
	private Timer timer;
	JButton btnEditItem;
	String item_risk_type = "";
	int packSize = 1; 
	private static DocScanning instance = null;

	// cutting tx value of items
	private double cutting_chrg_1=0,cutting_chrg_2=0,cutting_chrg_3=0,newUnitTax=0;

	final DefaultComboBoxModel departmentName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemBatchName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemBatchName1 = new DefaultComboBoxModel();
	final DefaultComboBoxModel measUnit = new DefaultComboBoxModel();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel insuranceModel = new DefaultComboBoxModel();
	final DefaultComboBoxModel paymentModes = new DefaultComboBoxModel();
	Vector<String> itemID = new Vector<String>();
	Vector<String> cutting_chrgV = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> KarunaRelief = new Vector<String>();
	Vector<String> newUnitPrice = new Vector<String>();
	Vector<String> karunaAmount = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> itemHSN_CODEV = new Vector<String>();
	Vector<String> issuedQtyV = new Vector<String>();
	Vector<String> itemBatchIDV = new Vector<String>();
	Vector<String> itemBatchV = new Vector<String>();
	Vector<String> unitPriceV = new Vector<String>();
	Vector<String> mrpPriceV = new Vector<String>();
	Vector<String> measUnitV = new Vector<String>();
	Vector<String> totalValueV = new Vector<String>();
	Vector<String> taxPercentageV = new Vector<String>();
	Vector<String> DiscountV = new Vector<String>();
	Vector<String> taxAmountV = new Vector<String>();
	Vector<String> surchargeV = new Vector<String>();
	Vector<String> surchargeAmountValueV = new Vector<String>();
	Vector<String> batchID = new Vector<String>();
	Vector<String> itemRiskValueV = new Vector<String>();
	double mrp = 0,karunaRelief=0;
	double purchase = 0;
	double value = 0;
	Vector<String> expiryDateV = new Vector<String>();
	String departmentNameSTR, departmentID, personname, supplierID;
	String itemIDSTR = "", itemNameSTR, itemDescSTR, itemHSNSTR, taxTypeSTR,
			expiryDateSTR = "", issuedDateSTR = "", dueDateSTR = "",
			previouseStock = "", itemBatchNameSTR = "", itemLocationSTR = "",
			payableSTR = "", formulaActive = "";
	double afterIssued = 0, discountValue = 0, taxValue = 0, itemValue,
			surchargeValue = 0, finalTaxValue = 0, finalDiscountValue = 0,
			finalTotalValue = 0,finalkarunaDStotalValue = 0, price = 0, batchQty = 0, taxAmountValue = 0,
			surchargeAmountValue = 0, taxAmountValue2, totalTaxAmount = 0,cutting_chrg=0,
			totalSurchargeAmount = 0, completeTaxAmount;
	int quantity = 0, qtyIssued = 0, quantity1 = 0;
	String batchIDSTR = "0";
	Object[][] ObjectArray_ListOfexamsSpecs;
	private JComboBox itemNameCB;
	private JTextField serachOPDNoTF;
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
	private JTextField balanceAmountTF;
	private JComboBox batchNameCB;
	private JComboBox insuranceCB;
	private JTextField itemLocationTF;
	private JTextField totalPayableAmountTF;
	private JTextField surchargeTF;
	BillBrowser billBrowser;
	private JTextField insuranceIDTB;
	private JTextArea patientAddressTA;
	private JTextArea doctorAddressTA;
	JTextField mrpTF;
	private JTextField packSizeET;
	String currenttime;
	private double karunaDiscountPer=0;
	public boolean iskaruna=false;
	private JTextField KDiscountTF;
	private double newunitprice=0;
	private double discountamount=0;
	private boolean alreadyPaid;
	private String req_urn=null;
	private double finalCharges;
	private String payMode;
	private double totalExamCharge;
	CuttingCharges cuttingCharges ;

	public static void main(String[] args) {
		new OPDBillEntry(null,null).setVisible(true);;
	}
	/**
	 * Create the dialog.
	 */
	public OPDBillEntry(final JFrame owner,final BillBrowser billBrowser) {
		super(owner, "Billing Form", false);
		addKeyListener(this);
		this.billBrowser = billBrowser;
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				OPDBillEntry.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 40, 1031, 680);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		currenttime = sdf.format(new Date());  
		//		System.out.println(str+"");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblCredit = new JLabel("Enter OPD No. :");
		lblCredit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCredit.setBounds(235, 11, 126, 25);
		contentPanel.add(lblCredit);

		patientNameTF = new JTextField();
		patientNameTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		patientNameTF.setColumns(10);
		patientNameTF.setBounds(371, 47, 218, 25);
		contentPanel.add(patientNameTF);

		JLabel lblDebit = new JLabel("Patient Name :");
		lblDebit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDebit.setBounds(235, 47, 126, 25);
		contentPanel.add(lblDebit);

		MobileNoTF = new JTextField();
		MobileNoTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		MobileNoTF.setColumns(10);
		MobileNoTF.setBounds(371, 123, 218, 25);
		contentPanel.add(MobileNoTF);

		JLabel lblBalance = new JLabel("Mobile No. :");
		lblBalance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBalance.setBounds(235, 123, 126, 25);
		contentPanel.add(lblBalance);

		doctorNameCB = new JComboBox();
		doctorNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					// opd_doctorname =
					// doctorNameCB.getSelectedItem().toString();
					getDoctorDetail(doctorNameCB.getSelectedItem().toString());
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		doctorNameCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		doctorNameCB.setBounds(751, 9, 218, 25);
		contentPanel.add(doctorNameCB);

		JLabel lblInvoiceNo = new JLabel("Select Doctor :");
		lblInvoiceNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInvoiceNo.setBounds(615, 9, 126, 25);
		contentPanel.add(lblInvoiceNo);

		searchItemTF = new JTextField();
		searchItemTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		searchItemTF.setBounds(10, 190, 119, 20);
		contentPanel.add(searchItemTF);
		searchItemTF.setColumns(10);
		searchItemTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				restartTimer();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				restartTimer();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				restartTimer();
			}

			private void restartTimer() {
				if (timer.isRunning()) {
					timer.stop();
				}
				timer.start();
			}
		});

		timer = new Timer(350, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				String searchText = searchItemTF.getText();

				// Check if patient name is empty and item search text is not
				if (patientNameTF.getText().isEmpty() && !searchText.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please Select Patient Name or OPD ID First", "Input Error", JOptionPane.ERROR_MESSAGE);
					searchItemTF.setText("");
					return;
				}

				if (!searchText.isEmpty()) {
					getItemName(searchText);
				} else {
					resetFields();
				}
			}
		});


		itemDescTF = new JTextField();
		itemDescTF.setEditable(false);
		itemDescTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemDescTF.setBounds(92, 221, 106, 20);
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
				taxTF.setText("");
				batchQtyTF.setText("");
				expiryDateTF.setText("");
				surchargeTF.setText("");
				mrpTF.setText("");
				packSizeET.setText("");

				getrisktype(itemIDSTR);
				if (itemName.getSize() > 0) {
					getItemBatchName(itemIDSTR);
					itemDescTF.setText("" + itemHSNSTR);
					qtyInHandTF.setText("" + quantity1);
					itemLocationTF.setText(itemLocationSTR);
				}
			}
		});
		itemNameCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemNameCB.setBounds(139, 190, 210, 20);
		contentPanel.add(itemNameCB);

		qtyInHandTF = new JTextField();
		qtyInHandTF.setEditable(false);
		qtyInHandTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyInHandTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		qtyInHandTF.setBounds(317, 220, 92, 20);
		contentPanel.add(qtyInHandTF);
		qtyInHandTF.setColumns(10);

		qtyIssuedTF = new FocusTextField();
		qtyIssuedTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyIssuedTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		qtyIssuedTF.setBounds(586, 190, 68, 20);
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
				updateQtyIssued();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateQtyIssued();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateQtyIssued();
			}

			private void updateQtyIssued() {
				String str = qtyIssuedTF.getText();
				qtyIssued = str.isEmpty() ? 0 : Integer.parseInt(str);
				afterIssued = quantity - qtyIssued;

				batchQtyTF.setForeground(qtyIssued > batchQty ? Color.RED : Color.BLACK);
				itemValue();
			}
		});


		JLabel lblSearch = new JLabel("Search Item");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSearch.setBounds(10, 165, 119, 14);
		contentPanel.add(lblSearch);

		JLabel lblQty = new JLabel("Total Qty. in Hand");
		lblQty.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblQty.setBounds(208, 224, 106, 14);
		contentPanel.add(lblQty);

		JLabel lblqtyIssued = new JLabel("Enter Qty.");
		lblqtyIssued.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblqtyIssued.setBounds(586, 165, 88, 14);
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
		btnNewButton.setBounds(909, 166, 106, 31);
		contentPanel.add(btnNewButton);

		JLabel lblExpireDate = new JLabel("Expire Date :");
		lblExpireDate.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExpireDate.setBounds(635, 224, 86, 14);
		contentPanel.add(lblExpireDate);

		final JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int cur_selectedRow;
				cur_selectedRow = table.getSelectedRow();
				cur_selectedRow = table.convertRowIndexToModel(cur_selectedRow);
				String toDelete = table.getModel().getValueAt(cur_selectedRow, 0).toString();
				itemIDV.remove(cur_selectedRow);
				itemNameV.remove(cur_selectedRow);
				itemDescV.remove(cur_selectedRow);
				itemHSN_CODEV.remove(cur_selectedRow);
				issuedQtyV.remove(cur_selectedRow);
				expiryDateV.remove(cur_selectedRow);
				itemBatchIDV.remove(cur_selectedRow);
				itemBatchV.remove(cur_selectedRow);
				unitPriceV.remove(cur_selectedRow);
				karunaAmount.remove(cur_selectedRow);
				mrpPriceV.remove(cur_selectedRow);
				measUnitV.remove(cur_selectedRow);
				totalValueV.remove(cur_selectedRow);
				taxPercentageV.remove(cur_selectedRow);
				DiscountV.remove(cur_selectedRow);
				KarunaRelief.remove(cur_selectedRow);
				newUnitPrice.remove(cur_selectedRow);
				taxAmountV.remove(cur_selectedRow);
				surchargeV.remove(cur_selectedRow);
				itemRiskValueV.remove(cur_selectedRow);
				surchargeAmountValueV.remove(cur_selectedRow);
				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRemove.setBounds(909, 200, 106, 31);
		contentPanel.add(btnRemove);
		btnRemove.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(13, 279, 1002, 232);
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
						selectedRowIndex = table.convertRowIndexToModel(selectedRowIndex);
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
					cur_selectedRow = table.convertRowIndexToModel(cur_selectedRow);
					String toDelete = table.getModel().getValueAt(cur_selectedRow, 0).toString();

					searchItemTF.setText(itemNameV.get(cur_selectedRow));
					batchNameCB.setSelectedItem(itemBatchV.get(cur_selectedRow));
					qtyIssuedTF.setText((issuedQtyV.get(cur_selectedRow)));
					taxTF.setText(taxPercentageV.get(cur_selectedRow));
					discountTF.setText(DiscountV.get(cur_selectedRow));
					surchargeTF.setText(surchargeV.get(cur_selectedRow));

					itemIDV.remove(cur_selectedRow);
					itemNameV.remove(cur_selectedRow);
					itemDescV.remove(cur_selectedRow);
					itemHSN_CODEV.remove(cur_selectedRow);
					issuedQtyV.remove(cur_selectedRow);
					expiryDateV.remove(cur_selectedRow);
					itemBatchIDV.remove(cur_selectedRow);
					itemBatchV.remove(cur_selectedRow);
					unitPriceV.remove(cur_selectedRow);
					karunaAmount.remove(cur_selectedRow);
					mrpPriceV.remove(cur_selectedRow);
					measUnitV.remove(cur_selectedRow);
					totalValueV.remove(cur_selectedRow);
					taxPercentageV.remove(cur_selectedRow);
					DiscountV.remove(cur_selectedRow);
					KarunaRelief.remove(cur_selectedRow);
					newUnitPrice.remove(cur_selectedRow);
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
		separator.setBounds(24, 162, 966, 2);
		contentPanel.add(separator);

		JButton btnNewButton_1 = new JButton("Generate Bill");
		btnNewButton_1.addActionListener(new ActionListener() {
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
				if(!insuranceCB.getSelectedItem().equals("Unknown")) {
					int i=JOptionPane.showConfirmDialog(null,"Please select insurance type!","",JOptionPane.YES_NO_OPTION);
					if(i!=JOptionPane.YES_OPTION) {
						return;
					}

				}
				//				if(paymentModeCB.getSelectedIndex()==0) {
				//					JOptionPane.showMessageDialog(null,
				//							"Please select payment mode.", "Input Error",
				//							JOptionPane.ERROR_MESSAGE);
				//					return;
				//				}
				//				if(!alreadyPaid) {	
				//					PaymentMain MachinePayDialog=new PaymentMain(new String[]{totalPayableAmountTF.getText()+"","",patientNameTF.getText()+"",StoreMain.userName,doctorNameCB.getSelectedItem().toString()},"OPD Bill");
				//					MachinePayDialog.setModal(true);
				//					MachinePayDialog.setVisible(true);
				//					req_urn=MachinePayDialog.getRequestedUrn();
				//					finalCharges = MachinePayDialog.getTotalCharges();
				//					payMode=MachinePayDialog.getPaymentMode();
				//					boolean result=MachinePayDialog.getFinalStatus();
				//					if(!result) {
				//						System.out.println("{"+result+ ", "+finalCharges+", "+req_urn+", "+payMode+"} Rejected Tnx ");
				//						return;
				//					}  
				//				}

				BillingDBConnection billingDBConnection = new BillingDBConnection();
				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				int index = 0;
				double totalTaxPayabble = taxAmountValue2 + totalTaxAmount;
				String[] data = new String[31];
				data[0] = serachOPDNoTF.getText() + "";
				data[1] = WordUtils.capitalize(patientNameTF.getText().toString());
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
				data[17] = patientAddressTA.getText().toString();
				data[18] = doctorAddressTA.getText().toString();
				data[19] = paymentModeCB.getSelectedItem().toString();
				data[20] ="OPD";
				data[21] =Math.round(finalkarunaDStotalValue*100.00)/100.00+"";
				data[22] =null;
				try {
					index = billingDBConnection.inserOPDBillEntry(data);
					billingDBConnection.updateReturnBillId(index, index);
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
					data[10] = itemBatchV.get(i);
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
					data[24] = mrpPriceV.get(i) + "";
					data[25] = measUnitV.get(i) + "";
					data[26]=itemRiskValueV.get(i)+"";
					ResultSet rs1 = billingDBConnection.retrieveBatchName(itemBatchIDV.get(i));
					int counter = 0;
					data[27] = KarunaRelief.get(i)+"";
					data[28] = newUnitPrice.get(i)+"";
					data[29] ="N/A";
					data[30] = index+"";
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

				OPDDBConnection karunaDiscountDB = new OPDDBConnection();
				data[0] = index + "";
				data[1] = Math.round(Double.parseDouble(totalAmountTF.getText()))+"";
				data[2] = Math.round(finalkarunaDStotalValue*100.00)/100.00+"";
				data[3] = "" + StoreMain.userName;
				data[4] = "" + StoreMain.userID;
				data[5] = "1";
				try {
					karunaDiscountDB.updateKarunaDiscount(serachOPDNoTF.getText().toString(),data);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				karunaDiscountDB.closeConnection();
				glAccountDBConnection.closeConnection();
				billingDBConnection.closeConnection();
				batchTrackingDBConnection.closeConnection();
				itemsDBConnection.closeConnection();
				if (billBrowser != null)
					billBrowser.populateTable1(
							DateFormatChange.StringToMysqlDate(new Date()),
							DateFormatChange.StringToMysqlDate(new Date()),"");
				dispose(); 
				try {
					new MedicalStoreBillSlippdf(index + "","OPD", insuranceCB.getSelectedItem().toString(),true,iskaruna);
				} catch (DocumentException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//				openOrShowDocScanning(owner);
				if (owner instanceof AdminMain) {((AdminMain) owner).showDocScanning(owner); }
				else if (owner instanceof StoreMain) { ((StoreMain) owner).showDocScanning(owner); }

				
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_1.setBounds(862, 541, 153, 39);
		contentPanel.add(btnNewButton_1);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(862, 592, 153, 39);
		contentPanel.add(btnCancel);
		
		  addMouseListener(new MouseAdapter() {
	            @Override
	            public void mousePressed(MouseEvent e) {
	                toFront();
	            }
	        });

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(OPDBillEntry.class
				.getResource("/icons/billimage.png")));
		lblNewLabel.setBounds(52, 47, 136, 101);
		contentPanel.add(lblNewLabel);

		serachOPDNoTF = new JTextField();
		serachOPDNoTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		serachOPDNoTF.setColumns(10);
		serachOPDNoTF.setBounds(371, 11, 218, 25);
		contentPanel.add(serachOPDNoTF);
		serachOPDNoTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updatePatientDetails();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updatePatientDetails();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updatePatientDetails();
			}

			private void updatePatientDetails() {
				String str = serachOPDNoTF.getText();
				if (!str.isEmpty()) {
					opd(str);
				} else {
					clearPatientDetails();
				}
			}

			private void clearPatientDetails() {
				patientNameTF.setText("");
				MobileNoTF.setText("");
				doctorNameCB.setSelectedIndex(0);
				insuranceCB.setSelectedIndex(0);
				patientAddressTA.setText("");
				KDiscountTF.setText("");
				karunaDiscountPer = 0;
			}
		});


		JLabel lblBillNo = new JLabel("Bill No.  :");
		lblBillNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBillNo.setBounds(14, 9, 126, 25);
		contentPanel.add(lblBillNo);

		JTextField billNoTF = new JTextField("Bill No.");
		billNoTF.setEditable(false);
		billNoTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		billNoTF.setBounds(79, 11, 119, 25);
		contentPanel.add(billNoTF);
		BillingDBConnection billingDBConnection = new BillingDBConnection();
		billNoTF.setText(billingDBConnection.retrieveCounterData() + "");
		billingDBConnection.closeConnection();

		batchNameCB = new JComboBox();
		batchNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					itemBatchNameSTR = batchNameCB.getSelectedItem().toString().split("\\(", 2)[0];
					batchIDSTR = batchID.get(batchNameCB.getSelectedIndex());
				} catch (Exception e) {
					// Handle exception if necessary
				}

				if (batchNameCB.getSelectedIndex() > -1) {
					resetFields();
					getItemDetail(itemIDSTR); 
					getItemBatch(batchIDSTR);
					getItemBatchDetail(batchIDSTR);
					if (itemBatchName.getSize() > 0) {
						setBatchDetails();
						checkExpiryDate();
					}
				}
			}

			private void resetFields() {
				batchQtyTF.setText("");
				expiryDateTF.setText("");
				itemPriceTF.setText("");
				mrpTF.setText("");
				packSizeET.setText("");
			}

			private void setBatchDetails() {
				itemPriceTF.setText(String.valueOf(price));
				mrpTF.setText(String.valueOf(mrp));
				batchQtyTF.setText(String.valueOf(batchQty));
				expiryDateTF.setText(expiryDateSTR);
				taxTF.setText(String.valueOf(taxValue));
				surchargeTF.setText(String.valueOf(surchargeValue));
				packSizeET.setText(String.valueOf(packSize));
			}

			private void checkExpiryDate() {
				try {
					String untildate = DateFormatChange.StringToMysqlDate(new Date());
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Calendar cal = Calendar.getInstance();
					cal.setTime(dateFormat.parse(untildate));
					cal.add(Calendar.DATE, 20);

					Date date1 = dateFormat.parse(dateFormat.format(cal.getTime()));
					Date date2 = dateFormat.parse(expiryDateSTR);

					expiryDateTF.setForeground(date1.compareTo(date2) < 0 ? Color.BLACK : Color.RED);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		});

		batchNameCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		batchNameCB.setBounds(357, 190, 219, 20);
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
		lblSelectBatch.setBounds(357, 165, 92, 14);
		contentPanel.add(lblSelectBatch);

		JLabel lblSelectItem = new JLabel("Select Item");
		lblSelectItem.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSelectItem.setBounds(140, 165, 92, 14);
		contentPanel.add(lblSelectItem);

		batchQtyTF = new JTextField();
		batchQtyTF.setHorizontalAlignment(SwingConstants.RIGHT);
		batchQtyTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		batchQtyTF.setEditable(false);
		batchQtyTF.setColumns(10);
		batchQtyTF.setBounds(533, 220, 92, 20);
		contentPanel.add(batchQtyTF);

		JLabel lblBatchQtyIn = new JLabel("Batch Qty. in Hand");
		lblBatchQtyIn.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblBatchQtyIn.setBounds(417, 224, 119, 14);
		contentPanel.add(lblBatchQtyIn);

		taxTF = new FocusTextField();
		taxTF.setHorizontalAlignment(SwingConstants.RIGHT);
		taxTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		taxTF.setColumns(10);
		taxTF.setBounds(664, 190, 68, 20);
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
			private void updateTax() {
				String str = taxTF.getText();
				taxValue = str.isEmpty() ? 0 : Double.parseDouble("0" + str);
				itemValue();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateTax();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateTax();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateTax();
			}
		});

		taxTF.setEnabled(false);
		JLabel lblTax = new JLabel("CGST");
		lblTax.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTax.setBounds(664, 165, 88, 14);
		contentPanel.add(lblTax);

		discountTF = new FocusTextField();
		discountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		discountTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		discountTF.setColumns(10);
		discountTF.setBounds(821, 190, 78, 20);
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
			private void updateDiscount() {
				String str = discountTF.getText();
				discountValue = str.isEmpty() ? 0 : Double.parseDouble("0" + str);
				itemValue();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateDiscount();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateDiscount();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateDiscount();
			}
		});

		JLabel lblDiscount = new JLabel("Discount");
		lblDiscount.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDiscount.setBounds(821, 165, 88, 14);
		contentPanel.add(lblDiscount);
		lblDiscount.setVisible(false);
		//
		itemPriceTF = new JTextField();
		itemPriceTF.setHorizontalAlignment(SwingConstants.RIGHT);
		itemPriceTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		itemPriceTF.setEditable(false);
		itemPriceTF.setColumns(10);
		itemPriceTF.setBounds(92, 247, 106, 20);
		contentPanel.add(itemPriceTF);



		JLabel lblItemPrice = new JLabel("Item Price");
		lblItemPrice.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblItemPrice.setBounds(14, 252, 78, 14);
		contentPanel.add(lblItemPrice);

		totalValueTF = new JTextField();
		totalValueTF.setHorizontalAlignment(SwingConstants.RIGHT);
		totalValueTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		totalValueTF.setEditable(false);
		totalValueTF.setColumns(10);
		totalValueTF.setBounds(288, 247, 98, 20);
		contentPanel.add(totalValueTF);

		JLabel lblTotalValue = new JLabel("Total Value");
		lblTotalValue.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTotalValue.setBounds(208, 253, 78, 14);
		contentPanel.add(lblTotalValue);

		expiryDateTF = new JTextField();
		expiryDateTF.setHorizontalAlignment(SwingConstants.RIGHT);
		expiryDateTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		expiryDateTF.setEditable(false);
		expiryDateTF.setColumns(10);
		expiryDateTF.setBounds(715, 221, 155, 20);
		contentPanel.add(expiryDateTF);

		finalDiscountTF = new JTextField();
		finalDiscountTF.setEditable(false);
		finalDiscountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		finalDiscountTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		finalDiscountTF.setColumns(10);
		finalDiscountTF.setBounds(390, 646, 146, 9);
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

		finalDiscountTF.getDocument().addDocumentListener(new DocumentListener() {
			private void updateFinalDiscount() {
				String str = finalDiscountTF.getText();
				finalDiscountValue = str.isEmpty() ? 0 : Double.parseDouble("0" + str);
				FinalAmount();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateFinalDiscount();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateFinalDiscount();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateFinalDiscount();
			}
		});

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
				}
			}
		});

		finalTaxTF.getDocument().addDocumentListener(new DocumentListener() {
			private void updateFinalTax() {
				String str = finalTaxTF.getText();
				finalTaxValue = str.isEmpty() ? 0 : Double.parseDouble("0" + str);
				FinalAmount();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateFinalTax();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateFinalTax();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateFinalTax();
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

		JLabel lblCashRecieved = new JLabel("Payment Recieved");
		lblCashRecieved.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCashRecieved.setBounds(540, 532, 119, 25);
		contentPanel.add(lblCashRecieved);

		cashRecievedTF = new JTextField();
		cashRecievedTF.setHorizontalAlignment(SwingConstants.RIGHT);
		cashRecievedTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cashRecievedTF.setColumns(10);
		cashRecievedTF.setBounds(664, 532, 146, 25);
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

		cashRecievedTF.getDocument().addDocumentListener(new DocumentListener() {
			private void updateBalance() {
				String str = cashRecievedTF.getText();
				if (!str.isEmpty()) {
					double amount = Double.parseDouble("0" + str);
					double payableAmount = Double.parseDouble("0" + totalPayableAmountTF.getText());
					double balance = Math.round((amount - payableAmount) * 100.0) / 100.0;
					balanceAmountTF.setText(String.valueOf(balance));
				} else {
					balanceAmountTF.setText("");
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateBalance();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateBalance();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateBalance();
			}
		});

		totalAmountTF.getDocument().addDocumentListener(new DocumentListener() {
			private void updatePayableAmount() {
				String str = totalAmountTF.getText();
				if (!str.isEmpty()) {
					double amount = Double.parseDouble("0" + str);
					totalPayableAmountTF.setText("" + Math.round(amount));
				} else {
					totalPayableAmountTF.setText("");
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updatePayableAmount();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updatePayableAmount();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updatePayableAmount();
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
		balanceAmountTF.setBounds(664, 568, 146, 25);
		contentPanel.add(balanceAmountTF);

		JLabel lblSelectInsurance = new JLabel("Select Head :");
		lblSelectInsurance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSelectInsurance.setBounds(615, 93, 126, 25);
		contentPanel.add(lblSelectInsurance);

		insuranceCB = new JComboBox();
		insuranceCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		insuranceCB.setBounds(751, 93, 218, 25);
		contentPanel.add(insuranceCB);

		JLabel lblItemLocation = new JLabel("Item Location");
		lblItemLocation.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblItemLocation.setBounds(404, 253, 92, 14);
		contentPanel.add(lblItemLocation);

		itemLocationTF = new JTextField();
		itemLocationTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		itemLocationTF.setEditable(false);
		itemLocationTF.setColumns(10);
		itemLocationTF.setBounds(503, 247, 136, 20);
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
		surchargeTF.setHorizontalAlignment(SwingConstants.RIGHT);
		surchargeTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		surchargeTF.setColumns(10);
		surchargeTF.setBounds(742, 190, 69, 20);
		contentPanel.add(surchargeTF);
		surchargeTF.addKeyListener(new KeyAdapter() {
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
		surchargeTF.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					Toolkit.getDefaultToolkit().beep();
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
		surchargeTF.setEnabled(false);
		JLabel lblSurChg = new JLabel("SGST");
		lblSurChg.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSurChg.setBounds(742, 165, 69, 14);
		contentPanel.add(lblSurChg);

		JLabel lblInsuranceId = new JLabel("Insurance ID *:");
		lblInsuranceId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInsuranceId.setBounds(615, 129, 126, 25);
		contentPanel.add(lblInsuranceId);

		insuranceIDTB = new JTextField();
		insuranceIDTB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		insuranceIDTB.setColumns(10);
		insuranceIDTB.setBounds(751, 129, 218, 25);
		contentPanel.add(insuranceIDTB);

		btnEditItem = new JButton("");
		btnEditItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});

		btnEditItem.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnEditItem.setBounds(919, 236, 96, 31);
		btnEditItem.setEnabled(false);
		contentPanel.add(btnEditItem);

		JLabel lblDoctorAddress = new JLabel("Doctor Address:");
		lblDoctorAddress.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDoctorAddress.setBounds(615, 47, 126, 25);
		contentPanel.add(lblDoctorAddress);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(751, 45, 218, 37);
		contentPanel.add(scrollPane_1);

		doctorAddressTA = new JTextArea();
		scrollPane_1.setViewportView(doctorAddressTA);

		JLabel lblPatientAddress = new JLabel("Patient Address :");
		lblPatientAddress.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPatientAddress.setBounds(235, 83, 126, 25);
		contentPanel.add(lblPatientAddress);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(371, 76, 218, 42);
		contentPanel.add(scrollPane_2);

		patientAddressTA = new JTextArea();
		scrollPane_2.setViewportView(patientAddressTA);

		JLabel lblTax_2 = new JLabel("Payment Mode");
		lblTax_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTax_2.setBounds(10, 575, 92, 25);
		contentPanel.add(lblTax_2);

		paymentModeCB = new JComboBox();
		paymentModeCB.addItem("Cash");
		paymentModeCB.addItem("Online");
		paymentModeCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		paymentModeCB.setBounds(110, 574, 142, 28);
		contentPanel.add(paymentModeCB);
		getAllPaymentModes();

		JLabel lblMrp = new JLabel("MRP:");
		lblMrp.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMrp.setBounds(645, 250, 54, 14);
		contentPanel.add(lblMrp);

		mrpTF = new JTextField();
		mrpTF.setHorizontalAlignment(SwingConstants.RIGHT);
		mrpTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		mrpTF.setColumns(10);
		mrpTF.setBounds(682, 250, 114, 20);
		mrpTF.setEditable(false);
		contentPanel.add(mrpTF);

		JLabel lblPackSize = new JLabel("Pack");
		lblPackSize.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPackSize.setBounds(803, 253, 42, 14);
		contentPanel.add(lblPackSize);

		packSizeET = new JTextField();
		packSizeET.setHorizontalAlignment(SwingConstants.RIGHT);
		packSizeET.setFont(new Font("Tahoma", Font.BOLD, 11));
		packSizeET.setEditable(false);
		packSizeET.setColumns(10);
		packSizeET.setBounds(849, 248, 60, 20);
		contentPanel.add(packSizeET);

		JLabel lblItemDescription = new JLabel("Item Desc.");
		lblItemDescription.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblItemDescription.setBounds(10, 224, 78, 14);
		contentPanel.add(lblItemDescription);

		JLabel lblTotalPayablet_1 = new JLabel("Karuna Discount :");
		lblTotalPayablet_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblTotalPayablet_1.setBounds(288, 606, 98, 25);
		contentPanel.add(lblTotalPayablet_1);

		KDiscountTF = new JTextField();
		KDiscountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		KDiscountTF.setFont(new Font("Dialog", Font.PLAIN, 14));
		KDiscountTF.setEditable(false);
		KDiscountTF.setColumns(10);
		KDiscountTF.setBounds(390, 605, 146, 25);
		contentPanel.add(KDiscountTF);
		getAllDoctors();
		getAllinsurance();
		Init();
	}

	public void getItemName(String index) {
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.searchItemWithIdOrNmae(index);
		itemName.removeAllElements();
		itemID.clear();
		try {
			while (resultSet.next()) {
				itemID.add(resultSet.getObject(1).toString());
				itemName.addElement(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		itemNameCB.setModel(itemName);
		if (itemID.size() > 0) {
			itemNameCB.setSelectedIndex(0);
		}
	}

	public void getItemBatchName(String index) {
		BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
		ResultSet resultSet = batchTrackingDBConnection.itemBatch(index);
		itemBatchName.removeAllElements();
		itemBatchName1.removeAllElements();
		batchID.clear();
		int i = 0;
		try {
			while (resultSet.next()) {
				batchID.add(resultSet.getObject(1).toString());
				itemBatchName1.addElement(resultSet.getObject(2).toString()	+ "(Batch" + (i + 1) + ")");
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
			batchNameCB.setSelectedIndex(0);
		}
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
			} else
				expiryDateTF.setForeground(Color.BLACK);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		price=Math.round((price+newUnitTax)*100.00)/100.00;
		itemIDV.add(itemIDSTR);
		itemNameV.add(itemNameSTR);
		itemDescV.add(itemDescSTR);
		itemHSN_CODEV.add(itemHSNSTR);
		issuedQtyV.add(qtyIssued + "");
		expiryDateV.add(expiryDateSTR);
		itemBatchIDV.add(batchIDSTR);
		itemBatchV.add(itemBatchNameSTR);
		unitPriceV.add((price) +"");
		System.out.println("price "+price+" unit tax "+newUnitTax);
		System.out.println(mrp+" addddd");
		mrpPriceV.add(mrp+"");
		measUnitV.add(packSize+"");
		System.out.println(price+"gfhfhfhfh "+newUnitTax);
		taxPercentageV.add(taxValue + "");
		DiscountV.add(discountValue + "");
		System.out.println(taxAmountValue+"  dfdfsf  "+surchargeAmountValue);
		totalValueV.add(itemValue+"");
		KarunaRelief.add(karunaRelief +"");
		newUnitPrice.add(newunitprice +"");
		karunaAmount.add(discountamount +"");
		itemRiskValueV.add(item_risk_type+"");
		taxAmountV.add(taxAmountValue+"");
		surchargeV.add(surchargeValue + "");
		surchargeAmountValueV.add(surchargeAmountValue + "");
		loadDataToTable();
		resetFields();
		searchItemTF.setText("");
		searchItemTF.requestFocusInWindow();
	}

	private void resetFields() {
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
		itemPriceTF.setText("");
	}

	public void Init() {
		cuttingCharges = new CuttingCharges();
		cuttingCharges.getCuttingCharges("OPD"); // Retrieve cutting charge rates
	}

	public void getItemBatchDetail(String index) {
		MSOPDPriceFormula processor=new MSOPDPriceFormula();
		processor.processItemBatch(index,formulaActive,itemIDSTR);
		price = processor.getPrice();
		mrp = processor.getMrp();
		purchase = processor.getPurchase();
		quantity =(int) processor.getQuantity();
		taxValue = processor.getTaxValue();
		surchargeValue = processor.getSurchargeValue();
		packSize = processor.getPackSize();
	}

	public void itemValue() {
		MSOPDPriceFormula processor=new MSOPDPriceFormula();
		System.out.println("itemUP :"+this.price);
		double price = this.price;
		if (iskaruna) {
			KarunaDiscount karunaDiscountPer = new KarunaDiscount();
			price = karunaDiscountPer.getKarunaRelief(this.price, this.karunaDiscountPer);
			newunitprice=price;
			//			System.out.println("enw  price:"+newunitprice );
			karunaRelief = karunaDiscountPer.getKarunaDiscountAmt(this.price, this.karunaDiscountPer);
			System.out.println(karunaRelief + " After karuna discount : " + price);
			discountamount=round(processor.addGstTemp((karunaRelief*qtyIssued), taxValue, surchargeValue));
			//			System.out.println("karuna dis:"+discountamount );
		}
		cuttingCharges.calculateCuttingCharge(qtyIssued, packSize);
		double  cuttingpercentage = cuttingCharges.getCuttingPercentage();
		price = cuttingCharges.addCuttingPercentage(price,cuttingpercentage);
		System.out.println(" After cutting charges : " + price);

		if(paymentModeCB.getSelectedIndex()==1) {
			OnlineDiscount onlineDiscount=new OnlineDiscount();
			price=onlineDiscount.getOnlineDiscount(price);
			System.out.println(" After online discount : " + price);
		}		

		price=processor.addGst(price*qtyIssued, taxValue, surchargeValue);
		taxAmountValue = round(processor.getCgstValue());
		surchargeAmountValue = round(processor.getCgstValue());
		System.out.println(" After gst : " + price);		
		itemValue=round(price);
		double mrpRate =round(mrp * qtyIssued);
		if (itemValue >= mrpRate) {
			itemValue = mrpRate; 
		}
		totalValueTF.setText(String.valueOf(itemValue));
		System.out.println(" Final Item Value: " + itemValue);
	}

	public void getrisktype(String index) {
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetailNew(index);
		try {
			while (resultSet.next()) {
				item_risk_type = resultSet.getObject(1).toString();
			}
		} catch (SQLException e) {
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

	public void getItemDetail(String index) {
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail2(index);
		try {
			while (resultSet.next()) {
				itemDescSTR = resultSet.getObject(3).toString();
				quantity1 = Integer.parseInt(resultSet.getObject(8).toString());
				itemLocationSTR = resultSet.getObject(12).toString();
				itemHSNSTR = resultSet.getObject(14).toString();
				formulaActive = resultSet.getObject(17).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();

	}

	public void getItemBatch(String index) {

		batchQty = 0;
		BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
		ResultSet resultSet = batchTrackingDBConnection.itemBatchDetail(index);
		try {
			while (resultSet.next()) {
				batchQty = Integer.parseInt(resultSet.getObject(1).toString());
				expiryDateSTR = resultSet.getObject(2).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		batchTrackingDBConnection.closeConnection();

	}

	private void loadDataToTable() {
		int size = itemIDV.size();
		finalTotalValue = 0;
		totalTaxAmount = 0;
		totalSurchargeAmount = 0;
		finalkarunaDStotalValue=0;
		ObjectArray_ListOfexamsSpecs = new Object[size][12];

		for (int i = 0; i < itemIDV.size(); i++) {
			ObjectArray_ListOfexamsSpecs[i][0] = itemIDV.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = itemNameV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = itemBatchV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = unitPriceV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = issuedQtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = taxPercentageV.get(i) + "%("+ taxAmountV.get(i) + ")";
			ObjectArray_ListOfexamsSpecs[i][6] = surchargeV.get(i) + "%("+ surchargeAmountValueV.get(i) + ")";
			ObjectArray_ListOfexamsSpecs[i][7] = DiscountV.get(i);
			ObjectArray_ListOfexamsSpecs[i][8] = totalValueV.get(i);
			ObjectArray_ListOfexamsSpecs[i][9] = expiryDateV.get(i);
			ObjectArray_ListOfexamsSpecs[i][10] = KarunaRelief.get(i);
			ObjectArray_ListOfexamsSpecs[i][11] = newUnitPrice.get(i);
			finalTotalValue = finalTotalValue+ Double.parseDouble(totalValueV.get(i));
			totalTaxAmount = totalTaxAmount	+ Double.parseDouble(taxAmountV.get(i));
			totalSurchargeAmount = totalSurchargeAmount+ Double.parseDouble(surchargeAmountValueV.get(i));
			finalkarunaDStotalValue+= Double.parseDouble(karunaAmount.get(i));
		}

		DefaultTableModel model = new DefaultTableModel(
				ObjectArray_ListOfexamsSpecs, new String[] { "Item ID",
						"Item Name", "Item Batch", "Unit Price", "Quantity",
						"CGST", "SGST", "Discount", "Total Value", "expiry","karuna Relief","newPrice"}) {
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

	public void getDoctorDetail(String name) {

		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveDataWithIndex(name);
		try {
			while (resultSet.next()) {
				doctorAddressTA.setText(resultSet.getObject(4).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}

	public void opd(String opdID) {
		patientNameTF.setText("");
		MobileNoTF.setText("");
		doctorNameCB.setSelectedIndex(0);
		insuranceCB.setSelectedIndex(0);
		karunaDiscountPer=0;
		try {
			OPDDBConnection db = new OPDDBConnection();
			ResultSet rs = db.retrieveAllDataWithOpdId2(opdID);
			while (rs.next()) {
				setPatientDetail(rs.getObject(1).toString());
				patientNameTF.setText("" + rs.getObject(2).toString());
				System.out.println("Doctor name : "	+ rs.getObject(3).toString());
				doctorNameCB.setSelectedIndex(doctors.getIndexOf(rs.getObject(3).toString()));
				karunaDiscountPer=rs.getDouble(4);
				iskaruna=(karunaDiscountPer>0)?true:false;
				if(iskaruna)
					KDiscountTF.setText(karunaDiscountPer+" %");
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
				patientAddressTA.setText(resultSet.getObject(4).toString()
						+ "," + resultSet.getObject(5).toString());
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

	public double round(double amt) {
		return Math.round(amt * 100.00) / 100.00;
	}

	public void setAlreadyPayData(String reqUrn,double finalAmt,double WithoutChargeAmt,String PayMode,String pid,int payIndex) {
		alreadyPaid=true;
		req_urn=reqUrn;
		finalCharges=finalAmt;
		totalExamCharge=WithoutChargeAmt;
		payMode=PayMode;
	}
	
	public static void openOrShowDocScanning(JFrame owner) {
		DocScanning DocScanning=new	DocScanning(owner);
		DocScanning.setModal(true);
		DocScanning.setVisible(true);
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
	public void getAllPaymentModes() {
		//		paymentModes.addElement("Select");
		//		paymentModes.addElement("Cash");
		//		PaymentsDBConnection dbConnection = new PaymentsDBConnection();
		//		ResultSet resultSet = dbConnection.retrieveAllPaymentMode();
		//		try {
		//			while (resultSet.next()) {
		//				paymentModes.addElement(resultSet.getObject(1).toString());
		//			}
		//		} catch (SQLException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		dbConnection.closeConnection();
		//		if(paymentModes.getSize()>0) {
		//			paymentModeCB.setModel(paymentModes);
		//			paymentModeCB.setSelectedIndex(0);
		//		}
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
}