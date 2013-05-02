package nl.topicus.cobra.util;

import java.util.Iterator;

/**
 * Generates combination indices. Modified version of http://www.merriampark.com/comb.htm
 */
public class CombinationGenerator implements Iterator<int[]>, Iterable<int[]>
{

	private int[] a;

	private int n;

	private int r;

	private long numLeft;

	private long total;

	/**
	 * Creates a new CombinationGenerator.
	 * 
	 * @param n
	 *            The total number of elements.
	 * @param r
	 *            The numbers of elements in the combination.
	 */
	public CombinationGenerator(int n, int r)
	{
		if (r > n)
		{
			throw new IllegalArgumentException("r > n");
		}
		if (n < 1)
		{
			throw new IllegalArgumentException("n < 1");
		}
		this.n = n;
		this.r = r;
		a = new int[r];
		long nFact = getFactorial(n);
		long rFact = getFactorial(r);
		long nminusrFact = getFactorial(n - r);
		total = nFact / (rFact * nminusrFact);
	}

	/**
	 * Reset
	 */
	public void reset()
	{
		for (int i = 0; i < a.length; i++)
		{
			a[i] = i;
		}
		numLeft = total;
	}

	/**
	 * Return number of combinations not yet generated
	 * 
	 * @return The number of combinations not yet generated
	 */
	public long getNumLeft()
	{
		return numLeft;
	}

	/**
	 * Are there more combinations?
	 * 
	 * @return True when more combinations are available.
	 */
	public boolean hasNext()
	{
		return numLeft > 0;
	}

	/**
	 * Return total number of combinations
	 * 
	 * @return The total number of combinations.
	 */
	public long getTotal()
	{
		return total;
	}

	/**
	 * Compute factorial
	 */
	private static long getFactorial(int n)
	{
		long fact = 1;
		for (int i = n; i > 1; i--)
		{
			fact = fact * i;
		}
		return fact;
	}

	/**
	 * Generate next combination (algorithm from Rosen p. 286)
	 * 
	 * @return The next combination
	 */
	public int[] next()
	{

		if (numLeft == total)
		{
			numLeft--;
			return a;
		}

		int i = r - 1;
		while (a[i] == n - r + i)
		{
			i--;
		}
		a[i] = a[i] + 1;
		for (int j = i + 1; j < r; j++)
		{
			a[j] = a[i] + j - i;
		}

		numLeft--;
		return a;

	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("Cannot remove");
	}

	@Override
	public Iterator<int[]> iterator()
	{
		reset();
		return this;
	}
}
