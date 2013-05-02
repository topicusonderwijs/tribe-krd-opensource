package nl.topicus.eduarte.entities.dbs.gedrag;

public enum Kleur
{
	Rood("red"),
	Oranje("yellow"),
	Groen("green"),
	Wit("white"),
	Grijs("grey");

	private String cssClass;

	private Kleur(String cssClass)
	{
		this.cssClass = cssClass;
	}

	public String getCssClass()
	{
		return cssClass;
	}

}
