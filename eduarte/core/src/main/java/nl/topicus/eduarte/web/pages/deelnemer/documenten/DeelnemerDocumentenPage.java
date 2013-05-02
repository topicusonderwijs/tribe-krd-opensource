/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.deelnemer.documenten;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.core.principals.deelnemer.verbintenis.DeelnemerVerbintenisDocumentInzien;
import nl.topicus.eduarte.core.principals.deelnemer.verbintenis.DeelnemerVerbintenisDocumentenRead;
import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.DeelnemerBijlage;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.DocumentenPanel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.groep.AbstractGroepPage;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;

/**
 * @author hoeve
 */
@PageInfo(title = "Documenten", menu = "Deelnemer > Documenten")
@InPrincipal(DeelnemerVerbintenisDocumentenRead.class)
public class DeelnemerDocumentenPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	/**
	 * Bookmarkable constructor.
	 * 
	 * @see AbstractGroepPage#getGroepFromPageParameters(PageParameters)
	 */
	public DeelnemerDocumentenPage(PageParameters parameters)
	{
		this(AbstractDeelnemerPage.getDeelnemerFromPageParameters(DeelnemerDocumentenPage.class,
			parameters));
	}

	public DeelnemerDocumentenPage(DeelnemerProvider deelnemer)
	{
		this(deelnemer.getDeelnemer());
	}

	public DeelnemerDocumentenPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum());
	}

	public DeelnemerDocumentenPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		super(DeelnemerMenuItem.Documenten, deelnemer, verbintenis);

		DocumentenPanel<DeelnemerBijlage, Deelnemer> detailPanel =
			new DocumentenPanel<DeelnemerBijlage, Deelnemer>("detailPanel",
				getContextDeelnemerModel(), false)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected CustomDataPanelRowFactory<BijlageEntiteit> createRowFactory()
				{
					return new CustomDataPanelPageLinkRowFactory<BijlageEntiteit>(
						EditDeelnemerDocumentPage.class);
				}

				@Override
				protected ISecurityCheck getBijlageLinkSecurityCheck()
				{
					return new DeelnemerSecurityCheck(new DataSecurityCheck(
						DeelnemerVerbintenisDocumentInzien.DEELNEMER_VERBINTENIS_DOCUMENT_INZIEN),
						getContextDeelnemer());
				}
			};
		detailPanel.setOutputMarkupId(true);
		add(detailPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.setOutputMarkupId(true);
		panel.addButton(new ToevoegenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new EditDeelnemerDocumentPage(getContextDeelnemer(),
					getContextVerbintenis(), DeelnemerDocumentenPage.this);
			}

			@Override
			public Class<EditDeelnemerDocumentPage> getPageIdentity()
			{
				return EditDeelnemerDocumentPage.class;
			}

		}));
	}
}
