package nl.topicus.eduarte.krd.web.pages.beheer.bron;

import java.io.Serializable;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.IdBasedModelSelection;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtWrite;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectiePage;
import nl.topicus.eduarte.web.pages.shared.SelectieTarget;

import org.apache.wicket.model.IModel;

@PageInfo(title = "Nieuwe BRON-batch aanmaken - Inhoud batch wijzigen", menu = "Deelnemer")
@InPrincipal(BronOverzichtWrite.class)
public class BronBatchAanmakenMeldingenPage extends
		AbstractSelectiePage<Serializable, IBronMelding, BronMeldingZoekFilter> implements
		IEditPage
{
	private static final long serialVersionUID = 1L;

	private IModel<List<IBronMelding>> meldingenModel;

	public BronBatchAanmakenMeldingenPage(SecurePage returnPage,
			IdBasedModelSelection<IBronMelding> selection,
			SelectieTarget<Serializable, IBronMelding> target,
			IModel<List<IBronMelding>> meldingenModel)
	{
		super(returnPage, null, selection, target);
		this.meldingenModel = meldingenModel;
	}

	@Override
	protected AbstractSelectiePanel<Serializable, IBronMelding, BronMeldingZoekFilter> createSelectiePanel(
			String id, BronMeldingZoekFilter filter, Selection<Serializable, IBronMelding> selection)
	{
		return new BronMeldingSelectiePanel(id, (IdBasedModelSelection<IBronMelding>) selection,
			meldingenModel);
	}

	@Override
	public int getMaxResults()
	{
		return Integer.MAX_VALUE;
	}
}
