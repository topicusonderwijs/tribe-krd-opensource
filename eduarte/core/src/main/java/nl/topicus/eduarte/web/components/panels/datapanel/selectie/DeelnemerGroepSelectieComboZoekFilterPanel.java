package nl.topicus.eduarte.web.components.panels.datapanel.selectie;

import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.web.components.choice.OpleidingCombobox;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.IModel;

/**
 * 
 * 
 * @author vanharen
 */
public class DeelnemerGroepSelectieComboZoekFilterPanel extends
		AutoZoekFilterPanel<VerbintenisZoekFilter>
{

	private static final long serialVersionUID = 1L;

	private IModel<Opleiding> opleidingModel;

	private IModel<List<Opleiding>> opleidingenModel;

	private VerbintenisZoekFilter filter;

	public DeelnemerGroepSelectieComboZoekFilterPanel(String id, VerbintenisZoekFilter zoekfilter,
			IPageable pageable)
	{
		super(id, zoekfilter, pageable);
		this.filter = zoekfilter;

		ModelManager manager = new DefaultModelManager(Opleiding.class);
		opleidingModel = ModelFactory.getCompoundModel(zoekfilter.getOpleiding(), manager);
		opleidingenModel = ModelFactory.getCompoundModel(zoekfilter.getOpleidingList(), manager);
		zoekfilter.setCohort(Cohort.getHuidigCohort());
		setPropertyNames(Arrays.asList("cohort"));
		addFieldModifier(new RequiredModifier(true, "cohort"));
		getForm().add(new OpleidingCombobox("opleiding", opleidingModel, opleidingenModel)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected boolean isSelected(Opleiding object, int index, String selected)
			{
				filter.setOpleiding(object);
				return super.isSelected(object, index, selected);
			}

		});
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		opleidingModel.detach();
		opleidingenModel.detach();
	}
}
