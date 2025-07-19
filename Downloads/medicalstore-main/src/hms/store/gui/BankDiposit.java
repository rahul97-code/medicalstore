package hms.store.gui;

import hms.gl.database.GLAccountDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.store.database.SuppliersDBConnection;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import com.toedter.calendar.JDateChooser;

public class BankDiposit extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField descTF;
	final DefaultComboBoxModel bankModel = new DefaultComboBoxModel();
	private JComboBox bankHeadCB;
	String headDisplaySTR="";
	private JTextField paymentTF;
	private JDateChooser selectDate;
	
	String dateSTR="";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			BankDiposit dialog = new BankDiposit();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public BankDiposit() {
		setTitle("Bank Diposit");
		setBounds(100, 100, 456, 238);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		bankHeadCB = new JComboBox();
		bankHeadCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bankHeadCB.setBounds(171, 11, 218, 25);
		contentPanel.add(bankHeadCB);
		bankHeadCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					headDisplaySTR = bankHeadCB.getSelectedItem()
							.toString();
				} catch (Exception e) {
					// TODO: handle exception

				}
				
			}
		});
		
		JLabel lblSelectInsurance = new JLabel("Select Bank");
		lblSelectInsurance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSelectInsurance.setBounds(35, 11, 126, 25);
		contentPanel.add(lblSelectInsurance);
		
		descTF = new JTextField();
		descTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		descTF.setColumns(10);
		descTF.setBounds(171, 47, 218, 25);
		contentPanel.add(descTF);
		
		JLabel lblDesc = new JLabel("Desc :");
		lblDesc.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDesc.setBounds(35, 47, 126, 25);
		contentPanel.add(lblDesc);
		
		JLabel lblPayment = new JLabel("Payment");
		lblPayment.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPayment.setBounds(35, 83, 126, 25);
		contentPanel.add(lblPayment);
		
		paymentTF = new JTextField();
		paymentTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		paymentTF.setColumns(10);
		paymentTF.setBounds(171, 83, 218, 25);
		contentPanel.add(paymentTF);
		
		selectDate = new JDateChooser();
		selectDate.setBounds(171, 119, 218, 20);
		contentPanel.add(selectDate);
		
		selectDate.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							
						}
					}
				});
		
		
		JLabel lblSelectDate = new JLabel("Select Date");
		lblSelectDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSelectDate.setBounds(35, 119, 126, 25);
		contentPanel.add(lblSelectDate);
		paymentTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						if(descTF.getText().toString().equals("")||paymentTF.getText().toString().equals("")||dateSTR.equals(""))
						{
							JOptionPane.showMessageDialog(null,
									"Please fill all fileds properlyy", "Input Error",
									JOptionPane.ERROR_MESSAGE);
						}
						else {
							long timeInMillis = System.currentTimeMillis();
							Calendar cal1 = Calendar.getInstance();
							cal1.setTimeInMillis(timeInMillis);
							SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
							String[] data = new String[30];
							data[0]=""+(bankHeadCB.getSelectedIndex()+1);
							data[1]=headDisplaySTR;
							data[2]="BANK";
							data[3]=""+ paymentTF.getText().toString();
							data[4]="0";
							data[5]="0";
							data[6]="0";
							data[7]=dateSTR;
							data[8]=""+timeFormat.format(cal1.getTime());
							data[9]=""+StoreMain.userName;
							data[10]=descTF.getText().toString();
						
							GLAccountDBConnection glAccountDBConnection=new GLAccountDBConnection();
							
							try {
								glAccountDBConnection.inserGLAccountBank(data);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								
							}
							
							glAccountDBConnection.closeConnection();
							JOptionPane.showMessageDialog(null,
									"Payment saved successfully", "Payment Save",
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
		getAllBanks();
	}
	
	
	public void getAllBanks() {
		bankModel.addElement("Yes Bank");
//		InsuranceDBConnection dbConnection = new InsuranceDBConnection();
//		ResultSet resultSet = dbConnection.retrieveAllData();
//		try {
//			while (resultSet.next()) {
//				bankModel.addElement(resultSet.getObject(2).toString());
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		dbConnection.closeConnection();
		bankHeadCB.setModel(bankModel);
		bankHeadCB.setSelectedIndex(0);
	}
	public JDateChooser getSelectDate() {
		return selectDate;
	}
}
