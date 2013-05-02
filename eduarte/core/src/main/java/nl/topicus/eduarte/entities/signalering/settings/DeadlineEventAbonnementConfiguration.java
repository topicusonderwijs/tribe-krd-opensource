/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.signalering.settings;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.cobra.web.components.form.AutoForm;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("DeadlineEventAbonnementConf")
public class DeadlineEventAbonnementConfiguration extends
		AbstractEventAbonnementConfiguration<Integer>
{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(nullable = true)
	@AutoForm(label = "Aantal dagen", description = "Het aantal dagen voordat "
		+ "(bij een positieve waarde) of nadat (bij een negatieve waarde) de deadline verstrijkt.")
	private Integer value = 10;

	public DeadlineEventAbonnementConfiguration()
	{
	}

	public DeadlineEventAbonnementConfiguration(Integer value)
	{
		setValue(value);
	}

	@Override
	public Integer getValue()
	{
		return value;
	}

	@Override
	public void setValue(Integer value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return "Verstrijkt over " + getValue() + " dagen";
	}

	@Override
	public DeadlineEventAbonnementConfiguration copy()
	{
		return new DeadlineEventAbonnementConfiguration(getValue());
	}
}
