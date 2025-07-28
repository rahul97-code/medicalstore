package hms.store.gui;

import hms.store.database.InvoiceDBConnection;
import hms.store.gui.SupplierPayments.EditableTableModel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class OrderPayments extends JDialog {

	private final JPanel contentPanel = new JPanel();
 String index="";
 private JTable table;
	/**
	 * Create the dialog.
	 */
	public OrderPayments(String id) {
		setBounds(100, 100, 669, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		index=id;
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 11, 633, 208);
		contentPanel.add(scrollPane_1);
		
		table = new JTable();
		table.setToolTipText("Double Click to  Bills");
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		scrollPane_1.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
					"id", "invoice_No", "order_no", "supplier_id", "supplier_name", "date", "time", "entry_user", "total_amount"
			}
		));
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		populateExpensesTable();
	}
	public void populateExpensesTable() {

		try {
			InvoiceDBConnection db = new InvoiceDBConnection();
			ResultSet rs = db.retrieveWithID(index);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = db.retrieveWithID(index);
		
			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];
		
			int R = 0;
			while (rs.next()) {
				Rows_Object_Array[R][0] = rs.getObject(1).toString();
				Rows_Object_Array[R][1] = rs.getObject(2).toString();
				Rows_Object_Array[R][2] = rs.getObject(3).toString();
				Rows_Object_Array[R][3] = rs.getObject(4).toString();
				Rows_Object_Array[R][4] = rs.getObject(5).toString();
				Rows_Object_Array[R][5] = rs.getObject(6).toString();
				Rows_Object_Array[R][6] = rs.getObject(7).toString();
				Rows_Object_Array[R][7] = rs.getObject(8).toString();
				Rows_Object_Array[R][8] = rs.getObject(9).toString();
				R++;
			}
			  DefaultTableModel model = new DefaultTableModel( Rows_Object_Array,new String[] {
					"id", "invoice_No", "order_no", "supplier_id", "supplier_name", "date", "time", "entry_user", "total_amount" });

			  table.setModel(model);
		} catch (SQLException ex) {

			System.out.println(ex);
		}
	}
}
