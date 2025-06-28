package com.creamy.cinema.models;

import java.util.List;

public class Movie {
    enum MovieStatus {
        COMING_SOON(0, "Coming Soon"),
        AIRING(1, "Airing"),
        ARCHIVED(2, "Archived");
        private final int level;
        private final String name;

        MovieStatus(int level, String name) {
            this.level = level;
            this.name = name;
        }

        public int getLevel() {
            return level;
        }

        public String getName() {
            return name;
        }
    }

    private int movieId;
    private String title;
    private String description;
    private int duration;
    private String imagePath;
    private List<String> tags;
    private List<String> crew;
    private String rating;
    private String language;
    private List<String> subtitles;
    private MovieStatus status;
    private boolean deleted;
}
