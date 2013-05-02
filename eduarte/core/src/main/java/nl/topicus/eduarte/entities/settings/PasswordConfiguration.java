package nl.topicus.eduarte.entities.settings;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import nl.topicus.cobra.web.components.form.AutoForm;

@Embeddable
public class PasswordConfiguration implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Column(name = "lengte", nullable = true)
	@AutoForm(label = "Minimum lengte", required = true)
	private int lengte;

	@Column(name = "hoofdletters", nullable = true)
	@AutoForm(label = "Hoofd- en kleine letters", required = true)
	private boolean hoofdletters;

	@Column(name = "leestekens", nullable = true)
	@AutoForm(label = "Leestekens", required = true)
	private boolean leestekens;

	public PasswordConfiguration()
	{
	}

	public int getLengte()
	{
		return lengte;
	}

	public void setLengte(int lengte)
	{
		this.lengte = lengte;
	}

	public boolean getHoofdletters()
	{
		return hoofdletters;
	}

	public void setHoofdletters(boolean hoofdletters)
	{
		this.hoofdletters = hoofdletters;
	}

	public boolean getLeestekens()
	{
		return leestekens;
	}

	public void setLeestekens(boolean leestekens)
	{
		this.leestekens = leestekens;
	}

	/**
	 * Contreleerd of het wachtwoord aan alle eisen voldoet
	 * 
	 * @param password
	 *            het plaintext wachtwoord
	 * @return true als het wachtwoord voldoet, anders false.
	 */
	public boolean validatePassword(String password)
	{
		String validateMe = password;
		if (validateMe == null)
			validateMe = "";
		validateMe = validateMe.trim(); // strip whitespace

		if (validateMe.length() < getLengte())
			return false;

		boolean letters = false;
		boolean otherChars = false;
		boolean upperCase = false;
		boolean lowerCase = false;
		int cp;
		for (int i = 0; i < validateMe.length(); i++)
		{
			cp = validateMe.codePointAt(i);
			if (Character.isUpperCase(cp))
				upperCase = true;
			else if (Character.isLowerCase(cp))
				lowerCase = true;
			if (Character.isLetter(cp))
				letters = true;
			else
				otherChars = true;
		}

		if (getHoofdletters())
		{
			if (!(upperCase && lowerCase))
				return false;
		}
		if (getLeestekens())
		{
			if (!(letters && otherChars))
				return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if (getLengte() > 0)
			sb.append("minimaal " + lengte + " tekens");
		if (getHoofdletters())
		{
			if (sb.length() > 0)
				sb.append(", ");
			sb.append("minimaal een hoofd- en kleine letter");
		}
		if (getLeestekens())
		{
			if (sb.length() > 0)
				sb.append(", ");
			sb.append("minimaal een leesteken");
		}
		return sb.length() == 0 ? "Geen eisen" : sb.toString();
	}
}
