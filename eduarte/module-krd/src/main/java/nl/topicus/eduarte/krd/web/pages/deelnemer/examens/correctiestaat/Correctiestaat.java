package nl.topicus.eduarte.krd.web.pages.deelnemer.examens.correctiestaat;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.HibernateObjectListModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.dao.helpers.InstellingsLogoDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.organisatie.InstellingsLogo;
import nl.topicus.eduarte.krd.web.components.choice.CorrectiestaatTijdvakComboBox;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

public class Correctiestaat implements Serializable, IDetachable
{
	private static final long serialVersionUID = 1L;

	@AutoForm(required = true, htmlClasses = "unit_max")
	private IModel<Onderwijsproduct> onderwijsproduct;

	private IModel<List<Verbintenis>> verbintenissen;

	@AutoForm(required = true, editorClass = CorrectiestaatTijdvakComboBox.class)
	private Integer tijdvak;

	private IModel<List<VerbintenisOnderwijsproductResultaten>> verbintenisResultaten;

	public Correctiestaat()
	{
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return getModelObject(onderwijsproduct);
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = makeModelFor(onderwijsproduct);
	}

	public List<Verbintenis> getVerbintenissen()
	{
		return getModelObject(verbintenissen);
	}

	public void setVerbintenissen(List<Verbintenis> verbintenissen)
	{
		this.verbintenissen = makeModelFor(verbintenissen);
	}

	public void setVerbintenisResultaten(
			List<VerbintenisOnderwijsproductResultaten> verbintenisResultaten)
	{
		this.verbintenisResultaten = makeModelFor(verbintenisResultaten);
	}

	public List<VerbintenisOnderwijsproductResultaten> getVerbintenisResultaten()
	{
		return getModelObject(verbintenisResultaten);
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
		ComponentUtil.detachQuietly(onderwijsproduct);
		ComponentUtil.detachQuietly(verbintenissen);
		ComponentUtil.detachQuietly(verbintenisResultaten);
	}

	public Image getInstellingsLogo()
	{
		Image curFoto = null;
		try
		{
			InstellingsLogo logo =
				DataAccessRegistry.getHelper(InstellingsLogoDataAccessHelper.class)
					.getInstellingsLogo();
			if (logo != null)
			{
				byte[] afbeelding = logo.getLogo().getBestand();
				if (afbeelding != null && afbeelding.length > 0)
				{
					curFoto = ImageIO.read(new ByteArrayInputStream(afbeelding));
				}
			}
		}
		catch (IOException e)
		{
			// ignore, we kunnen hier toch niets doen.
		}
		return curFoto;
	}

	@SuppressWarnings("unchecked")
	public void haalResultatenOp()
	{
		List<VerbintenisOnderwijsproductResultaten> results =
			new ArrayList<VerbintenisOnderwijsproductResultaten>();
		HibernateObjectListModel<List<Verbintenis>, Verbintenis> verbinModel =
			(HibernateObjectListModel<List<Verbintenis>, Verbintenis>) verbintenissen;
		for (int i = 0; i < verbinModel.size(); i++)
		{
			results.add(new VerbintenisOnderwijsproductResultaten(verbinModel.get(i),
				onderwijsproduct, tijdvak));
		}
		setVerbintenisResultaten(results);
	}

	public void setTijdvak(Integer tijdvak)
	{
		this.tijdvak = tijdvak;
	}

	public Integer getTijdvak()
	{
		return tijdvak;
	}

}