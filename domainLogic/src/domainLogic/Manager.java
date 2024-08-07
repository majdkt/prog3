package domainLogic;

import contract.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

public class Manager implements Serializable {
    private final long MAX_TOTAL_CAPACITY; // Max capacity in bytes
    Map<String, MediaContent> contentMap = new HashMap<>();
    private Set<String> uploaderSet = new HashSet<>();
    Queue<String> availableAddresses = new LinkedList<>();
    long currentTotalSize = 0;
    int addressCounter = 1;

    public Manager(long maxTotalCapacity) {
        this.MAX_TOTAL_CAPACITY = maxTotalCapacity;
    }

    // Uploader management methods
    public synchronized void createUploader(String uploaderName) {
        if (uploaderSet.contains(uploaderName)) {
            throw new IllegalArgumentException("Uploader already exists.");
        }
        uploaderSet.add(uploaderName);
    }

    public synchronized void deleteUploader(String uploaderName) {
        if (!uploaderSet.contains(uploaderName)) {
            throw new IllegalArgumentException("Uploader not found.");
        }
        uploaderSet.remove(uploaderName);
        contentMap.values().removeIf(content -> content.getUploader().getName().equals(uploaderName));
    }

    public synchronized List<String> getAllUploaders() {
        return new ArrayList<>(uploaderSet);
    }

    // Media management methods
    public synchronized void create(String uploaderName, String mediaType, Set<Tag> tags, long size, BigDecimal cost, int samplingRate, int resolution, Duration availability) {
        if (!uploaderSet.contains(uploaderName)) {
            throw new IllegalArgumentException("Uploader does not exist.");
        }

        // Check if adding this media would exceed the total capacity
        if (currentTotalSize + size > MAX_TOTAL_CAPACITY) {
            throw new IllegalArgumentException("Max capacity exceeded. Cannot upload media.");
        }

        String address = getNextAddress();
        MediaContent mediaContent;

        if (mediaType.equalsIgnoreCase("Audio")) {
            mediaContent = new AudioImpl(
                    samplingRate,
                    address,
                    tags,
                    0, // initial access count
                    size,
                    new UploaderImpl(uploaderName), // Still using UploaderImpl for consistency in MediaContent
                    availability,
                    cost
            );
        } else if (mediaType.equalsIgnoreCase("Video")) {
            mediaContent = new VideoImpl(
                    address,
                    tags,
                    0, // initial access count
                    size,
                    new UploaderImpl(uploaderName),
                    availability,
                    cost,
                    resolution
            );
        } else if (mediaType.equalsIgnoreCase("AudioVideo")) {
            mediaContent = new AudioVideoImpl(
                    samplingRate,
                    address,
                    tags,
                    0, // initial access count
                    size,
                    new UploaderImpl(uploaderName),
                    availability,
                    cost,
                    resolution
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

    public synchronized Map<Tag, Boolean> readByTag() {
        Map<Tag, Boolean> tagStatus = new HashMap<>();

        for (MediaContent content : contentMap.values()) {
            for (Tag tag : content.getTags()) {
                tagStatus.put(tag, true);
            }
        }

        // Ensure all tags are accounted for in the response
        for (Tag tag : Tag.values()) {
            if (!tagStatus.containsKey(tag)) {
                tagStatus.put(tag, false);
            }
        }

        return tagStatus;
    }


    public synchronized Map<String, Integer> readByUploader() {
        Map<String, Integer> uploaderMediaCount = new HashMap<>();
        for (MediaContent content : contentMap.values()) {
            String uploaderName = content.getUploader().getName();
            uploaderMediaCount.put(uploaderName, uploaderMediaCount.getOrDefault(uploaderName, 0) + 1);
        }
        return uploaderMediaCount;
    }

    public synchronized List<String> readByMediaType(String mediaType) {
        List<String> mediaDetails = new ArrayList<>();
        for (MediaContent content : contentMap.values()) {
            if ((mediaType.equalsIgnoreCase("Audio") && content instanceof AudioImpl) ||
                    (mediaType.equalsIgnoreCase("Video") && content instanceof VideoImpl) ||
                    (mediaType.equalsIgnoreCase("AudioVideo") && content instanceof AudioVideoImpl)) {
                mediaDetails.add(getMediaDetails(content));
            }
        }
        return mediaDetails;
    }

    private String getMediaDetails(MediaContent content) {
        if (content instanceof AudioImpl) {
            Audio audio = (AudioImpl) content;
            return String.format("Audio File [Address: %s, Size: %.2f MB, Sampling Rate: %d, Access Count: %d, Uploader: %s, Availability: %d millis, Cost: %.2f, Tags: %s]",
                    audio.getAddress(), audio.getSize() / 1.0, audio.getSamplingRate(), audio.getAccessCount(),
                    audio.getUploader().getName(), audio.getAvailability().toMillis(), audio.getCost(),
                    audio.getTags());
        } else if (content instanceof VideoImpl) {
            Video video = (VideoImpl) content;
            return String.format("Video File [Address: %s, Size: %.2f MB, Resolution: %d, Access Count: %d, Uploader: %s, Availability: %d minutes, Cost: %.2f, Tags: %s]",
                    video.getAddress(), video.getSize()/ 1.0, video.getResolution(), video.getAccessCount(),
                    video.getUploader().getName(), video.getAvailability().toMinutes() , video.getCost(),
                    video.getTags());
        } else if (content instanceof AudioVideoImpl) {
            AudioVideo audioVideo = (AudioVideo) content;
            return String.format("AudioVideo File [Address: %s, Size: %.2f MB, Sampling Rate: %d, Resolution: %d, Access Count: %d, Uploader: %s, Availability: %d millis, Cost: %.2f, Tags: %s]",
                    audioVideo.getAddress(), audioVideo.getSize()/ 1.0, audioVideo.getSamplingRate(), audioVideo.getResolution(),
                    audioVideo.getAccessCount(), audioVideo.getUploader().getName(), audioVideo.getAvailability().toMillis(), audioVideo.getCost(),
                    audioVideo.getTags());
        } else {
            return "Unknown media type";
        }
    }

    public synchronized void updateAccessCount(String address) {
        MediaContent media = contentMap.get(address);
        if (media != null) {
            media.setAccessCount(media.getAccessCount() + 1);
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
        uploaderSet.clear();
        currentTotalSize = 0;
        addressCounter = 1;
        availableAddresses.clear();
    }
}
