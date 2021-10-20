/*package com.wellsfargo.rarconsumer.util;

import org.springframework.stereotype.Component;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


@Component
public class SSHConnection {

   public static void main(String args[]) throws JSchException {

      JSch jsch = new JSch();
      Session session = jsch.getSession("root", "34.93.144.69", 22);
      session.setPassword("OnyApp@123");
      jsch.addIdentity("E://training//JAVA-Training//ony_new", "OnyApp@123");
      java.util.Properties config = new java.util.Properties();
      config.put("StrictHostKeyChecking", "no");
      config.put("ConnectionAttempts", "3");
      session.setConfig(config);
      session.connect();
      System.out.println("SSH Connected");
      int localPort = 8087;
      String remoteHost = "localhost";
      int remotePort = 27017;
      int assinged_port = session.setPortForwardingL(localPort, remoteHost, remotePort);
   }
}*/
