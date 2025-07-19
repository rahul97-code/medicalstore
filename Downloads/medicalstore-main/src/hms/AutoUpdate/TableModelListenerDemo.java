package hms.AutoUpdate;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class TableModelListenerDemo {
  public static void main(String args[]) {

    final Object rowData[][] = { { "1", "one", "I" }, { "2", "two", "II" }, { "3", "three", "III" } };
    final String columnNames[] = { "#", "English", "Roman" };

    final JTable table = new JTable(rowData, columnNames);
    JScrollPane scrollPane = new JScrollPane(table);
    table.getModel().addTableModelListener(new TableModelListener() {

      public void tableChanged(TableModelEvent e) {
    	  int firstRow = e.getFirstRow();
    	    int lastRow = e.getLastRow();
    	    int index = e.getColumn();

    	    switch (e.getType()) {
    	    case TableModelEvent.INSERT:
    	      for (int i = firstRow; i <= lastRow; i++) {
    	    	  String atr=table.getValueAt(lastRow, i).toString();
    	    	  System.out.print(atr);
    	      }
    	      break;
    	    case TableModelEvent.UPDATE:
    	      if (firstRow == TableModelEvent.HEADER_ROW) {
    	        if (index == TableModelEvent.ALL_COLUMNS) {
    	          System.out.println("A column was added");
    	        } else {
    	          System.out.println(index + "in header changed");
    	        }
    	      } else {
    	        for (int i = firstRow; i <= lastRow; i++) {
    	          if (index == TableModelEvent.ALL_COLUMNS) {
    	            System.out.println("All columns have changed");
    	          } else {
    	        	  String atr=table.getValueAt(lastRow, i).toString();
        	    	  System.out.print(atr);
    	          }
    	        }
    	      }
    	      break;
    	    case TableModelEvent.DELETE:
    	      for (int i = firstRow; i <= lastRow; i++) {
    	        System.out.println(i);
    	      }
    	      break;
    	    }
      }
    });

    table.setValueAt("",0,0);
    JFrame frame = new JFrame("Resizing Table");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.add(scrollPane, BorderLayout.CENTER);

    frame.setSize(300, 150);
    frame.setVisible(true);

  }
}