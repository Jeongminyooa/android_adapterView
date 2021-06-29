package ddwucom.mobile.finalreport;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Health> dataList;
    private LayoutInflater layoutInflater;

    public MyAdapter(Context context, int layout, ArrayList<Health> dataList) {
        this.context = context;
        this.layout = layout;
        this.dataList = dataList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dataList.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        ViewHodler viewHolder;

        if(convertView == null) {
            convertView = layoutInflater.inflate(layout, parent, false);

            viewHolder = new ViewHodler();
            viewHolder.tvPicture = convertView.findViewById(R.id.tvPicture);
            viewHolder.tvDate = convertView.findViewById(R.id.tvDate);
            viewHolder.tvWeight = convertView.findViewById(R.id.tvWeight);
            viewHolder.tvHeight = convertView.findViewById(R.id.tvHeight);
            viewHolder.tvCalorie = convertView.findViewById(R.id.tvCalorie);
            viewHolder.checkBox = convertView.findViewById(R.id.checkBox);
            viewHolder.checkBox.setFocusable(false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }
        // 항목마다 백그라운드 색을 바꿔줌. (다크모드 고려)
        changeColor(pos, convertView, viewHolder);

        // 부분 삭제를 완료한다면 check를 해제
        viewHolder.checkBox.setChecked(false);


        viewHolder.tvPicture.setImageResource(dataList.get(position).getPicture());
        viewHolder.tvDate.setText(dataList.get(position).getDate());
        viewHolder.tvWeight.setText(dataList.get(position).getWeight());
        viewHolder.tvHeight.setText(dataList.get(position).getHeight());
        viewHolder.tvDate.setText(dataList.get(position).getDate());
        viewHolder.tvCalorie.setText(dataList.get(position).getCalorie());
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    dataList.get(position).setIsChecked(true);
                else
                    dataList.get(position).setIsChecked(false);
            }
        });


        return convertView;
    }

    static class ViewHodler {
        ImageView tvPicture;
        TextView tvDate;
        TextView tvWeight;
        TextView tvHeight;
        TextView tvCalorie;
        CheckBox checkBox;
    }

    public void changeColor(int pos, View v, ViewHodler viewHodler) {
        TextView tvH = v.findViewById(R.id.textView5);
        TextView tvW = v.findViewById(R.id.textView6);
        TextView tvC = v.findViewById(R.id.textView7);

        //다크모드 : white
        if(MainActivity.transPoint.equals("WHITE")) {
            viewHodler.tvHeight.setTextColor(Color.BLACK);
            viewHodler.tvWeight.setTextColor(Color.BLACK);
            tvH.setTextColor(Color.BLACK);
            tvW.setTextColor(Color.BLACK);
            tvC.setTextColor(Color.BLACK);


            //항목마다 색깔을 달리함.
            if (pos % 2 == 0)
                v.setBackground(context.getDrawable(R.drawable.layout_background));
            else
                v.setBackground(context.getDrawable(R.drawable.layout_background_ver2));
        }
        else {
            viewHodler.tvHeight.setTextColor(Color.WHITE);
            viewHodler.tvWeight.setTextColor(Color.WHITE);
            tvH.setTextColor(Color.WHITE);
            tvW.setTextColor(Color.WHITE);
            tvC.setTextColor(Color.WHITE);

            if (pos % 2 == 0)
                v.setBackground(context.getDrawable(R.drawable.layout_background_dark));
            else
                v.setBackground(context.getDrawable(R.drawable.layout_background_dark_ver2));
        }
    }
}

