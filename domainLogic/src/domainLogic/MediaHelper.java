package domainLogic;

import contract.MediaContent;
import contract.Tag;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;

public class MediaHelper {
    private Random random = new Random();

    public long getRandomSize() {
        long minSize = 1_000_000L; // 1 MB in bytes
        long maxSize = 50_000_000L; // 50 MB in bytes
        return minSize + (long) (random.nextDouble() * (maxSize - minSize));
    }

    public int getRandomSamplingRate() {
        return 44_100; // Example fixed sampling rate for simplicity
    }

    public int getRandomResolution() {
        return 1080; // Example fixed resolution for simplicity
    }

    public Duration getRandomAvailability() {
        return Duration.ofDays(random.nextInt(365)); // Random duration up to 1 year
    }

    public BigDecimal getRandomCost() {
        return BigDecimal.valueOf(random.nextDouble() * 10).setScale(2, BigDecimal.ROUND_HALF_UP); // Random cost between 0 and 10, rounded to 2 decimal places
    }

    public MediaContent createMediaContent(String mediaType, String address, Set<Tag> tags, long size, UploaderImpl uploader, LocalDateTime uploadDate) {
        switch (mediaType.toLowerCase()) {
            case "audio":
                return new AudioImpl(
                        getRandomSamplingRate(),
                        address,
                        tags,
                        0,
                        size,
                        uploader,
                        getRandomAvailability(),
                        getRandomCost(),
                        uploadDate
                );
            case "video":
                return new VideoImpl(
                        address,
                        tags,
                        0,
                        size,
                        uploader,
                        getRandomAvailability(),
                        getRandomCost(),
                        getRandomResolution(),
                        uploadDate
                );
            case "audiovideo":
                return new AudioVideoImpl(
                        getRandomSamplingRate(),
                        address,
                        tags,
                        0,
                        size,
                        uploader,
                        getRandomAvailability(),
                        getRandomCost(),
                        getRandomResolution(),
                        uploadDate
                );
            default:
                throw new IllegalArgumentException("Invalid media type.");
        }
    }

    public String getMediaDetails(MediaContent content) {
        if (content instanceof AudioImpl) {
            AudioImpl audio = (AudioImpl) content;
            return String.format("Audio File [Address: %s, Size: %.2f MB, Sampling Rate: %d, Access Count: %d, Uploader: %s, Availability: %d, Cost: %.2f, Tags: %s, Upload Date: %s]",
                    audio.getAddress(), audio.getSize() / 1_000_000.0, audio.getSamplingRate(), audio.getAccessCount(),
                    audio.getUploader().getName(), audio.getAvailability().toDays(), audio.getCost(),
                    audio.getTags(), audio.getUploadDate());
        } else if (content instanceof VideoImpl) {
            VideoImpl video = (VideoImpl) content;
            return String.format("Video File [Address: %s, Size: %.2f MB, Resolution: %d, Access Count: %d, Uploader: %s, Availability: %d, Cost: %.2f, Tags: %s, Upload Date: %s]",
                    video.getAddress(), video.getSize() / 1_000_000.0, video.getResolution(), video.getAccessCount(),
                    video.getUploader().getName(), video.getAvailability().toDays(), video.getCost(),
                    video.getTags(), video.getUploadDate());
        } else if (content instanceof AudioVideoImpl) {
            AudioVideoImpl audioVideo = (AudioVideoImpl) content;
            return String.format("AudioVideo File [Address: %s, Size: %.2f MB, Sampling Rate: %d, Resolution: %d, Access Count: %d, Uploader: %s, Availability: %d, Cost: %.2f, Tags: %s, Upload Date: %s]",
                    audioVideo.getAddress(), audioVideo.getSize() / 1_000_000.0, audioVideo.getSamplingRate(), audioVideo.getResolution(),
                    audioVideo.getAccessCount(), audioVideo.getUploader().getName(), audioVideo.getAvailability().toDays(), audioVideo.getCost(),
                    audioVideo.getTags(), audioVideo.getUploadDate());
        } else {
            return "Unknown media type";
        }
    }
}
