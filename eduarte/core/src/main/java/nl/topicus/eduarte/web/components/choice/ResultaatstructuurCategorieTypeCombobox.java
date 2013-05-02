package nl.topicus.eduarte.web.components.choice;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.JavaUtil;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurCategorieDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.ResultaatstructuurCategorie;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class ResultaatstructuurCategorieTypeCombobox extends
		AbstractAjaxDropDownChoice<ResultaatstructuurCategorieTypeCombobox.CategorieType>
{
	private static final long serialVersionUID = 1L;

	public static class CategorieType
	{
		private ResultaatstructuurCategorie categorie;

		private Type type;

		public CategorieType(ResultaatstructuurCategorie categorie, Type type)
		{
			this.categorie = categorie;
			this.type = type;
		}

		public ResultaatstructuurCategorie getCategorie()
		{
			return categorie;
		}

		public Type getType()
		{
			return type;
		}

		@Override
		public int hashCode()
		{
			int ret = type.hashCode();
			if (categorie != null)
				ret ^= categorie.hashCode();
			return ret;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj instanceof CategorieType)
			{
				CategorieType other = (CategorieType) obj;
				return type == other.type && JavaUtil.equalsOrBothNull(categorie, other.categorie);
			}
			return false;
		}

		@Override
		public String toString()
		{
			return type == Type.SUMMATIEF ? "Summatief" : categorie.toString();
		}

		public String getId()
		{
			return type + ":" + (categorie == null ? "null" : categorie.getId());
		}
	}

	private class ListModel extends LoadableDetachableModel<List<CategorieType>>
	{
		private static final long serialVersionUID = 1L;

		private ResultaatstructuurZoekFilter filter;

		public ListModel(ResultaatstructuurZoekFilter filter)
		{
			this.filter = filter;
		}

		@Override
		protected List<CategorieType> load()
		{
			ResultaatstructuurZoekFilter copy = new ZoekFilterCopyManager().copyObject(filter);
			copy.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
				ResultaatstructuurCategorieTypeCombobox.this));
			copy.setCategorie(null);
			copy.setType(Type.FORMATIEF);
			List<ResultaatstructuurCategorie> cats =
				DataAccessRegistry.getHelper(ResultaatstructuurCategorieDataAccessHelper.class)
					.getCategorien(copy);

			List<CategorieType> ret = new ArrayList<CategorieType>();
			ret.add(new CategorieType(null, Type.SUMMATIEF));
			for (ResultaatstructuurCategorie curCategorie : cats)
			{
				ret.add(new CategorieType(curCategorie, Type.FORMATIEF));
			}
			return ret;
		}
	}

	public ResultaatstructuurCategorieTypeCombobox(String id, IModel<CategorieType> model,
			ResultaatstructuurZoekFilter filter)
	{
		super(id, model, (IModel<List<CategorieType>>) null, new IChoiceRenderer<CategorieType>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getIdValue(CategorieType object, int index)
			{
				return object.getId();
			}

			@Override
			public String getDisplayValue(CategorieType object)
			{
				String stringrep = " ";
				if (object != null)
					stringrep = object.toString();

				return stringrep;
			}
		});
		setChoices(new ListModel(filter));
	}
}
