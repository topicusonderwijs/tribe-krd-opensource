package nl.topicus.eduarte.entities.organisatie;

public enum EntiteitContext
{
	LANDELIJK
	{
		@Override
		public boolean isLandelijk()
		{
			return true;
		}
	},
	INSTELLING
	{
		@Override
		public boolean isLandelijk()
		{
			return false;
		}
	};

	public abstract boolean isLandelijk();
}
