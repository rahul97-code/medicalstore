package hms.doc.scanning;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import hms.doc.scanning.database.DocScanDBConnection;
import hms.patient.slippdf.OpenFile;

import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.border.MatteBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.JCheckBox;

public class DocScanning extends JDialog{
	

    private static DocScanning instance;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() { 
				try {
					new DocScanning(null).setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private double scale = 1.0;

	private static String scannedFolder="ScannedSlips";
	private static String scannedPdfFolder="ScannedPdf";

	Vector files = new Vector();
	List<BufferedImage> images = new ArrayList<>();
	private static Webcam webcam;
	private WebcamPanel liveCamPanel;
	int imagesCount=0; 

	private JPanel imagesPanel;
	private JTextField BillNoTF;

	/**
	 * Create the application.
	 * @throws Exception 
	 */
	public DocScanning(JFrame owner) {
		super(owner, "Document Scanner", false);
		deleteImageFiles(new File(scannedFolder));
		makeDirectory(scannedFolder);
		makeDirectory(scannedFolder+"/"+scannedPdfFolder);

		setResizable(false);

		setBounds(100, 100, 1022, 515);
		getContentPane().setLayout(null);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(471, 55, 528, 411); 
		getContentPane().add(scrollPane);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setIconImage(new ImageIcon(DocScanning.class.getResource("/icons/scanner.png")).getImage());



		imagesPanel = new JPanel();
		imagesPanel.setLayout(new BoxLayout(imagesPanel, BoxLayout.X_AXIS));
		scrollPane.setViewportView(imagesPanel);

		try {
			initializeWebcam();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		liveCamPanel = new WebcamPanel(webcam);
		liveCamPanel.setBounds(28, 12, 431, 370);
		liveCamPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		getContentPane().add(liveCamPanel);

		final JButton btnCaptured = new JButton("");
		btnCaptured.setBounds(28, 394, 431, 33);
		btnCaptured.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BillNoTF.setText("");
				BufferedImage captured = webcam.getImage();
				String billID=new AutoDetectBarcode128Reader().readAndCropBarcode(captured);
				BillNoTF.setText(billID);
				if (captured == null) {
					JOptionPane.showMessageDialog(null, "Failed to capture image.");
					return;
				} 
				BufferedImage rotated = rotateImage90(captured);
				ZoomableImagePanel zoomPanel = new ZoomableImagePanel(rotated,250);
				zoomPanel.setPreferredSize(new Dimension(400, 300)); 
				JScrollPane scrollPane = new JScrollPane(zoomPanel);
				scrollPane.setPreferredSize(new Dimension(400, 400)); 

				final JPanel wrapperPanel = new JPanel(new BorderLayout());
				wrapperPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
				wrapperPanel.add(zoomPanel, BorderLayout.CENTER);

				JButton btnDelete = new JButton("Delete");
				btnDelete.setIcon(new ImageIcon(DocScanning.class.getResource("/icons/trash_button.png")));
				wrapperPanel.add(btnDelete, BorderLayout.NORTH);
				btnDelete.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int i = imagesPanel.getComponentZOrder(wrapperPanel);
						System.out.println("Deleting panel at index: " + i);

						images.remove(i);
						files.remove(i);

						imagesPanel.remove(wrapperPanel);
						imagesPanel.revalidate();
						imagesPanel.repaint();
						if(images.size()==0)
							BillNoTF.setText("");
					}
				});
				if(captured!=null) 
				{images.add(captured);
				files.add("image_"+imagesCount);
				imagesCount++;
				}

				imagesPanel.add(wrapperPanel);
				imagesPanel.revalidate();
				imagesPanel.repaint();
			}
		});  
		btnCaptured.setIcon(new ImageIcon(DocScanning.class.getResource("/icons/shoot_button.png")));
		getContentPane().add(btnCaptured);

		
		  addMouseListener(new MouseAdapter() {
	            @Override
	            public void mousePressed(MouseEvent e) {
	                toFront();
	            }
	        });
		  
		JButton btnUpload = new JButton("Save");
		btnUpload.setIcon(new ImageIcon(DocScanning.class.getResource("/icons/SAVE.PNG")));
		btnUpload.setBounds(209, 439, 250, 27);
		btnUpload.getInputMap(JComponent.WHEN_FOCUSED)
		.put(KeyStroke.getKeyStroke("SPACE"), "none");

		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (images.size()==0) {
					JOptionPane.showMessageDialog(null, "Please capture images!");
					return;
				}

				if (BillNoTF.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "No barcode detected.!");
					return;
				}
				saveImages(images, scannedFolder);
				String billId=BillNoTF.getText().toString();
				String localPath=scannedFolder+"/"+scannedPdfFolder+"/"+billId+".pdf";
				String destiPath=billId+"/"+billId+".pdf";
				createPDFFromImages(scannedFolder, localPath);
			    SmbFileUploader.uploadAllPdfsInThread(scannedFolder+"/"+scannedPdfFolder);

				imagesPanel.removeAll();
				imagesPanel.revalidate();
				imagesPanel.repaint();
				images.clear();
				BillNoTF.setText("");
			}
		});
		btnUpload.setForeground(new Color(0, 0, 205)); 
		btnUpload.setBackground(new Color(176, 224, 230));
		btnUpload.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 13));
		getContentPane().add(btnUpload);

		JButton btnCancel = new JButton("Close");
		btnCancel.setIcon(new ImageIcon(DocScanning.class.getResource("/icons/CANCEL.PNG")));
		btnCancel.setBounds(28, 439, 169, 27);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				releaseResources();
			}
		});
		btnCancel.setForeground(new Color(255, 0, 0));
		btnCancel.setBackground(new Color(192, 192, 192));
		btnCancel.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 13));
		getContentPane().add(btnCancel);

		BillNoTF = new JTextField();
		BillNoTF.setEditable(false);
		BillNoTF.setColumns(10);
		BillNoTF.setBounds(558, 18, 143, 26);
		getContentPane().add(BillNoTF);

		JLabel lblBillId = new JLabel("Bill Id :");
		lblBillId.setFont(new Font("Dialog", Font.PLAIN, 13));
		lblBillId.setBounds(503, 25, 70, 15);
		getContentPane().add(lblBillId);

		JLabel lblMsBillSlip = new JLabel("MS Bill Slip Scanner");
		lblMsBillSlip.setForeground(new Color(51, 102, 153));
		lblMsBillSlip.setFont(new Font("Dialog", Font.ITALIC, 23));
		lblMsBillSlip.setBounds(713, 14, 256, 31);
		getContentPane().add(lblMsBillSlip);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(DocScanning.class.getResource("/icons/bSearch.png")));
		lblNewLabel.setBounds(955, 9, 70, 43);
		getContentPane().add(lblNewLabel);

		final JCheckBox checkBox = new JCheckBox("");
		checkBox.getInputMap(JComponent.WHEN_FOCUSED)
		.put(KeyStroke.getKeyStroke("SPACE"), "none");
		checkBox.setEnabled(false);
		checkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(checkBox.isSelected()) {
					BillNoTF.setEditable(true);
					BillNoTF.requestFocus();
				}else {
					BillNoTF.setEditable(false);
				}
			}
		});
		checkBox.setFont(new Font("Dialog", Font.BOLD, 15));
		checkBox.setBounds(471, 20, 32, 23);
		getContentPane().add(checkBox);


		imagesPanel.addMouseWheelListener(new MouseAdapter() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() < 0) {
					scale *= 1.1;
				} else {
					scale /= 1.1;
				}
				repaint();
			}
		});
  
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				releaseResources();
			}
		});

		JRootPane rootPane = this.getRootPane();
		rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
		.put(KeyStroke.getKeyStroke("SPACE"), "mySpaceAction");

		rootPane.getActionMap()
		.put("mySpaceAction", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				btnCaptured.doClick();
			}
		});

	}

	private static void createPDFFromImages(String folderPath, String pdfPath) {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
			document.open();

			File folder = new File(folderPath);
			File[] files = folder.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".png");
				}
			});

			if (files != null) {
				for (File file : files) {
					Image img = Image.getInstance(file.getAbsolutePath());
					float imgWidth = img.getWidth();
					float imgHeight = img.getHeight();
					float pageWidth = imgWidth + 36f + 36f;
					float pageHeight = imgHeight + 36f + 36f; 
					document.setPageSize(new Rectangle(pageWidth, pageHeight));
					document.newPage();
					document.setMargins(36f, 36f, 36f, 36f);
					img.setAbsolutePosition(36f, 36f); 
					img.scaleToFit(imgWidth, imgHeight);
					document.add(img);
					System.out.println("Added image to PDF: " + file.getAbsolutePath());
				}
			}
			document.close();
			System.out.println("PDF created successfully.");
			//			new OpenFile(pdfPath);
		} catch (DocumentException | IOException e) {
			System.out.println("Error creating PDF: " + e.getMessage());
		}
	}

	private static void makeDirectory(String folderPath) {
		File folder = new File(folderPath);
		if (!folder.exists()) {
			folder.mkdirs();
		} 
	}
	public static boolean deleteImageFiles(File dir) {
	    if (dir.isDirectory()) {
	        File[] files = dir.listFiles();
	        if (files != null) {
	            for (File file : files) {
	                if (file.isDirectory()) {
	                    deleteImageFiles(file); // recurse into subdir
	                } else {
	                    String name = file.getName().toLowerCase();
	                    if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") ||
	                        name.endsWith(".gif") || name.endsWith(".bmp") || name.endsWith(".webp")) {
	                        if (!file.delete()) {
	                            System.out.println("Failed to delete image: " + file.getAbsolutePath());
	                        }
	                    }
	                }
	            }
	        }
	    }
	    return true; // returning true since we're not deleting the dir itself
	}

	private static void saveImages(List<BufferedImage> images, String folderPath) {
		int i = 1;
		for (BufferedImage image : images) {
			File outputFile = new File(folderPath, "image" + i + ".png");
			try {
				ImageIO.write(image, "png", outputFile);
				System.out.println("Saved image: " + outputFile.getAbsolutePath());
			} catch (IOException e) {
				System.out.println("Error saving image: " + e.getMessage());
			}
			i++;
		}
	}


	private static void initializeWebcam() throws Exception {
		try {
			List<Webcam> webcams = Webcam.getWebcams();
			if (webcams.isEmpty()) {
				throw new WebcamException("No Camera detected.");
			}

			boolean opened = false;
			for (Webcam cam : webcams) {
				try {
					Dimension fhd = WebcamResolution.FHD.getSize();
					Dimension[] customSizes = new Dimension[] {
							fhd,
							new Dimension(2560, 1440),
							new Dimension(3840, 2160)
					};
					cam.setCustomViewSizes(customSizes);
					cam.setViewSize(fhd);

					cam.open();
					webcam = cam;
					System.out.println("Camera initialized successfully: " + cam.getName() + " at " + cam.getViewSize());
					opened = true;
					break;
				} catch (WebcamException e) {
					System.out.println("Camera busy: " + cam.getName());
				}
			}

			if (!opened) {
				throw new WebcamException("All Camera are currently in use.");
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			throw new Exception("Camera initialization failed: " + e.getMessage(), e);
		}
	}
	public static BufferedImage getFlippedImage(BufferedImage src, boolean flipH, boolean flipV) {
		int w = src.getWidth();
		int h = src.getHeight();

		int type = src.getType();
		if (type == 0) {
			type = BufferedImage.TYPE_INT_RGB;
		}

		BufferedImage dst = new BufferedImage(w, h, type);

		Graphics2D g = dst.createGraphics();

		int sx1 = flipH ? w : 0;
		int sy1 = flipV ? h : 0;
		int sx2 = flipH ? 0 : w;
		int sy2 = flipV ? 0 : h;

		g.drawImage(src, 0, 0, w, h, sx1, sy1, sx2, sy2, null);
		g.dispose();

		return dst;
	}


	public class ZoomableImagePanel extends JPanel {

		private BufferedImage image;
		private double zoom = 1.0;
		private int targetPanelWidth; 

		/**
		 * Create a zoomable image panel.
		 *
		 * @param img The image to display.
		 * @param panelWidth The fixed width of the panel.
		 */
		public ZoomableImagePanel(BufferedImage img, int panelWidth) {
			this.image = img;
			this.targetPanelWidth = panelWidth;
			int imgW = img.getWidth();
			int imgH = img.getHeight();
			int panelHeight = panelWidth * imgH / imgW;
			setPreferredSize(new Dimension(panelWidth, panelHeight));

			addMouseWheelListener(new MouseWheelListener() {
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					if (e.getWheelRotation() < 0) {
						zoom *= 1.1;
					} else {
						zoom /= 1.1;
					}
					revalidate();
					repaint();
				}
			});
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (image != null) {
				Graphics2D g2d = (Graphics2D) g;

				int panelW = getWidth();
				int panelH = getHeight();

				double imgAspect = (double) image.getWidth() / image.getHeight();
				double panelAspect = (double) panelW / panelH;

				int drawW, drawH;
				if (imgAspect > panelAspect) {
					drawW = (int) (panelW * zoom);
					drawH = (int) (drawW / imgAspect);
				} else {
					drawH = (int) (panelH * zoom);
					drawW = (int) (drawH * imgAspect);
				}

				g2d.drawImage(image,
						(panelW - drawW) / 2,
						(panelH - drawH) / 2,
						drawW,
						drawH,
						null);
			}
		}
	}
	public static BufferedImage rotateImage90(BufferedImage src) {
		int w = src.getWidth();
		int h = src.getHeight();

		BufferedImage rotated = new BufferedImage(h, w, src.getType() == 0 ? BufferedImage.TYPE_INT_RGB : src.getType());
		Graphics2D g2d = rotated.createGraphics();

		g2d.translate(h, 0);
		g2d.rotate(Math.toRadians(90));

		g2d.drawImage(src, 0, 0, null);
		g2d.dispose();

		return rotated;
	}

	private void releaseResources() {
		if (webcam!=null && webcam.isOpen()) {
			webcam.close();
			System.out.println("Webcam closed.");
		}
		if (this != null) {
			instance = null;
			dispose();
		}
	}
}
