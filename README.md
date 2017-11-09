# Orhttp
study OkHttp3 and Retrofit2

### UI主线程禁止使用http
##### 线程调用 GET
``` java
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
```

### okHttp3 提供的异步调用
``` java
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
            }
        });
    }

//同样也是使用回调的方式得到返回内容
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

```
#### POST方法上传文件类型参数参考这里[MIME 参考手册](http://www.w3school.com.cn/media/media_mimeref.asp)

