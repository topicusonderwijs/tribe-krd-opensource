package nl.topicus.eduarte.web.components.modalwindow.groep;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormValidator;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.AutoFormValidatorFieldModifier;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.PostProcessModifier;
import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.quicksearch.ISelectListener;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.web.components.quicksearch.groep.GroepSearchEditor;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.model.IModel;

/**
 * Panel voor het toevoegen en bewerken van een {@link Groepsdeelname}.
 * 
 * @author papegaaij
 */
public class GroepsdeelnameModalWindowPanel extends ModalWindowBasePanel<Groepsdeelname>
{
	private static final long serialVersionUID = 1L;

	private Form<Groepsdeelname> form;

	private AutoFieldSet<Groepsdeelname> fieldSet;

	public GroepsdeelnameModalWindowPanel(String id, CobraModalWindow<Groepsdeelname> modalWindow)
	{
		super(id, modalWindow);
		IModel<Groepsdeelname> model = modalWindow.getModel();
		form = new Form<Groepsdeelname>("form", model);
		form.setOutputMarkupId(true);
		add(form);

		fieldSet = new AutoFieldSet<Groepsdeelname>("fieldset", model);
		fieldSet.setPropertyNames("groep", "begindatum", "einddatum");
		fieldSet.setRenderMode(RenderMode.EDIT);
		fieldSet.setSortAccordingToPropertyNames(true);
		fieldSet.setOutputMarkupId(true);
		fieldSet.addFieldModifier(new RequiredModifier(true, "groep"));

		fieldSet.addFieldModifier(new PostProcessModifier("groep")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public <T> void postProcess(AutoFieldSet<T> fieldset, Component field,
					FieldProperties<T, ? , ? > fieldProperties)
			{
				GroepSearchEditor editor = (GroepSearchEditor) field;
				editor.addListener(getGroepListener());
			}
		});
		GroepZoekFilter groepZoekFilter = new GroepZoekFilter();
		groepZoekFilter.setPlaatsingsgroep(false);

		// Sortering voor zoek resultaten
		List<String> orderByList = new ArrayList<String>();
		orderByList.add("code");
		orderByList.add("naam");
		groepZoekFilter.setOrderByList(orderByList);

		fieldSet.addFieldModifier(new ConstructorArgModifier("groep", groepZoekFilter));
		fieldSet.addFieldModifier(new AutoFormValidatorFieldModifier("begindatum")
		{
			private static final long serialVersionUID = 1L;

			@Override
			@AutoForm(validators = @AutoFormValidator(formValidator = BegindatumVoorEinddatumValidator.class, otherProperties = "einddatum"))
			public <T> IModel<AutoFormValidator[]> getValidators(AutoFieldSet<T> pfieldSet,
					Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
			{
				return super.getValidators(pfieldSet, property, fieldProperties);
			}
		});
		form.add(new AbstractFormValidator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public FormComponent< ? >[] getDependentFormComponents()
			{
				FormComponent< ? > groep =
					(FormComponent< ? >) fieldSet.findFieldComponent("groep");
				FormComponent< ? > begindatum =
					(FormComponent< ? >) fieldSet.findFieldComponent("begindatum");
				FormComponent< ? > einddatum =
					(FormComponent< ? >) fieldSet.findFieldComponent("einddatum");
				return new FormComponent[] {groep, begindatum, einddatum};
			}

			@Override
			public void validate(Form< ? > aform)
			{
				Groepsdeelname deelname = getModalWindow().getModelObject();
				Deelnemer deelnemer = deelname.getDeelnemer();
				Groep groep = deelname.getGroep();
				for (Groepsdeelname curCheck : deelnemer.getGroepsdeelnames())
				{
					if (!curCheck.equals(deelname) && groep.equals(curCheck.getGroep()))
					{
						if (curCheck.getBegindatum().before(deelname.getEinddatumNotNull())
							&& deelname.getBegindatum().before(curCheck.getEinddatumNotNull()))
						{
							aform.error("Er is al een groepsdeelname voor deze groep in "
								+ "de gegeven periode.");
						}
					}
				}
				if (deelname.getBegindatum().before(groep.getBegindatum())
					|| (deelname.getEinddatum() != null && deelname.getEinddatum().after(
						groep.getEinddatumNotNull())))
				{
					aform.error("De begin- en/of einddatum vallen niet geheel binnen "
						+ "de geldigheidsperiode van de groep.");
				}
			}
		});
		form.add(fieldSet);

		createComponents();
	}

	private ISelectListener getGroepListener()
	{
		return new ISelectListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onUpdate(AjaxRequestTarget target)
			{
				GroepSearchEditor editor = (GroepSearchEditor) fieldSet.findFieldComponent("groep");
				DatumField begindatum = (DatumField) fieldSet.findFieldComponent("begindatum");
				Groep groep = editor.getModelObject();
				if (groep != null && begindatum.getDatum().before(groep.getGroep().getBegindatum()))
				{
					begindatum.setModelObject(groep.getGroep().getBegindatum());
					target.addComponent(fieldSet.findFieldComponent("begindatum"));
				}

			}
		};
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AjaxOpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > _form)
			{
				Groepsdeelname deelname = form.getModelObject();
				deelname.saveOrUpdate();
				deelname.commit();
				getModalWindow().close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > _form)
			{
				refreshFeedback(target);
			}
		}.setMakeDefault(true));
		panel.addButton(new AjaxAnnulerenButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				getModalWindow().close(target);
			}
		});
		panel.addButton(new AjaxOpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > _form)
			{
				Groepsdeelname deelname = form.getModelObject();
				deelname.saveOrUpdate();
				deelname.commit();

				Groepsdeelname nieuw = new Groepsdeelname();
				nieuw.setDeelnemer(deelname.getDeelnemer());
				form.setModelObject(nieuw);
				target.addComponent(fieldSet);
			}

			@Override
			public String getLabel()
			{
				return "Opslaan en nieuwe toevoegen";
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > _form)
			{
				refreshFeedback(target);
			}
		});
	}
}
