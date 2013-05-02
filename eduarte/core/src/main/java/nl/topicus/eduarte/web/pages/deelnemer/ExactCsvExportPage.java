package nl.topicus.eduarte.web.pages.deelnemer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.eduarte.core.principals.deelnemer.ExactCsvExport;
import nl.topicus.eduarte.entities.jobs.ExactCsvExportJobRun;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.jobs.csvexport.ExactCsvExportDataMap;
import nl.topicus.eduarte.jobs.csvexport.ExactCsvExportJob;
import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;

import org.apache.wicket.model.PropertyModel;
import org.quartz.JobDataMap;

@PageInfo(title = "Exact csv export", menu = "Deelnemer > Rapportages > Exact csv export")
@InPrincipal(ExactCsvExport.class)
public class ExactCsvExportPage extends AbstractJobBeheerPage<ExactCsvExportJobRun>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public ExactCsvExportPage()
	{
		super(CoreMainMenuItem.Deelnemer, ExactCsvExportJob.class, "");
		AutoFieldSet<JobDataMap> fieldset = getJobPanel().getAutoFieldSet();
		fieldset.addFieldModifier(new OrganisatieEenheidLocatieFieldModifier());
		OpleidingZoekFilter opleidingFilter = OpleidingZoekFilter.createDefaultFilter();
		// opleidingFilter.setAuthorizationContext(filter.getAuthorizationContext());
		opleidingFilter.setOrganisatieEenheidModel(new PropertyModel<OrganisatieEenheid>(fieldset
			.getModel(), "organisatieEenheid"));
		opleidingFilter.setLocatieModel(new PropertyModel<Locatie>(fieldset.getModel(), "locatie"));
		fieldset.addFieldModifier(new ConstructorArgModifier("opleiding", opleidingFilter));

	}

	@Override
	protected JobDataMap createDataMap()
	{
		return new ExactCsvExportDataMap();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.ExactCsvRapportage);
	}
}
