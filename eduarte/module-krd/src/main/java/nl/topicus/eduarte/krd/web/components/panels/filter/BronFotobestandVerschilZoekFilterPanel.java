package nl.topicus.eduarte.krd.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.krd.zoekfilters.BronFotobestandVerschilZoekFilter;
import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.PropertyModel;

public class BronFotobestandVerschilZoekFilterPanel extends
		AutoZoekFilterPanel<BronFotobestandVerschilZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public BronFotobestandVerschilZoekFilterPanel(String id,
			BronFotobestandVerschilZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("peildatum", "teldatum", "verschil", "achternaam", "pgn",
			"organisatieEenheid", "locatie"));
		addFieldModifier(new OrganisatieEenheidLocatieFieldModifier());
		OpleidingZoekFilter opleidingFilter = OpleidingZoekFilter.createDefaultFilter();
		opleidingFilter.setAuthorizationContext(filter.getAuthorizationContext());
		opleidingFilter.setOrganisatieEenheidModel(new PropertyModel<OrganisatieEenheid>(filter,
			"organisatieEenheid"));
		opleidingFilter.setLocatieModel(new PropertyModel<Locatie>(filter, "locatie"));
		addFieldModifier(new ConstructorArgModifier("opleiding", opleidingFilter));
	}

}
