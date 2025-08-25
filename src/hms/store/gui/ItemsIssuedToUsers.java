package hms.store.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.main.GeneralDBConnection;
import hms.patient.slippdf.PillsSlipDepartment;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.database.StoreAccountDBConnection;

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

public class ItemsIssuedToUsers extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JComboBox usersCB;
	private JTextField departmentTF;
	private JTextField searchItemTF;
	private JTextField itemDescTF;
	private JTextField qtyIssuedTF;
	private JTextField afterIssuedTF;
	private JTable table;
	final DefaultComboBoxModel AllUsers = new DefaultComboBoxModel();
	Vector<String> usersID = new Vector<String>();
	final DefaultComboBoxModel doctorName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemBatchName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel batchName = new DefaultComboBoxModel();
	Vector<String> batchUserQty = new Vector<String>();
	Vector<String> batchUserExpiry = new Vector<String>();
	final DefaultComboBoxModel measUnit = new DefaultComboBoxModel();
	Vector<String> supplierIDV = new Vector<String>();
	Vector<String> itemID = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> issuedQtyV = new Vector<String>();
	Vector<String> consumableV = new Vector<String>();
	Vector<String> batchID = new Vector<String>();
	Vector<String> expiryDateV = new Vector<String>();
	Vector<String> lastBatchQtyV = new Vector<String>();
	Vector<String> itemBatchIDV = new Vector<String>();
	Vector<String> itemBatchV = new Vector<String>();
	Vector<String> itemPriceV = new Vector<String>();
	Vector<String> totalPriceV = new Vector<String>();

	double price = 0, taxValue = 0, surchargeValue = 0;
	String doctorNameSTR;
	String userSTR, userID, personname, supplierID;
	String itemIDSTR, itemNameSTR, itemDescSTR, taxTypeSTR, taxValueSTR, expiryDateSTR = "", issuedDateSTR = "",
			dueDateSTR = "", itemBatchNameSTR = "", stockItem = "";
	int qtyIssued = 0, afterIssued = 0, discountValue = 0, finalTaxValue = 0, finalDiscountValue = 0,
			finalTotalValue = 0;
	double itemValue;
	int quantity = 0, batchQty = 0;
	String batchIDSTR = "0";

	Object[][] ObjectArray_ListOfexamsSpecs;
	private JComboBox itemNameCB;
	private JCheckBox chckbxConsumable;
	private JTextField batchQtyTF;
	private JComboBox batchNameCB;
	private JTextField expiryDateTF;
	int input = -1;
	private JButton btnNewButton;
	private JComboBox typeCB;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			StoreMain.userID="1";
			StoreMain.userName="arun";
			ItemsIssuedToUsers dialog = new ItemsIssuedToUsers();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ItemsIssuedToUsers() {
		setTitle("Issue Items Form To Department");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ItemsIssuedToUsers.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 100, 1031, 595);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		usersCB = new JComboBox();
		usersCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (itemIDV.size() > 0) {
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(ItemsIssuedToUsers.this,
							"Are you sure to change user.then all added prevoise items cleard",
							"Clear Added Items", dialogButton);
					if (dialogResult == 0) {
						itemIDV.clear();
						itemNameV.clear();
						itemDescV.clear();
						issuedQtyV.clear();
						batchID.clear();
						itemBatchIDV.clear();
						itemBatchV.clear();
						consumableV.clear();
						expiryDateV.clear();
						lastBatchQtyV.clear();
						loadDataToTable();
						searchItemTF.setText("");
						expiryDateTF.setText("");

					} else {
						usersCB.setSelectedItem(userSTR);
						userID=usersID.get(usersCB.getSelectedIndex());
						departmentTF.setText("" + userID);
						return;
					}
				}
				try {
					userSTR=usersCB.getSelectedItem().toString();
					userID=usersID.get(usersCB.getSelectedIndex());
					departmentTF.setText("" + userID);
				} catch (Exception e) {
					// TODO: handle exception

				}
			}
		});
		usersCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usersCB.setBounds(150, 11, 218, 25);
		contentPanel.add(usersCB);

		JLabel lblCredit = new JLabel("Users :");
		lblCredit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCredit.setBounds(36, 11, 126, 25);
		contentPanel.add(lblCredit);

		departmentTF = new JTextField();
		departmentTF.setEditable(false);
		departmentTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		departmentTF.setColumns(10);
		departmentTF.setBounds(150, 47, 218, 25);
		contentPanel.add(departmentTF);

		JLabel lblDebit = new JLabel("User ID :");
		lblDebit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDebit.setBounds(36, 47, 126, 25);
		contentPanel.add(lblDebit);

		JLabel lblDate = new JLabel("Date :");
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDate.setBounds(37, 84, 126, 25);
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
					batchQtyTF.setText("");
					batchName.removeAllElements();
					batchNameCB.setModel(batchName);
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();

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
					batchName.removeAllElements();
					batchQtyTF.setText("");
					batchNameCB.setModel(batchName);
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();

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
					batchName.removeAllElements();
					batchNameCB.setModel(batchName);
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();

				}
			}
		});

		itemDescTF = new JTextField();
		itemDescTF.setEditable(false);
		itemDescTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemDescTF.setBounds(66, 216, 293, 20);
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
				afterIssuedTF.setText("");
				getItemDetail(itemIDSTR);
				if (itemName.getSize() > 0) {
					if(typeCB.getSelectedIndex()==0)
						getItemBatchName(itemIDSTR);
					else
						getItemBatchNameUser(itemIDSTR);
					itemDescTF.setText("" + itemDescSTR);
					afterIssued = quantity - qtyIssued;
				}
			}
		});
		itemNameCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemNameCB.setBounds(139, 190, 168, 20);
		contentPanel.add(itemNameCB);

		qtyIssuedTF = new JTextField();
		qtyIssuedTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyIssuedTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		qtyIssuedTF.setBounds(551, 190, 119, 20);
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
					searchItemTF.requestFocus();
				}
			}
		});
		qtyIssuedTF.getDocument().addDocumentListener(new DocumentListener() {
			private void updateQtyIssued() {
				String str = qtyIssuedTF.getText().trim();

				if (str.isEmpty()) {
					qtyIssued = 0;
				} else {
					try {
						qtyIssued = Integer.parseInt(str);
					} catch (NumberFormatException e) {
						qtyIssued = 0;
					}
				}

				afterIssued = batchQty - qtyIssued;
				afterIssuedTF.setText(String.valueOf(afterIssued));
				itemValue(); // Call your method to update item values
			}

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
		});


		afterIssuedTF = new JTextField();
		afterIssuedTF.setEditable(false);
		afterIssuedTF.setHorizontalAlignment(SwingConstants.RIGHT);
		afterIssuedTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		afterIssuedTF.setBounds(680, 190, 118, 20);
		contentPanel.add(afterIssuedTF);
		afterIssuedTF.setColumns(10);

		JLabel lblSearch = new JLabel("Search Item");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSearch.setBounds(10, 165, 119, 14);
		contentPanel.add(lblSearch);

		JLabel lblqtyIssued = new JLabel("Issued Qty.");
		lblqtyIssued.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblqtyIssued.setBounds(565, 165, 88, 14);
		contentPanel.add(lblqtyIssued);

		JLabel lblTax = new JLabel("After Issued Stock.");
		lblTax.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTax.setBounds(682, 165, 153, 14);
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
				if (itemBatchName.getSize() <= 0) {
					JOptionPane.showMessageDialog(null, "Insufficient Item Batches!!You cannot Issue", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if ((batchNameCB.getSelectedItem().equals("select Batch"))) {
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
				if (itemIDV.indexOf(itemIDSTR) != -1 && itemBatchIDV.indexOf(batchIDSTR) != -1) {
					JOptionPane.showMessageDialog(null, "this item is already entered", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} 
				insucheck();
			}

			private void insucheck() {
				// TODO Auto-generated method stub
				itemIDV.add(itemIDSTR);
				itemNameV.add(itemNameSTR);
				itemDescV.add(itemDescSTR);
				itemBatchIDV.add(batchIDSTR);
				itemBatchV.add(itemBatchNameSTR);
				itemPriceV.add(price + "");
				totalPriceV.add(itemValue+"");

				issuedQtyV.add(qtyIssued + "");

				consumableV.add(chckbxConsumable.isSelected() ? "Yes" : "No");
				expiryDateV.add(expiryDateSTR);
				lastBatchQtyV.add(batchQtyTF.getText());
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
		lblExpireDate.setBounds(579, 219, 97, 14);
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
				issuedQtyV.remove(cur_selectedRow);
				consumableV.remove(cur_selectedRow);
				itemBatchIDV.remove(cur_selectedRow);
				itemBatchV.remove(cur_selectedRow);
				itemPriceV.remove(cur_selectedRow);
				totalPriceV.remove(cur_selectedRow);
				expiryDateV.remove(cur_selectedRow);
				lastBatchQtyV.remove(cur_selectedRow);

				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRemove.setBounds(909, 210, 106, 31);
		contentPanel.add(btnRemove);
		btnRemove.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 248, 992, 252);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Item ID", "Item Name", "Item Desc.",
				"Issued Qty.", "Previouse Stock", "Cosumable", "Expiry" }));
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
		separator.setBounds(10, 140, 966, 2);
		contentPanel.add(separator);

		JButton btnNewButton_1 = new JButton("Save");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (issuedDateSTR.equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter date", "Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (itemIDV.size() <= 0) {
					JOptionPane.showMessageDialog(null, "Please enter atleast one item", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();

				ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				int index = 0;
				String[] data = new String[20];
				int type=typeCB.getSelectedIndex();
				for (int i = 0; i < itemIDV.size(); i++) {
					data[0]  = (type==0?"":"-")+issuedQtyV.get(i);           // qty
					data[1]  = StoreMain.userID;            // issued_by_id
					data[2]  = StoreMain.userName;          // issued_by
					data[3]  = userSTR;                     // issued_to
					data[4]  = userID;                      // issued_to_id
					data[5]  = typeCB.getSelectedItem().toString();                     // entry_type (or "RETURN" based on context)
					data[6]  = issuedDateSTR;               // date_time (optional — DB sets it by default)
					data[7]  = itemIDV.get(i);              // item_id
					data[8]  = itemNameV.get(i);            // item_name
					data[9]  = itemBatchIDV.get(i);         // batch_id
					data[10] = itemBatchV.get(i).split("\\(", 2)[0];          // batch_name
					data[11] = "";                          // remarks (optional or fill if needed)
					data[12] = type==0?lastBatchQtyV.get(i):getItemBatch(itemIDV.get(index))+"";   
					data[13] = expiryDateV.get(i);// current_batch_qty

					try {
						itemsDBConnection.insertDataToAudit(data);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

				itemsDBConnection.closeConnection();
				JOptionPane.showMessageDialog(null, "Item Issued Successfully", "Issued Items",
						JOptionPane.INFORMATION_MESSAGE);

				dispose();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_1.setBounds(33, 511, 153, 39);
		contentPanel.add(btnNewButton_1);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(196, 511, 153, 39);
		contentPanel.add(btnCancel);

		JDateChooser invoiceDate = new JDateChooser();
		invoiceDate.getCalendarButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		invoiceDate.setBounds(150, 84, 218, 25);
		contentPanel.add(invoiceDate);

		chckbxConsumable = new JCheckBox("Consumable");
		chckbxConsumable.setSelected(true);
		chckbxConsumable.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxConsumable.setBounds(804, 187, 97, 25);
		contentPanel.add(chckbxConsumable);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(ItemsIssuedToUsers.class.getResource("/icons/aladdin3.gif")));
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
					if(typeCB.getSelectedIndex()==0)
						getItemBatch(batchIDSTR);
					else
						getItemBatch1(batchNameCB.getSelectedIndex());
					int addDays = 20;
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

		JLabel lblBatchQty = new JLabel("Batch Qty :");
		lblBatchQty.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblBatchQty.setBounds(367, 219, 119, 14);
		contentPanel.add(lblBatchQty);

		batchQtyTF = new JTextField();
		batchQtyTF.setText("                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ");
		batchQtyTF.setHorizontalAlignment(SwingConstants.RIGHT);
		batchQtyTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		batchQtyTF.setEditable(false);
		batchQtyTF.setColumns(10);
		batchQtyTF.setBounds(441, 215, 118, 20);
		contentPanel.add(batchQtyTF);

		expiryDateTF = new JTextField();
		expiryDateTF.setHorizontalAlignment(SwingConstants.RIGHT);
		expiryDateTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		expiryDateTF.setEditable(false);
		expiryDateTF.setColumns(10);
		expiryDateTF.setBounds(668, 216, 110, 20);
		contentPanel.add(expiryDateTF);

		JLabel lblDesc = new JLabel("Desc :");
		lblDesc.setFont(new Font("Dialog", Font.BOLD, 11));
		lblDesc.setBounds(20, 219, 119, 14);
		contentPanel.add(lblDesc);

		JLabel lblSelectItem = new JLabel("Select Item");
		lblSelectItem.setFont(new Font("Dialog", Font.BOLD, 11));
		lblSelectItem.setBounds(139, 164, 119, 14);
		contentPanel.add(lblSelectItem);

		typeCB = new JComboBox();
		typeCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (itemIDV.size() > 0 || !searchItemTF.getText().equals("")) {
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(ItemsIssuedToUsers.this,
							"Are you sure to change department.then all added prevoise items cleard",
							"Clear Added Items", dialogButton);
					if (dialogResult == 0) {
						itemIDV.clear();
						itemNameV.clear();
						itemDescV.clear();
						issuedQtyV.clear();
						batchID.clear();
						itemBatchIDV.clear();
						itemBatchV.clear();
						consumableV.clear();
						expiryDateV.clear();
						lastBatchQtyV.clear();
						loadDataToTable();
						searchItemTF.setText("");
						expiryDateTF.setText("");

					} else {
						usersCB.setSelectedItem(userSTR);
						userID=usersID.get(usersCB.getSelectedIndex());
						departmentTF.setText("" + userID);
						return;
					}
				}
				try {
					userSTR=usersCB.getSelectedItem().toString();
					userID=usersID.get(usersCB.getSelectedIndex());
					departmentTF.setText("" + userID);
				} catch (Exception e1) {
					// TODO: handle exception
					e1.printStackTrace();
				}


			}
		});
		typeCB.setModel(new DefaultComboBoxModel(new String[] {"ISSUE", "RETURN"}));
		typeCB.setFont(new Font("Dialog", Font.PLAIN, 14));
		typeCB.setBounds(621, 47, 218, 25);
		contentPanel.add(typeCB);
		getAllUsers();
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

	public void itemValue() {
		itemValue = qtyIssued * price;

		itemValue = Math.round(itemValue * 100.000) / 100.000;

	}
	public void getItemDetail(String index) {

		GeneralDBConnection formula = new GeneralDBConnection();
		double value = Double.parseDouble(formula.retrieveFormula1());
		formula.closeConnection();
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail2(index);
		double mrp = 0;
		double purchase = 0, tot = 0, tot1 = 0, sp = 0;
		int packSize = 1;

		quantity = 0;
		expiryDateSTR = "";
		String formulaActive = "";
		try {
			while (resultSet.next()) {
				expiryDateSTR = resultSet.getObject(9).toString();

				batchNameCB.setEnabled(true);

				itemDescSTR = resultSet.getObject(3).toString();
				quantity = Integer.parseInt(resultSet.getObject(8).toString());
				taxValue = Double.parseDouble(resultSet.getObject(6).toString());
				surchargeValue = Double.parseDouble(resultSet.getObject(13).toString());
				stockItem = resultSet.getObject(16).toString();
				purchase = Double.parseDouble(resultSet.getObject(19).toString());
				mrp = Double.parseDouble(resultSet.getObject(11).toString());
				price = Double.parseDouble(resultSet.getObject(10).toString());
				tot = (double) Math.round(purchase * value * 100) / 100;
				tot1 = (double) Math.round(purchase * 2.5 * 100) / 100;
				formulaActive = resultSet.getObject(15).toString();
				try {
					packSize = Integer.parseInt(resultSet.getObject(4).toString().trim());
				} catch (Exception e) {
					// TODO: handle exception
				}
				double tax = taxValue + surchargeValue;
				if (formulaActive.equals("1")) {
					sp = price;
				} else {
					if (purchase >= 10000 && purchase <= 20000) {

						double tempvar1 = mrp / packSize;
						double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
						double mrpwithouttax = tempvar1 - tempvar2;
						double temp = 1.15 * purchase;
						if (mrpwithouttax > temp) {
							sp = temp;

						} else {
							double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
							sp = mrpless1prcnt;
						}
						// sp = purchase * 1.15;
					} else if (purchase > 20000 && purchase <= 30000) {
						double tempvar1 = mrp / packSize;
						double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
						double mrpwithouttax = tempvar1 - tempvar2;
						double temp = 1.10 * purchase;
						if (mrpwithouttax > temp) {
							sp = temp;

						} else {
							double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
							sp = mrpless1prcnt;
						}
						// sp = purchase * 1.10;
					} else if (purchase > 30000) {
						double tempvar1 = mrp / packSize;
						double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
						double mrpwithouttax = tempvar1 - tempvar2;
						double temp = 1.05 * purchase;
						if (mrpwithouttax > temp) {
							sp = temp;

						} else {
							double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
							sp = mrpless1prcnt;
						}
						// sp = purchase * 1.05;
					} else if (purchase > 5000 && purchase <= 10000) {
						System.out.println("5000" + purchase);
						double tempvar1 = mrp / packSize;
						double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
						double mrpwithouttax = tempvar1 - tempvar2;
						double temp = 1.25 * purchase;
						if (mrpwithouttax > temp) {
							sp = temp;

						} else {
							double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
							sp = mrpless1prcnt;
						}
					} else if (purchase > 1000 && purchase <= 5000) {
						System.out.println("1000" + purchase);
						double tempvar1 = mrp / packSize;
						double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
						double mrpwithouttax = tempvar1 - tempvar2;
						double temp = 1.30 * purchase;
						if (mrpwithouttax > temp) {
							sp = temp;

						} else {
							double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
							sp = mrpless1prcnt;
						}
					} else if (purchase > 250 && purchase <= 1000) {
						System.out.println("250" + purchase);
						double tempvar1 = mrp / packSize;
						double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
						double mrpwithouttax = tempvar1 - tempvar2;
						double temp = 1.5 * purchase;
						if (mrpwithouttax > temp) {
							sp = temp;

						} else {
							double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
							sp = mrpless1prcnt;
						}
					} else if (purchase > 0 && purchase <= 250) {
						System.out.println("0" + purchase);
						double tempvar1 = mrp / packSize;
						double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
						double mrpwithouttax = tempvar1 - tempvar2;
						double temp = 2.5 * purchase;
						if (mrpwithouttax > temp) {
							sp = temp;

						} else {
							double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
							sp = mrpless1prcnt;
						}
					} else {
						double tempvar1 = mrp / packSize;
						double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
						double mrpwithouttax = tempvar1 - tempvar2;
						double temp = 2.5 * purchase;
						if (mrpwithouttax > temp) {
							sp = temp;

						} else {
							double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
							sp = mrpless1prcnt;
						}

					}
				}
			}

			sp=sp<purchase?purchase:sp;// to find the wrong price calculation 30 dec 2023


			price = (double) Math.round(sp * 100) / 100;
			double k = price * (taxValue / 100.0f);
			k = Math.round(k * 100.000) / 100.000;
			double s = price * (surchargeValue / 100.0f);
			s = Math.round(s * 100.000) / 100.000;

			price = price + k;
			price = price + s;

			price = Math.round(price * 100.000) / 100.000;

			System.out.print(price+" price");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();

	}



	public void getItemBatchName(String index) {
		System.out.println(index+" namebnbebeb");
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


	public void getItemBatchNameUser(String index) {
		BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
		ResultSet resultSet = batchTrackingDBConnection.getUsersBatchNames(index,userID);
		itemBatchName.removeAllElements();
		batchUserQty.removeAllElements();
		batchUserExpiry.removeAllElements();
		itemBatchName.addElement("select Batch");
		batchID.clear();
		batchID.add("0");
		batchUserQty.add("0");
		batchUserExpiry.add("");
		int i = 0;
		try {
			while (resultSet.next()) {
				batchUserQty.add(resultSet.getString(1));
				batchID.add(resultSet.getObject(2).toString());
				itemBatchName.addElement(resultSet.getObject(3).toString() + "(Batch-" + (i + 1) + ")");
				batchUserExpiry.add(resultSet.getString(4));
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

	public void getItemBatch1(int index) {
		batchQty = 0;
		batchQty =Integer.parseInt(batchUserQty.get(index));
		expiryDateSTR = batchUserExpiry.get(index);
	}
	public int getItemBatch(String index) {
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
		return batchQty;
	}

	private void loadDataToTable() {
		// get size of the hashmap
		int size = itemIDV.size();

		int total = 0;

		ObjectArray_ListOfexamsSpecs = new Object[size][10];

		for (int i = 0; i < itemIDV.size(); i++) {

			ObjectArray_ListOfexamsSpecs[i][0] = itemIDV.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = itemNameV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = itemDescV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = itemBatchV.get(i) == "select Batch" ? "" : itemBatchV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = issuedQtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = "0";
			ObjectArray_ListOfexamsSpecs[i][6] = consumableV.get(i);
			ObjectArray_ListOfexamsSpecs[i][7] = expiryDateV.get(i);
		}
		table.setModel(new DefaultTableModel(ObjectArray_ListOfexamsSpecs, new String[] { "Item ID", "Item Name",
				"Item Desc.", "Batch No.", "Issued Qty.", "Previouse Stock", "Cosumable", "Expiry" }));
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

	public void getAllUsers() {
		StoreAccountDBConnection dbConnection = new StoreAccountDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		AllUsers.removeAllElements();
		usersID.clear();
		int i = 0;
		try {
			while (resultSet.next()) {
				AllUsers.addElement(resultSet.getObject(2).toString());
				usersID.add(resultSet.getString(1));
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		usersCB.setModel(AllUsers);
		if (i > 0) {
			usersCB.setSelectedIndex(0);
		}
	}
}
