package nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs;

import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;

import org.apache.wicket.markup.html.link.Link;

public class OndPrKeuzeAangevenStap1Target extends AbstractSelectieTarget<Verbintenis, Verbintenis>
{
	private static final long serialVersionUID = 1L;

	public OndPrKeuzeAangevenStap1Target()
	{
		super(OndPrKeuzeAangevenStap2Page.class, "Volgende");
	}

	@Override
	public Link<Void> createLink(String linkId,
			final ISelectionComponent<Verbintenis, Verbintenis> base)
	{
		return new Link<Void>(linkId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				setResponsePage(new OndPrKeuzeAangevenStap2Page(base.getSelectedElements(),
					(OndPrKeuzeAangevenStap1Page) getPage()));
			}
		};
	}
}
