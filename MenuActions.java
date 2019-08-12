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



  public void actionPerformed( ActionEvent e )
    {
    String command = e.getActionCommand();
    mApp.showStatus( "ActionEvent Command is: " + command );

    if( command == "FileOpen" )
      {
      mApp.OpenFile();
      }

    if( command == "FileSaveAll" )
      {
      mApp.SaveAllFiles();
      }


    if( command == "HelpAbout" )
      {
      mApp.ShowAboutBox();
      }

    }



  }
