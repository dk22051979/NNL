package com.pythonanywhere.brilliantcomputer;

/**
 *
 * @author deepakkumar
 */

  
  class RunApp implements Runnable
  {
    private final NewNoLimit objMoneyTree;    
    private final String database;
    private final String tablePrefix;
    private final String table;

    public RunApp(NewNoLimit objMoneyTree,  String database, String tablePrefix, String table)        //4.1
    {
      
      this.objMoneyTree = objMoneyTree;      
      this.database = database;
      this.tablePrefix = tablePrefix;
      this.table = table;
      
    }

   
    public void run()
    {

    }
  }