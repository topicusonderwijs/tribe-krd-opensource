package nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering;

import javax.persistence.*;

import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronVoVakTerugkoppelMelding;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.BeoordelingSchoolExamen;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.HogerNiveau;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.ToepassingResultaat;
import nl.topicus.onderwijs.duo.bron.vo.batches.examen.VOVakGegevens;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.Examendeel;

import org.hibernate.annotations.Index;

@Entity
@Table(name = "BRON_VO_AANLEVER_VAKGEGEVENS")
public class BronVakGegegevensVOMelding extends InstellingEntiteit implements VOVakGegevens,
		IVakMelding
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private int examenVak;

	@Column(nullable = false)
	private boolean diplomaVak;

	@Column(nullable = true, name = "toepassingResultaatBeoordeling")
	@Enumerated(EnumType.STRING)
	private ToepassingResultaat toepassingResultaatOfBeoordelingExamenVak;

	@Column(nullable = true, name = "indWerkstuk")
	private Boolean indicatieWerkstuk;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private HogerNiveau hogerNiveau;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private BeoordelingSchoolExamen beoordelingSchoolExamen;

	@Column(nullable = true)
	private Integer cijferSchoolExamen;

	@Column(nullable = true)
	private Integer cijferCE1;

	@Column(nullable = true)
	private Integer cijferCE2;

	@Column(nullable = true)
	private Integer cijferCE3;

	@Column(nullable = true)
	private Integer eersteEindcijfer;

	@Column(nullable = true)
	private Integer tweedeEindcijfer;

	@Column(nullable = true)
	private Integer derdeEindcijfer;

	@Column(nullable = true)
	private Integer cijferCijferlijst;

	@Column(nullable = true, name = "indNaarVolgendTijdvak")
	private Boolean verwezenNaarVolgendeTijdvak;

	@Column(nullable = true)
	private Boolean certificaat;

	@Column(nullable = true, name = "indCombinatieCijfer")
	private Boolean indicatieCombinatieCijfer;

	@Column(nullable = true)
	private Integer vakCodeHogerNiveau;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "examenresultaat")
	@Index(name = "idx_BronVakGegeg_examenresult")
	@RestrictedAccess(hasSetter = false)
	private BronExamenresultaatVOMelding examenresultaat;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = true, name = "terugkoppelmelding")
	@Index(name = "idx_BronVakGegeg_terugkoppelm")
	private BronVoVakTerugkoppelMelding terugkoppelmelding;

	protected BronVakGegegevensVOMelding()
	{
	}

	public BronVakGegegevensVOMelding(BronExamenresultaatVOMelding bronExamenresultaatVOMelding)
	{
		examenresultaat = bronExamenresultaatVOMelding;
	}

	public int getExamenVak()
	{
		return examenVak;
	}

	public void setExamenVak(int examenVak)
	{
		this.examenVak = examenVak;
	}

	public boolean isDiplomaVak()
	{
		return diplomaVak;
	}

	public void setDiplomaVak(boolean diplomaVak)
	{
		this.diplomaVak = diplomaVak;
	}

	public ToepassingResultaat getToepassingResultaatOfBeoordelingExamenVak()
	{
		return toepassingResultaatOfBeoordelingExamenVak;
	}

	public void setToepassingResultaatOfBeoordelingExamenVak(
			ToepassingResultaat toepassingResultaatOfBeoordelingExamenVak)
	{
		this.toepassingResultaatOfBeoordelingExamenVak = toepassingResultaatOfBeoordelingExamenVak;
	}

	public Boolean getIndicatieWerkstuk()
	{
		return indicatieWerkstuk;
	}

	public void setIndicatieWerkstuk(Boolean indicatieWerkstuk)
	{
		this.indicatieWerkstuk = indicatieWerkstuk;
	}

	public HogerNiveau getHogerNiveau()
	{
		return hogerNiveau;
	}

	public void setHogerNiveau(HogerNiveau hogerNiveau)
	{
		this.hogerNiveau = hogerNiveau;
	}

	public BeoordelingSchoolExamen getBeoordelingSchoolExamen()
	{
		return beoordelingSchoolExamen;
	}

	public void setBeoordelingSchoolExamen(BeoordelingSchoolExamen beoordelingSchoolExamen)
	{
		this.beoordelingSchoolExamen = beoordelingSchoolExamen;
	}

	public Integer getCijferSchoolExamen()
	{
		return cijferSchoolExamen;
	}

	public void setCijferSchoolExamen(Integer cijferSchoolExamen)
	{
		this.cijferSchoolExamen = cijferSchoolExamen;
	}

	public Integer getCijferCE1()
	{
		return cijferCE1;
	}

	public void setCijferCE1(Integer cijferCE1)
	{
		this.cijferCE1 = cijferCE1;
	}

	public Integer getCijferCE2()
	{
		return cijferCE2;
	}

	public void setCijferCE2(Integer cijferCE2)
	{
		this.cijferCE2 = cijferCE2;
	}

	public Integer getCijferCE3()
	{
		return cijferCE3;
	}

	public void setCijferCE3(Integer cijferCE3)
	{
		this.cijferCE3 = cijferCE3;
	}

	public Integer getEersteEindcijfer()
	{
		return eersteEindcijfer;
	}

	public void setEersteEindcijfer(Integer eersteEindcijfer)
	{
		this.eersteEindcijfer = eersteEindcijfer;
	}

	public Integer getTweedeEindcijfer()
	{
		return tweedeEindcijfer;
	}

	public void setTweedeEindcijfer(Integer tweedeEindcijfer)
	{
		this.tweedeEindcijfer = tweedeEindcijfer;
	}

	public Integer getDerdeEindcijfer()
	{
		return derdeEindcijfer;
	}

	public void setDerdeEindcijfer(Integer derdeEindcijfer)
	{
		this.derdeEindcijfer = derdeEindcijfer;
	}

	public Integer getCijferCijferlijst()
	{
		return cijferCijferlijst;
	}

	public void setCijferCijferlijst(Integer cijferCijferlijst)
	{
		this.cijferCijferlijst = cijferCijferlijst;
	}

	public Boolean getVerwezenNaarVolgendeTijdvak()
	{
		return verwezenNaarVolgendeTijdvak;
	}

	public void setVerwezenNaarVolgendeTijdvak(Boolean verwezenNaarVolgendeTijdvak)
	{
		this.verwezenNaarVolgendeTijdvak = verwezenNaarVolgendeTijdvak;
	}

	public Boolean getCertificaat()
	{
		return certificaat;
	}

	public void setCertificaat(Boolean certificaat)
	{
		this.certificaat = certificaat;
	}

	public Boolean getIndicatieCombinatieCijfer()
	{
		return indicatieCombinatieCijfer;
	}

	public void setIndicatieCombinatieCijfer(Boolean indicatieCombinatieCijfer)
	{
		this.indicatieCombinatieCijfer = indicatieCombinatieCijfer;
	}

	public Integer getVakCodeHogerNiveau()
	{
		return vakCodeHogerNiveau;
	}

	public void setVakCodeHogerNiveau(Integer vakCodeHogerNiveau)
	{
		this.vakCodeHogerNiveau = vakCodeHogerNiveau;
	}

	public BronExamenresultaatVOMelding getExamenresultaat()
	{
		return examenresultaat;
	}

	public void setTerugkoppelmelding(BronVoVakTerugkoppelMelding terugkoppelmelding)
	{
		this.terugkoppelmelding = terugkoppelmelding;
	}

	public BronVoVakTerugkoppelMelding getTerugkoppelmelding()
	{
		return terugkoppelmelding;
	}

	@Override
	public String getExamenvak()
	{
		return String.valueOf(examenVak);
	}

	@Override
	public Boolean getIndicatieDiplomavak()
	{
		return diplomaVak;
	}

	@Override
	public Examendeel getExamendeel()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
