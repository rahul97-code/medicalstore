package hms.admin.gui;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class IPA  extends JFrame{
	public IPA() {
	}
	
	public static void main(String[] args){
		 InetAddress my_localhost = null;
		String hostname=null;
		try {
			my_localhost = InetAddress.getLocalHost();
			hostname=my_localhost.getHostName();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	      System.out.println("The IP Address of client is : " + (my_localhost.getHostAddress()).trim());
	      System.out.println("The SurverName Address of client is : " + hostname.trim());
	      JOptionPane.showMessageDialog(null, "IP Addresss: "+ (my_localhost.getHostAddress()) + " Server Name: " + hostname);
//	      String my_system_address = "";
//	      try{
//	         URL my_url = new URL("http://bot.whatismyipaddress.com");
//	         BufferedReader my_br = new BufferedReader(new
//	         InputStreamReader(my_url.openStream()));
//	         my_system_address = my_br.readLine().trim();
//	      }
//	      catch (Exception e){
//	         my_system_address = "Cannot Execute Properly";
//	      }
	}
}
