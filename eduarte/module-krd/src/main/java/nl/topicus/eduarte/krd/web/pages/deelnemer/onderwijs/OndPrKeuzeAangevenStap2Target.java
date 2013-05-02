package nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs;

import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;

import org.apache.wicket.markup.html.link.Link;

public class OndPrKeuzeAangevenStap2Target extends
		AbstractSelectieTarget<Productregel, Productregel>
{
	private static final long serialVersionUID = 1L;

	private OndPrKeuzeAangevenStap1Page stap1;

	public OndPrKeuzeAangevenStap2Target(OndPrKeuzeAangevenStap1Page stap1)
	{
		super(OndPrKeuzeAangevenStap3Page.class, "Volgende");
		this.stap1 = stap1;
	}

	@Override
	public Link<Void> createLink(String linkId,
			final ISelectionComponent<Productregel, Productregel> base)
	{
		return new Link<Void>(linkId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				setResponsePage(new OndPrKeuzeAangevenStap3Page(stap1.getSelectedElements(), base
					.getSelectedElements(), (SecurePage) getPage()));
			}
		};
	}
}
