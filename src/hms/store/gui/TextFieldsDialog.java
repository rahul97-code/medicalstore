package hms.store.gui;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.event.ActionEvent;

public class TextFieldsDialog extends JDialog {
	public JTextField unitPriceTF;
	public JTextField mrpTF;
	public JTextField pckTF;
	public JTextField cgstTF;
	public JTextField sgstTF;
	public JTextField igstTF;
	public boolean final_flag=false;
	List<Boolean> flag = new ArrayList<>(Collections.nCopies(6, true));	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new TextFieldsDialog(null,null).setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public TextFieldsDialog(List<Integer> changedIndexlist, final List<Double> list2) {
		setBounds(100, 100, 588, 121);
		setTitle("Price Verification");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null); 

		JPanel panel = new JPanel();
		panel.setBounds(4, 9, 584, 70);
		getContentPane().add(panel);
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(new LineBorder(new Color(51, 102, 153)),"Item Price Details", TitledBorder.LEFT, TitledBorder.BOTTOM,new Font("Tahoma", Font.ITALIC, 10), 
				new Color(51, 102, 153)));

		unitPriceTF = new JTextField();
		unitPriceTF.setBounds(13, 33, 68, 19);
		panel.add(unitPriceTF);
		unitPriceTF.setColumns(10);
		unitPriceTF.setEnabled(false);
		if(changedIndexlist.contains(0)) {
			unitPriceTF.setEnabled(true);
			flag.set(0, false);
		}

		unitPriceTF.setText(list2.get(0)+"");  
		unitPriceTF.setForeground(Color.GRAY);  
		unitPriceTF.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (unitPriceTF.getText().equals(list2.get(0)+"")) {
					unitPriceTF.setText("");  
					unitPriceTF.setForeground(Color.BLACK);  
					flag.set(0, true);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (unitPriceTF.getText().isEmpty()) {
					unitPriceTF.setText(list2.get(0)+"");  
					unitPriceTF.setForeground(Color.GRAY); 
					flag.set(0, false);
				}
			}
		});


		mrpTF = new JTextField();
		mrpTF.setColumns(10);
		mrpTF.setBounds(93, 33, 68, 19);
		panel.add(mrpTF);
		mrpTF.setEnabled(false);
		if(changedIndexlist.contains(1)) {
			mrpTF.setEnabled(true);
			flag.set(1, false);
		}

		mrpTF.setText(list2.get(1)+"");  
		mrpTF.setForeground(Color.GRAY);  
		mrpTF.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (mrpTF.getText().equals(list2.get(1)+"")) {
					mrpTF.setText("");  
					mrpTF.setForeground(Color.BLACK);  
					flag.set(1, true);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (mrpTF.getText().isEmpty()) {
					mrpTF.setText(list2.get(1)+"");  
					mrpTF.setForeground(Color.GRAY); 
					flag.set(1, false);
				}
			}
		});

		pckTF = new JTextField();
		pckTF.setColumns(10);
		pckTF.setBounds(173, 33, 68, 19);
		panel.add(pckTF);
		pckTF.setEnabled(false);
		if(changedIndexlist.contains(2)) {
			pckTF.setEnabled(true);
			flag.set(2, false);
		}

		pckTF.setText(list2.get(2)+"");  
		pckTF.setForeground(Color.GRAY);  
		pckTF.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (pckTF.getText().equals(list2.get(2)+"")) {
					pckTF.setText("");  
					pckTF.setForeground(Color.BLACK);  
					flag.set(2, true);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (pckTF.getText().isEmpty()) {
					pckTF.setText(list2.get(2)+"");  
					pckTF.setForeground(Color.GRAY); 
					flag.set(2, false);
				}
			}
		});


		cgstTF = new JTextField();
		cgstTF.setColumns(10);
		cgstTF.setBounds(253, 33, 68, 19);
		panel.add(cgstTF);
		cgstTF.setEnabled(false);
		if(changedIndexlist.contains(4)) {
			cgstTF.setEnabled(true);
			flag.set(4, false);
		}

		cgstTF.setText(list2.get(4)+"");  
		cgstTF.setForeground(Color.GRAY);  
		cgstTF.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (cgstTF.getText().equals(list2.get(4)+"")) {
					cgstTF.setText("");  
					cgstTF.setForeground(Color.BLACK);  
					flag.set(4, true);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (cgstTF.getText().isEmpty()) {
					cgstTF.setText(list2.get(4)+"");  
					cgstTF.setForeground(Color.GRAY); 
					flag.set(4, false);
				}
			}
		});


		sgstTF = new JTextField();
		sgstTF.setColumns(10);
		sgstTF.setBounds(333, 33, 68, 19);
		panel.add(sgstTF);
		sgstTF.setEnabled(false);
		if(changedIndexlist.contains(3)) {
			sgstTF.setEnabled(true);
			flag.set(3, false);
		}

		sgstTF.setText(list2.get(3)+"");  
		sgstTF.setForeground(Color.GRAY);  
		sgstTF.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (sgstTF.getText().equals(list2.get(3)+"")) {
					sgstTF.setText("");  
					sgstTF.setForeground(Color.BLACK);  
					flag.set(3, true);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (sgstTF.getText().isEmpty()) {
					sgstTF.setText(list2.get(3)+"");  
					sgstTF.setForeground(Color.GRAY); 
					flag.set(3, false);
				}
			}
		});


		igstTF = new JTextField();
		igstTF.setColumns(10);
		igstTF.setBounds(413, 33, 68, 19);
		panel.add(igstTF);
		igstTF.setEnabled(false);
		if(changedIndexlist.contains(5)) {
			igstTF.setEnabled(true);
			flag.set(5, false);
		}

		igstTF.setText(list2.get(5)+"");  
		igstTF.setForeground(Color.GRAY);  
		igstTF.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (igstTF.getText().equals(list2.get(5)+"")) {
					igstTF.setText("");  
					igstTF.setForeground(Color.BLACK);  
					flag.set(5, true);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (igstTF.getText().isEmpty()) {
					igstTF.setText(list2.get(5)+"");  
					igstTF.setForeground(Color.GRAY); 
					flag.set(5, false);
				}
			}
		});


		JButton btnOk = new JButton("OK");
		btnOk.setFont(new Font("Dialog", Font.ITALIC, 13));
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!flag.contains(false)) {
					final_flag=true;
					dispose();
				}else {		
					JOptionPane.showMessageDialog(null, "Please confirm All fields.",
						"Input Error", JOptionPane.ERROR_MESSAGE);
				     return;
				}
			}
		});
		btnOk.setBounds(493, 32, 63, 20);
		panel.add(btnOk);

		JLabel lblUp_1 = new JLabel("MRP");
		lblUp_1.setFont(new Font("Dialog", Font.ITALIC, 11));
		lblUp_1.setBounds(113, 18, 70, 15);
		panel.add(lblUp_1);

		JLabel lblUp_2 = new JLabel("PACK");
		lblUp_2.setFont(new Font("Dialog", Font.ITALIC, 11));
		lblUp_2.setBounds(189, 18, 70, 15);
		panel.add(lblUp_2);

		JLabel lblUp_3 = new JLabel("CGST");
		lblUp_3.setFont(new Font("Dialog", Font.ITALIC, 11));
		lblUp_3.setBounds(271, 18, 70, 15);
		panel.add(lblUp_3);

		JLabel lblUp_4 = new JLabel("SGST");
		lblUp_4.setFont(new Font("Dialog", Font.ITALIC, 11));
		lblUp_4.setBounds(353, 17, 70, 15);
		panel.add(lblUp_4);

		JLabel lblUp_5 = new JLabel("IGST");
		lblUp_5.setFont(new Font("Dialog", Font.ITALIC, 11));
		lblUp_5.setBounds(435, 18, 70, 15);
		panel.add(lblUp_5);

		final JLabel lblUp = new JLabel("U.P.");
		lblUp.setFont(new Font("Dialog", Font.ITALIC, 11));
		lblUp.setBounds(30, 18, 70, 15);
		panel.add(lblUp);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				// Focus the label or any non-focusable component
				lblUp.requestFocusInWindow();
			}
		});

	}
	
	 public JTextField getUnitPriceTF() {
	        return unitPriceTF;
	    }

	    // Getter for mrpTF
	    public JTextField getMrpTF() {
	        return mrpTF;
	    }

	    // Getter for pckTF
	    public JTextField getPckTF() {
	        return pckTF;
	    }

	    // Getter for cgstTF
	    public JTextField getCgstTF() {
	        return cgstTF;
	    }

	    // Getter for sgstTF
	    public JTextField getSgstTF() {
	        return sgstTF;
	    }

	    // Getter for igstTF
	    public JTextField getIgstTF() {
	        return igstTF;
	    }
	    
	    public boolean getFinalStatus() {
	        return final_flag;
	    }


}
