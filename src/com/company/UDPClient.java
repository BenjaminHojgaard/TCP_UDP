package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UDPClient
{
    // Client needs to know server identification, <IP:port>
    private static final int serverPort = 7777;

    // buffers for the messages
    public static String message;
    private static byte[] dataIn = new byte[256];
    private static byte[] dataOut = new byte[256];

    // In UDP messages are encapsulated in packages and sent over sockets
    private static DatagramPacket requestPacket;
    private static DatagramPacket responsePacket;
    private static DatagramSocket clientSocket;

    public static void main(String[] args) throws IOException
    {
        clientSocket = new DatagramSocket();
        InetAddress serverIP = InetAddress.getLocalHost();
        System.out.println(serverIP);

        Scanner scan = new Scanner(System.in);
        System.out.println("Type string path: ");

        while((message = scan.nextLine()) != null)
        {
            sendRequest(serverIP);
            receiveResponse();
        }
        clientSocket.close();
    }

    public static void sendRequest(InetAddress serverIP) throws IOException
    {
        //clientSocket = new DatagramSocket();

        URL url = new URL("https://media.istockphoto.com/photos/beautiful-luxury-home-exterior-at-twilight-picture-id1026205392");

        byte[] imageBytes;

        BufferedImage image = ImageIO.read(url);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        baos.flush();
        imageBytes = baos.toByteArray();
        baos.close();

        requestPacket = new DatagramPacket(imageBytes, 1024, serverIP, serverPort);
        clientSocket.send(requestPacket);


    }

    public static void receiveResponse() throws IOException
    {
        //clientSocket = new DatagramSocket();
        responsePacket = new DatagramPacket(dataIn, dataIn.length);
        clientSocket.receive(responsePacket);
        String message = new String(responsePacket.getData(), 0, responsePacket.getLength());
        System.out.println("Response from Server: " + message);
    }
}