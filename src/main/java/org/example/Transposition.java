package org.example;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Transposition {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Transposition <input_file> <semitones> <output_file>");
            return;
        }

        String inputFile = args[0];
        int semitones = Integer.parseInt(args[1]);
        String outputFile = args[2];

        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(inputFile)));
            ObjectMapper objectMapper = new ObjectMapper();
            List<List<Integer>> musicalPiece = objectMapper.readValue(jsonContent, List.class);

            List<List<Integer>> transposedPiece = transposePiece(musicalPiece, semitones);

            if (isPieceInRange(transposedPiece)) {
                objectMapper.writeValue(Paths.get(outputFile).toFile(), transposedPiece);
                System.out.println("Transposition successful. Result saved to " + outputFile);
            } else {
                System.out.println("Error: Transposed notes are out of keyboard range.");
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static List<List<Integer>> transposePiece(List<List<Integer>> musicalPiece, int semitones) {
        for (List<Integer> note : musicalPiece) {
            int octave = note.get(0);
            int noteNumber = note.get(1);

            int newNoteNumber = (noteNumber + semitones) % 12;
            int newOctave = octave + (noteNumber + semitones) / 12;

            note.set(0, newOctave);
            note.set(1, newNoteNumber);
        }
        return musicalPiece;
    }

    private static boolean isPieceInRange(List<List<Integer>> musicalPiece) {
        for (List<Integer> note : musicalPiece) {
            int octave = note.get(0);
            int noteNumber = note.get(1);

            if (!(octave >= -3 && octave <= 5 && noteNumber >= 1 && noteNumber <= 12)) {
                return false;
            }
        }
        return true;
    }
}
