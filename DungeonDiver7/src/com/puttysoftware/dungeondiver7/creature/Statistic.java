/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.creature;

import java.util.Objects;

class Statistic {
    // Fields
    private int value;
    private final int dynamism;
    private boolean hasMax;
    private int maxID;
    private final boolean hasMin;
    private int minVal;

    // Constructor
    Statistic() {
        this.value = 0;
        this.dynamism = 0;
        this.hasMax = false;
        this.maxID = StatConstants.STAT_NONE;
        this.hasMin = true;
        this.minVal = 0;
    }

    int getValue() {
        return this.value;
    }

    void setValue(final int newValue) {
        this.value = newValue;
    }

    void offsetValue(final int newValue) {
        this.value += newValue;
    }

    void offsetValueMultiply(final double newValue, final boolean max, final int maxValue) {
        if (max) {
            this.value -= (int) (maxValue - maxValue * newValue);
        } else {
            this.value *= newValue;
        }
    }

    int getDynamism() {
        return this.dynamism;
    }

    boolean hasMax() {
        return this.hasMax;
    }

    void setHasMax(final boolean newHasMax) {
        this.hasMax = newHasMax;
    }

    int getMaxID() {
        return this.maxID;
    }

    void setMaxID(final int newMaxID) {
        this.maxID = newMaxID;
    }

    boolean hasMin() {
        return this.hasMin;
    }

    int getMinVal() {
        return this.minVal;
    }

    void setMinVal(final int newMinVal) {
        this.minVal = newMinVal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.dynamism, this.hasMax, this.hasMin, this.maxID, this.minVal, this.value);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof final Statistic other) || this.dynamism != other.dynamism
                || this.hasMax != other.hasMax) {
            return false;
        }
        if (this.hasMin != other.hasMin || this.maxID != other.maxID || this.minVal != other.minVal
                || this.value != other.value) {
            return false;
        }
        return true;
    }
}
