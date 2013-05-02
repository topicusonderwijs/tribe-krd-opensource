package nl.topicus.cobra.web.components.panels.bottomrow;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * Panel waarin context-specifieke knoppen terechtkomen die onderaan de pagina worden
 * getoond. Voorbeelden zijn: bewerken, opslaan, etc.
 */
public class BottomRowPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private int buttonCount = 0;

	private RepeatingView leftButtons;

	private RepeatingView rightButtons;

	public BottomRowPanel(String id)
	{
		super(id);

		add(leftButtons = new RepeatingView("leftButtons"));
		add(rightButtons = new RepeatingView("rightButtons"));
	}

	public String getNextButtonId()
	{
		return Integer.toString(buttonCount++);
	}

	public void addButton(AbstractBottomRowButton button)
	{
		if (ButtonAlignment.LEFT.equals(button.getAlignment()))
			leftButtons.add(button);
		else
			rightButtons.add(button);
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();

		if (Application.DEVELOPMENT.equals(getApplication().getConfigurationType()))
		{
			visitChildren(new IVisitor<Component>()
			{
				@Override
				public Object component(Component component)
				{
					if (component instanceof AbstractBottomRowButton)
					{
						throw new WicketRuntimeException("Button " + component
							+ " direct aan panel toegevoegd ipv via BottomRowPanel#addButton");
					}
					return IVisitor.CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER;
				}
			});
		}
	}
}
