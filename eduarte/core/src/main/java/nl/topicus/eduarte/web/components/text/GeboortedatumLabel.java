package nl.topicus.eduarte.web.components.text;

import java.util.Date;

import nl.topicus.eduarte.entities.personen.Persoon.ToepassingGeboortedatum;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

public class GeboortedatumLabel extends Label
{
	private static final long serialVersionUID = 1L;

	private IModel<ToepassingGeboortedatum> toepassingModel;

	public GeboortedatumLabel(String id, IModel<Date> geboortedatumModel,
			IModel<ToepassingGeboortedatum> toepassingModel)
	{
		super(id, geboortedatumModel);

		this.toepassingModel = toepassingModel;
	}

	@Override
	public IConverter getConverter(Class< ? > type)
	{
		if (getToepassingGeboortedatum() != ToepassingGeboortedatum.Geboortejaar
			&& getToepassingGeboortedatum() != ToepassingGeboortedatum.GeboortemaandEnJaar)
		{
			return super.getConverter(type);
		}
		else
		{
			return new GeboorteMaandJaarConverter(getToepassingGeboortedatum());
		}
	}

	protected ToepassingGeboortedatum getToepassingGeboortedatum()
	{
		return toepassingModel.getObject();
	}
}
