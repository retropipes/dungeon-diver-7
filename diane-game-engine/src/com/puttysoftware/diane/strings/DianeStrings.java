package com.puttysoftware.diane.strings;

import java.util.ResourceBundle;

import com.puttysoftware.diane.utilties.Directions;

public final class DianeStrings {
  private DianeStrings() {
  }

  public static String directions(final Directions item) {
    return ResourceBundle.getBundle("locale.diane.direction").getString(Integer.toString(item.ordinal()));
  }

  public static String subst(final String orig, final String... values) {
    String result = orig;
    for (int s = 0; s < values.length; s++) {
      result = result.replace("%" + s, values[s]);
    }
    return result;
  }
}