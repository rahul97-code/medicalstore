package hms.store.gui;

import hms.store.database.StoreAccountDBConnection;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
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

public class EditStoreAccount extends JDialog {

	private JPanel contentPane;
	private JTextField storeNameTB;
	private JTextField userNameTB;
	private JPasswordField passwordTB;
	private JPasswordField confPasswordTB;
	private JTextField telephoneTB;
	String storeName = "", storeID = "", storeUserName = "", storePassword = "",
			storeTelephone = "",
			storeAddress = "", storeAchievement = "",usercollection="",useractivecollection="",updateaccess="";
	private HashMap allstores = new HashMap();
	String[] data = new String[20];
	String usercollect="",user_active="";
	JComboBox comboBox ;
	private JComboBox comboBox_active;
	private JComboBox comboBox_update_access;

	/**
	 * Create the frame.
	 */
	public EditStoreAccount(String opID) { 
		setTitle("Edit Store Account");
		setIconImage(Toolkit.getDefaultToolkit().getImage(EditStoreAccount.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 100, 667, 374);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		getAllstores();
		storeID=opID;
		 comboBox = new JComboBox();
			comboBox.setBounds(160, 149, 131, 20);
			comboBox.setFont(new Font("Tahoma", Font.BOLD, 11));
			
			comboBox.setModel(new DefaultComboBoxModel(new String[] { "Yes",
					"No"}));
			comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
			comboBox_active = new JComboBox();
			comboBox_active.setModel(new DefaultComboBoxModel(new String[] {"Yes", "No"}));
			comboBox_active.setFont(new Font("Dialog", Font.PLAIN, 11));
			comboBox_active.setBounds(160, 181, 131, 20);
			comboBox_update_access = new JComboBox();
			comboBox_update_access.setModel(new DefaultComboBoxModel(new String[] {"Yes", "No"}));
			comboBox_update_access.setFont(new Font("Dialog", Font.PLAIN, 11));
			comboBox_update_access.setBounds(160, 213, 131, 20);
		getDoctorDetail(storeID);
		JPanel panel_3 = new JPanel();
		
		panel_3.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Store Account ID : "+storeID,
				TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 14), null));
		panel_3.setBounds(0, 0, 649, 331);
		contentPane.add(panel_3);
		panel_3.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(6, 25, 321, 246);
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

		storeNameTB = new JTextField(storeName);
		storeNameTB.setBounds(135, 16, 180, 25);
		panel_1.add(storeNameTB);
		storeNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		storeNameTB.setColumns(10);

		userNameTB = new JTextField(storeUserName);
		userNameTB.setEditable(false);
		userNameTB.setBounds(135, 53, 180, 25);
		panel_1.add(userNameTB);
		userNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		userNameTB.setColumns(10);

		passwordTB = new JPasswordField(storePassword);
		passwordTB.setEditable(false);
		passwordTB.setBounds(135, 87, 180, 25);
		panel_1.add(passwordTB);
		passwordTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		passwordTB.setColumns(10);

		confPasswordTB = new JPasswordField(storePassword);
		confPasswordTB.setEditable(false);
		confPasswordTB.setBounds(135, 122, 180, 25);
		panel_1.add(confPasswordTB);
		confPasswordTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		confPasswordTB.setColumns(10);

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Achievement :",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_4.setBounds(6, 150, 305, 75);
		panel_1.add(panel_4);
		panel_4.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 19, 289, 47);
		panel_4.add(scrollPane_1);

		final JTextArea achievementTA = new JTextArea(storeAchievement);
		achievementTA.setFont(new Font("Tahoma", Font.PLAIN, 12));
		achievementTA.setLineWrap(true);
		achievementTA.setRows(3);
		scrollPane_1.setViewportView(achievementTA);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(343, 25, 300, 246);
		panel_3.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 14), null));
		panel_2.setLayout(null);

		JLabel lblTelephone = new JLabel("Telephone No. :");
		lblTelephone.setBounds(12, 14, 104, 18);
		panel_2.add(lblTelephone);
		lblTelephone.setFont(new Font("Tahoma", Font.PLAIN, 12));

		telephoneTB = new JTextField(storeTelephone);
		telephoneTB.setBounds(126, 11, 165, 25);
		panel_2.add(telephoneTB);
		telephoneTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		telephoneTB.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBounds(12, 43, 269, 96);
		panel_2.add(panel);
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Addresss :",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 16, 257, 73);
		panel.add(scrollPane);

		final JTextArea addressTA = new JTextArea(storeAddress);
		addressTA.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addressTA.setLineWrap(true);
		addressTA.setRows(4);
		scrollPane.setViewportView(addressTA); 
		
		panel_2.add(comboBox_active);
		panel_2.add(comboBox);
		panel_2.add(comboBox_update_access);
		
		JLabel lblUserCollectionReport = new JLabel("User Collection Report:");
		lblUserCollectionReport.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUserCollectionReport.setBounds(12, 150, 131, 18);
		panel_2.add(lblUserCollectionReport);
		
		
		
		
		JLabel lbluser_active = new JLabel("User Active :");
		lbluser_active.setFont(new Font("Dialog", Font.PLAIN, 12));
		lbluser_active.setBounds(12, 181, 130, 18);
		panel_2.add(lbluser_active);
		
		JLabel lbluser_active_1 = new JLabel("Item Update Access :");
		lbluser_active_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lbluser_active_1.setBounds(12, 213, 141, 18);
		panel_2.add(lbluser_active_1);
		
		 
	

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
				if(comboBox_active.getSelectedItem().toString().equals("Yes")){
					user_active="1";
				}else{
					user_active="0";
				}
				if(comboBox_update_access.getSelectedItem().toString().equals("Yes")){
					updateaccess="1";
				}else{
					updateaccess="0";
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
				}  else if (storeUserName.equals("")) {
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
					data[9] = storeID;
					data[6]=usercollect;
					data[7]=user_active;
					data[8]=updateaccess;
					System.out.println("usercollectusercollect"+usercollect+"");
					StoreAccountDBConnection dbConnection = new StoreAccountDBConnection();
					try {
						dbConnection.updateData(data);
						JOptionPane.showMessageDialog(null,
								"store updated successfully", "Data save",
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
		savestoreBT.setIcon(new ImageIcon(EditStoreAccount.class.getResource("/icons/exam_dialog.png")));
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
		closeBT.setIcon(new ImageIcon(EditStoreAccount.class
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
	
	public void getDoctorDetail(String DocId) {
		
		StoreAccountDBConnection dbConnection = new StoreAccountDBConnection();
		ResultSet resultSet = dbConnection.retrieveDataWithID(DocId);
		try {
			while (resultSet.next()) {
				storeName = resultSet.getObject(1).toString();
				storeUserName=resultSet.getObject(2)
						.toString();
				storePassword=resultSet.getObject(3)
						.toString();
				storeTelephone=resultSet.getObject(4)
						.toString();
				storeAddress=resultSet.getObject(5)
						.toString();
				storeAchievement=resultSet.getObject(6)
						.toString();
				usercollection=resultSet.getObject(7)
						.toString();

				useractivecollection=resultSet.getObject(8)
						.toString();
				updateaccess=resultSet.getObject(9)
						.toString();
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("usercollection"+usercollection);
		if(usercollection.equals("0")){
			comboBox.setSelectedItem("No");
		}else{
			comboBox.setSelectedItem("Yes");
		}
		if(useractivecollection.equals("0")){
			comboBox_active.setSelectedItem("No");
		}else{
			comboBox_active.setSelectedItem("Yes");
		}
		if(updateaccess.equals("0")){
			comboBox_update_access.setSelectedItem("No");
		}else{
			comboBox_update_access.setSelectedItem("Yes");
		}
		dbConnection.closeConnection();
	}
}
