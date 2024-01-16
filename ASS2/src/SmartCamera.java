public class SmartCamera extends Device {
    private double storageUsage;
    private double megabytesPerMinute;

    public SmartCamera() {
        this.storageUsage = 0;
        this.megabytesPerMinute = 0;
    }

    /**
     *
     * @return total information recorded by the device
     */
    public double getStorageUsage() {
        return storageUsage;
    }

    /**
     *
     * @param storageUsage total information recorded by the device
     */

    public void setStorageUsage(double storageUsage) {
        this.storageUsage = storageUsage;
    }

    /**
     *
     * @return information that the device can record per minute
     */

    public double getMegabytesPerMinute() {
        return megabytesPerMinute;
    }

    /**
     *
     * @param megabytesPerMinute information that the device can record per minute
     */

    public void setMegabytesPerMinute(double megabytesPerMinute) {
        this.megabytesPerMinute = megabytesPerMinute;
    }

    @Override
    public String toString() {
        if (getSwitchTime() == null) {
            return "Smart Camera " + getName() +
                    " is " + getStatus() +
                    " and used "+ (String.format("%.2f", getStorageUsage())) + " MB of storage so far (excluding current status),"+
                    " and its time to switch its status is " + getSwitchDate()+ ".";
        }else{
            if (getSwitchTime().getSecond() == 0) {
                return "Smart Camera " + getName() +
                        " is " + getStatus() +
                        " and used " + (String.format("%.2f", getStorageUsage())) + " MB of storage so far (excluding current status)," +
                        " and its time to switch its status is " + getSwitchDate() + "_" + getSwitchTime() + ":00.";
            }else{
                return "Smart Camera " + getName() +
                        " is " + getStatus() +
                        " and used " + (String.format("%.2f", getStorageUsage())) + " MB of storage so far (excluding current status)," +
                        " and its time to switch its status is " + getSwitchDate() + "_" + getSwitchTime() + ".";
            }
        }
    }
}
