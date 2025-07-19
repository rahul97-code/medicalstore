package hms.main;


import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import java.awt.Toolkit;

public class AboutHMS extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AboutHMS dialog = new AboutHMS();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AboutHMS() {
		setTitle("About MS++");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(AboutHMS.class.getResource("/icons/smallLogo.png")));
		setModal(true);
		setBounds(400, 200, 416, 332);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(144, 238, 144));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblHms = new JLabel("MS++");
			lblHms.setFont(new Font("Tahoma", Font.BOLD, 32));
			lblHms.setHorizontalAlignment(SwingConstants.CENTER);
			lblHms.setBounds(75, 22, 245, 39);
			contentPanel.add(lblHms);
		}
		{
			JLabel lblHospitalManagementSystem = new JLabel("Medical Store Management System");
			lblHospitalManagementSystem.setFont(new Font("Tahoma", Font.BOLD, 15));
			lblHospitalManagementSystem.setHorizontalAlignment(SwingConstants.CENTER);
			lblHospitalManagementSystem.setBounds(42, 65, 335, 25);
			contentPanel.add(lblHospitalManagementSystem);
		}
		{
			JLabel lblDevelopedManaged = new JLabel("Developed & Managed");
			lblDevelopedManaged.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblDevelopedManaged.setHorizontalAlignment(SwingConstants.CENTER);
			lblDevelopedManaged.setBounds(75, 137, 251, 19);
			contentPanel.add(lblDevelopedManaged);
		}
		{
			JLabel lblBy = new JLabel("By");
			lblBy.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblBy.setHorizontalAlignment(SwingConstants.CENTER);
			lblBy.setBounds(75, 167, 251, 20);
			contentPanel.add(lblBy);
		}
		{
			JLabel lblNewLabel = new JLabel("Sukhpal Saini");
			lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel.setBounds(75, 198, 251, 19);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblMob = new JLabel("Mob. : 9728073421");
			lblMob.setHorizontalAlignment(SwingConstants.CENTER);
			lblMob.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblMob.setBounds(75, 228, 251, 19);
			contentPanel.add(lblMob);
		}
		{
			JLabel lblEmailSukhpalsainigmailcom = new JLabel("Email : sukhpalsaini05@gmail.com");
			lblEmailSukhpalsainigmailcom.setHorizontalAlignment(SwingConstants.CENTER);
			lblEmailSukhpalsainigmailcom.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblEmailSukhpalsainigmailcom.setBounds(75, 258, 251, 19);
			contentPanel.add(lblEmailSukhpalsainigmailcom);
		}
		{
			UpdateCheckerDBConnection updateCheckerDBConnection=new UpdateCheckerDBConnection();
			String newVersion=updateCheckerDBConnection.retrieveVersionNo();
			updateCheckerDBConnection.closeConnection();
			JLabel lblVersionNo = new JLabel("Version No :  " +newVersion);
			lblVersionNo.setHorizontalAlignment(SwingConstants.CENTER);
			lblVersionNo.setFont(new Font("Tahoma", Font.PLAIN, 13));
			lblVersionNo.setBounds(85, 101, 224, 25);
			contentPanel.add(lblVersionNo);
		}
	}

}
