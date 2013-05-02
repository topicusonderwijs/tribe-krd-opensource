package nl.topicus.cobra.web.components.panels.bottomrow;

import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;

public abstract class AjaxActieButton extends AbstractAjaxLinkButton
{
	private static final long serialVersionUID = 1L;

	public AjaxActieButton(BottomRowPanel bottomRow, String label)
	{
		super(bottomRow, label, CobraKeyAction.LINKKNOP1, ButtonAlignment.RIGHT);
	}
}
