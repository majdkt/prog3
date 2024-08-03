package contract;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    @Test
    void testEnumValues() {
        // Verify that the enum constants are correctly defined
        Tag[] expectedTags = {Tag.Animal, Tag.Review, Tag.Music, Tag.News};
        assertArrayEquals(expectedTags, Tag.values(), "Enum constants should match the expected values");
    }

    @Test
    void testEnumValueOf() {
        assertEquals(Tag.Animal, Tag.valueOf("Animal"), "Tag should be Animal");
        assertEquals(Tag.Review, Tag.valueOf("Review"), "Tag should be Review");
        assertEquals(Tag.Music, Tag.valueOf("Music"), "Tag should be Music");
        assertEquals(Tag.News, Tag.valueOf("News"), "Tag should be News");
    }

    @Test
    void testEnumName() {
        assertEquals("Animal", Tag.Animal.name(), "Name of Animal tag should be 'Animal'");
        assertEquals("Review", Tag.Review.name(), "Name of Review tag should be 'Review'");
        assertEquals("Music", Tag.Music.name(), "Name of Music tag should be 'Music'");
        assertEquals("News", Tag.News.name(), "Name of News tag should be 'News'");
    }

    @Test
    void testEnumOrdinal() {
        assertEquals(0, Tag.Animal.ordinal(), "Ordinal of Animal tag should be 0");
        assertEquals(1, Tag.Review.ordinal(), "Ordinal of Review tag should be 1");
        assertEquals(2, Tag.Music.ordinal(), "Ordinal of Music tag should be 2");
        assertEquals(3, Tag.News.ordinal(), "Ordinal of News tag should be 3");
    }
}
