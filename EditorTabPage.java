// Copyright Eric Chauvin 2019.



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
    // ReadFromTextFile( fileName, true );
    }





/*
  private bool ReadFromTextFile( string FileName, bool AsciiOnly )
    {
    try
    {
    if( !File.Exists( FileName ))
      {
      // Might be adding a new file that doesn't
      // exist yet.
      MainTextBox.Text = "";
      return false;
      }

    StringBuilder SBuilder = new StringBuilder();
    using( StreamReader SReader = new StreamReader( FileName, Encoding.UTF8 ))
      {
      while( SReader.Peek() >= 0 )
        {
        string Line = SReader.ReadLine();
        if( Line == null )
          continue;

        Line = Line.Replace( "\t", "  " );
        Line = StringsEC.GetCleanUnicodeString( Line, 4000, false );
        Line = Line.TrimEnd(); // TrimStart()

        // if( Line == "" )
          // continue;

        SBuilder.Append( Line + "\r\n" );
        }
      }

    MainTextBox.Text = SBuilder.ToString();
    return true;
    }
    catch( Exception Except )
      {
      MForm.ShowStatus( "Could not read the file: \r\n" + FileName );
      MForm.ShowStatus( Except.Message );
      return false;
      }
    }
*/


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
