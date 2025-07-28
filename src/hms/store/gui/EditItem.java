package hms.store.gui;

import hms.admin.gui.AdminMain;
import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.store.database.ItemsDBConnection;
import hms.store.database.SuppliersDBConnection;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.SwingConstants;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.toedter.calendar.JDateChooser;

public class EditItem extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField itemNameTF;
	private JTextField descTF;
	private JTextField brandNameTF;
	JComboBox ItemTypeCB;
	private JTextField hsnCodeTF;
	private JTextField totalStockTF;
	private JTextField sellingPriceTF;
	private JTextField purchasePriceTF;
	private JTextField mrpTF_1;
	private JTextField minUnitTF;
	private JTextField cgstTF;
	private JCheckBox chckbxInpatientAccomadation;
	private JComboBox subCategoryCB;
	private JRadioButton rdbtnInventory;
	private JCheckBox chckbxLaboratoryTest;
	private JCheckBox chckbxTreatment;
	private JCheckBox chckbxIsActive;
	private JCheckBox chckbxImagingTest;
	private JCheckBox chckbxSurgeryRelated;
	private JComboBox itemUnitCB;
	private JCheckBox chckbxDrug;
	private JRadioButton rdbtnServiceItem;
	private JCheckBox chckbxUseInMedic;
	private JLabel lblItemType;
	String itemNameSTR,descSTR,brandSTR,hsnCodeSTR,totalStockSTR,sellingPriceSTR,
	purchasePriceSTR,mrpSTR,minUnitSTR,cgstSTR,sgstSTR,igstSTR,subCategorySTR="",
	mainCategorySTR="",measUnitSTR="Item Unit",itemTypeSTR
	,taxTypeSTR,expiryDateSTR,medicineLocationSTR,createdBy="";
	ButtonGroup itemType = new ButtonGroup();
	ButtonGroup tax = new ButtonGroup();
	private JDateChooser expiryDate;
	DateFormatChange dateFormat = new DateFormatChange();
	String itemID;
	String previousepriceSTR="0";
	private JTextField medicineLocationTB;
	private JTextField sgstTF;
	private JLabel label_2;
	private JLabel lblItemTax;
	private JLabel lblItemSurcharge;

	private JTextField igstEditText;
	private JTextField itemCodeTF;
	private JLabel lblItemCode;
	private JTextField reoderingLevelTF;
	private JLabel label_1;
	private JLabel label_3;
	private JTextField maximumUnitsTF;
	private JPanel panel_7;
	private JComboBox vendor1CB;
	private JLabel label_5;
	private JComboBox vendor2CB;
	private JLabel label_6;
	private JComboBox vendor3CB;
	private JLabel label_7;
	private JComboBox vendor4CB;
	private JLabel label_8;
	final DefaultComboBoxModel supplierName1 = new DefaultComboBoxModel();
	final DefaultComboBoxModel supplierName2 = new DefaultComboBoxModel();
	final DefaultComboBoxModel supplierName3 = new DefaultComboBoxModel();
	final DefaultComboBoxModel supplierName4 = new DefaultComboBoxModel();
	
	final DefaultComboBoxModel mainCategory = new DefaultComboBoxModel();
	final DefaultComboBoxModel subCategory = new DefaultComboBoxModel();
	private JLabel lblMainCategory;
	private JComboBox mainCategoryCB;
	private JLabel lblItemSalt;
	private JTextField itemSaltNameTF;
	private JLabel lblItemStatus;
	OPDBillEntry OPDBillEntry=null;
	NewInvoice newInvoice=null;
	NewChallan newChallan=null;
	private JLabel label_9;
	private JComboBox doctorcb;
	final DefaultComboBoxModel drName = new DefaultComboBoxModel();
	private JLabel itemUserEntryTF;
	private JCheckBox chckbxFormulaActive;
	private JComboBox formulaCB;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AdminMain.update_item_access="1";
			EditItem dialog = new EditItem("3547");
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EditItem(String ID) {
		setResizable(false);
		setTitle("Update Item");
		itemID=ID;
		
		setBounds(100, 100, 1027, 553);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Item Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(17, 0, 343, 252);
		contentPanel.add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblName.setBounds(22, 49, 103, 20);
		panel_2.add(lblName);
		
		itemNameTF = new JTextField();
		itemNameTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		itemNameTF.setBounds(159, 47, 174, 25);
		panel_2.add(itemNameTF);
		itemNameTF.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Description :");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel.setBounds(22, 119, 103, 20);
		panel_2.add(lblNewLabel);
		
		descTF = new JTextField();
		descTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		descTF.setBounds(159, 117, 174, 25);
		panel_2.add(descTF);
		descTF.setColumns(10);
		
		JLabel lblBrandName = new JLabel("Brand Name :");
		lblBrandName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblBrandName.setBounds(22, 187, 103, 20);
		panel_2.add(lblBrandName);
		
		brandNameTF = new JTextField();
		brandNameTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		brandNameTF.setBounds(159, 185, 174, 25);
		panel_2.add(brandNameTF);
		brandNameTF.setColumns(10);
		
		JLabel lblHsnCode = new JLabel("HSN Code :");
		lblHsnCode.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblHsnCode.setBounds(22, 218, 103, 20);
		panel_2.add(lblHsnCode);
		
		hsnCodeTF = new JTextField();
		hsnCodeTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		hsnCodeTF.setBounds(159, 216, 174, 25);
		panel_2.add(hsnCodeTF);
		hsnCodeTF.setColumns(10);
		
		chckbxIsActive = new JCheckBox("Is Active");
		chckbxIsActive.setSelected(true);
		chckbxIsActive.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxIsActive.setBounds(160, 17, 21, 23);
		panel_2.add(chckbxIsActive);
		
		itemCodeTF = new JTextField();
		itemCodeTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		itemCodeTF.setColumns(10);
		itemCodeTF.setBounds(159, 83, 174, 25);
		panel_2.add(itemCodeTF);
		
		lblItemCode = new JLabel("Item Code");
		lblItemCode.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblItemCode.setBounds(22, 85, 103, 20);
		panel_2.add(lblItemCode);
		
		lblItemSalt = new JLabel("Item Salt :");
		lblItemSalt.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblItemSalt.setBounds(22, 153, 103, 20);
		panel_2.add(lblItemSalt);
		
		itemSaltNameTF = new JTextField();
		itemSaltNameTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		itemSaltNameTF.setColumns(10);
		itemSaltNameTF.setBounds(159, 151, 174, 25);
		panel_2.add(itemSaltNameTF);
		
		lblItemStatus = new JLabel("Item Status");
		lblItemStatus.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblItemStatus.setBounds(22, 18, 103, 20);
		panel_2.add(lblItemStatus);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Medical Type", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(17, 263, 343, 125);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		chckbxTreatment = new JCheckBox("Treatment");
		chckbxTreatment.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxTreatment.setBounds(9, 16, 140, 23);
		panel.add(chckbxTreatment);
		
		chckbxLaboratoryTest = new JCheckBox("Laboratory Test");
		chckbxLaboratoryTest.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxLaboratoryTest.setBounds(172, 16, 143, 23);
		panel.add(chckbxLaboratoryTest);
		
		chckbxSurgeryRelated = new JCheckBox("Surgery Related");
		chckbxSurgeryRelated.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxSurgeryRelated.setBounds(9, 42, 143, 23);
		panel.add(chckbxSurgeryRelated);
		
		chckbxImagingTest = new JCheckBox("Imaging Test");
		chckbxImagingTest.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxImagingTest.setBounds(172, 42, 143, 23);
		panel.add(chckbxImagingTest);
		
		chckbxDrug = new JCheckBox("Drug");
		chckbxDrug.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxDrug.setBounds(9, 68, 140, 23);
		panel.add(chckbxDrug);
		
		chckbxInpatientAccomadation = new JCheckBox("Inpatient Accomadation");
		chckbxInpatientAccomadation.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxInpatientAccomadation.setBounds(172, 68, 159, 23);
		panel.add(chckbxInpatientAccomadation);
		
		chckbxUseInMedic = new JCheckBox("Use in medic income");
		chckbxUseInMedic.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxUseInMedic.setBounds(9, 94, 159, 23);
		panel.add(chckbxUseInMedic);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(17, 399, 343, 115);
		contentPanel.add(panel_3);
		panel_3.setLayout(null);
		
		JLabel lblCategory = new JLabel("Sub Category :");
		lblCategory.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblCategory.setBounds(16, 46, 108, 20);
		panel_3.add(lblCategory);
		
		subCategoryCB = new JComboBox();
		subCategoryCB.setEditable(true);
		subCategoryCB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		subCategoryCB.setBounds(159, 44, 174, 25);
		panel_3.add(subCategoryCB);
		
		subCategoryCB.setEditable(true);
		subCategoryCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
					subCategorySTR = (String) subCategoryCB.getSelectedItem();
					
			}
		});
		final JTextField tfListText = (JTextField) subCategoryCB.getEditor()
				.getEditorComponent();
		tfListText.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				String text = tfListText.getText();
				if (!text.equals("")) {

					subCategorySTR = text;
					
				}
			}
		});

		JLabel lblMeasUnits = new JLabel("Pack Size :");
		lblMeasUnits.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblMeasUnits.setBounds(16, 79, 108, 20);
		panel_3.add(lblMeasUnits);
		
		itemUnitCB = new JComboBox();
		itemUnitCB.setModel(new DefaultComboBoxModel(new String[] {""}));
		itemUnitCB.setEditable(true);
		itemUnitCB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		itemUnitCB.setBounds(159, 77, 174, 25);
		panel_3.add(itemUnitCB);
		
		itemUnitCB.setEditable(true);
		itemUnitCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			
					measUnitSTR = (String) itemUnitCB.getSelectedItem();
			}
		});
		final JTextField tfListText1 = (JTextField) itemUnitCB.getEditor()
				.getEditorComponent();
		tfListText.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				String text = tfListText1.getText();
				measUnitSTR = text;
			}
		});
		itemUnitCB.setSelectedIndex(0);
	
		lblMainCategory = new JLabel("Main Category :");
		lblMainCategory.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblMainCategory.setBounds(16, 13, 108, 20);
		panel_3.add(lblMainCategory);
		
		mainCategoryCB = new JComboBox();
		mainCategoryCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainCategorySTR = (String) mainCategoryCB.getSelectedItem();
					
			}
		});
		final JTextField mainCategoryListText = (JTextField) mainCategoryCB.getEditor()
				.getEditorComponent();
		mainCategoryListText.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				String text = mainCategoryListText.getText();
				if (!text.equals("")) {

					mainCategorySTR = text;
					
				}
			}
		});
		mainCategoryCB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		mainCategoryCB.setEditable(true);
		mainCategoryCB.setBounds(159, 11, 174, 25);
		panel_3.add(mainCategoryCB);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Type", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(722, 11, 288, 51);
		contentPanel.add(panel_1);
		panel_1.setLayout(null);
		
		rdbtnInventory = new JRadioButton("Inventory Item");
		rdbtnInventory.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rdbtnInventory.setBounds(6, 16, 134, 23);
		panel_1.add(rdbtnInventory);
		itemType.add(rdbtnInventory);
		
		rdbtnServiceItem = new JRadioButton("Service Item");
		rdbtnServiceItem.setSelected(true);
		rdbtnServiceItem.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rdbtnServiceItem.setBounds(155, 16, 169, 23);
		panel_1.add(rdbtnServiceItem);
		itemType.add(rdbtnServiceItem);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Item Tax", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setBounds(370, 70, 343, 108);
		contentPanel.add(panel_4);
		panel_4.setLayout(null);
		
		cgstTF = new JTextField();
		cgstTF.setHorizontalAlignment(SwingConstants.RIGHT);
		cgstTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		cgstTF.setBounds(148, 11, 86, 25);
		panel_4.add(cgstTF);
		cgstTF.setColumns(10);
		cgstTF.addKeyListener(new KeyAdapter() {
            @Override
			public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)
                        || (vChar == KeyEvent.VK_DELETE)||vChar== '.')) {
                    e.consume();
                    
                    //||vChar== '.'
                }
            }
        });
		
		JLabel label = new JLabel("%");
		label.setFont(new Font("Tahoma", Font.BOLD, 13));
		label.setBounds(240, 16, 46, 14);
		panel_4.add(label);
		
		sgstTF = new JTextField();
		sgstTF.setHorizontalAlignment(SwingConstants.RIGHT);
		sgstTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		sgstTF.setColumns(10);
		sgstTF.setBounds(148, 42, 86, 25);
		panel_4.add(sgstTF);
		
		label_2 = new JLabel("%");
		label_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_2.setBounds(240, 47, 46, 14);
		panel_4.add(label_2);
		
		lblItemTax = new JLabel("CGST :");
		lblItemTax.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblItemTax.setBounds(10, 17, 115, 20);
		panel_4.add(lblItemTax);
		
		lblItemSurcharge = new JLabel("SGST :");
		lblItemSurcharge.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblItemSurcharge.setBounds(10, 48, 115, 20);
		panel_4.add(lblItemSurcharge);
		
		JLabel lblIgst = new JLabel("IGST :");
		lblIgst.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblIgst.setBounds(10, 78, 115, 20);
		panel_4.add(lblIgst);
		
		igstEditText = new JTextField();
		igstEditText.setHorizontalAlignment(SwingConstants.RIGHT);
		igstEditText.setFont(new Font("Tahoma", Font.BOLD, 13));
		igstEditText.setColumns(10);
		igstEditText.setBounds(148, 72, 86, 25);
		panel_4.add(igstEditText);
		
		JLabel label_4 = new JLabel("%");
		label_4.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_4.setBounds(240, 77, 46, 14);
		panel_4.add(label_4);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_5.setBounds(370, 181, 343, 297);
		contentPanel.add(panel_5);
		panel_5.setLayout(null);
		
		JLabel lblTotalStock = new JLabel("Total Stock :");
		lblTotalStock.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblTotalStock.setBounds(16, 13, 115, 20);
		panel_5.add(lblTotalStock);
		
		totalStockTF = new JTextField();
		totalStockTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		totalStockTF.setBounds(159, 11, 174, 25);
		panel_5.add(totalStockTF);
		totalStockTF.setColumns(10);
		totalStockTF.addKeyListener(new KeyAdapter() {
            @Override
			public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)
                        || (vChar == KeyEvent.VK_DELETE))) {
                    e.consume();
                    
                    //||vChar== '.'
                }
            }
        });
		
		JLabel lblSellingPrice = new JLabel("Selling Price :");
		lblSellingPrice.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblSellingPrice.setBounds(16, 54, 115, 20);
		panel_5.add(lblSellingPrice);
		
		sellingPriceTF = new JTextField();
		sellingPriceTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		sellingPriceTF.setBounds(159, 52, 174, 25);
		panel_5.add(sellingPriceTF);
		sellingPriceTF.setColumns(10);
		sellingPriceTF.addKeyListener(new KeyAdapter() {
            @Override
			public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)|| (vChar == KeyEvent.VK_MINUS)
                        || (vChar == KeyEvent.VK_DELETE)||vChar== '.')) {
                    e.consume();
                    
                    //||vChar== '.'
                }
            }
        });
//		
		purchasePriceTF = new JTextField();
		purchasePriceTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		purchasePriceTF.setBounds(159, 83, 174, 25);
		panel_5.add(purchasePriceTF);
		purchasePriceTF.setColumns(10);
		purchasePriceTF.addKeyListener(new KeyAdapter() {
            @Override
			public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)|| (vChar == KeyEvent.VK_MINUS)
                        || (vChar == KeyEvent.VK_DELETE)||vChar== '.')) {
                    e.consume();
                    
                    //||vChar== '.'
                }
            }
        });
		
		
		JLabel lblPurchasePrice = new JLabel("Purchase Price :");
		lblPurchasePrice.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPurchasePrice.setBounds(16, 85, 115, 20);
		panel_5.add(lblPurchasePrice);
		
		JLabel mrpTF = new JLabel("MRP  :");
		mrpTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		mrpTF.setBounds(16, 179, 115, 20);
		panel_5.add(mrpTF);
//		mrpTF.setVisible(false);
		
		mrpTF_1 = new JTextField();
		mrpTF_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		mrpTF_1.setBounds(159, 177, 174, 25);
		panel_5.add(mrpTF_1);
		mrpTF_1.setColumns(10);
		mrpTF_1.addKeyListener(new KeyAdapter() {
            @Override
			public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)|| (vChar == KeyEvent.VK_MINUS)
                        || (vChar == KeyEvent.VK_DELETE)||vChar== '.')) {
                    e.consume();
                    
                    //||vChar== '.'
                }
            }
        });
//		mrpTF.setVisible(false);
//		
		JLabel minimumUnitsTF = new JLabel("Minimum Units :");
		minimumUnitsTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		minimumUnitsTF.setBounds(16, 117, 115, 20);
		panel_5.add(minimumUnitsTF);
		
		minUnitTF = new JTextField();
		minUnitTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		minUnitTF.setBounds(159, 112, 174, 25);
		panel_5.add(minUnitTF);
		minUnitTF.setColumns(10);
		minUnitTF.addKeyListener(new KeyAdapter() {
            @Override
			public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)
                        || (vChar == KeyEvent.VK_DELETE))) {
                    e.consume();
                    
                    //||vChar== '.'
                }
            }
        });
		
		expiryDate = new JDateChooser();
		expiryDate.setBounds(159, 261, 175, 25);
		panel_5.add(expiryDate);
		
		expiryDate.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-mrpated method stub
						if ("date".equals(arg0.getPropertyName())) {
							expiryDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
						}
					}
				});
		expiryDate.setMinSelectableDate(new Date());
		expiryDate.setDateFormatString("yyyy-MM-dd");
		
		JLabel lblExpiryDate = new JLabel("Expiry Date :");
		lblExpiryDate.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblExpiryDate.setBounds(16, 266, 115, 20);
		panel_5.add(lblExpiryDate);
		
		reoderingLevelTF = new JTextField();
		reoderingLevelTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		reoderingLevelTF.setColumns(10);
		reoderingLevelTF.setBounds(159, 220, 174, 25);
		panel_5.add(reoderingLevelTF);
		
		label_1 = new JLabel("Reordering Level :");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label_1.setBounds(16, 222, 115, 20);
		panel_5.add(label_1);
		
		label_3 = new JLabel("Maximum Units :");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label_3.setBounds(16, 148, 115, 20);
		panel_5.add(label_3);
		
		maximumUnitsTF = new JTextField();
		maximumUnitsTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		maximumUnitsTF.setColumns(10);
		maximumUnitsTF.setBounds(159, 141, 174, 25);
		panel_5.add(maximumUnitsTF);
		
		JButton btnNewButton = new JButton("Update");
		if(AdminMain.update_item_access.equals("1"))
			btnNewButton.setEnabled(true);
		else
			btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				itemNameSTR=itemNameTF.getText().toString().toUpperCase();
				descSTR=descTF.getText().toString();
				brandSTR=brandNameTF.getText().toString();
				hsnCodeSTR=hsnCodeTF.getText().toString();
				totalStockSTR=totalStockTF.getText().toString();
				sellingPriceSTR=sellingPriceTF.getText().toString();
				purchasePriceSTR=purchasePriceTF.getText().toString();
				mrpSTR=mrpTF_1.getText().toString();
				minUnitSTR=minUnitTF.getText().toString();
				cgstSTR=cgstTF.getText().toString();
				sgstSTR=sgstTF.getText().toString();
				igstSTR=igstEditText.getText().toString();
				medicineLocationSTR=medicineLocationTB.getText().toString();
				if(rdbtnInventory.isSelected())
				{
					itemTypeSTR="Inventory";
				}
				else {
					itemTypeSTR="Service";
				}
			
					taxTypeSTR="ItemTax";

				if(medicineLocationSTR.equals("")||itemNameSTR.equals("")||descSTR.equals("")||brandNameTF.equals("")||hsnCodeSTR.equals("")||minUnitSTR.equals("")||totalStockSTR.equals("")||mainCategorySTR.equals("")||subCategorySTR.equals("")||measUnitSTR.equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"Please enter value in fields", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}else if(!chckbxTreatment.isSelected()&&!chckbxDrug.isSelected()&&!chckbxImagingTest.isSelected()&&!chckbxInpatientAccomadation.isSelected()&&!chckbxLaboratoryTest.isSelected()&&!chckbxSurgeryRelated.isSelected()&&!chckbxUseInMedic.isSelected()){
					
					JOptionPane.showMessageDialog(null,
							"Please select atleast one medical type", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(doctorcb.getSelectedItem().toString().equals("Select Doctor")){
					JOptionPane.showMessageDialog(null,
							"Please select Doctor Name", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				else {
					if(sgstSTR.equals("")||cgstSTR.equals("")||igstSTR.equals(""))
					{
						JOptionPane.showMessageDialog(null,
								"Please enter tax value", "Input Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(expiryDateSTR.equals(""))
					{
						JOptionPane.showMessageDialog(null,
								"Please select expiry date", "Input Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(!previousepriceSTR.equals(purchasePriceSTR))
					{
						updatePrice();
					}
					String str=chckbxTreatment.isSelected() ? "Yes" : "No";
					long timeInMillis = System.currentTimeMillis();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(timeInMillis);
					SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
					String[] data=new String[42];
					
					data[0]=itemNameSTR;
					data[1]=itemCodeTF.getText().toString();
					data[2]=descSTR;
					data[3]=brandSTR;
					data[4]=hsnCodeSTR;
					data[5]=chckbxTreatment.isSelected() ? "Yes" : "No";
					data[6]=chckbxLaboratoryTest.isSelected() ? "Yes" : "No";
					data[7]=chckbxSurgeryRelated.isSelected() ? "Yes" : "No";
					data[8]=chckbxDrug.isSelected() ? "Yes" : "No";
					data[9]=chckbxImagingTest.isSelected() ? "Yes" : "No";
					data[10]=chckbxInpatientAccomadation.isSelected() ? "Yes" : "No";
					data[11]=chckbxUseInMedic.isSelected() ? "Yes" : "No";
					data[12]=mainCategorySTR;
					data[13]=subCategorySTR;
					data[14]=measUnitSTR;
					data[15]=itemTypeSTR;
					data[16]=taxTypeSTR;
					data[17]=cgstSTR;
					data[18]=sgstSTR;
					data[19]=igstSTR;
					data[20]=purchasePriceSTR;
					data[21]=sellingPriceSTR;
					data[22]=totalStockSTR;
					data[23]=mrpSTR;
					data[24]=minUnitSTR;
					data[25]=chckbxIsActive.isSelected() ? "Yes" : "No";
					data[26]=expiryDateSTR;
					data[27]=DateFormatChange.StringToMysqlDate(new Date());
					data[28]=""+timeFormat.format(cal1.getTime());
					data[29]=""+StoreMain.userName;
					data[30]=""+medicineLocationSTR;
					data[31]=""+maximumUnitsTF.getText().toString();
					data[32]=""+reoderingLevelTF.getText().toString();
					data[33]=""+itemSaltNameTF.getText().toString();
					data[34]=""+(vendor1CB.getSelectedIndex()==0 ? "" : vendor1CB.getSelectedItem() );
					data[35]=""+(vendor2CB.getSelectedIndex()==0 ? "" : vendor2CB.getSelectedItem() );
					data[36]=""+(vendor3CB.getSelectedIndex()==0 ? "" : vendor3CB.getSelectedItem() );
					data[37]=""+(vendor4CB.getSelectedIndex()==0 ? "" : vendor4CB.getSelectedItem());
				
					data[38]=ItemTypeCB.getSelectedItem()+"";
					data[39]=doctorcb.getSelectedItem()+"";
					data[40]=formulaCB.getSelectedIndex()+"";
					data[41]=itemID;
				
					ItemsDBConnection itemsDBConnection=new ItemsDBConnection();
					
					try {
						itemsDBConnection.updateData(data);
					} catch (Exception e) {
						// TODO Auto-mrpated catch block
						e.printStackTrace();
					}
					
					itemsDBConnection.closeConnection();
					JOptionPane.showMessageDialog(null,
							"Item Updated Successfully", "Update Item",
							JOptionPane.INFORMATION_MESSAGE);
					dispose();
					
					if(OPDBillEntry!=null)
					{
						OPDBillEntry.searchItemTF.setText(itemID);
					}
					if(newInvoice!=null)
					{
						newInvoice.searchItemTF.setText(itemID);
					}
					if(newChallan!=null)
					{
						newChallan.searchItemTF.setText(itemID);
					}
				
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(664, 477, 168, 37);
		contentPanel.add(btnNewButton);
		if(AdminMain.update_item_access.equals("1")){
			btnNewButton.setEnabled(true);
		}
		else{
			btnNewButton.setEnabled(false);
		}
//		if(StoreMain.userID.equals("5") && AdminMain.userID.equals("5")){
//			btnNewButton.setEnabled(true);
//		}else{
//			btnNewButton.setEnabled(false);
//		}
//		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnCancel.setBounds(842, 477, 168, 37);
		contentPanel.add(btnCancel);
		
		JPanel panel_6 = new JPanel();
		panel_6.setLayout(null);
		panel_6.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_6.setBounds(370, 11, 334, 46);
		contentPanel.add(panel_6);
		
		JLabel lblItemLocation = new JLabel("Item Location:");
		lblItemLocation.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblItemLocation.setBounds(10, 13, 131, 20);
		panel_6.add(lblItemLocation);
		
		medicineLocationTB = new JTextField();
		medicineLocationTB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		medicineLocationTB.setColumns(10);
		medicineLocationTB.setBounds(151, 11, 173, 25);
		panel_6.add(medicineLocationTB);
		
		panel_7 = new JPanel();
		panel_7.setLayout(null);
		panel_7.setBorder(new TitledBorder(null, "PREFERED VENDORS", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_7.setBounds(718, 73, 292, 392);
		contentPanel.add(panel_7);
		
		vendor1CB = new JComboBox();
	//	vendor1CB.setSelectedIndex(0);
		vendor1CB.setFont(new Font("Tahoma", Font.BOLD, 11));
		vendor1CB.setBounds(31, 37, 239, 20);
		panel_7.add(vendor1CB);
		
		label_5 = new JLabel("Vendor 1");
		label_5.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_5.setBounds(31, 21, 89, 14);
		panel_7.add(label_5);
		
		vendor2CB = new JComboBox();
		//vendor2CB.setSelectedIndex(0);
		vendor2CB.setFont(new Font("Tahoma", Font.BOLD, 11));
		vendor2CB.setBounds(31, 93, 239, 20);
		panel_7.add(vendor2CB);
		
		label_6 = new JLabel("Vendor 2");
		label_6.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_6.setBounds(31, 68, 89, 14);
		panel_7.add(label_6);
		
		vendor3CB = new JComboBox();
		//vendor3CB.setSelectedIndex(0);
		vendor3CB.setFont(new Font("Tahoma", Font.BOLD, 11));
		vendor3CB.setBounds(31, 149, 239, 20);
		panel_7.add(vendor3CB);
		
		label_7 = new JLabel("Vendor 3");
		label_7.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_7.setBounds(31, 124, 89, 14);
		panel_7.add(label_7);
		
		vendor4CB = new JComboBox();
		//vendor4CB.setSelectedIndex(0);
		vendor4CB.setFont(new Font("Tahoma", Font.BOLD, 11));
		vendor4CB.setBounds(31, 211, 239, 20);
		panel_7.add(vendor4CB);
		
		label_8 = new JLabel("Vendor 4");
		label_8.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_8.setBounds(31, 186, 89, 14);
		panel_7.add(label_8);
		lblItemType = new JLabel("Item Type");
		lblItemType.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblItemType.setBounds(31, 242, 89, 14);
		panel_7.add(lblItemType);
		
		 ItemTypeCB = new JComboBox();
//		ItemTypeCB.setSelectedIndex(0);
		ItemTypeCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		ItemTypeCB.setBounds(31, 263, 239, 20);
		ItemTypeCB.setModel(new DefaultComboBoxModel(new String[] { "General",
				"High Risk", "SHC-H1"}));
		ItemTypeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_7.add(ItemTypeCB);
		
		label_9 = new JLabel("Doctor Name");
		label_9.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_9.setBounds(31, 294, 89, 14);
		panel_7.add(label_9);
		
		doctorcb = new JComboBox();
//		doctorcb.setSelectedIndex(0);
		doctorcb.setFont(new Font("Tahoma", Font.BOLD, 11));
		doctorcb.setBounds(31, 315, 239, 20);
		panel_7.add(doctorcb);
		
	
		
		formulaCB = new JComboBox(new String[] {"Yes","No"});
		formulaCB.setSelectedIndex(0);
		formulaCB.setFont(new Font("Dialog", Font.BOLD, 11));
		formulaCB.setBounds(31, 362, 239, 20);
		panel_7.add(formulaCB);
		
		JLabel label_9_1 = new JLabel("Formula Active");
		label_9_1.setFont(new Font("Dialog", Font.BOLD, 11));
		label_9_1.setBounds(31, 341, 100, 14);
		panel_7.add(label_9_1);
		if(AdminMain.update_item_access.equals("1"))
			formulaCB.setEnabled(true);
		else
			formulaCB.setEnabled(false);
		
		itemUserEntryTF = new JLabel("");
		itemUserEntryTF.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		itemUserEntryTF.setBounds(402, 494, 237, 20);
		contentPanel.add(itemUserEntryTF);
		getSupplierName();
		getDoctorName();
		getAllMainCategories();
		getAllSubCategories();
		getItemDetail(itemID);
	}
	public void getDoctorName() {

		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveData();
		drName.removeAllElements();
		drName.addElement("Select Doctor");
		
	
		int i = 0;
		try {
			while (resultSet.next()) {
				drName.addElement(resultSet.getObject(1).toString());
			
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		doctorcb.setModel(drName);
		
	
		doctorcb.setSelectedIndex(0);
	
		
	}
	public void getItemDetail(String id) {
		
		ItemsDBConnection dbConnection = new ItemsDBConnection();
		ResultSet resultSet = dbConnection.retrieveItemDetail(id);
		try {
			while (resultSet.next()) {
			
				itemNameTF.setText(resultSet.getObject(1).toString());
				descTF.setText(resultSet.getObject(2).toString());
				brandNameTF.setText(resultSet.getObject(3).toString());
				hsnCodeTF.setText(resultSet.getObject(4).toString());
				chckbxTreatment.setSelected(resultSet.getObject(5).toString().equals("Yes") ? true : false);
				chckbxLaboratoryTest.setSelected(resultSet.getObject(6).toString().equals("Yes") ? true : false);
				chckbxSurgeryRelated.setSelected(resultSet.getObject(7).toString().equals("Yes") ? true : false);
				chckbxDrug.setSelected(resultSet.getObject(8).toString().equals("Yes") ? true : false);
				chckbxImagingTest.setSelected(resultSet.getObject(9).toString().equals("Yes") ? true : false);
				chckbxInpatientAccomadation.setSelected(resultSet.getObject(10).toString().equals("Yes") ? true : false);
				chckbxUseInMedic.setSelected(resultSet.getObject(11).toString().equals("Yes") ? true : false);
				mainCategoryCB.setSelectedItem(resultSet.getObject(12).toString());
				subCategoryCB.setSelectedItem(resultSet.getObject(13).toString());
				itemUnitCB.setSelectedItem(resultSet.getObject(14).toString());
				rdbtnInventory.setSelected(resultSet.getObject(15).toString().equals("Inventory") ? true : false);
				//rdbtnUseCampanyTax.setSelected(resultSet.getObject(15).toString().equals("CompanyTax") ? true : false);
				cgstTF.setText(resultSet.getObject(17).toString());
				sgstTF.setText(resultSet.getObject(18).toString());
				previousepriceSTR=resultSet.getObject(19).toString();
				purchasePriceTF.setText(previousepriceSTR);
				sellingPriceTF.setText(resultSet.getObject(20).toString());
				totalStockTF.setText(resultSet.getObject(21).toString());
				mrpTF_1.setText(resultSet.getObject(22).toString());
				minUnitTF.setText(resultSet.getObject(23).toString());
				chckbxIsActive.setSelected(resultSet.getObject(24).toString().equals("Yes") ? true : false);
				expiryDate.setDate(DateFormatChange.StringToDate(resultSet
						.getObject(25).toString()));
				
				medicineLocationTB.setText(resultSet.getObject(29).toString());
				igstEditText.setText(resultSet.getObject(30).toString());
				itemCodeTF.setText(resultSet.getObject(31).toString());
				maximumUnitsTF.setText(resultSet.getObject(32).toString());
				reoderingLevelTF.setText(resultSet.getObject(33).toString());
				vendor1CB.setSelectedItem(resultSet.getObject(34).toString().toString());
				vendor2CB.setSelectedItem(resultSet.getObject(35).toString().toString());
				vendor3CB.setSelectedItem(resultSet.getObject(36).toString().toString());
				vendor4CB.setSelectedItem(resultSet.getObject(37).toString().toString());
				itemSaltNameTF.setText(resultSet.getObject(38).toString());
				ItemTypeCB.setSelectedItem(resultSet.getObject(39).toString());
//				if(resultSet.getObject(40).toString().equals("")){
//					
//				}
				doctorcb.setSelectedItem(resultSet.getObject(40).toString());
				itemUserEntryTF.setText("Item created by "+resultSet.getObject(41).toString().toUpperCase());
				formulaCB.setSelectedIndex(resultSet.getInt(42));
			}
		} catch (SQLException e) {
			// TODO Auto-mrpated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
	public void updatePrice()
	{
		ItemsDBConnection db=new ItemsDBConnection();
		try {
			db.updateprice(itemID, purchasePriceSTR+"", previousepriceSTR+"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			db.closeConnection();
			e.printStackTrace();
		}
		db.closeConnection();
	}
	
	
	
	public void getSupplierName() {

		SuppliersDBConnection suppliersDBConnection = new SuppliersDBConnection();
		ResultSet resultSet = suppliersDBConnection
				.retrieveAllData2();
		supplierName1.removeAllElements();
		supplierName1.addElement("Select Vendor 1");
		supplierName2.removeAllElements();
		supplierName2.addElement("Select Vendor 2");
		supplierName3.removeAllElements();
		supplierName3.addElement("Select Vendor 3");
		supplierName4.removeAllElements();
		supplierName4.addElement("Select Vendor 4");
		int i = 0;
		try {
			while (resultSet.next()) {
				supplierName1.addElement(resultSet.getObject(2).toString());
				supplierName2.addElement(resultSet.getObject(2).toString());
				supplierName3.addElement(resultSet.getObject(2).toString());
				supplierName4.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		suppliersDBConnection.closeConnection();
		vendor1CB.setModel(supplierName1);
		vendor2CB.setModel(supplierName2);
		vendor3CB.setModel(supplierName3);
		vendor4CB.setModel(supplierName4);
	
		vendor1CB.setSelectedIndex(0);
		vendor2CB.setSelectedIndex(0);
		vendor3CB.setSelectedIndex(0);
		vendor4CB.setSelectedIndex(0);
		
	}
	public void getAllMainCategories() {
		ItemsDBConnection dbConnection = new ItemsDBConnection();
		ResultSet resultSet = dbConnection.retrieveMainCategories();

		mainCategory.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {
				
				mainCategory.addElement(resultSet.getObject(1).toString());
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		mainCategoryCB.setModel(mainCategory);
		if (i > 0) {
			mainCategoryCB.setSelectedIndex(0);
		}
	}
	public void getAllSubCategories() {
		ItemsDBConnection dbConnection = new ItemsDBConnection();
		ResultSet resultSet = dbConnection.retrieveSubCategories();
		subCategory.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {
				
				subCategory.addElement(resultSet.getObject(1).toString());
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		subCategoryCB.setModel(subCategory);
		if (i > 0) {
			subCategoryCB.setSelectedIndex(0);
		}
	}
	public void newInvoiceInstatnce(NewInvoice newInvoice)
	{
		this.newInvoice=newInvoice;
	}
	public void newBillInstatnce(OPDBillEntry OPDBillEntry)
	{
		this.OPDBillEntry=OPDBillEntry;
	}
	
	public void ChallanInstance(NewChallan newChallan)
	{
		this.newChallan=newChallan;
	}
}
