package nl.topicus.eduarte.krdparticipatie.web.pages.beheer.lesweek;

import java.util.List;
import java.util.Map;

import nl.topicus.cobra.entities.Time;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractAjaxLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.components.text.TijdField;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.eduarte.entities.participatie.LesdagIndeling;
import nl.topicus.eduarte.entities.participatie.LesuurIndeling;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class LesuurIndelingEditPanel extends ModalWindowBasePanel<LesdagIndeling>
{
	private static final long serialVersionUID = 1L;

	private IModel<LesdagIndeling> lesdagindelingModel;

	private Form<Void> form;

	private IModel<List<LesuurIndeling>> listModel;

	private int counter = 1;

	private WebMarkupContainer lesuurTable;

	public LesuurIndelingEditPanel(String id, CobraModalWindow<LesdagIndeling> modalWindow)
	{
		super(id, modalWindow);
		lesdagindelingModel = modalWindow.getModel();
		form = new Form<Void>("form");
		add(form);

		lesuurTable = new WebMarkupContainer("lesuurTable");
		lesuurTable.setOutputMarkupId(true);
		form.add(lesuurTable);

		listModel = new PropertyModel<List<LesuurIndeling>>(lesdagindelingModel, "lesuurIndeling");
		counter = (((lesdagindelingModel.getObject()).getLesuurIndeling()).size() + 1);

		ListView<LesuurIndeling> listview =
			new ListView<LesuurIndeling>("listview", getListModel())
			{

				private static final long serialVersionUID = 1L;

				private TijdField prevEind;

				@Override
				protected void onBeforeRender()
				{
					prevEind = null;
					super.onBeforeRender();
				}

				@Override
				protected void populateItem(ListItem<LesuurIndeling> item)
				{
					final int lesuurVal = (item.getModelObject()).getLesuur();
					TextField<Integer> lesuur =
						new RequiredTextField<Integer>("lesuur", new PropertyModel<Integer>(item
							.getModel(), "lesuur"), Integer.class);
					lesuur.setEnabled(false);

					TijdField beginTijd =
						new TijdField("beginTijd", new PropertyModel<Time>(item.getModel(),
							"beginTijd"));
					beginTijd.setLabel(new Model<String>("Begintijd"));
					beginTijd.setRequired(true);

					TijdField eindTijd =
						new TijdField("eindTijd", new PropertyModel<Time>(item.getModel(),
							"eindTijd"));
					eindTijd.setLabel(new Model<String>("Eindtijd"));
					eindTijd.setRequired(true);

					item.add(lesuur);
					item.add(beginTijd);
					item.add(eindTijd);

					form.add(new BegindatumVoorEinddatumValidator(beginTijd, eindTijd,
						"BegindatumVoorEinddatumValidatorLesuur.error")
					{
						private static final long serialVersionUID = 1L;

						@Override
						protected Map<String, Object> variablesMap()
						{
							Map<String, Object> ret = super.variablesMap();
							ret.put("lesuur", Integer.toString(lesuurVal));
							return ret;
						}
					});
					if (prevEind != null)
					{
						form.add(new BegindatumVoorEinddatumValidator(prevEind, beginTijd,
							"BegindatumVoorEinddatumValidatorVorig.error")
						{
							private static final long serialVersionUID = 1L;

							@Override
							protected Map<String, Object> variablesMap()
							{
								Map<String, Object> ret = super.variablesMap();
								ret.put("lesuur", Integer.toString(lesuurVal));
								return ret;
							}
						});
					}
					prevEind = eindTijd;
				}
			};
		listview.setOutputMarkupPlaceholderTag(true);
		lesuurTable.add(listview);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AbstractAjaxLinkButton(panel, "Lesuur toevoegen", CobraKeyAction.GEEN,
			ButtonAlignment.LEFT)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				addNewIndeling();
				target.addComponent(lesuurTable);
			}
		});
		panel.addButton(new AbstractAjaxLinkButton(panel, "Laatste lesuur verwijderen",
			CobraKeyAction.GEEN, ButtonAlignment.LEFT)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				List<LesuurIndeling> removeList = listModel.getObject();
				if (removeList.size() >= 1)
				{
					removeList.remove(removeList.get(removeList.size() - 1));
					counter--;
				}

				target.addComponent(lesuurTable);
			}
		});
		panel.addButton(new AjaxOpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > _form)
			{
				getModalWindow().close(target);
			}

			@Override
			public String getLabel()
			{
				return "Opslaan en sluiten";
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > _form)
			{
				refreshFeedback(target);
			}
		});
	}

	private void addNewIndeling()
	{
		LesuurIndeling lesuur = new LesuurIndeling();
		lesuur.setLesuur(counter);
		lesuur.setLesdagIndeling(lesdagindelingModel.getObject());
		counter++;

		(listModel.getObject()).add(lesuur);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(getListModel());
		ComponentUtil.detachQuietly(lesdagindelingModel);
	}

	public IModel<List<LesuurIndeling>> getListModel()
	{
		return listModel;
	}
}
