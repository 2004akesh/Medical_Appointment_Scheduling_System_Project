package com.healthcare.healthcaremanagement.admin;

import com.healthcare.healthcaremanagement.ManagementService;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;

@Service
public class AdminService implements ManagementService<Admin> {

    private static final String FILE_PATH = "src/main/resources/data/admins.txt";

    @Override
    public List<Admin> getAll() {
        List<Admin> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",");
                if (p.length == 6) {
                    list.add(new Admin(p[0], p[1], p[2], p[3], p[4], p[5]));
                }
            }
        } catch (IOException e) {}
        return list;
    }

    @Override
    public void save(Admin a) {
        List<Admin> list = getAll();
        a.setId(UUID.randomUUID().toString().substring(0, 8));
        list.add(a);
        writeAll(list);
    }

    @Override
    public void update(String id, Admin updated) {
        List<Admin> list = getAll();
        for (Admin a : list) {
            if (a.getId().equals(id)) {
                a.setName(updated.getName());
                a.setPhone(updated.getPhone());
                a.setUsername(updated.getUsername());
                a.setPassword(updated.getPassword());
                a.setRole(updated.getRole());
            }
        }
        writeAll(list);
    }

    @Override
    public void delete(String id) {
        List<Admin> list = getAll();
        list.removeIf(a -> a.getId().equals(id));
        writeAll(list);
    }

    @Override
    public Admin getById(String id) {
        return getAll().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst().orElse(null);
    }

    public Admin findByUsername(String username) {
        return getAll().stream()
                .filter(a -> a.getUsername().equals(username))
                .findFirst().orElse(null);
    }

    public List<Admin> getAllAdmins() { return getAll(); }
    public void saveAdmin(Admin a) { save(a); }
    public void updateAdmin(String id, Admin a) { update(id, a); }
    public void deleteAdmin(String id) { delete(id); }
    public Admin getAdminById(String id) { return getById(id); }

    private void writeAll(List<Admin> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Admin a : list) {
                bw.write(a.getId() + "," + a.getName() + "," + a.getPhone() + "," +
                        a.getUsername() + "," + a.getPassword() + "," + a.getRole());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}