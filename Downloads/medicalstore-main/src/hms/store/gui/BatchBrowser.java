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

public class BatchBrowser extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	int selectedRowIndex;
	private JTextField searchItemTF;
	Vector originalTableModel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			BatchBrowser dialog = new BatchBrowser("","");
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Create the dialog.
	 */
	public BatchBrowser(String itemid,String itemName) {
		setTitle("Batch Detail");
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
			scrollPane.setBounds(10, 39, 1035, 444);
			contentPanel.add(scrollPane);
			{
				table = new JTable();
				table.getTableHeader().setReorderingAllowed(false);
				table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setModel(new DefaultTableModel(new Object[][] { { null,
						null, null, null, null }, },
						new String[] { "Batch Name", "Qty Entered", "Qty Remaining",
								"Entry Date", "MRP","Pack Size","Purchase Price"  }));
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
							EditItem editItem=new EditItem(id);
							editItem.setModal(true);
							editItem.setVisible(true);
							
						}
					}
				});
				
				scrollPane.setViewportView(table);
			}
		}
		{
			JLabel lblDepartmentStock = new JLabel("Item Id:");
			lblDepartmentStock.setFont(new Font("Tahoma", Font.BOLD, 17));
			lblDepartmentStock.setBounds(59, 0, 115, 34);
			contentPanel.add(lblDepartmentStock);
		}
		{
			JLabel label = new JLabel("");
			label.setIcon(new ImageIcon(ItemBrowser.class.getResource("/icons/restore.gif")));
			label.setBounds(10, 0, 52, 34);
			contentPanel.add(label);
		}
		
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
		
		JLabel Item_id_value = new JLabel("");
		Item_id_value.setFont(new Font("Tahoma", Font.BOLD, 17));
		Item_id_value.setBounds(143, 0, 115, 34);
		contentPanel.add(Item_id_value);
		Item_id_value.setText(itemid);
		
		JLabel lblItemName = new JLabel("Item Name:");
		lblItemName.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblItemName.setBounds(278, 0, 115, 34);
		contentPanel.add(lblItemName);
		
		JLabel Item_name_value = new JLabel("<dynamic>");
		Item_name_value.setFont(new Font("Tahoma", Font.BOLD, 17));
		Item_name_value.setBounds(394, 0, 212, 34);
		contentPanel.add(Item_name_value);
		Item_name_value.setText(itemName);
		
	
		populateExpensesTable(itemid);
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
	public void populateExpensesTable(String item_id) {

		try {
			ItemsDBConnection db = new ItemsDBConnection();
			ResultSet rs = db.retrievetAllBatchesNew(item_id);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();
			rs.last();
			NumberOfRows=rs.getRow();
			rs.beforeFirst();
		

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns+1];

			int R = 0;
			while (rs.next()) {

				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "Batch Name","Expiry", "Qty Entered", "Qty Remaining","Entry Date",
							 "MRP","Pack Size","On Bill Purchase Price","Discounted Purchase Price","CGST","SGST","IGST" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table.setModel(model);
			originalTableModel = (Vector) ((DefaultTableModel) table.getModel())
					.getDataVector().clone();
		
			table.getColumnModel().getColumn(1).setPreferredWidth(180);
			table.getColumnModel().getColumn(1).setMinWidth(180);
			table.getColumnModel().getColumn(2).setPreferredWidth(150);
			table.getColumnModel().getColumn(2).setMinWidth(150);
			table.getColumnModel().getColumn(3).setMinWidth(100);
			table.getColumnModel().getColumn(4).setPreferredWidth(100);
			table.getColumnModel().getColumn(4).setMinWidth(100);
			table.getColumnModel().getColumn(5).setPreferredWidth(100);
			table.getColumnModel().getColumn(5).setMinWidth(100);
			table.getColumnModel().getColumn(6).setPreferredWidth(150);
			table.getColumnModel().getColumn(6).setMinWidth(150);
			table.getColumnModel().getColumn(7).setPreferredWidth(150);
			table.getColumnModel().getColumn(7).setMinWidth(150);
//			table.getColumnModel().getColumn(8).setPreferredWidth(150);
//			table.getColumnModel().getColumn(8).setMinWidth(150);
			table.setFont(new Font("Tahoma", Font.BOLD, 12));
//			table.getColumnModel().getColumn(6).setCellRenderer(new CustomRenderer());
		} catch (SQLException ex) {
			
		}

	}
	public class CustomRenderer extends DefaultTableCellRenderer 
	  {
	      @Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	      {
	          Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	          if(table.getValueAt(row, column).equals("Yes")){
	              cellComponent.setBackground(Color.GREEN);
	          } else{
	        	  cellComponent.setBackground(Color.RED);
	        	 
	          }
	          return cellComponent;
	      }
	  }

	public JTextField getSearchItemTF() {
		return searchItemTF;
	}
}
