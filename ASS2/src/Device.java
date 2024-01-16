import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Device {

    /**
     * Indicates whether the device is on or off.
     */
    private String status;
    /**
     * the switch time of the device in hours, minutes ,seconds.
     */
    private LocalTime switchTime;
    /**
     * the switch time of the device in years, months and days.
     */
    private LocalDate switchDate;
    /**
     *device's name
     */
    private String name;
    /**
     * It is used in the ZReport command to sort devices by switchtime.
     */
    private String switchdate;
    /**
     * It is used to calculate the values of camera and plug.
     */
    private LocalDateTime ontime;

    public Device() {
        this.status = "off";
        this.switchTime = null;
        this.switchDate = null;
        this.switchdate = "0";
        this.ontime = LocalDateTime.of(Main.date,Main.time);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return switch time of the device in hours, minutes, seconds.
     */

    public LocalTime getSwitchTime() {
        return switchTime;
    }

    /**
     *
     * @param switchTime switch time of the device in hours, minutes, seconds.
     */

    public void setSwitchTime(LocalTime switchTime) {
        this.switchTime = switchTime;
    }

    /**
     *
     * @return switch date of the device in years,months,days.
     */

    public LocalDate getSwitchDate() {
        return switchDate;
    }

    /**
     *
     * @param switchDate switch date of the device in years,months,days.
     */

    public void setSwitchDate(LocalDate switchDate) {
        this.switchDate = switchDate;
    }

    /**
     * It is used in the ZReport command to sort devices by switchtime.
     * @return device's switchdate as string
     */
    public String getSwitchdate() {
        return switchdate;
    }

    /**
     *
     * @param switchdate device's switchdate as string
     */

    public void setSwitchdate(String switchdate) {
        this.switchdate = switchdate;
    }

    /**
     * @return device's opentime in years,months,days,hours, minutes, seconds.
     */

    public LocalDateTime getOntime() {
        return ontime;
    }

    /**
     *
     * @param ontime device's opentime in years,months,days,hours, minutes, seconds.
     */

    public void setOntime(LocalDateTime ontime) {
        this.ontime = ontime;
    }
}

