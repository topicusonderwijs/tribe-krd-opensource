package nl.topicus.eduarte.onderwijscatalogus.web.pages.onderwijsproduct;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductVerbruiksmiddel;
import nl.topicus.eduarte.entities.onderwijsproduct.Verbruiksmiddel;
import nl.topicus.eduarte.onderwijscatalogus.principals.onderwijs.OnderwijsproductVerbruiksmiddelenWrite;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;
import nl.topicus.eduarte.web.pages.shared.AbstractVerbruiksmiddelSelectiePage;
import nl.topicus.eduarte.zoekfilters.VerbruiksmiddelZoekFilter;

import org.apache.wicket.markup.html.link.Link;

/**
 * Pagina voor toevoegen van een verbruiksmiddel aan een onderwijsproduct
 * 
 * @author papegaaij
 */
@PageInfo(title = "Verbruiksmiddel toevoegen", menu = {"Onderwijs > Onderwijsproducten > [Onderwijsproduct] > Verbruiksmiddel > Toevoegen"})
@InPrincipal(OnderwijsproductVerbruiksmiddelenWrite.class)
public class OnderwijsproductVerbruiksmiddelToevoegenPage extends
		AbstractVerbruiksmiddelSelectiePage implements IModuleEditPage<Onderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	private static final VerbruiksmiddelZoekFilter getDefaultFilter(Onderwijsproduct product)
	{
		VerbruiksmiddelZoekFilter filter = new VerbruiksmiddelZoekFilter();
		filter.setOnderwijsproduct(product);
		filter.setAlleenOngekoppeld(true);
		filter.addOrderByProperty("naam");
		return filter;
	}

	public OnderwijsproductVerbruiksmiddelToevoegenPage(Onderwijsproduct onderwijsproduct,
			final SecurePage returnToPage)
	{
		super(returnToPage, getDefaultFilter(onderwijsproduct),
			new HibernateSelection<Verbruiksmiddel>(Verbruiksmiddel.class),
			new AbstractSelectieTarget<Verbruiksmiddel, Verbruiksmiddel>(returnToPage.getClass(),
				"Toevoegen")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Link<Void> createLink(String linkId,
						final ISelectionComponent<Verbruiksmiddel, Verbruiksmiddel> base)
				{
					return new Link<Void>(linkId)
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick()
						{
							Onderwijsproduct product =
								((VerbruiksmiddelZoekFilter) base.getFilter())
									.getOnderwijsproduct();
							for (Verbruiksmiddel curMiddel : base.getSelectedElements())
							{
								OnderwijsproductVerbruiksmiddel newMiddel =
									new OnderwijsproductVerbruiksmiddel();
								newMiddel.setOnderwijsproduct(product);
								newMiddel.setVerbruiksmiddel(curMiddel);
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
