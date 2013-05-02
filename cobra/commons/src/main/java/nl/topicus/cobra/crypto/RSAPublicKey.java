package nl.topicus.cobra.crypto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RSAPublicKey extends AbstractRSAKey<PublicKey> implements IRSAKey<PublicKey>
{
	private static final Logger log = LoggerFactory.getLogger(RSAPublicKey.class);

	private static final String KEY_HEADER = "---- BEGIN RSA PUBLIC KEY ----";

	private static final String KEY_FOOTER = "---- END RSA PUBLIC KEY ----";

	public static final String filename = "public-key.pem";

	public RSAPublicKey(byte[] data)
	{
		super(KEY_HEADER, KEY_FOOTER, data);
	}

	public RSAPublicKey(KeyPair pair)
	{
		this(pair.getPublic().getEncoded());
	}

	public RSAPublicKey(PrivateKey key)
	{
		this(key.getEncoded());
	}

	public byte[] encrypt(byte[] data) throws CobraCryptoException
	{
		try
		{
			Cipher cipher = Cipher.getInstance(RSAKeyPair.ALGORITHM);

			cipher.init(Cipher.ENCRYPT_MODE, toKey());

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

	public boolean verify(byte[] data, byte[] signature) throws CobraCryptoException
	{
		try
		{
			Signature verify = Signature.getInstance("SHA1withRSA");
			verify.initVerify(toKey());
			verify.update(data);

			return verify.verify(signature);
		}
		catch (NoSuchAlgorithmException e)
		{
			log.error(e.getMessage(), e);
			throw new CobraCryptoException(e.getMessage(), e);
		}
		catch (InvalidKeyException e)
		{
			log.error(e.getMessage(), e);
			throw new CobraCryptoException(e.getMessage(), e);
		}
		catch (SignatureException e)
		{
			log.error(e.getMessage(), e);
			throw new CobraCryptoException(e.getMessage(), e);
		}

	}

	@Override
	public PublicKey toKey() throws CobraCryptoException
	{
		try
		{
			KeyFactory factory = KeyFactory.getInstance("RSA");
			return factory.generatePublic(new X509EncodedKeySpec(toByteArray()));
		}
		catch (InvalidKeySpecException e)
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

	public static RSAPublicKey readFromFile(File file) throws IOException
	{
		return readFromStream(new FileInputStream(file));
	}

	public static RSAPublicKey readFromStream(InputStream stream) throws IOException
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

		return new RSAPublicKey(Base64.decode(buffer.toString()));
	}
}
