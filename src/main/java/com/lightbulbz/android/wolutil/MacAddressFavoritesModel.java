package com.lightbulbz.android.wolutil;

import com.lightbulbz.net.MacAddress;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by kevin on 6/7/15.
 */
public class MacAddressFavoritesModel {
    private Map<String, MacAddress> favorites;

    public MacAddressFavoritesModel() {
        favorites = new HashMap<>();
    }

    public Favorite getFavorite(String key) {
        return new Favorite(key, favorites.get(key));
    }

    public void removeFavorite(Favorite f) {
        favorites.remove(f.name);
    }

    public void removeFavorite(String key) {
        favorites.remove(key);
    }

    public boolean hasFavorites() {
        return favorites.isEmpty();
    }

    public Set<String> getFavoriteNames() {
        return favorites.keySet();
    }

    public int size() {
        return favorites.size();
    }

    public void addFavorite(Favorite f) {
        addFavorite(f.name, f.addr);
    }

    public void addFavorite(String key, MacAddress value) {
        favorites.put(key, value);
    }

    public static class Favorite {
        String name;
        MacAddress addr;

        Favorite() {
            this(null, null);
        }

        Favorite(String name, MacAddress addr) {
            this.name = name;
            this.addr = addr;
        }
    }
}
