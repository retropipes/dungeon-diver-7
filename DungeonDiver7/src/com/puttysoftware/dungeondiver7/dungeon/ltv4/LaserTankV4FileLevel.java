/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.ltv4;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.puttysoftware.diane.direction.Direction;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.current.CurrentDungeonData;
import com.puttysoftware.dungeondiver7.dungeon.objects.ArrowTurret;
import com.puttysoftware.dungeondiver7.dungeon.objects.Box;
import com.puttysoftware.dungeondiver7.dungeon.objects.Bricks;
import com.puttysoftware.dungeondiver7.dungeon.objects.CrystalBlock;
import com.puttysoftware.dungeondiver7.dungeon.objects.Empty;
import com.puttysoftware.dungeondiver7.dungeon.objects.Flag;
import com.puttysoftware.dungeondiver7.dungeon.objects.Ground;
import com.puttysoftware.dungeondiver7.dungeon.objects.Ice;
import com.puttysoftware.dungeondiver7.dungeon.objects.Mirror;
import com.puttysoftware.dungeondiver7.dungeon.objects.Party;
import com.puttysoftware.dungeondiver7.dungeon.objects.PartyMover;
import com.puttysoftware.dungeondiver7.dungeon.objects.RotaryMirror;
import com.puttysoftware.dungeondiver7.dungeon.objects.ThinIce;
import com.puttysoftware.dungeondiver7.dungeon.objects.Tunnel;
import com.puttysoftware.dungeondiver7.dungeon.objects.Wall;
import com.puttysoftware.dungeondiver7.dungeon.objects.Water;
import com.puttysoftware.dungeondiver7.locale.Colors;
import com.puttysoftware.dungeondiver7.locale.Difficulty;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;

class LaserTankV4FileLevel {
    // Fields
    private static byte[] objects;
    private static byte[] name;
    private static byte[] hint;
    private static byte[] author;
    private static byte[] difficulty;
    private static final int OBJECTS_SIZE = 256;
    private static final int NAME_SIZE = 31;
    private static final int HINT_SIZE = 256;
    private static final int AUTHOR_SIZE = 31;
    private static final int DIFFICULTY_SIZE = 2;

    // Constructors
    private LaserTankV4FileLevel() {
        // Do nothing
    }

    static CurrentDungeonData loadAndConvert(final FileInputStream file, final AbstractDungeon a) {
        try {
            LaserTankV4FileLevel.objects = new byte[LaserTankV4FileLevel.OBJECTS_SIZE];
            LaserTankV4FileLevel.name = new byte[LaserTankV4FileLevel.NAME_SIZE];
            LaserTankV4FileLevel.hint = new byte[LaserTankV4FileLevel.HINT_SIZE];
            LaserTankV4FileLevel.author = new byte[LaserTankV4FileLevel.AUTHOR_SIZE];
            LaserTankV4FileLevel.difficulty = new byte[LaserTankV4FileLevel.DIFFICULTY_SIZE];
            final var t = new CurrentDungeonData();
            // Convert object byte map
            var bytesRead = file.read(LaserTankV4FileLevel.objects, 0, LaserTankV4FileLevel.OBJECTS_SIZE);
            if (bytesRead != LaserTankV4FileLevel.OBJECTS_SIZE) {
                return null;
            }
            for (var x = 0; x < 16; x++) {
                for (var y = 0; y < 16; y++) {
                    final var z = x * 16 + y;
                    AbstractDungeonObject ao = null;
                    final var b = LaserTankV4FileLevel.objects[z];
                    switch (b) {
                        case 0:
                            ao = new Ground();
                            break;
                        case 1:
                            ao = new Party(1);
                            break;
                        case 2:
                            ao = new Flag();
                            break;
                        case 3:
                            ao = new Water();
                            break;
                        case 4:
                            ao = new Wall();
                            break;
                        case 5:
                            ao = new Box();
                            break;
                        case 6:
                            ao = new Bricks();
                            break;
                        case 7:
                            ao = new ArrowTurret();
                            ao.setDirection(Direction.NORTH);
                            break;
                        case 8:
                            ao = new ArrowTurret();
                            ao.setDirection(Direction.EAST);
                            break;
                        case 9:
                            ao = new ArrowTurret();
                            ao.setDirection(Direction.SOUTH);
                            break;
                        case 10:
                            ao = new ArrowTurret();
                            ao.setDirection(Direction.WEST);
                            break;
                        case 11:
                            ao = new Mirror();
                            ao.setDirection(Direction.NORTH_WEST);
                            break;
                        case 12:
                            ao = new Mirror();
                            ao.setDirection(Direction.NORTH_EAST);
                            break;
                        case 13:
                            ao = new Mirror();
                            ao.setDirection(Direction.SOUTH_EAST);
                            break;
                        case 14:
                            ao = new Mirror();
                            ao.setDirection(Direction.SOUTH_WEST);
                            break;
                        case 15:
                            ao = new PartyMover();
                            ao.setDirection(Direction.NORTH);
                            break;
                        case 16:
                            ao = new PartyMover();
                            ao.setDirection(Direction.EAST);
                            break;
                        case 17:
                            ao = new PartyMover();
                            ao.setDirection(Direction.SOUTH);
                            break;
                        case 18:
                            ao = new PartyMover();
                            ao.setDirection(Direction.WEST);
                            break;
                        case 19:
                            ao = new CrystalBlock();
                            break;
                        case 20:
                            ao = new RotaryMirror();
                            ao.setDirection(Direction.NORTH_WEST);
                            break;
                        case 21:
                            ao = new RotaryMirror();
                            ao.setDirection(Direction.NORTH_EAST);
                            break;
                        case 22:
                            ao = new RotaryMirror();
                            ao.setDirection(Direction.SOUTH_EAST);
                            break;
                        case 23:
                            ao = new RotaryMirror();
                            ao.setDirection(Direction.SOUTH_WEST);
                            break;
                        case 24:
                            ao = new Ice();
                            break;
                        case 25:
                            ao = new ThinIce();
                            break;
                        case 64:
                        case 65:
                            ao = new Tunnel();
                            ao.setColor(Colors.RED);
                            break;
                        case 66:
                        case 67:
                            ao = new Tunnel();
                            ao.setColor(Colors.GREEN);
                            break;
                        case 68:
                        case 69:
                            ao = new Tunnel();
                            ao.setColor(Colors.BLUE);
                            break;
                        case 70:
                        case 71:
                            ao = new Tunnel();
                            ao.setColor(Colors.CYAN);
                            break;
                        case 72:
                        case 73:
                            ao = new Tunnel();
                            ao.setColor(Colors.YELLOW);
                            break;
                        case 74:
                        case 75:
                            ao = new Tunnel();
                            ao.setColor(Colors.MAGENTA);
                            break;
                        case 76:
                        case 77:
                            ao = new Tunnel();
                            ao.setColor(Colors.WHITE);
                            break;
                        case 78:
                        case 79:
                            ao = new Tunnel();
                            ao.setColor(Colors.GRAY);
                            break;
                        default:
                            ao = new Empty();
                    }
                    t.setCell(a, ao, x, y, 0, ao.getLayer());
                }
            }
            // Convert level name
            bytesRead = file.read(LaserTankV4FileLevel.name, 0, LaserTankV4FileLevel.NAME_SIZE);
            if (bytesRead != LaserTankV4FileLevel.NAME_SIZE) {
                return null;
            }
            final var levelName = Charset.forName(Strings.untranslated(Untranslated.DEFAULT_CHARSET))
                    .decode(ByteBuffer.wrap(LaserTankV4FileLevel.name)).toString();
            a.setName(levelName);
            // Convert level hint
            bytesRead = file.read(LaserTankV4FileLevel.hint, 0, LaserTankV4FileLevel.HINT_SIZE);
            if (bytesRead != LaserTankV4FileLevel.HINT_SIZE) {
                return null;
            }
            final var levelHint = Charset.forName(Strings.untranslated(Untranslated.DEFAULT_CHARSET))
                    .decode(ByteBuffer.wrap(LaserTankV4FileLevel.hint)).toString();
            a.setHint(levelHint);
            // Convert level author
            bytesRead = file.read(LaserTankV4FileLevel.author, 0, LaserTankV4FileLevel.AUTHOR_SIZE);
            if (bytesRead != LaserTankV4FileLevel.AUTHOR_SIZE) {
                return null;
            }
            final var levelAuthor = Charset.forName(Strings.untranslated(Untranslated.DEFAULT_CHARSET))
                    .decode(ByteBuffer.wrap(LaserTankV4FileLevel.author)).toString();
            a.setAuthor(levelAuthor);
            // Convert level difficulty
            bytesRead = file.read(LaserTankV4FileLevel.difficulty, 0, LaserTankV4FileLevel.DIFFICULTY_SIZE);
            if (bytesRead != LaserTankV4FileLevel.DIFFICULTY_SIZE) {
                return null;
            }
            final var tempDiff = LaserTankV4FileLevel.toInt(LaserTankV4FileLevel.difficulty);
            switch (tempDiff) {
                case 1:
                    a.setDifficulty(Difficulty.KIDS);
                    break;
                case 2:
                    a.setDifficulty(Difficulty.EASY);
                    break;
                case 4:
                    a.setDifficulty(Difficulty.MEDIUM);
                    break;
                case 8:
                    a.setDifficulty(Difficulty.HARD);
                    break;
                case 16:
                    a.setDifficulty(Difficulty.DEADLY);
                    break;
                default:
                    a.setDifficulty(Difficulty.MEDIUM);
                    break;
            }
            t.fillNulls(a, new Ground(), new Wall(), true);
            t.resize(a, AbstractDungeon.getMinFloors(), new Empty());
            t.fillVirtual();
            return t;
        } catch (final IOException ioe) {
            return null;
        }
    }

    private static int toInt(final byte[] data) {
        if (data == null || data.length != 2) {
            return 0x0;
        }
        return (0xff & data[0]) << 0 | (0xff & data[1]) << 8;
    }
}
