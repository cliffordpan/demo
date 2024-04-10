package me.hchome.example.utils;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.*;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

/**
 * Wrap java standard security library
 *
 * @author Cliff Pan
 */
public class EncryptionUtils {
	/**
	 * <a href="https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html#timestamp%28%29">
	 * About UUID timestamp
	 * </a>
	 */
	private static final long NUM_UUID_SHIFT_100NS = 0x01b21dd213814000L;
	private static final long NANO_PRE_SECOND = 1000000000L;

	private EncryptionUtils() {
	}


	/**
	 * Generate RSA key pair for saving it into file system
	 *
	 * @param n size of the key 1024, 2048, etc
	 */
	public static KeyPair generateRsaKeyPair(int n) throws NoSuchAlgorithmException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(n, new SecureRandom());
		return generator.generateKeyPair();
	}

	/**
	 * Save a key to destination path, create if not exists
	 *
	 * @param file path of destination
	 * @param type pem object type
	 * @param key  private/public key
	 * @throws IOException if an I/O error occurred
	 */
	public static void saveKey(Path file, String type, Key key) throws IOException {
		try (OutputStream os = Files.newOutputStream(file, StandardOpenOption.CREATE);
			 PemWriter writer = new PemWriter(new OutputStreamWriter(os))) {
			writer.writeObject(new PemObject(type, key.getEncoded()));
			writer.flush();
		}
	}

	/**
	 * Write a key to as a string
	 *
	 * @param type pem object type
	 * @param key  private/public key
	 * @return pem key string
	 * @throws IOException if an I/O error occurred
	 */
	public static String writeKey(String type, Key key) throws IOException {
		try (StringWriter stringWriter = new StringWriter();
			 PemWriter writer = new PemWriter(stringWriter)) {
			writer.writeObject(new PemObject(type, key.getEncoded()));
			writer.flush();
			return stringWriter.toString();
		}
	}


	/**
	 * Read a pem object for file and convert it to private/public key
	 *
	 * @param is    input
	 * @param clazz key class
	 * @param <K>   Key type of read pem object
	 * @throws IOException                   if an I/O error occurred
	 * @throws UnsupportedOperationException if target object isn't a private/public key
	 */
	@SuppressWarnings("unchecked")
	public static <K extends Key> K readKey(InputStream is, Class<K> clazz) throws IOException {
		try (PEMParser reader = new PEMParser(new InputStreamReader(is))) {
			JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
			Object object = reader.readObject();
			if (PrivateKey.class.isAssignableFrom(clazz)) {
				PrivateKeyInfo info = PrivateKeyInfo.getInstance(object);
				return (K) converter.getPrivateKey(info);
			} else if (PublicKey.class.isAssignableFrom(clazz)) {
				SubjectPublicKeyInfo info = SubjectPublicKeyInfo.getInstance(object);
				return (K) converter.getPublicKey(info);
			}
			throw new UnsupportedOperationException("Unsupported pem type: " + clazz.getSimpleName());
		}
	}

	public static Instant extraTimeStamp(UUID uuid) {
		long time = uuid.timestamp() - NUM_UUID_SHIFT_100NS;
		return Instant.ofEpochSecond(time / (NANO_PRE_SECOND / 100), time % (NANO_PRE_SECOND / 100));
	}
}
