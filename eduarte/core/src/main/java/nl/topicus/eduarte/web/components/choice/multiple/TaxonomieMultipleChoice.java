package nl.topicus.eduarte.web.components.choice.multiple;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementDataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;

import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Combobox met alle actieve taxonomien bij een instelling.
 * 
 * @author loite
 */
public class TaxonomieMultipleChoice extends ListMultipleChoice<Taxonomie>
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

	public TaxonomieMultipleChoice(String id)
	{
		this(id, null);
	}

	public TaxonomieMultipleChoice(String id, IModel<Collection<Taxonomie>> model)
	{
		super(id, model, new ListModel(), new EntiteitPropertyRenderer("afkorting"));
	}
}
