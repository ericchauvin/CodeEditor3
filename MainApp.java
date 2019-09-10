// Copyright Eric Chauvin 2019.




///////////////////////////////
// javax.media.j3d.Canvas3D
//////////////////////////////


import javax.swing.SwingUtilities;



public class MainApp implements Runnable
  {
  public static final String versionDate = "9/10/2019";
  private MainWindow mainWin;
  public ConfigureFile mainConfigFile;
  public ConfigureFile projectConfigFile;
  private String[] argsArray;



  public static void main( String[] args )
    {
    MainApp mApp = new MainApp( args );
    SwingUtilities.invokeLater( mApp );
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


    // argsArray
    // Pass this program's directory as a parameter from
    // the batch file.

    String programDirectory = "C:\\Eric\\CodeEditorJava\\";
    String mainConfigFileName = programDirectory +
                                      "MainConfigure.txt";

    mainConfigFile = new ConfigureFile( this,
                                mainConfigFileName );

    String currentProjectFileName =
            mainConfigFile.getString( "CurrentProjectFile" );

    if( currentProjectFileName.length() < 4 )
      {
      currentProjectFileName = programDirectory +
                                     "ProjectOptions.txt";

      mainConfigFile.setString( "CurrentProjectFile",
                                 currentProjectFileName,
                                 true );

      mainConfigFile.writeToTextFile();
      }

    projectConfigFile = new ConfigureFile( this,
                             currentProjectFileName );

    mainWin = new MainWindow( this, "Code Editor" );
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
