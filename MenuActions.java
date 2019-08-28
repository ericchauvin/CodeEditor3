// Copyright Eric Chauvin 2019.



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;



public class MenuActions implements ActionListener
  {
  private MainApp mApp;



  public MenuActions( MainApp useApp )
    {
    mApp = useApp;
    }



  public void actionPerformed( ActionEvent event )
    {
    // For most things this will be the outside try-catch
    // block.  In other words it catches anything not
    // otherwise caught somewhere else.
    try
    {

    String command = event.getActionCommand();
    mApp.showStatus( "ActionEvent Command is: " + command );

    //////////////
    // File Menu:
    if( command == "FileOpen" )
      {
      mApp.openFile();
      }

    if( command == "FileSaveAll" )
      {
      mApp.saveAllFiles();
      }

    if( command == "FileCloseCurrent" )
      {
      mApp.closeCurrentFile();
      }

    if( command == "FileExit" )
      {
      System.exit( 0 );
      }


    /////////////
    // Edit Menu:

    if( command == "EditCopy" )
      {
      mApp.editCopy();
      }




    //////////////
    // Help Menu:
    if( command == "HelpAbout" )
      {
      mApp.showAboutBox();
      }


    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in MenuActions.actionPerformed()." );
      mApp.showStatus( e.getMessage() );
      }

    }



  }
