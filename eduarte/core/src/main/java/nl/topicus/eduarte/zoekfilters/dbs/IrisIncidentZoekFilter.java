package nl.topicus.eduarte.zoekfilters.dbs;

import java.util.Date;

import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.dbs.gedrag.IncidentCategorie;
import nl.topicus.eduarte.entities.dbs.incident.IrisIncident;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.web.components.quicksearch.deelnemer.DeelnemerSearchEditor;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;
import nl.topicus.onderwijs.incidentkoppeling.model.IrisVoorval;

import org.apache.wicket.model.IModel;

public class IrisIncidentZoekFilter extends AbstractZoekFilter<IrisIncident>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(editorClass = DeelnemerSearchEditor.class)
	private IModel<Deelnemer> deelnemer;

	private IModel<IncidentCategorie> categorie;

	private IModel<IrisVoorval> irisCategorie;

	private IModel<Date> vanafDatum;

	private IModel<Date> totDatum;

	@AutoForm(editorClass = JaNeeCombobox.class)
	private IModel<Boolean> staatInIris;

	public IrisIncidentZoekFilter()
	{
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = makeModelFor(deelnemer);
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemer);
	}

	public void setCategorie(IncidentCategorie categorie)
	{
		this.categorie = makeModelFor(categorie);
	}

	public IncidentCategorie getCategorie()
	{
		return getModelObject(categorie);
	}

	public void setIrisCategorie(IrisVoorval irisCategorie)
	{
		this.irisCategorie = makeModelFor(irisCategorie);
	}

	public IrisVoorval getIrisCategorie()
	{
		return getModelObject(irisCategorie);
	}

	public void setVanafDatum(Date vanafDatum)
	{
		this.vanafDatum = makeModelFor(vanafDatum);
	}

	public Date getVanafDatum()
	{
		return getModelObject(vanafDatum);
	}

	public void setTotDatum(Date totDatum)
	{
		this.totDatum = makeModelFor(totDatum);
	}

	public Date getTotDatum()
	{
		return getModelObject(totDatum);
	}

	public void setStaatInIris(Boolean staatInIris)
	{
		this.staatInIris = makeModelFor(staatInIris);
	}

	public Boolean getStaatInIris()
	{
		return getModelObject(staatInIris);
	}

}
