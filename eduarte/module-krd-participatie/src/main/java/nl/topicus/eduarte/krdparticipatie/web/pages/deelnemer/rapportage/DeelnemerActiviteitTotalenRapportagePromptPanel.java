package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;
import nl.topicus.eduarte.entities.participatie.enums.Schooljaar;
import nl.topicus.eduarte.krdparticipatie.web.components.combobox.AfspraakTypeCategorieMultipleCheckbox;
import nl.topicus.eduarte.participatie.zoekfilters.DeelnemerActiviteitTotalenZoekFilter;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratiePanel;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratieRegistratie;
import nl.topicus.eduarte.web.components.quicksearch.contract.ContractSearchEditor;
import nl.topicus.eduarte.web.pages.shared.RapportageConfiguratiePage;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

@RapportageConfiguratieRegistratie(naam = "activiteit", factoryType = DeelnemerActiviteitTotalenZoekFilter.class, configuratieType = DeelnemerActiviteitTotalenZoekFilter.class)
public class DeelnemerActiviteitTotalenRapportagePromptPanel extends Panel implements
		RapportageConfiguratiePanel<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private Form<DeelnemerActiviteitTotalenZoekFilter> form;

	private static DeelnemerActiviteitTotalenZoekFilter getDefaultFilter()
	{
		DeelnemerActiviteitTotalenZoekFilter filter = new DeelnemerActiviteitTotalenZoekFilter();
		filter.setBeginDatum(Schooljaar.getHuidigSchooljaar().getVanafDatum());
		filter.setEindDatum(TimeUtil.getInstance().currentDate());
		filter.setActiviteitenTonen(true);
		List<AfspraakTypeCategory> afspraakTypeCategoryList = new ArrayList<AfspraakTypeCategory>();
		afspraakTypeCategoryList.add(AfspraakTypeCategory.INDIVIDUEEL);
		afspraakTypeCategoryList.add(AfspraakTypeCategory.ROOSTER);
		filter.setAfspraakTypeCategories(afspraakTypeCategoryList);
		return filter;

	}

	@SuppressWarnings("unused")
	public DeelnemerActiviteitTotalenRapportagePromptPanel(String id,
			RapportageConfiguratiePage<Verbintenis, Verbintenis, VerbintenisZoekFilter> page)
	{
		super(id);
		form =
			new Form<DeelnemerActiviteitTotalenZoekFilter>("form",
				new CompoundPropertyModel<DeelnemerActiviteitTotalenZoekFilter>(getDefaultFilter()))
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
		form.add(new CheckBox("activiteitenTonen"));
		form.add(new ContractSearchEditor("contract", new PropertyModel<Contract>(form
			.getModelObject(), "contract")).setRequired(true));
		List<AfspraakTypeCategory> afspraakTypeCategoryList = new ArrayList<AfspraakTypeCategory>();
		afspraakTypeCategoryList.add(AfspraakTypeCategory.INDIVIDUEEL);
		afspraakTypeCategoryList.add(AfspraakTypeCategory.ROOSTER);
		form.add(new AfspraakTypeCategorieMultipleCheckbox("afspraakTypeCategories",
			afspraakTypeCategoryList));
	}

	@Override
	public DeelnemerActiviteitTotalenZoekFilter getConfiguratie()
	{
		DeelnemerActiviteitTotalenZoekFilter filter = form.getModelObject();
		return new ZoekFilterCopyManager().copyObject(filter);
	}

}
