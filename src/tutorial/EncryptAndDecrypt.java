package tutorial;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

/*
 * Author: Meng Lin
 * This is class that show the usage of class Cipher.
 * It has some methods to encrypt or decrypt the text
 * Use the AES cipher with GCM mode*/
public class EncryptAndDecrypt {
	
	public static void main(String[] args) {
		Key k1 = null, k2 = null;
		try {
			KeyGenerator keygen1 = KeyGenerator.getInstance("AES");
			k1 = keygen1.generateKey();
			
			byte[] aesKeyData = k1.getEncoded();
			FileOutputStream fos = new FileOutputStream("key");
			fos.write(aesKeyData);
			fos.close();
			//FileInputStream fis = new FileInputStream("key");
			byte[] key = Files.readAllBytes(Paths.get("key"));
			//fis.read(key);
			k2 = new SecretKeySpec(key, "AES");
			//SecretKeyFactory factory = SecretKeyFactory.getInstance("AES");
			//k2 = factory.generateSecret(secretKey);
		}catch(Exception e) {
			e.printStackTrace();
		}
		String s = "This is a example of how the Cipher works";
		String file = "encrpted.txt";
		encrypt(file, s, k1);
		System.out.println(decrypt(file, k2));
	}
	
	public static void encrypt(String filename, String text, Key k) {
		try {
			Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
			aes.init(Cipher.ENCRYPT_MODE, k);
			FileOutputStream fos = new FileOutputStream(filename);
			CipherOutputStream out = new CipherOutputStream(fos, aes);
			MessageDigest md = MessageDigest.getInstance("SHA");
			out.write(text.getBytes());
			out.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String decrypt(String filename, Key k) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
			aes.init(Cipher.DECRYPT_MODE, k);
			FileInputStream fis = new FileInputStream(filename);
			CipherInputStream in = new CipherInputStream(fis, aes);
			
			byte[] b = new byte[1024];
			int numOfByteRead;
			while((numOfByteRead = in.read(b)) >= 0) {
				baos.write(b, 0, numOfByteRead);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new String(baos.toByteArray());
	}
}
