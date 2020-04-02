// Copyright Eric Chauvin 2019 - 2020.



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;



  public class FileUtility
  {

  public static String readFileToString( MainApp mApp,
                                         String fileName,
                                         boolean keepTabs )
    {
    try
    {
    Path path = Paths.get( fileName );

    if( !Files.exists( path, LinkOption.NOFOLLOW_LINKS ))
      {
      return "";
      }

    byte[] fileBytes = Files.readAllBytes( path );
    if( fileBytes == null )
      return "";

    String fileS = UTF8Strings.bytesToString(
                                          fileBytes,
                                          2000000000 );


    StringBuilder sBuilder = new StringBuilder();


    // int nonAsciiCount = 0;
    char newline = '\n';
    char space = ' ';
    char tab = '\t';
    int max = fileS.length();
    for( int count = 0; count < max; count++ )
      {
      char sChar = fileS.charAt( count );

      if( !keepTabs )
        {
        if( sChar == tab )
          sChar = space;

        }

      // Markers shouldn't be in this.
      if( Markers.isMarker( sChar ))
        sChar = ' ';

      if( sChar < space )
        {
        // It ignores the \r character.
        if( !((sChar == newline) ||
              (sChar == tab )))
          continue;

        }

      sBuilder.append( sChar );
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




  public static boolean writeStringToFile( MainApp mApp,
                                        String fileName,
                                        String textS,
                                        boolean keepTabs )
    {
    try
    {
    if( textS == null )
      return false;

    if( textS.trim().length() < 1 )
      return false;

    Path path = Paths.get( fileName );

    char newline = '\n';
    char space = ' ';
    char tab = '\t';
    StringBuilder sBuilder = new StringBuilder();
    int max = textS.length();
    for( int count = 0; count < max; count++ )
      {
      char sChar = textS.charAt( count );

      if( sChar > 0xD800 ) // High Surrogates
        continue;

      if( !keepTabs )
        {
        if( sChar == tab )
          sChar = space;

        }

      if( sChar < space )
        {
        if( !((sChar == newline) ||
              (sChar == tab )))
          continue;

        }

      sBuilder.append( sChar );
      }

    String outString = sBuilder.toString();
    if( outString == null )
      return false;

    if( outString.trim().length() < 1 )
      return false;

    byte[] outBuffer = UTF8Strings.stringToBytes( outString );
    if( outBuffer == null )
      {
      mApp.showStatus( "Could not write to the file: \n" + fileName );
      mApp.showStatus( "outBuffer was null." );
      return false;
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
