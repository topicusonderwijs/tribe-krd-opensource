package nl.topicus.eduarte.krdparticipatie.web.pages.verzuimloket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.choice.AjaxRadioChoice;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimdag;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimmelding;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class VerzuimdagEditPanel extends TypedPanel<List<IbgVerzuimdag>>
{
	private static final long serialVersionUID = 1L;

	private WebMarkupContainer tableContainer;

	private WebMarkupContainer panelContainer;

	private ListView<IbgVerzuimdag> list;

	private AjaxSubmitLink toevoegenButton;

	private WebMarkupContainer geenVerzuimdagenTekst;

	private WebMarkupContainer verzuimdagToevoegen;

	private static final ArrayList<Boolean> values = new ArrayList<Boolean>(2);

	private IModel<IbgVerzuimmelding> verzuimModel;

	private static final IModel<ArrayList<Boolean>> jaNeeModel =
		new Model<ArrayList<Boolean>>(values);
	static
	{
		values.add(Boolean.TRUE);
		values.add(Boolean.FALSE);
	}

	public VerzuimdagEditPanel(String id, IModel<List<IbgVerzuimdag>> verzuimdagModel,
			IModel<IbgVerzuimmelding> verzuimModel)
	{
		super(id, verzuimdagModel);
		this.verzuimModel = verzuimModel;
		panelContainer = new WebMarkupContainer("panelContainer");
		panelContainer.setOutputMarkupId(true);

		tableContainer = new WebMarkupContainer("gegevens");
		tableContainer.setOutputMarkupId(true);

		geenVerzuimdagenTekst = new WebMarkupContainer("geenVerzuimdagen")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return getContextVerzuimdagen().size() == 0;
			}

		};

		tableContainer.add(geenVerzuimdagenTekst);

		list = new ListView<IbgVerzuimdag>("verzuimdagList", verzuimdagModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<IbgVerzuimdag> item)
			{

				DatumField datumField =
					new DatumField("datum", new PropertyModel<Date>(item.getModel(), "datum"));

				final WebMarkupContainer lesurenContainer =
					new WebMarkupContainer("lesurenContainer");
				lesurenContainer.setOutputMarkupId(true);
				WebMarkupContainer lesurenTable = new WebMarkupContainer("lesuren")
				{

					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible()
					{
						IbgVerzuimdag verzuimdag = item.getModelObject();
						return !verzuimdag.isHeledag();
					}
				};

				addLesurenSpecificatie(lesurenTable, item.getModel());

				RadioChoice<Boolean> urenspecificatie =
					new AjaxRadioChoice<Boolean>("urenspecificatie", new PropertyModel<Boolean>(
						item.getModel(), "heledag"), jaNeeModel, new BooleanChoiceRenderer(
						"Hele dag", "Deel lesuren"))
					{
						private static final long serialVersionUID = 1L;

						@Override
						protected void onUpdate(AjaxRequestTarget target, Object newSelection)
						{
							target.addComponent(lesurenContainer);
						}
					}.setSuffix(" ");

				AjaxLink<Void> verwijderLink = new AjaxLink<Void>("verwijder")
				{

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						List<IbgVerzuimdag> verzuimdagen = getContextVerzuimdagen();
						verzuimdagen.remove(item.getModelObject());
						target.addComponent(tableContainer);
					}

				};

				item.add(datumField);
				item.add(verwijderLink);
				item.add(urenspecificatie);
				lesurenContainer.add(lesurenTable);
				item.add(lesurenContainer);
			}

			private void addLesurenSpecificatie(WebMarkupContainer wmc, IModel<IbgVerzuimdag> model)
			{
				wmc.add(new CheckBox("lesuur1", new PropertyModel<Boolean>(model, "lesuur1")));
				wmc.add(new CheckBox("lesuur2", new PropertyModel<Boolean>(model, "lesuur2")));
				wmc.add(new CheckBox("lesuur3", new PropertyModel<Boolean>(model, "lesuur3")));
				wmc.add(new CheckBox("lesuur4", new PropertyModel<Boolean>(model, "lesuur4")));
				wmc.add(new CheckBox("lesuur5", new PropertyModel<Boolean>(model, "lesuur5")));
				wmc.add(new CheckBox("lesuur6", new PropertyModel<Boolean>(model, "lesuur6")));
				wmc.add(new CheckBox("lesuur7", new PropertyModel<Boolean>(model, "lesuur7")));
				wmc.add(new CheckBox("lesuur8", new PropertyModel<Boolean>(model, "lesuur8")));
				wmc.add(new CheckBox("lesuur9", new PropertyModel<Boolean>(model, "lesuur9")));
				wmc.add(new CheckBox("lesuur10", new PropertyModel<Boolean>(model, "lesuur10")));
				wmc.add(new CheckBox("lesuur11", new PropertyModel<Boolean>(model, "lesuur11")));
				wmc.add(new CheckBox("lesuur12", new PropertyModel<Boolean>(model, "lesuur12")));
			}
		};

		verzuimdagToevoegen = new WebMarkupContainer("verzuimdagToevoegen");
		toevoegenButton = new AjaxSubmitLink("verzuimdagToevoegenButton")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > form)
			{
				List<IbgVerzuimdag> verzuimdagen = getContextVerzuimdagen();
				IbgVerzuimdag verzuimdag = new IbgVerzuimdag();

				// zet datum van nieuwe verzuimdag op de dag na de laatst toegevoegde
				// verzuimdag
				if (!verzuimdagen.isEmpty())
				{
					Date d = verzuimdagen.get(verzuimdagen.size() - 1).getDatum();
					Date nextDay = TimeUtil.getInstance().addDays(d, 1);
					verzuimdag.setDatum(nextDay);
				}

				verzuimdag.setVerzuimmelding(VerzuimdagEditPanel.this.verzuimModel.getObject());
				verzuimdagen.add(verzuimdag);
				geenVerzuimdagenTekst.setVisible(false);
				target.addComponent(tableContainer);

			}
		};

		list.setReuseItems(true);
		tableContainer.add(list);
		verzuimdagToevoegen.add(toevoegenButton);
		tableContainer.add(verzuimdagToevoegen);
		panelContainer.add(tableContainer);
		add(panelContainer);

	}

	public List<IbgVerzuimdag> getContextVerzuimdagen()
	{
		return getModelObject();
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		ComponentUtil.detachQuietly(verzuimModel);
	}
}
