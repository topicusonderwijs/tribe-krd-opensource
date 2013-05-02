package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bronmutatie;

import org.apache.wicket.model.IModel;

public class CollectiefBronmutatieModel implements IModel<CollectiefBronmutatieModel>
{
	private static final long serialVersionUID = 1L;

	private Enum< ? > soortMutatie;

	public CollectiefBronmutatieModel()
	{
	}

	public Enum< ? > getSoortMutatie()
	{
		return soortMutatie;
	}

	public void setSoortMutatie(Enum< ? > soortMutatie)
	{
		this.soortMutatie = soortMutatie;
	}

	@Override
	public CollectiefBronmutatieModel getObject()
	{
		return this;
	}

	@Override
	public void setObject(CollectiefBronmutatieModel object)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void detach()
	{
	}

}