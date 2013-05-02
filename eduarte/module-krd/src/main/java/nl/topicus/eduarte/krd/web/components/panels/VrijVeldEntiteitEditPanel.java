package nl.topicus.eduarte.krd.web.components.panels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.KoppelTabelModelSelection;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.components.wiquery.DropDownCheckList;
import nl.topicus.eduarte.dao.helpers.VrijVeldDataAccessHelper;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldEntiteit;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldKeuzeOptie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldType;
import nl.topicus.eduarte.web.components.choice.VrijVeldKeuzeOptieCombobox;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;
import nl.topicus.eduarte.web.components.panels.AbstractVrijVeldEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.VrijVeldEntiteitPanel;
import nl.topicus.eduarte.zoekfilters.VrijVeldZoekFilter;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * <p>
 * Panel welke een listview panel toont met de gekoppelde {@link VrijVeldEntiteit}en. Ook
 * zal deze listview alle nog niet gekoppelde maar wel relevante {@link VrijVeld}en tonen.
 * Men hoeft geen waarde in te vullen, ook wordt de lijst doorgekeken naar 'lege' velden
 * zodat er geen onnodige objecten worden opgeslagen.
 * </p>
 * <p>
 * Dit panel kan als volgt worden gebruikt: <br/>
 * <code>
 * new VrijVeldEntiteitEditPanel<PersoonVrijVeld>("id", new PropertyModel(getPersoon());<br/>
 * panel.setCategorie(VrijVeldCategorie.Deelnemerpersonalia);
 * panel.setDossierScherm(true);<br/>
 * </code>
 * </p>
 * <p>
 * Dit zal je een panel geven welke alle personalia velden voor een toon/edit scherm
 * toont.
 * <ul>
 * <li>Wanneer je {@link VrijVeldEntiteitPanel#setIntakeScherm(Boolean)} aanroept zal je
 * vrije velden te zien krijgen die bij een intake pagina horen.</li>
 * <li>Wanneer je {@link VrijVeldEntiteitPanel#setZoekenScherm(Boolean)} aanroept zal je
 * vrije velden te zien krijgen die bij een zoeken pagina horen.</li>
 * </ul>
 * Een combinatie hiervan werkt ook.
 * </p>
 * 
 * @author hoeve
 */
public class VrijVeldEntiteitEditPanel<T extends VrijVeldable< ? >> extends
		AbstractVrijVeldEntiteitPanel<T>
{
	private static final long serialVersionUID = 1L;

	private class KeuzeOptieSelectie extends
			KoppelTabelModelSelection<VrijVeldOptieKeuze, VrijVeldKeuzeOptie> implements
			IModel<Collection<VrijVeldKeuzeOptie>>
	{
		private static final long serialVersionUID = 1L;

		private IModel<VrijVeldEntiteit> vrijVeldEntiteit;

		/**
		 * Deze cache is niet alleen om te cachen, maar ook om DropDownCheckList te vriend
		 * te houden. Deze verwacht namelijk dat getObject steeds hetzelfde object terug
		 * geeft.
		 */
		private List<VrijVeldKeuzeOptie> selectionCache;

		public KeuzeOptieSelectie(IModel<VrijVeldEntiteit> model)
		{
			super(new PropertyModel<List<VrijVeldOptieKeuze>>(model, "keuzes"));
			vrijVeldEntiteit = model;
		}

		@Override
		protected VrijVeldKeuzeOptie convertRtoS(VrijVeldOptieKeuze object)
		{
			return object.getOptie();
		}

		@Override
		protected VrijVeldOptieKeuze newR(VrijVeldKeuzeOptie object)
		{
			VrijVeldOptieKeuze ret = new VrijVeldOptieKeuze();
			ret.setOptie(object);
			ret.setEntiteit(vrijVeldEntiteit.getObject());
			return ret;
		}

		@Override
		public List<VrijVeldKeuzeOptie> getObject()
		{
			if (selectionCache == null)
				selectionCache = getSelectedOpties();
			return selectionCache;
		}

		private List<VrijVeldKeuzeOptie> getSelectedOpties()
		{
			List<VrijVeldKeuzeOptie> ret = new ArrayList<VrijVeldKeuzeOptie>();
			for (VrijVeldOptieKeuze curOptie : getSelected())
				ret.add(curOptie.getOptie());
			return ret;
		}

		@Override
		public void setObject(Collection<VrijVeldKeuzeOptie> object)
		{
			List<VrijVeldKeuzeOptie> toDeselect = getSelectedOpties();
			toDeselect.removeAll(object);
			for (VrijVeldKeuzeOptie newKeuze : object)
				if (!isSelected(newKeuze))
					add(newKeuze);
			for (VrijVeldKeuzeOptie oldKeuze : toDeselect)
				remove(oldKeuze);
		}

		@Override
		public void detach()
		{
			super.detach();
			selectionCache = null;
			vrijVeldEntiteit.detach();
		}
	}

	private WebMarkupContainer container;

	private WebMarkupContainer geenVeldenContainer;

	private ListView<VrijVeldEntiteit> list;

	public final VrijVeldZoekFilter vrijVeldZoekFilter;

	/**
	 * Model welke eventuele nieuwe velden toevoegd zodat alle nodige velden getoond
	 * worden.
	 */
	private class ListModel extends LoadableDetachableModel<List<VrijVeldEntiteit>>
	{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		protected List<VrijVeldEntiteit> load()
		{
			if (getVrijVeldEntiteiten().size() > 0)
				getVrijVeldZoekFilter().setExclude(getVrijVelden(getVrijVeldEntiteiten()));
			else
				getVrijVeldZoekFilter().setExclude((List) Collections.emptyList());

			List<VrijVeld> newVrijVelden =
				DataAccessRegistry.getHelper(VrijVeldDataAccessHelper.class).list(
					getVrijVeldZoekFilter());

			for (VrijVeld vv : newVrijVelden)
				newVrijVeldEntiteit(vv);

			return getVrijVeldEntiteiten();
		}
	}

	public VrijVeldEntiteitEditPanel(String id, IModel<T> vrijveldenModel)
	{
		this(id, vrijveldenModel, "Vrije velden");
	}

	/**
	 * @param id
	 */
	public VrijVeldEntiteitEditPanel(String id, IModel<T> vrijveldenModel, String header)
	{
		super(id, vrijveldenModel);

		vrijVeldZoekFilter = new VrijVeldZoekFilter();
		vrijVeldZoekFilter.setActief(true);

		container = new WebMarkupContainer("gegevens");
		container.setOutputMarkupId(true);
		container.add(new Label("header", header));

		list = new ListView<VrijVeldEntiteit>("vrijVeldenList", new ListModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<VrijVeldEntiteit> item)
			{
				VrijVeldEntiteit vrijveld = item.getModelObject();

				item.add(new Label("naam", vrijveld.getVrijVeld().getNaam()));

				// JaNeeCombobox waardeComboBox =
				// new JaNeeCombobox("checkWaarde", new PropertyModel(item.getModel(),
				// "checkWaarde"));
				// waardeComboBox.setNullValid(true);
				CheckBox checkBox =
					new CheckBox("checkWaarde", new PropertyModel<Boolean>(item.getModel(),
						"checkWaarde"));
				checkBox.setVisible(vrijveld.getVrijVeld().getType().equals(
					VrijVeldType.AANKRUISVAK));
				item.add(checkBox);

				DatumField waardeDatumField =
					new DatumField("dateWaarde", new PropertyModel<Date>(item.getModel(),
						"dateWaarde"));
				waardeDatumField.setLabel(new Model<String>(vrijveld.getVrijVeld().getNaam()));
				waardeDatumField.setVisible(vrijveld.getVrijVeld().getType().equals(
					VrijVeldType.DATUM));
				item.add(waardeDatumField);

				VrijVeldKeuzeOptieCombobox waardeKeuzelijstField =
					new VrijVeldKeuzeOptieCombobox("keuze", new PropertyModel<VrijVeldKeuzeOptie>(
						item.getModel(), "keuze"), new PropertyModel<List<VrijVeldKeuzeOptie>>(item
						.getModel(), "vrijVeld.beschikbareKeuzeOpties"));
				waardeKeuzelijstField.setNullValid(true);
				waardeKeuzelijstField.setLabel(new Model<String>(vrijveld.getVrijVeld().getNaam()));
				waardeKeuzelijstField.setVisible(vrijveld.getVrijVeld().getType().equals(
					VrijVeldType.KEUZELIJST));
				item.add(waardeKeuzelijstField);

				TextArea<String> waardeLongTextArea =
					new TextArea<String>("longTextWaarde", new PropertyModel<String>(item
						.getModel(), "longTextWaarde"));
				waardeLongTextArea.setLabel(new Model<String>(vrijveld.getVrijVeld().getNaam()));
				waardeLongTextArea.setVisible(vrijveld.getVrijVeld().getType().equals(
					VrijVeldType.LANGETEKST));
				item.add(waardeLongTextArea);

				// multi keuzelijst
				DropDownCheckList<VrijVeldKeuzeOptie> waardeMultiKeuzelijstField =
					new DropDownCheckList<VrijVeldKeuzeOptie>("keuzes", new KeuzeOptieSelectie(item
						.getModel()), new PropertyModel<List<VrijVeldKeuzeOptie>>(item.getModel(),
						"vrijVeld.beschikbareKeuzeOpties"), new EntiteitPropertyRenderer("naam"));
				waardeMultiKeuzelijstField.setLabel(new Model<String>(vrijveld.getVrijVeld()
					.getNaam()));
				waardeMultiKeuzelijstField.setVisible(vrijveld.getVrijVeld().getType().equals(
					VrijVeldType.MULTISELECTKEUZELIJST));
				item.add(waardeMultiKeuzelijstField);

				TextField<Long> waardeNumberField =
					new TextField<Long>("numberWaarde", new PropertyModel<Long>(item.getModel(),
						"numberWaarde"));
				waardeNumberField.setLabel(new Model<String>(vrijveld.getVrijVeld().getNaam()));
				waardeNumberField.setVisible(vrijveld.getVrijVeld().getType().equals(
					VrijVeldType.NUMERIEK));
				item.add(waardeNumberField);

				TextField<String> waardeTextField =
					new TextField<String>("textWaarde", new PropertyModel<String>(item.getModel(),
						"textWaarde"));
				waardeTextField.setLabel(new Model<String>(vrijveld.getVrijVeld().getNaam()));
				waardeTextField.setVisible(vrijveld.getVrijVeld().getType().equals(
					VrijVeldType.TEKST));
				item.add(waardeTextField);
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && getVrijVeldable() != null && getAantalVrijeVelden() > 0;
			}
		};
		list.setReuseItems(true);
		container.add(list);

		geenVeldenContainer = new WebMarkupContainer("geenVeldenContainer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && getVrijVeldable() != null
					&& getAantalVrijeVelden() == 0;
			}
		};
		container.add(geenVeldenContainer);

		add(container);
	}

	private int getAantalVrijeVelden()
	{
		return ((List< ? >) list.getModelObject()).size();
	}

	private List<VrijVeldEntiteit> getVrijVeldEntiteiten()
	{
		Asserts.assertNotNull("vrijveldable", getVrijVeldable());

		return getVrijVeldable().getVrijVelden();
	}

	@SuppressWarnings("unchecked")
	private VrijVeldable<VrijVeldEntiteit> getVrijVeldable()
	{
		return (VrijVeldable) getModelObject();
	}

	public List<VrijVeld> getVrijVelden(List<VrijVeldEntiteit> ts)
	{
		List<VrijVeld> vrijVelden = new ArrayList<VrijVeld>();
		if (ts != null)
			for (VrijVeldEntiteit vve : ts)
			{
				vrijVelden.add(vve.getVrijVeld());
			}

		return vrijVelden;
	}

	/**
	 * subclass implementatie voor een nieuwe VrijVeldEntiteit.
	 */
	protected VrijVeldEntiteit newVrijVeldEntiteit(VrijVeld veld)
	{
		Asserts.assertNotNull("vrijveldable", getVrijVeldable());

		VrijVeldEntiteit vrijveld = getVrijVeldable().newVrijVeld();
		vrijveld.setVrijVeld(veld);

		if (getVrijVeldEntiteiten() != null)
			getVrijVeldEntiteiten().add(vrijveld);

		return vrijveld;
	}

	@Override
	public VrijVeldZoekFilter getVrijVeldZoekFilter()
	{
		return vrijVeldZoekFilter;
	}

	@Override
	protected void detachModel()
	{
		super.detachModel();

		ComponentUtil.detachQuietly(getVrijVeldZoekFilter());
	}

	/**
	 * Controleer of er nog {@link VrijVeld}en zijn welke geen waarde hebben, verwijder
	 * deze dan.
	 */
	@Override
	public void updateModel()
	{
		List<VrijVeldEntiteit> vrijeVelden = getVrijVeldEntiteiten();
		for (int i = 0; i < vrijeVelden.size();)
		{
			VrijVeldEntiteit veld = vrijeVelden.get(i);
			if (veld.isIngevuld())
				i++;
			else
				vrijeVelden.remove(i);
		}
	}
}
