package nl.topicus.eduarte.krdparticipatie.web.components.tables;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieRedenDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.AbsentieReden;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.datapanels.columns.AbsentieRedenenColumn;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage.AbstractActiviteitTotaal;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieRedenZoekFilter;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

public class ActiviteitTotaalTable<T extends AbstractActiviteitTotaal> extends
		CustomDataPanelContentDescription<T>
{
	private static final long serialVersionUID = 1L;

	public ActiviteitTotaalTable(String title,
			AbstractOrganisatieEenheidLocatieZoekFilter< ? > baseFilter, String firstColumnHeader,
			boolean toonBudgetten)
	{
		super(title);
		addColumn(new CustomPropertyColumn<T>(firstColumnHeader, firstColumnHeader, "omschrijving"));
		addColumn(new CustomPropertyColumn<T>("Aanbod", "Aanbod", "aanbod"));
		addColumn(new CustomPropertyColumn<T>("Aanwezig", "Aanwezig", "aanwezig"));
		addColumn(new CustomPropertyColumn<T>("Afwezig", "Afwezig", "afwezig")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Geoorloofd afwezig", "Afw. Geoorl.",
			"geoorloofdAfwezig"));
		addColumn(new CustomPropertyColumn<T>("Ongeoorloofd afwezig", "Afw. Ong.",
			"ongeoorloofdAfwezig"));
		addColumn(new CustomPropertyColumn<T>("Procent aanwezig", "% Aanwezig", "procentAanwezig"));
		addColumn(new CustomPropertyColumn<T>("Procent afwezig", "% Afwezig", "procentAfwezig")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Procent geoorloofd afwezig", "% Geoorloofd afw",
			"procentGeoorloofdAfwezig").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Procent ongeoorloofd afwezig", "% Ongeoorloofd afw",
			"procentOngeoorloofdAfwezig").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Aanbod (lesuren)", "Aanbod (lesuren)",
			"aanbodLesuren").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Aanwezig (lesuren)", "Aanwezig (lesuren)",
			"aanwezigLesuren").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Afwezig (lesuren)", "Afwezig (lesuren)",
			"afwezigLesuren").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Geoorloofd afwezig (lesuren)",
			"Afw. Geoorl. (lesuren)", "geoorloofdAfwezigLesuren").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Ongeoorloofd afwezig (lesuren)",
			"Afw. Ong. (lesuren)", "ongeoorloofdAfwezigLesuren").setDefaultVisible(false));
		AbsentieRedenDataAccessHelper helper =
			DataAccessRegistry.getHelper(AbsentieRedenDataAccessHelper.class);
		AbsentieRedenZoekFilter filter = new AbsentieRedenZoekFilter();
		filter.setOrganisatieEenheid(baseFilter.getOrganisatieEenheid());
		filter.setActief(true);
		filter.setAuthorizationContext(baseFilter.getAuthorizationContext());
		List<AbsentieReden> absentieRedenen = helper.list(filter);
		for (AbsentieReden absentieReden : absentieRedenen)
		{
			addColumn(new AbsentieRedenenColumn<T>(absentieReden.getOmschrijving(), absentieReden
				.getOmschrijving(), absentieReden, false).setDefaultVisible(false));
			addColumn(new AbsentieRedenenColumn<T>(absentieReden.getOmschrijving() + " (lesuren)",
				absentieReden.getOmschrijving() + " (lesuren)", absentieReden, true)
				.setDefaultVisible(false));
		}
		addColumn(new CustomPropertyColumn<T>("Geen reden", "Geen reden", "geenReden")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Geen reden (lesuren)", "Geen reden (lesuren)",
			"geenRedenLesuren").setDefaultVisible(false));
		if (toonBudgetten)
		{
			addColumn(new CustomPropertyColumn<T>("Budget", "Budget", "budget"));
			addColumn(new CustomPropertyColumn<T>("Resterend", "Resterend", "resterend"));
		}
	}

}
