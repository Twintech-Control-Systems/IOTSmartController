package com.twintech.smartcontroller;

import android.util.Log;

import java.util.Map;
import fi.iki.elonen.NanoHTTPD;

public class AndroidWebServer extends NanoHTTPD {

    public AndroidWebServer(int port) {
        super(port);
    }

    public AndroidWebServer(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public NanoHTTPD.Response serve(IHTTPSession session) {
        Log.d("DEBUG", "Serving");

        String msg = "<html><body><h1>Hello server</h1>\n";
        Map<String, String> params = session.getParms();
        if (params.get("username") == null) {
            msg += "<form action='?' method='get'>\n";
            msg += "<p>Your name: <input type='text' name='username'></p>\n";
            msg += "</form>\n";
        } else {
            msg += "<p>Hello, " + params.get("username") + "!</p>";
        }
        return newFixedLengthResponse( msg + "</body></html>\n" );
    }
}
