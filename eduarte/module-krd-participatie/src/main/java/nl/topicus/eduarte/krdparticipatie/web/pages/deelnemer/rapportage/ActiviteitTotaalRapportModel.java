package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.participatie.helpers.AfspraakDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.BudgetDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;
import nl.topicus.eduarte.entities.participatie.enums.UitnodigingStatus;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakTypeZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.DeelnemerActiviteitTotalenZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class ActiviteitTotaalRapportModel extends LoadableDetachableModel<List<ActiviteitTotaal>>
{
	private static final long serialVersionUID = 1L;

	private DeelnemerActiviteitTotalenZoekFilter filter;

	private IModel<Verbintenis> inschrijvingModel;

	public ActiviteitTotaalRapportModel(Verbintenis verbintenis,
			DeelnemerActiviteitTotalenZoekFilter filter)
	{
		this.filter = filter;
		this.inschrijvingModel = ModelFactory.getModel(verbintenis);
	}

	@Override
	protected List<ActiviteitTotaal> load()
	{
		Verbintenis verbintenis = inschrijvingModel.getObject();
		filter.setEindDatum(TimeUtil.getInstance().maakEindeVanDagVanDatum(filter.getEindDatum()));
		AfspraakZoekFilter afspraakZoekFilter = new AfspraakZoekFilter();
		afspraakZoekFilter.setAuthorizationContext(filter.getAuthorizationContext());
		afspraakZoekFilter.setDeelnemer(verbintenis.getDeelnemer());
		afspraakZoekFilter.setBeginDatumTijd(filter.getBeginDatum());
		afspraakZoekFilter.setEindDatumTijd(filter.getEindDatum());
		afspraakZoekFilter.setContract(filter.getContract());
		EnumSet<UitnodigingStatus> uitnStatussen =
			EnumSet.of(UitnodigingStatus.DIRECTE_PLAATSING, UitnodigingStatus.GEACCEPTEERD,
				UitnodigingStatus.INGETEKEND);
		afspraakZoekFilter.setUitnodigingStatussen(uitnStatussen);
		AfspraakTypeZoekFilter afspraakTypeZoekFilter = new AfspraakTypeZoekFilter();
		EnumSet<AfspraakTypeCategory> cats = EnumSet.noneOf(AfspraakTypeCategory.class);
		for (AfspraakTypeCategory category : filter.getAfspraakTypeCategories())
		{
			cats.add(category);
		}
		afspraakTypeZoekFilter.setCategories(cats);
		afspraakZoekFilter.setAfspraakTypeZoekFilter(afspraakTypeZoekFilter);
		AfspraakDataAccessHelper helper =
			DataAccessRegistry.getHelper(AfspraakDataAccessHelper.class);
		List<Afspraak> afsprakenList = new ArrayList<Afspraak>();
		if (!filter.getAfspraakTypeCategories().isEmpty())
			afsprakenList = helper.list(afspraakZoekFilter);

		List<ActiviteitTotaal> totalenList = new ArrayList<ActiviteitTotaal>();
		Map<Onderwijsproduct, List<Afspraak>> afsprakenMap =
			new HashMap<Onderwijsproduct, List<Afspraak>>();
		List<Afspraak> afsprakenZonderActiviteit = new ArrayList<Afspraak>();
		for (Afspraak afspraak : afsprakenList)
		{
			if (afspraak.getOnderwijsproduct() != null)
			{
				if (!afsprakenMap.containsKey(afspraak.getOnderwijsproduct()))
				{
					afsprakenMap.put(afspraak.getOnderwijsproduct(), new ArrayList<Afspraak>());
				}
				afsprakenMap.get(afspraak.getOnderwijsproduct()).add(afspraak);
			}
			else
			{
				afsprakenZonderActiviteit.add(afspraak);
			}
		}

		if (filter.isActiviteitenTonen() == null || filter.isActiviteitenTonen())
		{
			for (Onderwijsproduct odwpr : afsprakenMap.keySet())
			{
				BudgetDataAccessHelper budgetHelper =
					DataAccessRegistry.getHelper(BudgetDataAccessHelper.class);
				BigDecimal budget =
					new BigDecimal(budgetHelper.getBudget(odwpr, verbintenis.getDeelnemer()));
				totalenList.add(new ActiviteitTotaal(verbintenis.getDeelnemer(), odwpr.getCode(),
					afsprakenMap.get(odwpr), budget));
			}
			if (!afsprakenZonderActiviteit.isEmpty())
				totalenList.add(new ActiviteitTotaal(verbintenis.getDeelnemer(), "Geen activiteit",
					afsprakenZonderActiviteit, BigDecimal.ZERO));
		}
		totalenList.add(new ActiviteitTotaal(verbintenis.getDeelnemer(), "Totaal", afsprakenList,
			BigDecimal.ZERO));
		return totalenList;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
		ComponentUtil.detachQuietly(inschrijvingModel);
	}
}