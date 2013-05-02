package nl.topicus.cobra.crypto;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Key;

public interface IRSAKey<T extends Key>
{
	byte[] toByteArray();

	T toKey() throws CobraCryptoException;

	void toOutputStream(OutputStream stream) throws IOException;
}
