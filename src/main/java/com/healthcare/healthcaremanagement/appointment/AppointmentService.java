package com.healthcare.healthcaremanagement.appointment;

import com.healthcare.healthcaremanagement.ManagementService;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;

@Service
public class AppointmentService implements ManagementService<Appointment> {

    private static final String FILE_PATH = "src/main/resources/data/appointments.txt";


    public List<Appointment> getAppointmentsByPriority() {
        PriorityQueue<Appointment> priorityQueue = new PriorityQueue<>(
                Comparator.comparingInt(Appointment::getUrgency)
        );
        priorityQueue.addAll(getAll());
        List<Appointment> sorted = new ArrayList<>();
        while (!priorityQueue.isEmpty()) {
            sorted.add(priorityQueue.poll());
        }
        return sorted;
    }

    public List<Appointment> getAppointmentsSortedByTime() {
        List<Appointment> list = getAll();
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).getTimeSlot()
                        .compareTo(list.get(j + 1).getTimeSlot()) > 0) {
                    Appointment temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
        return list;
    }

    @Override
    public List<Appointment> getAll() {
        List<Appointment> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 7) {
                    list.add(new Appointment(p[0], p[1], p[2], p[3], p[4],
                            p[5], Integer.parseInt(p[6])));
                }
            }
        } catch (IOException e) {}
        return list;
    }

    @Override
    public void save(Appointment a) {
        List<Appointment> list = getAll();
        a.setId(UUID.randomUUID().toString().substring(0, 8));
        list.add(a);
        writeAll(list);
    }

    @Override
    public void update(String id, Appointment updated) {
        List<Appointment> list = getAll();
        for (Appointment a : list) {
            if (a.getId().equals(id)) {
                a.setPatientName(updated.getPatientName());
                a.setDoctorName(updated.getDoctorName());
                a.setDate(updated.getDate());
                a.setTimeSlot(updated.getTimeSlot());
                a.setStatus(updated.getStatus());
                a.setUrgency(updated.getUrgency());
            }
        }
        writeAll(list);
    }

    @Override
    public void delete(String id) {
        List<Appointment> list = getAll();
        list.removeIf(a -> a.getId().equals(id));
        writeAll(list);
    }

    @Override
    public Appointment getById(String id) {
        return getAll().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst().orElse(null);
    }

    public List<Appointment> getAllAppointments() { return getAll(); }
    public void saveAppointment(Appointment a) { save(a); }
    public void updateAppointment(String id, Appointment a) { update(id, a); }
    public void deleteAppointment(String id) { delete(id); }
    public Appointment getAppointmentById(String id) { return getById(id); }

    private void writeAll(List<Appointment> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Appointment a : list) {
                bw.write(a.getId() + "," + a.getPatientName() + "," +
                        a.getDoctorName() + "," + a.getDate() + "," +
                        a.getTimeSlot() + "," + a.getStatus() + "," + a.getUrgency());
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}