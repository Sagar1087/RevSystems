package OpportunityReport;

import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;

public class opportunityReport 
{
	String oppLocation, oppCounty;
	double oppPopulation,wstGenerationTPD, wstTPD, processingRevenue, materialRevenue, pickupCharge, 
	truckingExpenseUnbaled, householdCost, processingCost, netRecycling, annualRecycling;
	final double wstGenerationRate = 3.6;
	final double wstDiversionRate = 0.3;
	final double prcPricePerTon = 30;
	final double targetYield = 0.85;
	final double mtrlPricePerTon = 157.03; 
	final double peoplePerHouse = 2.4; 
	final double pickupChargePerHH = 1.50;
	final double processingCostPerTon = 53.58; 
	
	////////
	//distBetOpp should be dynamic variable as dist different bet cities
	// Need changes, currently marking final for verifying the calculations.  
	///////
	final double distanceBetweenOpportunity = 32;
	
	final double truckOperatingCost = 5.32; 
	final double truckCapacityUnbaled = 15;
	final double truckCapacityBaled = 21; 
	
	public static void main(String[] args) throws IOException 
	{
		opportunityReport oppObject = new opportunityReport();
		oppObject.identifyLocation();
		//oppObject.wasteGeneration(10000);
		oppObject.processingPricePerTon();
		oppObject.materialRevenue();
		oppObject.pickupCharge();
		oppObject.truckingExpense();
		oppObject.householdCost();
		oppObject.procCost();
		oppObject.netRecycling();
		oppObject.writeToFile();
	}

	public void identifyLocation()
	{ 	
		oppLocation = JOptionPane.showInputDialog("Enter the Opportunity Location");
		System.out.println(oppLocation);
		
		oppCounty = JOptionPane.showInputDialog("Enter the Opportunity County");
		System.out.println(oppCounty);
		
		oppPopulation = Double.parseDouble(JOptionPane.showInputDialog("Enter the Population :"));
		System.out.println(oppPopulation);
	}
	
	public void wasteGeneration(double oppPopulation)
	{
		wstGenerationTPD = oppPopulation * (wstGenerationRate/2000) * wstDiversionRate;
		
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.DOWN);
		wstTPD = Double.parseDouble(df.format(wstGenerationTPD));
		
		JOptionPane.showMessageDialog(null, "Waste Generated in " + oppLocation + " is " + wstTPD + " tons per day");
	}
	
	public void processingPricePerTon()
	{
		processingRevenue = Double.parseDouble(new DecimalFormat("#.##").format(wstGenerationTPD * prcPricePerTon));
		
		JOptionPane.showMessageDialog(null, "Processing Revenue is $" + processingRevenue);
	}
	
	public void materialRevenue()
	{
		materialRevenue = Double.parseDouble(new DecimalFormat("#.##").format(wstGenerationTPD * targetYield * mtrlPricePerTon));
		JOptionPane.showMessageDialog(null, "Material Revenue generated is $"+ materialRevenue);
	}
	
	public void pickupCharge()
	{
		pickupCharge = Double.parseDouble(new DecimalFormat("#.##").format((oppPopulation/peoplePerHouse) * (pickupChargePerHH/30)));
		JOptionPane.showMessageDialog(null, "Pickup charge per House hold $" + pickupCharge + " daily");
	}
	
	public void truckingExpense()
	{
		truckingExpenseUnbaled = Double.parseDouble(new DecimalFormat("#.##").format((2*distanceBetweenOpportunity*truckOperatingCost)/(wstGenerationTPD)*truckCapacityUnbaled));
		JOptionPane.showMessageDialog(null, "Trucking expense to transport unbaled material is $" + truckingExpenseUnbaled);
	}
	
	public void householdCost()
	{
		householdCost = Double.parseDouble(new DecimalFormat("#.##").format((processingRevenue + truckingExpenseUnbaled)/(oppPopulation/peoplePerHouse)*30));
		JOptionPane.showMessageDialog(null, "House hold cost $" + householdCost);
	}
	
	public void procCost()
	{
		processingCost = Double.parseDouble(new DecimalFormat("#.##").format((wstGenerationTPD * processingCostPerTon)));
		JOptionPane.showMessageDialog(null, "Processing Cost $" + processingCost);
	}
	
	public void netRecycling()
	{
		netRecycling = Double.parseDouble(new DecimalFormat("#.##").format(processingRevenue + materialRevenue + pickupCharge - processingCost));
		JOptionPane.showMessageDialog(null, "Net value of Recycling $" + netRecycling);
		
		annualRecycling = Double.parseDouble(new DecimalFormat("#.##").format(netRecycling * 365)); 
		JOptionPane.showMessageDialog(null, "Annualized Recycling revenue $" + annualRecycling);
	}
	
	public void writeToFile() throws IOException
	{
		String fileContent="";
		
		fileContent += "Waste Generated in " + oppLocation + " is " + wstTPD + " tons per day " +"\n" +
						"Processing Revenue is $" + processingRevenue + "\n" +
						"Material Revenue generated is $"+ materialRevenue + "\n" + 
						"Pickup charge per House hold $" + pickupCharge + " daily" + "\n" + 
						"Trucking expense to transport unbaled material is $" + truckingExpenseUnbaled + "\n" + 
						"House hold cost $" + householdCost + "\n" + 
						"Processing Cost $" + processingCost + "\n" + 
						"Net value of Recycling $" + netRecycling + "\n" + 
						"Annualized Recycling revenue $" + annualRecycling;
		
		FileWriter fw = new FileWriter("OpportunityReport.txt");
		fw.write(fileContent);
		fw.close();
	}
	
}
