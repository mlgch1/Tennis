
package com.scorer.tennis;

import android.app.Application;

public class GlobalClass extends Application {

    // ***********************************

    private static String strClub = "";

    public static String getClub() { return strClub; }

    public static void setClub(String aClub) { strClub = aClub; }

    // ***********************************

    private static String strName_A = "";

    public static String getName_A() { return strName_A; }

    public static void setName_A(String aName_A) { strClub = aName_A; }

    // ***********************************

    private static String strName_B = "";

    public static String getName_B() { return strName_B; }

    public static void setName_B(String aName_B) { strName_B = aName_B; }

    // ***********************************

    private static boolean wifiSetup = false;

    public static boolean getWiFiSetup() { return wifiSetup; }

    public static void setWiFiSetup(boolean aSetup) { wifiSetup = aSetup; }

    // ***********************************

    private static boolean wifiRead = false;

    public static boolean getWiFiRead() {
        return wifiRead;
    }

    public static void setWiFiRead(boolean aRead) {
        wifiRead = aRead;
    }

    // ***********************************

    private static boolean test = false;

    public static boolean getTest() {
        return test;
    }

    public static void setTest(boolean aTest) {
        test = aTest;
    }

    // ***********************************

    private static boolean testing = false;

    public static boolean getStartTesting() {
        return testing;
    }

    public static void setStartTesting(boolean aTesting) {
        testing = aTesting;
    }

    // ***********************************

    private static int intThresh = 0;

    public static int getThresh() {
        return intThresh;
    }

    public static void setThresh(int aThresh) {
        intThresh = aThresh;
    }

    // ***********************************

    private static int intInc = 0;

    public static int getInc() {
        return intInc;
    }

    public static void setInc(int aInc) {
        intInc = aInc;
    }

    // ***********************************

    private static int intSSID = 0;

    public static int getSSID() {
        return intSSID;
    }

    public static void setSSID(int aSSID) {
        intSSID = aSSID;
    }

    // ***********************************

    private static int intChannel = 0;

    public static int getChannel() {
        return intChannel;
    }

    public static void setChannel(int aChannel) {
        intChannel = aChannel;
    }
}
