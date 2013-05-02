/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.choice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.web.components.choice.renderer.VerbintenisRenderer;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Combobox om de inschrijving van een deelnemer te kunnen selecteren
 * 
 * @author marrink
 */
public class InschrijvingCombobox extends DropDownChoice<Verbintenis>
{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor voor gebruik met {@link CompoundPropertyModel}.
	 * 
	 * @param id
	 * @param deelnemer
	 */
	public InschrijvingCombobox(String id, IModel<Deelnemer> deelnemer)
	{
		this(id, null, deelnemer);
	}

	/**
	 * @param id
	 * @param model
	 *            model waar de inschrijving op gezet word.
	 * @param deelnemer
	 */
	public InschrijvingCombobox(String id, IModel<Verbintenis> model, IModel<Deelnemer> deelnemer)
	{
		super(id, model, (IModel<List<Verbintenis>>) null, new VerbintenisRenderer());
		setChoices(new ListModel(deelnemer));
		setRequired(false);
		setNullValid(false);
	}

	private final class ListModel extends LoadableDetachableModel<List<Verbintenis>>
	{
		private static final long serialVersionUID = 1L;

		private IModel<Deelnemer> deelnemer;

		public ListModel(IModel<Deelnemer> deelnemer)
		{
			super();
			this.deelnemer = deelnemer;
		}

		@Override
		protected List<Verbintenis> load()
		{
			Deelnemer myDeelnemer = deelnemer.getObject();
			if (myDeelnemer == null)
				return Collections.emptyList();
			List<Verbintenis> ret = new ArrayList<Verbintenis>(myDeelnemer.getVerbintenissen());
			Iterator<Verbintenis> it = ret.iterator();
			while (it.hasNext())
				if (!showVerbintenis(it.next()))
					it.remove();
			return ret;
		}

		@Override
		protected void onDetach()
		{
			deelnemer.detach();
		}
	}

	/**
	 * deze methode haalt een verbintenis uit de lijst van weer te geven verbintenissen
	 * als er false wordt terug gegeven
	 */
	@SuppressWarnings("unused")
	protected boolean showVerbintenis(Verbintenis verbintenis)
	{
		return true;
	}
}
