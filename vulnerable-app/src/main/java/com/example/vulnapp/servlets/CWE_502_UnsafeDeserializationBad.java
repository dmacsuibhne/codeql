package com.example.vulnapp.servlets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CWE-502: Unsafe Deserialization.
 * Source: the request body (request.getInputStream()). Sink: in.readObject().
 * Root cause: an object is deserialized from an untrusted stream.
 */
public class CWE_502_UnsafeDeserializationBad extends HttpServlet {

    public static class MyObject implements Serializable {
        private static final long serialVersionUID = 1L;
        public int field;

        public MyObject(int field) {
            this.field = field;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (ObjectInputStream in = new ObjectInputStream(request.getInputStream())) {
            Object obj = in.readObject(); // BAD: in is from untrusted source
            response.getWriter().print("deserialized: " + obj.getClass().getName());
        } catch (ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }
}

