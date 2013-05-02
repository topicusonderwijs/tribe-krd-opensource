package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.app.security.models.OrganisatieEenhedenLocatiesModel;

public class AlwaysAuthorizedContext extends OrganisatieEenheidLocatieAuthorizationContext
{
	private static final long serialVersionUID = 1L;

	public AlwaysAuthorizedContext()
	{
		super(new OrganisatieEenhedenLocatiesModel(null)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isInstellingClearance()
			{
				return true;
			}
		});
	}
}
