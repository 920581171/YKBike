package com.yk.bike.activity;

import android.graphics.Color;
import android.os.Build;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.base.AlertDialogListener;
import com.yk.bike.base.BaseActivity;
import com.yk.bike.constant.Consts;
import com.yk.bike.utils.SharedPreferencesUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

/**
 * Created by 92058 on 2017/5/22.
 */

public class MineWeeperActivity extends BaseActivity {
    private LinearLayout[] mineLinear = new LinearLayout[10];
    private Mine[][] mine = new Mine[10][10];
    private int num = 10; //每条边砖块数
    private int mineNum = 10; //地雷数
    private int remain = 100; //剩下砖块数
    private boolean fristClick = false;//第一次点击
    private Button again;
    private TextView annal;
    private Chronometer timer;

    private Drawable drawableStart;
    private Drawable drawableMine;
    private Drawable drawableNull;
    private Drawable drawableFlag;
    private Drawable drawableError;
    private Drawable drawableRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mineweeper);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        again = findViewById(R.id.again);
        annal = findViewById(R.id.annal);
        timer = findViewById(R.id.timer);

        drawableStart = getDrawable(R.drawable.ic_mineweeper_start);
        drawableMine = getDrawable(R.drawable.ic_mineweeper_mine);
        drawableNull = getDrawable(R.drawable.ic_mineweeper_null);
        drawableFlag = getDrawable(R.drawable.ic_mineweeper_flag);
        drawableError = getDrawable(R.drawable.ic_mineweeper_error);
        drawableRight = getDrawable(R.drawable.ic_mineweeper_right);
        //        mineError = getDrawable(R.drawable.ic_error);
        //        mineError = getDrawable(R.drawable.ic_error);

        for (int i = 0; i < 10; i++) {
            int linearId = getResources().getIdentifier("mineLinear" + i, "id", getPackageName());
            mineLinear[i] = (LinearLayout) findViewById(linearId);
            for (int j = 0; j < 10; j++) {
                mine[i][j] = new Mine(this);
                mine[i][j].setBackground(drawableStart);
                final int finalI = i;
                final int finalJ = j;
                mine[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!fristClick) {
                            timer.setBase(SystemClock.elapsedRealtime());
                            timer.start();
                            mine[finalI][finalJ].setBackground(drawableNull);
                            mine[finalI][finalJ].setStatus(2);
                            fristClick = true;
                            CreateMine(10);
                            SetNull(finalI, finalJ);
                        } else {
                            //判断是否是地雷
                            SetNull(finalI, finalJ);
                            if (remain == mineNum) {
                                timer.stop();

                                showAlertDialog("你赢了！！","你的时间：" + timer.getText(),new String[]{"再来一局","取消"},new AlertDialogListener(){
                                    @Override
                                    public void onPositiveClick(DialogInterface dialog, int which) {
                                        Again();
                                    }
                                });

                                for (int i = 0; i < num; i++) {
                                    for (int j = 0; j < num; j++) {
                                        mine[i][j].setEnabled(false);
                                        if (mine[i][j].getStatus() == 1) {
                                            mine[i][j].setBackground(drawableMine);
                                        }
                                        if (mine[i][j].getStatus() == 1 && mine[i][j].isFlag()) {
                                            mine[i][j].setBackground(drawableRight);
                                        }
                                        if (mine[i][j].getStatus() == 0 && mine[i][j].isFlag()) {
                                            mine[i][j].setBackground(drawableError);
                                        }
                                    }
                                }
                                annal.setText("最快：" + Fastest());
                            }
                        }
                    }
                });

                mine[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (!mine[finalI][finalJ].isFlag()) {
                            mine[finalI][finalJ].setBackground(drawableFlag);
                            mine[finalI][finalJ].setFlag(true);
                            return true;
                        } else {
                            mine[finalI][finalJ].setBackground(drawableStart);
                            mine[finalI][finalJ].setFlag(false);
                            return true;
                        }
                    }
                });
                mineLinear[i].addView(mine[i][j]);
            }
        }

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Again();
            }
        });

        annal.setText("最快：" + Fastest());
    }

    //创建地雷
    public void CreateMine(int num) {
        for (int i = 0; i < this.mineNum; ) {
            int X = new Random().nextInt(this.num);
            int Y = new Random().nextInt(this.num);

            if (mine[X][Y].getStatus() == 0) {
                mine[X][Y].setStatus(1);
                i++;
            }
        }
    }

    //判断地雷
    public boolean Boom(int X, int Y) {
        if (mine[Integer.valueOf(X)][Integer.valueOf(Y)].getStatus() == 1) {
            return true;
        } else {
            return false;
        }
    }

    //排雷
    public void SetNull(int XX, int YY) {
        int roundX = 3;
        int roundY = 3;
        int X = XX;
        int Y = YY;
        int thisX = XX;
        int thisY = YY;
        int count = 0;
        if (!Boom(XX, YY)) {

            //判断边界
            X = thisX - 1;
            Y = thisY - 1;
            if (X < 0) {
                X = X + 1;
                roundX = 2;
            }
            if (Y < 0) {
                Y = Y + 1;
                roundY = 2;
            }
            if (X == this.num - 2) {
                roundX = 2;
            }
            if (Y == this.num - 2) {
                roundY = 2;
            }

            //计算周围地雷
            for (int i = 0; i < roundX; i++) {
                for (int j = 0; j < roundY; j++) {
                    Y = Y + j;
                    if (Boom(X, Y)) {
                        count++;
                    }
                    Y = Y - j;
                }
                X++;
            }

            //周围无地雷
            if (count == 0) {
                mine[thisX][thisY].setEnabled(false);
                mine[thisX][thisY].setFlag(false);
                mine[thisX][thisY].setBackground(drawableNull);
                mine[thisX][thisY].setStatus(2);
                remain--;
                X = thisX - 1;
                Y = thisY - 1;
                if (X < 0) {
                    X = X + 1;
                    roundX = 2;
                }
                if (Y < 0) {
                    Y = Y + 1;
                    roundY = 2;
                }
                if (X == this.num - 2) {
                    roundX = 2;
                }
                if (Y == this.num - 2) {
                    roundY = 2;
                }
                for (int i = 0; i < roundX; i++) {
                    for (int j = 0; j < roundY; j++) {
                        Y = Y + j;
                        //                        mine[X][Y].setBackground(mineError);
                        //                        递归查找下一个砖块
                        if (mine[X][Y].getStatus() == 0) {
                            SetNull(X, Y);
                        }
                        Y = Y - j;
                    }
                    X++;
                }
            }

            //周围有地雷，显示数量
            else {
                mine[thisX][thisY].setEnabled(false);
                mine[thisX][thisY].setFlag(false);
                mine[thisX][thisY].setBackground(setMineNum(count));
                mine[thisX][thisY].setStatus(3);
                remain--;
            }
        }

        //炸了。。。
        else {
            timer.stop();
            showAlertDialog("你输了！！","再接再厉",new String[]{"再来一局","取消"},new AlertDialogListener(){
                @Override
                public void onPositiveClick(DialogInterface dialog, int which) {
                    Again();
                }
            });

            //遍历并显示所有地雷
            for (int i = 0; i < num; i++) {
                for (int j = 0; j < num; j++) {
                    mine[i][j].setEnabled(false);
                    if (mine[i][j].getStatus() == 1) {
                        mine[i][j].setBackground(drawableMine);
                    }
                    if (mine[i][j].getStatus() == 1 && mine[i][j].isFlag()) {
                        mine[i][j].setBackground(drawableRight);
                    }
                    if (mine[i][j].getStatus() == 0 && mine[i][j].isFlag()) {
                        mine[i][j].setBackground(drawableError);
                    }
                }
            }
        }
    }

    //显示地雷数量图片
    private Drawable setMineNum(int i) {
        Drawable num = getDrawable(getResources().getIdentifier("ic_mineweeper_mine_" + i, "drawable", getPackageName()));
        return num;
    }

    //比较最快时间
    private String Fastest() {
        String fast = (String) SharedPreferencesUtils.get(Consts.SP_STRING_MINE_FASTEST,"99:99");
        String s = (String) timer.getText();
        if (s.equals("00:00")) {
            return fast;
        }

        DateFormat dateFormat = new SimpleDateFormat("mm:ss");
        try {
            Date fastTime = dateFormat.parse(fast);
            Date newTime = dateFormat.parse(s);

            long diff = fastTime.getTime() - newTime.getTime();
            if (diff < 0) {
                return fast;
            } else {
                SharedPreferencesUtils.put(Consts.SP_STRING_MINE_FASTEST,s);
                return s;
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    //再玩一局
    private void Again() {
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                timer.setBase(SystemClock.elapsedRealtime());
                timer.stop();
                remain = 100;
                fristClick = false;
                mine[i][j].setStatus(0);
                mine[i][j].setFlag(false);
                mine[i][j].setBackground(drawableStart);
                mine[i][j].setEnabled(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class Mine extends android.support.v7.widget.AppCompatImageView {

        /**
         * 当前状态
         * 0：初始状态 start
         * 1：地雷 mine
         * 2：不是地雷 null
         * 3：旗帜 flag
         * 4: 显示数量
         * 5：错误
         * 6: 正确
         */
        private int status = 0;
        private boolean flag = false;

        public Mine(Context context) {
            super(context);
        }

        public Mine(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public Mine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }
    }
}
