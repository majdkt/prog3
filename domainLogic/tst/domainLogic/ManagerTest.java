package domainLogic;

import contract.Audio;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

    @Test
    void create() {
        Manager m = new Manager();
        AudioImpl a = new AudioImpl(2000);
        m.create(19);
        m.update(0);
        a.setAccessCount(99);
        assertEquals(1,m.read().get(0).getAccessCount());
    }



    @org.junit.jupiter.api.Test
    void update() {
        Manager m = new Manager();
        AudioImpl a = new AudioImpl(3000);
        m.create(1);
        m.update(0);
        assertEquals(1,m.read().get(0).getAccessCount());
    }

    @org.junit.jupiter.api.Test
    void delete() {
        Manager m = new Manager();
        AudioImpl a = new AudioImpl(1000);
        m.create(1);
        m.create(2);
        m.update(0);
        m.delete(m.read().get(0));
        assertEquals(1, m.read().size());
    }

    @org.junit.jupiter.api.Test
    void read() {
        Manager m = new Manager();
        m.create(3);
        m.create(2);
        m.create(28);

        List<Audio> readList = m.read();
        readList.clear();
        readList.add(new AudioImpl(4000));
        List<Audio> readList2 = m.read();

        assertEquals(3,readList2.size());
    }

    @org.junit.jupiter.api.Test
    void testGetSamplingRate(){
        Manager m = new Manager();
        int expectedSamplingRate = 44100;
        m.create(expectedSamplingRate);
        Audio audio = m.read().get(0);
        assertEquals(expectedSamplingRate,audio.getSamplingRate());
    }

}