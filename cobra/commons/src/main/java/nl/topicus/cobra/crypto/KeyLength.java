package nl.topicus.cobra.crypto;

public enum KeyLength
{
	Bits1024(1024),
	Bits2048(2048),
	Bits4096(4096);

	private final int keyLength;

	private KeyLength(int keyLength)
	{
		this.keyLength = keyLength;
	}

	public int getKeyLength()
	{
		return keyLength;
	}
}
