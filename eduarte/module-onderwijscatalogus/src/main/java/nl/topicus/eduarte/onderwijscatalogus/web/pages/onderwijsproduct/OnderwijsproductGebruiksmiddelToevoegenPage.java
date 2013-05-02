package nl.topicus.eduarte.onderwijscatalogus.web.pages.onderwijsproduct;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.eduarte.entities.onderwijsproduct.Gebruiksmiddel;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductGebruiksmiddel;
import nl.topicus.eduarte.onderwijscatalogus.principals.onderwijs.OnderwijsproductGebruiksmiddelenWrite;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractGebruiksmiddelSelectiePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;
import nl.topicus.eduarte.zoekfilters.GebruiksmiddelZoekFilter;

import org.apache.wicket.markup.html.link.Link;

/**
 * Pagina voor toevoegen van een gebruiksmiddel aan een onderwijsproduct
 * 
 * @author papegaaij
 */
@PageInfo(title = "Gebruiksmiddel toevoegen", menu = {"Onderwijs > Onderwijsproducten > [Onderwijsproduct] > Gebruiksmiddel > Toevoegen"})
@InPrincipal(OnderwijsproductGebruiksmiddelenWrite.class)
public class OnderwijsproductGebruiksmiddelToevoegenPage extends AbstractGebruiksmiddelSelectiePage
		implements IModuleEditPage<Onderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	private static final GebruiksmiddelZoekFilter getDefaultFilter(Onderwijsproduct product)
	{
		GebruiksmiddelZoekFilter filter = new GebruiksmiddelZoekFilter();
		filter.setOnderwijsproduct(product);
		filter.setAlleenOngekoppeld(true);
		filter.addOrderByProperty("naam");
		return filter;
	}

	public OnderwijsproductGebruiksmiddelToevoegenPage(Onderwijsproduct onderwijsproduct,
			final SecurePage returnToPage)
	{
		super(returnToPage, getDefaultFilter(onderwijsproduct),
			new HibernateSelection<Gebruiksmiddel>(Gebruiksmiddel.class),
			new AbstractSelectieTarget<Gebruiksmiddel, Gebruiksmiddel>(returnToPage.getClass(),
				"Toevoegen")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Link<Void> createLink(String linkId,
						final ISelectionComponent<Gebruiksmiddel, Gebruiksmiddel> base)
				{
					return new Link<Void>(linkId)
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick()
						{
							Onderwijsproduct product =
								((GebruiksmiddelZoekFilter) base.getFilter()).getOnderwijsproduct();
							for (Gebruiksmiddel curMiddel : base.getSelectedElements())
							{
								OnderwijsproductGebruiksmiddel newMiddel =
									new OnderwijsproductGebruiksmiddel();
								newMiddel.setOnderwijsproduct(product);
								newMiddel.setGebruiksmiddel(curMiddel);
								newMiddel.save();
							}
							product.commit();
							setResponsePage(returnToPage);
						}
					};
				}
			});
	}

	@Override
	public int getMaxResults()
	{
		return Integer.MAX_VALUE;
	}
}
