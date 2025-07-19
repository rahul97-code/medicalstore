package hms.store.gui;

import hms.gl.database.GLAccountDBConnection;
import hms.main.DateFormatChange;
import hms.patient.slippdf.Bill_PDF;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.ChallanDBConnection;
import hms.store.database.IndentDBConnection;
import hms.store.database.InvoiceDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.database.PODBConnection;
import hms.store.database.ReturnInvoiceDBConnection;
import hms.store.database.SuppliersDBConnection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
//import javax.swing.table.DatePickerCellEditor;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFRow;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.SystemColor;

public class NewInvoice extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public JTextField searchItemTF;
	private JTextField itemDescTF;
	private JTextField qtyTF;
	private JTextField unitPriceTF;
	private JTextField taxTF;
	private JTextField discountTF;
	private JTextField totalValueTF;
	private JTable table;
	private JTextField finalTaxTF;
	private JTextField finalDiscountTF;
	private JTextField mrpPriceTF;
	private JTextField poQtyTF;
	JDateChooser dateChooser;
	DecimalFormat df = new DecimalFormat("#.##");
	String measUnitS = "";
	String date1;
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
	Vector<String> mrpPriceV = new Vector<String>();
	Vector<String> taxV = new Vector<String>();
	Vector<String> taxValueV = new Vector<String>();
	Vector<String> PurchasePriceVV = new Vector<String>();
	Vector<String> surchargeV = new Vector<String>();
	Vector<String> surchargeValueV = new Vector<String>();
	Vector<String> igst = new Vector<String>();
	Vector<String> igstValueV = new Vector<String>();
	Vector<String> discountV = new Vector<String>();
	Vector<String> discountPerV = new Vector<String>();
	Vector<String> totalValueV = new Vector<String>();
	Vector<String> expiryDateV = new Vector<String>();
	Vector<String> batchNumberV = new Vector<String>();
	Vector<Boolean> addStock = new Vector<Boolean>();

	Vector<String> returnGoodBatch = new Vector<String>();
	Vector<String> challanBatch = new Vector<String>();
	Vector<String> returnitemID = new Vector<String>();
	Vector<String> challanItemID = new Vector<String>();

	String supplierDisplaySTR, mobileSTR, addressSTR, supplierID,
	supplierNameSTR;
	String itemIDSTR, itemNameSTR, itemDescSTR, taxTypeSTR, taxValueSTR,
	itemSurchargeSTR, itemIgstSTR, expiryDateSTR = "",
	invoiceDateSTR = "", dueDateSTR = "0000-00-00";
	double oldMrp = 0, unitPrice = 0, oldunitPrice = 0, taxValue = 0,
			itemSurcharge = 0, itemIgst = 0, discountValue = 0, itemValue,
			finalTaxValue = 0, finalDiscountValue = 0, finalTotalValue = 0,
			surchargeAmountValue = 0, igstAmountValue = 0, taxAmountValue = 0,
			finalTotalValueCoin = 0, mrpPrice = 0, discountPrcntValue = 0,
			finalReturnAmount = 0;
	double unitPriceCal=0;
	int quantity = 0, freeQuantity = 0, paidQuantity = 0;
	double mrp = 0;
	Object[][] ObjectArray_ListOfexamsSpecs;
	private JComboBox itemNameCB;
	private JDateChooser expiryDateC;
	private JTextField finalTotalTF;
	ButtonGroup paymentOption = new ButtonGroup();
	private JTextField batchNumberTF;
	private JTextField freeQtyTF;
	private JTextField itemSurchargeTF;
	private JLabel taxableAmountLB;
	private JLabel surchargeLB;
	private JLabel taxAmountLB;
	private JLabel coinADJLB;
	InvoiceBrowser browser = null;
	private JTextField igstTF;
	// public static invoice_id;
	public static String po_id = "";
	private JTextField discountprcnTF;
	// private JTextField measUnitCD;
	private JTextField measUnitET;
	private JTextField returnAmount;
	private JTextField unitPriceCalET;
	private String status;
	private JTextField itemNameTF;
	private JTextField itemIdTF;
	private JTextField supplierTF;
	private JTextField mobileTF;
	private JTextField addressTF;
	private JComboBox supplierCB;
	private JTextField invoiceNoTF;
	private JTextField orderNoTF;
	private JTextField finalamountTF;
	private JDateChooser invoiceDate;
	private JDateChooser dueDate;
	private JLabel igstLBL;
	private String supplierStateCode=null;
	private JCheckBox discountCBox;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewInvoice dialog = new NewInvoice("4141", "PO-25-4053", "HUNAR ENTERPRISES");
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public NewInvoice(String ID, String PONumber, String VendorName) {
		NewInvoice.po_id = ID;

		setResizable(false);
		setBounds(100, 70, 1143, 666);
		// setBounds(100, 70, 1031, 640);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JButton btnNewButton = new JButton("Add Line");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addItemLine();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(896, 253, 114, 33);
		contentPanel.add(btnNewButton);

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
				measUnitV.remove(cur_selectedRow);
				qtyV.remove(cur_selectedRow);
				freeqtyV.remove(cur_selectedRow);
				paidqtyV.remove(cur_selectedRow);
				unitPriceV.remove(cur_selectedRow);
				mrpPriceV.remove(cur_selectedRow);
				taxV.remove(cur_selectedRow);
				surchargeV.remove(cur_selectedRow);
				taxValueV.remove(cur_selectedRow);
				surchargeValueV.remove(cur_selectedRow);
				igst.remove(cur_selectedRow);
				igstValueV.remove(cur_selectedRow);
				discountV.remove(cur_selectedRow);
				discountPerV.remove(cur_selectedRow);
				totalValueV.remove(cur_selectedRow);
				expiryDateV.remove(cur_selectedRow);
				batchNumberV.remove(cur_selectedRow);
				addStock.remove(cur_selectedRow);
				PurchasePriceVV.remove(cur_selectedRow);
				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRemove.setBounds(1017, 253, 114, 33);
		contentPanel.add(btnRemove);
		btnRemove.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(3, 299, 1128, 243);
		contentPanel.add(scrollPane);
		table = new JTable();
		table.setToolTipText("Double Click to edit item");
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));

		table.setModel(new DefaultTableModel(new Object[][] { { null, null,
			null, null, null, null }, }, new String[] { "Item ID",
					"Item Name", "Item Batch.", "Qty.", "Rec Qty.",
					"Unit Price", "CGST", "SGST","IGST", "Discount",
					"Amount(Without Vat)", "Value", "Expiry", "MRP", "Pack Size","New Unit Price" }));

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
		table.getColumnModel().getColumn(10).setPreferredWidth(65);
		table.getColumnModel().getColumn(10).setMinWidth(90);
		table.getColumnModel().getColumn(11).setPreferredWidth(65);
		table.getColumnModel().getColumn(11).setMinWidth(90);

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

					itemIdTF.setText(itemIDV.get(cur_selectedRow));
					itemNameTF.setText(itemNameV.get(cur_selectedRow));
					qtyTF.setText((paidqtyV.get(cur_selectedRow)));
					itemDescTF.setText(itemDescV.get(cur_selectedRow));
					freeQtyTF.setText(freeqtyV.get(cur_selectedRow));
					poQtyTF.setText(qtyV.get(cur_selectedRow));
					mrpPriceTF.setText(mrpPriceV.get(cur_selectedRow));
					measUnitET.setText(measUnitV.get(cur_selectedRow));
					batchNumberTF.setText(batchNumberV.get(cur_selectedRow));
					unitPriceTF.setText(unitPriceV.get(cur_selectedRow));
					if(supplierStateCode.equals("6")) {
						taxTF.setText(taxV.get(cur_selectedRow));
						itemSurchargeTF.setText(surchargeV.get(cur_selectedRow));
						igstTF.setText("0");
					}else {
						taxTF.setText("0");
						itemSurchargeTF.setText("0");
						double t=Double.parseDouble(taxV.get(cur_selectedRow));
						double t1=Double.parseDouble(surchargeV.get(cur_selectedRow));
						if(Double.parseDouble(igst.get(cur_selectedRow))==0)
							igstTF.setText((t+t1)+"");
						else
							igstTF.setText(igst.get(cur_selectedRow));
					}

					java.util.Date date = null;
					try {
						if (!expiryDateV.get(cur_selectedRow).isEmpty()) {
							date = new SimpleDateFormat("yyyy-MM-d").parse(expiryDateV.get(cur_selectedRow));
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					expiryDateC.setDate(date); 
//					discountCBox
					if(Double.parseDouble(discountPerV.get(cur_selectedRow))!=0 ) {
					discountprcnTF.setText(""+(discountPerV.get(cur_selectedRow)));
					}

					itemIDV.remove(cur_selectedRow);
					itemNameV.remove(cur_selectedRow);
					itemDescV.remove(cur_selectedRow);
					measUnitV.remove(cur_selectedRow);
					qtyV.remove(cur_selectedRow);
					freeqtyV.remove(cur_selectedRow);
					paidqtyV.remove(cur_selectedRow);
					unitPriceV.remove(cur_selectedRow);
					mrpPriceV.remove(cur_selectedRow);
					taxV.remove(cur_selectedRow);
					surchargeV.remove(cur_selectedRow);
					taxValueV.remove(cur_selectedRow);
					surchargeValueV.remove(cur_selectedRow);
					discountV.remove(cur_selectedRow);
					discountPerV.remove(cur_selectedRow);
					totalValueV.remove(cur_selectedRow);
					expiryDateV.remove(cur_selectedRow);
					batchNumberV.remove(cur_selectedRow);
					addStock.remove(cur_selectedRow);
					igst.remove(cur_selectedRow);
					igstValueV.remove(cur_selectedRow);
					PurchasePriceVV.remove(cur_selectedRow);

					loadDataToTable();
					btnRemove.setEnabled(false);
					qtyTF.requestFocusInWindow();
				}
			}
		});
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 148, 1121, 2);
		contentPanel.add(separator);
		JButton btnNewButton_1 = new JButton("Save Invoice");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (supplierCB.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(null, "Please select supplier", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (invoiceNoTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter invoice no", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (orderNoTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter order no", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (invoiceDateSTR.equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter date", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (finalamountTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Enter Invoice Amount ", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					double finalamountentered = Double.parseDouble(finalamountTF.getText().toString());
					double finalamountgenerated = Double.parseDouble(finalTotalTF.getText().toString());
					if (finalamountentered != finalamountgenerated) {
						JOptionPane.showMessageDialog(null, "Invoice Amount and Total Amount are not match ",
								"Input Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}

				InvoiceDBConnection invoiceDBConnection = new InvoiceDBConnection();
				try {
					if (invoiceDBConnection.retrieveCheckInvoice(invoiceNoTF.getText().toString()) > 0) {
						JOptionPane.showMessageDialog(null, "Invoice number already entered. Please check manually",
								"Input Error", JOptionPane.ERROR_MESSAGE);
						invoiceDBConnection.closeConnection();
						return;
					}					
				} catch (SQLException e2) { 
					e2.printStackTrace();
				}
				TableModel model = table.getModel();
				for (int rows = 0; rows < model.getRowCount(); rows++) {
					if (model.getValueAt(rows, 13).toString().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Please enter Expiry Date", "Input Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if (model.getValueAt(rows, 2).toString().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Please enter Batch Value", "Input Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				

				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				int index = 0;
				String[] data_indent_entry = new String[30];
				data_indent_entry[0] = null;
				data_indent_entry[1] = invoiceNoTF.getText().toString();
				data_indent_entry[2] = orderNoTF.getText().toString();
				data_indent_entry[3] = supplierID;
				data_indent_entry[4] = supplierDisplaySTR;
				data_indent_entry[5] = invoiceDateSTR;
				data_indent_entry[6] = "" + timeFormat.format(cal1.getTime());
				data_indent_entry[7] = dueDateSTR;
				data_indent_entry[8] = "No";
				data_indent_entry[9] = "" + StoreMain.userName; 
				data_indent_entry[10] = finalTotalValue + "";
				data_indent_entry[11] = finalTaxValue + "";
				data_indent_entry[12] = finalDiscountValue + "";
				data_indent_entry[13] = finalTotalTF.getText().toString();
				data_indent_entry[14] = finalReturnAmount + "";
				data_indent_entry[15] = po_id;
				data_indent_entry[16] = "";

				System.out.println(Arrays.toString(data_indent_entry));
				String[] data = new String[30];
				ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
				BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
				try {
					index = invoiceDBConnection.inserInvoiceNew(data_indent_entry);

					for (int i = 0; i < itemIDV.size(); i++) {
						data[0] = null;
						data[1] = itemIDV.get(i);
						data[2] = itemNameV.get(i);
						data[3] = itemDescV.get(i);
						data[4] = index + "";
						data[5] =  (int) Float.parseFloat(measUnitV.get(i))+"";
						data[6] = qtyV.get(i);
						data[7] = freeqtyV.get(i);
						data[8] = model.getValueAt(i, 4).toString();
						data[9] = unitPriceV.get(i);
						data[10] = discountV.get(i);
						data[11] = taxV.get(i);
						data[12] = surchargeV.get(i);
						data[13] = taxValueV.get(i);
						data[14] = surchargeValueV.get(i);
						data[15] = totalValueV.get(i);
						data[16] = model.getValueAt(i, 13).toString();
						data[17] = "" + DateFormatChange.StringToMysqlDate(new Date());
						data[18] = "" + timeFormat.format(cal1.getTime());
						data[19] = "" + StoreMain.userName;
						data[20] = model.getValueAt(i, 2).toString();
						data[21] = mrpPriceV.get(i);
						data[22] = PurchasePriceVV.get(i) + "";
						data[23] = null;
						data[24] = igst.get(i);
						data[25] = igstValueV.get(i);
						System.out.println(Arrays.toString(data));


						int inv_id=invoiceDBConnection.inserInvoiceItemNew(data);
						invoiceDBConnection.UpdateReceivedQtyInPO(NewInvoice.po_id, itemIDV.get(i),
								model.getValueAt(i, 4).toString());
						double tempIGST=Double.parseDouble(igst.get(i))/2;
						tempIGST = Math.round(tempIGST * 100.00) / 100.00;
						double tempIGSTV=Double.parseDouble(igstValueV.get(i))/2;
						tempIGSTV = Math.round(tempIGSTV * 100.00) / 100.00;

						data[0] = itemIDV.get(i);
						data[1] = itemNameV.get(i);
						data[2] = itemDescV.get(i);
						data[3] = model.getValueAt(i, 2).toString();
						data[4] = paidqtyV.get(i);
						data[5] = paidqtyV.get(i);
						data[6] = model.getValueAt(i, 13).toString();
						data[7] = "" + DateFormatChange.StringToMysqlDate(new Date());
						data[8] = "" + timeFormat.format(cal1.getTime());
						data[9] = "" + DateFormatChange.StringToMysqlDate(new Date());
						data[10] = PurchasePriceVV.get(i) + "";
						data[11] = mrpPriceV.get(i);
						data[12] =  (int) Float.parseFloat(measUnitV.get(i))+"";
						data[13] = taxV.get(i);
						data[14] = surchargeV.get(i);
						data[15] = taxValueV.get(i);
						data[16] = surchargeValueV.get(i);
						data[17] = unitPriceV.get(i);
						data[18] = supplierDisplaySTR;
						data[19] = unitPriceV.get(i);
						data[20] = ""+inv_id;
						data[21] = igst.get(i);
						data[22] = igstValueV.get(i);

						if(Double.parseDouble(taxV.get(i))==0) {
							data[13] = tempIGST+"";
							data[14] = tempIGST+"";
							data[15] = tempIGSTV+"";
							data[16] = tempIGSTV+"";
						}
						System.out.println(Arrays.toString(data));

						int batch_id = batchTrackingDBConnection.inserInvoiceBatchData(data);
						batchTrackingDBConnection.returnInvoiceTotalStock(itemIDV.get(i),""+paidqtyV.get(i));
						invoiceDBConnection.UpdateInvoiceBatchID(inv_id,batch_id);

					}
					invoiceDBConnection.UpdatePoStatusNew(NewInvoice.po_id);

					invoiceDBConnection.closeConnection();
					batchTrackingDBConnection.closeConnection();
					itemsDBConnection.closeConnection();	

					InvoiceDBConnection invoiceDBConnection1 = new InvoiceDBConnection();
					data[0] = supplierID;
					data[1] = supplierDisplaySTR;
					data[2] = "SUPPLIER";
					data[3] = "" + finalTotalTF.getText().toString();
					data[4] = "0";
					data[5] = "0";
					data[6] = "0";
					data[7] = DateFormatChange.StringToMysqlDate(new Date());
					data[8] = "" + timeFormat.format(cal1.getTime());
					data[9] = "" + StoreMain.userName;
					data[10] = "Invoice No : " + invoiceNoTF.getText().toString() + " Auto Inc. : " + index;

					GLAccountDBConnection glAccountDBConnection = new GLAccountDBConnection();
					glAccountDBConnection.inserGLAccountSupplier(data);
					glAccountDBConnection.closeConnection();

					JOptionPane.showMessageDialog(null, "Invoice saved successfully", "Invoice Save",
							JOptionPane.INFORMATION_MESSAGE);

					if (browser != null) {
						browser.populateTable(DateFormatChange.StringToMysqlDate(new Date()),
								DateFormatChange.StringToMysqlDate(new Date()));
					}
					new Bill_PDF(index + "","");

				} catch (Exception e1) {
					e1.printStackTrace(); 
				}  
				dispose();
			}

		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_1.setBounds(672, 581, 153, 39);
		contentPanel.add(btnNewButton_1);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(835, 581, 153, 39);
		contentPanel.add(btnCancel);

		JLabel label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon(NewInvoice.class.getResource("/icons/invoice1.png")));
		label_1.setBounds(216, 18, 320, 108);
		contentPanel.add(label_1);

		JLabel lblTax_1 = new JLabel("Tax % :");
		lblTax_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTax_1.setBounds(20, 588, 51, 14);
		contentPanel.add(lblTax_1);

		JLabel lblDiscount_1 = new JLabel("Discount :");
		lblDiscount_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDiscount_1.setBounds(156, 593, 65, 14);
		contentPanel.add(lblDiscount_1);

		finalTaxTF = new JTextField();
		finalTaxTF.setEditable(false);
		finalTaxTF.setHorizontalAlignment(SwingConstants.RIGHT);
		finalTaxTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		finalTaxTF.setBounds(73, 588, 80, 25);
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
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateFinalTaxValue();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateFinalTaxValue();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateFinalTaxValue();
			}

			private void updateFinalTaxValue() {
				try {
					String str = finalTaxTF.getText().trim();
					finalTaxValue = str.isEmpty() ? 0 : Double.parseDouble("0" + str);
				} catch (NumberFormatException ex) {
					finalTaxValue = 0; // Fallback for invalid input
				}
				finalTotal();
			}
		});

		finalTaxTF.setColumns(10);
		JLabel lblReturnAmount = new JLabel("Return Amount:");
		lblReturnAmount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblReturnAmount.setBounds(306, 593, 100, 14);
		contentPanel.add(lblReturnAmount);

		returnAmount = new JTextField();
		returnAmount.setHorizontalAlignment(SwingConstants.RIGHT);
		returnAmount.setFont(new Font("Tahoma", Font.BOLD, 14));
		returnAmount.setColumns(10);
		returnAmount.setBounds(409, 588, 77, 25);
		contentPanel.add(returnAmount);

		returnAmount.addKeyListener(new KeyAdapter() {
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
		returnAmount.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateReturnAmount();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateReturnAmount();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateReturnAmount();
			}

			private void updateReturnAmount() {
				try {
					String str = returnAmount.getText().trim();
					finalReturnAmount = str.isEmpty() ? 0 : Double.parseDouble("0" + str);
				} catch (NumberFormatException ex) {
					finalReturnAmount = 0; // Fallback for invalid input
				}
				finalTotal();
			}
		});


		finalDiscountTF = new JTextField();
		finalDiscountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		finalDiscountTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		finalDiscountTF.setColumns(10);
		finalDiscountTF.setBounds(225, 588, 71, 25);
		finalDiscountTF.addKeyListener(new KeyAdapter() {
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
		finalDiscountTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateFinalDiscountValue();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateFinalDiscountValue();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateFinalDiscountValue();
			}

			private void updateFinalDiscountValue() {
				try {
					String str = finalDiscountTF.getText().trim();
					finalDiscountValue = str.isEmpty() ? 0 : Double.parseDouble("0" + str);
				} catch (NumberFormatException ex) {
					finalDiscountValue = 0; // Fallback for invalid input
				}
				finalTotal();
			}
		});

		contentPanel.add(finalDiscountTF);

		JLabel lblTotal = new JLabel("Total :");
		lblTotal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotal.setBounds(496, 593, 80, 14);
		contentPanel.add(lblTotal);

		finalTotalTF = new JTextField();
		finalTotalTF.setHorizontalAlignment(SwingConstants.RIGHT);
		finalTotalTF.setEditable(false);
		finalTotalTF.setFont(new Font("Tahoma", Font.BOLD, 14));
		finalTotalTF.setColumns(10);
		finalTotalTF.setBounds(553, 588, 113, 25);
		contentPanel.add(finalTotalTF);

		JLabel lblTaxebleAmount = new JLabel("Taxable Amount :");
		lblTaxebleAmount.setBounds(134, 554, 124, 14);
		contentPanel.add(lblTaxebleAmount);

		taxableAmountLB = new JLabel("taxableAmountLB");
		taxableAmountLB.setBounds(270, 554, 100, 14);
		contentPanel.add(taxableAmountLB);

		taxAmountLB = new JLabel("taxableAmountLB");
		taxAmountLB.setBounds(449, 554, 100, 14);
		contentPanel.add(taxAmountLB);

		JLabel lblSaleTax = new JLabel("CGST :");
		lblSaleTax.setBounds(394, 554, 68, 14);
		contentPanel.add(lblSaleTax);

		surchargeLB = new JLabel("surchargeLB");
		surchargeLB.setBounds(651, 554, 100, 14);
		contentPanel.add(surchargeLB);

		JLabel lblSurchargeAmount = new JLabel("SGST  :");
		lblSurchargeAmount.setBounds(591, 554, 77, 14);
		contentPanel.add(lblSurchargeAmount);

		coinADJLB = new JLabel("coints");
		coinADJLB.setBounds(977, 554, 100, 14);
		contentPanel.add(coinADJLB);

		JLabel lblCoinAdj = new JLabel("Coin ADJ.  :");
		lblCoinAdj.setBounds(890, 554, 77, 14);
		contentPanel.add(lblCoinAdj);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(51, 102, 153)), 
				"Item Desc",TitledBorder.LEFT,TitledBorder.BOTTOM,new Font("Tahoma", Font.ITALIC, 10), 
				new Color(51, 102, 153)));
		panel.setBounds(10, 154, 420, 143);
		contentPanel.add(panel);
		panel.setLayout(null);

		searchItemTF = new JTextField();
		searchItemTF.setBounds(77, 12, 119, 20);
		panel.add(searchItemTF);
		searchItemTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		searchItemTF.setColumns(10);

		JLabel lblSearch = new JLabel("Search :");
		lblSearch.setBounds(22, 15, 119, 14);
		panel.add(lblSearch);
		lblSearch.setFont(new Font("Dialog", Font.PLAIN, 11));

		itemNameCB = new JComboBox();
		itemNameCB.setBounds(200, 12, 210, 20);
		panel.add(itemNameCB);
		itemNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					itemNameSTR = itemNameCB.getSelectedItem().toString();
				} catch (Exception e) {
					return;
				}
				System.out.println(itemNameCB.getSelectedIndex() + "    " + itemID.size());

				if (itemNameCB.getSelectedIndex() > -1) {
					itemIDSTR = itemID.get(itemNameCB.getSelectedIndex());
				}
				clearFields();
				getItemDetail(itemIDSTR);
			}

			private void clearFields() {
				itemDescTF.setText("");
				igstTF.setText("");
				unitPriceTF.setText("");
				mrpPriceTF.setText("");
				taxTF.setText("");
				discountTF.setText("");
				itemSurchargeTF.setText("");
				freeQtyTF.setText("");
				unitPriceCalET.setText("");
				itemIdTF.setText("");
				itemNameTF.setText("");
			}

//			private void updateFields() {
//				if (!taxTypeSTR.equals("CompanyTax")) {
//					taxTF.setText(taxValueSTR);
//				}
//				itemIdTF.setText(itemIDSTR);
//				itemNameTF.setText(itemNameSTR);
//				itemDescTF.setText(itemDescSTR);
//				unitPriceTF.setText(String.valueOf(unitPrice));
//				unitPriceCalET.setText(String.valueOf(unitPrice));
//				mrpPriceTF.setText(String.valueOf(mrp));
//				itemSurchargeTF.setText(itemSurchargeSTR);
//				igstTF.setText(itemIgstSTR);
//				measUnitET.setText(measUnitS);
//			}
		});

		itemNameCB.setFont(new Font("Tahoma", Font.BOLD, 11));

		itemNameTF = new JTextField();
		itemNameTF.setBounds(200, 41, 210, 20);
		panel.add(itemNameTF);
		itemNameTF.setFont(new Font("Dialog", Font.BOLD, 11));
		itemNameTF.setEditable(false);
		itemNameTF.setColumns(10);

		itemIdTF = new JTextField();
		itemIdTF.setBounds(90, 41, 94, 20);
		panel.add(itemIdTF);
		itemIdTF.setFont(new Font("Dialog", Font.BOLD, 11));
		itemIdTF.setEditable(false);
		itemIdTF.setColumns(10);

		JLabel lblItemId = new JLabel("Item ID :");
		lblItemId.setBounds(22, 44, 119, 14);
		panel.add(lblItemId);
		lblItemId.setFont(new Font("Dialog", Font.PLAIN, 11));


		itemDescTF = new JTextField();
		itemDescTF.setBounds(77, 71, 333, 20);
		panel.add(itemDescTF);
		itemDescTF.setEditable(false);
		itemDescTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemDescTF.setColumns(10);

		JLabel lblDesc = new JLabel("Desc :");
		lblDesc.setFont(new Font("Dialog", Font.PLAIN, 11));
		lblDesc.setBounds(21, 73, 119, 14);
		panel.add(lblDesc);

		JButton btnNewItem = new JButton("");
		btnNewItem.setBounds(298, 99, 51, 25);
		panel.add(btnNewItem);
		btnNewItem.setIcon(new ImageIcon(NewInvoice.class
				.getResource("/icons/plus_button.png")));
		btnNewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				NewItem newItem = new NewItem();
				newItem.InvoiceInstance(NewInvoice.this);
				newItem.setModal(true);
				newItem.setVisible(true);
			}
		});
		btnNewItem.setFont(new Font("Tahoma", Font.BOLD, 13));

		JButton button = new JButton("");
		button.setBounds(359, 99, 51, 25);
		panel.add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (!itemIdTF.getText().equals("")) {
					BatchBrowser editItem = new BatchBrowser(itemIdTF.getText(),
							itemNameTF.getText());
					editItem.setModal(true);
					editItem.setVisible(true);
				}else {
					JOptionPane.showMessageDialog(null,
							"Please select item first", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		button.setIcon(new ImageIcon(NewInvoice.class
				.getResource("/icons/edit_button.png")));
		button.setFont(new Font("Tahoma", Font.BOLD, 13));

		JButton btnAddChallan = new JButton("Add Challan");
		btnAddChallan.setBounds(178, 99, 114, 25);
		panel.add(btnAddChallan);
		btnAddChallan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (!supplierID.equals("")) {
					SearchChallan searchChallan = new SearchChallan(supplierID);
					searchChallan.NewInvoiceInstance(NewInvoice.this);
					searchChallan.setModal(true);
					searchChallan.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null,
							"Please select supplier first", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		btnAddChallan.setFont(new Font("Tahoma", Font.BOLD, 13));

		JButton btnAddReturnGoods = new JButton("Add Return Goods");
		btnAddReturnGoods.setBounds(25, 99, 142, 25);
		panel.add(btnAddReturnGoods);
		btnAddReturnGoods.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				AddReturnGoods addReturnGoods = new AddReturnGoods(
						supplierNameSTR, NewInvoice.this);
				addReturnGoods.setModal(true);
				addReturnGoods.setVisible(true);
			}
		});
		btnAddReturnGoods.setFont(new Font("Tahoma", Font.BOLD, 13));

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(436, 153, 695, 92);
		panel_1.setBorder(new TitledBorder(
				new LineBorder(new Color(51, 102, 153)),
				"Price Cal.", 
				TitledBorder.LEFT, 
				TitledBorder.BOTTOM, 
				new Font("Tahoma", Font.ITALIC, 10), 
				new Color(51, 102, 153)
				));

		contentPanel.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblMeasUnits = new JLabel("Pack");
		lblMeasUnits.setBounds(21, 59, 40, 14);
		panel_1.add(lblMeasUnits);
		lblMeasUnits.setFont(new Font("Dialog", Font.PLAIN, 11));
		measUnitET = new JTextField();
		measUnitET.setBounds(58, 56, 51, 20);
		panel_1.add(measUnitET);
		measUnitET.setFont(new Font("Tahoma", Font.BOLD, 11));
		measUnitET.setColumns(10);
		JLabel lblMRP = new JLabel("MRP");
		lblMRP.setBounds(114, 59, 40, 14);
		panel_1.add(lblMRP);
		lblMRP.setFont(new Font("Dialog", Font.PLAIN, 11));

		mrpPriceTF = new JTextField();
		mrpPriceTF.setBounds(145, 56, 94, 20);
		panel_1.add(mrpPriceTF);
		mrpPriceTF.setHorizontalAlignment(SwingConstants.RIGHT);
		mrpPriceTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		mrpPriceTF.setColumns(10);

		JLabel lblPoQty = new JLabel("PO Qty:");
		lblPoQty.setBounds(246, 59, 51, 14);
		panel_1.add(lblPoQty);
		lblPoQty.setFont(new Font("Dialog", Font.PLAIN, 11));

		poQtyTF = new JTextField();
		poQtyTF.setBounds(297, 56, 68, 20);
		panel_1.add(poQtyTF);
		poQtyTF.setEditable(false);
		poQtyTF.setHorizontalAlignment(SwingConstants.RIGHT);
		poQtyTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		poQtyTF.setColumns(10);

		JLabel lblDiscount_2 = new JLabel("Discount:");
		lblDiscount_2.setBounds(371, 59, 61, 14);
		panel_1.add(lblDiscount_2);
		lblDiscount_2.setFont(new Font("Dialog", Font.PLAIN, 11));


		discountTF = new JTextField();
		discountTF.setBounds(430, 57, 61, 20);
		panel_1.add(discountTF);
		discountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		discountTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		discountTF.setColumns(10);

		igstTF = new JTextField();
		igstTF.setBounds(397, 30, 61, 20);
		panel_1.add(igstTF);
		igstTF.setHorizontalAlignment(SwingConstants.RIGHT);
		igstTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		igstTF.setColumns(10);

		discountprcnTF = new JTextField();
		discountprcnTF.setBounds(492, 30, 80, 20);
		panel_1.add(discountprcnTF);
		discountprcnTF.setHorizontalAlignment(SwingConstants.RIGHT);
		discountprcnTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		discountprcnTF.setColumns(10);

		itemSurchargeTF = new JTextField();
		itemSurchargeTF.setBounds(322, 30, 61, 20);
		panel_1.add(itemSurchargeTF);
		itemSurchargeTF.setHorizontalAlignment(SwingConstants.RIGHT);
		itemSurchargeTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemSurchargeTF.setColumns(10);


		taxTF = new JTextField();
		taxTF.setBounds(243, 30, 61, 20);
		panel_1.add(taxTF);
		taxTF.setHorizontalAlignment(SwingConstants.RIGHT);
		taxTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		taxTF.setColumns(10);


		unitPriceTF = new JTextField();
		unitPriceTF.setBounds(168, 30, 65, 20);
		panel_1.add(unitPriceTF);
		unitPriceTF.setHorizontalAlignment(SwingConstants.RIGHT);
		unitPriceTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		unitPriceTF.setColumns(10);

		freeQtyTF = new JTextField();
		freeQtyTF.setBounds(87, 30, 71, 20);
		panel_1.add(freeQtyTF);
		freeQtyTF.setHorizontalAlignment(SwingConstants.RIGHT);
		freeQtyTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		freeQtyTF.setColumns(10);

		qtyTF = new JTextField();
		qtyTF.setBounds(16, 30, 61, 20);
		panel_1.add(qtyTF);
		qtyTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		qtyTF.setColumns(10);

		JLabel lblQty = new JLabel("Qty.");
		lblQty.setBounds(19, 12, 68, 14);
		panel_1.add(lblQty);
		lblQty.setFont(new Font("Dialog", Font.PLAIN, 11));


		JLabel lblFreeQty = new JLabel("Free Qty.");
		lblFreeQty.setBounds(90, 12, 68, 14);
		panel_1.add(lblFreeQty);
		lblFreeQty.setFont(new Font("Dialog", Font.PLAIN, 11));

		JLabel lblUnitPrice = new JLabel("Unit Price ");
		lblUnitPrice.setBounds(168, 12, 65, 14);
		panel_1.add(lblUnitPrice);
		lblUnitPrice.setFont(new Font("Dialog", Font.PLAIN, 11));

		JLabel lblTax = new JLabel("CGST");
		lblTax.setBounds(245, 12, 67, 14);
		panel_1.add(lblTax);
		lblTax.setFont(new Font("Dialog", Font.PLAIN, 11));


		JLabel lblSurChg = new JLabel("SGST");
		lblSurChg.setBounds(324, 12, 67, 14);
		panel_1.add(lblSurChg);
		lblSurChg.setFont(new Font("Dialog", Font.PLAIN, 11));

		JLabel lblDiscount = new JLabel("Discount(%)");
		lblDiscount.setBounds(491, 12, 96, 14);
		panel_1.add(lblDiscount);
		lblDiscount.setFont(new Font("Dialog", Font.PLAIN, 11));


		JLabel lblIgst = new JLabel("IGST");
		lblIgst.setBounds(399, 12, 67, 14);
		panel_1.add(lblIgst);
		lblIgst.setFont(new Font("Dialog", Font.PLAIN, 11));

		JLabel lblNewLabel_2 = new JLabel("Total Value :");
		lblNewLabel_2.setBounds(496, 59, 86, 14);
		panel_1.add(lblNewLabel_2);
		lblNewLabel_2.setFont(new Font("Dialog", Font.PLAIN, 11));

		totalValueTF = new JTextField();
		totalValueTF.setBounds(571, 56, 113, 20);
		panel_1.add(totalValueTF);
		totalValueTF.setEditable(false);
		totalValueTF.setHorizontalAlignment(SwingConstants.RIGHT);
		totalValueTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		totalValueTF.setColumns(10);
		unitPriceCalET = new JTextField();
		unitPriceCalET.setBounds(584, 30, 92, 20);
		panel_1.add(unitPriceCalET);
		unitPriceCalET.setHorizontalAlignment(SwingConstants.RIGHT);
		unitPriceCalET.setFont(new Font("Tahoma", Font.BOLD, 11));
		unitPriceCalET.setColumns(10);

		JLabel lblUnitPrice_1 = new JLabel("Discount U.P");
		lblUnitPrice_1.setBounds(591, 13, 101, 14);
		panel_1.add(lblUnitPrice_1);
		lblUnitPrice_1.setFont(new Font("Dialog", Font.PLAIN, 11));
		
		discountCBox = new JCheckBox("");
		discountCBox.setBounds(468, 28, 21, 21);
		panel_1.add(discountCBox);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(436, 249, 448, 48);
		panel_2.setBorder(new TitledBorder(
				new LineBorder(new Color(51, 102, 153)), 
				"Batch Details", 
				TitledBorder.LEFT,  
				TitledBorder.BOTTOM,  
				new Font("Tahoma", Font.ITALIC, 10),
				new Color(51, 102, 153) 
				));


		contentPanel.add(panel_2);
		panel_2.setLayout(null);

		JLabel lblExpireDate = new JLabel("Expire : ");
		lblExpireDate.setBounds(236, 15, 86, 14);
		panel_2.add(lblExpireDate);
		lblExpireDate.setFont(new Font("Dialog", Font.PLAIN, 11));

		expiryDateC = new JDateChooser();
		expiryDateC.setBounds(283, 12, 135, 20);
		panel_2.add(expiryDateC);
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
						if ("date".equals(arg0.getPropertyName())
								&& arg0.getNewValue() != null) {
							expiryDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
						}
					}
				});
		expiryDateC.setMinSelectableDate(new Date());
		expiryDateC.setDateFormatString("yyyy-MM-dd");

		batchNumberTF = new JTextField();
		batchNumberTF.setBounds(54, 12, 164, 20);
		panel_2.add(batchNumberTF);
		batchNumberTF.setHorizontalAlignment(SwingConstants.RIGHT);
		batchNumberTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		batchNumberTF.setColumns(10);

		JLabel lblBatchNo = new JLabel("Batch : ");
		lblBatchNo.setBounds(12, 12, 80, 14);
		panel_2.add(lblBatchNo);
		lblBatchNo.setFont(new Font("Dialog", Font.PLAIN, 11));

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new TitledBorder(
				new LineBorder(new Color(51, 102, 153)), 
				"Supplier Details", 
				TitledBorder.LEFT, 
				TitledBorder.BOTTOM, 
				new Font("Tahoma", Font.ITALIC, 10), 
				new Color(51, 102, 153) 

				));
		panel_3.setBounds(16, 5, 390, 139);
		contentPanel.add(panel_3);

		JLabel lblNewLabel_1 = new JLabel("Search Supplier :");
		lblNewLabel_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(17, 11, 126, 21);
		panel_3.add(lblNewLabel_1);

		supplierTF = new JTextField();
		supplierTF.setText("<dynamic>");
		supplierTF.setFont(new Font("Dialog", Font.PLAIN, 14));
		supplierTF.setColumns(10);
		supplierTF.setBounds(143, 11, 218, 21);
		panel_3.add(supplierTF);

		supplierTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				handleTextChange();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				handleTextChange();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				handleTextChange();
			}

			private void handleTextChange() {
				String str = supplierTF.getText();
				if (!str.isEmpty()) {
					getSupplierName(str);
				} else {
					clearFields();
				}
			}

			private void clearFields() {
				addressTF.setText("");
				mobileTF.setText("");
				supplierName.removeAllElements();
				supplierCB.setModel(supplierName);
			}
		});


		JLabel lblCredit = new JLabel("Select Supplier");
		lblCredit.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblCredit.setBounds(17, 42, 126, 21);
		panel_3.add(lblCredit);

		supplierCB = new JComboBox();
		supplierCB.setFont(new Font("Dialog", Font.PLAIN, 14));
		supplierCB.setBounds(143, 42, 218, 21);
		panel_3.add(supplierCB);
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

		JLabel lblDebit = new JLabel("Mobile :");
		lblDebit.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblDebit.setBounds(17, 75, 126, 21);
		panel_3.add(lblDebit);

		mobileTF = new JTextField();
		mobileTF.setFont(new Font("Dialog", Font.PLAIN, 14));
		mobileTF.setColumns(10);
		mobileTF.setBounds(143, 73, 218, 21);
		panel_3.add(mobileTF);

		addressTF = new JTextField();
		addressTF.setFont(new Font("Dialog", Font.PLAIN, 14));
		addressTF.setColumns(10);
		addressTF.setBounds(143, 101, 218, 21);
		panel_3.add(addressTF);

		JLabel lblBalance = new JLabel("Address :");
		lblBalance.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblBalance.setBounds(17, 101, 126, 21);
		panel_3.add(lblBalance);

		invoiceNoTF = new JTextField();
		invoiceNoTF.setFont(new Font("Dialog", Font.PLAIN, 14));
		invoiceNoTF.setColumns(10);
		invoiceNoTF.setBounds(872, 16, 218, 28);
		contentPanel.add(invoiceNoTF);

		orderNoTF = new JTextField();
		orderNoTF.setText("");
		orderNoTF.setFont(new Font("Dialog", Font.PLAIN, 14));
		orderNoTF.setColumns(10);
		orderNoTF.setBounds(872, 48, 218, 28);
		contentPanel.add(orderNoTF);

		invoiceDate = new JDateChooser();
		invoiceDate.setBounds(872, 81, 218, 21);
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
		invoiceDate.setMaxSelectableDate(new Date());

		finalamountTF = new JTextField();
		finalamountTF.setFont(new Font("Dialog", Font.PLAIN, 14));
		finalamountTF.setColumns(10);
		finalamountTF.setBounds(984, 110, 135, 21);
		contentPanel.add(finalamountTF);

		JLabel labelfinalamount = new JLabel("Invoice Amt :");
		labelfinalamount.setFont(new Font("Dialog", Font.PLAIN, 14));
		labelfinalamount.setBounds(886, 110, 106, 25);
		contentPanel.add(labelfinalamount);

		dueDate = new JDateChooser();
		dueDate.setBounds(724, 110, 135, 21);
		contentPanel.add(dueDate);

		dueDate.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dueDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());

						}
					}
				});

		JLabel lblDueDate = new JLabel("Due Date :");
		lblDueDate.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblDueDate.setBounds(645, 110, 126, 25);
		contentPanel.add(lblDueDate);

		JLabel lblInvoiceNo = new JLabel("Invoice No. :");
		lblInvoiceNo.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblInvoiceNo.setBounds(735, 15, 126, 28);
		contentPanel.add(lblInvoiceNo);

		JPanel panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBorder(new TitledBorder(new LineBorder(new Color(51, 102, 153)), "Invoice Details",TitledBorder.LEFT,
				TitledBorder.BOTTOM,new Font("Tahoma", Font.ITALIC, 10),new Color(51, 102, 153)));
		panel_4.setBounds(633, 6, 494, 139);
		contentPanel.add(panel_4);

		JLabel lblBillorderNo = new JLabel("Bill/Order No. :");
		lblBillorderNo.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblBillorderNo.setBounds(102, 45, 126, 25);
		panel_4.add(lblBillorderNo);

		JLabel lblDate = new JLabel("Bill Date :");
		lblDate.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblDate.setBounds(102, 75, 126, 25);
		panel_4.add(lblDate);

		JLabel lblMs = new JLabel("MS++");
		lblMs.setForeground(new Color(255, 99, 71));
		lblMs.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 18));
		lblMs.setBounds(545, 38, 70, 15);
		contentPanel.add(lblMs);

		JLabel lblInvoice = new JLabel(" INVOICE");
		lblInvoice.setForeground(new Color(255, 102, 153));
		lblInvoice.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 18));
		lblInvoice.setBounds(528, 61, 111, 15);
		contentPanel.add(lblInvoice);

		JLabel label = new JLabel("* * * * * * * * *");
		label.setForeground(new Color(72, 209, 204));
		label.setBounds(532, 106, 97, 14);
		contentPanel.add(label);

		JLabel lblSurchargeAmount_1 = new JLabel("IGST :");
		lblSurchargeAmount_1.setBounds(763, 555, 77, 14);
		contentPanel.add(lblSurchargeAmount_1);

		igstLBL = new JLabel("0.0");
		igstLBL.setBounds(823, 555, 100, 14);
		contentPanel.add(igstLBL);
		unitPriceCalET.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateUnitPriceCal();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateUnitPriceCal();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateUnitPriceCal();
			}

			private void updateUnitPriceCal() {
				try {
					String str = unitPriceCalET.getText().trim();
					unitPriceCal = str.isEmpty() ? 0 : Double.parseDouble(str);
				} catch (NumberFormatException ex) {
					unitPriceCal = 0; // Fallback for invalid input
				}
			}
		});
		qtyTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();					
				}
			}
		});
		qtyTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				handleQuantityChange();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				handleQuantityChange();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				handleQuantityChange();
			}

			private void handleQuantityChange() {
				try {
					String str = qtyTF.getText().trim();
					quantity = str.isEmpty() ? 0 : Integer.parseInt(str);
				} catch (NumberFormatException ex) {
					quantity = 0; // Fallback for invalid input
				}
				calculateItemDetails();
			}
		});
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
				updateFreeQuantity();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateFreeQuantity();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateFreeQuantity();
			}

			private void updateFreeQuantity() {
				try {
					String str = freeQtyTF.getText().trim();
					freeQuantity = str.isEmpty() ? 0 : Integer.parseInt(str);
				} catch (NumberFormatException ex) {
					freeQuantity = 0; // Fallback for invalid input
				}
				calculateItemDetails();
			}
		});

		unitPriceTF.addKeyListener(new KeyAdapter() {
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

		unitPriceTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateUnitPrice();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateUnitPrice();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateUnitPrice();
			}

			private void updateUnitPrice() {
				try {
					String str = unitPriceTF.getText().trim();
					unitPrice = str.isEmpty() ? 0 : Double.parseDouble(str);
				} catch (NumberFormatException ex) {
					unitPrice = 0; // Fallback for invalid input
				}
				calculateItemDetails();
			}
		});
		taxTF.addKeyListener(new KeyAdapter() {
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
		taxTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateTaxValue();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateTaxValue();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateTaxValue();
			}

			private void updateTaxValue() {
				try {
					String str = taxTF.getText().trim();
					taxValue = str.isEmpty() ? 0 : Double.parseDouble("0" + str);
				} catch (NumberFormatException ex) {
					taxValue = 0; // Fallback for invalid input
				}
				calculateItemDetails();
			}
		});
		itemSurchargeTF.addKeyListener(new KeyAdapter() {
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
		itemSurchargeTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateItemSurcharge();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateItemSurcharge();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateItemSurcharge();
			}

			private void updateItemSurcharge() {
				try {
					String str = itemSurchargeTF.getText().trim();
					itemSurcharge = str.isEmpty() ? 0 : Double.parseDouble("0" + str);
				} catch (NumberFormatException ex) {
					itemSurcharge = 0; // Fallback for invalid input
				}
				calculateItemDetails();
			}
		});


		discountprcnTF.addKeyListener(new KeyAdapter() {
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

		discountprcnTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateDiscountValue();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateDiscountValue();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateDiscountValue();
			}

			private void updateDiscountValue() {
				try {
					String str = discountprcnTF.getText().trim();
					discountPrcntValue = str.isEmpty() ? 0 : Double.parseDouble("0" + str);
				} catch (NumberFormatException ex) {
					discountPrcntValue = 0; // Fallback for invalid input
				}
				calculateItemDetails();
			}
		});

		igstTF.addKeyListener(new KeyAdapter() {
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
		igstTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateItemIgst();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateItemIgst();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateItemIgst();
			}

			private void updateItemIgst() {
				try {
					String str = igstTF.getText().trim();
					itemIgst = str.isEmpty() ? 0 : Double.parseDouble("0" + str);
				} catch (NumberFormatException ex) {
					itemIgst = 0; // Fallback for invalid input
				}
				calculateItemDetails();
			}
		});
		mrpPriceTF.addKeyListener(new KeyAdapter() {
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
		mrpPriceTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateMrpPrice();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateMrpPrice();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateMrpPrice();
			}

			private void updateMrpPrice() {
				try {
					String str = mrpPriceTF.getText().trim();
					mrpPrice = str.isEmpty() ? 0 : Double.parseDouble(str);
				} catch (NumberFormatException ex) {
					mrpPrice = 0; // Fallback for invalid input
				}
			}
		});
		searchItemTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				handleTextChange();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				handleTextChange();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				handleTextChange();
			}

			private void handleTextChange() {
				String str = searchItemTF.getText().trim();
				if (!str.isEmpty()) {
					getItemName(str);
				} else {
					clearFields();
				}
			}
		});
		supplierTF.setText(VendorName);
		orderNoTF.setText(PONumber);
		getPOItems();
		loadDataToTable();
	}

	public void addItemLine() {
		if (itemDescTF.getText().toString().equals("")) {
			JOptionPane.showMessageDialog(null, "Please select item",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (qtyTF.getText().toString().equals("") ) {
			JOptionPane.showMessageDialog(null, "Please enter quantity",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (unitPriceTF.getText().toString().equals("")) {
			JOptionPane.showMessageDialog(null,
					"Please select item or enter unit price", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		} else if (totalValueTF.getText().toString().equals("")) {
			JOptionPane.showMessageDialog(null, "Please enter final value",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(!checkItemDetailChange(itemIdTF.getText())) {
			return;
		}
		itemIDV.add(itemIdTF.getText());
		itemNameV.add(itemNameTF.getText());
		itemDescV.add(itemDescTF.getText());
		PurchasePriceVV.add(df.format(unitPriceCal)+"");
		measUnitV.add(measUnitET.getText().toString());
		batchNumberV.add(batchNumberTF.getText().toString().toUpperCase());
		if (poQtyTF.getText().toString().equals("")) {
			qtyV.add("" + 0);
		} else {
			qtyV.add(poQtyTF.getText().toString()); 
		}
		freeqtyV.add("" +freeQuantity);
		paidqtyV.add("" + quantity);
		unitPriceV.add(unitPrice + "");
		mrpPriceV.add(mrpPriceTF.getText().toString() + "");
		taxV.add(taxValue + "");
		surchargeV.add(itemSurcharge + "");
		igst.add(itemIgst + "");
		taxValueV.add(taxAmountValue + "");
		surchargeValueV.add(surchargeAmountValue + "");
		igstValueV.add(igstAmountValue + "");
		discountV.add(discountValue + "");
		discountPerV.add(discountPrcntValue+"");
		totalValueV.add(totalValueTF.getText() + "");
		expiryDateV.add(expiryDateSTR);
		addStock.add(true);
		searchItemTF.setText("");
		searchItemTF.requestFocusInWindow();
		expiryDateC.setCalendar(null);
		loadDataToTable();
		clearFields();
	}


	public void getSupplierName(String index) {

		SuppliersDBConnection suppliersDBConnection = new SuppliersDBConnection();
		ResultSet resultSet = suppliersDBConnection.searchSupplierWithId1(index);
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
				supplierStateCode=resultSet.getString("supplier_state_code");
				/***after some this version***/
				if(supplierStateCode.equals("6")) {
					taxTF.setEnabled(true);
					itemSurchargeTF.setEnabled(true);
					igstTF.setEnabled(false);
				}else {
					taxTF.setEnabled(false);
					itemSurchargeTF.setEnabled(false);
					igstTF.setEnabled(true);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		suppliersDBConnection.closeConnection();
	}

	public void getItemName(String index) {

		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.searchItemWithIdNew(index);
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

	public boolean checkItemDetailChange(String index) {
		boolean isCgstSgst=supplierStateCode.equals("6")?true:false;//arun
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail(index);
		List<Double> list = new ArrayList<>();
		List<Double> list2 = new ArrayList<>();
		try {
			list2.add(parseDouble(unitPriceTF.getText()));
			list2.add(parseDouble(mrpPriceTF.getText()));
			list2.add(parseDouble(measUnitET.getText()));
			if(isCgstSgst) {
				list2.add(parseDouble(itemSurchargeTF.getText()));
				list2.add(parseDouble(taxTF.getText()));
				list2.add(0.0);
			}else {
				list2.add(0.0);
				list2.add(0.0);
				list2.add(parseDouble(igstTF.getText()));}

			while (resultSet.next()) {
				list.add(parseDouble(resultSet.getObject(7).toString()));
				list.add(parseDouble(resultSet.getObject(11).toString()));
				list.add(parseDouble(resultSet.getObject(4).toString()));
				if(isCgstSgst) {
					list.add(parseDouble(resultSet.getObject(12).toString()));
					list.add(parseDouble(resultSet.getObject(6).toString()));
					list.add(0.0);
				}else {
					list.add(0.0);
					list.add(0.0);
					list.add(parseDouble(resultSet.getObject(13).toString()));
				}
			}
			itemsDBConnection.closeConnection();
			System.out.println(isCgstSgst+"Index : "+(Arrays.toString(list.toArray()) +"----"+Arrays.toString(list2.toArray())));

			List<Integer> changedIndexlist=  findDifferencesUsingSets(list, list2);
			if(changedIndexlist.size()>0) {
				TextFieldsDialog TFD=new TextFieldsDialog(changedIndexlist,list2);
				TFD.setModal(true);
				TFD.setVisible(true); 
				if(TFD.getFinalStatus()) {
					unitPriceTF.setText(""+TFD.getUnitPriceTF().getText());
					mrpPriceTF.setText(""+TFD.getMrpTF().getText());
					itemSurchargeTF.setText(""+TFD.getSgstTF().getText());
					igstTF.setText(""+TFD.getIgstTF().getText());
					measUnitET.setText(""+TFD.getPckTF().getText());
					taxTF.setText(""+TFD.getCgstTF().getText());
					updateItemPriceData(itemIdTF.getText(),isCgstSgst);
					return true;
				}else {
					return false;
				}
			}else {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return false;
	}

	private double parseDouble(String value) {
		try {
			return Double.parseDouble(value.trim())+0;
		} catch (NumberFormatException e) {
			return 0.0; 
		}
	}


	public void getItemDetail(String index) {
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail(index);
		try {
			while (resultSet.next()) {
				itemIdTF.setText(itemIDSTR);
				itemNameTF.setText(itemNameSTR);
				itemDescTF.setText(resultSet.getObject(3).toString());
				unitPriceTF.setText(String.valueOf(Double.parseDouble("0"+ resultSet.getObject(7).toString())));
				unitPriceCalET.setText(String.valueOf(Double.parseDouble("0"+ resultSet.getObject(7).toString())));
				mrpPriceTF.setText(String.valueOf(Double.parseDouble("0"+ resultSet.getObject(11).toString())));
				measUnitET.setText(resultSet.getObject(4).toString());
				if(supplierStateCode.equals("6")) {
					itemSurchargeTF.setText(resultSet.getObject(12).toString());
					igstTF.setText("0");
					taxTF.setText(resultSet.getObject(6).toString());
				}else {
					itemSurchargeTF.setText("0");
					igstTF.setText(resultSet.getObject(13).toString());
					taxTF.setText("0");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
	}

	public void calculateItemDetails() {
		paidQuantity = quantity - freeQuantity;
		itemValue = paidQuantity * unitPrice;

		double discount = Math.round(itemValue * (discountPrcntValue / 100.0f) * 100.00) / 100.00;
		discountValue = discount;
		discountTF.setText(discountValue + "");
		discountTF.setEnabled(false);

		double tax = Math.round((itemValue - discount) * (taxValue / 100.0f) * 100.00) / 100.00;
		double surcharge = Math.round((itemValue - discount) * (itemSurcharge / 100.0f) * 100.00) / 100.00;
		double igst = Math.round(itemValue * (itemIgst / 100.0f) * 100.00) / 100.00;

		itemValue = Math.round((itemValue - discount + tax + surcharge + igst) * 100.00) / 100.00;

		taxAmountValue = tax;
		surchargeAmountValue = surcharge;
		igstAmountValue = igst;

		double discountPerUnit = discount / paidQuantity;

		double actualUnitPrice = unitPrice - discountPerUnit;
		double purchasePrice = Math.round((actualUnitPrice * paidQuantity / quantity) * 100.00) / 100.00;

		if (purchasePrice == 0.0) {
			purchasePrice = actualUnitPrice;
		}

		unitPriceCalET.setText("" + purchasePrice);
		unitPriceCal = purchasePrice;
		totalValueTF.setText("" + itemValue);
	}

	public void finalTotal() {
		double total = 0;
		total = finalTotalValue - finalDiscountValue;
		total = total - finalReturnAmount;
		double k = total * (finalTaxValue / 100.0f);
		total = total + k;

		total = Math.round(total * 100.000) / 100.000;
		if (total - Math.floor(total) > 0.5) {
			finalTotalTF.setText("" + Math.ceil(total));
			finalTotalValueCoin = Math.ceil(total) - total;
		} else {
			finalTotalTF.setText("" + Math.floor(total));
			finalTotalValueCoin = Math.floor(total) - total;
		}
		finalTotalValueCoin = Math.round(finalTotalValueCoin * 100.000) / 100.000;
		System.out.print(total + "  " + finalTotalValueCoin);
		coinADJLB.setText("" + (finalTotalValueCoin));

	}

	private void loadDataToTable() {
		int size = itemIDV.size();
		double total = 0;
		double taxable = 0, tax = 0, surcharge = 0,igstVar=0;
		ObjectArray_ListOfexamsSpecs = new Object[size][17];

		for (int i = 0; i < itemIDV.size(); i++) {
			ObjectArray_ListOfexamsSpecs[i][0] = itemIDV.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = itemNameV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = batchNumberV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = qtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = paidqtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = freeqtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][6] = unitPriceV.get(i);
			ObjectArray_ListOfexamsSpecs[i][7] = taxV.get(i) + "("+ taxValueV.get(i) + ")";
			ObjectArray_ListOfexamsSpecs[i][8] = surchargeV.get(i) + "("+ surchargeValueV.get(i) + ")";
			ObjectArray_ListOfexamsSpecs[i][9] = igst.get(i) + "("+ igstValueV.get(i) + ")";
			ObjectArray_ListOfexamsSpecs[i][10] = discountV.get(i);
			double amountWT = (Double.parseDouble(paidqtyV.get(i)) - Double.parseDouble(freeqtyV.get(i)))
					* Double.parseDouble(unitPriceV.get(i));
			amountWT = Math.round(amountWT * 100.00) / 100.00;
			ObjectArray_ListOfexamsSpecs[i][11] = amountWT;
			ObjectArray_ListOfexamsSpecs[i][12] = totalValueV.get(i);
			ObjectArray_ListOfexamsSpecs[i][13] = expiryDateV.get(i);
			ObjectArray_ListOfexamsSpecs[i][14] = mrpPriceV.get(i);
			ObjectArray_ListOfexamsSpecs[i][15] = measUnitV.get(i);
			ObjectArray_ListOfexamsSpecs[i][16] = PurchasePriceVV.get(i);
			total = total + Double.parseDouble(totalValueV.get(i));
			taxable = taxable + amountWT;
			tax = tax + Double.parseDouble(taxValueV.get(i));
			surcharge = surcharge + Double.parseDouble(surchargeValueV.get(i));
			igstVar = igstVar + Double.parseDouble(igstValueV.get(i));

		}
		final boolean[] canEdit = new boolean[] { false, false, false, false,
				false, false, false, false, false, false,false, false, false, false,false,false,false };
		DefaultTableModel model = new DefaultTableModel(
				ObjectArray_ListOfexamsSpecs, new String[] { "Item ID",
						"Item Name", "Item Batch.", "PO Qty.", "Recieved Qty.",
						"Free Qty", "Unit Price", "CGST", "SGST","IGST", "Discount",
						"Amount(W/T)", "Value", "Expiry", "MRP", "Pack Size","New Unit Price" }) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return canEdit[column];
			}
		};
		table.setModel(model);

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
		table.getColumnModel().getColumn(11).setPreferredWidth(100);
		table.getColumnModel().getColumn(11).setMinWidth(90);
		table.getColumnModel().getColumn(11).setCellEditor(new JDateChooserCellEditor(new JCheckBox()));
		table.getColumnModel().getColumn(12).setPreferredWidth(100);
		table.getColumnModel().getColumn(12).setMinWidth(90);

		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						int selectedRowIndex = table.getSelectedRow();
						int selectedColumnIndex = table.getSelectedColumn();
					}
				});

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
		igstVar = Math.round(igstVar * 100.00) / 100.00;
		finalTotalTF.setText("" + finalTotalValue);
		taxable = Math.round(taxable * 100.00) / 100.00;
		finalTotal();
		taxableAmountLB.setText("" + taxable);
		taxAmountLB.setText("" + tax);
		surchargeLB.setText("" + surcharge);
		igstLBL.setText("" + igstVar);
	}

	public void updateItemPriceData(String ItemId,boolean isCgstSgst) {
		ItemsDBConnection db = new ItemsDBConnection();
		String[] data = new String[6];
		data[0] = unitPriceTF.getText();
		data[1] = mrpPriceTF.getText();
		data[2] = itemSurchargeTF.getText();
		data[3] = igstTF.getText();
		data[4] = (int) Float.parseFloat(measUnitET.getText())+"";;
		data[5] = taxTF.getText();
		try {
			db.updateItemPriceData(ItemId,data,isCgstSgst);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			db.closeConnection();
			e.printStackTrace();
		}
		db.closeConnection();
	}

	public void addReturnGoods(String[] itemsID) {
		ReturnInvoiceDBConnection returnInvoiceDBConnection = new ReturnInvoiceDBConnection();
		for (int i = 0; i < itemsID.length; i++) {
			ResultSet resultSet = returnInvoiceDBConnection.retrieveReturenedGoodByid(itemsID[i]);
			try {
				while (resultSet.next()) { 
					returnitemID.add(itemsID[i]);
					itemIDV.add(resultSet.getObject(1).toString());
					itemNameV.add(resultSet.getObject(2).toString());
					itemDescV.add(resultSet.getObject(3).toString());
					measUnitV.add("NA");
					qtyV.add("0");
					freeqtyV.add(resultSet.getObject(4).toString());
					paidqtyV.add("-" + resultSet.getObject(4).toString());
					unitPriceV.add(resultSet.getObject(5).toString());
					discountV.add(resultSet.getObject(6).toString());
					taxV.add(resultSet.getObject(7).toString()); 
					surchargeV.add(resultSet.getObject(8).toString());
					taxValueV.add("-" + resultSet.getObject(9).toString() + "");
					surchargeValueV.add("-"+ resultSet.getObject(10).toString());
					totalValueV.add("-" + resultSet.getObject(11).toString());
					expiryDateV.add(resultSet.getObject(12).toString());
					batchNumberV.add(resultSet.getObject(13).toString());
					returnGoodBatch.add(resultSet.getObject(13).toString());
					discountPerV.add("0");
					addStock.add(false);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		returnInvoiceDBConnection.closeConnection();
		loadDataToTable();
	}

	public void getPOItems() {
		PODBConnection db = new PODBConnection();
		ResultSet rs = db.retrievePOItemDataNew(po_id);
		try {
			while (rs.next()) {   
				itemIDV.add(rs.getObject(1).toString());
				itemNameV.add(rs.getObject(2).toString());
				itemDescV.add(rs.getObject(3).toString());
				measUnitV.add(rs.getObject(4).toString());
				qtyV.add(rs.getInt(5)+"");
				freeqtyV.add(rs.getObject(6).toString()); 
				paidqtyV.add(rs.getInt(5)+"");
				unitPriceV.add(rs.getObject(7).toString());
				discountV.add(rs.getObject(8).toString());
				discountPerV.add("0");
				taxV.add(rs.getObject(9).toString()); 
				surchargeV.add(rs.getObject(10).toString());
				taxValueV.add(rs.getObject(11).toString() + "");
				surchargeValueV.add( rs.getObject(12).toString());
				totalValueV.add(rs.getObject(13).toString());
				expiryDateV.add(rs.getObject(14).toString());
				batchNumberV.add(rs.getObject(15).toString());
				PurchasePriceVV.add("0");
				mrpPriceV.add(rs.getObject(17).toString());
				igst.add(rs.getObject(18).toString());
				igstValueV.add(rs.getObject(19).toString()); 
				addStock.add(false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		db.closeConnection();
		loadDataToTable();
	}


	public void searchChallan(String searchChallan, String supplier_id) {
		ChallanDBConnection challanDBConnection = new ChallanDBConnection();
		ResultSet resultSet = challanDBConnection.retrieveChallan(searchChallan, supplier_id);
		try {
			while (resultSet.next()) {
				itemIDV.add(resultSet.getObject(1).toString());
				itemNameV.add(resultSet.getObject(2).toString());
				itemDescV.add(resultSet.getObject(3).toString());
				measUnitV.add(resultSet.getObject(4).toString());
				qtyV.add(resultSet.getObject(5).toString());
				freeqtyV.add(resultSet.getObject(6).toString());
				paidqtyV.add(resultSet.getObject(7).toString());
				unitPriceV.add(resultSet.getObject(8).toString());
				discountV.add(resultSet.getObject(9).toString());
				taxV.add(resultSet.getObject(10).toString());
				surchargeV.add(resultSet.getObject(11).toString());
				taxValueV.add(resultSet.getObject(12).toString() + "");
				surchargeValueV.add(resultSet.getObject(13).toString());
				totalValueV.add(resultSet.getObject(14).toString());
				expiryDateV.add(resultSet.getObject(15).toString());
				batchNumberV.add(resultSet.getObject(17).toString());
				challanBatch.add(resultSet.getObject(17).toString());
				challanItemID.add(resultSet.getObject(18).toString());
				addStock.add(false);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		challanDBConnection.closeConnection();
		loadDataToTable();
	}
	private void clearFields() {
		itemIdTF.setText("");
		itemNameTF.setText("");
		itemDescTF.setText("");
		unitPriceTF.setText("");
		mrpPriceTF.setText("");
		taxTF.setText("");
		igstTF.setText("");
		itemSurchargeTF.setText("");
		measUnitET.setText("");
		discountTF.setText("");
		unitPriceCalET.setText("");
		if(!discountCBox.isSelected())
		  discountprcnTF.setText("");
		itemName.removeAllElements();
		itemNameCB.setModel(itemName);
		qtyTF.setText("");
		freeQtyTF.setText("");
		batchNumberTF.setText("");
		poQtyTF.setText("");
		expiryDateC.setDate(null);
		expiryDateSTR = "";
	}
	public void invoiceBrowser(InvoiceBrowser browser) {
		this.browser = browser;
	}

	public JTextField getIgstTF() {
		return igstTF;
	}

	public static List<Integer> findDifferencesUsingSets(List<Double> list1, List<Double> list2) {
		List<Integer> differingIndices = new ArrayList<>();
		for (int i = 0; i < list1.size(); i++) {
			if (!list1.get(i).equals(list2.get(i))) {
				differingIndices.add(i); 
			}
		}
		return differingIndices;
	}
}
