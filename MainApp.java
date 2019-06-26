// Copyright Eric Chauvin 2019.

// Java I/O tutorial.
// https://docs.oracle.com/javase/tutorial/essential/io/bytestreams.html


// black, blue, cyan, darkGray, gray, green, lightGray,
// magenta, orange, pink, red, white, yellow.

    // Rectangle Rec = pane.getBounds();
    // Rec.height
    // Rec.width
    // Rec.x
    // Rec.y

    // JButton b1 = new JButton( "A Button" );


///////////////////////////////
// javax.media.j3d.Canvas3D
// https://en.wikipedia.org/wiki/Java_3D
//////////////////////////////


// import java.awt.Rectangle;
import java.awt.Container;
import java.awt.Insets;
// import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;



public class MainApp implements Runnable
  {
  public static final String versionDate = "6/26/2019";
  private Font mainFont;
  private JTabbedPane mainTabbedPane;
  private JFrame mainFrame;
  private JLabel statusLabel;
  private JTextArea statusTextArea;
  private EditorTabPage[] tabPagesArray;
  private int tabPagesArrayLast = 0;
  private String showProjectText = "";
  private String searchText = "";
  private String statusFileName = "";



  public static void main( String[] args )
    {
    // A static method in this class is creating
    // an instance of this class.
    try
    {
    MainApp mApp = new MainApp();

    // java.lang.Exception
    // throw new Exception( "This message" );
    
    // In the event queue.
    SwingUtilities.invokeLater( mApp );

    }
    catch( Exception e )
      {
      // Do something....
      // String e.getMessage()
      }
    // finally
    //  {
    //  }
    }



  // For the Runnable interface:
  @Override
  public void run()
    {
    setupMainFrame();
    }




  public void setupMainFrame()
    {
    // checkSingleInstance()

    tabPagesArray = new EditorTabPage[2];

    mainFrame = new JFrame( "Code Editor" );
    mainFont = new Font( Font.MONOSPACED, Font.PLAIN, 40 );

    mainFrame.setDefaultCloseOperation(
                     WindowConstants.EXIT_ON_CLOSE );

    mainFrame.getContentPane().setBackground( Color.red );
    // mainFrame.getContentPane().setForeground( Color.red );

    mainFrame.setSize( 1200, 600 );
    addComponentsToMainFrame( mainFrame.getContentPane() );
    // mainFrame.pack();
    // mainFrame.setExtendedState( JFrame.MAXIMIZED_BOTH );

    setupMenus();

    showStatus( "Programming by Eric Chauvin." );
    showStatus( "Version date: " + versionDate );

    showStatus( "Appending this." );

    // Center it.
    mainFrame.setLocationRelativeTo( null );
    mainFrame.setVisible( true );
    }




  private void addComponentsToMainFrame( Container pane )
    {
    pane.setLayout( new LayoutSimpleVertical( pane ));

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout( new LayoutSimpleVertical( mainPanel ));
    mainPanel.setBackground( Color.black );
    // Setting it to FixedHeightMax means this component is
    // stretchable.
    // new Dimension( Width, Height );
    mainPanel.setPreferredSize( new Dimension(
                         1, LayoutSimpleVertical.FixedHeightMax ));

    pane.add( mainPanel );

    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout( new LayoutSimpleVertical( bottomPanel ));
    bottomPanel.setBackground( Color.black );
    bottomPanel.setPreferredSize( new Dimension( 1, 74 ));
    pane.add( bottomPanel );

    // String "[" + minimum + "," + preferred + "," + maximum + "]@" + alignment;
    statusLabel = new JLabel( " Label with numbers to see." );
    statusLabel.setForeground( Color.white );
    statusLabel.setFont( mainFont );
    bottomPanel.add( statusLabel );

    // Add a second label below the first.
    // JLabel testLabel = new JLabel( " Test label." );

// black, blue, cyan, darkGray, gray, green, lightGray,
// magenta, orange, pink, red, white, yellow.

    mainTabbedPane = new JTabbedPane();
    mainTabbedPane.setBackground( Color.cyan );
    mainTabbedPane.setForeground( Color.black );
    mainTabbedPane.setFont( mainFont );
    mainTabbedPane.setPreferredSize( new Dimension(
                         1, LayoutSimpleVertical.FixedHeightMax ));

    mainPanel.add( mainTabbedPane );

    addStatusTextPane();

    addTextPane( "The tab's title", "FileName" );
    addTextPane( "Tab 2", "FileName" );
    addTextPane( "Tab 3", "FileName" );
    addTextPane( "Tab 4", "FileName" );
    }





  public void showStatus( String toShow )
    {
    statusTextArea.append( toShow + "\n" );
    }



  public void clearStatus()
    {
    statusTextArea.setText( "" );
    }



  private void addStatusTextPane()
    {
    // tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

    statusTextArea = new JTextArea();
    statusTextArea.setFont( mainFont );
    statusTextArea.setLineWrap( true );
    statusTextArea.setWrapStyleWord( true );
    statusTextArea.setBackground( Color.black );
    statusTextArea.setForeground( Color.white );

    JScrollPane scrollPane1 = new JScrollPane( statusTextArea );

    scrollPane1.setVerticalScrollBarPolicy(
             JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

    // scrollPane1.setBackground( Color.red );
    // scrollPane1.setForeground( Color.red );

    mainTabbedPane.addTab( "Status", null, scrollPane1 ); 
                          // , "Tool tip." );

    EditorTabPage newPage = new EditorTabPage( this,
                                               "Status",
                                               statusFileName,
                                               statusTextArea );

    tabPagesArray[tabPagesArrayLast] = newPage;
    // ProjectConfigFile.SetString( "RecentFile" + TabPagesArrayLast.ToString(), FileName, true );
    tabPagesArrayLast++;

    if( tabPagesArrayLast >= tabPagesArray.length )
      {
      tabPagesArray = resizeTabPagesArray( tabPagesArray, 16 );
      }
    }



  public EditorTabPage[] resizeTabPagesArray( EditorTabPage[] in, int sizeToAdd )
    {
    EditorTabPage[] newArray = new EditorTabPage[in.length + sizeToAdd];
    int max = in.length;
    for( int count = 0; count < max; count++ )
      newArray[count] = in[count];

    return newArray;
    }



  private void addTextPane( String tabTitle, String fileName )
    {
    // tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

    JTextArea textArea1 = new JTextArea();
    textArea1.setFont( mainFont );
    textArea1.setLineWrap( false );
    // textArea1.setWrapStyleWord( true );
    textArea1.setBackground( Color.black );
    textArea1.setForeground( Color.white );
    textArea1.setText( "The title is: " + tabTitle );
    // String getText();
    // selectAll()
    // append( String )

    JScrollPane scrollPane1 = new JScrollPane( textArea1 );

    scrollPane1.setVerticalScrollBarPolicy(
             JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

    // scrollPane1.setBackground( Color.red );
    // scrollPane1.setForeground( Color.red );

    mainTabbedPane.addTab( tabTitle, null, scrollPane1,
                      "Tool tip." );

    EditorTabPage newPage = new EditorTabPage( this,
                                               tabTitle,
                                               fileName,
                                               textArea1 );

    tabPagesArray[tabPagesArrayLast] = newPage;
    // ProjectConfigFile.SetString( "RecentFile" + TabPagesArrayLast.ToString(), FileName, true );
    tabPagesArrayLast++;

    if( tabPagesArrayLast >= tabPagesArray.length )
      {
      tabPagesArray = resizeTabPagesArray( tabPagesArray, 16 );
      }
    }





  private void setupMenus()
    {
    JMenuBar menuBar = new JMenuBar();
    menuBar.setBackground( Color.black );

    JMenu menu = new JMenu( "A Menu" );
    menu.setMnemonic( KeyEvent.VK_A );
    menu.setFont( mainFont );
    menu.setForeground( Color.white );
    menuBar.add( menu );

    JMenuItem menuItem = new JMenuItem( "A text-only menu item",
                         KeyEvent.VK_T);

    menuItem.setAccelerator( KeyStroke.getKeyStroke(
        KeyEvent.VK_1, ActionEvent.ALT_MASK ));

    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menu.add( menuItem );

    menuItem = new JMenuItem( "Second one." );
    menuItem.setMnemonic( KeyEvent.VK_B );
    menu.add( menuItem );

    // menu.addSeparator();
    // JMenu submenu = new JMenu( "A submenu" );
    // submenu.setMnemonic(KeyEvent.VK_S);
    // menuItem = new JMenuItem("An item in the submenu");
    // menuItem.setAccelerator(KeyStroke.getKeyStroke(
    //     KeyEvent.VK_2, ActionEvent.ALT_MASK));
    // submenu.add(menuItem);

    // menuItem = new JMenuItem("Another item");
    // submenu.add(menuItem);
    // menu.add(submenu);

    menu = new JMenu("Another Menu");
    menu.setMnemonic(KeyEvent.VK_N);
    menuBar.add(menu);

    // System.exit( 0 );

    mainFrame.setJMenuBar( menuBar );
    }



/*
  private void SaveAllFiles()
    {
    if( TabPagesArrayLast < 1 )
      return;

    for( int Count = 1; Count < TabPagesArrayLast; Count++ )
      {
      TabPagesArray[Count].WriteToTextFile();
      string FileName = TabPagesArray[Count].FileName;
      ProjectConfigFile.SetString( "RecentFile" + Count.ToString(), FileName, false );
      }

    ProjectConfigFile.WriteToTextFile();
    }
*/



  }
