package tutorial;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestTest {
	
	public byte[] encrptMessage(String message) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		}catch(NoSuchAlgorithmException e) {
			System.out.print(e);
			e.printStackTrace();
		}
		byte[] messageBytes = message.getBytes();
		return md.digest(messageBytes);
	}
	
}
