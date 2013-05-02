package nl.topicus.eduarte.entities;

import java.util.Date;

public interface IBronStatusEntiteit
{
	public void setBronStatus(BronEntiteitStatus bronStatus);

	public BronEntiteitStatus getBronStatus();

	public void setBronDatum(Date bronDatum);

	public Date getBronDatum();
}
