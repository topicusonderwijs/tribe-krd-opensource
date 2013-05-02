/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.participatie.web.components.choice.combobox;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.eduarte.entities.participatie.enums.MaatregelRegelSoort;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vandekamp
 */
public class MaatregelRegelSoortComboBox extends DropDownChoice<MaatregelRegelSoort>
{
	private static final long serialVersionUID = 1L;

	public MaatregelRegelSoortComboBox(String id)
	{
		super(id, new MaatregelRegelSoortModel());
		setRequired(true);
	}

	private static class MaatregelRegelSoortModel extends
			LoadableDetachableModel<List<MaatregelRegelSoort>>
	{
		private static final long serialVersionUID = 1L;

		private List<MaatregelRegelSoort> MaatregelRegelSoorten;

		public MaatregelRegelSoortModel()
		{
			MaatregelRegelSoorten = new ArrayList<MaatregelRegelSoort>();
			MaatregelRegelSoorten.add(MaatregelRegelSoort.Gelijk_Aan);
			MaatregelRegelSoorten.add(MaatregelRegelSoort.Elke_X_Meldingen);
		}

		@Override
		protected List<MaatregelRegelSoort> load()
		{
			return MaatregelRegelSoorten;
		}
	}
}
