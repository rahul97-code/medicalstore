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

public class CancelRestockFee {

	public JFrame frame;
	private JTextField restocktf1;
	private JTextField restocktf2;
	private JTextField restocktf3;
	String tax1="",tax2="",tax3="";
	JComboBox comboBoxTD1;
	JComboBox comboBoxTD2;
	JComboBox comboBoxTD3;
	JComboBox comboBoxTM1;
	JComboBox comboBoxTM3;
	JComboBox comboBoxTM2;
	JCheckBox checkBoxtf1;
	JCheckBox checkBoxtf2;
	JCheckBox checkBoxtf3;
	Vector<String> ID = new Vector<String>();
	Vector<String> checkboxV = new Vector<String>();
	Vector<String> CuttingFeeV = new Vector<String>();
	Vector<String> RestockFee = new Vector<String>();
	Vector<String> RestockFeeV = new Vector<String>();
	Vector<String> DaysV = new Vector<String>();
	Vector<String> MonthsV = new Vector<String>();
	
	final DefaultComboBoxModel RestockD2 = new DefaultComboBoxModel();
	final DefaultComboBoxModel RestockM2 = new DefaultComboBoxModel();
	private JTextField cuttingtf1;
	private JTextField cuttingtf2;
	private JTextField cuttingtf3;
	private JComboBox comboBoxtype;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CancelRestockFee window = new CancelRestockFee();
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
	public CancelRestockFee()
	{
		Initialize();
	}
	
	public void Initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Cancellation Restock fee");
		frame.setBounds(100, 100, 632, 257);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton_1 = new JButton("CANCEL");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnNewButton_1.setBounds(418, 183, 106, 25);
		frame.getContentPane().add(btnNewButton_1);
		
		restocktf1 = new JTextField();
		restocktf1.setBounds(268, 63, 70, 19);
		frame.getContentPane().add(restocktf1);
		restocktf1.setColumns(10);
		
		restocktf2 = new JTextField();
		restocktf2.setBounds(268, 99, 70, 19);
		frame.getContentPane().add(restocktf2);
		restocktf2.setColumns(10);
		
		restocktf3 = new JTextField();
		restocktf3.setBounds(268, 130, 70, 19);
		frame.getContentPane().add(restocktf3);
		restocktf3.setColumns(10);
		
		cuttingtf1 = new JTextField();
		cuttingtf1.setColumns(10);
		cuttingtf1.setBounds(402, 63, 70, 19);
		frame.getContentPane().add(cuttingtf1);
		
		cuttingtf2 = new JTextField();
		cuttingtf2.setColumns(10);
		cuttingtf2.setBounds(402, 99, 70, 19);
		frame.getContentPane().add(cuttingtf2);
		
		cuttingtf3 = new JTextField();
		cuttingtf3.setColumns(10);
		cuttingtf3.setBounds(402, 130, 70, 19);
		frame.getContentPane().add(cuttingtf3);
		
		comboBoxTD1 = new JComboBox();
		comboBoxTD1.setModel(new DefaultComboBoxModel(new String[] {"Days", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
		comboBoxTD1.setBounds(140, 63, 70, 19);
		frame.getContentPane().add(comboBoxTD1);
		
		comboBoxTD2 = new JComboBox();
		comboBoxTD2.setModel(new DefaultComboBoxModel(new String[] {"Days", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
		comboBoxTD2.setBounds(140, 99, 70, 19);
		frame.getContentPane().add(comboBoxTD2);
		
		comboBoxTD3 = new JComboBox();
		comboBoxTD3.setModel(new DefaultComboBoxModel(new String[] {"Days", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
		comboBoxTD3.setBounds(140, 130, 70, 19);
		frame.getContentPane().add(comboBoxTD3);
		
		comboBoxTM1 = new JComboBox();
		comboBoxTM1.setModel(new DefaultComboBoxModel(new String[] {"Months", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}));
		comboBoxTM1.setBounds(22, 63, 106, 19);
		frame.getContentPane().add(comboBoxTM1);
		
		comboBoxTM3 = new JComboBox();
		comboBoxTM3.setModel(new DefaultComboBoxModel(new String[] {"Months", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}));
		comboBoxTM3.setBounds(22, 130, 106, 19);
		frame.getContentPane().add(comboBoxTM3);
		 
		comboBoxtype = new JComboBox();
		comboBoxtype.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str=comboBoxtype.getSelectedItem()+"";
				if(!str.equals("Select Type"))
				getData();
			}
		});
			comboBoxtype.setModel(new DefaultComboBoxModel(new String[] {"Select Type", "OPD", "IPD"}));
			comboBoxtype.setBounds(24, 12, 125, 19);
			frame.getContentPane().add(comboBoxtype);
			
		comboBoxTM2 = new JComboBox();
		comboBoxTM2.setModel(new DefaultComboBoxModel(new String[] {"Months", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}));
		comboBoxTM2.setBounds(22, 99, 106, 19);
		frame.getContentPane().add(comboBoxTM2);
		
		checkBoxtf1 = new JCheckBox("");
		checkBoxtf1.setBounds(560, 57, 41, 25);
		frame.getContentPane().add(checkBoxtf1);
		
		checkBoxtf2 = new JCheckBox("");
		checkBoxtf2.setBounds(560, 93, 41, 25);
		frame.getContentPane().add(checkBoxtf2);
		
		checkBoxtf3 = new JCheckBox("");
		checkBoxtf3.setBounds(560, 130, 41, 25);
		frame.getContentPane().add(checkBoxtf3);
		
        getData();
		
		
		JButton btnNewButton_1_1 = new JButton("SAVE");
		btnNewButton_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RestockFeeV.clear();
				DaysV.clear();
				MonthsV.clear();
				String[] data=new String[5];
				CancelRestockFeeDB db=new CancelRestockFeeDB();
				if(comboBoxtype.getSelectedItem().toString().equals("Select Type"))
				{
					JOptionPane.showMessageDialog(null,
							"Please Select Type", "failed",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(comboBoxtype.getSelectedItem().toString().equals("OPD")) {
				checkboxV.add(0,checkBoxtf1.isSelected()?"1":"0");
				checkboxV.add(1,checkBoxtf2.isSelected()?"1":"0");
				checkboxV.add(2,checkBoxtf3.isSelected()?"1":"0");
				
				RestockFeeV.add(0, restocktf1.getText());
				RestockFeeV.add(1, restocktf2.getText());
				RestockFeeV.add(2, restocktf3.getText());
				
				CuttingFeeV.add(0, cuttingtf1.getText());
				CuttingFeeV.add(1, cuttingtf2.getText());
				CuttingFeeV.add(2, cuttingtf3.getText());
				
				DaysV.add(0, comboBoxTD1.getSelectedItem().toString().equals("Days")?"0":comboBoxTD1.getSelectedItem().toString());
				DaysV.add(1, comboBoxTD2.getSelectedItem().toString().equals("Days")?"0":comboBoxTD2.getSelectedItem().toString());
				DaysV.add(2, comboBoxTD3.getSelectedItem().toString().equals("Days")?"0":comboBoxTD3.getSelectedItem().toString());
				
				MonthsV.add(0, comboBoxTM1.getSelectedItem().toString().equals("Months")?"0":comboBoxTM1.getSelectedItem().toString());
				MonthsV.add(1, comboBoxTM3.getSelectedItem().toString().equals("Months")?"0":comboBoxTM3.getSelectedItem().toString());
				MonthsV.add(2, comboBoxTM2.getSelectedItem().toString().equals("Months")?"0":comboBoxTM2.getSelectedItem().toString());
				for(int id=1;id<=3;id++)
				{
					data[0]=RestockFeeV.get(id-1);
					data[1]=DaysV.get(id-1);
					data[2]=MonthsV.get(id-1);
					data[3]=checkboxV.get(id-1);
					data[4]=CuttingFeeV.get(id-1);
					
					db.updateData(data, id+"",comboBoxtype.getSelectedItem().toString());
				}
				JOptionPane.showMessageDialog(null,
						"OPD Restock Data saved Successfully.", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				frame.dispose();
				}
				else if(comboBoxtype.getSelectedItem().toString().equals("IPD")) {
					
					checkboxV.add(0,checkBoxtf1.isSelected()?"1":"0");
					checkboxV.add(1,checkBoxtf2.isSelected()?"1":"0");
					checkboxV.add(2,checkBoxtf3.isSelected()?"1":"0");
					
					RestockFeeV.add(0, restocktf1.getText());
					RestockFeeV.add(1, restocktf2.getText());
					RestockFeeV.add(2, restocktf3.getText());
					
					CuttingFeeV.add(0, cuttingtf1.getText());
					CuttingFeeV.add(1, cuttingtf2.getText());
					CuttingFeeV.add(2, cuttingtf3.getText());
					
					DaysV.add(0, comboBoxTD1.getSelectedItem().toString().equals("Days")?"0":comboBoxTD1.getSelectedItem().toString());
					DaysV.add(1, comboBoxTD2.getSelectedItem().toString().equals("Days")?"0":comboBoxTD2.getSelectedItem().toString());
					DaysV.add(2, comboBoxTD3.getSelectedItem().toString().equals("Days")?"0":comboBoxTD3.getSelectedItem().toString());
					
					MonthsV.add(0, comboBoxTM1.getSelectedItem().toString().equals("Months")?"0":comboBoxTM1.getSelectedItem().toString());
					MonthsV.add(1, comboBoxTM3.getSelectedItem().toString().equals("Months")?"0":comboBoxTM3.getSelectedItem().toString());
					MonthsV.add(2, comboBoxTM2.getSelectedItem().toString().equals("Months")?"0":comboBoxTM2.getSelectedItem().toString());
					for(int id=1;id<=3;id++)
					{
						data[0]=RestockFeeV.get(id-1);
						data[1]=DaysV.get(id-1);
						data[2]=MonthsV.get(id-1);
						data[3]=checkboxV.get(id-1);
						data[4]=CuttingFeeV.get(id-1);
						
						db.updateData(data, id+3+"",comboBoxtype.getSelectedItem().toString());
					}
					JOptionPane.showMessageDialog(null,
							"IPD Restock Data saved Successfully.", "Success",
							JOptionPane.INFORMATION_MESSAGE);
					frame.dispose();
				}
				db.closeConnection();
			}
		});
		btnNewButton_1_1.setBounds(286, 183, 106, 25);
		frame.getContentPane().add(btnNewButton_1_1);
		
		JLabel lblTax_1 = new JLabel("");
		lblTax_1.setBorder(new TitledBorder(null, "Till Date From Sale ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		lblTax_1.setBounds(12, 36, 222, 135);
		frame.getContentPane().add(lblTax_1);
		
		JLabel lblNewLabel_1 = new JLabel("%");
		lblNewLabel_1.setBounds(346, 67, 29, 15);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("%");
		lblNewLabel_1_1.setBounds(346, 103, 29, 15);
		frame.getContentPane().add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("%");
		lblNewLabel_1_2.setBounds(346, 134, 29, 15);
		frame.getContentPane().add(lblNewLabel_1_2);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBorder(new TitledBorder(null, "Restock Fee %", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		lblNewLabel.setBounds(246, 36, 131, 135);
		frame.getContentPane().add(lblNewLabel);
		
		
		
		JLabel lblReturnGst = new JLabel("");
		lblReturnGst.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Return GST", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(51, 51, 51)));
		lblReturnGst.setBounds(523, 36, 97, 135);
		frame.getContentPane().add(lblReturnGst);
		
		
		
		JLabel lblNewLabel_1_3 = new JLabel("%");
		lblNewLabel_1_3.setBounds(482, 63, 29, 15);
		frame.getContentPane().add(lblNewLabel_1_3);
		
		JLabel lblNewLabel_1_4 = new JLabel("%");
		lblNewLabel_1_4.setBounds(482, 101, 29, 15);
		frame.getContentPane().add(lblNewLabel_1_4);
		
		JLabel lblNewLabel_1_5 = new JLabel("%");
		lblNewLabel_1_5.setBounds(482, 132, 29, 15);
		frame.getContentPane().add(lblNewLabel_1_5);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Cutting Fee %", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		lblNewLabel_2.setBounds(387, 36, 125, 135);
		frame.getContentPane().add(lblNewLabel_2);
	}

	private void getData() {
		// TODO Auto-generated method stub
		ID.clear();
		RestockFee.clear();
		RestockD2.removeAllElements();
		RestockM2.removeAllElements();
		checkboxV.clear();
		CuttingFeeV.clear();
		CancelRestockFeeDB db=new CancelRestockFeeDB();
		ResultSet rs=db.retrieveData(comboBoxtype.getSelectedItem().toString());
		
		try {
			while(rs.next())
			{	
				ID.add(rs.getString(1));
				RestockFee.add(rs.getObject(2).toString());
				RestockD2.addElement(rs.getObject(3).toString());
				RestockM2.addElement(rs.getObject(4).toString());
				checkboxV.add(rs.getObject(5).toString());
				CuttingFeeV.add(rs.getObject(6).toString());
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(ID.size()>0)
		{
			comboBoxTD1.setSelectedIndex(Integer.parseInt(RestockD2.getElementAt(0).toString()));
			comboBoxTD2.setSelectedIndex(Integer.parseInt(RestockD2.getElementAt(1).toString()));
			comboBoxTD3.setSelectedIndex(Integer.parseInt(RestockD2.getElementAt(2).toString()));
			comboBoxTM1.setSelectedIndex(Integer.parseInt(RestockM2.getElementAt(0).toString()));
			comboBoxTM3.setSelectedIndex(Integer.parseInt(RestockM2.getElementAt(2).toString()));
			comboBoxTM2.setSelectedIndex(Integer.parseInt(RestockM2.getElementAt(1).toString()));
			checkBoxtf1.setSelected(Boolean.parseBoolean(checkboxV.get(0).toString()));
			checkBoxtf2.setSelected(Boolean.parseBoolean(checkboxV.get(1).toString()));
			checkBoxtf3.setSelected(Boolean.parseBoolean(checkboxV.get(2).toString()));
		}
		if (RestockFee.size()>0) {
			restocktf1.setText(RestockFee.get(0) + "");
			restocktf2.setText(RestockFee.get(1) + "");
			restocktf3.setText(RestockFee.get(2) + "");
			
			cuttingtf1.setText(CuttingFeeV.get(0) + "");
			cuttingtf2.setText(CuttingFeeV.get(1) + "");
			cuttingtf3.setText(CuttingFeeV.get(2) + "");
		}
		db.closeConnection();
	}
}
