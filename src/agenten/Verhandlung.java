package agenten;

import java.io.File;
import java.io.FileNotFoundException;


//SIMULATION!

/*
 * Was ist das "Problem" der nachfolgenden Verhandlung?
 * - Fr�he Stagnation, da die Agenten fr�hzeitig neue Contracte ablehnen
 * - Verhandlung ist nur f�r wenige Agenten geeignet, da mit Anzahl Agenten auch die Stagnationsgefahr w�chst
 *
 * Aufgabe: Entwicklung und Anaylse einer effizienteren Verhandlung. Eine Verhandlung ist effizienter, wenn
 * eine fr�he Stagnation vermieden wird und eine sozial-effiziente Gesamtl�sung gefunden werden kann.
 *
 * Ideen:
 * - Agenten m�ssen auch Verschlechterungen akzeptieren bzw. zustimmen, die einzuhaltende Mindestakzeptanzrate wird vom Mediator vorgegeben
 * - Agenten schlagen alternative Kontrakte vor
 * - Agenten konstruieren gemeinsam einen Kontrakt
 * - In jeder Runde werden mehrere alternative Kontrakte vorgeschlagen
 * - Alternative Konstruktionsmechanismen f�r Kontrakte
 * - Ausgleichszahlungen der Agenten (nur m�glich, wenn beide Agenten eine monetaere Zielfunktion haben
 *
 */


public class Verhandlung {


		public static void main(String[] args) {
			int[] contract, proposal;
			Agent agA, agB;

			int maxRounds, round;
			boolean voteA, voteB;

			try{
				String supplier_file = "data/daten3ASupplier_200.txt";
				String customer_file = "data/daten4BCustomer_200_5.txt";
				agA = new SupplierAgent(new File(supplier_file));
				agB = new CustomerAgent(new File(customer_file));

				maxRounds = 10000000;									//Verhandlungsrunden
				boolean abwechselnd = true;
				int rabatt_a =0;
				int rabatt_b =0;

				//Verhandlung_agentB starten
				for(round=1;round<maxRounds;round++) {					//Mediator

					if(abwechselnd){
						proposal = agB.getContract();
						voteA    = agA.vote(agB.getContract(),rabatt_b);
						voteB	= true;
						agB.setKompromiss(rabatt_b);
						abwechselnd = false;
						if(!voteA){
							rabatt_b = rabatt_b + 1;
						}

					}else{
						proposal = agA.getContract();
						abwechselnd = true;
						voteA = true;
						agA.setKompromiss(rabatt_a);
						voteB    = agB.vote(agA.getContract(),rabatt_a);
						if(!voteB){
							rabatt_a = rabatt_a + 1;
						}
					}


					if(voteA && voteB) {

						System.out.print("F�r File : " + supplier_file + "  \t");
						System.out.print("  Agent A : ");
						agA.printUtility(proposal);

						System.out.print("F�r File : " + customer_file + "  \t");
						System.out.print("   Agent B : ");
						agB.printUtility(proposal);


						break;
					}

				}

			}
			catch(FileNotFoundException e){
				System.out.println(e.getMessage());
			}
		}

		public static void ausgabe(Agent a1, Agent a2, int i, int[] contract){
			System.out.print(i + " -> " );
			a1.printUtility(contract);
			System.out.print("  ");
			a2.printUtility(contract);
			System.out.println();
		}


}