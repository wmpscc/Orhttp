package com.wmpscc.orhttp;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.platform.Platform;

import static okhttp3.internal.platform.Platform.INFO;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        new Thread(networkTask).start();
        asyGetHttp("http://www.baidu.com/");

    }

    public void asyGetHttp(String url)
    {
        OkHttpClient okHttpClient =new OkHttpClient();
        Request request =new Request.Builder().url(url).build();
        Call call= okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call,IOException e) {e.printStackTrace();}

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value",response.body().string());
                msg.setData(data);
                handler.sendMessage(msg);
//                System.out.println("我是异步线程,线程Id为:"+ Thread.currentThread().getId()+"body->"+response.body().string());
            }
        });
    }

    public String getHttp(String url)
    {
        OkHttpClient okHttpClient =new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call= okHttpClient.newCall(request);
        try {
            Response response = call.execute();

            return response.body().string(); //UTF-8解码后的内容
        }
        catch (IOException e) {
        e.printStackTrace();
        }
        return "-1";
    }

    public String postHttp(String url)
    {
        OkHttpClient okHttpClient =new OkHttpClient();


        //json表单
        MediaType JSON= MediaType.parse("application/json; charset=utf-8");
        RequestBody body= RequestBody.create(JSON,"你的json");

        //普通表单
//        RequestBody body =new FormBody.Builder().add("jian","zhi").add("jian","zhi").build();

        // 数据包含文件 未验证是否正常
//        File file = new File("file");
//        RequestBody requestBody =new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/png"), file)).build();


        Request request=new Request.Builder().url(url).post(body).build();
        Call call= okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            return response.body().string();
//            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "-1";
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("mylog", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
        }
    };

    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            String content = postHttp("http://47.95.7.169:8000/");
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", content);
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };
}
