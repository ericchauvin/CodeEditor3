// Copyright Eric Chauvin 2018 - 2020.



// When you use star-slash comments you usually use
// it to block out a larger section of code.  (As
// opposed to the two-slash single line comments.)  Maybe
// even hundreds of lines.  So you can't worry about
// if there is a string literal somewhere in there
// that would cause a problem.

// This removes anything inside the star-slash
// comments no matter what it is.  Whether it's in
// a string literal or not.

// In C# code I have to define the string
// with separated characters like this:
// string StarSlash = "*" + "/";
// Otherwise it will interpret it as the end
// of a comment, not as part of a string literal.



  public class RemoveStarComments
  {


  public static String removeComments( MainApp mApp,
                                       String inString )
    {
    // This ignores Markers.Begin, Markers.End
    // and any other markers.

    if( inString.trim().length() == 0 )
      return "";

    StringBuilder sBuilder = new StringBuilder();

    String SlashStar = "/" + "*";
    String StarSlash = "*" + "/";

    // This replaces the comment marker strings
    // anywhere and everywhere in the file.  Whether
    // they are inside quotes or not.
    inString = inString.replace( SlashStar, Character.toString( Markers.SlashStar ));
    inString = inString.replace( StarSlash, Character.toString( Markers.StarSlash ));

    boolean isInsideComment = false;
    int last = inString.length();
    for( int count = 0; count < last; count++ )
      {
      char testChar = inString.charAt( count );

      if( testChar == Markers.SlashStar )
        {
        if( isInsideComment )
          {
          // It shouldn't find this start marker
          // if it's already inside a comment.
          // sBuilder.append( Char.toString( Markers.ErrorPoint ));

          mApp.showStatus( " " );
          mApp.showStatus( "Error with nested comment at: " +
                                       count );
          return sBuilder.toString();
          }

        isInsideComment = true;
        // Replace a comment with white space so
        // that identifier strings don't come
        // together.
        sBuilder.append( " " );
        continue;
        }

      if( testChar == Markers.StarSlash )
        {
        isInsideComment = false;
        continue;
        }

      if( !isInsideComment )
        {
        if( testChar == Markers.StarSlash )
          {
          // It shouldn't find this end marker
          // if it's not already inside a comment.
          // sBuilder.append( Char.toString( Markers.ErrorPoint ));

          mApp.showStatus( " " );
          mApp.showStatus( "Error with start-slash outside of a comment at: " + count );
          return sBuilder.toString();
          }

        sBuilder.append( Character.toString( testChar ));
        }
      }

    return sBuilder.toString();
    }




 }
