package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;

import org.apache.wicket.markup.html.link.Link;

public class ProductregelsKopierenStap1Target extends
		AbstractSelectieTarget<Productregel, Productregel>
{
	private static final long serialVersionUID = 1L;

	public ProductregelsKopierenStap1Target()
	{
		super(ProductregelsKopierenStap2Page.class, "Volgende");
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
				ProductregelsKopierenStap1Page stap1 = (ProductregelsKopierenStap1Page) getPage();
				setResponsePage(new ProductregelsKopierenStap2Page(base.getSelectedElements(),
					stap1));
			}
		};
	}

}
