package com.lutzenberger.sascha.abload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.xml.sax.Attributes;

import com.lutzenberger.sascha.abload.data.Gallery;
import com.lutzenberger.sascha.abload.data.ImageUploaded;
import com.lutzenberger.sascha.abload.data.LoginResponse;
import com.lutzenberger.sascha.html.ImageSrc;
import com.lutzenberger.sascha.sax.RootElement;
import com.lutzenberger.sascha.sax.StartElementListener;
import com.lutzenberger.sascha.sax.Xml;

/**
 * This class is responsible for handling the data upload.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 08.09.2015
 */
public class UploadManager {
    //The gallery the images have to be uploaded into
    private Gallery gallery;
    //The current session
    private LoginResponse loginResponse;

    private List<ImageSrc> uploadedImageList = new ArrayList<>();

    public UploadManager(Gallery gallery, LoginResponse loginResponse) {
        this.gallery = gallery;
        this.loginResponse = loginResponse;
    }

    public boolean uploadFile(File file) {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("http://www.abload.de/api/upload");

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.addPart("session", new StringBody(loginResponse.getSession(), ContentType.DEFAULT_TEXT));
            entityBuilder.addPart("img0", new FileBody(file));
            entityBuilder.addPart("gallery", new StringBody(""+gallery.getId(), ContentType.DEFAULT_TEXT));

            post.setEntity(entityBuilder.build());

            HttpResponse response = client.execute(post);

            RootElement root = new RootElement("abload");
            root.getChild("images").getChild("image").setStartElementListener(new ImageListener());

            Xml.parse(response.getEntity().getContent(), Xml.Encoding.UTF_8, root.getContentHandler());

            if(!(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public List<ImageSrc> getUploadedImageList() {
        return this.uploadedImageList;
    }

    private class ImageListener implements StartElementListener {
        @Override
        public void start(Attributes attributes) {
            String newName = attributes.getValue("newname");
            String oldName = attributes.getValue("oldname");
            String res = attributes.getValue("res");
            String type = attributes.getValue("type");

            ImageUploaded imageUploaded = new ImageUploaded(newName, oldName, res, type);
            ImageSrc imageSrc = new ImageSrc(imageUploaded.getFileName());
            uploadedImageList.add(imageSrc);
        }
    }
}
