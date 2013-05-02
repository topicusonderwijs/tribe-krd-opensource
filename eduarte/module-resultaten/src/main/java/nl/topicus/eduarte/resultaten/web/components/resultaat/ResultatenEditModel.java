package nl.topicus.eduarte.resultaten.web.components.resultaat;

import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.web.components.resultaat.RecalculationManager;
import nl.topicus.eduarte.web.components.resultaat.ResultatenModel;
import nl.topicus.eduarte.zoekfilters.ResultaatZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class ResultatenEditModel extends ResultatenModel
{
	private static final long serialVersionUID = 1L;

	private RecalculationManager recalcManager;

	public ResultatenEditModel(ResultaatZoekFilter zoekFilter,
			IModel< ? extends List<Toets>> toetsenModel,
			IModel< ? extends List<Deelnemer>> deelnemersModel)
	{
		super(zoekFilter, toetsenModel, deelnemersModel);
		recalcManager =
			new RecalculationManager(ModelFactory.getModel(EduArteContext.get().getMedewerker()),
				this);
	}

	public void addRecalcuation(Toets toets, Deelnemer deelnemer)
	{
		recalcManager.addRecalcuation(toets, deelnemer);
	}

	public void recalculateResultaten()
	{
		DataAccessRegistry.getHelper(BatchDataAccessHelper.class).flush();
		recalcManager.recalculateResultaten(getFreshObject().getAllResultaten());
	}

	public void updateResultaat(Resultaat resultaat, int pogingNummer)
	{
		getObject().getResultaten(new Model<Toets>(resultaat.getToets()),
			new Model<Deelnemer>(resultaat.getDeelnemer())).get(pogingNummer + OFFSET).add(0,
			resultaat);
	}

	@Override
	public void detach()
	{
		super.detach();
		recalcManager.detach();
	}

	@Override
	public boolean isEditable()
	{
		return true;
	}
}
