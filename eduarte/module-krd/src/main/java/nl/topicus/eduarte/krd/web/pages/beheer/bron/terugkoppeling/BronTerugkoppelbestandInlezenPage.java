package nl.topicus.eduarte.krd.web.pages.beheer.bron.terugkoppeling;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxActieButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.krd.bron.jobs.BronTerugkoppelbestandInlezenJob;
import nl.topicus.eduarte.krd.entities.bron.BronTerugkoppelbestandInlezenJobRun;
import nl.topicus.eduarte.krd.principals.deelnemer.bron.DeelnemerBronTerugkoppelbestandInlezen;
import nl.topicus.eduarte.krd.web.components.modalwindow.bron.BronTerugkoppelbestandInlezenModalWindow;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronAlgemeenPage;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.BronMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.quartz.JobDataMap;

@PageInfo(title = "BRON Terugkoppelbestand inlezen", menu = "Deelnemer")
@InPrincipal(DeelnemerBronTerugkoppelbestandInlezen.class)
public class BronTerugkoppelbestandInlezenPage extends
		AbstractJobBeheerPage<BronTerugkoppelbestandInlezenJobRun>
{
	private static final long serialVersionUID = 1L;

	private BronTerugkoppelbestandInlezenModalWindow modalWindowTerugkoppelingInlezen;

	public BronTerugkoppelbestandInlezenPage()
	{
		super(CoreMainMenuItem.Beheer, BronTerugkoppelbestandInlezenJob.class, null);

		addModalWindowTerugkoppeling();
	}

	private void addModalWindowTerugkoppeling()
	{
		modalWindowTerugkoppelingInlezen =
			new BronTerugkoppelbestandInlezenModalWindow("modalWindowTerugkoppeling");
		modalWindowTerugkoppelingInlezen.setWindowClosedCallback(new WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
			}
		});
		add(modalWindowTerugkoppelingInlezen);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return null;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new BeheerMenu(id, BronMenuItem.BRON);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new TerugButton(panel, BronAlgemeenPage.class));
	}

	@Override
	protected String getTaakStartenButtonTekst()
	{
		return "Bestand inlezen";
	}

	@Override
	protected AbstractBottomRowButton createTaakStartenButton(BottomRowPanel panel)
	{
		return new AjaxActieButton(panel, getTaakStartenButtonTekst())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				modalWindowTerugkoppelingInlezen.show(target);
			}
		};
	}
}
