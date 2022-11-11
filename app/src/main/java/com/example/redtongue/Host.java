package com.example.redtongue;

import java.net.*;

public class Host {
  private InetAddress IP;
  private int port = 0;
  private String name;
  private TCP t;

  public Host(InetAddress ip, String hostName) {
    this.IP = ip;
    this.name = hostName;
  }

  public void close() {
    if (t != null) {
      t.close();
    }
  }

  public void setPort(int port) {
    this.port = port;
  }

  public void enableTCP(boolean send_recv) {
    if (send_recv == TCP.SEND) {
      t = new TCP(IP.getHostAddress(), port);
    } else {
      t = new TCP(port);
      t.connect();
    }
    if (port == 0) {
      port = t.getPort();
    }
  }

  public InetAddress getIP() {
    return IP;
  }

  public int getPort() {
    return port;
  }

  public String getName() {
    return name;
  }

  public TCP getTCP() {
    return t;
  }

  @Override
  public boolean equals(Object test) {
    return test.equals(name);
  }
}
