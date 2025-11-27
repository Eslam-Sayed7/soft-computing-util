package com.example.softcomputing.fuzzy.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple CSV loader for person/user data
 */
public class UserCSVLoader {

    public static class User {
        private int userId;
        private String name;
        private double budget;
        private List<String> interests;

        public User(int userId, String name, double budget, List<String> interests) {
            this.userId = userId;
            this.name = name;
            this.budget = budget;
            this.interests = interests;
        }

        public int getUserId() { return userId; }
        public String getName() { return name; }
        public double getBudget() { return budget; }
        public List<String> getInterests() { return interests; }
    }

    public static List<User> loadUsers(String csvPath) throws Exception {
        List<User> users = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String header = br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                if (tokens.length >= 4) {
                    int id = Integer.parseInt(tokens[0].trim());
                    String name = tokens[1].trim();
                    double budget = Double.parseDouble(tokens[2].trim());
                    // interests may be quoted and contain commas; extract substring starting at 4th token index
                    int idx = nthIndexOf(line, ',', 3);
                    String interestsRaw = idx >= 0 ? line.substring(idx + 1).trim() : "";
                    if (interestsRaw.startsWith("\"") && interestsRaw.endsWith("\"")) {
                        interestsRaw = interestsRaw.substring(1, interestsRaw.length() - 1);
                    }
                    List<String> interests = new ArrayList<>();
                    for (String s : interestsRaw.split(",")) {
                        if (!s.trim().isEmpty()) interests.add(s.trim());
                    }

                    users.add(new User(id, name, budget, interests));
                }
            }
        }

        return users;
    }

    private static int nthIndexOf(String str, char ch, int n) {
        int pos = -1;
        for (int i = 0; i < n; i++) {
            pos = str.indexOf(ch, pos + 1);
            if (pos == -1) return -1;
        }
        return pos;
    }
}
