public class SmartLampWithColor extends SmartLamp {

    private String colorCode;
    private boolean colorMode;

    public SmartLampWithColor() {
        super();
        this.colorCode = null;
        this.colorMode = false;
    }

    /**
     *
     * @return colorCode value of the device
     */
    public String getColorCode() {
        return colorCode;
    }

    /**
     *
     * @param colorCode colorCode value of the device
     */

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    /**
     * if device is in color mode,color code is important,else the kelvin value.
     * @return if device is in color mode, return true ,else false
     */

    public boolean isColorMode() {
        return colorMode;
    }

    /**
     * if device is in color mode,color code is important,else the kelvin value.
     * @param colorMode device color mode (true or false)
     */

    public void setColorMode(boolean colorMode) {
        this.colorMode = colorMode;
    }


    @Override
    public String toString() {
        if ( isColorMode()){
            if (getSwitchTime() == null) {
                return "Smart Color Lamp " + getName() +
                        " is " + getStatus() +
                        " and its color value is " + getColorCode() + " with " + getBrightness() + "% brightness," +
                        " and its time to switch its status is " + getSwitchDate()+ ".";
            }else{
                if (getSwitchTime().getSecond() == 0) {
                    return "Smart Color Lamp " + getName() +
                            " is " + getStatus() +
                            " and its color value is " + getColorCode() + " with " + getBrightness() + "% brightness," +
                            " and its time to switch its status is " + getSwitchDate() + "_" + getSwitchTime() + ":00.";
                }else{
                    return "Smart Color Lamp " + getName() +
                            " is " + getStatus() +
                            " and its color value is " + getColorCode() + " with " + getBrightness() + "% brightness," +
                            " and its time to switch its status is " + getSwitchDate() + "_" + getSwitchTime() + ".";

                }
            }
        }else{
            if (getSwitchTime() == null) {
                return "Smart Color Lamp " + getName() +
                        " is " + getStatus() +
                        " and its color value is " + getKelvin() + "K with " + getBrightness() + "% brightness," +
                        " and its time to switch its status is " + getSwitchDate()+ ".";
            }else{
                if (getSwitchTime().getSecond() == 0) {
                    return "Smart Color Lamp " + getName() +
                            " is " + getStatus() +
                            " and its color value is " + getKelvin() + "K with " + getBrightness() + "% brightness," +
                            " and its time to switch its status is " + getSwitchDate() + "_" + getSwitchTime() + ":00.";
                }else{
                    return "Smart Color Lamp " + getName() +
                            " is " + getStatus() +
                            " and its color value is " + getKelvin() + "K with " + getBrightness() + "% brightness," +
                            " and its time to switch its status is " + getSwitchDate() + "_" + getSwitchTime() + ".";

                }
            }
        }
    }
}
