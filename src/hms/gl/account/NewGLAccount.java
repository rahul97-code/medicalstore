package hms.gl.account;

import hms.gl.database.GLAccountDBConnection;
import hms.main.DateFormatChange;
import hms.store.database.ItemsDBConnection;
import hms.store.gui.StoreMain;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewGLAccount extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField glAccountNoTF;
	private JTextField descTF;
	private JComboBox accountType;
	private JCheckBox chckbxNewCheckBox;
	private JButton okButton;
	private JButton cancelButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewGLAccount dialog = new NewGLAccount();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public NewGLAccount() {
		setBounds(100, 100, 449, 269);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewGlAccount = new JLabel("New GL Account");
			lblNewGlAccount.setFont(new Font("Tahoma", Font.BOLD, 17));
			lblNewGlAccount.setBounds(10, 11, 254, 27);
			contentPanel.add(lblNewGlAccount);
		}
		{
			JLabel lblGlAccountNo = new JLabel("GL Account No.");
			lblGlAccountNo.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblGlAccountNo.setBounds(20, 49, 107, 20);
			contentPanel.add(lblGlAccountNo);
		}
		
		glAccountNoTF = new JTextField();
		glAccountNoTF.setFont(new Font("Tahoma", Font.BOLD, 12));
		glAccountNoTF.setBounds(135, 49, 273, 20);
		contentPanel.add(glAccountNoTF);
		glAccountNoTF.setColumns(10);
		
		JLabel lblDescription = new JLabel("Description");
		lblDescription.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblDescription.setBounds(20, 80, 107, 20);
		contentPanel.add(lblDescription);
		
		descTF = new JTextField();
		descTF.setFont(new Font("Tahoma", Font.BOLD, 12));
		descTF.setColumns(10);
		descTF.setBounds(135, 80, 273, 20);
		contentPanel.add(descTF);
		
		JLabel lblGlAccountType = new JLabel("GL Account Type");
		lblGlAccountType.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblGlAccountType.setBounds(20, 123, 107, 20);
		contentPanel.add(lblGlAccountType);
		
		JLabel lblGlAccountActive = new JLabel("GL Account Active");
		lblGlAccountActive.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblGlAccountActive.setBounds(20, 165, 118, 20);
		contentPanel.add(lblGlAccountActive);
		
		accountType = new JComboBox();
		accountType.setModel(new DefaultComboBoxModel(new String[] {"Cash", "Accounts Receivable", "Inventory", "Other Current Asset", "Fixed Asset", "Accumulated Depreciation", "Other Asset", "Accounts Payable", "Other Current Liabilities", "Income", "Cost of Sales", "Expenses", "Equity - Doesn't Close", "Equity - Gets Closed", "Equity - Retained Earnings"}));
		accountType.setFont(new Font("Tahoma", Font.BOLD, 12));
		accountType.setBounds(135, 124, 273, 19);
		contentPanel.add(accountType);
		
		chckbxNewCheckBox = new JCheckBox("");
		chckbxNewCheckBox.setSelected(true);
		chckbxNewCheckBox.setBounds(142, 165, 262, 23);
		contentPanel.add(chckbxNewCheckBox);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						if(glAccountNoTF.getText().toString().equals("")||descTF.getText().toString().equals(""))
						{
							
						}else {
							long timeInMillis = System.currentTimeMillis();
							Calendar cal1 = Calendar.getInstance();
							cal1.setTimeInMillis(timeInMillis);
							SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
							String[] data=new String[30];
							
							data[0]=glAccountNoTF.getText().toString();
							data[1]=descTF.getText().toString();
							data[2]=accountType.getSelectedItem().toString();
							
							data[3]=DateFormatChange.StringToMysqlDate(new Date());
							data[4]=""+timeFormat.format(cal1.getTime());
							data[5]=""+StoreMain.userName;
						
							GLAccountDBConnection glAccountDBConnection=new GLAccountDBConnection();
							
							try {
								glAccountDBConnection.inserGLAccountSupplier(data);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								JOptionPane.showMessageDialog(null,
										"Duplicate entry GL Account", "Duplicate entry",
										JOptionPane.ERROR_MESSAGE);
								return;
							}
							
							glAccountDBConnection.closeConnection();
							JOptionPane.showMessageDialog(null,
									"GL Account Saved Successfully", "Save GL",
									JOptionPane.INFORMATION_MESSAGE);
							dispose();
						}
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public JButton getOkButton() {
		return okButton;
	}
	public JButton getCancelButton() {
		return cancelButton;
	}
}
