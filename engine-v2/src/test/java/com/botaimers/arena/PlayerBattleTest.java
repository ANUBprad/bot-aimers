package com.botaimers.arena;

import com.botaimers.engine.Bot;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class PlayerBattleTest {
    @Test void movementNeverOverlapsEnemy() {
        PlayerBattle battle = new PlayerBattle(new GridMap(3, 1, Set.of()),
                new Bot("Player", 20, 5, 0), new GridPoint(0, 0),
                new Bot("CPU", 20, 5, 0), new GridPoint(1, 0));
        battle.takeTurn(PlayerBattle.Command.RIGHT);
        assertEquals(new GridPoint(0, 0), battle.playerPosition());
        assertNotEquals(battle.playerPosition(), battle.enemyPosition());
    }

    @Test void guardHalvesIncomingDamage() {
        PlayerBattle battle = new PlayerBattle(new GridMap(2, 1, Set.of()),
                new Bot("Player", 20, 5, 0), new GridPoint(0, 0),
                new Bot("CPU", 20, 9, 0), new GridPoint(1, 0));
        battle.takeTurn(PlayerBattle.Command.GUARD);
        assertEquals(15, battle.playerHealth());
    }

    @Test void attackCanWinAndStopsFurtherTurns() {
        PlayerBattle battle = new PlayerBattle(new GridMap(2, 1, Set.of()),
                new Bot("Player", 20, 10, 0), new GridPoint(0, 0),
                new Bot("CPU", 8, 1, 0), new GridPoint(1, 0));
        battle.takeTurn(PlayerBattle.Command.ATTACK);
        assertTrue(battle.finished());
        assertEquals("Player", battle.winner());
        assertEquals(20, battle.playerHealth());
        assertThrows(IllegalStateException.class, () -> battle.takeTurn(PlayerBattle.Command.ATTACK));
    }

    @Test void enemyNavigatesAroundWalls() {
        PlayerBattle battle = new PlayerBattle(
                new GridMap(4, 3, Set.of(new GridPoint(1, 1))),
                new Bot("Player", 40, 8, 1), new GridPoint(0, 1),
                new Bot("CPU", 40, 8, 1), new GridPoint(3, 1));
        battle.takeTurn(PlayerBattle.Command.GUARD);
        assertNotEquals(new GridPoint(3, 1), battle.enemyPosition());
        assertTrue(battle.map().walkable(battle.enemyPosition()));
    }
}
