package domainLogic;

import contract.MediaContent;
import contract.Tag;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

public class Manager implements Serializable {
    private final long MAX_TOTAL_CAPACITY; // 500 MB in bytes
    private Map<String, MediaContent> contentMap = new HashMap<>();
    private Queue<String> availableAddresses = new LinkedList<>();
    private long currentTotalSize = 0;
    private Random random = new Random();
    private int addressCounter = 1;

    public Manager(long maxTotalCapacity) {
        MAX_TOTAL_CAPACITY = maxTotalCapacity;
    }


    public synchronized void create(String uploaderName, String mediaType, Set<Tag> tags) {
        long size = getRandomSize();

        // Check if adding this media would exceed the total capacity
        if (currentTotalSize + size > MAX_TOTAL_CAPACITY) {
            throw new IllegalArgumentException( MAX_TOTAL_CAPACITY + "haha" + size + " " + "Max capacity exceeded. Cannot upload media.");
        }

        String address = getNextAddress();
        MediaContent mediaContent;

        if (mediaType.equalsIgnoreCase("Audio")) {
            mediaContent = new AudioImpl(
                    getRandomSamplingRate(),
                    address,
                    tags,
                    0, // initial access count
                    size,
                    new UploaderImpl(uploaderName),
                    getRandomAvailability(),
                    getRandomCost()
            );
        } else if (mediaType.equalsIgnoreCase("Video")) {
            mediaContent = new VideoImpl(
                    address,
                    tags,
                    0, // initial access count
                    size,
                    new UploaderImpl(uploaderName),
                    getRandomAvailability(),
                    getRandomCost(),
                    getRandomResolution()
            );
        } else if (mediaType.equalsIgnoreCase("AudioVideo")) {
            mediaContent = new AudioVideoImpl(
                    getRandomSamplingRate(),
                    address,
                    tags,
                    0, // initial access count
                    size,
                    new UploaderImpl(uploaderName),
                    getRandomAvailability(),
                    getRandomCost(),
                    getRandomResolution()
            );
        } else {
            throw new IllegalArgumentException("Invalid media type.");
        }

        contentMap.put(address, mediaContent);
        currentTotalSize += size;
    }

    private String getNextAddress() {
        if (availableAddresses.isEmpty()) {
            return String.valueOf(addressCounter++);
        } else {
            return availableAddresses.poll();
        }
    }

    public synchronized List<String> read() {
        List<String> mediaDetails = new ArrayList<>();
        for (Map.Entry<String, MediaContent> entry : contentMap.entrySet()) {
            mediaDetails.add(getMediaDetails(entry.getValue()));
        }
        return mediaDetails;
    }

    // AM besten sollte ersetzt werden durch Event-system
    private String getMediaDetails(MediaContent content) {
        if (content instanceof AudioImpl) {
            AudioImpl audio = (AudioImpl) content;
            return String.format("Audio File [Address: %s, Size: %.2f MB, Sampling Rate: %d, Access Count: %d, Uploader: %s, Availability: %s, Cost: %.2f, Tags: %s]",
                    audio.getAddress(), audio.getSize() / 1_000_000.0, audio.getSamplingRate(), audio.getAccessCount(),
                    audio.getUploader().getName(), audio.getAvailability().toDays(), audio.getCost(),
                    audio.getTags());
        } else if (content instanceof VideoImpl) {
            VideoImpl video = (VideoImpl) content;
            return String.format("Video File [Address: %s, Size: %.2f MB, Resolution: %d, Access Count: %d, Uploader: %s, Availability: %s, Cost: %.2f, Tags: %s]",
                    video.getAddress(), video.getSize() / 1_000_000.0, video.getResolution(), video.getAccessCount(),
                    video.getUploader().getName(), video.getAvailability().toDays(), video.getCost(),
                    video.getTags());
        } else if (content instanceof AudioVideoImpl) {
            AudioVideoImpl audioVideo = (AudioVideoImpl) content;
            return String.format("AudioVideo File [Address: %s, Size: %.2f MB, Sampling Rate: %d, Resolution: %d, Access Count: %d, Uploader: %s, Availability: %s, Cost: %.2f, Tags: %s]",
                    audioVideo.getAddress(), audioVideo.getSize() / 1_000_000.0, audioVideo.getSamplingRate(), audioVideo.getResolution(),
                    audioVideo.getAccessCount(), audioVideo.getUploader().getName(), audioVideo.getAvailability().toDays(), audioVideo.getCost(),
                    audioVideo.getTags());
        } else {
            return "Unknown media type";
        }
    }

    public synchronized void updateAccessCount(String address) {
        MediaContent media = contentMap.get(address);
        if (media != null) {
            media.setAccessCount(media.getAccessCount()+1);
        }
    }

    public synchronized void deleteMedia(String address) {
        MediaContent removedMedia = contentMap.remove(address);
        if (removedMedia != null) {
            currentTotalSize -= removedMedia.getSize();
            availableAddresses.add(address);
        }
    }

    public synchronized void logout() {
        contentMap.clear();
        currentTotalSize = 0;
        addressCounter = 1;
        availableAddresses.clear();
    }


    // Help methods
    private long getRandomSize() {
        long minSize = 1_000_000L; // 1 MB in bytes
        long maxSize = 50_000_000L; // 50 MB in bytes
        return minSize + (long) (random.nextDouble() * (maxSize - minSize));
    }

    private int getRandomSamplingRate() {
        return 44_100;
    }

    private int getRandomResolution() {
        return 1080;
    }

    private Duration getRandomAvailability() {
        return Duration.ofDays(random.nextInt(365)); // Random duration up to 1 year
    }

    private BigDecimal getRandomCost() {
        return BigDecimal.valueOf(random.nextDouble() * 10).setScale(2, BigDecimal.ROUND_HALF_UP); // Random cost between 0 and 10, rounded to 2 decimal places
    }



    // Added for testing purposes
    public Map<String, MediaContent> getContentMap() {
        return contentMap;
    }

    public Queue<String> getAvailableAddresses() {
        return availableAddresses;
    }

    public long getCurrentTotalSize() {
        return currentTotalSize;
    }

    public int getAddressCounter() {
        return addressCounter;
    }

}
