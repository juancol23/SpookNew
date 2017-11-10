package relato.app.dems.com.relato.beta;

/**
 * Created by CORAIMA on 01/11/2017.
 */

public class ItemFeed {


    private String title;
    private String desc;
    private String image;

    public ItemFeed(){

    }

    public ItemFeed(String title, String desc, String image) {
        this.title = title;
        this.desc = desc;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
