package nl.topicus.eduarte.entities.adres;

import java.io.Serializable;
import java.util.Date;

public class AdresTypePerDatum implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Date datum;

	private int type;

	public AdresTypePerDatum()
	{
	}

	public Date getDatum()
	{
		return datum;
	}

	public void setDatum(Date datum)
	{
		this.datum = datum;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}
}
