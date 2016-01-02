package com.lutzenberger.sascha.concurrency;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.xml.sax.Attributes;

import com.lutzenberger.sascha.abload.data.LoginResponse;
import com.lutzenberger.sascha.sax.RootElement;
import com.lutzenberger.sascha.sax.StartElementListener;
import com.lutzenberger.sascha.sax.Xml;

/**
 * This Task handles the Login on http://www.abload.de using the LoginAPI.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 28.12.2015
 */
public class LoginTask extends Task<LoginResponse> {
    private ResourceBundle res = ResourceBundle.getBundle("strings/Values");

    /**
     * Used to send updates in a thread safe manner from the subclass to the FXApplicationThread.
     * AtomicReference is used so as to coalesce updates such that the event queue doesn't get flooded.
     */
    private AtomicReference<String> atomicReferenceLoginStatus = new AtomicReference<>();

    /**
     * The StringProperty which is containing the login status.
     */
    private StringProperty loginStatus = new SimpleStringProperty();

    //The user name of the Abload account
    private static final String USERNAME = "";
    //The password for the Abload account
    private static final String PASSWORD = "";

    private LoginResponse loginResponse;

    public LoginTask() {
        //Set up the Properties to their default Strings
        loginStatus.setValue(res.getString("login.notLoggedIn"));
    }

    @Override
    protected LoginResponse call() throws Exception {
        updateLoginStatus(res.getString("login.loggingIn"));

        //Create the client which is executing the API call
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://www.abload.de/api/login?name=" +
                URLEncoder.encode(USERNAME, String.valueOf(StandardCharsets.UTF_8)) + "&password="
                + URLEncoder.encode(PASSWORD, String.valueOf(StandardCharsets.UTF_8)));

        HttpResponse response = client.execute(request);

        if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            //Login Failed because of unsuccessful Http Status
            updateLoginStatus(res.getString("login.loginFailed"));
        } else {
            loginResponse = new LoginResponse();

            //The answer of the API should be an xml document with "abload" as root element
            RootElement root = new RootElement("abload");
            //The element status should contain the status of the api
            root.getChild("status").setStartElementListener(new StatusListener());
            //The element login contains all the user information if available
            root.getChild("login").setStartElementListener(new LoginListener());

            //Get the answer from the API
            Xml.parse(response.getEntity().getContent(), Xml.Encoding.UTF_8, root.getContentHandler());
        }

        checkLoggedIn(loginResponse);

        return loginResponse;
    }

    /**
     * Get the loginStatus Property as ReadOnlyProperty to be able to bind this to an User Interface.
     *
     * @return The loginStatus Property which can be easily bound to an User Interface.
     */
    public ReadOnlyStringProperty loginStatusProperty() {
        return loginStatus;
    }

    private void checkLoggedIn(LoginResponse loginResponse) {
        if(loginResponse.getSession() == null) {
            updateLoginStatus(res.getString("login.loginFailed"));
        } else {
            updateLoginStatus(res.getString("login.loggedIn"));
        }
    }

    /**
     * The method is used for updating the loginStatus property. The method will be called by the thread.
     *
     * @param newLoginStatus The new status of the login.
     */
    private void updateLoginStatus(String newLoginStatus) {
        //This can only be updated inside the FxApplicationThread
        if(isFXApplicationThread()) {
            loginStatus.setValue(newLoginStatus);
        } else if(atomicReferenceLoginStatus.getAndSet(newLoginStatus) == null) {
            //This can only be updated inside the FxApplicationThread
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    String newLoginStatus = atomicReferenceLoginStatus.getAndSet(null);
                    LoginTask.this.loginStatus.setValue(newLoginStatus);
                }
            });
        }
    }

    //Checks if current thread is a JavaFx Application thread
    private boolean isFXApplicationThread() {
        return Platform.isFxApplicationThread();
    }

    private class StatusListener implements StartElementListener {
        @Override
        public void start(Attributes attributes) {
            loginResponse.setCode(attributes.getValue("code"));
        }
    }

    private class LoginListener implements StartElementListener {
        @Override
        public void start(Attributes attributes) {
            loginResponse.setUser(attributes.getValue("user"));
            loginResponse.setSession(attributes.getValue("session"));
        }
    }
}
