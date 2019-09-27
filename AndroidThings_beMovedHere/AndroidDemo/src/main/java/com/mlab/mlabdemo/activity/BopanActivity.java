package com.mlab.mlabdemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mlab.mlabdemo.tools.NumericWheelAdapter;
import com.mlab.mlabdemo.tools.OnWheelChangedListener;
import com.mlab.mlabdemo.tools.OnWheelScrollListener;
import com.mlab.mlabdemo.tools.WheelView;

import java.util.ArrayList;
import java.util.List;

public class BopanActivity extends Activity implements View.OnClickListener {

    private Button btnBack;
    private Button btnBoPanOK;
    NumericWheelAdapter numericWheelAdapterProduct;//适配器
    List<String> showStrListData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_bopan);
        initWheelForShowStrForProduct((R.id.wheelProduct));
        btnBack = (Button) findViewById(R.id.id_btn_boPanBack);
        btnBoPanOK = (Button) findViewById(R.id.id_btn_boPanOk);
        btnBoPanOK.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    // Wheel scrolled flag
    private boolean wheelScrolled = false;

    // Wheel scrolled listener
    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        public void onScrollingStarted(WheelView wheel) {
            wheelScrolled = true;
        }

        public void onScrollingFinished(WheelView wheel) {
            wheelScrolled = false;
            updateStatus();
        }
    };

    // Wheel changed listener
    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (!wheelScrolled) {
                updateStatus();
            }
        }
    };

    /**
     * Updates entered PIN status;
     * 更新显示数据
     */
    private void updateStatus() {
        EditText etProduct = (EditText) findViewById(R.id.etProduct);
        etProduct.setText(numericWheelAdapterProduct.getItem(getWheel(R.id.wheelProduct).getCurrentItem()));
    }

    /**
     * Initializes wheel; 滚轮中显示 字符串形式时候的初始化；
     *
     * @param id the wheel widget Id
     */
    private void initWheelForShowStrForProduct(int id) {

        //构造 数据源
        List<String> showStrListData1;
        showStrListData1 = new ArrayList<>();
        showStrListData1.add("装修材料");
        showStrListData1.add("化妆品");
        showStrListData1.add("办公用品");
        showStrListData1.add("玩具");
        showStrListData1.add("日用品");
        WheelView wheel = getWheel(id);
        numericWheelAdapterProduct = new NumericWheelAdapter(1000, 10000, true, showStrListData1);
        numericWheelAdapterProduct.showStrOrInt = true;
        wheel.setAdapter(numericWheelAdapterProduct);

        wheel.setCurrentItem(0);

        wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(scrolledListener);
        wheel.setCyclic(true);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
    }

    /**
     * Returns wheel by Id
     *
     * @param id the wheel Id
     * @return the wheel with passed Id
     */
    private WheelView getWheel(int id) {
        return (WheelView) findViewById(id);
    }


    /**
     * Tests wheel value
     *
     * @param id    the wheel Id
     * @param value the value to test
     * @return true if wheel value is equal to passed value
     */
    private boolean testWheelValue(int id, int value) {

        return getWheel(id).getCurrentItem() == value;
    }

    /**
     * Mixes wheel
     *
     * @param id the wheel id
     */
    private void mixWheel(int id) {
        WheelView wheel = getWheel(id);
        wheel.scroll(-25 + (int) (Math.random() * 50), 2000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_boPanBack:
                this.onBackPressed();
                break;
            case R.id.id_btn_boPanOk:
                Toast.makeText(this, "您点击了按钮哦", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
