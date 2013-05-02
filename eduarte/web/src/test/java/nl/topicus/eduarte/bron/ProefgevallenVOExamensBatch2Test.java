package nl.topicus.eduarte.bron;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronExamenresultaatVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronVakGegegevensVOMelding;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.BeoordelingSchoolExamen;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.BeoordelingWerkstuk;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.VerantwoordelijkeAanlevering;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ProefgevallenVOExamensBatch2Test extends BronSchakelTestCaseDB
{
	@Test
	public void proefgeval12() throws Exception
	{
		createGeval12();
		writeDummyExamenBatchVO();

		BronExamenresultaatVOMelding melding = getBronExamenresultaatVOMelding(0);

		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(830000008));
		assertThat(melding.getLeerlingNummerInstelling(), is("3"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19891219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getILTCode(), is(150));
		assertThat(melding.getExamenJaar(), is(2009));
		assertThat(melding.getTitelOfThemaWerkstuk(), is("Werkstuk 12"));
		assertThat(melding.getBeoordelingWerkstuk(), is(BeoordelingWerkstuk.Voldoende));

		List<BronVakGegegevensVOMelding> vakMeldingen = melding.getVakgegevens();
		assertThat(vakMeldingen.size(), is(14));

		BronVakGegegevensVOMelding vakMelding = getVakMelding(vakMeldingen, 11);
		assertNotNull(vakMelding);
		assertThat(vakMelding.getExamenVak(), is(11));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(60));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = getVakMelding(vakMeldingen, 51);
		assertNotNull(vakMelding);
		assertThat(vakMelding.getExamenVak(), is(51));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(63));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = getVakMelding(vakMeldingen, 52);
		assertNotNull(vakMelding);
		assertThat(vakMelding.getExamenVak(), is(52));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(75));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = getVakMelding(vakMeldingen, 61);
		assertNotNull(vakMelding);
		assertThat(vakMelding.getExamenVak(), is(61));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(90));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = getVakMelding(vakMeldingen, 71);
		assertNotNull(vakMelding);
		assertThat(vakMelding.getExamenVak(), is(71));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(66));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = getVakMelding(vakMeldingen, 301);
		assertNotNull(vakMelding);
		assertThat(vakMelding.getExamenVak(), is(301));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(67));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = getVakMelding(vakMeldingen, 351);
		assertNotNull(vakMelding);
		assertThat(vakMelding.getExamenVak(), is(351));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getBeoordelingSchoolExamen(), is(BeoordelingSchoolExamen.Goed));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = getVakMelding(vakMeldingen, 361);
		assertNotNull(vakMelding);
		assertThat(vakMelding.getExamenVak(), is(361));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(76));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = getVakMelding(vakMeldingen, 401);
		assertNotNull(vakMelding);
		assertThat(vakMelding.getExamenVak(), is(401));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getBeoordelingSchoolExamen(), is(BeoordelingSchoolExamen.Goed));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = getVakMelding(vakMeldingen, 122);
		assertNotNull(vakMelding);
		assertThat(vakMelding.getExamenVak(), is(122));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(100));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = getVakMelding(vakMeldingen, 321);
		assertNotNull(vakMelding);
		assertThat(vakMelding.getExamenVak(), is(321));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(100));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = getVakMelding(vakMeldingen, 152);
		assertNotNull(vakMelding);
		assertThat(vakMelding.getExamenVak(), is(152));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(99));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = getVakMelding(vakMeldingen, 172);
		assertNotNull(vakMelding);
		assertThat(vakMelding.getExamenVak(), is(172));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(52));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = getVakMelding(vakMeldingen, 183);
		assertNotNull(vakMelding);
		assertThat(vakMelding.getExamenVak(), is(183));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(true));
		assertThat(vakMelding.getCijferSchoolExamen(), is(61));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
	}

	private BronVakGegegevensVOMelding getVakMelding(List<BronVakGegegevensVOMelding> vakMeldingen,
			int examenVak)
	{
		for (BronVakGegegevensVOMelding melding : vakMeldingen)
		{
			if (melding.getExamenVak() == examenVak)
				return melding;
		}
		return null;
	}

	private void createGeval12() throws Exception
	{

		DeelnemerDataAccessHelper helper =
			DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class);
		Deelnemer deelnemer = helper.getByBSN(830000008L).get(0);
		Verbintenis verbintenis = deelnemer.getVerbintenissen().get(0);
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		addChange(verbintenis.getExamendeelnames().get(0), "examenstatus", null, deelname
			.getExamenstatus());
	}

	@Test
	public void VOBatch2Test() throws Exception
	{
		createGeval12();

		examenBatchVO = maakBronExamenVOBatch();
		examenBatchVO.setBatchNummer(2);

		assertThat(examenBatchVO.getBRINNummer(), is("00DI"));
		assertThat(examenBatchVO.getAanleverPuntNummer(), is(0));
		assertThat(examenBatchVO.getBatchNummer(), is(2));
		assertThat(examenBatchVO.getBestandSoort(), is(BestandSoort.AANLEVERING));
		assertThat(examenBatchVO.getSoortMelding(), is(SoortMelding.Examenresultaat));

		// FIXME 2 onderstaande regels moet 4 zijn
		assertThat(examenBatchVO.getAantalRecords(), is(1));
		assertThat(examenBatchVO.getAantalToevoegingen(), is(1));

		assertThat(examenBatchVO.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanlevering.Instelling));

		writeBronBatch(examenBatchVO);

	}
}