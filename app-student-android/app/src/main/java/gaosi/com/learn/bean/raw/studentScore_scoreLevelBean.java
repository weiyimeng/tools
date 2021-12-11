package gaosi.com.learn.bean.raw;

import com.gsbaselib.base.bean.BaseData;

/**
 * Created by mrz on 18/4/25.
 */
public class studentScore_scoreLevelBean extends BaseData {

    private String id;
    private String level;
    private String score;
    private String studentScore;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScore() {
        return score;
    }

    public void setStudentScore(String studentScore) {
        this.studentScore = studentScore;
    }

    public String getStudentScore() {
        return studentScore;
    }

}
