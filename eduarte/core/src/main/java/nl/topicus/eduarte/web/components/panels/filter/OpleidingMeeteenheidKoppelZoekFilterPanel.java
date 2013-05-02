package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;
import nl.topicus.eduarte.zoekfilters.TaxonomieElementZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * 
 * 
 * @author vanharen
 */
public class OpleidingMeeteenheidKoppelZoekFilterPanel extends
		AutoZoekFilterPanel<OpleidingZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public OpleidingMeeteenheidKoppelZoekFilterPanel(String id, OpleidingZoekFilter filter,
			IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("code", "naam", "leerweg", "verbintenisgebied",
			"maximumAanwezig", "cohort"));
		addFieldModifier(new ConstructorArgModifier("verbintenisgebied",
			new TaxonomieElementZoekFilter(Verbintenisgebied.class)));
	}
}
