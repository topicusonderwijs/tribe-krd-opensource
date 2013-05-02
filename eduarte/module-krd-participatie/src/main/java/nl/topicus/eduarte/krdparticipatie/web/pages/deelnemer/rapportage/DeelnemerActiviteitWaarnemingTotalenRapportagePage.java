package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.fileresources.PdfFileResourceStream;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krdparticipatie.principals.deelnemer.DeelnemerTotalenPerOnderwijsproductRapportage;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.filter.DeelnemerActiviteitTotalenPanel;
import nl.topicus.eduarte.participatie.zoekfilters.DeelnemerActiviteitTotalenZoekFilter;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.GroepCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.GroepCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pagina voor het maken van activiteit totalen rapportages voor deelnemers
 * 
 * @author vandekamp
 */
@PageInfo(title = "Deelnemeractiviteittotalen", menu = {
	"Deelnemer > Rapportage > Totalen per activiteit > [deelnemer]",
	"Groep > Rapportage > Totalen per activiteit > [Groep]"})
@InPrincipal(DeelnemerTotalenPerOnderwijsproductRapportage.class)
public class DeelnemerActiviteitWaarnemingTotalenRapportagePage extends SecurePage
{
	private static final Logger log =
		LoggerFactory.getLogger(DeelnemerActiviteitWaarnemingTotalenRapportagePage.class);

	private Form<Void> form;

	private MenuItemKey selectedMenuItem;

	private List<DeelnemerActiviteitTotalenPanel> panels =
		new ArrayList<DeelnemerActiviteitTotalenPanel>();

	public DeelnemerActiviteitWaarnemingTotalenRapportagePage(List<Verbintenis> verbintenissen,
			DeelnemerActiviteitTotalenZoekFilter filter, CoreMainMenuItem coreMainMenuItem)
	{
		super(coreMainMenuItem);
		if (coreMainMenuItem.equals(CoreMainMenuItem.Groep))
			this.selectedMenuItem = GroepCollectiefMenuItem.Rapportages;
		else
			this.selectedMenuItem = DeelnemerCollectiefMenuItem.Rapportages;

		form = new Form<Void>("form")
		{
			private static final long serialVersionUID = 1L;

			@SuppressWarnings( {"unchecked", "null"})
			@Override
			protected void onSubmit()
			{
				boolean first = true;
				JasperPrint print = null;
				for (DeelnemerActiviteitTotalenPanel panel : panels)
				{
					if (first)
					{
						JasperPrint jp = panel.getJasperPrint();
						if (jp != null)
						{
							print = jp;
							first = false;
						}
					}
					else
					{
						JasperPrint jp = panel.getJasperPrint();
						if (jp != null)
						{
							List<JRPrintPage> pages = jp.getPages();
							for (JRPrintPage page : pages)
							{
								print.addPage(page);
							}
						}
					}
				}
				try
				{
					File file = File.createTempFile("lijst", ".pdf");
					JasperExportManager.exportReportToPdfFile(print, file.getPath());
					PdfFileResourceStream stream =
						new PdfFileResourceStream(file, TimeUtil.getInstance().currentDateTime(),
							"Lijst afdrukken");
					((WebResponse) getRequestCycle().getResponse()).setHeader(
						"Content-Disposition", "attachment; filename=\"" + stream.getFileName()
							+ "\"");
					getRequestCycle().setRequestTarget(new ResourceStreamRequestTarget(stream));
					getRequestCycle().setRedirect(false);
				}
				catch (JRException e)
				{
					log.error(e.toString(), e);
				}
				catch (IOException e)
				{
					log.error(e.toString(), e);
				}
			}
		};
		add(form);

		RepeatingView totalen = new RepeatingView("totalen");
		for (Verbintenis verbintenis : verbintenissen)
		{
			DeelnemerActiviteitTotalenPanel panel =
				new DeelnemerActiviteitTotalenPanel(verbintenis.getId().toString(), verbintenis,
					filter);
			totalen.add(panel);
			panels.add(panel);
		}
		add(totalen);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form, "PDF genereren"));
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		if (DeelnemerCollectiefMenuItem.Rapportages.equals(selectedMenuItem))
		{
			return new DeelnemerCollectiefMenu(id, selectedMenuItem);
		}
		return new GroepCollectiefMenu(id, selectedMenuItem);
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, "Deelnemer activiteit totalen");
	}
}
