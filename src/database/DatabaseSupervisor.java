package database;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import user.FYPCoordinator;
import user.Supervisor;

public class DatabaseSupervisor {
    private static HashMap<String, Supervisor> supervisorData = new HashMap<>();
    private static FYPCoordinator fypCoordinator;

    public static Supervisor getSupervisor(String userid) {
        return supervisorData.get(userid);
    }

    public static Supervisor getSupervisorByName(String name) {
        for (Supervisor s : supervisorData.values()) {
            if (s.getName().compareTo(name) == 0) {
                return s;
            }
        }
        return null;
    }

    public static FYPCoordinator getFYPCoordinator() {
        return DatabaseSupervisor.fypCoordinator;
    }

    public static void addSupervisor(Supervisor supervisor) {
        supervisorData.put(supervisor.getUserID(), supervisor);
    }

    public static void addFYPCoordinator(FYPCoordinator fypCoordinator) {
        DatabaseSupervisor.fypCoordinator = fypCoordinator;
    }

    public static void printSupervisorData() {
        int index = 1;
        System.out.println("Supervisor list:");
        for (Supervisor s : supervisorData.values()) {
            System.out.println(index + ". Name: " + s.getName() + ", User ID: " +  s.getUserID() + ", Email: " + s.getEmail());
            index++;
        }
        System.out.println();
    }

    public static void readSupervisorCSV(String loc) {
        String line = "";
        String splitBy = ",";
        boolean isFirstLine = true;
        boolean isSecondLine = true;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
            DatabaseStudent.class.getResourceAsStream(loc)))) {
            while ((line = br.readLine()) != null)
            {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                String[] readLine = line.split(splitBy, -1);
                String userid = readLine[0].trim();
                String email = readLine[1].trim();
                String name = readLine[2].trim();
                String password = readLine[3].trim();
                Supervisor supervisor = new Supervisor(userid, email, name, password);
                
                if (isSecondLine) {
                    FYPCoordinator fypCoordinator = new FYPCoordinator(userid, email, name, password);
                    DatabaseSupervisor.addFYPCoordinator(fypCoordinator);
                    isSecondLine = false;
                    continue;
                }
                else 
                    addSupervisor(supervisor);
            }
        } catch (IOException e) {
            System.out.println("Could not read file");
            e.printStackTrace();
        }
    }

    public static void updateSupervisorCSV(String loc){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(DatabaseStudent.class.getResource(loc).getFile()));
            writer.write("userID,Email,Name,password");
            writer.newLine();

            String line = fypCoordinator.getUserID();
            line = fypCoordinator.getUserID();
            line += "," + fypCoordinator.getEmail();
            line += "," + fypCoordinator.getName();
            line += "," + fypCoordinator.getPassword();
            writer.write(line);
            writer.newLine();

            for (Supervisor s : supervisorData.values()) {
                line = s.getUserID();
                line += "," + s.getEmail();
                line += "," + s.getName();
                line += "," + s.getPassword();
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not write file");
            e.printStackTrace();
        }
    }
}
