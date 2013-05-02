package nl.topicus.eduarte.krd.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.krd.zoekfilters.BronCfiTerugmeldingZoekFilter;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class BronCfiTerugmeldingZoekFilterPanel extends
		AutoZoekFilterPanel<BronCfiTerugmeldingZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public BronCfiTerugmeldingZoekFilterPanel(String id, BronCfiTerugmeldingZoekFilter filter,
			IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("bestandsnaam", "status", "type", "peildatumInBestand",
			"ingelezenDoor"));
	}

}
