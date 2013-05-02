package nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten;

import java.io.Serializable;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.IdBasedModelSelection;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronSignaal;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtWrite;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.selectie.BronSignaalSelectiePanel;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.selectie.SignalenListModel;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronAlgemeenPage;
import nl.topicus.eduarte.krd.zoekfilters.BronSignaalZoekFilter;
import nl.topicus.eduarte.web.components.menu.BronMenuItem;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectiePage;
import nl.topicus.eduarte.web.pages.shared.DeelnemerCollectiefPageContext;

@PageInfo(title = "BRON Signalen", menu = "Deelnemer")
@InPrincipal(BronOverzichtWrite.class)
public class BronSignalenPage extends
		AbstractSelectiePage<Serializable, IBronSignaal, BronSignaalZoekFilter> implements
		IEditPage
{
	private static final long serialVersionUID = 1L;

	private BronSignaalZoekFilter filter;

	public BronSignalenPage(BronSignaalZoekFilter filter)
	{
		this(filter, new IdBasedModelSelection<IBronSignaal>());
	}

	public BronSignalenPage(BronSignaalZoekFilter filter,
			IdBasedModelSelection<IBronSignaal> selection)
	{
		super(BronAlgemeenPage.class, new DeelnemerCollectiefPageContext("BRON Signalen",
			BronMenuItem.BRON), filter, selection, new BronSignalenSelectieTarget());
		this.filter = filter;

		List<IBronSignaal> signalen = SignalenListModel.getSignalen(filter);
		for (IBronSignaal signaal : signalen)
		{
			if (signaal.getGeaccordeerd() != null && signaal.getGeaccordeerd())
				selection.add(signaal);
		}
	}

	@Override
	protected AbstractSelectiePanel<Serializable, IBronSignaal, BronSignaalZoekFilter> createSelectiePanel(
			String id, BronSignaalZoekFilter zoekfilter,
			Selection<Serializable, IBronSignaal> selection)
	{
		return new BronSignaalSelectiePanel(id, zoekfilter, selection);
	}

	@Override
	public int getMaxResults()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(BronSignaalZoekFilter.class);
		ctorArgValues.add(filter);
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(filter);
		super.onDetach();
	}
}
