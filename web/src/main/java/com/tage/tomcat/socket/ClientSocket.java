package com.tage.tomcat.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket {

  public static void main(String[] args) throws IOException, InterruptedException {
    Socket socket = new Socket("127.0.0.1", 8090);
    OutputStream os = socket.getOutputStream();
    boolean autoFlush = true;
    PrintWriter writer = new PrintWriter(os, autoFlush);
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    writer.println("GET /myweb/hello HTTP/1.1");
    writer.println("Host: localhost:8090");
    writer.println("Connection: Close");
    writer.println();
    boolean loop = true;
    StringBuffer sb = new StringBuffer(8096);
    while (loop) {
      if (in.ready()) {
        int i = 0;
        while (i != -1) {
          i = in.read();
          sb.append((char) i);
        }
        loop = false;
      }
      Thread.currentThread().sleep(50);
    }
    System.out.println(sb.toString());
    socket.close();
  }

}
