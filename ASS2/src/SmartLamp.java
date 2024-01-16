public class SmartLamp extends Device {

    private int kelvin;
    private int brightness;

    public SmartLamp() {
        this.kelvin = 4000;
        this.brightness = 100;
    }

    /**
     *
     * @return kelvin value of the device
     */

    public int getKelvin() {
        return kelvin;
    }

    /**
     *
     * @param kelvin kelvin value of the device
     */

    public void setKelvin(int kelvin) {
        this.kelvin = kelvin;

    }

    /**
     *
     * @return brightness value of the device
     */

    public int getBrightness() {
        return brightness;
    }

    /**
     *
     * @param brightness brightness value of the device
     */

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }
    @Override
    public String toString() {
        if (getSwitchTime() == null) {
            return "Smart Lamp " + getName() +
                    " is " + getStatus() +
                    " and its kelvin value is " + getKelvin() + "K with " + getBrightness() + "% brightness," +
                    " and its time to switch its status is " + getSwitchDate()+ ".";
        }else{
            if (getSwitchTime().getSecond() == 0) {
                return "Smart Lamp " + getName() +
                        " is " + getStatus() +
                        " and its kelvin value is " + getKelvin() + "K with " + getBrightness() + "% brightness," +
                        " and its time to switch its status is " + getSwitchDate() + "_" + getSwitchTime() + ":00.";
           }else{
                return "Smart Lamp " + getName() +
                        " is " + getStatus() +
                        " and its kelvin value is " + getKelvin() + "K with " + getBrightness() + "% brightness," +
                        " and its time to switch its status is " + getSwitchDate() + "_" + getSwitchTime() + ".";
            }

        }
    }
}
