package nl.topicus.eduarte.entities.personen;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Burgelijkestaat")
@XmlEnum(String.class)
public enum BurgerlijkeStaat
{
	@XmlEnumValue("gehuwd")
	GEHUWD
	{
		@Override
		public boolean isGehuwd()
		{
			return true;
		}

		@Override
		public String toString()
		{
			return "Gehuwd";
		}

	},
	@XmlEnumValue("geregistreerd partnerschap")
	GEREGISTREERDPARTNERSCHAP
	{
		@Override
		public boolean isGeregistreerdPartnerschap()
		{
			return true;
		}

		@Override
		public String toString()
		{
			return "Geregistreerd partnerschap";
		}
	},
	@XmlEnumValue("gescheiden")
	GESCHEIDEN
	{
		@Override
		public boolean isGescheiden()
		{
			return true;
		}

		@Override
		public String toString()
		{
			return "Gescheiden";

		}
	},
	@XmlEnumValue("ongehuwd")
	ONGEHUWD
	{
		@Override
		public boolean isOngehuwd()
		{
			return true;
		}

		@Override
		public String toString()
		{
			return "Ongehuwd";
		}
	},
	@XmlEnumValue("ontbonden geregistreerd partnerschap")
	ONTBONDENGEREGISTREERDPARTNERSCHAP
	{
		@Override
		public boolean isOntbondenGeregistreerdPartnerschap()
		{
			return true;
		}

		@Override
		public String toString()
		{
			return "Ontbonden geregistreerd partnerschap";
		}
	},
	@XmlEnumValue("weduwe/weduwnaar")
	WEDUWE_WEDUWNAAR
	{
		@Override
		public boolean isWeduweOfWeduwnaar()
		{
			return true;
		}

		@Override
		public String toString()
		{
			return "Weduwe/Weduwnaar";
		}
	};

	public boolean isGehuwd()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isGeregistreerdPartnerschap()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isGescheiden()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isOngehuwd()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isOntbondenGeregistreerdPartnerschap()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isWeduweOfWeduwnaar()
	{
		// TODO Auto-generated method stub
		return false;
	}
}
