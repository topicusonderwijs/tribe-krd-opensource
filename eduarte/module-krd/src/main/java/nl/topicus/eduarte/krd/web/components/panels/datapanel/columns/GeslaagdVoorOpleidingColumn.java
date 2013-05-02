package nl.topicus.eduarte.krd.web.components.panels.datapanel.columns;

import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.util.criteriumbank.BereikbareDiplomasUtil;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class GeslaagdVoorOpleidingColumn extends AbstractCustomColumn<Opleiding>
{
	private static final long serialVersionUID = 1L;

	private final IModel<Deelnemer> deelnemerModel;

	private final IModel<Cohort> cohortModel;

	public GeslaagdVoorOpleidingColumn(String id, IModel<Deelnemer> deelnemerModel,
			IModel<Cohort> cohortModel)
	{
		super(id, "Geslaagd");
		this.deelnemerModel = deelnemerModel;
		this.cohortModel = cohortModel;
	}

	private Deelnemer getDeelnemer()
	{
		return deelnemerModel.getObject();
	}

	private Cohort getCohort()
	{
		return cohortModel.getObject();
	}

	@Override
	public void populateItem(WebMarkupContainer cellItem, String componentId,
			WebMarkupContainer row, IModel<Opleiding> rowModel, int span)
	{
		Opleiding opleiding = rowModel.getObject();
		BereikbareDiplomasUtil util =
			new BereikbareDiplomasUtil(getDeelnemer(), opleiding, getCohort());
		util.berekenOpleidingenWaarvoorDeDeelnemerIsGeslaagd();
		List<Opleiding> geslaagdVoor = util.getGeslaagdVoorOpleidingen();
		boolean geslaagd = geslaagdVoor.size() == 1;
		cellItem.add(ComponentFactory.getDataLabel(componentId, geslaagd ? "Ja" : "Nee"));
	}

	@Override
	public void detach()
	{
		super.detach();
		ComponentUtil.detachQuietly(deelnemerModel);
		ComponentUtil.detachQuietly(cohortModel);
	}

}
