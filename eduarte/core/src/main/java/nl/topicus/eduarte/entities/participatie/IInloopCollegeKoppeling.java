package nl.topicus.eduarte.entities.participatie;

import nl.topicus.eduarte.entities.Entiteit;

public interface IInloopCollegeKoppeling<S extends Entiteit>
{
	public void setInloopCollege(InloopCollege inloopCollege);

	public InloopCollege getInloopCollege();

	public void setKoppeling(S entiteit);

	public S getKoppeling();
}
