package hms.admin.gui;

import hms.AutoUpdate.NewUpdates;
import hms.insurance.gui.AddInsuranceType;
import hms.main.AboutHMS;
import hms.main.ChangeMessage;
import hms.main.MainLoginMedicalStore;
import hms.main.NewsDBConnection;
import hms.main.RealTimeClock;
import hms.store.gui.NewStoreAccount;
import hms.store.gui.OPDReturnBillForm;
import hms.store.gui.StockAdjustment;
import hms.store.gui.StoreAccountBrowser;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import ca.odell.glazedlists.impl.adt.gnutrove.TIntArrayList;

public class AdminMain1 extends JFrame {

	private JPanel contentPane;
	static String OS;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					AdminMain1 frame = new AdminMain1("admin");
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
	public AdminMain1(final String userName) {
		OS=System.getProperty("os.name").toLowerCase();
		setTitle("Admin Login");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				AdminMain1.class.getResource("/icons/rotaryLogo.png")));
		setBackground(new Color(32, 178, 170));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		if(isWindows())
		{
			width-=234;
			height-=232;
		}else
		{

			width-=20; 
			height-=60;
		}
		setBounds(10, 10, width , height);
		contentPane = new JPanel();
		setVisible(true);
		setResizable(false);
		AdminDBConnection db = new AdminDBConnection();
		try {
			db.updateDataLastLogn(userName);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		db.closeConnection();

		
		contentPane.setBackground(SystemColor.control);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		NewsDBConnection newsDBConnection = new NewsDBConnection();
		newsDBConnection.closeConnection();
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1356, 43);
		contentPane.add(menuBar);
		
		JMenu mnMyAccount = new JMenu("My Account");
		mnMyAccount.setIcon(new ImageIcon(AdminMain1.class.getResource("/icons/employee.png")));
		mnMyAccount.setFont(new Font("Tahoma", Font.BOLD, 15));
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
		
		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				dispose();
				MainLoginMedicalStore mainLogin=new MainLoginMedicalStore();
				mainLogin.setVisible(true);
			}
		});
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
		
		JMenu mnSukhpal = new JMenu("Manage Accounts");
		mnSukhpal.setIcon(new ImageIcon(AdminMain1.class.getResource("/icons/emp.png")));
		mnSukhpal.setFont(new Font("Tahoma", Font.BOLD, 15));
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
		mntmManageStoreAccount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnStoreAccount.add(mntmManageStoreAccount);
		
		JMenu mnManageHeads = new JMenu("Manage Heads");
		mnManageHeads.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnManageHeads);
		
		JMenuItem mntmManageHeads = new JMenuItem("Manage Heads");
		mntmManageHeads.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				

				AddInsuranceType addInsuranceType=new AddInsuranceType();
				addInsuranceType.setModal(true);
				addInsuranceType.setVisible(true);
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
		
		JMenu mnCancelReciept = new JMenu("Cancel Reciept");
		mnCancelReciept.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnCancelReciept);
		
		JMenuItem mntmCancelReciept = new JMenuItem("Cancel Reciept");
		mntmCancelReciept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				OPDReturnBillForm OPDReturnBillForm=new OPDReturnBillForm();
				OPDReturnBillForm.setModal(true);
				OPDReturnBillForm.setVisible(true);
			}
		});
		mntmCancelReciept.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnCancelReciept.add(mntmCancelReciept);
		
		JMenuItem mntmReport = new JMenuItem("Report");
		mntmReport.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnCancelReciept.add(mntmReport);
		
		JMenu mnReports = new JMenu("Reports");
		mnReports.setIcon(new ImageIcon(AdminMain1.class.getResource("/icons/list_dialog.png")));
		mnReports.setFont(new Font("Tahoma", Font.BOLD, 15));
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
		mnSystemSettings.setIcon(new ImageIcon(AdminMain1.class.getResource("/icons/Settings.png")));
		mnSystemSettings.setFont(new Font("Tahoma", Font.BOLD, 15));
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
		
		JLabel label = new JLabel("");
		label.setBounds(685, 63, 84, 54);
		contentPane.add(label);
		label.setIcon(new ImageIcon(AdminMain.class.getResource("/icons/smallLogo.png")));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblRotaryAmbalaCancer = new JLabel(" DR. JAI DEV MEMORIAL ROTARY MEDICAL STORE ");
		lblRotaryAmbalaCancer.setBorder(null);
		lblRotaryAmbalaCancer.setBounds(754, 63, 580, 54);
		contentPane.add(lblRotaryAmbalaCancer);
		lblRotaryAmbalaCancer.setForeground(new Color(0, 0, 0));
		lblRotaryAmbalaCancer.setFont(new Font("Tahoma", Font.BOLD, 22));
		
		RealTimeClock realTimeClock = new RealTimeClock();
		realTimeClock.setFont(new Font("Dialog", Font.PLAIN,20));
		realTimeClock.setBorder(null);
		realTimeClock.setBackground(new Color(153, 204, 255));
		//realTimeClock.setForeground(new Color(0, 0, 0));
		realTimeClock.setBounds(1119, 116, 215, 25);
		contentPane.add(realTimeClock);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBackground(new Color(169, 169, 169));
		lblNewLabel.setBorder(UIManager.getBorder("InternalFrame.optionDialogBorder"));
		lblNewLabel.setForeground(new Color(100, 149, 237));
		lblNewLabel.setIcon(new ImageIcon(AdminMain.class.getResource("/icons/Rotary-HD.jpg")));
		lblNewLabel.setBounds(0, 45, 1346, 660);
		contentPane.add(lblNewLabel);
	}
	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}
}
