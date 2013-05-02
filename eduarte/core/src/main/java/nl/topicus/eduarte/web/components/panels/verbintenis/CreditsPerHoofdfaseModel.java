package nl.topicus.eduarte.web.components.panels.verbintenis;

import nl.topicus.eduarte.entities.hogeronderwijs.Hoofdfase;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;

import org.apache.wicket.model.IModel;

public class CreditsPerHoofdfaseModel implements IModel<Integer>
{
	private static final long serialVersionUID = 1L;

	private IModel<Verbintenis> verbintenisModel;

	private Hoofdfase hoofdfase;

	public CreditsPerHoofdfaseModel(IModel<Verbintenis> verbintenisModel, Hoofdfase hoofdfase)
	{
		this.verbintenisModel = verbintenisModel;
		this.hoofdfase = hoofdfase;
	}

	@Override
	public Integer getObject()
	{
		return verbintenisModel.getObject().getCredits(hoofdfase);
	}

	@Override
	public void setObject(Integer object)
	{
		verbintenisModel.getObject().setCredits(hoofdfase, object);
	}

	@Override
	public void detach()
	{
		verbintenisModel.detach();
	}

}
