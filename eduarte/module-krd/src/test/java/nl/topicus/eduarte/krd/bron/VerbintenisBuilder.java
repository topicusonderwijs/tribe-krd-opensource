package nl.topicus.eduarte.krd.bron;

import static nl.topicus.eduarte.entities.organisatie.EntiteitContext.*;

import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.entities.taxonomie.vo.Elementcode;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;

public class VerbintenisBuilder
{
	private TimeUtil timeUtil = TimeUtil.getInstance();

	private Deelnemer deelnemer;

	private Opleiding opleiding;

	private Date ingangsdatum;

	private Date geplandeDatumUitschrijving;

	private Intensiteit intensiteit;

	private Bekostigd bekostigd;

	private VerbintenisBuilder(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
		this.ingangsdatum = Schooljaar.huidigSchooljaar().getBegindatum();
		this.intensiteit = Intensiteit.Voltijd;
	}

	public void setIngangsdatum(Date datum)
	{
		ingangsdatum = datum;
	}

	public void setIngangsdatum(String datum)
	{
		ingangsdatum = timeUtil.parseDateString(datum);
	}

	public void setIngangsdatum(Integer datum)
	{
		ingangsdatum = timeUtil.parseDateString(datum.toString());
	}

	public void setGeplandeDatumUitschrijving(Date date)
	{
		geplandeDatumUitschrijving = date;
	}

	public void setGeplandeDatumUitschrijving(String string)
	{
		geplandeDatumUitschrijving = timeUtil.parseDateString(string);
	}

	public void setGeplandeDatumUitschrijving(Integer integer)
	{
		geplandeDatumUitschrijving = timeUtil.parseDateString(integer.toString());
	}

	public void setOpleiding(String code, String naam)
	{
		opleiding = new Opleiding();
		opleiding.setBegindatum(timeUtil.parseDateString("20000801"));
		opleiding.setNaam(naam);

		Taxonomie taxonomie = new Taxonomie(LANDELIJK);
		taxonomie.setCode("2");
		taxonomie.setAfkorting("MBO");
		taxonomie.setNaam("MBO");

		Verbintenisgebied verbintenisgebied = new Elementcode(EntiteitContext.LANDELIJK);
		verbintenisgebied.setTaxonomie(taxonomie);
		verbintenisgebied.setExterneCode(code);

		opleiding.setVerbintenisgebied(verbintenisgebied);
	}

	public void setOpleiding(String code, String naam, MBOLeerweg leerweg)
	{
		setOpleiding(code, naam);
		opleiding.setLeerweg(leerweg);
	}

	public Verbintenis build()
	{
		Verbintenis verbintenis = new Verbintenis(deelnemer);

		deelnemer.getVerbintenissen().add(verbintenis);

		verbintenis.setBegindatum(ingangsdatum);
		verbintenis.setBekostigd(bekostigd);
		verbintenis.setCohort(Cohort.getCohort(timeUtil.getYear(ingangsdatum)));
		verbintenis.setDatumDefinitief(ingangsdatum);
		verbintenis.setDatumGeplaatst(ingangsdatum);
		verbintenis
			.setGeplandeEinddatum(geplandeDatumUitschrijving != null ? geplandeDatumUitschrijving
				: timeUtil.addDays(timeUtil.addYears(ingangsdatum, 4), -1));
		verbintenis.setIntensiteit(intensiteit);
		verbintenis.setOpleiding(opleiding);
		verbintenis.setLocatie(null);
		verbintenis.setIndicatieGehandicapt(false);
		verbintenis.setStatus(VerbintenisStatus.Definitief);
		verbintenis.setRelevanteVooropleidingVooropleiding(deelnemer.getHoogsteVooropleiding());
		return verbintenis;
	}

	public void setBekostigd(Bekostigd b)
	{
		bekostigd = b;
	}
}
