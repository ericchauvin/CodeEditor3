// This is modified from Oracle's example/tutorial code, so
// it has to retain their copyright notice, etc.

// "Redistribution ... permitted provided that the
// following conditions are met..."

// This has been modified from the original tutorial code
// to make it so that I can see it with my bad vision,
// and other changes.


/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */




// package components;

import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.beans.*; //Property change stuff
import java.awt.*;
import java.awt.event.*;




public class DialogDemo extends JPanel
  {
  // It needs to have a version UID since it's
  // serializable.
  public static final long serialVersionUID = 1;

  JLabel label;
  //  ImageIcon icon = createImageIcon("images/middle.gif");
  JFrame frame;
  String simpleDialogDesc = "Some simple message dialogs";
  String iconDesc = "A JOptionPane has its choice of icons";
  String moreDialogDesc = "Some more dialogs";
  CustomDialog customDialog;


  public DialogDemo( JFrame frame )
    {
    super( new BorderLayout() );
    this.frame = frame;
    customDialog = new CustomDialog( frame, "geisel", this );
    customDialog.pack();

    JPanel frequentPanel = createSimpleDialogBox();
    JPanel featurePanel = createFeatureDialogBox();
    JPanel iconPanel = createIconDialogBox();
    label = new JLabel( "Click the \"Show it!\" button" +
                        " to bring up the selected dialog.",
                        JLabel.CENTER );

    Border padding = BorderFactory.createEmptyBorder(
                                          20, 20, 5, 20 );
    frequentPanel.setBorder( padding );
    featurePanel.setBorder( padding );
    iconPanel.setBorder( padding );

    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab( "Simple Modal Dialogs", null,
                          frequentPanel,
                          simpleDialogDesc );

    tabbedPane.addTab( "More Dialogs", null,
                          featurePanel,
                          moreDialogDesc);

    tabbedPane.addTab( "Dialog Icons", null,
                          iconPanel,
                          iconDesc );

    add( tabbedPane, BorderLayout.CENTER );
    add( label, BorderLayout.PAGE_END );
    label.setBorder( BorderFactory.createEmptyBorder(
                                       10, 10, 10, 10 ));
    }




  void setLabel( String newText )
    {
    label.setText( newText );
    }



/*
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = DialogDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
*/




  private JPanel createSimpleDialogBox()
    {
    final int numButtons = 4;
    JRadioButton[] radioButtons = new JRadioButton[numButtons];
    final ButtonGroup group = new ButtonGroup();

    JButton showItButton = null;

    final String defaultMessageCommand = "default";
    final String yesNoCommand = "yesno";
    final String yeahNahCommand = "yeahnah";
    final String yncCommand = "ync";

    radioButtons[0] = new JRadioButton("OK (in the L&F's words)");
    radioButtons[0].setActionCommand(defaultMessageCommand);

    radioButtons[1] = new JRadioButton("Yes/No (in the L&F's words)");
    radioButtons[1].setActionCommand(yesNoCommand);

    radioButtons[2] = new JRadioButton("Yes/No "
                      + "(in the programmer's words)");
    radioButtons[2].setActionCommand(yeahNahCommand);

    radioButtons[3] = new JRadioButton("Yes/No/Cancel "
                           + "(in the programmer's words)");
    radioButtons[3].setActionCommand(yncCommand);

    for( int i = 0; i < numButtons; i++ )
      {
      group.add( radioButtons[i] );
      }

    radioButtons[0].setSelected(true);

    showItButton = new JButton("Show it!");
    showItButton.addActionListener( new ActionListener()
      {
      public void actionPerformed( ActionEvent e )
        {
        String command = group.getSelection().
                                       getActionCommand();

        if( command == defaultMessageCommand )
          {
          JOptionPane.showMessageDialog( frame,
                     "Eggs aren't supposed to be green." );

          }
        else if( command == yesNoCommand )
          {
          int n = JOptionPane.showConfirmDialog(
                       frame,
                       "Would you like green eggs and ham?",
                       "An Inane Question",
                       JOptionPane.YES_NO_OPTION );

          if( n == JOptionPane.YES_OPTION )
            {
            setLabel( "Ewww!" );
            }
          else if( n == JOptionPane.NO_OPTION )
            {
            setLabel( "Me neither!" );
            }
          else
            {
            setLabel( "Come on -- tell me!" );
            }
          }
        else if( command == yeahNahCommand )
          {
          Object[] options = { "Yes, please", "No way!" };
          int n = JOptionPane.showOptionDialog(
                     frame,
                     "Would you like green eggs and ham?",
                     "A Silly Question",
                     JOptionPane.YES_NO_OPTION,
                     JOptionPane.QUESTION_MESSAGE,
                     null,
                     options,
                     options[0] );

          if( n == JOptionPane.YES_OPTION )
            {
            setLabel( "You're kidding!" );
            }
          else if( n == JOptionPane.NO_OPTION )
            {
            setLabel( "I don't like them, either." );
            }
          else
            {
            setLabel( "Come on -- 'fess up!" );
            }
          }
        else if( command == yncCommand )
          {
          Object[] options = { "Yes, please",
                               "No, thanks",
                               "No eggs, no ham!" };

          int n = JOptionPane.showOptionDialog(
                   frame,
                   "Would you like some green eggs to go " +
                   "with that ham?",
                   "A Silly Question",
                   JOptionPane.YES_NO_CANCEL_OPTION,
                   JOptionPane.QUESTION_MESSAGE,
                   null,
                   options,
                   options[2] );

          if( n == JOptionPane.YES_OPTION )
            {
            setLabel( "Here you go: green eggs and ham!" );
            }
          else if( n == JOptionPane.NO_OPTION )
            {
            setLabel( "OK, just the ham, then." );
            }
          else if( n == JOptionPane.CANCEL_OPTION )
            {
            setLabel( "Well, I'm certainly not going to eat them!" );
            }
          else
            {
            setLabel( "Please tell me what you want!" );
            }
          }

        return;
        }
      });

    return createPane( simpleDialogDesc + ":",
                       radioButtons,
                       showItButton);

    }




  private JPanel createPane( String description,
                             JRadioButton[] radioButtons,
                             JButton showButton )
    {
    int numChoices = radioButtons.length;
    JPanel box = new JPanel();
    JLabel label = new JLabel( description );

    box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
    box.add(label);

    for( int i = 0; i < numChoices; i++ )
      {
      box.add( radioButtons[i] );
      }

    JPanel pane = new JPanel(new BorderLayout());
    pane.add(box, BorderLayout.PAGE_START);
    pane.add(showButton, BorderLayout.PAGE_END);
    return pane;
    }




  private JPanel create2ColPane( String description,
                                 JRadioButton[] radioButtons,
                                 JButton showButton )
    {
    JLabel label = new JLabel(description);
    int numPerColumn = radioButtons.length/2;

    JPanel grid = new JPanel(new GridLayout(0, 2));
    for( int i = 0; i < numPerColumn; i++ )
      {
      grid.add( radioButtons[i] );
      grid.add(radioButtons[i + numPerColumn]);
      }

    JPanel box = new JPanel();
    box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
    box.add(label);
    grid.setAlignmentX(0.0f);
    box.add(grid);

    JPanel pane = new JPanel(new BorderLayout());
    pane.add(box, BorderLayout.PAGE_START);
    pane.add(showButton, BorderLayout.PAGE_END);

    return pane;
    }




  private JPanel createIconDialogBox()
    {
    JButton showItButton = null;

    final int numButtons = 6;
    JRadioButton[] radioButtons = new 
                                   JRadioButton[numButtons];

    final ButtonGroup group = new ButtonGroup();

    final String plainCommand = "plain";
    final String infoCommand = "info";
    final String questionCommand = "question";
    final String errorCommand = "error";
    final String warningCommand = "warning";
    final String customCommand = "custom";

    radioButtons[0] = new JRadioButton("Plain (no icon)");
    radioButtons[0].setActionCommand(plainCommand);

    radioButtons[1] = new JRadioButton("Information icon");
    radioButtons[1].setActionCommand(infoCommand);

    radioButtons[2] = new JRadioButton("Question icon");
    radioButtons[2].setActionCommand(questionCommand);

    radioButtons[3] = new JRadioButton("Error icon");
    radioButtons[3].setActionCommand(errorCommand);

    radioButtons[4] = new JRadioButton("Warning icon");
    radioButtons[4].setActionCommand(warningCommand);

    radioButtons[5] = new JRadioButton("Custom icon");
    radioButtons[5].setActionCommand(customCommand);

    for( int i = 0; i < numButtons; i++ )
      {
      group.add(radioButtons[i]);
      }

    radioButtons[0].setSelected(true);

    showItButton = new JButton("Show it!");
    showItButton.addActionListener( new ActionListener()
      {
      public void actionPerformed(ActionEvent e)
        {
        String command = group.getSelection().
                                        getActionCommand();

        if( command == plainCommand )
          {
          JOptionPane.showMessageDialog(
                        frame,
                        "Eggs aren't supposed to be green.",
                        "A plain message",
                        JOptionPane.PLAIN_MESSAGE);

          }
        else if( command == infoCommand )
          {
          JOptionPane.showMessageDialog(
                         frame,
                         "Eggs aren't supposed to be green.",
                         "Inane informational dialog",
                         JOptionPane.INFORMATION_MESSAGE);

          }
        else if( command == questionCommand )
          {
          JOptionPane.showMessageDialog(
                   frame,
                   "You shouldn't use a message dialog " +
                   "(like this)\n" +
                   "for a question, OK?",
                   "Inane question",
                   JOptionPane.QUESTION_MESSAGE );

          }
        else if( command == errorCommand )
          {
          JOptionPane.showMessageDialog(
                      frame,
                      "Eggs aren't supposed to be green.",
                      "Inane error",
                      JOptionPane.ERROR_MESSAGE);

          }
        else if( command == warningCommand )
          {
          JOptionPane.showMessageDialog(
                        frame,
                        "Eggs aren't supposed to be green.",
                        "Inane warning",
                        JOptionPane.WARNING_MESSAGE);

          }
        else if( command == customCommand )
          {
          JOptionPane.showMessageDialog(
                        frame,
                        "Eggs aren't supposed to be green.",
                        "Inane custom dialog",
                        JOptionPane.INFORMATION_MESSAGE,
                        null );

          }
        }
      });

    return create2ColPane( iconDesc + ":",
                           radioButtons,
                           showItButton );
    }





  private JPanel createFeatureDialogBox()
    {
    final int numButtons = 5;
    JRadioButton[] radioButtons = new JRadioButton[numButtons];
    final ButtonGroup group = new ButtonGroup();

    JButton showItButton = null;

    final String pickOneCommand = "pickone";
    final String textEnteredCommand = "textfield";
    final String nonAutoCommand = "nonautooption";
    final String customOptionCommand = "customoption";
    final String nonModalCommand = "nonmodal";

    radioButtons[0] = new JRadioButton("Pick one of several choices");
    radioButtons[0].setActionCommand(pickOneCommand);

    radioButtons[1] = new JRadioButton("Enter some text");
    radioButtons[1].setActionCommand(textEnteredCommand);

    radioButtons[2] = new JRadioButton("Non-auto-closing dialog");
    radioButtons[2].setActionCommand(nonAutoCommand);

    radioButtons[3] = new JRadioButton("Input-validating dialog "
                                           + "(with custom message area)");
    radioButtons[3].setActionCommand(customOptionCommand);

    radioButtons[4] = new JRadioButton("Non-modal dialog");
    radioButtons[4].setActionCommand(nonModalCommand);

    for( int i = 0; i < numButtons; i++ )
      {
      group.add(radioButtons[i]);
      }

    radioButtons[0].setSelected(true);

    showItButton = new JButton("Show it!");
    showItButton.addActionListener(new ActionListener() {
    public void actionPerformed( ActionEvent e )
      {
      String command = group.getSelection().
                                      getActionCommand();

      if( command == pickOneCommand )
        {
        Object[] possibilities = {"ham", "spam", "yam"};
        String s = (String)JOptionPane.showInputDialog(
                                     frame,
                            "Complete the sentence:\n" +
                            "\"Green eggs and...\"",
                            "Customized Dialog",
                            JOptionPane.PLAIN_MESSAGE,
                            null, // icon,
                            possibilities,
                            "ham");

        if( (s != null) && (s.length() > 0))
          {
          setLabel("Green eggs and... " + s + "!");
          return;
          }

        setLabel("Come on, finish the sentence!");
        }
      else if( command == textEnteredCommand )
        {
        String s = (String)JOptionPane.showInputDialog(
                                        frame,
                              "Complete the sentence:\n"
                                        + "\"Green eggs and...\"",
                                        "Customized Dialog",
                                        JOptionPane.PLAIN_MESSAGE,
                                        null, // icon,
                                        null,
                                        "ham");

        if ((s != null) && (s.length() > 0))
          {
          setLabel("Green eggs and... " + s + "!");
          return;
          }

        setLabel("Come on, finish the sentence!");
        }
      else if (command == nonAutoCommand)
        {
        final JOptionPane optionPane = new JOptionPane(
                                    "The only way to close this dialog is by\n"
                                    + "pressing one of the following buttons.\n"
                                    + "Do you understand?",
                                    JOptionPane.QUESTION_MESSAGE,
                                    JOptionPane.YES_NO_OPTION);

        final JDialog dialog = new JDialog(frame,
                                                 "Click a button",
                                                 true);
        dialog.setContentPane(optionPane);
        dialog.setDefaultCloseOperation(
                        JDialog.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener( new WindowAdapter()
          {
          public void windowClosing(WindowEvent we)
            {
            setLabel("Thwarted user attempt to close window.");
            }
          });
          
        optionPane.addPropertyChangeListener(
                        new PropertyChangeListener()
          {

                            public void propertyChange(PropertyChangeEvent e) {
                                String prop = e.getPropertyName();

                                if (dialog.isVisible()
                                 && (e.getSource() == optionPane)
                                 && (JOptionPane.VALUE_PROPERTY.equals(prop))) {
                                    //If you were going to check something
                                    //before closing the window, you'd do
                                    //it here.
                                    dialog.setVisible(false);
                                }
                            }
                        });
                    dialog.pack();
                    dialog.setLocationRelativeTo(frame);
                    dialog.setVisible(true);

                    int value = ((Integer)optionPane.getValue()).intValue();
                    if (value == JOptionPane.YES_OPTION) {
                        setLabel("Good.");
                    } else if (value == JOptionPane.NO_OPTION) {
                        setLabel("Try using the window decorations "
                                 + "to close the non-auto-closing dialog. "
                                 + "You can't!");
                    } else {
                        setLabel("Window unavoidably closed (ESC?).");
                    }

                //non-auto-closing dialog with custom message area
                //NOTE: if you don't intend to check the input,
                //then just use showInputDialog instead.
                } else if (command == customOptionCommand) {
                    customDialog.setLocationRelativeTo(frame);
                    customDialog.setVisible(true);

                    String s = customDialog.getValidatedText();
                    if (s != null) {
                        //The text is valid.
                        setLabel("Congratulations!  "
                                 + "You entered \""
                                 + s
                                 + "\".");
                    }

                //non-modal dialog
                } else if (command == nonModalCommand) {
                    //Create the dialog.
                    final JDialog dialog = new JDialog(frame,
                                                       "A Non-Modal Dialog");

                    //Add contents to it. It must have a close button,
                    //since some L&Fs (notably Java/Metal) don't provide one
                    //in the window decorations for dialogs.
                    JLabel label = new JLabel("<html><p align=center>"
                        + "This is a non-modal dialog.<br>"
                        + "You can have one or more of these up<br>"
                        + "and still use the main window.");
                    label.setHorizontalAlignment(JLabel.CENTER);
                    Font font = label.getFont();
                    label.setFont( label.getFont().deriveFont( Font.PLAIN,
                                                             14.0f));

                    JButton closeButton = new JButton("Close");
                    closeButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            dialog.setVisible(false);
                            dialog.dispose();
                        }
                    });
                    JPanel closePanel = new JPanel();
                    closePanel.setLayout(new BoxLayout(closePanel,
                                                       BoxLayout.LINE_AXIS));
                    closePanel.add(Box.createHorizontalGlue());
                    closePanel.add(closeButton);
                    closePanel.setBorder(BorderFactory.
                        createEmptyBorder(0,0,5,5));

                    JPanel contentPane = new JPanel(new BorderLayout());
                    contentPane.add(label, BorderLayout.CENTER);
                    contentPane.add(closePanel, BorderLayout.PAGE_END);
                    contentPane.setOpaque(true);
                    dialog.setContentPane(contentPane);

                    //Show it.
                    dialog.setSize(new Dimension(300, 150));
                    dialog.setLocationRelativeTo(frame);
                    dialog.setVisible(true);
                }
            }
        });

        return createPane(moreDialogDesc + ":",
                          radioButtons,
                          showItButton);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("DialogDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        DialogDemo newContentPane = new DialogDemo(frame);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}
