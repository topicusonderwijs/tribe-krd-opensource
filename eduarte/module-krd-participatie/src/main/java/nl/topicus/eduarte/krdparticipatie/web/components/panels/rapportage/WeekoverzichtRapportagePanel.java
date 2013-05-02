package nl.topicus.eduarte.krdparticipatie.web.components.panels.rapportage;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.participatie.zoekfilters.AanwezigheidsWeekZoekFilter;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratiePanel;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratieRegistratie;
import nl.topicus.eduarte.web.pages.shared.RapportageConfiguratiePage;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

@RapportageConfiguratieRegistratie(naam = "aanwezigheid", factoryType = AanwezigheidsWeekZoekFilter.class, configuratieType = AanwezigheidsWeekZoekFilter.class)
public class WeekoverzichtRapportagePanel extends Panel implements
		RapportageConfiguratiePanel<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private AutoFieldSet<AanwezigheidsWeekZoekFilter> fieldset;

	@SuppressWarnings("unused")
	public WeekoverzichtRapportagePanel(String id,
			RapportageConfiguratiePage<Verbintenis, Verbintenis, VerbintenisZoekFilter> page)
	{
		super(id);
		fieldset =
			new AutoFieldSet<AanwezigheidsWeekZoekFilter>("fieldset",
				new CompoundPropertyModel<AanwezigheidsWeekZoekFilter>(
					new AanwezigheidsWeekZoekFilter()), "Weekoverzicht rapportage");
		fieldset.setRenderMode(RenderMode.EDIT);
		fieldset.setPropertyNames("jaar", "beginWeek", "eindWeek");
		fieldset.setSortAccordingToPropertyNames(true);
		add(fieldset);
	}

	@Override
	public AanwezigheidsWeekZoekFilter getConfiguratie()
	{
		AanwezigheidsWeekZoekFilter filter = fieldset.getModelObject();
		return new ZoekFilterCopyManager().copyObject(filter);
	}
}