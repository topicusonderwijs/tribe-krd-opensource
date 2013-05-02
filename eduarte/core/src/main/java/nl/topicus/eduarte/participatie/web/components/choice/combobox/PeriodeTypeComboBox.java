/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.participatie.web.components.choice.combobox;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.eduarte.entities.participatie.enums.PeriodeType;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vandekamp
 */
public class PeriodeTypeComboBox extends DropDownChoice<PeriodeType>
{
	private static final long serialVersionUID = 1L;

	public PeriodeTypeComboBox(String id)
	{
		super(id, new PeriodeTypeModel());
		setRequired(true);
	}

	private static class PeriodeTypeModel extends LoadableDetachableModel<List<PeriodeType>>
	{
		private static final long serialVersionUID = 1L;

		private List<PeriodeType> periodeTypes;

		public PeriodeTypeModel()
		{
			periodeTypes = new ArrayList<PeriodeType>();
			periodeTypes.add(PeriodeType.Schooljaar);
			periodeTypes.add(PeriodeType.Laatste_x_weken);
			periodeTypes.add(PeriodeType.Periode);
		}

		@Override
		protected List<PeriodeType> load()
		{
			return periodeTypes;
		}
	}
}
