package nl.topicus.eduarte.entities.taxonomie.ho;

import java.util.Date;

import javax.persistence.*;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumLandelijkEntiteit;
import nl.topicus.eduarte.entities.hogeronderwijs.OpleidingsVorm;
import nl.topicus.eduarte.entities.organisatie.Brin;

import org.hibernate.annotations.Index;

/**
 * In de CROHO-taxonomie is (op landelijk niveau!) tevens vastgelegd welke brinnummers de
 * CROHO-opleiding aanbieden. Daar zijn tevens nog enkele extra properties ingesteld,
 * zoals studielast, aanvullende eisen, en begin- en einddata. De aanwezigheid van een
 * CrohoOpleidingAanbod-entiteit betekent niet automatisch dat de instelling over
 * opleidingen van deze CROHO-opleiding beschikt. De aanwezigheid van deze entiteit is wel
 * een voorwaarde voor het aanmaken van die opleiding voor die instelling.
 * 
 * @author hop
 */
@Entity()
@Table(name = "CrohoOpleidingAanbod", uniqueConstraints = @UniqueConstraint(columnNames = {
	"crohoOpleiding", "brin", "opleidingsvorm", "begindatum"}))
public class CrohoOpleidingAanbod extends BeginEinddatumLandelijkEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "crohoOpleiding", nullable = false)
	@Index(name = "idx_CrohoOplAanbod_opleiding")
	private CrohoOpleiding crohoOpleiding;

	@Enumerated(EnumType.STRING)
	@Column(length = 30, nullable = false)
	private CrohoOnderdeel onderdeel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "brin", nullable = false)
	@Index(name = "idx_CrohoOplAanbod_brin")
	private Brin brin;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OpleidingsVorm opleidingsvorm;

	private Date datumEindeInstroom;

	@Column(nullable = false)
	private Integer studielast;

	@AutoForm(label = "Studielast VT")
	@Column(nullable = false)
	private Integer studielastVT;

	@Column(nullable = false)
	private Boolean propedeutischExamen;

	@Column(nullable = false)
	private Boolean werkzaamheden;

	public static enum CrohoBekostiging
	{
		Bekostigd,
		Aangewezen,
		OpenBestel,
		Overig;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CrohoBekostiging bekostiging;

	@AutoForm(label = "Deficiëntie")
	private Boolean deficientie;

	private Boolean beroepsvereisten;

	@Temporal(value = TemporalType.DATE)
	private Date datumAccreditatiebesluit;

	@Temporal(value = TemporalType.DATE)
	private Date datumUitstelTotAccreditatie;

	@Temporal(value = TemporalType.DATE)
	private Date datumEindeInstroomAccreditatie;

	@Temporal(value = TemporalType.DATE)
	private Date datumVervallenAccreditatie;

	public static enum CrohoSoortAanmelding
	{
		Centraal,
		Instelling;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CrohoSoortAanmelding soortAanmelding;

	public static enum CrohoSoortFixus
	{
		Studiefixus,
		Instellingsfixus
	}

	@Enumerated(EnumType.STRING)
	private CrohoSoortFixus soortFixus;

	@Column(nullable = false)
	private Boolean decentraleSelectie;

	private Date vervaldatumDecentraleSelectie;

	@Column(nullable = false)
	private Boolean aanvullendeEisen;

	public void setCrohoOpleiding(CrohoOpleiding crohoOpleiding)
	{
		this.crohoOpleiding = crohoOpleiding;
	}

	public CrohoOpleiding getCrohoOpleiding()
	{
		return crohoOpleiding;
	}

	public void setBrin(Brin brin)
	{
		this.brin = brin;
	}

	public Brin getBrin()
	{
		return brin;
	}

	public void setOpleidingsvorm(OpleidingsVorm opleidingsvorm)
	{
		this.opleidingsvorm = opleidingsvorm;
	}

	public OpleidingsVorm getOpleidingsvorm()
	{
		return opleidingsvorm;
	}

	public Date getDatumEindeInstroom()
	{
		return datumEindeInstroom;
	}

	public void setDatumEindeInstroom(Date datumEindeInstroom)
	{
		this.datumEindeInstroom = datumEindeInstroom;
	}

	public Integer getStudielast()
	{
		return studielast;
	}

	public void setStudielast(Integer studielast)
	{
		this.studielast = studielast;
	}

	public Integer getStudielastVT()
	{
		return studielastVT;
	}

	public void setStudielastVT(Integer studielastVT)
	{
		this.studielastVT = studielastVT;
	}

	public Boolean getPropedeutischExamen()
	{
		return propedeutischExamen;
	}

	public void setPropedeutischExamen(Boolean propedeutischExamen)
	{
		this.propedeutischExamen = propedeutischExamen;
	}

	public Boolean getWerkzaamheden()
	{
		return werkzaamheden;
	}

	public void setWerkzaamheden(Boolean werkzaamheden)
	{
		this.werkzaamheden = werkzaamheden;
	}

	public CrohoBekostiging getBekostiging()
	{
		return bekostiging;
	}

	public void setBekostiging(CrohoBekostiging bekostiging)
	{
		this.bekostiging = bekostiging;
	}

	public Boolean getDeficientie()
	{
		return deficientie;
	}

	public void setDeficientie(Boolean deficientie)
	{
		this.deficientie = deficientie;
	}

	public Boolean getBeroepsvereisten()
	{
		return beroepsvereisten;
	}

	public void setBeroepsvereisten(Boolean beroepsvereisten)
	{
		this.beroepsvereisten = beroepsvereisten;
	}

	public CrohoSoortAanmelding getSoortAanmelding()
	{
		return soortAanmelding;
	}

	public void setSoortAanmelding(CrohoSoortAanmelding soortAanmelding)
	{
		this.soortAanmelding = soortAanmelding;
	}

	public CrohoSoortFixus getSoortFixus()
	{
		return soortFixus;
	}

	public void setSoortFixus(CrohoSoortFixus soortFixus)
	{
		this.soortFixus = soortFixus;
	}

	public Boolean getDecentraleSelectie()
	{
		return decentraleSelectie;
	}

	public void setDecentraleSelectie(Boolean decentraleSelectie)
	{
		this.decentraleSelectie = decentraleSelectie;
	}

	public Date getVervaldatumDecentraleSelectie()
	{
		return vervaldatumDecentraleSelectie;
	}

	public void setVervaldatumDecentraleSelectie(Date vervaldatumDecentraleSelectie)
	{
		this.vervaldatumDecentraleSelectie = vervaldatumDecentraleSelectie;
	}

	public Boolean getAanvullendeEisen()
	{
		return aanvullendeEisen;
	}

	public void setAanvullendeEisen(Boolean aanvullendeEisen)
	{
		this.aanvullendeEisen = aanvullendeEisen;
	}

	public void setDatumAccreditatiebesluit(Date datumAccreditatiebesluit)
	{
		this.datumAccreditatiebesluit = datumAccreditatiebesluit;
	}

	public Date getDatumAccreditatiebesluit()
	{
		return datumAccreditatiebesluit;
	}

	public void setDatumUitstelTotAccreditatie(Date datumUitstelTotAccreditatie)
	{
		this.datumUitstelTotAccreditatie = datumUitstelTotAccreditatie;
	}

	public Date getDatumUitstelTotAccreditatie()
	{
		return datumUitstelTotAccreditatie;
	}

	public void setDatumEindeInstroomAccreditatie(Date datumEindeInstroomAccreditatie)
	{
		this.datumEindeInstroomAccreditatie = datumEindeInstroomAccreditatie;
	}

	public Date getDatumEindeInstroomAccreditatie()
	{
		return datumEindeInstroomAccreditatie;
	}

	public void setDatumVervallenAccreditatie(Date datumVervallenAccreditatie)
	{
		this.datumVervallenAccreditatie = datumVervallenAccreditatie;
	}

	public Date getDatumVervallenAccreditatie()
	{
		return datumVervallenAccreditatie;
	}

	public void setOnderdeel(CrohoOnderdeel onderdeel)
	{
		this.onderdeel = onderdeel;
	}

	public CrohoOnderdeel getOnderdeel()
	{
		return onderdeel;
	}

}
