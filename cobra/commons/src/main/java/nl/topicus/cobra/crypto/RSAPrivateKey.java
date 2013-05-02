package nl.topicus.cobra.crypto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RSAPrivateKey extends AbstractRSAKey<PrivateKey> implements IRSAKey<PrivateKey>
{
	private static final Logger log = LoggerFactory.getLogger(RSAPrivateKey.class);

	private static final String KEY_HEADER = "---- BEGIN RSA PRIVATE KEY ----";

	private static final String KEY_FOOTER = "---- END RSA PRIVATE KEY ----";

	public static final String filename = "private-key.pem";

	public RSAPrivateKey(byte[] data)
	{
		super(KEY_HEADER, KEY_FOOTER, data);
	}

	RSAPrivateKey(KeyPair pair)
	{
		this(pair.getPrivate().getEncoded());
	}

	RSAPrivateKey(PrivateKey key)
	{
		this(key.getEncoded());
	}

	public byte[] decrypt(byte[] data) throws CobraCryptoException
	{
		try
		{
			Cipher cipher = Cipher.getInstance(RSAKeyPair.ALGORITHM);

			cipher.init(Cipher.DECRYPT_MODE, toKey());

			return cipher.doFinal(data);
		}
		catch (InvalidKeyException e)
		{
			log.error(e.getMessage(), e);
			throw new CobraCryptoException(e.getMessage(), e);
		}
		catch (NoSuchAlgorithmException e)
		{
			log.error(e.getMessage(), e);
			throw new CobraCryptoException(e.getMessage(), e);
		}
		catch (NoSuchPaddingException e)
		{
			log.error(e.getMessage(), e);
			throw new CobraCryptoException(e.getMessage(), e);
		}
		catch (IllegalBlockSizeException e)
		{
			log.error(e.getMessage(), e);
			throw new CobraCryptoException(e.getMessage(), e);
		}
		catch (BadPaddingException e)
		{
			log.error(e.getMessage(), e);
			throw new CobraCryptoException(e.getMessage(), e);
		}
	}

	public byte[] sign(byte[] data) throws CobraCryptoException
	{
		try
		{
			Signature sig = Signature.getInstance("SHA1withRSA");

			sig.initSign(toKey());

			sig.update(data);

			return sig.sign();
		}
		catch (NoSuchAlgorithmException e)
		{
			log.error(e.getMessage(), e);
			throw new CobraCryptoException(e.getMessage(), e);
		}

		catch (SignatureException e)
		{
			log.error(e.getMessage(), e);
			throw new CobraCryptoException(e.getMessage(), e);
		}
		catch (InvalidKeyException e)
		{
			log.error(e.getMessage(), e);
			throw new CobraCryptoException(e.getMessage(), e);
		}
	}

	@Override
	public PrivateKey toKey() throws CobraCryptoException
	{
		try
		{
			KeyFactory factory = KeyFactory.getInstance("RSA");
			return factory.generatePrivate(new PKCS8EncodedKeySpec(toByteArray()));
		}
		catch (NoSuchAlgorithmException e)
		{
			log.error(e.getMessage(), e);
			throw new CobraCryptoException(e.getMessage(), e);
		}
		catch (InvalidKeySpecException e)
		{
			log.error(e.getMessage(), e);
			throw new CobraCryptoException(e.getMessage(), e);
		}

	}

	public static RSAPrivateKey readFromFile(File file) throws IOException
	{
		return readFromStream(new FileInputStream(file));
	}

	public static RSAPrivateKey readFromStream(InputStream stream) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder buffer = new StringBuilder();

		String data = reader.readLine();
		while (data != null && !data.equals(KEY_FOOTER))
		{
			if (!data.equals(KEY_HEADER))
			{
				buffer.append(data);
			}
			data = reader.readLine();
		}

		return new RSAPrivateKey(Base64.decode(buffer.toString()));
	}
}
