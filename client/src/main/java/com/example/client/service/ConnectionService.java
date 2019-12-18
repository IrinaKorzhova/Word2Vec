package com.example.client.service;

import com.example.client.ClientApplication;
import com.example.client.dto.Info;
import com.example.client.dto.Word;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConnectionService {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionService.class);

    @Autowired
    private Gson gson;

    public List<Word> returnWords(Info info) {
        List<Word> wordList = null;
        try {
            Socket socket = new Socket("127.0.0.1", 8000);
            BufferedWriter writer = new BufferedWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream())));
            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));
            logger.info("Connected to server");
            String request = gson.toJson(info);

            writer.write(request);
            writer.newLine();
            writer.flush();

            String response = reader.readLine();
            Type listType = new TypeToken<ArrayList<Word>>() {
            }.getType();
            wordList = gson.fromJson(response, listType);
            return wordList;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordList;
    }
}
