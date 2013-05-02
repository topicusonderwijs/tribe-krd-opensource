package nl.topicus.eduarte.rapportage.entities.list;

import java.util.List;

import nl.topicus.cobra.templates.annotations.Exportable;

/**
 * Wrapper om een lijst. Een dergelijke wrapper is nodig voor exportable methodes die een
 * argument verwachten en een lijst opleveren. In de samenvoegdocumenten moet die
 * opgenomen worden als "entiteit.propertyName('argument').lijst[]" waarbij het
 * betreffende property een WrappedList is. De class is abstract omdat onze parser vereist
 * dat de return type vast getypeerd is (oftewel niet <T> maar bijvoorbeeld
 * <TaxonomieElement>).
 * 
 * @author loite
 * 
 */
@Exportable
public abstract class WrappedList<T>
{
	private final List<T> lijst;

	public WrappedList(List<T> lijst)
	{
		this.lijst = lijst;
	}

	public abstract List<T> getLijst();

	protected List<T> internalGetLijst()
	{
		return lijst;
	}

}
