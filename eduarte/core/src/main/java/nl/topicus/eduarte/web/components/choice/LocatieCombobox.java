package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.app.security.models.LocatiesPageModel;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.providers.LocatieProvider;
import nl.topicus.eduarte.providers.OrganisatieEenheidProvider;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitToStringRenderer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.checks.ComponentSecurityCheck;

/**
 * Locatiecombobox met security check.
 * 
 * @author loite
 */
public class LocatieCombobox extends AbstractAjaxDropDownChoice<Locatie> implements LocatieProvider
{
	private static final long serialVersionUID = 1L;

	public LocatieCombobox(String id)
	{
		this(id, null, null, null);
	}

	public LocatieCombobox(String id, IModel<Locatie> model)
	{
		this(id, model, null, null);
	}

	public LocatieCombobox(String id, IModel<Locatie> model,
			OrganisatieEenheidProvider organisatieEenheidProvider, IModel< ? > filterEntiteitModel)
	{
		super(id, model, (IModel<List<Locatie>>) null, new EntiteitToStringRenderer());
		setChoices(new LocatiesPageModel(new ComponentSecurityCheck(this),
			organisatieEenheidProvider, filterEntiteitModel));
	}

	@Override
	protected void onError(AjaxRequestTarget target, RuntimeException e)
	{
		super.onError(target, e);
		setModelObject(null);
	}

	@Override
	public Locatie getLocatie()
	{
		return getModelObject();
	}
}
