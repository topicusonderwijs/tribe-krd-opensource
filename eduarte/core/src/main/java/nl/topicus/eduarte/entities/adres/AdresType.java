package nl.topicus.eduarte.entities.adres;

import nl.topicus.cobra.modelsv2.ModelFactory;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

public class AdresType<T extends AdresEntiteit<T>> implements IDetachable
{
	private static final long serialVersionUID = 1L;

	private IModel<T> adresEntiteitModel;

	private int type;

	public AdresType()
	{
	}

	public T getAdresEntiteit()
	{
		if (adresEntiteitModel != null)
			return adresEntiteitModel.getObject();
		return null;
	}

	public void setAdresEntiteit(T adresEntiteit)
	{
		if (adresEntiteit != null)
			adresEntiteitModel = ModelFactory.getModel(adresEntiteit);
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	@Override
	public void detach()
	{
		if (adresEntiteitModel != null)
		{
			adresEntiteitModel.detach();
		}
	}
}
