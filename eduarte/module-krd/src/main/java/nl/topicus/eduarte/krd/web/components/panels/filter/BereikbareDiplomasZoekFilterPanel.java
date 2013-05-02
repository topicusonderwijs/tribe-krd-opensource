package nl.topicus.eduarte.krd.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.krd.zoekfilters.BereikbareDiplomasZoekFilter;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.TaxonomieElementZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class BereikbareDiplomasZoekFilterPanel extends
		AutoZoekFilterPanel<BereikbareDiplomasZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public BereikbareDiplomasZoekFilterPanel(String id, BereikbareDiplomasZoekFilter filter,
			IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("peildatum", "code", "naam", "taxonomiecode",
			"verbintenisgebied", "geslaagd"));
		addFieldModifier(new ConstructorArgModifier("verbintenisgebied",
			new TaxonomieElementZoekFilter(Verbintenisgebied.class)));
	}

}
