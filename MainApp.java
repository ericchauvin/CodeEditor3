// Copyright Eric Chauvin 2019.




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
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.Timer;
// import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;


// black, blue, cyan, darkGray, gray, green, lightGray,
// magenta, orange, pink, red, white, yellow.



public class MainApp implements Runnable
  {
  public static final String versionDate = "8/29/2019";
  private Font mainFont;
  private Font menuFont;
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
  private TimerActions tActions;
  private ConfigureFile mainConfigFile;
  private ConfigureFile projectConfigFile;
  private final int maximumTabsOpen = 30;
  private Timer keyboardTimer;



  public static void main( String[] args )
    {
    MainApp mApp = new MainApp();
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

    // Pass this program's directory as a parameter from
    // the batch file.
    String programDirectory = "C:\\Eric\\CodeEditorJava\\";
    String mainConfigFileName = programDirectory +
                                      "MainConfigure.txt";

    mainConfigFile = new ConfigureFile( this,
                                mainConfigFileName );

    String currentProjectFileName =
            mainConfigFile.getString( "CurrentProjectFile" );

    if( currentProjectFileName.length() < 4 )
      {
      currentProjectFileName = programDirectory +
                                     "ProjectOptions.txt";

      mainConfigFile.setString( "CurrentProjectFile",
                                 currentProjectFileName,
                                 true );

      mainConfigFile.writeToTextFile();
      }

    projectConfigFile = new ConfigureFile( this,
                             currentProjectFileName );

    setShowProjectText();


    mActions = new MenuActions( this );
    tActions = new TimerActions( this );

    tabPagesArray = new EditorTabPage[2];

    mainFrame = new JFrame( "Code Editor" );
    mainFont = new Font( Font.MONOSPACED, Font.PLAIN, 40 );
    menuFont = new Font( Font.MONOSPACED, Font.PLAIN, 46 );

    mainFrame.setDefaultCloseOperation(
                     WindowConstants.EXIT_ON_CLOSE );

    mainFrame.getContentPane().setBackground( Color.red );
    // mainFrame.getContentPane().setForeground( Color.red );

    mainFrame.setSize( 1200, 600 );
    addComponentsToMainFrame( mainFrame.getContentPane() );
    // mainFrame.pack();
    // mainFrame.setExtendedState( JFrame.MAXIMIZED_BOTH );

    setupMenus();

    openRecentFiles();

    showStatus( "Programming by Eric Chauvin." );
    showStatus( "Version date: " + versionDate );

    setupTimer();

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
    }




  public void showStatus( String toShow )
    {
    if( statusTextArea == null )
      return;

    statusTextArea.append( toShow + "\n" );
    }



  public void clearStatus()
    {
    if( statusTextArea == null )
      return;

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
    textArea1.setSelectedTextColor( Color.white );
    textArea1.setSelectionColor( Color.darkGray );
// black, blue, cyan, darkGray, gray, green, lightGray,
// magenta, orange, pink, red, white, yellow.


    // textArea1.setText( "The title is: " + tabTitle );
    // String getText();
    // append( String )

    JScrollPane scrollPane1 = new JScrollPane( textArea1 );

    scrollPane1.setVerticalScrollBarPolicy(
             JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

    mainTabbedPane.addTab( tabTitle, null, scrollPane1,
                      "Tool tip." );

    // getTabComponentAt(int index)
    JLabel tabLabel = new JLabel( tabTitle );
    tabLabel.setForeground( Color.black );
    // It's transparent.  tabLabel.setBackground( Color.red );
    tabLabel.setFont( mainFont );
    mainTabbedPane.setTabComponentAt( tabPagesArrayLast,
                                tabLabel );

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

    ///////////////////////
    // File Menu:
    JMenu fileMenu = new JMenu( "File" );
    fileMenu.setMnemonic( KeyEvent.VK_F );
    fileMenu.setFont( menuFont );
    fileMenu.setForeground( Color.white );
    menuBar.add( fileMenu );

    JMenuItem menuItem = new JMenuItem( "Open" );
    menuItem.setMnemonic( KeyEvent.VK_O );
    menuItem.setActionCommand( "FileOpen" );
    menuItem.addActionListener( mActions );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    fileMenu.add( menuItem );

    menuItem = new JMenuItem( "Save All", KeyEvent.VK_L );
    // menuItem.setAccelerator( KeyStroke.getKeyStroke(
       // KeyEvent.VK_1, ActionEvent.ALT_MASK ));
    menuItem.setActionCommand( "FileSaveAll" );
    menuItem.addActionListener( mActions );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    fileMenu.add( menuItem );


    menuItem = new JMenuItem( "Close Current" );
    menuItem.setMnemonic( KeyEvent.VK_U );
    menuItem.setActionCommand( "FileCloseCurrent" );
    menuItem.addActionListener( mActions );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    fileMenu.add( menuItem );


    menuItem = new JMenuItem( "Exit" );
    menuItem.setMnemonic( KeyEvent.VK_X );
    menuItem.setActionCommand( "FileExit" );
    menuItem.addActionListener( mActions );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    fileMenu.add( menuItem );


    ///////////////////////
    // Edit Menu:
    JMenu editMenu = new JMenu( "Edit" );
    editMenu.setMnemonic( KeyEvent.VK_E );
    editMenu.setForeground( Color.white );
    editMenu.setFont( menuFont );
    menuBar.add( editMenu );

    menuItem = new JMenuItem( "Copy" );
    menuItem.setMnemonic( KeyEvent.VK_C );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    menuItem.setActionCommand( "EditCopy" );
    menuItem.addActionListener( mActions );
    editMenu.add( menuItem );

    menuItem = new JMenuItem( "Cut" );
    menuItem.setMnemonic( KeyEvent.VK_T );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    menuItem.setActionCommand( "EditCut" );
    menuItem.addActionListener( mActions );
    editMenu.add( menuItem );

    menuItem = new JMenuItem( "Paste" );
    menuItem.setMnemonic( KeyEvent.VK_P );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    menuItem.setActionCommand( "EditPaste" );
    menuItem.addActionListener( mActions );
    editMenu.add( menuItem );

    menuItem = new JMenuItem( "Select All" );
    menuItem.setMnemonic( KeyEvent.VK_A );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    menuItem.setActionCommand( "EditSelectAll" );
    menuItem.addActionListener( mActions );
    editMenu.add( menuItem );


// =======

    ///////////////////////
    // Project Menu:
    JMenu projectMenu = new JMenu( "Project" );
    projectMenu.setMnemonic( KeyEvent.VK_P );
    projectMenu.setForeground( Color.white );
    projectMenu.setFont( menuFont );
    menuBar.add( projectMenu );

    menuItem = new JMenuItem( "Set Current Project" );
    menuItem.setMnemonic( KeyEvent.VK_C );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    menuItem.setActionCommand( "ProjectSetCurrent" );
    menuItem.addActionListener( mActions );
    projectMenu.add( menuItem );


    ///////////////////////
    // Help Menu:
    JMenu helpMenu = new JMenu( "Help" );
    helpMenu.setMnemonic( KeyEvent.VK_H );
    helpMenu.setForeground( Color.white );
    helpMenu.setFont( menuFont );
    menuBar.add( helpMenu );

    menuItem = new JMenuItem( "About" );
    menuItem.setMnemonic( KeyEvent.VK_A );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    menuItem.setActionCommand( "HelpAbout" );
    menuItem.addActionListener( mActions );
    helpMenu.add( menuItem );


    mainFrame.setJMenuBar( menuBar );
    }




  public void saveAllFiles()
    {
    if( tabPagesArrayLast < 1 )
      return;

    for( int count = 1; count < tabPagesArrayLast; count++ )
      {
      tabPagesArray[count].writeToTextFile();

      String fileName = tabPagesArray[count].getFileName();
      showStatus( "Setting: " + fileName );

      projectConfigFile.setString( "RecentFile" + count, fileName, false );
      }

    showStatus( "Saving project config file." );
    projectConfigFile.writeToTextFile();
    showStatus( "Saving main config file." );
    mainConfigFile.writeToTextFile();
    }




  public void openFile()
    {
    final JFileChooser fc = new JFileChooser();
    // fc.setCurrentDirectory()

    // FileFilter filter = new FileNameExtensionFilter( "Text file", "txt" );
    // fc.addChoosableFileFilter( filter );

    int returnVal = fc.showOpenDialog( mainFrame );
    if( returnVal != JFileChooser.APPROVE_OPTION )
      return;

    // Save Dialog:
    // int returnVal = fc.showSaveDialog(FileChooserDemo.this);


    // https://docs.oracle.com/javase/7/docs/api/java/nio/file/package-summary.html
    // https://docs.oracle.com/javase/7/docs/api/java/io/File.html
    File file = fc.getSelectedFile();
    String fileName = file.getName();
    showStatus( "File name picked is: " + fileName );

    String pathName = file.getPath();
    showStatus( "Path name picked is: " + pathName );

    if( fileIsInTabPages( pathName ))
      {
      JOptionPane.showMessageDialog( mainFrame,
                 "The file is already in the tabs." );

      return;
      }

    addTextPane( fileName, pathName );
    }




  public void showAboutBox()
    {
    JOptionPane.showMessageDialog( mainFrame,
                 "Programming by Eric Chauvin.  Version date: " + versionDate );

    }



  private void setShowProjectText()
    {
    String showS = projectConfigFile.getString( "ProjectDirectory" );

    showS = showS.replace( "C:\\Eric\\", "" );
    showProjectText = showS;
    }



  private void openRecentFiles()
    {
    // showStatus( "Opening recent files." );

    // int howMany = 0;
    for( int count = 1; count <= maximumTabsOpen; count++ )
      {
      String fileName = projectConfigFile.getString(
                                "RecentFile" + count );

      if( fileName.length() < 1 )
        break;

      // howMany++;
      Path path = Paths.get( fileName );
      File file = path.toFile();
      String tabTitle = file.getName();
      // showStatus( "tabTitle is: " + tabTitle );

      // String pathName = file.getPath();
      // showStatus( "Path name picked is: " + pathName );

      addTextPane( tabTitle, fileName );
      }

    // showStatus( "Files opened: " + howMany );
    }



  private boolean fileIsInTabPages( String fileName )
    {
    for( int count = 0; count < tabPagesArrayLast; count++ )
      {
      String fileAtTab = tabPagesArray[count].getFileName();
      if( fileAtTab.compareToIgnoreCase( fileName ) == 0 )
        return true;

      }

    return false;
    }



  public void closeCurrentFile()
    {
    try
    {
    // Don't save anything automatically.
    // saveAllFiles();

    if( tabPagesArrayLast < 2 )
      return;

    // getTabCount()
    int selectedIndex = mainTabbedPane.getSelectedIndex();
    if( selectedIndex < 1 )
      return;

    if( selectedIndex >= tabPagesArrayLast )
      return;

    // Clear all RecentFile entries.
    for( int count = 1; count <= maximumTabsOpen; count++ )
      projectConfigFile.setString( "RecentFile" + count, "", false );

    int where = 1;
    for( int count = 1; count < tabPagesArrayLast; count++ )
      {
      if( count == selectedIndex )
        continue;

      String fileName = tabPagesArray[count].getFileName();
      projectConfigFile.setString( "RecentFile" + where, fileName, false );
      where++;
      }

    projectConfigFile.writeToTextFile();

    mainTabbedPane.removeAll();
    tabPagesArrayLast = 0;
    addStatusTextPane();
    openRecentFiles();
    }
    catch( Exception e )
      {
      showStatus( "Exception in closeCurrentFile()." );
      showStatus( e.getMessage() );
      }
    }





/*
  private void setCurrentProjectToolStripMenuItem_Click(object sender, EventArgs e)
    {
    string FileToOpen = "";

    try
    {
    // Get this starting directory name from a confifiguration
    // file or something.
    FileToOpen = OpenFileNameDialog( "C:\\Eric", "*.txt" );
    if( FileToOpen.Length < 1 )
      return;

    if( !FileToOpen.ToLower().Contains( ".txt" ))
      {
      MessageBox.Show( "This should be a .txt file: " + FileToOpen, MessageBoxTitle, MessageBoxButtons.OK );
      return;
      }

    // MessageBox.Show( "File to open: " + FileToOpen, MessageBoxTitle, MessageBoxButtons.OK );

    if( !File.Exists( FileToOpen ))
      {
      MessageBox.Show( "The file does not exist: " + FileToOpen, MessageBoxTitle, MessageBoxButtons.OK );
      return;
      }

    MainTabControl.TabPages.Clear();
    TabPagesArrayLast = 0;
    AddStatusPage();

    string ProjectFileName = FileToOpen;

    if( ProjectFileName.Length < 4 )
      {
      MessageBox.Show( "Pick a file in the Project directory.", MessageBoxTitle, MessageBoxButtons.OK );
      return;
      }

    MainConfigFile.SetString( "CurrentProjectFile", ProjectFileName, true );

    ProjectConfigFile = new ConfigureFile( ProjectFileName, this );

    string WorkingDir = ProjectFileName;
    WorkingDir = Path.GetDirectoryName( WorkingDir );
   ProjectConfigFile.SetString( "ProjectDirectory", WorkingDir, false );

    OpenRecentFiles();

    // string BuildBatchFileName = ProjectConfigFile.GetString( "BuildBatchFile" );

    SetShowProjectText();

    }
    catch( Exception Except )
      {
      string ShowS = "Exception with opening project file.\r\n" +
                     "Entered: " + FileToOpen + "\r\n" +
                     Except.Message;

      MessageBox.Show( ShowS, MessageBoxTitle, MessageBoxButtons.OK );
      }
    }
*/



  private JTextArea getSelectedTextArea()
    {
    if( tabPagesArrayLast < 2 )
      return null;

    int selectedIndex = mainTabbedPane.getSelectedIndex();
    if( selectedIndex < 1 )
      return null;

    if( selectedIndex >= tabPagesArrayLast )
      return null;

    JTextArea selectedTextArea = tabPagesArray[
                       selectedIndex].getTextArea();

    return selectedTextArea;
    }



// ========
  // https://docs.oracle.com/javase/tutorial/uiswing/components/text.html
  // https://docs.oracle.com/javase/7/docs/api/javax/swing/JTextArea.html

  public void editCopy()
    {
    try
    {
    JTextArea selectedTextArea = getSelectedTextArea();
    if( selectedTextArea == null )
      return;

    selectedTextArea.copy();
    }
    catch( Exception e )
      {
      showStatus( "Exception in editCopy()." );
      showStatus( e.getMessage() );
      }
    }



  public void editCut()
    {
    try
    {
    JTextArea selectedTextArea = getSelectedTextArea();
    if( selectedTextArea == null )
      return;

    selectedTextArea.cut();
    }
    catch( Exception e )
      {
      showStatus( "Exception in editCut()." );
      showStatus( e.getMessage() );
      }
    }



  public void editPaste()
    {
    try
    {
    JTextArea selectedTextArea = getSelectedTextArea();
    if( selectedTextArea == null )
      return;

    selectedTextArea.paste();
    }
    catch( Exception e )
      {
      showStatus( "Exception in editPaste()." );
      showStatus( e.getMessage() );
      }
    }



  public void editSelectAll()
    {
    try
    {
    JTextArea selectedTextArea = getSelectedTextArea();
    if( selectedTextArea == null )
      return;

    selectedTextArea.selectAll();
    }
    catch( Exception e )
      {
      showStatus( "Exception in editSelectAll()." );
      showStatus( e.getMessage() );
      }
    }



  private void setupTimer()
    {
    int delay = 1000;
    keyboardTimer = new Timer( delay, tActions );
    keyboardTimer.start();
    }



  public void keyboardTimerEvent()
    {
    try
    {
    showStatus( "keyboardTimerEvent called." );
    keyboardTimer.stop();


/*
====
https://docs.oracle.com/javase/tutorial/uiswing/events/windowlistener.html
https://docs.oracle.com/javase/7/docs/api/java/awt/event/WindowEvent.html

    if (IsClosing)
      return;

    int SelectedIndex = MainTabControl.SelectedIndex;
    if( SelectedIndex >= TabPagesArray.Length )
      {
      CursorLabel.Text = "No textbox selected.";
      return;
      }

    if( SelectedIndex < 0 )
      {
      CursorLabel.Text = "No textbox selected.";
      // MessageBox.Show( "There is no tab page selected, or the status page is selected. (Top.)", MessageBoxTitle, MessageBoxButtons.OK );
      return;
      }

    // The status page is at zero.
    if( SelectedIndex == 0 )
      {
      CursorLabel.Text = "Status page.";
      return;
      }

    string TabTitle = TabPagesArray[SelectedIndex].TabTitle;
    // TabPagesArray[SelectedIndex].FileName;

    TextBox SelectedTextBox = TabPagesArray[SelectedIndex].MainTextBox;
    if (SelectedTextBox == null)
      return;

    if (SelectedTextBox.IsDisposed)
      return;

    int Start = SelectedTextBox.SelectionStart;
    // int Start2 = SelectedTextBox.GetFirstCharIndexOfCurrentLine();

    // The +1 is for display and matching with
    // the compiler error line number.
    int Line = 1 + SelectedTextBox.GetLineFromCharIndex( Start );
    CursorLabel.Text = "Line: " + Line.ToString("N0") + "     " + TabTitle + "      Proj: " + ShowProjectText;

*/
    keyboardTimer.start();
    }
    catch( Exception e )
      {
      showStatus( "Exception in keyboardTimerEvent()." );
      showStatus( e.getMessage() );
      }
    }



  }
