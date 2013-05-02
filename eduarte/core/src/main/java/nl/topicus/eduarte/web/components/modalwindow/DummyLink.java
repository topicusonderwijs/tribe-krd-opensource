package nl.topicus.eduarte.web.components.modalwindow;

import org.apache.wicket.markup.html.link.Link;

/**
 * Link die niets doet
 * 
 * @author hop
 */
public class DummyLink extends Link<Void>
{
	private static final long serialVersionUID = 1L;

	public DummyLink(String id)
	{
		super(id);
	}

	/**
	 * Doet niets
	 */
	@Override
	public void onClick()
	{
	}
}
