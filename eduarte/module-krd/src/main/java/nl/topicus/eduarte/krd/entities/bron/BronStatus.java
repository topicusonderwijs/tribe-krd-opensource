package nl.topicus.eduarte.krd.entities.bron;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.krd.dao.helpers.BronSchooljaarStatusDataAccessHelper;
import nl.topicus.eduarte.krd.zoekfilters.BronSchooljaarStatusZoekFilter;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;

/**
 * Status van het schooljaar voor een specifieke onderwijssoort en aanleverpunt.
 */
public enum BronStatus
{
	GegevensWordenIngevoerd
	{
		@Override
		public String toString()
		{
			return "Gegevens invoeren";
		}
	},
	VolledigheidsverklaringGeregistreerd
	{
		@Override
		public String toString()
		{
			return "Volledigheidsverklaring";
		}
	},
	MutatiebeperkingIngesteld
	{
		@Override
		public String toString()
		{
			return "Mutatiebeperking";
		}
	},
	MutatiestopIngesteld
	{
		@Override
		public String toString()
		{
			return "Mutatiestop";
		}
	},
	AssurancerapportOpgesteld
	{
		@Override
		public String toString()
		{
			return "Assurancerapport";
		}
	},
	Historisch;

	@Override
	public String toString()
	{
		return StringUtil.convertCamelCase(name());
	}

	public BronStatus[] getVervolgStatus()
	{
		switch (this)
		{
			case GegevensWordenIngevoerd:
				return new BronStatus[] {VolledigheidsverklaringGeregistreerd};
			case VolledigheidsverklaringGeregistreerd:
				return new BronStatus[] {MutatiebeperkingIngesteld};
			case MutatiebeperkingIngesteld:
				return new BronStatus[] {MutatiestopIngesteld};
			case MutatiestopIngesteld:
				return new BronStatus[] {AssurancerapportOpgesteld};
			case AssurancerapportOpgesteld:
				return new BronStatus[] {Historisch};
			case Historisch:
				return new BronStatus[] {};
			default:
		}
		return new BronStatus[] {this};
	}

	public boolean isInstellingMutatieToegestaan()
	{
		switch (this)
		{
			case GegevensWordenIngevoerd:
				return true;
			case VolledigheidsverklaringGeregistreerd:
				return true;
			case MutatiebeperkingIngesteld:
				return true;
			case MutatiestopIngesteld:
				return false;
			case AssurancerapportOpgesteld:
				return false;
			case Historisch:
				return false;
			default:
		}
		return false;
	}

	public boolean isLaatsteAanleveringIndicatie()
	{
		switch (this)
		{
			case GegevensWordenIngevoerd:
				return false;
			case VolledigheidsverklaringGeregistreerd:
				return true;
			case MutatiebeperkingIngesteld:
				return true;
			case MutatiestopIngesteld:
				return true;
			case AssurancerapportOpgesteld:
				return true;
			case Historisch:
				return true;
			default:
		}
		return false;
	}

	public boolean isReguliereMutatieToegestaan()
	{
		if (this == BronStatus.GegevensWordenIngevoerd
			|| this == BronStatus.VolledigheidsverklaringGeregistreerd
			|| this == BronStatus.MutatiebeperkingIngesteld)
			return true;
		return false;
	}

	public boolean isAccountantMutatieToegestaan()
	{
		if (this == BronStatus.MutatiestopIngesteld)
			return true;
		return false;
	}

	public boolean isGeenMutatieToegestaan()
	{
		if (this == BronStatus.AssurancerapportOpgesteld || this == BronStatus.Historisch)
			return true;
		return false;
	}

	public boolean isMutatieToegestaan()
	{
		return isAccountantMutatieToegestaan() || isInstellingMutatieToegestaan();
	}

	public boolean isAlleMutatiesToegestaan()
	{
		if (GegevensWordenIngevoerd == this || VolledigheidsverklaringGeregistreerd == this)
			return true;
		return false;
	}

	public static BronStatus getBronStatus(Verbintenis verbintenis)
	{
		Date peildatum = TimeUtil.vandaag();
		Locatie locatie = verbintenis.getLocatie();
		BronOnderwijssoort soortOnderwijs = verbintenis.getBronOnderwijssoort();

		return getBronStatus(peildatum, locatie, soortOnderwijs);
	}

	public static BronStatus getBronStatus(Date peildatum, Locatie locatie,
			BronOnderwijssoort soortOnderwijs)
	{
		BronSchooljaarStatusDataAccessHelper helper =
			DataAccessRegistry.getHelper(BronSchooljaarStatusDataAccessHelper.class);
		BronSchooljaarStatusZoekFilter filter = new BronSchooljaarStatusZoekFilter();
		filter.setSchooljaren(Arrays.asList(Schooljaar.valueOf(peildatum)));

		List<BronSchooljaarStatus> bronSchooljaarStatussen = helper.list(filter);
		BronSchooljaarStatus schooljaarStatus = null;
		for (BronSchooljaarStatus status : bronSchooljaarStatussen)
		{
			for (BronAanleverpuntLocatie bronAanleverpuntLocatie : status.getAanleverpunt()
				.getLocaties())
			{
				Locatie aanleverpuntLocatie = bronAanleverpuntLocatie.getLocatie();
				Locatie verbintenisLocatie = locatie;
				if (aanleverpuntLocatie.equals(verbintenisLocatie))
				{
					schooljaarStatus = status;
					break;
				}
			}
			if (schooljaarStatus != null)
				break;
		}
		if (schooljaarStatus == null)
			return null;
		return schooljaarStatus.getStatus(soortOnderwijs);
	}
}
