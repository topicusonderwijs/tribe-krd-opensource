package nl.topicus.eduarte.entities.kenmerk;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"categorie", "code",
	"organisatie"})})
public class Kenmerk extends CodeNaamActiefInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@AutoForm(htmlClasses = "unit_max")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categorie", nullable = false)
	@Index(name = "idx_kenmerk_category")
	private KenmerkCategorie categorie;

	public Kenmerk()
	{
	}

	public KenmerkCategorie getCategorie()
	{
		return categorie;
	}

	public void setCategorie(KenmerkCategorie categorie)
	{
		this.categorie = categorie;
	}

	public String getCategorieNaam()
	{
		return getCategorie().getNaam();
	}

	public String getNaamEnCategorieNaam()
	{
		return getCategorie().getNaam() + " - " + getNaam();
	}
}
