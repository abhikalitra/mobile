package com.abhikalitra.level;

import android.content.Context;

/**
 * Copyright sushil
 * Date: 29 Dec, 2010
 * Time: 7:56:17 PM
 */
public class LevelTree {

    private static boolean loaded = false;

    public static boolean isLoaded() {
        return loaded;
    }

    public static void loadLevelTree(int level_tree, Context context) {
        loaded = true; 
    }

    public static void loadAllDialog(Context context) {
            
    }
}
