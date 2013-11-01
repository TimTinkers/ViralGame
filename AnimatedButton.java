package viral;

import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.StateBasedGame;

/**
 * The class for the animated resource buttons.
 *
 * @author Tim Clancy
 * @version 10.31.2013
 */
public class AnimatedButton extends MouseOverArea {

    private boolean activated = false;
    private boolean lastMouseOver = false;
    private final Animation animation;
    private final Image inactiveButton;
    private final Image activeButton;
    private final StateBasedGame sbg;
    private final int stateID;
    private final List<ButtonAction> actions = new ArrayList<ButtonAction>();

    /**
     *
     * Constructs a button.
     *
     * @param guic The GUIContext, to determine rendering layers.
     * @param animation An animation, via sprite sheet, to apply to the button.
     * @param x The x coordinate of the button on the screen.
     * @param y The y coordinate of the button on the screen.
     * @param sbg The page object to render this button on.
     * @param stateID The ID of the page object to render this button in.
     * @throws SlickException A special flavor of OpenGL screwup.
     */
    public AnimatedButton(GUIContext guic, Animation animation, int x, int y,
            StateBasedGame sbg, int stateID) throws SlickException {
        super(guic, animation.getImage(1), x, y);
        super.setMouseDownColor(Color.red);
        super.setMouseOverColor(Color.blue);
        this.animation = animation;
        this.sbg = sbg;
        this.stateID = stateID;

        //Load the images of the two button states.
        inactiveButton = new Image("data/inactive.png");
        activeButton = new Image("data/active.png");
    }

    /**
     *
     * Determines when the mouse has moved over this button.
     *
     * @param oldx Mouse's old x coordinate.
     * @param oldy Mouse's old y coordinate.
     * @param newx Mouses's new x coordinate.
     * @param newy Mouses's new y coordinate.
     */
    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        if (sbg.getCurrentStateID() == stateID) {
            if (isMouseOver() && !lastMouseOver && !isActivated()) {
                lastMouseOver = true;
            } else if (!isMouseOver()) {
                lastMouseOver = false;
            }
        }
        super.mouseMoved(oldx, oldy, newx, newy);
    }

    /**
     *
     * Renders the button on the screen.
     *
     * @param guic The GUI layer on which to render the button.
     * @param g The graphics object to draw the button.
     */
    @Override
    public void render(GUIContext guic, Graphics g) {
        if (activated) {
            g.drawImage(activeButton, getX() - 2, getY() - 2);
            g.drawAnimation(animation, getX() + 2, getY() + 2);
        } else {
            g.drawImage(inactiveButton, getX() - 3, getY() - 3);
            super.render(guic, g);
        }
    }

    /**
     *
     * Determines whether or not the button is selected.
     *
     * @return Whether or not the button is selected.
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     *
     * Sets the button's state of being selected.
     *
     * @param b Whether or not the button is selected.
     */
    protected void setActivated(boolean b) {
        activated = b;
    }

    /**
     *
     * Determines what happens when a button is clicked.
     *
     * @param button The button clicked.
     * @param x The x coordinate of the mouse.
     * @param y The y coordinate of the mouse.
     * @param clickCount The number of times the button was clicked.
     */
    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        if (isMouseOver() && sbg.getCurrentStateID() == stateID && !isActivated()) {
            this.setActivated(true);
            for (ButtonAction action : actions) {
                action.perform();
            }
        }
        super.mouseClicked(button, x, y, clickCount);
    }

    /**
     * 
     * Assigns the button something to do when clicked.
     * 
     * @param action The action to perform when clicked.
     */
    public void add(ButtonAction action) {
        actions.add(action);
    }
}