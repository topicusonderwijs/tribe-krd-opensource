package nl.topicus.eduarte.web.components.resultaat;

import java.util.List;

import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.cobra.web.components.datapanel.columns.ExportHeaderColumn;
import nl.topicus.cobra.web.components.datapanel.columns.HideableColumn;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.Scoreschaal;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class ResultaatColumn<T> extends AbstractCustomColumn<T> implements IStyledColumn<T>,
		HideableColumn<T>, ExportHeaderColumn<T>
{
	private static final long serialVersionUID = 1L;

	protected int pogingNummer;

	protected ResultatenModel resultatenModel;

	protected DeelnemerToetsResolver<T> deelnemerToetsResolver;

	protected EditorJavascriptRenderer<T> javascriptRenderer;

	public ResultaatColumn(String id, String header, int pogingNummer,
			ResultatenModel resultatenModel, DeelnemerToetsResolver<T> deelnemerToetsResolver,
			EditorJavascriptRenderer<T> javascriptRenderer)
	{
		super(id, header);
		this.pogingNummer = pogingNummer;
		this.resultatenModel = resultatenModel;
		this.deelnemerToetsResolver = deelnemerToetsResolver;
		this.javascriptRenderer = javascriptRenderer;
	}

	public Resultaat getResultaat(IModel<Toets> toetsModel, IModel<Deelnemer> deelnemerModel)
	{
		return resultatenModel.getResultaat(toetsModel, deelnemerModel, pogingNummer);
	}

	public List<Resultaat> getHistory(IModel<Toets> toetsModel, IModel<Deelnemer> deelnemerModel)
	{
		return resultatenModel.getResultaatHistory(toetsModel, deelnemerModel, pogingNummer);
	}

	public Object getDisplayWaarde(Resultaat resultaat)
	{
		if (resultaat == null)
			return null;

		return resultaat.getDisplayWaarde(isCijferColumn(resultaat.getToets()));
	}

	public boolean isCijferColumn(Toets toets)
	{
		return pogingNummer == ResultatenModel.ALTERNATIEF_NR
			|| pogingNummer == ResultatenModel.RESULTAAT_NR
			|| toets.getScoreschaal().equals(Scoreschaal.Geen);
	}

	public boolean isAanpasbaar(Toets toets, Deelnemer deelnemer)
	{
		return resultatenModel.isAanpasbaar(toets, deelnemer, pogingNummer);
	}

	public String getNietAanpasbaarMessage(Toets toets, Deelnemer deelnemer)
	{
		if (toets == null)
			return null;
		if (!toets.getResultaatstructuur().isBeschikbaar())
			return "De resultaatstructuur is niet beschikbaar";
		if (resultatenModel.isBevroren(toets, deelnemer, pogingNummer))
			return "Het resultaat is bevroren";
		if (pogingNummer == ResultatenModel.RESULTAAT_NR && !toets.isOverschrijfbaar())
			return "Het resultaat van deze toets is niet overschrijfbaar";
		return null;
	}

	public int getPoging()
	{
		return pogingNummer;
	}

	public Toets getToets()
	{
		return deelnemerToetsResolver.getToetsModel(null).getObject();
	}

	public List<Deelnemer> getDeelnemers()
	{
		return resultatenModel.getDeelnemers();
	}

	@Override
	public boolean isColumnVisible()
	{
		return deelnemerToetsResolver.isColumnVisible();
	}

	@Override
	public boolean isColumnVisibleInExport()
	{
		return deelnemerToetsResolver.isColumnVisibleInExport();
	}

	@Override
	public String getCssClass()
	{
		return "ciColumn ciDepth-" + Math.min(3, deelnemerToetsResolver.getToetsDepth());
	}

	@Override
	public Component getHeader(Component cell, String componentId)
	{
		Component ret =
			new ResultaatColumnHeaderPanel(componentId, getDisplayModel(), deelnemerToetsResolver
				.getToetsModel(null), resultatenModel.isEditable());
		cell.setOutputMarkupId(true);
		javascriptRenderer.registerHeaderCell(cell, this);
		return ret;
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		final IModel<Toets> toetsModel = deelnemerToetsResolver.getToetsModel(rowModel);
		final IModel<Deelnemer> deelnemerModel = deelnemerToetsResolver.getDeelnemerModel(rowModel);
		cell.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				return resultatenModel.getHtmlClassesVoorResultaat(toetsModel, deelnemerModel,
					pogingNummer);
			}
		}, " "));
		final ResultaatCellPanel cellPanel =
			createCellComponent(componentId, toetsModel, deelnemerModel, cell);
		cell.add(cellPanel);
		cell.setOutputMarkupId(true);
		boolean isResultaatAvailable =
			resultatenModel.isAvailable(toetsModel, deelnemerModel, pogingNummer);
		if (isResultaatAvailable)
		{
			IModel<List<Resultaat>> resultaten = new LoadableDetachableModel<List<Resultaat>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Resultaat> load()
				{
					return getHistory(toetsModel, deelnemerModel);
				}
			};
			cell.add(new AppendingAttributeModifier("class", "ciField"));
			ResultaatKey key = new ResultaatKey(toetsModel, deelnemerModel);
			if (pogingNummer == 0)
				javascriptRenderer.registerCijferCell(key, cell, cellPanel, resultaten);
			else
				javascriptRenderer.registerPogingCell(key, cell, cellPanel, pogingNummer,
					resultaten);
			javascriptRenderer.registerFeedback(this, new AbstractReadOnlyModel<Boolean>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean getObject()
				{
					return cellPanel.hasFeedback();
				}
			});
		}
		else
		{
			cellPanel.setVisible(false);
		}
	}

	protected ResultaatCellPanel createCellComponent(String id, IModel<Toets> toetsModel,
			IModel<Deelnemer> deelnemerModel, WebMarkupContainer cell)
	{
		return new ResultaatCellPanel(id, toetsModel, deelnemerModel, this, cell);
	}

	@Override
	public void detach()
	{
		super.detach();
		deelnemerToetsResolver.detach();
		resultatenModel.detach();
	}

	@Override
	public String getExportHeader()
	{
		Toets toets = deelnemerToetsResolver.getToetsModel(null).getObject();
		return toets == null ? getDisplayModel().getObject() : toets.getCodeVoorWeergave();
	}
}
