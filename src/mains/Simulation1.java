package mains;

import domainLogic.Manager;

public class Simulation1 {
    public static void main(String[] args) {
        //mains.Simulation1 beinhaltet Random Faktor

        Manager manager = new Manager();

        Thread createThread = new Thread(() -> {
            while (true) {
                //manager.create();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread deleteThread = new Thread(() -> {
            while (true) {
                String address = "address_" + (manager.read().size());
                manager.delete(address);
               // manager.read().forEach(System.out::println);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        createThread.start();
        deleteThread.start();

        System.out.println("Final list of audio files:");
        manager.read().forEach(System.out::println);
    }
}
