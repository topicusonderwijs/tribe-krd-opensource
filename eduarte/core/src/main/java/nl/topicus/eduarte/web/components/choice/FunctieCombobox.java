package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.FunctieDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Functie;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitToStringRenderer;
import nl.topicus.eduarte.zoekfilters.FunctieZoekFilter;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Combobox voor functies.
 * 
 * @author loite
 */
public class FunctieCombobox extends DropDownChoice<Functie>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Functie>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Functie> load()
		{
			return DataAccessRegistry.getHelper(FunctieDataAccessHelper.class).list(
				new FunctieZoekFilter());
		}
	}

	public FunctieCombobox(String id)
	{
		this(id, null);
	}

	public FunctieCombobox(String id, IModel<Functie> model)
	{
		super(id, model, new ListModel(), new EntiteitToStringRenderer());
	}
}
