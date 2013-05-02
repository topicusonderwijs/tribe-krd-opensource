package nl.topicus.eduarte.web.pages.beheer.vasco;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ExtendedHibernateModel;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.core.principals.beheer.systeem.VascoTokensBeheren;
import nl.topicus.eduarte.dao.helpers.vasco.TokenDataAccessHelper;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.security.authentication.vasco.Token;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.filter.vasco.TokenZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.vasco.TokenZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Vasco tokens organisatie overzicht", menu = "Beheer > Gebruikers > Vasco tokens")
@InPrincipal(VascoTokensBeheren.class)
public class TokensOverzichtPage extends AbstractBeheerPage<Void>
{
	private TokenEditModalWindow editWindow;

	private EduArteDataPanel<Token> datapanel;

	public TokensOverzichtPage()
	{
		super(BeheerMenuItem.Gebruikers);

		TokenZoekFilter zoekfilter = new TokenZoekFilter();
		IDataProvider<Token> provider =
			GeneralFilteredSortableDataProvider.of(zoekfilter, TokenDataAccessHelper.class);
		datapanel = new EduArteDataPanel<Token>("tokens", provider, new TokenTable());
		datapanel.setRowFactory(new CustomDataPanelAjaxClickableRowFactory<Token>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<Token> item)
			{
				editWindow.setDefaultModelObject(item.getModelObject());
				editWindow.show(target);
			}
		});
		add(datapanel);
		add(new TokenZoekFilterPanel("filter", zoekfilter, datapanel));

		editWindow =
			new TokenEditModalWindow("editor", new ExtendedHibernateModel<Token>(null,
				new DefaultModelManager(Token.class)));
		editWindow.setWindowClosedCallback(new WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				Object selected = editWindow.getDefaultModelObject();
				if (selected instanceof Entiteit)
				{
					Entiteit selectedEntity = (Entiteit) selected;
					if (selectedEntity.isSaved())
						selectedEntity.refresh();
				}
				target.addComponent(datapanel);
			}
		});

		add(editWindow);

		createComponents();
	}

	/**
	 * CustomDataPanel descriptor voor {@link Token} entiteiten op landelijk niveau. Deze
	 * tokens mogen uitgegeven zijn aan organisaties of gebruikers.
	 */
	public class TokenTable extends CustomDataPanelContentDescription<Token>
	{
		private static final long serialVersionUID = 1L;

		public TokenTable()
		{
			super("Tokens");
			addColumn(new CustomPropertyColumn<Token>("Serienummer", "Serienummer", "serienummer",
				"serienummer"));
			addColumn(new CustomPropertyColumn<Token>("Gebruikersnaam", "Gebruikersnaam",
				"gebruiker.gebruikersnaam", "gebruiker.gebruikersnaam"));
			addColumn(new CustomPropertyColumn<Token>("Gebruiker", "Gebruiker",
				"gebruiker.eigenaar.achternaamVoorletters"));
			addColumn(new CustomPropertyColumn<Token>("Status", "Status", "status", "status"));
		}
	}
}
