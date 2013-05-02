package nl.topicus.eduarte.krdparticipatie.web.components.panels.rapportage;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.EditorClassModifier;
import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.participatie.helpers.LesweekIndelingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.entities.participatie.enums.AbsentiePresentieEnum;
import nl.topicus.eduarte.entities.participatie.enums.Schooljaar;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingWeergaveEnum;
import nl.topicus.eduarte.krdparticipatie.web.components.combobox.LesweekIndelingCombobox;
import nl.topicus.eduarte.participatie.zoekfilters.LesweekindelingZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingOverzichtZoekFilter;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratiePanel;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratieRegistratie;
import nl.topicus.eduarte.web.pages.shared.RapportageConfiguratiePage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;

@RapportageConfiguratieRegistratie(naam = "waarneming", factoryType = WaarnemingOverzichtZoekFilter.class, configuratieType = WaarnemingOverzichtZoekFilter.class)
public class JaaroverzichtRapportagePanel extends Panel implements
		RapportageConfiguratiePanel<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private AutoFieldSet<WaarnemingOverzichtZoekFilter> fieldset;

	private static final WaarnemingOverzichtZoekFilter getDefaultFilter(LesweekIndeling indeling)
	{
		WaarnemingOverzichtZoekFilter filter = new WaarnemingOverzichtZoekFilter();
		filter.setBeginDatum(Schooljaar.getHuidigSchooljaar().getVanafDatum());
		filter.setEindDatum(TimeUtil.getInstance().currentDate());
		filter.setAbsentieOfPresentie(AbsentiePresentieEnum.Absentie_en_Presentie);
		filter.setWaarnemingWeergave(WaarnemingWeergaveEnum.AlleenWaarneming);
		filter.setToonLegeRegels(true);
		filter.setToonTotalenKolommen(true);
		filter.setLesweekIndeling(indeling);
		return filter;
	}

	@SuppressWarnings("unused")
	public JaaroverzichtRapportagePanel(String id,
			RapportageConfiguratiePage<Verbintenis, Verbintenis, VerbintenisZoekFilter> page)
	{
		super(id);
		LesweekIndeling indeling =
			getDefaultLesweekIndeling(EduArteContext.get().getDefaultOrganisatieEenheid());
		if (indeling == null)
		{
			error("Er is geen lesweekindeling gevonden");
			add(new EmptyPanel("fieldset"));
		}
		else
		{
			fieldset =
				new AutoFieldSet<WaarnemingOverzichtZoekFilter>("fieldset",
					new CompoundPropertyModel<WaarnemingOverzichtZoekFilter>(
						getDefaultFilter(indeling)), "Jaaroverzicht rapportage");
			fieldset.setRenderMode(RenderMode.EDIT);
			fieldset.setPropertyNames("beginDatum", "eindDatum", "lesweekIndeling", "vanafLesuur",
				"totLesuur", "absentieOfPresentie", "waarnemingWeergave", "contract",
				"toonLegeRegels", "toonTotalenKolommen");
			fieldset.setSortAccordingToPropertyNames(true);
			fieldset.addFieldModifier(new RequiredModifier(true, "beginDatum", "eindDatum",
				"toonLegeRegels", "toonTotalenKolommen", "lesweekIndeling"));
			fieldset.addFieldModifier(new EditorClassModifier(LesweekIndelingCombobox.class,
				"lesweekIndeling"));
			IModel<LesweekIndeling> indelingModel =
				new PropertyModel<LesweekIndeling>(fieldset.getModelObject(), "lesweekIndeling");
			fieldset.addFieldModifier(new EduArteAjaxRefreshModifier("lesweekIndeling",
				"vanafLesuur", "totLesuur"));
			fieldset.addFieldModifier(new ConstructorArgModifier("vanafLesuur", indelingModel));
			fieldset.addFieldModifier(new ConstructorArgModifier("totLesuur", indelingModel));
			add(fieldset);
		}
	}

	private LesweekIndeling getDefaultLesweekIndeling(OrganisatieEenheid defaultOrganisatieEenheid)
	{
		LesweekIndelingDataAccessHelper helper =
			DataAccessRegistry.getHelper(LesweekIndelingDataAccessHelper.class);
		LesweekindelingZoekFilter filter = new LesweekindelingZoekFilter();
		filter.setOrganisatieEenheid(defaultOrganisatieEenheid);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new AlwaysGrantedSecurityCheck()));
		LesweekIndeling ret = helper.getlesweekIndeling(filter);
		if (ret != null)
			return ret;
		List<LesweekIndeling> retList = helper.list();
		if (!retList.isEmpty())
			return retList.get(0);
		return null;

	}

	@Override
	public WaarnemingOverzichtZoekFilter getConfiguratie()
	{
		WaarnemingOverzichtZoekFilter filter = fieldset.getModelObject();
		return new ZoekFilterCopyManager().copyObject(filter);
	}
}