package hms.AutoUpdate;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.border.TitledBorder;

import hms.main.Developing_environment;

import javax.swing.border.LineBorder;
import java.awt.Color;

public class UpdaterMain extends JDialog {


	String[] data=new String[10];
	String CURRENT_VERSION="";
	String NEW_VERSION="",mainDir="";
	static String OS;
	String path;
	public Timer timer;
	String mainPath;
	public Window lblNewLabel;
	private JLabel TotalFileSizeLable;
	private JLabel RemainingFileSizeLable;
	private JLabel cuurentVersionLbl;
	private JLabel NewVersionLbl;
	public static JProgressBar progressBar;
	public static String UserDir = new String("" + System.getProperty("user.dir"));
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		new UpdaterMain().setVisible(true);
	}

	public UpdaterMain() {
		UpdateCheckerDBConnection updateCheckerDBConnection=new UpdateCheckerDBConnection();
		NEW_VERSION=updateCheckerDBConnection.retrieveVersionNo();
		updateCheckerDBConnection.closeConnection();
		readData();
		
		setTitle("New Update");
		setPreferredSize(new Dimension(400, 125));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		//setBounds(new Rectangle(350, 401, 400, 125));
		setBounds(650, 400, 549, 231);
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - getWidth()/2, (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - getHeight()/2);

		getContentPane().setLayout(null);
		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);

		progressBar = new JProgressBar();
		progressBar.setBounds(27, 149, 483, 29);
		progressBar.setToolTipText("MS++ is updating so please wait");
		getContentPane().add(progressBar);

		JLabel lblNewLabel_2 = new JLabel(" MS++ UPDATING ...");
		lblNewLabel_2.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 16));
		lblNewLabel_2.setIcon(new ImageIcon(UpdaterMain.class.getResource("/icons/updates.png")));
		lblNewLabel_2.setBounds(12, 12, 262, 61);
		getContentPane().add(lblNewLabel_2);

		JLabel label = new JLabel("");
		label.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Downloading", TitledBorder.RIGHT, TitledBorder.TOP, null, new Color(51, 51, 51)));
		label.setBounds(12, 128, 515, 61);
		getContentPane().add(label);

		JLabel lblFileSize = new JLabel("File Size : ");
		lblFileSize.setBounds(27, 85, 79, 15);
		getContentPane().add(lblFileSize);

		TotalFileSizeLable = new JLabel("");
		TotalFileSizeLable.setBounds(96, 85, 104, 15);
		getContentPane().add(TotalFileSizeLable);

		JLabel lblFileSize_1 = new JLabel("Downloaded : ");
		lblFileSize_1.setBounds(27, 112, 119, 15);
		getContentPane().add(lblFileSize_1);

		RemainingFileSizeLable = new JLabel("");
		RemainingFileSizeLable.setBounds(134, 112, 111, 15);
		getContentPane().add(RemainingFileSizeLable);

		JLabel label_1 = new JLabel("Current version : ");
		label_1.setBounds(310, 12, 123, 15);
		getContentPane().add(label_1);

		JLabel label_1_1 = new JLabel("New version : ");
		label_1_1.setBounds(333, 35, 100, 15);
		getContentPane().add(label_1_1);

		cuurentVersionLbl= new JLabel(CURRENT_VERSION);
		cuurentVersionLbl.setBounds(445, 12, 82, 15);
		getContentPane().add(cuurentVersionLbl);

		NewVersionLbl = new JLabel(NEW_VERSION);
		NewVersionLbl.setBounds(445, 36, 82, 15);
		getContentPane().add(NewVersionLbl);

	}
	public boolean checkUpdate()
	{	
		System.out.println("[Current Version] :  "+CURRENT_VERSION);
		System.out.println("[New Version] :  "+NEW_VERSION);

		if(!CURRENT_VERSION.equals(NEW_VERSION))
			return true;
		else 
			return false;
	}
	public void DoUpdate() {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		String source=mainDir+"/MS/MS UPDATES/MS++.jar";
		System.out.println("[ServerFile Dir] :   "+source);
		final File desti=new File(UserDir,"MS++.jar");
		System.out.println("[LocalFile Dir] :	"+desti);


		String fileName = "data.mdi";
		String content = null;
		File file = new File(UserDir,fileName); // For example, foo.txt
		FileReader reader = null;
		try {
			reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			content = new String(chars);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		content=content.replace(CURRENT_VERSION, NEW_VERSION);
		System.out.println(content);
		try {
			FileWriter writer = new FileWriter(file);
			System.out.println("writing---"+file);
			writer.write(content);
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	


		new Task_StringUpdate(source,desti).execute();
	}

	class Task_StringUpdate extends SwingWorker<Void, String> {

		JLabel jlabel;
		public String url;
		public File target;		

		public Task_StringUpdate(String url,File target) {
			this.url=url;
			this.target=target;
			progressBar.setStringPainted(true);

		}

		@Override
		public void process(List<String> chunks) {
			int i = Integer.parseInt(chunks.get(chunks.size()-1));
			progressBar.setValue(i);
		}

		@Override
		public Void doInBackground() throws Exception {            
			SmbFile remoteFile = new SmbFile(url+"");
			OutputStream os = new FileOutputStream(target);
			InputStream is = remoteFile.getInputStream();
			long completeFileSize=remoteFile.getContentLength();
			TotalFileSizeLable.setText(GetFileSize(completeFileSize));
			long read = 0;
			int count = 0;
			long downloadedFileSize=0;
			int bufferSize = 5096;    		

			byte[] b = new byte[bufferSize];
			while ((count = is.read(b)) != -1) {
				os.write(b, 0, count);

				read += count;
				downloadedFileSize+= count;
				RemainingFileSizeLable.setText(GetFileSize(downloadedFileSize));
				final int currentProgress = (int) ((((double)downloadedFileSize) / ((double)completeFileSize)) * 100d);
				publish(currentProgress+"");

			}
			os.close();
			is.close();

			return null;
		}

		@Override
		public void done() {

			try {
				get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			runprogram();
		}
	}

	public void readData() {
		FileInputStream fis = null;
		int counter = 0;
		try {
			File file = new File(UserDir,"data.mdi");
			fis = new FileInputStream(file);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {

				data[counter] = strLine;
				counter++;
			}
			String mainDir1[] = new String[22];
			int j = 0;
			for (String retval : data[0].split("@")) {
				mainDir1[j] = retval;
				j++;
			}
			mainDir = mainDir1[1];
			if(counter==1)
			{
				return;
			}

			String verion[] = new String[22];
			int i = 0;
			for (String retval : data[1].split(":")) {
				verion[i] = retval;
				i++;
			}
			CURRENT_VERSION=verion[1].trim();
			in.close();
		} catch (Exception ex) {
			System.out.print(""+ex);
		} finally {
			try {
				if (null != fis)
					fis.close();
			} catch (IOException ex) {
			}
		}

	}
	public static void runprogram() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if (System.getProperty("java.version").toString().equals("null")) {
					ProcessBuilder pb = new ProcessBuilder(
							new File(System.getProperty("user.dir"), "jre/bin/java.exe")
							.toString(), "-jar", new File(System
									.getProperty("user.dir"), "MS++.jar")
							.toString());
					pb.directory(new File("" + System.getProperty("user.dir")));
					try {
						Process p = pb.start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					ProcessBuilder pb = new ProcessBuilder("java", "-jar",
							new File("MS++.jar")
							.toString());
					System.out.println("java -jar "+new File(System.getProperty("user.dir")+"MS++.jar")
							.toString());
					pb.directory(new File("" + System.getProperty("user.dir")));
					try {
						Process p = pb.start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		System.exit(0);
	}

	public static String GetFileSize(long size) {

		DecimalFormat df = new DecimalFormat("0.00");
		float sizeKb = 1024.0f;
		float sizeMb = sizeKb * sizeKb;
		float sizeGb = sizeMb * sizeKb;
		float sizeTerra = sizeGb * sizeKb;

		if(size < sizeMb)
			return df.format(size / sizeKb)+ " Kb";
		else if(size < sizeGb)
			return df.format(size / sizeMb) + " Mb";
		else if(size < sizeTerra)
			return df.format(size / sizeGb) + " Gb";

		return "";
	}

	public void OPenFileWindows(String path) {
		try {

			File f = new File(path);
			if (f.exists()) {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(f);
				} else {
					System.out.println("File does not exists!");
				}
			}
		} catch (Exception ert) {
		}
	}

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	

	public static boolean isSolaris() {
		return (OS.indexOf("sunos") >= 0);
	}
	public static void KillAllProcess() {
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("bash", "-c", "jps -V");
			Process process = null ;
			try {

				process = processBuilder.start();
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line;
				Vector<String> v=new Vector<String>();
				while ((line = reader.readLine()) != null) {
					if(line.split(" ")[1].equals("MS++.jar")) {
						v.add(line.split(" ")[0]);
					}
				}
			for(int i=0;i<v.size();i++) {
//				System.out.println(v.get(i));
				new ProcessBuilder().command("bash", "-c", "kill "+v.get(i)+"").start();
			}
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				process.destroy();
			} 
	}
	public static boolean isUnix() {
		OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS
				.indexOf("aix") > 0);
	}

	
}
