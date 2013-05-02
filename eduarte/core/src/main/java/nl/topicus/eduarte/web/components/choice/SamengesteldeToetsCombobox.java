package nl.topicus.eduarte.web.components.choice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class SamengesteldeToetsCombobox extends AbstractAjaxDropDownChoice<String>
{
	private static final long serialVersionUID = 1L;

	private class ChoicesModel extends LoadableDetachableModel<Map<String, String>>
	{
		private static final long serialVersionUID = 1L;

		private ResultaatstructuurZoekFilter filter;

		public ChoicesModel(ResultaatstructuurZoekFilter filter)
		{
			this.filter = filter;
		}

		@Override
		protected Map<String, String> load()
		{
			ResultaatstructuurZoekFilter copy = new ZoekFilterCopyManager().copyObject(filter);
			copy.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
				SamengesteldeToetsCombobox.this));
			ToetsZoekFilter toetsFilter = new ToetsZoekFilter();
			toetsFilter.setSamengesteldeToets(true);
			toetsFilter.setResultaatstructuurFilter(filter);
			toetsFilter.setEindResultaat(false);
			List<Toets> toetsen =
				DataAccessRegistry.getHelper(ToetsDataAccessHelper.class).list(toetsFilter);

			Map<String, String> ret = new HashMap<String, String>();
			for (Toets curToets : toetsen)
			{
				ret.put(curToets.getCodePath(), curToets.getCode() + " - " + curToets.getNaam());
			}
			return ret;
		}
	}

	private ChoicesModel choices;

	public SamengesteldeToetsCombobox(String id, IModel<String> model,
			ResultaatstructuurZoekFilter structuurFilter)
	{
		super(id, model, (IModel<List<String>>) null);
		choices = new ChoicesModel(structuurFilter);
		setChoiceRenderer(new IChoiceRenderer<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(String object)
			{
				return choices.getObject().get(object);
			}

			@Override
			public String getIdValue(String object, int index)
			{
				return object;
			}
		});
		setAddSelectedItemToChoicesWhenNotInList(false);
	}

	@Override
	public List<String> getChoices()
	{
		return new ArrayList<String>(choices.getObject().keySet());
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		choices.detach();
	}
}
