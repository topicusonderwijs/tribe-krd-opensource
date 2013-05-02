package nl.topicus.eduarte.entities.bijlage;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Type van een document dat aan deelnemers en andere entiteiten gekoppeld kunnen worden.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
public class DocumentType extends CodeNaamActiefInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "categorie")
	@Index(name = "idx_doctype_categorie")
	private DocumentCategorie categorie;

	public DocumentType()
	{
	}

	public DocumentCategorie getCategorie()
	{
		return categorie;
	}

	public void setCategorie(DocumentCategorie categorie)
	{
		this.categorie = categorie;
	}

}
