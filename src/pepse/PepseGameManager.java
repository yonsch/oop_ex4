package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.util.List;

import static pepse.world.Sky.create;

public class PepseGameManager extends GameManager {
    private static final float WINDOW_WIDTH = 700f;
    private static final float WINDOW_HEIGHT = 500f;
    private WindowController windowController;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private Vector2 windowDimensions;

    private GameObject sky;

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
        List<GameObject> blocks = terrain.createInRange(0, (int)WINDOW_WIDTH);
        for (GameObject obj : blocks) {
            gameObjects().addGameObject(obj, Layer.STATIC_OBJECTS);
        }
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
