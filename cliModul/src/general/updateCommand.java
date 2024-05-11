package general;

import contract.MediaContent;
import domainLogic.AudioImpl;
import domainLogic.Manager;
import domainLogic.VideoImpl;

import java.util.List;
import java.util.Scanner;

public class updateCommand {
    public MediaContent mediaContent;
    List<MediaContent> userMediaList;
    public int index;
    public int type;
    public String strType;

    public String username;

    public updateCommand(String username,List<MediaContent> userMediaList, int index, int type) {
        this.index = index;
        this.userMediaList = userMediaList;
        this.type = type;
        this.username = username;
    }
    MediaContent toUpdate = userMediaList.get(index-1);
    public MediaContent run(){
        if (type == 1){
            System.out.println("For now you only can edit the sampling rate.");
            System.out.println("Please add new sampling rate: ");
            Scanner scanner = new Scanner(System.in);
            int newSamp = scanner.nextInt();
            mediaContent = new AudioImpl(newSamp,toUpdate.getAddress(),toUpdate.getSize(),username);
            strType = "audio";
        }
        if (type == 2){
            System.out.println("For now you only can edit the resolution.");
            System.out.println("Please add new resolution: ");
            Scanner scanner = new Scanner(System.in);
            int newRes = scanner.nextInt();
            mediaContent = new VideoImpl(newRes,toUpdate.getAddress(),toUpdate.getSize(),username);
            strType = "video";
        }
        else{
            System.out.println("invalid index.");
        }
        return mediaContent;
    }
    public String getStrType() {
        return strType;
    }
}
