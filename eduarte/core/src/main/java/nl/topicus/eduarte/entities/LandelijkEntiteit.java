package nl.topicus.eduarte.entities;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class LandelijkEntiteit extends Entiteit
{
	private static final long serialVersionUID = 1L;

	public LandelijkEntiteit()
	{
	}

	@Override
	public final boolean isLandelijk()
	{
		return true;
	}
}
