package nl.topicus.eduarte.krdparticipatie.web.components.panels.filter;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;
import nl.topicus.eduarte.krdparticipatie.web.components.combobox.AfspraakTypeCategorieMultipleCheckbox;
import nl.topicus.eduarte.participatie.zoekfilters.DeelnemerActiviteitTotalenZoekFilter;
import nl.topicus.eduarte.web.components.quicksearch.contract.ContractSearchEditor;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author vandekamp
 */
public class DeelnemerActiviteitTotalenZoekFilterPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public DeelnemerActiviteitTotalenZoekFilterPanel(String id,
			DeelnemerActiviteitTotalenZoekFilter filter)
	{
		super(id);
		Form<DeelnemerActiviteitTotalenZoekFilter> form =
			new Form<DeelnemerActiviteitTotalenZoekFilter>("form",
				new CompoundPropertyModel<DeelnemerActiviteitTotalenZoekFilter>(filter))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit()
				{
					// niks te doen
				}
			};
		add(form);
		form.add(new DatumField("beginDatum"));
		form.add(new DatumField("eindDatum"));
		form.add(new JaNeeCombobox("activiteitenTonen").setNullValid(true));
		form.add(new ContractSearchEditor("contract", new PropertyModel<Contract>(form
			.getModelObject(), "contract")));
		List<AfspraakTypeCategory> afspraakTypeCategoryList = new ArrayList<AfspraakTypeCategory>();
		afspraakTypeCategoryList.add(AfspraakTypeCategory.INDIVIDUEEL);
		afspraakTypeCategoryList.add(AfspraakTypeCategory.ROOSTER);
		form.add(new AfspraakTypeCategorieMultipleCheckbox("afspraakTypeCategories",
			afspraakTypeCategoryList));

	}
}
