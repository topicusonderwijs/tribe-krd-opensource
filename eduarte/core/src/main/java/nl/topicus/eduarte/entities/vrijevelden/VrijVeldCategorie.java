package nl.topicus.eduarte.entities.vrijevelden;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;

public enum VrijVeldCategorie
{
	DEELNEMERPERSONALIA
	{
		@Override
		public String toString()
		{
			return "Deelnemerpersonalia";
		}
	},
	INTAKE
	{
		@Override
		public String toString()
		{
			return "Intakegesprek";
		}
	},
	VOOROPLEIDING
	{
		@Override
		public String toString()
		{
			return "Vooropleiding";
		}
	},
	VERBINTENIS
	{
		@Override
		public String toString()
		{
			return "Verbintenis";
		}
	},
	PLAATSING
	{
		@Override
		public String toString()
		{
			return "Plaatsing";
		}
	},
	UITSCHRIJVING
	{
		@Override
		public String toString()
		{
			return "Uitschrijving";
		}
	},
	RELATIE
	{
		@Override
		public String toString()
		{
			return "Relatie";
		}
	},
	MEDEWERKERPERSONALIA
	{
		@Override
		public String toString()
		{
			return "Medewerkerpersonalia";
		}
	},
	MEDEWERKERAANSTELLING
	{
		@Override
		public String toString()
		{
			return "Medewerker-aanstelling";
		}
	},
	ONDERWIJSPRODUCT
	{
		@Override
		public String toString()
		{
			return "Onderwijsproduct";
		}
	},
	OPLEIDING
	{
		@Override
		public String toString()
		{
			return "Opleiding";
		}
	},
	EXTERNEORGANISATIE
	{
		@Override
		public String toString()
		{
			return "Externe organisatie";
		}
	},
	GROEP
	{
		@Override
		public String toString()
		{
			return "Groep";
		}
	},
	CONTRACT
	{
		@Override
		public String toString()
		{
			return "Contract";
		}
	},
	BPV_INSCHRIJVING
	{
		@Override
		public String toString()
		{
			return "BPV Inschrijving";
		}
	},
	DA_DEELNEMERPERSONALIA
	{
		@Override
		public String toString()
		{
			return "DA-Deelnemerpersonalia";
		}
	},
	DA_INTAKE
	{
		@Override
		public String toString()
		{
			return "DA-Intakegesprek";
		}
	},
	DA_VOOROPLEIDING
	{
		@Override
		public String toString()
		{
			return "DA-Vooropleiding";
		}
	},
	DA_RELATIE
	{
		@Override
		public String toString()
		{
			return "DA-Relatie";
		}
	};

	public static List<VrijVeldCategorie> getKRDCategorieeeen()
	{
		ArrayList<VrijVeldCategorie> categorieen =
			new ArrayList<VrijVeldCategorie>(Arrays.asList(VrijVeldCategorie.values()));
		if (!EduArteApp.get().isModuleActive(EduArteModuleKey.DIGITAALAANMELDEN))
		{
			categorieen.remove(DA_DEELNEMERPERSONALIA);
			categorieen.remove(DA_INTAKE);
			categorieen.remove(DA_VOOROPLEIDING);
			categorieen.remove(DA_RELATIE);
		}
		return categorieen;

	}
}
