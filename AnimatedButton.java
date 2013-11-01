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

    public AnimatedButton(GUIContext guic, Animation animation, int x, int y,
            StateBasedGame sbg, int stateID) throws SlickException {
        super(guic, animation.getImage(1), x, y);
        super.setMouseDownColor(Color.red);
        super.setMouseOverColor(Color.blue);
        this.animation = animation;
        this.sbg = sbg;
        this.stateID = stateID;

        inactiveButton = new Image("data/inactive.png");
        activeButton = new Image("data/active.png");
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        if (sbg.getCurrentStateID() == stateID) {
            if (isMouseOver() && !lastMouseOver && !isActivated()) {
                // SoundManager.getInstance().getButtonOver().play(1, (float) .2);
                lastMouseOver = true;
            } else if (!isMouseOver()) {
                lastMouseOver = false;
            }
        }
        super.mouseMoved(oldx, oldy, newx, newy);
    }

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

    public boolean isActivated() {
        return activated;
    }

    protected void setActivated(boolean b) {
        activated = b;
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        if (isMouseOver() && sbg.getCurrentStateID() == stateID && !isActivated()) {
            this.setActivated(true);
            //SoundManager.getInstance().getButtonClick().play();
            for (ButtonAction action : actions) {
                action.perform();
            }
        }
        super.mouseClicked(button, x, y, clickCount);
    }

    public void add(ButtonAction action) {
        System.out.println("e");
        actions.add(action);
    }
}