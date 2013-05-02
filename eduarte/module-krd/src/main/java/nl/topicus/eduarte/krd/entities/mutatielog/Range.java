package nl.topicus.eduarte.krd.entities.mutatielog;

import java.io.Serializable;

public class Range implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Integer from;

	private Integer to;

	public Integer getFrom()
	{
		return from;
	}

	public void setFrom(Integer from)
	{
		this.from = from;
	}

	public Integer getTo()
	{
		return to;
	}

	public void setTo(Integer to)
	{
		this.to = to;
	}
}
