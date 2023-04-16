package database;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import user.Student;
public class DatabaseStudent {
    private static HashMap<String, Student> studentData = new HashMap<>();

    public static Student getStudent(String userid) {
        return studentData.get(userid);
    }

    public static void addStudent(Student student) {
        studentData.put(student.getUserID(), student);
    }

    public static Student getStudentByName(String name) {
        for (Student student : studentData.values()) {
            if (student.getName().compareTo(name) == 0) {
                return student;
            }
        }
        return null;
    }

    public static void printStudentData() {
        int index = 1;
        System.out.println("Student list:");
        for (Student s : studentData.values()) {
            System.out.println(index + ". Name: " + s.getName() + ", User ID: " +  s.getUserID() + ", Email: " + s.getEmail());
            index++;
        }
        System.out.println();
    }

    public static void readStudentCSV(String loc) {
        String line = "";
        String splitBy = ",";
        boolean isFirstLine = true;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                DatabaseStudent.class.getResourceAsStream(loc)))) {
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] readLine = line.split(splitBy, -1);
                String userid = readLine[0].trim();
                String email = readLine[1].trim();
                String name = readLine[2].trim();
                String status = readLine[3].trim();
                Integer projectid = Integer.parseInt(readLine[4].trim());
                String password = readLine[5].trim();
                Student student = new Student(userid, email, name, password, Student.StudentStatus.valueOf(status), projectid);
                addStudent(student);

            }
        } catch (IOException e) {
            System.out.println("Could not read file");
            e.printStackTrace();
        }
    }
    
    public static void updateStudentCSV(String loc){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(DatabaseStudent.class.getResource(loc).getFile()));
            writer.write("userID,Email,Name,Status,projectID,password");
            writer.newLine();
            for (Student s : studentData.values()) {
                String line = s.getUserID();
                line += "," + s.getEmail();
                line += "," + s.getName();
                line += "," + s.getStudentStatus();
                line += "," + s.getProjectID();
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