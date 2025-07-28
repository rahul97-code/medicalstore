package hms.store.gui;

import java.awt.Component;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.toedter.calendar.JDateChooser;

class JDateChooserRenderer extends JDateChooser implements TableCellRenderer {
    public JDateChooserRenderer(JDateChooser dateChooser) {
        if (dateChooser != null) {
            this.setDate(dateChooser.getDate());
        }
    }
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1,
			boolean arg2, boolean arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
	      if (arg1 instanceof Date) {
	            this.setDate((Date) arg1);
	        } else if (arg1 instanceof String) {
	        }
	        return this;
	
	}
}