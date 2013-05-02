package nl.topicus.eduarte.resultaten.jobs;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.IModificationCallback;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsVerwijzing;

public class VerwijderToetsVerwijzingCallback implements IModificationCallback
{
	@Override
	public void delete(IdObject object, Class< ? extends IdObject> clazz)
	{
		if (ToetsVerwijzing.class.equals(clazz))
		{
			ToetsVerwijzing verwijzing = (ToetsVerwijzing) object;
			verwijzing.getSchrijvenIn().getInkomendeVerwijzingen().remove(verwijzing);
			verwijzing.getLezenUit().getUitgaandeVerwijzingen().remove(verwijzing);
		}
	}

	@Override
	public void saveOrUpdate(IdObject object, Class< ? extends IdObject> clazz)
	{
	}
}
