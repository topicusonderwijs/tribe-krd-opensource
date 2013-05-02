package nl.topicus.eduarte.krd.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.krd.zoekfilters.BronFotobestandZoekFilter;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class BronFotobestandZoekFilterPanel extends AutoZoekFilterPanel<BronFotobestandZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public BronFotobestandZoekFilterPanel(String id, BronFotobestandZoekFilter filter,
			IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("bestandsnaam", "status", "verwerkingsstatus", "type",
			"peildatumInFoto", "ingelezenDoor"));
	}
}
