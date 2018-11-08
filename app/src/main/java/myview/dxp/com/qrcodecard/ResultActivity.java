package myview.dxp.com.qrcodecard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.dtr.zxing.decode.DecodeThread;

/**
 * 二维码扫描后的信息显示页面
 */
public class ResultActivity extends AppCompatActivity {
//    显示图片
    private ImageView iv_result;
//    显示文本信息
    private TextView tv_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_result);
        iv_result = (ImageView) findViewById(R.id.iv_result);
        tv_result = (TextView) findViewById(R.id.tv_result);
        Intent data = getIntent();
        Bundle bundle = data.getExtras();
//		扫码获得一个字符串内容
        String str = bundle.getString("result");
        tv_result.setText(str);
//		扫码获得一张图片
        Bitmap barcode = null;
        byte[] compressedBitmap = bundle.getByteArray(DecodeThread.BARCODE_BITMAP);
        if (compressedBitmap != null) {
            barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
            // Mutable copy:
            barcode = barcode.copy(Bitmap.Config.RGB_565, true);
        }
        iv_result.setImageBitmap(barcode);

    }
}
