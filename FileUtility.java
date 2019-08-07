// Copyright Eric Chauvin 2019.




import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;



  public class FileUtility
  {

  public static String readAsciiFileToString( MainApp mApp,
                                              String fileName )
    {
    try
    {
    Path path = Paths.get( fileName );

    if( !Files.exists( path, LinkOption.NOFOLLOW_LINKS ))
      {
      return "";
      }

    byte[] fileArray;
    fileArray = Files.readAllBytes( path );

    StringBuilder sBuilder = new StringBuilder();

    // There is a String constructor that reads a byte array
    // using a particular character set, but this tests it
    // to make sure it doesn't have bad data in it.

    short newline = (short)'\n';
    short space = (short)' ';
    short tab = (short)'\t';
    int max = fileArray.length;
    for( int count = 0; count < max; count++ )
      {
      short sChar = Utility.ByteToShort( fileArray[count] );

      if( sChar == tab )
        sChar = space;

      if( sChar > 127 )
        continue; // Don't want this character.

      if( sChar < space )
        {
        if( sChar != newline )
          continue;

        }

      sBuilder.append( (char)sChar );
      }

    return sBuilder.toString();
    }
    catch( Exception e )
      {
      mApp.showStatus( "Could not read the file: \n" + fileName );
      mApp.showStatus( e.getMessage() );
      return "";
      }
    }




  public static boolean writeAsciiStringToFile( MainApp mApp,
                                                String fileName,
                                                String textS )
    {
    try
    {
    Path path = Paths.get( fileName );

    char newline = '\n';
    char space = ' ';
    char tab = '\t';
    StringBuilder sBuilder = new StringBuilder();
    int max = textS.length();
    for( int count = 0; count < max; count++ )
      {
      char sChar = textS.charAt( count );
      if( sChar > 127 )
        continue;

      if( sChar == tab )
        sChar = space;

      if( sChar < space )
        {
        if( sChar != newline )
          continue;

        }

      sBuilder.append( sChar );
      }

    String outString = sBuilder.toString();
    char[] outCharArray = outString.toCharArray();
    byte[] outBuffer = new byte[outCharArray.length];
    max = outCharArray.length;
    for( int count = 0; count < max; count++ )
      {
      outBuffer[count] = (byte)outCharArray[count];
      }

    Files.write( path, outBuffer,  StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE );

    return true;
    }
    catch( Exception e )
      {
      mApp.showStatus( "Could not write to the file: \n" + fileName );
      mApp.showStatus( e.getMessage() );
      return false;
      }
    }



  }
