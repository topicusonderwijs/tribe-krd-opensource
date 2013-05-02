package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.aanmaken;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.HibernateObjectCopyManager;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.cobra.web.components.link.ConfirmationLink;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenissenWrite;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.AbstractVerbintenisSelectiePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.link.Link;

/**
 * @author idserda
 */
@PageInfo(title = "Collectief aanmaken stap 2", menu = {"Deelnemer > Collecitief > Aanmaken"})
@InPrincipal(DeelnemerVerbintenissenWrite.class)
public class CollectiefAanmakenVerbintenisSelectiePage extends AbstractVerbintenisSelectiePage
		implements IEditPage
{
	private static final long serialVersionUID = 1L;

	public CollectiefAanmakenVerbintenisSelectiePage(CollectiefAanmakenModel model,
			SecurePage returnPage)
	{
		super(returnPage, getDefaultFilter(), getSelectieTarget(model));
	}

	private static VerbintenisZoekFilter getDefaultFilter()
	{
		VerbintenisZoekFilter filter = new VerbintenisZoekFilter();
		return filter;
	}

	private static AbstractSelectieTarget<Verbintenis, Verbintenis> getSelectieTarget(
			final CollectiefAanmakenModel model)
	{
		return new AbstractSelectieTarget<Verbintenis, Verbintenis>(
			CollectiefAanmakenOverzichtPage.class, "Aanmaken")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Link<Void> createLink(String linkId,
					final ISelectionComponent<Verbintenis, Verbintenis> base)
			{
				return new ConfirmationLink<Void>(linkId,
					"Weet u zeker dat u voor de geselecteerde items een nieuwe plaatsing wilt aanmaken?")
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick()
					{
						CollectiefAanmakenOverzichtPage overzichtPage =
							new CollectiefAanmakenOverzichtPage();

						/**
						 * Copy maken van het CollectiefAanmakenModel (en entiteiten
						 * hierin) om mee te geven aan de job. Op deze manier heeft de job
						 * een eigen model dat niet stiekem door de pagina gedetached kan
						 * worden voordat de job start.
						 */
						HibernateObjectCopyManager manager =
							new HibernateObjectCopyManager(Plaatsing.class, Verbintenis.class);

						Plaatsing plaatsingCopy = manager.copyObject(model.getNieuwePlaatsing());
						Verbintenis verbintenisCopy =
							manager.copyObject(model.getNieuweVerbintenis());

						CollectiefAanmakenModel modelCopy =
							new CollectiefAanmakenModel(model.getSoort());

						modelCopy.setNieuwePlaatsing(ModelFactory.getModel(plaatsingCopy,
							new DefaultModelManager(Plaatsing.class)));
						modelCopy.setNieuweVerbintenis(ModelFactory.getModel(verbintenisCopy,
							new DefaultModelManager(Verbintenis.class)));
						modelCopy.setOnderwijsproductAfnamesAanmaken(model
							.getOnderwijsproductAfnamesAanmaken());

						overzichtPage.startJob(new CollectiefAanmakenDataMap(base, modelCopy));
						setResponsePage(overzichtPage);
					}
				};
			}
		};
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new AnnulerenButton(panel, CollectiefAanmakenOverzichtPage.class));
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}
}