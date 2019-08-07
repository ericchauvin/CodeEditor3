// Copyright Eric Chauvin 2019.



// https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html



// What directory is the program in?
// Config file.
// Execute a batch file?




///////////////////////////////
// javax.media.j3d.Canvas3D
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
import javax.swing.JOptionPane;



// black, blue, cyan, darkGray, gray, green, lightGray,
// magenta, orange, pink, red, white, yellow.



public class MainApp implements Runnable
  {
  public static final String versionDate = "8/7/2019";
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
  private MenuActions mActions;




  public static void main( String[] args )
    {
    // A static method in this class is creating
    // an instance of this class.
    MainApp mApp = new MainApp();

    // It goes in the event queue.
    SwingUtilities.invokeLater( mApp );
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
    mActions = new MenuActions( this );

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

    // Center it.
    mainFrame.setLocationRelativeTo( null );
    mainFrame.setVisible( true );
    }




  private void addComponentsToMainFrame( Container pane )
    {
    pane.setLayout( new LayoutSimpleVertical());

    JPanel mainPanel = new JPanel();

    mainPanel.setLayout( new LayoutSimpleVertical());
    mainPanel.setBackground( Color.black );
    // Setting it to FixedHeightMax means this component is
    // stretchable.
    // new Dimension( Width, Height );
    mainPanel.setPreferredSize( new Dimension(
                         1, LayoutSimpleVertical.FixedHeightMax ));

    pane.add( mainPanel );

    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout( new LayoutSimpleVertical());
    bottomPanel.setBackground( Color.black );
    bottomPanel.setPreferredSize( new Dimension( 1, 74 ));
    pane.add( bottomPanel );

    statusLabel = new JLabel( " Label with numbers to see." );
    statusLabel.setForeground( Color.white );
    statusLabel.setFont( mainFont );
    bottomPanel.add( statusLabel );

    // Add a second label below the first.
    // JLabel testLabel = new JLabel( " Test label." );

// black, blue, cyan, darkGray, gray, green, lightGray,
// magenta, orange, pink, red, white, yellow.

    mainTabbedPane = new JTabbedPane();
    mainTabbedPane.setBackground( Color.green );
    mainTabbedPane.setForeground( Color.black );

    mainTabbedPane.setFont( mainFont );
    mainTabbedPane.setPreferredSize( new Dimension(
                         1, LayoutSimpleVertical.FixedHeightMax ));

    mainPanel.add( mainTabbedPane );

    addStatusTextPane();

    addTextPane( "TestNotes.txt", "c:\\Eric\\CodeEditorJava\\TestNotes.txt" );
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
    statusTextArea = new JTextArea();
    statusTextArea.setFont( mainFont );
    statusTextArea.setLineWrap( true );
    statusTextArea.setWrapStyleWord( true );
    statusTextArea.setBackground( Color.black );
    statusTextArea.setForeground( Color.white );
    statusTextArea.setCaretColor( Color.white );
    statusTextArea.setSelectedTextColor( Color.black );
    statusTextArea.setSelectionColor( Color.white );

    JScrollPane scrollPane1 = new JScrollPane( statusTextArea );

    scrollPane1.setVerticalScrollBarPolicy(
             JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

    mainTabbedPane.addTab( "Status", null, scrollPane1 );

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

    // mainTabbedPane.setBackgroundAt( 0, Color.red );
    // setForegroundAt(int index, Color foreground)

    }



  public EditorTabPage[] resizeTabPagesArray( EditorTabPage[] in, int sizeToAdd )
    {
    EditorTabPage[] newArray;
    try
    {
    newArray = new EditorTabPage[in.length + sizeToAdd];
    int max = in.length;
    for( int count = 0; count < max; count++ )
      newArray[count] = in[count];

    }
    catch( Exception e )
      {
      showStatus( "Couldn't allocate a new array for the EditorTabPage array." );
      showStatus( e.getMessage() );
      return in;
      }

    return newArray;
    }



  private void addTextPane( String tabTitle, String fileName )
    {
    JTextArea textArea1 = new JTextArea();
    textArea1.setFont( mainFont );
    textArea1.setLineWrap( false );
    // textArea1.setWrapStyleWord( true );
    textArea1.setBackground( Color.black );
    textArea1.setForeground( Color.white );
    textArea1.setCaretColor( Color.white );
    textArea1.setSelectedTextColor( Color.black );
    textArea1.setSelectionColor( Color.white );

    textArea1.setText( "The title is: " + tabTitle );
    // String getText();
    // selectAll()
    // append( String )

    JScrollPane scrollPane1 = new JScrollPane( textArea1 );

    scrollPane1.setVerticalScrollBarPolicy(
             JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

    mainTabbedPane.addTab( tabTitle, null, scrollPane1,
                      "Tool tip." );

    EditorTabPage newPage = new EditorTabPage( this,
                                               tabTitle,
                                               fileName,
                                               textArea1 );

    newPage.readFromTextFile();

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

    // File Menu:
    JMenu fileMenu = new JMenu( "File" );
    fileMenu.setMnemonic( KeyEvent.VK_F );
    fileMenu.setFont( mainFont );
    fileMenu.setForeground( Color.white );
    menuBar.add( fileMenu );

    JMenuItem menuItem = new JMenuItem( "Save All", KeyEvent.VK_A );
    // menuItem.setAccelerator( KeyStroke.getKeyStroke(
       // KeyEvent.VK_1, ActionEvent.ALT_MASK ));
    menuItem.setActionCommand( "FileSaveAll" );
    menuItem.addActionListener( mActions );

    menuItem.setForeground( Color.white );
    // menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    fileMenu.add( menuItem );

    menuItem = new JMenuItem( "Second one." );
    menuItem.setMnemonic( KeyEvent.VK_B );
    fileMenu.add( menuItem );

    // Edit Menu:
    JMenu editMenu = new JMenu( "Edit" );
    editMenu.setMnemonic( KeyEvent.VK_E );
    menuBar.add( editMenu );



    // Help Menu:
    JMenu helpMenu = new JMenu( "Help" );
    editMenu.setMnemonic( KeyEvent.VK_H );
    menuBar.add( helpMenu );

    menuItem = new JMenuItem( "About" );
    menuItem.setMnemonic( KeyEvent.VK_A );
    fileMenu.add( menuItem );

    // System.exit( 0 );

    mainFrame.setJMenuBar( menuBar );
    }




  public void SaveAllFiles()
    {
    // https://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html
    // https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html

    JOptionPane.showMessageDialog( mainFrame,
                 "Save All was clicked." );

    if( tabPagesArrayLast < 1 )
      return;

    for( int Count = 1; Count < tabPagesArrayLast; Count++ )
      {
      tabPagesArray[Count].writeToTextFile();

      // string FileName = TabPagesArray[Count].FileName;
      // ProjectConfigFile.SetString( "RecentFile" + Count.ToString(), FileName, false );
      }


    // ProjectConfigFile.WriteToTextFile();
    }




  }
