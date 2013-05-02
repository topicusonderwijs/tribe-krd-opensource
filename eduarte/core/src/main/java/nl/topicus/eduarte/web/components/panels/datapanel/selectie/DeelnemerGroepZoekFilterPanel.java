package nl.topicus.eduarte.web.components.panels.datapanel.selectie;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * 
 * 
 * @author vanharen
 */
public class DeelnemerGroepZoekFilterPanel extends AutoZoekFilterPanel<VerbintenisZoekFilter>
{

	private static final long serialVersionUID = 1L;

	public DeelnemerGroepZoekFilterPanel(String id, VerbintenisZoekFilter zoekfilter,
			IPageable pageable)
	{
		super(id, zoekfilter, pageable);
		setPropertyNames(Arrays.asList("opleiding", "cohort"));
		addFieldModifier(new ConstructorArgModifier("opleiding", zoekfilter
			.getOpleidingZoekFilter()));
	}

}
