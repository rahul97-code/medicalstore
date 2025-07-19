package hms.store.gui;

import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;

import hms.store.database.CancelRestockFeeDB;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import javax.swing.JCheckBox;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class CuttingCharges {

	public JFrame frame;
	String tax1="",tax2="",tax3="";
	Vector<String> ID = new Vector<String>();
	Vector<String> CuttingFeeV = new Vector<String>();
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CuttingCharges window = new CuttingCharges();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	/**
	 * Initialize the contents of the frame.
	 
	 */
	public CuttingCharges()
	{
		Initialize();
	}
	
	public void Initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Cancellation Restock fee");
		frame.setBounds(100, 100, 278, 267);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton_1 = new JButton("CANCEL");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnNewButton_1.setBounds(155, 190, 106, 25);
		frame.getContentPane().add(btnNewButton_1);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(37, 65, 70, 19);
		frame.getContentPane().add(textField_3);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(37, 101, 70, 19);
		frame.getContentPane().add(textField_4);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(37, 132, 70, 19);
		frame.getContentPane().add(textField_5);
		
			
		
		JButton btnNewButton_1_1 = new JButton("SAVE");
		btnNewButton_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				CuttingFeeV.clear();
				
				CuttingFeeV.add(0, textField_3.getText());
				CuttingFeeV.add(1, textField_4.getText());
				CuttingFeeV.add(2, textField_5.getText());
				System.out.println(CuttingFeeV);
			
				CancelRestockFeeDB db=new CancelRestockFeeDB();
				
				for(int i=0;i<ID.size();i++)
				{
					try {
						db.updateData1(CuttingFeeV.get(i),ID.get(i)+"");
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				JOptionPane.showMessageDialog(null,
						"Data saved Successfully.", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				frame.dispose();
				
				db.closeConnection();
			}
		});
		btnNewButton_1_1.setBounds(37, 190, 106, 25);
		frame.getContentPane().add(btnNewButton_1_1);
		
		
		
		JLabel lblNewLabel_1_3 = new JLabel("%");
		lblNewLabel_1_3.setBounds(117, 65, 29, 15);
		frame.getContentPane().add(lblNewLabel_1_3);
		
		JLabel lblNewLabel_1_4 = new JLabel("%");
		lblNewLabel_1_4.setBounds(117, 103, 29, 15);
		frame.getContentPane().add(lblNewLabel_1_4);
		
		JLabel lblNewLabel_1_5 = new JLabel("%");
		lblNewLabel_1_5.setBounds(117, 134, 29, 15);
		frame.getContentPane().add(lblNewLabel_1_5);
		
		JLabel lblNewLabel = new JLabel("Low");
		lblNewLabel.setBounds(150, 69, 70, 15);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Meduim");
		lblNewLabel_1.setBounds(150, 103, 70, 15);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblHigh = new JLabel("High");
		lblHigh.setBounds(150, 134, 70, 15);
		frame.getContentPane().add(lblHigh);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Cutting Charges %", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		lblNewLabel_2.setBounds(22, 44, 239, 135);
		frame.getContentPane().add(lblNewLabel_2);
		
		final JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String type=comboBox.getSelectedItem()+"";
				if (comboBox.getSelectedIndex()!=0) {
					getData(type);
				}
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Select", "OPD", "IPD"}));
		comboBox.setBounds(37, 8, 166, 24);
		frame.getContentPane().add(comboBox);
	}

	private void getData(String type) {
		// TODO Auto-generated method stub
		CancelRestockFeeDB db=new CancelRestockFeeDB();
		ResultSet rs=db.retrieveDataNew(type);
		ID.clear();
		CuttingFeeV.clear();
		try {
			while(rs.next())
			{
				Object ob=rs.getObject(1);
				ID.add(ob==null?"":ob.toString());
				CuttingFeeV.add(rs.getObject(2).toString());
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		db.closeConnection();
		textField_3.setText(CuttingFeeV.get(0) + "");
		textField_4.setText(CuttingFeeV.get(1) + "");
		textField_5.setText(CuttingFeeV.get(2) + "");
	}
}
