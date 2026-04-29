package com.healthcare.healthcaremanagement.scheduling;

import com.healthcare.healthcaremanagement.ManagementService;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;

@Service
public class SchedulingService implements ManagementService<Schedule> {

    private static final String FILE_PATH = "src/main/resources/data/schedules.txt";

    public List<Schedule> getSchedulesByDoctor(String doctorName) {
        return getAll().stream()
                .filter(s -> s.getDoctorName().equals(doctorName))
                .toList();
    }

    public List<Schedule> getAvailableSchedules() {
        return getAll().stream()
                .filter(s -> "Available".equals(s.getStatus()))
                .toList();
    }

    @Override
    public List<Schedule> getAll() {
        List<Schedule> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 9) {
                    list.add(new Schedule(p[0], p[1], p[2], p[3], p[4],
                            p[5], Integer.parseInt(p[6]),
                            Integer.parseInt(p[7]), p[8]));
                }
            }
        } catch (IOException e) {}
        return list;
    }

    @Override
    public void save(Schedule s) {
        List<Schedule> list = getAll();
        s.setId(UUID.randomUUID().toString().substring(0, 8));
        if (s.getCurrentPatients() >= s.getMaxPatients()) {
            s.setStatus("Full");
        } else {
            s.setStatus("Available");
        }
        list.add(s);
        writeAll(list);
    }

    @Override
    public void update(String id, Schedule updated) {
        List<Schedule> list = getAll();
        for (Schedule s : list) {
            if (s.getId().equals(id)) {
                s.setDoctorName(updated.getDoctorName());
                s.setAvailableDays(updated.getAvailableDays());
                s.setStartTime(updated.getStartTime());
                s.setEndTime(updated.getEndTime());
                s.setRoomNumber(updated.getRoomNumber());
                s.setMaxPatients(updated.getMaxPatients());
                s.setCurrentPatients(updated.getCurrentPatients());
                if (s.getCurrentPatients() >= s.getMaxPatients()) {
                    s.setStatus("Full");
                } else {
                    s.setStatus("Available");
                }
            }
        }
        writeAll(list);
    }

    @Override
    public void delete(String id) {
        List<Schedule> list = getAll();
        list.removeIf(s -> s.getId().equals(id));
        writeAll(list);
    }

    @Override
    public Schedule getById(String id) {
        return getAll().stream()
                .filter(s -> s.getId().equals(id))
                .findFirst().orElse(null);
    }

    public List<Schedule> getAllSchedules() { return getAll(); }
    public void saveSchedule(Schedule s) { save(s); }
    public void updateSchedule(String id, Schedule s) { update(id, s); }
    public void deleteSchedule(String id) { delete(id); }
    public Schedule getScheduleById(String id) { return getById(id); }

    private void writeAll(List<Schedule> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Schedule s : list) {
                bw.write(s.getId() + "," + s.getDoctorName() + "," +
                        s.getAvailableDays() + "," + s.getStartTime() + "," +
                        s.getEndTime() + "," + s.getRoomNumber() + "," +
                        s.getMaxPatients() + "," + s.getCurrentPatients() + "," +
                        s.getStatus());
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}