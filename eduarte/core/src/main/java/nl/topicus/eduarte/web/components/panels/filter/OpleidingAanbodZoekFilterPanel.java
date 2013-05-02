package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.zoekfilters.OpleidingAanbodZoekFilter;
import nl.topicus.eduarte.zoekfilters.TaxonomieElementZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * 
 * 
 * @author vanharen
 */
public class OpleidingAanbodZoekFilterPanel extends AutoZoekFilterPanel<OpleidingAanbodZoekFilter>
{

	private static final long serialVersionUID = 1L;

	public OpleidingAanbodZoekFilterPanel(String id, OpleidingAanbodZoekFilter zoekfilter,
			IPageable pageable)
	{
		super(id, zoekfilter, pageable);
		setPropertyNames(Arrays.asList("peildatum", "code", "opleidingNaam", "leerweg",
			"taxonomiecode", "verbintenisgebied"));
		addFieldModifier(new ConstructorArgModifier("verbintenisgebied",
			new TaxonomieElementZoekFilter(Verbintenisgebied.class)));
	}

}
