package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.vavo;

import static nl.topicus.cobra.types.personalia.Geslacht.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.HoogsteVooropleiding.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.onderwijs.duo.bron.BRONConstants;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.vavo.InschrijvingsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;

import org.junit.Test;

public class ProefgevallenBveVAVOBatch1Test extends ProefgevallenBveVAVO
{
	@Test
	public void proefgeval1() throws Exception
	{
		createGeval1();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2001"));
		assertThat(melding.getSofinummer(), is("210000004"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910501")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(1));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));
	}

	private void createGeval1() throws Exception
	{
		getDeelnemer2001();

		addChange(verbintenis, "status", null, verbintenis.getStatus());
	}

	@Test
	public void proefgeval2() throws Exception
	{
		createGeval2();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2008"));
		assertThat(melding.getSofinummer(), is("210000090"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910508")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(1));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));
	}

	private void createGeval2() throws Exception
	{
		getDeelnemer2008();

		addChange(verbintenis, "status", null, verbintenis.getStatus());
	}

	@Test
	public void proefgeval3() throws Exception
	{
		createGeval3();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2009"));
		assertThat(melding.getSofinummer(), is("210000107"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910509")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(1));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));
	}

	private void createGeval3() throws Exception
	{
		getDeelnemer2009();

		addChange(verbintenis, "status", null, verbintenis.getStatus());
	}

	@Test
	public void proefgeval4() throws Exception
	{
		createGeval4();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2010"));
		assertThat(melding.getSofinummer(), is("210000119"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910510")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(1));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));
	}

	private void createGeval4() throws Exception
	{
		getDeelnemer2010();

		addChange(verbintenis, "status", null, verbintenis.getStatus());
	}

	@Test
	public void proefgeval5() throws Exception
	{
		createGeval5();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2011"));
		assertThat(melding.getSofinummer(), is("210000120"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910511")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(1));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));
	}

	private void createGeval5() throws Exception
	{
		getDeelnemer2011();

		addChange(verbintenis, "status", null, verbintenis.getStatus());
	}

	@Test
	public void proefgeval6() throws Exception
	{
		createGeval6();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2012"));
		assertThat(melding.getSofinummer(), is("210000132"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910512")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(1));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));
	}

	private void createGeval6() throws Exception
	{
		getDeelnemer2012();

		addChange(verbintenis, "status", null, verbintenis.getStatus());
	}

	@Test
	public void proefgeval7() throws Exception
	{
		createGeval7();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2013"));
		assertThat(melding.getSofinummer(), is("210000144"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910513")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(1));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));
	}

	private void createGeval7() throws Exception
	{
		getDeelnemer2013();

		addChange(verbintenis, "status", null, verbintenis.getStatus());

	}

	@Test
	public void bveVAVOBatch1() throws Exception
	{
		createGeval1();
		createGeval2();
		createGeval3();
		createGeval4();
		createGeval5();
		createGeval6();
		createGeval7();

		batchBVE = maakBronVAVOBatch();
		batchBVE.setBatchNummer(1);
		batchBVE.getAanleverpunt().setNummer(1);

		assertThat(batchBVE.getAantalMeldingen(), is(7));
		assertThat(batchBVE.getAantalRecords(), is(15));

		writeBronBatch(batchBVE);
	}
}
