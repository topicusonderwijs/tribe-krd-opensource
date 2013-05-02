package nl.topicus.eduarte.web.components.resultaat;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

public class ResultaatEditorSliderPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public ResultaatEditorSliderPanel(String id, int aantalPogingen, boolean left)
	{
		super(id);

		List<Integer> pogingen = collectPogingen(aantalPogingen, left);
		createHeaders(pogingen);
		createWaardes(pogingen);
	}

	private List<Integer> collectPogingen(int aantalPogingen, boolean left)
	{
		List<Integer> pogingen = new ArrayList<Integer>();
		pogingen.add(ResultatenModel.RESULTAAT_NR);
		pogingen.add(ResultatenModel.ALTERNATIEF_NR);
		for (int curPoging = 1; curPoging <= aantalPogingen; curPoging++)
		{
			pogingen.add(curPoging);
		}
		if (left)
			pogingen.remove(aantalPogingen == 0 ? 0 : pogingen.size() - 1);
		else
			pogingen.remove(0);
		return pogingen;
	}

	private void createHeaders(List<Integer> pogingen)
	{
		add(new ListView<Integer>("headers", pogingen)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Integer> item)
			{
				int poging = item.getModelObject();
				item.add(new Label("header", getPogingLabel(poging)));

				item.add(new AppendingAttributeModifier("class", getPogingClass(poging), " "));
			}
		});
	}

	private void createWaardes(List<Integer> pogingen)
	{
		add(new ListView<Integer>("waardes", pogingen)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Integer> item)
			{
				int poging = item.getModelObject();
				item.add(new AppendingAttributeModifier("class", getPogingClass(poging), " "));
			}
		});
	}

	private String getPogingClass(int poging)
	{
		return poging == -1 ? "ciAlternatief" : "ciPoging-" + poging;
	}

	private String getPogingLabel(int poging)
	{
		switch (poging)
		{
			case ResultatenModel.ALTERNATIEF_NR:
				return "Alt.";
			case ResultatenModel.RESULTAAT_NR:
				return "Res.";
			default:
				return "Pog. " + poging;
		}
	}
}
