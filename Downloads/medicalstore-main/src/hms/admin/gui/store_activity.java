package hms.admin.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import hms.main.DateFormatChange;
import hms.store.database.StoreAccountDBConnection;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.toedter.calendar.JDateChooser;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class store_activity extends JDialog {

	public JFrame frmStoreActivity;
	private JTable table;
	final DefaultComboBoxModel username = new DefaultComboBoxModel();
	private JComboBox comboBox;
	private String user,FromDate,ToDate;
	private JTextField textField;
	private JComboBox departmentCB;
	private String department;
	private JDateChooser dateFromDC;
	private JDateChooser dateToDC;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					store_activity window = new store_activity();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
       public void users() {
    	   StoreAccountDBConnection storeAccountDBConnection = new StoreAccountDBConnection();
    	   ResultSet rs= storeAccountDBConnection.retrieveAllReceptionName(department);
    	   username.removeAllElements();
    	   username.addElement("Select User");
    	  try {
			while(rs.next()) {
				username.addElement(rs.getString(2));
			  }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	  if(username.getSize()>0)
    	  comboBox.setModel(username);
       }
       public void activity() {
    	   StoreAccountDBConnection storeAccountDBConnection = new StoreAccountDBConnection();
    	   ResultSet rs= storeAccountDBConnection.retrieveuseractivity(user,department,FromDate,ToDate);
    	   int NumberOfColumns = 0, NumberOfRows = 0;
			try {
				NumberOfColumns = rs.getMetaData().getColumnCount();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				while (rs.next()) {
					NumberOfRows++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				rs.beforeFirst();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			try {
				while (rs.next()) {
					for (int C = 1; C <= NumberOfColumns; C++) {
						Rows_Object_Array[R][C - 1] = rs.getObject(C);
					}
					R++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "ID", "User Name", "In Date","In time","Out Date",
							"Out Time","Report Access",
							 }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table.setModel(model);
			
			storeAccountDBConnection.closeConnection();
		
		}
       
	/**
	 * Create the application.
	 */
	public store_activity() {
		initialize();
		users();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
	
		setTitle("MedicalStore Activity");
		setBounds(100, 100, 716, 410);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(12, 116, 692, 245);
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 12, 668, 221);
		panel.add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "User Name", "In Date", "In Time", "Out Date", "Out Time","Report Access"
			}
		));
		scrollPane.setViewportView(table);
		
		 comboBox = new JComboBox();
		 comboBox.setBounds(35, 66, 192, 46);
		 comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		 		if(username.getSize()>1 && !comboBox.getSelectedItem().equals("Select User")) {
		 			 user=comboBox.getSelectedItem().toString();
		 			activity();
		 		}
		 		
		 	}
		 });
		comboBox.setBorder(new TitledBorder(null, "User Name", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(comboBox);
		
		 dateFromDC = new JDateChooser(new Date());
		 FromDate = DateFormatChange
					.StringToMysqlDate(dateFromDC.getDate());
		 System.out.print(FromDate+"rahulrrrrrrr");
		 dateFromDC.addPropertyChangeListener(new PropertyChangeListener() {
		 	public void propertyChange(PropertyChangeEvent evt) {
		 		if ("date".equals(evt.getPropertyName())) {

					FromDate = DateFormatChange
							.StringToMysqlDate((Date) evt
									.getNewValue());
					System.out.print(FromDate+"rahulrrrrrrr");
				}
		 		
		 	}
		 });
		dateFromDC.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "In Date", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		dateFromDC.setBounds(520, 10, 178, 44);
		dateFromDC.setDateFormatString("yyyy-MM-dd");
		getContentPane().add(dateFromDC);
		
		
		dateToDC = new JDateChooser(new Date());
		ToDate = DateFormatChange
				.StringToMysqlDate(dateToDC.getDate());
	     System.out.print(ToDate+"rahulrrrrrrr");
		dateToDC.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if ("date".equals(evt.getPropertyName())) {

					ToDate = DateFormatChange
							.StringToMysqlDate((Date) evt
									.getNewValue());
					System.out.print(ToDate+"rahulrrrrrrr");
				}
			}
		});
		dateToDC.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Out Date", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		dateToDC.setBounds(520, 66, 178, 46);
		dateToDC.setDateFormatString("yyyy-MM-dd");
		getContentPane().add(dateToDC);
		
		 departmentCB = new JComboBox();
		departmentCB.addActionListener(new ActionListener() {
		

			public void actionPerformed(ActionEvent e) {
				if(!departmentCB.getSelectedItem().equals("Select Department")) {
					 department=departmentCB.getSelectedItem()+"";
					 System.out.println(department+"ggggggggg");
					 users();
					 activity();
				}
			}
		});
		departmentCB.setModel(new DefaultComboBoxModel(new String[] {"Select Department", "Store", "Admin"}));
		departmentCB.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Department", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		departmentCB.setBounds(35, 10, 192, 46);
		getContentPane().add(departmentCB);
		
		textField = new JTextField();
		textField.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textField.setBounds(285, 49, 170, 37);
		getContentPane().add(textField);
		textField.setColumns(10);
	}
}
