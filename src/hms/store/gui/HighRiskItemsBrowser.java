package hms.store.gui;

import hms.admin.gui.AdminMain;
import hms.main.DateFormatChange;
import hms.patient.slippdf.PO_PDF;
import hms.report.excel.POExcel;
import hms.store.database.ItemsDBConnection;
import hms.store.database.PODBConnection;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.JToggleButton;
import java.awt.Canvas;
import java.awt.Color;

public class HighRiskItemsBrowser extends JDialog {

	public JPanel contentPane;
	private JTable HighRiskTable;
	ButtonGroup agegroup = new ButtonGroup();
	Vector<String> itemID=new Vector<String>();
	Vector<String> removeitemID=new Vector<String>();
	DateFormatChange dateFormat = new DateFormatChange();
	Vector originalTableModel;
	String dateFrom, dateTo;
	double totalAmount = 0;
	String selectedPOId = "", selectedPONum = "", selectedVendName = "",
			selectedStatus = "",selectedStatusSent="";
	private JTextField searchTF;
	private JTable table;
	 TableRowSorter<TableModel> rowSorter;
	private JButton btnExcel;
	private JButton btnNewButton_1_1;
    

	public static void main(String[] arg) {
		HighRiskItemsBrowser browser = new HighRiskItemsBrowser();
		browser.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public HighRiskItemsBrowser() {
		setResizable(false);
		setTitle("High Risk Item List");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				HighRiskItemsBrowser.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 950, 524);
		contentPane = new JPanel();
		//setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(26, 80, 365, 395);
		contentPane.add(scrollPane);

		HighRiskTable = new JTable();
		HighRiskTable.setToolTipText("Click to Add");
		HighRiskTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		HighRiskTable.getTableHeader().setReorderingAllowed(false);
		HighRiskTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		HighRiskTable.setModel(new DefaultTableModel(new Object[][] {},
				new String[] {"Item ID","Item Name","High Risk","Select"}));
		HighRiskTable.getColumnModel().getColumn(0).setMinWidth(50);
		HighRiskTable.getColumnModel().getColumn(1).setPreferredWidth(105);
		HighRiskTable.getColumnModel().getColumn(1).setMinWidth(105);
		HighRiskTable.getColumnModel().getColumn(2).setPreferredWidth(90);
		HighRiskTable.getColumnModel().getColumn(2).setMinWidth(90);
		HighRiskTable.getColumnModel().getColumn(3).setPreferredWidth(90);
		HighRiskTable.getColumnModel().getColumn(3).setMinWidth(90);
		//		HighRiskTable.getColumnModel().getColumn(4).setPreferredWidth(90);
		//		HighRiskTable.getColumnModel().getColumn(4).setMinWidth(90);
		scrollPane.setViewportView(HighRiskTable);
		HighRiskTable.addMouseListener(new MouseListener() {

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
				btnExcel.setEnabled(true);
				btnNewButton_1_1.setEnabled(false);
				if (arg0.getClickCount() == 1) {
					//	JTable target = (JTable) arg0.getSource();
					itemID.removeAllElements();
					int row = HighRiskTable.getSelectedRow();
			
					for(int i=0;i<HighRiskTable.getRowCount();i++)
					{
						boolean b=(boolean) HighRiskTable.getValueAt(i, 3);
					    if(b)
					    {
					    	itemID.add(HighRiskTable.getValueAt(i, 0).toString());
					    }
					}
				}
			}
		});
		populateTable() ;
	
		JButton btnNewButton_1 = new JButton("Close");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(HighRiskItemsBrowser.class
				.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setBounds(414, 401, 111, 35);
		contentPane.add(btnNewButton_1);
		btnNewButton_1.setIcon(new ImageIcon(HighRiskItemsBrowser.class
				.getResource("/icons/CANCEL.PNG")));
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(26, 12, 892, 56);
		contentPane.add(panel);
		panel.setLayout(null);

		searchTF = new JTextField();
		searchTF.setBounds(128, 12, 165, 25);
		panel.add(searchTF);
		searchTF.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = searchTF.getText() + "";
						searchTableContents(str);

					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = searchTF.getText() + "";
						searchTableContents(str);
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = searchTF.getText() + "";
						searchTableContents(str);
					}
				});
		searchTF.setColumns(10);

		JLabel lblNewLabel = new JLabel("Search Item :");
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel.setBounds(27, 17, 83, 15);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("High Risk Item List ----------------------------------------------------------------------------");
		lblNewLabel_1.setFont(new Font("Kinnari", Font.ITALIC, 16));
		lblNewLabel_1.setBounds(322, 16, 528, 21);
		panel.add(lblNewLabel_1);

		btnExcel = new JButton("ADD");
		btnExcel.setEnabled(false);
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ItemsDBConnection db = new ItemsDBConnection();
				for(int i=0;i<itemID.size();i++)
				{
					try {
						db.UpdateHighRisk(itemID.get(i));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				populateTable();
				populateTable1();
			}
		});
		btnExcel.setIcon(null);
		btnExcel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExcel.setBounds(415, 128, 110, 35);
		contentPane.add(btnExcel);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(554, 80, 364, 395);
		contentPane.add(scrollPane_1);

		table = new JTable();
		table.setToolTipText("Double Click to reprint PO Slip");
		table.setFont(new Font("Dialog", Font.PLAIN, 12));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setBounds(0, 0, 525, 1);
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] {"Item ID","Item Name","High Risk","Price"}));
		table.getColumnModel().getColumn(0).setMinWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(105);
		table.getColumnModel().getColumn(1).setMinWidth(105);
		table.getColumnModel().getColumn(2).setPreferredWidth(90);
		table.getColumnModel().getColumn(2).setMinWidth(90);
		table.getColumnModel().getColumn(3).setPreferredWidth(90);
		table.getColumnModel().getColumn(3).setMinWidth(90);
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
				btnExcel.setEnabled(false);
				btnNewButton_1_1.setEnabled(true);
				if (arg0.getClickCount() == 1) {
					//	JTable target = (JTable) arg0.getSource();
					removeitemID.removeAllElements();
					int row = table.getSelectedRow();
			
					for(int i=0;i<table.getRowCount();i++)
					{
						boolean b=(boolean) table.getValueAt(i, 3);
					    if(b)
					    {
					    	removeitemID.add(table.getValueAt(i, 0).toString());
					    }
					}


				}
			}
		});
		
		//		table.getColumnModel().getColumn(4).setPreferredWidth(90);
		//		table.getColumnModel().getColumn(4).setMinWidth(90);
		scrollPane_1.setViewportView(table);
		
		btnNewButton_1_1 = new JButton("REMOVE");
		btnNewButton_1_1.setEnabled(false);
		btnNewButton_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ItemsDBConnection db = new ItemsDBConnection();
				for(int i=0;i<removeitemID.size();i++)
				{
					try {
						db.UpdateHighRiskRemove(removeitemID.get(i));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				populateTable();
				populateTable1();
			}
		});
		btnNewButton_1_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnNewButton_1_1.setBounds(414, 192, 111, 35);
		contentPane.add(btnNewButton_1_1);
		 populateTable1();
	}
	public void searchTableContents(String searchString) {
		  if (searchString.trim().length() == 0) {
              rowSorter.setRowFilter(null);
          } else {
              rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchString));
          }
	}

	public void populateTable() {
		try {
			ItemsDBConnection db = new ItemsDBConnection();
			ResultSet rs = db.retrieveAllHighRiskData();
			

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = db.retrieveAllHighRiskData();

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns+1];

			int R = 0;
			while (rs.next()) {
				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
				}
				Rows_Object_Array[R][3]=new Boolean(false);
				R++;
			}
			// Finally load data to the table
			TableModel model = new EditableTableModel_H(
					new String[] { "Item ID","Item Name","High Risk","Select"},Rows_Object_Array) {
			};
			HighRiskTable.setModel(model);
			HighRiskTable.getColumnModel().getColumn(0).setMinWidth(50);
			HighRiskTable.getColumnModel().getColumn(1).setPreferredWidth(105);
			HighRiskTable.getColumnModel().getColumn(1).setMinWidth(105);
			HighRiskTable.getColumnModel().getColumn(2).setPreferredWidth(90);
			HighRiskTable.getColumnModel().getColumn(2).setMinWidth(90);
			HighRiskTable.getColumnModel().getColumn(3).setPreferredWidth(90);
			HighRiskTable.getColumnModel().getColumn(3).setMinWidth(90);
			rowSorter = new TableRowSorter<>(HighRiskTable.getModel());
			HighRiskTable.setRowSorter(rowSorter);
		} catch (SQLException ex) {
			Logger.getLogger(HighRiskItemsBrowser.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}
	public void populateTable1() {
		try {
			ItemsDBConnection db = new ItemsDBConnection();
			ResultSet rs = db.retrieveAllHighRiskData1();
			

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = db.retrieveAllHighRiskData1();

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns+1];

			int R = 0;
			while (rs.next()) {
				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
				}
				Rows_Object_Array[R][3]=new Boolean(false);
				R++;
			}
			// Finally load data to the table
			TableModel model = new EditableTableModel_H(
					new String[] { "Item ID","Item Name","High Risk","Select"},Rows_Object_Array) {
			};
			table.setModel(model);
			table.getColumnModel().getColumn(0).setMinWidth(50);
			table.getColumnModel().getColumn(1).setPreferredWidth(105);
			table.getColumnModel().getColumn(1).setMinWidth(105);
			table.getColumnModel().getColumn(2).setPreferredWidth(90);
			table.getColumnModel().getColumn(2).setMinWidth(90);
			table.getColumnModel().getColumn(3).setPreferredWidth(90);
			table.getColumnModel().getColumn(3).setMinWidth(90);
		} catch (SQLException ex) {
			Logger.getLogger(HighRiskItemsBrowser.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}
	
	class EditableTableModel_H extends AbstractTableModel {
		String[] columnTitles;

		Object[][] dataEntries;

		int rowCount;

		public EditableTableModel_H(String[] columnTitles, Object[][] dataEntries) {
			this.columnTitles = columnTitles;
			this.dataEntries = dataEntries;
		}

		public int getRowCount() {
			return dataEntries.length;
		}

		public int getColumnCount() {
			return columnTitles.length;
		}

		public Object getValueAt(int row, int column) {
			return dataEntries[row][column];
		}

		public String getColumnName(int column) {
			return columnTitles[column];
		}

		public Class getColumnClass(int column) {
			return getValueAt(0, column).getClass();
		}

		public boolean isCellEditable(int row, int column) {
			if(column==3)
			{
				return true;
			}else {
			return false;	
			}

		}

		public void setValueAt(Object value, int row, int column) {
			dataEntries[row][column] = value;
		}
	}
}
