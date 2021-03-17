package server.bri.managers;

import server.model.Developer;

/**
 * Manager assign to each developer connected
 */
public class ProgServiceManager {
    private final Developer dev;

    public ProgServiceManager(Developer dev) {
        this.dev = dev;
    }

    public void installService(Class<?> bean) throws Exception {
        BRIManager.installService(this.dev, bean);
    }

    public void stopService(int numService) throws Exception {
        BRIManager.stopService(this.dev, numService);
    }

    public void startService(int numService) throws Exception {
        BRIManager.startService(this.dev, numService);
    }

    public void uninstallService(int numService) throws Exception {
        BRIManager.uninstallService(this.dev, numService);
    }

    public void updateService(Class<?> beanUpdated, int numService) throws Exception {
        BRIManager.updateService(this.dev, beanUpdated, numService);
    }
}
