package nl.topicus.eduarte.resultaten.web.components.bevriezen;

import java.util.List;

import nl.topicus.cobra.web.components.wiquery.tristate.TriState;
import nl.topicus.eduarte.entities.resultaatstructuur.IBevriezing;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.resultaten.web.pages.shared.AbstractToetsenBevriezenPage;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ToetsTable;
import nl.topicus.eduarte.web.components.resultaat.ResultatenModel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

public class ToetsBevriezenTable extends ToetsTable
{
	private static final long serialVersionUID = 1L;

	public ToetsBevriezenTable(IModel<Integer> spanCountModel,
			AbstractToetsenBevriezenPage bevriezenPage)
	{
		super(spanCountModel);

		addColumn(ResultatenModel.ALTERNATIEF_NR, bevriezenPage);
		for (int count = 0; count <= bevriezenPage.getMaxAantalPogingen(); count++)
		{
			addColumn(count, bevriezenPage);
		}
	}

	private void addColumn(int pogingNr, final AbstractToetsenBevriezenPage bevriezenPage)
	{
		addColumn(new BevriezenColumn(pogingNr, bevriezenPage.getDisableBevrorenToetsen())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected List< ? extends IBevriezing> getBevriezingen(Toets toets)
			{
				return bevriezenPage.getBevriezingen(toets);
			}

			@Override
			protected void onCheckboxSelectionChanged(IModel<TriState> checkboxModel,
					IModel<Toets> rowModel, AjaxRequestTarget target)
			{
				target.addComponent(bevriezenPage.get("datapanel"));
			}
		});
	}
}
