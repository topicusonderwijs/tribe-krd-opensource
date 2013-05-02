package nl.topicus.cobra.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RSAKeyPair
{
	private static final Logger log = LoggerFactory.getLogger(RSAKeyPair.class);

	public static final String ALGORITHM = "RSA/ECB/PKCS1Padding";

	private final RSAPublicKey publicKey;

	private final RSAPrivateKey privateKey;

	private RSAKeyPair(KeyPair pair)
	{
		this(new RSAPublicKey(pair), new RSAPrivateKey(pair));
	}

	private RSAKeyPair(RSAPublicKey publicKey, RSAPrivateKey privateKey)
	{
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	public RSAPublicKey getPublicKey()
	{
		return publicKey;
	}

	public RSAPrivateKey getPrivateKey()
	{
		return privateKey;
	}

	public void writeToFile(File file) throws IOException
	{
		writeToStream(new FileOutputStream(file));
	}

	public void writeToStream(OutputStream stream) throws IOException
	{
		ZipOutputStream zipStream = new ZipOutputStream(stream);

		ZipEntry entry = new ZipEntry(RSAPublicKey.filename);
		zipStream.putNextEntry(entry);
		publicKey.toOutputStream(zipStream);
		zipStream.closeEntry();

		entry = new ZipEntry(RSAPrivateKey.filename);
		zipStream.putNextEntry(entry);
		privateKey.toOutputStream(zipStream);
		zipStream.closeEntry();
		zipStream.closeEntry();

		zipStream.flush();
		zipStream.close();
		stream.flush();
		stream.close();
	}

	public static RSAKeyPair readFromStream(InputStream stream) throws IOException,
			CobraCryptoException
	{
		ZipInputStream zipStream = new ZipInputStream(stream);

		ZipEntry entry;

		RSAPrivateKey privateKey = null;

		RSAPublicKey publicKey = null;

		while ((entry = zipStream.getNextEntry()) != null)
		{
			if (entry.getName().equals(RSAPublicKey.filename))
			{
				publicKey = RSAPublicKey.readFromStream(zipStream);
			}
			else if (entry.getName().equals(RSAPrivateKey.filename))
			{
				privateKey = RSAPrivateKey.readFromStream(zipStream);
			}

		}

		zipStream.close();

		if (privateKey == null || publicKey == null)
		{
			throw new CobraCryptoException("Stream does not contain both files "
				+ RSAPrivateKey.filename + " and " + RSAPublicKey.filename);
		}

		return new RSAKeyPair(publicKey, privateKey);
	}

	public static RSAKeyPair readFromFile(File file) throws IOException, CobraCryptoException
	{
		return readFromStream(new FileInputStream(file));
	}

	public static RSAKeyPair generate(KeyLength length) throws CobraCryptoException
	{
		try
		{
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

			AlgorithmParameterSpec spec =
				new RSAKeyGenParameterSpec(length.getKeyLength(), RSAKeyGenParameterSpec.F0);

			keyGen.initialize(spec);

			return new RSAKeyPair(keyGen.generateKeyPair());
		}
		catch (InvalidAlgorithmParameterException e)
		{
			log.error(e.getMessage(), e);
			throw new CobraCryptoException(e.getMessage(), e);
		}
		catch (NoSuchAlgorithmException e)
		{
			log.error(e.getMessage(), e);
			throw new CobraCryptoException(e.getMessage(), e);
		}
	}

}