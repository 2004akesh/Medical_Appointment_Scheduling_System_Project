package com.healthcare.healthcaremanagement.patient;

import com.healthcare.healthcaremanagement.ManagementService;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;

@Service
public class PatientService implements ManagementService<Patient> {

    private static final String FILE_PATH = "src/main/resources/data/patients.txt";

    @Override
    public List<Patient> getAll() {
        List<Patient> patients = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 7) {
                    patients.add(new Patient(p[0], p[1], Integer.parseInt(p[2]),
                            p[3], p[4], p[5], p[6]));
                }
            }
        } catch (IOException e) {}
        return patients;
    }

    @Override
    public void save(Patient patient) {
        List<Patient> patients = getAll();
        patient.setId(UUID.randomUUID().toString().substring(0, 8));
        patients.add(patient);
        writeAll(patients);
    }

    @Override
    public void update(String id, Patient updated) {
        List<Patient> patients = getAll();
        for (Patient p : patients) {
            if (p.getId().equals(id)) {
                p.setName(updated.getName());
                p.setAge(updated.getAge());
                p.setGender(updated.getGender());
                p.setPhone(updated.getPhone());
                p.setPassword(updated.getPassword());
                p.setStatus(updated.getStatus());
            }
        }
        writeAll(patients);
    }

    @Override
    public void delete(String id) {
        List<Patient> patients = getAll();
        patients.removeIf(p -> p.getId().equals(id));
        writeAll(patients);
    }

    @Override
    public Patient getById(String id) {
        return getAll().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst().orElse(null);
    }

    // Only approved patients can login
    public Patient findByPhoneAndPassword(String phone, String password) {
        return getAll().stream()
                .filter(p -> p.getPhone().equals(phone)
                        && p.getPassword().equals(password)
                        && "Approved".equals(p.getStatus()))
                .findFirst().orElse(null);
    }

    public void approvePatient(String id) {
        List<Patient> patients = getAll();
        for (Patient p : patients) {
            if (p.getId().equals(id)) p.setStatus("Approved");
        }
        writeAll(patients);
    }

    public void rejectPatient(String id) {
        List<Patient> patients = getAll();
        for (Patient p : patients) {
            if (p.getId().equals(id)) p.setStatus("Rejected");
        }
        writeAll(patients);
    }

    public List<Patient> getPendingPatients() {
        return getAll().stream()
                .filter(p -> "Pending".equals(p.getStatus()))
                .toList();
    }
    public List<Patient> getApprovedPatients() {
        return getAll().stream()
                .filter(p -> p.getStatus().equals("Approved"))
                .toList();
    }

    public List<Patient> getAllPatients() { return getAll(); }
    public void savePatient(Patient p) { save(p); }
    public void updatePatient(String id, Patient p) { update(id, p); }
    public void deletePatient(String id) { delete(id); }
    public Patient getPatientById(String id) { return getById(id); }

    private void writeAll(List<Patient> patients) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Patient p : patients) {
                bw.write(p.getId() + "," + p.getName() + "," + p.getAge() + "," +
                        p.getGender() + "," + p.getPhone() + "," +
                        p.getPassword() + "," + p.getStatus());
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}