package nl.topicus.eduarte.krd.bron.schakeltest.vo.examenresultaten;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.bron.schakeltest.BronSchakelTestCase;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronExamenresultaatVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronVakGegegevensVOMelding;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.BeoordelingSchoolExamen;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.HogerNiveau;

@SuppressWarnings("hiding")
public abstract class ProefgevallenVOExamens extends BronSchakelTestCase
{

	protected Deelnemer getDeelnemer1()
	{
		add(1, 320000011, null, 19911219, "9722TB", "", 20080801, 2551, Geslacht.Man);
		verbintenis.setLocatie(getLocatie("00DI00"));
		Plaatsing plaatsing = getPlaatsing(4, verbintenis);
		verbintenis.setPlaatsingen(Arrays.asList(plaatsing));
		addExamendeelname(verbintenis, 0);
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		deelname.setExamenjaar(2009);
		return deelnemer;
	}

	protected Deelnemer getDeelnemer2()
	{
		add(2, 330000007, null, 19911219, "9722TB", "", 20080801, 2551, Geslacht.Man);
		verbintenis.setLocatie(getLocatie("00DI00"));
		Plaatsing plaatsing = getPlaatsing(4, verbintenis);
		verbintenis.setPlaatsingen(Arrays.asList(plaatsing));
		addExamendeelname(verbintenis, 0);
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		deelname.setExamenjaar(2009);
		return deelnemer;
	}

	protected Deelnemer getDeelnemer3()
	{
		add(3, 830000008, null, 19891219, "9722TB", "", 20081001, 150, Geslacht.Man);
		verbintenis.setLocatie(getLocatie("00DI00"));
		Plaatsing plaatsing = getPlaatsing(6, verbintenis);
		verbintenis.setPlaatsingen(Arrays.asList(plaatsing));
		addExamendeelname(verbintenis, 0);
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		deelname.setExamenjaar(2009);
		return deelnemer;
	}

	protected Deelnemer getDeelnemer4()
	{
		add(4, 860000011, null, 19891219, "9722TB", "", 20080930, 150, Geslacht.Man);
		verbintenis.setLocatie(getLocatie("00DI00"));
		Plaatsing plaatsing = getPlaatsing(6, verbintenis);
		verbintenis.setPlaatsingen(Arrays.asList(plaatsing));
		addExamendeelname(verbintenis, 0);
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		deelname.setExamenjaar(2009);
		return deelnemer;
	}

	protected Deelnemer getDeelnemer5()
	{
		add(5, 340000004, null, 19891219, "9722TB", "", 20080901, 150, Geslacht.Man);
		verbintenis.setLocatie(getLocatie("00DI00"));
		Plaatsing plaatsing = getPlaatsing(6, verbintenis);
		verbintenis.setPlaatsingen(Arrays.asList(plaatsing));
		addExamendeelname(verbintenis, 0);
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		deelname.setExamenjaar(2009);
		return deelnemer;
	}

	protected Deelnemer getDeelnemer6()
	{
		add(6, 310000002, null, 19891219, "9722TB", "", 20080901, 150, Geslacht.Man);
		verbintenis.setLocatie(getLocatie("00DI00"));
		Plaatsing plaatsing = getPlaatsing(6, verbintenis);
		verbintenis.setPlaatsingen(Arrays.asList(plaatsing));
		addExamendeelname(verbintenis, 0);
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		deelname.setExamenjaar(2009);
		return deelnemer;
	}

	protected Deelnemer getDeelnemer7()
	{
		add(7, 840000005, null, 19911219, "9722TB", "", 20080801, 372, Geslacht.Man);
		verbintenis.setLocatie(getLocatie("00DI00"));
		Plaatsing plaatsing = getPlaatsing(5, verbintenis);
		verbintenis.setPlaatsingen(Arrays.asList(plaatsing));
		addExamendeelname(verbintenis, 0);
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		deelname.setExamenjaar(2009);
		return deelnemer;
	}

	protected Deelnemer getDeelnemer8()
	{
		add(8, 810000003, null, 19911219, "9722TB", "", 20080801, 372, Geslacht.Man);
		verbintenis.setLocatie(getLocatie("00DI00"));
		Plaatsing plaatsing = getPlaatsing(5, verbintenis);
		verbintenis.setPlaatsingen(Arrays.asList(plaatsing));
		addExamendeelname(verbintenis, 0);
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		deelname.setExamenjaar(2009);
		return deelnemer;
	}

	protected Deelnemer getDeelnemer9()
	{
		add(9, 820000012, null, 19911219, "9722TB", "", 20080801, 372, Geslacht.Man);
		verbintenis.setLocatie(getLocatie("00DI00"));
		Plaatsing plaatsing = getPlaatsing(5, verbintenis);
		verbintenis.setPlaatsingen(Arrays.asList(plaatsing));
		addExamendeelname(verbintenis, 0);
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		deelname.setExamenjaar(2009);
		return deelnemer;
	}

	protected Deelnemer getDeelnemer10()
	{
		add(10, 870000007, null, 19911219, "9722TB", "", 20080801, 372, Geslacht.Man);
		verbintenis.setLocatie(getLocatie("00DI00"));
		Plaatsing plaatsing = getPlaatsing(5, verbintenis);
		verbintenis.setPlaatsingen(Arrays.asList(plaatsing));
		addExamendeelname(verbintenis, 0);
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		deelname.setExamenjaar(2009);
		return deelnemer;
	}

	protected Deelnemer getDeelnemer11()
	{
		add(11, 850000002, null, 19911219, "9722TB", "", 20080801, 2911, Geslacht.Man);
		verbintenis.setLocatie(getLocatie("00DI00"));
		Plaatsing plaatsing = getPlaatsing(4, verbintenis);
		verbintenis.setPlaatsingen(Arrays.asList(plaatsing));
		addExamendeelname(verbintenis, 0);
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		deelname.setExamenjaar(2009);
		return deelnemer;
	}

	protected BronVakGegegevensVOMelding createSeGegevensCert(BronExamenresultaatVOMelding melding,
			int examenVak, Boolean diplomaVak, Boolean indicatieWerkstuk,
			BeoordelingSchoolExamen beoordelingSE, Integer cijferSE,
			Boolean verwezenNaarVolgendeTijdvak, Boolean certificaat)
	{
		return createVakGegegevens(melding, examenVak, diplomaVak, indicatieWerkstuk, null,
			beoordelingSE, cijferSE, null, null, null, verwezenNaarVolgendeTijdvak, certificaat,
			null, null, false);
	}

	protected BronVakGegegevensVOMelding createSeGegevens(BronExamenresultaatVOMelding melding,
			int examenVak, Boolean diplomaVak, Boolean indicatieWerkstuk, HogerNiveau hogerNiveau,
			BeoordelingSchoolExamen beoordelingSE, Integer cijferSE, Integer cijferCE1,
			Boolean verwezenNaarVolgendeTijdvak, Boolean certificaat,
			Boolean indicatieCombinatiecijfer, Integer vakCodeHogerNiveau, Boolean berekenEindCijfer)
	{
		return createVakGegegevens(melding, examenVak, diplomaVak, indicatieWerkstuk, hogerNiveau,
			beoordelingSE, cijferSE, cijferCE1, null, null, verwezenNaarVolgendeTijdvak,
			certificaat, indicatieCombinatiecijfer, vakCodeHogerNiveau, berekenEindCijfer);
	}

	protected BronVakGegegevensVOMelding createVakGegegevens(BronExamenresultaatVOMelding melding,
			int examenVak, boolean diplomaVak, Boolean indicatieWerkstuk, HogerNiveau hogerNiveau,
			BeoordelingSchoolExamen beoordelingSE, Integer cijferSE, Integer cijferCE1,
			Integer cijferCE2, Integer cijferCE3, Boolean verwezenNaarVolgendeTijdvak,
			Boolean certificaat, Boolean indicatieCombinatieCijfer, Integer vakCodeHogerNiveau,
			Boolean berekenEindCijfer)
	{
		BronVakGegegevensVOMelding vakMelding = new BronVakGegegevensVOMelding(melding);
		vakMelding.setExamenVak(examenVak);
		vakMelding.setDiplomaVak(diplomaVak);
		vakMelding.setToepassingResultaatOfBeoordelingExamenVak(null);
		vakMelding.setIndicatieWerkstuk(indicatieWerkstuk);
		vakMelding.setHogerNiveau(hogerNiveau);
		vakMelding.setBeoordelingSchoolExamen(beoordelingSE);
		vakMelding.setCijferSchoolExamen(cijferSE);
		vakMelding.setCijferCE1(cijferCE1);
		vakMelding.setCijferCE2(cijferCE2);
		vakMelding.setCijferCE3(cijferCE3);
		if (berekenEindCijfer)
		{
			vakMelding.setEersteEindcijfer(getGemiddelde(cijferSE, cijferCE1));
			vakMelding.setTweedeEindcijfer(getGemiddelde(cijferSE, cijferCE2));
			vakMelding.setDerdeEindcijfer(getGemiddelde(cijferSE, cijferCE3));
			vakMelding.setCijferCijferlijst(getCijferCijferlijst(vakMelding));
		}
		vakMelding.setVerwezenNaarVolgendeTijdvak(verwezenNaarVolgendeTijdvak);
		vakMelding.setCertificaat(certificaat);
		vakMelding.setIndicatieCombinatieCijfer(indicatieCombinatieCijfer);
		vakMelding.setVakCodeHogerNiveau(vakCodeHogerNiveau);
		return vakMelding;

	}

	private Integer getCijferCijferlijst(BronVakGegegevensVOMelding vakMelding)
	{
		if (vakMelding.getEersteEindcijfer() == null)
		{
			Integer cijferSE = vakMelding.getCijferSchoolExamen();
			if (cijferSE != null && cijferSE != 0)
			{
				BigDecimal se = new BigDecimal(cijferSE);
				BigDecimal res = se.divide(BigDecimal.valueOf(10.0), RoundingMode.HALF_UP);
				return res.abs().intValue();
			}
		}
		int hoogsteCijfer = vakMelding.getEersteEindcijfer();
		if (vakMelding.getTweedeEindcijfer() != null
			&& vakMelding.getTweedeEindcijfer() > hoogsteCijfer)
			hoogsteCijfer = vakMelding.getTweedeEindcijfer();
		if (vakMelding.getDerdeEindcijfer() != null
			&& vakMelding.getDerdeEindcijfer() > hoogsteCijfer)
			hoogsteCijfer = vakMelding.getDerdeEindcijfer();
		return hoogsteCijfer;
	}

	private Integer getGemiddelde(Integer cijferSE, Integer cijferCE)
	{
		if (cijferSE == null || cijferCE == null)
			return null;
		BigDecimal se = new BigDecimal(cijferSE);
		BigDecimal ce = new BigDecimal(cijferCE);
		BigDecimal seAndSe = se.add(ce);
		BigDecimal res = seAndSe.divide(BigDecimal.valueOf(20.0), RoundingMode.HALF_UP);
		return res.abs().intValue();
	}
}
