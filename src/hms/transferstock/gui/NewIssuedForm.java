package hms.transferstock.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.main.GeneralDBConnection;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.gui.StoreMain;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.ImageIcon;
import com.toedter.calendar.JDateChooser;
import javax.swing.JCheckBox;
import java.awt.Toolkit;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

public class NewIssuedForm extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JComboBox departmentCB;
	private JComboBox doctorCB;
	private JTextField departmentTF;
	private JTextField personTF;
	private JTextField invoiceNoTF;
	private JTextField searchItemTF;
	private JTextField itemDescTF;
	private JTextField qtyInHandTF;
	private JTextField qtyIssuedTF;
	private JTextField afterIssuedTF;
	private JTextField previouseStockTF;
	private JTable table;
	final DefaultComboBoxModel departmentName = new DefaultComboBoxModel();
	final DefaultComboBoxModel doctorName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemBatchName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName1 = new DefaultComboBoxModel();
	final DefaultComboBoxModel batchName = new DefaultComboBoxModel();
	final DefaultComboBoxModel measUnit = new DefaultComboBoxModel();
	Vector<String> supplierIDV = new Vector<String>();
	Vector<String> itemID = new Vector<String>();
	Vector<String> itemID1 = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> itemIDVHMS = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> itemNameVHMS = new Vector<String>();
	Vector<String> itemDescVHMS = new Vector<String>();
	Vector<String> issuedQtyV = new Vector<String>();
	Vector<String> previouseStockV = new Vector<String>();
	Vector<String> consumableV = new Vector<String>();
	Vector<String> batchID = new Vector<String>();
	Vector<String> expiryDateV = new Vector<String>();
	Vector<String> itemBatchIDV = new Vector<String>();
	Vector<String> itemBatchV = new Vector<String>();
	Vector<String> mrpPriceV = new Vector<String>();
	Vector<String> measUnitV = new Vector<String>();
	Vector<String> taxPercentageV = new Vector<String>();
	Vector<String> taxAmountV = new Vector<String>();
	Vector<String> surchargeV = new Vector<String>();
	Vector<String> surchargeAmountValueV = new Vector<String>();
	Vector<String> itemPriceV = new Vector<String>();
	Vector<Boolean> flagV = new Vector<Boolean>();

	double price = 0, taxValue = 0, surchargeValue = 0;
	int packSize = 1;
	String doctorNameSTR,bool;
	String departmentNameSTR, departmentID, personname, supplierID;
	String itemIDSTR, itemNameSTR, itemDescSTR, taxTypeSTR, taxValueSTR, itemIDSTR1, itemNameSTR1, itemDescSTR1,
			expiryDateSTR = "", issuedDateSTR = "", dueDateSTR = "", formulaActive = "", previouseStock = "",
			itemBatchNameSTR = "", itemLocationSTR = "", stockItem = "";
	int qtyIssued = 0, afterIssued = 0, discountValue = 0, itemValue, finalTaxValue = 0, finalDiscountValue = 0,
			finalTotalValue = 0;
	int quantity = 0, batchQty = 0;
	String batchIDSTR = "0";
	double mrp = 0, mrptotal = 0;
	double purchase = 0;
	double value = 0, surchargeAmountValue = 0, taxAmountValue = 0;
	boolean flag = false;
	Object[][] ObjectArray_ListOfexamsSpecs;
	private JComboBox itemNameCB;
	private JCheckBox chckbxConsumable;
	private JTextField batchQtyTF;
	private JComboBox batchNameCB;
	private JTextField expiryDateTF;
	int input = -1;
	private JButton btnNewButton;
	private JTextField textField;
	private JTextField textField_1;
	private JComboBox itemNameCB_1;
	private JButton btnNewItem;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewIssuedForm dialog = new NewIssuedForm();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public NewIssuedForm() {
		setTitle("Transfer Stock To Department");
		setIconImage(Toolkit.getDefaultToolkit().getImage(NewIssuedForm.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 100, 1031, 634);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		departmentCB = new JComboBox();
		departmentCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (itemIDV.size() > 0) {
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(NewIssuedForm.this,
							"Are you sure to change department.then all added prevoise items cleard",
							"Clear Added Items", dialogButton);
					if (dialogResult == 0) {
						itemIDV.clear();

						itemNameV.clear();
						itemDescV.clear();
						itemIDVHMS.clear();
						itemNameVHMS.clear();
						itemDescVHMS.clear();
						issuedQtyV.clear();
						batchID.clear();
						itemBatchIDV.clear();
						itemBatchV.clear();
						previouseStockV.clear();
						flagV.clear();
						;
						consumableV.clear();
						expiryDateV.clear();

						loadDataToTable();
						searchItemTF.setText("");
						expiryDateTF.setText("");

					} else {
						departmentCB.setSelectedItem(departmentNameSTR);
						return;
					}
				}
				try {
					departmentNameSTR = departmentCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}

				getDepartmentsDetail(departmentNameSTR);
				departmentTF.setText("");
				searchItemTF.setText("");
				if (departmentName.getSize() > 0) {

					departmentTF.setText("" + departmentID);
				}
			}
		});
		departmentCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		departmentCB.setBounds(150, 11, 218, 25);
		contentPanel.add(departmentCB);

		JLabel lblCredit = new JLabel("Select Department");
		lblCredit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCredit.setBounds(14, 11, 126, 25);
		contentPanel.add(lblCredit);

		departmentTF = new JTextField();
		departmentTF.setEditable(false);
		departmentTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		departmentTF.setColumns(10);
		departmentTF.setBounds(150, 47, 218, 25);
		contentPanel.add(departmentTF);

		JLabel lblDebit = new JLabel("Department ID :");
		lblDebit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDebit.setBounds(14, 47, 126, 25);
		contentPanel.add(lblDebit);

		personTF = new JTextField();
		personTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		personTF.setColumns(10);
		personTF.setBounds(150, 83, 218, 25);
		contentPanel.add(personTF);

		JLabel lblBalance = new JLabel("Person Name :");
		lblBalance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBalance.setBounds(14, 83, 126, 25);
		contentPanel.add(lblBalance);

		invoiceNoTF = new JTextField();
		invoiceNoTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		invoiceNoTF.setColumns(10);
		invoiceNoTF.setBounds(750, 47, 218, 25);
		contentPanel.add(invoiceNoTF);

		JLabel lblInvoiceNo = new JLabel("Intend Slip No. :");
		lblInvoiceNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInvoiceNo.setBounds(614, 47, 126, 25);
		contentPanel.add(lblInvoiceNo);

		JLabel lblDate = new JLabel("Date :");
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDate.setBounds(614, 83, 126, 25);
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
					qtyIssuedTF.setText("");
					afterIssuedTF.setText("");
					afterIssuedTF.setText("");
					previouseStockTF.setText("");
					batchQtyTF.setText("");
					batchName.removeAllElements();
					batchNameCB.setModel(batchName);
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();
					btnNewItem.setEnabled(false);
					textField.setText("");
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {

					itemDescTF.setText("");
					qtyIssuedTF.setText("");
					afterIssuedTF.setText("");
					afterIssuedTF.setText("");
					previouseStockTF.setText("");
					batchName.removeAllElements();
					batchQtyTF.setText("");
					batchNameCB.setModel(batchName);
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();
					btnNewItem.setEnabled(false);
					textField.setText("");
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {

					itemDescTF.setText("");
					qtyIssuedTF.setText("");
					afterIssuedTF.setText("");
					afterIssuedTF.setText("");
					batchQtyTF.setText("");
					previouseStockTF.setText("");
					batchName.removeAllElements();
					batchNameCB.setModel(batchName);
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();
					btnNewItem.setEnabled(false);
					textField.setText("");
				}
			}
		});

		itemDescTF = new JTextField();
		itemDescTF.setEditable(false);
		itemDescTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemDescTF.setBounds(14, 214, 293, 20);
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

				itemDescTF.setText("");
				qtyInHandTF.setText("");
				afterIssuedTF.setText("");
				previouseStockTF.setText("");
				getItemDetail(itemIDSTR);
				
				bool =" ";
				ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
				ResultSet resultSet = itemsDBConnection.retrieveHMSresult(itemIDSTR);
				int i = 0;
				try {
					while (resultSet.next()) {
						bool = resultSet.getString(1);
						i++;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (i != 0) {
					textField.setText(bool);
					btnNewItem.setEnabled(true);
				}
				

				// last_limit_check(itemIDSTR);
				if (itemName.getSize() > 0) {
//					if (!flag) {
//						expiryDateTF.setText("" + expiryDateSTR);
//						batchQtyTF.setText("" + quantity+"");
//						itemBatchNameSTR="N/A";
//						batchIDSTR="0";
//						getItemStock1(itemIDSTR);
//						
//					}else
					{
						getItemBatchName(itemIDSTR);
						getItemStock(itemIDSTR);

					}

					itemDescTF.setText("" + itemDescSTR);
					qtyInHandTF.setText("" + quantity);

					afterIssued = quantity - qtyIssued;
					// itemLocationTF.setText(itemLocationSTR);
					previouseStockTF.setText(previouseStock + "");
				}
			}
		});
		itemNameCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemNameCB.setBounds(139, 190, 168, 20);
		contentPanel.add(itemNameCB);

		qtyInHandTF = new JTextField();
		qtyInHandTF.setEditable(false);
		qtyInHandTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyInHandTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		qtyInHandTF.setBounds(406, 213, 126, 20);
		contentPanel.add(qtyInHandTF);
		qtyInHandTF.setColumns(10);

		qtyIssuedTF = new JTextField();
		qtyIssuedTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyIssuedTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		qtyIssuedTF.setBounds(14, 308, 119, 20);
		contentPanel.add(qtyIssuedTF);
		qtyIssuedTF.setColumns(10);
		qtyIssuedTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
					// ||vChar== '.'
				}
			}
		});
		qtyIssuedTF.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					Toolkit.getDefaultToolkit().beep();
					System.out.println("ENTER pressed");
					btnNewButton.doClick();
				}
			}
		});
		qtyIssuedTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = qtyIssuedTF.getText() + "";
				if (flag && !str.equals("") && (!(batchNameCB.getSelectedItem().equals("select Batch")))) {
					System.out.println("tttttttt" + stockItem);
					// System.out.println("tttttttt"
					// + qtyInHandET.getText().toString());
					String qtyhnd = qtyInHandTF.getText().toString();
					if (stockItem.equals("si")) {
						int check = Integer.parseInt(qtyhnd) - qtyIssued;
						System.out.println("check" + check);
						if (Integer.parseInt(qtyhnd) > 0 || check > 0) {
							System.out.println("tttttttt" + qtyhnd);
							qtyIssued = Integer.parseInt(str);
						} else {
							// enterQtyET.setText("");
							// JOptionPane.showMessageDialog(null,
							// "Please enter Stock", "Input Error",
							// JOptionPane.ERROR_MESSAGE);
							input = JOptionPane.showConfirmDialog(null, "Please enter Stock", "Input Error",
									JOptionPane.DEFAULT_OPTION);
							System.out.print("input" + input);
							return;
						}
					}
				}
				qtyIssued = Integer.parseInt(str);
				afterIssued = quantity - qtyIssued;

				afterIssuedTF.setText("" + afterIssued);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = qtyIssuedTF.getText() + "";
				if (!str.equals("")) {
					System.out.println("tttttttt" + stockItem);
					String qtyhnd = qtyInHandTF.getText().toString();
					if (stockItem.equals("si")) {

						if (Integer.parseInt(qtyhnd) > 0) {
							qtyIssued = Integer.parseInt(str);
						}

						else {
							// enterQtyET.setText("");
							// JOptionPane.showMessageDialog(null,
							// "Please enter Stock", "Input Error",
							// JOptionPane.ERROR_MESSAGE);
							input = JOptionPane.showConfirmDialog(null, "Please enter Stock", "Input Error",
									JOptionPane.DEFAULT_OPTION);
							System.out.print("input" + input);

							return;
						}

					} else {

						qtyIssued = Integer.parseInt(str);

					}
				} else {
					qtyIssued = 0;
				}
				afterIssued = quantity - qtyIssued;
				afterIssuedTF.setText("" + afterIssued);

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = qtyIssuedTF.getText() + "";
				if (!str.equals("")) {
					System.out.println("tttttttt" + stockItem);
					// System.out.println("tttttttt"
					// + qtyInHandET.getText().toString());
					String qtyhnd = qtyInHandTF.getText().toString();
					if (stockItem.equals("si")) {

						if (Integer.parseInt(qtyhnd) > 0) {
							qtyIssued = Integer.parseInt(str);
						}

						else {
							// enterQtyET.setText("");
							// JOptionPane.showMessageDialog(null,
							// "Please enter Stock", "Input Error",
							// JOptionPane.ERROR_MESSAGE);
							input = JOptionPane.showConfirmDialog(null, "Please enter Stock", "Input Error",
									JOptionPane.DEFAULT_OPTION);
							System.out.print("input" + input);

							return;
						}

					} else {

						qtyIssued = Integer.parseInt(str);

					}
				} else {
					qtyIssued = 0;
				}
				afterIssued = quantity - qtyIssued;
				afterIssuedTF.setText("" + afterIssued);
			}
		});

		afterIssuedTF = new JTextField();
		afterIssuedTF.setEditable(false);
		afterIssuedTF.setHorizontalAlignment(SwingConstants.RIGHT);
		afterIssuedTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		afterIssuedTF.setBounds(143, 308, 118, 20);
		contentPanel.add(afterIssuedTF);
		afterIssuedTF.setColumns(10);

		previouseStockTF = new JTextField();
		previouseStockTF.setEditable(false);
		previouseStockTF.setHorizontalAlignment(SwingConstants.RIGHT);
		previouseStockTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		previouseStockTF.setBounds(133, 245, 126, 20);
		contentPanel.add(previouseStockTF);
		previouseStockTF.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Previouse Stock :");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_2.setBounds(16, 248, 107, 14);
		contentPanel.add(lblNewLabel_2);

		JLabel lblSearch = new JLabel("Search Item");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSearch.setBounds(10, 165, 119, 14);
		contentPanel.add(lblSearch);

		JLabel lblQty = new JLabel("Qty. in Hand");
		lblQty.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblQty.setBounds(313, 217, 83, 14);
		contentPanel.add(lblQty);

		JLabel lblqtyIssued = new JLabel("Issued Qty.");
		lblqtyIssued.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblqtyIssued.setBounds(28, 283, 88, 14);
		contentPanel.add(lblqtyIssued);

		JLabel lblTax = new JLabel("After Issued Stock.");
		lblTax.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTax.setBounds(145, 283, 116, 14);
		contentPanel.add(lblTax);

		btnNewButton = new JButton("Add Line");
		btnNewButton.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					Toolkit.getDefaultToolkit().beep();
					System.out.println("ENTER pressed");
				}
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (itemDescTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please select item", "Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (textField_1.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please select item of hms", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (itemNameCB_1.getSelectedIndex() < -1) {
					JOptionPane.showMessageDialog(null, "Please select item of hms", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (flag && itemBatchName.getSize() <= 0) {
					JOptionPane.showMessageDialog(null, "Insufficient Item Batches!!You cannot Issue", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (batchNameCB.getSelectedItem().equals("select Batch")) {
					JOptionPane.showMessageDialog(null, "Please Select Batch!", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (qtyIssuedTF.getText().toString().equals("") || qtyIssuedTF.getText().toString().equals("0")) {
					JOptionPane.showMessageDialog(null, "Please enter issued qty.", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (Integer.parseInt(qtyIssuedTF.getText().toString()) > Integer
						.parseInt(batchQtyTF.getText().toString())) {
					JOptionPane.showMessageDialog(null, "your requirements exceeded the Item Batch stock",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (Integer.parseInt(afterIssuedTF.getText().toString()) < 0 && stockItem.equals("si")) {
					JOptionPane.showMessageDialog(null, "Insufficient Stock!!You cannot Issue", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (itemIDV.indexOf(itemIDSTR) != -1 && flag && itemBatchIDV.indexOf(batchIDSTR) != -1) {
					JOptionPane.showMessageDialog(null, "this item is already entered", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					if (itemIDV.indexOf(itemIDSTR) != -1 && !flag) {
						JOptionPane.showMessageDialog(null, "this item is already entered", "Input Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				insucheck();
			}

			private void insucheck() {
				// TODO Auto-generated method stub
				itemIDV.add(itemIDSTR);

				itemNameV.add(itemNameSTR);

				itemDescV.add(itemDescSTR);
				itemIDVHMS.add(itemIDSTR1);
				itemDescVHMS.add(itemDescSTR1);
				itemNameVHMS.add(itemNameSTR1);
				itemBatchIDV.add(batchIDSTR);
				itemBatchV.add(itemBatchNameSTR);
				mrpPriceV.add(mrptotal + "");
				measUnitV.add(packSize + "");
				taxPercentageV.add(taxValue + "");
				taxAmountV.add(taxAmountValue + "");
				surchargeV.add(surchargeValue + "");
				surchargeAmountValueV.add(surchargeAmountValue + "");
				flagV.add(flag);
				itemPriceV.add(price + "");

				issuedQtyV.add(qtyIssued + "");
				if (previouseStockTF.getText().toString().equals("")) {
					previouseStockV.add("New Item");
				} else {
					previouseStockV.add(previouseStockTF.getText().toString());
				}

				consumableV.add(chckbxConsumable.isSelected() ? "Yes" : "No");
				expiryDateV.add(expiryDateSTR);
				loadDataToTable();
				searchItemTF.setText("");
				expiryDateTF.setText("");
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(909, 165, 106, 31);
		contentPanel.add(btnNewButton);

		JLabel lblExpireDate = new JLabel("Expire Date :");
		lblExpireDate.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExpireDate.setBounds(271, 249, 86, 14);
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
				itemIDVHMS.remove(cur_selectedRow);
				itemNameV.remove(cur_selectedRow);
				itemDescV.remove(cur_selectedRow);
				itemNameVHMS.remove(cur_selectedRow);
				itemDescVHMS.remove(cur_selectedRow);
				issuedQtyV.remove(cur_selectedRow);
				previouseStockV.remove(cur_selectedRow);
				consumableV.remove(cur_selectedRow);
				itemBatchIDV.remove(cur_selectedRow);
				itemBatchV.remove(cur_selectedRow);
				flagV.remove(cur_selectedRow);
				itemPriceV.remove(cur_selectedRow);
				expiryDateV.remove(cur_selectedRow);

				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRemove.setBounds(909, 210, 106, 31);
		contentPanel.add(btnRemove);
		btnRemove.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 340, 992, 201);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Item ID MS++", "Item Name MS++", "Item Desc. MS++", "Item ID HMS", "Item Name HMS",
						"Item Desc. HMS", "Batch", "Issued Qty.", "Previouse Stock", "Cosumable", "Expiry" }));
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
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				int selectedRowIndex = table.getSelectedRow();
				selectedRowIndex = table.convertRowIndexToModel(selectedRowIndex);
				int selectedColumnIndex = table.getSelectedColumn();
				btnRemove.setEnabled(true);
			}
		});
		scrollPane.setViewportView(table);

		JSeparator separator = new JSeparator();
		separator.setBounds(64, 145, 468, 2);
		contentPanel.add(separator);

		JButton btnNewButton_1 = new JButton("Issue Items");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                      updateHMSCode();
				// if(departmentCB.getSelectedIndex()==-1)
				// {
				// JOptionPane.showMessageDialog(null,
				// "Please select supplier", "Input Error",
				// JOptionPane.ERROR_MESSAGE);
				// return;
				// }

				if (invoiceNoTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter invoice no", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (issuedDateSTR.equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter date", "Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (personTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter person name", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (itemIDV.size() <= 0) {
					JOptionPane.showMessageDialog(null, "Please enter atleast one item", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
				TransferDBConnection issuedItemsDBConnection = new TransferDBConnection();
				ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				int index = 0;

				String[] data = new String[30];
				data[0] = departmentID;
				data[1] = departmentNameSTR;
				data[2] = personTF.getText().toString();
				data[3] = "N/A";
				data[4] = "" + invoiceNoTF.getText().toString();
				data[5] = issuedDateSTR;
				data[6] = "" + timeFormat.format(cal1.getTime());
				for (int i = 0; i < itemIDV.size(); i++) {
					data[7] = itemIDVHMS.get(i);
					data[8] = itemNameVHMS.get(i);
					data[9] = itemDescVHMS.get(i);
					data[10] = issuedQtyV.get(i);
					data[11] = previouseStockV.get(i);
					data[12] = consumableV.get(i);
					data[13] = "0000-00-00";
					String str = itemBatchV.get(i);
					String[] arrOfStr = str.split("\\(", 2);
					data[14] = arrOfStr[0];
					data[15] = expiryDateV.get(i);
					data[16] = "" + StoreMain.userName; // user name
					data[17] = doctorNameSTR;
					data[18] = itemPriceV.get(i);
					try {
						issuedItemsDBConnection.inserIssuedData(data);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						itemsDBConnection.subtractStock(itemIDV.get(i), issuedQtyV.get(i));

						batchTrackingDBConnection.subtractStock(itemBatchIDV.get(i), issuedQtyV.get(i),
								DateFormatChange.StringToMysqlDate(new Date()), StoreMain.userName);

					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
				issuedItemsDBConnection.closeConnection();
				batchTrackingDBConnection.closeConnection();
				itemsDBConnection.closeConnection();
				TransferDBConnection departmentStockDBConnection = new TransferDBConnection();

				for (int i = 0; i < itemIDV.size(); i++) {
					data[7] = issuedDateSTR;
					data[8] = "" + timeFormat.format(cal1.getTime());
					data[17] = "MS++ Item";
					data[0] = itemIDVHMS.get(i);
					data[1] = itemNameVHMS.get(i);
					data[2] = itemDescVHMS.get(i);

					String str = itemBatchV.get(i);
					String[] arrOfStr = str.split("\\(", 2);
					data[3] = arrOfStr[0];	
					data[4] = "0";
					data[5] = "0";
					data[6] = expiryDateV.get(i);
					data[9] = "" + StoreMain.userName; // user name
					data[10] = itemPriceV.get(i);
					data[11] = mrpPriceV.get(i);
					data[12] = measUnitV.get(i);
					data[13] = taxPercentageV.get(i);
					data[14] = surchargeV.get(i);
					data[15] = taxAmountV.get(i);
					data[16] = surchargeAmountValueV.get(i);
					data[18] = itemPriceV.get(i);
					try {
						int key = departmentStockDBConnection.inserDataExam(data);
						data[0] = departmentID;
						data[1] = departmentNameSTR;
						data[2] = "" + StoreMain.userName;
						data[3] = itemIDVHMS.get(i);
						data[4] = itemNameVHMS.get(i);
						data[5] = itemDescVHMS.get(i);
						data[6] = issuedQtyV.get(i);
						data[7] = issuedDateSTR;
						data[8] = expiryDateV.get(i);
						data[9] = "1";
						String str1 = itemBatchV.get(i);
						String[] arrOfStr1 = str.split("\\(", 2);
						data[10] = arrOfStr1[0];
						data[11] = key + "";
						departmentStockDBConnection.inserDepartmentStock(data);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					{
						// try {
						// departmentStockDBConnection.addStock(
						// itemIDV.get(i), issuedQtyV.get(i),
						// departmentNameSTR);
						// } catch (Exception e1) {
						// // TODO Auto-generated catch block
						// e1.printStackTrace();
						// }
					}
				}
				departmentStockDBConnection.closeConnection();
				JOptionPane.showMessageDialog(null, "Item Issued Successfully", "Issued Items",
						JOptionPane.INFORMATION_MESSAGE);
				try {
					new HMSPillsSlipDepartment(itemIDV,itemIDVHMS,departmentNameSTR, itemNameV,itemNameVHMS, issuedQtyV, index, itemBatchV, expiryDateV,
							itemPriceV, personTF.getText());
				} catch (Exception exception) {
					// TODO: handle exception
				}

				dispose();
			}

			private void updateHMSCode() {
				// TODO Auto-generated method stub
				if(bool==null) {
				int hmsid=Integer.parseInt(itemIDSTR1);
				ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
				
			    itemsDBConnection.updateHMScode(itemIDSTR,hmsid);
			    System.out.println("ghggghhghghgh");
			    itemsDBConnection.closeConnection();
				}
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_1.setBounds(29, 553, 153, 39);
		contentPanel.add(btnNewButton_1);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(192, 553, 153, 39);
		contentPanel.add(btnCancel);

		JDateChooser invoiceDate = new JDateChooser();
		invoiceDate.getCalendarButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		invoiceDate.setBounds(750, 83, 218, 25);
		contentPanel.add(invoiceDate);

		chckbxConsumable = new JCheckBox("Consumable");
		chckbxConsumable.setSelected(true);
		chckbxConsumable.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxConsumable.setBounds(289, 305, 97, 25);
		contentPanel.add(chckbxConsumable);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(NewIssuedForm.class.getResource("/icons/aladdin3.gif")));
		lblNewLabel.setBounds(416, 0, 171, 133);
		contentPanel.add(lblNewLabel);
		invoiceDate.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				// TODO Auto-generated method stub
				if ("date".equals(arg0.getPropertyName())) {
					issuedDateSTR = DateFormatChange.StringToMysqlDate((Date) arg0.getNewValue());
				}
			}
		});
		invoiceDate.setDate(new Date());

		doctorCB = new JComboBox();
		doctorCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					doctorNameSTR = doctorCB.getSelectedItem().toString();

				} catch (Exception e1) {
					// TODO: handle exception

				}
			}
		});
		doctorCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		doctorCB.setBounds(750, 11, 218, 25);
		contentPanel.add(doctorCB);

		JLabel lblSelectDoctor = new JLabel("Select Doctor");
		lblSelectDoctor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSelectDoctor.setBounds(614, 11, 126, 25);
		contentPanel.add(lblSelectDoctor);

		batchNameCB = new JComboBox();
		batchNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				qtyIssuedTF.setText("");
				try {
					itemBatchNameSTR = batchNameCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}

				if (batchNameCB.getSelectedIndex() > -1) {
					batchIDSTR = batchID.get(batchNameCB.getSelectedIndex());

				}

				batchQtyTF.setText("");
				expiryDateTF.setText("");

				if (itemBatchName.getSize() > 0 && batchNameCB.getSelectedItem() != ("select Batch")) {
					getItemBatch(batchIDSTR);
					getItemStock(itemIDSTR);

					int addDays = 20;
					previouseStockTF.setText("" + previouseStock);
					batchQtyTF.setText("" + batchQty);
					expiryDateTF.setText("" + expiryDateSTR);
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
		batchNameCB.setBounds(313, 190, 219, 20);
		contentPanel.add(batchNameCB);

		JLabel label = new JLabel("Select Batch");
		label.setFont(new Font("Tahoma", Font.BOLD, 11));
		label.setBounds(313, 165, 92, 14);
		contentPanel.add(label);

		JLabel label_1 = new JLabel("Batch Qty. in Hand");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_1.setBounds(279, 279, 119, 14);
		contentPanel.add(label_1);

		batchQtyTF = new JTextField();
		batchQtyTF.setHorizontalAlignment(SwingConstants.RIGHT);
		batchQtyTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		batchQtyTF.setEditable(false);
		batchQtyTF.setColumns(10);
		batchQtyTF.setBounds(408, 275, 118, 20);
		contentPanel.add(batchQtyTF);

		expiryDateTF = new JTextField();
		expiryDateTF.setHorizontalAlignment(SwingConstants.RIGHT);
		expiryDateTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		expiryDateTF.setEditable(false);
		expiryDateTF.setColumns(10);
		expiryDateTF.setBounds(359, 245, 155, 20);
		contentPanel.add(expiryDateTF);

		textField = new JTextField();
		textField.setFont(new Font("Dialog", Font.BOLD, 11));
		textField.setColumns(10);
		textField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = textField.getText() + "";
				if (!str.equals("")) {
					getItemNameHMS(str);
				} else {

					textField_1.setText("");
					itemName1.removeAllElements();
					itemNameCB_1.setModel(itemName1);

				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = textField.getText() + "";
				if (!str.equals("")) {
					getItemNameHMS(str);
				} else {
					textField_1.setText("");
					itemName1.removeAllElements();
					itemNameCB_1.setModel(itemName1);

				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = textField.getText() + "";
				if (!str.equals("")) {
					getItemNameHMS(str);
				} else {
					textField_1.setText("");
					itemName1.removeAllElements();
					itemNameCB_1.setModel(itemName1);

				}
			}
		});
		textField.setBounds(558, 191, 119, 20);
		contentPanel.add(textField);

		itemNameCB_1 = new JComboBox();
		itemNameCB_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					itemNameSTR1 = itemNameCB_1.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}

				if (itemNameCB_1.getSelectedIndex() > -1) {
					itemIDSTR1 = itemID1.get(itemNameCB_1.getSelectedIndex());
				}

				textField_1.setText("");
//				qtyInHandTF.setText("");
//				afterIssuedTF.setText("");
//				previouseStockTF.setText("");
				getItemDetailHMS(itemIDSTR1);
				if (itemName1.getSize() > 0) {

					textField_1.setText("" + itemDescSTR1);
				}

			}
		});
		itemNameCB_1.setFont(new Font("Dialog", Font.BOLD, 11));
		itemNameCB_1.setBounds(687, 191, 168, 20);
		contentPanel.add(itemNameCB_1);

		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(544, 145, 2, 202);
		contentPanel.add(separator_1);

		JLabel lblSearch_1 = new JLabel("Search Item");
		lblSearch_1.setFont(new Font("Dialog", Font.BOLD, 11));
		lblSearch_1.setBounds(558, 165, 119, 14);
		contentPanel.add(lblSearch_1);

		JLabel lblMs = new JLabel("MS++");
		lblMs.setBounds(14, 130, 70, 25);
		contentPanel.add(lblMs);

		JLabel lblHms = new JLabel("HMS");
		lblHms.setBounds(560, 130, 70, 25);
		contentPanel.add(lblHms);

		btnNewItem = new JButton("New Item");
		btnNewItem.setEnabled(false);
		btnNewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewItem dialog = new NewItem();
				dialog.setVisible(true);
			}
		});
		btnNewItem.setFont(new Font("Dialog", Font.BOLD, 13));
		btnNewItem.setBounds(909, 266, 106, 31);
		contentPanel.add(btnNewItem);

		textField_1 = new JTextField();
		textField_1.setFont(new Font("Dialog", Font.BOLD, 11));
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		textField_1.setBounds(562, 242, 293, 20);
		contentPanel.add(textField_1);

		JLabel lblSearch_1_1 = new JLabel("Item Desc");
		lblSearch_1_1.setFont(new Font("Dialog", Font.BOLD, 12));
		lblSearch_1_1.setBounds(568, 217, 119, 14);
		contentPanel.add(lblSearch_1_1);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(598, 145, 417, 2);
		contentPanel.add(separator_2);
		getAllDoctors();
		getAllDepartments();
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

	public void getItemNameHMS(String index) {

		TransferDBConnection itemsDBConnection = new TransferDBConnection();
		ResultSet resultSet = itemsDBConnection.searchItemWithIdOrNmae(index);
		itemName1.removeAllElements();
		itemID1.clear();
		int i = 0;
		try {
			while (resultSet.next()) {
				itemID1.add(resultSet.getObject(1).toString());
				itemName1.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		itemNameCB_1.setModel(itemName1);
		if (i > 0) {
			itemNameCB_1.setSelectedIndex(0);
		}
	}
//	public void last_limit_check(String item_id)
//	{
//		DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
//		ResultSet resultSet = departmentStockDBConnection.retrieveDptLastWeekLimit(item_id,
//				departmentID);
//
//		try {
//			while (resultSet.next()) {
//
//				limitTF.setText("Last Week Issued Qty : "+resultSet.getString(1)+"");
//
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

	public void getItemDetailHMS(String index) {
		TransferDBConnection itemsDBConnection = new TransferDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail2(index);

		try {
			while (resultSet.next()) {
				itemDescSTR1 = resultSet.getObject(3).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getItemDetail(String index) {

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
               System.out.println(itemDescSTR+"hhhhhhh");
               System.out.println(index+"hhhhhhhh");

                expiryDateSTR = resultSet.getObject(10).toString();
				itemDescSTR = resultSet.getObject(9).toString();
				taxValue = Double.parseDouble(resultSet.getObject(4).toString());
				purchase = Double.parseDouble(resultSet.getObject(1).toString());
				System.out.println(purchase+"jjjjjjjjjj");
				System.out.println("purchase" + purchase);
				quantity = Integer.parseInt(resultSet.getObject(8).toString());

				surchargeValue = Double.parseDouble(resultSet.getObject(5).toString());
				// System.out.println(taxValue + "surcharge " +
				// surchargeValue);
				// itemHSNSTR = resultSet.getObject(14).toString();
				try {
					packSize = Integer.parseInt(resultSet.getObject(3).toString().trim());
				} catch (Exception e) {
					// TODO: handle exception
				}

//				SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
//				Date ten = parser.parse(currenttime);
//				  LocalTime time1 ;
//				currenttime="2020-11-09 09:00:00";
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				String time1 = formatter.format(date);
				SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
				Date date1 = new Date();
				String date_current = formatter1.format(date1);

				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, 1);
				String day = formatter1.format(calendar.getTime());
				System.out.println("nextdate" + day);
				Timestamp ts1 = Timestamp.valueOf(time1);
//				  System.out.println("dc"+date_current);
				String cmparetime = date_current + " " + "22:00:00";
				System.out.println("dc" + cmparetime);
				String cmparetimemrng = date_current + " " + "06:00:00";
				System.out.println("dc1" + cmparetimemrng);
//				  String cmparetimemrng=date_current+" "+"12:00:00";
				Timestamp ts2 = Timestamp.valueOf(cmparetime);
				Timestamp ts3 = Timestamp.valueOf(cmparetimemrng);
//				  String dd=;
//				   Timestamp ts2 = Timestamp.valueOf("2020-11-09 11:49:00"); 
				int b3 = ts1.compareTo(ts2);
				int b4 = ts1.compareTo(ts3);
				System.out.println("b3" + b3);
				System.out.println("b4" + b4);
				if (b4 < 0) {
					System.out.print("nnnbb");
					mrp = Double.parseDouble(resultSet.getObject(2).toString());
					mrptotal = mrp;
					if (formulaActive.equals("1")) {
						System.out.println("ifblock");
						sp = purchase;
					} else {
						System.out.println("elseblock");
						double tempvar1 = mrp / packSize;
						double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
						System.out.println("temp111" + tempvar2);
						double mrpwithouttax = tempvar1 - tempvar2;
						System.out.println("mrpwithouttax" + mrpwithouttax);
						double seventyprcntmrpwithouttax = 0.7 * mrpwithouttax;

						double margin = (mrpwithouttax - purchase) / purchase * 100;
						System.out.println("margin" + margin);
						double value = 0;
						if (purchase >= 10000 && purchase <= 20000) {
							sp = purchase * 1.15;
						} else if (purchase > 20000 && purchase <= 30000) {
							sp = purchase * 1.10;
						} else if (purchase >= 30000) {
							sp = purchase * 1.05;
						} else {
							if (margin > 100) {
								value = 1.95;
								sp = (double) Math.round(purchase * value * 100) / 100;

							} else if (margin > 80 && margin <= 100) {
								value = 1.75;
								sp = (double) Math.round(purchase * value * 100) / 100;
							} else if (margin > 70.0 && margin <= 80) {
								System.out.println("70");
								value = 1.65;
								sp = (double) Math.round(purchase * value * 100) / 100;
								System.out.println("sp" + sp);
							} else if (margin <= 70.0 && margin > 60.0) {
								System.out.println("60");
								value = 1.55;
								sp = (double) Math.round(purchase * value * 100) / 100;
								System.out.println("sp" + sp);
							} else if (margin <= 60.0 && margin > 50.0) {
								System.out.println("50");
								value = 1.45;
								sp = (double) Math.round(purchase * value * 100) / 100;
								System.out.println("sp" + sp);
							} else if (margin <= 50.0 && margin > 40.0) {
								System.out.println("40");
								value = 1.35;
								sp = (double) Math.round(purchase * value * 100) / 100;
								System.out.println("sp" + sp);
							} else if (margin <= 40.0 && margin > 30.0) {
								System.out.println("30");
								value = 1.30;
								sp = (double) Math.round(purchase * value * 100) / 100;
								System.out.println("sp" + sp);
							} else if (margin <= 30.0) {
								System.out.println("30less");

								value = 1.30;
								sp = (double) Math.round(purchase * value * 100) / 100;
								double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
								if (sp < mrpless1prcnt) {
									sp = sp;
								} else {
									sp = mrpless1prcnt;
								}
								System.out.println("sp" + sp);
							}

						}
					}

					sp = Math.round(sp * 100.00) / 100.00;
					price = sp;

				} else if ((b3 == 0 || b3 > 0)) {
					System.out.println("enyer");
					mrp = Double.parseDouble(resultSet.getObject(2).toString());
					mrptotal = mrp;
					if (formulaActive.equals("1")) {
						sp = purchase;
					} else {
						double tempvar1 = mrp / packSize;
						double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
						System.out.println("temp111" + tempvar2);
						double mrpwithouttax = tempvar1 - tempvar2;
						System.out.println("mrpwithouttax" + mrpwithouttax);
						double seventyprcntmrpwithouttax = 0.7 * mrpwithouttax;

						double margin = (mrpwithouttax - purchase) / purchase * 100;
						System.out.println("margin" + margin);
						double value = 0;
						if (purchase >= 10000 && purchase <= 20000) {
							sp = purchase * 1.15;
						} else if (purchase > 20000 && purchase <= 30000) {
							sp = purchase * 1.10;
						} else if (purchase >= 30000) {
							sp = purchase * 1.05;
						} else {
							if (margin > 100) {
								value = 1.95;
								sp = (double) Math.round(purchase * value * 100) / 100;

							} else if (margin > 80 && margin <= 100) {
								value = 1.75;
								sp = (double) Math.round(purchase * value * 100) / 100;
							} else if (margin > 70.0 && margin <= 80) {
								System.out.println("70");
								value = 1.65;
								sp = (double) Math.round(purchase * value * 100) / 100;
								System.out.println("sp" + sp);
							} else if (margin <= 70.0 && margin > 60.0) {
								System.out.println("60");
								value = 1.55;
								sp = (double) Math.round(purchase * value * 100) / 100;
								System.out.println("sp" + sp);
							} else if (margin <= 60.0 && margin > 50.0) {
								System.out.println("50");
								value = 1.45;
								sp = (double) Math.round(purchase * value * 100) / 100;
								System.out.println("sp" + sp);
							} else if (margin <= 50.0 && margin > 40.0) {
								System.out.println("40");
								value = 1.35;
								sp = (double) Math.round(purchase * value * 100) / 100;
								System.out.println("sp" + sp);
							} else if (margin <= 40.0 && margin > 30.0) {
								System.out.println("30");
								value = 1.30;
								sp = (double) Math.round(purchase * value * 100) / 100;
								System.out.println("sp" + sp);
							} else if (margin <= 30.0) {
								System.out.println("30less");

								value = 1.30;
								sp = (double) Math.round(purchase * value * 100) / 100;
								double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
								if (sp < mrpless1prcnt) {
									sp = sp;
								} else {
									sp = mrpless1prcnt;
								}
								System.out.println("sp" + sp);
							}

						}
					}

					sp = Math.round(sp * 100.00) / 100.00;
					price = sp;
				} else {
					System.out.println("ritika");
					mrp = Double.parseDouble(resultSet.getObject(2).toString());
					mrptotal = mrp;
					if (formulaActive.equals("1")) {
						sp = purchase;
					} else {
						double tempvar1 = mrp / packSize;
						System.out.println("temp111" + tempvar1);
						double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
						System.out.println("temp111" + tempvar2);
						double mrpwithouttax = tempvar1 - tempvar2;
						System.out.println("mrpwithouttax" + mrpwithouttax);
						double seventyprcntmrpwithouttax = 0.7 * mrpwithouttax;

						double margin = (mrpwithouttax - purchase) / purchase * 100;
						System.out.println("margin" + margin + "purchase" + purchase);
						double value = 0;
						if (purchase >= 10000 && purchase <= 20000) {
							sp = purchase * 1.15;
						} else if (purchase > 20000 && purchase <= 30000) {
							sp = purchase * 1.10;
						} else if (purchase >= 30000) {
							sp = purchase * 1.05;
						} else {
							if (margin > 70.0) {
								System.out.println("70");
								value = 1.5;
								sp = (double) Math.round(purchase * value * 100) / 100;
								System.out.println("sp" + sp);
							} else if (margin <= 70.0 && margin > 60.0) {
								System.out.println("60");
								value = 1.4;
								sp = (double) Math.round(purchase * value * 100) / 100;
								System.out.println("sp" + sp);
							} else if (margin <= 60.0 && margin > 50.0) {
								System.out.println("50");
								value = 1.3;
								sp = (double) Math.round(purchase * value * 100) / 100;
								System.out.println("sp" + sp);
							} else if (margin <= 50.0 && margin > 40.0) {
								System.out.println("40");
								value = 1.2;
								sp = (double) Math.round(purchase * value * 100) / 100;
								System.out.println("sp" + sp);
							} else if (margin <= 40.0 && margin > 30.0) {
								System.out.println("30");
								value = 1.15;
								sp = (double) Math.round(purchase * value * 100) / 100;
								System.out.println("sp" + sp);
							} else if (margin <= 30.0) {
								System.out.println("30less");

								value = 1.15;
								sp = (double) Math.round(purchase * value * 100) / 100;
								double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
								if (sp < mrpless1prcnt) {
									sp = sp;
								} else {
									sp = mrpless1prcnt;
								}
								System.out.println("sp" + sp);
							}

						}
					}
				}
				sp = Math.round(sp * 100.00) / 100.00;
				price = sp;
				System.out.println(price+"jhhhhhh");
			}

			double Tf = price * (taxValue / 100.0f);
			taxAmountValue = Math.round(Tf * 100.000) / 100.000;
			double Sf = price * (surchargeValue / 100.0f);
			surchargeAmountValue = Math.round(Sf * 100.000) / 100.000;
			// System.out.println("priceeeeeeeeee" + price);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getItemStock(String index) {

		previouseStock = "";
		TransferDBConnection departmentStockDBConnection = new TransferDBConnection();
		ResultSet resultSet = departmentStockDBConnection.retrieveDptStock(index, departmentNameSTR);

		try {
			while (resultSet.next()) {

				previouseStock = (resultSet.getObject(1) == null ? "0" : resultSet.getString(1).toString());

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		departmentStockDBConnection.closeConnection();

	}

	public void getItemStock1(String index) {

		previouseStock = "";
		TransferDBConnection departmentStockDBConnection = new TransferDBConnection();
		ResultSet resultSet = departmentStockDBConnection.retrieveDptStock(index, departmentNameSTR);

		try {
			while (resultSet.next()) {

				previouseStock = (resultSet.getObject(1) == null ? "0" : resultSet.getString(1).toString());

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		departmentStockDBConnection.closeConnection();

	}

	public void getItemBatchName(String index) {

		BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
		ResultSet resultSet = batchTrackingDBConnection.itemBatch(index);
		itemBatchName.removeAllElements();
		itemBatchName.addElement("select Batch");
		batchID.clear();
		batchID.add("0");
		int i = 0;
		try {
			while (resultSet.next()) {
				batchID.add(resultSet.getObject(1).toString());
				itemBatchName.addElement(resultSet.getObject(2).toString() + "(Batch-" + (i + 1) + ")");
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		batchTrackingDBConnection.closeConnection();
		batchNameCB.setModel(itemBatchName);
		if (i > 0) {
			batchNameCB.setSelectedIndex(0);
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

		int total = 0;

		ObjectArray_ListOfexamsSpecs = new Object[size][11];

		for (int i = 0; i < itemIDV.size(); i++) {

			ObjectArray_ListOfexamsSpecs[i][0] = itemIDV.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = itemNameV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = itemDescV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = itemIDVHMS.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = itemNameVHMS.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = itemDescVHMS.get(i);
			ObjectArray_ListOfexamsSpecs[i][6] = itemBatchV.get(i) == "select Batch" ? "" : itemBatchV.get(i);
			ObjectArray_ListOfexamsSpecs[i][7] = issuedQtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][8] = previouseStockV.get(i);
			ObjectArray_ListOfexamsSpecs[i][9] = consumableV.get(i);
			ObjectArray_ListOfexamsSpecs[i][10] = expiryDateV.get(i);
		}
		table.setModel(new DefaultTableModel(ObjectArray_ListOfexamsSpecs,
				new String[] { "Item ID MS++", "Item Name MS++", "Item Desc. MS++", "Item ID HMS", "Item Name HMS",
						"Item Desc. HMS", "Batch", "Issued Qty.", "Previouse Stock", "Cosumable", "Expiry" }));
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

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

		finalTotalValue = total;

	}

	public void getDepartmentsDetail(String deptName) {
		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
		ResultSet resultSet = dbConnection.retrieveDataWithName(deptName);
		int i = 0;
		try {
			while (resultSet.next()) {
				departmentID = resultSet.getObject(1).toString();
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();

	}

	public void getAllDepartments() {
		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		departmentName.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {
				departmentName.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		departmentCB.setModel(departmentName);
		if (i > 0) {
			departmentCB.setSelectedIndex(0);
		}
	}

	// public void getDoctorDetail(String docName) {
	// DepartmentDBConnection dbConnection = new DepartmentDBConnection();
	// ResultSet resultSet = dbConnection.retrieveDataWithName(docName);
	// int i = 0;
	// try {
	// while (resultSet.next()) {
	// departmentID = resultSet.getObject(3).toString();
	// i++;
	// }
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// dbConnection.closeConnection();
	//
	// }
	//

	public void getAllDoctors() {
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		doctorName.removeAllElements();
		doctorName.addElement("Other Doctor");
		int i = 0;
		try {
			while (resultSet.next()) {
				doctorName.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		doctorCB.setModel(doctorName);
		if (i > 0) {
			doctorCB.setSelectedIndex(0);
		}
	}
}
