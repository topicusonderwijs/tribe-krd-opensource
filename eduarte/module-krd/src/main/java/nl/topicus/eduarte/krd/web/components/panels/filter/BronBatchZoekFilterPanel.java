package nl.topicus.eduarte.krd.web.components.panels.filter;

import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.krd.zoekfilters.BronBatchZoekFilter;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class BronBatchZoekFilterPanel extends AutoZoekFilterPanel<BronBatchZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public BronBatchZoekFilterPanel(String id, final BronBatchZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("bronAanleverpunt", "schooljaar", "onderwijssoort",
			"batchnummer", "heeftMeldingenInBehandeling"));
		addFieldModifier(new ConstructorArgModifier("schooljaar",
			new AbstractReadOnlyModel<List<Schooljaar>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public List<Schooljaar> getObject()
				{
					return filter.getSchooljaarList();
				}
			}));
	}

}
