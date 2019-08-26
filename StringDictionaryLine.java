// Copyright Eric Chauvin 2019.



// See notes in StringDictionary about this being
// case-sensitive.


public class StringDictionaryLine
  {
  private String[] keyArray;
  private String[] valueArray;
  private int[] sortIndexArray;
  private int arrayLast = 0;



  public StringDictionaryLine()
    {
    keyArray = new String[8];
    valueArray = new String[8];
    sortIndexArray = new int[8];
    resetSortIndexArray();
    }



  private void resetSortIndexArray()
    {
    // It's not to arrayLast.  It's to the whole length.
    int max = sortIndexArray.length;
    for( int count = 0; count < max; count++ )
      sortIndexArray[count] = count;

    }



  private void resizeArrays( int toAdd )
    {
    int max = sortIndexArray.length;
    sortIndexArray = new int[max + toAdd];
    resetSortIndexArray();

    String[] tempKeyArray = new String[max + toAdd];
    String[] tempValueArray = new String[max + toAdd];

    for( int count = 0; count < max; count++ )
      {
      tempKeyArray[count] = keyArray[count];
      tempValueArray[count] = valueArray[count];
      }

    keyArray = tempKeyArray;
    valueArray = tempValueArray;
    }




  public void sort()
    {
    if( arrayLast < 2 )
      return;

    // The worst case scenario for this Bubble Sort is
    // if the very last item had to "bubble up" from
    // the very bottom.  So the maximum number of outer
    // loops here is arrayLast number of times.
    for( int count = 0; count < arrayLast; count++ )
      {
      if( !bubbleSortOnePass() )
        break;

      }
    }



  private boolean bubbleSortOnePass()
    {
    // This returns true if it swaps anything.

    boolean switched = false;
    for( int count = 0; count < (arrayLast - 1); count++ )
      {
      // compareTo() uses case.
      if( keyArray[count].compareToIgnoreCase(
                              keyArray[count + 1] ) > 0 )
        {
        int temp = sortIndexArray[count];
        sortIndexArray[count] = sortIndexArray[count + 1];
        sortIndexArray[count + 1] = temp;
        switched = true;
        }
      }

    return switched;
    }



  private int getIndexOfKey( String key )
    {
    if( arrayLast < 1 )
      return -1;

    for( int count = 0; count < arrayLast; count++ )
      {
      // compareTo() uses case.
      if( keyArray[count].compareTo( key ) == 0 )
        return count;

      }

    return -1;
    }



  public void setString( String key, String value )
    {
    int index = getIndexOfKey( key );
    if( index >= 0 )
      {
      valueArray[index] = value;
      }
    else
      {
      if( arrayLast >= sortIndexArray.length )
        resizeArrays( 16 );

      keyArray[arrayLast] = key;
      valueArray[arrayLast] = value;
      arrayLast++;
      }
    }



  public String getString( String key )
    {
    int index = getIndexOfKey( key );
    if( index < 0 )
      return "";

    return valueArray[index];
    }



  public String makeKeysValuesString()
    {
    if( arrayLast < 1 )
      return "";

    StringBuilder sBuilder = new StringBuilder();

    for( int count = 0; count < arrayLast; count++ )
      {
      // Using the sortIndexArray for the sorted order.
      String oneLine = keyArray[sortIndexArray[count]] +
                       "\t" +
                       valueArray[sortIndexArray[count]] +
                       "\n";

      sBuilder.append( oneLine );
      }

    return sBuilder.toString();
    }



  }
