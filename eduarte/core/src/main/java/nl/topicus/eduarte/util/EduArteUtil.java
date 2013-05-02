package nl.topicus.eduarte.util;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.OrganisatieDataAccessHelper;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.adres.AdresEntiteit;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.kenmerk.DeelnemerKenmerk;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.organisatie.Beheer;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;

public class EduArteUtil
{
	/**
	 * Organisatie codes voor het kunnen identificeren van een individuele organisatie in
	 * bijvoorbeeld update tasks.
	 */
	public enum OrganisatieCode
	{
		ALBEDACOLLEGE("00GT"),
		AMARANTIS_ONDERWIJSGROEP("04EM"),
		AOC_FRIESLAND("12VI"),
		ARCUSCOLLEGE("25PU"),
		CITAVERDE("21CS"),
		CLUSIUSCOLLEGE("25EF"),
		DA_VINCICOLLEGE("20MQ"),
		DELTIONCOLLEGE("25PJ"),
		EDUCUSCOLLEGE("01AA"),
		HOORNBEECK_COLLEGE("09MR"),
		HORIZONCOLLEGE("25PT"),
		ID_COLLEGE("25LN"),
		ROC_A12("25PM"),
		ROC_AVENTUS("27DV"),
		ROC_LEIDEN("25MA"),
		ROC_RIVOR("04CY"),
		SYSTEEMBEHEER(""),
		WELLANTCOLLEGE("01OE");

		private final String brin;

		private OrganisatieCode(String brin)
		{
			this.brin = brin;
		}

		public boolean isOrganisatie(Organisatie organisatie)
		{
			if (organisatie instanceof Instelling)
			{
				Brin organisatieCode = ((Instelling) organisatie).getBrincode();
				return organisatieCode != null && brin.equalsIgnoreCase(organisatieCode.getCode());
			}
			else if (organisatie instanceof Beheer)
			{
				return brin.isEmpty();
			}
			return false;
		}
	}

	/**
	 * Haalt de specifieke instelling/organisatie op behorende bij de organisatieCode. De
	 * lookup geschiedt aan de hand van de BRIN code en is dus veilig over de
	 * verschillende straten heen.
	 * 
	 * @return <code>null</code> wanneer de organisatie niet in de database gevonden kon
	 *         worden.
	 */
	public static Organisatie getInstelling(OrganisatieCode organisatieCode)
	{
		OrganisatieDataAccessHelper helper =
			DataAccessRegistry.getHelper(OrganisatieDataAccessHelper.class);
		List<Organisatie> organisaties = helper.list(Organisatie.class);
		for (Organisatie organisatie : organisaties)
		{
			if (organisatieCode.isOrganisatie(organisatie))
				return organisatie;
		}
		return null;
	}

	/**
	 * Probeert een deelnemer te achterhalen op basis van de tabelnaam en object id.
	 */
	public static Deelnemer getDeelnemer(String tabel, Long id)
	{
		Entiteit entiteit = get(tabel, id);

		if (entiteit == null)
			return null;

		Deelnemer deelnemer = getDeelnemer(entiteit);
		return deelnemer;
	}

	@SuppressWarnings("unchecked")
	public static Entiteit get(String tabel, Long id)
	{
		BatchDataAccessHelper<Entiteit> helper =
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
		if ("DEELNEMER".equalsIgnoreCase(tabel))
		{
			return helper.get(Deelnemer.class, id);
		}
		if ("PERSOON".equalsIgnoreCase(tabel))
		{
			return helper.get(Persoon.class, id);
		}
		if ("DEELNEMERKENMERK".equalsIgnoreCase(tabel))
		{
			return helper.get(DeelnemerKenmerk.class, id);
		}
		if ("ADRES".equalsIgnoreCase(tabel))
		{
			return helper.get(Adres.class, id);
		}
		if ("ADRESENTITEIT".equalsIgnoreCase(tabel))
		{
			return helper.get(AdresEntiteit.class, id);
		}
		if ("PERSOONCONTACTGEGEVEN".equalsIgnoreCase(tabel))
		{
			return helper.get(PersoonContactgegeven.class, id);
		}
		throw new IllegalArgumentException("Onbekende tabel " + tabel
			+ ", weet niet hoe daar een entiteit van te maken");
	}

	/**
	 * Bepaalt de bij de entity behorende deelnemer (of als de entity zelf een deelnemer
	 * is, de entity) door in het EduArte object model terug te navigeren naar de
	 * Deelnemer.
	 * <p>
	 * <h3>Voorbeeld</h3>
	 * <p>
	 * getDeelnemer(verbintenis) zal aan de verbintenis de deelnemer opvragen.
	 */
	public static Deelnemer getDeelnemer(Object entity)
	{
		if (entity instanceof Deelnemer)
		{
			return (Deelnemer) entity;
		}
		if (entity instanceof Adres)
		{
			return getDeelnemer((Adres) entity);
		}
		if (entity instanceof PersoonAdres)
		{
			return getDeelnemer((PersoonAdres) entity);
		}
		if (entity instanceof PersoonContactgegeven)
		{
			return getDeelnemer((PersoonContactgegeven) entity);
		}
		if (entity instanceof Persoon)
		{
			return getDeelnemer((Persoon) entity);
		}
		if (entity instanceof DeelnemerKenmerk)
		{
			return getDeelnemer((DeelnemerKenmerk) entity);
		}
		if (entity instanceof Verbintenis)
		{
			return getDeelnemer((Verbintenis) entity);
		}
		if (entity instanceof Plaatsing)
		{
			return getDeelnemer((Plaatsing) entity);
		}
		if (entity instanceof Bekostigingsperiode)
		{
			return getDeelnemer((Bekostigingsperiode) entity);
		}
		if (entity instanceof BPVInschrijving)
		{
			return getDeelnemer((BPVInschrijving) entity);
		}
		if (entity instanceof Examendeelname)
		{
			return getDeelnemer((Examendeelname) entity);
		}
		if (entity instanceof OnderwijsproductAfname)
		{
			return getDeelnemer((OnderwijsproductAfname) entity);
		}
		if (entity instanceof OnderwijsproductAfnameContext)
		{
			return getDeelnemer((OnderwijsproductAfnameContext) entity);
		}
		if (entity instanceof Resultaat)
		{
			return getDeelnemer((Resultaat) entity);
		}
		if (entity instanceof Vooropleiding)
		{
			return getDeelnemer((Vooropleiding) entity);
		}
		if (entity instanceof Collection< ? >)
		{
			Collection< ? > collection = (Collection< ? >) entity;
			for (Object object : collection)
			{
				Deelnemer deelnemer = getDeelnemer(object);
				if (deelnemer != null)
					return deelnemer;
			}
		}
		return null;
	}

	private static Deelnemer getDeelnemer(Adres adres)
	{
		if (adres == null)
		{
			return null;
		}
		return getDeelnemer(adres.getPersoonAdressen());
	}

	private static Deelnemer getDeelnemer(PersoonAdres persoonAdres)
	{
		if (persoonAdres == null)
		{
			return null;
		}
		return getDeelnemer(persoonAdres.getPersoon());
	}

	private static Deelnemer getDeelnemer(Persoon persoon)
	{
		if (persoon == null)
		{
			return null;
		}
		return persoon.getDeelnemer();
	}

	private static Deelnemer getDeelnemer(PersoonContactgegeven contactgegeven)
	{
		if (contactgegeven == null)
			return null;
		return getDeelnemer(contactgegeven.getPersoon());
	}

	private static Deelnemer getDeelnemer(DeelnemerKenmerk kenmerk)
	{
		if (kenmerk == null)
			return null;
		return kenmerk.getDeelnemer();
	}

	private static Deelnemer getDeelnemer(Verbintenis verbintenis)
	{
		if (verbintenis == null)
		{
			return null;
		}
		return verbintenis.getDeelnemer();
	}

	private static Deelnemer getDeelnemer(Vooropleiding vooropleiding)
	{
		if (vooropleiding == null)
		{
			return null;
		}
		return vooropleiding.getDeelnemer();
	}

	private static Deelnemer getDeelnemer(Plaatsing plaatsing)
	{
		if (plaatsing == null)
		{
			return null;
		}
		return plaatsing.getVerbintenis().getDeelnemer();
	}

	private static Deelnemer getDeelnemer(Bekostigingsperiode periode)
	{
		if (periode == null)
		{
			return null;
		}
		return getDeelnemer(periode.getVerbintenis());
	}

	private static Deelnemer getDeelnemer(BPVInschrijving bpv)
	{
		if (bpv == null)
		{
			return null;
		}
		return getDeelnemer(bpv.getVerbintenis());
	}

	private static Deelnemer getDeelnemer(Examendeelname deelname)
	{
		if (deelname == null)
		{
			return null;
		}
		return getDeelnemer(deelname.getVerbintenis());
	}

	private static Deelnemer getDeelnemer(OnderwijsproductAfname afname)
	{
		if (afname == null)
		{
			return null;
		}
		return afname.getDeelnemer();
	}

	private static Deelnemer getDeelnemer(OnderwijsproductAfnameContext context)
	{
		if (context == null)
		{
			return null;
		}
		return getDeelnemer(context.getVerbintenis());
	}

	private static Deelnemer getDeelnemer(Resultaat resultaat)
	{
		if (resultaat == null)
		{
			return null;
		}
		return resultaat.getDeelnemer();
	}
}
