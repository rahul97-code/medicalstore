package hms.store.gui;

import hms.gl.database.GLAccountDBConnection;
import hms.main.DateFormatChange;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.ChallanDBConnection;
import hms.store.database.InvoiceDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.database.SuppliersDBConnection;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

public class NewChallan extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField supplierTF;
	private JComboBox supplierCB;
	private JTextField mobileTF;
	private JTextField addressTF;
	private JTextField invoiceNoTF;
	public JTextField searchItemTF;
	private JTextField itemDescTF;
	private JTextField qtyTF;
	private JTextField unitPriceTF;
	private JTextField taxTF;
	private JTextField discountTF;
	private JTextField totalValueTF;
	private JTable table;
	final DefaultComboBoxModel supplierName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel measUnit = new DefaultComboBoxModel();
	Vector<String> supplierIDV = new Vector<String>();
	Vector<String> itemID = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> measUnitV = new Vector<String>();
	Vector<String> qtyV = new Vector<String>();
	Vector<String> freeqtyV = new Vector<String>();
	Vector<String> paidqtyV = new Vector<String>();
	Vector<String> unitPriceV = new Vector<String>();
	Vector<String> taxV = new Vector<String>();
	Vector<String> taxValueV = new Vector<String>();
	Vector<String> surchargeV = new Vector<String>();
	Vector<String> surchargeValueV = new Vector<String>();
	Vector<String> discountV = new Vector<String>();
	Vector<String> totalValueV = new Vector<String>();
	Vector<String> expiryDateV = new Vector<String>();
	Vector<String> batchNumberV = new Vector<String>();
	String supplierDisplaySTR, mobileSTR, addressSTR, supplierID,
			supplierNameSTR;
	String itemIDSTR, itemNameSTR, itemDescSTR, taxTypeSTR, taxValueSTR,itemSurchargeSTR,
			expiryDateSTR = "", invoiceDateSTR = "", dueDateSTR = "0000-00-00";
	double unitPrice = 0, oldunitPrice = 0, taxValue = 0,itemSurcharge = 0, discountValue = 0,
			itemValue, finalTaxValue = 0, finalDiscountValue = 0,
			finalTotalValue = 0,surchargeAmountValue=0,taxAmountValue=0;
	int quantity = 0,freeQuantity=0,paidQuantity=0;
	String mrp = "";
	Object[][] ObjectArray_ListOfexamsSpecs;
	private JComboBox itemNameCB;
	private JDateChooser expiryDateC;
	private JComboBox measUnitCD;
	private JTextField finalTotalTF;
	ButtonGroup paymentOption = new ButtonGroup();
	private JTextField batchNumberTF;
	private JTextField freeQtyTF;
	private JTextField itemSurchargeTF;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewChallan dialog = new NewChallan();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Create the dialog.
	 */
	public NewChallan() {
		setResizable(false);
		setBounds(100, 70, 1031, 640);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Search Supplier :");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(20, 11, 126, 25);
		contentPanel.add(lblNewLabel_1);

		supplierTF = new JTextField();
		supplierTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		supplierTF.setBounds(156, 11, 218, 25);
		contentPanel.add(supplierTF);
		supplierTF.setColumns(10);
		supplierTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = supplierTF.getText() + "";
				if (!str.equals("")) {
					getSupplierName(str);
				} else {

					addressTF.setText("");
					mobileTF.setText("");
					supplierName.removeAllElements();
					supplierCB.setModel(supplierName);

				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = supplierTF.getText() + "";
				if (!str.equals("")) {
					getSupplierName(str);
				} else {
					addressTF.setText("");
					mobileTF.setText("");
					supplierName.removeAllElements();
					supplierCB.setModel(supplierName);
				}
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = supplierTF.getText() + "";
				if (!str.equals("")) {
					getSupplierName(str);
				} else {
					addressTF.setText("");
					mobileTF.setText("");
					supplierName.removeAllElements();
					supplierCB.setModel(supplierName);
				}
			}
		});

		supplierCB = new JComboBox();
		supplierCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					supplierDisplaySTR = supplierCB.getSelectedItem()
							.toString();
				} catch (Exception e) {
					// TODO: handle exception

				}
				addressTF.setText("");
				mobileTF.setText("");

				getSupplierDetail(supplierDisplaySTR);
				if (supplierName.getSize() > 0) {
					addressTF.setText("" + addressSTR);
					mobileTF.setText("" + mobileSTR);
				}
			}
		});
		supplierCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		supplierCB.setBounds(156, 44, 218, 25);
		contentPanel.add(supplierCB);

		JLabel lblCredit = new JLabel("Select Supplier");
		lblCredit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCredit.setBounds(20, 44, 126, 25);
		contentPanel.add(lblCredit);

		mobileTF = new JTextField();
		mobileTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mobileTF.setColumns(10);
		mobileTF.setBounds(156, 80, 218, 25);
		contentPanel.add(mobileTF);

		JLabel lblDebit = new JLabel("Mobile :");
		lblDebit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDebit.setBounds(20, 80, 126, 25);
		contentPanel.add(lblDebit);

		addressTF = new JTextField();
		addressTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		addressTF.setColumns(10);
		addressTF.setBounds(156, 116, 218, 25);
		contentPanel.add(addressTF);

		JLabel lblBalance = new JLabel("Address :");
		lblBalance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBalance.setBounds(20, 116, 126, 25);
		contentPanel.add(lblBalance);

		invoiceNoTF = new JTextField();
		invoiceNoTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		invoiceNoTF.setColumns(10);
		invoiceNoTF.setBounds(758, 11, 218, 25);
		contentPanel.add(invoiceNoTF);

		JLabel lblInvoiceNo = new JLabel("Challan No. :");
		lblInvoiceNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInvoiceNo.setBounds(622, 11, 126, 25);
		contentPanel.add(lblInvoiceNo);

		JLabel lblDate = new JLabel("Date :");
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDate.setBounds(622, 47, 126, 25);
		contentPanel.add(lblDate);

		searchItemTF = new JTextField();
		searchItemTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		searchItemTF.setBounds(10, 190, 119, 20);
		contentPanel.add(searchItemTF);
		searchItemTF.setColumns(10);
		searchItemTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {

					itemDescTF.setText("");
					unitPriceTF.setText("");
					taxTF.setText("");
					discountTF.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();
					measUnitCD.setModel(measUnit);
					qtyTF.setText("");
					batchNumberTF.setText("");
					
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {
					itemDescTF.setText("");
					unitPriceTF.setText("");
					taxTF.setText("");
					discountTF.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();
					measUnitCD.setModel(measUnit);
					qtyTF.setText("");
					batchNumberTF.setText("");
					
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {
					itemDescTF.setText("");
					unitPriceTF.setText("");
					taxTF.setText("");
					discountTF.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();
					measUnitCD.setModel(measUnit);
					qtyTF.setText("");
					batchNumberTF.setText("");
				}
			}
		});

		itemDescTF = new JTextField();
		itemDescTF.setEditable(false);
		itemDescTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemDescTF.setBounds(10, 221, 163, 20);
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
				System.out.println(itemNameCB.getSelectedIndex() + "    "
						+ itemID.size());
				if (itemNameCB.getSelectedIndex() > -1) {
					itemIDSTR = itemID.get(itemNameCB.getSelectedIndex());
				}

				itemDescTF.setText("");
				unitPriceTF.setText("");
				taxTF.setText("");
				discountTF.setText("");
				itemSurchargeTF.setText("");

				getItemDetail(itemIDSTR);
				if (itemName.getSize() > 0) {
					if (!taxTypeSTR.equals("CompanyTax")) {
						taxTF.setText(taxValueSTR);
					}
					itemDescTF.setText("" + itemDescSTR);
					unitPriceTF.setText("" + unitPrice);
					itemSurchargeTF.setText(""+itemSurchargeSTR);
				}
			}
		});
		itemNameCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemNameCB.setBounds(139, 190, 210, 20);
		contentPanel.add(itemNameCB);

		measUnitCD = new JComboBox();
		measUnitCD.setFont(new Font("Tahoma", Font.BOLD, 11));
		measUnitCD.setBounds(359, 190, 80, 20);
		contentPanel.add(measUnitCD);

		qtyTF = new JTextField();
		qtyTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		qtyTF.setBounds(449, 190, 61, 20);
		contentPanel.add(qtyTF);
		qtyTF.setColumns(10);
		qtyTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();

					// ||vChar== '.'
				}
			}
		});
		qtyTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = qtyTF.getText() + "";
				if (!str.equals("")) {

					quantity = Integer.parseInt(str);
					itemValue();

				} else {

					quantity = 0;
					itemValue();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = qtyTF.getText() + "";
				if (!str.equals("")) {

					quantity = Integer.parseInt(str);
					itemValue();

				} else {

					quantity = 0;
					itemValue();
				}

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = qtyTF.getText() + "";
				if (!str.equals("")) {
					quantity = Integer.parseInt(str);
					itemValue();

				} else {

					quantity = 0;
					itemValue();
				}

			}
		});

		unitPriceTF = new JTextField();
		unitPriceTF.setHorizontalAlignment(SwingConstants.RIGHT);
		unitPriceTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		unitPriceTF.setBounds(601, 190, 65, 20);
		contentPanel.add(unitPriceTF);
		unitPriceTF.setColumns(10);
		unitPriceTF.addKeyListener(new KeyAdapter() {
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
		unitPriceTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = unitPriceTF.getText() + "";
				if (!str.equals("")) {

					unitPrice = Double.parseDouble(str);
					itemValue();

				} else {

					unitPrice = 0;
					itemValue();
				}

			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = unitPriceTF.getText() + "";
				if (!str.equals("")) {

					unitPrice = Double.parseDouble(str);
					itemValue();

				} else {

					unitPrice = 0;
					itemValue();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = unitPriceTF.getText() + "";
				if (!str.equals("")) {

					unitPrice = Double.parseDouble(str);
					itemValue();

				} else {

					unitPrice = 0;
					itemValue();
				}
			}
		});

		taxTF = new JTextField();
		taxTF.setHorizontalAlignment(SwingConstants.RIGHT);
		taxTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		taxTF.setBounds(676, 190, 61, 20);
		contentPanel.add(taxTF);
		taxTF.setColumns(10);
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

		discountTF = new JTextField();
		discountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		discountTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		discountTF.setBounds(819, 190, 61, 20);
		contentPanel.add(discountTF);
		discountTF.setColumns(10);
		discountTF.addKeyListener(new KeyAdapter() {
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

		totalValueTF = new JTextField();
		totalValueTF.setEditable(false);
		totalValueTF.setHorizontalAlignment(SwingConstants.RIGHT);
		totalValueTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		totalValueTF.setBounds(525, 221, 113, 20);
		contentPanel.add(totalValueTF);
		totalValueTF.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Total Value :");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_2.setBounds(434, 224, 86, 14);
		contentPanel.add(lblNewLabel_2);

		JLabel lblSearch = new JLabel("Search Item");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSearch.setBounds(10, 165, 119, 14);
		contentPanel.add(lblSearch);

		JLabel lblDescription = new JLabel("Select Item :");
		lblDescription.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDescription.setBounds(139, 165, 153, 14);
		contentPanel.add(lblDescription);

		JLabel lblMeasUnits = new JLabel("Meas Units");
		lblMeasUnits.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMeasUnits.setBounds(359, 165, 80, 14);
		contentPanel.add(lblMeasUnits);

		JLabel lblQty = new JLabel("Qty.");
		lblQty.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblQty.setBounds(452, 165, 68, 14);
		contentPanel.add(lblQty);

		JLabel lblUnitPrice = new JLabel("Unit Price ");
		lblUnitPrice.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblUnitPrice.setBounds(601, 165, 65, 14);
		contentPanel.add(lblUnitPrice);

		JLabel lblTax = new JLabel("CGST");
		lblTax.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTax.setBounds(678, 165, 67, 14);
		contentPanel.add(lblTax);

		JLabel lblDiscount = new JLabel("Discount");
		lblDiscount.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDiscount.setBounds(820, 165, 61, 14);
		contentPanel.add(lblDiscount);

		JButton btnNewButton = new JButton("Add Line");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (itemDescTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please select item",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (qtyTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter quantity", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (unitPriceTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please select item or enter unit price",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				} else if (totalValueTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter final value", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} 
				else if (batchNumberTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter Batch Number", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else if (expiryDateSTR.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter expiry date", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} 
//				else if (itemIDV.indexOf(itemIDSTR) != -1) {
//					JOptionPane.showMessageDialog(null,
//							"this item already entered", "Input Error",
//							JOptionPane.ERROR_MESSAGE);
//					return;
//				}
				if (oldunitPrice != unitPrice) {
					updatePrice();
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(
							NewChallan.this,
							"Do you want to change the mrp price." + "\n"
									+ " Current MRP is " + mrp + " Rupees",
							"Cancel", dialogButton);
					if (dialogResult == 0) {
						EditItem editItem = new EditItem(itemIDSTR);
						editItem.setModal(true);
						editItem.setVisible(true);
					}
				}
				itemIDV.add(itemIDSTR);
				itemNameV.add(itemNameSTR);
				itemDescV.add(itemDescSTR);
				measUnitV.add(measUnitCD.getSelectedItem().toString());
				batchNumberV.add(batchNumberTF.getText().toString().toUpperCase());
				qtyV.add("" + quantity);
				freeqtyV.add("" + freeQuantity);
				paidqtyV.add("" + paidQuantity);
				unitPriceV.add(unitPrice + "");
				taxV.add(taxValue + "");
				surchargeV.add(itemSurcharge+"");
				taxValueV.add(taxAmountValue+"");
				surchargeValueV.add(surchargeAmountValue+"");
				discountV.add(discountValue + "");
				totalValueV.add(totalValueTF.getText() + "");
				expiryDateV.add(expiryDateSTR);
				searchItemTF.setText("");
				expiryDateC.setCalendar(null);
				expiryDateSTR="";
				loadDataToTable();

			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(891, 156, 114, 25);
		contentPanel.add(btnNewButton);

		JLabel lblExpireDate = new JLabel("Expire Date :");
		lblExpireDate.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExpireDate.setBounds(645, 224, 86, 14);
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
				itemNameV.remove(cur_selectedRow);
				itemDescV.remove(cur_selectedRow);
				measUnitV.remove(cur_selectedRow);
				qtyV.remove(cur_selectedRow);
				freeqtyV.remove(cur_selectedRow);
				paidqtyV.remove(cur_selectedRow);
				unitPriceV.remove(cur_selectedRow);
				taxV.remove(cur_selectedRow);
				surchargeV.remove(cur_selectedRow);
				taxValueV.remove(cur_selectedRow);
				surchargeValueV.remove(cur_selectedRow);
				discountV.remove(cur_selectedRow);
				totalValueV.remove(cur_selectedRow);
				expiryDateV.remove(cur_selectedRow);
				batchNumberV.remove(cur_selectedRow);
				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRemove.setBounds(890, 190, 114, 25);
		contentPanel.add(btnRemove);
		btnRemove.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 252, 992, 270);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		table.setModel(new DefaultTableModel(new Object[][] {
		}, new String[] { "Item ID", "Item Name", "Item Batch", "MU", "Qty.","Batch Qty.",
				"Unit Price", "Discount", "Tax", "Value", "Expiry" }));
		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(3).setMinWidth(75);
		table.getColumnModel().getColumn(4).setMinWidth(75);
		table.getColumnModel().getColumn(5).setMinWidth(75);
		table.getColumnModel().getColumn(6).setPreferredWidth(65);
		table.getColumnModel().getColumn(6).setMinWidth(65);
		table.getColumnModel().getColumn(7).setPreferredWidth(65);
		table.getColumnModel().getColumn(7).setMinWidth(65);
		table.getColumnModel().getColumn(8).setPreferredWidth(100);
		table.getColumnModel().getColumn(8).setMinWidth(75);
		table.getColumnModel().getColumn(9).setPreferredWidth(100);
		table.getColumnModel().getColumn(9).setMinWidth(90);
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
		scrollPane.setViewportView(table);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 149, 966, 2);
		contentPanel.add(separator);

		JButton btnNewButton_1 = new JButton("Save Challan");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (supplierCB.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(null,
							"Please select supplier", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (invoiceNoTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter challan no", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}


				if (invoiceDateSTR.equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter date",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (itemIDV.size() == 0) {
					JOptionPane.showMessageDialog(null,
							"Please add atlest one item", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				int index = 0;
				String[] data = new String[30];
				ChallanDBConnection challanDBConnection = new ChallanDBConnection();
				
				ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
				data[0] = invoiceNoTF.getText().toString() + "";
				data[1] = supplierID;
				data[2] = supplierDisplaySTR;
				data[18] = "" + DateFormatChange.StringToMysqlDate(new Date());
				data[19] = "" + timeFormat.format(cal1.getTime());
				data[20] = ""+StoreMain.userName; // /user
				for (int i = 0; i < itemIDV.size(); i++) {
					data[3] = itemIDV.get(i);
					data[4] = itemNameV.get(i);
					data[5] = itemDescV.get(i);
					data[6] = measUnitV.get(i);
					data[7] = qtyV.get(i);
					data[8] = freeqtyV.get(i);
					data[9] = paidqtyV.get(i);
					data[10] = unitPriceV.get(i);
					data[11] = discountV.get(i);
					data[12] = taxV.get(i);
					data[13] = surchargeV.get(i);
					data[14] = taxValueV.get(i);
					data[15] = surchargeValueV.get(i);
					data[16] = totalValueV.get(i);
					data[17] = expiryDateV.get(i);
					data[21] = batchNumberV.get(i);

					System.out.println(""+surchargeV.get(i));
					try {
						challanDBConnection.inserChallanItem(data);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						itemsDBConnection.addStock(itemIDV.get(i), qtyV.get(i),
								expiryDateV.get(i),unitPriceV.get(i));
						itemsDBConnection.updateOrderStaus(itemIDV.get(i), "NO", qtyV.get(i));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				challanDBConnection.closeConnection();
				itemsDBConnection.closeConnection();

				BatchTrackingDBConnection batchTrackingDBConnection=new BatchTrackingDBConnection();
				for (int i = 0; i < itemIDV.size(); i++) {
					data[0] = itemIDV.get(i);
					data[1] = itemNameV.get(i);
					data[2] = itemDescV.get(i);
					data[3] = batchNumberV.get(i);
					data[4] = qtyV.get(i);
					data[5] = qtyV.get(i);
					data[6] = expiryDateV.get(i);
					data[7] =  "" + DateFormatChange.StringToMysqlDate(new Date());
					data[8] = "" + timeFormat.format(cal1.getTime());
					data[9] ="" + DateFormatChange.StringToMysqlDate(new Date());
					data[10]="N/A";
					data[11]="N/A";
					data[12]="N/A";
					data[13]= "N/A";
					data[14]="N/A";
					data[15]="N/A";
					data[16]="N/A";
					try {
						batchTrackingDBConnection.inserData(data);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
				JOptionPane.showMessageDialog(null,
						"Challan saved successfully", "Challan Save",
						JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_1.setBounds(33, 552, 153, 39);
		contentPanel.add(btnNewButton_1);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(196, 552, 153, 39);
		contentPanel.add(btnCancel);

		expiryDateC = new JDateChooser();
		expiryDateC.getCalendarButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		expiryDateC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())&&arg0
								.getNewValue()!=null) {
							expiryDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());

						}
					}
				});
		expiryDateC.setMinSelectableDate(new Date());
		expiryDateC.setDateFormatString("yyyy-MM-dd");
		expiryDateC.setBounds(728, 221, 152, 20);
		contentPanel.add(expiryDateC);

		JLabel lblTotal = new JLabel("Total :");
		lblTotal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotal.setBounds(758, 559, 80, 14);
		contentPanel.add(lblTotal);

		finalTotalTF = new JTextField();
		finalTotalTF.setHorizontalAlignment(SwingConstants.RIGHT);
		finalTotalTF.setEditable(false);
		finalTotalTF.setFont(new Font("Tahoma", Font.BOLD, 14));
		finalTotalTF.setColumns(10);
		finalTotalTF.setBounds(838, 554, 162, 25);
		contentPanel.add(finalTotalTF);

		JDateChooser invoiceDate = new JDateChooser();
		invoiceDate.setBounds(758, 47, 218, 25);
		contentPanel.add(invoiceDate);
		invoiceDate.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							invoiceDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());

						}
					}
				});
		invoiceDate.setDate(new Date());
		
		batchNumberTF = new JTextField();
		batchNumberTF.setHorizontalAlignment(SwingConstants.RIGHT);
		batchNumberTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		batchNumberTF.setColumns(10);
		batchNumberTF.setBounds(260, 221, 164, 20);
		contentPanel.add(batchNumberTF);
		
		JLabel lblBatchNo = new JLabel("Batch No. :");
		lblBatchNo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblBatchNo.setBounds(183, 224, 80, 14);
		contentPanel.add(lblBatchNo);
		
		freeQtyTF = new JTextField();
		freeQtyTF.setHorizontalAlignment(SwingConstants.RIGHT);
		freeQtyTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		freeQtyTF.setColumns(10);
		freeQtyTF.setBounds(520, 190, 71, 20);
		contentPanel.add(freeQtyTF);
		freeQtyTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});
		freeQtyTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = freeQtyTF.getText() + "";
				if (!str.equals("")) {

					freeQuantity = Integer.parseInt(str);
					itemValue();

				} else {

					freeQuantity = 0;
					itemValue();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = freeQtyTF.getText() + "";
				if (!str.equals("")) {

					freeQuantity = Integer.parseInt(str);
					itemValue();

				} else {

					freeQuantity = 0;
					itemValue();
				}

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = freeQtyTF.getText() + "";
				if (!str.equals("")) {
					freeQuantity = Integer.parseInt(str);
					itemValue();

				} else {

					freeQuantity = 0;
					itemValue();
				}

			}
		});
		
		JLabel lblFreeQty = new JLabel("Free Qty.");
		lblFreeQty.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFreeQty.setBounds(523, 165, 68, 14);
		contentPanel.add(lblFreeQty);
		
		itemSurchargeTF = new JTextField();
		itemSurchargeTF.setHorizontalAlignment(SwingConstants.RIGHT);
		itemSurchargeTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemSurchargeTF.setColumns(10);
		itemSurchargeTF.setBounds(747, 190, 61, 20);
		contentPanel.add(itemSurchargeTF);
		itemSurchargeTF.addKeyListener(new KeyAdapter() {
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
		itemSurchargeTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = itemSurchargeTF.getText() + "";
				if (!str.equals("")) {

					itemSurcharge = Double.parseDouble("0" + str);
					itemValue();

				} else {

					itemSurcharge = 0;
					itemValue();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = itemSurchargeTF.getText() + "";
				if (!str.equals("")) {

					itemSurcharge = Double.parseDouble("0" + str);
					itemValue();

				} else {

					itemSurcharge = 0;
					itemValue();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = itemSurchargeTF.getText() + "";
				if (!str.equals("")) {

					itemSurcharge = Double.parseDouble("0" + str);
					itemValue();

				} else {

					itemSurcharge = 0;
					itemValue();
				}
			}
		});

		
		JLabel lblSurChg = new JLabel("SGST");
		lblSurChg.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSurChg.setBounds(749, 165, 67, 14);
		contentPanel.add(lblSurChg);
		
		JButton btnNewItem = new JButton("");
		btnNewItem.setIcon(new ImageIcon(NewChallan.class.getResource("/icons/plus_button.png")));
		btnNewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				NewItem newItem=new NewItem();
				newItem.ChallanInstance(NewChallan.this);
				newItem.setModal(true);
				newItem.setVisible(true);
			}
		});
		btnNewItem.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewItem.setBounds(891, 220, 51, 25);
		contentPanel.add(btnNewItem);
		
		JButton button = new JButton("");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(!itemDescSTR.equals(""))
				{
					EditItem editItem=new EditItem(itemIDSTR);
					editItem.ChallanInstance(NewChallan.this);
					editItem.setModal(true);
					editItem.setVisible(true);
				}
				
			}
		});
		button.setIcon(new ImageIcon(NewChallan.class.getResource("/icons/edit_button.png")));
		button.setFont(new Font("Tahoma", Font.BOLD, 13));
		button.setBounds(952, 220, 51, 25);
		contentPanel.add(button);
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
		supplierCB.setModel(supplierName);
		if (i > 0) {
			supplierCB.setSelectedIndex(0);
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
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		itemNameCB.setModel(itemName);
		if (i > 0) {
			itemNameCB.setSelectedIndex(0);
		}
	}
	public void getItemDetail(String index) {

		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail(index);
		measUnit.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {

				itemDescSTR = resultSet.getObject(3).toString();
				measUnit.addElement(resultSet.getObject(4).toString());
				taxTypeSTR = resultSet.getObject(5).toString();
				taxValueSTR = resultSet.getObject(6).toString();
				System.out
						.println(itemDescSTR + "  "
								+ resultSet.getObject(7).toString() + " "
								+ taxValueSTR);
				unitPrice = Double.parseDouble("0"
						+ resultSet.getObject(7).toString());
				oldunitPrice = unitPrice;
				mrp = resultSet.getObject(11).toString();
				itemSurchargeSTR= resultSet.getObject(12).toString();
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		measUnitCD.setModel(measUnit);
		if (i > 0) {
			measUnitCD.setSelectedIndex(0);
		}
	}
	public void itemValue() {
		paidQuantity=quantity-freeQuantity;
		
		itemValue = paidQuantity * unitPrice;
		itemValue = itemValue - discountValue;
		double k = itemValue * (taxValue / 100.0f);
		double surcharge = itemValue * (itemSurcharge / 100.0f);
		surcharge=Math.round(surcharge * 100.00) / 100.00;
		k=Math.round(k * 100.00) / 100.00;
		taxAmountValue=k;
		surchargeAmountValue=surcharge;
		itemValue = itemValue + k;
		itemValue = itemValue + surcharge;
		itemValue = Math.round(itemValue * 100.00) / 100.00;
		totalValueTF.setText("" + itemValue);

	}

	public void finalTotal() {
		double total = 0;
		total = finalTotalValue - finalDiscountValue;

		double k = total * (finalTaxValue / 100.0f);

		total = total + k;

		total = Math.round(total * 100.000) / 100.000;

		
		if (total - Math.floor(total) > 0.5) {
			finalTotalTF.setText("" + Math.ceil(total));
			
		} else {
			finalTotalTF.setText("" + Math.floor(total));
		
		}

	}

	private void loadDataToTable() {
		// get size of the hashmap
		int size = itemIDV.size();

		double total = 0;

		double taxable=0,tax=0,surcharge=0;
		ObjectArray_ListOfexamsSpecs = new Object[size][12];

		for (int i = 0; i < itemIDV.size(); i++) {

			ObjectArray_ListOfexamsSpecs[i][0] = itemIDV.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = itemNameV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = batchNumberV.get(i);
		//	ObjectArray_ListOfexamsSpecs[i][3] = measUnitV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = qtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = paidqtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = unitPriceV.get(i);
			ObjectArray_ListOfexamsSpecs[i][6] = taxV.get(i)+"("+taxValueV.get(i)+")";
			ObjectArray_ListOfexamsSpecs[i][7] = surchargeV.get(i)+"("+surchargeValueV.get(i)+")";
			ObjectArray_ListOfexamsSpecs[i][8] = discountV.get(i);
			double amountWT= Double.parseDouble(paidqtyV.get(i))* Double.parseDouble(unitPriceV.get(i));
			amountWT = Math.round(amountWT * 100.00) / 100.00;
			ObjectArray_ListOfexamsSpecs[i][9] = amountWT;
			ObjectArray_ListOfexamsSpecs[i][10] = totalValueV.get(i);
			ObjectArray_ListOfexamsSpecs[i][11] = expiryDateV.get(i);
			total = total + Double.parseDouble(totalValueV.get(i));
			taxable=taxable+amountWT;
			tax=tax+Double.parseDouble(taxValueV.get(i));
			surcharge=surcharge+Double.parseDouble(surchargeValueV.get(i));
		}
		table.setModel(new DefaultTableModel(ObjectArray_ListOfexamsSpecs,
				new String[] { "Item ID", "Item Name", "Item Batch.", 
						"Qty.","Paid Qty.", "Unit Price","CGST","SGST", "Discount", "Amount(W/T)", "Value",
						"Expiry" }));

		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(3).setMinWidth(75);
		table.getColumnModel().getColumn(4).setMinWidth(75);
		table.getColumnModel().getColumn(5).setMinWidth(75);
		table.getColumnModel().getColumn(6).setPreferredWidth(65);
		table.getColumnModel().getColumn(6).setMinWidth(65);
		table.getColumnModel().getColumn(7).setPreferredWidth(65);
		table.getColumnModel().getColumn(7).setMinWidth(65);
		table.getColumnModel().getColumn(8).setPreferredWidth(65);
		table.getColumnModel().getColumn(8).setMinWidth(65);
		table.getColumnModel().getColumn(9).setPreferredWidth(100);
		table.getColumnModel().getColumn(9).setMinWidth(75);
		table.getColumnModel().getColumn(10).setPreferredWidth(100);
		table.getColumnModel().getColumn(10).setMinWidth(90);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);
		finalTotalValue = total;

		tax = Math.round(tax * 100.00) / 100.00;
		surcharge = Math.round(surcharge * 100.00) / 100.00;
		finalTotalTF.setText("" + finalTotalValue);
		finalTotal();
	}

	public void updatePrice() {
		ItemsDBConnection db = new ItemsDBConnection();
		try {
			db.updateprice(itemIDSTR, unitPrice + "", oldunitPrice + "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			db.closeConnection();
			e.printStackTrace();
		}
		db.closeConnection();
	}
}
