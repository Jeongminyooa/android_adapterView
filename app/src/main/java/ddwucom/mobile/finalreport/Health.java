package ddwucom.mobile.finalreport;

import java.io.Serializable;

public class Health implements Serializable {
    private long _id;
    private String sex;
    private String weight;
    private String height;
    private String age;
    private String date;
    private String exercise_intensity;
    private String calorie;
    private int picture;
    private boolean isChecked;

    //constructor
    public Health(long _id, String sex, String weight, String height, String age, String date, String exercise_intensity, int picture) {
        this._id = _id;
        this.sex = sex;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.date = date;
        this.exercise_intensity = exercise_intensity;
        this.picture = picture;
        this.calorie = calculateCalorie();
        this.isChecked = false;
    }

    //constructor
    public Health(String sex, String weight, String height, String age, String date, String exercise_intensity, int picture) {
        this.sex = sex;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.date = date;
        this.exercise_intensity = exercise_intensity;
        this.picture = picture;
        this.calorie = calculateCalorie();
        this.isChecked = false;
    }

    //getter
    public long get_id() { return _id; }

    public String getSex() { return sex; }

    public String getWeight() { return weight; }

    public String getHeight() { return height; }

    public String getAge() { return age; }

    public String getDate() { return date; }

    public String getExercise_intensity() { return exercise_intensity; }

    public String getCalorie() { return calorie; }

    public int getPicture() { return picture; }

    public boolean getIsChecked() { return isChecked; }

    //setter

    public void set_id(long _id) {
        this._id = _id;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setExercise_intensity(String exercise_intensity) {
        this.exercise_intensity = exercise_intensity;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public void setIsChecked(boolean check) { this.isChecked = check; }

    // 몸무게, 키, 나이, 운동 강도를 통해 적절한 칼로리를 계산한다.
    public String calculateCalorie() {
        String calorie_rlt = "";
        double basal_metabolism = 0; // 기초대사량을 계산
        int parseIntAge = Integer.parseInt(age);
        double parseWeight = Double.parseDouble(weight);
        double parseHeight = Double.parseDouble(height);

        //기초 대사량을 계산
        if(sex.equals("여자")) {
            basal_metabolism = 665.1 + (9.56 * parseWeight) + (1.85 * parseHeight) - (4.68 * parseIntAge);
        } else if(sex.equals("남자")){
            basal_metabolism = 66.47 + (13.75 * parseWeight) + (5 * parseHeight) - (6.76 * parseIntAge);
        }

        //일일 칼로리 계산법 : 기초 대사량 + 활동 대사량
        switch(exercise_intensity) {
            case "1": //활동이 적거나 운동을 안함
                calorie_rlt = String.valueOf( (int)(basal_metabolism + (basal_metabolism * 0.2)));
                break;
            case "2": //가벼운 조깅 강도
                calorie_rlt = String.valueOf((int)(basal_metabolism + (basal_metabolism * 0.375)));
                break;
            case "3" : //보통  강도
                calorie_rlt = String.valueOf((int)(basal_metabolism + (basal_metabolism * 0.555)));
                break;
            case "4": // 땀이 날 강도
                calorie_rlt = String.valueOf((int)(basal_metabolism + (basal_metabolism * 0.725)));
                break;
            case "5": // 운동선수 훈련 강도
                calorie_rlt = String.valueOf((int)(basal_metabolism + (basal_metabolism * 0.9)));
                break;
        }
        return calorie_rlt;
    }
}
