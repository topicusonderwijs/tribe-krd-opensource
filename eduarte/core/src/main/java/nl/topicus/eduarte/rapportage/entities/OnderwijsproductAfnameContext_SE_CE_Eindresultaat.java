package nl.topicus.eduarte.rapportage.entities;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.ResultaatVrijstelling;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

/**
 * Temp entiteit voor het SE-, CE- en eindresultaat van een onderwijsproductafnamecontext.
 * 
 * @author loite
 * 
 */
@Exportable
public class OnderwijsproductAfnameContext_SE_CE_Eindresultaat
{
	private final OnderwijsproductAfnameContext keuze;

	private final Resultaat seResultaat;

	private final Resultaat ceResultaat;

	private Resultaat cseResultaat;

	private Resultaat cspe1Resultaat;

	private String cspe1VakCode;

	private Resultaat cspe2Resultaat;

	private String cspe2VakCode;

	private final Resultaat eindresultaat;

	public OnderwijsproductAfnameContext_SE_CE_Eindresultaat(OnderwijsproductAfnameContext keuze,
			Resultaat seResultaat, Resultaat ceResultaat, Resultaat eindresultaat)
	{
		this.keuze = keuze;

		if (keuze.getOnderwijsproductAfname().getVrijstellingType().isVrijstelling())
		{
			this.ceResultaat = null;
			this.seResultaat = null;
			this.eindresultaat = new ResultaatVrijstelling(keuze.getOnderwijsproductAfname());
		}
		else
		{
			this.seResultaat = seResultaat;
			this.ceResultaat = ceResultaat;
			this.eindresultaat = eindresultaat;

			if (ceResultaat != null && ceResultaat.getToets().getChildren().size() > 0)
			{
				ResultaatDataAccessHelper resultaatHelper =
					DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class);

				for (Toets cechild : ceResultaat.getToets().getChildren())
				{
					if (cechild.getCode().equalsIgnoreCase("cse"))
						cseResultaat =
							resultaatHelper.getGeldendeResultaat(cechild, keuze.getVerbintenis()
								.getDeelnemer());
					else
					{
						Toets cspe =
							getCSPE1ToetsMetResultaten(cechild, keuze.getVerbintenis()
								.getDeelnemer());

						if (cspe != null)
						{
							if (cspe1Resultaat == null)
							{
								cspe1Resultaat =
									resultaatHelper.getGeldendeResultaat(cechild, keuze
										.getVerbintenis().getDeelnemer());

								cspe1VakCode = cspe.getCode().substring(5).trim();

							}
							else if (cspe2Resultaat == null)
							{
								cspe2Resultaat =
									resultaatHelper.getGeldendeResultaat(cechild, keuze
										.getVerbintenis().getDeelnemer());

								cspe2VakCode = cspe.getCode().substring(5).trim();
							}
						}
					}
				}
			}
		}
	}

	private Toets getCSPE1ToetsMetResultaten(Toets toets, Deelnemer deelnemer)
	{
		if (StringUtil.startsWithIgnoreCase(toets.getCode(), "cspe1")
			&& toets.getHeeftResultaten(deelnemer))
			return toets;
		else
		{
			for (Toets subToets : toets.getChildren())
			{
				if (getCSPE1ToetsMetResultaten(subToets, deelnemer) != null)
					return subToets;
			}
			return null;
		}
	}

	@Exportable
	public String getCijferlijstSoortProductregelTekst()
	{
		if (keuze != null && keuze.getProductregel().getVolgnummer() == 1)
			return keuze.getProductregel().getSoortProductregel().getNaam();
		return "";
	}

	@Exportable
	public String getCijferlijstSoortProductregelDiplomanaamTekst()
	{
		if (keuze != null && keuze.getProductregel().getVolgnummer() == 1)
			return keuze.getProductregel().getSoortProductregel().getDiplomanaam();
		return "";
	}

	@Exportable
	public OnderwijsproductAfnameContext getKeuze()
	{
		return keuze;
	}

	@Exportable
	public Resultaat getSeResultaat()
	{
		return seResultaat;
	}

	@Exportable
	public Resultaat getCeResultaat()
	{
		return ceResultaat;
	}

	@Exportable
	public Resultaat getCseResultaat()
	{
		return cseResultaat;
	}

	@Exportable
	public Resultaat getEindresultaat()
	{
		return eindresultaat;
	}

	@Exportable
	public Resultaat getCspe1Resultaat()
	{
		return cspe1Resultaat;
	}

	@Exportable
	public Resultaat getCspe2Resultaat()
	{
		return cspe2Resultaat;
	}

	@Exportable
	public String getCspe1VakCode()
	{
		return cspe1VakCode;
	}

	@Exportable
	public String getCspe2VakCode()
	{
		return cspe2VakCode;
	}

}
