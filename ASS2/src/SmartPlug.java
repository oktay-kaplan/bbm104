public class SmartPlug extends Device {

    private int voltage;
    private double ampere;
    private boolean pluggedIn;
    /**
     * total energy consumption recorded by the device
     */
    private double totalEnergyConsumption;

    public SmartPlug() {
        this.voltage = 220;
        this.ampere = 0;
        this.pluggedIn = false;
        this.totalEnergyConsumption = 0;
    }

    /**
     *
     * @return voltage value of the device
     */

    public int getVoltage() {
        return voltage;
    }

    /**
     *
     * @return ampere value of the device
     */
    public double getAmpere() {
        return ampere;
    }

    /**
     *
     * @param ampere ampere value of the device
     */
    public void setAmpere(double ampere) {
        this.ampere = ampere;
    }

    /**
     * Indicates whether the device is plugged in or not.
     * @return if device is in plug in, return true ,else false
     */
    public boolean isPluggedIn() {
        return pluggedIn;
    }

    /**
     * Indicates whether the device is plugged in or not.
     * @param pluggedIn Whether the device is plugged in or not (true or false)
     */
    public void setPluggedIn(boolean pluggedIn) {
        this.pluggedIn = pluggedIn;
    }

    /**
     *
     * @return total energy consumption recorded by the device
     */
    public double getTotalEnergyConsumption() {return totalEnergyConsumption; }

    /**
     *
     * @param totalEnergyConsumption total energy consumption recorded by the device
     */
    public void setTotalEnergyConsumption(double totalEnergyConsumption) { this.totalEnergyConsumption = totalEnergyConsumption; }

    @Override
    public String toString() {
        if (getSwitchTime() == null) {
            return "Smart Plug " + getName() +
                    " is " + getStatus() +
                    " and consumed "+ (String.format("%.2f", getTotalEnergyConsumption())) + "W so far (excluding current device),"+
                    " and its time to switch its status is " + getSwitchDate()+ ".";
        }else{
            if (getSwitchTime().getSecond() == 0) {
                return "Smart Plug " + getName() +
                        " is " + getStatus() +
                        " and consumed " + (String.format("%.2f", getTotalEnergyConsumption())) + "W so far (excluding current device)," +
                        " and its time to switch its status is " + getSwitchDate() + "_" + getSwitchTime() + ":00.";
            }else{
                return "Smart Plug " + getName() +
                        " is " + getStatus() +
                        " and consumed " + (String.format("%.2f", getTotalEnergyConsumption())) + "W so far (excluding current device)," +
                        " and its time to switch its status is " + getSwitchDate() + "_" + getSwitchTime() + ".";
            }
        }
    }

}