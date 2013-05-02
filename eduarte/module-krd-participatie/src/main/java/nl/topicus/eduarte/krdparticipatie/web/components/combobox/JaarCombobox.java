/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krdparticipatie.web.components.combobox;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.util.TimeUtil;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Combobox om een x aantal jaren te tonen. Geen Schooljaren!
 * 
 * @author marrink
 */
public class JaarCombobox extends DropDownChoice<Integer>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Combo toont 10 jaar voor huidige jaartal en 5 erna, totaal dus 16 jaren.
	 * 
	 * @param id
	 */
	public JaarCombobox(String id)
	{
		this(id, TimeUtil.getInstance().getCurrentYear() - 10, TimeUtil.getInstance()
			.getCurrentYear() + 5);
	}

	/**
	 * Combo met dagen iig Ma tm Za, en evt Zo.
	 * 
	 * @param id
	 * @param begin
	 *            eerste jaar dat getoond wordt
	 * @param eind
	 *            laatste jaar dat getoond wordt
	 */
	public JaarCombobox(String id, int begin, int eind)
	{
		this(id, null, begin, eind);
	}

	/**
	 * Combo met Ma tm Zo.
	 * 
	 * @param id
	 * @param model
	 * @param begin
	 *            eerste jaar dat getoond wordt
	 * @param eind
	 *            laatste jaar dat getoond wordt
	 */
	public JaarCombobox(String id, IModel<Integer> model, int begin, int eind)
	{
		super(id, model, new ListModel(begin, eind));
		setRequired(false);
		setNullValid(false);
	}

	/**
	 * Lijst met jaartallen, inc begin en eind jaartallen
	 * 
	 * @author marrink
	 */
	private static final class ListModel extends LoadableDetachableModel<List<Integer>>
	{
		private static final long serialVersionUID = 1L;

		private final int begin;

		private final int eind;

		/**
		 * @param zondag
		 *            lijst bevat wel of niet 7
		 */
		private ListModel(int begin, int eind)
		{
			super();
			this.begin = begin;
			this.eind = eind;
		}

		@Override
		protected List<Integer> load()
		{
			List<Integer> jaren = new ArrayList<Integer>(eind - begin);
			for (int i = begin; i < eind; i++)
				jaren.add(i);
			return jaren;
		}

	}
}
