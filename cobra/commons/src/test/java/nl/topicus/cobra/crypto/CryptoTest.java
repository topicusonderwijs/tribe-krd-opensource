package nl.topicus.cobra.crypto;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

public class CryptoTest
{
	private static RSAKeyPair[] pairs;

	private final static String before = "Teddy rules!";

	@BeforeClass
	public static void generatePair() throws CobraCryptoException
	{
		pairs = new RSAKeyPair[KeyLength.values().length];
		int i = 0;

		for (KeyLength length : KeyLength.values())
		{
			pairs[i++] = RSAKeyPair.generate(length);
		}
	}

	@Test
	public void encryptDecrypt() throws CobraCryptoException
	{
		int i = 0;
		for (KeyLength length : KeyLength.values())
		{
			RSAKeyPair pair = pairs[i++];
			byte[] encrypted = pair.getPublicKey().encrypt(before.getBytes());
			String after = new String(pair.getPrivateKey().decrypt(encrypted));

			assertEquals("Encryption/decryption with key length " + length.getKeyLength(), before,
				after);

		}

	}

	@Test
	public void signVerify() throws CobraCryptoException
	{
		int i = 0;
		for (KeyLength length : KeyLength.values())
		{
			RSAKeyPair pair = pairs[i++];
			byte[] signature = pair.getPrivateKey().sign(before.getBytes());
			assertTrue("Sign/verify with key length " + length.getKeyLength(), pair.getPublicKey()
				.verify(before.getBytes(), signature));
		}
	}

	@Test
	public void writeReadKeyPairZip() throws IOException, CobraCryptoException
	{
		int i = 0;
		for (KeyLength length : KeyLength.values())
		{
			RSAKeyPair pair = pairs[i++];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			pair.writeToStream(baos);
			byte[] data = baos.toByteArray();

			ByteArrayInputStream bios = new ByteArrayInputStream(data);
			RSAKeyPair keypair = RSAKeyPair.readFromStream(bios);

			assertArrayEquals("Private key file for  " + length.getKeyLength() + " bits key", pair
				.getPrivateKey().toByteArray(), keypair.getPrivateKey().toByteArray());
			assertArrayEquals("Public key file for " + length.getKeyLength() + " bits key", pair
				.getPublicKey().toByteArray(), keypair.getPublicKey().toByteArray());
		}

	}
}
