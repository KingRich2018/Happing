package top.happing.utils;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;


public class RsaUtils {

    private static final String ALGORITHM_RSA = "RSA";

    public static KeyPair genKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_RSA);
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
    }

    public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        return executeAlgorithm(Cipher.ENCRYPT_MODE, data, loadPrivateKey(key), null);
    }

    public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {
    	return executeAlgorithm(Cipher.ENCRYPT_MODE, data, null, loadPublicKey(key));
    }
    
    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        return executeAlgorithm(Cipher.DECRYPT_MODE, data, loadPrivateKey(key), null);
    }

    public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {
        return executeAlgorithm(Cipher.DECRYPT_MODE, data, null, loadPublicKey(key));
    }

    private static PrivateKey loadPrivateKey(byte[] key) throws Exception {
        KeySpec keySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory factory = KeyFactory.getInstance(ALGORITHM_RSA);
        return factory.generatePrivate(keySpec);
    }

    private static PublicKey loadPublicKey(byte[] key) throws Exception {
        KeySpec keySpec = new X509EncodedKeySpec(key);
        KeyFactory factory = KeyFactory.getInstance(ALGORITHM_RSA);
        return factory.generatePublic(keySpec);
    }

    private static byte[] executeAlgorithm(int mode, byte[] data, PrivateKey privateKey,
                                           PublicKey publicKey) throws Exception {
        Key key = privateKey == null ? publicKey : privateKey;

        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(mode, key);
        return cipher.doFinal(data);
    }

    private static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCEYC2DhXkikPCTwLbJi+jFtuYJDvP6dA0yQzCkh7yGBQnB6K35Dil4Vhtc1sKBS5/lXvLcVp7HOHH+hAABgocr0NNzHGs120EMu8esmOgzbJgraakImJuR1zO5ZfxznYqRJWeGW32OVtpBfEcdoHarvDmffj5Oc62RVMTb6K8fpwIDAQAB";
    
    private static String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIRgLYOFeSKQ8JPAtsmL6MW25gkO8/p0DTJDMKSHvIYFCcHorfkOKXhWG1zWwoFLn+Ve8txWnsc4cf6EAAGChyvQ03McazXbQQy7x6yY6DNsmCtpqQiYm5HXM7ll/HOdipElZ4ZbfY5W2kF8Rx2gdqu8OZ9+Pk5zrZFUxNvorx+nAgMBAAECgYAn70frxIltxyH8wQejt+SKOQoes3chGH+eqSx0hkp2d/CgogxR9VbHadqpYFSfYuwi60kC9dLCsSGascG6suEKBMCizVJ3Nn/0XBUv6zagqNJB8emc6+9Wsyq4tga0vQ8W2sXJfnEd1WkIQhhDLw+JTkJTY5U5beSTd10LTX5HsQJBAL11R908+DgRqOit6I3YVx1VqMPBLkhINEAobQOJzEYiN9sMfcCiG5AQlR+do7vWPmy12aLUkqwlv1BFcCNHgXkCQQCy3nP9m3B/pyZzCxu8kALZy1ZtdP2uatHR6hZyBOuU8KFcu9tzjSWL2VugwAqV7ke2Jbv3PmvjHDqJZF0Kv4IfAkAutSRDcc2MHSZ0PrOvjJ9ldwHFSPH9FffzvUOq9KTswXDdywWRoSWEbeIlyjpci6QWLvr5vshuMlwpEQVlXTKZAkEAlveuinRxZSydBShxBpu97Dj0/3loy5eY9EVRIHcKpzDZ4L0loceV0w1niqvLw3PTzubWWP20bYFb3q544SN2HQJAayXyDC8f9jiZp+AInuSPB8PfrcbxPW5oIVoDcDWcH0TDndscnqQ8qO0V8Gzet+M3HbFaZ3g3gKG4gN+qD0x+6w==";

    /**
     * 公钥加密明文
     * @param plainText
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String plainText) throws Exception {
    	return Base64.encodeBase64String(encryptByPublicKey(plainText.getBytes(), Base64.decodeBase64(PUBLIC_KEY)));
    }
    
    /**
     * 私钥解密
     * @param encodedText
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String encodedText) throws Exception {
    	return new String(decryptByPrivateKey(Base64.decodeBase64(encodedText), Base64.decodeBase64(PRIVATE_KEY)));
    }
    
//    public static void main(String[] args) throws Exception {
//		KeyPair keyPair = Rsa.genKey();
//		System.out.println(Base64.encodeBase64String(keyPair.getPublic().getEncoded()));
//		System.out.println(Base64.encodeBase64String(keyPair.getPrivate().getEncoded()));
//		String test = Base64.encodeBase64String(Rsa.encryptByPublicKey("123456 abcdef".getBytes(), keyPair.getPublic().getEncoded()));
//		System.out.println(test);
//		test = new String(Rsa.decryptByPrivateKey(Base64.decodeBase64(test), keyPair.getPrivate().getEncoded()));
//		System.out.println(test);
//		
//		String test = RsaUtils.encryptByPublicKey("xxxxxx");
//		System.out.println(test.length());
//		System.out.println(test);
//		test = RsaUtils.decryptByPrivateKey(test);
//		System.out.println(test);
//    }
}
