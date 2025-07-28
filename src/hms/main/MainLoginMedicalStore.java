package hms.main;

import hms.AutoDeleteFiles.AutoDeleteFiles;
import hms.AutoUpdate.UpdaterMain;
import hms.Printer.PrinterSetting;
import hms.admin.gui.AdminDBConnection;
import hms.admin.gui.AdminMain;
import hms.admin.gui.AdminMain1;
import hms.store.database.StoreAccountDBConnection;
import hms.store.gui.StoreMain;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;


import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import de.javasoft.plaf.synthetica.SyntheticaBlueLightLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaGreenDreamLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaOrangeMetallicLookAndFeel;

@SuppressWarnings("serial")
public class MainLoginMedicalStore extends JFrame {

	private JPanel contentPane;
	private JPasswordField passwordField;
	ResultSet resultSet;
	JTextField userNameTB;
	Vector userNameItem = new Vector();
	Vector departmentItem = new Vector();
	private JButton loginBT;
	String userName = "Admin", validEDate = "", password = "";
	String mainDir = "";
	String id = "",
			name = "",is_admin="";

	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		//		  for (int i = 0; i < args.length; i++)
		//	            System.out.println(args[i]);
		//		 try {
		//		        UIManager.setLookAndFeel(new SeaGlassLookAndFeel());
		//		    } catch (Exception e) {
		//		        e.printStackTrace();
		//		    }
		new Developing_environment(); 
		MainLoginMedicalStore frame = new MainLoginMedicalStore();
		frame.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("unchecked")
	public MainLoginMedicalStore() {
		UpdaterMain up = new UpdaterMain();
		if (up.checkUpdate()) {	
			dispose();
		    up.setVisible(true);
		    up.DoUpdate();
		    return;
		}
		
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(MainLoginMedicalStore.class.getResource("/icons/Rotary-HD.jpg")));
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				MainLoginMedicalStore.class.getResource("/icons/rotaryLogo.png")));
		setTitle("Medical Store Management System");
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(350, 200, 452, 379);// 350, 200, 549, 358
		contentPane = new JPanel();
		contentPane.setBackground(new Color(248, 248, 255));
		contentPane.setBorder(UIManager.getBorder("InternalFrame.border"));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		new AutoDeleteFiles().GetFolderData();
		JLabel lblLaboratoryInformationManagement = new JLabel(
				"Medical Store Management System");
		lblLaboratoryInformationManagement.setForeground(new Color(105, 105,
				105));
		lblLaboratoryInformationManagement.setFont(new Font("Vani", Font.BOLD,
				17));
		lblLaboratoryInformationManagement.setBounds(10, 42, 410, 39);
		contentPane.add(lblLaboratoryInformationManagement);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 92, 424, 2);
		contentPane.add(separator);

		JLabel lblUserName = new JLabel("User Name ");
		lblUserName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUserName.setBounds(154, 131, 77, 14);
		contentPane.add(lblUserName);

		userNameTB = new JTextField();
		userNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		userNameTB.setBounds(241, 125, 179, 27);
		contentPane.add(userNameTB);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPassword.setBounds(154, 168, 77, 14);
		contentPane.add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.BOLD, 12));
		passwordField.setBounds(241, 163, 179, 26);
		contentPane.add(passwordField);

		JLabel lblDepartment = new JLabel("Department");
		lblDepartment.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDepartment.setBounds(154, 198, 77, 27);
		contentPane.add(lblDepartment);

		final JComboBox comboBox_1 = new JComboBox(departmentItem);
		comboBox_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {
				"Admin",
		"Store" }));
		comboBox_1.setBounds(241, 200, 179, 27);
		contentPane.add(comboBox_1);

		JLabel lblLogin = new JLabel("Login");
		lblLogin.setForeground(Color.BLACK);
		lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblLogin.setBounds(139, 93, 67, 27);
		contentPane.add(lblLogin);

		loginBT = new JButton("Login");
		loginBT.setIcon(new ImageIcon(MainLoginMedicalStore.class
				.getResource("/icons/Key.gif")));
		getRootPane().setDefaultButton(loginBT);
		loginBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String pass = passwordField.getText().toString();
				String user = userNameTB.getText().toString(); 
				
				if (user.equals("")) {
					JOptionPane.showMessageDialog(null, "Enter Username",
							"Login Fail", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (pass.equals("")) {
					JOptionPane.showMessageDialog(null, "Enter password",
							"Login Fail", JOptionPane.ERROR_MESSAGE);
					return;
				}
				switch (comboBox_1.getSelectedIndex()) {
				case 0:

					int n11 = 0;
					AdminDBConnection db11 = new AdminDBConnection();
					ResultSet rs11 = db11.retrieveUserPassword(user, pass);
					String update_item_access="";
					try {
						while (rs11.next()) {
							id = rs11.getObject(1).toString();
							name = rs11.getObject(2).toString();
							is_admin= rs11.getObject(7).toString();
							update_item_access=rs11.getObject(8).toString();
							n11++;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						db11.closeConnection();
					}
					db11.closeConnection();
					if (n11 == 0) {
						JOptionPane.showMessageDialog(null,
								"Invalide username and password combination",
								"Login Fail", JOptionPane.ERROR_MESSAGE);
						return;
					}
					AdminMain adminMain = new AdminMain(user,id,is_admin,update_item_access);
					adminMain.setVisible(true);
					dispose();
					break;

				case 1:
					int n01 = 0;

					StoreAccountDBConnection storeAccountDBConnection = new StoreAccountDBConnection();
					ResultSet rs01 = storeAccountDBConnection
							.retrieveUserPassword(user, pass);
					String collection_report_access="";
					String update_item_access_store="";
					try {
						while (rs01.next()) {
							id = rs01.getObject(1).toString();
							name = rs01.getObject(2).toString();
							collection_report_access = rs01.getObject(3).toString();
							update_item_access_store=rs01.getObject(4).toString();

							n01++;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						storeAccountDBConnection.closeConnection();
					}
					storeAccountDBConnection.closeConnection();
					if (n01 == 0) {
						JOptionPane.showMessageDialog(null,
								"Invalide username and password combination",
								"Login Fail", JOptionPane.ERROR_MESSAGE);
						return;
					}
					//					System.out.print("updateitemaccess"+update_item_access);
					StoreMain storeMain = new StoreMain(user,name, id,collection_report_access,update_item_access_store);
					storeMain.setVisible(true);
					dispose();
					break;
				}

			}
		});
		loginBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		loginBT.setBounds(241, 253, 125, 40);
		contentPane.add(loginBT);
		InputMap im = loginBT.getInputMap();
		im.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		im.put(KeyStroke.getKeyStroke("released ENTER"), "released");

		JButton cancelBT = new JButton("Cancel");
		cancelBT.setIcon(new ImageIcon(MainLoginMedicalStore.class
				.getResource("/icons/Exit.gif")));
		cancelBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				dispose();
				// updateLAF();
			}
		});
		cancelBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cancelBT.setBounds(106, 253, 125, 40);
		contentPane.add(cancelBT);
		InputMap cancelIM = cancelBT.getInputMap();
		cancelIM.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		cancelIM.put(KeyStroke.getKeyStroke("released ENTER"), "released");

		JLabel iconImage = new JLabel();
		iconImage.setIcon(new ImageIcon(MainLoginMedicalStore.class
				.getResource("/icons/rotaryLogo.png")));
		iconImage.setBounds(10, 103, 130, 155);
		contentPane.add(iconImage);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(10, 304, 424, 36);
		contentPane.add(panel);
		panel.setLayout(null);

		//		JLabel lblDevelopedBy = new JLabel(
		//				"Developed By : Sukhpal  Saini, Mob. 9728073421");
		JLabel lblDevelopedBy = new JLabel(
				"For Support Contact:8295652525");
		lblDevelopedBy.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDevelopedBy.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblDevelopedBy.setBounds(10, 5, 404, 25);
		panel.add(lblDevelopedBy);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 446, 27);
		contentPane.add(menuBar);

		JMenu mnThemes = new JMenu("Themes");
		mnThemes.setFont(new Font("Tahoma", Font.BOLD, 12));
		menuBar.add(mnThemes);

		JMenuItem mntmSystemTheme = new JMenuItem("System Theme");
		mntmSystemTheme.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mntmSystemTheme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				updateLAF(1);
			}
		});
		mnThemes.add(mntmSystemTheme);

		JMenuItem mntmNewMenuItem = new JMenuItem("Blue Light");
		mntmNewMenuItem.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mntmNewMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateLAF(2);
			}
		});
		mnThemes.add(mntmNewMenuItem);

		JMenuItem mntmNimbusTheme = new JMenuItem("Nimbus Theme");
		mntmNimbusTheme.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mntmNimbusTheme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateLAF(3);
			}
		});
		mnThemes.add(mntmNimbusTheme);

		JMenuItem mntmOrangeMetallic = new JMenuItem("Orange Metallic");
		mntmOrangeMetallic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateLAF(4);
			}
		});
		mntmOrangeMetallic.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mnThemes.add(mntmOrangeMetallic);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Green Dream");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateLAF(5);
			}
		});
		mntmNewMenuItem_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mnThemes.add(mntmNewMenuItem_1);

		JMenu mnAboutHms = new JMenu("About MS++");
		mnAboutHms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		mnAboutHms.setFont(new Font("Tahoma", Font.BOLD, 12));
		menuBar.add(mnAboutHms);

		JMenuItem mntmAboutHms = new JMenuItem("About MS++");
		mntmAboutHms.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mntmAboutHms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				AboutHMS aboutHMS = new AboutHMS();
				aboutHMS.setModal(true);
				aboutHMS.setVisible(true);
			}
		});
		mnAboutHms.add(mntmAboutHms);
		
		JMenuItem mntmPrinterSetting = new JMenuItem("Printer Setting");
		mntmPrinterSetting.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mntmPrinterSetting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PrinterSetting PrinterSetting = new PrinterSetting();
				PrinterSetting.setModal(true);
				PrinterSetting.setVisible(true);
			}
		});
		mnAboutHms.add(mntmPrinterSetting);
		readCounterFile();
		makeDirectory();

	}

	public void runprogram() {
		if (System.getProperty("java.version").toString().equals("null")) {
			ProcessBuilder pb = new ProcessBuilder(
					new File(System.getProperty("user.dir"), "jre/bin/java.exe")
					.toString(), "-jar", new File(System
							.getProperty("user.dir"), "HMS-UPDATE.jar")
					.toString());
			pb.directory(new File("" + System.getProperty("user.dir")));
			try {
				Process p = pb.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			ProcessBuilder pb = new ProcessBuilder("java", "-jar",
					new File(System.getProperty("user.dir"), "HMS-UPDATE.jar")
					.toString());
			pb.directory(new File("" + System.getProperty("user.dir")));
			try {
				Process p = pb.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		dispose();
	}

	public void makeDirectory() {

		try {
			SmbFile dir = new SmbFile(mainDir + "/HMS/Patient");
			if (!dir.exists()) {
				dir.mkdirs();
				new SmbFile(mainDir + "/HMS/Doctor").mkdirs();
				new SmbFile(mainDir + "/HMS/opdslip").mkdirs();
				new SmbFile(mainDir + "/HMS/patientslip").mkdirs();
			}
		} catch (SmbException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void readCounterFile() {
		// The name of the file to open.
		String fileName = "data.mdi";

		// This will reference one line at a time
		String line = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String str = null;
			boolean fetch = true;
			while ((line = bufferedReader.readLine()) != null && fetch) {
				// System.out.println(line);
				str = line;
				fetch = false;
			}
			String data[] = new String[22];
			int i = 0;
			for (String retval : str.split("@")) {
				data[i] = retval;
				i++;
			}
			mainDir = data[1];
			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out
			.println("Unable to open file '" + fileName + "' \n" + ex);
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
	}

	public static void updateLAF(int key) {

		try {
			switch (key) {
			case 1:
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
				for (Frame frame : Frame.getFrames()) {
					updateLAFRecursively(frame);
				}
				break;

			case 2:
				UIManager.setLookAndFeel(new SyntheticaBlueLightLookAndFeel());
				for (Frame frame : Frame.getFrames()) {
					updateLAFRecursively(frame);
				}
				break;

			case 3:
				for (LookAndFeelInfo info : UIManager
						.getInstalledLookAndFeels()) {
					if ("Nimbus".equals(info.getName())) {
						UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}
				for (Frame frame : Frame.getFrames()) {
					updateLAFRecursively(frame);
				}
				break;
			case 4:
				try {
					UIManager
					.setLookAndFeel(new SyntheticaOrangeMetallicLookAndFeel());
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 5:
				try {
					UIManager
					.setLookAndFeel(new SyntheticaGreenDreamLookAndFeel());
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | UnsupportedLookAndFeelException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (Frame frame : Frame.getFrames()) {
				updateLAFRecursively(frame);
			}
		}
	}

	public static void updateLAFRecursively(Window window) {
		for (Window childWindow : window.getOwnedWindows()) {
			updateLAFRecursively(childWindow);
		}
		SwingUtilities.updateComponentTreeUI(window);
	}
}
