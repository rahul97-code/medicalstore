package hms.patient.slippdf;

import hms.admin.gui.AdminMain;
import hms.departments.database.DepartmentDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.opd.database.OPDDBConnection;
import hms.report.excel.DoctorWiseReportExcel;
import hms.report.excel.InvoiceItemsReportExcel;
import hms.report.excel.PurchaseTaxReportExcel;
import hms.report.excel.SalesTaxReportExcel;
import hms.store.gui.StoreMain;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;

@SuppressWarnings("serial")
public class DateSelection extends JDialog {

	private JPanel contentPane;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom, dateTo;

	/**
	 * Create the frame.
	 */
	public DateSelection(final int index) {
		setResizable(false);
		setTitle("Date Selection");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				DateSelection.class.getResource("/icons/rotaryLogo.png")));
		setBounds(400, 150, 334, 266);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setModal(true);
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 306, 213);
		contentPane.add(panel);
		panel.setLayout(null);

		dateFromDC = new JDateChooser();
		dateFromDC.setBounds(106, 54, 178, 25);
		panel.add(dateFromDC);
		dateFromDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateFrom = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
						}
					}
				});
		dateFromDC.setDate(new Date());
		dateFromDC.setMaxSelectableDate(new Date());
		dateFromDC.setDateFormatString("yyyy-MM-dd");

		dateToDC = new JDateChooser();
		dateToDC.setBounds(106, 90, 178, 25);
		panel.add(dateToDC);
		dateToDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateTo = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
						}
					}
				});
		dateToDC.setDate(new Date());
		//dateToDC.setMaxSelectableDate(new Date());
		dateToDC.setDateFormatString("yyyy-MM-dd");

		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setBounds(10, 95, 73, 14);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(10, 54, 82, 14);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnNewButton_2 = new JButton("PDF");
		btnNewButton_2.addActionListener(new ActionListener() {
			private JComponent[] inputs;

			@Override
			public void actionPerformed(ActionEvent e) {

				if (index == 0) {
					try {
						new NewSummaryReportPDF(dateFrom, dateTo);
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				if (index == 2) {
					// try {
					// new CancelReportPDF(dateFrom, dateTo);
					// } catch (DocumentException e1) {
					// // TODO Auto-generated catch block
					// e1.printStackTrace();
					// } catch (IOException e1) {
					// // TODO Auto-generated catch block
					// e1.printStackTrace();
					// }
				}
				if (index == 3) {

					try {
						new InvoiceItemsReportExcel(dateFrom, dateTo);
					} catch (DocumentException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (index == 4) {
					JTextField firstName = new JTextField();
					JTextField CashAmt = new JTextField();
					JTextField YesAmt = new JTextField();
					YesAmt.setText("0.0");
					CashAmt.addKeyListener(new KeyAdapter() {
						@Override
						public void keyTyped(KeyEvent e) {
							char c=e.getKeyChar();
							if(!Character.isDigit(c)) {
								e.consume();
							}
						}
					});
					YesAmt.addKeyListener(new KeyAdapter() {
						@Override
						public void keyTyped(KeyEvent e) {
							char c=e.getKeyChar();
							if(!Character.isDigit(c)) {
								e.consume();
							}
						}
					});
					try {

						if(dateFrom.equals(DateFormatChange.StringToMysqlDate(new Date())) && dateTo.equals(DateFormatChange.StringToMysqlDate(new Date()))) {

							InsuranceDBConnection db=new InsuranceDBConnection();

							int value=db.retrieveDatacashexist(StoreMain.userID);
							db.closeConnection();
							if(value==1) {
								inputs = new JComponent[] {
										new JLabel("Reason :-"),
										firstName,
										new JLabel("Enter Today Cash Amount"),
										CashAmt,
										new JLabel("Enter YES BANK Amount"),
										YesAmt
								};

							}
							else {
								inputs = new JComponent[] {
										new JLabel("Enter Today Cash Amount"),
										CashAmt,
										new JLabel("Enter YES BANK Amount"),
										YesAmt


								};
							}
							int result = JOptionPane.showConfirmDialog(null, inputs, "Verify Cash Amount", JOptionPane.PLAIN_MESSAGE);
							if (result == JOptionPane.OK_OPTION) {
								if(value==1) {
									if(!(CashAmt.getText().toString().equals("") || firstName.getText().toString().equals("")))
									{
										new NewSummaryReport(dateFrom, dateTo,StoreMain.userName,StoreMain.id);
									}
									else {
										JOptionPane.showMessageDialog(null,"Please, Enter Field Value.");  
										return;
									}

								}
								else {
									if(!(CashAmt.getText().toString().equals(""))) {
										new NewSummaryReport(dateFrom, dateTo,StoreMain.userName,StoreMain.id);
									}
									else {
										JOptionPane.showMessageDialog(null,"Please, Enter Field Value."); 
										return;
									}
								}

							}


							String[] data = new String[8];
							InsuranceDBConnection db1=new InsuranceDBConnection();
							data[0]=StoreMain.userID;
							data[1]=StoreMain.userName;
							data[2]=CashAmt.getText().toString();
							data[3]=cashamount()+"";
							data[4]="store";
							data[5]=firstName.getText()+"";
							data[6]=compareAmount(cashamount(),Integer.parseInt(CashAmt.getText().toString()))+"";
							data[7]=YesAmt.getText()+"";
							try {
								db1.InsertData(data);
								db1.closeConnection();
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							//new NewSummaryReport(dateFrom, dateTo,StoreMain.userName,StoreMain.id);
						} 



						else {
							new NewSummaryReport(dateFrom, dateTo,StoreMain.userName,StoreMain.id);
						}

					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (index == 5) {
					try {
						JCheckBox checkBox = new JCheckBox("I agree.");
						Object[] params = { "Print Vendor:", checkBox };

						int option = JOptionPane.showConfirmDialog(
								null,
								params,
								"Confirmation",
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE
								);

						boolean isChecked = false;

						if (option == JOptionPane.OK_OPTION) {
							isChecked = checkBox.isSelected();
							new NearExpiryPdf(dateFrom, dateTo,isChecked);
						}

					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (index == 6) {
					try {
						new HighRiskItemPDF(dateFrom, dateTo);
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (index == 7) {

					try {
						new ItemnotsalePDF2().generateReport(dateTo, dateFrom);
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (index == 8) {
					try {
						new medicalstoreSaleSummery().generateReport(dateTo, dateFrom);
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				if(index == 9)
				{
					try {
						new karunaMedDiscountPDF().generateReport(dateTo, dateFrom);
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

			private int compareAmount(int cashamount,int enteramount) {
				// TODO Auto-generated method stub
				int diff=Math.abs(cashamount - enteramount);
				int check;
				if(diff<10) 
					check=1;
				else
					check =0;



				return check;
			}

			private int cashamount() {
				// TODO Auto-generated method stub
				InsuranceDBConnection db1=new InsuranceDBConnection();

				ResultSet rs=db1.CheckcashAmount(dateFrom, dateTo,StoreMain.userName);
				int amount = 0;
				try {
					while(rs.next()) {
						amount=rs.getInt(1);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return amount;
			}
		});
		btnNewButton_2.setBounds(10, 155, 87, 35);
		panel.add(btnNewButton_2);
		btnNewButton_2.setIcon(null);
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setIcon(new ImageIcon(DateSelection.class
				.getResource("/icons/opd.gif")));
		lblNewLabel.setBounds(35, 324, 111, 83);
		panel.add(lblNewLabel);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setIcon(new ImageIcon(DateSelection.class
				.getResource("/icons/CANCEL.PNG")));
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancel.setBounds(209, 155, 87, 35);
		panel.add(btnCancel);

		JLabel lblHms = new JLabel("HMS");
		lblHms.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblHms.setHorizontalAlignment(SwingConstants.CENTER);
		lblHms.setBounds(76, 11, 133, 32);
		panel.add(lblHms);

		JButton btnExcel = new JButton("Excel");
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (index == 3) {

					try {
						new InvoiceItemsReportExcel(dateFrom, dateTo);
					} catch (DocumentException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (index == 20) {
					try {
						new DoctorWiseReportExcel(dateFrom, dateTo);
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				if (index == 21) {
					try {
						new PurchaseTaxReportExcel(dateFrom, dateTo);
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				if (index == 22) {
					try {
						new SalesTaxReportExcel(dateFrom, dateTo);
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		});
		btnExcel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExcel.setBounds(106, 155, 87, 35);
		panel.add(btnExcel);
	}
}
