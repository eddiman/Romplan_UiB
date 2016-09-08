package com.pensive.android.romplanuib.models;

/**
 * Helper class used to serialize Activity objects by extracting the values of
 * the non-serializable fields and storing them in a string array
 *
 * @author Anders Eide
 * @version 1.0
 *
 */
public class CalActivitySerializationHelper {
    private static final long serialVersionUID = 1L;

    String[] properties = new String[7];

    public CalActivitySerializationHelper(String weekday, String time,
                                       String date, String subject, String type, String buildingCode,
                                       String roomCode) {

        properties[0] = weekday;
        properties[1] = time;
        properties[2] = date;
        properties[3] = subject;
        properties[4] = type;
        properties[5] = roomCode;
        properties[6] = buildingCode;

    }

    /**
     * Method used to deserialize activities
     * @return The activity used to generate this helper object
     */
    public CalActivity getActivity() {
        return new CalActivity(null, properties[3], properties[1], properties[4],
                properties[6], properties[5], properties[0] + properties[2]);
    }

}
