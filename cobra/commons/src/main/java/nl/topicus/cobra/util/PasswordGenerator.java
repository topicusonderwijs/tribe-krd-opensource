package nl.topicus.cobra.util;

/**
 * Copyright (c) Ian F. Darwin, http://www.darwinsys.com/, 1996-2002. All rights reserved.
 * Software written by Ian F. Darwin and others. $Id: LICENSE,v 1.8 2004/02/09 03:33:38
 * ian Exp $
 * 
 * @author loite
 */
public class PasswordGenerator
{
	/** The random number generator. */
	private static java.util.Random r = new java.util.Random();

	/*
	 * Set of characters that is valid. Must be printable, memorable, and "won't break
	 * HTML" (i.e., not ' <', '>', '&', '=', ...). or break shell commands (i.e., not '
	 * <', '>', '$', '!', ...). I, L and O are good to leave out, as are numeric zero and
	 * one.
	 */
	private static char[] goodChar =
		{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7',
			'8', '9', '+', '-', '@',};

	/**
	 * Generate a random password
	 * 
	 * @param length
	 * @return a pseudo-random password
	 */
	public static String generatePassword(int length)
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++)
		{
			sb.append(goodChar[r.nextInt(goodChar.length)]);
		}
		return sb.toString();
	}

}
