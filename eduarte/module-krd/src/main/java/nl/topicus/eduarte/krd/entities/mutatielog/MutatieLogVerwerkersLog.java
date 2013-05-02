package nl.topicus.eduarte.krd.entities.mutatielog;

import static nl.topicus.cobra.util.StringUtil.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.namespace.QName;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.entities.LandelijkEntiteit;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.krd.dao.helpers.MutatieLogVerwerkersLogDataAccessHelper;
import nl.topicus.eduarte.krd.web.pages.beheer.mutatielog.MutatieLogVerwerkerDropDownChoice;
import nl.topicus.eduarte.util.IWebServiceClientContext;

import org.hibernate.annotations.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "MUTATIELOGVERWERKERSLOG")
public class MutatieLogVerwerkersLog extends LandelijkEntiteit implements IWebServiceClientContext
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(WebServiceMutatieLogVerwerker.class);

	private boolean actief;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORGANISATIE", nullable = false)
	@Index(name = "GENERATED_NAME")
	private Organisatie organisatie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VERWERKER", nullable = false)
	@Index(name = "GENERATED_NAME")
	@AutoForm(editorClass = MutatieLogVerwerkerDropDownChoice.class)
	private MutatieLogVerwerker verwerker;

	@Column(name = "LAATST_VERWERKT_ID", nullable = false)
	private long laatstVerwerktId;

	@Column(name = "LAATST_VERWERKT_OP", nullable = false)
	private Timestamp laatstVerwerktOp;

	@Column(name = "TEST_ENDPOINTADDRESS", length = 1000)
	@AutoForm(htmlClasses = "unit_250")
	private String testEndpointAddress;

	@Column(name = "TEST_NSURI", length = 1000)
	@AutoForm(htmlClasses = "unit_250")
	private String testServiceNameSpaceURI;

	@Column(name = "TEST_NAME", length = 1000)
	@AutoForm(htmlClasses = "unit_250")
	private String testServiceName;

	@Column(name = "PROD_ENDPOINTADDRESS", length = 1000)
	@AutoForm(htmlClasses = "unit_250")
	private String prodEndpointAddress;

	@Column(name = "PROD_NSURI", length = 1000)
	@AutoForm(htmlClasses = "unit_250")
	private String prodServiceNameSpaceURI;

	@Column(name = "PROD_NAME", length = 1000)
	@AutoForm(htmlClasses = "unit_250")
	private String prodServiceName;

	public Organisatie getOrganisatie()
	{
		return organisatie;
	}

	public void setOrganisatie(Organisatie organisatie)
	{
		this.organisatie = organisatie;
	}

	public boolean isProductieGeconfigureerd()
	{
		return areAllNotEmpty(getProdEndpointAddress(), getProdServiceName(),
			getProdServiceNameSpaceURI());
	}

	public boolean isTestGeconfigureerd()
	{
		return areAllNotEmpty(getTestEndpointAddress(), getTestServiceName(),
			getTestServiceNameSpaceURI());
	}

	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	public MutatieLogVerwerker getVerwerker()
	{
		return verwerker;
	}

	public void setVerwerker(MutatieLogVerwerker verwerker)
	{
		this.verwerker = verwerker;
	}

	public long getLaatstVerwerktId()
	{
		return laatstVerwerktId;
	}

	public void setLaatstVerwerktId(long laatstVerwerktId)
	{
		this.laatstVerwerktId = laatstVerwerktId;
	}

	public Timestamp getLaatstVerwerktOp()
	{
		return laatstVerwerktOp;
	}

	public void setLaatstVerwerktOp(Timestamp laatstVerwerktOp)
	{
		this.laatstVerwerktOp = laatstVerwerktOp;
	}

	public String getTabellen()
	{
		return StringUtil.join(getVerwerker().getTabellen(), ", ");
	}

	public int getQueueCount()
	{
		MutatieLogVerwerkersLogDataAccessHelper helper =
			DataAccessRegistry.getHelper(MutatieLogVerwerkersLogDataAccessHelper.class);
		return helper.getQueueCount(this);
	}

	@Override
	public String getTestEndpointAddress()
	{
		return testEndpointAddress;
	}

	@Override
	public void setTestEndpointAddress(String testEndpointAddress)
	{
		this.testEndpointAddress = testEndpointAddress;
	}

	@Override
	public String getTestServiceNameSpaceURI()
	{
		return testServiceNameSpaceURI;
	}

	@Override
	public void setTestServiceNameSpaceURI(String testServiceNameSpaceURI)
	{
		this.testServiceNameSpaceURI = testServiceNameSpaceURI;
	}

	@Override
	public String getTestServiceName()
	{
		return testServiceName;
	}

	@Override
	public void setTestServiceName(String testServiceName)
	{
		this.testServiceName = testServiceName;
	}

	@Override
	public String getProdEndpointAddress()
	{
		return prodEndpointAddress;
	}

	@Override
	public void setProdEndpointAddress(String prodEndpointAddress)
	{
		this.prodEndpointAddress = prodEndpointAddress;
	}

	@Override
	public String getProdServiceNameSpaceURI()
	{
		return prodServiceNameSpaceURI;
	}

	@Override
	public void setProdServiceNameSpaceURI(String prodServiceNameSpaceURI)
	{
		this.prodServiceNameSpaceURI = prodServiceNameSpaceURI;
	}

	@Override
	public String getProdServiceName()
	{
		return prodServiceName;
	}

	@Override
	public void setProdServiceName(String prodServiceName)
	{
		this.prodServiceName = prodServiceName;
	}

	public MutatieLogVerwerkersLog()
	{
	}

	/**
	 * Geeft het Endpoint Address URI terug afhankelijk van de configuratie van de
	 * applicatie: test of productie.
	 */
	@Override
	public URL getEndpointAddress()
	{
		String endpointAddress;
		if (EduArteApp.get().getConfiguration().isProductie())
		{
			endpointAddress = getProdEndpointAddress();
		}
		else
		{
			endpointAddress = getTestEndpointAddress();
		}
		try
		{
			return new URL(endpointAddress);
		}
		catch (MalformedURLException e)
		{
			String verwerkerNaam = "";
			if (verwerker != null)
			{
				verwerkerNaam = verwerker.getNaam();
			}

			log.error(
				"De URL naar de WSDL locatie van webservice {} voor de {} omgeving is ongeldig.",
				new Object[] {verwerkerNaam, "test"}, e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Geeft de service name terug afhankelijk van de configuratie van de applicatie: test
	 * of productie.
	 */
	@Override
	public QName getServiceName()
	{
		String nsUri;
		String tsName;
		if (EduArteApp.get().getConfiguration().isProductie())
		{
			nsUri = getProdServiceNameSpaceURI();
			tsName = getProdServiceName();
		}
		else
		{
			nsUri = getTestServiceNameSpaceURI();
			tsName = getTestServiceName();
		}

		return new QName(nsUri, tsName);
	}
}
