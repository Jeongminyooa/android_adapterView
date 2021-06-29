package ddwucom.mobile.finalreport;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {
    HealthDBManager dbManager;
    Health updateHealth;

    RadioGroup sexGroup, exerciseGroup;
    RadioButton ex1, ex2, ex3, ex4, ex5, woman, man;
    EditText etHeight, etWeight, etAge;
    DatePicker selectDate;
    TextView showDate;
    ImageView ivPicture;

    String sex, exercise;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        dbManager = new HealthDBManager(this);

        Intent intent = getIntent();
        updateHealth = (Health) intent.getSerializableExtra("health");
        dbManager = new HealthDBManager(this);

        sexGroup = findViewById(R.id.updateGroupSex);
        woman = findViewById(R.id.updateWoman);
        man = findViewById(R.id.updateMan);
        etHeight = findViewById(R.id.updateHeight);
        etWeight = findViewById(R.id.updateWeight);
        etAge = findViewById(R.id.updateAge);
        showDate = findViewById(R.id.updateDate);
        selectDate = findViewById(R.id.updateDatePicker);
        ex1 = findViewById(R.id.addRadio1);
        ex2 = findViewById(R.id.addRadio2);
        ex3 = findViewById(R.id.addRadio3);
        ex4 = findViewById(R.id.addRadio4);
        ex5 = findViewById(R.id.addRadio5);
        exerciseGroup = findViewById(R.id.updateExercise);
        ivPicture = findViewById(R.id.updateCimg);

        radioCheck();
        etHeight.setText(updateHealth.getHeight());
        etWeight.setText(updateHealth.getWeight());
        etAge.setText(updateHealth.getAge());
        showDate.setText(updateHealth.getDate());
        ivPicture.setImageResource(updateHealth.getPicture());

        // 사진을 누르면 크게 볼 수 있음.
        ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ConstraintLayout showLayout =
                        (ConstraintLayout)View.inflate(UpdateActivity.this, R.layout.picture_dialog, null);

                ImageView showPicture = showLayout.findViewById(R.id.showPicture);
                showPicture.setImageResource(updateHealth.getPicture());

                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);

                builder.setView(showLayout)
                        .setTitle("사진 확인하기")
                        .setPositiveButton("닫기", null)
                        .show();

            }
        });
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
                if(checkedId == R.id.updateWoman) {
                    sex = "여자";
                }
                else if(checkedId == R.id.updateMan){
                    sex = "남자";
                }
            }
        });

        //어떤 운동이 선택되었는지를 확인해 저장
        exerciseGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.addRadio1) {  exercise = "1";
                } else if(checkedId == R.id.addRadio2) { exercise = "2";
                } else if(checkedId == R.id.addRadio3) { exercise = "3";
                } else if(checkedId == R.id.addRadio4) { exercise = "4";
                } else if(checkedId == R.id.addRadio5) { exercise = "5"; }
            }

        });

    }

    //항목 정보에 해당하는 radio 버튼을 check해둠.
    public void radioCheck() {
        switch(updateHealth.getSex()) {
            case "여자":
                sex = "여자";
                woman.setChecked(true);
                break;
            case "남자":
                sex = "남자";
                man.setChecked(true);
                break;
        }

        switch(updateHealth.getExercise_intensity()) {
            case "1":
                exercise = "1";
                ex1.setChecked(true);
                break;
            case "2":
                exercise = "2";
                ex2.setChecked(true);
                break;
            case "3":
                exercise = "3";
                ex3.setChecked(true);
                break;
            case "4":
                exercise = "4";
                ex4.setChecked(true);
                break;
            case "5":
                exercise = "5";
                ex5.setChecked(true);
                break;

        }
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.updateBtn:
                if(!isCheckedEmpty())
                    break;

                //라디오 버튼은 멤버 변수를 이용하므로 수정된 값이 있을때만 set을 해준다. 그렇지 않으면 null exception
                if(sex != null)
                    updateHealth.setSex(sex);
                if(exercise != null)
                    updateHealth.setExercise_intensity(exercise);

                updateHealth.setAge(etAge.getText().toString());
                updateHealth.setHeight(etHeight.getText().toString());
                updateHealth.setWeight(etWeight.getText().toString());
                updateHealth.setDate((String)showDate.getText());
                updateHealth.setPicture(updateHealth.getPicture());
                updateHealth.setCalorie(updateHealth.calculateCalorie());

                boolean result = dbManager.modifyHealth(updateHealth);

                if(result) {
                    Intent rltIntent = new Intent();
                    rltIntent.putExtra("health", updateHealth);
                    setResult(RESULT_OK, rltIntent);
                }else {
                    setResult(RESULT_CANCELED);
                }
                finish();
                break;
            case R.id.updateCancel:
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