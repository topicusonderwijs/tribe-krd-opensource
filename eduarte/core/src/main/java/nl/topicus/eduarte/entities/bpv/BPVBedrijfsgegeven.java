package nl.topicus.eduarte.entities.bpv;

import java.text.DecimalFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.web.components.autoform.HerkomstCodeEnumRadioChoice;
import nl.topicus.eduarte.web.components.choice.KenniscentrumCombobox;
import nl.topicus.onderwijs.duo.bron.Bron;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen ExterneOrganisatie en BPVGegevens.
 * 
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Exportable
@IsViewWhenOnNoise
public class BPVBedrijfsgegeven extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "externeOrganisatie", nullable = false)
	@Index(name = "idx_BPVBedr_externeOrgan")
	private ExterneOrganisatie externeOrganisatie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "brin", nullable = false)
	@Index(name = "idx_BPVBedr_brin")
	@AutoForm(editorClass = KenniscentrumCombobox.class)
	@FieldPersistance(FieldPersistenceMode.SAVE)
	private Brin kenniscentrum;

	@Column(nullable = true, length = 20)
	@AutoForm(htmlClasses = "unit_max")
	private String relatienummer;

	@Bron
	@Column(nullable = true, length = 40)
	@AutoForm(htmlClasses = "unit_max")
	private String codeLeerbedrijf;

	public static enum BPVCodeHerkomst
	{
		Invoer,
		Systeem,
		BRON,
		COLO;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	@AutoForm(editorClass = HerkomstCodeEnumRadioChoice.class)
	private BPVCodeHerkomst herkomstCode;

	public BPVBedrijfsgegeven()
	{
	}

	public BPVBedrijfsgegeven(ExterneOrganisatie externeOrganisatie)
	{
		this.externeOrganisatie = externeOrganisatie;
	}

	@Exportable
	public String getCodeLeerbedrijf()
	{
		return codeLeerbedrijf;
	}

	public void setCodeLeerbedrijf(String codeLeerbedrijf)
	{
		Asserts.assertMaxLength("codeLeerbedrijf", codeLeerbedrijf, 40);
		this.codeLeerbedrijf = codeLeerbedrijf;
	}

	/**
	 * @param externeOrganisatie
	 *            The externeOrganisatie to set.
	 */
	public void setExterneOrganisatie(ExterneOrganisatie externeOrganisatie)
	{
		this.externeOrganisatie = externeOrganisatie;
	}

	/**
	 * @return Returns the externeOrganisatie.
	 */
	@Exportable
	public ExterneOrganisatie getExterneOrganisatie()
	{
		return externeOrganisatie;
	}

	public void setKenniscentrum(Brin kenniscentrum)
	{
		this.kenniscentrum = kenniscentrum;
	}

	@Exportable
	public Brin getKenniscentrum()
	{
		return kenniscentrum;
	}

	public void setRelatienummer(String relatienummer)
	{
		Asserts.assertMaxLength("relatienummer", relatienummer, 15);
		this.relatienummer = relatienummer;
	}

	@Exportable
	public String getRelatienummer()
	{
		return relatienummer;
	}

	@Override
	public String toString()
	{
		return externeOrganisatie.toString();
	}

	public void setHerkomstCode(BPVCodeHerkomst herkomstCode)
	{
		this.herkomstCode = herkomstCode;
	}

	public BPVCodeHerkomst getHerkomstCode()
	{
		return herkomstCode;
	}

	public String genereerSysteemCodeLeerbedrijf()
	{
		if (getKenniscentrum() != null)
		{
			StringBuilder code = new StringBuilder(getKenniscentrum().getCode());
			if (getExterneOrganisatie().getFysiekAdres() != null
				&& getExterneOrganisatie().getFysiekAdres().getAdres() != null)
			{
				Adres adres = getExterneOrganisatie().getFysiekAdres().getAdres();

				if (!StringUtil.isEmpty(adres.getPostcode()))
				{
					code.append(adres.getPostcode());
					if (!StringUtil.isEmpty(adres.getHuisnummer()))
					{
						Integer huisnummer =
							StringUtil.getFirstNumberSequence(adres.getHuisnummer());
						String huisnummerString = new DecimalFormat("00000").format(huisnummer);
						code.append(huisnummerString);
						code.append("01"); // volgnummer
					}
				}

			}
			return code.toString();
		}
		else
		{
			return null;
		}
	}
}