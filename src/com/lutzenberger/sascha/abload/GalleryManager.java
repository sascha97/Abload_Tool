package com.lutzenberger.sascha.abload;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.xml.sax.Attributes;

import com.lutzenberger.sascha.abload.data.Gallery;
import com.lutzenberger.sascha.abload.data.LoginResponse;
import com.lutzenberger.sascha.sax.RootElement;
import com.lutzenberger.sascha.sax.StartElementListener;
import com.lutzenberger.sascha.sax.Xml;

/**
 * This class is responsible for selecting the gallery to upload the pictures to.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 08.09.2015
 */
public class GalleryManager {
    private LoginResponse loginResponse;

    private List<Gallery> galleryList;

    public GalleryManager(LoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }

    /**
     * This method returns a gallery list of all the available galleries.
     *
     * @return A gallery list with all the available galleries.
     */
    public List<Gallery> getGalleries() {
        galleryList = null;

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet("http://www.abload.de/api/gallerylist?session=" + loginResponse.getSession());
            HttpResponse response = client.execute(request);

            galleryList = new ArrayList<>();
            RootElement root = new RootElement("abload");
            root.getChild("galleries").getChild("gallery").setStartElementListener(new GalleryListener());
            Xml.parse(response.getEntity().getContent(), Xml.Encoding.UTF_8, root.getContentHandler());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return galleryList;
    }

    public boolean createNewGallery(String galleryName) {
        boolean result = false;

        try {
            BasicCookieStore cookieStore = new BasicCookieStore();
            cookieStore.addCookie(loginResponse.getSessionCookie());

            HttpClient client = HttpClientBuilder.create().setUserAgent("Abloadlib/0.1")
                    .setDefaultCookieStore(cookieStore).build();

            HttpGet request = new HttpGet("http://www.abload.de/calls/createGallery.php?name="
                    + URLEncoder.encode(galleryName, String.valueOf(StandardCharsets.UTF_8)) + "&desc=");

            HttpResponse response = client.execute(request);

            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent());
                BufferedReader reader = new BufferedReader(inputStreamReader);

                String createdGalleryStatus = reader.readLine();

                try {
                    long galleryId = Long.parseLong(createdGalleryStatus);
                    result = true;
                } catch (NumberFormatException e) {
                    result = false;
                }

            } else {
                result = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private class GalleryListener implements StartElementListener {
        @Override
        public void start(Attributes attributes) {
            String name = attributes.getValue("name");
            long id = Long.parseLong(attributes.getValue("id"));

            Gallery gallery = new Gallery(name, id);
            galleryList.add(gallery);
        }
    }
}
