/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject2;

/**
 *
 * @author paula.paez
 */
import java.net.MalformedURLException;
import java.net.URL;

public class URLParser {

    public static void main(String[] args) throws MalformedURLException {
        URL myurl = new URL("https://lbdn.is.esucelaing.edu.co:8676/index.html?val=96&t=56#events");
        System.out.println("Protocol: " + myurl.getProtocol());
        System.out.println("Authority: " + myurl.getAuthority());
        System.out.println("Host: " + myurl.getHost());
        System.out.println("Port: " + myurl.getPort());
        System.out.println("Path: " + myurl.getPath());
        System.out.println("Query: " + myurl.getQuery());
        System.out.println("File: " + myurl.getFile());
        System.out.println("Ref: " + myurl.getRef());
    }
}
