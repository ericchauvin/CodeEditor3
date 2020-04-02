// Copyright Eric Chauvin 2018 - 2020.


      // Basic Multilingual Plane
      // C0 Controls and Basic Latin (Basic Latin)
      //                                 (0000 007F)
      // C1 Controls and Latin-1 Supplement (0080 00FF)
      // Latin Extended-A (0100 017F)
      // Latin Extended-B (0180 024F)
      // IPA Extensions (0250 02AF)
      // Spacing Modifier Letters (02B0 02FF)
      // Combining Diacritical Marks (0300 036F)
      // General Punctuation (2000 206F)
      // Superscripts and Subscripts (2070 209F)
      // Currency Symbols (20A0 20CF)
      // Combining Diacritical Marks for Symbols
      //                                (20D0 20FF)
      // Letterlike Symbols (2100 214F)
      // Number Forms (2150 218F)
      // Arrows (2190 21FF)
      // Mathematical Operators (2200 22FF)
      // Box Drawing (2500 257F)
      // Geometric Shapes (25A0 25FF)
      // Miscellaneous Symbols (2600 26FF)
      // Dingbats (2700 27BF)
      // Miscellaneous Symbols and Arrows (2B00 2BFF)


  public class Markers
  {
  public static final char EscapedSingleQuote =
                                       (char)0x2700;
  public static final char EscapedDoubleQuote =
                                       (char)0x2701;
/*
  public static final char Begin = (char)0x2702;
  public static final char End = (char)0x2703;
  public static final char TypeString = (char)0x2704;
  public static final char TypeChar = (char)0x2705;
  public static final char TypeNumber = (char)0x2706;
  public static final char TypeIdentifier = (char)0x2707;
  public static final char ErrorPoint = (char)0x2708;
  public static final char TypeLineNumber = (char)0x2709;*/

  public static final char SlashStar = (char)0x270A;
  public static final char StarSlash = (char)0x270B;
/*
  public static final char DoubleSlash = (char)0x270C;
  public static final char TypeOperator = (char)0x270D;
  public static final char EscapedSlash = (char)0x270E;
  public static final char QuoteAsSingleCharacter =
                                         (char)0x270F;
  public static final char TypePreprocessor =
                                         (char)0x2710;
  // public static final char TypeBeginPreproc =
  //                                       (char)0x2711;
  // public static final char TypeEndPreproc =
  //                                       (char)0x2712;

*/


  public static boolean isMarker( char testChar )
    {
    // Reserve these symbols for markers.
    // Miscellaneous Symbols (0x2600 to 0x26FF)
    // Dingbats (0x2700 to 0x27BF)
    // Miscellaneous Symbols and Arrows (0x2B00 to 0x2BFF)

    int value = (int)testChar;
    if( (value >= 0x2600) && (value <= 0x2BFF))
      return true;

    return false;
    }



  }
