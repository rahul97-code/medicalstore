package hms.store.gui;

import hms.admin.gui.AdminMain;
import hms.store.database.ItemsDBConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;

public class ItemBrowser extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JButton btnCancel;
	private JButton btnNewItem;
	private JButton btnEditItem;
	int selectedRowIndex;
	private JButton btnDelete;
	JButton btnViewBatch;
	private JTextField searchItemTF;
	Vector originalTableModel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ItemBrowser dialog = new ItemBrowser();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ItemBrowser() {
		setTitle("Items Browser");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				ItemBrowser.class.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 1060, 536);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 39, 1035, 398);
			contentPanel.add(scrollPane);
			{
				table = new JTable();
				table.getTableHeader().setReorderingAllowed(false);
				table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setModel(new DefaultTableModel(new Object[][] { { null,
						null, null, null, null }, }, new String[] { "Item Id",
						"Item Name", "Item Desc.", "Expiry Date", "Item Stock",
						"Pack Size" }));
				table.getColumnModel().getColumn(1).setPreferredWidth(180);
				table.getColumnModel().getColumn(1).setMinWidth(150);
				table.getColumnModel().getColumn(2).setPreferredWidth(180);
				table.getColumnModel().getColumn(2).setMinWidth(150);
				table.getColumnModel().getColumn(3).setMinWidth(150);
				table.getColumnModel().getColumn(4).setPreferredWidth(150);
				table.getColumnModel().getColumn(4).setMinWidth(100);
				table.setFont(new Font("Tahoma", Font.BOLD, 14));
				table.getSelectionModel().addListSelectionListener(
						new ListSelectionListener() {
							@Override
							public void valueChanged(ListSelectionEvent e) {
								// TODO Auto-generated method stub
								selectedRowIndex = table.getSelectedRow();
								selectedRowIndex = table
										.convertRowIndexToModel(selectedRowIndex);
								int selectedColumnIndex = table
										.getSelectedColumn();
								btnEditItem.setEnabled(true);
								 btnViewBatch.setEnabled(true);
								if (StoreMain.userID.equals("5")
										&& AdminMain.userID.equals("5")) {
									btnDelete.setEnabled(true);
								} else {
									btnDelete.setEnabled(false);
								}
								// btnDelete.setEnabled(true);
							}
						});
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

							Object selectedObject1 = table.getModel()
									.getValueAt(selectedRowIndex, 0);
							String id = selectedObject1.toString();
							EditItem editItem = new EditItem(id);
							editItem.setModal(true);
							editItem.setVisible(true);
							btnEditItem.setEnabled(false);
							btnDelete.setEnabled(false);
						}
					}
				});

				scrollPane.setViewportView(table);
			}
		}
		{
			JLabel lblDepartmentStock = new JLabel("Item Browser");
			lblDepartmentStock.setFont(new Font("Tahoma", Font.BOLD, 17));
			lblDepartmentStock.setBounds(59, 0, 310, 34);
			contentPanel.add(lblDepartmentStock);
		}
		{
			JLabel label = new JLabel("");
			label.setIcon(new ImageIcon(ItemBrowser.class
					.getResource("/icons/restore.gif")));
			label.setBounds(10, 0, 52, 34);
			contentPanel.add(label);
		}

		btnNewItem = new JButton("New Item");
		btnNewItem.setIcon(new ImageIcon(ItemBrowser.class
				.getResource("/icons/plus_button.png")));
		btnNewItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewItem newItem = new NewItem();
				newItem.setModal(true);
				newItem.setVisible(true);

			}
		});
		btnNewItem.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewItem.setBounds(10, 448, 155, 44);
		contentPanel.add(btnNewItem);

		btnEditItem = new JButton("Edit Item");
		btnEditItem.setIcon(new ImageIcon(ItemBrowser.class
				.getResource("/icons/edit_button.png")));
		btnEditItem.setEnabled(false);
		btnEditItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				Object selectedObject1 = table.getModel().getValueAt(
						selectedRowIndex, 0);
				System.out.print("" + selectedObject1);
				String id = selectedObject1.toString();
				EditItem editItem = new EditItem(id);
				editItem.setModal(true);
				editItem.setVisible(true);
				btnEditItem.setEnabled(false);
				btnDelete.setEnabled(false);
			}
		});
		btnEditItem.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnEditItem.setBounds(197, 448, 155, 46);
		contentPanel.add(btnEditItem);

		btnCancel = new JButton("Cancel");
		btnCancel.setIcon(new ImageIcon(ItemBrowser.class
				.getResource("/icons/CANCEL.PNG")));
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCancel.setBounds(890, 449, 155, 44);
		contentPanel.add(btnCancel);

		btnDelete = new JButton("Delete");

		btnDelete.setEnabled(false);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(
						ItemBrowser.this, "Are you sure to delete item",
						"Cancel", dialogButton);
				if (dialogResult == 0) {
					Object selectedObject1 = table.getModel().getValueAt(
							selectedRowIndex, 0);
					System.out.print("" + selectedObject1);
					String id = selectedObject1.toString();
					ItemsDBConnection connection = new ItemsDBConnection();
					try {
						connection.deleteRow(id);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					connection.closeConnection();

					populateExpensesTable();

				}
				btnDelete.setEnabled(false);
				btnEditItem.setEnabled(false);
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnDelete.setBounds(362, 448, 155, 44);
		contentPanel.add(btnDelete);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				populateExpensesTable();
				btnEditItem.setEnabled(false);
				btnDelete.setEnabled(false);
			}
		});
		btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRefresh.setBounds(706, 448, 155, 44);
		contentPanel.add(btnRefresh);

		searchItemTF = new JTextField();
		searchItemTF.setColumns(10);
		searchItemTF.setBounds(785, 11, 260, 20);
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

		JLabel label = new JLabel("Search Item");
		label.setBounds(708, 13, 85, 20);
		contentPanel.add(label);

		 btnViewBatch = new JButton("View Batch");
		btnViewBatch.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnViewBatch.setBounds(527, 448, 155, 44);
		contentPanel.add(btnViewBatch);
		btnViewBatch.setEnabled(false);
		btnViewBatch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				Object selectedObject1 = table.getModel().getValueAt(
						selectedRowIndex, 0);
				Object selectedObject2 = table.getModel().getValueAt(
						selectedRowIndex, 1);
				System.out.print("" + selectedObject1);
				String id = selectedObject1.toString();
				String name = selectedObject2.toString();
				BatchBrowser editItem = new BatchBrowser(id,name);
				editItem.setModal(true);
				editItem.setVisible(true);
				btnEditItem.setEnabled(false);
				btnDelete.setEnabled(false);
			}
		});
		populateExpensesTable();
	}

	public void searchTableContents(String searchString) {
		DefaultTableModel currtableModel = (DefaultTableModel) table.getModel();
		// To empty the table before search
		currtableModel.setRowCount(0);
		// To search for contents from original table content
		for (Object rows : originalTableModel) {
			Vector rowVector = (Vector) rows;
			for (Object column : rowVector) {
				if (column.toString().toLowerCase()
						.contains(searchString.toLowerCase())) {
					// content found so adding to table
					currtableModel.addRow(rowVector);
					break;
				}
			}
		}
	}

	public void populateExpensesTable() {

		try {
			ItemsDBConnection db = new ItemsDBConnection();
			ResultSet rs = db.retrievetAllItemsNew();

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = db.retrievetAllItemsNew();

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {

				for (int C = 1; C <= NumberOfColumns; C++) {
				
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
//					if(Rows_Object_Array[R][C - 1].equals("null")){
//						Rows_Object_Array[R][C - 1]=""; 
//					}
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "Item Id", "Item Name", "Doctor Name",
							"Item Brand.", "Vendor Name1","Vendor Name2", "Total Stock",
							"Active", "Pack Size","Item Type" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table.setModel(model);
			originalTableModel = (Vector) ((DefaultTableModel) table.getModel())
					.getDataVector().clone();

			table.getColumnModel().getColumn(1).setPreferredWidth(180);
			table.getColumnModel().getColumn(1).setMinWidth(150);
			table.getColumnModel().getColumn(2).setPreferredWidth(180);
			table.getColumnModel().getColumn(2).setMinWidth(150);
			table.getColumnModel().getColumn(3).setMinWidth(150);
			table.getColumnModel().getColumn(4).setPreferredWidth(150);
			table.getColumnModel().getColumn(4).setMinWidth(100);
			table.getColumnModel().getColumn(5).setPreferredWidth(80);
			table.getColumnModel().getColumn(5).setMinWidth(80);
			table.getColumnModel().getColumn(6).setPreferredWidth(80);
			table.getColumnModel().getColumn(6).setMinWidth(80);
			table.getColumnModel().getColumn(7).setPreferredWidth(100);
			table.getColumnModel().getColumn(7).setMinWidth(100);
			table.setFont(new Font("Tahoma", Font.BOLD, 12));
			table.getColumnModel().getColumn(6)
					.setCellRenderer(new CustomRenderer());
			table.getColumnModel().getColumn(8)
			.setCellRenderer(new CustomRenderer1());
		} catch (SQLException ex) {

		}

	}

	public class CustomRenderer1 extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component cellComponent = super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);

			if (table.getValueAt(row, column).equals("High Risk")) {
				cellComponent.setBackground(Color.RED);
			} else if(table.getValueAt(row, column).equals("SHC-H1")){
				cellComponent.setBackground(Color.ORANGE);
			}else{
				cellComponent.setBackground(Color.WHITE);
			}
			return cellComponent;
		}
	}
	public class CustomRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component cellComponent = super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);

			if (table.getValueAt(row, column).equals("Yes")) {
				cellComponent.setBackground(Color.GREEN);
			} else {
				cellComponent.setBackground(Color.RED);

			}
			return cellComponent;
		}
	}

	public JButton getBtnDelete() {
		return btnDelete;
	}

	public JTextField getSearchItemTF() {
		return searchItemTF;
	}
}
