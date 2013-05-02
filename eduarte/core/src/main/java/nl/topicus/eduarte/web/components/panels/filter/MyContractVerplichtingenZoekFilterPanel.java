package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.eduarte.zoekfilters.ContractVerplichtingZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * 
 * 
 * @author vanharen
 */
public class MyContractVerplichtingenZoekFilterPanel extends
		AutoZoekFilterPanel<ContractVerplichtingZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public MyContractVerplichtingenZoekFilterPanel(String id,
			ContractVerplichtingZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);

		setPropertyNames(Arrays.asList("beginDatum", "externeOrganisatie", "uitgevoerd", "naam",
			"code"));

		addFieldModifier(new RequiredModifier(false, "uitgevoerd"));

		addFieldModifier(new LabelModifier("beginDatum", "Datum vanaf"));
		addFieldModifier(new LabelModifier("uitgevoerd", "Uitgevoerd"));
		addFieldModifier(new LabelModifier("naam", "Contractnaam"));
		addFieldModifier(new LabelModifier("code", "Contractcode"));

	}
}