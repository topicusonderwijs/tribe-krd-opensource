package nl.topicus.eduarte.rapportage.model;

import java.util.Arrays;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRField;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.web.components.resultaat.ResultatenModel;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.ResultaatZoekFilter;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

public class DeelnemerResultatenRapportageModel implements IDetachable, JRDataSource
{
	private static final long serialVersionUID = 1L;

	private ResultatenModel resultatenModel;

	public DeelnemerResultatenRapportageModel(final IModel<Verbintenis> verbintenisModel,
			OrganisatieEenheidLocatieAuthorizationContext context)
	{
		Verbintenis verbintenis = verbintenisModel.getObject();
		Asserts.assertNotEmpty("verbintenis mag niet null zijn", verbintenis);

		final ToetsZoekFilter toetsFilter = new ToetsZoekFilter(new ResultaatstructuurZoekFilter());
		toetsFilter.getResultaatstructuurFilter().setAuthorizationContext(context);
		toetsFilter.getResultaatstructuurFilter().setContextVerbintenis(verbintenis);
		toetsFilter.getResultaatstructuurFilter().setCohort(verbintenis.getCohort());
		toetsFilter.getResultaatstructuurFilter().setDeelnemers(
			Arrays.asList(verbintenis.getDeelnemer()));

		IModel<List<Toets>> toetsenModel = new LoadableDetachableModel<List<Toets>>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Toets> load()
			{
				return DataAccessRegistry.getHelper(ToetsDataAccessHelper.class).list(toetsFilter);
			}
		};
		IModel<List<Deelnemer>> deelnemersModel = new LoadableDetachableModel<List<Deelnemer>>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Deelnemer> load()
			{
				return Arrays.asList((verbintenisModel.getObject()).getDeelnemer());
			}
		};

		toetsFilter.getResultaatstructuurFilter().setCohort(verbintenis.getCohort());

		resultatenModel =
			new ResultatenModel(new ResultaatZoekFilter(toetsFilter, Arrays.asList(verbintenis
				.getDeelnemer())), toetsenModel, deelnemersModel);

	}

	private int toetsIndex = -1;

	public List<Toets> getToetsen()
	{
		return resultatenModel.getToetsen();
	}

	public ResultatenModel getResultatenModel()
	{
		return resultatenModel;
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(resultatenModel);
	}

	@Override
	public Object getFieldValue(JRField jrField)
	{
		Object returnObject = null;

		if ("toets".equals(jrField.getName()))
			returnObject = getToetsen().get(toetsIndex).getCode();
		else if ("onderwijsproduct".equals(jrField.getName()))
			returnObject =
				getToetsen().get(toetsIndex).getResultaatstructuur().getOnderwijsproduct();
		else if ("cijfer".equals(jrField.getName()) || "cijferMeasure".equals(jrField.getName()))
		{
			Resultaat resultaat =
				resultatenModel.getResultaat(new Model<Toets>(getToetsen().get(toetsIndex)),
					new Model<Deelnemer>(resultatenModel.getDeelnemers().get(0)),
					ResultatenModel.RESULTAAT_IDX);
			if (resultaat != null)
				returnObject = resultaat.getFormattedDisplayWaarde();
		}

		// System.out.println("jrField: " + jrField.getName() + " - "
		// + (returnObject == null ? "null" : returnObject.toString()));

		return returnObject;
	}

	@Override
	public boolean next()
	{
		if ((toetsIndex + 1) >= getToetsen().size())
			return false;

		toetsIndex++;
		return toetsIndex < getToetsen().size();
	}
}
