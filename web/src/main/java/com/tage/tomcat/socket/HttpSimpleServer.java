package com.tage.tomcat.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpSimpleServer {
  private static final String CLOSE_COMMAND="/close";
  private boolean shut = false;
  public static void main(String[] args) throws IOException {
       HttpSimpleServer httpServer = new HttpSimpleServer();
       httpServer.await();
  }

  private void await() throws IOException {
    ServerSocket serverSocket =null;
    int port = 8090;
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e){
      e.printStackTrace();
      System.exit(1);
    }
    while (!shut){
      System.out.println("start");
      Socket socket = null;
      try {
        socket = serverSocket.accept();
        InputStream in =socket.getInputStream();
        BufferedReader inReader = new BufferedReader(new InputStreamReader(in));
        String input;
        while ((input =inReader.readLine()).length()>0){
          System.out.println(input);
        }
        // 向客户端回复信息
        OutputStream outputStream = socket.getOutputStream();
        PrintStream out = new PrintStream(socket.getOutputStream());
        out.println("HTTP/1.1 200 OK\n"
            + "Accept-Ranges: bytes\n"
            + "Cache-Control: max-age=315360000\n"
            + "Content-Length: 754\n"
            + "Content-Type: application/json; charset=utf-8\n"
            + "Date: Fri, 14 Aug 2020 04:07:09 GMT\n"
            + "Etag: \"1ec5-502264e2ae4c0\"\n"
            + "Expires: Mon, 12 Aug 2030 04:07:09 GMT\n"
            + "Server: Apache\n"
            + "\r\n"
            + "\"protocols\":{\"management\":{\"username\":\"rabbitmq\",\"password\":\"rabbitmq\",\"virtual_host\":\"production\",\"hostname\":\"localhost\",\"hostnames\":[\"localhost\",\"localhost\"],\"port\":15672,\"ssl\":\"false\",\"protocol_versions\":[\"HTTP 1.1\"],\"uri\":\"http://rabbitmq:rabbitmq@localhost:15672/api\",\"uris\":[\"http://rabbitmq:rabbitmq@localhost:15672/api\",\"http://rabbitmq:rabbitmq@localhost:15672/api\"]},\"amqp\":{\"username\":\"rabbitmq\",\"password\":\"rabbitmq\",\"virtual_host\":\"production\",\"hostname\":\"localhost\",\"hostnames\":[\"localhost\",\"localhost\"],\"port\":5672,\"ssl\":\"false\",\"protocol_versions\":[\"AMQP 0.9.1\"],\"uri\":\"amqp://rabbitmq:rabbitmq@localhost:5672/production\",\"uris\":[\"amqp://rabbitmq:rabbitmq@localhost:5672/production\",\"amqp://rabbitmq:rabbitmq@localhost:5672/production\"]}}");
        in.close();
        out.close();
        System.out.println("end");
      }finally {
        try {
          socket.close();
        } catch (Exception e) {
          socket = null;
          System.out.println("服务端 finally 异常:" + e.getMessage());
        }
      }

    }
  }
  private class HandlerThread implements Runnable {
    private Socket socket;
    public HandlerThread(Socket client) {
      socket = client;
      new Thread(this).start();
    }

    public void run() {
      try {
        // 读取客户端数据
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String clientInputStr = input.readLine();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException
        // 处理客户端数据
        System.out.println("客户端发过来的内容:" + clientInputStr);

        // 向客户端回复信息
        PrintStream out = new PrintStream(socket.getOutputStream());
        out.println("HTTP/1.1 200 OK\n"
            + "Accept-Ranges: bytes\n"
            + "Cache-Control: max-age=315360000\n"
            + "Content-Length: 7877\n"
            + "Content-Type: image/png\n"
            + "Date: Fri, 14 Aug 2020 04:07:09 GMT\n"
            + "Etag: \"1ec5-502264e2ae4c0\"\n"
            + "Expires: Mon, 12 Aug 2030 04:07:09 GMT\n"
            + "Last-Modified: Wed, 03 Sep 2014 10:00:27 GMT\n"
            + "Server: Apache");

        out.close();
        input.close();
      } catch (Exception e) {
        System.out.println("服务器 run 异常: " + e.getMessage());
      } finally {
        if (socket != null) {
          try {
            socket.close();
          } catch (Exception e) {
            socket = null;
            System.out.println("服务端 finally 异常:" + e.getMessage());
          }
        }
      }
    }
  }
}
