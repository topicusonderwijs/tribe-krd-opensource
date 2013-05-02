package nl.topicus.eduarte.entities;

import java.util.ArrayList;
import java.util.List;

public enum BronMeldingOnderdeel
{
	Sleutel(306),
	Persoon(310),
	GBA,
	Inschrijving(320, 325, 330),
	Periode(321),
	BPV(322),
	Examen(323, 331),
	Resultaat(326),
	Vak(332, 327),
	NT2Onderdelen(328)
	{
		@Override
		public String toString()
		{
			return "NT2 Onderdelen";
		}
	},
	VOInschrijving(110)
	{
		@Override
		public String toString()
		{
			return "VO-inschrijving";
		}
	},
	VOExamen(120)
	{
		@Override
		public String toString()
		{
			return "VO-examen";
		}
	};
	private List<Integer> recordTypes = new ArrayList<Integer>();

	private BronMeldingOnderdeel(Integer... recordTypes)
	{
		for (Integer recordType : recordTypes)
		{
			this.recordTypes.add(recordType);
		}
	}

	public List<Integer> getRecordTypes()
	{
		return recordTypes;
	}
}