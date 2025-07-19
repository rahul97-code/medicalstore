package hms.reportstable;

import hms.main.DateFormatChange;
import hms.store.database.BillingDBConnection;
import hms.store.database.IndentDBConnection;
import hms.store.gui.IndentBrowser;
import hms.store.gui.IndentItems;
import hms.store.gui.NewPO;
import hms.store.gui.StoreMain;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.toedter.calendar.JDateChooser;
import javax.swing.SwingConstants;
import java.awt.Color;

public class ApprovedList extends JDialog {

	public JPanel contentPane;
	private JTable jTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	String dateFrom, dateTo;
	private JTextField searchTF;
	Vector originalTableModel;
String selectedObject="",selectedObject0="";
	public static void main(String[] arg) {
		new ApprovedList().setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public ApprovedList() {
		setResizable(false);
		setTitle("Approved List");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				NewPurchaseOrderReport.class
						.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1017, 464);
		contentPane = new JPanel();
		setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 52, 991, 377);
		contentPane.add(scrollPane);

		jTable = new JTable();
		jTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		jTable.getTableHeader().setReorderingAllowed(false);
		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setModel(new DefaultTableModel(new Object[][] {}, new String[] {
				"OPD No.", "Patient ID", "Patient Name", "Doctor Name",
				"OPD Date", "Token No.", "OPD Type" }));
		scrollPane.setViewportView(jTable);
		searchTF = new JTextField();
		searchTF.setBounds(67, 13, 178, 28);
		contentPane.add(searchTF);
		searchTF.getDocument().addDocumentListener(new DocumentListener() {
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
		searchTF.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSelectDisease = new JLabel("Search");
		lblSelectDisease.setBounds(10, 19, 95, 14);
		contentPane.add(lblSelectDisease);
		lblSelectDisease.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnExcel = new JButton("Export Excel");
		btnExcel.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnExcel.setBounds(697, 8, 147, 38);
		// contentPane.add(btnExcel);
		JButton btnPO = new JButton("Create PO");
		btnPO.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnPO.setBounds(854, 8, 147, 38);
		contentPane.add(btnPO);
		btnPO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewPO PO = new NewPO(selectedObject);
				PO.setModal(true);
				PO.setVisible(true);
			}});
		JButton btnSaveIntend = new JButton("Save Indent");
		btnSaveIntend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(null,
						"Are you sure to save indent?", "WARNING",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					// yes option
				} else {
					return;
				}
				IndentDBConnection indentDBConnection = new IndentDBConnection();
				String indentNumber = indentDBConnection.retrieveIndentNUMBER();

				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				int index = 0;
				String[] data = new String[30];
				data[0] = indentNumber;
				data[1] = DateFormatChange.StringToMysqlDate(new Date());
				data[2] = "" + timeFormat.format(cal1.getTime());
				data[3] = "" + StoreMain.userName; // user
				data[4]="YES";

				try {
					index = indentDBConnection.inserIndent(data);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				TableModel model = jTable.getModel();

				for (int rows = 0; rows < model.getRowCount(); rows++) { // For
																			// each
																			// table
																			// row
					String item_id = model.getValueAt(rows, 0) + "";
					String quantity = model.getValueAt(rows, 6) + "";

					String subQuery = "(SELECT '"
							+ index
							+ "',A.`ITEM CODE`, A.`ITEM NAME`, A.`DESC`, A.`BRAND`, A.`CONCENTRATION`, A.`SALT`, A.`PACK SIZE`, A.`MRP`, A.`PURCHASE PRICE`, A.`CURRENT STOCK`, A.`Two Months Qty`, A.`15 Days Qty`, A.`Already Ordered`, A.`To Be Ordered Qty`, A.`Preferred Vendor`,'"
							+ quantity
							+ "' FROM `auto_purchase_order` A WHERE A.`ITEM CODE`='"
							+ item_id + "')";
//					try {
////						indentDBConnection.insertIndentItems(subQuery);
//					} catch (SQLException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}

				}
				indentDBConnection.closeConnection();
				JOptionPane.showMessageDialog(null,
						"Indent Saved Successfully", "Indent Save",
						JOptionPane.INFORMATION_MESSAGE);
				dispose();

			}
		});
		btnSaveIntend.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnSaveIntend.setBounds(854, 8, 147, 38);
		// contentPane.add(btnSaveIntend);

		JLabel lblForChangeTo = new JLabel(
				"For Change To Be Order Quantity Double Click On It");
		lblForChangeTo.setForeground(Color.RED);
		lblForChangeTo.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblForChangeTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblForChangeTo.setBounds(255, 13, 435, 28);
		// contentPane.add(lblForChangeTo);
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(new File("PO_" + new Date()
						+ ".xls"));
				if (fileChooser.showSaveDialog(ApprovedList.this) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					// save to file
					ReportExcel(jTable, file.toPath().toString());
				}
			}
		});
		populateTable1();
	}

	public void populateTable1() {

		try {
			IndentDBConnection db = new IndentDBConnection();

			ResultSet rs = db.retrieveApprovedData();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			Vector<String> columnNames = new Vector<String>();
			for (int i = 0; i < columnsNumber; i++) {
				columnNames.add((rsmd.getColumnName(i + 1)));
			}
			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}

			rs.beforeFirst();

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
			final boolean[] canEdit = new boolean[] { false, false, false,
					false, false, false, true };
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					columnNames.toArray()) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return canEdit[column];
				}
			};
			jTable.setModel(model);
			originalTableModel = (Vector) ((DefaultTableModel) jTable
					.getModel()).getDataVector().clone();
			jTable.getColumnModel().getColumn(0).setPreferredWidth(150);
			jTable.getColumnModel().getColumn(1).setPreferredWidth(150);
			jTable.getColumnModel().getColumn(2).setPreferredWidth(100);
			jTable.getColumnModel().getColumn(3).setPreferredWidth(150);
			jTable.getColumnModel().getColumn(4).setPreferredWidth(100);
			jTable.getColumnModel().getColumn(5).setPreferredWidth(90);
//			jTable.getColumnModel().getColumn(5).setPreferredWidth(150);
//			jTable.getColumnModel().getColumn(6).setPreferredWidth(70);
//			jTable.getColumnModel().getColumn(7).setPreferredWidth(70);
//			jTable.getColumnModel().getColumn(8).setPreferredWidth(70);
//			jTable.getColumnModel().getColumn(9).setPreferredWidth(70);
//			jTable.getColumnModel().getColumn(10).setPreferredWidth(80);
			// jTable.getColumnModel().getColumn(11).setPreferredWidth(80);
			jTable.addMouseListener(new MouseListener() {

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
					if (arg0.getClickCount() == 1) {
						JTable target = (JTable) arg0.getSource();
						int row = target.getSelectedRow();
						int column = target.getSelectedColumn();
						// do some action

						 selectedObject = jTable.getModel().getValueAt(
								row, 0).toString();

//						 selectedObject0 = jTable.getModel().getValueAt(
//								row, 1).toString();

//						Object selectedObject1 = jTable.getModel().getValueAt(
//								row, 5);
//						System.out.println("kk"+selectedObject0.toString());
						
					}
				}
			});
			// searchTF.setText("NO ORDER");
		} catch (SQLException ex) {
			Logger.getLogger(IndentItems.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public void searchTableContents(String searchString) {
		DefaultTableModel currtableModel = (DefaultTableModel) jTable
				.getModel();
		// To empty the table before search
		currtableModel.setRowCount(0);
		// To search for contents from original table content
		for (Object rows : originalTableModel) {
			Vector rowVector = (Vector) rows;
			for (Object column : rowVector) {
				String str = column + "";
				if (str.toLowerCase().contains(searchString.toLowerCase())) {
					// content found so adding to table
					currtableModel.addRow(rowVector);
					break;
				}
			}
		}
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
					String str = model.getValueAt(rows, cols) + "";
					row.createCell(cols).setCellValue(str); // Write value
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
}
