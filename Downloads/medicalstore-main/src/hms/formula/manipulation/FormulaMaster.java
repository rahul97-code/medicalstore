package hms.formula.manipulation;

import hms.admin.gui.AdminMain;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.main.GeneralDBConnection;
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
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
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
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;
import javax.swing.JComboBox;

public class FormulaMaster extends JDialog {

	public JPanel contentPane;
	private JTable table;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	String dateFrom,dateTo;
	double totalAmount=0;
	private String content;
	Vector originalTableModel;
	public JTextField textFieldCount = new JTextField();
	public JTextField textFieldAmt  = new JTextField();
	String selectedIndex=null,vendorName=null;
	JPopupMenu contextMenu = new JPopupMenu();
	Vector<String> insID=new Vector<>();
	final DefaultComboBoxModel insuranceModel = new DefaultComboBoxModel();
	final DefaultComboBoxModel categoryModel = new DefaultComboBoxModel();
	final DefaultComboBoxModel formulaModel = new DefaultComboBoxModel();

	String[] enumFormulasValues;
	String categorySTR="";
	private JComboBox formulaCB;
	private JComboBox categoryCB;
	private JComboBox insCB;
	public static void main(String[] arg)
	{
		FormulaMaster browser=new FormulaMaster();
		browser.setVisible(true);
	}
	/**
	 * Create the frame.
	 */
	public FormulaMaster() {
		JMenuItem search=new JMenuItem("  Search <<");
		search.setFont(new Font("Dialog", Font.ITALIC, 12));
		contextMenu.add(search);

		setResizable(false);
		setTitle("Formula Master");
		setIconImage(Toolkit.getDefaultToolkit().getImage(FormulaMaster.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 632, 524);
		contentPane = new JPanel();
		setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setEnabled(false);
		scrollPane.setBounds(12, 123, 584, 306);
		contentPane.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));

		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
				}
				));
		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseListener() {



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
					content=table.getValueAt(row, column)+"";
				}

			}
			private void doPop(MouseEvent e) {
				if (table.getSelectedRowCount() == 0) {
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

			}
		});

		search.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e) {        
				DefaultTableModel currtableModel = (DefaultTableModel) table.getModel();
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
			}  
		});  
		JButton btnNewButton = new JButton("Update");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = table.getSelectedRow();
				int column = table.getSelectedColumn();
				if(row==-1) { 
					JOptionPane.showMessageDialog(null,
							"Please select row to update!", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				int option = JOptionPane.showConfirmDialog(null, "Do you want to Save?", "Confirm Update", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {      
		
				GeneralDBConnection db=new  GeneralDBConnection();
				try {
					db.inserFormulaData(new String[]{table.getValueAt(row, 0)+"",table.getValueAt(row, 1)+"",table.getValueAt(row, 2)+"",table.getValueAt(row, 3)+"",table.getValueAt(row, 4)+""});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					db.closeConnection();
				}
				JOptionPane.showMessageDialog(null,
						"Data Saved Successfully ", "Data Save",
						JOptionPane.INFORMATION_MESSAGE);
				getAllItemCategories();
				populateTable();
				}
			}
		});
		btnNewButton.setIcon(new ImageIcon(FormulaMaster.class.getResource("/icons/SAVE.PNG")));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(262, 440, 160, 35);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Close");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(FormulaMaster.class.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setBounds(434, 440, 160, 35);
		contentPane.add(btnNewButton_1);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 11, 584, 100);
		contentPane.add(panel);
		panel.setLayout(null);

		categoryCB = new JComboBox();
		categoryCB.setFont(new Font("Dialog", Font.PLAIN, 12));
		categoryCB.setEditable(true);
		categoryCB.setBounds(12, 69, 196, 24);
		panel.add(categoryCB);
		final JTextField ItemLocationtext = (JTextField) categoryCB.getEditor()
				.getEditorComponent();
		ItemLocationtext.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				String text = ItemLocationtext.getText();
				if (!text.equals("")) {
					categorySTR = text;
				}
			}
		});

		insCB = new JComboBox();
		insCB.setFont(new Font("Dialog", Font.PLAIN, 12));
		insCB.setBounds(11, 24, 196, 24);
		panel.add(insCB);

		getAllFormulas();
		formulaCB = new JComboBox(enumFormulasValues);
		formulaCB.setFont(new Font("Dialog", Font.PLAIN, 12));
		formulaCB.setBounds(233, 69, 196, 24);
		panel.add(formulaCB);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(categorySTR.equals("") || categorySTR.equals("select")) { 
					JOptionPane.showMessageDialog(null,
							"Please enter item category first!", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				int option = JOptionPane.showConfirmDialog(null, "Do you want to Save?", "Confirm Update", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {          
					GeneralDBConnection db=new  GeneralDBConnection();
					try {
						db.inserFormulaData(new String[]{null,insID.get(insCB.getSelectedIndex()),insCB.getSelectedItem()+"",categorySTR,formulaCB.getSelectedItem()+""});
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}finally {
						db.closeConnection();
					}
					JOptionPane.showMessageDialog(null,
							"Data Saved Successfully ", "Data Save",
							JOptionPane.INFORMATION_MESSAGE);
					getAllItemCategories();
					populateTable();
				}
			}

		});
		btnSave.setBounds(451, 68, 121, 25);
		panel.add(btnSave);

		JLabel lblInsurance = new JLabel("Insurance :");
		lblInsurance.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblInsurance.setBounds(12, 8, 95, 15);
		panel.add(lblInsurance);

		JLabel lblInsurance_1 = new JLabel("Formula :");
		lblInsurance_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblInsurance_1.setBounds(233, 53, 95, 15);
		panel.add(lblInsurance_1);

		JLabel lblInsurance_2 = new JLabel("Item Category :");
		lblInsurance_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblInsurance_2.setBounds(12, 52, 95, 15);
		panel.add(lblInsurance_2);

		JLabel lblFormulaMaster = new JLabel("Formula Master");
		lblFormulaMaster.setForeground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
		lblFormulaMaster.setIcon(new ImageIcon(FormulaMaster.class.getResource("/icons/6406.gif")));
		lblFormulaMaster.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 19));
		lblFormulaMaster.setBounds(327, 10, 245, 49);
		panel.add(lblFormulaMaster);

		getAllItemCategories();
		getAllinsurance();

		populateTable();
	}
	public void searchTableContents(String searchString) {
		DefaultTableModel currtableModel = (DefaultTableModel) table.getModel();
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
	public void populateTable() {
		GeneralDBConnection db = null;
		try {
			db = new GeneralDBConnection();
			JComboBox<String> comboBox = new JComboBox<>(enumFormulasValues);
			ResultSet rs = db.retrieveFormulaData();

			int rowCount = getRowCount(rs);
			int colCount = rs.getMetaData().getColumnCount();

			Object[][] data = new Object[rowCount][colCount];
			loadDataToObjectArray(rs, data);

			db.closeConnection(); 

			final boolean[] canEdit = {false, false, false, false, true};

			DefaultTableModel model = new DefaultTableModel(data, new String[] {
					"ID", "Ins ID", "Insurance Name", "Item Category", "Formula"
			}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return canEdit[column];
				}
			};
			table.setModel(model);
			TableColumn formulaColumn = table.getColumnModel().getColumn(4);
			formulaColumn.setCellEditor(new DefaultCellEditor(comboBox));
			if (rowCount > 0) {
				get();
			}
			table.getColumnModel().getColumn(0).setPreferredWidth(70);
			table.getColumnModel().getColumn(1).setPreferredWidth(100);
			table.getColumnModel().getColumn(2).setPreferredWidth(150);
			table.getColumnModel().getColumn(3).setPreferredWidth(120);
			table.getColumnModel().getColumn(4).setPreferredWidth(130);
			originalTableModel = (Vector) ((DefaultTableModel) table.getModel()).getDataVector().clone();

		} catch (SQLException ex) {
			Logger.getLogger(FormulaMaster.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			if (db != null) {
				db.closeConnection();  
			}
		}
	}

	private int getRowCount(ResultSet rs) throws SQLException {
		rs.last();
		int rowCount = rs.getRow();
		rs.beforeFirst();
		return rowCount;
	}

	private void loadDataToObjectArray(ResultSet rs, Object[][] data) throws SQLException {
		int row = 0;
		while (rs.next()) {
			for (int col = 1; col <= rs.getMetaData().getColumnCount(); col++) {
				data[row][col - 1] = rs.getObject(col);
			}
			row++;
		}
	}
	public void getAllFormulas() {
		GeneralDBConnection db =  new GeneralDBConnection();
		enumFormulasValues=db.retrieveAllFormulas(); 
		db.closeConnection();
	}

	public void getAllinsurance() {
		insuranceModel.removeAllElements();
		insuranceModel.addElement("Unknown");
		insID.add("0");
		InsuranceDBConnection dbConnection = new InsuranceDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				insuranceModel.addElement(resultSet.getObject(2).toString());
				insID.add(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		insCB.setModel(insuranceModel);
		insCB.setSelectedIndex(0);
	}
	public void getAllItemCategories() {
		categoryModel.removeAllElements();
		categoryModel.addElement("select");
		GeneralDBConnection dbConnection = new GeneralDBConnection();
		ResultSet resultSet = dbConnection.retrieveItemCategories();
		try {
			while (resultSet.next()) {
				categoryModel.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		categoryCB.setModel(categoryModel);
		categoryCB.setSelectedIndex(0);
	}
	private void get() {
		// TODO Auto-generated method stub
		table.setAutoCreateRowSorter(true);
	}

}
