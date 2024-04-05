package me.hchome.example.security;

import me.hchome.example.utils.EncryptionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.StringBufferInputStream;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Cliff Pan
 */
public class EncryptionUtilsTests {
	@Test
	@Disabled
	public void generateKeys() throws Exception {
		KeyPair keyPair = EncryptionUtils.generateRsaKeyPair(4096);
		EncryptionUtils.saveKey(Paths.get("./private.key"), "PRIVATE KEY", keyPair.getPrivate());
		EncryptionUtils.saveKey(Paths.get("./public.pub"), "PUBLIC KEY", keyPair.getPublic());
	}

	@Test
	public void write_read_pem_key_test() throws Exception {
		KeyPair keyPair = EncryptionUtils.generateRsaKeyPair(4096);
		String privateKeyStr = EncryptionUtils.writeKey("PRIVATE KEY", keyPair.getPrivate());
		String publicKeyStr = EncryptionUtils.writeKey("PUBLIC KEY", keyPair.getPublic());
		System.err.println(privateKeyStr);
		System.err.println("==========");
		System.err.println(publicKeyStr);
		try(ByteArrayInputStream bis = new ByteArrayInputStream(privateKeyStr.getBytes())) {
			Assertions.assertEquals(keyPair.getPrivate(), EncryptionUtils.readKey(bis, PrivateKey.class));
		}
		try(ByteArrayInputStream bis = new ByteArrayInputStream(publicKeyStr.getBytes())) {
			Assertions.assertEquals(keyPair.getPublic(), EncryptionUtils.readKey(bis, PublicKey.class));
		}

	}
}
