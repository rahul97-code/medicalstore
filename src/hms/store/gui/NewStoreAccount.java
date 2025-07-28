package hms.store.gui;

import hms.store.database.StoreAccountDBConnection;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class NewStoreAccount extends JDialog {

	private JPanel contentPane;
	private JTextField storeNameTB;
	private JTextField userNameTB;
	private JPasswordField passwordTB;
	private JPasswordField confPasswordTB;
	private JTextField telephoneTB;
	String storeName = "", storeID = "", storeUserName = "", storePassword = "",
		 storeTelephone = "",
			storeAddress = "", storeAchievement = "";
	private HashMap allstores = new HashMap();
	String[] data = new String[20];
	JComboBox comboBox;
	String usercollect="";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					NewStoreAccount frame = new NewStoreAccount();
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
	public NewStoreAccount() {
		setTitle("Add New Store Account");
		setIconImage(Toolkit.getDefaultToolkit().getImage(NewStoreAccount.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 100, 667, 374);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		getAllstores();
		setModal(true);
		int storeID=allstores.size()+1;
		JPanel panel_3 = new JPanel();
		
		panel_3.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Store Account ID : "+storeID,
				TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 14), null));
		panel_3.setBounds(0, 0, 649, 331);
		contentPane.add(panel_3);
		panel_3.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(6, 25, 321, 236);
		panel_3.add(panel_1);
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_1.setLayout(null);

		JLabel lblstoreName = new JLabel("Name :");
		lblstoreName.setBounds(6, 19, 119, 14);
		panel_1.add(lblstoreName);
		lblstoreName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblUserName = new JLabel("User Name :");
		lblUserName.setBounds(6, 56, 81, 14);
		panel_1.add(lblUserName);
		lblUserName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setBounds(6, 90, 81, 14);
		panel_1.add(lblPassword);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblConfirmPassword = new JLabel("Confirm Password :");
		lblConfirmPassword.setBounds(6, 125, 119, 14);
		panel_1.add(lblConfirmPassword);
		lblConfirmPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));

		storeNameTB = new JTextField();
		storeNameTB.setBounds(135, 16, 180, 25);
		panel_1.add(storeNameTB);
		storeNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		storeNameTB.setColumns(10);

		userNameTB = new JTextField();
		userNameTB.setBounds(135, 53, 180, 25);
		panel_1.add(userNameTB);
		userNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		userNameTB.setColumns(10);

		passwordTB = new JPasswordField();
		passwordTB.setBounds(135, 87, 180, 25);
		panel_1.add(passwordTB);
		passwordTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		passwordTB.setColumns(10);

		confPasswordTB = new JPasswordField();
		confPasswordTB.setBounds(135, 122, 180, 25);
		panel_1.add(confPasswordTB);
		confPasswordTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		confPasswordTB.setColumns(10);
		
		JLabel label = new JLabel("User Collection Report:");
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setBounds(10, 176, 131, 18);
		panel_1.add(label);
		
		 comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Yes", "No"}));
		comboBox.setFont(new Font("Tahoma", Font.BOLD, 11));
		comboBox.setBounds(151, 176, 131, 20);
		panel_1.add(comboBox);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(343, 25, 300, 236);
		panel_3.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 14), null));
		panel_2.setLayout(null);

		JLabel lblTelephone = new JLabel("Telephone No. :");
		lblTelephone.setBounds(12, 14, 104, 18);
		panel_2.add(lblTelephone);
		lblTelephone.setFont(new Font("Tahoma", Font.PLAIN, 12));

		telephoneTB = new JTextField();
		telephoneTB.setBounds(126, 11, 165, 25);
		panel_2.add(telephoneTB);
		telephoneTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		telephoneTB.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBounds(12, 129, 269, 96);
		panel_2.add(panel);
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Addresss :",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 16, 257, 73);
		panel.add(scrollPane);

		final JTextArea addressTA = new JTextArea();
		addressTA.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addressTA.setLineWrap(true);
		addressTA.setRows(4);
		scrollPane.setViewportView(addressTA);
		
				JPanel panel_4 = new JPanel();
				panel_4.setBounds(12, 54, 305, 75);
				panel_2.add(panel_4);
				panel_4.setBorder(new TitledBorder(null, "Achievement :",
						TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
								Font.PLAIN, 12), null));
				panel_4.setLayout(null);
				
						JScrollPane scrollPane_1 = new JScrollPane();
						scrollPane_1.setBounds(10, 19, 289, 47);
						panel_4.add(scrollPane_1);
						
								final JTextArea achievementTA = new JTextArea();
								achievementTA.setFont(new Font("Tahoma", Font.PLAIN, 12));
								achievementTA.setLineWrap(true);
								achievementTA.setRows(3);
								scrollPane_1.setViewportView(achievementTA);

		JButton savestoreBT = new JButton("Save");
		savestoreBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				storeName = storeNameTB.getText().toString();
				storePassword = passwordTB.getText().toString();
				storeUserName = userNameTB.getText().toString();
				storeTelephone = telephoneTB.getText().toString();
				storeAchievement = achievementTA.getText().toString();
				storeAddress = addressTA.getText().toString();
				if(comboBox.getSelectedItem().toString().equals("Yes")){
					usercollect="1";
				}else{
					usercollect="0";
				}
				Object getAlready = null;
				try {
					getAlready = allstores.get(storeUserName);
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (storeName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter store name", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					storeNameTB.requestFocus();
					return;
				} else if (getAlready != null) {
					JOptionPane.showMessageDialog(NewStoreAccount.this,
							"Can not enter duplicate username .Username already exist",
							"Duplicate Username ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				} else if (storeUserName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please Enter User Name", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					userNameTB.requestFocus();
					return;

				} else if (storePassword.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter password", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					passwordTB.requestFocus();
					return;

				} else if (!confPasswordTB.getText().toString()
						.equals(storePassword)) {
					JOptionPane.showMessageDialog(null,
							"Password does not match", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					confPasswordTB.requestFocus();
					return;

				} else if (storeTelephone.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter telephone number", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					telephoneTB.requestFocus();
					return;

				} else {
					data[0] = storeName;
					data[1] = storeUserName;
					data[2] = storePassword;
					data[3] = storeTelephone;
					data[4] = storeAddress;
					data[5] = storeAchievement;
					data[6]=usercollect;
					StoreAccountDBConnection dbConnection = new StoreAccountDBConnection();
					try {
						dbConnection.inserData(data);
						JOptionPane.showMessageDialog(null,
								"store add successfully", "Data save",
								JOptionPane.INFORMATION_MESSAGE);
						dispose();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						dbConnection.closeConnection();
					}
					dbConnection.closeConnection();
				}

			}
		});
		savestoreBT.setIcon(new ImageIcon(NewStoreAccount.class.getResource("/icons/exam_dialog.png")));
		savestoreBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		savestoreBT.setBounds(153, 272, 166, 45);
		panel_3.add(savestoreBT);

		JButton closeBT = new JButton("Cancel");
		closeBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		closeBT.setIcon(new ImageIcon(NewStoreAccount.class
				.getResource("/icons/close_button.png")));
		closeBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		closeBT.setBounds(343, 272, 166, 45);
		panel_3.add(closeBT);
	}

	public void savestoreData() {

	}

	public void getAllstores() {
		StoreAccountDBConnection dbConnection = new StoreAccountDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				allstores.put(resultSet.getObject(5).toString(), resultSet
						.getObject(6).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
}
