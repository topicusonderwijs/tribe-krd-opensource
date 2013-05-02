package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.HtmlClassModifier;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.PropertyModel;

/**
 * @author idserda
 */
public class DeelnemerSelectieZoekFilterPanel extends AutoZoekFilterPanel<VerbintenisZoekFilter>
{
	public DeelnemerSelectieZoekFilterPanel(String id, VerbintenisZoekFilter filter,
			IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("peildatum", "deelnemernummer", "organisatieEenheid",
			"locatie", "opleiding", "groep", "cohort"));
		addFieldModifier(new OrganisatieEenheidLocatieFieldModifier());

		addFieldModifier(new LabelModifier("deelnemernummer", "Nr."));

		addFieldModifier(new HtmlClassModifier("unit_110", "opleiding"));
		if (isOpleidingRequired())
			addFieldModifier(new RequiredModifier(true, "opleiding"));

		OpleidingZoekFilter opleidingFilter = OpleidingZoekFilter.createDefaultFilter();
		opleidingFilter.setAuthorizationContext(filter.getAuthorizationContext());
		opleidingFilter.setOrganisatieEenheidModel(new PropertyModel<OrganisatieEenheid>(filter,
			"organisatieEenheid"));
		opleidingFilter.setLocatieModel(new PropertyModel<Locatie>(filter, "locatie"));
		addFieldModifier(new ConstructorArgModifier("opleiding", opleidingFilter));

		GroepZoekFilter groepFilter = GroepZoekFilter.createDefaultFilter();
		groepFilter.setAuthorizationContext(filter.getAuthorizationContext());
		groepFilter.setOrganisatieEenheidModel(new PropertyModel<OrganisatieEenheid>(filter,
			"organisatieEenheid"));
		groepFilter.setLocatieModel(new PropertyModel<Locatie>(filter, "locatie"));
		addFieldModifier(new ConstructorArgModifier(getGroepPropertyName(), groepFilter));
	}

	protected String getGroepPropertyName()
	{
		return "groep";
	}

	private static final long serialVersionUID = 1L;

	protected boolean isOpleidingRequired()
	{
		return false;
	}
}
