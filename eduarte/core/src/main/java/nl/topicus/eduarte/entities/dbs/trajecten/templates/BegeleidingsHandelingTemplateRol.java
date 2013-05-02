package nl.topicus.eduarte.entities.dbs.trajecten.templates;

/**
 * @author maatman
 */
public enum BegeleidingsHandelingTemplateRol
{
	EersteUitvoerende
	{
		@Override
		public String toString()
		{
			return "Eerste uitvoerende";
		}
	},
	GeselecteerdePersoon
	{
		@Override
		public String toString()
		{
			return "Geselecteerde persoon";
		}

	},
	Mentor,
	Verantwoordelijke;

}
