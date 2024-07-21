package domainLogic;

import contract.MediaContent;
import contract.Tag;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

public class Manager implements Serializable {
    private static final long MAX_TOTAL_CAPACITY = 500_000_000L; // 500 MB in bytes
    private Map<String, MediaContent> contentMap = new HashMap<>();
    private Queue<String> availableAddresses = new LinkedList<>();
    private long currentTotalSize = 0;
    private Random random = new Random();
    private int addressCounter = 1;

    public synchronized void uploadMedia(String uploaderName, String mediaType, Set<Tag> tags) {
        long size = getRandomSize();

        // Check if adding this media would exceed the total capacity
        if (currentTotalSize + size > MAX_TOTAL_CAPACITY) {
            throw new IllegalArgumentException("Max capacity exceeded. Cannot upload media.");
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
                    new UploaderImpl(uploaderName),
                    tags,
                    address,
                    size,
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
            return "address_" + addressCounter++;
        } else {
            return availableAddresses.poll();
        }
    }

    private long getRandomSize() {
        long minSize = 1_000_000L; // 1 MB in bytes
        long maxSize = 100_000_000L; // 100 MB in bytes
        return minSize + (long) (random.nextDouble() * (maxSize - minSize));
    }

    private int getRandomSamplingRate() {
        return 44_100; // Example fixed sampling rate for simplicity
    }

    private int getRandomResolution() {
        return 1080; // Example fixed resolution for simplicity
    }

    private Duration getRandomAvailability() {
        return Duration.ofDays(random.nextInt(365)); // Random duration up to 1 year
    }

    private BigDecimal getRandomCost() {
        return BigDecimal.valueOf(random.nextDouble() * 10); // Random cost between 0 and 10
    }

    public synchronized List<String> read() {
        List<String> mediaDetails = new ArrayList<>();
        for (Map.Entry<String, MediaContent> entry : contentMap.entrySet()) {
            mediaDetails.add(getMediaDetails(entry.getValue()));
        }
        return mediaDetails;
    }

    public synchronized void updateAccessCount(String address, long newAccessCount) {
        MediaContent media = contentMap.get(address);
        if (media != null) {
            media.setAccessCount(newAccessCount);
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

    private String getMediaDetails(MediaContent content) {
        if (content instanceof AudioImpl) {
            AudioImpl audio = (AudioImpl) content;
            return String.format("Audio File [Address: %s, Size: %d bytes, Sampling Rate: %d, Access Count: %d, Uploader: %s, Availability: %s, Cost: %s, Tags: %s]",
                    audio.getAddress(), audio.getSize(), audio.getSamplingRate(), audio.getAccessCount(),
                    audio.getUploader().getName(), audio.getAvailability(), audio.getCost(),
                    audio.getTags());
        } else if (content instanceof VideoImpl) {
            VideoImpl video = (VideoImpl) content;
            return String.format("Video File [Address: %s, Size: %d bytes, Resolution: %d, Access Count: %d, Uploader: %s, Availability: %s, Cost: %s, Tags: %s]",
                    video.getAddress(), video.getSize(), video.getResolution(), video.getAccessCount(),
                    video.getUploader().getName(), video.getAvailability(), video.getCost(),
                    video.getTags());
        } else {
            return "Unknown media type";
        }
    }

}
