package com.lightbulbz.android.wolutil;

import com.lightbulbz.net.MacAddress;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kevin on 6/7/15.
 */
public class MacAddressFavoritesModel {
    private final List<Favorite> favorites = new ArrayList<>();
    private final Map<String, Integer> favoritesByName = new HashMap<>();

    public MacAddressFavoritesModel() {
    }

    public int getFavoriteCount() {
        return favorites.size();
    }

    public Favorite getFavorite(int index) {
        return favorites.get(index);
    }

    public Favorite getFavorite(String key) {
        return favorites.get(favoritesByName.get(key));
    }

    public void removeFavorite(Favorite f) {
        removeFavorite(f.name);
    }

    public void removeFavorite(String key) {
        removeFavorite(favoritesByName.get(key));
    }

    public void removeFavorite(int index) {
        favorites.remove(index);
        rebuildMap();
    }

    private void rebuildMap() {
        favoritesByName.clear();
        for (int idx = 0; idx < favorites.size(); idx++) {
            favoritesByName.put(favorites.get(idx).name, idx);
        }
    }

    public void removeAll(Collection<? extends Integer> positions) {
        if (positions.size() > 0) {
            ArrayList<Integer> posList = new ArrayList<>(positions);
            Collections.sort(posList, Collections.reverseOrder());
            for (int i : posList) {
                favorites.remove(i);
            }
            rebuildMap();
        }
    }

    public boolean hasFavorites() {
        return favorites.isEmpty();
    }

    public void addFavorite(Favorite f) {
        addFavorite(f.name, f.addr);
    }

    public void addFavorite(String key, MacAddress value) {
        Favorite favorite = new Favorite(key, value);
        if (favoritesByName.containsKey(key)) {
            favorites.set(favoritesByName.get(key), favorite);
        } else {
            favorites.add(favorite);
            favoritesByName.put(key, favorites.size()-1);
        }
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
