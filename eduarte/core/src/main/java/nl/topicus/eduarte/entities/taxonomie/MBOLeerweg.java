package nl.topicus.eduarte.entities.taxonomie;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;

/**
 * Leerweg in het MBO: BOL/BBL
 * 
 * @author loite
 */
@Exportable
public enum MBOLeerweg
{
	BOL("Beroepsopleidende leerweg"),
	BBL("Beroepsbegeleidende leerweg"),
	COL("Combinatie beroepsopleidende leerweg"),
	CBL("Combinatie beroepsbegeleidende leerweg");

	private final String omschrijving;

	private MBOLeerweg(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	/**
	 * @return Returns the omschrijving.
	 */
	@Exportable
	public String getOmschrijving()
	{
		return omschrijving;
	}

	@Exportable
	public String getOmschrijvingLC()
	{
		return omschrijving.toLowerCase();
	}

	public static MBOLeerweg parse(String leerweg)
	{
		if (StringUtil.isNotEmpty(leerweg))
		{
			for (MBOLeerweg lw : MBOLeerweg.values())
			{
				if (lw.name().startsWith(leerweg))
				{
					return lw;
				}
			}
		}
		return null;
	}

}
