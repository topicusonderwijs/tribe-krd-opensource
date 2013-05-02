package nl.topicus.eduarte.web.pages.beheer.vasco;

import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.security.authentication.vasco.Token;

import org.apache.wicket.model.IModel;

public class TokenEditModalWindow extends CobraModalWindow<Token>
{
	private static final long serialVersionUID = 1L;

	public TokenEditModalWindow(String id, IModel<Token> model)
	{
		super(id, model);
		setTitle("Vasco token toekennen");
		setInitialHeight(200);
		setInitialWidth(500);
		setResizable(false);
	}

	@Override
	protected CobraModalWindowBasePanel<Token> createContents(String id)
	{
		return new TokenEditModalWindowPanel(id, this);
	}
}
