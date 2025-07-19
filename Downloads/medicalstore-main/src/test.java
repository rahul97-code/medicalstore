import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileFilter;

import java.io.IOException;

public class test {

    public static void main(String[] args) {
        // SMB server details
        String smbUrl = "smb://192.168.1.33/data/HMS/Patient/0000140017999/Exam/	";
        
        try {
            // Connect to the SMB share using anonymous access
            SmbFile smbFile = new SmbFile(smbUrl);
            
            // List files in the directory
            SmbFile[] files = smbFile.listFiles(new SmbFileFilter() {
                @Override
                public boolean accept(SmbFile file) {
                    return true;  // Accept all files
                }
            });
            
            System.out.println("Files in the directory:");
            for (SmbFile file : files) {
                System.out.println(file.getName());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
