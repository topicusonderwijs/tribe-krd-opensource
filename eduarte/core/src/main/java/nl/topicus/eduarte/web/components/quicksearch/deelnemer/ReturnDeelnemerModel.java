package nl.topicus.eduarte.web.components.quicksearch.deelnemer;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.apache.wicket.model.IModel;

public class ReturnDeelnemerModel implements IModel<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private IModel<Deelnemer> deelnemerModel;

	public ReturnDeelnemerModel(IModel<Deelnemer> deelnemerModel)
	{
		this.deelnemerModel = deelnemerModel;
	}

	@Override
	public Verbintenis getObject()
	{
		Deelnemer deelnemer = deelnemerModel.getObject();
		return deelnemer == null ? null : deelnemer.getEersteInschrijvingOpPeildatum();
	}

	@Override
	public void setObject(Verbintenis object)
	{
		deelnemerModel.setObject(object.getDeelnemer());
	}

	@Override
	public void detach()
	{
		deelnemerModel.detach();
	}
}