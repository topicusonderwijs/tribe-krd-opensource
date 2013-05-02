package nl.topicus.cobra.web.components.panels.bottomrow;

import org.apache.wicket.markup.html.WebMarkupContainer;

public class ConfigurableButton extends AbstractBottomRowButton
{
	private static final long serialVersionUID = 1L;

	private ButtonCreator buttonCeator;

	public ConfigurableButton(BottomRowPanel bottomRow, ButtonCreator buttonCreator)
	{
		super(bottomRow, buttonCreator.getLabel(), buttonCreator.getAction(), buttonCreator
			.getAlignment());
		this.buttonCeator = buttonCreator;
	}

	@Override
	protected WebMarkupContainer getLink(String linkId)
	{
		return buttonCeator.createLink(linkId);
	}
}
