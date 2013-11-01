package viral;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * The class for the game itself.
 *
 * @author Tim Clancy
 * @version 10.31.2013
 */
public class Game extends StateBasedGame {

    static final String gameName = "Viral";
    static final int menu = 0;
    static final int play = 1;

    public Game() {
        super(gameName);
        this.addState(new Menu(menu));
        this.addState(new Play(play));
    }

    /**
     *
     * Initializes the different "pages" of the game.
     *
     * @param gc The GameContainer, a specific class to contain much of nuts and
     * bolts involved.
     * @throws SlickException A specific series of OpenGL errors when rendering
     * may fail.
     */
    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        this.getState(menu).init(gc, this);
        this.getState(play).init(gc, this);
        this.enterState(menu);
    }

    /**
     *
     * The main method to actually drive this game and all associated loops.
     *
     * @param args Command-line arguments, useless here.
     */
    public static void main(String[] args) {
        try {
            AppGameContainer appgc = new AppGameContainer(new Game());
            appgc.setDisplayMode(960, 320, false);
            appgc.setVSync(true);
            appgc.setShowFPS(false);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}