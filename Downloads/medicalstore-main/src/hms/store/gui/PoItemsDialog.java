package hms.store.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import hms.store.database.PODBConnection;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PoItemsDialog extends JDialog {
	JTable table;

	public PoItemsDialog(final EditInvoice owner, String po_id) {
		// Call the parent JDialog constructor
		super(owner, "PO Items", true);

		// Set size and location of the dialog
		setSize(400, 358);
		setLocationRelativeTo(owner); // Center the dialog relative to the parent frame

		// Create a table with 3 columns and some sample data
		String[] columns = {"ID", "Name", "Price"};
		Object[][] data = {
				{1, "Item A", 100.0},
				{2, "Item B", 150.0},
				{3, "Item C", 200.0}
		};

		// Create table model and set it on JTable
		DefaultTableModel model = new DefaultTableModel(data, columns);
		table = new JTable(model);

		// Add mouse listener to detect double-clicks


		// Add the table inside a JScrollPane so it can scroll
		JScrollPane scrollPane = new JScrollPane(table);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		// Add button at the bottom
		JButton button = new JButton("Process Items");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Object[]> selectedItems = new ArrayList<>();

				// Iterate through each row to check if column 4 (Select) is true
				for (int row = 0; row < table.getRowCount(); row++) {
					boolean isSelected = (Boolean) table.getValueAt(row, 3); // Check "Select" column (column 4)
					if (isSelected) {
						// If selected, get the values from column 1 (Item ID) and column 3 (Remaining Quantity)
						Object itemID = table.getValueAt(row, 0); // Item ID
						Object remainingQty = table.getValueAt(row, 2); // Remaining Quantity
						// Add the selected values as an array to the list
						selectedItems.add(new Object[]{itemID, remainingQty});
					}
				}
				if(selectedItems.size()>0)
					owner.addRemainingPoItems(selectedItems);
			}
		});


		// Add button to the SOUTH of the dialog
		getContentPane().add(button, BorderLayout.SOUTH);

		// Populate the table with data from the database
		populateTable(po_id);
	}

	//    // Main method to show the dialog
	//    public static void main(String[] args) {
	//        // SwingUtilities.invokeLater to ensure the Swing components are created in the Event Dispatch Thread
	//        SwingUtilities.invokeLater(new Runnable() {
	//            @Override
	//            public void run() {
	//                // Create and show the dialog
	//                JFrame ownerFrame = new JFrame();
	//                ownerFrame.setSize(300, 200); // Set size for the main window
	//                ownerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//                ownerFrame.setVisible(true);
	//
	//                // Create the dialog and show it
	//                PoItemsDialog dialog = new PoItemsDialog(ownerFrame, "10875");
	//                dialog.setVisible(true);
	//            }
	//        });
	//    }

	// Method to populate the table with data from the database
	public void populateTable(String po_id) {
		try {
			PODBConnection db = new PODBConnection();
			ResultSet rs = db.retrieveRamainingPOItems(po_id);

			// Get column count and row count from the ResultSet
			int numberOfColumns = rs.getMetaData().getColumnCount();
			rs.last();
			int numberOfRows = rs.getRow();
			rs.beforeFirst();

			// Create a 2D array to store data
			Object[][] rowsObjectArray = new Object[numberOfRows][numberOfColumns + 1];
			int rowIndex = 0;
			while (rs.next()) {
				rowsObjectArray[rowIndex][0] = rs.getObject(1); // Item ID
				rowsObjectArray[rowIndex][1] = rs.getObject(2); // Item Name
				rowsObjectArray[rowIndex][2] = rs.getObject(3); // Remaining Quantity
				rowsObjectArray[rowIndex][3] = new Boolean(false); // Default "select" checkbox value
				rowIndex++;
			}
			db.closeConnection();

			// Set the table model with the data retrieved from the database
			DefaultTableModel model = new DefaultTableModel(rowsObjectArray, 
					new String[] {"Item ID", "Item Name", "Po Qty", "Select"}) {

				// Make the "Select" column editable (checkbox)
				@Override
				public boolean isCellEditable(int row, int column) {
					return column == 3; // Only the "Select" column is editable
				}

				// Ensure the correct column types
				@Override
				public Class<?> getColumnClass(int column) {
					return getValueAt(0, column).getClass();
				}
			};
			table.setModel(model);

		} catch (SQLException ex) {
			Logger.getLogger(PoItemsDialog.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
