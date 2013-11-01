package viral;

import java.util.Timer;
import java.util.TimerTask;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * The class for the menu screen of the game.
 *
 * @author Tim Clancy
 * @version 10.31.2013
 */
public class Menu extends BasicGameState {

    private int xpos = 50;
    private int ypos = 50;
    private int opacity;
    private int opacity1;
    private int opacity2;
    private int opacity3;
    private int index = 0;
    private int index2 = 0;
    private float volume = 0;
    private Image screen;
    private Image slide1;
    private Image slide2;
    private Image slide3;
    private boolean debuff = false;
    private Music openingMenuMusic;
    private Music storyMusic;

    Menu(int state) {
    }

    /**
     *
     * Initializes variables and other bits of data while this page loads but
     * before its loops start to run.
     *
     * @param gc The GameContainer for the entire program.
     * @param sbg A more specific object for swapping between pages.
     * @throws SlickException A specific flavor of OpenGL exception.
     */
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        screen = new Image("data/screen.png");
        slide1 = new Image("data/slide1.png");
        slide2 = new Image("data/slide2.png");
        slide3 = new Image("data/slide3.png");

        openingMenuMusic = new Music("data/darkness.ogg");
        openingMenuMusic.loop();
        openingMenuMusic.setVolume(0.5F);

        storyMusic = new Music("data/edgen.ogg");
    }

    /**
     *
     * The render loop, responsible for taking things and putting them on the
     * screen.
     *
     * @param gc The GameContainer for the entire program.
     * @param sbg A more specific object for swapping between pages.
     * @param g The Graphic object, used to actually draw things.
     * @throws SlickException A specific flavor of OpenGL exception.
     */
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        if (opacity1 <= 0
                && opacity2 <= 0
                && opacity3 <= 0) {
            //Render buttons and labels.
            g.drawString("Life as we know it is on the", 50, 50);
            g.drawString("brink of destruction. Humanity", 50, 75);
            g.drawString("must survive this virus.", 50, 100);
            g.drawRect(50, 150, 100, 32);
            g.drawString("Play Viral", 54, 158);
        }

        //Render the fade-out.
        g.drawImage(screen, 0, 0, new Color(255, 255, 255, opacity));

        //Render all the slides of the introduction.
        g.drawImage(slide1, 0, 0, new Color(255, 255, 255, opacity1));
        g.drawImage(slide2, 0, 0, new Color(255, 255, 255, opacity2));
        g.drawImage(slide3, 0, 0, new Color(255, 255, 255, opacity3));
    }

    /**
     *
     * The update loop, run just as frequently as the rendering one, but used
     * instead to drive forward game logic.
     *
     * @param gc The GameContainer for the entire program.
     * @param sbg A more specific object for swapping between pages.
     * @param delta The time between updates, this matches the frame rate of the
     * renderer.
     * @throws SlickException A specific flavor of OpenGL exception.
     */
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        openingMenuMusic.setVolume(0.5F / volume);
        xpos = Mouse.getX();
        ypos = Mouse.getY();
        if (Mouse.isButtonDown(0)
                && !debuff) {
            debuff = true;
            if (xpos > 50
                    && ypos > gc.getHeight() - 150 - 32
                    && xpos < 150
                    && ypos < gc.getHeight() - 100) {
                //Button clicked, switch to play view.
                fade(sbg);
            }
        }
        if (!Mouse.isButtonDown(0)) {
            debuff = false;
        }
    }

    /**
     *
     * Gets the ID of this page of the program.
     *
     * @return 0, for the main menu.
     */
    @Override
    public int getID() {
        return 0;
    }

    /**
     *
     * A private method to fade to black when the continue button is pressed.
     *
     * @param sbg The state of the game, needed to eventually switch to play
     * mode.
     */
    private void fade(final StateBasedGame sbg) {
        index = 0;
        volume = 0;
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (index > 30) {
                    openingMenuMusic.stop();
                    fade2(sbg);
                    this.cancel();
                }
                opacity += 10;
                ++index;
                volume += 0.1;
            }
        };
        timer.scheduleAtFixedRate(task, 0, 100);
    }

    /**
     *
     * A secondary fader to switch between the three slides of the introduction
     * sequence.
     *
     * @param sbg The state of the game, needed to switch to the playing screen.
     */
    private void fade2(final StateBasedGame sbg) {
        index2 = 0;
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (index2 == 0) {
                    storyMusic.play();
                }
                if (index2 > 0
                        && index2 < 10) {
                    opacity -= 26;
                    opacity1 += 26;
                }
                if (index2 > 90
                        && index2 < 100) {
                    opacity1 -= 26;
                    opacity2 += 26;
                }
                if (index2 > 190
                        && index2 < 200) {
                    opacity2 -= 26;
                    opacity3 += 26;
                }
                if (index2 > 300
                        && index2 < 330) {
                    opacity3 -= 9;
                    opacity += 9;
                }
                if (index2 > 340) {
                    storyMusic.stop();
                    sbg.enterState(1);
                    this.cancel();
                }
                ++index2;
            }
        };
        timer.scheduleAtFixedRate(task, 2000, 100);
    }
}