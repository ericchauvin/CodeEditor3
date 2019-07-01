// Copyright Eric Chauvin 2019.


// Highlight colors?
// https://docs.oracle.com/javase/7/docs/api/javax/swing/JTextArea.html


// https://docs.oracle.com/javase/tutorial/essential/io/index.html


// https://docs.oracle.com/javase/tutorial/essential/io/


// static int parseInt(String s)



import javax.swing.JTextArea;




  public class EditorTabPage
  {
  private MainApp mApp;
  private String fileName = "";
  private String tabTitle = "";
  private JTextArea mainTextArea;
  private int searchPosition = -1;



  public EditorTabPage( MainApp useApp,
                        String setTabTitle,
                        String setFileName,
                        JTextArea setTextArea )
    {
    mApp = useApp;
    fileName = setFileName;
    tabTitle = setTabTitle;
    mainTextArea = setTextArea;
    }




  public void ReadFromTextFile()
    {
    try
    {
    String fileS = FileUtility.ReadAsciiFileToString( mApp,
                                                      fileName );

    if( fileS == "" )
      {
      mainTextArea.setText( "" );
      return;
      }

    fileS = fileS.trim();
    fileS = fileS + "\n";

    StringBuilder sBuilder = new StringBuilder();
    String[] lines = fileS.split( "\n" );
    for( int count = 0; count < lines.length; count++ )
      {
      String oneLine = StringsUtility.trimEnd( lines[count] );
      sBuilder.append( oneLine + "\n" );
      }

    mainTextArea.setText( sBuilder.toString() );
    // mainTextArea.append( toShow + "\n" );

    }
    catch( Exception e )
      {
      mApp.showStatus( "Could not read the file: \n" + fileName );
      mApp.showStatus( e.getMessage() );
      }
    }




/*
  internal bool WriteToTextFile()
    {
    try
    {
    MForm.ShowStatus( "Saving: " + FileName );

    using( StreamWriter SWriter = new StreamWriter( FileName, false, Encoding.UTF8 ))
      {
      string[] Lines = MainTextBox.Lines;

      foreach( string Line in Lines )
        {
        // SWriter.WriteLine( Line.TrimEnd() + "\r\n" );
        SWriter.WriteLine( Line.TrimEnd() );
        }

      // SWriter.WriteLine( " " );
      }

    return true;
    }
    catch( Exception Except )
      {
      MForm.ShowStatus( "Could not write to the file:" );
      MForm.ShowStatus( FileName );
      MForm.ShowStatus( Except.Message );
      return false;
      }
    }
*/



  }
