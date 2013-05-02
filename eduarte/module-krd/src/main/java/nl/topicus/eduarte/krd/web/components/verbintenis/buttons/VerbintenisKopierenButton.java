package nl.topicus.eduarte.krd.web.components.verbintenis.buttons;

import java.util.ArrayList;

import nl.topicus.cobra.util.HibernateObjectCopyManager;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractPageBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.EditVerbintenisPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.DeelnemerVerbintenisPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

public class VerbintenisKopierenButton extends AbstractPageBottomRowButton
{
	private static final long serialVersionUID = 1L;

	private IModel<Boolean> visibilityModel;

	public VerbintenisKopierenButton(BottomRowPanel parent,
			final IModel<Verbintenis> verbintenisModel, final DeelnemerVerbintenisPage returnPage,
			IModel<Boolean> visibilityModel)
	{
		super(parent, "Verbintenis kopiëren", CobraKeyAction.GEEN, ButtonAlignment.LEFT,

		new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				Verbintenis oudeVerbintenis = verbintenisModel.getObject();
				Verbintenis nieuweVerbintenis =
					new HibernateObjectCopyManager(Verbintenis.class).copyObject(oudeVerbintenis);
				nieuweVerbintenis.setBegindatum(TimeUtil.getInstance().currentDate());
				oudeVerbintenis.setEinddatum(TimeUtil.getInstance().yesterday());
				nieuweVerbintenis.setEinddatum(null);
				nieuweVerbintenis.setStatus(VerbintenisStatus.Voorlopig);
				nieuweVerbintenis.setIdInOudPakket(null);
				nieuweVerbintenis.wijsNieuwVolgnummerToe();
				nieuweVerbintenis.setVolgnummerInOudPakket(null);
				nieuweVerbintenis.setBronDatum(null);
				nieuweVerbintenis.setBronStatus(null);
				nieuweVerbintenis.setBekostigd(Bekostigd.Nee);
				nieuweVerbintenis.setBekostigingsperiodes(new ArrayList<Bekostigingsperiode>());
				// Lijst van plaatsingen leeg maken. Op EditVerbintenisPage wordt er een
				// nieuwe plaatsing bij deze verbintenis aangemaakt.
				nieuweVerbintenis.setPlaatsingen(new ArrayList<Plaatsing>());
				return new EditVerbintenisPage(nieuweVerbintenis, oudeVerbintenis, returnPage, true);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return EditVerbintenisPage.class;
			}

		});

		this.visibilityModel = visibilityModel;
	}

	@Override
	public boolean isVisible()
	{
		return super.isVisible() && visibilityModel.getObject();
	}
}
