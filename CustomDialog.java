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
import javax.swing.JTextField;
import java.beans.*; //property change stuff
import java.awt.*;
import java.awt.event.*;



class CustomDialog extends JDialog
                   implements ActionListener,
                              PropertyChangeListener
  {
  // It needs to have a version UID since it's
  // serializable.
  public static final long serialVersionUID = 1;
  private String typedText = null;
  private JTextField textField;
  private DialogDemo dd;
  private String magicWord;
  private JOptionPane optionPane;
  private String btnString1 = "Enter";
  private String btnString2 = "Cancel";



  public String getValidatedText()
    {
    return typedText;
    }



  public CustomDialog( Frame aFrame,
                       String aWord,
                       DialogDemo parent )
    {
    super( aFrame, true );
    dd = parent;

    magicWord = aWord.toUpperCase();
    setTitle( "Quiz" );

    textField = new JTextField( 10 );

    String msgString1 = "What was Dr. SEUSS's real last name?";
    String msgString2 = "(The answer is \"" + magicWord
                                            + "\". )";
    Object[] array = { msgString1, msgString2, textField };
    Object[] options = {btnString1, btnString2};

    optionPane = new JOptionPane(array,
                             JOptionPane.QUESTION_MESSAGE,
                             JOptionPane.YES_NO_OPTION,
                             null,
                             options,
                             options[0] );

    setContentPane( optionPane );
    setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );

    addWindowListener( new WindowAdapter()
      {
      public void windowClosing( WindowEvent we )
        {
        optionPane.setValue( new Integer(
                                JOptionPane.CLOSED_OPTION ));

        }
      });

    addComponentListener( new ComponentAdapter()
      {
      public void componentShown( ComponentEvent ce )
        {
        textField.requestFocusInWindow();
        }
      });

    textField.addActionListener( this );

    optionPane.addPropertyChangeListener( this );
    }




  public void actionPerformed( ActionEvent e )
    {
    optionPane.setValue( btnString1 );
    }



  public void propertyChange( PropertyChangeEvent e )
    {
    String prop = e.getPropertyName();

    if( isVisible() &&
        (e.getSource() == optionPane) &&
        (JOptionPane.VALUE_PROPERTY.equals( prop ) ||
         JOptionPane.INPUT_VALUE_PROPERTY.equals( prop )))
      {
      Object value = optionPane.getValue();

      if( value == JOptionPane.UNINITIALIZED_VALUE )
        {
        return;
        }

      optionPane.setValue(
                    JOptionPane.UNINITIALIZED_VALUE );

      if( btnString1.equals( value ))
        {
        typedText = textField.getText();
        String ucText = typedText.toUpperCase();
        if( magicWord.equals( ucText ))
          {
          clearAndHide();
          }
        else
          {
          textField.selectAll();
          JOptionPane.showMessageDialog(
                           CustomDialog.this,
                           "Sorry, \"" + typedText + "\" " +
                           "isn't a valid response.\n" +
                           "Please enter " +
                           magicWord + ".",
                           "Try again",
                           JOptionPane.ERROR_MESSAGE );

          typedText = null;
          textField.requestFocusInWindow();
          }
        }
      else
        {
        dd.setLabel( "It's OK.  " +
                     "We won't force you to type " +
                     magicWord + "." );

        typedText = null;
        clearAndHide();
        }
      }
    }



  public void clearAndHide()
    {
    textField.setText( null );
    setVisible( false );
    }

  }
