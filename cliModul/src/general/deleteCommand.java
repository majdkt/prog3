package general;

import contract.MediaContent;
import java.util.List;

class deleteCommand {
    private final List<MediaContent> userMediaList;
    int indx;

    public deleteCommand(List<MediaContent> userMediaList, int indx) {
        this.userMediaList = userMediaList;
        this.indx = indx;
        }

    public MediaContent getToDelete() {
        if (userMediaList.isEmpty()){return null;}
        else return userMediaList.get(indx-1);}
}
