package nl.topicus.eduarte.krd.web.components.modalwindow.bron;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter.TypeMelding;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;

import org.apache.wicket.model.IModel;

/**
 * Modal window voor het toevoegen van een nieuwe bookmark.
 * 
 * @author vandekamp
 */
public class SelecteerOnderwijssoortModalWindow extends CobraModalWindow<BronOnderwijssoort>
{
	private static final long serialVersionUID = 1L;

	private BronMeldingZoekFilter meldingFilter;

	private TypeMelding typeMelding;

	public SelecteerOnderwijssoortModalWindow(String id, IModel<BronOnderwijssoort> model,
			BronMeldingZoekFilter meldingFilter, TypeMelding typeMelding)
	{
		super(id, model);
		this.meldingFilter = meldingFilter;
		this.typeMelding = typeMelding;
		setTitle("Selecteer een onderwijssoort");
		setInitialHeight(150);
		setInitialWidth(250);
		setResizable(false);
	}

	@Override
	protected CobraModalWindowBasePanel<BronOnderwijssoort> createContents(String id)
	{
		return new SelecteerOnderwijssoortPanel(id, this, meldingFilter, typeMelding);
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(meldingFilter);
		super.onDetach();
	}
}
