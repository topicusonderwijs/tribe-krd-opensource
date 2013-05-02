package nl.topicus.eduarte.entities.bpv;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

@Entity
public class BPVDeelnemerProfiel extends InstellingEntiteit
{

	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bpvDeelnemerProfiel")
	private List<BPVCriteriaBPVDeelnemerProfiel> bpvCriteria =
		new ArrayList<BPVCriteriaBPVDeelnemerProfiel>();

	public void setBpvCriteria(List<BPVCriteriaBPVDeelnemerProfiel> bpvCriteria)
	{
		this.bpvCriteria = bpvCriteria;
	}

	public List<BPVCriteriaBPVDeelnemerProfiel> getBpvCriteria()
	{
		return bpvCriteria;
	}
}
