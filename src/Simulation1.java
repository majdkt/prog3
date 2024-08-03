import contract.MediaContent;
import contract.Tag;
import domainLogic.AudioImpl;
import domainLogic.AudioVideoImpl;
import domainLogic.Manager;
import domainLogic.VideoImpl;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Simulation1 {

    private static final String[] MEDIA_TYPES = {"Audio", "Video", "AudioVideo"};
    private static final Random RANDOM = new Random();
    private static final String[] UPLOADERS = {"Uploader1", "Uploader2", "Uploader3"};

    public static void main(String[] args) {
        int t = Integer.parseInt(args[0]);
        Manager manager = new Manager(t);

        Thread uploaderThread = new Thread(() -> {
            while (true) {
                try {
                    String uploaderName = UPLOADERS[RANDOM.nextInt(UPLOADERS.length)];
                    String mediaType = MEDIA_TYPES[RANDOM.nextInt(MEDIA_TYPES.length)];
                    Set<Tag> tags = new HashSet<>(); // Leere Menge von Tags
                    long size = RANDOM.nextInt(100) + 1; // Zufällige Größe zwischen 1 und 100
                    BigDecimal cost = BigDecimal.valueOf(RANDOM.nextDouble() * 10);
                    int samplingRate = RANDOM.nextInt(44100); // Zufällige Sampling Rate
                    int resolution = RANDOM.nextInt(1080); // Zufällige Auflösung
                    Duration availability = Duration.ofDays(RANDOM.nextInt(30) + 1); // Verfügbarkeit zwischen 1 und 30 Tagen

                    try {
                        manager.create(uploaderName, mediaType, tags, size, cost, samplingRate, resolution, availability);

                        // Get the details of all media and log the most recently added one
                        synchronized (manager) {
                            if (!manager.contentMap.isEmpty()) {
                                Map.Entry<String, MediaContent> entry = manager.contentMap.entrySet().iterator().next();
                                MediaContent mediaContent = entry.getValue();
                                String mediaDetails = getMediaDetails(mediaContent);
                                System.out.println(Thread.currentThread().getName() + " Added Media: " + mediaDetails);
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(Thread.currentThread().getName() + " Failed to Add Media: " + e.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread cleanerThread = new Thread(() -> {
            while (true) {
                try {
                    String mediaAddress = null;

                    synchronized (manager) {
                        if (!manager.contentMap.isEmpty()) {
                            mediaAddress = manager.contentMap.keySet().iterator().next();
                        }
                    }

                    if (mediaAddress != null) {
                        synchronized (manager) {
                            MediaContent mediaContent = manager.contentMap.get(mediaAddress);
                            if (mediaContent != null) {
                                String mediaDetails = getMediaDetails(mediaContent);
                                manager.deleteMedia(mediaAddress);
                                System.out.println(Thread.currentThread().getName() + " Deleted Media: " + mediaDetails);
                            }
                        }
                    } else {
                        System.out.println(Thread.currentThread().getName() + " No Media to Delete");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        uploaderThread.start();
        cleanerThread.start();
    }

    private static String getMediaDetails(MediaContent content) {
        if (content instanceof AudioImpl) {
            AudioImpl audio = (AudioImpl) content;
            return String.format("Audio File [Address: %s, Size: %.2f MB, Sampling Rate: %d, Access Count: %d, Uploader: %s, Availability: %d Days, Cost: %.2f, Tags: %s]",
                    audio.getAddress(), audio.getSize() / 1.0, audio.getSamplingRate(), audio.getAccessCount(),
                    audio.getUploader().getName(), audio.getAvailability().toDays(), audio.getCost(),
                    audio.getTags());
        } else if (content instanceof VideoImpl) {
            VideoImpl video = (VideoImpl) content;
            return String.format("Video File [Address: %s, Size: %.2f MB, Resolution: %d, Access Count: %d, Uploader: %s, Availability: %d Days, Cost: %.2f, Tags: %s]",
                    video.getAddress(), video.getSize() / 1.0, video.getResolution(), video.getAccessCount(),
                    video.getUploader().getName(), video.getAvailability().toDays(), video.getCost(),
                    video.getTags());
        } else if (content instanceof AudioVideoImpl) {
            AudioVideoImpl audioVideo = (AudioVideoImpl) content;
            return String.format("AudioVideo File [Address: %s, Size: %.2f MB, Sampling Rate: %d, Resolution: %d, Access Count: %d, Uploader: %s, Availability: %d Days, Cost: %.2f, Tags: %s]",
                    audioVideo.getAddress(), audioVideo.getSize() / 1.0, audioVideo.getSamplingRate(), audioVideo.getResolution(),
                    audioVideo.getAccessCount(), audioVideo.getUploader().getName(), audioVideo.getAvailability().toDays(), audioVideo.getCost(),
                    audioVideo.getTags());
        }
        return "";
    }
}
