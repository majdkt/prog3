package domainLogic;

import contract.MediaContent;
import contract.Tag;
import events.*;
import events.EventListener;

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

    // New maps for producer management
    private Map<String, UploaderImpl> uploaderMap = new HashMap<>();
    private Map<UploaderImpl, List<String>> uploaderMediaMap = new HashMap<>();
    private List<EventListener> eventListeners = new ArrayList<>();

    public synchronized void addEventListener(EventListener listener) {
        eventListeners.add(listener);
    }

    private void notifyListeners(Event event) {
        for (EventListener listener : eventListeners) {
            listener.onEvent(event);
        }
    }

    public synchronized void create(String uploaderName, String mediaType, Set<Tag> tags) {
        long size = getRandomSize();

        if (currentTotalSize + size > MAX_TOTAL_CAPACITY) {
            throw new IllegalArgumentException("Max capacity exceeded. Cannot upload media.");
        }

        UploaderImpl uploader = uploaderMap.get(uploaderName);
        if (uploader == null) {
            throw new IllegalArgumentException("Uploader does not exist.");
        }

        String address = getNextAddress();
        MediaContent mediaContent;

        if (mediaType.equalsIgnoreCase("Audio")) {
            mediaContent = new AudioImpl(
                    getRandomSamplingRate(),
                    address,
                    tags,
                    0,
                    size,
                    uploader,
                    getRandomAvailability(),
                    getRandomCost()
            );
        } else if (mediaType.equalsIgnoreCase("Video")) {
            mediaContent = new VideoImpl(
                    address,
                    tags,
                    0,
                    size,
                    uploader,
                    getRandomAvailability(),
                    getRandomCost(),
                    getRandomResolution()
            );
        } else if (mediaType.equalsIgnoreCase("AudioVideo")) {
            mediaContent = new AudioVideoImpl(
                    getRandomSamplingRate(),
                    address,
                    tags,
                    0,
                    size,
                    uploader,
                    getRandomAvailability(),
                    getRandomCost(),
                    getRandomResolution()
            );
        } else {
            throw new IllegalArgumentException("Invalid media type.");
        }

        contentMap.put(address, mediaContent);
        currentTotalSize += size;

        notifyListeners(new MediaUploadedEvent(uploaderName, mediaType, tags, size, getRandomCost()));
        notifyListeners(new TagChangedEvent(getAllTags()));
    }

    private String getNextAddress() {
        return String.valueOf(addressCounter++);
    }

    private long getRandomSize() {
        long minSize = 1_000_000L; // 1 MB in bytes
        long maxSize = 50_000_000L; // 50 MB in bytes
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
        return BigDecimal.valueOf(random.nextDouble() * 10).setScale(2, BigDecimal.ROUND_HALF_UP); // Random cost between 0 and 10, rounded to 2 decimal places
    }

    public synchronized List<String> read() {
        List<String> mediaDetails = new ArrayList<>();
        for (Map.Entry<String, MediaContent> entry : contentMap.entrySet()) {
            mediaDetails.add(getMediaDetails(entry.getValue()));
        }
        return mediaDetails;
    }

    public synchronized List<String> read1(String mediaType) {
        List<String> mediaDetails = new ArrayList<>();
        for (MediaContent content : contentMap.values()) {
            if (mediaType == null || content.getClass().getSimpleName().equalsIgnoreCase(mediaType)) {
                mediaDetails.add(getMediaDetails(content));
            }
        }
        return mediaDetails;
    }

    private String getMediaDetails(MediaContent content) {
        if (content instanceof AudioImpl) {
            AudioImpl audio = (AudioImpl) content;
            return String.format("Audio File [Address: %s, Size: %.2f MB, Sampling Rate: %d, Access Count: %d, Uploader: %s, Availability: %d, Cost: %.2f, Tags: %s]",
                    audio.getAddress(), audio.getSize() / 1_000_000.0, audio.getSamplingRate(), audio.getAccessCount(),
                    audio.getUploader().getName(), audio.getAvailability().toDays(), audio.getCost(),
                    audio.getTags());
        } else if (content instanceof VideoImpl) {
            VideoImpl video = (VideoImpl) content;
            return String.format("Video File [Address: %s, Size: %.2f MB, Resolution: %d, Access Count: %d, Uploader: %s, Availability: %d, Cost: %.2f, Tags: %s]",
                    video.getAddress(), video.getSize() / 1_000_000.0, video.getResolution(), video.getAccessCount(),
                    video.getUploader().getName(), video.getAvailability().toDays(), video.getCost(),
                    video.getTags());
        } else if (content instanceof AudioVideoImpl) {
            AudioVideoImpl audioVideo = (AudioVideoImpl) content;
            return String.format("AudioVideo File [Address: %s, Size: %.2f MB, Sampling Rate: %d, Resolution: %d, Access Count: %d, Uploader: %s, Availability: %d, Cost: %.2f, Tags: %s]",
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
            media.setAccessCount(media.getAccessCount() + 1);
            notifyListeners(new AccessCountUpdatedEvent(address));
        }
    }

    public synchronized void deleteMedia(String address) {
        MediaContent removedMedia = contentMap.remove(address);
        if (removedMedia != null) {
            currentTotalSize -= removedMedia.getSize();
            availableAddresses.add(address);
            notifyListeners(new MediaDeletedEvent(address));
        }
    }

    public synchronized void deleteUploader(String uploaderName) {
        UploaderImpl uploader = uploaderMap.remove(uploaderName);
        if (uploader != null) {
            List<String> mediaList = uploaderMediaMap.remove(uploader);
            if (mediaList != null) {
                for (String address : mediaList) {
                    contentMap.remove(address);
                }
            }
            notifyListeners(new UploaderDeletedEvent(uploaderName));
        }
    }

    public synchronized void logout() {
        contentMap.clear();
        currentTotalSize = 0;
        addressCounter = 1;
        availableAddresses.clear();
        uploaderMap.clear();
        uploaderMediaMap.clear();
    }

    public synchronized void addUploader(String uploaderName) {
        if (uploaderMap.containsKey(uploaderName)) {
            throw new IllegalArgumentException("Uploader with this name already exists.");
        }
        uploaderMap.put(uploaderName, new UploaderImpl(uploaderName));
    }

    public synchronized Map<String, Integer> getUploaderMediaCount() {
        Map<String, Integer> uploaderMediaCount = new HashMap<>();
        for (Map.Entry<UploaderImpl, List<String>> entry : uploaderMediaMap.entrySet()) {
            uploaderMediaCount.put(entry.getKey().getName(), entry.getValue().size());
        }
        return uploaderMediaCount;
    }

    public synchronized Set<Tag> getAllTags() {
        Set<Tag> tags = new HashSet<>();
        for (MediaContent content : contentMap.values()) {
            tags.addAll(content.getTags());
        }
        return tags;
    }

    // Getter methods for testing purposes
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
