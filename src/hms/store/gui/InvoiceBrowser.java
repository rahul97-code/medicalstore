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
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
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
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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

public class InvoiceBrowser extends JDialog {

	public JPanel contentPane;
	private JTable invoicebrowserTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom,dateTo;
	double totalAmount=0;
	private JTextField searchTF;
	private String content;
	Vector originalTableModel;
	public JTextField textFieldCount = new JTextField();
	public JTextField textFieldAmt  = new JTextField();
	String selectedIndex=null,vendorName=null;
	JPopupMenu contextMenu = new JPopupMenu();
	public static void main(String[] arg)
	{
		InvoiceBrowser browser=new InvoiceBrowser();
		browser.setVisible(true);
	}
	/**
	 * Create the frame.
	 */
	public InvoiceBrowser() {
		JMenuItem search=new JMenuItem("  Search <<");
		search.setFont(new Font("Dialog", Font.ITALIC, 12));
		contextMenu.add(search);
		
		setResizable(false);
		setTitle("Invoice List");
		setIconImage(Toolkit.getDefaultToolkit().getImage(InvoiceBrowser.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 950, 524);
		contentPane = new JPanel();
		setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setEnabled(false);
		scrollPane.setBounds(198, 11, 726, 418);
		contentPane.add(scrollPane);
		
		invoicebrowserTable = new JTable();
		invoicebrowserTable.setToolTipText("Double Click to reprint Bill Slip");
		invoicebrowserTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
	
		invoicebrowserTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		invoicebrowserTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
			}
		));
		scrollPane.setViewportView(invoicebrowserTable);
		invoicebrowserTable.addMouseListener(new MouseListener() {
			
		

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				 JTable source = (JTable)e.getSource();
				    int row = source.rowAtPoint(e.getPoint());
				    int column = source.columnAtPoint(e.getPoint());

				    if (!source.isRowSelected(row)) {
				        source.changeSelection(row, column, false, false);
				        content=invoicebrowserTable.getValueAt(row, column)+"";
				    }
				handleRowClick(e);
				if (e.isPopupTrigger()) {
					doPop(e);
				} else {
					hidePop();
				}
			}
			private void doPop(MouseEvent e) {
				if (invoicebrowserTable.getSelectedRowCount() == 0) {
					return;
				}
				contextMenu.setVisible(true);
				contextMenu.show(e.getComponent(), e.getX(), e.getY());
			}

			private void hidePop() {
				contextMenu.setVisible(false);
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
				         int row = invoicebrowserTable.getSelectedRow();
				         int column = invoicebrowserTable.getSelectedColumn();
				         
				         Object selectedObject = invoicebrowserTable.getValueAt(row, 0);
				         Object po_id = invoicebrowserTable.getValueAt(row, 7);
				         System.out.print(po_id);
				         
				         EditInvoice opdEntery=new EditInvoice(selectedObject.toString(),po_id.toString());
							opdEntery.setModal(true);
							opdEntery.setVisible(true);
							
				         
				  }
				  if (arg0.getClickCount() == 1) {      
				         int row = invoicebrowserTable.getSelectedRow();
				         int column = invoicebrowserTable.getSelectedColumn();
				         selectedIndex=invoicebrowserTable.getValueAt(row, 0)+"";
				          vendorName = invoicebrowserTable.getValueAt(row, 1)+"";				  
				  }
			}
		});
		
		search.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e) {        
				DefaultTableModel currtableModel = (DefaultTableModel) invoicebrowserTable.getModel();
				currtableModel.setRowCount(0);
				for (Object rows : originalTableModel) {
					Vector rowVector = (Vector) rows;
					for (Object column : rowVector) {
						if (column.toString().toLowerCase().equals(content.toLowerCase())) {
							// content found so adding to table
							currtableModel.addRow(rowVector);
							break;
						}
					}
				} 
				countALLString();
			}  
		});  
		JButton btnNewButton = new JButton("New Invoice");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
//				NewInvoice opdEntery=new NewInvoice("","","");
//				opdEntery.invoiceBrowser(InvoiceBrowser.this);
//				opdEntery.setModal(true);
//				opdEntery.setVisible(true);
			}
		});
		btnNewButton.setIcon(new ImageIcon(InvoiceBrowser.class.getResource("/icons/Business.png")));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(218, 440, 160, 35);
		contentPane.add(btnNewButton);
		
		if(!AdminMain.is_admin.equals("1")){
			btnNewButton.setVisible(false);
		}
		JButton btnNewButton_1 = new JButton("Close");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(InvoiceBrowser.class.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setBounds(397, 440, 160, 35);
		contentPane.add(btnNewButton_1);
		JButton Paid = new JButton("Paid");
		Paid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableModel model = invoicebrowserTable.getModel();
				invoicebrowserTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				for (int rows = 0; rows < model.getRowCount(); rows++) { //
					String invoice_id = model.getValueAt(rows, 0) + "";
				
	;

					Boolean b = Boolean.valueOf(model.getValueAt(rows, 6)
							.toString());
					
					
					 InvoiceDBConnection db=new InvoiceDBConnection();
			            
					if (b) {
					 try {
						db.updateInvoicePaid(invoice_id);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					}
					
				}
				 JOptionPane.showMessageDialog(null,
							"Payment Paid", "Payment Paid",
							JOptionPane.INFORMATION_MESSAGE);
				}
			
		});
//		btnNewButton_1.setIcon(new ImageIcon(InvoiceBrowser.class.getResource("/icons/CANCEL.PNG")));
		Paid.setFont(new Font("Tahoma", Font.PLAIN, 12));
		Paid.setBounds(574, 440, 160, 35);
		contentPane.add(Paid);	
		if(AdminMain.is_admin.equals("1")){
			Paid.setVisible(true);	
		}else{
			Paid.setVisible(false);
		}
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(0, 11, 192, 418);
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
		
		textFieldAmt = new JTextField();
		textFieldAmt.setText("");
		textFieldAmt.setEditable(false);
		textFieldAmt.setColumns(10);
		textFieldAmt.setBounds(4, 345, 170, 28);
		panel.add(textFieldAmt);
		
		JLabel lblTotalAmount = new JLabel("Total Amount :");
		lblTotalAmount.setBounds(4, 318, 111, 15);
		panel.add(lblTotalAmount);
		
		textFieldCount = new JTextField();
		textFieldCount.setText("");
		textFieldCount.setEditable(false);
		textFieldCount.setColumns(10);
		textFieldCount.setBounds(4, 278, 170, 28);
		panel.add(textFieldCount);
		
		JLabel lblCount = new JLabel("Count :");
		lblCount.setBounds(6, 251, 70, 15);
		panel.add(lblCount);
		
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
		btnExcel.setIcon(new ImageIcon(InvoiceBrowser.class.getResource("/icons/1BL.PNG")));
		btnExcel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExcel.setBounds(48, 440, 160, 35);
		contentPane.add(btnExcel);
		
		JButton Paid_1 = new JButton("Print");
		Paid_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 try {
						new Bill_PDF(selectedIndex,vendorName.toString());
					} catch (DocumentException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace(); 
					}
			}
		});
		Paid_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		Paid_1.setBounds(746, 441, 160, 35);
		contentPane.add(Paid_1);
		
		
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
		countALLString();
	}
	public void populateTable(String dateFrom,String dateTo)
	{
		
		
		try {
			 InvoiceDBConnection db=new InvoiceDBConnection();
            ResultSet rs = db.retrieveAllData(dateFrom, dateTo);
            int NumberOfColumns = 0, NumberOfRows = 0;
            NumberOfColumns = rs.getMetaData().getColumnCount();
            
            while(rs.next()){
            NumberOfRows++;
            }
            rs = db.retrieveAllData(dateFrom, dateTo);
            Object Rows_Object_Array[][];
            Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];
            
            int R = 0;
            while(rs.next()) {
                for(int C=1; C<=NumberOfColumns;C++) {
                    Rows_Object_Array[R][C-1] = rs.getObject(C);
                    if( rs.getObject(7).toString().equals("Yes")){
                    	Rows_Object_Array[R][6] = Boolean.TRUE;
                    }else{
                    	Rows_Object_Array[R][6] = Boolean.FALSE;
                    }
                	
				
                }
                R++;
            }
            //Finally load data to the table
            final boolean[] canEdit = new boolean[] { false, false, false,
					false, false, false, true,false};
            DefaultTableModel model = new DefaultTableModel(Rows_Object_Array, new String[] {
            		"ID", "Vendor Name", "Date", "Time","Invoice No","Total","Paid","PO ID"
    			}) {
            	public boolean isCellEditable(int row, int column) {
					return canEdit[column];
				}

//				public Class<?> getColumnClass(int colIndex) {
//
//					return getValueAt(0, colIndex).getClass();
//
//				}
    		};
    		invoicebrowserTable.setModel(model);
    		if(NumberOfRows>0)
    		{countALLString();
    		}
    		TableColumn columnsize = null;
    		for (int i = 0; i < 6; i++) {
    			columnsize = invoicebrowserTable.getColumnModel().getColumn(i);
    			columnsize.setPreferredWidth(110); 
    			if(i==1||i==2||i==3)
    			{
    				columnsize.setPreferredWidth(150); 
    			}
    			if(i==5)
    				columnsize.setPreferredWidth(60); 
    		} 
    		
    		 if(NumberOfRows>0)
             {get();}
    		 originalTableModel = (Vector) ((DefaultTableModel) invoicebrowserTable.getModel())
 					.getDataVector().clone();
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	public void countALLString() {
		double amt=0;int r=0;
		// TODO Auto-generated method stub
		for(int i=0;i<invoicebrowserTable.getRowCount();i++)
		{
			amt+=Double.parseDouble(!invoicebrowserTable.getValueAt(i, 5).toString().equals("")?invoicebrowserTable.getValueAt(i, 5).toString():0+"");
			r++;
		}
		if(invoicebrowserTable.getRowCount()>0)
		{
			
			textFieldCount.setText(r+"");
			textFieldAmt.setText(amt+"");
		}
	}
	private void get() {
		// TODO Auto-generated method stub
		invoicebrowserTable.setAutoCreateRowSorter(true);
	}
	public JTextField getSearchET() {
		return searchTF;
	}
	private void handleRowClick(MouseEvent e) {
		ListSelectionModel selectionModel = invoicebrowserTable.getSelectionModel();
		Point contextMenuOpenedAt = e.getPoint();
		int clickedRow = invoicebrowserTable.rowAtPoint(contextMenuOpenedAt);

		if (clickedRow < 0) {
			// No row selected
			selectionModel.clearSelection();
		} else {
			// Some row selected
			if ((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) {
				int maxSelect = selectionModel.getMaxSelectionIndex();

				if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
					// Shift + CTRL
					selectionModel.addSelectionInterval(maxSelect, clickedRow);
				} else {
					// Shift
					selectionModel.setSelectionInterval(maxSelect, clickedRow);
				}
			} else if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
				// CTRL
				selectionModel.addSelectionInterval(clickedRow, clickedRow);
			} else {
				// No modifier key pressed
				selectionModel.setSelectionInterval(clickedRow, clickedRow);
			}
		}
	}
}
