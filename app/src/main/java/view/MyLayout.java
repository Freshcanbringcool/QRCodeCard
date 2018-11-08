package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import myview.dxp.com.qrcodecard.R;

/**

 * 自定义框架布局
 */
public class MyLayout extends FrameLayout {
    private TextView tv;
    private EditText et;
    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
//        获取自定义布局和组件
        View view = LayoutInflater.from(context).inflate(R.layout.my_view,this);
        tv = (TextView) view.findViewById(R.id.tv);
        et = (EditText) view.findViewById(R.id.et);
    }

    /**
     * 设置TextView的内容
     * @param string
     */
    public void setTv(String string){
        tv.setText(string);

    }
    public void setEt(String string)
    {
        et.setWidth( 100 );

    }

    /**
     * 设置EditText的提示内容
     * @param string
     */
    public void setEtHint(String string){
        et.setHint(string);
    }

    /**
     * 得到输入的内容
     */
    public String getEtText(){
        return  et.getText().toString();
    }

}
