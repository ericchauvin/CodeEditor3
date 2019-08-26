// Copyright Eric Chauvin 2019.


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
  // The ASCII value of the space character is 32.
  private final int space = 32;
  private final int BiggestAsciiValue = 127 - space;
  private final int DoubleBiggestAsciiValue =
               (BiggestAsciiValue << 7) + BiggestAsciiValue;
  private final int smallKeySize = BiggestAsciiValue + 1;
  private final int bigKeySize = DoubleBiggestAsciiValue + 1;



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



  private int getIndex( String key )
    {
    // This index needs to be in sorted order.
    // There will be empty gaps in this.  There are lots
    // of different ways to optimize this one function
    // so that it's done a lot more efficiently to
    // avoid gaps.  But it depends on how you use it.
    // Like if you are only going to use it for ASCII
    // letters, etc.

    int keyLength = key.length();
    if( keyLength < 1 )
      return 0;

    int one = key.charAt( 0 );
    one -= space;
    if( one < 0 )
      one = 0;

    // For most things in the United States the non-ASCII
    // characters are pretty rare in text strings and so
    // this just lumps any bigger characters in to one
    // StringDictionaryLine at the end.
    if( one > BiggestAsciiValue )
      one = BiggestAsciiValue;

    if( keyLength < 2 )
      return one;

    if( keySize == smallKeySize )
      return one;

    int tempTwo = key.charAt( 1 );
    tempTwo -= space;
    if( tempTwo < 0 )
      tempTwo = 0;

    if( tempTwo > BiggestAsciiValue )
      tempTwo = BiggestAsciiValue;

    int two = one << 7;
    two = two | tempTwo;

    if( two > DoubleBiggestAsciiValue )
      two = DoubleBiggestAsciiValue;

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
    mApp.showStatus( "Sorting..." );
    sort();
    mApp.showStatus( "Finished sorting." );

    StringBuilder sBuilder = new StringBuilder();

    for( int count = 0; count < keySize; count++ )
      {
      if( lineArray[count] == null )
        continue;

      sBuilder.append( lineArray[count].makeKeysValuesString() );
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
