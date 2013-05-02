package nl.topicus.eduarte.entities.dbs.trajecten.templates;

import nl.topicus.cobra.util.StringUtil;

/**
 * @author maatman
 */
public enum AanwezigeType
{
	Mentor,
	EersteUitvoerende,
	AlleUitvoerenden,
	Verantwoordelijke,
	Deelnemer,
	OudersVerzorgers
	{
		@Override
		public String toString()
		{
			return "Ouders/verzorgers";
		}
	},
	GeselecteerdePersoon;

	@Override
	public String toString()
	{
		return StringUtil.convertCamelCase(name());
	}
}
