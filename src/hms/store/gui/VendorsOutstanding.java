package hms.store.gui;

import hms.gl.database.GLAccountDBConnection;
import hms.store.database.BillingDBConnection;
import hms.store.database.SuppliersDBConnection;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.DocumentException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class VendorsOutstanding extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			VendorsOutstanding dialog = new VendorsOutstanding();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public VendorsOutstanding() {
		setTitle("Vendors Out Standing Report");
		setBounds(100, 100, 556, 463);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 520, 370);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] { { null, null,
				null }, },
				new String[] { "Vendor ID", "Vendor Name", "Balance" }));
		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		scrollPane.setViewportView(table);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);

			JButton btnExcelExport = new JButton("Excel Export");
			btnExcelExport.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					try {
						new ExcelFile("OutStandin Detail", table);
					} catch (DocumentException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			btnExcelExport.setActionCommand("OK");
			buttonPane.add(btnExcelExport);
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		loadData() ;
	}

	public void loadData() {
		int rows = 0;
		try {
			rows = lastSupplier();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Object Rows_Object_Array[][];
		Rows_Object_Array = new Object[rows][3];
		int R = 0;
		for (int i = 0; i < rows; i++) {
			try {
				GLAccountDBConnection db = new GLAccountDBConnection();
				ResultSet rs = db.retrieveAllOutstanding(i + "");

				// to set rows in this array

				while (rs.next()) {
					for (int C = 1; C <= 3; C++) {
						Rows_Object_Array[R][C - 1] = rs.getObject(C);
					}
					R++;
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		// Finally load data to the table
		DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
				new String[] { "Vendor ID", "Vendor Name", "Balance" }) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;// This causes all cells to be not editable
			}
		};
		
		table.setModel(model);
		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setMinWidth(150);

	}

	public int lastSupplier() throws SQLException {
		int id = 0;
		SuppliersDBConnection db = new SuppliersDBConnection();
		ResultSet rs = db.lastSupplier();
		while (rs.next()) {

			id = Integer.parseInt(rs.getObject(1).toString());

		}
		db.closeConnection();
		return id;
	}
}
