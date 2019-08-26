// Copyright Eric Chauvin 2019.




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



  public String getFileName()
    {
    return fileName;
    }



  public void readFromTextFile()
    {
    try
    {
    String fileS = FileUtility.readAsciiFileToString( mApp,
                                                      fileName,
                                                      false );

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





  public void writeToTextFile()
    {
    try
    {
    mApp.showStatus( "Saving: " + fileName );
    String outString = mainTextArea.getText();

    FileUtility.writeAsciiStringToFile( mApp,
                                        fileName,
                                        outString,
                                        false );

    }
    catch( Exception e )
      {
      mApp.showStatus( "Could not write to the file: \n" + fileName );
      mApp.showStatus( e.getMessage() );
      }
    }




  }
