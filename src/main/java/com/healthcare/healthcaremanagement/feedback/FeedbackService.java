package com.healthcare.healthcaremanagement.feedback;

import com.healthcare.healthcaremanagement.ManagementService;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;

@Service
public class FeedbackService implements ManagementService<Feedback> {

    private static final String FILE_PATH = "src/main/resources/data/feedbacks.txt";

    @Override
    public List<Feedback> getAll() {
        List<Feedback> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 6) {
                    list.add(new Feedback(p[0], p[1], p[2], p[3],
                            Integer.parseInt(p[4]), p[5]));
                }
            }
        } catch (IOException e) {}
        return list;
    }

    @Override
    public void save(Feedback f) {
        List<Feedback> list = getAll();
        f.setId(UUID.randomUUID().toString().substring(0, 8));
        list.add(f);
        writeAll(list);
    }

    @Override
    public void update(String id, Feedback updated) {
        List<Feedback> list = getAll();
        for (Feedback f : list) {
            if (f.getId().equals(id)) {
                f.setPatientName(updated.getPatientName());
                f.setDoctorName(updated.getDoctorName());
                f.setMessage(updated.getMessage());
                f.setRating(updated.getRating());
                f.setDate(updated.getDate());
            }
        }
        writeAll(list);
    }

    @Override
    public void delete(String id) {
        List<Feedback> list = getAll();
        list.removeIf(f -> f.getId().equals(id));
        writeAll(list);
    }

    @Override
    public Feedback getById(String id) {
        return getAll().stream()
                .filter(f -> f.getId().equals(id))
                .findFirst().orElse(null);
    }

    public List<Feedback> getAllFeedbacks() { return getAll(); }
    public void saveFeedback(Feedback f) { save(f); }
    public void updateFeedback(String id, Feedback f) { update(id, f); }
    public void deleteFeedback(String id) { delete(id); }
    public Feedback getFeedbackById(String id) { return getById(id); }

    private void writeAll(List<Feedback> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Feedback f : list) {
                bw.write(f.getId() + "," + f.getPatientName() + "," +
                        f.getDoctorName() + "," + f.getMessage() + "," +
                        f.getRating() + "," + f.getDate());
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}