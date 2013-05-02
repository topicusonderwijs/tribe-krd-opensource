/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.columns;

import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameContextDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.productregel.Productregel.TypeProductregel;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Panel om te tonen in een customdatapanel, die het afgenomen onderwijsproduct toont een
 * een tekst dat deze verplicht nog ingevuld moet worden, of niets
 * 
 * @author vandekamp
 */
public class OndPrAfnKeuzeColumnPanel extends Panel
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

	public OndPrAfnKeuzeColumnPanel(String id, IModel<Productregel> productregelModel,
			IModel<Verbintenis> verbintenisModel)
	{
		super(id);
		keuzesModel = new KeuzesModel(verbintenisModel);
		Productregel productregel = productregelModel.getObject();
		Verbintenis verbintenis = verbintenisModel.getObject();
		if (productregel.getTypeProductregel() == TypeProductregel.Productregel)
		{
			OnderwijsproductAfnameContext context = getOnderwijsproductAfnameContext(productregel);
			if (context != null)
				add(new Label("label", context.getOnderwijsproductAfname().getOnderwijsproduct()
					.getCodeTitelEnExterneCode()));
			else if (productregel.isVerplicht())
			{
				add(new Label("label", "Deze keuze is verplicht").add(new SimpleAttributeModifier(
					"style", "color: red;")));
			}
			else
			{
				add(new Label("label", ""));
			}
		}
		else
		{
			// Afgeleide productregel.
			Map<Productregel, OnderwijsproductAfnameContext> keuzes = keuzesModel.getObject();
			List<OnderwijsproductAfname> afnames =
				productregel.getAfgeleideProductregelKeuzes(verbintenis.getOpleiding(), keuzes
					.values());
			String label =
				StringUtil.toString(afnames, "",
					new StringUtil.StringConverter<OnderwijsproductAfname>()
					{

						@Override
						public String getSeparator(int listIndex)
						{
							return ", ";
						}

						@Override
						public String toString(OnderwijsproductAfname object, int listIndex)
						{
							return object.getOnderwijsproduct().getCode();
						}

					});
			add(new Label("label", label));
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
