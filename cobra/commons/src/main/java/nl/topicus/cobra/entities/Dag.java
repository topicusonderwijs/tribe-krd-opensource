/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.util.TimeUtil;

/**
 * @author loite
 */
public enum Dag
{
	Maandag
	{
		@Override
		public int getDagNummer()
		{
			return 1;
		}
	},
	Dinsdag
	{
		@Override
		public int getDagNummer()
		{
			return 2;
		}
	},
	Woensdag
	{
		@Override
		public int getDagNummer()
		{
			return 3;
		}
	},
	Donderdag
	{
		@Override
		public int getDagNummer()
		{
			return 4;
		}
	},
	Vrijdag
	{
		@Override
		public int getDagNummer()
		{
			return 5;
		}
	},
	Zaterdag
	{
		@Override
		public int getDagNummer()
		{
			return 6;
		}
	},
	Zondag
	{
		@Override
		public int getDagNummer()
		{
			return 0;
		}
	};

	/**
	 * Geeft het dagnummer zoals dit in de meeste importbestanden e.d. wordt gebruikt.
	 * Maandag = 1, Dinsdag = 2 etc.
	 * 
	 */
	public abstract int getDagNummer();

	/**
	 * Geeft een afkorting van de gegeven dag. Bijv: Ma, Di, Wo, Do, Vr, Za, Zo.
	 */
	public String getAfkorting()
	{
		return toString().substring(0, 2);
	}

	/**
	 * Geeft de dag met het gegeven nummer.
	 */
	public static Dag getDag(int nummer)
	{
		for (Dag dag : Dag.values())
		{
			if (dag.getDagNummer() == nummer)
			{
				return dag;
			}
		}
		return null;
	}

	/**
	 * Geeft de weekdag van de gegeven datum.
	 */
	public static Dag getDag(Date date)
	{
		return getDag(TimeUtil.getInstance().getDayOfWeek(date) - 1);
	}

	private static final List<Dag> werkDagen = new ArrayList<Dag>(5);
	static
	{
		werkDagen.add(Maandag);
		werkDagen.add(Dinsdag);
		werkDagen.add(Woensdag);
		werkDagen.add(Donderdag);
		werkDagen.add(Vrijdag);
	}

	/**
	 * Geeft een lijst met alle werkdagen (Ma t/m Vr).
	 */
	public static List<Dag> getWerkDagen()
	{
		return werkDagen;
	}

}
