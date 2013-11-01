package viral;

import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.StateBasedGame;

public class BuildButton extends AnimatedButton {

    //List of all buttons in program
    private static List<BuildButton> buttons = new ArrayList<BuildButton>();

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
    @SuppressWarnings("LeakingThisInConstructor")
    public BuildButton(GUIContext guic, Animation animation, int x, int y, StateBasedGame sbg, int stateID)
            throws SlickException {
        super(guic, animation, x, y, sbg, stateID);
        buttons.add(this);
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
        if (!isMouseOver()) {
            //Only one button may be chosen at a time.
            for (BuildButton b : buttons) {
                if (b.isMouseOver()) {
                    b.setActivated(false);
                    break;
                }
            }
        }
        super.mouseClicked(button, x, y, clickCount);
    }
}