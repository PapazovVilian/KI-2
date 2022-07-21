package agenten;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class SupplierAgent extends Agent{

	private final int[][] costMatrix;
	private int kompromiss =0;
	private int rabatt = 0;
	private final int[] contract;

	public SupplierAgent(File file) throws FileNotFoundException{
//		try{
			Scanner scanner = new Scanner(file);
			int dim         = scanner.nextInt();
			costMatrix      = new int[dim][dim];
			for(int i=0;i<dim;i++){
				for(int j=0;j<dim;j++){
					int x = scanner.nextInt();
					costMatrix[i][j] = x;
				}
			}
			scanner.close();
		this.contract = ProposeContract();

//		}
//		catch(FileNotFoundException e){
//			System.out.println(e.getMessage());
//        }
	}

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



	public boolean vote( int[] proposal,int rabatt_in_euro){

		this.rabatt= rabatt_in_euro;

		int costContract = evaluate(this.getContract());
		int costProposal = evaluate(proposal);

        return (costProposal - rabatt_in_euro) < (costContract);
	}



	public int getContractSize(){
		return costMatrix.length;
	}

	@Override
	public int[] ProposeContract() {

			/*
		Erste Ideen beschreiben.
				z.B Mediator so �ndern das construct Proposal random permutation gibt.

				M�glicher DOKU Text Da das Ziel f�r Agenten A und B die Minimierung der R�st beziehungsweise
		Fertigungskosten ist und die Berechnung von in dem Beispiel 200 Permutation zu rechenintensiv ist blalblabla.
				Eine Methode die von einem Zuf�llig Ort aus anf�ngt und von diesem Punkt aus den n�chsten mit den Kleinsten Wert nimmt
		daraus entsteht ein Proposal den man gerne den anderen Agenten pr�sentieren m�chte. (Die Methode liefert nicht den besten Wert aber einen niedrigeren als zuf�llig)

		i= random number im index 0 bis contractSize oder alle durchgehen?
				j = kleinster Wert vom array der noch nicht Besucht wurde
		als Besucht z�hlen alle Zahlen die in der Variable i oder j sind
				Loop
		neues i ist j
		neues j ist kleinster Wert vom array der noch nicht Besucht wurde
		Loop Ende wenn alle Besucht worden sind.


				BesteWerte[] proposal werte mit Reihenfolge von beste f�r den Agenten bis zum schlechtesten

		Alle BesteWerte[] proposals sowohl von Agent A als auch von Agent B durchlaufen lassen und das beste gewinnt dann.

				Danach kann jeder Agent einen Preisnachlass von genau BesteWerte[1] - BesteWerte[0] = preisnachlass f�r Proposal
		BesteWerte[0] geben
		wobei das Array BesteWerte von klein nach viel
		sortiert ist
		 */
		ArrayList<ArrayList<Integer>> alle_ausprobiert = new ArrayList<>();



		for(int b = 0 ; b<getContractSize();b++){
			boolean[] besucht = new boolean[getContractSize()];
			ArrayList<Integer> result = new ArrayList<>();
			int i = b;
			int j;
			result.add(i);
			besucht[i]=true;
			while(!areAllTrue(besucht)){

		        j = getIndexOfMinValue(costMatrix[i],besucht );
				i = j;
				result.add(i);
				besucht[i] = true;
			}
			alle_ausprobiert.add(result);
		}

        //hole von allen ausprobierten den besten wert
		int final_wert = Integer.MAX_VALUE;
		ArrayList<Integer> result = new ArrayList<>();
		for(ArrayList<Integer> reihenfolge : alle_ausprobiert){
			int wert = evaluate(convertIntegers(reihenfolge));
			//System.out.println(wert);
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
            System.out.print(" + " + this.kompromiss + " Zugest�ndnis ");
        }else{
            evaluation -= this.rabatt;
            System.out.print(" - " + this.rabatt+ " Rabatt ");
        }
        System.out.print(" = " + evaluation);
        System.out.println();
	}



	/*
	 * Ab hier private Methoden
	 */

	private int evaluate(int[] contract){
		//WICHTIG: Diese Methode muss private sein!
		//Nur zu Analyse-Zwecken darf diese auf public gesetzt werden

		int result = 0;
		for(int i=0;i<contract.length-1;i++) {
			int zeile  = contract[i];
			int spalte = contract[i+1];
			result    += costMatrix[zeile][spalte];
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