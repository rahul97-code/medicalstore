package hms.gl.account;

import hms.admin.gui.AdminMain;
//import hms.accounts.database.AccountsUserDBConnection;
import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.opd.database.OPDDBConnection;
import hms.patient.slippdf.NewSummaryReport;
import hms.patient.slippdf.NewSummaryReportPDF;
import hms.patient.slippdf.NewSummaryReportUserwisePDFdata;
import hms.store.database.PurchaseOrderDBConnection;
import hms.store.database.StoreAccountDBConnection;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;

@SuppressWarnings("serial")
public class ReceptionUsersReport extends JDialog {

	private JPanel contentPane;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom, dateTo;
	private JComboBox UsersCB;
	final DefaultComboBoxModel AllUserV = new DefaultComboBoxModel();

	public static void main(String arg[])
	{
		new ReceptionUsersReport().setVisible(true);
	}
	/**
	 * Create the frame.
	 */
	public ReceptionUsersReport() {
		setResizable(false);
		setTitle("Users Report");
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				ReceptionUsersReport.class.getResource("/icons/rotaryLogo.png")));
		setBounds(400, 150, 447, 328);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 418, 277);
		contentPane.add(panel);
		panel.setLayout(null);
		UsersCB = new JComboBox<Object>();
		UsersCB.setBounds(69, 75, 270, 28);
		panel.add(UsersCB);
		UsersCB.setModel(new DefaultComboBoxModel<Object>(new String[] {
				"Summary Report", "Doctor Report","Cancelled Receipt Report","Bed Occupancy Report","Exams Report","IPD Discharge Patient","IPD Pending List" }));
		UsersCB.setFont(new Font("Tahoma", Font.BOLD, 12));

		dateFromDC = new JDateChooser();
		dateFromDC.setBounds(161, 129, 178, 25);
		panel.add(dateFromDC);
		dateFromDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateFrom = DateFormatChange.StringToMysqlDate((Date) arg0
									.getNewValue());
						}
					}
				});
		dateFromDC.setDate(new Date());
		dateFromDC.setMaxSelectableDate(new Date());
		dateFromDC.setDateFormatString("yyyy-MM-dd");

		dateToDC = new JDateChooser();
		dateToDC.setBounds(161, 165, 178, 25);
		panel.add(dateToDC);
		dateToDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateTo = DateFormatChange.StringToMysqlDate((Date) arg0
									.getNewValue());
						}
					}
				});
		dateToDC.setDate(new Date());
		dateToDC.setMaxSelectableDate(new Date());
		dateToDC.setDateFormatString("yyyy-MM-dd");

		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setBounds(69, 165, 73, 25);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(72, 129, 82, 25);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnNewButton_2 = new JButton("PDF");
		btnNewButton_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String username = UsersCB.getSelectedItem().toString();
				
				try {
					//new AllUsersCashReportPDF(dateFrom, dateTo,username);
					new NewSummaryReport(dateFrom, dateTo,username,AdminMain.id);
					//new NewSummaryReportPDF(dateFrom, dateTo);
				} catch (DocumentException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnNewButton_2.setBounds(10, 212, 99, 35);
		panel.add(btnNewButton_2);
		btnNewButton_2.setIcon(null);
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setIcon(new ImageIcon(ReceptionUsersReport.class
				.getResource("/icons/opd.gif")));
		lblNewLabel.setBounds(35, 324, 111, 83);
		panel.add(lblNewLabel);
		
		JLabel lblHms = new JLabel(" MedicalStore Report");
		lblHms.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblHms.setHorizontalAlignment(SwingConstants.CENTER);
		lblHms.setBounds(69, 22, 270, 34);
		panel.add(lblHms);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setIcon(new ImageIcon(ReceptionUsersReport.class.getResource("/icons/CANCEL.PNG")));
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancel.setBounds(292, 212, 116, 35);
		panel.add(btnCancel);
		
		JButton btnExcel = new JButton("ALL Users Collection");
		btnExcel.setEnabled(true);
//		if(AdminMain.is_admin.equals("1"))
//		{
//			btnExcel.setEnabled(true);
//		}
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String username = UsersCB.getSelectedItem().toString();
				
				try {
					//new AllUsersCashSummeryPDF(dateFrom, dateTo);
					new NewSummaryReport(dateFrom, dateTo,"All",AdminMain.id);
				} catch (DocumentException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnExcel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExcel.setBounds(119, 212, 163, 35);
		panel.add(btnExcel);
		getAllUsers();
	}
	public void getAllUsers() {
		StoreAccountDBConnection dbConnection = new StoreAccountDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllReceptionName1();
		try {
			while (resultSet.next()) {
				AllUserV.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//dbConnection.closeConnection();
		//PurchaseOrderDBConnection db = new PurchaseOrderDBConnection();
		ResultSet rs = dbConnection.retrieveAllData21();
		try {
			while (rs.next()) {
				AllUserV.addElement(rs.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		UsersCB.setModel(AllUserV);
		UsersCB.setSelectedIndex(0);
	}
}
