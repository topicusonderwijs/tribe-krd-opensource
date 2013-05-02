package nl.topicus.eduarte.resultaten.web.components.resultaat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.web.components.resultaat.EditorJavascriptRenderer;
import nl.topicus.eduarte.web.components.resultaat.ResultaatColumnCreator;
import nl.topicus.eduarte.web.components.resultaat.ResultatenUIFactory;
import nl.topicus.eduarte.zoekfilters.ResultaatZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class EditResultatenUIFactory<T> implements ResultatenUIFactory<T, ResultatenEditModel>
{
	private static final long serialVersionUID = 1L;

	public static class ExtraInputFieldsBean implements Serializable
	{
		private static final long serialVersionUID = 1L;

		@AutoForm(required = true, htmlClasses = "unit_80")
		private Date datumBehaald;

		public ExtraInputFieldsBean()
		{
			datumBehaald = TimeUtil.getInstance().currentDate();
		}

		public Date getDatumBehaald()
		{
			return datumBehaald;
		}

		public void setDatumBehaald(Date datumBehaald)
		{
			this.datumBehaald = datumBehaald;
		}
	}

	private ExtraInputFieldsBean extraInputFields;

	public EditResultatenUIFactory()
	{
		extraInputFields = new ExtraInputFieldsBean();
	}

	@Override
	public ResultaatColumnCreator<T, ResultatenEditModel> createColumnCreator(
			EditorJavascriptRenderer<T> javascriptRenderer)
	{
		return new ResultaatEditColumnCreator<T>(javascriptRenderer, extraInputFields);
	}

	@Override
	public ResultatenEditModel createResultatenModel(ResultaatZoekFilter resultaatFilter,
			IModel< ? extends List<Toets>> toetsenModel,
			IModel< ? extends List<Deelnemer>> deelnemersModel)
	{
		return new ResultatenEditModel(resultaatFilter, toetsenModel, deelnemersModel);
	}

	@Override
	public Component createInputfields(String id)
	{
		AutoFieldSet<ExtraInputFieldsBean> ret =
			new AutoFieldSet<ExtraInputFieldsBean>(id, Model.of(extraInputFields));
		ret.setRenderMode(RenderMode.EDIT);
		return ret;
	}
}
