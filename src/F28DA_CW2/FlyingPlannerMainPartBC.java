package F28DA_CW2;

import java.io.FileNotFoundException;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * This is the class containing a single main method.
 * 
 * @author Liew Pei Yee
 * @version 1.0
 * @since 10-03-2022
 * 
 */
public class FlyingPlannerMainPartBC {

	public static void main(String[] args) throws FileNotFoundException, FlyingPlannerException {

		// Your implementation should be in FlyingPlanner.java, this class is only to
		// run the user interface of your programme.

		FlyingPlanner fi;
		fi = new FlyingPlanner();
		try {
			fi.populate(new FlightsReader());
			Scanner scan = new Scanner(System.in);
			Set<String> airportList = fi.getAirportList();

			// Implement here your user interface using the methods of Part B. You could
			// optionally expand it to use the methods of Part C.
			String continueStr = null;
			do {

				System.out.println();
				System.out.println("Welcome to Flying Planner Part BC!");
				String errPrint = "Invalid airport. Please enter again!";
				String startPrint = "Please enter the start airport: ";
				System.out.print(startPrint);
				String startAP = scan.nextLine();
				startAP = startAP.toUpperCase();

				while (!airportList.contains(startAP)) {
					System.err.println(errPrint);
					System.out.print(startPrint);
					startAP = scan.nextLine();
					startAP = startAP.toUpperCase();
				}

				String desPrint = "Please enter the destination airport: ";
				System.out.print(desPrint);
				String desAP = scan.nextLine();
				desAP = desAP.toUpperCase();

				while (!airportList.contains(desAP)) {
					System.err.println(errPrint);
					System.out.print(desPrint);
					desAP = scan.nextLine();
					desAP = desAP.toUpperCase();
				}

				String continuePrint = "Do you wish to continue? Enter y for yes: ";
				String excludePrint = "Please enter the airports that need to be excluded (Enter 'n' to stop): ";

				System.out.println();
				System.out.println("Please select your search: ");
				System.out.println("1. Journey Search");
				System.out.println("2. Meet-Up Search");
				int search = scan.nextInt();

				switch (search) {
				case 1:
					System.out.println();
					System.out.println("Please select your journey preference: ");
					System.out.println("1. Least Cost Journey");
					System.out.println("2. Least Cost Journey (excluding some aiports)");
					System.out.println("3. Least Changeovers Journey");
					System.out.println("4. Least Changeovers Journey (excluding some airports)");
					System.out.println("5. Least Journey Time");
					int preference = scan.nextInt();

					switch (preference) {
					case 1:
						Journey jlc = fi.leastCost(startAP, desAP);
						jlc.printJourney();
						System.out.println();
						System.out.println(continuePrint);
						scan.nextLine();
						continueStr = scan.nextLine();
						break;
					case 2:
						List<String> excludingC = new LinkedList<String>();
						System.out.println(excludePrint);
						String exAPC;
						do {
							exAPC = scan.nextLine();
							exAPC = exAPC.toUpperCase();
							excludingC.add(exAPC);
						} while (!exAPC.equalsIgnoreCase("n"));

						Journey jlce = fi.leastCost(startAP, desAP, excludingC);
						jlce.printJourney();
						System.out.println();
						System.out.println(continuePrint);
						continueStr = scan.nextLine();
						break;
					case 3:
						Journey jlh = fi.leastHop(startAP, desAP);
						jlh.printJourney();
						System.out.println();
						System.out.println(continuePrint);
						scan.nextLine();
						continueStr = scan.nextLine();
						break;
					case 4:
						List<String> excludingH = new LinkedList<String>();
						System.out.println(excludePrint);
						String exAPH;
						do {
							exAPH = scan.nextLine();
							exAPH = exAPH.toUpperCase();
							excludingH.add(exAPH);
						} while (!exAPH.equalsIgnoreCase("n"));

						Journey jlhe = fi.leastHop(startAP, desAP, excludingH);
						jlhe.printJourney();
						System.out.println();
						System.out.println(continuePrint);
						continueStr = scan.nextLine();
						break;
					case 5:
						System.out.println("Maximum number of stops (2 - 5): ");
						int maxStop = scan.nextInt();
						Journey jlt = fi.leastTime(startAP, desAP, maxStop);
						jlt.printJourney();
						System.out.println();
						System.out.println(continuePrint);
						scan.nextLine();
						continueStr = scan.nextLine();
						break;
					}
					break;
				case 2:
					System.out.println();
					System.out.println("Please select your search preference: ");
					System.out.println("1. Least Cost");
					System.out.println("2. Least Hops");
					System.out.println("3. Least Journey Time");
					int number = scan.nextInt();
					switch (number) {
					case 1:
						String meetUpLC = fi.leastCostMeetUp(startAP, desAP);
						Journey mulc1 = fi.leastCost(startAP, meetUpLC);
						Journey mulc2 = fi.leastCost(desAP, meetUpLC);
						mulc1.printMeetUp(mulc1, mulc2, "Least Cost", "");
						System.out.println();
						System.out.println(continuePrint);
						scan.nextLine();
						continueStr = scan.nextLine();
						break;
					case 2:
						String meetUpLH = fi.leastHopMeetUp(startAP, desAP);
						Journey mulh1 = fi.leastCost(startAP, meetUpLH);
						Journey mulh2 = fi.leastCost(desAP, meetUpLH);
						mulh1.printMeetUp(mulh1, mulh2, "Least Changeovers", "");
						System.out.println();
						System.out.println(continuePrint);
						scan.nextLine();
						continueStr = scan.nextLine();
						break;
					case 3:
						System.out.println();
						System.out.println("Please enter your start time (24-hour format): ");
						String startTime = scan.next();
						String meetUpLT = fi.leastTimeMeetUp(startAP, desAP, startTime);
						Journey mult1 = fi.leastTime(startAP, meetUpLT, 4);
						Journey mult2 = fi.leastTime(desAP, meetUpLT, 4);
						mult1.printMeetUp(mult1, mult2, "Least Total Journey Time", startTime);
						System.out.println();
						System.out.println(continuePrint);
						scan.nextLine();
						continueStr = scan.nextLine();
						break;
					}
					break;
				}

			} while (continueStr.equalsIgnoreCase("y"));
			System.out.println("Thank you for using!");

		} catch (FileNotFoundException | FlyingPlannerException e) {
			e.printStackTrace();
		}

	}

}
