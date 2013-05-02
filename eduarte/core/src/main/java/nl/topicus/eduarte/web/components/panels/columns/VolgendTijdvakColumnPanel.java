/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.columns;

import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameContextDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.productregel.Productregel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Panel om te tonen in een customdatapanel, die het afgenomen onderwijsproduct toont een
 * een tekst of dit product doorverwezen is naar het volgend tijdvak
 * 
 * @author vandekamp
 * @author loite
 */
public class VolgendTijdvakColumnPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private static final class KeuzesModel extends
			LoadableDetachableModel<Map<Productregel, OnderwijsproductAfnameContext>>
	{
		private static final long serialVersionUID = 1L;

		private final IModel<Verbintenis> verbintenisModel;

		private KeuzesModel(IModel<Verbintenis> verbintenisModel)
		{
			this.verbintenisModel = verbintenisModel;
		}

		@Override
		protected Map<Productregel, OnderwijsproductAfnameContext> load()
		{
			Map<Productregel, OnderwijsproductAfnameContext> keuzes =
				DataAccessRegistry.getHelper(OnderwijsproductAfnameContextDataAccessHelper.class)
					.list(getVerbintenis());

			return keuzes;
		}

		private Verbintenis getVerbintenis()
		{
			return verbintenisModel.getObject();
		}

		@Override
		protected void onDetach()
		{
			ComponentUtil.detachQuietly(verbintenisModel);
		}

	}

	private final KeuzesModel keuzesModel;

	public VolgendTijdvakColumnPanel(String id, IModel<Productregel> productregelModel,
			IModel<Verbintenis> verbintenisModel)
	{
		super(id);
		keuzesModel = new KeuzesModel(verbintenisModel);
		Productregel productregel = productregelModel.getObject();
		OnderwijsproductAfnameContext context = getOnderwijsproductAfnameContext(productregel);
		if (context != null)
			add(new Label("label", context.isVerwezenNaarVolgendTijdvak() ? "Ja" : "Nee"));
		else
		{
			add(new Label("label", ""));
		}
		setRenderBodyOnly(false);
	}

	private OnderwijsproductAfnameContext getOnderwijsproductAfnameContext(Productregel productregel)
	{
		Map<Productregel, OnderwijsproductAfnameContext> keuzes = keuzesModel.getObject();
		return keuzes.get(productregel);
	}

	@Override
	public void detachModels()
	{
		ComponentUtil.detachQuietly(keuzesModel);
		super.detachModels();
	}
}
