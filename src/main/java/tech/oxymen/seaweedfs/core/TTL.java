package tech.oxymen.seaweedfs.core;

public class TTL {
    
    //examples, 3m: 3 minutes, 4h: 4 hours, 5d: 5 days, 6w: 6 weeks,
     //*             7M: 7 months, 8y: 8 years
    public static String years(int value) {

        return String.valueOf(value) + "y";
    }

    public static String months(int value) {

        return String.valueOf(value) + "M";
    }


    public static String weeks(int value) {

        return String.valueOf(value) + "w";
    }

    public static String days(int value) {

        return String.valueOf(value) + "d";
    }

    public static String hours(int value) {

        return String.valueOf(value) + "h";
    }

    public static String minutes(int value) {

        return String.valueOf(value) + "m";
    }

}
