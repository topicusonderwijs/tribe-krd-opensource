package nl.topicus.eduarte.participatie.zoekfilters;

import java.util.Date;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.eduarte.entities.participatie.AfspraakType;
import nl.topicus.eduarte.entities.participatie.olc.OlcLocatie;
import nl.topicus.eduarte.entities.participatie.olc.OlcWaarneming;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class OlcWaarnemingZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<OlcWaarneming>
{

	private Boolean verwerkt;

	private IModel<OlcLocatie> olcLocatie = new Model<OlcLocatie>();

	private Date beginTijd;

	private Date eindTijd;

	private Date datum;

	private IModel<Deelnemer> deelnemer = new Model<Deelnemer>();

	private IModel<Medewerker> medewerker = new Model<Medewerker>();

	private IModel<AfspraakType> afspraakType = new Model<AfspraakType>();

	private Boolean nietVerwerkteEnVandaag;

	private static final long serialVersionUID = 1L;

	public Boolean getVerwerkt()
	{
		return verwerkt;
	}

	public void setVerwerkt(Boolean verwerkt)
	{
		this.verwerkt = verwerkt;
	}

	public void setOlcLocatie(OlcLocatie olcLocatie)
	{
		this.olcLocatie = ModelFactory.getModel(olcLocatie);
	}

	public OlcLocatie getOlcLocatie()
	{
		return olcLocatie.getObject();
	}

	public void setBeginTijd(Date beginTijd)
	{
		this.beginTijd = beginTijd;
	}

	public Date getBeginTijd()
	{
		return beginTijd;
	}

	public void setEindTijd(Date eindTijd)
	{
		this.eindTijd = eindTijd;
	}

	public Date getEindTijd()
	{
		return eindTijd;
	}

	public void setDatum(Date datum)
	{
		this.datum = datum;
	}

	public Date getDatum()
	{
		return datum;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = ModelFactory.getModel(deelnemer);
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer.getObject();
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = ModelFactory.getModel(medewerker);
	}

	public Medewerker getMedewerker()
	{
		return medewerker.getObject();
	}

	public void setAfspraakType(AfspraakType afspraakType)
	{
		this.afspraakType = ModelFactory.getModel(afspraakType);
	}

	public AfspraakType getAfspraakType()
	{
		return afspraakType.getObject();
	}

	public void setNietVerwerkteEnVandaag(Boolean nietVerwerkteEnVandaag)
	{
		this.nietVerwerkteEnVandaag = nietVerwerkteEnVandaag;
	}

	public Boolean isNietVerwerkteEnVandaag()
	{
		return nietVerwerkteEnVandaag;
	}
}
