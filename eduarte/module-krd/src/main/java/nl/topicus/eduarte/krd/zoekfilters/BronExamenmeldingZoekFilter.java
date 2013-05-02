package nl.topicus.eduarte.krd.zoekfilters;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronExamenverzameling;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronExamenMelding;
import nl.topicus.eduarte.krd.web.components.choice.BronAanleverpuntComboBox;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;

import org.apache.wicket.model.IModel;

public class BronExamenmeldingZoekFilter extends AbstractZoekFilter<IBronExamenMelding>
{
	private static final long serialVersionUID = 1L;

	private IModel<BronExamenverzameling> examenverzameling;

	@AutoForm(editorClass = EnumCombobox.class, htmlClasses = "unit_160")
	private BronOnderwijssoort bronOnderwijssoort;

	@AutoForm(editorClass = BronAanleverpuntComboBox.class)
	private IModel<BronAanleverpunt> aanleverpunt;

	public BronExamenmeldingZoekFilter()
	{
	}

	public BronOnderwijssoort getBronOnderwijssoort()
	{
		return bronOnderwijssoort;
	}

	public void setBronOnderwijssoort(BronOnderwijssoort bronOnderwijssoort)
	{
		this.bronOnderwijssoort = bronOnderwijssoort;
	}

	public void setAanleverpunt(BronAanleverpunt aanleverpunt)
	{
		this.aanleverpunt = makeModelFor(aanleverpunt);
	}

	public BronAanleverpunt getAanleverpunt()
	{
		return getModelObject(aanleverpunt);
	}

	public void setExamenverzameling(BronExamenverzameling examenverzameling)
	{
		this.examenverzameling = makeModelFor(examenverzameling);
	}

	public BronExamenverzameling getExamenverzameling()
	{
		return getModelObject(examenverzameling);
	}

}