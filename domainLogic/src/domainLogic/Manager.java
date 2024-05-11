package domainLogic;

import contract.MediaContent;
import contract.Uploadable;
import contract.Uploader;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

public class Manager {
    public Map<MediaContent, Uploadable> getTaskAssignments() {
        return taskAssignments;
    }

    private final List<MediaContent> mediaList = new ArrayList<>();
    private final Map<MediaContent, Uploadable> taskAssignments = new HashMap<>();

    public String getCurrentUser() {
        return currentUser;
    }

    private String currentUser;

    public Manager(String currentUser) {
        this.currentUser = currentUser;
    }


     public void create(String currentUser, String mediaType) {
        if (!currentUser.equals(this.currentUser)) {
            System.err.println("Error: Unauthorized operation.");
        }
        Uploader uploader = new UploaderImpl(currentUser);
        Random random = new Random();
        Duration availability = Duration.ofHours(random.nextInt(24) + 1);
        BigDecimal cost = BigDecimal.valueOf(random.nextDouble() * 100);
        Uploadable uploadable = new UploadableImpl(uploader, availability, cost);

        String address = "NULL";
        long size = 0;
        MediaContent mediaContent = null;

        switch (mediaType) {
            case "audio":
                int samplingRate = random.nextInt(10) + 1;
                mediaContent = new AudioImpl(samplingRate, address, size, uploader);
                System.out.println("Audio file added.");
                break;
            case "video":
                int resolution = random.nextInt(10) + 1;
                mediaContent = new VideoImpl(resolution, address, size, uploader);
                System.out.println("Video file added.");
                break;
            default:
                System.out.println("Invalid media type.");
        }

        if (mediaContent != null){
            mediaList.add(mediaContent);
            taskAssignments.put(mediaContent, uploadable);
        }

    }

    public List<MediaContent> read() {
        return new ArrayList<>(this.mediaList);
    }

    public void update(MediaContent content, String attribute, Object value) {
        if (content == null) {
            System.err.println("Error: Media content is null.");
            return;
        }

        switch (attribute) {
            case "sampling rate":
                if (content instanceof AudioImpl) {
                    ((AudioImpl) content).updateSamplingRate((int) value);
                } else {
                    System.err.println("Error: Sampling rate is not applicable for this content type.");
                }
                break;
            case "resolution":
                if (content instanceof VideoImpl) {
                    ((VideoImpl) content).updateResolution((int) value);
                } else {
                    System.err.println("Error: Resolution is not applicable for this content type.");
                }
                break;
            default:
                System.err.println("Error: Invalid attribute.");
        }
    }

    public boolean delete(MediaContent content) {
        return mediaList.remove(content);
    }

 }

