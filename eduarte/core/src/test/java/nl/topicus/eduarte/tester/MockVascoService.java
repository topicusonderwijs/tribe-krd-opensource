package nl.topicus.eduarte.tester;

import java.util.List;

import nl.topicus.vasco.VascoDigipassImport;
import nl.topicus.vasco.VascoService;
import nl.topicus.vasco.VascoStatus;
import nl.topicus.vasco.VascoValidationResponse;

public class MockVascoService implements VascoService
{
	@Override
	public VascoStatus getStatus()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public List<VascoDigipassImport> importDpxFile(byte[] contents, String dpxkey)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public VascoValidationResponse verify(String serial, String dpdata, String password,
			String challenge)
	{
		throw new UnsupportedOperationException();
	}
}
