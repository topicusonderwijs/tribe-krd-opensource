package nl.topicus.eduarte.entities.bijlage;

import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductBijlage;

/**
 * Entiteit welke 0 of meer {@link Bijlage} koppel objecten kan hebben. Voorbeeld:
 * {@link Onderwijsproduct} met koppel object {@link OnderwijsproductBijlage}.
 * 
 * @author hoeve
 */
public interface IBijlageKoppelEntiteit<T extends BijlageEntiteit> extends IdObject
{
	public List<T> getBijlagen();

	public void setBijlagen(List<T> bijlagen);

	public boolean bestaatBijlage(Bijlage bijlage);

	/**
	 * Linkt de bijlage aan deze entiteit. Er wordt echter geen begindatum/einddatum
	 * opgegeven en het object wordt niet opgeslagen.
	 */
	public T addBijlage(Bijlage bijlage);
}
