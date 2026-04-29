package com.healthcare.healthcaremanagement.doctor;

import com.healthcare.healthcaremanagement.ManagementService;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;

@Service
public class DoctorService implements ManagementService<Doctor> {

    private static final String FILE_PATH = "src/main/resources/data/doctors.txt";

    @Override
    public List<Doctor> getAll() {
        List<Doctor> doctors = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 7) {
                    doctors.add(new Doctor(p[0], p[1], p[2], p[3], p[4], p[5], p[6]));
                }
            }
        } catch (IOException e) {}
        return doctors;
    }

    @Override
    public void save(Doctor doctor) {
        List<Doctor> doctors = getAll();
        doctor.setId(UUID.randomUUID().toString().substring(0, 8));
        doctors.add(doctor);
        writeAll(doctors);
    }

    @Override
    public void update(String id, Doctor updated) {
        List<Doctor> doctors = getAll();
        for (Doctor d : doctors) {
            if (d.getId().equals(id)) {
                d.setName(updated.getName());
                d.setPhone(updated.getPhone());
                d.setSpecialization(updated.getSpecialization());
                d.setAvailability(updated.getAvailability());
                d.setPassword(updated.getPassword());
                d.setStatus(updated.getStatus());
            }
        }
        writeAll(doctors);
    }

    @Override
    public void delete(String id) {
        List<Doctor> doctors = getAll();
        doctors.removeIf(d -> d.getId().equals(id));
        writeAll(doctors);
    }

    @Override
    public Doctor getById(String id) {
        return getAll().stream()
                .filter(d -> d.getId().equals(id))
                .findFirst().orElse(null);
    }


    public Doctor findByNameAndPassword(String name, String password) {
        return getAll().stream()
                .filter(d -> d.getName().equals(name)
                        && d.getPassword().equals(password)
                        && "Approved".equals(d.getStatus()))
                .findFirst().orElse(null);
    }

    public void approveDoctor(String id) {
        List<Doctor> doctors = getAll();
        for (Doctor d : doctors) {
            if (d.getId().equals(id)) d.setStatus("Approved");
        }
        writeAll(doctors);
    }

    public void rejectDoctor(String id) {
        List<Doctor> doctors = getAll();
        for (Doctor d : doctors) {
            if (d.getId().equals(id)) d.setStatus("Rejected");
        }
        writeAll(doctors);
    }

    public List<Doctor> getPendingDoctors() {
        return getAll().stream()
                .filter(d -> "Pending".equals(d.getStatus()))
                .toList();
    }


    public List<Doctor> getApprovedDoctors() {
        return getAll().stream()
                .filter(d -> "Approved".equals(d.getStatus()))
                .toList();
    }

    public List<Doctor> getAllDoctors() { return getAll(); }
    public void saveDoctor(Doctor d) { save(d); }
    public void updateDoctor(String id, Doctor d) { update(id, d); }
    public void deleteDoctor(String id) { delete(id); }
    public Doctor getDoctorById(String id) { return getById(id); }

    private void writeAll(List<Doctor> doctors) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Doctor d : doctors) {
                bw.write(d.getId() + "," + d.getName() + "," + d.getPhone() + "," +
                        d.getSpecialization() + "," + d.getAvailability() + "," +
                        d.getPassword() + "," + d.getStatus());
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}