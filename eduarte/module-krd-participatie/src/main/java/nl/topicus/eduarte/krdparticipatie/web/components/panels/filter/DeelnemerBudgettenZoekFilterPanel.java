package nl.topicus.eduarte.krdparticipatie.web.components.panels.filter;

import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.participatie.zoekfilters.BudgetZoekFilter;
import nl.topicus.eduarte.web.components.quicksearch.onderwijsproduct.OnderwijsproductSearchEditor;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author vandekamp
 */
public class DeelnemerBudgettenZoekFilterPanel extends Panel
{
	private CompoundPropertyModel<DeelnemerBudgettenZoekFilterPanel> model;

	private static final long serialVersionUID = 1L;

	public DeelnemerBudgettenZoekFilterPanel(String id, BudgetZoekFilter filter,
			final IPageable pageable)
	{
		super(id);
		model = new CompoundPropertyModel<DeelnemerBudgettenZoekFilterPanel>(filter);
		Form<DeelnemerBudgettenZoekFilterPanel> form =
			new Form<DeelnemerBudgettenZoekFilterPanel>("form", model)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit()
				{
					pageable.setCurrentPage(0);
				}
			};
		add(form);
		form.add(new OnderwijsproductSearchEditor("onderwijsproduct",
			new PropertyModel<Onderwijsproduct>(model, "Onderwijsproduct")));
	}
}
