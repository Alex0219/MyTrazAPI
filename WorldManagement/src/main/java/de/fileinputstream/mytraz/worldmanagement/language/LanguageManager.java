package de.fileinputstream.mytraz.worldmanagement.language;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.DBUser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Alexander on 22.08.2020
 * © 2020 Alexander Fiedler
 **/
public class LanguageManager {

    public JSONObject languageObject;
    public JSONObject german;
    public JSONObject english;

    public LanguageManager() {
        german = readLanguageFile("de");
        english = readLanguageFile("en");
    }

    public JSONObject readLanguageFile(String language) {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(new File(Bootstrap.getInstance().getDataFolder(), "language.json"))) {
            JSONObject parentObject = (JSONObject) jsonParser.parse(reader);
            return (JSONObject) parentObject.get(language);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String translateMessage(final String messageKey, final DBUser dbUser) {
        if (dbUser.getLanguage() == Language.GERMAN) {
            return german.get(messageKey).toString();
        } else if (dbUser.getLanguage() == Language.ENGLISH) {
            return english.get(messageKey).toString();
        }
        return null;
    }
}
