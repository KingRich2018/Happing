package top.happing.conf.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "happing.id")
public class IdProperties {

    ////
    private long epoch;
    private long center;
    private long worker;

    ////



    public long getEpoch() {
        return epoch;
    }

    public void setEpoch(long epoch) {
        this.epoch = epoch;
    }

    public long getCenter() {
        return center;
    }

    public void setCenter(long center) {
        this.center = center;
    }

    public long getWorker() {
        return worker;
    }

    public void setWorker(long worker) {
        this.worker = worker;
    }
}
