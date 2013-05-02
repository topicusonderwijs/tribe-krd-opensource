package nl.topicus.eduarte.krd.bron.schakeltest;

import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;

public class BronVerbintenisBuilder
{
	private final TimeUtil timeUtil = TimeUtil.getInstance();

	private final BronBuilder parent;

	private Date begindatum;

	private Intensiteit intensiteit;

	private Date geplandeEinddatum;

	private Bekostigd bekostigd;

	private Opleiding opleiding;

	private Verbintenis verbintenis;

	private boolean indicatieGehandicapt;

	public BronVerbintenisBuilder(BronBuilder parent)
	{
		this.parent = parent;
		intensiteit = Intensiteit.Voltijd;
		indicatieGehandicapt = false;
	}

	public BronBuilder build()
	{
		verbintenis = new Verbintenis(parent.getDeelnemer());
		verbintenis.setLocatie(null);
		verbintenis.setStatus(VerbintenisStatus.Definitief);
		if (verbintenis.getGeplandeEinddatum() == null)
		{
			verbintenis.setGeplandeEinddatum(timeUtil.addDays(
				timeUtil.addYears(verbintenis.getBegindatum(), 4), -1));
		}

		verbintenis.setBegindatum(begindatum);
		verbintenis.setCohort(Cohort.getCohort(timeUtil.getYear(begindatum)));
		verbintenis.setDatumDefinitief(begindatum);
		verbintenis.setDatumGeplaatst(begindatum);
		verbintenis.setIntensiteit(intensiteit);
		verbintenis.setIndicatieGehandicapt(indicatieGehandicapt);
		verbintenis.setGeplandeEinddatum(geplandeEinddatum);
		verbintenis.setBekostigd(bekostigd);
		verbintenis.setOpleiding(opleiding);

		parent.getDeelnemer().getVerbintenissen().add(verbintenis);
		verbintenis.setRelevanteVooropleidingVooropleiding(parent.getDeelnemer()
			.getHoogsteVooropleiding());

		parent.setVerbintenis(verbintenis);
		return parent;
	}

	public BronVerbintenisBuilder setIntensiteit(Intensiteit intensiteit)
	{
		this.intensiteit = intensiteit;
		return this;
	}

	public BronVerbintenisBuilder setIngangsdatum(int ingangsdatum)
	{
		begindatum = timeUtil.parseDateString(String.valueOf(ingangsdatum));
		return this;
	}

	public BronVerbintenisBuilder setGeplandeEinddatum(int einddatum)
	{
		geplandeEinddatum = timeUtil.parseDateString(String.valueOf(einddatum));
		return this;
	}

	public BronVerbintenisBuilder setOpleidingMBO(int code, MBOLeerweg leerweg)
	{
		BronOpleidingBuilderKwalificatie builder = new BronOpleidingBuilderKwalificatie();
		builder.setCode(code);
		builder.setLeerweg(leerweg);
		setOpleiding(builder.build());
		return this;
	}

	public BronVerbintenisBuilder setOpleidingEducatie(int code)
	{
		BronOpleidingBuilderEducatie builder = new BronOpleidingBuilderEducatie();
		builder.setCode(code);
		setOpleiding(builder.build());
		return this;
	}

	public BronVerbintenisBuilder setOpleidingVavo(int code)
	{
		BronOpleidingBuilderVO builder = new BronOpleidingBuilderVO();
		builder.setCode(code);
		setOpleiding(builder.build());
		return this;
	}

	public BronVerbintenisBuilder setBekostigd(Bekostigd bekostigd)
	{
		this.bekostigd = bekostigd;
		return this;
	}

	public BronVerbintenisBuilder setIndicatieGehandicapt(boolean indicatieGehandicapt)
	{
		this.indicatieGehandicapt = indicatieGehandicapt;
		return this;
	}

	BronVerbintenisBuilder setOpleiding(Opleiding opleiding)
	{
		this.opleiding = opleiding;
		return this;
	}

	Verbintenis getVerbintenis()
	{
		return verbintenis;
	}
}
