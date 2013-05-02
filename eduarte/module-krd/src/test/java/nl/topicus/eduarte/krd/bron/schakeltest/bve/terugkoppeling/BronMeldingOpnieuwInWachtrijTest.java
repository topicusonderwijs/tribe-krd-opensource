package nl.topicus.eduarte.krd.bron.schakeltest.bve.terugkoppeling;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.bo.ProefgevallenBveBO;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.PeriodegegevensInschrijvingRecord;

import org.junit.Test;

public class BronMeldingOpnieuwInWachtrijTest extends ProefgevallenBveBO
{
	@Test
	public void mantis58416() throws Exception
	{
		getDeelnemer1001();

		verbintenis.setBegindatum(asDate(20100201));
		Bekostigingsperiode periode = new Bekostigingsperiode();
		periode.setBegindatum(asDate(20090801));
		periode.setBekostigd(true);
		periode.setVerbintenis(verbintenis);

		verbintenis.getBekostigingsperiodes().add(periode);
		verbintenis.setBekostigd(Bekostigd.Gedeeltelijk);
		verbintenis.setStatus(VerbintenisStatus.Definitief);

		addChange(periode, "begindatum", null, periode.getBegindatum());

		controller.save();

		BronAanleverMelding aanlevermelding = getEersteMelding();

		BronBveAanleverRecord periodeRecord =
			(BronBveAanleverRecord) aanlevermelding
				.getRecord(PeriodegegevensInschrijvingRecord.class);

		// zet bekostigingsperiode op null op het record om de fout in de conversie te
		// emuleren.
		Property<BronBveAanleverRecord, ? , ? > property =
			ReflectionUtil.findProperty(BronBveAanleverRecord.class, "bekostigingsperiode");
		property.setValue(periodeRecord, null);

		periodeRecord.ververs(false);

		assertThat(periodeRecord.getDatumIngangPeriodegegevensInschrijving(), is(periode
			.getBegindatum()));
		assertThat(periodeRecord.getBekostigingsperiode(), is(periode));
	}
}
