/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krdparticipatie.web.components.panels.datapanels.columns;

import java.text.NumberFormat;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.eduarte.entities.participatie.AbsentieReden;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage.AbstractActiviteitTotaal;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Toont de totalen per absentiereden
 * 
 * @author vandekamp
 */
public class AbsentieRedenenColumn<T extends AbstractActiviteitTotaal> extends
		AbstractCustomColumn<T>
{
	private static final long serialVersionUID = 1L;

	private final class StringModel extends LoadableDetachableModel<String>
	{
		private static final long serialVersionUID = 1L;

		private IModel<T> activiteitTotaalModel;

		public StringModel(IModel<T> activiteitTotaalModel)
		{
			this.activiteitTotaalModel = activiteitTotaalModel;
		}

		@Override
		protected String load()
		{
			T activiteitTotaal = activiteitTotaalModel.getObject();
			AbsentieReden reden = absentieReden.getObject();
			if (lesurenKolom)
				return activiteitTotaal.getTotaalVoorRedenLesuren(reden.getAfkorting()).toString();
			return FORMAT.format(activiteitTotaal.getTotaalVoorReden(reden.getAfkorting()));
		}

		@Override
		protected void onDetach()
		{
			super.onDetach();
			ComponentUtil.detachQuietly(activiteitTotaalModel);
		}
	}

	private IModel<AbsentieReden> absentieReden;

	private boolean lesurenKolom;

	private static final NumberFormat FORMAT = NumberFormat.getNumberInstance();
	static
	{
		FORMAT.setMinimumFractionDigits(1);
		FORMAT.setMaximumFractionDigits(1);
	}

	public AbsentieRedenenColumn(String id, String header, AbsentieReden absentieReden,
			boolean lesurenKolom)
	{
		super(id, header);
		this.absentieReden = ModelFactory.getModel(absentieReden);
		this.lesurenKolom = lesurenKolom;
	}

	@Override
	public void populateItem(WebMarkupContainer cellItem, String componentId,
			WebMarkupContainer row, IModel<T> rowModel, int span)
	{
		cellItem.add(new Label(componentId, new StringModel(rowModel)));
	}

	@Override
	public void detach()
	{
		super.detach();
		ComponentUtil.detachQuietly(absentieReden);
	}
}
