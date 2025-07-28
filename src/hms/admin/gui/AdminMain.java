package hms.admin.gui;

import hms.AutoUpdate.NewUpdates;
import hms.doc.scanning.DocScanning;
import hms.formula.manipulation.FormulaMaster;
import hms.gl.account.ReceptionUsersReport;
import hms.insurance.gui.AddInsuranceType;
import hms.insurance.gui.departmentinsurance;
import hms.main.AboutHMS;
import hms.main.MainLoginMedicalStore;
import hms.patient.slippdf.ReturnBillSlippdf;
import hms.patient.slippdf.CancelBillSlippdf;
import hms.patient.slippdf.DateSelection;
import hms.patient.slippdf.NearExpiryPdf;
import hms.reportstable.ApprovedList;
import hms.reportstable.ExcessStockReport;
import hms.reportstable.ItemsSaleReport;
import hms.reportstable.NewPurchaseOrderReport;
import hms.reportstable.PurchaseOrderReport;
import hms.store.database.StoreAccountDBConnection;
import hms.store.gui.BankDiposit;
import hms.store.gui.BankGLPayments;
import hms.store.gui.BillBrowser;
import hms.store.gui.BillBrowserPillsReq;
import hms.store.gui.ReturnApprovedBrowser;
import hms.store.gui.CancelRestockFee;
import hms.store.gui.CuttingCharges;
import hms.store.gui.HighRiskItemsBrowser;
import hms.store.gui.IPDReturnBillForm;
import hms.store.gui.IndentBrowser;
import hms.store.gui.InsuranceGLPayments;
import hms.store.gui.InsurancePayment;
import hms.store.gui.InvoiceBrowser;
import hms.store.gui.ItemBrowser;
import hms.store.gui.NewChallan;
import hms.store.gui.NewInvoice;
import hms.store.gui.NewIssuedForm;
import hms.store.gui.NewItem;
import hms.store.gui.NewPO;
import hms.store.gui.NewReturnItemsForm;
import hms.store.gui.NewStoreAccount;
import hms.store.gui.NewSupplier;
import hms.store.gui.OPDReturnBillForm;
import hms.store.gui.POBrowser;
import hms.store.gui.PurchaseOrder;
import hms.store.gui.ReturnInvoiceBrowser;
import hms.store.gui.StockAdjustment;
import hms.store.gui.StoreAccountBrowser;
import hms.store.gui.StoreMain;
import hms.store.gui.SupplierBrowser;
import hms.store.gui.SupplierGLPayments;
import hms.store.gui.SupplierPayments;
import hms.store.gui.TotalDepartmentStock;
import hms.store.gui.TotalStock;
import hms.store.gui.UserCashActivity;
import hms.store.gui.VendorsOutstanding;


import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.itextpdf.text.DocumentException;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import hms.main.RealTimeClock;
import java.awt.Insets;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import java.awt.SystemColor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AdminMain extends JFrame {

	private JPanel contentPane;
	private DocScanning docScanning;
	public static String id;
	public static String userName="";
	public static String userID="";
	public static String is_admin="",update_item_access="";
	static String OS;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					AdminMain frame = new AdminMain("sukhpal","10","","");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the frame.
	 */
	public AdminMain(String adminName,final String userID,String is_admin,String update_item_access) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				StoreAccountDBConnection storeAccountDBConnection = new StoreAccountDBConnection();
				ResultSet rs=storeAccountDBConnection.saveActivity(AdminMain.userName,AdminMain.userID,"Admin");
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
		setForeground(new Color(0, 0, 139));

		AdminMain.userName=adminName;
		AdminMain.userID=userID;
		AdminMain.is_admin=is_admin;
		AdminMain.update_item_access=update_item_access;
		StoreMain.update_item_access=update_item_access;
		StoreMain.userName=adminName;
		StoreMain.userID=userID;
		setTitle(userName);
		OS=System.getProperty("os.name").toLowerCase();
		setIconImage(Toolkit.getDefaultToolkit().getImage(AdminMain.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		if(isWindows())
		{

			width-=234;
			height-=200;
		}else
		{

			height-=45;
		}
		setBounds(10, 10, width,height);
		System.out.println("w="+width);
		System.out.println("h="+height+"\n"+OS);
		AdminDBConnection db = new AdminDBConnection();
		try {
			db.updateDataLastLogn(userName);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		db.closeConnection();

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(100, 149, 237));
		//menuBar.setMargin(new Insets(0, 6, 0, 0));
		setJMenuBar(menuBar);

		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainLoginMedicalStore mainLogin=new MainLoginMedicalStore();
				mainLogin.setVisible(true);
				dispose();
			}
		});

		JMenu mnMyAccount = new JMenu("My Acc.");
		mnMyAccount.setIcon(new ImageIcon(AdminMain1.class.getResource("/icons/employee.png")));
		mnMyAccount.setFont(new Font("Tahoma", Font.BOLD, 13));
		menuBar.add(mnMyAccount);

		JMenuItem mntmChangePassword = new JMenuItem("Change Password");
		mntmChangePassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AdminSettings ds = new AdminSettings(userName);
				ds.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				ds.setLocationRelativeTo(contentPane);
				ds.setModal(true);
				ds.setVisible(true);
			}
		});
		mntmChangePassword.setIcon(new ImageIcon(AdminMain1.class.getResource("/icons/SECURITY.PNG")));
		mntmChangePassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnMyAccount.add(mntmChangePassword);

		mntmLogout.setIcon(new ImageIcon(AdminMain1.class.getResource("/icons/hoverOver_close_tab.JPG")));
		mntmLogout.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnMyAccount.add(mntmLogout);

		JMenuItem mntmAbout = new JMenuItem("About HMS");
		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				AboutHMS aboutHMS=new AboutHMS();
				aboutHMS.setModal(true);
				aboutHMS.setVisible(true);
			}
		});
		mntmAbout.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnMyAccount.add(mntmAbout);

		JMenu mnSukhpal = new JMenu("Manage Acc.");
		mnSukhpal.setIcon(null);
		mnSukhpal.setFont(new Font("Tahoma", Font.BOLD, 13));
		menuBar.add(mnSukhpal);

		JMenu mnStoreAccount = new JMenu("Store Account");
		mnStoreAccount.setIcon(new ImageIcon(AdminMain1.class.getResource("/icons/OPEN.GIF")));
		mnStoreAccount.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnSukhpal.add(mnStoreAccount);

		JMenuItem mntmNewStoreAccount = new JMenuItem("New Store Account");
		mntmNewStoreAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewStoreAccount newStoreAccount=new NewStoreAccount();
				newStoreAccount.setModal(true);
				newStoreAccount.setVisible(true);
			}
		});
		mntmNewStoreAccount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnStoreAccount.add(mntmNewStoreAccount);

		JMenuItem mntmManageStoreAccount = new JMenuItem("Manage Store Account");
		mntmManageStoreAccount.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				StoreAccountBrowser storeAccountBrowser=new StoreAccountBrowser();
				storeAccountBrowser.setModal(true);
				storeAccountBrowser.setVisible(true);
			}
		});

		JMenuItem mntmStoreActivity = new JMenuItem("Store Activity");
		mntmStoreActivity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				store_activity st=new store_activity();
				st.setModal(true);
				st.setVisible(true);

			}
		});
		mnStoreAccount.add(mntmStoreActivity);

		JMenuItem mntmStoreActivity_1 = new JMenuItem("User Cash Report Activity");
		mntmStoreActivity_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new UserCashActivity().setVisible(true);;
			}
		});
		if (update_item_access.equals("1")) {
			mnStoreAccount.add(mntmStoreActivity_1);

		}
		mntmManageStoreAccount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnStoreAccount.add(mntmManageStoreAccount);

		JMenu mnManageHeads = new JMenu("Manage Heads");
		mnManageHeads.setFont(new Font("Tahoma", Font.BOLD, 13));
		menuBar.add(mnManageHeads);

		JMenuItem mntmManageHeads = new JMenuItem("Manage Heads");
		mntmManageHeads.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				departmentinsurance dptInsu=new departmentinsurance();

				dptInsu.frame.setVisible(true);

			}
		});
		mntmManageHeads.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnManageHeads.add(mntmManageHeads);

		JMenuItem mntmStockAdjustment = new JMenuItem("Stock Adjustment");
		mntmStockAdjustment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				StockAdjustment adjustment=new StockAdjustment();
				adjustment.setModal(true);
				adjustment.setVisible(true);
			}
		});
		mntmStockAdjustment.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnManageHeads.add(mntmStockAdjustment);
		if(!is_admin.equals("1")){
			mntmStockAdjustment.setVisible(false);
		}
		JMenu mnCancelReciept = new JMenu("Cancel Reciept");
		mnCancelReciept.setFont(new Font("Tahoma", Font.BOLD, 13));
		menuBar.add(mnCancelReciept);

		JMenuItem mntmReport = new JMenuItem("Report");
		mntmReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateSelection dateSelection=new DateSelection(1);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
			}
		});

		JMenuItem mntmCancelRequests = new JMenuItem("OPD Cancel Requests");
		mntmCancelRequests.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ReturnApprovedBrowser cancelBillForm = new ReturnApprovedBrowser();
				cancelBillForm.setVisible(true);
				cancelBillForm.setModal(true);


			}
		});
		JMenuItem mntmCancelReceipt = new JMenuItem("OPD Cancel Reciept");
		mntmCancelReceipt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OPDReturnBillForm OPDReturnBillForm = new OPDReturnBillForm();
				OPDReturnBillForm.setVisible(true);
				OPDReturnBillForm.setModal(true);
			}
		});
		mntmCancelReceipt.setFont(new Font("Dialog", Font.PLAIN, 14));

		mntmCancelRequests.setFont(new Font("Dialog", Font.PLAIN, 14));
		if (update_item_access.equals("1")) {
			mnCancelReciept.add(mntmCancelRequests);
			mnCancelReciept.add(mntmCancelReceipt);
		}else
		{
			mnCancelReciept.add(mntmCancelReceipt);
		}


		mntmReport.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnCancelReciept.add(mntmReport);

		JMenuItem mntmCancelRecieptSetting = new JMenuItem("Setting");
		mntmCancelRecieptSetting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				CancelRestockFee window = new CancelRestockFee();
				window.frame.setVisible(true);

			}
		});

		JMenuItem mntmPrintReciept = new JMenuItem("Old Print Reciept");
		mntmPrintReciept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFrame frame = new JFrame();
					Object result = JOptionPane.showInputDialog(frame, "Enter Slip number:");
					if(result!=null && result.equals(""))
					{JOptionPane.showMessageDialog(null,
							"Please Enter Slip number",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
					}else {
						new CancelBillSlippdf(result.toString(),"CANCEL");
					}

				} catch (DocumentException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mntmPrintReciept.setFont(new Font("Dialog", Font.PLAIN, 14));
		mnCancelReciept.add(mntmPrintReciept);

		JMenuItem mntmIpdCancelRecipt = new JMenuItem("IPD Cancel Recipt");
		mntmIpdCancelRecipt.setFont(new Font("Dialog", Font.PLAIN, 14));
		mntmIpdCancelRecipt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IPDReturnBillForm cancelBillForm = new IPDReturnBillForm();
				cancelBillForm.setVisible(true);
				cancelBillForm.setModal(true);
			}
		});
		mnCancelReciept.add(mntmIpdCancelRecipt);
		mntmCancelRecieptSetting.setFont(new Font("Tahoma", Font.PLAIN, 14));
		if (update_item_access.equals("1")) {
			mnCancelReciept.add(mntmCancelRecieptSetting);
		}

		JMenu mnReports = new JMenu("Reports");
		mnReports.setIcon(null);
		mnReports.setFont(new Font("Tahoma", Font.BOLD, 13));
		menuBar.add(mnReports);
		JMenuItem mntmInsuranceSummery = new JMenuItem("Insurance Summery");
		mntmInsuranceSummery.setIcon(new ImageIcon(AdminMain1.class.getResource("/icons/NEW.PNG")));
		mntmInsuranceSummery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		mntmInsuranceSummery.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnReports.add(mntmInsuranceSummery);

		JMenu mnSystemSettings = new JMenu("System Settings");
		mnSystemSettings.setIcon(null);
		mnSystemSettings.setFont(new Font("Tahoma", Font.BOLD, 13));
		menuBar.add(mnSystemSettings);

		JMenuItem mntmSystemMessage = new JMenuItem("TIN No");
		mntmSystemMessage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ITN_LIC_NO dialog = new ITN_LIC_NO();
				dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		mntmSystemMessage.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnSystemSettings.add(mntmSystemMessage);

		JMenuItem mntmVersionControl = new JMenuItem("New Updates");
		mntmVersionControl.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				NewUpdates newUpdates=new NewUpdates();
				newUpdates.setModal(true);
				newUpdates.setVisible(true);
			}
		});
		mntmVersionControl.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnSystemSettings.add(mntmVersionControl);

		JMenu mnOutdoorPatient = new JMenu("Suppliers");
		mnOutdoorPatient.setIcon(null);
		mnOutdoorPatient.setFont(new Font("Tahoma", Font.BOLD, 13));
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

		JMenuItem mntmOutStandingReport = new JMenuItem("Out Standing Report");
		mntmOutStandingReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				VendorsOutstanding outstanding=new VendorsOutstanding();
				outstanding.setModal(true);
				outstanding.setVisible(true);
			}
		});
		mntmOutStandingReport.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnNewMenu_1.add(mntmOutStandingReport);

		JMenuItem mntmSupplierInformation = new JMenuItem("Supplier Information");
		mntmSupplierInformation.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatient.add(mntmSupplierInformation);

		JMenu mnInsuranceHead = new JMenu("Insurance Head");
		mnInsuranceHead.setIcon(null);
		mnInsuranceHead.setFont(new Font("Tahoma", Font.BOLD, 13));
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

		JMenu mnBankHead = new JMenu("Bank Head");
		mnBankHead.setFont(new Font("Tahoma", Font.BOLD, 13));
		menuBar.add(mnBankHead);

		JMenuItem menuItem_1 = new JMenuItem("New Payment");
		menuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {


				BankDiposit insurancePayment=new BankDiposit();
				insurancePayment.setModal(true);
				insurancePayment.setVisible(true);
			}
		});
		menuItem_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnBankHead.add(menuItem_1);

		JMenuItem menuItem_2 = new JMenuItem("Payment Manager");
		menuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				BankGLPayments glPayments=new BankGLPayments();
				glPayments.setModal(true);
				glPayments.setVisible(true);
			}
		});
		menuItem_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnBankHead.add(menuItem_2);

		JMenu mnItems = new JMenu("Items");
		mnItems.setIcon(null);
		mnItems.setFont(new Font("Tahoma", Font.BOLD, 13));
		menuBar.add(mnItems);

		JMenuItem mntmNewItem = new JMenuItem("New Item");
		mntmNewItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewItem newItem=new NewItem();
				newItem.setModal(true);
				newItem.setVisible(true);
			}
		});
		mntmNewItem.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItems.add(mntmNewItem);

		JMenuItem mntmManageItems = new JMenuItem("Manage Items");
		mntmManageItems.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				ItemBrowser itemBrowser=new ItemBrowser();
				itemBrowser.setModal(true);
				itemBrowser.setVisible(true);
			}
		});
		mntmManageItems.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItems.add(mntmManageItems);

		JMenuItem mntmExcessStock = new JMenuItem("Excess Stock");
		mntmExcessStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ExcessStockReport excessStockReport=new ExcessStockReport();
				excessStockReport.setModal(true);
				excessStockReport.setVisible(true);
			}
		});

		JMenuItem mntmManageHighRisk = new JMenuItem("Manage High Risk Items");
		mntmManageHighRisk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HighRiskItemsBrowser ob=new HighRiskItemsBrowser();
				ob.setModal(true);
				ob.setVisible(true);
			}
		});

		JMenuItem mntmManageItems_1 = new JMenuItem("Formula Master");
		mntmManageItems_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FormulaMaster FormulaMaster=new FormulaMaster();
				FormulaMaster.setModal(true);
				FormulaMaster.setVisible(true);
			}
		});
		mntmManageItems_1.setFont(new Font("Dialog", Font.BOLD, 14));
		if(AdminMain.update_item_access.equals("1"))
			mnItems.add(mntmManageItems_1);

		mntmManageHighRisk.setFont(new Font("Dialog", Font.BOLD, 14));
		mnItems.add(mntmManageHighRisk);
		mntmExcessStock.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItems.add(mntmExcessStock);

		JMenuItem mntmStockAdjustment_1 = new JMenuItem("Stock Adjustment");
		mntmStockAdjustment_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StockAdjustment dialog = new StockAdjustment();
				dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		mntmStockAdjustment_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		if(userName.equals("admin"))
		{
			mnItems.add(mntmStockAdjustment_1);
		}

		JMenuItem mntmItemsDetail = new JMenuItem("Items Expiry Report");
		mntmItemsDetail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DateSelection dateSelection=new DateSelection(5);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
			}
		});

		JMenuItem mntmSummerReport = new JMenuItem("Summery Report");
		mntmSummerReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				ReceptionUsersReport dateSelection=new ReceptionUsersReport();
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
			}
		});
		mntmSummerReport.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmSummerReport);

		JMenuItem mntmCancelReport = new JMenuItem("Not Moving Item Report");
		mntmCancelReport.setEnabled(true);
		mntmCancelReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DateSelection dateSelection=new DateSelection(7);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
			}
		});

		JMenuItem mntmSummerReport_1 = new JMenuItem("High Risk Item Report");
		mntmSummerReport_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateSelection dateSelection=new DateSelection(6);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);

			}
		});
		mntmSummerReport_1.setFont(new Font("Dialog", Font.BOLD, 14));
		mnReports.add(mntmSummerReport_1);
		mntmCancelReport.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmCancelReport);

		JMenuItem mntmDaywiseSaleReport = new JMenuItem("DayWise Sale Report");
		mntmDaywiseSaleReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateSelection dateSelection=new DateSelection(8);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
			}
		});

		JMenuItem mntmKarunaDiscount = new JMenuItem("Karuna Medicine Discount Report");
		mntmKarunaDiscount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				DateSelection dateSelection=new DateSelection(9);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
			}
		});
		mntmKarunaDiscount.setFont(new Font("Dialog", Font.BOLD, 14));
		mntmKarunaDiscount.setEnabled(true);
		mnReports.add(mntmKarunaDiscount);
		mnReports.add(mntmDaywiseSaleReport);
		mntmItemsDetail.setFont(new Font("Dialog", Font.BOLD, 14));
		mnReports.add(mntmItemsDetail);

		JMenuItem mntmPurchaseOrder = new JMenuItem("Purchase Order");
		mntmPurchaseOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		mntmPurchaseOrder.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmPurchaseOrder);

		JMenuItem mntmInvoiceDetails = new JMenuItem("Purchase Reports");
		mntmInvoiceDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DateSelection dateSelection=new DateSelection(21);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);

			}
		});
		mntmInvoiceDetails.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmInvoiceDetails);

		JMenuItem mntmDoctorWiseSale = new JMenuItem("Doctor Wise Sale");
		mntmDoctorWiseSale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DateSelection dateSelection=new DateSelection(20);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
			}
		});
		mntmDoctorWiseSale.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmDoctorWiseSale);
		JMenuItem mntmInvoiceDetails1 = new JMenuItem("Invoice Items Reports");
		mntmInvoiceDetails1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {


				DateSelection dateSelection=new DateSelection(3);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
			}
		});
		mntmInvoiceDetails1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmInvoiceDetails1);
		JMenuItem menuItem = new JMenuItem("Sales Tax Report");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				DateSelection dateSelection=new DateSelection(22);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
			}
		});
		menuItem.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(menuItem);

		JMenuItem mntmPurchaseTaxReport = new JMenuItem("Purchase Tax Report");
		mntmPurchaseTaxReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PurchaseTaxReport purchaseTaxReport=new PurchaseTaxReport();
				purchaseTaxReport.setModal(true);
				purchaseTaxReport.setVisible(true);
			}
		});
		mntmPurchaseTaxReport.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmPurchaseTaxReport);

		JMenuItem mntmItemWiseSale = new JMenuItem("Item Wise Sale");
		mntmItemWiseSale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ItemsSaleReport itemsSaleReport=new ItemsSaleReport();
				itemsSaleReport.setModal(true);
				itemsSaleReport.setVisible(true);
			}
		});
		mntmItemWiseSale.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmItemWiseSale);

		JMenuItem mntmHospitalDepartmentStock = new JMenuItem("Hospital Department Stock");
		mntmHospitalDepartmentStock.setFont(new Font("Tahoma", Font.BOLD, 14));
		mntmHospitalDepartmentStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TotalDepartmentStock TotalDepartmentStock=new TotalDepartmentStock();
				TotalDepartmentStock.setModal(true);
				TotalDepartmentStock.setVisible(true);
			}
		});
		mnReports.add(mntmHospitalDepartmentStock);

		JMenu mnInvoice = new JMenu("Invoice");
		mnInvoice.setIcon(null);
		mnInvoice.setFont(new Font("Tahoma", Font.BOLD, 13));
		menuBar.add(mnInvoice);

		JMenu mnPurchaseInvoice = new JMenu("Purchase Invoice");
		mnPurchaseInvoice.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnInvoice.add(mnPurchaseInvoice);

		JMenuItem mntmNewInvoice = new JMenuItem("Manage Invoice");
		mntmNewInvoice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				InvoiceBrowser newInvoice=new InvoiceBrowser();
				newInvoice.setModal(true);
				newInvoice.setVisible(true);
			}
		});
		mntmNewInvoice.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPurchaseInvoice.add(mntmNewInvoice);

		JMenu mnChallanEntry = new JMenu("Challan Entry");
		mnChallanEntry.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnInvoice.add(mnChallanEntry);

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
		mnInvoice.add(mnReturnInvoice);

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
		JMenu mnPo = new JMenu("Purchase Order");
		mnPo.setIcon(null);
		mnPo.setFont(new Font("Tahoma", Font.BOLD, 13));
		menuBar.add(mnPo);

		JMenuItem mntmPurchaseOrder_1 = new JMenuItem("Purchase Order Automatic");
		mntmPurchaseOrder_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewPurchaseOrderReport order=new NewPurchaseOrderReport();
				order.setModal(true);
				order.setVisible(true);
			}
		});
		mntmPurchaseOrder_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPo.add(mntmPurchaseOrder_1);
		JMenuItem approvelist = new JMenuItem("Approved List");
		approvelist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ApprovedList order=new ApprovedList();
				order.setModal(true);
				order.setVisible(true);
			}
		});
		approvelist.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPo.add(approvelist);
		JMenuItem mntmActiveItemsStatus = new JMenuItem("Active Items Status");
		mntmActiveItemsStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ActiveItemsStatusReport order=new ActiveItemsStatusReport();
				order.setModal(true);
				order.setVisible(true);
			}
		});

		JMenu menu = new JMenu("Purchase Order");
		menu.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPo.add(menu);

		JMenuItem menuItem_3 = new JMenuItem("New Form");
		menuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewPO newPO=new NewPO("");
				newPO.setModal(true);
				newPO.setVisible(true);
			}
		});
		menuItem_3.setFont(new Font("Tahoma", Font.BOLD, 14));
		menu.add(menuItem_3);
		if(!is_admin.equals("1")){
			menuItem_3.setVisible(false);
		}

		JMenuItem menuItem_4 = new JMenuItem("Manager");
		menuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				POBrowser newPO=new POBrowser();
				newPO.setModal(true);
				newPO.setVisible(true);
			}
		});
		menuItem_4.setFont(new Font("Tahoma", Font.BOLD, 14));
		menu.add(menuItem_4);
		mntmActiveItemsStatus.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPo.add(mntmActiveItemsStatus);

		JMenu mnIndent = new JMenu("Indent");
		mnIndent.setFont(new Font("Tahoma", Font.BOLD, 13));
		menuBar.add(mnIndent);

		JMenuItem mntmIndentManager = new JMenuItem("Indent Manager");
		mntmIndentManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				IndentBrowser indentBrowser=new IndentBrowser();
				indentBrowser.setModal(true);
				indentBrowser.setVisible(true);
			}
		});
		mntmIndentManager.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnIndent.add(mntmIndentManager);
		mnIndent.setVisible(false);

		JMenu mnItemIssue = new JMenu("Billing");
		mnItemIssue.setIcon(null);
		mnItemIssue.setFont(new Font("Tahoma", Font.BOLD, 13));
		menuBar.add(mnItemIssue);

		JMenuItem mntmNewIssueForm = new JMenuItem("Billing");
		mnItemIssue.add(mntmNewIssueForm);
		mntmNewIssueForm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				BillBrowser newIssuedForm=new BillBrowser(AdminMain.this,"","admin");
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
				showDocScanning(AdminMain.this);
			}
		});
		mntmNewIssueForm.setFont(new Font("Tahoma", Font.BOLD, 14));

		JMenuItem mntmIssueToDept = new JMenuItem("Issue To Dept.");
		mntmIssueToDept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//				NewIssuedForm dialog = new NewIssuedForm();
				//				dialog.setVisible(true);
				BillBrowserPillsReq dialog = new BillBrowserPillsReq(AdminMain.this,AdminMain.userID,"admin");
				dialog.setVisible(true);

			}
		});
		mntmIssueToDept.setFont(new Font("Dialog", Font.BOLD, 14));
		mnItemIssue.add(mntmIssueToDept);

		JMenuItem mntmCuttingSetting = new JMenuItem("Cutting Setting");
		mntmCancelRecieptSetting.setFont(new Font("Tahoma", Font.PLAIN, 14));
		if (update_item_access.equals("1")) {
			mnItemIssue.add(mntmCuttingSetting);
		}

		mntmCuttingSetting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				CuttingCharges window = new CuttingCharges();
				window.frame.setVisible(true);
			}
		});
		mntmCuttingSetting.setFont(new Font("Tahoma", Font.BOLD, 14));

		JMenu mnMyStock = new JMenu("My Stock");
		mnMyStock.setIcon(null);
		mnMyStock.setFont(new Font("Tahoma", Font.BOLD, 13));
		menuBar.add(mnMyStock);

		JMenuItem mntmStockRegister = new JMenuItem("Stock Register");
		mntmStockRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				TotalStock totalStock=new TotalStock();
				totalStock.setModal(true);
				totalStock.setVisible(true);
			}
		});
		mntmStockRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnMyStock.add(mntmStockRegister);

		JMenu mnSuperAdmin = new JMenu("Super Admin");
		mnSuperAdmin.setFont(new Font("Tahoma", Font.BOLD, 13));
		//menuBar.add(mnSuperAdmin);

		JMenuItem mntmSuperAdmin = new JMenuItem("Super Admin");
		mntmSuperAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				SuperAdmin admin= new SuperAdmin();
				admin.setModal(true);
				admin.setVisible(true);
			}
		});
		mntmSuperAdmin.setFont(new Font("Tahoma", Font.BOLD, 14));
		//mnSuperAdmin.add(mntmSuperAdmin);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label = new JLabel("");
		label.setBounds(696, 22, 84, 78);
		contentPane.add(label);
		label.setIcon(new ImageIcon(AdminMain.class.getResource("/icons/smallLogo.png")));
		label.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblRotaryAmbalaCancer = new JLabel(" DR. JAI DEV MEMORIAL ROTARY MEDICAL STORE ");
		lblRotaryAmbalaCancer.setBorder(null);
		lblRotaryAmbalaCancer.setBounds(766, 36, 580, 54);
		contentPane.add(lblRotaryAmbalaCancer);
		lblRotaryAmbalaCancer.setForeground(new Color(0, 0, 0));
		lblRotaryAmbalaCancer.setFont(new Font("Tahoma", Font.BOLD, 22));

		RealTimeClock realTimeClock = new RealTimeClock();
		realTimeClock.setFont(new Font("Dialog", Font.PLAIN,20));
		realTimeClock.setBorder(null);
		realTimeClock.setBackground(new Color(153, 204, 255));
		//realTimeClock.setForeground(new Color(0, 0, 0));
		realTimeClock.setBounds(1108, 12, 215, 25);
		contentPane.add(realTimeClock);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBackground(new Color(169, 169, 169));
		lblNewLabel.setBorder(UIManager.getBorder("InternalFrame.optionDialogBorder"));
		lblNewLabel.setForeground(new Color(100, 149, 237));
		lblNewLabel.setIcon(new ImageIcon(AdminMain.class.getResource("/icons/Rotary-HD.jpg")));
		if(isWindows())
		{
			lblNewLabel.setBounds(0,-20,width,height);
		}else {
			lblNewLabel.setBounds(0,-40,width,height);
		}
		contentPane.add(lblNewLabel);
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
	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}
}
