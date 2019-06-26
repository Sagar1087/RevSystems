package OpportunityReport;

import java.sql.*;
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

		btnGenerateReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					Class.forName(JDBC_Driver);

					aConnection = DriverManager.getConnection(dbURL, userName, "");
					aStatement = aConnection.createStatement();

					mthCountyName();
					mthCitiesWithinCounty();

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
		qryCountyName = "Select CountyName \r\n" + "  From dbo.CIty_County_State\r\n" + "  Where StateName='"
				+ txtFieldStateName.getText() + "' and CityName = '" + txtFieldOpportunityName.getText() + "'";

		rsCountyName = aStatement.executeQuery(qryCountyName);

		while (rsCountyName.next()) {
			String Counties = null;
			Counties = rsCountyName.getString("CountyName");
			JOptionPane.showMessageDialog(null, "CountyName = " + Counties);
		}
	}//End of Method mthCountyName
	
	public static void mthCitiesWithinCounty() throws HeadlessException, SQLException
	{
		String allCitiesList = null; 
		String Cities = null;
		double allPopulation=0, cityPopulation = 0;
		qryCitiesWithinCounty = "  Select CityName, population\r\n" + "  From dbo.CIty_County_State\r\n"
				+ "  Where CountyName=( Select CountyName As Counties\r\n"
				+ "  From dbo.CIty_County_State\r\n" + "  Where StateName='" + txtFieldStateName.getText()
				+ "' and CityName= '" + txtFieldOpportunityName.getText()
				+ "') and population is not null and StateName = '" + txtFieldStateName.getText() + "'\r\n"
				+ "  Order by population DESC";

		rsCitiesWithinCounty = aStatement.executeQuery(qryCitiesWithinCounty);

		while (rsCitiesWithinCounty.next()) 
		{
			Cities = rsCitiesWithinCounty.getString("CityName");
			cityPopulation = rsCitiesWithinCounty.getDouble("population");
			//JOptionPane.showMessageDialog(null, "Population of " + Cities + " is = " + cityPopulation);
			//allCitiesList += Cities + "\n";
			
			oppReportObject.wasteGeneration(cityPopulation, Cities);
		}
		
		//JOptionPane.showMessageDialog(null, "List of cities within the county : " + allCitiesList);

	}//End of method mthCitiesWithinCounty

}// End of Class
