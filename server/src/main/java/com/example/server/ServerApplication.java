package com.example.server;

import com.example.server.dto.Info;
import com.example.server.dto.Word;
import com.example.server.service.TrainingService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

@SpringBootApplication
public class ServerApplication {
    private static final Logger logger = LoggerFactory.getLogger(ServerApplication.class);

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        TrainingService.trainNetwork();

        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            logger.info("Server started!");

            while (true) {

                Socket socket = serverSocket.accept();

                BufferedWriter writer =
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        socket.getOutputStream()));
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()));

                new Thread(() -> {
                    String request = null;
                    try {
                        request = reader.readLine();
                        Info info = gson.fromJson(request,Info.class);
                        List<Word> wordList = TrainingService.returnWords(info);
//                        try {
//                            Thread.sleep(4000);
//                        } catch (InterruptedException e) {
//                        }
                        writer.write(gson.toJson(wordList));
                        writer.newLine();
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }).start();


            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            SpringApplication.run(ServerApplication.class, args);
        }

    }
}
