/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.signalering.settings;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.participatie.VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("VerzuimTaakEventAboConf")
public class VerzuimTaakEventAbonnementConfiguration
		extends
		AbstractEventAbonnementConfiguration<List<VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel>>
{
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "abonnementConfiguration")
	@AutoForm(label = "", editorClass = VerzuimTaakSignaalDefinitiesMultiSelectiePanel.class)
	private List<VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel> value =
		new ArrayList<VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel>();

	public VerzuimTaakEventAbonnementConfiguration()
	{
	}

	public VerzuimTaakEventAbonnementConfiguration(
			List<VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel> value)
	{
		setValue(value);
	}

	@Override
	public String toString()
	{
		return "Verzuimtaak signaaldefinities: " + getValue() + "";
	}

	@Override
	public VerzuimTaakEventAbonnementConfiguration copy()
	{
		return new VerzuimTaakEventAbonnementConfiguration(getValue());
	}

	@Override
	public List<VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel> getValue()
	{
		return value;
	}

	@Override
	public void setValue(List<VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel> value)
	{
		this.value = value;

	}

}
