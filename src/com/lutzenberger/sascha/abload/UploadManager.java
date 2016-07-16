/*
 * Copyright (c) 2016. Sascha Lutzenberger. All rights reserved.
 *
 * This file is part of the project "Abload_Tool"
 *
 * Redistribution and use in source and binary forms, without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * - The author of this source code has given you the permission to use this
 *   source code.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * - The code is not used in commercial projects, except you got the permission
 *   for using the code in any commerical projects from the author.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
 * @version 1.1 - 31.12.2015
 */
public class UploadManager {
    //The gallery the images have to be uploaded into
    private Gallery gallery;
    //The current session
    private LoginResponse loginResponse;

    //A list containing all the uploaded images
    private List<ImageSrc> uploadedImageList = new ArrayList<>();

    /**
     * Constructor to set up the UploadManager.
     *
     * @param gallery The Gallery to which the image should be uploaded to.
     * @param loginResponse The response of the Login task.
     */
    public UploadManager(Gallery gallery, LoginResponse loginResponse) {
        this.gallery = gallery;
        this.loginResponse = loginResponse;
    }

    /**
     * This method tries to upload a image file to http://www.abload.de
     *
     * @param file The image file which sould be uploaded.
     *
     * @return true if upload was successful false if not
     */
    public boolean uploadFile(File file) {
        try {
            //Create a client which is executing the request
            HttpClient client = HttpClientBuilder.create().build();
            //Using post method to upload the image
            HttpPost post = new HttpPost("http://www.abload.de/api/upload");

            //Create the Entity which should be sent to the API
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.addPart("session", new StringBody(loginResponse.getSession(), ContentType.DEFAULT_TEXT));
            entityBuilder.addPart("img0", new FileBody(file));
            entityBuilder.addPart("gallery", new StringBody(""+gallery.getId(), ContentType.DEFAULT_TEXT));

            //Send the request
            post.setEntity(entityBuilder.build());

            HttpResponse response = client.execute(post);

            //The answer of the API should be an xml document with "abload" as root
            RootElement root = new RootElement("abload");
            //The node images should contain one node which is holding all the information returned by the API
            root.getChild("images").getChild("image").setStartElementListener(new ImageListener());

            //Get the answer from the API
            Xml.parse(response.getEntity().getContent(), Xml.Encoding.UTF_8, root.getContentHandler());

            if(!(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * Returns the List which is containing all ImageSrc of the server.
     *
     * @return The List<ImageSrc> which is containing all information about the uploaded image.
     */
    public List<ImageSrc> getUploadedImageList() {
        return this.uploadedImageList;
    }

    private class ImageListener implements StartElementListener {
        @Override
        public void start(Attributes attributes) {
            //The attributes for the ImageUploaded model.
            String newName = attributes.getValue("newname");
            String oldName = attributes.getValue("oldname");
            String res = attributes.getValue("res");
            String type = attributes.getValue("type");

            //Create new Image Uploaded
            ImageUploaded imageUploaded = new ImageUploaded(newName, oldName, res, type);
            //Create a new ImageSrc which is then added to the uploaded file list.
            ImageSrc imageSrc = new ImageSrc(imageUploaded.getFileName());
            uploadedImageList.add(imageSrc);
        }
    }
}
