package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.avatar.Avatar;
import pepse.world.avatar.EnergyUI;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.Terrain;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;

import java.awt.*;
import java.util.List;

import static pepse.world.Sky.create;

public class PepseGameManager extends GameManager {
    private static final float WINDOW_WIDTH = 1200;
    private static final float WINDOW_HEIGHT = 690f;
    private WindowController windowController;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private Vector2 windowDimensions;

    private GameObject sky;
    private GameObject night;

    public static final String GROUND_TAG = "ground";
    public static final String TRUNK_TAG = "trunk";
    public static final String LEAF_TAG = "leaf";

    public static final Color BROWN = new Color(100, 50, 20);
    public static final Color GREEN = new Color(50, 200, 30);
    public PepseGameManager(String windowTitle, Vector2 windowDimension) {
        super(windowTitle, windowDimension);
    }

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        this.inputListener = inputListener;

        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.sky = create(windowDimensions);
        gameObjects().addGameObject(this.sky, Layer.BACKGROUND);

        Terrain terrain = new Terrain(windowDimensions, 0);
        List<GameObject> blocks = terrain.createInRange(-1000, 2000);
        for (GameObject obj : blocks) {
            gameObjects().addGameObject(obj, Layer.STATIC_OBJECTS);
        }

        this.night = Night.create(windowDimensions, 30f);
        gameObjects().addGameObject(this.night, 10);

        GameObject sun = Sun.create(windowDimensions, 30f);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);

        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);


        // 3. Test Trees via Flora (using dummy ground height Y = 1000f)
        Flora flora = new Flora(x -> terrain.groundHeightAt(x));
        List<GameObject> treeParts = flora.createInRange(-1000, 1000);

        for (GameObject part : treeParts) {
            if (part.getTag().equals(PepseGameManager.TRUNK_TAG)) {
                // Trunks belong in solid static layer
                gameObjects().addGameObject(part, Layer.DEFAULT);
            } else {
                // Leaves belong in a visual, non-colliding layer
                gameObjects().addGameObject(part, Layer.DEFAULT);
            }
        }

        // 4. Create your Avatar
        var avatar = new Avatar(Vector2.of(0, 900), inputListener, imageReader);
        gameObjects().addGameObject(avatar);

        // 5. Create EnergyUI
        var energyUI = new EnergyUI(avatar::getEnergy);
        gameObjects().addGameObject(energyUI, Layer.UI);

        // 6. Camera tracking
        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(), windowController.getWindowDimensions()));
    }

    @Override
    public void update(float timeDelta) {
        super.update(timeDelta);
    }

    public static void main(String[] args) {
        System.out.println("Pepse");
        PepseGameManager manager = new PepseGameManager(
                "Pepse",
                new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT));
        manager.run();
    }
}
