package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;

import org.apache.wicket.markup.html.link.Link;

public class CriteriaKopierenStap1Target extends AbstractSelectieTarget<Criterium, Criterium>
{
	private static final long serialVersionUID = 1L;

	public CriteriaKopierenStap1Target()
	{
		super(CriteriaKopierenStap2Page.class, "Volgende");
	}

	@Override
	public Link<Void> createLink(String linkId, final ISelectionComponent<Criterium, Criterium> base)
	{
		return new Link<Void>(linkId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				CriteriaKopierenStap1Page stap1 = (CriteriaKopierenStap1Page) getPage();
				setResponsePage(new CriteriaKopierenStap2Page(base.getSelectedElements(), stap1));
			}
		};
	}

}
