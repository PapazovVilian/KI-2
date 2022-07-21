package agenten;

public abstract class Agent {
	//A
	public abstract boolean vote(int[] proposal,int rabatt_in_euro);
	public abstract void    printUtility(int[] contract);
	public abstract int     getContractSize();
	public abstract int[] ProposeContract();

	public abstract int[] getContract();

	public abstract void setKompromiss(int kompromiss);
}
