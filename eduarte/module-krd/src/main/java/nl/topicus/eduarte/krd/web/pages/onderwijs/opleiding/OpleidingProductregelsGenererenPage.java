package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Aggregatieniveau;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductStatus;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductTaxonomie;
import nl.topicus.eduarte.entities.onderwijsproduct.SoortOnderwijsproduct;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.productregel.SoortProductregel;
import nl.topicus.eduarte.entities.productregel.ToegestaanOnderwijsproduct;
import nl.topicus.eduarte.entities.productregel.Productregel.TypeProductregel;
import nl.topicus.eduarte.entities.taxonomie.Deelgebied;
import nl.topicus.eduarte.entities.taxonomie.SoortTaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElementType;
import nl.topicus.eduarte.krd.principals.onderwijs.OpleidingWrite;
import nl.topicus.eduarte.krd.web.components.choice.SoortProductregelCombobox;
import nl.topicus.eduarte.krd.web.validators.ValidVariableNameValidator;
import nl.topicus.eduarte.providers.TaxonomieProvider;
import nl.topicus.eduarte.web.components.choice.AggregatieniveauCombobox;
import nl.topicus.eduarte.web.components.choice.SoortOnderwijsproductCombobox;
import nl.topicus.eduarte.web.components.choice.TaxonomieElementTypeCombobox;
import nl.topicus.eduarte.web.components.menu.OpleidingMenuItem;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.AbstractOpleidingPage;
import nl.topicus.eduarte.zoekfilters.TaxonomieElementZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * Pagina waarmee productregels aangemaakt kunnen worden voor een opleiding op basis van
 * een template. Bij het maken van de productregels kun je zelf kiezen of de productregels
 * gegenereerd moeten worden op basis van deelgebieden of dat je zelf het aantal en de
 * namen van de productregels wil kunnen aangeven.
 * 
 * @author loite
 * 
 */
@PageInfo(title = "Productregels genereren", menu = {"Onderwijs > Opleidingen > [Opleiding] > Productregels > Genereren"})
@InPrincipal(OpleidingWrite.class)
public class OpleidingProductregelsGenererenPage extends AbstractOpleidingPage implements
		IModuleEditPage<Opleiding>
{
	private static final long serialVersionUID = 1L;

	private static final class GegenereerdeProductregel implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private int volgnummer;

		private String code;

		private String naam;

		private String onderwijsproductCode;

		private String taxonomiecode;

		private GegenereerdeProductregel(int volgnummer, String code, String naam,
				String taxonomiecode, String onderwijsproductCode)
		{
			this.volgnummer = volgnummer;
			this.code = code;
			this.naam = naam;
			this.taxonomiecode = taxonomiecode;
			this.onderwijsproductCode = onderwijsproductCode;
		}
	}

	private final class OpleidingTaxonomieProvider implements TaxonomieProvider
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Taxonomie getTaxonomie()
		{
			return getContextOpleiding().getVerbintenisgebied().getTaxonomie();
		}

	}

	private final Form<Void> form;

	private List<GegenereerdeProductregel> productregels =
		new ArrayList<GegenereerdeProductregel>();

	public static enum OnderwijsproductKoppeling
	{
		DirectAanmaken,
		BestaandeKoppelen;

		@Override
		public String toString()
		{
			return StringUtil.convertCamelCase(name());
		}
	}

	public OpleidingProductregelsGenererenPage(Opleiding opleiding, final IModel<Cohort> cohortModel)
	{
		super(OpleidingMenuItem.Productregels, opleiding);
		final SoortProductregelCombobox soortProductregelCombo =
			new SoortProductregelCombobox("soortProductregel", ModelFactory
				.getModel((SoortProductregel) null), ModelFactory.getModel(opleiding
				.getVerbintenisgebied().getTaxonomie()));
		soortProductregelCombo.setNullValid(false);
		soortProductregelCombo.setRequired(true);
		final AggregatieniveauCombobox aggregatieniveauCombo =
			new AggregatieniveauCombobox("aggregatieniveau", ModelFactory
				.getModel((Aggregatieniveau) null));
		aggregatieniveauCombo.setNullValid(false);
		aggregatieniveauCombo.setRequired(true);
		final SoortOnderwijsproductCombobox soortOnderwijsproductCombo =
			new SoortOnderwijsproductCombobox("soortOnderwijsproduct", ModelFactory
				.getModel((SoortOnderwijsproduct) null));
		soortOnderwijsproductCombo.setNullValid(false);
		soortOnderwijsproductCombo.setRequired(true);
		final EnumCombobox<OnderwijsproductKoppeling> onderwijsproductKoppeling =
			new EnumCombobox<OnderwijsproductKoppeling>("onderwijsproductenGenereren", ModelFactory
				.getModel(OnderwijsproductKoppeling.DirectAanmaken), OnderwijsproductKoppeling
				.values());
		soortOnderwijsproductCombo.setNullValid(false);
		soortOnderwijsproductCombo.setRequired(true);
		form = new Form<Void>("form")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				Cohort cohort = cohortModel.getObject();
				SoortProductregel soort = soortProductregelCombo.getModelObject();
				Aggregatieniveau aggregatieniveau = aggregatieniveauCombo.getModelObject();
				SoortOnderwijsproduct soortProduct = soortOnderwijsproductCombo.getModelObject();
				OnderwijsproductKoppeling koppeling = onderwijsproductKoppeling.getModelObject();
				// Check op unieke namen.
				for (GegenereerdeProductregel gp : productregels)
				{
					for (GegenereerdeProductregel gp2 : productregels)
					{
						if (gp != gp2 && gp.code.equals(gp2.code))
						{
							error("De code " + gp.code + " wordt meerdere keren gebruikt");
							return;
						}
					}
				}
				for (GegenereerdeProductregel gp : productregels)
				{
					// Maak productregel
					Productregel productregel = new Productregel(EntiteitContext.INSTELLING);
					productregel.setAfkorting(gp.code);
					productregel.setCohort(cohort);
					productregel.setNaam(gp.naam);
					productregel.setOpleiding(getContextOpleiding());
					productregel.setSoortProductregel(soort);
					productregel.setTypeProductregel(TypeProductregel.Productregel);
					productregel.setVerbintenisgebied(getContextOpleiding().getVerbintenisgebied());
					productregel.setVerplicht(true);
					productregel.setVolgnummer(gp.volgnummer);
					productregel.save();
					Onderwijsproduct onderwijsproduct = null;

					if (OnderwijsproductKoppeling.BestaandeKoppelen == koppeling)
					{
						// Haal bestaand onderwijsproduct op
						onderwijsproduct = getOnderwijsproduct(gp.taxonomiecode);
					}
					else
					{
						// Maak onderwijsproduct.
						String productcode = gp.taxonomiecode == null ? gp.code : gp.taxonomiecode;
						onderwijsproduct =
							DataAccessRegistry.getHelper(OnderwijsproductDataAccessHelper.class)
								.get(productcode);
						if (onderwijsproduct == null)
						{
							onderwijsproduct = new Onderwijsproduct();
							onderwijsproduct.setAggregatieniveau(aggregatieniveau);
							onderwijsproduct.setBegindatum(cohort.getBegindatum());
							onderwijsproduct.setCode(productcode);
							onderwijsproduct.setOmschrijving(gp.naam);
							onderwijsproduct.setSoortProduct(soortProduct);
							onderwijsproduct.setStartonderwijsproduct(true);
							onderwijsproduct.setStatus(OnderwijsproductStatus.Beschikbaar);
							onderwijsproduct.setTitel(gp.naam);
							onderwijsproduct.save();
							// Maak koppeling tussen onderwijsproduct en taxonomie.
							if (gp.taxonomiecode != null)
							{
								TaxonomieElement element =
									DataAccessRegistry.getHelper(
										TaxonomieElementDataAccessHelper.class).get(
										gp.taxonomiecode);
								OnderwijsproductTaxonomie ot =
									new OnderwijsproductTaxonomie(element, onderwijsproduct);
								ot.save();
								onderwijsproduct.getOnderwijsproductTaxonomieList().add(ot);
							}
						}
					}
					// Koppel onderwijsproduct aan productregel.
					if (onderwijsproduct != null)
					{
						ToegestaanOnderwijsproduct to = new ToegestaanOnderwijsproduct();
						to.setOnderwijsproduct(onderwijsproduct);
						to.setProductregel(productregel);
						to.save();
						productregel.getToegestaneOnderwijsproducten().add(to);
					}
				}
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(new OpleidingProductregelsPage(getContextOpleiding()));
			}
		};
		add(form);
		final Form<Void> aantalForm = new Form<Void>("aantalForm");
		add(aantalForm);
		aantalForm.add(soortProductregelCombo);
		aantalForm.add(aggregatieniveauCombo);
		aantalForm.add(soortOnderwijsproductCombo);
		aantalForm.add(onderwijsproductKoppeling);
		aantalForm.add(ComponentFactory.getDataLabel("cohort", new PropertyModel<String>(
			cohortModel, "naam")));
		final RequiredTextField<Integer> aantalField =
			new RequiredTextField<Integer>("aantal", new Model<Integer>(), Integer.class);
		aantalField.setOutputMarkupId(true);
		aantalForm.add(aantalField);
		final TextField<String> taxonomiecodeFilter =
			new TextField<String>("taxonomiecodeFilter", new Model<String>());
		taxonomiecodeFilter.setOutputMarkupId(true);
		aantalForm.add(taxonomiecodeFilter);
		final IModel<TaxonomieElementType> typeModel =
			ModelFactory.getModel((TaxonomieElementType) null);
		TaxonomieElementTypeCombobox typeCombo =
			new TaxonomieElementTypeCombobox("deelgebied", typeModel,
				new OpleidingTaxonomieProvider(), false, SoortTaxonomieElement.Deelgebied)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target, TaxonomieElementType newSelection)
				{
					super.onUpdate(target, newSelection);
					aantalField.setEnabled(newSelection == null);
					target.addComponent(aantalField);
				}
			};
		typeCombo.setAutoSelectOnlyOption(false);
		typeCombo.setNullValid(true);
		aantalForm.add(typeCombo);

		final WebMarkupContainer container = new WebMarkupContainer("productregels");
		container.setOutputMarkupId(true);
		form.add(container);
		final ListView<GegenereerdeProductregel> listview =
			new ListView<GegenereerdeProductregel>("productregel", productregels)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<GegenereerdeProductregel> item)
				{
					item.add(new RequiredTextField<Integer>("volgnummer", Integer.class));
					item.add(new RequiredTextField<String>("code")
						.add(new ValidVariableNameValidator()));
					item.add(new RequiredTextField<String>("naam"));
					GegenereerdeProductregel gp = item.getModelObject();
					OnderwijsproductKoppeling koppeling =
						onderwijsproductKoppeling.getModelObject();
					if (OnderwijsproductKoppeling.DirectAanmaken == koppeling)
						item.add(new Label("onderwijsproductCode",
							"Onderwijsproduct moet nog aangemaakt worden"));
					else if (!StringUtil.isEmpty(gp.onderwijsproductCode))
						item.add(new Label("onderwijsproductCode"));
					else
						item.add(new Label("onderwijsproductCode",
							"Geen of meerdere onderwijsproducten gevonden"));
				}

				@Override
				protected IModel<GegenereerdeProductregel> getListItemModel(
						IModel< ? extends List<GegenereerdeProductregel>> listViewModel, int index)
				{
					return new CompoundPropertyModel<GegenereerdeProductregel>(super
						.getListItemModel(listViewModel, index));
				}
			};
		listview.setReuseItems(true);
		container.add(listview);
		container.add(new WebMarkupContainer("geenElementen")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return listview.getList().isEmpty();
			}
		});

		aantalForm.add(new AjaxSubmitLink("genereren", aantalForm)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target, Form< ? > submittedForm)
			{
				Integer aantalObject = aantalField.getModelObject();
				TaxonomieElementType type = typeModel.getObject();
				OnderwijsproductKoppeling ondProdKop = onderwijsproductKoppeling.getModelObject();
				String taxcodeFilter = taxonomiecodeFilter.getModelObject();
				int aantal = 0;
				if (aantalObject != null)
					aantal = aantalObject.intValue();
				if (type == null && aantal < 1)
				{
					error("Selecteer een deelgebied of voer een aantal in");
					refreshFeedback(target);
				}
				else
				{
					productregels = genereerProductregels(type, aantal, ondProdKop, taxcodeFilter);
					listview.setList(productregels);
					target.addComponent(container);
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > errorForm)
			{
				refreshFeedback(target);
			}

		});

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form));
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new OpleidingProductregelsPage(getContextOpleiding());
			}

			@Override
			public Class<OpleidingProductregelsPage> getPageIdentity()
			{
				return OpleidingProductregelsPage.class;
			}

		}));
	}

	private List<GegenereerdeProductregel> genereerProductregels(TaxonomieElementType type,
			int aantal, OnderwijsproductKoppeling ondProdKop, String taxcodeFilter)
	{
		List<GegenereerdeProductregel> res;
		if (type == null)
		{
			res = new ArrayList<GegenereerdeProductregel>(aantal);
			for (int i = 0; i < aantal; i++)
			{
				res.add(new GegenereerdeProductregel(i + 1, null, null, null, null));
			}
		}
		else
		{
			TaxonomieElementDataAccessHelper helper =
				DataAccessRegistry.getHelper(TaxonomieElementDataAccessHelper.class);
			TaxonomieElementZoekFilter filter = new TaxonomieElementZoekFilter(Deelgebied.class);
			filter.setTaxonomieElementType(type);
			String taxonomiecode = getContextOpleiding().getVerbintenisgebied().getTaxonomiecode();
			if (taxcodeFilter != null)
				taxonomiecode = taxonomiecode.concat("_" + taxcodeFilter);
			filter.setTaxonomiecode(taxonomiecode);
			List<TaxonomieElement> deelgebieden = helper.list(filter);
			res = new ArrayList<GegenereerdeProductregel>(deelgebieden.size());
			int i = 1;
			Onderwijsproduct onderwijsproduct = null;
			for (TaxonomieElement deelgebied : deelgebieden)
			{
				if (OnderwijsproductKoppeling.BestaandeKoppelen == ondProdKop)
					onderwijsproduct = getOnderwijsproduct(deelgebied.getTaxonomiecode());
				res.add(new GegenereerdeProductregel(i, deelgebied.getTaxonomieElementType()
					.getAfkorting()
					+ deelgebied.getAfkorting(), deelgebied.getNaam(), deelgebied
					.getTaxonomiecode(), onderwijsproduct != null ? onderwijsproduct.getCode()
					: null));
				i++;
			}

		}
		return res;
	}

	private Onderwijsproduct getOnderwijsproduct(String taxonomiecode)
	{
		OnderwijsproductDataAccessHelper helper =
			DataAccessRegistry.getHelper(OnderwijsproductDataAccessHelper.class);
		List<Onderwijsproduct> onderwijsproducten =
			helper.getOnderwijsproductByTaxCode(taxonomiecode);
		if (onderwijsproducten.size() == 1)
			return onderwijsproducten.get(0);
		return null;
	}

}
