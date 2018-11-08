package myview.dxp.com.qrcodecard;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dtr.zxing.activity.CaptureActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;

import base.BaseInterFace;
import view.MyLayout;

/**
 * 用于生成二维码和扫描二维码的主页
 */
public class MainActivity extends AppCompatActivity implements BaseInterFace{
    private static final int QR_WIDTH = 100;
    private static final int QR_HEIGHT = 100;

    private MyLayout name;                  //    姓名
    private MyLayout phone;                 //    手机号
    private MyLayout bookname;              //    借阅书名

    private MyLayout studentname;           //    学生号

    private MyLayout returnbookdata;        //    还书日期
//    显示生成的二维码
    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Button clickscan = (Button)findViewById( R.id.button );
        clickscan.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intentj = new Intent( MainActivity.this,CaptureActivity.class );//执行页面跳转到扫描页面
                startActivity( intentj );
            }
        } );
        init();
        initData();
        initOper();
    }

    @Override
    public void init() {
        name = (MyLayout) findViewById(R.id.name_layout);
        phone =(MyLayout)findViewById( R.id.phone_layout);
        bookname = (MyLayout) findViewById(R.id.bookname_layout);
        studentname = (MyLayout) findViewById(R.id.studentname_layout);
        returnbookdata = (MyLayout) findViewById(R.id.returnbookdata_layout);

        iv = (ImageView) findViewById(R.id.iv);
    }

    @Override
    public void initData() {
        name.setTv( getString( R.string.name ) );
        phone.setTv( getString( R.string.phone ) );
        bookname.setTv( getString( R.string.bookname ) );
        studentname.setTv( getString( R.string.studentname ) );
        returnbookdata.setTv( getString( R.string.returnbookdata ) );
    }

    @Override
    public void initOper() {
//        长按保存二维码图片到本地
        iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                判断是否有SD卡
                boolean flag =  Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
                String path = null;
                if (flag){
                    path = Environment.getExternalStorageDirectory().toString();
                }else{
                    path = Environment.getDataDirectory().toString();
                }
//                创建文件
                final File file = new File(path+"/二维码名片.jpg");
//                弹出对话框，确定是否保存
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("是否保存?");
                builder.setMessage("路径:"+path);
                builder.setNegativeButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) iv.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                            fos.flush();
                            fos.close();
                            toastStr("保存成功!");
                        } catch (Exception e) {
                            toastStr("保存失败");
                            e.printStackTrace();
                        }
                    }
                });
                builder.setPositiveButton("取消", null);
                builder.show();
                return true;
            }
        });
    }

    /**
     * 生成二维码
     * @param view
     */
    public void clickedCreate(View view)
    {
        //        进行显示
        iv.setVisibility( View.VISIBLE );
        //        判断名字
        if (name.getEtText().equals( "" ))
        {
            toastStr( "请输入姓名" );
            return;
        }
        //        判断手机号
        if (phone.getEtText().equals( "" ))
        {
            toastStr( "请输入手机号" );
            return;
        }
        if(bookname.getEtText().equals( "" ))
        {
            toastStr( "请输入借阅的书名" );
            return;
        }
        if(studentname.getEtText().equals( "" ))
        {
            toastStr( "请输入学生号" );
            return;
        }
        if(returnbookdata.getEtText().equals( "" ))
        {
            toastStr( "请输入还书的日期格式x年x月x日" );
            return;
        }
        //        生成二维码
        String url = name.getEtText() + "," + phone.getEtText() + "," + bookname.getEtText() + "," + studentname.getEtText() + "," + returnbookdata.getEtText();
        createQRImage( url );
    }
    public void toastStr(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }


    //    生成二维码
//    QR_WIDTH, QR_HEIGHT, 需要自己指定大小
    public void createQRImage(String url)
    {
        try
        {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1)
            {
                Toast.makeText(this,"请填写个人还书信息！",Toast.LENGTH_SHORT).show();
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++)
            {
                for (int x = 0; x < QR_WIDTH; x++)
                {
                    if (bitMatrix.get(x, y))
                    {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    }
                    else
                    {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);

            iv.setImageBitmap(bitmap);
        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }
    }
}
