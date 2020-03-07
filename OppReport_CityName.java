package OpportunityReport;

import java.sql.*;
import java.text.DecimalFormat;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import java.awt.Window.Type;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class OppReport_CityName extends JFrame {

	private JPanel contentPane;
	private static JTextField txtFieldOpportunityName;
	private JLabel lblEnterStateName;
	private static JTextField txtFieldStateName;
	
	public static opportunityReport oppReportObject = new opportunityReport(); 	//Object of the class opportunityReport

	Connection aConnection = null;
	static Statement aStatement = null;

	static ResultSet rsCountyName;
	static ResultSet rsCitiesWithinCounty;
	static ResultSet rsCityPopulation;

	String JDBC_Driver = "com.mysql.jdbc.Driver";
	String dbURL = "jdbc:sqlserver://FLEXBIZHTAPP\\SQLEXPRESS:1433;databaseName=USPopulation;integratedSecurity=true";
	String userName = "CTLIO/Sbhatt";

	static String qryCountyName;
	static String qryCitiesWithinCounty;
	static String qryCityPopulation;
	static double cityLongitude1; 
	static double cityLatitude1;
	static double cityLongitude2; 
	static double cityLatitude2;
	static double distanceBetCities; 
	static String Counties = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OppReport_CityName frame = new OppReport_CityName();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public OppReport_CityName() {
		setTitle("Revolution Systems");
		setIconImage(Toolkit.getDefaultToolkit().getImage(OppReport_CityName.class
				.getResource("/windowBuilder/Resources/6cfcb4e9556799a6fcbf983aab9fab19-32bits-48.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel lblEnterOpportunityName = new JLabel("Enter Opportunity Name");

		txtFieldOpportunityName = new JTextField();
		txtFieldOpportunityName.setFont(new Font("Calibri", Font.PLAIN, 16));
		txtFieldOpportunityName.setColumns(10);

		JButton btnGenerateReport = new JButton("Generate Report");

		////////////////////////////////////////////////
		//Action Listener Method to generate Report. Method calls when button pressed.
		/////////////////////////////////////////////////
		btnGenerateReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					
					//For Connecting the SQL Database. 
					
					Class.forName(JDBC_Driver);
					aConnection = DriverManager.getConnection(dbURL, userName, "");
					aStatement = aConnection.createStatement();

					///////////////////////////////
					//Calling Methods. 
					///////////////////////////////
					
					mthCountyName();
					mthCitiesWithinCounty();
					//mthCentralCity();
					//mthCitiesMO();
					JOptionPane.showMessageDialog(null, "Report Generated Successfully");

				}

				catch (Exception e) {
					e.printStackTrace();
				}

				finally {
					try {
						aStatement.close();
						aConnection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}

		});

		lblEnterStateName = new JLabel("Enter State Name");

		txtFieldStateName = new JTextField();
		txtFieldStateName.setFont(new Font("Calibri", Font.PLAIN, 16));
		txtFieldStateName.setColumns(10);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addGap(18)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(lblEnterStateName)
								.addComponent(lblEnterOpportunityName, GroupLayout.PREFERRED_SIZE, 145,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(txtFieldStateName)
								.addComponent(txtFieldOpportunityName, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE,
										186, GroupLayout.PREFERRED_SIZE))
						.addGap(39))
				.addGroup(gl_contentPane.createSequentialGroup().addGap(96).addComponent(btnGenerateReport)
						.addContainerGap(201, Short.MAX_VALUE)));
		gl_contentPane
				.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup().addGap(22)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblEnterOpportunityName, GroupLayout.PREFERRED_SIZE, 27,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(txtFieldOpportunityName, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblEnterStateName).addComponent(txtFieldStateName,
												GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
								.addGap(35).addComponent(btnGenerateReport).addContainerGap(107, Short.MAX_VALUE)));
		contentPane.setLayout(gl_contentPane);
	}// End of Constructor

	public static void mthCountyName() throws SQLException {
		qryCountyName = "Select CountyName \r\n" + " from dbo.USPopulation2019\r\n" + "  Where StateName='"
				+ txtFieldStateName.getText() + "' and CityName = '" + txtFieldOpportunityName.getText() + "'";

		rsCountyName = aStatement.executeQuery(qryCountyName);

		while (rsCountyName.next()) {
			
			Counties = rsCountyName.getString("CountyName");
			JOptionPane.showMessageDialog(null, "County name = " + Counties);
		}
	}//End of Method mthCountyName
	
	public static void mthCitiesWithinCounty() throws HeadlessException, SQLException, IOException
	{
		String allCitiesList = null; 
		String Cities = null, state =null;
		double allPopulation=0, cityPopulation = 0;
		
		qryCitiesWithinCounty = "  Select CityName, StateName, population, Longitude, Latitude \r\n" + " from dbo.USPopulation2019\r\n"
				+ "  Where CountyName=( Select CountyName As Counties\r\n"
				+ "  From dbo.USPopulation2019\r\n" + "  Where StateName='" + txtFieldStateName.getText()
				+ "' and CityName= '" + txtFieldOpportunityName.getText()
				+ "') and population is not null and population <> 0 and StateName = '" + txtFieldStateName.getText() + "'\r\n"
				+ "  Order by population DESC";

		rsCitiesWithinCounty = aStatement.executeQuery(qryCitiesWithinCounty);

		////////////////////////
		//For the first (centered) city to identify all the opportunity Values: 
		/////////////////////////
		
		
		if(rsCitiesWithinCounty.next()) {
			cityLatitude1 = rsCitiesWithinCounty.getDouble("Latitude");
			cityLongitude1 = rsCitiesWithinCounty.getDouble("Longitude");
			
			Cities = rsCitiesWithinCounty.getString("CityName");
			cityPopulation = rsCitiesWithinCounty.getDouble("population");
			state = rsCitiesWithinCounty.getString("StateName");
			
			oppReportObject.wasteGeneration(cityPopulation, Cities);
			oppReportObject.processingPricePerTon(Cities);
			oppReportObject.materialRevenue(Cities);
			oppReportObject.pickupCharge(cityPopulation,Cities);
			
			//distanceBetCities = Double.parseDouble(new DecimalFormat("#.##").format(oppReportObject.distance(cityLatitude1, cityLongitude1, cityLatitude2, cityLongitude2)));
			distanceBetCities = 0;
			oppReportObject.truckingExpense(distanceBetCities);
			oppReportObject.householdCost(cityPopulation);
			oppReportObject.procCost();
			oppReportObject.netRecycling();
			
			oppReportObject.writeToFile(cityPopulation, Cities,state,true, Counties,cityLatitude1,cityLongitude1);
	
		}
		
		while (rsCitiesWithinCounty.next()) 
		{
			Cities = rsCitiesWithinCounty.getString("CityName");
			cityPopulation = rsCitiesWithinCounty.getDouble("population");

			cityLatitude2 = rsCitiesWithinCounty.getDouble("Latitude");
			cityLongitude2 = rsCitiesWithinCounty.getDouble("Longitude");
			
			//JOptionPane.showMessageDialog(null, "Population of " + Cities + " is = " + cityPopulation);
			//allCitiesList += Cities + "\n";
			
			oppReportObject.wasteGeneration(cityPopulation, Cities);
			oppReportObject.processingPricePerTon(Cities);
			oppReportObject.materialRevenue(Cities);
			oppReportObject.pickupCharge(cityPopulation,Cities);
			
			distanceBetCities = Double.parseDouble(new DecimalFormat("#.##").format(oppReportObject.distance(cityLatitude1, cityLongitude1, cityLatitude2, cityLongitude2)));
			
			oppReportObject.truckingExpense(distanceBetCities);
			oppReportObject.householdCost(cityPopulation);
			oppReportObject.procCost();
			oppReportObject.netRecycling();
			
			oppReportObject.writeToFile(cityPopulation, Cities, state,false, Counties,cityLatitude2,cityLongitude2);
		}
		
		//JOptionPane.showMessageDialog(null, "List of cities within the county : " + allCitiesList);

	}//End of method mthCitiesWithinCounty
	
	public static void mthCitiesMO() throws HeadlessException, SQLException, IOException
	{

		String allCitiesList = null, qrycitiesMO; 
		String Cities = null,countyMO = null;
		double allPopulation=0, cityPopulation = 0;
		
		
		qrycitiesMO = "SELECT * \r\n" + 
				"from dbo.USPopulation2019 \r\n" + 
				"Where StateName='"+txtFieldStateName.getText()+"' And population is not null and population <> 0 Order by population desc";

		rsCitiesWithinCounty = aStatement.executeQuery(qrycitiesMO);

		////////////////////////
		//For the first (centered) city to identify all the opportunity Values: 
		/////////////////////////
		
		
		if(rsCitiesWithinCounty.next()) {
			
			cityLatitude1 = rsCitiesWithinCounty.getDouble("Latitude");
			cityLongitude1 = rsCitiesWithinCounty.getDouble("Longitude");
			
			Cities = rsCitiesWithinCounty.getString("CityName");
			cityPopulation = rsCitiesWithinCounty.getDouble("population");
			countyMO = rsCitiesWithinCounty.getString("CountyName");
			
			oppReportObject.wasteGeneration(cityPopulation, Cities);
			oppReportObject.processingPricePerTon(Cities);
			oppReportObject.materialRevenue(Cities);
			oppReportObject.pickupCharge(cityPopulation,Cities);
			
			//distanceBetCities = Double.parseDouble(new DecimalFormat("#.##").format(oppReportObject.distance(cityLatitude1, cityLongitude1, cityLatitude2, cityLongitude2)));
			distanceBetCities = 0;
			oppReportObject.truckingExpense(distanceBetCities);
			oppReportObject.householdCost(cityPopulation);
			oppReportObject.procCost();
			oppReportObject.netRecycling();
			
			//oppReportObject.writeToFile(cityPopulation, Cities,true, countyMO);
	
		}
		
		while (rsCitiesWithinCounty.next()) 
		{
			Cities = rsCitiesWithinCounty.getString("CityName");
			cityPopulation = rsCitiesWithinCounty.getDouble("population");
			countyMO = rsCitiesWithinCounty.getString("CountyName");
			
			cityLatitude2 = rsCitiesWithinCounty.getDouble("Latitude");
			cityLongitude2 = rsCitiesWithinCounty.getDouble("Longitude");
			
			//JOptionPane.showMessageDialog(null, "Population of " + Cities + " is = " + cityPopulation);
			//allCitiesList += Cities + "\n";
			
			oppReportObject.wasteGeneration(cityPopulation, Cities);
			oppReportObject.processingPricePerTon(Cities);
			oppReportObject.materialRevenue(Cities);
			oppReportObject.pickupCharge(cityPopulation,Cities);
			
			distanceBetCities = Double.parseDouble(new DecimalFormat("#.##").format(oppReportObject.distance(cityLatitude1, cityLongitude1, cityLatitude2, cityLongitude2)));
			
			oppReportObject.truckingExpense(distanceBetCities);
			oppReportObject.householdCost(cityPopulation);
			oppReportObject.procCost();
			oppReportObject.netRecycling();
			
			//oppReportObject.writeToFile(cityPopulation, Cities, false, countyMO);
		}
		
		//JOptionPane.showMessageDialog(null, "List of cities within the county : " + allCitiesList);

	}//End of method mthCitiesMO
	

	public static void mthCentralCity() throws SQLException, IOException
	{
		
	}//End of method mthCentralCity()
	
	

}// End of Class
