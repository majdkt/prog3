public class Simulation1 {
    public static void main(String[] args) {
        Menu menu = new Menu();
        for (int i = 0; i < 5; i++) {
            menu.createMedia();
        }
        menu.readMedia();
    }
}
