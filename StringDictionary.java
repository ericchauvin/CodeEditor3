// Copyright Eric Chauvin 2019.


// There is nothing in here that says keys have to be
// in lower case.



public class StringDictionary
  {
  private MainApp mApp;
  private int keySize = 127;
  private StringDictionaryLine lineArray[];
  // The ASCII value of the space character is 32.
  private final int space = 32;
  private final int BiggestAsciiValue = 126 - space;
  private final int DoubleBiggestAsciiValue =
               (BiggestAsciiValue << 7) + BiggestAsciiValue;
  private final int smallKeySize = BiggestAsciiValue + 1;
  private final int bigKeySize = DoubleBiggestAsciiValue + 1;




  public StringDictionary( boolean useBigKeySize )
    {
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
    // There will be empty gaps in this.
    // To do:
    // What's the best way to remove the empty gaps
    // and still keep it in sorted order?

    int keyLength = key.length();
    if( keyLength < 1 )
      return 0;

    // key = key.toLowerCase();  See setString().

    int one = key.charAt( 0 );
    one -= space;

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

    int two = one << 7;
    two = two | tempTwo;

    if( two > DoubleBiggestAsciiValue )
      two = DoubleBiggestAsciiValue;

    return two;
    }




  public void setString( String key, String value )
    {
    key = key.toLowerCase();
    int index = getIndex( key );

    //  lineArray[count] = null;


    }



  public String getString( String key )
    {
    if( key.length() < 1 )
      return "";


    int index = getIndex( key );
    if( lineArray[index] == null )
      return "";

    return lineArray[index].getString( key );
    }



  }

