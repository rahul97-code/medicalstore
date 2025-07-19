package hms.store.gui;

import hms.main.DateFormatChange;
import hms.opd.database.OPDDBConnection;
import hms.patient.slippdf.MedicalStoreBillSlippdf;
import hms.patient.slippdf.MedicalStoreIPDPdf;
import hms.store.database.BillingDBConnection;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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

public class BillBrowser extends JDialog {

	public JPanel contentPane;
	private JTable billbrowserTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom,dateTo;
	private JTextField searchPatientNameTB;
	private JLabel lblTotalamountlb;
	double totalAmount=0;
	Vector originalTableModel;
	String userid="";
	JCheckBox chckbxNewCheckBox;

	public static void main(String[] arg)
	{
		BillBrowser billBrowser=new BillBrowser(null,"","");
		billBrowser.setVisible(true);

	}
	/**
	 * Create the frame.
	 */
	public BillBrowser(final JFrame owner,final String Userid,String type) {
		//		this.userid=Userid;
		 super(owner, "Bill Entry List", false);
		setResizable(false);
		//		System.out.println(Userid+"userid");
		setIconImage(Toolkit.getDefaultToolkit().getImage(BillBrowser.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 950, 524);
		contentPane = new JPanel();
//		setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setEnabled(false);
		scrollPane.setBounds(198, 11, 726, 418);
		contentPane.add(scrollPane);

		lblTotalamountlb = new JLabel("");
		if(!StoreMain.update_item_access.equals("1")) {
			lblTotalamountlb.hide();
		}
		lblTotalamountlb.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTotalamountlb.setBounds(825, 440, 137, 25);
		contentPane.add(lblTotalamountlb);

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
						"Bill No.", "OPD ID", "Patient Name", "Doctor Name", "Bill Date","Surcharge","Tax","Amount", "Insurance", "Time","Bill Type","User Name"
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
				// TODO Auto-generated method stub
				if (arg0.getClickCount() == 2) {
					try {
						int row = billbrowserTable.getSelectedRow();
						int column = billbrowserTable.getSelectedColumn();

						Object selectedObject = billbrowserTable.getValueAt(row, 0);
						Object selectedObject1 = billbrowserTable.getValueAt(row, 10);
						Object selectedObject2= billbrowserTable.getValueAt(row, 8);
						Double selectedObject3= Double.parseDouble(billbrowserTable.getValueAt(row, 13).toString());
						boolean bool= selectedObject3>0?false:true;
						if(selectedObject1.toString().equals("IPD")) {
							new MedicalStoreIPDPdf(selectedObject+"");
						}else {
							new MedicalStoreBillSlippdf(selectedObject+"",selectedObject1+"",selectedObject2+"",false,bool);
						}
					} catch (DocumentException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		addMouseListener(new MouseAdapter() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		        setAlwaysOnTop(true);
		        toFront();
		        requestFocus();
		        setAlwaysOnTop(false);
		    }
		});

		
		JButton btnNewButton = new JButton("New Bill");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				OPDBillEntry opdEntery=new OPDBillEntry(owner,BillBrowser.this);
//				opdEntery.setModal(true);
				opdEntery.setVisible(true);
			}
		});
		btnNewButton.setIcon(new ImageIcon(BillBrowser.class.getResource("/icons/Business.png")));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(33, 440, 160, 35);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Close");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(BillBrowser.class.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setBounds(550, 440, 160, 35);
		contentPane.add(btnNewButton_1);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(4, 11, 192, 418);
		contentPane.add(panel);
		panel.setLayout(null);
		searchPatientNameTB = new JTextField();
		searchPatientNameTB.setBounds(6, 101, 178, 28);
		panel.add(searchPatientNameTB);
		chckbxNewCheckBox = new JCheckBox("My Bills");
		chckbxNewCheckBox.setEnabled(false);
		chckbxNewCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 17));
		chckbxNewCheckBox.setBounds(62, 10, 126, 23);
		panel.add(chckbxNewCheckBox);
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (chckbxNewCheckBox.isSelected() ){
					System.out.println("clicked");
					userid=Userid;
					populateTable(dateFrom, dateTo,userid);
				}
				else{
					userid="";
					populateTable(dateFrom, dateTo,userid);
				}


			}
		}
				);
		if(type.equals("admin")){
			chckbxNewCheckBox.setVisible(false);

		}
		//		   if (chckbxNewCheckBox.isSelected()){
		//			   this.userid=Userid;
		//		   }
		//           
		//            else{
		//            	 this.userid="";
		//            }


		searchPatientNameTB.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = searchPatientNameTB.getText() + "";
						searchTableContents(str);
						//						if (!str.equals("")) {
						//							populateTable(dateFrom, dateTo,userid);
						//						} else {
						//							populateTable1(dateFrom, dateTo,userid);
						//						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = searchPatientNameTB.getText() + "";
						searchTableContents(str);
						//						if (!str.equals("") ) {
						//							
						//							populateTable(dateFrom, dateTo,userid);
						//						} else {
						//							populateTable1(dateFrom, dateTo,userid);
						//						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = searchPatientNameTB.getText() + "";
						searchTableContents(str);
						//						if (!str.equals("")) {
						//							populateTable(dateFrom, dateTo,userid);
						//						} else {
						//							populateTable1(dateFrom, dateTo,userid);
						//						}
					}
				});
		searchPatientNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSelectDisease = new JLabel("Search Patient");
		lblSelectDisease.setBounds(52, 76, 95, 14);
		panel.add(lblSelectDisease);
		lblSelectDisease.setFont(new Font("Tahoma", Font.PLAIN, 12));

		dateFromDC = new JDateChooser();
		dateFromDC.setBounds(6, 165, 178, 25);
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
							populateTable1(dateFrom, dateTo,userid);
						}
					}
				});
		dateFromDC.setDate(new Date());
		dateFromDC.setMaxSelectableDate(new Date());
		dateFromDC.setDateFormatString("yyyy-MM-dd");


		dateToDC = new JDateChooser();
		dateToDC.setBounds(6, 226, 178, 25);
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
							populateTable1(dateFrom, dateTo,userid);
						}
					}
				});
		dateToDC.setDate(new Date());
		dateToDC.setMaxSelectableDate(new Date());
		dateToDC.setDateFormatString("yyyy-MM-dd");

		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setBounds(37, 201, 73, 14);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(52, 140, 82, 14);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JRadioButton rdbtnAll = new JRadioButton("All");
		rdbtnAll.setBounds(6, 296, 49, 23);
		panel.add(rdbtnAll);
		rdbtnAll.setSelected(true);
		rdbtnAll.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnAll);

		JRadioButton rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setBounds(51, 296, 59, 23);
		panel.add(rdbtnMale);
		rdbtnMale.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnMale);

		JRadioButton rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setBounds(112, 296, 73, 23);
		panel.add(rdbtnFemale);
		rdbtnFemale.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnFemale);

		JLabel lblSelectSex = new JLabel("Select Sex");
		lblSelectSex.setBounds(50, 262, 73, 14);
		panel.add(lblSelectSex);
		lblSelectSex.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnNewButton_2 = new JButton("Search");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton_2.setBounds(36, 326, 111, 35);
		panel.add(btnNewButton_2);
		btnNewButton_2.setIcon(new ImageIcon(BillBrowser.class.getResource("/icons/zoom_r_button.png")));
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnExcel = new JButton("Excel");
		btnExcel.setBounds(36, 372, 101, 35);
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
		btnExcel.setIcon(new ImageIcon(BillBrowser.class.getResource("/icons/1BL.PNG")));
		btnExcel.setFont(new Font("Tahoma", Font.PLAIN, 12));


		JLabel lblTotal = new JLabel("Total :");
		lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotal.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTotal.setBounds(712, 440, 103, 25);
		contentPane.add(lblTotal);

		JButton button = new JButton("New Bill 2");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				OPDBillEntry opdEntery=new OPDBillEntry(owner,BillBrowser.this);
//				opdEntery.setModal(true);
				opdEntery.setVisible(true);
			}
		});
		button.setFont(new Font("Tahoma", Font.PLAIN, 12));
		button.setBounds(203, 440, 160, 35);
		contentPane.add(button);

		JButton btnIpdBilling = new JButton("IPD Billing");
		btnIpdBilling.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				IPDBillEntry opdEntery=new IPDBillEntry(owner,BillBrowser.this,null,"",false,"","");
//				opdEntery.setModal(true);
				opdEntery.setVisible(true);
			}
		});
		btnIpdBilling.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnIpdBilling.setBounds(380, 440, 160, 35);
		contentPane.add(btnIpdBilling);


		populateTable1(DateFormatChange.StringToMysqlDate(new Date()),DateFormatChange.StringToMysqlDate(new Date()),userid);
	}

	public void populateTable(String dateFrom,String dateTo,String UserId)
	{
		System.out.println(UserId+"new");
		lblTotalamountlb.setText("");
		totalAmount=0;
		String index=searchPatientNameTB.getText() + "";
		try {
			BillingDBConnection db=new BillingDBConnection();
			ResultSet rs=null;
			if(UserId.equals("")){
				rs = db.retrieveAllData1(dateFrom, dateTo,index);
			}else{
				if(index.equals("")){
					System.out.println(UserId+"if");
					rs = db.retrieveAllDatauserwise(dateFrom, dateTo,UserId);
				}else{
					System.out.println(UserId+"else");
					rs = db.retrieveAllDatauserwiseSearch(dateFrom, dateTo,UserId,index);
				}
			}
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			rs.last();
			NumberOfRows=rs.getRow();
			rs.beforeFirst();

			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns+1];

			int R = 0;
			while(rs.next()) {
				for(int C=1; C<=NumberOfColumns;C++) {
					Rows_Object_Array[R][C-1] = rs.getObject(C);


				}
				totalAmount=totalAmount+Double.parseDouble(rs.getObject(8).toString());
				R++;
			}
			//Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array, new String[] {
					"Bill No.", "OPD ID", "Patient Name", "Doctor Name", "Bill Date","Surcharge","Tax","Amount", "Insurance", "Time","Bill Type","User Name","Karuna Discount","Scanned"

			}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			billbrowserTable.setModel(model);
			billbrowserTable.getColumnModel().getColumn(14).setCellRenderer(new DefaultTableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value,
						boolean isSelected, boolean hasFocus, int row, int column) {

					Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

					if (value != null && value.toString().equalsIgnoreCase("yes")) {
						c.setForeground(Color.WHITE);
						c.setBackground(Color.GREEN.darker());
					} else if(value != null && value.toString().equalsIgnoreCase("no")){
						c.setForeground(Color.WHITE);
						c.setBackground(Color.RED);
					}else {
						c.setForeground(null);
						c.setBackground(null);
					}

					return c;
				}
			}); 
			originalTableModel = (Vector) ((DefaultTableModel) billbrowserTable.getModel())
					.getDataVector().clone();
			TableColumn columnsize = null;
			for (int i = 0; i < 9; i++) {
				columnsize = billbrowserTable.getColumnModel().getColumn(i);
				columnsize.setPreferredWidth(110); 
				if(i==1||i==2||i==3)
				{
					columnsize.setPreferredWidth(150); 
				}
				if(i==5)
					columnsize.setPreferredWidth(60); 
			}   
			lblTotalamountlb.setText(""+totalAmount);
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(BillBrowser.class.getName()).log(Level.SEVERE, null, ex);
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
	public void populateTable1(String dateFrom,String dateTo,String userid)
	{
		System.out.println("1pop");
		searchPatientNameTB.setText("");
		lblTotalamountlb.setText("");
		totalAmount=0;
		String index=searchPatientNameTB.getText() + "";
		try {
			BillingDBConnection db=new BillingDBConnection();
			ResultSet rs =null;
			if(userid.equals("")){
				rs = db.retrieveAllData(dateFrom, dateTo);
			}else{
				rs = db.retrieveAllDatauserwise(dateFrom, dateTo,userid);
				;
			}
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();
			rs.last();
			NumberOfRows=rs.getRow();
			rs.beforeFirst();


			//to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while(rs.next()) {
				for(int C=1; C<=NumberOfColumns;C++) {
					Rows_Object_Array[R][C-1] = rs.getObject(C);

				}
				totalAmount=totalAmount+Double.parseDouble(rs.getObject(8).toString());
				R++;
			}
			//Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array, new String[] {
					"Bill No.", "OPD ID", "Patient Name", "Doctor Name", "Bill Date","Surcharge","Tax","Amount", "Insurance", "Time","Bill Type","User Name","Payment","Karuna Discount","Scanned"
			}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			billbrowserTable.setModel(model);
			billbrowserTable.getColumnModel().getColumn(14).setCellRenderer(new DefaultTableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value,
						boolean isSelected, boolean hasFocus, int row, int column) {

					Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

					if (value != null && value.toString().equalsIgnoreCase("yes")) {
						c.setForeground(Color.WHITE);
						c.setBackground(Color.GREEN.darker());
					} else if(value != null && value.toString().equalsIgnoreCase("no")){
						c.setForeground(Color.WHITE);
						c.setBackground(Color.RED);
					}else {
						c.setForeground(null);
						c.setBackground(null);
					}

					return c;
				}
			});

			originalTableModel = (Vector) ((DefaultTableModel) billbrowserTable.getModel())
					.getDataVector().clone();
			TableColumn columnsize = null;
			for (int i = 0; i < 7; i++) {
				columnsize = billbrowserTable.getColumnModel().getColumn(i);
				columnsize.setPreferredWidth(110); 
				if(i==1||i==2||i==3)
				{
					columnsize.setPreferredWidth(150); 
				}
				if(i==5)
					columnsize.setPreferredWidth(60); 
			}  

			lblTotalamountlb.setText(""+totalAmount);
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(BillBrowser.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}