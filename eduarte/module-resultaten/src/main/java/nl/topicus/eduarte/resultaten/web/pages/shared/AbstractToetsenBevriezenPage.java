package nl.topicus.eduarte.resultaten.web.pages.shared;

import static nl.topicus.eduarte.web.components.resultaat.AbstractToetsBoomComparator.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.dataproviders.IModelDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.eduarte.entities.resultaatstructuur.IBevriezing;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.resultaten.web.components.bevriezen.ToetsBevriezenTable;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.resultaat.StructuurToetsComparator;
import nl.topicus.eduarte.web.pages.AbstractDynamicContextPage;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.PageContext;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

public abstract class AbstractToetsenBevriezenPage extends
		AbstractDynamicContextPage<Resultaatstructuur> implements
		IModuleEditPage<Resultaatstructuur>
{

	public AbstractToetsenBevriezenPage(PageContext context,
			IModel<Resultaatstructuur> structuurModel)
	{
		super(structuurModel, context);
	}

	@Override
	protected void onBeforeRender()
	{
		WebMarkupContainer tabel;
		if (getResultaatstructuur() == null)
		{
			tabel = new WebMarkupContainer("datapanel");
		}
		else
		{
			ToetsBevriezenTable tableContents =
				new ToetsBevriezenTable(new PropertyModel<Integer>(getContextModel(), "depth"),
					this);

			IModelDataProvider<Toets> provider =
				new IModelDataProvider<Toets>(new LoadableDetachableModel<List<Toets>>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected List<Toets> load()
					{
						List<Toets> toetsen =
							new ArrayList<Toets>(getResultaatstructuur().getToetsen());
						Collections.sort(toetsen, new StructuurToetsComparator(ASCENDING));
						return toetsen;
					}
				})
				{
					private static final long serialVersionUID = 1L;

					@Override
					public IModel<Toets> model(Toets object)
					{
						return getToetsModel(object);
					}
				};
			tabel = new EduArteDataPanel<Toets>("datapanel", provider, tableContents);
		}
		addOrReplace(tabel);
		super.onBeforeRender();
	}

	protected IModel<Toets> getToetsModel(Toets toets)
	{
		return ModelFactory.getModel(toets);
	}

	public abstract List< ? extends IBevriezing> getBevriezingen(Toets toets);

	public abstract boolean getDisableBevrorenToetsen();

	protected Resultaatstructuur getResultaatstructuur()
	{
		return getContextModelObject();
	}

	public int getMaxAantalPogingen()
	{
		int max = 0;
		for (Toets curToets : getResultaatstructuur().getToetsen())
			max = Math.max(curToets.getAantalPogingen(), max);
		return max;
	}
}
