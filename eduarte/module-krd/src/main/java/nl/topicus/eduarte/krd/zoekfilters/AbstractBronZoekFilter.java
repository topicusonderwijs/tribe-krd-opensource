package nl.topicus.eduarte.krd.zoekfilters;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.eduarte.krd.web.components.choice.BronAanleverpuntComboBox;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

public abstract class AbstractBronZoekFilter<T> extends AbstractZoekFilter<T>
{
	@AutoForm(editorClass = BronAanleverpuntComboBox.class, htmlClasses = "unit_100")
	private IModel<BronAanleverpunt> bronAanleverpunt;

	@AutoForm(editorClass = DropDownChoice.class, htmlClasses = "unit_100")
	private IModel<Schooljaar> schooljaar;

	private static final long serialVersionUID = 1L;

	protected AbstractBronZoekFilter()
	{
	}

	public AbstractBronZoekFilter(IModel<BronSchooljaarStatus> bronSchooljaarStatusModel)
	{
		BronSchooljaarStatus status = bronSchooljaarStatusModel.getObject();
		setBronAanleverpunt(status.getAanleverpunt());
		setSchooljaar(status.getSchooljaar());
	}

	public AbstractBronZoekFilter(BronAanleverpunt aanleverpunt, Schooljaar schooljaar)
	{
		setBronAanleverpunt(aanleverpunt);
		setSchooljaar(schooljaar);
	}

	public BronAanleverpunt getBronAanleverpunt()
	{
		return getModelObject(bronAanleverpunt);
	}

	public void setBronAanleverpunt(BronAanleverpunt bronAanleverpunt)
	{
		this.bronAanleverpunt = makeModelFor(bronAanleverpunt);
	}

	public Schooljaar getSchooljaar()
	{
		return getModelObject(schooljaar);
	}

	public void setSchooljaar(Schooljaar schooljaar)
	{
		this.schooljaar = makeModelFor(schooljaar);
	}

	public List<Schooljaar> getSchooljaarList()
	{
		List<Schooljaar> ret = new ArrayList<Schooljaar>();
		Schooljaar huidig = Schooljaar.huidigSchooljaar();
		ret.add(huidig.getVorigSchooljaar().getVorigSchooljaar());
		ret.add(huidig.getVorigSchooljaar());
		ret.add(huidig);
		ret.add(huidig.getVolgendSchooljaar());
		return ret;
	}
}