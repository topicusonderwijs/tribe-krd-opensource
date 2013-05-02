/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.beheer.participatie;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.InGebruikCheckDataAccessHelper;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.form.modifier.ValidateModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.core.principals.beheer.participatie.AfspraaktypesWrite;
import nl.topicus.eduarte.dao.participatie.helpers.AfspraakTypeDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.AfspraakType;
import nl.topicus.eduarte.participatie.web.components.choice.combobox.AfspraakTypeCategoryComboBox;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakTypeZoekFilter;
import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.validation.validator.RangeValidator;

/**
 * @author papegaaij
 */
@PageInfo(title = "Afspraaktype bewerken", menu = {"Beheer > Participatie > Afspraaktypes > [afspraaktype]"})
@InPrincipal(AfspraaktypesWrite.class)
public class EditAfspraakTypePage extends AbstractBeheerPage<Void> implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private AfspraakTypeZoekFilter afspraakTypeZoekFilter;

	private Form<AfspraakType> form;

	public EditAfspraakTypePage(AfspraakType afspraakType,
			AfspraakTypeZoekFilter afspraakTypeZoekFilter)
	{
		super(BeheerMenuItem.AfspraakTypes);
		this.afspraakTypeZoekFilter = afspraakTypeZoekFilter;
		form =
			new Form<AfspraakType>("form", ModelFactory.getCompoundModel(afspraakType,
				new DefaultModelManager(AfspraakType.class)))
			{
				private static final long serialVersionUID = 1L;

				@Override
				@SuppressWarnings("hiding")
				protected void onSubmit()
				{
					AfspraakType afspraakType = getModelObject();
					AfspraakTypeDataAccessHelper helper =
						DataAccessRegistry.getHelper(AfspraakTypeDataAccessHelper.class);
					if (helper.conflicts(afspraakType))
					{
						error("De opgegeven naam conflicteert met een bestaand afspraak type.");
						return;
					}

					afspraakType.saveOrUpdate();
					afspraakType.commit();
					setResponsePage(new AfspraakTypePage(
						EditAfspraakTypePage.this.afspraakTypeZoekFilter));
				}
			};

		AutoFieldSet<AfspraakType> editFields =
			new AutoFieldSet<AfspraakType>("inputFields", form.getModel(), "Afspraaktype gegevens")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onBeforeRender()
				{
					super.onBeforeRender();
					AfspraakTypeCategoryComboBox atcCombo =
						(AfspraakTypeCategoryComboBox) findFieldComponent("category");
					atcCombo.onUpdate(null);
				}
			};
		editFields.setRenderMode(RenderMode.EDIT);
		if (isEditableAfspraakType())
			editFields.addFieldModifier(new OrganisatieEenheidLocatieFieldModifier());
		else
			editFields.addFieldModifier(new EnableModifier(false, "category", "organisatieEenheid",
				"locatie"));

		editFields.addFieldModifier(new ValidateModifier(new RangeValidator<Integer>(0, 100),
			"percentageIIVO"));

		form.add(editFields);

		add(form);

		createComponents();
	}

	private boolean isEditableAfspraakType()
	{
		AfspraakType type = form.getModelObject();
		return !type.isSaved()
			|| !DataAccessRegistry.getHelper(InGebruikCheckDataAccessHelper.class)
				.isInGebruik(type);
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(afspraakTypeZoekFilter);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form));
		panel.addButton(new TerugButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new AfspraakTypePage(afspraakTypeZoekFilter);
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return AfspraakTypePage.class;
			}

		}));

		panel.addButton(new VerwijderButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				AfspraakType type = form.getModelObject();
				type.delete();
				type.commit();
				setResponsePage(new AfspraakTypePage(afspraakTypeZoekFilter));

			}

			@Override
			public boolean isVisible()
			{
				AfspraakType type = form.getModelObject();
				return type.isSaved()
					&& !DataAccessRegistry.getHelper(InGebruikCheckDataAccessHelper.class)
						.isInGebruik(type);
			}
		});
	}
}
