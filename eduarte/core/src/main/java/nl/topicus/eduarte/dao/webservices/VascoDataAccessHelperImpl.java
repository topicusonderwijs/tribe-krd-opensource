package nl.topicus.eduarte.dao.webservices;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import nl.topicus.eduarte.dao.helpers.vasco.VascoDataAccessHelper;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.security.authentication.vasco.Token;
import nl.topicus.vasco.VascoDigipassImport;
import nl.topicus.vasco.VascoService;
import nl.topicus.vasco.VascoStatus;
import nl.topicus.vasco.VascoValidationResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vasco.utils.Digipass;

@Service
public class VascoDataAccessHelperImpl implements VascoDataAccessHelper
{
	@Autowired
	private VascoService vascoService;

	public VascoDataAccessHelperImpl()
	{
	}

	@Override
	public VascoStatus getStatus()
	{
		try
		{
			return vascoService.getStatus();
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public void setVascoService(VascoService vascoService)
	{
		this.vascoService = vascoService;
	}

	@Override
	public void importFile(String dpxkey, byte[] contents) throws VascoImportException
	{
		try
		{
			List<VascoDigipassImport> records = vascoService.importDpxFile(contents, dpxkey);
			Token token = null;
			for (VascoDigipassImport record : records)
			{
				String applicatie = record.getApplicatie();
				Digipass digipass = record.getDigipass();

				token = new Token(digipass.getSerial(), applicatie, digipass.getStringData());
				token.save();
			}
			if (token != null)
			{
				token.commit();
			}
		}
		catch (java.rmi.ConnectException e)
		{
			throw new VascoImportException("Kon geen verbinding maken met de Vasco server", e);
		}
		catch (Exception e)
		{
			throw new VascoImportException(e.getMessage(), e);
		}
	}

	@Override
	public VascoValidationResponse verifieer(String vascoSerienummer, String digipassData,
			String password)
	{
		try
		{
			return vascoService.verify(vascoSerienummer, digipassData, password, null);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Validatie van digipass faalde", e);
		}
	}

	@Override
	public void delete(Entiteit dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void evict(Entiteit dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <R extends Entiteit> R get(Class<R> class1, Serializable id)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <R extends Entiteit> List<R> list(Class<R> class1, String... orderBy)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <R extends Entiteit> R load(Class<R> class1, Serializable id)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Serializable save(Entiteit dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void saveOrUpdate(Entiteit dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(Entiteit dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <R extends Entiteit> List<R> list(Class<R> class1,
			Collection< ? extends Serializable> ids)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <Y> int touch(Class< ? > clz, String property, Y van, Y totEnMet)
	{
		throw new UnsupportedOperationException("update not supported");
	}
}
