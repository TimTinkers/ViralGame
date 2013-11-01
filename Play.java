package viral;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * The class for the play screen of the game.
 *
 * @author Tim Clancy
 * @version 10.31.2013
 */
public class Play extends BasicGameState {

    private int xpos = 50;
    private int ypos = 50;
    private int turn = 0;
    private int resourceCredits = 0;
    private int selected = -1;
    private int warning = 0;
    private int start = 0;
    private boolean debuff = false;
    private Random random = new Random();
    private float difficulty = 10 + random.nextInt(10);
    private String advanceMessage = "Proceed to Next Day";
    private Image infected;
    private Image clean;
    private Image airport;
    private Image army;
    private Image armyDamaged1;
    private Image armyDamaged2;
    private Image armyDamaged3;
    private Image none;
    private Image intro;
    private Image vaccine;
    private Image armywin;
    private Image infection;
    private Tileset background;
    private Tileset airports;
    private Tileset bases;
    private Rectangle button = new Rectangle(640, 250, 200, 32);
    private BuildButton military;
    private ArrayList<BuildButton> buttons = new ArrayList<BuildButton>();
    private BuildButton barrier;
    private BuildButton nuke;
    private Image orange;
    private Music oscillation;
    private boolean debuff2 = false;
    private int index;
    private int volume;
    private int opacity;
    private int opacity1;
    private int opacity2;

    Play(int state) {
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        clean = new Image("data/yellow.png"); //0
        infected = new Image("data/purple.png"); //1
        airport = new Image("data/plane.png"); //2
        army = new Image("data/green.png"); //3
        armyDamaged1 = new Image("data/greenDamaged1.png");
        armyDamaged2 = new Image("data/greenDamaged2.png");
        armyDamaged3 = new Image("data/greenDamaged3.png");
        none = new Image("data/grey.png"); //4
        orange = new Image("data/orange.png"); //5

        intro = new Image("data/intro.png");
        vaccine = new Image("data/vaccine.png");
        infection = new Image("data/infection.png");
        armywin = new Image("data/armywin.png");

        background = new Tileset(20, 10);
        airports = new Tileset(background.getWidth(), background.getHeight());
        bases = new Tileset(background.getWidth(), background.getHeight());

        //Populate airport array.
        placeAirports();

        //Create buttons.
        SpriteSheet selectionSheet = new SpriteSheet("data/selection.png", 49, 37);
        Animation selection = new Animation();
        selection.addFrame(selectionSheet.getSprite(0, 0), 650);
        selection.addFrame(selectionSheet.getSprite(1, 0), 650);

        barrier = new BuildButton(gc, selection, 650, gc.getHeight() - 175, sbg, 1);
        buttons.add(barrier);
        barrier.add(new ButtonAction() {
            @Override
            public void perform() {
                clearButtons(barrier);
                selected = 5;
            }
        });

        nuke = new BuildButton(gc, selection, 650, gc.getHeight() - 275, sbg, 1);
        buttons.add(nuke);
        nuke.add(new ButtonAction() {
            @Override
            public void perform() {
                clearButtons(nuke);
                selected = 4;
            }
        });

        military = new BuildButton(gc, selection, 650, gc.getHeight() - 225, sbg, 1);
        buttons.add(military);
        military.add(new ButtonAction() {
            @Override
            public void perform() {
                clearButtons(military);
                selected = 3;
            }
        });
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        if (start == 0) {
            g.drawImage(intro, 0, 0);
            g.drawRect(900, 280, 40, 20);
            g.drawString("Go!", 910, 285);
        } else {
            if (opacity != 0
                    || opacity1 != 0
                    || opacity2 != 0) {
                //Render the win/lose screen
                g.drawImage(vaccine, 0, 0, new Color(255, 255, 255, opacity));
                g.drawImage(infection, 0, 0, new Color(255, 255, 255, opacity1));
                g.drawImage(armywin, 0, 0, new Color(255, 255, 255, opacity2));
            } else {
                //Render buttons and things that don't change. 
                g.drawRect((float) button.getX(), (float) button.getY(), (float) button.getWidth(), (float) button.getHeight());
                g.drawString(advanceMessage, 656, 258);
                g.drawString("First infection was " + turn + " days ago.", 640, 300);
                g.drawString("The difficulty rating is: " + (difficulty - 9) + "/10.", 640, 220);
                g.drawString("You have " + resourceCredits + " resource points.", 640, 10);
                g.drawString("Military: 150", military.getX() + military.getWidth() + 10, military.getY() + 5);
                g.drawString("Nuke: 200", nuke.getX() + nuke.getWidth() + 10, nuke.getY() + 5);
                g.drawString("Road Block: 75", barrier.getX() + barrier.getWidth() + 10, barrier.getY() + 5);

                //Render the out of money warning.
                if (warning == 1) {
                    g.drawString("Not enough resources for that!", 640, gc.getHeight() - 35);
                }

                //Render the resource purchase buttons.
                for (BuildButton b : buttons) {
                    b.render(gc, g);
                }

                //Loop for rendering the playing field.
                for (int i = 0; i < background.getWidth(); ++i) {
                    for (int j = 0; j < background.getHeight(); ++j) {
                        if (background.getTile(i, j) == 0) {
                            clean.draw(i * 32, 320 - j * 32 - 32);
                        }
                        if (background.getTile(i, j) == 1) {
                            infected.draw(i * 32, 320 - j * 32 - 32);
                        }
                        if (background.getTile(i, j) == 2) {
                            airport.draw(i * 32, 320 - j * 32 - 32);
                        }
                        if (background.getTile(i, j) == 3) {
                            if (bases.getTile(i, j) == 4) {
                                army.draw(i * 32, 320 - j * 32 - 32);
                            } else if (bases.getTile(i, j) == 3) {
                                armyDamaged1.draw(i * 32, 320 - j * 32 - 32);
                            } else if (bases.getTile(i, j) == 2) {
                                armyDamaged2.draw(i * 32, 320 - j * 32 - 32);
                            } else if (bases.getTile(i, j) == 1) {
                                armyDamaged3.draw(i * 32, 320 - j * 32 - 32);
                            } else {
                                background.setTile(i, j, 0);
                            }
                        }
                        if (background.getTile(i, j) == 4) {
                            none.draw(i * 32, 320 - j * 32 - 32);
                        }
                        if (background.getTile(i, j) == 5) {
                            orange.draw(i * 32, 320 - j * 32 - 32);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        //Dynamic music volume
        if (oscillation != null) {
            oscillation.setVolume(0.5F / volume);
        }

        //Win/loss conditions.
        if (turn > 100 && !debuff2) {//Time victory, made it to vaccine.
            debuff2 = true;
            endGame(0, sbg);
        } else {
            if (background.search(1).size() == background.getHeight() * background.getWidth() && debuff2 == false) { //Lost, overran.
                debuff2 = true;
                endGame(1, sbg);
            }
            if (background.search(1).isEmpty() && debuff2 == false && turn != 0) { //The infection was eradicated.
                debuff2 = true;
                endGame(2, sbg);
            }
        }

        xpos = Mouse.getX();
        ypos = Mouse.getY();
        //If the mouse is clicked.
        if (Mouse.isButtonDown(0)
                && !debuff) {
            if (xpos > 900
                    && xpos < 940
                    && ypos < gc.getHeight() - 280
                    && ypos > gc.getHeight() - 300
                    && start == 0) {
                start++;
                oscillation = new Music("data/oscillation.ogg");
                oscillation.loop();
            }
            debuff = true;
            System.out.println(selected);
            //If the progress turn button got clicked.
            if (xpos > 640
                    && ypos > gc.getHeight() - 250 - 32
                    && xpos < 840
                    && ypos < gc.getHeight() - 250) {
                //Button clicked, begin game/advance time.
                System.out.println(turn);

                //If it's the first turn, simply create the first infected area.
                if (turn == 0) {
                    int seedX = random.nextInt(background.getWidth());
                    int seedY = random.nextInt(background.getHeight());
                    background.setTile(seedX, seedY, 1);
                } else { //Otherwise, spread the infection and ask the user for options.
                    takeTurn();
                }
                ++turn;
            } else { //If anything not the next turn button got clicked.
                //If we're in the management side of things.
                if (xpos > 640) {
                    int strikes = 0;
                    for (BuildButton b : buttons) {
                        if (!b.isMouseOver()) {
                            ++strikes;
                        }
                    }
                    if (strikes == buttons.size()) {
                        clearButtons();
                        selected = -1;
                    }
                } else { //If we aren't in the management side of things.
                    int gridX = xpos / 32;
                    int gridY = ypos / 32;
                    if (selected != -1) {
                        if (selected == 3) { //Military
                            if (resourceCredits - 150 >= 0) { //If there's enough money.
                                background.setTile(gridX, gridY, selected);
                                bases.setTile(gridX, gridY, 4);
                                airports.setTile(gridX, gridY, 0);
                                resourceCredits -= 150;
                                clearButtons();
                                selected = 1 * (-1);
                            } else {
                                clearButtons();
                                selected = 1 * (-1);
                                flickerResources();
                            }
                        }
                        if (selected == 4) { //Nuke
                            if (resourceCredits - 200 >= 0) {
                                nuke(gridX, gridY);
                                resourceCredits -= 200;
                                clearButtons();
                                selected = 1 * (-1);
                            } else {
                                clearButtons();
                                selected = 1 * (-1);
                                flickerResources();
                            }
                        }
                        if (selected == 5) { //Roadblock
                            if (resourceCredits - 75 >= 0) {
                                if (background.getTile(gridX, gridY) == 0) { //Roadblocks can only be placed on clean tiles.
                                    background.setTile(gridX, gridY, selected);
                                    bases.setTile(gridX, gridY, 0);
                                    airports.setTile(gridX, gridY, 0);
                                    resourceCredits -= 75;
                                    clearButtons();
                                    selected = 1 * (-1);
                                }
                            } else {
                                clearButtons();
                                selected = 1 * (-1);
                                flickerResources();
                            }
                        }
                    }
                }
            }
        }
        if (!Mouse.isButtonDown(0)) {
            debuff = false;
            advanceMessage = "Proceed to Next Day";
            button.width = 200;
        }
    }

    @Override
    public int getID() {
        return 1;
    }

    private void spread() {
        ArrayList<Vector2> infectedList = background.search(1);
        for (Vector2 i : infectedList) {
            int x = i.getX();
            int y = i.getY();

            //If any of the tiles getting infected are airports, spread.
            if (airports.getTile(x, y) == 2) {
                infectAirports();
            }

            if (!background.hasAdjacentTile(x, y, 3, 2)) { //If no military base nearby.
                //If the tile isn't out of bounds, give it a 25% chance of being infected.
                if (x != 0
                        && random.nextInt(4) == 0) {
                    if (background.getTile(x - 1, y) == 5) { //If a roadblock is present, reduce infection rate for the tile to 5%.
                        if (random.nextInt(5) == 0) {
                            background.setTile(x - 1, y, 1);
                        }
                    } else {
                        background.setTile(x - 1, y, 1);
                    }
                }
                if (y != 0
                        && random.nextInt(4) == 0) {
                    if (background.getTile(x, y - 1) == 5) {
                        if (random.nextInt(5) == 0) {
                            background.setTile(x, y - 1, 1);
                        }
                    } else {
                        background.setTile(x, y - 1, 1);
                    }
                }
                if (x != background.getWidth() - 1
                        && random.nextInt(4) == 0) {
                    if (background.getTile(x + 1, y) == 5) {
                        if (random.nextInt(5) == 0) {
                            background.setTile(x + 1, y, 1);
                        }
                    } else {
                        background.setTile(x + 1, y, 1);
                    }
                }
                if (y != background.getHeight() - 1
                        && random.nextInt(4) == 0) {
                    if (background.getTile(x, y + 1) == 5) {
                        if (random.nextInt(5) == 0) {
                            background.setTile(x, y + 1, 1);
                        }
                    } else {
                        background.setTile(x, y + 1, 1);
                    }
                }
            }
        }
    }

    private void infectAirports() {
        for (int i = 0; i < airports.getWidth(); ++i) {
            for (int j = 0; j < airports.getHeight(); ++j) {
                if (airports.getTile(i, j) == 2
                        && background.getTile(i, j) == 2) {
                    background.setTile(i, j, 1);
                    airports.setTile(i, j, 1);
                }
            }
        }
    }

    private void placeAirports() {
        int aiportCount = 0;
        while (aiportCount < 3) {
            for (int i = 0; i < background.getWidth(); ++i) {
                for (int j = 0; j < background.getHeight(); ++j) {
                    if (random.nextInt(100) == 0
                            && background.getTile(i, j) != 2
                            && !background.hasAdjacentTile(i, j, 2, 3)) {
                        ++aiportCount;
                        airports.setTile(i, j, 2);
                        background.setTile(i, j, 2);
                    }
                }
            }
        }
    }

    private void takeTurn() {
        if (turn > 4) {
            resourceCredits += (random.nextInt(10) / difficulty) * ((background.getHeight() * background.getWidth()) - (background.search(1).size() + background.search(4).size()));
        }
        if (turn == 4) {
            resourceCredits += ((background.getHeight() * background.getWidth()) - (background.search(1).size() + background.search(4).size()));
        }
        spread();
        updateBases();
        selected = -1;
    }

    private void updateBases() {
        for (int i = 0; i < bases.getWidth(); ++i) {
            for (int j = 0; j < bases.getHeight(); ++j) {
                if (bases.getTile(i, j) != 0
                        && background.hasAdjacentTile(i, j, 1, 2)) {
                    bases.setTile(i, j, bases.getTile(i, j) - 1);
                }
            }
        }
    }

    private void nuke(int x, int y) {
        background.setTile(x, y, 4);
        bases.setTile(x, y, 0);
        if (x != 0) {
            background.setTile(x - 1, y, 4);
            bases.setTile(x - 1, y, 0);
        }
        if (y != 0) {
            background.setTile(x, y - 1, 4);
            bases.setTile(x, y - 1, 0);
        }
        if (x != background.getWidth() - 1) {
            background.setTile(x + 1, y, 4);
            bases.setTile(x + 1, y, 0);
        }
        if (y != background.getHeight() - 1) {
            background.setTile(x, y + 1, 4);
            bases.setTile(x, y + 1, 0);
        }
        if (x != 0
                && y != 0
                && random.nextInt(4) == 0) {
            background.setTile(x - 1, y - 1, 4);
            bases.setTile(x - 1, y - 1, 0);
        }
        if (x != 0
                && y != background.getHeight() - 1
                && random.nextInt(4) == 0) {
            background.setTile(x - 1, y + 1, 4);
            bases.setTile(x - 1, y + 1, 0);
        }
        if (x != background.getWidth() - 1
                && y != 0
                && random.nextInt(4) == 0) {
            background.setTile(x + 1, y - 1, 4);
            bases.setTile(x + 1, y - 1, 0);
        }
        if (x != background.getWidth() - 1
                && y != background.getHeight() - 1
                && random.nextInt(4) == 0) {
            background.setTile(x + 1, y + 1, 4);
            bases.setTile(x + 1, y + 1, 0);
        }
    }

    private void clearButtons() {
        for (BuildButton b : buttons) {
            b.setActivated(false);
        }
    }

    private void clearButtons(BuildButton b) {
        for (BuildButton bT : buttons) {
            if (!bT.equals(b)) {
                bT.setActivated(false);
            }
        }
    }

    private void flickerResources() {
        warning = 1;
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                warning = 0;
            }
        };
        timer.schedule(task, 3000);
    }

    private void endGame(int i, final StateBasedGame sbg) {
        if (i == 0) {
            //Discovered vaccine
            index = 0;
            volume = 0;
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (index > 100) {
                        oscillation.stop();
                        this.cancel();
                    }
                    opacity += 10;
                    ++index;
                    volume += 0.1;
                }
            };
            timer.scheduleAtFixedRate(task, 0, 100);
        }
        if (i == 1) {
            //Overrun
            index = 0;
            volume = 0;
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (index > 100) {
                        oscillation.stop();
                        this.cancel();
                    }
                    opacity1 += 10;
                    ++index;
                    volume += 0.1;
                }
            };
            timer.scheduleAtFixedRate(task, 0, 100);
        }
        if (i == 2) {
            //Eradicated zombie menace
            index = 0;
            volume = 0;
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (index > 100) {
                        oscillation.stop();
                        this.cancel();
                    }
                    opacity2 += 10;
                    ++index;
                    volume += 0.1;
                }
            };
            timer.scheduleAtFixedRate(task, 0, 100);
        }
    }
}