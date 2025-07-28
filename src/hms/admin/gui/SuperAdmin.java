package hms.admin.gui;

import hms.store.gui.MyDemo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SuperAdmin extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SuperAdmin dialog = new SuperAdmin();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SuperAdmin() {
		setTitle("Super Admin");
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 450, 284);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.textHighlight);
		panel.setBounds(10, 32, 147, 203);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		JButton btnSupplier = new JButton("Supplier");
		btnSupplier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MyDemo("SUPPLIER");
			}
		});
		btnSupplier.setBounds(10, 29, 127, 23);
		panel.add(btnSupplier);
		
		JButton btnInsurance = new JButton("Insurance");
		btnInsurance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MyDemo("INSURANCE");
			}
		});
		btnInsurance.setBounds(10, 63, 127, 23);
		panel.add(btnInsurance);
		
		JLabel lblGlPayments = new JLabel("GL Payments");
		lblGlPayments.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblGlPayments.setHorizontalAlignment(SwingConstants.CENTER);
		lblGlPayments.setBounds(10, 11, 107, 14);
		contentPanel.add(lblGlPayments);
	}
}
