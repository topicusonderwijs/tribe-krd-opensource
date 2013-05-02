package nl.topicus.eduarte.krd.bron.schakeltest;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;

public class BronBuilder
{
	private Deelnemer deelnemer;

	private Verbintenis verbintenis;

	public BronBuilder()
	{
	}

	public BronDeelnemerBuilder buildDeelnemer()
	{
		return new BronDeelnemerBuilder(this);
	}

	void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public Deelnemer getDeelnemer()
	{
		if (deelnemer == null)
		{
			throw new IllegalStateException("Deelnemer is nog niet geconstrueerd");
		}
		return deelnemer;
	}

	void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = verbintenis;
	}

	public Verbintenis getVerbintenis()
	{
		if (verbintenis == null)
		{
			throw new IllegalStateException("Verbintenis is nog niet geconstrueerd");
		}
		return verbintenis;
	}

	public BronBuilder buildDeelnemer(int deelnemernummer, Integer bsn, Integer onderwijsnummer,
			Integer geboortedatum, String postcode, String land)
	{
		return buildDeelnemer().setDeelnemernummer(deelnemernummer).setBsn(bsn).setOnderwijsnummer(
			onderwijsnummer).setGeboortedatum(geboortedatum).setAdresPostcode(postcode)
			.setAdresLand(Land.getLand(land)).build();
	}

	public BronVerbintenisBuilder addVerbintenisMBO()
	{
		return new BronVerbintenisBuilder(this);
	}

	public BronBuilder addVerbintenisMBO(int ingangsdatum, int code, MBOLeerweg leerweg,
			Bekostigd bekostigd)
	{
		BronVerbintenisBuilder builder = addVerbintenisMBO();
		builder.setIngangsdatum(ingangsdatum);
		builder.setOpleidingMBO(code, leerweg);
		builder.setBekostigd(bekostigd);
		builder.setGeplandeEinddatum(20110101);
		builder.build();
		verbintenis = builder.getVerbintenis();
		return this;
	}

	public BronTestData build()
	{
		return new BronTestData(this, deelnemer, verbintenis);
	}

	public BronBuilder addVerbintenisVAVO(int ingangsdatum, int code, int verwachteEinddatum)
	{
		BronVerbintenisBuilder builder = new BronVerbintenisBuilder(this);

		builder.setGeplandeEinddatum(verwachteEinddatum);
		builder.setIngangsdatum(ingangsdatum);
		builder.setOpleidingVavo(code);
		builder.build();
		verbintenis = builder.getVerbintenis();
		return this;
	}

	public BronBuilder addVerbintenisVO(int ingangsdatum, int code)
	{
		BronVerbintenisBuilder builder = new BronVerbintenisBuilder(this);

		builder.setIngangsdatum(ingangsdatum);
		builder.setOpleidingVavo(code);
		builder.build();
		verbintenis = builder.getVerbintenis();
		return this;
	}
}
