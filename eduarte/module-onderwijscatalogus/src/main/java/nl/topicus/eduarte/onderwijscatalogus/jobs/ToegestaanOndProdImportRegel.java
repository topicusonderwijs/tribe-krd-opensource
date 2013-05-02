package nl.topicus.eduarte.onderwijscatalogus.jobs;

public class ToegestaanOndProdImportRegel
{
	private String opleidingscode;

	private String cohort;

	private String productregelAfkorting;

	private String onderwijsproductCode;

	public ToegestaanOndProdImportRegel(String opleidingscode, String cohort,
			String productregelAfkorting, String onderwijsproductCode)
	{
		this.opleidingscode = opleidingscode;
		this.cohort = cohort;
		this.productregelAfkorting = productregelAfkorting;
		this.onderwijsproductCode = onderwijsproductCode;
	}

	public String getOpleidingscode()
	{
		return opleidingscode;
	}

	public void setOpleidingscode(String opleidingscode)
	{
		this.opleidingscode = opleidingscode;
	}

	public String getCohort()
	{
		return cohort;
	}

	public void setCohort(String cohort)
	{
		this.cohort = cohort;
	}

	public String getProductregelAfkorting()
	{
		return productregelAfkorting;
	}

	public void setProductregelAfkorting(String productregelAfkorting)
	{
		this.productregelAfkorting = productregelAfkorting;
	}

	public String getOnderwijsproductCode()
	{
		return onderwijsproductCode;
	}

	public void setOnderwijsproductCode(String onderwijsproductCode)
	{
		this.onderwijsproductCode = onderwijsproductCode;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ToegestaanOndProdImportRegel)
		{
			ToegestaanOndProdImportRegel object = (ToegestaanOndProdImportRegel) obj;
			if (object.getOpleidingscode().equals(opleidingscode)
				&& object.getCohort().equals(cohort)
				&& object.getProductregelAfkorting().equals(productregelAfkorting)
				&& object.getOnderwijsproductCode().equals(onderwijsproductCode))
				return true;
		}
		return false;
	}
}