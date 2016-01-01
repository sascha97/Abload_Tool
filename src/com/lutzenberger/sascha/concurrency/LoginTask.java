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
 * Created by saschalutzenberger on 28/12/15.
 */
public class LoginTask extends Task<LoginResponse> {
    private ResourceBundle res = ResourceBundle.getBundle("strings/Values");

    /**
     * Used to send updates in a thread safe manner from the subclass to the FXApplicationThread.
     * AtomicReference is used so as to coalesce updates such that the event queue doesn't get flooded.
     */
    private AtomicReference<String> atomicReferenceLoginStatus = new AtomicReference<>();

    private StringProperty loginStatus = new SimpleStringProperty();

    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    private LoginResponse loginResponse;

    public LoginTask() {
        loginStatus.setValue(res.getString("login.notLoggedIn"));
    }

    @Override
    protected LoginResponse call() throws Exception {
        updateLoginStatus(res.getString("login.loggingIn"));

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

            RootElement root = new RootElement("abload");
            root.getChild("status").setStartElementListener(new StatusListener());
            root.getChild("login").setStartElementListener(new LoginListener());

            Xml.parse(response.getEntity().getContent(), Xml.Encoding.UTF_8, root.getContentHandler());
        }

        checkLoggedIn(loginResponse);

        return loginResponse;
    }

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

    private void updateLoginStatus(String newLoginStatus) {
        if(isFXApplicationThread()) {
            loginStatus.setValue(newLoginStatus);
        } else if(atomicReferenceLoginStatus.getAndSet(newLoginStatus) == null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    String newLoginStatus = atomicReferenceLoginStatus.getAndSet(null);
                    LoginTask.this.loginStatus.setValue(newLoginStatus);
                }
            });
        }
    }

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
