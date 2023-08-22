package com.pythonanywhere.brilliantcomputer;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Date;
import java.util.Calendar;
import java.util.Vector;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.awt.event.*;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.UIManager;
import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.plaf.metal.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartTransferable;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.StandardChartTheme;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.IntervalXYDataset;

public final class NewNoLimit extends ApplicationFrame implements ActionListener, TreeSelectionListener
{
  
  private JTree tree;
  private static final Calendar calendar = Calendar.getInstance();
  private static String[]	months = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"},
								monthn = {"01","02","03","04","05","06","07","08","09","10","11","12"},
								dayn = {"00", "01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
  private static final String C_YEAR = String.valueOf(calendar.get(Calendar.YEAR));
  private static final int C_YEAR_N = calendar.get(Calendar.YEAR);
  private static final int C_MONTH = calendar.get(Calendar.MONTH);
  private static final int C_DAY = calendar.get(Calendar.DAY_OF_MONTH);
  private static final String EXIT_COMMAND = "EXIT",
							NSE_DB = "nsecm2023",
							BSE_DB = "nsecm2023",
							NSEFUT_DB = "nsefo2023",
							NSEOPT_DB = "nsefo2023";

  private static String[] Symbols = new String[60000], Contracts = new String[60000], NseEqTables = new String[8000],
						BseEqTables = new String[8000],
						NseFnoTables = new String[36000],
						NseOptTables = new String[99999],
						closeData = new String[4000],
						tradeDates = new String[4000],
						temp = new String[10],
						actualNseFno= new String[99999],
						NseFnoFromFile= new String[99999],
						mynseequity= new String[99999],
						mybseequity= new String[99999],
						mynsefno= new String[99999],
						dbs = {NSE_DB, BSE_DB, NSEFUT_DB, NSEOPT_DB };
  private static final String[] CMTables = new String[8000];
  private static final String[] FOTables = new String[60000];
  private Vector AllTables = new Vector();
  private Vector niftyLows = new Vector(),
  				niftyHighs = new Vector(),
                niftyTradeDates = new Vector(),
				niftyCloses = new Vector(),
				niftyVolumes = new Vector(),
				niftyPrevCMPs = new Vector(),
				niftyCMPs = new Vector(),
				niftyScrips = new Vector(),
				niftyPchange = new Vector(),
				curNiftyIndex = new Vector(),
				curNiftyVol = new Vector(),
				curNiftyPC = new Vector();

  private static int totalRecords,niftyCount,NseFnoScripCount,
					totalTradeDates,
					bserows, nserows,
					bsecols, nsecols;

  private static String[][] BseSectors = new String[250][250],
							NseSectors = new String[250][250];


  private JComponent  compQueryOhlc, compQueryPerc;
  private JPanel chartContainerPanel, chartDisplayPanel, insertDataPanel, myEquityPanel, mainPanel, mainPanel2,
  			tabPanel3, centerBorderPanel, resultPanel, connectionPanel, connectionPanel2;
  private JScrollPane spOhlc, spPerc, scrollpaneMyScrips;
  private JTable tableMyScrips;
  private JComboBox fnoComboBox, fnoComboBox2, cmbNifty, cboMyEquity, cmbDatabase;
  private JTextArea   queryTextArea, queryTextArea2, texaNews, texaProfitLoss;
  private JButton fetchButton, fetchButton2, showConnectionInfoButton, showConnectionInfoButton2,
	  		btnMyEquityInsert, btnNas100, btnSp500, btnDjia,
			btnSsicomposite, btnNikkei, btnEurostox50, btnFtse100;
  private JLabel lblRbi, lblNifty, lblBseEquity, lblNseEquity, lblNseFno, lblMcxFut, lblNcdexFut, lblNseOpt,
			lblBseFno, lblBseOpt, lblNseCurFut, lblNseCurOpt, lblFiiDii, userNameLabel, userNameLabel2,
			passwordLabel, passwordLabel2, serverLabel, serverLabel2, driverLabel, driverLabel2,
			lblScrip, lblCompany,  lblLot,  lblSector, lblFaceValue,  lblBonusPerShare, lblAGMdate, lblURI,
			labelScripName, labelTransactionDate, labelTransactionType, labelPrice, labelQuantity,
			labelAmountPaid, labelTaxBrkPaid,
			lblNas100, lblSp500, lblDjia, lblDateWorldIndices,
			lblSsicomposite, lblNikkei, lblEurostox50, lblFtse100,
			lblPchangeDate, lblPchangeDateFrom,
			lblPchangeDate2, lblPchangeDateFrom2;
  private JTextField txtRbiFileName, txtNiftyFileName, txtBseEquityFileName, txtNseEquityFileName, txtNseFnoFileName,
			txtMcxFutFileName, txtNcdexFutFileName, txtNseOptFileName, txtBseFnoFileName, txtBseOptFileName,
			txtNseCurFutFileName, txtNseCurOptFileName, txtFiiDiiFileName, userNameField, userNameField2,
			passwordField, passwordField2, serverField, serverField2, driverField, driverField2,
			textFromDate1, textToDate1, textFromDate2, textToDate2, textScripName, textTransactionDate,
			textTransactionType, textPrice, textQuantity, textAmountPaid, textTaxBrkPaid,
			textNas100, textSp500, textDjia, textDateWorldIndices,
			textSsicomposite, textNikkei, textEurostox50, textFtse100,
			textPchangeDate , textPchangeDateFrom,
			textPchangeDate2 , textPchangeDateFrom2;
   
 
  public NewNoLimit(String appTitle)
  {
    super(appTitle);
    setJMenuBar(createMenuBar());
    setContentPane(createContent());

  }
      public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
                /*
                insertCmCsv("cm04AUG2023bhav.csv");
                insertFnoCsv("fo09AUG2023bhav.csv");*/
                createAndShowGUI();
                //AllFunDbMysql objCm = new AllFunDbMysql();
                //objCm.setTables("nsecm2023", "cm");
                
                //AllFunDbMysql objFo = new AllFunDbMysql();
                //objFo.setTables("nsefo2023", "fo");
                
                //objCm.getCmTbls();
                //objFo.getFoTbls();
                //objCm.setRecord("nsecm2023", "cm01aug2023", "*", "WHERE SERIES='EQ'");
                //objCm.getRecord();
                //System.out.println(objCm.record);
                //System.out.println(objCm.close);
                //System.out.println(objCm.vol);
            }
        });

    }
    private static  void createAndShowGUI() {
   /*
    try
    {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
      UIManager.setLookAndFeel("javax.swing.plaf.basic.BasicLookAndFeel");
    }
    catch (Exception e1)
    {
      try
      {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch (Exception e2)
      {
        e2.printStackTrace();
      }
    }
    */

    JFrame.setDefaultLookAndFeelDecorated(true);
    NewNoLimit objectMoneyTree = new NewNoLimit("NoLimit");
    objectMoneyTree.pack();
    RefineryUtilities.centerFrameOnScreen(objectMoneyTree);
    objectMoneyTree.setVisible(true);
  
    } 
  
  public class RunNNL implements Runnable
  {
    private NewNoLimit objNewNoLimit;    
    private String instType;
    private String[] tables;
    private String scrip;

    public RunNNL(NewNoLimit objNewNoLimit,  String instType, String[] tables, String scrip)        //4.1
    {
      this.objNewNoLimit = objNewNoLimit;      
      this.instType = instType;
      this.tables = tables;
      this.scrip = scrip;
    }

    public void run()
    {
        //getRecords(database, "tradedate, ohlc", "");
        
        CandlestickChart csc = new CandlestickChart(instType, tables, scrip);
        this.objNewNoLimit.chartContainerPanel.add(csc.createDemoPanel());
        this.objNewNoLimit.chartDisplayPanel.validate();

     

    }
  }
  public void actionPerformed(ActionEvent paramActionEvent)
  {
    String str = paramActionEvent.getActionCommand();
  }
  public void valueChanged(TreeSelectionEvent tse) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)
    tree.getLastSelectedPathComponent();

    if (node == null) return;

    Object lastLeaf = node.getUserObject();
    String inst = node.getParent().getParent().toString();
    String scrip = (String)node.toString();
    SwingUtilities.invokeLater(new RunNNL(this, inst.toLowerCase(), CMTables, scrip));      
  }


  public JMenuBar createMenuBar()
  {
    JMenuBar localJMenuBar = new JMenuBar();
    JMenu localJMenu1 = new JMenu("File", true);
    localJMenu1.setMnemonic('F');
    JMenuItem localJMenuItem1 = new JMenuItem("Export to PDF...", 112);
    localJMenuItem1.setActionCommand("EXPORT_TO_PDF");
    localJMenuItem1.addActionListener(this);
    localJMenu1.add(localJMenuItem1);
    localJMenu1.addSeparator();
    JMenuItem localJMenuItem2 = new JMenuItem("Exit", 120);
    localJMenuItem2.setActionCommand("EXIT");
    localJMenuItem2.addActionListener(this);
    localJMenu1.add(localJMenuItem2);
    localJMenuBar.add(localJMenu1);
    JMenu localJMenu2 = new JMenu("Edit", false);
    localJMenuBar.add(localJMenu2);
    JMenuItem localJMenuItem3 = new JMenuItem("Copy", 67);
    localJMenuItem3.setActionCommand("COPY");
    localJMenuItem3.addActionListener(this);
    localJMenu2.add(localJMenuItem3);
    JMenu localJMenu3 = new JMenu("Theme", true);
    localJMenu3.setMnemonic('T');
    JCheckBoxMenuItem localJCheckBoxMenuItem1 = new JCheckBoxMenuItem("JFree", true);
    localJCheckBoxMenuItem1.setActionCommand("JFREE_THEME");
    localJCheckBoxMenuItem1.addActionListener(this);
    localJMenu3.add(localJCheckBoxMenuItem1);
    JCheckBoxMenuItem localJCheckBoxMenuItem2 = new JCheckBoxMenuItem("Darkness", false);
    localJCheckBoxMenuItem2.setActionCommand("DARKNESS_THEME");
    localJCheckBoxMenuItem2.addActionListener(this);
    localJMenu3.add(localJCheckBoxMenuItem2);
    JCheckBoxMenuItem localJCheckBoxMenuItem3 = new JCheckBoxMenuItem("Legacy", false);
    localJCheckBoxMenuItem3.setActionCommand("LEGACY_THEME");
    localJCheckBoxMenuItem3.addActionListener(this);
    localJMenu3.add(localJCheckBoxMenuItem3);
    ButtonGroup localButtonGroup = new ButtonGroup();
    localButtonGroup.add(localJCheckBoxMenuItem1);
    localButtonGroup.add(localJCheckBoxMenuItem2);
    localButtonGroup.add(localJCheckBoxMenuItem3);
    localJMenuBar.add(localJMenu3);
    return localJMenuBar;
  }
  public JPanel createContent()
  {
    tree = new JTree(createTreeModel());
    tree.addTreeSelectionListener(this);

    JScrollPane scrollPaneTree = new JScrollPane(tree);
    JSplitPane splitPaneTreeAndChart = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    splitPaneTreeAndChart.setLeftComponent(scrollPaneTree);
    splitPaneTreeAndChart.setRightComponent(createChartDisplayPanel());

    JPanel pnlTreeChartContainer = new JPanel(new BorderLayout());
    pnlTreeChartContainer.add(splitPaneTreeAndChart);

    JPanel pnlAllExchTab = new JPanel(new BorderLayout());
    pnlAllExchTab.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    pnlAllExchTab.add(pnlTreeChartContainer);

    JTabbedPane tabbedPaneApp = new JTabbedPane();
    tabbedPaneApp.add("All Exchange", pnlAllExchTab);
    tabbedPaneApp.add("Insert Data", ShowTabInsertDataPanel());

	JPanel pnltabbedPaneApp = new JPanel(new BorderLayout());
    pnltabbedPaneApp.setPreferredSize(new Dimension(1000,675));
    pnltabbedPaneApp.add(tabbedPaneApp);

    return pnltabbedPaneApp;
  }
  public TreeModel createTreeModel()
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("Instrument");

    localDefaultMutableTreeNode.add(createCMDepth1Node("nsecm2023"));
    localDefaultMutableTreeNode.add(createFODepth1Node("nsefo2023"));
    //localDefaultMutableTreeNode.add(createNodeNameFromDB("nsecm2023", "Nse"));
    //localDefaultMutableTreeNode.add(createNodeNameFromDBFromFile("nsefo2023"));
    //localDefaultMutableTreeNode.add(createNodeNameFromDB("nsefo2023", "NseFut"));
    //localDefaultMutableTreeNode.add(createBseSectorsNode());
    //localDefaultMutableTreeNode.add(createNseSectorsNode());
    return new DefaultTreeModel(localDefaultMutableTreeNode);
  }
  public MutableTreeNode createNodeNameFromDBTable(String database, String table)
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode(database);
    getAllScrips(database, table);

    for(int i=1;NseOptTables[i]!=null;++i){
     String s = new String( (String)NseOptTables[i]);
     localDefaultMutableTreeNode.add(createLeaf(s, s));
    }
    
    return localDefaultMutableTreeNode;
  }
  public MutableTreeNode createNodeNameFromDBFromFile(String databaseName)
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode(databaseName);
    getFromCsvFile(databaseName);
    if(databaseName.equalsIgnoreCase("Nse"))
       for(int i=0;NseEqTables[i]!=null;++i){
           String s = new String( (String)NseEqTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(databaseName.equalsIgnoreCase("Bse"))
       for(int i=1;BseEqTables[i]!=null;++i){
           String s = new String( (String)BseEqTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(databaseName.equalsIgnoreCase("NseFut"))
       for(int i=0;NseFnoTables[i]!=null;++i){
           String s = new String( (String)NseFnoTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(databaseName.equalsIgnoreCase("NseOpt"))
       for(int i=0;NseOptTables[i]!=null;++i){
           String s = new String( (String)NseOptTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    return localDefaultMutableTreeNode;
  }
  public void getFromCsvFile(String databaseName)
  {
     	try	{
            String strLine = null, fileName = "_" + databaseName + "_ALL_TABLES.txt";
            String[] ar = null;
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            while( (strLine = br.readLine()) != null) {
                ar = strLine.split(",");

			    if(databaseName.equalsIgnoreCase("Nse")) for(int i = 0; i< ar.length; ++i) NseEqTables[i] = ar[i];
			    else if(databaseName.equalsIgnoreCase("Bse")) for(int i = 0; i< ar.length; ++i) BseEqTables[i] = ar[i];
			    else if(databaseName.equalsIgnoreCase("NseFut")) for(int i = 0; i< ar.length; ++i) NseFnoTables[i] = ar[i];
			    else if(databaseName.equalsIgnoreCase("NseOpt")) for(int i = 0; i< ar.length; ++i) NseOptTables[i] = ar[i];
            }
         br.close();
        } catch(Exception e) {
            System.out.println("Exception while reading csv file: " + e);
        }
  }
  public MutableTreeNode createNodeNameFromDB(String databaseName, String tableArrayPrefix)
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode(databaseName);
    getCMTables(databaseName);
    if(tableArrayPrefix.equals("Nse"))
       for(int i=0;NseEqTables[i]!=null;++i){
           String s = new String( (String)NseEqTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(tableArrayPrefix.equals("Bse"))
       for(int i=1;BseEqTables[i]!=null;++i){
           String s = new String( (String)BseEqTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(tableArrayPrefix.equals("NseFut"))
       for(int i=0;NseFnoTables[i]!=null;++i){
           String s = new String( (String)NseFnoTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    else if(tableArrayPrefix.equals("NseOpt"))
       for(int i=0;NseOptTables[i]!=null;++i){
           String s = new String( (String)NseOptTables[i]);
               localDefaultMutableTreeNode.add(createLeaf(s, s));
       }
    localDefaultMutableTreeNode.add(createNodeNameFromDBTable(databaseName,tableArrayPrefix));
    return localDefaultMutableTreeNode;
  }
  public void getCMTables(String database)
  {
      try {
        Class.forName("com.mysql.jdbc.Driver");
        totalRecords = 0;
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + database, "root","deepak");
        Statement statement = connection.createStatement();
        String sql = "SHOW TABLES";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            String table = rs.getString("Tables_In_" + database);
            CMTables[totalRecords] = new String(table);                  
            ++totalRecords;
        }
        connection.clearWarnings();
        connection.close();
    }
    catch (ClassNotFoundException ex) {
        System.err.println("ClassNotFoundException");
    }
    catch (SQLException ex) {
        System.err.println("SQLException");
    }
  }  
  public void getFOTables(String database)
  {
      try {
        Class.forName("com.mysql.jdbc.Driver");
        totalRecords = 0;
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + database, "root","deepak");
        Statement statement = connection.createStatement();
        String sql = "SHOW TABLES";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            String table = rs.getString("Tables_In_" + database);
            FOTables[totalRecords] = new String(table);                  
            ++totalRecords;
        }
        connection.clearWarnings();
        connection.close();
    }
    catch (ClassNotFoundException ex) {
        System.err.println("ClassNotFoundException");
    }
    catch (SQLException ex) {
        System.err.println("SQLException");
    }
  }
  public void getAllScrips(String database, String table)
  {
      try {
        Class.forName("com.mysql.jdbc.Driver");
        totalRecords = 0;
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + database, "root","deepak");
        Statement statement = connection.createStatement();
        String sql = "SELECT SYMBOL FROM " + table + " WHERE SERIES = 'EQ'";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            String scrip = rs.getString("SYMBOL");
            Symbols[totalRecords] = new String(scrip);
            ++totalRecords;
        }
        connection.clearWarnings();
        connection.close();
    }
    catch (ClassNotFoundException ex) {
        System.err.println("ClassNotFoundException");
    }
    catch (SQLException ex) {
        System.err.println("SQLException");
    }
  }  public void getAllContracts(String database, String table)
  {
      try {
        Class.forName("com.mysql.jdbc.Driver");
        totalRecords = 0;
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + database, "root","deepak");
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM " + table;
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            String i = rs.getString("INSTRUMENT");
            String s = rs.getString("SYMBOL");
            String ed = rs.getString("EXPIRY_DT");
            String sp = rs.getString("STRIKE_PR");
            String ot = rs.getString("OPTION_TYP");
            
            Contracts[totalRecords] = new String(i + " " + s + " "+ ed + " " + sp + " " + ot);
            //Contracts[totalRecords] = new String(s);
            ++totalRecords;
        }
        connection.clearWarnings();
        connection.close();
    }
    catch (ClassNotFoundException ex) {
        System.err.println("ClassNotFoundException");
    }
    catch (SQLException ex) {
        System.err.println("SQLException");
    }
  }
  public MutableTreeNode createLeaf(String strClassName, String strDescription)
  {
    return new DefaultMutableTreeNode(strClassName);
  }
  public MutableTreeNode createNseFnOEquityNode()
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("NSE_FNO_Equity");
    getFromFile("NSE_FNO_Equity.txt");
    actualNseFno = new String[NseFnoScripCount];
    for(int i=0;i<NseFnoScripCount;++i) {
        actualNseFno[i] = NseFnoFromFile[i];
        localDefaultMutableTreeNode.add(createLeaf((String)actualNseFno[i], (String)actualNseFno[i]));
    }
    return localDefaultMutableTreeNode;
  }
  public void getFromFile(String file){
        try{
            BufferedReader br = new BufferedReader( new FileReader(file));
            String oneLineFromFile = "";
            int count = 0, countbsecols = 1, countnsecols = 1;
            if(file.equals("NSE_FNO_Equity.txt")){
                 while( (oneLineFromFile = br.readLine()) != null){
                        NseFnoFromFile[count++] = oneLineFromFile;
                    ++NseFnoScripCount;
                 }
            }else if(file.equals("BSE_SECTORS.txt")){
                 while( (oneLineFromFile = br.readLine()) != null){
                        BseSectors[count++][0] = oneLineFromFile;
                 }
            }else if(file.equals("NSE_SECTORS.txt")){
                 while( (oneLineFromFile = br.readLine()) != null){
                        NseSectors[count++][0] = oneLineFromFile;
                 }
            }else{
                 while( (oneLineFromFile = br.readLine()) != null){
                        BseSectors[bserows][countbsecols++] = oneLineFromFile;
                        NseSectors[nserows][countnsecols++] = oneLineFromFile;
                        ++bsecols;
                        ++nsecols;
                 }
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
  }

  public MutableTreeNode createCMDepth1Node(String db)
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("NSECM");
    getCMTables(db);
    for(int i=0;CMTables[i]!=null;++i){
        String tbl = new String( (String)CMTables[i]);
        localDefaultMutableTreeNode.add(createCMDepth2Node(db, tbl));
    }
    return localDefaultMutableTreeNode;
  }  
  public MutableTreeNode createCMDepth2Node(String db, String tbl)
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode(tbl);
    getAllScrips(db, tbl);
     for(int i=0;Symbols[i]!=null;++i){
        String s = new String( (String)Symbols[i]);
        localDefaultMutableTreeNode.add(createLeaf(s, s));
    }
    return localDefaultMutableTreeNode;
  }  
  public MutableTreeNode createFODepth1Node(String db)
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("NSEFO");
    getFOTables(db);
    for(int i=0;FOTables[i]!=null;++i){
        String tbl = new String( (String)FOTables[i]);
        localDefaultMutableTreeNode.add(createFODepth2Node(db, tbl));
    }
    return localDefaultMutableTreeNode;
  }  
  public MutableTreeNode createFODepth2Node(String db, String tbl)
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode(tbl);
    getAllContracts(db, tbl);
     for(int i=0;Contracts[i]!=null;++i){
        String s = new String( (String)Contracts[i]);
        localDefaultMutableTreeNode.add(createLeaf(s, s));
    }
    return localDefaultMutableTreeNode;
  }
  public MutableTreeNode createBseSectorsNode()
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("BseSectors");
    getFromFile("BSE_SECTORS.txt");
    bserows = 0; bsecols = 0;
    for(int r = 0; BseSectors[r][0]!=null; ++ r){
        String sectorName = new String( (String)BseSectors[r][0]);
            localDefaultMutableTreeNode.add(createBseSectorsLeafNode(sectorName , r));
    }
    return localDefaultMutableTreeNode;
  }  
  public MutableTreeNode createBseSectorsLeafNode(String sectorName, int r)
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode(sectorName);
    getFromFile(sectorName + ".txt");
    for(int c = 1; c<=bsecols; ++ c){
        String sectorScrip = new String( (String)BseSectors[bserows][c]);
            localDefaultMutableTreeNode.add(createLeaf(sectorScrip, sectorScrip));
    }
    bsecols = 0;
    return localDefaultMutableTreeNode;
  }
  public MutableTreeNode createNseSectorsNode()
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("NseSectors");
    getFromFile("NSE_SECTORS.txt");
    nserows = 0; nsecols = 0;
    for(int r = 0; NseSectors[r][0]!=null; ++ r){
        String sectorName = new String( (String)NseSectors[r][0]);
            localDefaultMutableTreeNode.add(createNseSectorsLeafNode(sectorName , r));
    }
    return localDefaultMutableTreeNode;
  }

  public MutableTreeNode createNseSectorsLeafNode(String sectorName, int r)
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode(sectorName);
    getFromFile(sectorName + ".txt");
    for(int c = 1; c<=nsecols; ++ c){
        String sectorScrip = new String( (String)NseSectors[nserows][c]);
            localDefaultMutableTreeNode.add(createLeaf(sectorScrip, sectorScrip));
    }
    nsecols = 0;
    return localDefaultMutableTreeNode;
  }
  public JPanel createChartDisplayPanel()
  {
    chartDisplayPanel = new JPanel(new BorderLayout());
    chartDisplayPanel.setPreferredSize(new Dimension(500, 500));
    chartContainerPanel = new JPanel(new BorderLayout());
    //chartContainerPanel.setPreferredSize(new Dimension(500, 500));
    //chartContainerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4), BorderFactory.createLineBorder(Color.black)));
    //chartContainerPanel.add(createNoNodeSelectedPanel());
    //chartContainerPanel.add(createNiftyChart(nifty100));
    chartDisplayPanel.add(chartContainerPanel);
    return  chartDisplayPanel;
  }
  public JPanel createNoNodeSelectedPanel()
  {
     JPanel objEmptyChartPanel = new JPanel(new FlowLayout()) {
	       public String getToolTipText() {
				//return "(" + getWidth() + ", " + getHeight() + ")";
				return "Select a leaf in left tree";
			}
	 };
    ToolTipManager.sharedInstance().registerComponent(objEmptyChartPanel);
    objEmptyChartPanel.add(new JLabel("No Node selected"));
    objEmptyChartPanel.setPreferredSize(new Dimension(500, 500));
    return objEmptyChartPanel;
  }
  

  public JPanel ShowTabInsertDataPanel()
  {
	insertDataPanel = new JPanel(new GridLayout(1, 2, 20, 20));
	insertDataPanel.setPreferredSize(new Dimension(800,400));

        insertDataPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
	JPanel filesPanel = new JPanel(new GridLayout(13, 2, 20, 20));
	filesPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
	
	filesPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0)));
	
	
	lblNifty = new JLabel("   NIFTY");
	txtNiftyFileName = new JTextField("S&P CNX NIFTY" + dayn[calendar.get(Calendar.DAY_OF_MONTH)] + "-" + monthn[calendar.get(Calendar.MONTH)] + "-" + C_YEAR + "-" + dayn[calendar.get(Calendar.DAY_OF_MONTH)] + "-" + monthn[calendar.get(Calendar.MONTH)] + "-" + C_YEAR + ".csv",20);
	lblBseEquity = new JLabel("   BSE EQ ");
	txtBseEquityFileName = new JTextField("eq" + dayn[calendar.get(Calendar.DAY_OF_MONTH)] + monthn[calendar.get(Calendar.MONTH)] + C_YEAR.substring(2) + ".csv", 20);
	lblNseEquity = new JLabel("   NSE EQ ");
	txtNseEquityFileName = new JTextField("cm" + dayn[calendar.get(Calendar.DAY_OF_MONTH)] + "" +  months[calendar.get(Calendar.MONTH)] + C_YEAR + "bhav.csv", 20);
	lblNseFno = new JLabel("   NSE F & O ");
	txtNseFnoFileName = new JTextField("fo" + dayn[calendar.get(Calendar.DAY_OF_MONTH)] + "" +  months[calendar.get(Calendar.MONTH)] + C_YEAR + "bhav.csv", 20);
	lblNseOpt = new JLabel("NSE O");
	txtNseOptFileName = new JTextField("", 20);
	

	


	filesPanel.add(lblNifty);
	filesPanel.add(txtNiftyFileName);
	filesPanel.add(lblBseEquity);
	filesPanel.add(txtBseEquityFileName);
        
	filesPanel.add(lblNseEquity);
	filesPanel.add(txtNseEquityFileName);
	filesPanel.add(lblNseFno);
	filesPanel.add(txtNseFnoFileName);


	insertDataPanel.add(filesPanel);



	   txtNiftyFileName.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent ke) {
	            int key =ke.getKeyCode();
	            if(key==KeyEvent.VK_ENTER){
	                insertNiftyCsvFileToDatabase(txtNiftyFileName.getText());
	                txtNiftyFileName.setBackground(new Color(255, 0, 255));
	            }
	        }

	    }
	   );
	   txtBseEquityFileName.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent ke) {
	            int key =ke.getKeyCode();
	            if(key==KeyEvent.VK_ENTER){
	                insertBseEquityCsvFileToDatabase(txtBseEquityFileName.getText());
	                txtBseEquityFileName.setBackground(new Color(255, 0, 255));
	            }
	        }

	    }
	   );
	   txtNseEquityFileName.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent ke) {
	            int key =ke.getKeyCode();
	            if(key==KeyEvent.VK_ENTER){
                        SwingUtilities.invokeLater(new CSVInsert(txtNseEquityFileName.getText(), "nsecm"));
	                
	                txtNseEquityFileName.setBackground(new Color(255, 0, 255));
	            }
	        }

	    }
	   );
	   txtNseFnoFileName.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent ke) {
	            int key =ke.getKeyCode();
	            if(key==KeyEvent.VK_ENTER){
                        SwingUtilities.invokeLater(new CSVInsert(txtNseFnoFileName.getText(), "nsefo"));
	                
	                txtNseFnoFileName.setBackground(new Color(255, 0, 255));
	            }
	        }

	    }
	   );
       return insertDataPanel;
  }
  public void getAllTablesFromDatabase(String database)
  {
      try {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + database, "root","deepak");
        Statement statement = connection.createStatement();
        String sql = "SHOW TABLES";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
			String table = rs.getString("Tables_In_" + database);
			AllTables.add(table.substring(table.indexOf("_") + 1));
        }
        connection.clearWarnings();
        connection.close();
    }
    catch (ClassNotFoundException ex) {
        System.err.println("ClassNotFoundException");
    }
    catch (SQLException ex) {
        System.err.println("SQLException");
    }
  }
  public void getNiftyIndex(String trdate)
  {
	   try {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + NSE_DB, "root","deepak");
        Statement statement = connection.createStatement();
        String sql = "SELECT ohlc FROM nse_nifty WHERE tradedate <= " + trdate  + " ORDER BY tradedate DESC LIMIT 2";
		ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
			String ohlcdata = rs.getString("ohlc");
			String[] oh = null;
			oh = ohlcdata.split(",");
			curNiftyIndex.add(oh[3]);
			curNiftyVol.add(oh[4]);
        }
        connection.clearWarnings();
        connection.close();
    }
    catch (ClassNotFoundException ex) {
        System.err.println("ClassNotFoundException");
    }
    catch (SQLException ex) {
        System.err.println("SQLException");
        ex.printStackTrace();
    }
  }



 public void getNifty(String tableName, int scripno)
 {
       try {
        Class.forName("com.mysql.jdbc.Driver");
        totalRecords = 0;
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + NSE_DB, "root","deepak");
        Statement statement = connection.createStatement();
        String sql = "SELECT tradedate, ohlc FROM " + tableName;
        //System.out.println(sql);
		ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
			String date = rs.getString("tradedate");
			String ohlcdata = rs.getString("ohlc");
			String[] oh = null;
			oh = ohlcdata.split(",");
			niftyTradeDates.add(date);
			niftyHighs.add(oh[1]);
			niftyLows.add(oh[2]);
			niftyCloses.add(oh[3]);
			niftyVolumes.add(oh[4]);
			++totalRecords;
        }
        ++niftyCount;
        connection.clearWarnings();
        connection.close();
    }
    catch (ClassNotFoundException ex) {
        System.err.println("ClassNotFoundException");
    }
    catch (SQLException ex) {
        System.err.println("SQLException");
        ex.printStackTrace();
    }
 }

  public XYDataset createNiftyLowsDataset(String[] ar) {
		int sc = 0, niftyCount = 0;
		TimeSeriesCollection localTimeSeriesCollection = new TimeSeriesCollection();
        for(int k = 0; k < ar.length ; k++) localTimeSeriesCollection.addSeries(createNiftyLowsTimeSeries(ar[k]));
    return localTimeSeriesCollection;
  }
  public XYDataset createNiftyClosesDataset(String[] ar) {
		int sc = 0, niftyCount = 0;
		TimeSeriesCollection localTimeSeriesCollection = new TimeSeriesCollection();
        for(int k = 0; k < ar.length ; k++) localTimeSeriesCollection.addSeries(createNiftyClosesTimeSeries(ar[k]));
    return localTimeSeriesCollection;
  }
  public XYDataset createNiftyVolumesDataset(String[] ar) {
		int sc = 0, niftyCount = 0;
		TimeSeriesCollection localTimeSeriesCollection = new TimeSeriesCollection();
        for(int k = 0; k < ar.length ; k++) localTimeSeriesCollection.addSeries(createNiftyVolumesTimeSeries(ar[k]));
    return localTimeSeriesCollection;
  }
 public TimeSeries createNiftyVolumesTimeSeries(String scrip)
 {
 	 	TimeSeries  localTimeSeries = new TimeSeries(scrip);
 	 	getNifty("nse_" + scrip, 0);
		for(int i = 0; i <totalRecords; i++) {
		    String[] dts = null;
			String tdates;
			tdates = (String)niftyTradeDates.get(i);
			String lowv;
			lowv = (String)niftyVolumes.get(i);
		    dts = tdates.split("-");
		    int mon = Integer.valueOf(dts[1]), dayn = Integer.valueOf(dts[2]), yr = Integer.valueOf(dts[0]);
		    double low = Double.valueOf(lowv).doubleValue();
			localTimeSeries.add(new Day(dayn, mon, yr), low);
		}
	    //System.out.println(niftyTradeDates);
		//System.out.println(niftyLows);
		niftyTradeDates.removeAllElements();
		niftyLows.removeAllElements();
		niftyCloses.removeAllElements();
		niftyVolumes.removeAllElements();
	 return  localTimeSeries;
 }
  public TimeSeries createNiftyClosesTimeSeries(String scrip)
 {
 	 	TimeSeries  localTimeSeries = new TimeSeries(scrip);
 	 	getNifty("nse_" + scrip, 0);
		for(int i = 0; i <totalRecords; i++) {
		    String[] dts = null;
			String tdates;
			tdates = (String)niftyTradeDates.get(i);
			String lowv;
			lowv = (String)niftyCloses.get(i);
		    dts = tdates.split("-");
		    int mon = Integer.valueOf(dts[1]), dayn = Integer.valueOf(dts[2]), yr = Integer.valueOf(dts[0]);
		    double low = Double.valueOf(lowv).doubleValue();
			localTimeSeries.add(new Day(dayn, mon, yr), low);
		}
	    //System.out.println(niftyTradeDates);
		//System.out.println(niftyLows);
		niftyTradeDates.removeAllElements();
		niftyLows.removeAllElements();
		niftyCloses.removeAllElements();
		niftyVolumes.removeAllElements();
	 return  localTimeSeries;
 }
  public TimeSeries createNiftyLowsTimeSeries(String scrip)
 {
 	 	TimeSeries  localTimeSeries = new TimeSeries(scrip);
 	 	getNifty("nse_" + scrip, 0);
		for(int i = 0; i <totalRecords; i++) {
		    String[] dts = null;
			String tdates;
			tdates = (String)niftyTradeDates.get(i);
			String lowv;
			lowv = (String)niftyLows.get(i);
		    dts = tdates.split("-");
		    int mon = Integer.valueOf(dts[1]), dayn = Integer.valueOf(dts[2]), yr = Integer.valueOf(dts[0]);
		    double low = Double.valueOf(lowv).doubleValue();
			localTimeSeries.add(new Day(dayn, mon, yr), low);
		}
	    //System.out.println(niftyTradeDates);
		//System.out.println(niftyLows);
		niftyTradeDates.removeAllElements();
		niftyLows.removeAllElements();
		niftyCloses.removeAllElements();
		niftyVolumes.removeAllElements();
	 return  localTimeSeries;
 }



  public void insertBseEquityCsvFileToDatabase(String fileName)
  {
     try
     {
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String strLine = "";

            String[] result = null;
            String d =new String(fileName.substring(2,4));
            String m =new String(fileName.substring(4,6));
            String y =new String(fileName.substring(6,8));
            y = "20" + y;
            String myDate =  y + m + d;
            int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + BSE_DB, "root","deepak");
            Statement statement = connection.createStatement();

            while( (strLine = br.readLine()) != null)
            {
                result = strLine.split(",");
                if(count > 0)
                {
                        String sname,o,h,l,c,v;
                        sname = new String(result[1].replaceAll("\\W","").trim()); //+ "_" + result[0].replaceAll("\\W","").trim() + "_" + result[2].replaceAll("\\W","").trim() + "_" + result[3].replaceAll("\\W","").trim());
                        o = new String(result[4].replaceAll("\"","").trim());
                        h = new String(result[5].replaceAll("\"","").trim());
                        l = new String(result[6].replaceAll("\"","").trim());
                        c = new String(result[7].replaceAll("\"","").trim());
                        v = new String(result[11].replaceAll("\"","").trim());
                        String sql1 = "CREATE TABLE IF NOT EXISTS BSE_"+ sname + "(id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                        statement.executeUpdate(sql1);
                        if (h.equals(l) ) { o = c; h = c ; l = c; }
                        String sql2 = "INSERT INTO BSE_"+ sname + " (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + o + "," + h + "," + l + "," + c + "," + v + "')";
                        statement.executeUpdate(sql2);
                        System.out.println(sname);
                }
                ++count;

            }
         connection.close();
         br.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading csv file: " + e);
        }
  }
  public void insertNiftyCsvFileToDatabase(String fileName)
  {
     try
     {
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String strLine = "";
            String[] columns = null;
            int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + NSE_DB, "root","deepak");
            Statement statement = connection.createStatement();

            while( (strLine = br.readLine()) != null)
            {
                columns = strLine.split(",");
                if((count > 0) && (count <= 4000))
                {
                        String date1, o, h, l, c, t;
                        date1 = new String(columns[0].replaceAll("\"","").trim());;
                        String d =new String(date1.substring(0,2));
                        String m1 =new String(date1.substring(3,6).toLowerCase());
                        String m = "";
                        if(m1.equals("jan")) m = "01";
                        else if(m1.equals("jan")) m = "01";
                        else if(m1.equals("feb")) m = "02";
                        else if(m1.equals("mar")) m = "03";
                        else if(m1.equals("apr")) m = "04";
                        else if(m1.equals("may")) m = "05";
                        else if(m1.equals("jun")) m = "06";
                        else if(m1.equals("jul")) m = "07";
                        else if(m1.equals("aug")) m = "08";
                        else if(m1.equals("sep")) m = "09";
                        else if(m1.equals("oct")) m = "10";
                        else if(m1.equals("nov")) m = "11";
                        else if(m1.equals("dec")) m = "12";

                        String y =new String(date1.substring(7));
                        String myDate =  y + "-" + m + "-" + d;

                        o = new String(columns[1].replaceAll("\"","").trim());
                        h = new String(columns[2].replaceAll("\"","").trim());
                        l = new String(columns[3].replaceAll("\"","").trim());
                        c = new String(columns[4].replaceAll("\"","").trim());
                        t = new String(columns[5].replaceAll("\"","").trim());
                        String sql1 = "CREATE TABLE IF NOT EXISTS nse_nifty (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                        statement.executeUpdate(sql1);
                        if (h.equals(l) ) { o = c; h = c ; l = c; }
                        String sql2 = "INSERT INTO nse_nifty (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + o + "," + h + "," + l + "," + c + "," + t + "')";
                        statement.executeUpdate(sql2);
                        System.out.println("Nifty " + myDate + " Inserted");

                }
                ++count;

            }
         connection.close();
         br.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading csv file: " + e);
        }
  }
  
  public void insertNseEquityCsvFileToDatabase(String fileName)
  {
     try
     {
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String strLine = "";
            String[] columns = null;
            int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + NSE_DB, "root","deepak");
            Statement statement = connection.createStatement();

            while( (strLine = br.readLine()) != null)
            {
                columns = strLine.split(",");
                if(count > 0)
                {
                        String date1, o, h, l, c, oi;
                        date1 = new String(fileName.replaceAll("\"","").trim());;
                        String d =new String(date1.substring(2,4));
                        String m1 =new String(date1.substring(4,7).toLowerCase());
                        String m = "";
                        if(m1.equals("jan")) m = "01";
                        else if(m1.equals("jan")) m = "01";
                        else if(m1.equals("feb")) m = "02";
                        else if(m1.equals("mar")) m = "03";
                        else if(m1.equals("apr")) m = "04";
                        else if(m1.equals("may")) m = "05";
                        else if(m1.equals("jun")) m = "06";
                        else if(m1.equals("jul")) m = "07";
                        else if(m1.equals("aug")) m = "08";
                        else if(m1.equals("sep")) m = "09";
                        else if(m1.equals("oct")) m = "10";
                        else if(m1.equals("nov")) m = "11";
                        else if(m1.equals("dec")) m = "12";

                        String y =new String(date1.substring(7,11));
                        String myDate =  y + "-" + m + "-" + d;

                        o = new String(columns[2].replaceAll("\"","").trim());
                        h = new String(columns[3].replaceAll("\"","").trim());
                        l = new String(columns[4].replaceAll("\"","").trim());
                        c = new String(columns[5].replaceAll("\"","").trim());
                        oi = new String(columns[8].replaceAll("\"","").trim());
                        String scripName = new  String((columns[0].replaceAll("\\W","")) + "_" + (columns[1].replaceAll("\\W","")) );

                         String sql1 = "CREATE TABLE IF NOT EXISTS nse_" + scripName + " (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                         statement.executeUpdate(sql1);
                         if (h.equals(l) ) { o = c; h = c ; l = c; }
                         String sql2 = "INSERT INTO nse_" + scripName + " (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + o + "," + h + "," + l + "," + c + "," + oi + "')";
                         statement.executeUpdate(sql2);
                         System.out.println(scripName);
                }
                ++count;

            }
         connection.close();
         br.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading csv file: " + e);
        }
  }
  
  public void insertNseFutCsvFileToDatabase(String fileName)
  {
     try
     {
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String strLine = "";
            String[] columns = null;
            int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + NSEFUT_DB, "root","deepak");
            Statement statement = connection.createStatement();

            while( (strLine = br.readLine()) != null)
            {
                columns = strLine.split(",");
                if(count > 0)
                {
                        String date1, o, h, l, c, oi, optmmm1="", optmmm2="", optmmm3="", contract1="", contract2="", contract3="";
                        date1 = new String(fileName.substring(0,12).replaceAll("\"","").trim());;
                        String d =new String(date1.substring(2,4));
                        String m1 =new String(date1.substring(4,7).toLowerCase());
                        String m = "";
                        String y =new String(date1.substring(7,11));
                        int currentYear = Integer.valueOf(y), nextYear = Integer.valueOf(y);
                        if(m1.equals("jan")) {m = "01"; optmmm1 = "01"; optmmm2 = "02"; optmmm3 = "03";}
                        else if(m1.equals("feb")) {m = "02"; optmmm1 = "02"; optmmm2 = "03"; optmmm3 = "04";}
                        else if(m1.equals("mar")) {m = "03"; optmmm1 = "03"; optmmm2 = "04"; optmmm3 = "05";}
                        else if(m1.equals("apr")) {m = "04"; optmmm1 = "04"; optmmm2 = "05"; optmmm3 = "06";}
                        else if(m1.equals("may")) {m = "05"; optmmm1 = "05"; optmmm2 = "06"; optmmm3 = "07";}
                        else if(m1.equals("jun")) {m = "06"; optmmm1 = "06"; optmmm2 = "07"; optmmm3 = "08";}
                        else if(m1.equals("jul")) {m = "07"; optmmm1 = "07"; optmmm2 = "08"; optmmm3 = "09";}
                        else if(m1.equals("aug")) {m = "08"; optmmm1 = "08"; optmmm2 = "09"; optmmm3 = "10";}
                        else if(m1.equals("sep")) {m = "09"; optmmm1 = "09"; optmmm2 = "10"; optmmm3 = "11";}
                        else if(m1.equals("oct")) {m = "10"; optmmm1 = "10"; optmmm2 = "11"; optmmm3 = "12";}
                        else if(m1.equals("nov")) {m = "11"; optmmm1 = "11"; optmmm2 = "12"; optmmm3 = "01"; ++nextYear;}
                        else if(m1.equals("dec")) {m = "12"; optmmm1 = "12"; optmmm2 = "01"; optmmm3 = "02"; ++nextYear;}

                        String myDate =  y + "-" + m + "-" + d;
                        if(optmmm1.equals("11"))
                        {
                         contract1 =  String.valueOf(currentYear) + "_" + optmmm1;
                         contract2 =  String.valueOf(currentYear) + "_" + optmmm2;
                         contract3 =  String.valueOf(nextYear) + "_" + optmmm3;
                        }
                        else if(optmmm1.equals("12"))
                        {
                         contract1 =  String.valueOf(currentYear) + "_" + optmmm1;
                         contract2 =  String.valueOf(nextYear) + "_" + optmmm2;
                         contract3 =  String.valueOf(nextYear) + "_" + optmmm3;
                        }
                        else if(optmmm1.equals("01") || optmmm1.equals("02") || optmmm1.equals("03") ||  optmmm1.equals("04") ||  optmmm1.equals("05") ||  optmmm1.equals("06") ||  optmmm1.equals("07") || optmmm1.equals("08") ||  optmmm1.equals("09") ||  optmmm1.equals("10"))
                        {
                         contract1 =  String.valueOf(currentYear) + "_" + optmmm1;
                         contract2 =  String.valueOf(currentYear) + "_" + optmmm2;
                         contract3 =  String.valueOf(currentYear) + "_" + optmmm3;
                        }
                        o = new String(columns[5].replaceAll("\"","").trim());
                        h = new String(columns[6].replaceAll("\"","").trim());
                        l = new String(columns[7].replaceAll("\"","").trim());
                        c = new String(columns[8].replaceAll("\"","").trim());
                        oi = new String(columns[12].replaceAll("\"","").trim());

                        String cy = null, cm = null, cmt = null, cd = null, temp = columns[2].replaceAll("\\W","").toLowerCase();
                        cy =  temp.substring(5);
                        cmt =  temp.substring(2,5);

                        if(cmt.equals("jan")) cm = "01";
                        else if(cmt.equals("jan")) cm = "01";
                        else if(cmt.equals("feb")) cm = "02";
                        else if(cmt.equals("mar")) cm = "03";
                        else if(cmt.equals("apr")) cm = "04";
                        else if(cmt.equals("may")) cm = "05";
                        else if(cmt.equals("jun")) cm = "06";
                        else if(cmt.equals("jul")) cm = "07";
                        else if(cmt.equals("aug")) cm = "08";
                        else if(cmt.equals("sep")) cm = "09";
                        else if(cmt.equals("oct")) cm = "10";
                        else if(cmt.equals("nov")) cm = "11";
                        else if(cmt.equals("dec")) cm = "12";

                        cd =  temp.substring(0,2);

                        String scripName;
                        if(columns[0].substring(0,1).equals("F"))
                        {
                            scripName = new  String((columns[0].replaceAll("\\W","").substring(0,1)) + "_" + (columns[1].replaceAll("\\W","")) + "_" + cy + "_" + cm + "_" + cd );
                        }
                        else
                        {
                            scripName = new  String((columns[0].replaceAll("\\W","").substring(0,1)) + "_" + (columns[1].replaceAll("\\W","")) + "_" + cy + "_" + cm + "_" + cd + "_" + (columns[3].replaceAll("\\W","")) + "_" + (columns[4].replaceAll("\\W","")));
                        }
                        if(columns[0].substring(0,1).equals("F") || ( scripName.substring(0, 7).equalsIgnoreCase("O_NIFTY") && (scripName.substring(8,15).equals(contract1) || scripName.substring(8,15).equals(contract2) || scripName.substring(8,15).equals(contract3)) ) )
                        {

                         String sql1 = "CREATE TABLE IF NOT EXISTS nsefno_" + scripName + " (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                         statement.executeUpdate(sql1);
                         if (h.equals(l) ) { o = c; h = c ; l = c; }
                         String sql2 = "INSERT INTO nsefno_" + scripName + " (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + o + "," + h + "," + l + "," + c + "," + oi + "')";
                         statement.executeUpdate(sql2);
                         System.out.println(scripName);
                        }
                }
                ++count;

            }
         connection.close();
         br.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading csv file: " + e);
        }
  }
  public void insertNseOptCsvFileToDatabase(String fileName)
  {
     try
     {
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String strLine = "";
            String[] columns = null;
            int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + NSEOPT_DB, "root","deepak");
            Statement statement = connection.createStatement();

            while( (strLine = br.readLine()) != null)
            {
                columns = strLine.split(",");
                if(count > 0)
                {
                        String date1, o, h, l, c, oi, optmmm1="", optmmm2="", optmmm3="", contract1="", contract2="", contract3="";
                        date1 = new String(fileName.substring(0,12).replaceAll("\"","").trim());;
                        String d =new String(date1.substring(2,4));
                        String m1 =new String(date1.substring(4,7).toLowerCase());
                        String m = "";
                        String y =new String(date1.substring(7,11));
                        int currentYear = Integer.valueOf(y), nextYear = Integer.valueOf(y);
                        if(m1.equals("jan")) {m = "01"; optmmm1 = "01"; optmmm2 = "02"; optmmm3 = "03";}
                        else if(m1.equals("feb")) {m = "02"; optmmm1 = "02"; optmmm2 = "03"; optmmm3 = "04";}
                        else if(m1.equals("mar")) {m = "03"; optmmm1 = "03"; optmmm2 = "04"; optmmm3 = "05";}
                        else if(m1.equals("apr")) {m = "04"; optmmm1 = "04"; optmmm2 = "05"; optmmm3 = "06";}
                        else if(m1.equals("may")) {m = "05"; optmmm1 = "05"; optmmm2 = "06"; optmmm3 = "07";}
                        else if(m1.equals("jun")) {m = "06"; optmmm1 = "06"; optmmm2 = "07"; optmmm3 = "08";}
                        else if(m1.equals("jul")) {m = "07"; optmmm1 = "07"; optmmm2 = "08"; optmmm3 = "09";}
                        else if(m1.equals("aug")) {m = "08"; optmmm1 = "08"; optmmm2 = "09"; optmmm3 = "10";}
                        else if(m1.equals("sep")) {m = "09"; optmmm1 = "09"; optmmm2 = "10"; optmmm3 = "11";}
                        else if(m1.equals("oct")) {m = "10"; optmmm1 = "10"; optmmm2 = "11"; optmmm3 = "12";}
                        else if(m1.equals("nov")) {m = "11"; optmmm1 = "11"; optmmm2 = "12"; optmmm3 = "01"; ++nextYear;}
                        else if(m1.equals("dec")) {m = "12"; optmmm1 = "12"; optmmm2 = "01"; optmmm3 = "02"; ++nextYear;}

                        String myDate =  y + "-" + m + "-" + d;
                        if(optmmm1.equals("11"))
                        {
                         contract1 =  String.valueOf(currentYear) + "_" + optmmm1;
                         contract2 =  String.valueOf(currentYear) + "_" + optmmm2;
                         contract3 =  String.valueOf(nextYear) + "_" + optmmm3;
                        }
                        else if(optmmm1.equals("12"))
                        {
                         contract1 =  String.valueOf(currentYear) + "_" + optmmm1;
                         contract2 =  String.valueOf(nextYear) + "_" + optmmm2;
                         contract3 =  String.valueOf(nextYear) + "_" + optmmm3;
                        }
                        else if(optmmm1.equals("01") || optmmm1.equals("02") || optmmm1.equals("03") ||  optmmm1.equals("04") ||  optmmm1.equals("05") ||  optmmm1.equals("06") ||  optmmm1.equals("07") || optmmm1.equals("08") ||  optmmm1.equals("09") ||  optmmm1.equals("10"))
                        {
                         contract1 =  String.valueOf(currentYear) + "_" + optmmm1;
                         contract2 =  String.valueOf(currentYear) + "_" + optmmm2;
                         contract3 =  String.valueOf(currentYear) + "_" + optmmm3;
                        }
                        o = new String(columns[5].replaceAll("\"","").trim());
                        h = new String(columns[6].replaceAll("\"","").trim());
                        l = new String(columns[7].replaceAll("\"","").trim());
                        c = new String(columns[8].replaceAll("\"","").trim());
                        oi = new String(columns[12].replaceAll("\"","").trim());

                        String cy = null, cm = null, cmt = null, cd = null, temp = columns[2].replaceAll("\\W","").toLowerCase();
                        cy =  temp.substring(5);
                        cmt =  temp.substring(2,5);

                        if(cmt.equals("jan")) cm = "01";
                        else if(cmt.equals("jan")) cm = "01";
                        else if(cmt.equals("feb")) cm = "02";
                        else if(cmt.equals("mar")) cm = "03";
                        else if(cmt.equals("apr")) cm = "04";
                        else if(cmt.equals("may")) cm = "05";
                        else if(cmt.equals("jun")) cm = "06";
                        else if(cmt.equals("jul")) cm = "07";
                        else if(cmt.equals("aug")) cm = "08";
                        else if(cmt.equals("sep")) cm = "09";
                        else if(cmt.equals("oct")) cm = "10";
                        else if(cmt.equals("nov")) cm = "11";
                        else if(cmt.equals("dec")) cm = "12";

                        cd =  temp.substring(0,2);

                        String scripName;
                        if(columns[0].substring(0,1).equals("F"))
                        {
                            scripName = new  String((columns[0].replaceAll("\\W","").substring(0,1)) + "_" + (columns[1].replaceAll("\\W","")) + "_" + cy + "_" + cm + "_" + cd );
                        }
                        else
                        {
                            scripName = new  String((columns[0].replaceAll("\\W","").substring(0,1)) + "_" + (columns[1].replaceAll("\\W","")) + "_" + cy + "_" + cm + "_" + cd + "_" + (columns[3].replaceAll("\\W","")) + "_" + (columns[4].replaceAll("\\W","")));
                        }
                        if(!(oi.equals("0")) && columns[0].substring(0,1).equals("O") /*&& !( scripName.substring(0, 7).equalsIgnoreCase("O_NIFTY"))*/ )
                        {

                         String sql1 = "CREATE TABLE IF NOT EXISTS nseopt_" + scripName + " (id int(11) auto_increment, tradedate date, ohlc varchar(500),PRIMARY KEY(id))";
                         //System.out.println(sql1);
                         statement.executeUpdate(sql1);
                         if (h.equals(l) ) { o = c; h = c ; l = c; }
                         String sql2 = "INSERT INTO nseopt_" + scripName + " (id, tradedate, ohlc) VALUES (null,'"  + myDate +  "','" + o + "," + h + "," + l + "," + c + "," + oi + "')";
                         //System.out.println(sql2);
                         statement.executeUpdate(sql2);
                         System.out.println(scripName);
                        }
                }
                ++count;

            }
         connection.close();
         br.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading csv file: " + e);
        }
  }


}
