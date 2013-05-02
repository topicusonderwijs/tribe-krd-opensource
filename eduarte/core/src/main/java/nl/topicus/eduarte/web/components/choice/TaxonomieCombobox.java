package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementDataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.providers.TaxonomieProvider;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Combobox met alle actieve taxonomien bij een instelling.
 * 
 * @author loite
 */
public class TaxonomieCombobox extends AbstractAjaxDropDownChoice<Taxonomie> implements
		TaxonomieProvider
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Taxonomie>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Taxonomie> load()
		{
			return DataAccessRegistry.getHelper(TaxonomieElementDataAccessHelper.class)
				.listTaxonomien();
		}
	}

	public TaxonomieCombobox(String id)
	{
		this(id, null);
	}

	public TaxonomieCombobox(String id, IModel<Taxonomie> model)
	{
		super(id, model, new ListModel(), new EntiteitPropertyRenderer("afkorting"));
	}

	@Override
	public Taxonomie getTaxonomie()
	{
		return getModelObject();
	}
}
