package nl.topicus.eduarte.web.components.panels.columns;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat.Resultaatsoort;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Panel voor gebruik in een custom datapanel die het eindresultaat van een
 * onderwijsproductafname van een deelnemer toont.
 * 
 * @author loite
 * 
 */
public class EindresultaatColumnPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public EindresultaatColumnPanel(String id, IModel<Productregel> productregelModel,
			IModel<Verbintenis> verbintenisModel)
	{
		super(id);
		Productregel productregel = productregelModel.getObject();
		Verbintenis verbintenis = verbintenisModel.getObject();
		Resultaat resultaat = productregel.getEindresultaat(verbintenis);
		if (resultaat == null)
		{
			add(new Label("label", ""));
		}
		else
		{
			Label label = new Label("label", resultaat.getFormattedDisplayWaarde(true));
			if (resultaat.getSoort() != Resultaatsoort.Tijdelijk)
			{
				label.add(new SimpleAttributeModifier("style", "font-weight: bold;"));
			}
			else
			{
				label.add(new SimpleAttributeModifier("style", "font-style: italic;"));
				label.add(new SimpleAttributeModifier("title", "Voorlopig resultaat"));
			}
			add(label);
		}
		setRenderBodyOnly(false);
	}

}
