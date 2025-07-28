package hms.store.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.main.DateFormatChange;
import hms.store.database.ItemsDBConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JButton;

import com.itextpdf.text.DocumentException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JTextField;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import javax.swing.JComboBox;

public class TotalDepartmentStock extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JTextField searchItemTF;
	Vector originalTableModel;
	private JButton btnNewButton;
	final DefaultComboBoxModel departmentName = new DefaultComboBoxModel();
	private JComboBox departmentCB;
	protected String selectedDepartment="Department Name";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TotalDepartmentStock dialog = new TotalDepartmentStock();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public TotalDepartmentStock() {
		
		setTitle("Total Department Stock Register");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				TotalDepartmentStock.class.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 1068, 578);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 39, 1042, 490);
			contentPanel.add(scrollPane);
			{
				table = new JTable();
				table.getTableHeader().setReorderingAllowed(false);
				table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setModel(new DefaultTableModel(new Object[][] { { null,
						null, null, null, null }, }, new String[] { "Item Id",
						"Item Name", "Item Desc.", "Excess Stock",
						"Expiry Date", "Item Stock" }));
				table.getColumnModel().getColumn(1).setPreferredWidth(180);
				table.getColumnModel().getColumn(1).setMinWidth(150);
				table.getColumnModel().getColumn(2).setPreferredWidth(180);
				table.getColumnModel().getColumn(2).setMinWidth(150);
				table.getColumnModel().getColumn(3).setMinWidth(150);
				table.getColumnModel().getColumn(4).setPreferredWidth(150);
				table.getColumnModel().getColumn(4).setMinWidth(100);
				table.setFont(new Font("Tahoma", Font.BOLD, 14));
				scrollPane.setViewportView(table);
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
						if (arg0.getClickCount() == 2) {
							JTable target = (JTable) arg0.getSource();
							int row = target.getSelectedRow();
							int column = target.getSelectedColumn();
							// do some action

							Object selectedObject = table.getModel()
									.getValueAt(row, 0);

							BatchStock batchStock = new BatchStock(
									selectedObject.toString());
							batchStock.setModal(true);
							batchStock.setVisible(true);

						}
					}
				});
			}
		}
		{
			JLabel lblDepartmentStock = new JLabel("Department Stock");
			lblDepartmentStock.setFont(new Font("Tahoma", Font.BOLD, 17));
			lblDepartmentStock.setBounds(57, 0, 200, 34);
			contentPanel.add(lblDepartmentStock);
		}
		{
			JLabel label = new JLabel("");
			label.setIcon(new ImageIcon(TotalDepartmentStock.class
					.getResource("/icons/stockicon.png")));
			label.setBounds(10, 0, 52, 34);
			contentPanel.add(label);
		}
		{
			btnNewButton = new JButton("Excel File");
			btnNewButton.setEnabled(false);
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setSelectedFile(new File("Excel_data.xls"));
					if (fileChooser.showSaveDialog(TotalDepartmentStock.this) == JFileChooser.APPROVE_OPTION) {
						File file = fileChooser.getSelectedFile();
						// save to file
						ReportExcel(table, file.toPath().toString());
					}
				}
			});
			btnNewButton.setIcon(new ImageIcon(TotalDepartmentStock.class
					.getResource("/icons/1BL.PNG")));
			btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnNewButton.setBounds(722, 9, 138, 23);
			contentPanel.add(btnNewButton);
		}
		if(StoreMain.update_item_access.equals("1") || StoreMain.update_item_access.equals("1")) {
			btnNewButton.setEnabled(true);}
		else {
			btnNewButton.setEnabled(false);}
		searchItemTF = new JTextField();
		searchItemTF.setColumns(10);
		searchItemTF.setBounds(505, 8, 207, 20);
		contentPanel.add(searchItemTF);
		searchItemTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				searchTableContents(str);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				searchTableContents(str);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				searchTableContents(str);
			}
		});
		JLabel label = new JLabel("Search");
		label.setBounds(443, 11, 52, 17);
		contentPanel.add(label);
		
			 departmentCB = new JComboBox();
			 departmentCB.addActionListener(new ActionListener() {
				    

					public void actionPerformed(ActionEvent e) {
				        // Retrieve the selected item and cast it to a String
				        selectedDepartment = (String) departmentCB.getSelectedItem();
				        
				        // You can now use the selectedDepartment string
				        System.out.println("Selected department: " + selectedDepartment);
				        populateExpensesTable(selectedDepartment);
				    }
				});

			departmentCB.setBounds(244, 10, 181, 17);
			contentPanel.add(departmentCB);
			getAllDepartments();
		    populateExpensesTable(selectedDepartment);
	}

	public void ReportExcel(JTable table, String path) {
		// TODO Auto-generated constructor stub
		try {
			String filename = path;
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Report");
			HSSFRow row = sheet.createRow(1);
			TableModel model = table.getModel();
			HSSFRow rowhead = sheet.createRow((short) 0);

			HSSFRow headerRow = sheet.createRow(0); // Create row at line 0
			for (int headings = 0; headings < model.getColumnCount(); headings++) { // For
																					// each
																					// column
				headerRow.createCell(headings).setCellValue(
						model.getColumnName(headings));// Write column name
			}

			for (int rows = 0; rows < model.getRowCount(); rows++) { // For each
																		// table
																		// row
				for (int cols = 0; cols < table.getColumnCount(); cols++) { // For
																			// each
																			// table
																			// column
					row.createCell(cols).setCellValue(
							model.getValueAt(rows, cols).toString()); // Write
																		// value
				}

				// Set the row to the next one in the sequence
				row = sheet.createRow((rows + 2));
			}

			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			JOptionPane.showMessageDialog(null,
					"Excel File Generated Successfully", "Data Saved",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void searchTableContents(String searchString) {
		DefaultTableModel currtableModel = (DefaultTableModel) table.getModel();
		// To empty the table before search
		currtableModel.setRowCount(0);
		// To search for contents from original table content
		for (Object rows : originalTableModel) {
			Vector rowVector = (Vector) rows;
			for (Object column : rowVector) {
				String str=column+"";
				if (str.toLowerCase().contains(searchString.toLowerCase())) {
					// content found so adding to table
					currtableModel.addRow(rowVector);
					break;
				}
			}
		}
	}
	
	public void getAllDepartments() {
		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		departmentName.removeAllElements();
		departmentName.addElement("Department Name");
		int i = 0;
		try { 
			while (resultSet.next()) {
				departmentName.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		departmentCB.setModel(departmentName);
		if (i > 0) {
			departmentCB.setSelectedIndex(0);
		}
	}

	public void populateExpensesTable(String department) {
	    try {
	        ItemsDBConnection db = new ItemsDBConnection();
	        
	        // Assuming department_stock returns a ResultSet based on the given department
	        ResultSet rs = db.department_stock(department);
	        
	        // Get the number of columns
	        int numberOfColumns = rs.getMetaData().getColumnCount();
	        
	        // Calculate the number of rows first
	        int numberOfRows = 0;
	        while (rs.next()) {
	            numberOfRows++;
	        }
	        
	        // Reset the ResultSet pointer to the beginning
	        rs.beforeFirst(); // This makes sure we can loop through it again
	        
	        // Now create the array with the correct size
	        Object[][] rowsObjectArray = new Object[numberOfRows][numberOfColumns];
	        
	        // Fill the array with data from the ResultSet
	        int row = 0;
	        while (rs.next()) {
	            for (int col = 1; col <= numberOfColumns; col++) {
	                rowsObjectArray[row][col - 1] = rs.getObject(col);
	            }
	            row++;
	        }
	        
	        // Finally load data into the table
	        DefaultTableModel model = new DefaultTableModel(
	            rowsObjectArray,
	            new String[] {
	                "Department Name", "Item Id", "Item Name", "Item Desc.", 
	                "User Name", "Current Stock", "Batch Id", "Batch Name", "Expiry Date"
	            }) {
	            @Override
	            public boolean isCellEditable(int row, int column) {
	                return false; // All cells are non-editable
	            }
	        };
	        
	        table.setModel(model);
	        
	        // Set column widths
	        table.getColumnModel().getColumn(0).setPreferredWidth(180);
	        table.getColumnModel().getColumn(0).setMinWidth(150);
	        table.getColumnModel().getColumn(1).setPreferredWidth(180);
	        table.getColumnModel().getColumn(1).setMinWidth(150);
	        table.getColumnModel().getColumn(2).setPreferredWidth(180);
	        table.getColumnModel().getColumn(2).setMinWidth(150);
	        table.getColumnModel().getColumn(3).setMinWidth(150);
	        table.getColumnModel().getColumn(4).setPreferredWidth(150);
	        table.getColumnModel().getColumn(4).setMinWidth(100);
	        table.getColumnModel().getColumn(5).setPreferredWidth(150);
	        table.getColumnModel().getColumn(5).setMinWidth(100);
	        table.getColumnModel().getColumn(6).setPreferredWidth(150);
	        table.getColumnModel().getColumn(6).setMinWidth(100);
	        table.getColumnModel().getColumn(7).setPreferredWidth(100);
	        table.getColumnModel().getColumn(7).setMinWidth(100);
	        
	        // Set the font for the table
	        table.setFont(new Font("Tahoma", Font.BOLD, 12));
	        
	        // Set custom renderers if necessary
//	        table.getColumnModel().getColumn(8).setCellRenderer(new CustomRenderer());
//	        table.getColumnModel().getColumn(8).setCellRenderer(new CustomRenderer2());
	        
	        // Clone the original model data
	        originalTableModel = (Vector) ((DefaultTableModel) table.getModel()).getDataVector().clone();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}


	public class CustomRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component cellComponent = super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);

			if (table.getValueAt(row, column).equals("With in level")) {
				cellComponent.setBackground(Color.GREEN);
			} else {
				cellComponent.setBackground(Color.RED);

			}
			return cellComponent;
		}
	}

	class CustomRenderer2 extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component cellComponent = super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);

			try {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date1 = sdf.parse(DateFormatChange
						.StringToMysqlDate(DateFormatChange.addMonths(
								new Date(), 3)));
				if (table.getValueAt(row, column).toString().equals("")) {
					cellComponent.setBackground(Color.PINK);
					return cellComponent;
				}
				Date date2 = sdf
						.parse(table.getValueAt(row, column).toString());

				System.out.println(row + "    " + column);
				// System.out.println(sdf.format(date2));

				if (date1.compareTo(date2) > 0) {
					// System.out.println("Date1 is after Date2");
					cellComponent.setBackground(Color.RED);
				} else if (date1.compareTo(date2) < 0) {
					// System.out.println("Date1 is before Date2");
					cellComponent.setBackground(Color.GREEN);
				} else if (date1.compareTo(date2) == 0) {
					// System.out.println("Date1 is equal to Date2");
					cellComponent.setBackground(Color.PINK);
				} else {
					// System.out.println("How to get here?");
				}

			} catch (ParseException ex) {

				ex.printStackTrace();

			}
			return cellComponent;
		}
	}

	public JTextField getSearchItemTF() {
		return searchItemTF;
	}
}
