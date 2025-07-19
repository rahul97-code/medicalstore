package hms.doc.scanning;
import jcifs.smb.*;
import java.io.*;
import javax.swing.*;

import hms.doc.scanning.database.DocScanDBConnection;

public class SmbFileUploader implements Runnable {

    private static final String SMB_SERVER = "192.168.1.138";  // Samba server IP
    private static final String SMB_SHARE = "/data/MS/ScannedBillSlips/";        // Share name

    private static String smbUsername = "hospital";
    private static String smbPassword = "rotaryhospital";

    private final String localFilePath;
    private final String remoteRelativePath;
    private final String billId;
    
    public static void main(String[] args) {
        String localFile = "opdslip.pdf";
        String remotePath = "MS/DailyScannedSlips/" + new File(localFile).getName();


        uploadInThread(localFile, remotePath,"");
    }

 
    public SmbFileUploader(String localFilePath, String remoteRelativePath,String billId) {
        this.localFilePath = localFilePath;
        this.remoteRelativePath = remoteRelativePath;
        this.billId = billId;
    }

    @Override
    public void run() {
        InputStream in = null;
        OutputStream out = null;

        try {
            File file = new File(localFilePath);
            if (!file.exists()) {
                showError("Local file not found: " + localFilePath);
                return;
            }

            in = new FileInputStream(file);

            NtlmPasswordAuthentication auth =
                new NtlmPasswordAuthentication(null, smbUsername, smbPassword);

            String smbFolderUrl = "smb://" + SMB_SERVER + SMB_SHARE;
            String smbFileUrl = smbFolderUrl + remoteRelativePath;

            System.out.println("Uploading to: " + smbFileUrl);

            SmbFile smbFile = new SmbFile(smbFileUrl, auth);
            SmbFile parentDir = new SmbFile(smbFile.getParent(), auth);

            if (!parentDir.exists()) {
                System.out.println("Creating remote directory: " + parentDir.getPath());
                parentDir.mkdirs();
            } else if (!parentDir.isDirectory()) {
                throw new IOException("Remote path exists but is not a directory: " + parentDir.getPath());
            } else {
                System.out.println("Remote directory exists: " + parentDir.getPath());
            }

            out = new SmbFileOutputStream(smbFile);

            byte[] buffer = new byte[32768];
            int bytesRead;

            System.out.println("Start uploading file...");
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            System.out.println("File uploaded successfully.");
            DocScanDBConnection db=new DocScanDBConnection();
			db.updateScannedBillFlag(billId);
			db.closeConnection();


        } catch (Exception e) {
            final String errorMessage = "Error uploading file:\n" + e.getMessage();
            System.err.println(errorMessage);
            e.printStackTrace();
            showError(errorMessage);
        } finally {
            try { if (in != null) in.close(); } catch (IOException ignored) {}
            try { if (out != null) out.close(); } catch (IOException ignored) {}
        }
    }

    public static void uploadInThread(String localFilePath, String remoteRelativePath,String billId) {
        SmbFileUploader uploader = new SmbFileUploader(localFilePath, remoteRelativePath,billId);
        new Thread(uploader).start();
    }

    private void showError(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(null, message, "Upload Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

   
}
