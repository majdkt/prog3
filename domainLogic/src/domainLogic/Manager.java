package domainLogic;

import contract.MediaContent;
import contract.Uploadable;
import contract.Uploader;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

public class Manager {
    private List<MediaContent> mediaList = new ArrayList<>();
    private Map<MediaContent, Uploadable> taskAssignments = new HashMap<>();

    public void create(MediaContent content, Uploadable uploadable) {
        mediaList.add(content);
        taskAssignments.put(content, uploadable);
    }

    public Uploadable createSampleUploadable(String uploaderName) {
        Uploader uploader = new UploaderImpl(uploaderName);
        Random random = new Random();
        Duration availability = Duration.ofHours(random.nextInt(24) + 1);
        BigDecimal cost = BigDecimal.valueOf(random.nextDouble() * 100);
        return new UploadableImpl(uploader, availability, cost);
    }

    public MediaContent createSampleMediaContent(String uploaderName, String mediaType) {
        Uploader uploader = createSampleUploadable(uploaderName).getUploader();
        String address = "NULL";
        long size = 0;
        MediaContent mediaContent = null;

        switch (mediaType) {
            case "audio":
                Random random = new Random();
                int samplingRate = random.nextInt(10) + 1;
                mediaContent = new AudioImpl(samplingRate, address, size,uploader );
                break;
            case "video":
                Random random1 = new Random();
                int resolution = random1.nextInt(10) +1;
                mediaContent = new VideoImpl(resolution, address, size, uploader);
                break;
            default:
                System.out.println("Invalid media type.");
        }
        return mediaContent;
    }

    public List<MediaContent> read() {
        return new ArrayList<>(this.mediaList);
    }

    MediaContent update(int i) {
        MediaContent content = mediaList.get(i);
        long ac = content.getAccessCount();
        return content;
    }

    boolean delete(MediaContent content) {
        return mediaList.remove(content);
    }

    public Map<MediaContent, Uploadable> getTaskAssignments() {
        return taskAssignments;
    }

     void updateSamplingRate(AudioImpl audioContent, int newSamplingRate) {
         if (audioContent != null) {
             audioContent.updateSamplingRate(newSamplingRate);
         } else {
             System.err.println("Error: audio content is null.");
         }
     }

 }

