package domainLogic;

public class AdminManager {

    Manager adminManager;
    public AdminManager(Manager manager) {
        this.adminManager = manager;
    }

    public Manager getAdminManager() {
        return adminManager;
    }

    public void setAdminManager(Manager adminManager) {
        this.adminManager = adminManager;
    }
}
