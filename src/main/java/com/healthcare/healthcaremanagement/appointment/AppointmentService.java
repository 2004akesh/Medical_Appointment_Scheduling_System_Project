package com.healthcare.healthcaremanagement.appointment;

import com.healthcare.healthcaremanagement.ManagementService;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentService implements ManagementService<Appointment> {

    private static final String FILE_PATH = "src/main/resources/data/appointments.txt";


    public List<Appointment> getAppointmentsByPriority() {
        List<Appointment> allList = new ArrayList<>(getAll());
        List<Appointment> result = new ArrayList<>();

        List<String> dates = allList.stream()
                .map(Appointment::getDate)
                .distinct()
                .sorted()
                .toList();

        for (String date : dates) {
            // Get all appointments for this date
            List<Appointment> dayList = allList.stream()
                    .filter(a -> a.getDate().equals(date))
                    .collect(Collectors.toList());

            PriorityQueue<Appointment> priorityQueue = new PriorityQueue<>(
                    Comparator.comparingInt(Appointment::getUrgency)
            );
            priorityQueue.addAll(dayList);

            List<Appointment> urgencySorted = new ArrayList<>();
            while (!priorityQueue.isEmpty()) {
                urgencySorted.add(priorityQueue.poll());
            }

            List<String> urgencyLevels = urgencySorted.stream()
                    .map(a -> String.valueOf(a.getUrgency()))
                    .distinct()
                    .sorted()
                    .toList();

            for (String urgency : urgencyLevels) {
                List<Appointment> urgencyGroup = urgencySorted.stream()
                        .filter(a -> String.valueOf(
                                a.getUrgency()).equals(urgency))
                        .collect(Collectors.toList());


                int n = urgencyGroup.size();
                for (int i = 0; i < n - 1; i++) {
                    for (int j = 0; j < n - i - 1; j++) {
                        int time1 = convertToMinutes(
                                urgencyGroup.get(j).getTimeSlot());
                        int time2 = convertToMinutes(
                                urgencyGroup.get(j + 1).getTimeSlot());
                        if (time1 > time2) {
                            Appointment temp = urgencyGroup.get(j);
                            urgencyGroup.set(j, urgencyGroup.get(j + 1));
                            urgencyGroup.set(j + 1, temp);
                        }
                    }
                }
                result.addAll(urgencyGroup);
            }
        }
        return result;
    }



    public List<Appointment> getAppointmentsSortedByTime() {
        List<Appointment> list = new ArrayList<>(getAll());
        int n = list.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                boolean shouldSwap = false;

                int dateCompare = list.get(j).getDate()
                        .compareTo(list.get(j + 1).getDate());

                if (dateCompare > 0) {

                    shouldSwap = true;
                } else if (dateCompare == 0) {

                    int time1 = convertToMinutes(list.get(j).getTimeSlot());
                    int time2 = convertToMinutes(list.get(j + 1).getTimeSlot());
                    if (time1 > time2) shouldSwap = true;
                }

                if (shouldSwap) {
                    Appointment temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
        return list;
    }


    private int convertToMinutes(String timeSlot) {
        try {
            java.time.LocalTime time = java.time.LocalTime.parse(
                    timeSlot.toUpperCase(),
                    java.time.format.DateTimeFormatter.ofPattern("hh:mm a")
            );
            return time.getHour() * 60 + time.getMinute();
        } catch (Exception e) {
            return 0;
        }
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
                        a.getTimeSlot() + "," + a.getStatus() + "," +
                        a.getUrgency());
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}