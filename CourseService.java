import java.util.List;

interface CourseService {
    void addNewCourse(String courseTitle);
    List<Course> getAllCourses();
    Course getCourseById(Integer courseId);
}
