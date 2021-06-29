package ddwucom.mobile.finalreport;

//과제명 : 운동 기록 관리 앱
//분반 : 02분반
//학번 : 20190980 성명 : 유정민
//제출일 : 2021년 6월 24일

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    HealthDBHelper dbHelper;
    ArrayList<Health> healthList;
    MyAdapter adapter;
    ListView listView;
    HealthDBManager healthDBManager;

    //백그라운드와 글씨색 바꾸는 정도로만 다크모드 구현
    Switch transWB;
    static public String transPoint = "WHITE";
    ConstraintLayout main;
    TextView tvTitle;
    Button btnPartremove;


    // DatePicker에서 현재 시간을 표시하기 위함.
    Calendar c = Calendar.getInstance();
    int nYear = c.get(Calendar.YEAR);
    int nMon = c.get(Calendar.MONTH);
    int nDay = c.get(Calendar.DAY_OF_MONTH);

    final int ADD_CODE = 100;
    final int UPDATE_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        healthDBManager = new HealthDBManager(this);
        dbHelper = new HealthDBHelper(this);
        healthList = new ArrayList<Health>();

        transWB = findViewById(R.id.transDark);
        main = findViewById(R.id.main_activity);
        tvTitle = findViewById(R.id.textView);
        btnPartremove = findViewById(R.id.btnPartremove);

        listView = (ListView)findViewById(R.id.listView);
        adapter = new MyAdapter(this, R.layout.custom_layout, healthList);
        listView.setAdapter(adapter);

        //항목 수정
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Health health = healthList.get(position);

                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                intent.putExtra("health", health);
                startActivityForResult(intent, UPDATE_CODE);
            }
        });

        //항목 삭제
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.dialog_title)
                        .setMessage(healthList.get(position).getDate() + " 기록을 삭제하시겠습니까?\n (우리의 추억이 모두 삭제됩니다.)")
                        .setPositiveButton(R.string.dialog_remove, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean rlt = healthDBManager.removehealth(healthList.get(position).get_id());
                                if(rlt) {
                                    Toast.makeText(MainActivity.this, "삭제 완료", Toast.LENGTH_SHORT).show();
                                    healthList.clear();
                                    healthList.addAll(healthDBManager.getAllFood());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(MainActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.dialog_cancel, null)
                        .setCancelable(false) //다른 부분 눌러도 다이얼로그가 닫히지 않음
                        .show();
                return true;
            }
        });

        // 스위치 -> 다크모드 활성화
        transWB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(transWB.isChecked()) {
                    transPoint = "BLACK";
                    main.setBackgroundColor(Color.BLACK);
                    tvTitle.setTextColor(Color.WHITE);
                    listView.setBackgroundColor(Color.BLACK);
                    btnPartremove.setBackgroundColor(0xFF00293C);
                    btnPartremove.setTextColor(Color.WHITE);
                }
                else {
                    transPoint = "WHITE";
                    main.setBackgroundColor(Color.WHITE);
                    tvTitle.setTextColor(Color.BLACK);
                    listView.setBackgroundColor(Color.WHITE);
                    btnPartremove.setBackgroundColor(0xFFDAEDF6);
                    btnPartremove.setTextColor(Color.BLACK);
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        healthList.clear();
        healthList.addAll(healthDBManager.getAllFood());

        //계산된 칼로리를 DB에 저장
        for(int i = 0; i < healthList.size(); i++) {
            healthDBManager.modifyCalorie(healthList.get(i));
        }

        adapter.notifyDataSetChanged();
    }

    //부분 삭제 구현
    public void onClickRemove(View v) {
        switch(v.getId()) {
            case R.id.btnPartremove:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("선택한 항목을 삭제합니까?")
                        .setMessage("확인 버튼을 누르면 선택 항목이 삭제됩니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(int i = 0; i < healthList.size(); i++) {
                                    if(healthList.get(i).getIsChecked()) {
                                        boolean rlt = healthDBManager.removehealth(healthList.get(i).get_id());
                                    }
                                }
                                healthList.clear();
                                healthList.addAll(healthDBManager.getAllFood());
                                adapter.notifyDataSetChanged();

                                Toast.makeText(MainActivity.this, "삭제 완료!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(true)
                        .show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(item.getItemId()) {
            case R.id.itemAdd: //아이템 추가 액티비티
                Intent intent = new Intent(this, AddActivity.class);
                startActivityForResult(intent, ADD_CODE);
                break;
            case R.id.itemIntroduce:
                final ConstraintLayout introduceLayout =
                        (ConstraintLayout)View.inflate(this, R.layout.introduce_layout, null);

                builder = new AlertDialog.Builder(this);
                builder.setView(introduceLayout)
                        .setPositiveButton("수고했어요", null)
                        .setCancelable(false)
                        .show();

                break;
            case R.id.itemSearch:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //날짜를 선택 후 확인 버튼을 누른다면 . .
                        String date = year + "-" + (month+1) + "-" + dayOfMonth;

                        Health searchHealth = healthDBManager.getHealthByDate(date);

                        //date에 해당하는 기록이 없다면 add
                        if(searchHealth == null) {
                            Toast.makeText(MainActivity.this, "해당 날짜에는 기록이 없습니다. 추가하십시오.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, AddActivity.class);
                            startActivityForResult(intent, ADD_CODE);
                        } else {
                            //date에 해당하는 기록이 있다면 update
                            Toast.makeText(MainActivity.this, "해당 날짜에는 기록을 가져왔습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                            intent.putExtra("health", searchHealth);
                            startActivityForResult(intent, UPDATE_CODE);
                        }

                    }
                },nYear, nMon, nDay);
                datePickerDialog.setMessage("검색을 원하는 날짜를 선택하세요.");
                datePickerDialog.show();
                break;
            case R.id.itemFinish:
                builder.setTitle("앱 종료")
                        .setMessage("앱을 종료하시겠습니까?")
                        .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();
                break;
        }
        return true;
    }

    // item onClick -> youtube로 운동 영상
    public void onItemClick(MenuItem item) {
        String uri; //해당 유튜브 사이트를 저장
        String kindName; //어떤 영상인지

        switch(item.getItemId()) {
            case R.id.youtube1:
                uri = "https://www.youtube.com/watch?v=QqqZH3j_vH0";
                kindName = item.getTitle().toString();
                break;
            case R.id.youtube2:
                uri = "https://www.youtube.com/watch?v=NDsjmxTROEo";
                kindName = item.getTitle().toString();
                break;
            case R.id.youtube3:
                uri = "https://www.youtube.com/watch?v=PjGcOP-TQPE";
                kindName = item.getTitle().toString();
                break;
            case R.id.youtube4:
                uri = "https://www.youtube.com/watch?v=BEm70PkkVPs";
                kindName = item.getTitle().toString();
                break;
            case R.id.youtube5:
                uri = "https://www.youtube.com/watch?v=CTojmKLkWTo&t=2s";
                kindName = item.getTitle().toString();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("유튜브에 접속합니다.")
                .setMessage(kindName + "을 시청하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW)
                                .setData(Uri.parse(uri))
                                .setPackage("com.google.android.youtube"));
                    }
                })
                .setNegativeButton("취소", null)
                .setCancelable(false)
                .show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_CODE:
                if(resultCode == RESULT_OK) {

                    String date = data.getStringExtra("date");

                    Toast.makeText(this, date + " 기록 완료!\n 오늘도 다이어트 수고했어요 :)", Toast.LENGTH_SHORT).show();
                }
                else if(resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "기록 추가 취소", Toast.LENGTH_SHORT).show();
                }
                break;
            case UPDATE_CODE:
                if(resultCode == RESULT_OK) {
                    Health health = (Health) data.getSerializableExtra("health");
                    Toast.makeText(this, health.getDate() + " 수정 완료!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "수정 취소", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}