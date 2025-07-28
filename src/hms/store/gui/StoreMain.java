package hms.store.gui;

import hms.admin.gui.ActiveItemsStatusReport;
import hms.admin.gui.AdminMain;
import hms.doc.scanning.DocScanning;
import hms.main.AboutHMS;
import hms.main.MainLoginMedicalStore;
import hms.patient.slippdf.DateSelection;
import hms.reportstable.ApprovedList;
import hms.reportstable.ExcessStockReport;
import hms.reportstable.NewPurchaseOrderReport;
import hms.store.database.StoreAccountDBConnection;
//import hms.transferstock.gui.NewIssuedForm;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import hms.main.RealTimeClock;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StoreMain extends JFrame {
	private DocScanning docScanning;
	private JPanel contentPane;
	static String OS;
	public static String id;
	public static String userName="";
	public static String userID="";
	public static String item_master_update_access="";
	public static String update_item_access="";
	public static String collection_report_access="";


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					StoreMain frame = new StoreMain("nidhi","sukhpal","2","1","0");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the frame.
	 * @param update_item_access 
	 */
	public StoreMain(final String storeID,String storename,final String userID,String collection_report_access_1, String update_item_access ) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				StoreAccountDBConnection storeAccountDBConnection = new StoreAccountDBConnection();
				ResultSet rs=storeAccountDBConnection.saveActivity(StoreMain.userName,StoreMain.userID,"Store");

				try {
					while(rs.next()) {
						id=rs.getObject(1).toString();
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				storeAccountDBConnection.closeConnection();
			}
			@Override
			public void windowClosing(WindowEvent e) {
				StoreAccountDBConnection storeAccountDBConnection = new StoreAccountDBConnection();
				storeAccountDBConnection.closeActivity(id);


				storeAccountDBConnection.closeConnection();
			}
		});

		OS=System.getProperty("os.name").toLowerCase();

		StoreMain.collection_report_access=collection_report_access_1;
		StoreMain.userName=storename;
		StoreMain.userID=userID;
		StoreMain.update_item_access=update_item_access;
		//		StoreMain.update_item_access=update_item_access;
		setTitle(userName);
		setIconImage(Toolkit.getDefaultToolkit().getImage(StoreMain.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		if(isWindows())
		{
			width-=240;
			height-=180;
		}else
		{

			width-=20; 
			height-=60;
		}
		setBounds(10, 10, width, height);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("My Account");
		mnNewMenu.setFont(new Font("Tahoma", Font.BOLD, 15));
		mnNewMenu.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/author.png")));
		menuBar.add(mnNewMenu);

		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				MainLoginMedicalStore mainLogin=new MainLoginMedicalStore();
				mainLogin.setVisible(true);
				dispose();
			}
		});

		JMenuItem mntmChangePassword = new JMenuItem("Change Password");
		mntmChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StoreSettings settings=new StoreSettings(storeID);
				settings.setVisible(true);
				settings.setModal(true);
			}
		});
		mntmChangePassword.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnNewMenu.add(mntmChangePassword);
		mntmLogout.setFont(new Font("Tahoma", Font.BOLD, 14));
		mntmLogout.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/exits.png")));
		mnNewMenu.add(mntmLogout);

		JMenuItem mntmAboutHms = new JMenuItem("About HMS");
		mntmAboutHms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AboutHMS aboutHMS=new AboutHMS();
				aboutHMS.setVisible(true);
				aboutHMS.setModal(true);
			}
		});
		mntmAboutHms.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnNewMenu.add(mntmAboutHms);

		JMenu mnOutdoorPatient = new JMenu("Suppliers");
		if(StoreMain.update_item_access.equals("1"))
			mnOutdoorPatient.setEnabled(true);
		else 
			mnOutdoorPatient.setEnabled(false);		
		mnOutdoorPatient.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/officer.png")));
		mnOutdoorPatient.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnOutdoorPatient);

		JMenuItem mntmNewSupplier = new JMenuItem("New Supplier");
		mntmNewSupplier.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewSupplier newSupplier=new NewSupplier();
				newSupplier.setModal(true);
				newSupplier.setVisible(true);
			}
		});
		mntmNewSupplier.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatient.add(mntmNewSupplier);

		JMenuItem mntmEditSupplier = new JMenuItem("Edit Supplier");
		mntmEditSupplier.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SupplierBrowser supplierBrowser=new SupplierBrowser();
				supplierBrowser.setModal(true);
				supplierBrowser.setVisible(true);
			}
		});
		mntmEditSupplier.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatient.add(mntmEditSupplier);

		JMenu mnNewMenu_1 = new JMenu("Supplier Payments");
		mnNewMenu_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatient.add(mnNewMenu_1);

		JMenuItem mntmNewMenuItem = new JMenuItem("New Payment");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SupplierPayments payment=new SupplierPayments();
				payment.setModal(true);
				payment.setVisible(true);

			}
		});
		mntmNewMenuItem.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnNewMenu_1.add(mntmNewMenuItem);

		JMenuItem mntmSupplierPayment = new JMenuItem("Payments Manager");
		mnNewMenu_1.add(mntmSupplierPayment);
		mntmSupplierPayment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				SupplierGLPayments glPayments=new SupplierGLPayments();
				glPayments.setModal(true);
				glPayments.setVisible(true);
			}
		});
		mntmSupplierPayment.setFont(new Font("Tahoma", Font.BOLD, 14));

		JMenuItem mntmSupplierInformation = new JMenuItem("Supplier Information");
		mntmSupplierInformation.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatient.add(mntmSupplierInformation);

		JMenu mnInsuranceHead = new JMenu("Insurance Head");
		if(StoreMain.update_item_access.equals("1"))
			mnInsuranceHead.setEnabled(true);
		else {
			mnInsuranceHead.setEnabled(false);
		}

		mnInsuranceHead.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/EXPENSE.PNG")));
		mnInsuranceHead.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnInsuranceHead);

		JMenuItem mntmNewPayment = new JMenuItem("New Payment");
		mntmNewPayment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				InsurancePayment insurancePayment=new InsurancePayment();
				insurancePayment.setModal(true);
				insurancePayment.setVisible(true);
			}
		});
		mntmNewPayment.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnInsuranceHead.add(mntmNewPayment);

		JMenuItem mntmManage = new JMenuItem("Payment Manager");
		mntmManage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				InsuranceGLPayments glPayments=new InsuranceGLPayments();
				glPayments.setModal(true);
				glPayments.setVisible(true);
			}
		});
		mntmManage.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnInsuranceHead.add(mntmManage);

		//		JMenu mnItems = new JMenu("Items");
		//		mnItems.setEnabled(false);
		//		mnItems.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/restore.gif")));
		//		mnItems.setFont(new Font("Tahoma", Font.BOLD, 15));
		//		menuBar.add(mnItems);
		//		
		//		JMenuItem mntmNewItem = new JMenuItem("New Item");
		//		mntmNewItem.addActionListener(new ActionListener() {
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				NewItem newItem=new NewItem();
		//				newItem.setModal(true);
		//				newItem.setVisible(true);
		//			}
		//		});
		//		mntmNewItem.setFont(new Font("Tahoma", Font.BOLD, 14));
		//		mnItems.add(mntmNewItem);
		//		
		//		JMenuItem mntmManageItems = new JMenuItem("Manage Items");
		//		mntmManageItems.addActionListener(new ActionListener() {
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				
		//				ItemBrowser itemBrowser=new ItemBrowser();
		//				itemBrowser.setModal(true);
		//				itemBrowser.setVisible(true);
		//			}
		//		});
		//		mntmManageItems.setFont(new Font("Tahoma", Font.BOLD, 14));
		//		mnItems.add(mntmManageItems);

		JMenu mnReports = new JMenu("Reports");
		mnReports.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/list_dialog.png")));
		mnReports.setFont(new Font("Tahoma", Font.BOLD, 15));
		if(collection_report_access.equals("1")){
			menuBar.add(mnReports);
		}else{
			//menuBar.add(mnReports);
		}


		JMenuItem mntmItemsDetail = new JMenuItem("Items Detail");
		mntmItemsDetail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {


			}
		});

		JMenuItem mntmSummerReport = new JMenuItem("Summery Report");
		mntmSummerReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				DateSelection dateSelection=new DateSelection(4);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
			}
		});
		mntmSummerReport.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmSummerReport);

		JMenuItem mntmCancelReport = new JMenuItem("Cancel Report");
		mntmCancelReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		mntmCancelReport.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmCancelReport);
		mntmItemsDetail.setFont(new Font("Tahoma", Font.BOLD, 14));
		//		mnReports.add(mntmItemsDetail);

		JMenuItem mntmPurchaseOrder = new JMenuItem("Purchase Order");
		mntmPurchaseOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DateSelection dateSelection=new DateSelection(2);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
			}
		});
		mntmPurchaseOrder.setFont(new Font("Tahoma", Font.BOLD, 14));
		//		mnReports.add(mntmPurchaseOrder);

		JMenuItem mntmInvoiceDetails = new JMenuItem("Invoice Items Reports");
		mntmInvoiceDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {


				DateSelection dateSelection=new DateSelection(3);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
			}
		});
		mntmInvoiceDetails.setFont(new Font("Tahoma", Font.BOLD, 14));
		//		mnReports.add(mntmInvoiceDetails);

		JMenu mnInvoice = new JMenu("Excess Stock");
		mnInvoice.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/invoice.png")));
		mnInvoice.setFont(new Font("Tahoma", Font.BOLD, 15));
		//		menuBar.add(mnInvoice);

		JMenuItem mntmNewInvoice = new JMenuItem("Excess Stock");
		mnInvoice.add(mntmNewInvoice);
		mntmNewInvoice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ExcessStockReport excessStockReport=new ExcessStockReport();
				excessStockReport.setModal(true);
				excessStockReport.setVisible(true);
			}
		});
		mntmNewInvoice.setFont(new Font("Tahoma", Font.BOLD, 14));

		JMenu mnCancelRecieptReq = new JMenu("Cancel Reciept");
		mnCancelRecieptReq.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/CANCEL.PNG")));
		mnCancelRecieptReq.setFont(new Font("Dialog", Font.BOLD, 15));
		menuBar.add(mnCancelRecieptReq);

		JMenuItem mntmCancelBillRequest = new JMenuItem("OPD Cancel Bill Request");
		mntmCancelBillRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OPDReturnBillForm OPDReturnBillForm = new OPDReturnBillForm();
				OPDReturnBillForm.setVisible(true);
				OPDReturnBillForm.setModal(true);
			}
		});
		mntmCancelBillRequest.setFont(new Font("Dialog", Font.BOLD, 14));
		mnCancelRecieptReq.add(mntmCancelBillRequest);

		JMenuItem mntmIpdCancelBill = new JMenuItem("IPD Cancel Bill Request");
		mntmIpdCancelBill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IPDReturnBillForm  IPDReturnBillForm=new IPDReturnBillForm();
				IPDReturnBillForm.setVisible(true);	
				IPDReturnBillForm.setModal(true);
			}
		});
		mntmIpdCancelBill.setFont(new Font("Dialog", Font.BOLD, 14));
		mnCancelRecieptReq.add(mntmIpdCancelBill);

		JMenu mnPo = new JMenu("Indent");
		if(StoreMain.update_item_access.equals("1"))
			mnPo.setEnabled(true);
		else {
			mnPo.setEnabled(false);
		}

		mnPo.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/categories.png")));
		mnPo.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnPo);

		JMenuItem mntmPurchaseOrder_1 = new JMenuItem("Automatic Indent Report");
		mntmPurchaseOrder_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewPurchaseOrderReport order=new NewPurchaseOrderReport();
				order.setModal(true);
				order.setVisible(true);
			}
		});
		mntmPurchaseOrder_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPo.add(mntmPurchaseOrder_1);
		mnPo.setVisible(false);

		JMenu mnItemIssue = new JMenu("Billing");
		mnItemIssue.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/PRODUCT.PNG")));
		mnItemIssue.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnItemIssue);

		JMenuItem mntmNewIssueForm = new JMenuItem("Billing");
		mnItemIssue.add(mntmNewIssueForm);
		mntmNewIssueForm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				BillBrowser newIssuedForm=new BillBrowser(StoreMain.this,StoreMain.userID,"store");
				//				newIssuedForm.setModal(true);
				newIssuedForm.setVisible(true);
			}
		});
		mntmNewIssueForm.setFont(new Font("Tahoma", Font.BOLD, 14));

		mntmNewIssueForm = new JMenuItem("Slip Scanner");
		mnItemIssue.add(mntmNewIssueForm);
		mntmNewIssueForm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showDocScanning(StoreMain.this);
			}
		});
		mntmNewIssueForm.setFont(new Font("Tahoma", Font.BOLD, 14));

		JMenuItem mntmIssueToDept = new JMenuItem("Issue To Dept.");
		mntmIssueToDept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//				NewIssuedForm dialog = new NewIssuedForm();
				//				dialog.setVisible(true);
				BillBrowserPillsReq dialog = new BillBrowserPillsReq(StoreMain.this,StoreMain.userID,"store");
				dialog.setVisible(true);
			}
		});
		mntmIssueToDept.setFont(new Font("Dialog", Font.BOLD, 14));
		mnItemIssue.add(mntmIssueToDept);

		JMenuItem mntmReturnFromDept = new JMenuItem("Return From Dept.");
		mntmReturnFromDept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewReturnForm dialog = new NewReturnForm();
				dialog.setVisible(true);
			}
		});
		mntmReturnFromDept.setFont(new Font("Dialog", Font.BOLD, 14));
		mnItemIssue.add(mntmReturnFromDept);
		JMenu mnInvoice1 = new JMenu("Invoice");
		if(StoreMain.update_item_access.equals("1"))
			mnInvoice1.setEnabled(true);
		else {
			mnInvoice1.setEnabled(false);
		}

		mnInvoice1.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/PRODUCT.PNG")));
		mnInvoice1.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnInvoice1);

		JMenu mnPurchaseInvoice = new JMenu("Purchase Invoice");
		mnPurchaseInvoice.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/PRODUCT.PNG")));
		mnPurchaseInvoice.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnInvoice1.add(mnPurchaseInvoice);

		JMenuItem mntmNewInvoice1 = new JMenuItem("Manage Invoice");
		mntmNewInvoice1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("manage");
				InvoiceBrowser newInvoice=new InvoiceBrowser();
				newInvoice.setModal(true);
				newInvoice.setVisible(true);
			}
		});
		mntmNewInvoice1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPurchaseInvoice.add(mntmNewInvoice1);

		JMenu mnChallanEntry = new JMenu("Challan Entry");
		mnChallanEntry.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnInvoice1.add(mnChallanEntry);

		JMenuItem mntmNewChallan = new JMenuItem("New Challan");
		mntmNewChallan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewChallan newChallan=new NewChallan();
				newChallan.setModal(true);
				newChallan.setVisible(true);
			}
		});
		mntmNewChallan.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnChallanEntry.add(mntmNewChallan);

		JMenu mnReturnInvoice = new JMenu("Return Invoice");
		mnReturnInvoice.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnInvoice1.add(mnReturnInvoice);

		JMenuItem mntmNewInvoice_1 = new JMenuItem("New Invoice");
		mntmNewInvoice_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewReturnItemsForm newReturnInvoice=new NewReturnItemsForm();
				newReturnInvoice.setModal(true);
				newReturnInvoice.setVisible(true);
			}
		});
		mntmNewInvoice_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReturnInvoice.add(mntmNewInvoice_1);
		JMenuItem mntmNewInvoice_12 = new JMenuItem("Return Invoice Manager");
		mntmNewInvoice_12.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ReturnInvoiceBrowser newReturnInvoice=new ReturnInvoiceBrowser();
				newReturnInvoice.setModal(true);
				newReturnInvoice.setVisible(true);
			}
		});
		mntmNewInvoice_12.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReturnInvoice.add(mntmNewInvoice_12);
		JMenu mnPo1 = new JMenu("Purchase Order");
		if(StoreMain.update_item_access.equals("1"))
			mnPo1.setEnabled(true);
		else {
			mnPo1.setEnabled(false);
		}
		mnPo1.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/PRODUCT.PNG")));
		mnPo1.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnPo1);

		JMenuItem mntmPurchaseOrder_2 = new JMenuItem("Purchase Order Automatic");
		mntmPurchaseOrder_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewPurchaseOrderReport order=new NewPurchaseOrderReport();
				order.setModal(true);
				order.setVisible(true);
			}
		});
		mntmPurchaseOrder_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPo1.add(mntmPurchaseOrder_2);
		JMenuItem approvelist = new JMenuItem("Approved List");
		approvelist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ApprovedList order=new ApprovedList();
				order.setModal(true);
				order.setVisible(true);
			}
		});
		approvelist.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPo1.add(approvelist);
		JMenu menu = new JMenu("Purchase Order");
		menu.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPo1.add(menu);

		JMenuItem menuItem_3 = new JMenuItem("New Form");
		menuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewPO newPO=new NewPO("");
				newPO.setModal(true);
				newPO.setVisible(true);
			}
		});
		menuItem_3.setFont(new Font("Tahoma", Font.BOLD, 14));
		//		menu.add(menuItem_3);
		//		if(!is_admin.equals("1")){
		//		mnPo1.setVisible(false);
		//		}

		JMenuItem menuItem_4 = new JMenuItem("Manager");
		menuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				POBrowser newPO=new POBrowser();
				newPO.setModal(true);
				newPO.setVisible(true);
			}
		});
		menuItem_4.setFont(new Font("Tahoma", Font.BOLD, 14));
		//		menuItem_3.add(menuItem_4);
		menu.add(menuItem_4);
		//
		//		JMenu mnInvoiceNew = new JMenu("Invoice");
		//		mnInvoiceNew.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/PRODUCT.PNG")));
		//		mnInvoiceNew.setFont(new Font("Tahoma", Font.BOLD, 15));
		////		menuBar.add(mnInvoiceNew);
		//		
		//		JMenu mnPurchaseInvoice = new JMenu("Purchase Invoice");
		//		mnPurchaseInvoice.setFont(new Font("Tahoma", Font.BOLD, 14));
		////		mnInvoiceNew.add(mnPurchaseInvoice);
		//		
		////		JMenuItem mntmNewInvoice1 = new JMenuItem("Manage Invoice");
		////		mntmNewInvoice.addActionListener(new ActionListener() {
		////			@Override
		////			public void actionPerformed(ActionEvent e) {
		////				InvoiceBrowser newInvoice=new InvoiceBrowser();
		////				newInvoice.setModal(true);
		////				newInvoice.setVisible(true);
		////			}
		////		});
		////		mntmNewInvoice1.setFont(new Font("Tahoma", Font.BOLD, 14));
		////		mnPurchaseInvoice.add(mntmNewInvoice1);
		//		
		//		JMenu mnChallanEntry = new JMenu("Challan Entry");
		//		mnChallanEntry.setFont(new Font("Tahoma", Font.BOLD, 14));
		////		mnInvoice.add(mnChallanEntry);
		//		
		//		JMenuItem mntmNewChallan = new JMenuItem("New Challan");
		//		mntmNewChallan.addActionListener(new ActionListener() {
		//			public void actionPerformed(ActionEvent arg0) {
		//				NewChallan newChallan=new NewChallan();
		//				newChallan.setModal(true);
		//				newChallan.setVisible(true);
		//			}
		//		});
		//		mntmNewChallan.setFont(new Font("Tahoma", Font.BOLD, 14));
		////		mnChallanEntry.add(mntmNewChallan);
		//		
		//		JMenu mnReturnInvoice = new JMenu("Return Invoice");
		//		mnReturnInvoice.setFont(new Font("Tahoma", Font.BOLD, 14));
		////		mnInvoice.add(mnReturnInvoice);
		//		
		//		JMenuItem mntmNewInvoice_1 = new JMenuItem("New Invoice");
		//		mntmNewInvoice_1.addActionListener(new ActionListener() {
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				NewReturnItemsForm newReturnInvoice=new NewReturnItemsForm();
		//				newReturnInvoice.setModal(true);
		//				newReturnInvoice.setVisible(true);
		//			}
		//		});
		//		mntmNewInvoice_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		////		mnReturnInvoice.add(mntmNewInvoice_1);
		//		JMenuItem mntmNewInvoice_12 = new JMenuItem("Return Invoice Manager");
		//		mntmNewInvoice_12.addActionListener(new ActionListener() {
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				ReturnInvoiceBrowser newReturnInvoice=new ReturnInvoiceBrowser();
		//				newReturnInvoice.setModal(true);
		//				newReturnInvoice.setVisible(true);
		//			}
		//		});
		//		mntmNewInvoice_12.setFont(new Font("Tahoma", Font.BOLD, 14));
		////		mnReturnInvoice.add(mntmNewInvoice_12);
		//	
		//		if(invoice_access.equals("0")){
		//			
		//		}else{
		//			if(invoice_access.equals("1")){
		//				menuBar.add(mnInvoiceNew);
		//				mnInvoiceNew.add(mnPurchaseInvoice);
		//				JMenuItem mntmNewInvoice1 = new JMenuItem("Manage Invoice");
		//				mntmNewInvoice.addActionListener(new ActionListener() {
		//					@Override
		//					public void actionPerformed(ActionEvent e) {
		//						InvoiceBrowser newInvoice=new InvoiceBrowser();
		//						newInvoice.setModal(true);
		//						newInvoice.setVisible(true);
		//					}
		//				});
		//				mntmNewInvoice1.setFont(new Font("Tahoma", Font.BOLD, 14));
		//				mnPurchaseInvoice.add(mntmNewInvoice1);
		//			}
		//			
		//		}

		//		JMenu mnMyStock = new JMenu("My Stock");
		//		mnMyStock.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/stockicon.png")));
		//		mnMyStock.setFont(new Font("Tahoma", Font.BOLD, 15));
		//		menuBar.add(mnMyStock);
		//		
		//		JMenuItem mntmStockRegister = new JMenuItem("Stock Register");
		//		mntmStockRegister.addActionListener(new ActionListener() {
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//			
		//				TotalStock totalStock=new TotalStock();
		//				totalStock.setModal(true);
		//				totalStock.setVisible(true);
		//			}
		//		});
		//		mntmStockRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
		//		mnMyStock.add(mntmStockRegister);
		JMenu mnOutdoorPatient1 = new JMenu("Suppliers");
		mnOutdoorPatient1.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/PRODUCT.PNG")));
		mnOutdoorPatient1.setFont(new Font("Tahoma", Font.BOLD, 15));
		//menuBar.add(mnOutdoorPatient1);

		JMenuItem mntmNewSupplier1 = new JMenuItem("New Supplier");
		mntmNewSupplier1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewSupplier newSupplier=new NewSupplier();
				newSupplier.setModal(true);
				newSupplier.setVisible(true);
			}
		});
		mntmNewSupplier1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatient1.add(mntmNewSupplier1);

		JMenuItem mntmEditSupplier1 = new JMenuItem("Edit Supplier");
		mntmEditSupplier1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SupplierBrowser supplierBrowser=new SupplierBrowser();
				supplierBrowser.setModal(true);
				supplierBrowser.setVisible(true);
			}
		});
		mntmEditSupplier1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatient1.add(mntmEditSupplier1);

		JMenu mnNewMenu_2 = new JMenu("Supplier Payments");
		mnNewMenu_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatient1.add(mnNewMenu_2);

		JMenuItem mntmNewMenuItem1 = new JMenuItem("New Payment");
		mntmNewMenuItem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SupplierPayments payment=new SupplierPayments();
				payment.setModal(true);
				payment.setVisible(true);

			}
		});
		mntmNewMenuItem1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnNewMenu_2.add(mntmNewMenuItem1);

		JMenuItem mntmSupplierPayment1 = new JMenuItem("Payments Manager");
		mnNewMenu_1.add(mntmSupplierPayment1);
		mntmSupplierPayment1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				SupplierGLPayments glPayments=new SupplierGLPayments();
				glPayments.setModal(true);
				glPayments.setVisible(true);
			}
		});
		mntmSupplierPayment1.setFont(new Font("Tahoma", Font.BOLD, 14));

		JMenuItem mntmOutStandingReport = new JMenuItem("Out Standing Report");
		mntmOutStandingReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				VendorsOutstanding outstanding=new VendorsOutstanding();
				outstanding.setModal(true);
				outstanding.setVisible(true);
			}
		});
		mntmOutStandingReport.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnNewMenu_2.add(mntmOutStandingReport);

		JMenuItem mntmSupplierInformation1 = new JMenuItem("Supplier Information");
		mntmSupplierInformation1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatient1.add(mntmSupplierInformation1);


		JMenu mnItems1 = new JMenu("Items");
		if(StoreMain.update_item_access.equals("1"))
			mnItems1.setEnabled(true);
		else {
			mnItems1.setEnabled(false);
		}
		mnItems1.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/restore.gif")));
		mnItems1.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnItems1);

		JMenuItem mntmNewItem1 = new JMenuItem("New Item");
		mntmNewItem1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewItem newItem=new NewItem();
				newItem.setModal(true);
				newItem.setVisible(true);
			}
		});
		mntmNewItem1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItems1.add(mntmNewItem1);

		JMenuItem mntmManageItems1 = new JMenuItem("Manage Items");
		mntmManageItems1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				ItemBrowser itemBrowser=new ItemBrowser();
				itemBrowser.setModal(true);
				itemBrowser.setVisible(true);
			}
		});
		mntmManageItems1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItems1.add(mntmManageItems1);

		JMenuItem mntmExcessStock1 = new JMenuItem("Excess Stock");
		mntmExcessStock1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ExcessStockReport excessStockReport=new ExcessStockReport();
				excessStockReport.setModal(true);
				excessStockReport.setVisible(true);
			}
		});
		mntmExcessStock1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItems1.add(mntmExcessStock1);
		JMenu mnPillsReminder = new JMenu("Reminder");
		mnPillsReminder.setFont(new Font("Tahoma", Font.BOLD, 15));
		mnPillsReminder.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/remindericon.png")));
		menuBar.add(mnPillsReminder);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JLabel label = new JLabel("");
		label.setBounds(685, 12, 84, 54);
		contentPane.add(label);
		label.setIcon(new ImageIcon(AdminMain.class.getResource("/icons/smallLogo.png")));
		label.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblRotaryAmbalaCancer = new JLabel(" DR. JAI DEV MEMORIAL ROTARY MEDICAL STORE .");
		lblRotaryAmbalaCancer.setBorder(null);
		lblRotaryAmbalaCancer.setBounds(754, 12, 580, 54);
		contentPane.add(lblRotaryAmbalaCancer);
		lblRotaryAmbalaCancer.setForeground(new Color(0, 0, 0));
		lblRotaryAmbalaCancer.setFont(new Font("Tahoma", Font.BOLD, 22));

		RealTimeClock realTimeClock = new RealTimeClock();
		realTimeClock.setFont(new Font("Dialog", Font.PLAIN,20));
		realTimeClock.setBorder(null);
		realTimeClock.setBackground(new Color(153, 204, 255));
		//realTimeClock.setForeground(new Color(0, 0, 0));
		realTimeClock.setBounds(1119, 65, 215, 25);
		contentPane.add(realTimeClock);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBackground(new Color(169, 169, 169));
		lblNewLabel.setBorder(UIManager.getBorder("InternalFrame.optionDialogBorder"));
		lblNewLabel.setForeground(new Color(100, 149, 237));
		lblNewLabel.setIcon(new ImageIcon(AdminMain.class.getResource("/icons/Rotary-HD.jpg")));		lblNewLabel.setBounds(0, 0, 1346, 672);
		contentPane.add(lblNewLabel);
	}
	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public void showDocScanning(JFrame owner) {
		if (docScanning == null || !docScanning.isDisplayable()) {
			docScanning = new DocScanning(owner);
			docScanning.setVisible(true);
		} else {
			docScanning.setAlwaysOnTop(true);
			docScanning.toFront();
			docScanning.requestFocus();
			docScanning.setAlwaysOnTop(false);
		}
	}
}
