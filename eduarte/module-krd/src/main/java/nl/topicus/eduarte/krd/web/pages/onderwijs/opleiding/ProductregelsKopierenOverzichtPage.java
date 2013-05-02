package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.krd.entities.ProductregelsKopierenJobRun;
import nl.topicus.eduarte.krd.jobs.ProductregelsKopierenJob;
import nl.topicus.eduarte.krd.principals.onderwijs.OpleidingProductregelsKopieren;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.quartz.JobDataMap;

/**
 * @author loite
 */
@PageInfo(title = "Productregels kopieren", menu = "Onderwijs > Kopiëren productregels")
@InPrincipal(OpleidingProductregelsKopieren.class)
public class ProductregelsKopierenOverzichtPage extends
		AbstractJobBeheerPage<ProductregelsKopierenJobRun>
{
	public ProductregelsKopierenOverzichtPage()
	{
		super(CoreMainMenuItem.Onderwijs, ProductregelsKopierenJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return null;
	}

	@Override
	protected AbstractBottomRowButton createTaakStartenButton(BottomRowPanel panel)
	{
		return new PageLinkButton(panel, "Kopiëren", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new ProductregelsKopierenStap2Page(ProductregelsKopierenOverzichtPage.this);
			}

			@Override
			public Class<ProductregelsKopierenStap2Page> getPageIdentity()
			{
				return ProductregelsKopierenStap2Page.class;
			}
		});
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.ProductregelsKopieren);
	}
}
