package nl.topicus.eduarte.rapportage.entities.list;

import java.util.List;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;

public class TaxonomieElementWrappedList extends WrappedList<TaxonomieElement>
{

	public TaxonomieElementWrappedList(List<TaxonomieElement> lijst)
	{
		super(lijst);
	}

	@Exportable
	@Override
	public List<TaxonomieElement> getLijst()
	{
		return internalGetLijst();
	}

}
