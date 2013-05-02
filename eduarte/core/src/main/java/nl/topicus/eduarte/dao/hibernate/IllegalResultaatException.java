package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;

public class IllegalResultaatException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	private Resultaat resultaat;

	public IllegalResultaatException(String message)
	{
		super(message);
		this.resultaat = null;
	}

	public IllegalResultaatException(Resultaat resultaat, String message)
	{
		super(constructMessage(resultaat, message));
		this.resultaat = resultaat;
	}

	public IllegalResultaatException(Resultaat resultaat, String message, Throwable cause)
	{
		super(constructMessage(resultaat, message), cause);
		this.resultaat = resultaat;
	}

	private static String constructMessage(Resultaat resultaat, String message)
	{
		return resultaat + " : " + message;
	}

	public Resultaat getResultaat()
	{
		return resultaat;
	}
}
