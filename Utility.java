// Copyright Eric Chauvin 2019.


  public class Utility
  {

  public static int[] ResizeIntArray( int[] in, int sizeToAdd )
    {
    int[] newArray = new int[in.length + sizeToAdd];
    int max = in.length;
    for( int count = 0; count < max; count++ )
      newArray[count] = in[count];

    return newArray;
    }





  // Java doesn't have unsigned values so the ByteToShort()
  // ShortToByte() functions are needed to make things work
  // right in Java.

  public static short ByteToShort( byte In )
    {
    short Result = (short)(In & 0x7F);

    if( (In & 0x80) == 0x80 )
      Result |= 0x80;

    return Result;
    }





  }

