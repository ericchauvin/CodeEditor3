// Copyright Eric Chauvin 2019.




public class ConfigureFile
  {
  private MainApp mApp;
  private StringDictionary strDictionary;
  private String fileName;




  public ConfigureFile( String fileToUseName,  MainApp useApp )
    {
    mApp = useApp;

    fileName = fileToUseName;
    strDictionary = new StringDictionary( false );

    readFromTextFile();
    }


/*
  internal string GetString( string KeyWord )
    {
    key = key.toLowerCase();


    keyWord = keyWord.trim().toLowerCase();
    strDictionary.setString( keyWord, value );

    strDictionary.getString( keyWord );

    string Value;
    if( CDictionary.TryGetValue( KeyWord, out Value ))
      return Value;
    else
      return "";

    }
*/




  public void setString( String keyWord, String value,
                                         boolean writeFile )
    {
    if( keyWord == "" )
      {
      // MForm.ShowStatus( "Can't add an empty keyword to the dictionary in ConfigureFile.cs." );
      return;
      }

    keyWord = keyWord.trim().toLowerCase();
    strDictionary.setString( keyWord, value );

    if( writeFile )
      writeToTextFile();

    }





  private void readFromTextFile()
    {
    try
    {
    strDictionary.clear();

    String fileS = FileUtility.readAsciiFileToString( mApp,
                                                      fileName );
    if( fileS.length() < 2 )
      return;

    String[] lines = fileS.split( "\n" );
    for( int count = 0; count < lines.length; count++ )
      {
      String oneLine = lines[count];
      if( oneLine.length() < 3 )  // key, tab, value.
        continue;

      String[] lineParts = fileS.split( "\t" );
      if( lineParts.length < 2 )
        continue;

      String key = lineParts[0].trim();
      if( key.length() < 1 )
        continue;

      String value = lineParts[1];
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
    // mApp.showStatus( "Keys: " + keyS );

/*


    using( StreamWriter SWriter = new StreamWriter( FileName, false, Encoding.UTF8 ))
      {
      foreach( KeyValuePair<string, string> Kvp in CDictionary )
        {
        string Line = Kvp.Key + "\t" + Kvp.Value;
        // Line = AESEncrypt.EncryptString( Line );
        SWriter.WriteLine( Line );
        }

      SWriter.WriteLine( " " );
      }
*/

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

