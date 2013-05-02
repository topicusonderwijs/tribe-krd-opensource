package nl.topicus.eduarte.krd.web.pages.beheer.mutatielog;

import static java.util.Collections.*;
import static nl.topicus.cobra.dao.DataAccessRegistry.*;
import static nl.topicus.cobra.util.StringUtil.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.krd.dao.helpers.MutatieLogVerwerkersDataAccessHelper;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerker;
import nl.topicus.eduarte.krd.zoekfilters.MutatieLogVerwerkersZoekFilter;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hibernate.Hibernate;

public class MutatieLogVerwerkerDropDownChoice extends DropDownChoice<MutatieLogVerwerker>
{
	private static final long serialVersionUID = 1L;

	public MutatieLogVerwerkerDropDownChoice(String id)
	{
		super(id, new NogNietUitgegevenVerwerkersModel(), new VerwerkerRenderer());
	}

	public MutatieLogVerwerkerDropDownChoice(String id, IModel<MutatieLogVerwerker> model)
	{
		super(id, model, new NogNietUitgegevenVerwerkersModel(), new VerwerkerRenderer());
	}

	private static class NogNietUitgegevenVerwerkersModel extends
			LoadableDetachableModel<List<MutatieLogVerwerker>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<MutatieLogVerwerker> load()
		{
			MutatieLogVerwerkersDataAccessHelper helper =
				getHelper(MutatieLogVerwerkersDataAccessHelper.class);
			MutatieLogVerwerkersZoekFilter filter = new MutatieLogVerwerkersZoekFilter();
			filter.setNogNietUitgegeven(true);

			List<MutatieLogVerwerker> nogNietUitgegevenVerwerkers = helper.list(filter);

			List<MutatieLogVerwerker> resultaat = new ArrayList<MutatieLogVerwerker>();
			for (MutatieLogVerwerker verwerker : nogNietUitgegevenVerwerkers)
			{
				Class< ? > verwerkerClass = Hibernate.getClass(verwerker);
				Module module = verwerkerClass.getAnnotation(Module.class);
				if (EduArteApp.get().isModuleActive(module.value()))
				{
					resultaat.add(verwerker);
				}
			}
			sort(resultaat, new Comparator<MutatieLogVerwerker>()
			{
				@Override
				public int compare(MutatieLogVerwerker o1, MutatieLogVerwerker o2)
				{
					return o1.getNaam().compareTo(o2.getNaam());
				}
			});
			return resultaat;
		}
	}

	private static class VerwerkerRenderer extends ChoiceRenderer<MutatieLogVerwerker>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Object getDisplayValue(MutatieLogVerwerker object)
		{
			return convertCamelCase(Hibernate.getClass(object).getSimpleName());
		}

		@Override
		public String getIdValue(MutatieLogVerwerker object, int index)
		{
			return Hibernate.getClass(object).getSimpleName();
		}
	}
}
