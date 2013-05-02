package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * @author vandenbrink
 */
@Entity
@Table(appliesTo = "Taalkeuze")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Taalkeuze extends InstellingEntiteit
{

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false)
	@JoinColumn(name = "taal", nullable = false)
	@Index(name = "idx_taalkeuze_taal")
	private ModerneTaal taal;

	@ManyToOne(optional = false)
	@JoinColumn(name = "verbintenis", nullable = false)
	@Index(name = "idx_taalkeuze_verbintenis")
	private Verbintenis verbintenis;

	public ModerneTaal getTaal()
	{
		return taal;
	}

	public void setTaal(ModerneTaal taal)
	{
		this.taal = taal;
	}

	public Verbintenis getVerbintenis()
	{
		return verbintenis;
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = verbintenis;
	}

}
