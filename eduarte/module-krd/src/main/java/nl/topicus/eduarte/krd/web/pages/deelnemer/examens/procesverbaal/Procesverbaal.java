package nl.topicus.eduarte.krd.web.pages.deelnemer.examens.procesverbaal;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.topicus.cobra.entities.Time;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.text.TijdField;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheidAdres;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

public class Procesverbaal implements Serializable, IDetachable
{
	private static final long serialVersionUID = 1L;

	@AutoForm(required = true, htmlClasses = "unit_max")
	private IModel<OrganisatieEenheid> organisatieEenheid;

	@AutoForm(required = true, htmlClasses = "unit_max")
	private IModel<Onderwijsproduct> onderwijsproduct;

	@AutoForm(htmlClasses = "unit_max")
	private IModel<Opleiding> opleiding;

	@AutoForm(required = true, htmlClasses = "unit_80")
	private String lokaal;

	@AutoForm(required = true, htmlClasses = "unit_80")
	private Date examenDatum;

	@AutoForm(required = true, editorClass = TijdField.class, htmlClasses = "unit_50")
	private Time begintijd;

	@AutoForm(required = true, editorClass = TijdField.class, htmlClasses = "unit_50")
	private Time eindtijd;

	private IModel<List<Verbintenis>> verbintenissen;

	private List<Surveillant> surveillanten = new ArrayList<Surveillant>();

	public Procesverbaal()
	{
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return getModelObject(organisatieEenheid);
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = makeModelFor(organisatieEenheid);
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return getModelObject(onderwijsproduct);
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = makeModelFor(onderwijsproduct);
	}

	public String getLokaal()
	{
		return lokaal;
	}

	public void setLokaal(String lokaal)
	{
		this.lokaal = lokaal;
	}

	public Date getExamenDatum()
	{
		return examenDatum;
	}

	public void setExamenDatum(Date examenDatum)
	{
		this.examenDatum = examenDatum;
	}

	public Time getBegintijd()
	{
		return begintijd;
	}

	public void setBegintijd(Time begintijd)
	{
		this.begintijd = begintijd;
	}

	public Time getEindtijd()
	{
		return eindtijd;
	}

	public void setEindtijd(Time eindtijd)
	{
		this.eindtijd = eindtijd;
	}

	public List<Verbintenis> getVerbintenissen()
	{
		return getModelObject(verbintenissen);
	}

	public void setVerbintenissen(List<Verbintenis> verbintenissen)
	{
		this.verbintenissen = makeModelFor(verbintenissen);
	}

	/**
	 * Wraps a null check around getting the object from the model.
	 * 
	 * @param model
	 * @return the object from the model or null if the model is null.
	 */
	protected <Y> Y getModelObject(IModel<Y> model)
	{
		if (model != null)
			return model.getObject();
		return null;
	}

	/**
	 * Wraps a null check around building a model for a value.
	 * 
	 * @param object
	 * @return a new IModel or null if the object is null.
	 */
	protected <T> IModel<T> makeModelFor(T object)
	{
		if (object != null)
			return ModelFactory.getModel(object);
		return null;
	}

	protected <T, Z extends Collection<T>> IModel<Z> makeModelFor(Z list)
	{
		if (list != null)
			return ModelFactory.getListModel(list);
		return null;
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(organisatieEenheid);
		ComponentUtil.detachQuietly(onderwijsproduct);
		ComponentUtil.detachQuietly(verbintenissen);
		ComponentUtil.detachQuietly(opleiding);
	}

	public void setSurveillanten(List<Surveillant> surveillanten)
	{
		this.surveillanten = surveillanten;
	}

	public List<Surveillant> getSurveillanten()
	{
		return surveillanten;
	}

	public String getExamenDatumAlsString()
	{
		if (getExamenDatum() != null)
		{
			return new SimpleDateFormat("dd MMMMM yyyy", new Locale("nl", "NL"))
				.format(getExamenDatum());
		}
		return "";
	}

	public String getPlaats()
	{
		OrganisatieEenheid organ = getOrganisatieEenheid();
		if (organ != null)
		{
			Adres adres = null;
			for (OrganisatieEenheidAdres organAdres : organ.getAdressenOpPeildatum())
			{
				if (adres == null)
					adres = organAdres.getAdres();
				if (organAdres.isFysiekadres())
					adres = organAdres.getAdres();
			}
			if (adres != null)
				return adres.getPlaats();
		}
		return "";
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = makeModelFor(opleiding);
	}

	public Opleiding getOpleiding()
	{
		return getModelObject(opleiding);
	}
}