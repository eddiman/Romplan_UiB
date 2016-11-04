package com.pensive.android.romplanuib.io;


import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pensive.android.romplanuib.io.util.URLEncoding;
import com.pensive.android.romplanuib.models.UIBroom;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Class used to retrieve a list of rooms from a URL
 * @author Gaute Gjerløw Remen
 * @version 1.0
 *
 */
public class RoomParser {

    List<UIBroom> uibRooms = new ArrayList<UIBroom>();

    public RoomParser(String url, String building) throws IOException {
        createRooms(getValueFromHTML(url), building);
    }

    /**
     * Find the buildings in the HTML document
     *
     * @throws IOException
     * @throws SocketTimeoutException
     *             if the internet connection fails. Gets a messagebox stating
     *             this. MessageBox is a library downloaded from
     *             http://sourceforge
     *             .jp/projects/jfxmessagebox/downloads/57065/jfxmessagebox
     *             -1.1.0.jar/
     */
    public Elements getValueFromHTML(String url){
        Document doc = null;
        url = URLEncoding.encode(url);
        try {
            doc = Jsoup.connect(url).get();
        } catch (SocketTimeoutException | UnknownHostException e) {
            System.out.println("No conn");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements realTimeValues = doc.select("option[value*=:]");

        return realTimeValues;
    }

    /**
     * Cleans the building tags for special characters and unnecessary text,
     * using RegEx, and creates objects from the output.
     *
     * @param rooms
     */
    private void createRooms(Elements rooms, String building) {

        for (Element room : rooms) {
            Pattern roomCodePattern = Pattern.compile(":([^)]+)");
            Matcher roomCodeMatcher = roomCodePattern.matcher(room.text());
            Pattern roomNamePattern = Pattern.compile("(?<=\\)).*");
            Matcher roomNameMatcher = roomNamePattern.matcher(room.text());


            if (roomCodeMatcher.find()) {
                if(roomNameMatcher.find()) {
                    UIBroom uib_room = new UIBroom(roomCodeMatcher.group(1), building, roomNameMatcher.group(0).trim());
                    uibRooms.add(uib_room);
                    //System.out.println(roomCodeMatcher.group(1));
                }else {
                    UIBroom uib_room = new UIBroom(roomCodeMatcher.group(1), building, roomCodeMatcher.group(1));
                    uibRooms.add(uib_room);
                }
            }
        }
    }

    public List<UIBroom> getRooms() {
        return uibRooms;
    }
}