import general.CLIControl;
import domainLogic.Manager;

public class cliMain {
    public static void main(String[] args) {
        Manager manager = new Manager();
        CLIControl cli = new CLIControl(manager);
        cli.run();
    }
}