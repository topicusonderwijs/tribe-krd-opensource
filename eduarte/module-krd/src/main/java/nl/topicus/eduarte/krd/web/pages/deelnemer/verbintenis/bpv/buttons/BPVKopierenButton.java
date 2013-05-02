package nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.bpv.buttons;

import nl.topicus.cobra.util.HibernateObjectCopyManager;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractPageBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.bpv.EditBPVInschrijvingPage;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

public class BPVKopierenButton extends AbstractPageBottomRowButton
{
	private static final long serialVersionUID = 1L;

	private IModel<Boolean> visibilityModel;

	public BPVKopierenButton(BottomRowPanel parent, final IModel<BPVInschrijving> bpvModel,
			final SecurePage returnPage, IModel<Boolean> visibilityModel)
	{
		super(parent, "BPV kopiëren", CobraKeyAction.GEEN, ButtonAlignment.LEFT,

		new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				BPVInschrijving oudeBPV = bpvModel.getObject();
				BPVInschrijving nieuweBPV =
					new HibernateObjectCopyManager(BPVInschrijving.class).copyObject(oudeBPV);
				nieuweBPV.setBegindatum(TimeUtil.getInstance().currentDate());
				oudeBPV.setEinddatum(TimeUtil.getInstance().yesterday());
				nieuweBPV.setEinddatum(null);
				nieuweBPV.setStatus(BPVStatus.Voorlopig);
				nieuweBPV.setIdInOudPakket(null);
				nieuweBPV.wijsNieuwVolgnummerToe();
				nieuweBPV.setBronDatum(null);
				nieuweBPV.setBronStatus(null);
				return new EditBPVInschrijvingPage(nieuweBPV, oudeBPV, returnPage);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return EditBPVInschrijvingPage.class;
			}

		});

		this.visibilityModel = visibilityModel;
	}

	@Override
	public boolean isVisible()
	{
		return super.isVisible() && (Boolean) visibilityModel.getObject();
	}
}
