package tutorial;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * Author: Meng Lin
 * MessageDigest usage test
 * */

public class MessageDigestTest {
	
	public void passwordTest(String passwordInFile, String passwordEntered){
		//String passwordInFile = "fjreuisdbqw1123";
		//String passwordEntered = "fjreuisdbqw1123";
		if(checkMatch(encrptMessage(passwordInFile), encrptMessage(passwordEntered)))
			System.out.println("password match");
		else System.out.println("wrong password");
	}

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

	public boolean checkMatch(byte[] en1, byte[] en2){
		if(en1.length != en2.length)
			return false;
		for(int i = 0; i < en1.length; i++){
			if(en1[i] != en2[i])
				return false;
		}
		return true;
	}
}
