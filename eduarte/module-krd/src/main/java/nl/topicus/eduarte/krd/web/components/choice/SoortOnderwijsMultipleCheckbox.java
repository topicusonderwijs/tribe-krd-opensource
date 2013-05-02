package nl.topicus.eduarte.krd.web.components.choice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.entities.opleiding.SoortOnderwijsTax;
import nl.topicus.eduarte.krd.bron.jobs.BronExamenverzamelingenAanmakenJobDataMap;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;

import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class SoortOnderwijsMultipleCheckbox extends CheckBoxMultipleChoice<SoortOnderwijsTax>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<SoortOnderwijsTax>>
	{
		private static final long serialVersionUID = 1L;

		private IModel<BronExamenverzamelingenAanmakenJobDataMap> datamapModel;

		public ListModel(IModel<BronExamenverzamelingenAanmakenJobDataMap> datamapModel)
		{
			this.datamapModel = datamapModel;
		}

		@Override
		protected List<SoortOnderwijsTax> load()
		{
			BronExamenverzamelingenAanmakenJobDataMap datamap = datamapModel.getObject();
			if (datamap != null && BronOnderwijssoort.VAVO == datamap.getBronOnderwijssoort())
			{
				List<SoortOnderwijsTax> ret = new ArrayList<SoortOnderwijsTax>();
				ret.add(SoortOnderwijsTax.VWO);
				ret.add(SoortOnderwijsTax.HAVO);
				ret.add(SoortOnderwijsTax.VMBOTL);
				return ret;
			}
			return Arrays.asList(SoortOnderwijsTax.values());
		}

		@Override
		protected void onDetach()
		{
			ComponentUtil.detachQuietly(datamapModel);
			super.onDetach();
		}
	}

	public SoortOnderwijsMultipleCheckbox(String id,
			IModel<Collection<SoortOnderwijsTax>> propertyModel,
			IModel<BronExamenverzamelingenAanmakenJobDataMap> datamapModel)
	{
		super(id, propertyModel, new ListModel(datamapModel), new SoortOnderwijsRenderer());
	}

	@Override
	public String getInputName()
	{
		return "SoortOnderwijsMultipleCheckbox";
	}

	private static class SoortOnderwijsRenderer implements IChoiceRenderer<SoortOnderwijsTax>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Object getDisplayValue(SoortOnderwijsTax object)
		{
			return object.toString();
		}

		@Override
		public String getIdValue(SoortOnderwijsTax object, int index)
		{
			return object.toString();
		}

	}
}
