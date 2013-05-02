package nl.topicus.cobra.crypto;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Key;

public abstract class AbstractRSAKey<T extends Key> implements IRSAKey<T>
{

	private final String headerText;

	private final String footerText;

	private final byte[] bytes;

	public AbstractRSAKey(String headerText, String footerText, byte[] bytes)
	{
		this.headerText = headerText;
		this.footerText = footerText;
		this.bytes = bytes;
	}

	@Override
	public byte[] toByteArray()
	{
		return bytes;
	}

	@Override
	public void toOutputStream(OutputStream stream) throws IOException
	{
		stream.write(headerText.getBytes());
		writeNewline(stream);
		stream.write(Base64.encodeBytes(bytes).getBytes());
		writeNewline(stream);
		stream.write(footerText.getBytes());
		writeNewline(stream);
		stream.flush();
	}

	private void writeNewline(OutputStream stream) throws IOException
	{
		stream.write(new byte[] {(byte) 0x0D, (byte) 0x0A});
	}

}
