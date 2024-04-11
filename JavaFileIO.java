import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.Table;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

class Course{
    private Integer courseId;
    private String courseTitle;
    private String courseStartedDate;
    private String courseEndedDate;
    private Boolean isAvailable;
    public Course() {
        this(0, "", "", "", false);
    }

    public Course(Integer courseId, String courseTitle, String courseStartedDate, String courseEndedDate, Boolean isAvailable) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseStartedDate = courseStartedDate;
        this.courseEndedDate = courseEndedDate;
        this.isAvailable = isAvailable;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseStartedDate() {
        return courseStartedDate;
    }

    public void setCourseStartedDate(String courseStartedDate) {
        this.courseStartedDate = courseStartedDate;
    }

    public String getCourseEndedDate() {
        return courseEndedDate;
    }

    public void setCourseEndedDate(String courseEndedDate) {
        this.courseEndedDate = courseEndedDate;
    }

    public boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }
}

class CourseServiceImp implements CourseService {
    private static final int MIN_ID = 10;
    private static final int MAX_ID = 99;
    @Override
    public void addNewCourse(String courseTitle) {

        Integer courseId = generateCourseId();
        String courseStartedDate = getCurrentDate();
        String courseEndedDate = getCurrentEndDate();
        Boolean isAvailable = true;

        Course course = new Course(courseId, courseTitle, courseStartedDate, courseEndedDate, isAvailable);

        try{
            FileWriter fileWriter = new FileWriter("course.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);

            printWriter.println(
                    course.getCourseId() + ", " +
                            course.getCourseTitle() + ", " +
                            course.getCourseStartedDate() + ", " +
                            course.getCourseEndedDate() + ", " +
                            course.getAvailable()
            );

            printWriter.close();
            bufferedWriter.close();
            fileWriter.close();
            System.out.println("New course added successfully.");
        }catch (IOException e) {
            System.out.println("An error occurred while adding the course.");
            e.printStackTrace();
        }
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();

        try{
            FileReader fileReader = new FileReader("course.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split(",");
                Integer courseId = Integer.parseInt(split[0]);
                String courseTitle = split[1];
                String courseStartedDate = split[2];
                String courseEndedDate = split[3];
                Boolean isAvailable = Boolean.parseBoolean(split[4].trim());

                Course course = new Course(courseId, courseTitle, courseStartedDate, courseEndedDate, isAvailable?true:false);
                courses.add(course);
            }

            bufferedReader.close();
            fileReader.close();
        }catch (IOException e){
            System.out.println("An error occurred while reading the courses.");
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public Course getCourseById(Integer courseId) {
        try {
            FileReader fileReader = new FileReader("course.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null){
                String[] data = line.split(",");
                Integer id = Integer.parseInt(data[0]);

                if (id.equals(courseId)){
                    String courseTitle = data[1];
                    String courseStartedDate = data[2];
                    String courseEndedDate = data[3];
                    Boolean isAvailable = Boolean.parseBoolean(data[4].trim());

                    Course course = new Course(id, courseTitle, courseStartedDate, courseEndedDate, isAvailable?true:false);
                    bufferedReader.close();
                    fileReader.close();
                    return course;
                }
            }
            bufferedReader.close();
            fileReader.close();
        }catch (IOException e){
            System.out.println("An error occurred while searching for the course.");
            e.printStackTrace();
        }
        System.out.println("Course not found.");
        return null;
    }

    //for auto id +1
//    private Integer generateCourseId(){
//        List<Course> courses = getAllCourses();
//        int highestId = 0;
//        for (Course course : courses) {
//            highestId = Math.max(highestId, course.getCourseId());
//        }
//        return highestId + 1;
//    }

    private Integer generateCourseId() {
        Random random = new Random();
        int newId;
        do {
            newId = random.nextInt(MAX_ID - MIN_ID + 1) + MIN_ID;
        } while (isCourseIdExists(newId));
        return newId;
    }

    private boolean isCourseIdExists(int newId) {
        return false;
    }

    private String getCurrentDate(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
    private String getCurrentEndDate(){
        Date endDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("2024-12-31");
        return formatter.format(endDate);
    }
}

class ViewModel{
    public static void menu(CourseService courseService){
        Scanner scanner = new Scanner(System.in);

        while (true){
            System.out.println("---------- Menu ----------");
            System.out.println("[1]. Add New Course");
            System.out.println("[2]. List All Courses");
            System.out.println("[3]. Get Search Course by ID");
            System.out.println("[4]. Exit");
            System.out.println("--------------------------");
            System.out.print("[+]Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice){
                case 1->{
                    scanner.nextLine();
                    System.out.print("[+]Enter the course title: ");
                    String courseTitle = scanner.nextLine();
                    courseService.addNewCourse(courseTitle);
                    break;
                }
                case 2->{
                    System.out.println("--------- All Courses ---------");
                    printCoursesTable(courseService.getAllCourses());
                    break;
                }
                case 3->{
                    System.out.print("[+]Enter the course ID: ");
                    int courseId = scanner.nextInt();
                    Course course = courseService.getCourseById(courseId);
                    if (course != null) {
                        System.out.println("--------- Course ---------");
                        printCoursesTable(course);
                    }
                    break;
                }
                case 4->{
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                }
                default->{
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }
    private static void printCoursesTable(Course course){
        Table table = new Table(5, BorderStyle.HEAVY);
        table.setColumnWidth(4,20,40);

        table.addCell(" Course ID ",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(" Course Title ",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(" Course Started Date ",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(" Course Ended Date ",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Course Available",new CellStyle(CellStyle.HorizontalAlign.CENTER));

        table.addCell(String.valueOf(course.getCourseId()), new CellStyle(CellStyle.HorizontalAlign.CENTER),0);
        table.addCell(course.getCourseTitle(), new CellStyle(CellStyle.HorizontalAlign.CENTER),1);
        table.addCell(course.getCourseStartedDate(), new CellStyle(CellStyle.HorizontalAlign.CENTER),1);
        table.addCell(course.getCourseEndedDate(), new CellStyle(CellStyle.HorizontalAlign.CENTER),1);
        table.addCell(String.valueOf(course.getAvailable()), new CellStyle(CellStyle.HorizontalAlign.CENTER),1);

        System.out.println(table.render());
    }
    private static void printCoursesTable(List<Course> courses){
        Table table = new Table(5, BorderStyle.HEAVY);
        table.setColumnWidth(4,20,40);

        table.addCell(" Course ID ",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(" Course Title ",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(" Course Started Date ",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(" Course Ended Date ",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Course Available",new CellStyle(CellStyle.HorizontalAlign.CENTER));

        for(Course course : courses){
            table.addCell(String.valueOf(course.getCourseId()), new CellStyle(CellStyle.HorizontalAlign.CENTER),0);
            table.addCell(course.getCourseTitle(), new CellStyle(CellStyle.HorizontalAlign.CENTER),1);
            table.addCell(course.getCourseStartedDate(), new CellStyle(CellStyle.HorizontalAlign.CENTER),1);
            table.addCell(course.getCourseEndedDate(), new CellStyle(CellStyle.HorizontalAlign.CENTER),1);
            table.addCell(String.valueOf(course.getAvailable()), new CellStyle(CellStyle.HorizontalAlign.CENTER),1);
        }
        System.out.println(table.render());
    }
}

public class JavaFileIO {
    public static void main(String[] args){
        CourseService courseService = new CourseServiceImp();
        ViewModel.menu(courseService);
    }
}
