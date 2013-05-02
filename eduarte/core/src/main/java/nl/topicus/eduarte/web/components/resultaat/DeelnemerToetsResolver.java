package nl.topicus.eduarte.web.components.resultaat;

import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

public interface DeelnemerToetsResolver<T> extends IDetachable
{
	public boolean isColumnVisible();

	public boolean isColumnVisibleInExport();

	public IModel<Deelnemer> getDeelnemerModel(IModel<T> rowModel);

	public IModel<Toets> getToetsModel(IModel<T> rowModel);

	public int getToetsDepth();
}
