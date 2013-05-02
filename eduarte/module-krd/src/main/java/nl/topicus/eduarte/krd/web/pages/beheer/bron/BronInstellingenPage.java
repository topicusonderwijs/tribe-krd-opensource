package nl.topicus.eduarte.krd.web.pages.beheer.bron;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractAjaxLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxActieButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.principals.deelnemer.bron.DeelnemerBronInstellingenWrite;
import nl.topicus.eduarte.krd.web.components.modalwindow.bron.AanleverpuntLocatieToevoegenModalWindow;
import nl.topicus.eduarte.krd.web.components.modalwindow.bron.NieuwAanleverpuntModalWindow;
import nl.topicus.eduarte.web.components.menu.BronMenuItem;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.model.IModel;

@PageInfo(title = "BRON Instellingen", menu = "Deelnemer")
@InPrincipal(DeelnemerBronInstellingenWrite.class)
public class BronInstellingenPage extends AbstractBronPage
{
	private NieuwAanleverpuntModalWindow aanleverpuntModalWindow;

	private AanleverpuntLocatieToevoegenModalWindow aanleverpuntLocatieModalWindow;

	private BronInrichtingAanleverpuntenPanel aanleverpuntenPanel;

	public BronInstellingenPage()
	{
		aanleverpuntenPanel = new BronInrichtingAanleverpuntenPanel("aanleverpuntenPanel");
		aanleverpuntenPanel.setOutputMarkupId(true);
		add(aanleverpuntenPanel);
		aanleverpuntModalWindow =
			new NieuwAanleverpuntModalWindow("aanleverpuntWindow", getNieuwModel());
		aanleverpuntModalWindow.setWindowClosedCallback(new WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				target.addComponent(aanleverpuntenPanel);
				aanleverpuntLocatieModalWindow.show(target);
			}
		});
		add(aanleverpuntModalWindow);
		aanleverpuntLocatieModalWindow =
			new AanleverpuntLocatieToevoegenModalWindow("aanleverpuntLocatieWindow");
		aanleverpuntLocatieModalWindow.setWindowClosedCallback(new WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				target.addComponent(aanleverpuntenPanel);
			}
		});
		add(aanleverpuntLocatieModalWindow);
		createComponents();
	}

	private IModel<BronAanleverpunt> getNieuwModel()
	{
		return ModelFactory.getCompoundChangeRecordingModel(new BronAanleverpunt(),
			new DefaultModelManager(BronAanleverpunt.class));
	}

	@Override
	protected MenuItemKey getSelectedMenuBarItem()
	{
		return BronMenuItem.Instellingen;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AjaxActieButton(panel, "Nieuw aanleverpunt")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				aanleverpuntModalWindow.show(target);
			}
		});
		panel.addButton(new AbstractAjaxLinkButton(panel, "Locatie toevoegen",
			CobraKeyAction.TOEVOEGEN)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				aanleverpuntLocatieModalWindow.show(target);
			}
		});
	}
}
