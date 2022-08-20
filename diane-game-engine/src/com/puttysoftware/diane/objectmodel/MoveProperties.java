/*
 * Diane Game Engine Copyleft (C) 2019-present Eric Ahnell
 * 
 * Any questions should be directed to the author via email at:
 * support@puttysoftware.com
 */
package com.puttysoftware.diane.objectmodel;

import java.util.Objects;

import com.puttysoftware.diane.utilties.DirectionResolver;
import com.puttysoftware.diane.utilties.Directions;
import com.puttysoftware.storage.FlagStorage;

class MoveProperties {
  // Private enumeration
  private enum MoveDataTypes {
    PUSH(0), PULL(1), PUSH_INTO(2), PULL_INTO(3), PUSH_OUT(4), PULL_OUT(5);

    private int index;

    MoveDataTypes(final int value) {
      this.index = value;
    }
  }

  // Properties
  private final FlagStorage moveData;
  private static final int MOVE_DATA_TYPES = 6;

  // Constructors
  public MoveProperties() {
    this.moveData = new FlagStorage(MoveProperties.MOVE_DATA_TYPES, DirectionResolver.COUNT);
  }

  // Methods
  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final MoveProperties other = (MoveProperties) obj;
    if (!Objects.equals(this.moveData, other.moveData)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 17 * hash + Objects.hashCode(this.moveData);
    return hash;
  }

  public boolean isPushable() {
    boolean result = true;
    for (int dir = 0; dir < DirectionResolver.COUNT; dir++) {
      result = result && this.moveData.getCell(MoveDataTypes.PUSH.index, dir);
    }
    return result;
  }

  public boolean isDirectionallyPushable(final int dirX, final int dirY) {
    final Directions dir = DirectionResolver.resolve(dirX, dirY);
    try {
      if (dir != Directions.NONE) {
        return this.moveData.getCell(MoveDataTypes.PUSH.index, dir.ordinal());
      } else {
        return false;
      }
    } catch (final ArrayIndexOutOfBoundsException aioob) {
      return false;
    }
  }

  public boolean isPullable() {
    boolean result = true;
    for (int dir = 0; dir < DirectionResolver.COUNT; dir++) {
      result = result && this.moveData.getCell(MoveDataTypes.PULL.index, dir);
    }
    return result;
  }

  public boolean isDirectionallyPullable(final int dirX, final int dirY) {
    final Directions dir = DirectionResolver.resolve(dirX, dirY);
    try {
      if (dir != Directions.NONE) {
        return this.moveData.getCell(MoveDataTypes.PULL.index, dir.ordinal());
      } else {
        return false;
      }
    } catch (final ArrayIndexOutOfBoundsException aioob) {
      return false;
    }
  }

  public boolean isPushableInto() {
    boolean result = true;
    for (int dir = 0; dir < DirectionResolver.COUNT; dir++) {
      result = result && this.moveData.getCell(MoveDataTypes.PUSH_INTO.index, dir);
    }
    return result;
  }

  public boolean isDirectionallyPushableInto(final int dirX, final int dirY) {
    final Directions dir = DirectionResolver.resolve(dirX, dirY);
    try {
      if (dir != Directions.NONE) {
        return this.moveData.getCell(MoveDataTypes.PUSH_INTO.index, dir.ordinal());
      } else {
        return false;
      }
    } catch (final ArrayIndexOutOfBoundsException aioob) {
      return false;
    }
  }

  public boolean isPullableInto() {
    boolean result = true;
    for (int dir = 0; dir < DirectionResolver.COUNT; dir++) {
      result = result && this.moveData.getCell(MoveDataTypes.PULL_INTO.index, dir);
    }
    return result;
  }

  public boolean isDirectionallyPullableInto(final int dirX, final int dirY) {
    final Directions dir = DirectionResolver.resolve(dirX, dirY);
    try {
      if (dir != Directions.NONE) {
        return this.moveData.getCell(MoveDataTypes.PULL_INTO.index, dir.ordinal());
      } else {
        return false;
      }
    } catch (final ArrayIndexOutOfBoundsException aioob) {
      return false;
    }
  }

  public boolean isPushableOut() {
    boolean result = true;
    for (int dir = 0; dir < DirectionResolver.COUNT; dir++) {
      result = result && this.moveData.getCell(MoveDataTypes.PUSH_OUT.index, dir);
    }
    return result;
  }

  public boolean isDirectionallyPushableOut(final int dirX, final int dirY) {
    final Directions dir = DirectionResolver.resolve(dirX, dirY);
    try {
      if (dir != Directions.NONE) {
        return this.moveData.getCell(MoveDataTypes.PUSH_OUT.index, dir.ordinal());
      } else {
        return false;
      }
    } catch (final ArrayIndexOutOfBoundsException aioob) {
      return false;
    }
  }

  public boolean isPullableOut() {
    boolean result = true;
    for (int dir = 0; dir < DirectionResolver.COUNT; dir++) {
      result = result && this.moveData.getCell(MoveDataTypes.PULL_OUT.index, dir);
    }
    return result;
  }

  public boolean isDirectionallyPullableOut(final int dirX, final int dirY) {
    final Directions dir = DirectionResolver.resolve(dirX, dirY);
    try {
      if (dir != Directions.NONE) {
        return this.moveData.getCell(MoveDataTypes.PULL_OUT.index, dir.ordinal());
      } else {
        return false;
      }
    } catch (final ArrayIndexOutOfBoundsException aioob) {
      return false;
    }
  }

  public void setPushable(final boolean value) {
    for (int dir = 0; dir < DirectionResolver.COUNT; dir++) {
      this.setDirectionallyPushable(dir, value);
    }
  }

  public void setDirectionallyPushable(final int dir, final boolean value) {
    this.moveData.setCell(value, MoveDataTypes.PUSH.index, dir);
  }

  public void setPullable(final boolean value) {
    for (int dir = 0; dir < DirectionResolver.COUNT; dir++) {
      this.setDirectionallyPullable(dir, value);
    }
  }

  public void setDirectionallyPullable(final int dir, final boolean value) {
    this.moveData.setCell(value, MoveDataTypes.PULL.index, dir);
  }

  public void setPushableInto(final boolean value) {
    for (int dir = 0; dir < DirectionResolver.COUNT; dir++) {
      this.setDirectionallyPushableInto(dir, value);
    }
  }

  public void setDirectionallyPushableInto(final int dir, final boolean value) {
    this.moveData.setCell(value, MoveDataTypes.PUSH_INTO.index, dir);
  }

  public void setPullableInto(final boolean value) {
    for (int dir = 0; dir < DirectionResolver.COUNT; dir++) {
      this.setDirectionallyPullableInto(dir, value);
    }
  }

  public void setDirectionallyPullableInto(final int dir, final boolean value) {
    this.moveData.setCell(value, MoveDataTypes.PULL_INTO.index, dir);
  }

  public void setPushableOut(final boolean value) {
    for (int dir = 0; dir < DirectionResolver.COUNT; dir++) {
      this.setDirectionallyPushableOut(dir, value);
    }
  }

  public void setDirectionallyPushableOut(final int dir, final boolean value) {
    this.moveData.setCell(value, MoveDataTypes.PUSH_OUT.index, dir);
  }

  public void setPullableOut(final boolean value) {
    for (int dir = 0; dir < DirectionResolver.COUNT; dir++) {
      this.setDirectionallyPullableOut(dir, value);
    }
  }

  public void setDirectionallyPullableOut(final int dir, final boolean value) {
    this.moveData.setCell(value, MoveDataTypes.PULL_OUT.index, dir);
  }
}
