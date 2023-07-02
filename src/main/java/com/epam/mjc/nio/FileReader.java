package com.epam.mjc.nio;

import java.io.*;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;


public class FileReader {
    public Profile getDataFromFile(File file) {

        Profile profile = new Profile();

        String name;
        int age;
        String email;
        long phone;

        try (RandomAccessFile aFile = new RandomAccessFile(file, "r"); FileChannel inChannel = aFile.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            StringBuilder newString = new StringBuilder();

            while (inChannel.read(buffer) > 0) {
                buffer.flip();
                for (int i = 0; i < buffer.limit(); i++) {
                    newString.append((char) buffer.get());
                }
                buffer.clear();
                inChannel.read(buffer); // reading the buffer
            }

            String formattedText = newString.toString().replace("\n", ",").replace("\r", "");
            Map<String, String> map = new HashMap<>();
            String[] listString = formattedText.split("\\,");
            for (String element : listString) {
                String[] field = element.split(": ");
                map.put(field[0], field[1]);
            }

            name = map.get("Name");
            age = Integer.valueOf(map.get("Age"));
            email = map.get("Email");
            phone = Long.valueOf(map.get("Phone"));

            profile = new Profile(name, age, email, phone);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return profile;
    }
}
