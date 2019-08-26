// Copyright Eric Chauvin 2019.




public class ConfigureFile
  {
  private MainApp mApp;
  private StringDictionary strDictionary;
  private String fileName;




  public ConfigureFile( MainApp useApp, String fileToUseName )
    {
    mApp = useApp;

    fileName = fileToUseName;
    strDictionary = new StringDictionary( mApp, false );
    readFromTextFile();
    }



  public String getString( String keyWord )
    {
    keyWord = keyWord.trim().toLowerCase();
    return strDictionary.getString( keyWord );
    }



  public void setString( String keyWord,
                         String value,
                         boolean writeFile )
    {
    if( keyWord == "" )
      {
      // MForm.ShowStatus( "Can't add an empty keyword to the dictionary in ConfigureFile.cs." );
      return;
      }

    // The StringDictionary is not case-sensitive, but
    // this makes all keys lower case.
    keyWord = keyWord.trim().toLowerCase();
    strDictionary.setString( keyWord, value );

    if( writeFile )
      writeToTextFile();

    }




  public void readFromTextFile()
    {
    try
    {
    mApp.showStatus( "Reading: " + fileName );
    strDictionary.clear();

    String fileS = FileUtility.readAsciiFileToString( mApp,
                                                      fileName,
                                                      true );

    mApp.showStatus( "fileS: " + fileS );

    if( fileS.length() < 2 )
      return;

    String[] lines = fileS.split( "\n" );
    for( int count = 0; count < lines.length; count++ )
      {
      String oneLine = lines[count];
      mApp.showStatus( "oneLine: " + oneLine );
      if( oneLine.length() < 3 )  // key, tab, value.
        continue;

      mApp.showStatus( "Before lineParts" );
      String[] lineParts = oneLine.split( "\t" );
      mApp.showStatus( "lineParts.length: " + lineParts.length );

      if( lineParts.length < 2 )
        continue;

      mApp.showStatus( "Before key" );
      String key = lineParts[0].trim();
      if( key.length() < 1 )
        continue;

      String value = lineParts[1];
      mApp.showStatus( "value: " + value );
      // KeyWord = KeyWord.Replace( "\"", "" );
      // Value = Value.Replace( "\"", "" );

      strDictionary.setString( key, value );
      }

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
    String fileStr = strDictionary.makeKeysValuesString();
    mApp.showStatus( "fileStr: " + fileStr );
    if( fileStr.trim().length() < 1 )
      return;

    FileUtility.writeAsciiStringToFile( mApp,
                                        fileName,
                                        fileStr,
                                        true );

    }
    catch( Exception e )
      {
      mApp.showStatus( "Could not write the file: \n" + fileName );
      mApp.showStatus( e.getMessage() );
      }
    }




  /*
  private int GetIntegerValue( string Key )
    {
    try
    {
    return Int32.Parse( Config.GetString( Key ));
    }
    catch( Exception ) // Except )
      {
      // MForm.ShowStatus( "Exception in GetIntegerValue():" );
      // MForm.ShowStatus( Except.Message );
      return -1;
      }
    }
    */


  /*
  private void SetIntegerValue( string Key, int ToSet )
    {
    SetString( Key, ToSet.ToString() );
    WriteToTextFile();
    }
    */



  }
