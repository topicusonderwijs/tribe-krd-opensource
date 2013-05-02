package nl.topicus.eduarte.entities.dbs.trajecten.templates;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Persoon;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * @author maatman
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class EigenaarTemplate extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BegeleidingsHandelingTemplateRol rol;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "persoon", nullable = true)
	@ForeignKey(name = "FK_EigeTempl_persoon")
	@Index(name = "idx_EigeTempl_persoon")
	@AutoForm(required = true)
	private Persoon persoon;

	public EigenaarTemplate()
	{
	}

	public BegeleidingsHandelingTemplateRol getRol()
	{
		return rol;
	}

	public void setRol(BegeleidingsHandelingTemplateRol rol)
	{
		this.rol = rol;
	}

	public Persoon getPersoon()
	{
		return persoon;
	}

	public void setPersoon(Persoon persoon)
	{
		this.persoon = persoon;
	}
}
