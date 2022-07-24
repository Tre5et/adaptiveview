package net.treset.dynview.tools;

import com.google.gson.*;
import net.treset.dynview.DynViewMod;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileTools {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static boolean writeJsonToFile(JsonObject obj, File file)
    {
        File parent = file.getParentFile();
        if(parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + parent);
        }

        File fileTmp = new File(file.getParentFile(), file.getName() + ".tmp"); //create temporary storage file

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileTmp), StandardCharsets.UTF_8)) { //open writer
            writer.write(GSON.toJson(obj)); //write json to file
            writer.close();

            if (file.exists() && file.isFile() && !file.delete()) return false; //delete old file if exists

            return fileTmp.renameTo(file); //commit temporary file
        }
        catch (Exception e) {
            e.printStackTrace();
            DynViewMod.LOGGER.info("Failed to write JSON data to file '{}'", fileTmp.getAbsolutePath());
        }
        return false;
    }

    public static JsonObject readJsonFile(File file) {
        if (file.exists() && file.isFile() && file.canRead()) { //file exists and can be read

            JsonElement elm;
            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) { //open reader
                elm = JsonParser.parseReader(reader); //read file to json element
            } catch (Exception e) {
                e.printStackTrace();
                DynViewMod.LOGGER.error("Failed to parse the JSON file '{}'", file.getAbsolutePath());
                return null;
            }

            return elm.getAsJsonObject(); //return json object
        }
        return null;
    }

}
