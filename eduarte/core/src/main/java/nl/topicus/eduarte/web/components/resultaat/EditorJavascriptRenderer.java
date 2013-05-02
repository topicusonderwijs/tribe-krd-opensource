package nl.topicus.eduarte.web.components.resultaat;

import java.io.Serializable;
import java.util.*;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

public class EditorJavascriptRenderer<T> implements IDetachable
{
	private static final long serialVersionUID = 1L;

	private class ToetsCellData implements Serializable, IDetachable
	{
		private static final long serialVersionUID = 1L;

		private class PogingCellData implements Comparable<PogingCellData>, Serializable,
				IDetachable
		{
			private static final long serialVersionUID = 1L;

			private int poging;

			private WebMarkupContainer pogingCell;

			private ResultaatCellPanel pogingCellPanel;

			private IModel<List<Resultaat>> pogingResultaten;

			public PogingCellData(int poging, WebMarkupContainer pogingCell,
					ResultaatCellPanel pogingCellPanel, IModel<List<Resultaat>> pogingResultaten)
			{
				this.poging = poging;
				this.pogingCell = pogingCell;
				this.pogingCellPanel = pogingCellPanel;
				this.pogingResultaten = pogingResultaten;
			}

			private String getPogingResultaatJSON()
			{
				return Resultaat.resultatenToJSON(pogingResultaten.getObject(), false);
			}

			public void appendJavascript(StringBuilder sb)
			{
				sb.append("$('#");
				sb.append(pogingCell.getMarkupId());
				sb.append("').data('toetsInfo', {");
				sb.append("resolveId:'#").append(cijferCell.getMarkupId()).append("',");
				sb.append("poging:").append(poging).append(',');
				renderMessage(sb, pogingCellPanel);
				sb.append("history:").append(getPogingResultaatJSON());
				sb.append("});\n");
			}

			@Override
			public int compareTo(PogingCellData o)
			{
				return poging - o.poging;
			}

			public String getCellMarkupId()
			{
				return pogingCell.getMarkupId();
			}

			public int getPoging()
			{
				return poging;
			}

			@Override
			public void detach()
			{
				pogingResultaten.detach();
			}
		}

		private WebMarkupContainer cijferCell;

		private ResultaatCellPanel cellPanel;

		private IModel<List<Resultaat>> resultaten;

		private IModel<Toets> toets;

		private SortedSet<PogingCellData> cells = new TreeSet<PogingCellData>();

		public ToetsCellData(WebMarkupContainer cijferCell, ResultaatCellPanel cellPanel,
				IModel<Toets> toets, IModel<List<Resultaat>> resultaten)
		{
			this.cijferCell = cijferCell;
			this.cellPanel = cellPanel;
			this.toets = toets;
			this.resultaten = resultaten;
		}

		public void addPogingCell(WebMarkupContainer cell, ResultaatCellPanel pogingCellPanel,
				int poging, IModel<List<Resultaat>> pogingResultaten)
		{
			cells.add(new PogingCellData(poging, cell, pogingCellPanel, pogingResultaten));
		}

		private String getResultaatJSON()
		{
			return Resultaat.resultatenToJSON(resultaten.getObject(), true);
		}

		private String getToetsJSON()
		{
			return (getToets()).toLabelValueJSON();
		}

		private boolean isAlternatief()
		{
			return (getToets()).isAlternatiefResultaatMogelijk();
		}

		public void appendJavascript(StringBuilder sb)
		{
			sb.append("$('#");
			sb.append(cijferCell.getMarkupId());
			sb.append("').data('toetsInfo', {");
			sb.append("resolveId:'#").append(cijferCell.getMarkupId()).append("',");
			sb.append("poging:0,");
			renderMessage(sb, cellPanel);
			sb.append("editorId:'#").append(editorPanel.getMarkupId()).append("',");
			sb.append("cellCount:").append(getCellCount()).append(",");
			sb.append("alternatief:").append(isAlternatief()).append(",");
			sb.append("history:").append(getResultaatJSON()).append(',');
			sb.append("info:").append(getToetsJSON()).append(',');
			sb.append("cells:[");
			sb.append("'#").append(cijferCell.getMarkupId()).append("'");
			PogingCellData altCell = getPogingCell(-1);
			if (altCell != null)
			{
				sb.append(",");
				sb.append("'#").append(altCell.getCellMarkupId()).append("'");
			}
			for (int pogingCount = 1; pogingCount <= getToets().getAantalPogingen(); pogingCount++)
			{
				PogingCellData curCell = getPogingCell(pogingCount);
				sb.append(",");
				if (curCell == null)
					sb.append("null");
				else
					sb.append("'#").append(curCell.getCellMarkupId()).append("'");
			}
			sb.append("]});\n");

			for (PogingCellData curCell : cells)
				curCell.appendJavascript(sb);
		}

		private int getCellCount()
		{
			Toets toetsObj = getToets();
			return toetsObj.getAantalPogingen() + 1
				+ (toetsObj.isAlternatiefResultaatMogelijk() ? 1 : 0);
		}

		private Toets getToets()
		{
			return toets.getObject();
		}

		private PogingCellData getPogingCell(int pogingNr)
		{
			for (PogingCellData curCell : cells)
			{
				if (curCell.getPoging() == pogingNr)
					return curCell;
			}
			return null;
		}

		@Override
		public void detach()
		{
			for (PogingCellData curCell : cells)
				curCell.detach();
			toets.detach();
			resultaten.detach();
		}
	}

	private Map<ResultaatKey, ToetsCellData> cellData =
		new LinkedHashMap<ResultaatKey, ToetsCellData>();

	private Map<ResultaatColumn<T>, String> headerIds =
		new LinkedHashMap<ResultaatColumn<T>, String>();

	private Map<ResultaatColumn<T>, List<IModel<Boolean>>> feedbackData =
		new HashMap<ResultaatColumn<T>, List<IModel<Boolean>>>();

	private ResultaatEditorPanel editorPanel;

	public void registerCijferCell(ResultaatKey key, WebMarkupContainer cell,
			ResultaatCellPanel cellPanel, IModel<List<Resultaat>> resultaten)
	{
		cellData.put(key, new ToetsCellData(cell, cellPanel, key.getToetsModel(), resultaten));
	}

	public void registerPogingCell(ResultaatKey key, WebMarkupContainer cell,
			ResultaatCellPanel cellPanel, int poging, IModel<List<Resultaat>> resultaten)
	{
		cellData.get(key).addPogingCell(cell, cellPanel, poging, resultaten);
	}

	public void registerHeaderCell(Component cell, ResultaatColumn<T> column)
	{
		headerIds.put(column, cell.getMarkupId());
	}

	public void registerFeedback(ResultaatColumn<T> column, IModel<Boolean> hasFeedback)
	{
		List<IModel<Boolean>> feedbackModels = feedbackData.get(column);
		if (feedbackModels == null)
		{
			feedbackModels = new ArrayList<IModel<Boolean>>();
			feedbackData.put(column, feedbackModels);
		}
		feedbackModels.add(hasFeedback);
	}

	public void setResultaatEditorPanel(ResultaatEditorPanel editorPanel)
	{
		this.editorPanel = editorPanel;
	}

	public String renderJavascript(int aantalPogingen, boolean toonAlternatief, boolean multiToets,
			boolean edit)
	{
		StringBuilder sb = new StringBuilder();
		// sb.append("$(document).ready(function(){\n");
		for (ToetsCellData curCellData : cellData.values())
		{
			curCellData.appendJavascript(sb);
		}
		sb.append("$('#");
		sb.append(editorPanel.getMarkupId());
		sb.append("').data('editorInfo', {");
		sb.append("editorId:'#").append(editorPanel.getMarkupId()).append("',");
		sb.append("visible:false,");
		sb.append("multiToets:").append(multiToets).append(",");
		sb.append("edit:").append(edit).append(",");
		sb.append("alternatief:").append(toonAlternatief).append(',');
		sb.append("editable:").append(editorPanel.isEditable()).append(',');
		sb.append("maxCells:").append(toonAlternatief ? aantalPogingen + 1 : aantalPogingen)
			.append(',');
		sb.append("initialVisible:[");
		boolean first = true;
		for (List<ResultaatColumn<T>> curColsPerToets : splitPerToets(headerIds.keySet()))
		{
			ResultaatColumn<T> displayCol = null;
			for (ResultaatColumn<T> curCol : curColsPerToets)
			{
				if (curCol.getPoging() == ResultatenModel.RESULTAAT_NR)
					displayCol = curCol;
				if (hasFeedback(curCol))
				{
					displayCol = curCol;
					break;
				}
			}
			if (!first)
				sb.append(",");
			first = false;
			sb.append("'#").append(headerIds.get(displayCol)).append("'");
		}
		sb.append("]");
		sb.append("});\n");
		// sb.append("});");
		return sb.toString();
	}

	private List<List<ResultaatColumn<T>>> splitPerToets(Collection<ResultaatColumn<T>> columns)
	{
		List<List<ResultaatColumn<T>>> ret = new ArrayList<List<ResultaatColumn<T>>>();
		List<ResultaatColumn<T>> curList = new ArrayList<ResultaatColumn<T>>();
		boolean createNewListOnResultaat = false;
		boolean createNewList = false;
		for (ResultaatColumn<T> curColumn : columns)
		{
			if (curColumn.getPoging() == ResultatenModel.ALTERNATIEF_NR)
			{
				createNewList = true;
				createNewListOnResultaat = false;
			}
			else if (curColumn.getPoging() == ResultatenModel.RESULTAAT_NR)
			{
				createNewList = createNewListOnResultaat;
				createNewListOnResultaat = true;
			}
			if (createNewList && !curList.isEmpty())
			{
				createNewList = false;
				ret.add(curList);
				curList = new ArrayList<ResultaatColumn<T>>();
			}
			curList.add(curColumn);
		}
		if (!curList.isEmpty())
			ret.add(curList);
		return ret;
	}

	private boolean hasFeedback(ResultaatColumn<T> column)
	{
		if (!feedbackData.containsKey(column))
			return false;
		for (IModel<Boolean> curFeedbackModel : feedbackData.get(column))
		{
			if (Boolean.TRUE.equals(curFeedbackModel.getObject()))
				return true;
		}
		return false;
	}

	private void renderMessage(StringBuilder sb, ResultaatCellPanel cellPanel)
	{
		if (cellPanel.hasFeedback())
		{
			sb.append("message:\"");
			sb.append(StringUtil.escapeForJavascriptString(cellPanel.getFeedback()));
			sb.append("\",");
			sb.append("immediate:true,");
		}
		else
		{
			String message = cellPanel.getUserMessage();
			if (StringUtil.isNotEmpty(message))
			{
				sb.append("message:\"");
				sb.append(StringUtil.escapeForJavascriptString(message));
				sb.append("\",");
				sb.append("immediate:false,");
			}
		}
	}

	public void reset()
	{
		cellData.clear();
		feedbackData.clear();
		headerIds.clear();
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(cellData);
		ComponentUtil.detachQuietly(feedbackData.values());
		ComponentUtil.detachQuietly(feedbackData.keySet());
	}
}
