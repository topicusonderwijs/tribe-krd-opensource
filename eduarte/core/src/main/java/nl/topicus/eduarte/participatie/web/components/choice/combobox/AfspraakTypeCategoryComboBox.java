/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.participatie.web.components.choice.combobox;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.eduarte.entities.participatie.AfspraakType;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;
import nl.topicus.eduarte.entities.participatie.enums.DeelnemerPresentieRegistratie;
import nl.topicus.eduarte.entities.participatie.enums.OnderwijsproductGebruik;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.model.IObjectClassAwareModel;

/**
 * Combobox voor de afspraak type category. Alleen bedoeld voor het afspraak type edit
 * scherm.
 * 
 * @author papegaaij
 */
public class AfspraakTypeCategoryComboBox extends EnumCombobox<AfspraakTypeCategory>
{
	private static final long serialVersionUID = 1L;

	private AutoFieldSet<AfspraakType> fieldSet;

	public AfspraakTypeCategoryComboBox(String id, final AutoFieldSet<AfspraakType> fieldSet,
			IObjectClassAwareModel<AfspraakTypeCategory> model)
	{
		super(id, model);
		this.fieldSet = fieldSet;
		add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				AfspraakTypeCategoryComboBox.this.onUpdate(target);
			}
		});
	}

	public void onUpdate(AjaxRequestTarget target)
	{
		AfspraakTypeCategory curCat = getModelObject();
		if (curCat == null)
			return;
		AfspraakType type = fieldSet.getModelObject();
		Component onderwijsproduct = fieldSet.findFieldComponent("onderwijsproductGebruik");
		Component percentageIIVO = fieldSet.findFieldComponent("percentageIIVO");
		Component presentieRegistratie = fieldSet.findFieldComponent("presentieRegistratie");
		Component presentieRegistratieDefault =
			fieldSet.findFieldComponent("presentieRegistratieDefault");
		Component medewerkerOnly = fieldSet.findFieldComponent("medewerkerOnly");
		switch (curCat)
		{
			case PRIVE:
			case EXTERN:
				type.setOnderwijsproductGebruik(OnderwijsproductGebruik.ONGEBRUIKT);
				type.setPercentageIIVO(0);
				type.setPresentieRegistratie(DeelnemerPresentieRegistratie.NIET);
				type.setPresentieRegistratieDefault(false);

				onderwijsproduct.setEnabled(false);
				percentageIIVO.setEnabled(false);
				presentieRegistratie.setEnabled(false);
				presentieRegistratieDefault.setEnabled(false);
				medewerkerOnly.setEnabled(true);
				break;
			case ROOSTER:
				type.setMedewerkerOnly(true);

				onderwijsproduct.setEnabled(true);
				percentageIIVO.setEnabled(true);
				presentieRegistratie.setEnabled(true);
				presentieRegistratieDefault.setEnabled(true);
				medewerkerOnly.setEnabled(false);
				break;
			case INDIVIDUEEL:
			case BESCHERMD:
				onderwijsproduct.setEnabled(true);
				percentageIIVO.setEnabled(true);
				presentieRegistratie.setEnabled(true);
				presentieRegistratieDefault.setEnabled(true);
				medewerkerOnly.setEnabled(true);
				break;
		}
		if (target != null)
		{
			target.addComponent(onderwijsproduct);
			target.addComponent(percentageIIVO);
			target.addComponent(presentieRegistratie);
			target.addComponent(presentieRegistratieDefault);
			target.addComponent(medewerkerOnly);
		}
	}
}
