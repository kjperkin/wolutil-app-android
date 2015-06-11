package com.lightbulbz.android.wolutil;

import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import com.lightbulbz.android.wolutil.MacAddressFavoritesModel.Favorite;
import com.lightbulbz.net.MacAddress;
import com.lightbulbz.net.MacAddressFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


/**
 * Created by kevin on 6/7/15.
 */
class MacAddressFavoritesStorage {
    public static MacAddressFavoritesModel readJsonStream(InputStream in) throws IOException, IllegalStateException {
        final JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        final MacAddressFavoritesModel model = new MacAddressFavoritesModel();
        try {
            readFavorites(reader, model);
        } finally {
            reader.close();
        }
        return model;
    }

    private static void readFavorites(JsonReader reader, MacAddressFavoritesModel model) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            try {
                Favorite f = readFavorite(reader);
                model.addFavorite(f);
            } catch (MacAddressFormatException e) {
                Log.w("WOLUtil", e.getMessage());
            }
        }
        reader.endArray();
    }

    private static Favorite readFavorite(JsonReader reader) throws IOException {
        Favorite f = new Favorite();
        MacAddressFormatException ex = null;

        reader.beginObject();
        while (reader.hasNext()) {
            final String name = reader.nextName();
            switch (name) {
                case "name":
                    f.name = reader.nextString();
                    break;
                case "addr":
                    try {
                        f.addr = MacAddress.parseMacAddress(reader.nextString());
                    } catch (MacAddressFormatException e) {
                        // We need to try parsing the rest of the JSON
                        // We will throw this later.
                        ex = e;
                    }
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        if (ex != null) {
            throw ex;
        }

        return f;
    }

    public static void writeJsonStream(MacAddressFavoritesModel model, OutputStream out) throws IOException {
        final JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        try {
            writeFavorites(model, writer);
        } finally {
            writer.close();
        }
    }

    private static void writeFavorites(MacAddressFavoritesModel model, JsonWriter writer) throws IOException {
        writer.beginArray();
        int favoriteCount = model.getFavoriteCount();
        for (int idx = 0; idx < favoriteCount; idx++) {
            writeFavorite(model.getFavorite(idx), writer);
        }
        writer.endArray();
    }

    private static void writeFavorite(Favorite f, JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name("name");
        writer.value(f.name);
        writer.name("addr");
        writer.value(f.addr.toString());
        writer.endObject();
    }


}
