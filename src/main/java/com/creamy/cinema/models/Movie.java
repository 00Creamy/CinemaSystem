package com.creamy.cinema.models;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Movie {
    public enum MovieStatus {
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

        public static MovieStatus getStatusFromLevel(int level) {
            return switch (level) {
                case 0 -> COMING_SOON;
                case 1 -> AIRING;
                case 2 -> ARCHIVED;
                default -> null;
            };
        }
    }

    private int movieId;
    private String title;
    private String description;
    private Duration duration;
    private String imagePath;
    private List<String> tags;
    private List<String> directors;
    private List<Actor> actors;
    private String rating;
    private String language;
    private List<String> subtitles;
    private MovieStatus status;
    private boolean deleted;

    public Movie() {
        this.tags = new ArrayList<>();
        this.directors = new ArrayList<>();
        this.actors = new ArrayList<>();
        this.subtitles = new ArrayList<>();
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(List<String> subtitles) {
        this.subtitles = subtitles;
    }

    public MovieStatus getStatus() {
        return status;
    }

    public void setStatus(MovieStatus status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
