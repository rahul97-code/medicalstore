package hms.store.gui;

import hms.main.DateFormatChange;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

import com.toedter.calendar.JDateChooser;

class JDateChooserCellEditor extends AbstractCellEditor implements
TableCellEditor {
	String dateedit;
	public JDateChooserCellEditor(JCheckBox checkBox)
	  {
	    super();

	  }

	  JDateChooser date;
	  public Component getTableCellEditorComponent(JTable table, Object value,
	      boolean isSelected, int row, int column) 
	  {

	         date = new JDateChooser();
//	         date.setDateFormatString("dd-MM-yyyy");
	         
	         
	         date.setMinSelectableDate(new Date());
//			expiryDateC.setDateFormatString("yyyy-MM-dd");
	         date.setDateFormatString("yyyy-MM-dd");
	         return date;
	  }

	  public Object getCellEditorValue() 
	  {
	    return new String(((JTextField)date.getDateEditor().getUiComponent()).getText());
	  }

	  public boolean stopCellEditing()
	  {
	    return super.stopCellEditing();
	  }

	  protected void fireEditingStopped() {
	    super.fireEditingStopped();
	  }
}