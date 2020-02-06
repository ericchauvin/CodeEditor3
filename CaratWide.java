// This is modified from Oracle's example/tutorial code, so
// it has to retain their copyright notice, etc.
// This has been modified from the original tutorial code
// to make it so that I can see it with my bad vision,
// and other changes.

// "Redistribution ... permitted provided that the
// following conditions are met..."

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



import javax.swing.text.DefaultCaret;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.plaf.TextUI;




public class CaretWide extends DefaultCaret
  {
  // It needs to have a version UID since it's
  // serializable.
  public static final long serialVersionUID = 2;
  private final int drawWidth = 8;



  public CaretWide()
    {
    }



  protected synchronized void damage( Rectangle r )
    {
    if( r == null )
      return;

    int damageWidth = drawWidth;
    x = r.x - 4 - (damageWidth >> 1);
    y = r.y;
    width = 9 + damageWidth;
    height = r.height;

    repaint();
    }




  public void paint( Graphics g )
    {
    if( !isVisible() )
      return;

    try
    {
    TextUI mapper = getComponent().getUI();
    Rectangle r = mapper.modelToView( getComponent(),
                                      getDot(),
                                      getDotBias() );

    if( r == null )
      return;

    if( (r.width <= 0) && (r.height <= 0) )
      return;

    if( !contains( r ) )
      {
      // It got out of sync and needs the right location?
      Rectangle clip = g.getClipBounds();
      if( clip != null )
        {
        if( !clip.contains( this ))
          {
          repaint();
          }
        }

      damage( r );
      }

    g.setColor( getComponent().getCaretColor() );
    r.x -= drawWidth  >> 1;
    g.fillRect( r.x, r.y, drawWidth, r.height );
    }
    catch( Exception e )
      {
      // mApp.snowStatus()
      }
    }



  }
