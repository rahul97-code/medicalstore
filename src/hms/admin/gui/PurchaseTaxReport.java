package hms.admin.gui;

import hms.report.excel.FinalSalesTaxReportExcel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

import com.itextpdf.text.DocumentException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class PurchaseTaxReport extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	String filePath="";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PurchaseTaxReport dialog = new PurchaseTaxReport();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PurchaseTaxReport() {
		setBounds(100, 100, 498, 252);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblPurchaseTaxReport = new JLabel("Purchase Tax Report");
		lblPurchaseTaxReport.setHorizontalAlignment(SwingConstants.CENTER);
		lblPurchaseTaxReport.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblPurchaseTaxReport.setBounds(10, 11, 462, 42);
		contentPanel.add(lblPurchaseTaxReport);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(10, 99, 314, 27);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File desktop = new File(System.getProperty("user.home"), "Desktop");
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(desktop);
				int result = fileChooser.showOpenDialog(PurchaseTaxReport.this);
				if (result == JFileChooser.APPROVE_OPTION) {
				    File selectedFile = fileChooser.getSelectedFile();
				    textField.setText(selectedFile.getAbsolutePath());
				    filePath=selectedFile.getAbsolutePath();
				    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
				}
			}
		});
		btnBrowse.setBounds(336, 99, 136, 27);
		contentPanel.add(btnBrowse);
		
		JLabel lblBrowsePuchaseReport = new JLabel("Browse Puchase Report");
		lblBrowsePuchaseReport.setBounds(10, 76, 314, 14);
		contentPanel.add(lblBrowsePuchaseReport);
		
		JButton btnNewButton = new JButton("Get Report");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(filePath.equals(""))
				{
					JOptionPane
					.showMessageDialog(
							null,
							"Please Select File Path First",
							"File Path Error", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					try {
						new FinalSalesTaxReportExcel(filePath);
					} catch (DocumentException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton.setBounds(34, 146, 176, 42);
		contentPanel.add(btnNewButton);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(235, 144, 162, 44);
		contentPanel.add(btnCancel);
	}
}
