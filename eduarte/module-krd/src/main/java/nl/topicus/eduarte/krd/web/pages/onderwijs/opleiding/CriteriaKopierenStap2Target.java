package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;

import org.apache.wicket.markup.html.link.Link;

public class CriteriaKopierenStap2Target extends AbstractSelectieTarget<Opleiding, Opleiding>
{
	private static final long serialVersionUID = 1L;

	private SecurePage startPage;

	public CriteriaKopierenStap2Target(SecurePage startPage)
	{
		super(CriteriaKopierenStap3Page.class, "Volgende");
		this.startPage = startPage;
	}

	@Override
	public Link<Void> createLink(String linkId, final ISelectionComponent<Opleiding, Opleiding> base)
	{
		return new Link<Void>(linkId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				setResponsePage(new CriteriaKopierenStap3Page(startPage,
					(CriteriaKopierenStap2Page) getPage(), base.getSelectedElements(),
					((OpleidingZoekFilter) base.getFilter()).getCohort()));
			}
		};
	}

}
