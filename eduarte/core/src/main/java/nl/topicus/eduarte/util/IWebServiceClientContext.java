package nl.topicus.eduarte.util;

import java.net.URL;

import javax.xml.namespace.QName;

public interface IWebServiceClientContext
{
	public String getTestEndpointAddress();

	public void setTestEndpointAddress(String testEndpointAddress);

	public String getTestServiceNameSpaceURI();

	public void setTestServiceNameSpaceURI(String testServiceNameSpaceURI);

	public String getTestServiceName();

	public void setTestServiceName(String testServiceName);

	public String getProdEndpointAddress();

	public void setProdEndpointAddress(String prodEndpointAddress);

	public String getProdServiceNameSpaceURI();

	public void setProdServiceNameSpaceURI(String prodServiceNameSpaceURI);

	public String getProdServiceName();

	public void setProdServiceName(String prodServiceName);

	public URL getEndpointAddress();

	public QName getServiceName();
}
