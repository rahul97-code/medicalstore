package hms.store.gui;

import hms.admin.gui.AdminMain;
import hms.main.DateFormatChange;
import hms.patient.slippdf.PO_PDF;
import hms.report.excel.POExcel;
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
import java.util.Calendar;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;
import javax.swing.JTextField;

public class POBrowser extends JDialog {

	public JPanel contentPane;
	private JTable pobrowserTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	Vector originalTableModel;
	String dateFrom, dateTo;
	double totalAmount = 0;
	String selectedPOId = "", selectedPONum = "", selectedVendName = "",
			selectedStatus = "",selectedStatusSent="";
	private JTextField searchTF;

	public static void main(String[] arg) {
		POBrowser browser = new POBrowser();
		browser.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public POBrowser() {
		setResizable(false);
		setTitle("PO List");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				POBrowser.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 950, 524);
		contentPane = new JPanel();
		//setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(198, 11, 726, 418);
		contentPane.add(scrollPane);

		pobrowserTable = new JTable();
		pobrowserTable.setToolTipText("Double Click to reprint PO Slip");
		pobrowserTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		pobrowserTable.getTableHeader().setReorderingAllowed(false);
		pobrowserTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		pobrowserTable.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "PO No", "Vendor Name", "Date", "Time",
						"Tax", "Surcharge", "Total" }));
		scrollPane.setViewportView(pobrowserTable);
		pobrowserTable.addMouseListener(new MouseListener() {

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
			    // Handle double-click (click count == 2)
			    if (arg0.getClickCount() == 2) {
			        int row = pobrowserTable.getSelectedRow();
			        int column = pobrowserTable.getSelectedColumn();

			        Object selectedObject = pobrowserTable.getValueAt(row, 0);
			        String selectedStatus = pobrowserTable.getValueAt(row, 6).toString();

			        System.out.println(selectedObject);

			        try {
			            if (!selectedStatus.equals("CANCELLED")) {
			                // Show a confirmation dialog with two options
			                Object[] options = {"With Amount", "Without Amount"};
			                int choice = JOptionPane.showOptionDialog(null, 
			                        "Print the bill with amount or without amount?", 
			                        "Print Bill", 
			                        JOptionPane.DEFAULT_OPTION, 
			                        JOptionPane.INFORMATION_MESSAGE, 
			                        null, 
			                        options, 
			                        options[0]);

			                // Based on the choice, print the bill accordingly
			                if (choice == 0) {  // "With Amount" button clicked
			                    new PO_PDF(selectedObject + "", true); // Assuming true means with amount
			                } else if (choice == 1) {  // "Without Amount" button clicked
			                    new PO_PDF(selectedObject + "", false); // Assuming false means without amount
			                }
			            }
			        } catch (DocumentException | IOException e) {
			            // Handle exceptions
			            e.printStackTrace();
			        }
			    }

			    // Handle single-click (click count == 1)
			    if (arg0.getClickCount() == 1) {
			        int row = pobrowserTable.getSelectedRow();
			        int column = pobrowserTable.getSelectedColumn();

			        // Get the selected data
			        selectedPOId = pobrowserTable.getValueAt(row, 0).toString();
			        selectedPONum = pobrowserTable.getValueAt(row, 1).toString();
			        selectedVendName = pobrowserTable.getValueAt(row, 2).toString();
			        selectedStatus = pobrowserTable.getValueAt(row, 6).toString();
			    }
			}
		});
		JButton btnNewButton = new JButton("New PO");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				NewPO opdEntery = new NewPO("");
				opdEntery.setModal(true);
				opdEntery.setVisible(true);
			}
		});
		btnNewButton.setIcon(new ImageIcon(POBrowser.class
				.getResource("/icons/Business.png")));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(294, 440, 160, 35);
		contentPane.add(btnNewButton);
		if (!AdminMain.is_admin.equals("1")) {
			btnNewButton.setVisible(false);
		}
		JButton btnNewButton_1 = new JButton("Close");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(POBrowser.class
				.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setBounds(473, 440, 160, 35);
		contentPane.add(btnNewButton_1);
		JButton BillButton = new JButton("Convert to Bill");
		BillButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//dispose();
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(POBrowser.class
				.getResource("/icons/CANCEL.PNG")));
		BillButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		BillButton.setBounds(660, 440, 160, 35);
		contentPane.add(BillButton);
		// BillButton.setVisible(false);
		BillButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// NewInvoice Bill = new NewInvoice();
				if(selectedStatus.equals("CLOSE")){
					JOptionPane.showMessageDialog(null,
							"PO Closed!","",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}else {
					NewInvoice Bill = new NewInvoice(selectedPOId,selectedPONum, selectedVendName);
					Bill.setModal(true);
					Bill.setVisible(true);
				}

			}
		});
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(4, 11, 192, 418);
		contentPane.add(panel);
		panel.setLayout(null);

		dateFromDC = new JDateChooser();
		dateFromDC.setBounds(4, 122, 178, 25);
		panel.add(dateFromDC);
		dateFromDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateFrom = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							populateTable(dateFrom, dateTo);
						}
					}
				});
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -3);
		Date d = cal.getTime();
		dateFromDC.setDate(d);
		dateFromDC.setMaxSelectableDate(new Date());
		dateFromDC.setDateFormatString("yyyy-MM-dd");

		dateToDC = new JDateChooser();
		dateToDC.setBounds(4, 177, 178, 25);
		panel.add(dateToDC);
		dateToDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateTo = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							populateTable(dateFrom, dateTo);
						}
					}
				});
		dateToDC.setDate(new Date());
		dateToDC.setMaxSelectableDate(new Date());
		dateToDC.setDateFormatString("yyyy-MM-dd");

		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setBounds(50, 158, 73, 14);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(50, 102, 82, 14);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		searchTF = new JTextField();
		searchTF.setBounds(4, 33, 165, 25);
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
		
		JLabel lblNewLabel = new JLabel("Search :");
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel.setBounds(50, 12, 70, 15);
		panel.add(lblNewLabel);

		JButton btnExcel = new JButton("Excel");
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					new POExcel(dateFrom, dateTo);
				} catch (DocumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnExcel.setIcon(new ImageIcon(POBrowser.class
				.getResource("/icons/1BL.PNG")));
		btnExcel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExcel.setBounds(124, 440, 160, 35);
		contentPane.add(btnExcel);

		populateTable(DateFormatChange.StringToMysqlDate(d),
				DateFormatChange.StringToMysqlDate(new Date()));
	}
	public void searchTableContents(String searchString) {
		DefaultTableModel currtableModel = (DefaultTableModel) pobrowserTable.getModel();
		// To empty the table before search
		currtableModel.setRowCount(0);
		// To search for contents from original table content
		for (Object rows : originalTableModel) {
			Vector rowVector = (Vector) rows;
			for (Object column : rowVector) {
				if (column.toString().toLowerCase().contains(searchString.toLowerCase())) {
					// content found so adding to table
					currtableModel.addRow(rowVector);
					break;
				}
			}
		}
	}

	public void populateTable(String dateFrom, String dateTo) {
		try {
			PODBConnection db = new PODBConnection();
			ResultSet rs = db.retrieveAllData(dateFrom, dateTo);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = db.retrieveAllData(dateFrom, dateTo);

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {
				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "ID", "PO No", "Vendor Name", "Date",
							"Time", "Total", "Status" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			pobrowserTable.setModel(model);
			TableColumn columnsize = null;
			for (int i = 0; i < 6; i++) {
				columnsize = pobrowserTable.getColumnModel().getColumn(i);
				columnsize.setPreferredWidth(110);
				if (i == 1 || i == 2 || i == 3) {
					columnsize.setPreferredWidth(150);
				}
				if (i == 5)
					columnsize.setPreferredWidth(60);
			}
			originalTableModel = (Vector) ((DefaultTableModel) pobrowserTable.getModel())
					.getDataVector().clone();
			if(NumberOfRows>0)
			{
				get();
			}
		} catch (SQLException ex) {
			Logger.getLogger(POBrowser.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	private void get() {
		// TODO Auto-generated method stub
		pobrowserTable.setAutoCreateRowSorter(true);
	}
}
