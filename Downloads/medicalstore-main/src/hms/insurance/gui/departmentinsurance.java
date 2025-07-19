package hms.insurance.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.DocumentException;

import hms.insurance.gui.InsuranceDBConnection;
import hms.patient.slippdf.PO_PDF;
import hms.store.database.ItemsDBConnection;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import java.awt.Dimension;
import javax.swing.DropMode;

public class departmentinsurance {

	public JFrame frame;
	private JTextField searchTXT;
	private JTextField detailtf;
	private JTable table;
	JButton btnNewButton_1;
	Vector<String> itemID = new Vector<String>();
	Vector<String> insuID = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> insuNameV = new Vector<String>();
	Vector<String> insuIDV = new Vector<String>();
	Vector<String> insuV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> expensesIndexVector = new Vector<String>();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel insuName = new DefaultComboBoxModel();

	String itemNameSTR="",itemIDSTR="",InsNameSTR="",insuranceIDSTR="";
	private JTextField insurancetf;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					departmentinsurance window = new departmentinsurance();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public departmentinsurance() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Insurance Items Status");
		frame.setResizable(false);
		frame.getContentPane().setSize(new Dimension(500, 500));
		frame.setSize(new Dimension(594, 406));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		searchTXT = new JTextField();
		detailtf = new JTextField();
		//detailtf.setDropMode(DropMode.ON);
		detailtf.setBounds(118, 65, 168, 38);


		detailtf.setColumns(10);
		detailtf.setColumns(10);
		detailtf.setBounds(118, 65, 168, 26);
		frame.getContentPane().add(detailtf);
		detailtf.setColumns(10);

		JButton btnNewButton = new JButton("Save");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((insurancetf.getText().toString().equals(""))) {
					JOptionPane.showMessageDialog(null, "Please Fill Insurance Name!",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (detailtf.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please Fill Detail Column !",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				InsuranceDBConnection db=new InsuranceDBConnection();
				String data1[] =new String[10];			
				data1[0]=insurancetf.getText().toString();
				data1[1]=detailtf.getText().toString();
				data1[2]="N/A";
				data1[3]="N/A";
				InsuranceDBConnection dbConnection=new InsuranceDBConnection();
				
					try {
						dbConnection.inserData(data1);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
				JOptionPane.showMessageDialog(null, "Data Add Successfully.",
						"Success", JOptionPane.ERROR_MESSAGE);
				searchTXT.setText("");
				populateExpensesTable();
				
				insurancetf.setText("");
				detailtf.setText("");
			}
		});
		btnNewButton.setBounds(308, 25, 117, 25);
		frame.getContentPane().add(btnNewButton);

	    btnNewButton_1 = new JButton("Remove");
	    btnNewButton_1.setEnabled(false);
		btnNewButton_1.setForeground(new Color(239, 41, 41));
		btnNewButton_1.setFont(new Font("Dialog", Font.BOLD, 12));
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int cur_selectedRow;
				
				cur_selectedRow = table.getSelectedRow();
				cur_selectedRow = table
						.convertRowIndexToModel(cur_selectedRow);
				String itemID = table.getModel()
						.getValueAt(cur_selectedRow, 0).toString();
				String InsuName = table.getModel()
						.getValueAt(cur_selectedRow, 1).toString();
				System.out.print(itemID+"ddddddd"+InsuName);
				
				int input = JOptionPane.showConfirmDialog(null, "Do you want to delete this Row !?");
				if(input==0)
				{
				InsuranceDBConnection db=new InsuranceDBConnection();
				try {
					db.deleteItemInsuRow(InsuName,itemID);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				}else
				{
					return;
				}
				JOptionPane.showMessageDialog(null, "Data Deleted Successfully.",
						"Delete", JOptionPane.ERROR_MESSAGE);
				populateExpensesTable();
				btnNewButton_1.setEnabled(false);
			}
		});
		btnNewButton_1.setBounds(308, 65, 117, 25);
		frame.getContentPane().add(btnNewButton_1);



		JLabel lblNewLabel = new JLabel("Insurance :");
		lblNewLabel.setBounds(22, 30, 88, 15);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Details :");
		lblNewLabel_1.setBounds(22, 70, 76, 15);
		frame.getContentPane().add(lblNewLabel_1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 144, 403, 107);
		frame.getContentPane().add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		populateExpensesTable();
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						//returnMedicineBT.setEnabled(true);
						btnNewButton_1.setEnabled(true);
					}
				});
	
		scrollPane.setViewportView(table);
		
		insurancetf = new JTextField();
		insurancetf.setColumns(10);
		insurancetf.setBounds(118, 27, 168, 26);
		frame.getContentPane().add(insurancetf);
		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub					
				}
			
		});
		

	}

	public void EnableComp()
	{
		btnNewButton_1.setEnabled(true);
	}
//	public void getItemName(String index) {
//
//		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
//		ResultSet resultSet = itemsDBConnection.searchItemWithIdOrNmae(index);
//		itemName.removeAllElements();
//		itemID.clear();
//		int i = 0;
//		try {
//			while (resultSet.next()) {
//				
//				itemID.add(resultSet.getObject(1).toString());
//				itemName.addElement(resultSet.getObject(2).toString());
//				i++;
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		itemsDBConnection.closeConnection();
//		
//	}
	public void populateExpensesTable() {
        itemIDV.clear();
        insuNameV.clear();
		expensesIndexVector.clear();
		try {
			InsuranceDBConnection db = new InsuranceDBConnection();
			ResultSet rs = db.retrieveInsuranceItems();

			 System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = db.retrieveInsuranceItems();

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {
				expensesIndexVector.add(rs.getObject(1).toString());
				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
					itemIDV.add(Rows_Object_Array[R][0]+"");
					insuNameV.add(Rows_Object_Array[R][2]+"");
					// System.out.println("Table: " + rs.getObject(C));
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] {"Item ID", "Item Name", "Ins Details"
							 }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
		
			table.setModel(model);
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
			table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
			table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
			table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
			table.getColumnModel().getColumn(0).setPreferredWidth(100);
			table.getColumnModel().getColumn(0).setMinWidth(100);
			table.getColumnModel().getColumn(1).setResizable(false);
			table.getColumnModel().getColumn(1).setPreferredWidth(100);
			table.getColumnModel().getColumn(1).setMinWidth(100);
		
			
			db.closeConnection();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	
	}
}

