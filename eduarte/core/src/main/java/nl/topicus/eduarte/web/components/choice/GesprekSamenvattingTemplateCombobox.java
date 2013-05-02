package nl.topicus.eduarte.web.components.choice;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.dbs.GesprekSamenvattingTemplateDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.trajecten.GesprekSamenvattingTemplate;

import org.apache.wicket.model.IModel;

/**
 * @author N. Henzen
 */
public class GesprekSamenvattingTemplateCombobox extends
		AbstractAjaxDropDownChoice<GesprekSamenvattingTemplate>
{
	private static final long serialVersionUID = 1L;

	public GesprekSamenvattingTemplateCombobox(String id)
	{
		this(id, null);
	}

	public GesprekSamenvattingTemplateCombobox(String id, IModel<GesprekSamenvattingTemplate> model)
	{
		super(id, model, ModelFactory.getListModel(DataAccessRegistry.getHelper(
			GesprekSamenvattingTemplateDataAccessHelper.class).list(
			GesprekSamenvattingTemplate.class)), new GesprekSamenvattingTemplateRenderer());
	}
}
