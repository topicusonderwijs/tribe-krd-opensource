package nl.topicus.eduarte.entities.organisatie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.dao.EncryptionProvider;
import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.security.CobraEncryptonProvider;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.entities.LandelijkEntiteit;
import nl.topicus.eduarte.web.components.choice.InstellingCombobox;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Afname van een servicemodule zoals Competentiemeter of Participatie door een
 * onderwijsinstelling.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"organisatie", "moduleName"}))
public class ModuleAfname extends LandelijkEntiteit implements IActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 100)
	private String moduleName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatie", nullable = false)
	@FieldPersistance(FieldPersistenceMode.SAVE)
	@Index(name = "module_afname_org")
	@AutoForm(label = "Instelling", editorClass = InstellingCombobox.class)
	private Instelling organisatie;

	/**
	 * De naam van de organisatie wordt expliciet opgeslagen omdat dit meegaat in de
	 * checksum. Mocht de naam van de instelling veranderen, hoeft de checksum niet
	 * opnieuw berekend te worden.
	 */
	@Column(nullable = false, length = 100)
	private String organizationName;

	@Column(nullable = false, length = 100)
	@AutoForm(readOnly = true)
	private String checksum;

	@Column(nullable = false)
	private boolean actief = true;

	/**
	 * Default constructor voor Hibernate.
	 */
	public ModuleAfname()
	{
	}

	public ModuleAfname(Instelling instelling, String moduleName,
			EncryptionProvider encryptionProvider)
	{
		setModuleName(moduleName);
		setOrganisatie(instelling);
		setOrganizationName(instelling.getNaam());
		setChecksum(encryptionProvider.encrypt(getChecksumParts()));
	}

	private String getChecksumParts()
	{
		return getOrganizationName() + " - " + getModuleName();
	}

	/**
	 * @return true als de checksum van deze moduleafname geldig is. Er wordt een nieuwe
	 *         CobraEncryptionProvider aangemaakt waarmee de check uitgevoerd wordt. Deze
	 *         methode mag daarom alleen aangeroepen worden als al de encryptiecontext
	 *         gezet is (bijvoorbeeld doordat de webapplicatie al draait).
	 */
	public boolean isChecksumValid()
	{
		return isChecksumValid(new CobraEncryptonProvider());
	}

	/**
	 * @param encryptionProvider
	 * @return true als de checksum van deze moduleafname geldig is.
	 */
	public boolean isChecksumValid(EncryptionProvider encryptionProvider)
	{
		String calc = encryptionProvider.encrypt(getChecksumParts());
		return calc.equals(getChecksum());
	}

	/**
	 * Berekent de checksum van deze moduleafname en set de nieuwe checksum op dit object.
	 * 
	 * @param encryptionProvider
	 */
	public void berekenChecksum(EncryptionProvider encryptionProvider)
	{
		String calc = encryptionProvider.encrypt(getChecksumParts());
		setChecksum(calc);
	}

	@AutoForm(include = true, required = true)
	public EduArteModuleKey getModule()
	{
		return getModuleName() == null ? null : EduArteModuleKey.getModuleKey(getModuleName());
	}

	public void setModule(EduArteModuleKey module)
	{
		setModuleName(module == null ? null : module.getName());
	}

	public String getModuleName()
	{
		return moduleName;
	}

	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}

	public Instelling getOrganisatie()
	{
		return organisatie;
	}

	public void setOrganisatie(Instelling organisatie)
	{
		this.organisatie = organisatie;
	}

	public String getOrganizationName()
	{
		return organizationName;
	}

	public void setOrganizationName(String organizationName)
	{
		this.organizationName = organizationName;
	}

	public String getChecksum()
	{
		return checksum;
	}

	public void setChecksum(String checksum)
	{
		this.checksum = checksum;
	}

	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}
}
