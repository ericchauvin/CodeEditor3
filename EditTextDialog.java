// Copyright Eric Chauvin 2020.



import java.awt.Component;
import java.awt.Frame;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.ComponentAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JDialog;
import javax.swing.WindowConstants;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BoxLayout;



class EditTextDialog extends JDialog
                   implements ActionListener
  {
  // It needs to have a version UID since it's
  // serializable.
  public static final long serialVersionUID = 1;
  private MainApp mApp;
  private Font mainFont;
  private JTextField textField;
  private JButton okButton;




  public EditTextDialog( Frame useFrame,
                     MainApp useApp,
                     Font useFont )
    {
    super( useFrame, true );

    mApp = useApp;
    mainFont = useFont;
    addComponents( getContentPane() );
    // setContentPane( );

    // setSize( 400, 300 );
    pack();

    setTitle( "Enter Text" );

    setDefaultCloseOperation( WindowConstants.HIDE_ON_CLOSE );
    // DISPOSE_ON_CLOSE, DO_NOTHING_ON_CLOSE, EXIT_ON_CLOSE

    addWindowListener( new WindowAdapter()
      {
      public void windowClosing( WindowEvent we )
        {

        }
      });

    addComponentListener( new ComponentAdapter()
      {
      public void componentShown( ComponentEvent ce )
        {
        textField.requestFocusInWindow();
        }
      });

    // Center it.
    setLocationRelativeTo( null );
    }




  private void addComponents( Container pane )
    {
    pane.setBackground( Color.green );
    // pane.setForeground( Color.red );

    // The default layout manager for a JPanel is
    // FlowLayout.  But it's left to right.

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout( new BoxLayout( mainPanel,
                                  BoxLayout.PAGE_AXIS ));
                                     // LINE_AXIS

    mainPanel.setBackground( Color.black );

    JLabel label = new JLabel( "Enter text:               " );
    label.setFont( mainFont );
    label.setBackground( Color.black );
    label.setForeground( Color.white );

    mainPanel.add( label );

    textField = new JTextField();
    textField.setFont( mainFont );
    textField.setBackground( Color.black );
    textField.setForeground( Color.white );
    textField.setActionCommand( "TextFieldEnter" );
    textField.addActionListener( this );

    mainPanel.add( textField );

    okButton = new JButton( "OK" );
    okButton.setMnemonic( KeyEvent.VK_O );
    okButton.setActionCommand( "OkButton" );
    okButton.addActionListener( this );
    // okButton.setAlignmentX( Component.CENTER_ALIGNMENT );
    okButton.setFont( mainFont );
    okButton.setBackground( Color.black );
    okButton.setForeground( Color.white );
    mainPanel.add( okButton );

    pane.add( mainPanel );
    }



  public String getText()
    {
    return textField.getText().trim();
    }




  public void actionPerformed( ActionEvent event )
    {
    try
    {
    // String paramS = event.paramString();

    String command = event.getActionCommand();

    if( command == null )
      {
      mApp.showStatus( "ActionEvent Command is null." );
      // keyboardTimerEvent();
      return;
      }

    mApp.showStatus( "ActionEvent Command is: " + command );

    if( command == "OkButton" )
      {
      mApp.showStatus( "okButton was clicked." );
      setVisible( false );
      return;
      }

    if( command == "TextFieldEnter" )
      {
      mApp.showStatus( "Enter was pressed." );
      setVisible( false );
      return;
      }

    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in actionPerformed()." );
      mApp.showStatus( e.getMessage() );
      }
    }




  }
