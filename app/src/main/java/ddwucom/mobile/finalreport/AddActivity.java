package ddwucom.mobile.finalreport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    RadioGroup sexGroup;
    EditText etHeight;
    EditText etWeight;
    EditText etAge;
    DatePicker selectDate;
    TextView showDate;
    RadioGroup exerciseGroup;
    ImageView ivPicture;

    HealthDBManager dbManager;

    String sex;
    String exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        sexGroup = findViewById(R.id.addGroupSex);
        etHeight = findViewById(R.id.addHeight);
        etWeight = findViewById(R.id.addWeight);
        etAge = findViewById(R.id.addAge);
        showDate = findViewById(R.id.addDate);
        selectDate = findViewById(R.id.addDatePicker);
        exerciseGroup = findViewById(R.id.addExercise);
        ivPicture = findViewById(R.id.addCimg);

        //DatePicker을 오늘 날짜로 초기화, 리스너 등록
        selectDate.init(selectDate.getYear(), selectDate.getMonth(), selectDate.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //monthOfYear은 0부터 값을 가지므로 +1을 해준다.
                        showDate.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                    }
                });

        //어떤 성별이 골라졌는지를 확인해서 저장
        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.addWoman) {
                    sex = "여자";
                }
                else if(checkedId == R.id.addMan){
                    sex = "남자";
                }
            }
        });

        //어떤 운동이 선택되었는지를 확인해 저장
        exerciseGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.addRadio1) {
                    exercise = "1";
                } else if(checkedId == R.id.addRadio2) {
                    exercise = "2";
                }
                else if(checkedId == R.id.addRadio3) {
                    exercise = "3";
                }
                else if(checkedId == R.id.addRadio4) {
                    exercise = "4";
                }
                else if(checkedId == R.id.addRadio5) {
                    exercise = "5";
                }
            }
        });

        dbManager = new HealthDBManager(this);

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.addBtn:
                if(!isCheckedEmpty())
                    break;
                boolean result = dbManager.addNewHealth(
                        new Health(sex, etWeight.getText().toString(), etHeight.getText().toString(), etAge.getText().toString(),
                                (String)showDate.getText(), exercise, R.mipmap.addsample));

                if (result) {    // 정상수행에 따른 처리
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("date", (String)showDate.getText());
                    setResult(RESULT_OK, resultIntent);
                } else {        // 이상에 따른 처리
                    Toast.makeText(this, "새로운 기록 추가 실패!", Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
            case R.id.addCancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    //필수 항목이 입력되지 않은 경우, 토스트
    public boolean isCheckedEmpty() {
        String empty_view;

        if(sex == null) empty_view = "성별";
        else if(etHeight.getText().toString().equals("")) empty_view = "키";
        else if(etWeight.getText().toString().equals("")) empty_view = "체중";
        else if(etAge.getText().toString().equals("")) empty_view = "나이";
        else if(showDate.getText().toString().equals("")) empty_view = "날짜";
        else if(exercise == null) empty_view = "운동";
        else return true;

        Toast.makeText(this,  empty_view + "은(는) 반드시 입력해야 합니다.",Toast.LENGTH_SHORT).show();
        return false;
    }
}
