/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.security.authorization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import nl.topicus.cobra.util.StringUtil;

/**
 * Hierarchy for Principals. This determines globally what you can do, add new users,
 * perform application maintenance, administrative tasks etc. The lower your level the
 * less you can do. Being super user however does not mean you can do all the tasks.
 * 
 * @author marrink
 */
@XmlType
@XmlEnum
public enum AuthorisatieNiveau
{
	/**
	 * performs tasks on users with level APPLICATIE. Always at least 1 super user.
	 */
	@XmlEnumValue("super")
	SUPER(2),
	/**
	 * user management (not including SUPER users).
	 */
	@XmlEnumValue("applicatie")
	APPLICATIE(1),
	/**
	 * regular users.
	 */
	@XmlEnumValue("rest")
	REST(0);
	private int level;

	private AuthorisatieNiveau(int level)
	{
		this.level = level;
	}

	/**
	 * Het niveau, hoe lager hoe minder rechten.
	 * 
	 * @return het niveau
	 */
	public int niveau()
	{
		return level;
	}

	/**
	 * Returns a List of AuthorisatieNiveaus who's niveau/level is below this niveau/level
	 * 
	 * @return list
	 */
	public List<AuthorisatieNiveau> implied()
	{
		List<AuthorisatieNiveau> all = Arrays.asList(AuthorisatieNiveau.values());
		List<AuthorisatieNiveau> implied = new ArrayList<AuthorisatieNiveau>();
		for (AuthorisatieNiveau niveau2 : all)
		{
			if (niveau2.niveau() < niveau())
				implied.add(niveau2);
		}
		return implied;
	}

	/**
	 * Checks if this AuthorisatieNiveau implies the other. In effect this checks if the
	 * level of the other is smaller then this one.
	 * 
	 * @param other
	 * @return true if this one implies the other
	 */
	public boolean implies(AuthorisatieNiveau other)
	{
		return other.level < level;
	}

	/**
	 * Returns a List of AuthorisatieNiveaus who's niveau/level is at or below this
	 * niveau/level
	 * 
	 * @return list
	 */
	public List<AuthorisatieNiveau> sameNiveauAndLower()
	{
		List<AuthorisatieNiveau> all = Arrays.asList(AuthorisatieNiveau.values());
		List<AuthorisatieNiveau> implied = new ArrayList<AuthorisatieNiveau>();
		for (AuthorisatieNiveau niveau2 : all)
		{
			if (niveau2.niveau() <= niveau())
				implied.add(niveau2);
		}
		return implied;
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		return StringUtil.firstCharUppercase(name());
	}

}
