package system.Objects.Utils.NfswUtils.Nekos;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

public class NekosUtil {

    private String tagName;

    public NekosUtil() {

    }

    public String getDomen() {
        return "nekos.life";
    }

    public String getUrl() {
        return "https://nekos.life/api/v2/img/";
    }

    public String getImage(String tag) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://nekos.life/api/v2/img/" + NekosTags.valueOf(tag.toUpperCase()).getTag())
                .get()
                .addHeader("Content-Type", "application/vnd.api+json")
                .addHeader("Accept", "application/vnd.api+json")
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            assert response.body() != null;
            JSONObject result = new JSONObject(response.body().string());

            return result.getString("url");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRandomImage() {
        String tag = NekosTags.values()[(int) (Math.random()*NekosTags.values().length)].getTag();
        this.tagName = tag;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://nekos.life/api/v2/img/" + tag)
                .get()
                .addHeader("Content-Type", "application/vnd.api+json")
                .addHeader("Accept", "application/vnd.api+json")
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            assert response.body() != null;
            JSONObject result = new JSONObject(response.body().string());

            return result.getString("url");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTagName() {
        return tagName;
    }
}
