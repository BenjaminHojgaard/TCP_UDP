package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer
{
    private static final int serverPort = 7777;

    // buffers for the messages
    private static byte[] dataIn = new byte[128];
    private static byte[] dataOut = new byte[128];

    // In UDP messages are encapsulated in packages and sent over sockets
    private static DatagramPacket requestPacket;
    private static DatagramPacket responsePacket;
    private static DatagramSocket serverSocket;


    public static void main(String[] args) throws Exception
    {
        String messageIn, messageOut;
        try
        {
            String serverIP = InetAddress.getLocalHost().getHostAddress();
            // Opens socket for accepting requests
            serverSocket = new DatagramSocket(serverPort);
            while(true)
            {
                System.out.println("Server " + serverIP + " running ...");
                messageIn = receiveRequest();
                if (messageIn.equals("stop")) break;
                messageOut = processRequest(messageIn);
                sendResponse(messageOut);
            }
        }
        catch(Exception e)
        {
            System.out.println(" Connection fails: " + e);
        }
        finally
        {
            serverSocket.close();
            System.out.println("Server port closed");
        }
    }

    public static String receiveRequest() throws IOException
    {
        try {
            requestPacket = new DatagramPacket(dataIn, dataIn.length);
            serverSocket.receive(requestPacket);

            byte[] imageBytes = requestPacket.getData();
            System.out.println(imageBytes);
            InputStream in = new ByteArrayInputStream(imageBytes);
            BufferedImage bImage = ImageIO.read(in);

            ImageIO.write(bImage, "jpg", new File("C:/Users/Highyard/Desktop/new_image.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }


        return "Image received!";
    }

    public static String processRequest(String message)
    {
        return message.toUpperCase();
    }

    public static void sendResponse(String message) throws IOException
    {
        InetAddress clientIP;
        int clientPort;

        clientIP = requestPacket.getAddress();
        clientPort = requestPacket.getPort();
        System.out.println("Client port: " + clientPort);
        System.out.println("Response: " + message);
        dataOut = message.getBytes();
        responsePacket = new DatagramPacket(dataOut, dataOut.length, clientIP, clientPort);
        serverSocket.send(responsePacket);
        System.out.println("Message sent back " + message);
    }
}