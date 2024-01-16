import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Assignment2
 * @author OKTAY KAPLAN
 * @since 20.04.2023
 */

public class Main {

    /**
     * keep the current time as year month day
     */
    public static LocalDate date;
    /**
     * keep the current time as hour minute second
     */
    public static LocalTime time;
    /**
     * keep information of each device
     */
    public static LinkedHashMap<String,Device> device_map = new LinkedHashMap<>();
    /**
     * keep information of each lamp
     */
    public static LinkedHashMap<String,SmartLamp> lamp_map = new LinkedHashMap<>();
    /**
     * keep information of each colored lamp
     */
    public static LinkedHashMap<String,SmartLampWithColor> lampwithcolor_map = new LinkedHashMap<>();
    /**
     * keep information of each plug
     */
    public static LinkedHashMap<String,SmartPlug> plug_map = new LinkedHashMap<>();
    /**
     * keep information of each camera
     */
    public static LinkedHashMap<String,SmartCamera> camera_map = new LinkedHashMap<>();
    /**
     * stores the switch time of devices
     */
    public static LinkedHashMap<String,String> switch_map = new LinkedHashMap<>();
    /**
     * stores the switch time of devices
     */
    public static ArrayList<String> switch_date = new ArrayList<>();



    public static void main(String[] args) throws IOException {

        Reader reader = new Reader(args[0],args[1]);
        reader.readfile();
    }

    /**
     * Convert a base hexadecimal number to decimal number.
     * @param hexVal A hexadecimal number to convert to base decimal
     * @return an integer converted to base decimal
     */
    public static int hexadecimalToDecimal(String hexVal){
        int len = hexVal.length();
        int base = 1;
        int dec_val = 0;
        for (int i = len - 1; i >= 0; i--) {
            if((hexVal.charAt(i) >= '0' && hexVal.charAt(i) <= '9') || (hexVal.charAt(i) >= 'A' && hexVal.charAt(i) <= 'F')) {
                if (hexVal.charAt(i) >= '0' && hexVal.charAt(i) <= '9') {
                    dec_val += (hexVal.charAt(i) - 48) * base;
                    base = base * 16;
                } else if (hexVal.charAt(i) >= 'A' && hexVal.charAt(i) <= 'F') {
                    dec_val += (hexVal.charAt(i) - 55) * base;
                    base = base * 16;
                }
            }else{
                dec_val=0;
                return dec_val;
            }
        }return dec_val;

    }
    /**
     * print the date in padded format whether it has been given padded or not
     * @param edit date(padded or not given)
     * @return   the date in padded format
     */
    public static String date_edit(String edit){
        String[] split_edit =edit.split("_");
        String[] split_date = split_edit[0].split("-");
        String[] split_time = split_edit[1].split(":");
        for (int i =0;i<split_time.length;i++){
            if (split_time[i].length() == 1){
                split_time[i] = "0" + split_time[i];
                split_edit[1] = split_time[0] +":"+ split_time[1] +":"+ split_time[2];
            }
        }
        for (int i =0;i<split_date.length;i++) {
            if (split_date[i].length() == 1) {
                split_date[i] = "0" + split_date[i];
                split_edit[0] = split_date[0] +"-"+ split_date[1] +"-"+ split_date[2];
            }
        }
        return split_edit[0] +"_"+split_edit[1];
    }
    /**
     * sets the current time
     * @param date date as year month day
     * @param time time as hour minute second
     */
    public static void settime(String date,String time) {   //overloading
        Main.time = LocalTime.parse(time);
        Main.date = LocalDate.parse(date);
    }
    /**
     * sets the current time
     * @param date date as year month day
     * @param time time as hour minute second
     */
    public static void settime(LocalDate date,LocalTime time) {  //overloading
        Main.time = time;
        Main.date = date;
    }
    /**
     * print all devices's info
     * @param writer write object
     * @throws IOException occurs when the file is not found
     */
    public static void printZreport(BufferedWriter writer) throws IOException {
        LinkedHashMap<String, String> zreport_map = new LinkedHashMap<>();
        Collection<Device> devices = new ArrayList<>();
        devices = Main.device_map.values();
        Object[] arr = devices.toArray();
        for (int i = 0; i < Main.device_map.size(); i++) {
            List<String> keys = new ArrayList<>(Main.device_map.keySet());
            zreport_map.put(arr[i].toString(), Main.device_map.get(keys.get(i)).getSwitchdate());
        }
        //Devices in zreport_map are sorted by their switchtime.
        LinkedHashMap<String, String> sortedMap = zreport_map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        zreport_map = sortedMap;

        if (Main.time.getSecond() == 0) {
            writer.write("Time is:\t" + Main.date + "_" + Main.time +":00\n");
        }else{
            writer.write("Time is:\t" + Main.date + "_" + Main.time+"\n");
        }
        for (int i = 0; i < zreport_map.size(); i++) {
            List<String> keys = new ArrayList<>(zreport_map.keySet());
            if (zreport_map.get(keys.get(i)) != "0") {
                writer.write(keys.get(i)+"\n");
            }
        }
        for (int i = 0; i < zreport_map.size(); i++) {
            List<String> keys = new ArrayList<String>(zreport_map.keySet());
            if (zreport_map.get(keys.get(i)) == "0") {
                writer.write(keys.get(i)+"\n");
            }
        }
    }
}