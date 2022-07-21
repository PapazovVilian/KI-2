package agenten;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CustomerAgent extends Agent{

	private final int[][] timeMatrix;
	private int[][] delayMatrix;//wird anhand der timeMatrix berechnet
	private final int[] contract;

	private int rabatt = 0;
	private int kompromiss = 0;

	public static int getIndexOfMinValue(int[] array,boolean[] besucht) {
		int minValue =  Integer.MAX_VALUE;
		int index_of_smallest_value = 0;
		for (int i = 0; i < array.length; i++) {
			if ((array[i] < minValue) && !besucht[i] && (array[i] != 0)) {
				minValue = array[i];
				index_of_smallest_value = i;
			}
		}
		return index_of_smallest_value;
	}

	public static boolean areAllTrue(boolean[] array)
	{
		for(boolean b : array) if(!b) return false;
		return true;
	}

	public static int[] convertIntegers(List<Integer> integers)
	{
		int[] ret = new int[integers.size()];
		for (int i=0; i < ret.length; i++)
		{
			ret[i] = integers.get(i);
		}
		return ret;
	}

	public CustomerAgent(File file) throws FileNotFoundException{
	//	try {
			Scanner scanner = new Scanner(file);
			int jobs = scanner.nextInt();
			int machines = scanner.nextInt();
			timeMatrix = new int[jobs][machines];
			for (int i = 0; i < timeMatrix.length; i++) {
				for (int j = 0; j < timeMatrix[i].length; j++) {
					int x = scanner.nextInt();
					timeMatrix[i][j] = x;
				}
			}
			calculateDelay(timeMatrix.length);
			
			scanner.close();
			this.contract = ProposeContract();
//		} catch (FileNotFoundException e) {
//			System.out.println(e.getMessage());
//		}
	}
	public boolean vote(int[] proposal,int rabatt_in_euro) {
		this.rabatt= rabatt_in_euro;


		int costContract = evaluate(this.getContract());
		int costProposal = evaluate(proposal);

		return (costProposal - rabatt_in_euro) < (costContract);
	}

	public int getContractSize() {
		return timeMatrix.length;
	}



	@Override
	public int[] ProposeContract() {
		ArrayList<ArrayList<Integer>> alle_ausprobiert = new ArrayList<>();



		for(int b = 0 ; b<getContractSize();b++){
			boolean[] besucht = new boolean[getContractSize()];
			ArrayList<Integer> result = new ArrayList<>();
			int i = b;
			int j;
			result.add(i);
			besucht[i]=true;
			while(!areAllTrue(besucht)){

				j = getIndexOfMinValue(delayMatrix[i],besucht );
				//j = getIndexOfMinValue(costMatrix[i],besucht);
				i = j;
				result.add(i);
				besucht[i] = true;
			}
			alle_ausprobiert.add(result);
		}

		int final_wert = Integer.MAX_VALUE;
		ArrayList<Integer> result = new ArrayList<>();
		for(ArrayList<Integer> reihenfolge : alle_ausprobiert){
			int wert = evaluate(convertIntegers(reihenfolge));

			if(wert< final_wert){
				final_wert = wert;
				result = new ArrayList<>(reihenfolge);
			}
		}




		//Collections.sort(result);
		return convertIntegers(result);
	}


	public void printUtility(int[] contract){
		int evaluation = evaluate(contract);
		System.out.print("Contract: " + evaluation);

		if(Arrays.equals(this.contract, contract)){
			evaluation += this.kompromiss;
			System.out.print(" + " + this.kompromiss +" Zugest�ndnis ");
		}else{
			evaluation -= this.rabatt;
			System.out.print(" - " + this.rabatt + " Rabatt ");
		}
		System.out.print(" = " + evaluation);
		System.out.println();
	}
	
	/*
	 * Ab hier private Methoden
	 */

	private void calculateDelay(int jobNr){
		delayMatrix = new int[jobNr][jobNr];
		for (int h = 0; h < jobNr; h++){
			for (int j = 0; j < jobNr; j++){
				delayMatrix[h][j] = 0;
				if(h!=j){
					int maxWait = 0;
					for(int machine = 0; machine < timeMatrix[0].length; machine++){
						int wait_h_j_machine;						
						
						int time1 = 0;
						for(int k=0; k<=machine; k++){
							time1 += timeMatrix[h][k];
						}
						int time2 = 0;
						for(int k=1; k<=machine; k++){
							time2 += timeMatrix[j][k-1];
						}
						wait_h_j_machine = Math.max(time1-time2, 0);
						if(wait_h_j_machine > maxWait)maxWait = wait_h_j_machine;
					}
					delayMatrix[h][j] = maxWait;
				}
			}
		}		
	}
	
	private int evaluate(int[] contract) {
		// WICHTIG: Diese Methode muss private sein!
		// Nur zu Analyse-Zwecken darf diese auf public gesetzt werden

		int result = 0;

		for (int i = 1; i < contract.length; i++) {//starte bei zweitem Job (also Index 1)
			int jobVor  = contract[i-1];
			int job     = contract[i];
			result     += delayMatrix[jobVor][job];
		}
		
		int lastjob = contract[contract.length-1];
		for (int machine = 0; machine < timeMatrix[0].length; machine++) {
			result  +=  timeMatrix[lastjob][machine];
		}
		

		return result;
	}



	public int[] getContract() {
		return contract;
	}



	public void setKompromiss(int kompromiss) {
		this.kompromiss = kompromiss;
	}

}
