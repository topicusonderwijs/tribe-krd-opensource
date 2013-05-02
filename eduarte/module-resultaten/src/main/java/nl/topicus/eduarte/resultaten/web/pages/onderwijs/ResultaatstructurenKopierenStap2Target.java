package nl.topicus.eduarte.resultaten.web.pages.onderwijs;

import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;

import org.apache.wicket.markup.html.link.Link;

public class ResultaatstructurenKopierenStap2Target extends
		AbstractSelectieTarget<Onderwijsproduct, Onderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	private SecurePage startPage;

	public ResultaatstructurenKopierenStap2Target(SecurePage startPage)
	{
		super(ResultaatstructurenKopierenStap2Page.class, "Volgende");
		this.startPage = startPage;
	}

	@Override
	public Link<Void> createLink(String linkId,
			final ISelectionComponent<Onderwijsproduct, Onderwijsproduct> base)
	{
		return new Link<Void>(linkId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				setResponsePage(new ResultaatstructurenKopierenStap2Page(startPage,
					(ResultaatstructurenKopierenStap1Page) getPage(), base.getSelectedElements(),
					((OnderwijsproductZoekFilter) base.getFilter()).getCohort()));
			}
		};
	}

}
