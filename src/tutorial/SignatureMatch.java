package tutorial;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;


/*
 * Author: Meng Lin
 * Signature usage test
 * */

public class SignatureMatch {

	
	public KeyPair generateKeyPair(){
		KeyPair pair = null;
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
			keyGen.initialize(2048);
			pair = keyGen.generateKeyPair();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return pair;
	}
	
	public byte[] generateSignature(PrivateKey key, String data) {
		byte[] b = null;
		try {
			Signature dsa = Signature.getInstance("SHA256withDSA");
			dsa.initSign(key);
			dsa.update(data.getBytes());
			b = dsa.sign();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
	public boolean verifySignature(PublicKey key, byte[] signature, byte[] data) {
		boolean verify = false;
		try {
			Signature dsa = Signature.getInstance("SHA256withDSA");
			dsa.initVerify(key);
			dsa.update(data);
			verify = dsa.verify(signature);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return verify;
	}
	
	public void printData() {
		KeyPair kp;
		byte[] sig;
		String[] data = new String[3];
		data[0] = "Tracy Lamar McGrady Jr. (born May 24, 1979) is an American retired professional basketball player who is best known for his career in the National Basketball Association (NBA), where he played as both a shooting guard and small forward. McGrady is a seven-time NBA All-Star, seven-time All-NBA selection, two-time NBA scoring champion, and one-time winner of the NBA Most Improved Player Award. He was inducted into the Naismith Memorial Basketball Hall of Fame as part of the Class of 2017.";
		data[1] = "McGrady entered the NBA straight out of high school and was selected as the ninth overall pick by the Toronto Raptors in the 1997 NBA draft. Beginning his career as a low-minute player, he gradually improved his role with the team, eventually forming an exciting duo with his cousin Vince Carter. In 2000, he left the Raptors for the Orlando Magic, where he became one of the league's most prolific scorers and a candidate for the NBA Most Valuable Player Award. In 2004, he was traded to the Houston Rockets, where he paired with center Yao Ming to help the Rockets become a perennial playoff team. His final seasons in the NBA were plagued by injuries, and he retired in 2013 following a brief stint with the Qingdao DoubleStar Eagles of the Chinese Basketball Association (CBA) and the San Antonio Spurs.";
		data[2] = "Since retiring, McGrady has worked as a basketball analyst for ESPN. From April–July 2014, he realized his dream of playing professional baseball, pitching for the Sugar Land Skeeters of the Atlantic League of Professional Baseball.";
		for(int i = 0; i < 3; i++) {
			kp = generateKeyPair();
			sig = generateSignature(kp.getPrivate(), data[i]);
			try {
				FileOutputStream fos = new FileOutputStream("/home/linm/Projects/JCA_tutorial/publickey" + i);
				fos.write(kp.getPublic().getEncoded());
				fos.close();
				fos = new FileOutputStream("/home/linm/Projects/JCA_tutorial/sig" + i);
				fos.write(sig);
				fos.close();
				fos = new FileOutputStream("/home/linm/Projects/JCA_tutorial/data" + i);
				fos.write(data[i].getBytes());
				fos.close();
			
			}catch(Exception e) {
				e.printStackTrace();
			}
		 
		}
	}
}
