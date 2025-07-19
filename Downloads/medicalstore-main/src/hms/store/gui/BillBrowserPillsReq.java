package hms.store.gui;

import hms.main.DateFormatChange;
import hms.opd.database.OPDDBConnection;
import hms.patient.slippdf.MedicalStoreBillSlippdf;
import hms.patient.slippdf.MedicalStoreIPDPdf;
import hms.patient.slippdf.RequestPillsSlipDepartment;
import hms.patient.slippdf.RequestPillsSlipDepartment.DepartmentItems;
import hms.patient.slippdf.RequestPillsSlipDesCheck;
import hms.store.database.BillingDBConnection;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;
import javax.swing.JCheckBox;

public class BillBrowserPillsReq extends JDialog {

	public JPanel contentPane;
	private JTable billbrowserTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom ="", dateTo = "";
	private JTextField searchPatientNameTB;
	double totalAmount=0;
	Vector originalTableModel;
	String userid="";
	private JButton btnExcel_1;
    public Vector<String> ReqitemName = new Vector<String>();
    public Vector<String> Reqquantity = new Vector<String>();
    public Vector<String> ReqInvoiceID = new Vector<String>();
    public Vector<String> ReqUserName = new Vector<String>();
    public Vector<String> itemName = new Vector<String>();
    public Vector<String> quantity = new Vector<String>();
    public Vector<String> batch_name = new Vector<String>();
    public Vector<String> dept_name = new Vector<String>();
    public Vector<String> expiry_date = new Vector<String>();

	public static void main(String[] arg)
	{
		BillBrowserPillsReq billBrowser=new BillBrowserPillsReq(null,"","");
		billBrowser.setVisible(true);

	}
	/**
	 * Create the frame.
	 */
	public BillBrowserPillsReq(final JFrame owner,final String Userid,String type) {
		//		this.userid=Userid;
		setResizable(false);
		//		System.out.println(Userid+"userid");
		setTitle("IPD Bill Request Entry List");
		setIconImage(Toolkit.getDefaultToolkit().getImage(BillBrowserPillsReq.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 937, 471);
		contentPane = new JPanel();
		setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setEnabled(false);
		scrollPane.setBounds(198, 11, 726, 418);
		contentPane.add(scrollPane);

		billbrowserTable = new JTable();
		
		billbrowserTable.setToolTipText("Double Click to reprint Bill Slip");
		billbrowserTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		billbrowserTable.getTableHeader().setReorderingAllowed(false);
		billbrowserTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		billbrowserTable.setAutoCreateRowSorter(true);
		billbrowserTable.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Invoice_ID","Dept_Name","IPD_ID","P_ID","P_Name","Doctor_Name","User_Name","Request_Desc","Request_Qty","Entry_Time","Is_Deleted","Status"
				}
				));
		
		scrollPane.setViewportView(billbrowserTable);
		billbrowserTable.addMouseListener(new MouseListener() {

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
				    // Double-click event handler
				    if (arg0.getClickCount() == 2) {
				        
				        // Get the selected row index
				        int selectedRow = billbrowserTable.getSelectedRow();
				        if (selectedRow != -1) {
				            // Convert row index to model index
				            selectedRow = billbrowserTable.convertRowIndexToModel(selectedRow);

				            // Get the status of the selected row (assuming status is in column 11)
				            String status = billbrowserTable.getModel().getValueAt(selectedRow, 11).toString();

				            // If status is "DELIVERED", prevent further actions
				            if ("DELIVERED".equalsIgnoreCase(status)) {
				                System.out.println("Cannot perform action: Status is DELIVERED.");
				                return;  // Exit the method if the status is "DELIVERED"
				            }

				            // Example: Get Dept_Name, Request_Desc, Request_Qty from the selected row
				            String deptName = billbrowserTable.getModel().getValueAt(selectedRow, 1).toString(); // Dept_
				            String requestUsr = billbrowserTable.getModel().getValueAt(selectedRow, 6).toString(); // Request_Qty
				            // Open the IPDBillEntry frame (doesn't block the thread)
				            String invoiceID = billbrowserTable.getModel().getValueAt(selectedRow, 0).toString();
				            String ipd_id = billbrowserTable.getModel().getValueAt(selectedRow, 2).toString();
				            String doc_name = billbrowserTable.getModel().getValueAt(selectedRow, 5).toString();
				            System.out.println(doc_name + " dr");
				            RetreiveRqstData(invoiceID,deptName,requestUsr);
				            // Open the IPDBillEntry frame
				            IPDBillEntry opdEntery = new IPDBillEntry(owner,null, BillBrowserPillsReq.this, invoiceID, true, ipd_id, doc_name);
				            opdEntery.setModal(true);  // Set to true to block the rest of the operations until the frame is closed
				            opdEntery.setVisible(true);
				        }
				    }
				}

		});

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(4, 11, 192, 418);
		contentPane.add(panel);
		panel.setLayout(null);
		searchPatientNameTB = new JTextField();
		searchPatientNameTB.setBounds(8, 37, 178, 28);
		panel.add(searchPatientNameTB);


		searchPatientNameTB.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = searchPatientNameTB.getText() + "";
						searchTableContents(str);
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = searchPatientNameTB.getText() + "";
						searchTableContents(str);
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = searchPatientNameTB.getText() + "";
						searchTableContents(str);
					}
				});
		searchPatientNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSelectDisease = new JLabel("Search Patient");
		lblSelectDisease.setBounds(54, 12, 95, 14);
		panel.add(lblSelectDisease);
		lblSelectDisease.setFont(new Font("Tahoma", Font.PLAIN, 12));

		dateFromDC = new JDateChooser();
		dateFromDC.setBounds(8, 101, 178, 25);
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
						}
					}
				});
		dateFromDC.setDate(new Date());
		dateFromDC.setMaxSelectableDate(new Date());
		dateFromDC.setDateFormatString("yyyy-MM-dd");


		dateToDC = new JDateChooser();
		dateToDC.setBounds(8, 162, 178, 25);
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
						}
					}
				});
		dateToDC.setDate(new Date());
		dateToDC.setMaxSelectableDate(new Date());
		dateToDC.setDateFormatString("yyyy-MM-dd");

		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setBounds(39, 137, 73, 14);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(54, 76, 82, 14);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnNewButton_2 = new JButton("Search");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				populateTable(dateFrom,dateTo);
			}
		});
		btnNewButton_2.setBounds(39, 202, 111, 35);
		panel.add(btnNewButton_2);
		btnNewButton_2.setIcon(new ImageIcon(BillBrowserPillsReq.class.getResource("/icons/zoom_r_button.png")));
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnExcel = new JButton("Excel");
		btnExcel.setBounds(39, 259, 110, 35);
		panel.add(btnExcel);
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					new ExcelFile("Bills Detail", billbrowserTable);
				} catch (DocumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnExcel.setIcon(new ImageIcon(BillBrowserPillsReq.class.getResource("/icons/1BL.PNG")));
		btnExcel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
				JButton btnNewButton_1 = new JButton("Close");
				btnNewButton_1.setBounds(20, 371, 160, 35);
				panel.add(btnNewButton_1);
				btnNewButton_1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				btnNewButton_1.setIcon(new ImageIcon(BillBrowserPillsReq.class.getResource("/icons/CANCEL.PNG")));
				btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
				
				btnExcel_1 = new JButton("Gen PDF");
				btnExcel_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						generatePDF();
					}
				});
				btnExcel_1.setFont(new Font("Dialog", Font.PLAIN, 12));
				btnExcel_1.setBounds(39, 316, 110, 35);
				panel.add(btnExcel_1);

	


		populateTable(DateFormatChange.StringToMysqlDate(new Date()),DateFormatChange.StringToMysqlDate(new Date()));
	}
	public void populateTable(String dateFrom, String dateTo) {
	    totalAmount = 0;
	    String index = searchPatientNameTB.getText() + "";

	    try {
	        BillingDBConnection db = new BillingDBConnection();
	        // Pass date range to the query
	        ResultSet rs = db.retrievePillsData(dateFrom, dateTo); // Pass date range here
	        final int numberOfColumns = rs.getMetaData().getColumnCount();

	        // Counting rows first
	        int numberOfRows = 0;
	        while (rs.next()) {
	            numberOfRows++;
	        }
	        System.out.println("rows : " + numberOfRows);
	        System.out.println("columns : " + numberOfColumns);

	        // Initialize the row array with one extra column for checkboxes
	        Object[][] rowsObjectArray = new Object[numberOfRows][numberOfColumns + 1];
	        
	        // Reset the cursor to the start of the result set
	        rs.beforeFirst();

	        // Fill the row array with data from the ResultSet
	        int rowIndex = 0;
	        while (rs.next()) {
	            for (int colIndex = 1; colIndex <= numberOfColumns; colIndex++) {
	                rowsObjectArray[rowIndex][colIndex - 1] = rs.getObject(colIndex);
	                System.out.println("column " + colIndex + ": " + rs.getObject(colIndex));
	            }

	            // Check if status is "pending", disable checkbox if true
	            String status = (String) rs.getObject(12); // Assuming the Status column is the 12th column
	            boolean isCheckboxEnabled = !status.equalsIgnoreCase("pending");

	            // Set the checkbox value to false (unchecked) for "pending" rows
	            rowsObjectArray[rowIndex][numberOfColumns] = status.equalsIgnoreCase("pending") ? false : false;

	            rowIndex++;
	        }

	        // Create the table model with the fetched data and the extra checkbox column
	        DefaultTableModel model = new DefaultTableModel(rowsObjectArray, new String[]{
	            "Invoice_ID", "Dept_Name", "IPD_ID", "P_ID", "P_Name", "Doctor_Name",
	            "User_Name", "Request_Desc", "Request_Qty", "Entry_Time", "Is_Deleted", "Status", "Select"
	        }) {
	            @Override
	            public boolean isCellEditable(int row, int column) {
	                // Make only the checkbox column editable if the status is NOT "pending"
	                String status = (String) billbrowserTable.getModel().getValueAt(row, 11); // Status column index (12th column)
	                return column == numberOfColumns && !status.equalsIgnoreCase("pending");
	            }
	        };

	        // Set the model to the JTable
	        billbrowserTable.setModel(model);

	        // Adjust the table columns' widths
	        originalTableModel = (Vector) ((DefaultTableModel) billbrowserTable.getModel())
	                .getDataVector().clone();
	        TableColumn columnSize = null;
	        for (int i = 0; i < 13; i++) { // Total 13 columns now
	            columnSize = billbrowserTable.getColumnModel().getColumn(i);
	            columnSize.setPreferredWidth(110);
	            if (i == 1 || i == 2 || i == 3) {
	                columnSize.setPreferredWidth(150);
	            }
	            if (i == 5) {
	                columnSize.setPreferredWidth(60);
	            }
	        }

	        // Set the checkbox renderer and editor
	        billbrowserTable.getColumnModel().getColumn(numberOfColumns).setCellRenderer(new DefaultTableCellRenderer() {
	            @Override
	            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	                javax.swing.JCheckBox checkBox = new javax.swing.JCheckBox();
	                checkBox.setSelected((Boolean) value);
	                String status = (String) table.getModel().getValueAt(row, 11); // Status column index
	                checkBox.setEnabled(!status.equalsIgnoreCase("pending"));  // Disable checkbox if "pending"
	                return checkBox;
	            }
	        });

	        billbrowserTable.getColumnModel().getColumn(numberOfColumns).setCellEditor(new DefaultCellEditor(new javax.swing.JCheckBox()));

	        // Close the database connection
	        db.closeConnection();

	    } catch (SQLException ex) {
	        Logger.getLogger(BillBrowserPillsReq.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}


	public void searchTableContents(String searchString) {
		DefaultTableModel currtableModel = (DefaultTableModel) billbrowserTable.getModel();
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
	
	private void generatePDF() {
	    ReqInvoiceID.removeAllElements();
	    ReqUserName.removeAllElements();
	    Map<String, RequestPillsSlipDepartment.DepartmentItems> departmentData = new LinkedHashMap<>();
	    
	    // Flag to check if any row is selected
	    boolean rowSelected = false;
	    
	    for (int row = 0; row < billbrowserTable.getRowCount(); row++) {
	        Boolean isSelected = (Boolean) billbrowserTable.getValueAt(row, 12);
	        if (isSelected) {
	            String deptName = billbrowserTable.getValueAt(row, 0).toString();
	            String itemName = billbrowserTable.getValueAt(row, 7).toString();
	            String quantity = billbrowserTable.getValueAt(row, 8).toString();
	            ReqInvoiceID.add(billbrowserTable.getValueAt(row, 0).toString());
	            ReqUserName.add(billbrowserTable.getValueAt(row, 6).toString());
	            rowSelected = true;  // Set flag to true when at least one row is selected
	        }
	    }

	    // If no row is selected, show a JOptionPane message
	    if (!rowSelected) {
	        JOptionPane.showMessageDialog(null, "Please select at least one row before generating the PDF.", 
	                                      "Selection Required", JOptionPane.WARNING_MESSAGE);
	        return;  // Exit the method early if no rows are selected
	    }

	    BillingDBConnection db = new BillingDBConnection();
	    for (int i = 0; i < ReqInvoiceID.size(); i++) {
	        RequestPillsSlipDepartment.DepartmentItems deptItems = new RequestPillsSlipDepartment.DepartmentItems();
	        itemName.removeAllElements();
	        quantity.removeAllElements();
	        batch_name.removeAllElements();
	        dept_name.removeAllElements();
	        expiry_date.removeAllElements();

	        System.out.println(ReqInvoiceID.get(i) + "  request invoice ");

	        ResultSet rs = db.MedicalStoreGenMultiplePDF(ReqInvoiceID.get(i));
	        try {
	            while (rs.next()) {
	                itemName.add(rs.getObject(1).toString());
	                quantity.add(rs.getObject(2).toString());
	                batch_name.add(rs.getObject(3).toString());
	                dept_name.add(rs.getObject(4).toString());
	                expiry_date.add(rs.getObject(5).toString());
	            }

	            // Assuming Request_Desc and Request_Qty are single items (could be multiple if you split them by commas)
	            for (int j = 0; j < itemName.size(); j++) {
	                System.out.println(itemName.get(j) + "   item name invoice ");
	                System.out.println(quantity.get(j) + "  quantity invoice ");
	                System.out.println(batch_name.get(j) + "  batch_name invoice ");
	                System.out.println(quantity.get(j) + "  quantity invoice ");

	                deptItems.itemName.add(itemName.get(j));
	                deptItems.quantity.add(quantity.get(j));
	                deptItems.itemBatchV.add(batch_name.get(j));
	                deptItems.expiryDateV.add(expiry_date.get(j));
	                // Put into map by deptName
	                departmentData.put(dept_name.get(j), deptItems);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    db.closeConnection();

	    // Create the slip object and generate the slip
	    RequestPillsSlipDepartment ob = new RequestPillsSlipDepartment();
	    try {
	        ob.generateSlipForMultipleDepartments(departmentData, ReqInvoiceID, ReqUserName);

	        // Try to open the generated PDF file automatically
	        if (Desktop.isDesktopSupported()) {
	            Desktop.getDesktop().open(new File(RequestPillsSlipDesCheck.RESULT));
	        } else {
	            System.out.println("Desktop is not supported. Cannot open PDF automatically.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	 private void RetreiveRqstData(String invoiceId , String deptName, String Requestusr) {
		 ReqitemName.removeAllElements();
		 Reqquantity.removeAllElements();
		 BillingDBConnection db = new BillingDBConnection();
		 ResultSet rs=db.MedicalStoreRequestpills(invoiceId);
		 try {
			while(rs.next()) {
				ReqitemName.add(rs.getObject(1).toString());
				Reqquantity.add(rs.getObject(2).toString());
			 }
			db.closeConnection();
			
			// Prepare the map to pass
            Map<String, RequestPillsSlipDesCheck.DepartmentItems> departmentData = new LinkedHashMap<>();
            RequestPillsSlipDesCheck.DepartmentItems deptItems = new RequestPillsSlipDesCheck.DepartmentItems();

            // Assuming Request_Desc and Request_Qty are single items (could be multiple if you split them by commas)
            for(int i=0;i<ReqitemName.size();i++) {
            deptItems.itemName.add(ReqitemName.get(i));
            deptItems.quantity.add(Reqquantity.get(i));
            // Put into map by deptName
            departmentData.put(deptName, deptItems);
            }
            // Create the slip object and generate the slip
            RequestPillsSlipDesCheck ob = new RequestPillsSlipDesCheck();
            try {
                // Generate the PDF and save it
                ob.generateSlipForMultipleDepartments(departmentData, Requestusr,invoiceId);

                // Try to open the generated PDF file automatically
                if (Desktop.isDesktopSupported()) {
                    File pdfFile = new File(RequestPillsSlipDesCheck.RESULT);
                    if (pdfFile.exists()) {
                        Desktop.getDesktop().open(pdfFile);  // Open the PDF without closing the frame
                    } else {
                        System.out.println("PDF file not found.");
                    }
                } else {
                    System.out.println("Desktop is not supported. Cannot open PDF automatically.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
}
