package nl.topicus.eduarte.web.components.panels.adres;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.labels.JaNeeLabel;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.adres.AdresEntiteit;
import nl.topicus.eduarte.web.components.label.PostcodeWoonplaatsLabel;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

public class AdresPanel<T extends AdresEntiteit<T>> extends TypedPanel<T>
{
	private static final long serialVersionUID = 1L;

	public AdresPanel(String id, IModel<T> adresModel)
	{
		super(id, new CompoundPropertyModel<T>(adresModel));
		setRenderBodyOnly(true);
		add(new Label("adres.straatHuisnummer"));
		add(new PostcodeWoonplaatsLabel("adres.postcodePlaats"));
		add(new Label("adres.land"));
		WebMarkupContainer deelstaatContainer = new WebMarkupContainer("deelstaatContainer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				Adres adres = AdresPanel.this.getModelObject().getAdres();
				return adres.getLand() != null && adres.getLand().isDuitsland();
			}
		};
		deelstaatContainer.setOutputMarkupPlaceholderTag(true);
		add(deelstaatContainer);
		deelstaatContainer.add(new Label("adres.duitseDeelstaat"));
		add(new Label("begindatum"));
		add(new Label("einddatum")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTagBody(final MarkupStream markupStream,
					final ComponentTag openTag)
			{
				String stringRep = getDefaultModelObjectAsString();
				if (StringUtil.isEmpty(stringRep))
					stringRep = "heden";

				replaceComponentTagBody(markupStream, openTag, stringRep);
			}
		});

		add(new JaNeeLabel("adres.geheim"));
		add(new JaNeeLabel("postadres"));
		add(new JaNeeLabel("fysiekadres"));
		add(new Label("fysiekLabel"));
	}
}
