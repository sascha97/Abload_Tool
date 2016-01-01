package com.lutzenberger.sascha.abload.data;

import java.io.Serializable;

import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 * Created by saschalutzenberger on 29/12/15.
 */
public class LoginResponse implements Serializable {
    private String code;
    private String user;
    private String session;
    private Cookie clientCookie;

    public void setCode(String code) {
        this.code = code;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Cookie getSessionCookie() {
        if(clientCookie == null) {
            String domain = ".abload.de";
            String path = "/";

            BasicClientCookie clientCookie = new BasicClientCookie("ablgntan", this.getSession());
            clientCookie.setDomain(domain);
            clientCookie.setPath(path);

            clientCookie.setAttribute(ClientCookie.PATH_ATTR, path);
            clientCookie.setAttribute(ClientCookie.DOMAIN_ATTR, domain);

            this.clientCookie = clientCookie;
        }

        return clientCookie;
    }

    public String getSession() {
        return this.session;
    }
}
