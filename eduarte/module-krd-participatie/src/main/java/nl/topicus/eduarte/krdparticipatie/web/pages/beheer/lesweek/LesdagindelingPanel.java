package nl.topicus.eduarte.krdparticipatie.web.pages.beheer.lesweek;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.modelsv2.ReadOnlyListPropertyModel;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.datapanel.CustomColumn;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.datapanel.GroupProperty.GroupPropertyList;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.panels.datapanel.columns.AjaxDeleteColumn;
import nl.topicus.eduarte.entities.participatie.LesdagIndeling;
import nl.topicus.eduarte.entities.participatie.LesuurIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.krdparticipatie.util.enums.LesdagEnum;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.CloseButtonCallback;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class LesdagindelingPanel extends FormComponentPanel<List<LesdagIndeling>>
{
	private EduArteDataPanel<LesdagIndeling> datapanel;

	private ListModelDataProvider<LesdagIndeling> provider;

	private EnumCombobox<LesdagEnum> input;

	private Form<Void> form;

	private IModel<LesdagIndeling> modelWindowModel;

	private IModel<LesweekIndeling> lesweekindelingModel;

	private LesuurindelinEditModalWindow modelWindow;

	private static final long serialVersionUID = 1L;

	private List<CustomColumn<LesdagIndeling>> getColumns()
	{
		List<CustomColumn<LesdagIndeling>> cols = new ArrayList<CustomColumn<LesdagIndeling>>(3);
		cols.add(new CustomPropertyColumn<LesdagIndeling>("Dag", "Dag", "dag"));
		cols.add(new CustomPropertyColumn<LesdagIndeling>("Aantal lesuren", "Aantal lesuren",
			"aantalLesuren"));
		cols.add(new CustomPropertyColumn<LesdagIndeling>("Begintijd", "Begintijd",
			"begintijdFormatted"));
		cols.add(new CustomPropertyColumn<LesdagIndeling>("Eindtijd", "Eindtijd",
			"eindtijdFormatted"));
		cols.add(new AjaxDeleteColumn<LesdagIndeling>("verwijderen", "")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<LesdagIndeling> rowModel,
					AjaxRequestTarget target)
			{
				LesdagindelingPanel.this.getLesdagIndelingen().remove(rowModel.getObject());

				target.addComponent(LesdagindelingPanel.this.datapanel);
			}
		}.setPositioning(Positioning.FIXED_RIGHT));
		return cols;
	}

	private GroupPropertyList<LesdagIndeling> getProperties()
	{
		GroupPropertyList<LesdagIndeling> cols = new GroupPropertyList<LesdagIndeling>(1);

		cols.add(new GroupProperty<LesdagIndeling>("dag", "Dag", "dag"));

		return cols;
	}

	public LesdagindelingPanel(String id, IModel<List<LesdagIndeling>> listModel,
			IModel<LesweekIndeling> lesweekindelingModel)
	{
		super(id, listModel);
		this.lesweekindelingModel = lesweekindelingModel;

		if (getLesdagIndelingen().size() <= 0)
			add5Days();

		form = new Form<Void>("form");
		add(form);

		form
			.add(input =
				new EnumCombobox<LesdagEnum>("input", new Model<LesdagEnum>(null), LesdagEnum
					.values()));
		input.setNullValid(true);

		AjaxSubmitLink copyLink = new AjaxSubmitLink("copyLink", form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target,
					@SuppressWarnings("hiding") Form< ? > form)
			{
				copieerLesuurIndelingen(getLesdagIndelingen().get(0));
				target.addComponent(getWebPage());
			}
		};
		copyLink.add(new Label("copyLinkLabel", "Uren bovenste dag kopi\u00EBren"));
		form.add(copyLink);

		AjaxSubmitLink objectToevoegen = new AjaxSubmitLink("objectToevoegen", form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target,
					@SuppressWarnings("hiding") Form< ? > form)
			{
				boolean hasError = false;
				if (input.getConvertedInput() != null)
				{
					for (LesdagIndeling ldi : getLesdagIndelingen())
					{
						if ((input.getConvertedInput().toString()).equals(ldi.getDag()))
						{
							error("Er bestaad al een dag indeling voor "
								+ input.getConvertedInput());
							hasError = true;
							target.addComponent(getWebPage());
							break;
						}
					}
					if (!hasError)
					{
						addObject(input.getConvertedInput());
						input.setModelObject(null);
						target.addComponent(input);
						target.addComponent(datapanel);
					}
				}
			}
		};
		objectToevoegen.add(new Label("objectToevoegenLabel", "Dag toevoegen"));
		form.add(objectToevoegen);

		provider = new ListModelDataProvider<LesdagIndeling>(getModel());

		CustomDataPanelContentDescription<LesdagIndeling> desc =
			new CustomDataPanelContentDescription<LesdagIndeling>("Lesdagindeling");
		desc.setColumns(getColumns());
		desc.setGroupProperties(getProperties());

		datapanel = new EduArteDataPanel<LesdagIndeling>("dagindelingDatapanel", provider, desc);

		datapanel.setRowFactory(new CustomDataPanelAjaxClickableRowFactory<LesdagIndeling>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<LesdagIndeling> item)
			{
				getModelWindowModel().setObject(item.getModelObject());
				getModelWindow().show(target);
			}
		});
		add(datapanel);

		modelWindowModel = new ReadOnlyListPropertyModel<LesdagIndeling>(listModel);

		modelWindow = new LesuurindelinEditModalWindow("modelWindow", getModelWindowModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void close(AjaxRequestTarget target)
			{
				target.addComponent(datapanel);
				super.close(target);

			}
		};
		modelWindow.setCloseButtonCallback(new CloseButtonCallback()
		{

			private static final long serialVersionUID = 1L;

			@Override
			public boolean onCloseButtonClicked(AjaxRequestTarget target)
			{
				target
					.appendJavascript("alert('Dit venster is alleen te sluiten middels de Opslaan en sluiten-knop. Om wijzigingen te Annuleren kan dit via de Terug-knop op de onderliggende pagina.');");
				return false;
			}
		});
		add(modelWindow);
	}

	/**
	 * voegt lesdag toe aan lesweekindeling
	 */
	protected void addObject(Object newSelection)
	{
		LesdagIndeling lesdag = new LesdagIndeling();
		lesdag.setDag(newSelection.toString());
		lesdag.setLesweekIndeling(getLesweekIndeling());
		getLesdagIndelingen().add(lesdag);
	}

	/**
	 * Kopieer lesdag lesuurIndeling van copie lesdag naar target lesdag
	 */
	public void copieerLesuurIndelingen(LesdagIndeling targetLesdag, LesdagIndeling copieLesdag)
	{
		List<LesdagIndeling> list = getLesweekIndeling().getLesdagIndelingen();
		for (LesdagIndeling lesdag : list)
		{
			if (lesdag.getDag().equals(targetLesdag.getDag()))
			{
				lesdag.setLesuurIndeling(copieLesdag.getLesuurIndeling());
			}
		}
	}

	/**
	 * Kopieer lesdag lesuurIndeling van copie lesdag naar Alle
	 */
	public void copieerLesuurIndelingen(LesdagIndeling copieLesdag)
	{
		List<LesdagIndeling> list = getLesweekIndeling().getLesdagIndelingen();
		for (LesdagIndeling lesdag : list)
		{
			if (!lesdag.getDag().equals(copieLesdag.getDag()))
			{
				List<LesuurIndeling> newLesuurindelingen = new ArrayList<LesuurIndeling>();
				for (LesuurIndeling lesuur : copieLesdag.getLesuurIndeling())
				{
					LesuurIndeling Lesuurindeling = new LesuurIndeling();
					Lesuurindeling.setBeginTijd(lesuur.getBeginTijd());
					Lesuurindeling.setEindTijd(lesuur.getEindTijd());
					Lesuurindeling.setLesuur(lesuur.getLesuur());
					Lesuurindeling.setLesdagIndeling(lesdag);
					newLesuurindelingen.add(Lesuurindeling);
				}
				lesdag.setLesuurIndeling(newLesuurindelingen);
			}
		}
	}

	// private void setLesuren(LesdagIndeling lesdag)
	// {
	// String startUur = "08:00";
	// int aantalUren = 8;
	// int lesDuurInMinuten = 30;
	//
	// List<LesuurIndeling> lesuurLijst = new ArrayList<LesuurIndeling>();
	// Time tijd = TimeUtil.getInstance().parseTimeString(startUur);
	//
	// for (int i = 0; i < aantalUren; i++)
	// {
	// LesuurIndeling Lesuurindeling = new LesuurIndeling();
	// Lesuurindeling.setBeginTijd(TimeUtil.getInstance().timeAddMinutes(tijd,
	// (i * lesDuurInMinuten)));
	// Lesuurindeling.setEindTijd(TimeUtil.getInstance().timeAddMinutes(tijd,
	// (((1 + i) * lesDuurInMinuten))));
	// Lesuurindeling.setLesuur(1 + i);
	// Lesuurindeling.setLesdagIndeling(lesdag);
	// lesuurLijst.add(Lesuurindeling);
	// }
	//
	// lesdag.setLesuurIndeling(lesuurLijst);
	// }

	protected void add5Days()
	{
		addObject(LesdagEnum.MA);
		addObject(LesdagEnum.DI);
		addObject(LesdagEnum.WO);
		addObject(LesdagEnum.DO);
		addObject(LesdagEnum.VR);
	}

	private List<LesdagIndeling> getLesdagIndelingen()
	{
		return getModelObject();
	}

	@Override
	protected void onComponentTag(ComponentTag tag)
	{
		tag.setName("div");
		super.onComponentTag(tag);
	}

	private LesweekIndeling getLesweekIndeling()
	{
		return lesweekindelingModel.getObject();
	}

	@Override
	public void updateModel()
	{
		// Update Model moet niks doen.
	}

	@Override
	public void detachModels()
	{
		super.detachModels();

		ComponentUtil.detachQuietly(lesweekindelingModel);
	}

	public IModel<LesdagIndeling> getModelWindowModel()
	{
		return modelWindowModel;
	}

	public LesuurindelinEditModalWindow getModelWindow()
	{
		return modelWindow;
	}

}
