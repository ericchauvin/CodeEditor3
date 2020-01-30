// Copyright Eric Chauvin 2019 - 2020.


// An identifier name like Table is different from the
// name table, so this dictionary needs to be
// case-sensitive.  But when it's sorted, it is a
// case-insensitive sort.  So the words Table and
// table would be considered equal in the sort order.
// But ConfigureFile.java, for example, sets all its
// keys to lower case before putting them in here.
// So ConfigureFile is case-insensitive for its keys.



public class StringDictionary
  {
  private MainApp mApp;
  private int keySize = 127;
  private StringDictionaryLine lineArray[];
  private final int maxIndexLetter = 'z' - 'a';
  private final int smallKeySize = maxIndexLetter + 1;
  private final int bigKeySize = ((maxIndexLetter << 6) |
                                  maxIndexLetter) + 1;




  private StringDictionary()
    {
    }



  public StringDictionary( MainApp useApp,
                           boolean useBigKeySize )
    {
    mApp = useApp;

    if( useBigKeySize )
      keySize = bigKeySize;
    else
      keySize = smallKeySize;

    lineArray = new StringDictionaryLine[keySize];
    }



  public void clear()
    {
    for( int count = 0; count < keySize; count++ )
      lineArray[count] = null;

    }


  // https://docs.oracle.com/javase/7/docs/api/java/lang/Character.html

  private int letterToIndexNumber( char letter )
    {
    letter = Character.toLowerCase( letter );
    int index = letter - 'a';
    if( index < 0 )
      index = 0;

    if( index > maxIndexLetter )
      index = maxIndexLetter;

    return index;
    }



  private int getIndex( String key )
    {
    // This index needs to be in sorted order.

    int keyLength = key.length();
    if( keyLength < 1 )
      return 0;

    int one = letterToIndexNumber( key.charAt( 0 ));
    if( keyLength < 2 )
      return one;

    if( keySize == smallKeySize )
      return one;

    int tempTwo = letterToIndexNumber( key.charAt( 1 ));
    int two = (one << 6) | tempTwo;

    if( two >= bigKeySize )
      two = bigKeySize - 1;

    return two;
    }




  public void setString( String key, String value )
    {
    if( key == null )
      return;

    key = key.trim();
    if( key.length() < 1 )
      return;

    int index = getIndex( key );

    if( lineArray[index] == null )
      lineArray[index] = new StringDictionaryLine();

    lineArray[index].setString( key, value );
    }



  public String getString( String key )
    {
    if( key == null )
      return "";

    key = key.trim();
    if( key.length() < 1 )
      return "";

    int index = getIndex( key );
    if( lineArray[index] == null )
      return "";

    return lineArray[index].getString( key );
    }



  public void sort()
    {
    for( int count = 0; count < keySize; count++ )
      {
      if( lineArray[count] == null )
        continue;

      lineArray[count].sort();
      }
    }



  public String makeKeysValuesString()
    {
    try
    {
    // mApp.showStatus( "Sorting..." );
    sort();
    // mApp.showStatus( "Finished sorting." );

    StringBuilder sBuilder = new StringBuilder();

    for( int count = 0; count < keySize; count++ )
      {
      if( lineArray[count] == null )
        continue;

      sBuilder.append( lineArray[count].
                                makeKeysValuesString());

      }

    return sBuilder.toString();

    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in StringDictionary.makeKeysValuesString():\n" );
      mApp.showStatus( e.getMessage() );
      return "";
      }
    }




  }
