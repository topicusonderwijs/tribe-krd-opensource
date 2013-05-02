package nl.topicus.eduarte.web.pages.deelnemer.documenten;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.core.principals.deelnemer.verbintenis.DeelnemerVerbintenisDocumenten;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.TypeBijlage;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.DeelnemerBijlage;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.bijlage.BijlageEditPanel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.Page;

@PageInfo(title = "Documenten", menu = "Deelnemer > Documenten > [document] > Bewerken")
@InPrincipal(DeelnemerVerbintenisDocumenten.class)
public class EditDeelnemerDocumentPage extends AbstractDeelnemerPage implements IEditPage
{

	private final BijlageEditPanel bijlagePanel;

	private final Page returnToPage;

	private static final DeelnemerBijlage getDefaultDeelnemerBijlage(Deelnemer deelnemer)
	{
		DeelnemerBijlage bijlage = new DeelnemerBijlage();
		bijlage.setDeelnemer(deelnemer);
		bijlage.setBijlage(new Bijlage());
		bijlage.getBijlage().setTypeBijlage(TypeBijlage.Overig);
		return bijlage;
	}

	public EditDeelnemerDocumentPage(Deelnemer deelnemer, Verbintenis verbintenis, Page returnToPage)
	{
		this(deelnemer, verbintenis, getDefaultDeelnemerBijlage(deelnemer), returnToPage);
	}

	public EditDeelnemerDocumentPage(DeelnemerBijlage bijlage, Page returnToPage)
	{
		this(bijlage.getDeelnemer(), bijlage.getDeelnemer().getEersteInschrijvingOpPeildatum(true),
			bijlage, returnToPage);
	}

	public EditDeelnemerDocumentPage(Deelnemer deelnemer, Verbintenis verbintenis,
			DeelnemerBijlage bijlage, Page returnToPage)
	{
		super(DeelnemerMenuItem.Documenten, deelnemer, verbintenis);
		this.returnToPage = returnToPage;
		if (bijlage.getBijlage().getOntvangstdatum() == null)
			bijlage.getBijlage().setOntvangstdatum(TimeUtil.getInstance().currentDate());
		bijlagePanel = new BijlageEditPanel("bijlagePanel", bijlage, returnToPage);
		add(bijlagePanel);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, bijlagePanel.getForm()));
		panel.addButton(new AnnulerenButton(panel, returnToPage));
		panel.addButton(new VerwijderButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				BijlageEntiteit bijlageEntiteit = bijlagePanel.getForm().getModelObject();
				Bijlage bijlage = bijlageEntiteit.getBijlage();
				bijlageEntiteit.delete();
				bijlage.delete();
				bijlage.commit();
				setResponsePage(new DeelnemerDocumentenPage(getContextDeelnemer(),
					getContextVerbintenis()));
			}

			@Override
			public boolean isVisible()
			{
				BijlageEntiteit bijlageEntiteit = bijlagePanel.getForm().getModelObject();
				return bijlageEntiteit.isSaved();
			}
		});
	}

}
