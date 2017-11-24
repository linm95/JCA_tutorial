package tutorial;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;


/*
 * Author: Meng Lin
 * Answer of exercise 3
 * */

public class Exercise3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SignatureMatch sm = new SignatureMatch();
		//sm.printData();
		byte[][] sigs= new byte[3][];
		byte[][] data = new byte[3][];
		PublicKey[] keys = new PublicKey[3];
		for(int i = 0; i < 3; i++) {
			try {
				sigs[i] = Files.readAllBytes(Paths.get("/home/linm/Projects/JCA_tutorial/sig" + i));
				data[i] = Files.readAllBytes(Paths.get("/home/linm/Projects/JCA_tutorial/data" + i));
				byte[] encodedPubKey = Files.readAllBytes(Paths.get("/home/linm/Projects/JCA_tutorial/publickey" + i));
				X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encodedPubKey);
				KeyFactory keyFactory = KeyFactory.getInstance("DSA");
				keys[i] = keyFactory.generatePublic(pubKeySpec);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		sm = new SignatureMatch();
		for(int i = 0; i < 3; i ++) {
			for(int j = 0; j  < 3; j++) {
				for(int k = 0; k < 3; k ++) {
					if(sm.verifySignature(keys[i], sigs[j], data[k])) {
						System.out.println("publickey" + i + ", sig" + j + ", data" + k +" match");
					}
				}
			}
		}
	}

}
