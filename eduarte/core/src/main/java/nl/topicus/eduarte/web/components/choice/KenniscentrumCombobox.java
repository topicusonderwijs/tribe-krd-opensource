package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.BrinDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.Brin.Onderwijssector;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;
import nl.topicus.eduarte.zoekfilters.BrinZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Combobox voor brin met onderwijssecter kenniscentrum.
 * 
 * @author vandekamp
 */
public class KenniscentrumCombobox extends AbstractAjaxDropDownChoice<Brin>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Brin>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Brin> load()
		{
			BrinDataAccessHelper helper = DataAccessRegistry.getHelper(BrinDataAccessHelper.class);

			// Altijd in landelijke BRIN tabel zoeken, aangezien kenniscentra niet zelf
			// kunnen worden ingevoerd.
			BrinZoekFilter filter = new BrinZoekFilter(true);

			filter.setOnderwijssector(Onderwijssector.LOB);
			filter.addOrderByProperty("naam");
			return helper.list(filter);
		}
	}

	public KenniscentrumCombobox(String id)
	{
		this(id, null);
	}

	public KenniscentrumCombobox(String id, IModel<Brin> model)
	{
		super(id, model, new ListModel(), new EntiteitPropertyRenderer("naam"));
	}

	public KenniscentrumCombobox(String id, IModel<Brin> model,
			IModel< ? extends List< ? extends Brin>> choices)
	{
		super(id, model, choices, new EntiteitPropertyRenderer("naam"));
	}
}
