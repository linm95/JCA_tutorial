# JCA_tutorial
Java Cryptography Architecture (JCA) Tutorial
Meng Lin, Wenting Song
Introduction
The Java platform strongly emphasizes security, including language safety, cryptography, public key infrastructure, authentication, secure communication, and access control.

The JCA is a major piece of the platform, and contains a "provider" architecture and a set of APIs for digital signatures, message digests (hashes), certificates and certificate validation, encryption (symmetric/asymmetric block/stream ciphers), key generation and management, and secure random number generation, to name a few. These APIs allow developers to easily integrate security into their application code. The architecture was designed around the following principles:
Implementation independence: Applications do not need to implement security algorithms. Rather, they can request security services from the Java platform. Security services are implemented in providers (see below), which are plugged into the Java platform via a standard interface. An application may rely on multiple independent providers for security functionality.
Implementation interoperability: Providers are interoperable across applications. Specifically, an application is not bound to a specific provider, and a provider is not bound to a specific application.
Algorithm extensibility: The Java platform includes a number of built-in providers that implement a basic set of security services that are widely used today. However, some applications may rely on emerging standards not yet implemented, or on proprietary services. The Java platform supports the installation of custom providers that implement such services.

This tutorial will not cover all the aspects of the JCA and only focus on some basic usages like SHA, encryption and decryption, manipulation of keys, digital signature. For the full view, please visit https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html

Cryptographic Service Providers
java.security.Provider is the base class for all security providers. Each CSP contains an instance of this class which contains the provider's name and lists all of the security services/algorithms it implements. When an instance of a particular algorithm is needed, the JCA framework consults the provider's database, and if a suitable match is found, the instance is created.
Providers contain a package (or a set of packages) that supply concrete implementations for the advertised cryptographic algorithms. Each JDK installation has one or more providers installed and configured by default. Additional providers may be added statically or dynamically. Clients may configure their runtime environment to specify the provider preference order. The preference order is the order in which providers are searched for requested services when no specific provider is requested. The graph below illustrate the relationship between application and provider:

In this tutorial, we will use the default provider of JDK as examples.

Engine Classes and Algorithms
An engine class provides the interface to a specific type of cryptographic service, independent of a particular cryptographic algorithm or provider. The engines either provide:
cryptographic operations (encryption, digital signatures, message digests, etc.),
generators or converters of cryptographic material (keys and algorithm parameters), or
objects (keystores or certificates) that encapsulate the cryptographic data and can be used at higher layers of abstraction.
Main Functions
1. MessageDigest
1.1 Introduction
The SHA (Secure Hash Algorithm) is one of a number of cryptographic hash functions. A cryptographic hash is like a signature for a text or a data file. SHA-256 algorithm generates an almost-unique, fixed size 256-bit (32-byte) hash. Hash is a one way function – it cannot be decrypted back.This makes it suitable for password validation, challenge hash authentication, anti-tamper, digital signatures
1.2 Usage
In JCA, MessageDigest class provide method to encrypt the message to a hashcode use the SHA. In this example, we will use the SHA-256.

First, let’s create a instance of MessageDigest:
MessageDigest md = MessageDigest.getInstance("SHA");

This will create a instance of MessageDigest using the algorithm of “SHA”, which is “SHA-256”.
You can also use:
MessageDigest md = MessageDigest.getInstance("SHA-256");
To create one.
Byte[] hash = md.digest("password".getByte());
This method will transform the string “password” to its corresponding 256-bit hash.
Or if you have multiple text and you want to hash them all, you can use the update() method.
Supposing we have b1, b2 and b3 three byte arrays;
md.update(b1);
md.update(b2);
md.update(b3);
Byte[] hash = md.digest(); //info of b1, b2, b3 will be lost
Notice that every time you call digest() method, all the data you update before will be lost. If you want to keep the former data, you can use the clone() method.
md.update(b1);
md.update(b2);
Byte[] hash = md.clone().digest(); // info of b1, b2 will be kept
md.update(b3);
Byte[] hash = md.digest(); //info of b1, b2, b3 will be lost

1.3 Exercise 1
Use the MessageDigest class to convert two Strings passwordInFile, passwordEntered to their corresponding 256 bit hash. And then compare the hash, if they are equal, print out “password match”, else, print out “wrong password”.
Case 1: 
passwordInFile: “fjreuisdbqw1123”
passwordEntered: “fjreuisdbqw1123”
Case 2:
passwordInFile: “ffreyufrew213”
passwordEntered: “ffreyyufrew213”

2. Key
2.1 Introduction
Key is a interface in JCA used to encrypt or decrypt. The encryption method needs a key to encrypt and only with the correct key can user decrypt the ciphertext.
The java.security.Key interface is the top-level interface for all opaque keys. It defines the functionality shared by all opaque key objects.
An opaque key representation is one in which you have no direct access to the key material that constitutes a key. In other words: "opaque" gives you limited access to the key--just the three methods defined by the Key interface (see below): getAlgorithm, getFormat, and getEncoded.
2.2 KeyGenerator
A key generator is used to generate secret keys for symmetric algorithms.
For example, we can get a instance of KeyGenerator by calling this:
KeyGenerator keygen = KeyGenerator.getInstance("AES"); 
// Use "AES" algorithm
Key k = keygen.generateKey(); // generate the key
This will generate a secrete key which can be used in the “AES” encryption method.
2.3 SecretKeySpec
Sometimes we need to transfer the key we generated to others, so that others can use the key to decrypt ciphertext we generate. And this class can help us generate the key from specific byte array.
First, we should get the content of the encoded key.
byte[] aesKeyData = k.getEncoded();
This method will convert a key to the byte array format.
Next, we use the SecretKeySpec class to restore the key.
k2 = new SecretKeySpec(aesKeyData, "AES");
You should notice that the encryption method should be the same or you will get the wrong key.
2.4 KeyFactory
KeyFactory is used to get the generated public key and private key from encoded keys or key specifications. Public/private key is different from secret key, they are used in the digital signature verifying. 
The usage of key factory is as below:
First, let’s see how to generate the keys from encoded key.
X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encodedPubKey);
//generate the corresponding public key specification object

KeyFactory keyFactory = KeyFactory.getInstance("DSA");//use DSA algorithm

PublicKey pubKey = keyFactory.generatePublic(pubKeySpec); 
// get the corresponding public key.

Next, let’s see how to generate keys from key’s specifications:
DSAPrivateKeySpec dsaPrivKeySpec = new DSAPrivateKeySpec(x, p, q, g);
//Indicate the value of specified parameters

KeyFactory keyFactory = KeyFactory.getInstance("DSA");
// use DSA algorithm

PrivateKey privKey = keyFactory.generatePrivate(dsaPrivKeySpec);
//generate private key

3. Cipher
3.1 Introduction
Although SHA is good algorithm that can provide pretty good encrypt, it is a one-way encryption, which means we cannot get the decode the secret text and get the original message. This is not we want in some cases such as message delivery.
3.2 Usage
The Cipher class provides the functionality of a cryptographic cipher used for encryption and decryption. Encryption is the process of taking data (called cleartext) and a key, and producing data (ciphertext) meaningless to a third-party who does not know the key. Decryption is the inverse process: that of taking ciphertext and a key and producing cleartext.

Now let’s look at the usage of Cipher.
First, we create a Cipher instance:
Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
This indicate that we want use the AES algorithm with ECB mode and PKCS5Padding scheme. Of course you can use other encryption algorithm but in this example we will use the AES method.
Then we need to generate some Key used to encrypt:
KeyGenerator keygen = KeyGenerator.getInstance("AES"); 
// Use “AES” algorithm
Key k = keygen.generateKey(); // generate the key
Then let’s encrypt some message, so choose the encryption mode:
aes.init(Cipher.ENCRYPT_MODE, k);
Now we can encrypt some message like this:
String message = "This is an example of how the Cipher works";
Byte[] ciphertext = aes.doFinal(message.getByte());
The byte array ciphertext is the encrypted message.
Also just like in MessageDigest, if you have multiple scripts of message to encrypt, you can use the update() method:
aes.update("This is an ".getByte());
aes.update("example of ".getByte());
aes.update("how the Cipher works".getByte());
Byte[] ciphertext = aes.doFinal();
Then we can taking the same key used in encryption to work out the cleartext like this:
aes.init(Cipher.DECRYPT_MODE, k);
Byte[] cleartext = aes.doFinal(ciphertext);
Then just convert the byte array to char array and you can get the original message.
3.3 Exercise 2
Now I have a key’s encoded content file key and a ciphertext. The encryption algorithm is AES. Write a program to decrypt the ciphertext and see what the cleartext is.

4. Signature
4.1 Introduction
A digital signature is a mathematical scheme for demonstrating the authenticity of digital messages or documents. A valid digives gital signature a recipient reason to believe that the message was created by a known sender (authentication), that the sender cannot deny having sent the message (non-repudiation), and that the message was not altered in transit (integrity).
Digital signatures are a standard element of most cryptographic protocol suites, and are commonly used for software distribution, financial transactions, contract management software, and in other cases where it is important to detect forgery or tampering.
4.2 Usage
The Signature class is an engine class designed to provide the functionality of a cryptographic digital signature algorithm such as DSA or RSAwithMD5. A cryptographically secure signature algorithm takes arbitrary-sized input and a private key and generates a relatively short (often fixed-size) string of bytes, called the signature.
It can also be used to verify whether or not an alleged signature is in fact the authentic signature of the data associated with it.

A Signature object is initialized for signing with a Private Key and is given the data to be signed. The resulting signature bytes are typically kept with the signed data. When verification is needed, another Signature object is created and initialized for verification and given the corresponding Public Key. The data and the signature bytes are fed to the signature object, and if the data and signature match, the Signature object reports success.

Let’s try a simple case of generating and verifying a Signature Using Generated Keys.
4.2.1. Generating a public-private key pair
Firstly, we will generate a public-private key pair for the algorithm named "DSA" (Digital Signature Algorithm).

KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA"); //get a key pair generator object

keyGen.initialize(2048);  //generate keys with a keysize of 2048

KeyPair pair = keyGen.generateKeyPair(); //generate the key pair

Now, the public-private key pair is generated and let’s go back to the signature generation and verification example.
4.2.2. Generating a Signature

Signature dsa = Signature.getInstance("SHA256withDSA"); //create a signature object
String data = "This is an example";
PrivateKey priv = pair.getPrivate(); 
dsa.initSign(priv);  //Initializing the object with a private key
dsa.update(data);
byte[] sig = dsa.sign();   //update and sign a byte array called data

4.2.3. Verifying a Signature
PublicKey pub = pair.getPublic();
dsa.initVerify(pub);  //Initializing the object with the public key
dsa.update(data); //update the data
boolean verifies = dsa.verify(sig);
System.out.println("signature verifies: " + verifies);  // verify the data 

4.3 Exercise 3
Now I have three public key(encoded) publickey0, publickey1, publickey2, three signatures sig0, sig1, sig2 and three data files data0, data1, data2. Write a program to decide which key, sig and data belongs to the same group. The key and signature use “DSA” algorithm.





Answer of Exercise
https://github.com/linm95/JCA_tutorial/tree/master/src/tutorial

Exercise 1:
https://github.com/linm95/JCA_tutorial/blob/master/src/tutorial/Exercise1.java

Exercise 2:
https://github.com/linm95/JCA_tutorial/blob/master/src/tutorial/Exercise2.java
Exercise 3:
https://github.com/linm95/JCA_tutorial/blob/master/src/tutorial/Exercise3.java

