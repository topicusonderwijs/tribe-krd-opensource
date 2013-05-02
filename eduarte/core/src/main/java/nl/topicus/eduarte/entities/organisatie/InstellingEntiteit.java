package nl.topicus.eduarte.entities.organisatie;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.Entiteit;

import org.hibernate.annotations.Index;

/**
 * @author loite
 */
@MappedSuperclass()
public abstract class InstellingEntiteit extends Entiteit implements IInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	/**
	 * De instelling waarbij deze entiteit hoort.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatie", nullable = false)
	@FieldPersistance(FieldPersistenceMode.SAVE)
	@RestrictedAccess(hasSetter = false)
	@AutoForm(include = false)
	@Index(name = "GENERATED_NAME_org")
	private Instelling organisatie;

	/**
	 * Het id van deze entiteit in het oude pakket (bijvoorbeeld nOISe). Wordt vooral
	 * gebruikt tijdens de conversie.
	 */
	@Column(nullable = true)
	@AutoForm(include = false)
	@Index(name = "GENERATED_NAME_oudId")
	private Long idInOudPakket;

	public InstellingEntiteit()
	{
		organisatie = EduArteContext.get().getInstelling();
	}

	public InstellingEntiteit(Instelling organisatie)
	{
		this.organisatie = organisatie;
	}

	/**
	 * Geeft aan of deze entiteit een landelijke of een instellingsspecifieke entiteit is.
	 * 
	 * @return true als getOrganisatie() null teruggeeft, en anders false.
	 */
	@Override
	public final boolean isLandelijk()
	{
		return false;
	}

	/**
	 * <strong>Let op:</strong> Geeft een INSTELLING terug
	 * 
	 * @return Returns the instelling.
	 */
	@Exportable
	public Instelling getOrganisatie()
	{
		return organisatie;
	}

	/**
	 * @return Returns the idInOudPakket.
	 */
	public Long getIdInOudPakket()
	{
		return idInOudPakket;
	}

	/**
	 * @param idInOudPakket
	 *            The idInOudPakket to set.
	 */
	public void setIdInOudPakket(Long idInOudPakket)
	{
		this.idInOudPakket = idInOudPakket;
	}

	public Brin getInstellingBrincode()
	{
		return getOrganisatie().getBrincode();
	}
}
