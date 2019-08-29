// Copyright Eric Chauvin 2019.



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class TimerActions implements ActionListener
  {
  private MainApp mApp;



  public TimerActions( MainApp useApp )
    {
    mApp = useApp;
    }



  public void actionPerformed( ActionEvent event )
    {
    try
    {
    mApp.keyboardTimerEvent();
    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in TimerActions.actionPerformed()." );
      mApp.showStatus( e.getMessage() );
      }
    }



  }
