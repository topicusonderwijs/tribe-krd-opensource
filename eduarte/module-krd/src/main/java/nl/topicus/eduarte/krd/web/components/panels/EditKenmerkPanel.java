package nl.topicus.eduarte.krd.web.components.panels;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.components.text.RequiredDatumField;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.eduarte.entities.kenmerk.KenmerkCategorie;
import nl.topicus.eduarte.providers.KenmerkProvider;
import nl.topicus.eduarte.web.components.choice.KenmerkCategorieCombobox;
import nl.topicus.eduarte.web.components.choice.KenmerkCombobox;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;

/**
 * @author loite
 */
public class EditKenmerkPanel<T extends KenmerkProvider> extends TypedPanel<T>
{
	private static final long serialVersionUID = 1L;

	private KenmerkCombobox kenmerkCombo;

	public EditKenmerkPanel(String id, IModel<T> model, Form< ? > form)
	{
		super(id, model);
		KenmerkProvider kenmerk = model.getObject();
		KenmerkCategorieCombobox categorieCombo =
			new KenmerkCategorieCombobox("categorie", ModelFactory.getModel(kenmerk == null
				|| kenmerk.getKenmerk() == null ? null : kenmerk.getKenmerk().getCategorie()))
			{

				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target, KenmerkCategorie newSelection)
				{
					kenmerkCombo.setModelObject(null);
					target.addComponent(kenmerkCombo);
				}
			};
		categorieCombo.setNullValid(false).setRequired(true);
		add(categorieCombo);
		kenmerkCombo = new KenmerkCombobox("kenmerk", categorieCombo.getModel());
		kenmerkCombo.setNullValid(false).setRequired(true);
		add(kenmerkCombo);
		categorieCombo.connectListForAjaxRefresh(kenmerkCombo);
		TextArea<String> toelichtingField = new TextArea<String>("toelichting");
		add(toelichtingField);
		RequiredDatumField begindatumField = new RequiredDatumField("begindatum");
		add(begindatumField);
		DatumField einddatumField = new DatumField("einddatum");
		add(einddatumField);
		form.add(new BegindatumVoorEinddatumValidator(begindatumField, einddatumField));
	}
}
