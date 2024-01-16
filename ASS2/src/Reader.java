import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Reader {
    /**
     * input file to be read
     */
    String input_file;
    /**
     * output file to be overwritten
     */
    String output_file;

    /**
     * @param input_file the name of the file to be read and taken as argument0
     * @param output_file the name of the file taken as an argument1 and to be overwritten
     */

    public Reader(String input_file,String output_file) {

        this.input_file = input_file;
        this.output_file = output_file;
    }

    /**
     * in here,input.txt is read.
     * input file is read line by line and split.
     * The first word in the line is looked at and operations are performed according to the mission of that word.
     * finally,appropriate outputs are written on the output file.
     * @throws IOException occurs when the file is not found
     */

    public void readfile() throws IOException {
        FileReader fileReader1 = new FileReader(input_file);
        BufferedReader br1 = new BufferedReader(fileReader1);
        BufferedWriter writer = new BufferedWriter(new FileWriter(output_file));
        String line;
        String lastline = null;  //This variable is created to see if your last command is ZReport or not.
        int count1 = 0;
        int count2 = 0;
        while ((line = br1.readLine()) != null) {
            String[] split = line.split("\t"); //It was created to check whether the first time was given in the setinitialtime command.
            lastline = line;
            if (!line.isEmpty()) { //If the line is empty, that line is skipped.
                if (count2 > 0 || ((line.startsWith("SetInitialTime") && split.length>1))) {
                    try {
                        count2++;
                        if (line.equals("ZReport")) {
                            writer.write("COMMAND: ZReport\n");
                            Main.printZreport(writer);
                        } else if (line.equals("Nop")) {
                            writer.write("COMMAND: " + line+"\n");
                            if (Main.switch_map.size() > 0) {
                                List<String> keys = new ArrayList<>(Main.switch_map.keySet());
                                String[] date = Main.switch_date.get(0).split("_");
                                Main.settime(LocalDate.parse(date[0]), LocalTime.parse(date[1]));
                                try {
                                    while (LocalDateTime.of(LocalDate.parse(date[0]),LocalTime.parse(date[1])).equals(LocalDateTime.of(Main.date, Main.time))){
                                        if (Main.plug_map.containsKey(keys.get(0))) {
                                            if (Main.plug_map.get(keys.get(0)).getStatus().equals("on")) {
                                                double time = (Main.plug_map.get(keys.get(0)).getVoltage() / 60.0)*Main.plug_map.get(keys.get(0)).getAmpere()*
                                                        (double) Main.plug_map.get(keys.get(0)).getOntime().until
                                                                (LocalDateTime.of(Main.date, Main.time), ChronoUnit.MINUTES);
                                                Main.plug_map.get(keys.get(0)).setTotalEnergyConsumption(Main.plug_map.get(keys.get(0)).getTotalEnergyConsumption()+time);
                                                Main.device_map.replace(keys.get(0), Main.plug_map.get(keys.get(0)));
                                            } else {
                                                Main.plug_map.get(keys.get(0)).setOntime(LocalDateTime.of(Main.date, Main.time));
                                            }
                                        } else if (Main.camera_map.containsKey(keys.get(0))) {
                                            if (Main.camera_map.get(keys.get(0)).getStatus().equals("on")) {
                                                double time = Main.camera_map.get(keys.get(0)).getMegabytesPerMinute()*
                                                        (double) Main.device_map.get(keys.get(0)).getOntime().until
                                                                (LocalDateTime.of(Main.date, Main.time), ChronoUnit.MINUTES);
                                                Main.camera_map.get(keys.get(0)).setStorageUsage(Main.camera_map.get(keys.get(0)).getStorageUsage()+time);
                                                Main.device_map.replace(keys.get(0), Main.camera_map.get(keys.get(0)));
                                            } else {
                                                Main.camera_map.get(keys.get(0)).setOntime(LocalDateTime.of(Main.date, Main.time));
                                            }
                                        }
                                        Main.device_map.get(keys.get(0)).setSwitchDate(null);
                                        Main.device_map.get(keys.get(0)).setSwitchTime(null);
                                        Main.device_map.get(keys.get(0)).setSwitchdate("0");
                                        if (Main.device_map.get(keys.get(0)).getStatus().equals("on")) {
                                            Main.device_map.get(keys.get(0)).setStatus("off");
                                        } else {
                                            Main.device_map.get(keys.get(0)).setStatus("on");
                                            Main.device_map.get(keys.get(0)).setOntime(LocalDateTime.of(Main.date, Main.time));
                                        }

                                        /*This trick is used to prevent the device
                                          whose switchtime time comes after the Nop command from being written to the last in ZReport.*/
                                        LinkedHashMap<String, Device> device_map_edit = new LinkedHashMap<>();
                                        device_map_edit.put(keys.get(0), Main.device_map.get(keys.get(0)));
                                        Main.device_map.remove(keys.get(0));
                                        device_map_edit.putAll(Main.device_map);
                                        Main.device_map = device_map_edit;

                                        Main.switch_map.remove(keys.get(0));
                                        Main.switch_date.remove(0);

                                        date = Main.switch_date.get(0).split("_");
                                        keys = new ArrayList<String>(Main.switch_map.keySet());
                                    }
                                }catch (Exception e) {
                                }
                            } else {
                                writer.write("ERROR: There is nothing to switch!\n");
                            }
                        } else {
                            String[] split_line = line.split("\t");
                            if (split_line[0].equals("SetInitialTime")) {
                                split_line[1] = Main.date_edit(split_line[1]);
                                writer.write("COMMAND: SetInitialTime\t" + split_line[1]+"\n");
                                if (count1 == 0) {    //It is checked that the initialtime is set once
                                    String[] liner = split_line[1].split("_");
                                    Main.settime(liner[0], liner[1]);
                                    if (Main.time.getSecond() == 0){
                                        writer.write("SUCCESS: Time has been set to " + Main.date +"_"+Main.time+ ":00!\n");
                                    }else{
                                        writer.write("SUCCESS: Time has been set to " + Main.date +"_"+Main.time+ "!\n");
                                    }
                                    count1++;
                                } else {
                                    writer.write("ERROR: Erroneous command!\n");
                                }
                            }
                            else if (split_line[0].equals("SetTime")) {
                                List<String> keys = new ArrayList<>(Main.switch_map.keySet());
                                split_line[1] = Main.date_edit(split_line[1]);
                                writer.write("COMMAND: SetTime\t" + split_line[1]+"\n");
                                String[] liner = split_line[1].split("_");
                                try {
                                    LocalDateTime new_date = LocalDateTime.of(LocalDate.parse(liner[0]), LocalTime.parse(liner[1]));
                                    if (new_date.isAfter(LocalDateTime.of(Main.date, Main.time))) {
                                        try {
                                            LocalTime new_time = LocalTime.parse(liner[1]);
                                            while (new_date.compareTo(LocalDateTime.of(Main.device_map.get(keys.get(0)).getSwitchDate(),Main.device_map.get(keys.get(0)).getSwitchTime()))>=0){
                                                if(new_date.compareTo(LocalDateTime.of(Main.device_map.get(keys.get(0)).getSwitchDate(),Main.device_map.get(keys.get(0)).getSwitchTime()))>=0){
                                                    Main.settime(liner[0], liner[1]);
                                                    if (Main.plug_map.containsKey(keys.get(0))) {
                                                        if (Main.plug_map.get(keys.get(0)).getStatus().equals("off")) {
                                                            Main.plug_map.get(keys.get(0)).setOntime(LocalDateTime.of(Main.date, Main.time));
                                                        } else {
                                                            double time = (Main.plug_map.get(keys.get(0)).getVoltage() / 60.0) * Main.plug_map.get(keys.get(0)).getAmpere()*
                                                                    (double) Main.plug_map.get(keys.get(0)).getOntime().until
                                                                            (LocalDateTime.of(Main.device_map.get(keys.get(0)).getSwitchDate(),Main.device_map.get(keys.get(0)).getSwitchTime()), ChronoUnit.MINUTES);
                                                            Main.plug_map.get(keys.get(0)).setTotalEnergyConsumption(Main.plug_map.get(keys.get(0)).getTotalEnergyConsumption()+time);
                                                            Main.device_map.replace(keys.get(0), Main.plug_map.get(keys.get(0)));
                                                        }
                                                    } else if (Main.camera_map.containsKey(keys.get(0))) {
                                                        if (Main.camera_map.get(keys.get(0)).getStatus().equals("off")) {
                                                            Main.camera_map.get(keys.get(0)).setOntime(LocalDateTime.of(Main.date, Main.time));
                                                        } else {
                                                            double time = Main.camera_map.get(keys.get(0)).getMegabytesPerMinute() *
                                                                    (double) Main.device_map.get(keys.get(0)).getOntime().until
                                                                            (LocalDateTime.of(Main.device_map.get(keys.get(0)).getSwitchDate(), Main.device_map.get(keys.get(0)).getSwitchTime()), ChronoUnit.MINUTES);
                                                            Main.camera_map.get(keys.get(0)).setStorageUsage(Main.camera_map.get(keys.get(0)).getStorageUsage()+time);
                                                            Main.device_map.replace(keys.get(0), Main.camera_map.get(keys.get(0)));
                                                        }
                                                    }
                                                    Main.device_map.get(keys.get(0)).setSwitchDate(null);
                                                    Main.device_map.get(keys.get(0)).setSwitchTime(null);
                                                    Main.device_map.get(keys.get(0)).setSwitchdate("0");
                                                    if (Main.device_map.get(keys.get(0)).getStatus().equals("on")) {
                                                        Main.device_map.get(keys.get(0)).setStatus("off");
                                                    } else {
                                                        Main.device_map.get(keys.get(0)).setStatus("on");
                                                        Main.device_map.get(keys.get(0)).setOntime(LocalDateTime.of(Main.date, Main.time));
                                                    }
                                                    /*This trick is used to prevent the device
                                                    whose switchtime time comes after the SetTime command from being written to the last in ZReport.*/
                                                    LinkedHashMap<String, Device> device_map_edit = new LinkedHashMap<>();
                                                    device_map_edit.put(keys.get(0), Main.device_map.get(keys.get(0)));
                                                    Main.device_map.remove(keys.get(0));
                                                    device_map_edit.putAll(Main.device_map);
                                                    Main.device_map = device_map_edit;

                                                    Main.switch_date.remove(0);
                                                    Main.switch_map.remove(keys.get(0));
                                                    keys.remove(0);

                                                }
                                                else if(new_date.compareTo(LocalDateTime.of(Main.device_map.get(keys.get(0)).getSwitchDate(), Main.device_map.get(keys.get(0)).getSwitchTime())) == 0){
                                                    Main.settime(liner[0], liner[1]);
                                                    break;
                                                }
                                            }if (new_date.isAfter(LocalDateTime.of(Main.date, Main.time)) && new_time.isBefore(Main.device_map.get(keys.get(0)).getSwitchTime())) {
                                                Main.settime(liner[0], liner[1]);
                                            }
                                        } catch (Exception e) {
                                            Main.settime(liner[0], liner[1]);
                                        }

                                    }else if(new_date.equals(LocalDateTime.of(Main.date, Main.time))){
                                        writer.write("ERROR: There is nothing to change!\n");

                                    } else {
                                        writer.write("ERROR: Time cannot be reversed!\n");
                                    }
                                } catch (Exception e) {
                                    writer.write("ERROR: Time format is not correct!\n");
                                }
                            } else if (split_line[0].equals("SkipMinutes")) {
                                writer.write("COMMAND: " + line+"\n");
                                List<String> keys = new ArrayList<>(Main.switch_map.keySet());
                                try {
                                    LocalTime new_time = Main.time.plusMinutes(Integer.parseInt(split_line[1]));
                                    LocalDateTime new_date = LocalDateTime.of(Main.date, Main.time).plusMinutes(Integer.parseInt(split_line[1]));
                                    try {
                                        if (split_line.length == 2 && Integer.parseInt(split_line[1]) > 0) {
                                            while (new_date.compareTo(LocalDateTime.of(Main.device_map.get(keys.get(0)).getSwitchDate(), Main.device_map.get(keys.get(0)).getSwitchTime())) >= 0) {
                                                if(new_date.compareTo(LocalDateTime.of(Main.device_map.get(keys.get(0)).getSwitchDate(), Main.device_map.get(keys.get(0)).getSwitchTime()))>=0){
                                                    /*This trick is used to prevent the device
                                                    whose switchtime time comes after the SkipMinutes command from being written to the last in ZReport.*/
                                                    LinkedHashMap<String, Device> device_map_edit = new LinkedHashMap<>();
                                                    device_map_edit.put(keys.get(0), Main.device_map.get(keys.get(0)));
                                                    Main.device_map.remove(keys.get(0));
                                                    device_map_edit.putAll(Main.device_map);
                                                    Main.device_map = device_map_edit;

                                                    if (Main.plug_map.containsKey(keys.get(0))) {
                                                        if (Main.plug_map.get(keys.get(0)).getStatus().equals("off")) {
                                                            Main.plug_map.get(keys.get(0)).setOntime(LocalDateTime.of(Main.date, Main.time));
                                                        } else {
                                                            double time = (Main.plug_map.get(keys.get(0)).getVoltage() / 60.0) * Main.plug_map.get(keys.get(0)).getAmpere() *
                                                                    (double) Main.device_map.get(keys.get(0)).getOntime().until
                                                                            (LocalDateTime.of(Main.device_map.get(keys.get(0)).getSwitchDate(), Main.device_map.get(keys.get(0)).getSwitchTime()), ChronoUnit.MINUTES);
                                                            Main.plug_map.get(keys.get(0)).setTotalEnergyConsumption(Main.plug_map.get(keys.get(0)).getTotalEnergyConsumption() + time);
                                                            Main.device_map.replace(keys.get(0), Main.plug_map.get(keys.get(0)));
                                                        }
                                                    } else if (Main.camera_map.containsKey(keys.get(0))) {
                                                        if (Main.camera_map.get(keys.get(0)).getStatus().equals("off")) {
                                                            Main.camera_map.get(keys.get(0)).setOntime(LocalDateTime.of(Main.date, Main.time));
                                                        } else {
                                                            double time = Main.camera_map.get(keys.get(0)).getMegabytesPerMinute() * (double) Main.device_map.get(keys.get(0)).getOntime().until
                                                                    (LocalDateTime.of(Main.device_map.get(keys.get(0)).getSwitchDate(), Main.device_map.get(keys.get(0)).getSwitchTime()), ChronoUnit.MINUTES);
                                                            Main.camera_map.get(keys.get(0)).setStorageUsage(Main.camera_map.get(keys.get(0)).getStorageUsage()+time);
                                                            Main.device_map.replace(keys.get(0), Main.camera_map.get(keys.get(0)));
                                                        }
                                                    }
                                                    Main.device_map.get(keys.get(0)).setSwitchDate(null);
                                                    Main.device_map.get(keys.get(0)).setSwitchTime(null);
                                                    Main.device_map.get(keys.get(0)).setSwitchdate("0");
                                                    if (Main.device_map.get(keys.get(0)).getStatus().equals("on")) {
                                                        Main.device_map.get(keys.get(0)).setStatus("off");
                                                    } else {
                                                        Main.device_map.get(keys.get(0)).setStatus("on");
                                                        Main.device_map.get(keys.get(0)).setOntime(LocalDateTime.of(Main.date, Main.time));
                                                    }
                                                    Main.switch_date.remove(0);
                                                    Main.switch_map.remove(keys.get(0));
                                                    keys.remove(0);
                                                } else {
                                                    Main.time = new_time;
                                                    Main.date = new_date.toLocalDate();
                                                    break;
                                                }
                                            }
                                            Main.time = new_time;
                                            Main.date = new_date.toLocalDate();
                                        } else if (split_line.length == 2 && Integer.parseInt(split_line[1]) == 0) {
                                            writer.write("ERROR: There is nothing to skip!\n");
                                        } else if (split_line.length == 2 && Integer.parseInt(split_line[1]) < 0) {
                                            writer.write("ERROR: Time cannot be reversed!\n");
                                        } else {
                                            writer.write("ERROR: Erroneous command!\n");
                                        }
                                    } catch (Exception e) {
                                        Main.time = new_time;
                                        Main.date = new_date.toLocalDate();
                                    }
                                }catch (Exception e) {
                                    writer.write("ERROR: Erroneous command!\n");
                                }
                            } else if (split_line[0].equals("Add") && split_line[1].equals("SmartPlug")) {
                                writer.write("COMMAND: " + line+"\n");
                                SmartPlug plug = new SmartPlug();
                                if (split_line.length == 3) {
                                    if (!Main.device_map.containsKey(split_line[2])) {
                                        plug.setName(split_line[2]);
                                        Main.device_map.put(split_line[2], plug);
                                        Main.plug_map.put(split_line[2], plug);
                                    } else {
                                        writer.write("ERROR: There is already a smart device with same name!\n");
                                    }
                                } else if (split_line.length == 4) {
                                    if (!Main.device_map.containsKey(split_line[2])) {
                                        if (split_line[3].equals("On") || split_line[3].equals("Off")) {
                                            if (split_line[3].equals("On")) {
                                                plug.setStatus("on");
                                                plug.setOntime(LocalDateTime.of(Main.date, Main.time));
                                            } else {
                                                plug.setStatus("off");
                                            }
                                            plug.setName(split_line[2]);
                                            Main.device_map.put(split_line[2], plug);
                                            Main.plug_map.put(split_line[2], plug);
                                        } else {
                                            writer.write("ERROR: Erroneous command!\n");
                                        }
                                    } else {
                                        writer.write("ERROR: There is already a smart device with same name!\n");
                                    }
                                } else if (split_line.length == 5) {
                                    if (!Main.device_map.containsKey(split_line[2])) {
                                        if (split_line[3].equals("On") || split_line[3].equals("Off")) {
                                            if (Double.parseDouble(split_line[4]) > 0) {
                                                if (split_line[3].equals("On")) {
                                                    plug.setStatus("on");
                                                    plug.setOntime(LocalDateTime.of(Main.date, Main.time));
                                                } else {
                                                    plug.setStatus("off");
                                                }
                                                plug.setPluggedIn(true);
                                                plug.setName(split_line[2]);
                                                plug.setAmpere(Double.parseDouble(split_line[4]));
                                                Main.device_map.put(split_line[2], plug);
                                                Main.plug_map.put(split_line[2], plug);
                                            } else {
                                                writer.write("ERROR: Ampere value must be a positive number!\n");
                                            }
                                        } else {
                                            writer.write("ERROR: Erroneous command!\n");
                                        }
                                    } else {
                                        writer.write("ERROR: There is already a smart device with same name!\n");
                                    }
                                } else {
                                    writer.write("ERROR: Erroneous command!\n");
                                }
                            } else if (split_line[0].equals("Add") && split_line[1].equals("SmartCamera")) {
                                writer.write("COMMAND: " + line+"\n");
                                SmartCamera camera = new SmartCamera();
                                if (split_line.length == 4) {
                                    if (!Main.device_map.containsKey(split_line[2])) {
                                        if (Integer.parseInt(split_line[3]) > 0) {
                                            camera.setName(split_line[2]);
                                            camera.setMegabytesPerMinute(Integer.parseInt(split_line[3]));
                                            Main.device_map.put(split_line[2], camera);
                                            Main.camera_map.put(split_line[2], camera);
                                        } else {
                                            writer.write("ERROR: Megabyte value must be a positive number!\n");
                                        }
                                    } else {
                                        writer.write("ERROR: There is already a smart device with same name!\n");
                                    }
                                } else if (split_line.length == 5) {
                                    if (!Main.device_map.containsKey(split_line[2])) {
                                        if (Double.parseDouble(split_line[3]) > 0) {
                                            if (split_line[4].equals("On") || split_line[4].equals("Off")) {
                                                if (split_line[4].equals("On")) {
                                                    camera.setStatus("on");
                                                    camera.setOntime(LocalDateTime.of(Main.date, Main.time));
                                                } else {
                                                    camera.setStatus("off");
                                                }
                                                camera.setName(split_line[2]);
                                                camera.setMegabytesPerMinute(Double.parseDouble(split_line[3]));
                                                Main.device_map.put(split_line[2], camera);
                                                Main.camera_map.put(split_line[2], camera);
                                            } else {
                                                writer.write("ERROR: Erroneous command!\n");
                                            }
                                        } else {
                                            writer.write("ERROR: Megabyte value must be a positive number!\n");
                                        }
                                    } else {
                                        writer.write("ERROR: There is already a smart device with same name!\n");
                                    }
                                } else {
                                    writer.write("ERROR: Erroneous command!\n");
                                }
                            } else if (split_line[0].equals("Add") && split_line[1].equals("SmartLamp")) {
                                writer.write("COMMAND: " + line+"\n");
                                SmartLamp lamps = new SmartLamp();
                                if (!Main.device_map.containsKey(split_line[2])) {
                                    if (split_line.length == 3) {
                                        lamps.setName(split_line[2]);
                                        Main.device_map.put(lamps.getName(), lamps);
                                        Main.lamp_map.put(lamps.getName(), lamps);
                                    } else if (split_line.length == 4) {
                                        if (split_line[3].equals("On") || split_line[3].equals("Off")) {
                                            lamps.setName(split_line[2]);
                                            lamps.setStatus(split_line[3].toLowerCase());
                                            Main.device_map.put(lamps.getName(), lamps);
                                            Main.lamp_map.put(lamps.getName(), lamps);
                                        } else {
                                            writer.write("ERROR: Erroneous command!\n");
                                        }
                                    } else if (split_line.length == 6) {
                                        if (split_line[3].equals("On") || split_line[3].equals("Off")) {
                                            try{
                                                if(Math.floor(Double.parseDouble(split_line[4])) == Double.parseDouble(split_line[4])){
                                                    if (Integer.parseInt(split_line[4]) >= 2000 && Integer.parseInt(split_line[4]) <= 6500) {
                                                        if(Math.floor(Double.parseDouble(split_line[5])) == Double.parseDouble(split_line[5])) {
                                                            if (Integer.parseInt(split_line[5]) >= 0 && Integer.parseInt(split_line[5]) <= 100) {
                                                                lamps.setKelvin(Integer.parseInt(split_line[4]));
                                                                lamps.setStatus(split_line[3].toLowerCase());
                                                                lamps.setName(split_line[2]);
                                                                lamps.setBrightness(Integer.parseInt(split_line[5]));
                                                                Main.device_map.put(lamps.getName(), lamps);
                                                                Main.lamp_map.put(lamps.getName(), lamps);
                                                            } else {
                                                                writer.write("ERROR: Brightness must be in range of 0%-100%!\n");
                                                            }
                                                        }else{
                                                            writer.write("ERROR: Brightness value must be an integer\n");
                                                        }
                                                    } else {
                                                        writer.write("ERROR: Kelvin value must be in range of 2000K-6500K!\n");
                                                    }
                                                }else{
                                                    writer.write("ERROR: Kelvin value must be an integer\n");
                                                }
                                            }catch (Exception e){
                                                writer.write("ERROR: Kelvin and Brightness value must be an integer\n");
                                            }
                                        } else {
                                            writer.write("ERROR: Erroneous command!\n");
                                        }
                                    } else {
                                        writer.write("ERROR: Erroneous command!\n");
                                    }
                                } else {
                                    writer.write("ERROR: There is already a smart device with same name!\n");
                                }
                            } else if (split_line[0].equals("Add") && split_line[1].equals("SmartColorLamp")) {
                                writer.write("COMMAND: " + line+"\n");
                                SmartLampWithColor lampwithcolor = new SmartLampWithColor();
                                if (!Main.device_map.containsKey(split_line[2])) {
                                    if (split_line.length == 3) {
                                        lampwithcolor.setName(split_line[2]);
                                        Main.device_map.put(lampwithcolor.getName(), lampwithcolor);
                                        Main.lampwithcolor_map.put(lampwithcolor.getName(), lampwithcolor);
                                    } else if (split_line.length == 4) {
                                        if (split_line[3].equals("On") || split_line[3].equals("Off")) {
                                            lampwithcolor.setStatus(split_line[3].toLowerCase());
                                            lampwithcolor.setName(split_line[2]);
                                            Main.device_map.put(lampwithcolor.getName(), lampwithcolor);
                                            Main.lampwithcolor_map.put(lampwithcolor.getName(), lampwithcolor);
                                        } else {
                                            writer.write("ERROR: Erroneous command!\n");
                                        }
                                    } else if (split_line.length == 6) {
                                        String[] liner = split_line[4].split("x");
                                        if (split_line[3].equals("On") || split_line[3].equals("Off")) {
                                            if (split_line[4].contains("x")) {
                                                if (Main.hexadecimalToDecimal(liner[1]) > 0 && Main.hexadecimalToDecimal(liner[1]) <= 16777215) {
                                                    try{
                                                        if(Math.floor(Double.parseDouble(split_line[5])) == Double.parseDouble(split_line[5])){
                                                            if (Integer.parseInt(split_line[5]) >= 0 && Integer.parseInt(split_line[5]) <= 100) {
                                                                lampwithcolor.setBrightness(Integer.parseInt(split_line[5]));
                                                                lampwithcolor.setName(split_line[2]);
                                                                lampwithcolor.setColorMode(true);
                                                                lampwithcolor.setStatus(split_line[3].toLowerCase());
                                                                lampwithcolor.setColorCode((split_line[4]));
                                                                Main.device_map.put(lampwithcolor.getName(), lampwithcolor);
                                                                Main.lampwithcolor_map.put(lampwithcolor.getName(), lampwithcolor);
                                                            } else {
                                                                writer.write("ERROR: Brightness must be in range of 0%-100%!\n");
                                                            }
                                                        }else{
                                                            writer.write("ERROR: Brightness value must be an integer\n");
                                                        }
                                                    }catch (Exception e){
                                                        writer.write("ERROR:Brightness value must be an integer\n");
                                                    }
                                                } else if (Main.hexadecimalToDecimal(liner[1]) == 0) {
                                                    writer.write("ERROR: Erroneous command!\n");
                                                } else {
                                                    writer.write("ERROR: Color code value must be in range of 0x0-0xFFFFFF!\n");
                                                }
                                            } else {
                                                try{
                                                    if(Math.floor(Double.parseDouble(split_line[4])) == Double.parseDouble(split_line[4])){
                                                        if (Integer.parseInt(split_line[4]) >= 2000 && Integer.parseInt(split_line[4]) <= 6500) {
                                                            if(Math.floor(Double.parseDouble(split_line[5])) == Double.parseDouble(split_line[5])){
                                                                if (Integer.parseInt(split_line[5]) >= 0 && Integer.parseInt(split_line[5]) <= 100) {
                                                                    lampwithcolor.setBrightness(Integer.parseInt(split_line[5]));
                                                                    lampwithcolor.setName(split_line[2]);
                                                                    lampwithcolor.setStatus(split_line[3].toLowerCase());
                                                                    lampwithcolor.setKelvin(Integer.parseInt(split_line[4]));
                                                                    Main.device_map.put(lampwithcolor.getName(), lampwithcolor);
                                                                    Main.lampwithcolor_map.put(lampwithcolor.getName(), lampwithcolor);
                                                                } else {
                                                                    writer.write("ERROR: Brightness must be in range of 0%-100%!\n");
                                                                }
                                                            } else {
                                                                writer.write("ERROR: Brightness value must be an integer");
                                                            }
                                                        } else {
                                                            writer.write("ERROR: Kelvin value must be in range of 2000K-6500K!\n");
                                                        }
                                                    }else{
                                                        writer.write("ERROR: Kelvin value must be an integer\n");
                                                    }
                                                }catch (Exception e){
                                                    writer.write("ERROR: Kelvin and Brightness value must be an integer\n");
                                                }
                                            }
                                        } else {
                                            writer.write("ERROR: Erroneous command!\n");
                                        }
                                    }else {
                                        writer.write("ERROR: Erroneous command!\n");
                                    }
                                } else {
                                    writer.write("ERROR: There is already a smart device with same name!\n");
                                }
                            } else if (split_line[0].equals("Remove")) {
                                writer.write("COMMAND: " + line+"\n");
                                if (split_line.length == 2) {
                                    if (Main.device_map.containsKey(split_line[1])) {
                                        if (Main.plug_map.containsKey(split_line[1])) {
                                            if (Main.plug_map.get(split_line[1]).getStatus().equals("on")) {
                                                double time = (Main.plug_map.get(split_line[1]).getVoltage() / 60.0) * Main.plug_map.get(split_line[1]).getAmpere() *
                                                        (double) Main.plug_map.get(split_line[1]).getOntime().until
                                                                (LocalDateTime.of(Main.date, Main.time), ChronoUnit.MINUTES);
                                                Main.plug_map.get(split_line[1]).setTotalEnergyConsumption(Main.plug_map.get(split_line[1]).getTotalEnergyConsumption() + time);
                                                Main.device_map.replace(split_line[1], Main.plug_map.get(split_line[1]));
                                            }
                                            Main.plug_map.remove(split_line[1]);
                                        } else if (Main.lamp_map.containsKey(split_line[1])) {
                                            Main.lamp_map.remove(split_line[1]);
                                        } else if (Main.lampwithcolor_map.containsKey(split_line[1])) {
                                            Main.lampwithcolor_map.remove(split_line[1]);
                                        } else if (Main.camera_map.containsKey(split_line[1])) {
                                            if (Main.camera_map.get(split_line[1]).getStatus().equals("on")) {
                                                double time = Main.camera_map.get(split_line[1]).getMegabytesPerMinute() *
                                                        (double) Main.device_map.get(split_line[1]).getOntime().until
                                                                (LocalDateTime.of(Main.date, Main.time), ChronoUnit.MINUTES);
                                                Main.camera_map.get(split_line[1]).setStorageUsage(Main.camera_map.get(split_line[1]).getStorageUsage()+time);
                                                Main.device_map.replace(split_line[1], Main.camera_map.get(split_line[1]));
                                            }
                                            Main.camera_map.remove(split_line[1]);
                                        }
                                        if (Main.device_map.get(split_line[1]).getStatus().equals("on")) {
                                            Main.device_map.get(split_line[1]).setStatus("off");
                                        }
                                        writer.write("SUCCESS: Information about removed smart device is as follows:\n");
                                        writer.write(Main.device_map.get(split_line[1]).toString()+"\n");
                                        Main.device_map.remove(split_line[1]);
                                    } else {
                                        writer.write("ERROR: There is not such a device!\n");
                                    }
                                } else {
                                    writer.write("ERROR: Erroneous command!\n");
                                }
                            } else if (split_line[0].equals("SetSwitchTime")) {
                                writer.write("COMMAND: " + split_line[0]+"\t"+split_line[1]+"\t"+split_line[2]+"\n");
                                if (split_line.length == 3) {
                                    if (Main.device_map.containsKey(split_line[1])) {
                                        split_line[2] = Main.date_edit(split_line[2]);
                                        String[] date = split_line[2].split("_");
                                        LocalDateTime switch_date = LocalDateTime.of(LocalDate.parse(date[0]),LocalTime.parse(date[1]));
                                        if (switch_date.isAfter(LocalDateTime.of(Main.date, Main.time))) {
                                            Main.device_map.get(split_line[1]).setSwitchTime(LocalTime.parse(date[1]));
                                            Main.device_map.get(split_line[1]).setSwitchdate(split_line[2]);
                                            Main.device_map.get(split_line[1]).setSwitchDate(LocalDate.parse(date[0]));

                                            Main.switch_date.add(split_line[2]);
                                            Collections.sort(Main.switch_date);
                                            Main.switch_map.put(split_line[1], split_line[2]);

                                            //Devices in switch_map are sorted by their switchtime.
                                            LinkedHashMap<String, String> sortedMap = Main.switch_map.entrySet()
                                                    .stream()
                                                    .sorted(Map.Entry.comparingByValue())
                                                    .collect(Collectors.toMap(
                                                            Map.Entry::getKey,
                                                            Map.Entry::getValue,
                                                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                                            Main.switch_map = sortedMap;
                                        } else if(switch_date.isEqual(LocalDateTime.of(Main.date, Main.time))){
                                            if (Main.device_map.get(split_line[1]).getStatus().equals("off")) {
                                                Main.device_map.get(split_line[1]).setStatus("on");
                                                Main.device_map.get(split_line[1]).setOntime(LocalDateTime.of(Main.date, Main.time));
                                            }else if (Main.device_map.get(split_line[1]).getStatus().equals("on")) {
                                                Main.device_map.get(split_line[1]).setStatus("off");
                                            }
                                            /*This trick is used to prevent the device
                                                    whose switchtime time comes after the SetSwitchTime command from being written to the last in ZReport.*/
                                            LinkedHashMap<String, Device> device_map1 = new LinkedHashMap<>();
                                            device_map1.put(split_line[1], Main.device_map.get(split_line[1]));
                                            Main.device_map.remove(split_line[1]);
                                            device_map1.putAll(Main.device_map);
                                            Main.device_map = device_map1;
                                        }else{
                                            writer.write("ERROR: Switch time cannot be in the past!\n");
                                        }
                                    } else {
                                        writer.write("ERROR: There is not such a device!\n");
                                    }
                                } else {
                                    writer.write("ERROR: Erroneous command!\n");
                                }
                            } else if (split_line[0].equals("Switch")) {
                                writer.write("COMMAND: " + line+"\n");
                                if (split_line.length == 3) {
                                    if (Main.device_map.containsKey(split_line[1])) {
                                        if (!Main.device_map.get(split_line[1]).getStatus().equals(split_line[2].toLowerCase())) {
                                            Main.device_map.get(split_line[1]).setStatus(split_line[2].toLowerCase());
                                            if (split_line[2].equals("On")) {
                                                Main.device_map.get(split_line[1]).setOntime(LocalDateTime.of(Main.date, Main.time));
                                            }
                                            if (Main.switch_map.containsKey(split_line[1])){
                                                Main.switch_map.remove(split_line[1]);
                                                Main.switch_date.remove(split_line[1]);
                                                Main.device_map.get(split_line[1]).setSwitchTime(null);
                                                Main.device_map.get(split_line[1]).setSwitchDate(null);
                                                Main.device_map.get(split_line[1]).setSwitchdate("0");
                                                /*This trick is used to prevent the device
                                                  whose switchtime time comes after the Switch command from being written to the last in ZReport.*/
                                                LinkedHashMap<String, Device> device_map_edit = new LinkedHashMap<>();
                                                device_map_edit.put(split_line[1], Main.device_map.get(split_line[1]));
                                                Main.device_map.remove(split_line[1]);
                                                device_map_edit.putAll(Main.device_map);
                                                Main.device_map = device_map_edit;
                                            }
                                            if (Main.plug_map.containsKey(split_line[1])) {
                                                if (split_line[2].equals("Off")) {
                                                    double time = (Main.plug_map.get(split_line[1]).getVoltage() / 60.0) * Main.plug_map.get(split_line[1]).getAmpere() *
                                                            (double) Main.plug_map.get(split_line[1]).getOntime().until
                                                                    (LocalDateTime.of(Main.date, Main.time), ChronoUnit.MINUTES);
                                                    Main.plug_map.get(split_line[1]).setTotalEnergyConsumption(Main.plug_map.get(split_line[1]).getTotalEnergyConsumption() + time);
                                                    Main.device_map.replace(split_line[1], Main.plug_map.get(split_line[1]));
                                                } else {
                                                    Main.plug_map.get(split_line[1]).setOntime(LocalDateTime.of(Main.date, Main.time));
                                                }
                                            } else if (Main.camera_map.containsKey(split_line[1])) {
                                                if (split_line[2].equals("Off")) {
                                                    double time = Main.camera_map.get(split_line[1]).getMegabytesPerMinute() *
                                                            (double) Main.device_map.get(split_line[1]).getOntime().until
                                                                    (LocalDateTime.of(Main.date, Main.time), ChronoUnit.MINUTES);
                                                    Main.camera_map.get(split_line[1]).setStorageUsage(Main.camera_map.get(split_line[1]).getStorageUsage()+ time);
                                                    Main.device_map.replace(split_line[1], Main.camera_map.get(split_line[1]));
                                                } else {
                                                    Main.camera_map.get(split_line[1]).setOntime(LocalDateTime.of(Main.date, Main.time));
                                                }
                                            }
                                        } else {
                                            writer.write("ERROR: This device is already switched " + split_line[2].toLowerCase() + "!\n");
                                        }
                                    } else {
                                        writer.write("ERROR: There is not such a device!\n");
                                    }
                                } else {
                                    writer.write("ERROR: Erroneous command!\n");
                                }
                            } else if (split_line[0].equals("ChangeName")) {
                                writer.write("COMMAND: " + line+"\n");
                                if (split_line.length == 3) {
                                    if (!split_line[1].equals(split_line[2])) {
                                        try {
                                            if (Main.device_map.containsKey(split_line[1])) {
                                                if (Main.device_map.get(split_line[2]).getName().equals(split_line[2])) {
                                                    writer.write("ERROR: There is already a smart device with same name!\n");
                                                }
                                            } else {
                                                writer.write("ERROR: There is not such a device!\n");
                                            }
                                        } catch (Exception e) {
                                            if (Main.plug_map.containsKey(split_line[1])) {
                                                Main.plug_map.get(split_line[1]).setName(split_line[2]);
                                                Main.plug_map.put(split_line[2], Main.plug_map.get(split_line[1]));
                                            } else if (Main.lamp_map.containsKey(split_line[1])) {
                                                Main.lamp_map.get(split_line[1]).setName(split_line[2]);
                                                Main.lamp_map.put(split_line[2], Main.lamp_map.get(split_line[1]));
                                            } else if (Main.lampwithcolor_map.containsKey(split_line[1])) {
                                                Main.lampwithcolor_map.get(split_line[1]).setName(split_line[2]);
                                                Main.lampwithcolor_map.put(split_line[2], Main.lampwithcolor_map.get(split_line[1]));
                                            } else if (Main.camera_map.containsKey(split_line[1])) {
                                                Main.camera_map.get(split_line[1]).setName(split_line[2]);
                                                Main.camera_map.put(split_line[2], Main.camera_map.get(split_line[1]));
                                            }

                                            Main.device_map.get(split_line[1]).setName(split_line[2]);
                                            Main.device_map.put(split_line[2], Main.device_map.get(split_line[1]));
                                        }
                                    } else {
                                        writer.write("ERROR: Both of the names are the same, nothing changed!\n");
                                    }
                                } else {
                                    writer.write("ERROR: Erroneous command!\n");
                                }
                            } else if (split_line[0].equals("PlugIn")) {
                                writer.write("COMMAND: " + line+"\n");
                                if (split_line.length == 3) {
                                    if (Main.plug_map.containsKey(split_line[1])) {
                                        if (!Main.plug_map.get(split_line[1]).isPluggedIn()) {
                                            if (Integer.parseInt(split_line[2]) > 0) {
                                                Main.plug_map.get(split_line[1]).setPluggedIn(true);
                                                Main.plug_map.get(split_line[1]).setAmpere(Integer.parseInt(split_line[2]));
                                                Main.device_map.put(split_line[1], Main.plug_map.get(split_line[1]));
                                                if (Main.plug_map.get(split_line[1]).getStatus().equals("on")) {
                                                    double time = (Main.plug_map.get(split_line[1]).getVoltage() / 60.0) * Main.plug_map.get(split_line[1]).getAmpere() *
                                                            (double) Main.plug_map.get(split_line[1]).getOntime().until
                                                                    (LocalDateTime.of(Main.date, Main.time), ChronoUnit.MINUTES);
                                                    Main.plug_map.get(split_line[1]).setTotalEnergyConsumption(Main.plug_map.get(split_line[1]).getTotalEnergyConsumption() + time);
                                                    Main.device_map.replace(split_line[1], Main.plug_map.get(split_line[1]));
                                                }
                                            } else {
                                                writer.write("ERROR: Ampere value must be a positive number!\n");
                                            }
                                        } else {
                                            writer.write("ERROR: There is already an item plugged in to that plug!\n");
                                        }
                                    } else {
                                        writer.write("ERROR: This device is not a smart plug!\n");
                                    }
                                } else {
                                    writer.write("ERROR: Erroneous command!\n");
                                }
                            } else if (split_line[0].equals("PlugOut")) {
                                writer.write("COMMAND: " + line+"\n");
                                if (split_line.length == 2) {
                                    if (Main.plug_map.containsKey(split_line[1])) {
                                        if (Main.plug_map.get(split_line[1]).isPluggedIn()) {
                                            Main.plug_map.get(split_line[1]).setPluggedIn(false);
                                            Main.device_map.put(split_line[1], Main.plug_map.get(split_line[1]));
                                            if (Main.plug_map.get(split_line[1]).getStatus().equals("on")) {
                                                double time = (Main.plug_map.get(split_line[1]).getVoltage() / 60.0) * Main.plug_map.get(split_line[1]).getAmpere() *
                                                        (double) Main.plug_map.get(split_line[1]).getOntime().until
                                                                (LocalDateTime.of(Main.date, Main.time), ChronoUnit.MINUTES);
                                                Main.plug_map.get(split_line[1]).setTotalEnergyConsumption(Main.plug_map.get(split_line[1]).getTotalEnergyConsumption() + time);
                                                Main.device_map.replace(split_line[1], Main.plug_map.get(split_line[1]));
                                                Main.device_map.get(split_line[1]).setOntime(LocalDateTime.of(Main.date,Main.time));
                                                Main.plug_map.get(split_line[1]).setOntime(LocalDateTime.of(Main.date,Main.time));
                                            }
                                        } else {
                                            writer.write("ERROR: This plug has no item to plug out from that plug!\n");
                                        }
                                    } else {
                                        writer.write("ERROR: This device is not a smart plug!\n");
                                    }
                                } else {
                                    writer.write("ERROR: Erroneous command!\n");
                                }
                            } else if (split_line[0].equals("SetKelvin")) {
                                writer.write("COMMAND: " + line+"\n");
                                if (split_line.length == 3) {
                                    if (Main.lamp_map.containsKey(split_line[1])) {
                                        try{
                                            if(Math.floor(Double.parseDouble(split_line[2])) == Double.parseDouble(split_line[2])){
                                                if (2000 <= Integer.parseInt(split_line[2]) && Integer.parseInt(split_line[2]) <= 6500) {
                                                    Main.lamp_map.get(split_line[1]).setKelvin(Integer.parseInt(split_line[2]));
                                                    Main.device_map.put(split_line[1], Main.lamp_map.get(split_line[1]));
                                                } else {
                                                    writer.write("ERROR: Kelvin value must be in range of 2000K-6500K!\n");
                                                }
                                            }else{
                                                writer.write("ERROR: Kelvin value must be an integer\n");
                                            }
                                        }catch (Exception e){
                                            writer.write("ERROR: Kelvin value must be an integer\n");
                                        }
                                    } else if (Main.lampwithcolor_map.containsKey(split_line[1])) {
                                        try{
                                            if(Math.floor(Double.parseDouble(split_line[2])) == Double.parseDouble(split_line[2])){
                                                if (2000 <= Integer.parseInt(split_line[2]) && Integer.parseInt(split_line[2]) <= 6500) {
                                                    Main.lampwithcolor_map.get(split_line[1]).setKelvin(Integer.parseInt(split_line[2]));
                                                    Main.device_map.put(split_line[1], Main.lampwithcolor_map.get(split_line[1]));
                                                } else {
                                                    writer.write("ERROR: Kelvin value must be in range of 2000K-6500K!\n");
                                                }
                                            }else{
                                                writer.write("ERROR: Kelvin value must be an integer\n");
                                            }
                                        }catch (Exception e){
                                            writer.write("ERROR: Kelvin value must be an integer\n");
                                        }
                                    } else {
                                        writer.write("ERROR: This device is not a smart lamp!\n");
                                    }
                                } else {
                                    writer.write("ERROR: Erroneous command!\n");
                                }
                            } else if (split_line[0].equals("SetBrightness")) {
                                writer.write("COMMAND: " + line+"\n");
                                if (split_line.length == 3) {
                                    if (Main.lamp_map.containsKey(split_line[1])) {
                                        try{
                                            if(Math.floor(Double.parseDouble(split_line[2])) == Double.parseDouble(split_line[2])){
                                                if (0 <= Integer.parseInt(split_line[2]) && Integer.parseInt(split_line[2]) <= 100) {
                                                    Main.lamp_map.get(split_line[1]).setBrightness(Integer.parseInt(split_line[2]));
                                                    Main.device_map.put(split_line[1], Main.lamp_map.get(split_line[1]));
                                                } else {
                                                    writer.write("ERROR: Brightness must be in range of 0%-100%!\n");
                                                }
                                            }else{
                                                writer.write("ERROR: Brightness value must be an integer\n");
                                            }
                                        }catch (Exception e){
                                            writer.write("ERROR: Brightness value must be an integer\n");
                                        }
                                    } else if (Main.lampwithcolor_map.containsKey(split_line[1])) {
                                        try{
                                            if(Math.floor(Double.parseDouble(split_line[2])) == Double.parseDouble(split_line[2])){
                                                if (0 <= Integer.parseInt(split_line[2]) && Integer.parseInt(split_line[2]) <= 100) {
                                                    Main.lampwithcolor_map.get(split_line[1]).setBrightness(Integer.parseInt(split_line[2]));
                                                    Main.device_map.put(split_line[1], Main.lampwithcolor_map.get(split_line[1]));
                                                } else {
                                                    writer.write("ERROR: Brightness must be in range of 0%-100%!\n");
                                                }
                                            }else{
                                                writer.write("ERROR: Brightness value must be an integer\n");
                                            }
                                        }catch (Exception e){
                                            writer.write("ERROR: Brightness value must be an integer\n");
                                        }
                                    } else {
                                        writer.write("ERROR: This device is not a smart lamp!\n");
                                    }
                                } else {
                                    writer.write("ERROR: Erroneous command!\n");
                                }
                            } else if (split_line[0].equals("SetColorCode")) {
                                writer.write("COMMAND: " + line+"\n");
                                if (split_line.length == 3) {
                                    if (Main.lampwithcolor_map.containsKey(split_line[1])) {
                                        if (split_line[2].startsWith("0x")) {
                                            String[] liner = split_line[2].split("x");
                                            if (Main.hexadecimalToDecimal(liner[2]) > 0 && Main.hexadecimalToDecimal(liner[2]) <= 16777215) {
                                                Main.lampwithcolor_map.get(split_line[1]).setColorCode((liner[2]));
                                                Main.device_map.put(split_line[1], Main.lampwithcolor_map.get(split_line[1]));
                                            } else if (Main.hexadecimalToDecimal(liner[1]) == 0) {
                                                writer.write("ERROR: Erroneous command!\n");
                                            } else {
                                                writer.write("ERROR: Color code value must be in range of 0x0-0xFFFFFF\n");
                                            }
                                        } else {
                                            writer.write("ERROR: Erroneous command!\n");
                                        }
                                    } else {
                                        writer.write("ERROR: This device is not a smart color lamp!\n");
                                    }
                                } else {
                                    writer.write("ERROR: Erroneous command!\n");
                                }
                            } else if (split_line[0].equals("SetWhite")) {
                                writer.write("COMMAND: " + line+"\n");
                                if (split_line.length == 4) {
                                    if (Main.lampwithcolor_map.containsKey(split_line[1])) {
                                        try{
                                            if(Math.floor(Double.parseDouble(split_line[2])) == Double.parseDouble(split_line[2])){
                                                if (2000 <= Integer.parseInt(split_line[2]) && Integer.parseInt(split_line[2]) <= 6500) {
                                                    if(Math.floor(Double.parseDouble(split_line[3])) == Double.parseDouble(split_line[3])) {
                                                        if (0 <= Integer.parseInt(split_line[3]) && Integer.parseInt(split_line[3]) <= 100) {
                                                            Main.lampwithcolor_map.get(split_line[1]).setKelvin(Integer.parseInt(split_line[2]));
                                                            Main.lampwithcolor_map.get(split_line[1]).setBrightness(Integer.parseInt(split_line[3]));
                                                            Main.device_map.put(split_line[1], Main.lampwithcolor_map.get(split_line[1]));
                                                        } else {
                                                            writer.write("ERROR: Brightness must be in range of 0%-100%!\n");
                                                        }
                                                    }else{
                                                        writer.write("ERROR: Brightness value must be an integer\n");
                                                    }
                                                } else {
                                                    writer.write("ERROR: Kelvin value must be in range of 2000K-6500K!\n");
                                                }
                                            }else{
                                                writer.write("ERROR: Kelvin value must be an integer\n");
                                            }
                                        }catch (Exception e){
                                            writer.write("ERROR: Kelvin and Brightness value must be an integer\n");
                                        }
                                    } else if (Main.lamp_map.containsKey(split_line[1])) {
                                        try{
                                            if(Math.floor(Double.parseDouble(split_line[2])) == Double.parseDouble(split_line[2])){
                                                if (2000 <= Integer.parseInt(split_line[2]) && Integer.parseInt(split_line[2]) <= 6500) {
                                                    if(Math.floor(Double.parseDouble(split_line[3])) == Double.parseDouble(split_line[3])) {
                                                        if (0 <= Integer.parseInt(split_line[3]) && Integer.parseInt(split_line[3]) <= 100) {
                                                            Main.lamp_map.get(split_line[1]).setKelvin(Integer.parseInt(split_line[2]));
                                                            Main.lamp_map.get(split_line[1]).setBrightness(Integer.parseInt(split_line[3]));
                                                            Main.lamp_map.put(split_line[1], Main.lamp_map.get(split_line[1]));
                                                        } else {
                                                            writer.write("ERROR: Brightness must be in range of 0%-100%!\n");
                                                        }
                                                    }else{
                                                        writer.write("ERROR: Brightness value must be an integer\n");
                                                    }
                                                } else {
                                                    writer.write("ERROR: Kelvin value must be in range of 2000K-6500K!\n");
                                                }
                                            }else{
                                                writer.write("ERROR: Kelvin value must be an integer\n");
                                            }
                                        }catch (Exception e){
                                            writer.write("ERROR: Kelvin and Brightness value must be an integer\n");
                                        }
                                    } else {
                                        writer.write("ERROR: This device is not a smart lamp!\n");
                                    }
                                } else {
                                    writer.write("ERROR: Erroneous command!\n");
                                }
                            } else if (split_line[0].equals("SetColor")) {
                                writer.write("COMMAND: " + line+"\n");
                                if (split_line.length == 4) {
                                    if (Main.lampwithcolor_map.containsKey(split_line[1])) {
                                        if (split_line[2].startsWith("0x")) {
                                            String[] liner = split_line[2].split("x");
                                            if (Main.hexadecimalToDecimal(liner[1]) > 0 && Main.hexadecimalToDecimal(liner[1]) <= 16777215) {
                                                try{
                                                    if(Math.floor(Double.parseDouble(split_line[3])) == Double.parseDouble(split_line[3])) {
                                                        if (0 <= Integer.parseInt(split_line[3]) && Integer.parseInt(split_line[3]) <= 100) {
                                                            Main.lampwithcolor_map.get(split_line[1]).setColorCode((split_line[2]));
                                                            Main.lampwithcolor_map.get(split_line[1]).setColorMode(true);
                                                            Main.lampwithcolor_map.get(split_line[1]).setBrightness(Integer.parseInt(split_line[3]));
                                                            Main.device_map.put(split_line[1], Main.lampwithcolor_map.get(split_line[1]));
                                                        } else {
                                                            writer.write("ERROR: Brightness must be in range of 0%-100%!\n");
                                                        }
                                                    }else{
                                                        writer.write("ERROR: Brightness value must be an integer\n");
                                                    }
                                                }catch (Exception e){
                                                    writer.write("ERROR: Brightness value must be an integer\n");
                                                }
                                            } else if (Main.hexadecimalToDecimal(liner[1]) == 0) {
                                                writer.write("ERROR: Erroneous command!\n");
                                            } else {
                                                writer.write("ERROR: Color code value must be in range of 0x0-0xFFFFFF\n");
                                            }
                                        } else {
                                            writer.write("ERROR: Erroneous command!\n");
                                        }
                                    } else {
                                        writer.write("ERROR: This device is not a smart color lamp!\n");
                                    }
                                } else {
                                    writer.write("ERROR: Erroneous command!\n");
                                }
                            } else {
                                writer.write("COMMAND: " + line+"\n");
                                writer.write("ERROR: Erroneous command!\n");
                            }
                        }
                    }catch (Exception e){
                        writer.write("ERROR: Format of the initial date is wrong! Program is going to terminate!");
                        lastline = "ZReport";
                        break;
                    }
                } else{
                    writer.write("COMMAND: " + line+"\n");
                    writer.write("ERROR: First command must be set initial time! Program is going to terminate!\n");
                    lastline = "ZReport";
                    break;
                }
            }
        }
        if(!lastline.equals("ZReport")){
            writer.write("ZReport:\n");
            Main.printZreport(writer);
        }
        writer.close();
    }
}
