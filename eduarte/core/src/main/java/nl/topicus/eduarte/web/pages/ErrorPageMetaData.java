package nl.topicus.eduarte.web.pages;

import java.io.Serializable;

import org.apache.wicket.MetaDataKey;

public class ErrorPageMetaData implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final MetaDataKey<ErrorPageMetaData> KEY = new MetaDataKey<ErrorPageMetaData>()
	{
		private static final long serialVersionUID = 1L;
	};

	private boolean vorigeRenderingFout = false;

	public ErrorPageMetaData()
	{
	}

	public boolean isVorigeRenderingFout()
	{
		return vorigeRenderingFout;
	}

	public void setVorigeRenderingFout(boolean vorigeRenderingFout)
	{
		this.vorigeRenderingFout = vorigeRenderingFout;
	}
}
