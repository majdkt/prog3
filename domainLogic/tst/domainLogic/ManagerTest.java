package domainLogic;

import contract.Audio;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

    @Test
    void create() {
        Manager m = new Manager();
        AudioImpl a = new AudioImpl(2000, "whatever");
        m.create(19, "whatever");
        m.update(0);
        a.setAccessCount(99);
        assertEquals(1,m.read().get(0).getAccessCount());
    }



    @Test
    void update() {
        Manager m = new Manager();
        AudioImpl a = new AudioImpl(3000,"whatever");
        m.create(1,"whatever");
        m.update(0);
        assertEquals(1,m.read().get(0).getAccessCount());
    }

    @Test
    void delete() {
        Manager m = new Manager();
        AudioImpl a = new AudioImpl(1000,"whatever");
        m.create(1,"whatever");
        m.create(2,"whatever");
        m.update(0);
        m.delete(m.read().get(0));
        assertEquals(1, m.read().size());
    }

    @Test
    void read() {
        Manager m = new Manager();
        m.create(3,"Berlin");
        m.create(2, "Frankfurt");
        m.create(28, "Hamburg");

        List<Audio> readList = m.read();
        readList.clear();
        readList.add(new AudioImpl(4000, "New York"));
        List<Audio> readList2 = m.read();

        assertEquals(3,readList2.size());
    }

    @Test
    void testGetSamplingRateAndGetAdress(){
        Manager m = new Manager();
        int expectedSamplingRate = 44100;
        String expectedAdress= "Berlin";
        m.create(expectedSamplingRate,expectedAdress);
        Audio audio = m.read().get(0);
        assertEquals(expectedSamplingRate,audio.getSamplingRate());
        assertEquals(expectedAdress,audio.getAddress());
    }

}