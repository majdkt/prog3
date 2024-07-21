package domainLogic;

import contract.MediaContent;
import contract.Tag;
import contract.Uploader;

import java.io.Serializable;
import java.util.*;

public class Manager implements Serializable {
    private Map<String, MediaContent> contentMap = new HashMap<>();
    private int addressCounter = 1;
    private Queue<String> availableAddresses = new LinkedList<>();

    public synchronized void create(MediaContent content) {
        String address;
        if (content.toString() == "Audio"){
            if (availableAddresses.isEmpty()) {
                address = "address_" + addressCounter;
                addressCounter++;
            } else {
                address = availableAddresses.poll();
            }
            contentMap.put(address, content);
        }
        else System.out.println("HAHAAHA");
        System.out.println("Created content of type: " + getContentType(content));
    }

    public synchronized List<String> read() {
        List<String> contentDetails = new ArrayList<>();
        for (Map.Entry<String, MediaContent> entry : contentMap.entrySet()) {
            contentDetails.add(entry.getValue().toString());
        }
        return contentDetails;
    }

    public synchronized void update(String address, long newAccessCount) {
        MediaContent content = contentMap.get(address);
        if (content != null) {
            content.setAccessCount(newAccessCount);
        }
    }

    public synchronized void delete(String address) {
        MediaContent removedContent = contentMap.remove(address);
        if (removedContent != null) {
            availableAddresses.add(address);
        }
    }

    public synchronized List<MediaContent> getContentList() {
        return new ArrayList<>(contentMap.values());
    }

    public synchronized void logout() {
        contentMap.clear();
        addressCounter = 0;
        availableAddresses.clear();
    }

    private String getContentType(MediaContent content) {
        if (content instanceof contract.Audio) {
            return "Audio";
        } else if (content instanceof contract.Video) {
            return "Video";
        } else {
            return "Unknown";
        }
    }
}
