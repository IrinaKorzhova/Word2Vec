package com.example.server.service;

import com.example.server.dto.Info;
import com.example.server.dto.Word;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    private static Word2Vec vec;

    public static void trainNetwork() throws IOException {
        logger.info("Load & Vectorize Sentences....");
        // Strip white space before and after for each line
        SentenceIterator iter = new BasicLineIterator(new ClassPathResource("raw_sentences.txt").getFile());
        // Split on white spaces in the line to get words
        TokenizerFactory t = new DefaultTokenizerFactory();

        t.setTokenPreProcessor(new CommonPreprocessor());

        logger.info("Building model....");
        vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();
        logger.info("Fitting Word2Vec model....");
        vec.fit();

        logger.info("Writing word vectors to text file....");

        logger.info("Closest Words:");
    }

    public static List<Word> returnWords(Info info) {
        return vec.wordsNearestSum(info.getWord(), info.getNumber()).stream().map(item -> new Word(item + " " + info.getWord())).collect(Collectors.toList());
    }
}
