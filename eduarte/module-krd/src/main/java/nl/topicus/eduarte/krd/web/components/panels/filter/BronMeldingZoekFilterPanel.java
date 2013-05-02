package nl.topicus.eduarte.krd.web.components.panels.filter;

import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class BronMeldingZoekFilterPanel extends AutoZoekFilterPanel<BronMeldingZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public BronMeldingZoekFilterPanel(String id, final BronMeldingZoekFilter filter,
			IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("aanleverpunt", "schooljaar", "bronOnderwijssoort",
			"batchnummer", "deelnemer", "bronMeldingOnderdeel", "typeMelding", "creboIltCode",
			"soortMutatie", "meldingStatus", "geblokkeerd", "organisatieEenheid", "locatie"));
		addFieldModifier(new OrganisatieEenheidLocatieFieldModifier());
		addFieldModifier(new EnableModifier(false, "aanleverpunt", "schooljaar"));

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
