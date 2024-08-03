package domainLogic;

import contract.*;
import contract.Observer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

public class Manager implements Serializable {
    private static final long serialVersionUID = 1L;
    public final long MAX_TOTAL_CAPACITY;
    private final List<Observer> observers = new ArrayList<>();
    public Map<String, MediaContent> contentMap = new HashMap<>();
    private Set<String> uploaderSet = new HashSet<>();
    Queue<String> availableAddresses = new LinkedList<>();
    long currentTotalSize = 0;
    int addressCounter = 1;

    public Manager(long maxTotalCapacity) {
        this.MAX_TOTAL_CAPACITY = maxTotalCapacity;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    // Uploader management methods
    public synchronized void createUploader(String uploaderName) {
        uploaderSet.add(uploaderName);
        notifyObservers("Uploader created: " + uploaderName);
    }

    public synchronized void deleteUploader(String uploaderName) {
        if (!uploaderSet.contains(uploaderName)) {
            notifyObservers("Failed to delete uploader: Uploader not found.");
            throw new IllegalArgumentException("Uploader not found.");
        }

        // Calculate the total size of media uploaded by the uploader
        long sizeToDelete = contentMap.values().stream()
                .filter(content -> content.getUploader().getName().equals(uploaderName))
                .mapToLong(MediaContent::getSize)
                .sum();
        currentTotalSize -= sizeToDelete;
        uploaderSet.remove(uploaderName);
        contentMap.values().removeIf(content -> content.getUploader().getName().equals(uploaderName));
        notifyObservers("Uploader deleted: " + uploaderName);
    }

    public boolean uploaderExists(String uploaderName) {
        return uploaderSet.contains(uploaderName);
    }

    public long getCurrentTotalSize() {
        return currentTotalSize;
    }

    // Media management methods
    public synchronized void create(String uploaderName, String mediaType, Set<Tag> tags, long size, BigDecimal cost, int samplingRate, int resolution, Duration availability) {
        if (!uploaderExists(uploaderName)) {
            createUploader(uploaderName);
        }
        if (currentTotalSize + size > MAX_TOTAL_CAPACITY) {
            notifyObservers("Failed to add media: Max capacity exceeded.");
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
            notifyObservers("Failed to add media: Invalid media type.");
            throw new IllegalArgumentException("Invalid media type.");
        }

        contentMap.put(address, mediaContent);
        currentTotalSize += size;
        notifyObservers("Media added: " + getMediaDetails(mediaContent));
    }

    String getNextAddress() {
        if (availableAddresses.isEmpty()) {
            return String.valueOf(addressCounter++);
        } else {
            return availableAddresses.poll();
        }
    }

    public synchronized String read() {
        StringBuilder mediaDetails = new StringBuilder();
        for (Map.Entry<String, MediaContent> entry : contentMap.entrySet()) {
            mediaDetails.append(getMediaDetails(entry.getValue())).append(System.lineSeparator());
        }
        return mediaDetails.toString();
    }

    public synchronized Map<Tag, Boolean> readByTag() {
        Map<Tag, Boolean> tagStatus = new HashMap<>();

        for (MediaContent content : contentMap.values()) {
            for (Tag tag : content.getTags()) {
                tagStatus.put(tag, true);
            }
        }

        for (Tag tag : Tag.values()) {
            if (!tagStatus.containsKey(tag)) {
                tagStatus.put(tag, false);
            }
        }

        return tagStatus;
    }

    public synchronized Map<String, Integer> readByUploader() {
        Map<String, Integer> uploaderMediaCount = new HashMap<>();

        for (String uploaderName : uploaderSet) {
            uploaderMediaCount.put(uploaderName, 0);
        }

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


    String getMediaDetails(MediaContent content) {
        if (content instanceof AudioImpl) {
            Audio audio = (AudioImpl) content;
            return String.format("Audio File [Address: %s, Size: %.2f MB, Sampling Rate: %d, Access Count: %d, Uploader: %s, Availability: %d Days, Cost: %.2f, Tags: %s]",
                    audio.getAddress(), audio.getSize() / 1.0, audio.getSamplingRate(), audio.getAccessCount(),
                    audio.getUploader().getName(), audio.getAvailability().toDays(), audio.getCost(),
                    audio.getTags());
        } else if (content instanceof VideoImpl) {
            Video video = (VideoImpl) content;
            return String.format("Video File [Address: %s, Size: %.2f MB, Resolution: %d, Access Count: %d, Uploader: %s, Availability: %d Days, Cost: %.2f, Tags: %s]",
                    video.getAddress(), video.getSize()/ 1.0, video.getResolution(), video.getAccessCount(),
                    video.getUploader().getName(), video.getAvailability().toDays() , video.getCost(),
                    video.getTags());
        } else if (content instanceof AudioVideoImpl) {
            AudioVideo audioVideo = (AudioVideoImpl) content;
            return String.format("AudioVideo File [Address: %s, Size: %.2f MB, Sampling Rate: %d, Resolution: %d, Access Count: %d, Uploader: %s, Availability: %d Days, Cost: %.2f, Tags: %s]",
                    audioVideo.getAddress(), audioVideo.getSize()/ 1.0, audioVideo.getSamplingRate(), audioVideo.getResolution(),
                    audioVideo.getAccessCount(), audioVideo.getUploader().getName(), audioVideo.getAvailability().toDays(), audioVideo.getCost(),
                    audioVideo.getTags());
        }
        return "";
    }

    public synchronized void updateAccessCount(String address) {
        MediaContent media = contentMap.get(address);
        if (media != null) {
            media.setAccessCount(media.getAccessCount() + 1);
            notifyObservers("Access count updated: " + getMediaDetails(media));
        }
    }

    public synchronized void deleteMedia(String address) {
        MediaContent removedMedia = contentMap.remove(address);
        if (removedMedia != null) {
            currentTotalSize -= removedMedia.getSize();
            availableAddresses.add(address);
            notifyObservers("Media deleted: " + getMediaDetails(removedMedia));
        } else {
            notifyObservers("Failed to delete media: Media not found.");
        }
    }
}
