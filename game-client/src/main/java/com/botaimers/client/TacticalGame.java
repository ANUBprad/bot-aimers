package com.botaimers.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.botaimers.arena.*;
import com.botaimers.engine.Bot;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Isometric military sci-fi presentation. All movement, combat, and AI decisions
 * remain in PlayerBattle; this class only handles input and rendering.
 */
public final class TacticalGame extends ApplicationAdapter {
    private static final float HALF_W = 42f, HALF_H = 21f;
    private static final float CX = 480f, CY = 345f;
    private static final Color FLOOR_A = new Color(0.15f, 0.22f, 0.25f, 1);
    private static final Color FLOOR_B = new Color(0.18f, 0.26f, 0.29f, 1);
    private static final Color EDGE = new Color(0.28f, 0.42f, 0.43f, 1);
    private static final Color CYAN = new Color(0.22f, 0.87f, 0.84f, 1);
    private static final Color ORANGE = new Color(1f, 0.43f, 0.24f, 1);
    private static final Color DARK = new Color(0.045f, 0.075f, 0.10f, 1);
    private ShapeRenderer shapes;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private GridMap map;
    private ArenaGenerator.Arena arena;
    private long nextSeed;
    private PlayerBattle battle;
    private final MatchReporter reporter = new MatchReporter("http://localhost:8080");
    private volatile String networkStatus = "OFFLINE READY  /  CONNECT SERVER TO SAVE";
    private String feedback = "MISSION START";
    private boolean reported;
    private boolean paused;
    private float pulse;

    @Override public void create() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(960, 640, camera);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        shapes = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        nextSeed = ThreadLocalRandom.current().nextLong();
        reset();
    }

    private void reset() {
        arena = ArenaGenerator.generate(nextSeed++);
        map = arena.map();
        battle = new PlayerBattle(map,
                new Bot("Player", 60, 12, 3), arena.playerSpawn(),
                new Bot("CPU", 60, 10, 4), arena.enemySpawn());
        feedback = "MISSION " + arena.theme() + "  /  SEED " + arena.seed();
        networkStatus = "OFFLINE READY  /  CONNECT SERVER TO SAVE";
        reported = false;
        paused = false;
    }

    @Override public void render() {
        pulse += Math.min(Gdx.graphics.getDeltaTime(), 0.05f);
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) reset();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
                || Gdx.input.isKeyJustPressed(Input.Keys.P)) paused = !paused;
        if (!paused && !battle.finished()) {
            PlayerBattle.Command command = null;
            // Grid directions stay unchanged; screen projection is purely visual.
            if (pressed(Input.Keys.W, Input.Keys.UP)) command = PlayerBattle.Command.UP;
            else if (pressed(Input.Keys.S, Input.Keys.DOWN)) command = PlayerBattle.Command.DOWN;
            else if (pressed(Input.Keys.A, Input.Keys.LEFT)) command = PlayerBattle.Command.LEFT;
            else if (pressed(Input.Keys.D, Input.Keys.RIGHT)) command = PlayerBattle.Command.RIGHT;
            else if (Gdx.input.isKeyJustPressed(Input.Keys.F)) command = PlayerBattle.Command.ATTACK;
            else if (Gdx.input.isKeyJustPressed(Input.Keys.G)) command = PlayerBattle.Command.GUARD;
            if (command != null) feedback = battle.takeTurn(command);
        }
        if (battle.finished() && !reported) {
            reported = true;
            networkStatus = "UPLOADING MISSION REPORT...";
            reporter.report(battle).thenAccept(status -> networkStatus = status);
        }

        ScreenUtils.clear(DARK);
        viewport.apply();
        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0.065f, 0.11f, 0.14f, 1);
        shapes.rect(0, 0, 960, 640);
        shapes.setColor(0.08f, 0.15f, 0.18f, 1);
        shapes.rect(0, 552, 960, 88);
        shapes.rect(0, 0, 960, 115);
        shapes.setColor(CYAN);
        shapes.rect(0, 549, 960, 2);
        shapes.rect(0, 113, 960, 2);

        // Painter order: back rows first, then foreground rows and actors.
        for (int y = 0; y < map.height(); y++) {
            for (int x = 0; x < map.width(); x++) {
                float sx = screenX(x, y), sy = screenY(x, y);
                diamond(sx, sy, ((x + y) & 1) == 0 ? FLOOR_A : FLOOR_B);
                diamondOutline(sx, sy, EDGE);
                if (!map.walkable(new GridPoint(x, y))) obstacle(sx, sy);
                if (battle.playerPosition().x() == x && battle.playerPosition().y() == y)
                    soldier(sx, sy, CYAN, true);
                if (battle.enemyPosition().x() == x && battle.enemyPosition().y() == y)
                    soldier(sx, sy, ORANGE, false);
            }
        }
        shapes.setColor(0.12f, 0.19f, 0.23f, 1);
        shapes.rect(28, 570, 302, 12);
        shapes.rect(630, 570, 302, 12);
        shapes.setColor(CYAN);
        shapes.rect(28, 570, 302f * battle.playerHealth() / 60f, 12);
        shapes.setColor(ORANGE);
        shapes.rect(630, 570, 302f * battle.enemyHealth() / 60f, 12);
        shapes.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.setColor(CYAN);
        font.draw(batch, "BOT-AIMERS  //  OPERATION IRON VEIL", 28, 623);
        font.setColor(Color.WHITE);
        font.draw(batch, "ALPHA UNIT   " + battle.playerHealth() + "/60 HP", 28, 592);
        font.draw(batch, "HOSTILE   " + battle.enemyHealth() + "/60 HP", 630, 592);
        font.draw(batch, "TURN " + battle.turns() + "  /  " + arena.theme() + "  /  " + map.width() + "x" + map.height(), 28, 526);
        font.draw(batch, "WASD / ARROWS : MOVE     F : STRIKE     G : GUARD     P / ESC : PAUSE     R : RESTART", 28, 86);
        font.setColor(CYAN);
        font.draw(batch, feedback, 28, 58);
        font.setColor(Color.LIGHT_GRAY);
        font.draw(batch, networkStatus, 28, 28);
        if (paused || battle.finished()) {
            font.setColor(Color.YELLOW);
            font.draw(batch, paused ? "MISSION PAUSED  /  P OR ESC TO RESUME"
                    : "MISSION COMPLETE  /  " + battle.winner() + " WINS  /  R TO RESTART", 280, 495);
        }
        batch.end();
    }

    private boolean pressed(int first, int second) {
        return Gdx.input.isKeyJustPressed(first) || Gdx.input.isKeyJustPressed(second);
    }

    private float screenX(int x, int y) {
        return CX + (x - y - (map.width() - map.height()) / 2f) * HALF_W;
    }

    private float screenY(int x, int y) {
        return CY + (x + y - (map.width() + map.height() - 2) / 2f) * HALF_H;
    }

    private void diamond(float x, float y, Color color) {
        shapes.setColor(color);
        shapes.triangle(x, y + HALF_H, x + HALF_W, y, x, y - HALF_H);
        shapes.triangle(x, y + HALF_H, x, y - HALF_H, x - HALF_W, y);
    }

    private void diamondOutline(float x, float y, Color color) {
        shapes.setColor(color);
        shapes.rectLine(x, y + HALF_H, x + HALF_W, y, 1);
        shapes.rectLine(x + HALF_W, y, x, y - HALF_H, 1);
        shapes.rectLine(x, y - HALF_H, x - HALF_W, y, 1);
        shapes.rectLine(x - HALF_W, y, x, y + HALF_H, 1);
    }

    private void obstacle(float x, float y) {
        shapes.setColor(0.07f, 0.12f, 0.15f, 1);
        shapes.rect(x - 23, y - 9, 46, 30);
        shapes.setColor(0.33f, 0.41f, 0.44f, 1);
        shapes.rect(x - 23, y + 12, 46, 9);
        shapes.setColor(0.20f, 0.27f, 0.31f, 1);
        shapes.rect(x - 20, y - 5, 40, 17);
        shapes.setColor(0.93f, 0.61f, 0.19f, 1);
        shapes.rect(x - 19, y + 15, 9, 3);
        shapes.rect(x + 10, y + 15, 9, 3);
    }

    private void soldier(float x, float y, Color accent, boolean friendly) {
        shapes.setColor(0.03f, 0.07f, 0.09f, 1);
        shapes.ellipse(x - 17, y - 10, 34, 13);
        shapes.setColor(0.17f, 0.22f, 0.24f, 1);
        shapes.rect(x - 12, y - 2, 9, 19);
        shapes.rect(x + 3, y - 2, 9, 19);
        shapes.setColor(friendly ? 0.21f : 0.33f, 0.29f, 0.30f, 1);
        shapes.rect(x - 15, y + 15, 30, 21);
        shapes.setColor(accent);
        shapes.rect(x - 12, y + 23, 24, 4);
        shapes.setColor(0.10f, 0.16f, 0.19f, 1);
        shapes.circle(x, y + 41, 11, 16);
        shapes.setColor(accent);
        shapes.rect(x - 7, y + 40, 14, 4);
        shapes.setColor(0.07f, 0.10f, 0.12f, 1);
        shapes.rect(x + 13, y + 17, 20, 6);
        shapes.setColor(accent);
        shapes.rect(x + 27, y + 18, 6, 3);
        if (friendly) {
            shapes.setColor(accent.r, accent.g, accent.b,
                    0.55f + 0.45f * (float) Math.abs(Math.sin(pulse * 2.4)));
            shapes.circle(x, y - 14, 3, 12);
        }
    }

    @Override public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void dispose() {
        shapes.dispose();
        batch.dispose();
        font.dispose();
    }
}
