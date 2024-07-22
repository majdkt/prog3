package domainLogic;

import contract.MediaContent;
import contract.Tag;
import events.*;
import events.EventListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Manager implements Serializable {
    private static final long MAX_TOTAL_CAPACITY = 500_000_000L; // 500 MB in bytes
    private Map<String, MediaContent> contentMap = new HashMap<>();
    private long currentTotalSize = 0;
    private int addressCounter = 1;

    private Map<String, UploaderImpl> uploaderMap = new HashMap<>();
    private List<EventListener> eventListeners = new ArrayList<>();
    private MediaHelper mediaHelper = new MediaHelper();

    public synchronized void addEventListener(EventListener listener) {
        eventListeners.add(listener);
    }

    private void notifyListeners(Event event) {
        for (EventListener listener : eventListeners) {
            listener.onEvent(event);
        }
    }

    public synchronized void addUploader(String uploaderName) {
        if (uploaderMap.containsKey(uploaderName)) {
            throw new IllegalArgumentException("Uploader with this name already exists.");
        }
        uploaderMap.put(uploaderName, new UploaderImpl(uploaderName));
    }

    public synchronized void uploadMedia(String uploaderName, String mediaType, Set<Tag> tags, long size) {
        if (currentTotalSize + size > MAX_TOTAL_CAPACITY) {
            throw new IllegalArgumentException("Max capacity exceeded. Cannot upload media.");
        }

        UploaderImpl uploader = uploaderMap.get(uploaderName);
        if (uploader == null) {
            throw new IllegalArgumentException("Uploader does not exist.");
        }

        String address = getNextAddress();
        MediaContent mediaContent = mediaHelper.createMediaContent(mediaType, address, tags, size, uploader, LocalDateTime.now());

        contentMap.put(address, mediaContent);
        currentTotalSize += size;
        uploader.addMedia(address);

        notifyListeners(new MediaUploadedEvent(uploaderName, mediaType, tags, size, mediaContent.getCost()));
        notifyListeners(new TagChangedEvent(getAllTags()));
    }

    private String getNextAddress() {
        return String.valueOf(addressCounter++);
    }

    public synchronized List<String> getAllProducersWithMediaCount() {
        List<String> producerDetails = new ArrayList<>();
        for (UploaderImpl uploader : uploaderMap.values()) {
            producerDetails.add(String.format("Producer: %s, Media Count: %d", uploader.getName(), uploader.getMediaList().size()));
        }
        return producerDetails;
    }

    public synchronized List<String> getMediaFiles(String mediaType) {
        List<String> mediaDetails = new ArrayList<>();
        for (MediaContent content : contentMap.values()) {
            if (mediaType == null || content.getClass().getSimpleName().equalsIgnoreCase(mediaType)) {
                mediaDetails.add(mediaHelper.getMediaDetails(content));
            }
        }
        return mediaDetails;
    }

    public synchronized Set<Tag> getAllTags() {
        Set<Tag> tags = new HashSet<>();
        for (MediaContent content : contentMap.values()) {
            tags.addAll(content.getTags());
        }
        return tags;
    }

    public synchronized void incrementAccessCount(String address) {
        MediaContent media = contentMap.get(address);
        if (media != null) {
            media.setAccessCount(media.getAccessCount() + 1);
            notifyListeners(new AccessCountUpdatedEvent(address));
        }
    }

    public synchronized void deleteProducer(String uploaderName) {
        UploaderImpl uploader = uploaderMap.remove(uploaderName);
        if (uploader != null) {
            for (String address : uploader.getMediaList()) {
                MediaContent removedMedia = contentMap.remove(address);
                if (removedMedia != null) {
                    currentTotalSize -= removedMedia.getSize();
                }
            }
            notifyListeners(new UploaderDeletedEvent(uploaderName));
        }
    }

    public synchronized void deleteMedia(String address) {
        MediaContent removedMedia = contentMap.remove(address);
        if (removedMedia != null) {
            currentTotalSize -= removedMedia.getSize();
            removedMedia.getUploader().removeMedia(address);
            notifyListeners(new MediaDeletedEvent(address));
        }
    }

    public synchronized void logout() {
        contentMap.clear();
        currentTotalSize = 0;
        addressCounter = 1;
        uploaderMap.clear();
    }

    // Getter methods for testing purposes
    public Map<String, MediaContent> getContentMap() {
        return contentMap;
    }

    public long getCurrentTotalSize() {
        return currentTotalSize;
    }

    public int getAddressCounter() {
        return addressCounter;
    }
}
