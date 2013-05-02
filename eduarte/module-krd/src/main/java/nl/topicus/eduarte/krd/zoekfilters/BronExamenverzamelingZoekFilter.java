package nl.topicus.eduarte.krd.zoekfilters;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.krd.entities.bron.BronExamenverzameling;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;

import org.apache.wicket.model.IModel;

public class BronExamenverzamelingZoekFilter extends AbstractBronZoekFilter<BronExamenverzameling>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(editorClass = EnumCombobox.class, htmlClasses = "unit_160")
	private BronOnderwijssoort bronOnderwijssoort;

	public BronExamenverzamelingZoekFilter(IModel<BronSchooljaarStatus> bronSchooljaarStatusModel)
	{
		super(bronSchooljaarStatusModel);
	}

	public BronOnderwijssoort getBronOnderwijssoort()
	{
		return bronOnderwijssoort;
	}

	public void setBronOnderwijssoort(BronOnderwijssoort bronOnderwijssoort)
	{
		this.bronOnderwijssoort = bronOnderwijssoort;
	}
}