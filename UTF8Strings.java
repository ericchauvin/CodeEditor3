// Copyright Eric Chauvin 2018 - 2020.



// Some programs add a byte order mark at the start
// of a file even though they shouldn't.  They are
// the three bytes: 0xEF, 0xBB, 0xBF
// Those three bytes encode the one character that
// is the Byte Order Mark.

// http://en.wikipedia.org/wiki/Unicode
// http://en.wikipedia.org/wiki/UTF-8
// http://en.wikipedia.org/wiki/Basic_Multilingual_Plane


  public class UTF8Strings
  {



  public static byte[] stringToBytes( String in )
    {
    if( in == null )
      return null;

    if( in.length() == 0 )
      return null;

    byte[] result;
    try
    {
    // Times 3 is the worst-case scenario.
    result = new byte[in.length() * 3];
    }
    catch( Exception e )
      {
      return null;
      }

    int where = 0;
    int max = in.length();
    for( int count = 0; count < max; count++ )
      {
      char fullChar = in.charAt( count );
      if( fullChar <= 0x7F )
        {
        // Regular ASCII.
        result[where] = (byte)fullChar;
        where++;
        continue;
        }

      if( fullChar >= 0xD800 ) // High Surrogates
        break;

      //  7   U+007F   0xxxxxxx
      // 11   U+07FF   110xxxxx   10xxxxxx
      // 16   U+FFFF   1110xxxx   10xxxxxx   10xxxxxx
      if( (fullChar > 0x7F) && (fullChar <= 0x7FF) )
        {
        // Notice that this conversion from
        // characters to bytes
        // doesn't involve characters over 0x7F.
        // Bottom 6 bits.
        byte smallByte = (byte)(fullChar & 0x3F);
        // Big 5 bits.
        byte bigByte = (byte)((fullChar >> 6) & 0x1F);

        bigByte |= 0xC0; // Mark it as the beginning byte.
        smallByte |= 0x80; // Mark it as a continuing byte.
        result[where] = bigByte;
        where++;
        result[where] = smallByte;
        where++;
        }

      // 16   U+FFFF   1110xxxx   10xxxxxx   10xxxxxx
      if( fullChar > 0x7FF )
        {
        byte byte3 = (byte)(fullChar & 0x3F);
        byte byte2 = (byte)((fullChar >> 6) & 0x3F);
        byte bigByte = (byte)((fullChar >> 12) & 0x0F); // Biggest 4 bits.

        bigByte |= 0xE0; // Mark it as the beginning byte.
        byte2 |= 0x80; // Mark it as a continuing byte.
        byte3 |= 0x80; // Mark it as a continuing byte.
        result[where] = bigByte;
        where++;
        result[where] = byte2;
        where++;
        result[where] = byte3;
        where++;
        }
      }

    return Utility.resizeByteArraySmaller( result,
                                             where );
    }



  public static String bytesToString( byte[] in,
                                      int maxLen )
    {
    try
    {
    if( in == null )
      return "";

    if( in.length == 0 )
      return "";

    if( in[0] == 0 )
      return "";

    if( maxLen > in.length )
      maxLen = in.length;

    int runOnBytes = 0;
    char fullChar = ' ';
    StringBuilder sBuilder = new StringBuilder( maxLen );
    for( int count = 0; count < maxLen; count++ )
      {
      byte charPart = in[count];
      if( charPart == 0 )
        break;

      if( (charPart & 0x80) == 0 )
        {
        runOnBytes = 0;
        // It's regular ASCII.
        sBuilder.append( (char)charPart );
        continue;
        }

      if( (charPart & 0xC0) == 0x80 )
        {
        runOnBytes++;
        if( runOnBytes > 3 )
          return sBuilder.toString();

        // It's a continuing byte that has already
        // been read below.
        continue;
        }

      if( (charPart & 0xC0) == 0xC0 )
        {
        runOnBytes = 0;
        // It's a beginning byte.
        // A beginning byte is either 110xxxxx or
        // 1110xxxx.
        if( (charPart & 0xF0) == 0xE0 )
          {
          // Starts with 1110xxxx.
          // It's a 3-byte character.
          if( (count + 2) >= maxLen )
            break; // Ignore the garbage.

          char bigByte = (char)(charPart & 0x0F);
          char byte2 = (char)(in[count + 1] & 0x3F);
          char byte3 = (char)(in[count + 2] & 0x3F);

          fullChar = (char)(bigByte << 12);
          fullChar |= (char)(byte2 << 6);
          fullChar |= byte3;
          if( fullChar < 0xD800 ) // High Surrogates
            sBuilder.append( fullChar );

          }

        if( (charPart & 0xE0) == 0xC0 )
          {
          // Starts with 110xxxxx.
          // It's a 2-byte character.
          if( (count + 1) >= maxLen )
            continue; // Ignore the garbage.

          char bigByte = (char)(charPart & 0x1F);
          char byte2 = (char)(in[count + 1] & 0x3F);

          fullChar = (char)(bigByte << 6);
          fullChar |= byte2;

          if( fullChar < 0xD800 ) // High Surrogates
            sBuilder.append( fullChar );

          }

        // If it doesn't match the two above it
        // gets ignored.
        }
      }

    String result = sBuilder.toString();
    if( result == null )
      return "";

    return result;
    }
    catch( Exception e )
      {
      return "Error in UTF8Strings.bytesToString(): " + e.getMessage();
      // return "";
      }
    }





  }
