package nl.topicus.eduarte.web.components.resultaat;

public class ToetsCodepathVertex
{
	private String codepath;

	public ToetsCodepathVertex(String codepath)
	{
		this.codepath = codepath;
	}

	public String getCodepath()
	{
		return codepath;
	}

	public void setCode(String codepath)
	{
		this.codepath = codepath;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ToetsCodepathVertex)
			return getCodepath().equals(((ToetsCodepathVertex) obj).getCodepath());

		return false;
	}

	@Override
	public String toString()
	{
		return codepath;
	}
}
