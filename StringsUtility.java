// Copyright Eric Chauvin 2019.




// float a = Float.parseFloat( SomeString );



  public class StringsUtility
  {



  public static String trimEnd( String in )
    {
    if( in.trim() == "" )
      return "";

    int max = in.length();
    if( max == 1 )
      return in;

    int markEnd = max;
    for( int count = max - 1; count >= 0; count-- )
      {
      char c = in.charAt( count );
      if( c != ' ' )
        {
        markEnd = count + 1;
        break;
        }
      }

    if( markEnd == max )
      return in;

    return in.substring( 0, markEnd );
    }






  /*
  // This is a Cyclic Redundancy Check (CRC) function.
  // CCITT is the international standards body.
  // This CRC function is translated from a magazine
  // article in Dr. Dobbs Journal.
  // By Bob Felice, June 17, 2007
  // But this is my translation of what was in that
  // article.  (It was written in C.)
  internal static uint GetCRC16( string InString )
    {
    // Different Polynomials can be used.
    uint Polynomial = 0x8408;
    uint crc = 0xFFFF;
    if( InString == null )
      return ~crc;

    if( InString.Length == 0 )
      return ~crc;

    uint data = 0;
    for( int Count = 0; Count < InString.Length; Count++ )
      {
      data = (uint)(0xFF & InString[Count] );
      // For each bit in the data byte.
      for( int i = 0; i < 8; i++ )
        {
        if( 0 != ((crc & 0x0001) ^ (data & 0x0001)) )
          crc = (crc >> 1) ^ Polynomial;
        else
          crc >>= 1;

        data >>= 1;
        }
      }

    crc = ~crc;
    data = crc;
    crc = (crc << 8) | ((data >> 8) & 0xFF);

    // Just make sure it's 16 bits.
    return crc & 0xFFFF;
    }
    */



  }
