package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.UitkomstIntakegesprekDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.UitkomstIntakegesprek;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;
import nl.topicus.eduarte.zoekfilters.UitkomstIntakegesprekZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author idserda
 */
public class UitkomstIntakegesprekCombobox extends
		AbstractAjaxDropDownChoice<UitkomstIntakegesprek>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends
			LoadableDetachableModel<List<UitkomstIntakegesprek>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<UitkomstIntakegesprek> load()
		{
			return DataAccessRegistry.getHelper(UitkomstIntakegesprekDataAccessHelper.class).list(
				new UitkomstIntakegesprekZoekFilter());
		}
	}

	public UitkomstIntakegesprekCombobox(String id)
	{
		this(id, null);
	}

	public UitkomstIntakegesprekCombobox(String id, IModel<UitkomstIntakegesprek> model)
	{
		super(id, model, new ListModel(), new EntiteitPropertyRenderer("naam"));
	}
}
