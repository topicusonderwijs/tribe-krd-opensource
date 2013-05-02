package nl.topicus.eduarte.entities.opleiding;

public enum SoortOnderwijsTax
{
	VWO("3.2.1"),
	HAVO("3.2.2"),
	VMBOTL("3.2.3.1"),
	VMBO("3.2.3.2"),
	VMBOKBL("3.2.4"),
	VMBOBBL("3.2.5");

	private String taxCode;

	private SoortOnderwijsTax(String taxCode)
	{
		this.taxCode = taxCode;
	}

	public String getTaxCode()
	{
		return taxCode;
	}
}