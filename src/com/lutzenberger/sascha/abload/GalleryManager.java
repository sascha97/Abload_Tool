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
 * This class represents a Gallery Manager for http://www.abload.de
 * This manager can get all Galleries associated with the user and it can also create a new gallery for a user.
 *
 * @author Sascha Lutzenberger
 * @version 1.1 - 31.12.2015
 */
public class GalleryManager {
    //The Login data
    private LoginResponse loginResponse;

    //The list which will contain the Galleries
    private List<Gallery> galleryList = new ArrayList<>();

    public GalleryManager(LoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }

    /**
     * This method returns a gallery list of all the available galleries.
     *
     * @return A gallery list with all the available galleries.
     */
    public List<Gallery> getGalleries() {
        galleryList.clear(); //Delete all the Galleries added previously to the list

        try {
            //Get a http client to send a request to the abload server.
            HttpClient client = HttpClientBuilder.create().build();
            //This is the server to send the request to
            HttpGet request = new HttpGet("http://www.abload.de/api/gallerylist?session=" +
                URLEncoder.encode(loginResponse.getSession(), String.valueOf(StandardCharsets.UTF_8)));
            HttpResponse response = client.execute(request);

            //Response should be an xml file containing all images with the root "abload"
            RootElement root = new RootElement("abload");
            //This root element should contain a node galleries with nodes gallery
            root.getChild("galleries").getChild("gallery").setStartElementListener(new GalleryListener());
            Xml.parse(response.getEntity().getContent(), Xml.Encoding.UTF_8, root.getContentHandler());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return galleryList;
    }

    /**
     * This method creates a new Gallery on http://www.abload.de
     *
     * @param galleryName The name of the new gallery.
     *
     * @return true if gallery was created successful false if not
     */
    public boolean createNewGallery(String galleryName) {
        //flag containing a negative result
        boolean result = false;

        try {
            //Setting the Session cookie in order to get any response from the API
            BasicCookieStore cookieStore = new BasicCookieStore();
            cookieStore.addCookie(loginResponse.getSessionCookie());

            //Set the user agent this is neccessary because otherwise you will get a XSS Validation error
            HttpClient client = HttpClientBuilder.create().setUserAgent("Abloadlib/0.1")
                    .setDefaultCookieStore(cookieStore).build();

            //The api which should be requested
            HttpGet request = new HttpGet("http://www.abload.de/calls/createGallery.php?name="
                    + URLEncoder.encode(galleryName, String.valueOf(StandardCharsets.UTF_8)) + "&desc=");

            HttpResponse response = client.execute(request);

            //If request was okay
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //Read in the response message from the API
                InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent());
                BufferedReader reader = new BufferedReader(inputStreamReader);

                //If the message is a number which can be converted into a long then everything was successful
                String createdGalleryStatus = reader.readLine(); //The number must be in the first line

                try {
                    //Try to convert the response into a long number
                    long galleryId = Long.parseLong(createdGalleryStatus);
                    //Gallery was successfully created
                    result = true;
                } catch (NumberFormatException e) {
                    //Gallery was not successfully created.
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
            //Listen for the attributes name and id which are needed by the Gallery model.
            String name = attributes.getValue("name");
            long id = Long.parseLong(attributes.getValue("id"));

            //Create a new Gallery model and add it to the galleryList.
            Gallery gallery = new Gallery(name, id);
            galleryList.add(gallery);
        }
    }
}
