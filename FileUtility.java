// Copyright Eric Chauvin 2019.


// https://docs.oracle.com/javase/tutorial/essential/io/index.html


    // Files.isReadable(Path)
    // Files.isWritable(Path)
    // Files.isExecutable(Path)
    // Files.delete(Path)
    // Files.copy(Path, Path, CopyOption...)
    // Files.copy(source, target, REPLACE_EXISTING );



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;



  public class FileUtility
  {

  public static String ReadAsciiFileToString( MainApp mApp,
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
    // .write( Path, byte[], OpenOption... )

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



  }
