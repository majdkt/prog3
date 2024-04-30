package domainLogic;

import contract.Audio;
import contract.Uploadable;
import contract.Uploader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {


    @Test
    void testManager() {
        Manager manager = new Manager();
        Uploadable audio = new AudioImpl(2,"m",2);
        audio.getUploader().getName();

        assertEquals("majd", audio.getUploader().getName());
    }

    @Test
    void create() {
        Manager m = new Manager();
        Audio a = new AudioImpl(2000, "whatever", 80);
        m.create();
        a.ge
        m.update(0);
        assertEquals(1,m.read().get(0).getAccessCount());
    }



    @Test
    void update() {
        Manager m = new Manager();
        AudioImpl a = new AudioImpl(3000,"whatever", 4);
        m.create(1,"whatever",1);
        m.update(0);
        assertEquals(1,m.read().get(0).getAccessCount());
    }

    @Test
    void delete() {
        Manager m = new Manager();
        AudioImpl a = new AudioImpl(1000,"whatever", 2);
        m.create(1,"whatever",9);
        m.create(2,"whatever", 1);
        m.update(0);
        m.delete(m.read().get(0));
        assertEquals(1, m.read().size());
    }

    @Test
    void read() {
        Manager m = new Manager();
        m.create(3,"Berlin", 10);
        m.create(2, "Frankfurt", 50);
        m.create(28, "Hamburg", 12);

        List<Audio> readList = m.read();
        readList.clear();
        readList.add(new AudioImpl(4000, "New York", 4));
        List<Audio> readList2 = m.read();

        assertEquals(3,readList2.size());
    }

    @Test
    void testGetSamplingRateANDGetAdressANDGetSize(){
        Manager m = new Manager();
        int expectedSamplingRate = 44100;
        String expectedAdress= "Berlin";
        long expectedSize = 80;
        m.create(expectedSamplingRate,expectedAdress, expectedSize);
        m.update(0);
        Audio audio = m.read().get(0);
        assertEquals(1,audio.getAccessCount());
        assertEquals(2,audio.getAccessCount());
        assertEquals(expectedSamplingRate,audio.getSamplingRate());
        assertEquals(expectedAdress,audio.getAddress());
        assertEquals(80,audio.getSize());
    }

}