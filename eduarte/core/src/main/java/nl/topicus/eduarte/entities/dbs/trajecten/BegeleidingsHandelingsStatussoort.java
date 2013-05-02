package nl.topicus.eduarte.entities.dbs.trajecten;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum BegeleidingsHandelingsStatussoort
{
	Inplannen,
	Uitvoeren,
	Bespreken,
	Voltooid
	{
		@Override
		public boolean isEindStatus()
		{
			return true;
		}
	},
	Geannuleerd
	{
		@Override
		public boolean isEindStatus()
		{
			return true;
		}
	};

	public boolean isEindStatus()
	{
		return false;
	}

	private static final List<BegeleidingsHandelingsStatussoort> EINDSTATUSSEN =
		new ArrayList<BegeleidingsHandelingsStatussoort>(2);

	private static final List<BegeleidingsHandelingsStatussoort> EINDSTATUSSEN_UNMODIFIABLE =
		Collections.unmodifiableList(EINDSTATUSSEN);

	static
	{
		for (BegeleidingsHandelingsStatussoort status : BegeleidingsHandelingsStatussoort.values())
		{
			if (status.isEindStatus())
			{
				EINDSTATUSSEN.add(status);
			}
		}
	}

	/**
	 * @return Alle eindstatussen
	 */
	public static List<BegeleidingsHandelingsStatussoort> getEindStatussen()
	{
		return EINDSTATUSSEN_UNMODIFIABLE;
	}
}
