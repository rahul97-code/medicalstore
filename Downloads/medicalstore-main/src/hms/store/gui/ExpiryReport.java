package hms.store.gui;

import hms.admin.gui.AdminMain;
import hms.main.DateFormatChange;
import hms.opd.database.OPDDBConnection;
import hms.patient.slippdf.Bill_PDF;
import hms.patient.slippdf.MedicalStoreBillSlippdf;
import hms.patient.slippdf.PO_PDF;
import hms.store.database.BillingDBConnection;
import hms.store.database.IndentDBConnection;
import hms.store.database.InvoiceDBConnection;

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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;

public class ExpiryReport extends JDialog {

	public JPanel contentPane;
	private JTable invoicebrowserTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom,dateTo;
	double totalAmount=0;
	private JTextField searchTF;
	Vector originalTableModel;
	public static void main(String[] arg)
	{
		ExpiryReport browser=new ExpiryReport();
		browser.setVisible(true);
	}
	/**
	 * Create the frame.
	 */
	public ExpiryReport() {
		setResizable(false);
		setTitle("Expiry Report");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ExpiryReport.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 950, 524);
		contentPane = new JPanel();
		setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(198, 11, 726, 418);
		contentPane.add(scrollPane);
		
		invoicebrowserTable = new JTable();
//		invoicebrowserTable.setToolTipText("Double Click to reprint Bill Slip");
		invoicebrowserTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		invoicebrowserTable.getTableHeader().setReorderingAllowed(false);
		invoicebrowserTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		invoicebrowserTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Item ID", "Item Name", "Description", "Batch", "Expiry Date","Stock","MRP","Pack SIze","Invoice No.","Vendor Name","Invoice Date"
			}
		));
		scrollPane.setViewportView(invoicebrowserTable);
		invoicebrowserTable.addMouseListener(new MouseListener() {
			
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
				         JTable target = (JTable)arg0.getSource();
				         int row = target.getSelectedRow();
				         int column = target.getSelectedColumn();
				         // do some action
				         
//				         Object selectedObject = invoicebrowserTable.getModel().getValueAt(row, 0);
//				      
//				         EditInvoice opdEntery=new EditInvoice(selectedObject.toString());
//				
//							opdEntery.setModal(true);
//							opdEntery.setVisible(true);
							
				  }
				  if (arg0.getClickCount() == 1) {
//				         JTable target = (JTable)arg0.getSource();
//				         int row = target.getSelectedRow();
//				         int column = target.getSelectedColumn();
//				         // do some action
//				         
//				         Object selectedObject = invoicebrowserTable.getModel().getValueAt(row, 0);
//				      try {
//						new Bill_PDF(selectedObject+"");
//					} catch (DocumentException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				  
				  }
			}
		});
//		JButton btnNewButton = new JButton("New Invoice");
//		btnNewButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				NewInvoice opdEntery=new NewInvoice("","","");
//				opdEntery.invoiceBrowser(ExpiryReport.this);
//				opdEntery.setModal(true);
//				opdEntery.setVisible(true);
//			}
//		});
//		btnNewButton.setIcon(new ImageIcon(ExpiryReport.class.getResource("/icons/Business.png")));
//		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
//		btnNewButton.setBounds(294, 440, 160, 35);
//		contentPane.add(btnNewButton);
//		
//		if(!AdminMain.is_admin.equals("1")){
//			btnNewButton.setVisible(false);
//		}
		JButton btnNewButton_1 = new JButton("Close");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(ExpiryReport.class.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setBounds(473, 440, 160, 35);
		contentPane.add(btnNewButton_1);
//		JButton Paid = new JButton("Paid");
//		Paid.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				TableModel model = invoicebrowserTable.getModel();
//				invoicebrowserTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//				for (int rows = 0; rows < model.getRowCount(); rows++) { //
//					String invoice_id = model.getValueAt(rows, 0) + "";
//				
//	;
//
//					Boolean b = Boolean.valueOf(model.getValueAt(rows, 6)
//							.toString());
//					
//					
//					 InvoiceDBConnection db=new InvoiceDBConnection();
//			            
//					if (b) {
//					 try {
//						db.updateInvoicePaid(invoice_id);
//					} catch (Exception e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					
//					}
//					
//				}
//				 JOptionPane.showMessageDialog(null,
//							"Payment Paid", "Payment Paid",
//							JOptionPane.INFORMATION_MESSAGE);
//				}
//			
//		});
////		btnNewButton_1.setIcon(new ImageIcon(InvoiceBrowser.class.getResource("/icons/CANCEL.PNG")));
//		Paid.setFont(new Font("Tahoma", Font.PLAIN, 12));
//		Paid.setBounds(673, 440, 160, 35);
//		contentPane.add(Paid);	
//		if(AdminMain.is_admin.equals("1")){
//			Paid.setVisible(true);	
//		}else{
//			Paid.setVisible(false);
//		}
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
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
		dateFromDC.setDate(new Date());
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
		searchTF.setBounds(4, 45, 178, 25);
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
		
		JLabel lblSearch = new JLabel("Search");
		lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSearch.setBounds(41, 25, 82, 14);
		panel.add(lblSearch);
		
		JButton btnExcel = new JButton("Excel");
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					new ExcelFile("Invoice Detail", invoicebrowserTable);
				} catch (DocumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnExcel.setIcon(new ImageIcon(ExpiryReport.class.getResource("/icons/1BL.PNG")));
		btnExcel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExcel.setBounds(124, 440, 160, 35);
		contentPane.add(btnExcel);
		
		
		populateTable(DateFormatChange.StringToMysqlDate(new Date()),DateFormatChange.StringToMysqlDate(new Date()));
	}
	public void searchTableContents(String searchString) {
		DefaultTableModel currtableModel = (DefaultTableModel) invoicebrowserTable.getModel();
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
	public void populateTable(String dateFrom,String dateTo)
	{
		
		
		try {
			 InvoiceDBConnection db=new InvoiceDBConnection();
//            ResultSet rs = db.retrieveAllData(dateFrom, dateTo);
            ResultSet rs = db.retrieveExpiryData();
            
//            System.out.println("Table: " + rs.getMetaData().getTableName(1));
            int NumberOfColumns = 0, NumberOfRows = 0;
            NumberOfColumns = rs.getMetaData().getColumnCount();
            System.out.println(NumberOfColumns+"No of Columns");
            while(rs.next()){
            NumberOfRows++;
            }
            System.out.println(NumberOfRows+"Rows");
//            System.out.println("rownnnn "+NumberOfRows);
//            System.out.println("columns : "+NumberOfColumns);
//            rs = db.retrieveAllData(dateFrom, dateTo);
            rs = db.retrieveExpiryData();
            
            //to set rows in this array
            Object Rows_Object_Array[][];
            Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];
            
            int R = 0;
            while(rs.next()) {
                for(int C=1; C<=NumberOfColumns;C++) {
                    Rows_Object_Array[R][C-1] = rs.getObject(C);
//                    System.out.println( "kk"+rs.getObject(7).toString());
//                    if( rs.getObject(7).toString().equals("Yes")){
////                    	Rows_Object_Array[R][6] = "Yes";
//                    	Rows_Object_Array[R][6] = Boolean.TRUE;
//                    }else{
//                    	Rows_Object_Array[R][6] = Boolean.FALSE;
//                    }
                	
				
                }
                R++;
            }
            //Finally load data to the table
            final boolean[] canEdit = new boolean[] { false, false, false,
					false, false, false, true};
            DefaultTableModel model = new DefaultTableModel(Rows_Object_Array, new String[] {
            		"Item ID", "Item Name", "Description", "Batch", "Expiry Date","Stock","MRP","Pack SIze","Invoice No.","Vendor Name","Invoice Date"
    			});
    
    	
    		
    		invoicebrowserTable.setModel(model);
			originalTableModel = (Vector) ((DefaultTableModel) invoicebrowserTable.getModel())
					.getDataVector().clone();

			invoicebrowserTable.getColumnModel().getColumn(1).setPreferredWidth(180);
			invoicebrowserTable.getColumnModel().getColumn(1).setMinWidth(150);
			invoicebrowserTable.getColumnModel().getColumn(2).setPreferredWidth(180);
			invoicebrowserTable.getColumnModel().getColumn(2).setMinWidth(150);
			invoicebrowserTable.getColumnModel().getColumn(3).setMinWidth(150);
			invoicebrowserTable.getColumnModel().getColumn(4).setPreferredWidth(150);
			invoicebrowserTable.getColumnModel().getColumn(4).setMinWidth(100);
			invoicebrowserTable.getColumnModel().getColumn(5).setPreferredWidth(80);
			invoicebrowserTable.getColumnModel().getColumn(5).setMinWidth(80);
			invoicebrowserTable.getColumnModel().getColumn(6).setPreferredWidth(80);
			invoicebrowserTable.getColumnModel().getColumn(6).setMinWidth(80);
			invoicebrowserTable.getColumnModel().getColumn(7).setPreferredWidth(100);
			invoicebrowserTable.getColumnModel().getColumn(7).setMinWidth(100);
			invoicebrowserTable.getColumnModel().getColumn(8).setPreferredWidth(100);
			invoicebrowserTable.getColumnModel().getColumn(8).setMinWidth(100);
			invoicebrowserTable.getColumnModel().getColumn(9).setPreferredWidth(100);
			invoicebrowserTable.getColumnModel().getColumn(9).setMinWidth(100);
			invoicebrowserTable.getColumnModel().getColumn(10).setPreferredWidth(100);
			invoicebrowserTable.getColumnModel().getColumn(10).setMinWidth(100);
			
        } catch (SQLException ex) {
            Logger.getLogger(ExpiryReport.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	public JTextField getSearchET() {
		return searchTF;
	}
}
