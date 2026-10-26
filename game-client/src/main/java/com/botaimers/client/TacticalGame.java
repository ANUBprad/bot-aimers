package com.botaimers.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.botaimers.arena.*;
import com.botaimers.engine.Bot;
import java.util.Set;

/** Simple asset-free 2D tactical arena, driven by the shared Java engine. */
public final class TacticalGame extends ApplicationAdapter {
    private static final int TILE = 64;
    private static final int ORIGIN_X = 192;
    private static final int ORIGIN_Y = 130;
    private final Color floor = new Color(0.14f, 0.19f, 0.25f, 1f);
    private final Color wall = new Color(0.34f, 0.38f, 0.46f, 1f);
    private final Color playerColor = new Color(0.23f, 0.77f, 0.91f, 1f);
    private final Color enemyColor = new Color(0.95f, 0.39f, 0.28f, 1f);
    private ShapeRenderer shapes;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private GridMap map;
    private PlayerBattle battle;
    private String feedback = "Move toward the enemy. Press F to attack.";

    @Override public void create() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(960, 640, camera);
        viewport.apply();
        camera.position.set(480, 320, 0);
        camera.update();
        shapes = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        reset();
    }

    private void reset() {
        map = new GridMap(9, 5, Set.of(
                new GridPoint(4, 0), new GridPoint(4, 1),
                new GridPoint(4, 3), new GridPoint(4, 4)));
        battle = new PlayerBattle(map,
                new Bot("Player", 60, 12, 3), new GridPoint(1, 2),
                new Bot("CPU", 60, 10, 4), new GridPoint(7, 2));
        feedback = "WASD / arrows: move | F: attack | G: guard | R: restart";
    }

    @Override public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) reset();
        if (!battle.finished()) {
            PlayerBattle.Command command = null;
            if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP))
                command = PlayerBattle.Command.UP;
            else if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
                command = PlayerBattle.Command.DOWN;
            else if (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
                command = PlayerBattle.Command.LEFT;
            else if (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
                command = PlayerBattle.Command.RIGHT;
            else if (Gdx.input.isKeyJustPressed(Input.Keys.F))
                command = PlayerBattle.Command.ATTACK;
            else if (Gdx.input.isKeyJustPressed(Input.Keys.G))
                command = PlayerBattle.Command.GUARD;
            if (command != null) feedback = battle.takeTurn(command);
        }
        ScreenUtils.clear(0.055f, 0.075f, 0.11f, 1f);
        viewport.apply();
        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        for (int y = 0; y < map.height(); y++) {
            for (int x = 0; x < map.width(); x++) {
                GridPoint point = new GridPoint(x, y);
                float px = ORIGIN_X + x * TILE;
                float py = ORIGIN_Y + (map.height() - 1 - y) * TILE;
                shapes.setColor(map.walkable(point) ? floor : wall);
                shapes.rect(px + 2, py + 2, TILE - 4, TILE - 4);
            }
        }
        drawBot(battle.playerPosition(), playerColor);
        drawBot(battle.enemyPosition(), enemyColor);
        shapes.setColor(Color.DARK_GRAY);
        shapes.rect(190, 510, 260, 18);
        shapes.rect(510, 510, 260, 18);
        shapes.setColor(playerColor);
        shapes.rect(190, 510, 260f * battle.playerHealth() / 60f, 18);
        shapes.setColor(enemyColor);
        shapes.rect(510, 510, 260f * battle.enemyHealth() / 60f, 18);
        shapes.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, "BOT-AIMERS  /  TACTICAL ARENA", 190, 584);
        font.draw(batch, "YOU  " + battle.playerHealth() + " HP", 190, 545);
        font.draw(batch, "CPU  " + battle.enemyHealth() + " HP", 510, 545);
        font.draw(batch, "WASD / ARROWS: MOVE     F: ATTACK     G: GUARD     R: RESTART", 190, 94);
        font.draw(batch, "TURN " + battle.turns() + "  |  " + feedback, 190, 62);
        if (battle.finished()) {
            font.setColor(Color.YELLOW);
            font.draw(batch, "GAME OVER - " + battle.winner() + " wins! Press R to play again.", 190, 38);
        }
        batch.end();
    }

    private void drawBot(GridPoint point, Color color) {
        float x = ORIGIN_X + point.x() * TILE;
        float y = ORIGIN_Y + (map.height() - 1 - point.y()) * TILE;
        shapes.setColor(color);
        shapes.rect(x + 12, y + 12, TILE - 24, TILE - 24);
        shapes.setColor(Color.WHITE);
        shapes.rect(x + 23, y + 39, 7, 6);
        shapes.rect(x + 35, y + 39, 7, 6);
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
