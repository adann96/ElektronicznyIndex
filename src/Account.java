import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import net.proteanit.sql.DbUtils;

import java.awt.Canvas;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class Account extends Login {

	private JPanel contentPane;
	private JTable gradTable;
	private JTable oplTable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Account frame = new Account();
					frame.setVisible(true);
					Class.forName("oracle.jdbc.driver.OracleDriver");
			
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	public Account() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 821, 559);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Canvas canvas = new Canvas();
		canvas.setBackground(new Color(204, 51, 51));
		canvas.setBounds(10, 10, 148, 158);
		contentPane.add(canvas);
		
		JLabel nameLabel = new JLabel("Adam Wojciech Doma\u0144ski");
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setBounds(10, 174, 148, 48);
		contentPane.add(nameLabel);
		
		JLabel indNoLabel = new JLabel("Numer Indeksu: 212305");
		indNoLabel.setBounds(20, 233, 148, 14);
		contentPane.add(indNoLabel);
		
		JLabel facLabel = new JLabel("Kierunek: Informatyka");
		facLabel.setFont(new Font("Tahoma", Font.ITALIC, 11));
		facLabel.setBounds(20, 252, 159, 14);
		contentPane.add(facLabel);
		
		JLabel wydLabel = new JLabel("Wydzia\u0142: EEIiA");
		wydLabel.setFont(new Font("Tahoma", Font.ITALIC, 11));
		wydLabel.setBounds(20, 270, 138, 14);
		contentPane.add(wydLabel);
		
		try 
		{
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orclAD2019","univ_db","szczesny1");
			String getDataFromStudenci = "select studenci.NUMER_INDEKSU, studenci.Imie, studenci.drugie_imie, studenci.Nazwisko, Kierunki.NAZWA, wydzia³y.nazwa from studenci inner join Kierunki on Kierunki.KIERUNEK_ID = studenci.KIERUNEK_ID inner join wydzia³y on wydzia³y.wydzia³_id = kierunki.wydzia³_id where NUMER_INDEKSU="+ id + " and PASSWORD_ACC='" + pass + "'";
			
			PreparedStatement stGetDataFromStudenci = con.prepareStatement(getDataFromStudenci);
			ResultSet resGetDataFromStudenci = stGetDataFromStudenci.executeQuery(getDataFromStudenci);
			
			if (resGetDataFromStudenci.next() && (resGetDataFromStudenci.getString(3) == null)) {
				nameLabel.setText(resGetDataFromStudenci.getString(2) + " " + resGetDataFromStudenci.getString(4));
				indNoLabel.setText("Numer albumu: " + resGetDataFromStudenci.getString(1));
				facLabel.setText("Kierunek: " + resGetDataFromStudenci.getString(5));
				wydLabel.setText("Wydzia³: " + resGetDataFromStudenci.getString(6));
			}
			else {
				nameLabel.setText(resGetDataFromStudenci.getString(2) + " " + resGetDataFromStudenci.getString(3) + " " + resGetDataFromStudenci.getString(4));
				indNoLabel.setText("Numer albumu: " + resGetDataFromStudenci.getString(1));
				facLabel.setText("Kierunek: " + resGetDataFromStudenci.getString(5));
				wydLabel.setText("Wydzia³: " + resGetDataFromStudenci.getString(6));
			}
				
		}
		catch(Exception e2) {
			System.out.print(e2);
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(164, 66, 631, 218);
		contentPane.add(scrollPane);
		
		gradTable = new JTable();
		scrollPane.setViewportView(gradTable);
		
		String[] semTypes = {"Semestr 1","Semestr 2","Semestr 3","Semestr 4","Semestr 5","Semestr 6","Semestr 7","Semestr 8"};
		JComboBox semComboBox = new JComboBox(semTypes);
		
		semComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JComboBox cb = (JComboBox)arg0.getSource();
				String selectName = (String)cb.getSelectedItem();
				
				try 
				{	
					
					String semNo = selectName.substring(8, 9);
					int semNoInt = Integer.parseInt(semNo);
					
					Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orclAD2019","univ_db","szczesny1");
					String getGrades = "select przedmioty.PRZEDMIOT_ID, przedmioty.NAZWA_PRZEDMIOTU, przedmioty.PUNKTY_ECTS, oceny.OCENA_KONC from studenci inner join przedmioty on przedmioty.KIERUNEK_ID = studenci.KIERUNEK_ID inner join OCENY on OCENY.PRZEDMIOT_ID = przedmioty.przedmiot_id where PRZEDMIOTY.NR_SEMESTRU = " + semNoInt + " and OCENY.NUMER_INDEKSU=" + id + " and STUDENCI.PASSWORD_ACC='" + pass + "' order by 1";

					PreparedStatement stGetGrades = con.prepareStatement(getGrades);
					ResultSet resGetGrades = stGetGrades.executeQuery(getGrades);
					
						gradTable.setModel(DbUtils.resultSetToTableModel(resGetGrades));
						
						TableColumnModel modelProp = gradTable.getColumnModel();
						DefaultTableCellRenderer centerRen = new DefaultTableCellRenderer();
						centerRen.setHorizontalAlignment(JLabel.CENTER);

						for (int i = 0; i<4; i++) {
							if(i != 1) {
								gradTable.getColumnModel().getColumn(i).setCellRenderer(centerRen);
							}
							else {
								//DO NOTHING
							}
						}
					
						modelProp.getColumn(0).setPreferredWidth(145);
						modelProp.getColumn(0).setHeaderValue("ID przedmiotu");

						modelProp.getColumn(1).setHeaderValue("Nazwa przedmiotu");
						modelProp.getColumn(1).setPreferredWidth(380);
						
						modelProp.getColumn(2).setHeaderValue("ECTS");
						modelProp.getColumn(2).setPreferredWidth(100);
						
						modelProp.getColumn(3).setHeaderValue("Ocena");
						modelProp.getColumn(3).setPreferredWidth(110);
				}
				catch(Exception e1) {
					System.out.print(e1);
				}
			}
		});
		semComboBox.setBackground(new Color(240, 240, 240));
		semComboBox.setBounds(164, 35, 148, 20);
		contentPane.add(semComboBox);
		
		JLabel lblOpatyZaStudia = new JLabel("Op\u0142aty za studia");
		lblOpatyZaStudia.setBounds(164, 295, 148, 14);
		contentPane.add(lblOpatyZaStudia);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(164, 351, 631, 48);
		contentPane.add(scrollPane_1);
		
		oplTable = new JTable();
		scrollPane_1.setViewportView(oplTable);
		
		String[] semTypesOpl = {"Semestr 1","Semestr 2","Semestr 3","Semestr 4","Semestr 5","Semestr 6","Semestr 7","Semestr 8"};
		JComboBox oplComboBox = new JComboBox(semTypesOpl);

		oplComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JComboBox cb = (JComboBox)arg0.getSource();
				String selectName = (String)cb.getSelectedItem();
				
				try {
					String semNo = selectName.substring(8, 9);
					int semNoInt = Integer.parseInt(semNo);
					
					Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orclAD2019","univ_db","szczesny1");
					String getOpl = "select NVL(RATA_PIERWSZA,0) || ' z³', NVL(RATA_DRUGA,0) || ' z³', NVL(RATA_TRZECIA,0) || ' z³', NVL(RATA_CZWARTA,0) || ' z³', NVL(POWT_PRZEDM,0) || ' z³', NVL(ODSETKI,0) || ' z³', NVL(NADP£ATA,0) || ' z³' from OP£ATY where NUMER_INDEKSU = " + id + " and SEMESTR = " + semNoInt;

					PreparedStatement stGetOpl = con.prepareStatement(getOpl);
					ResultSet resGetOpl = stGetOpl.executeQuery(getOpl);
					
					oplTable.setModel(DbUtils.resultSetToTableModel(resGetOpl));
					
					TableColumnModel modelProp = oplTable.getColumnModel();
					DefaultTableCellRenderer centerRen = new DefaultTableCellRenderer();
					centerRen.setHorizontalAlignment(JLabel.CENTER);
					
					for (int i = 0; i<7; i++) {
						oplTable.getColumnModel().getColumn(i).setCellRenderer(centerRen);
					}
					
					modelProp.getColumn(0).setHeaderValue("1 RATA");
					modelProp.getColumn(1).setHeaderValue("2 RATA");
					modelProp.getColumn(2).setHeaderValue("3 RATA");
					modelProp.getColumn(3).setHeaderValue("4 RATA");
					modelProp.getColumn(4).setHeaderValue("Powtarzanie przedmiotu");
					modelProp.getColumn(4).setPreferredWidth(145);
					modelProp.getColumn(5).setHeaderValue("Odsetki");
					modelProp.getColumn(6).setHeaderValue("Nadp³ata");
					
				}
				catch(Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		
		oplComboBox.setBackground(SystemColor.menu);
		oplComboBox.setBounds(164, 320, 148, 20);
		contentPane.add(oplComboBox);
		
		JLabel lblPodgldOcen = new JLabel("Podgl\u0105d ocen:");
		lblPodgldOcen.setBounds(164, 10, 148, 14);
		contentPane.add(lblPodgldOcen);
	}
}