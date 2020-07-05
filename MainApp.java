// Copyright Eric Chauvin 2019 - 2020.



// Code Editor 3.





import javax.swing.SwingUtilities;



public class MainApp implements Runnable
  {
  public static final String versionDate = "7/5/2020";
  private MainWindow mainWin;
  public ConfigureFile mainConfigFile;
  public ConfigureFile projectConfigFile;
  private String[] argsArray;
  private String programDirectory = "";



  public static void main( String[] args )
    {
    MainApp mApp = new MainApp( args );
    SwingUtilities.invokeLater( mApp );
    }



  public String getProgramDirectory()
    {
    return programDirectory;
    }



  @Override
  public void run()
    {
    setupProgram();
    }


  private MainApp()
    {
    }


  public MainApp( String[] args )
    {
    argsArray = args;
    }



  private void setupProgram()
    {
    // checkSingleInstance()

    // All programs need to have a batch file give
    // it the program directory so they're not stuck
    // in that directory.
    programDirectory = "\\EricMain\\CodeEditor3";

    int length = argsArray.length;
    if( length > 0 )
      programDirectory = argsArray[0];


    String mainConfigFileName = programDirectory +
                                  "\\MainConfigure.txt";

    mainConfigFile = new ConfigureFile( this,
                                mainConfigFileName );

    String currentProjectFileName =
            mainConfigFile.getString(
                              "CurrentProjectFile" );

    if( currentProjectFileName.length() < 4 )
      {
      currentProjectFileName = programDirectory +
                               "\\ProjectOptions.txt";

      mainConfigFile.setString( "CurrentProjectFile",
                               currentProjectFileName,
                               true );

      // mainConfigFile.writeToTextFile();
      }

    projectConfigFile = new ConfigureFile( this,
                             currentProjectFileName );

    mainWin = new MainWindow( this, "Code Editor 3" );
    mainWin.initialize();

    showStatus( " " );
    showStatus( "currentProjectFileName:\n" +
                          currentProjectFileName );

    showStatus( " " );
    showStatus( "argsArray length: " + length );
    for( int count = 0; count < length; count++ )
      showStatus( argsArray[count] );

    showStatus( " " );
    showStatus( "Program Directory:" );
    showStatus( programDirectory );
    }




  public void showStatus( String toShow )
    {
    if( mainWin == null )
      return;

    mainWin.showStatus( toShow );
    }



  public void clearStatus()
    {
    if( mainWin == null )
      return;

    mainWin.clearStatus();
    }




  }
