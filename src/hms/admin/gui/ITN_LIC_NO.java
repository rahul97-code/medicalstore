package hms.admin.gui;

import hms.insurance.gui.InsuranceDBConnection;
import hms.store.database.SuppliersDBConnection;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ITN_LIC_NO extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField licNoTF;
	private JTextField tinNoTF;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ITN_LIC_NO dialog = new ITN_LIC_NO();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ITN_LIC_NO() {
		setBounds(100, 100, 450, 189);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Drug Lic. No.");
			lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
			lblNewLabel.setBounds(10, 39, 120, 22);
			contentPanel.add(lblNewLabel);
		}
		{
			licNoTF = new JTextField();
			licNoTF.setFont(new Font("Tahoma", Font.BOLD, 13));
			licNoTF.setBounds(140, 39, 272, 22);
			contentPanel.add(licNoTF);
			licNoTF.setColumns(10);
		}
		{
			JLabel lblTinNo = new JLabel("TIN No. ");
			lblTinNo.setFont(new Font("Tahoma", Font.BOLD, 13));
			lblTinNo.setBounds(10, 81, 120, 22);
			contentPanel.add(lblTinNo);
		}
		{
			tinNoTF = new JTextField();
			tinNoTF.setFont(new Font("Tahoma", Font.BOLD, 13));
			tinNoTF.setColumns(10);
			tinNoTF.setBounds(140, 81, 272, 22);
			contentPanel.add(tinNoTF);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						
						if(tinNoTF.getText().toString().equals("")||licNoTF.getText().toString().equals(""))
						{
							JOptionPane.showMessageDialog(null, "Please Fill all fields properly",
									"Input Error", JOptionPane.ERROR_MESSAGE);
						}else{
							String data[] =new String[10];
							data[0]=licNoTF.getText().toString();
							data[1]=tinNoTF.getText().toString();
						
							StoreInfoDBConnection dbConnection=new StoreInfoDBConnection();
							try {
								dbConnection.inserData(data);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							dbConnection.closeConnection();
							JOptionPane.showMessageDialog(null, "Info Saved Successfully",
									"Info save", JOptionPane.INFORMATION_MESSAGE);
							dispose();
						}
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		getInfo();
	}
	public void getInfo() {

		StoreInfoDBConnection dbConnection=new StoreInfoDBConnection();
		ResultSet resultSet = dbConnection
				.retrieveAllData();
		
		try {
			while (resultSet.next()) {
				licNoTF.setText(resultSet.getObject(2).toString());
				tinNoTF.setText(resultSet.getObject(3).toString());
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
