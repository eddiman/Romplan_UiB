package com.pensive.android.romplanuib.io;


import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.pensive.android.romplanuib.Exceptions.DownloadException;
import com.pensive.android.romplanuib.models.UIBroom;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This class parses though the URL given, placing each RegEx-cleaned text into
 * an object.
 *
 * @author Gaute Gjerløw Remen, Fredrik Heimsæter
 * @version 1.1
 *
 */
public class BuildingParser {

    Pattern pattern;
    Matcher matcher;
    List<String> uibBuildingNames = new ArrayList<>();


    public BuildingParser(String url) throws IOException, DownloadException {
        createBuilding(getValueFromHTML(url));
    }

    /**
     * Find the buildings in the HTML document.
     *
     * @throws IOException
     * @throws SocketTimeoutException
     *             if the internet connection fails. Gets a messagebox stating
     *             this. MessageBox is a library downloaded from
     *             http://sourceforge
     *             .jp/projects/jfxmessagebox/downloads/57065/jfxmessagebox
     *             -1.1.0.jar/
     */
    public Elements getValueFromHTML(String url) throws IOException, DownloadException {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (SocketTimeoutException | UnknownHostException e) {
            throw new DownloadException();
        }
        Elements realTimeValues = doc.select("option[value*=:]");

        return realTimeValues;
    }

    /**
     * Cleans the building tags for special characters and unnecessary text,
     * using RegEx, and adds the building name to the list.
     *
     * @param buildings
     */
    private void createBuilding(Elements buildings) {
        List<UIBroom> emptyList = new ArrayList<>();
        for (Element building : buildings) {

            pattern = Pattern.compile("([^\\n\\r]+)");
            matcher = pattern.matcher(building.text());

            if (matcher.find()) {
                uibBuildingNames.add(matcher.group(0));
            }

        }
    }

    /**
     *
     * @return the list of building names
     */
    public List<String> getBuildings() {
        return uibBuildingNames;
    }

}
