package hms.store.gui;

import hms.main.DateFormatChange;
import hms.patient.slippdf.PO_PDF;
import hms.report.excel.POExcel;
import hms.store.database.IndentDBConnection;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;

public class IndentBrowser extends JDialog {

	public JPanel contentPane;
	private JTable pobrowserTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom, dateTo;
	double totalAmount = 0;

	public static void main(String[] arg) {
		IndentBrowser browser = new IndentBrowser();
		browser.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public IndentBrowser() {
		setResizable(false);
		setTitle("Indent List");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				IndentBrowser.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 950, 466);
		contentPane = new JPanel();
		setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(198, 11, 726, 418);
		contentPane.add(scrollPane);

		pobrowserTable = new JTable();
		pobrowserTable.setToolTipText("Double Click to open Indent");
		pobrowserTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		pobrowserTable.getTableHeader().setReorderingAllowed(false);
		pobrowserTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		pobrowserTable.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "Indent No", "Date", "Time", "Request By",
						"Approved By", "Approved Date", "Approved Status" }));
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
				// TODO Auto-generated method stub
				if (arg0.getClickCount() == 2) {
					JTable target = (JTable) arg0.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					// do some action

					Object selectedObject = pobrowserTable.getModel()
							.getValueAt(row, 0);
					
					Object selectedObject0 = pobrowserTable.getModel()
							.getValueAt(row, 1);
					
					Object selectedObject1 = pobrowserTable.getModel()
							.getValueAt(row, 5);

					IndentItems indentItems=new IndentItems(selectedObject.toString(),selectedObject1.toString(),selectedObject0.toString(), IndentBrowser.this);
					indentItems.setModal(true);
					indentItems.setVisible(true);
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

		JButton btnExcel = new JButton("Excel");
		btnExcel.setBounds(22, 230, 160, 35);
		panel.add(btnExcel);
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
		btnExcel.setIcon(new ImageIcon(IndentBrowser.class
				.getResource("/icons/1BL.PNG")));
		btnExcel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
				JButton btnNewButton_1 = new JButton("Close");
				btnNewButton_1.setBounds(22, 276, 160, 35);
				panel.add(btnNewButton_1);
				btnNewButton_1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				btnNewButton_1.setIcon(new ImageIcon(IndentBrowser.class
						.getResource("/icons/CANCEL.PNG")));
				btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		populateTable(DateFormatChange.StringToMysqlDate(new Date()),
				DateFormatChange.StringToMysqlDate(new Date()));
	}

	public void populateTable(String dateFrom, String dateTo) {
		try {
			IndentDBConnection db = new IndentDBConnection();
			ResultSet rs = db.retrieveAllData(dateFrom, dateTo);

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
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] {"ID", "Indent No", "Date", "Time", "Request By",
					"Approved By", "Approved Date", "Approved Status" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			pobrowserTable.setModel(model);

			pobrowserTable.getColumnModel().getColumn(0).setPreferredWidth(70);
			pobrowserTable.getColumnModel().getColumn(1).setPreferredWidth(100);
			pobrowserTable.getColumnModel().getColumn(2).setPreferredWidth(100);
			pobrowserTable.getColumnModel().getColumn(3).setPreferredWidth(100);
			pobrowserTable.getColumnModel().getColumn(4).setPreferredWidth(80);
			pobrowserTable.getColumnModel().getColumn(5).setPreferredWidth(80);
			pobrowserTable.getColumnModel().getColumn(6).setPreferredWidth(90);
			pobrowserTable.getColumnModel().getColumn(7).setPreferredWidth(110);
		} catch (SQLException ex) {
			Logger.getLogger(IndentBrowser.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}
}
