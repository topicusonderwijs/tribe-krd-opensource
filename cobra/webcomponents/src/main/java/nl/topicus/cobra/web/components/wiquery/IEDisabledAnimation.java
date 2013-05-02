package nl.topicus.cobra.web.components.wiquery;

import org.odlabs.wiquery.ui.accordion.AccordionAnimated;

public class IEDisabledAnimation extends AccordionAnimated
{
	private static final long serialVersionUID = 1L;

	public IEDisabledAnimation()
	{
		super("jQuery.browser.msie ? false : 'slide'");
	}

	@Override
	public CharSequence getJavascriptOption()
	{
		if (getEffectPAram() == null)
		{
			throw new IllegalArgumentException(
				"The AccordionAnimated must have one not null parameter");
		}

		CharSequence sequence = null;

		if (getEffectPAram() != null)
		{
			sequence = getEffectPAram();
		}
		else
		{
			throw new IllegalArgumentException(
				"The AccordionAnimated must have one not null parameter");
		}

		return sequence;
	}
}
