package com.example.vcsystem;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.vcsystem.model.ReadModelPitch;
import com.example.vcsystem.model.FHPupload;
import com.example.vcsystem.model.Lbsupload;
import com.example.vcsystem.ui.RealtimeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class BTtoDBservice extends IntentService {

    private static final String TAG = "TAG";
    private String CHANNEL_ID = "Coder";

    //藍芽所需
    private int mMaxChars = 100;//Default
    private UUID mDeviceUUID;
    private BluetoothAdapter btAdapter = null;
    private static BluetoothSocket mBTSocket = null;
    private BluetoothDevice mDevice = null;
    private InputStream inputStream = null;
    private static OutputStream outputStream = null;
    private boolean bStop = false;
    private boolean mConnectSuccessful = true;
    private boolean mIsUserInitiatedDisconnect = false;
    private boolean mIsBluetoothConnected = false;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    //lbs為角度磅數，FHP為頭部前傾姿勢
    private int lbs60, lbs49, lbs40, lbs27, lbs12;
    private int NoFHP, sFHP, mFHP;

    private String mAccount, userID;

    //設定日期格式
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdfhavehm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat sdfonlydate = new SimpleDateFormat("yyyy-MM-dd");

    Handler mHandler;

    public BTtoDBservice() {
        super("BTtoDBservice");
        FirebaseApp.initializeApp(this);
        mHandler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle b = intent.getExtras();
        mDevice = b.getParcelable(Bluetooth.DEVICE_EXTRA);
        Log.d(TAG, "UUID" + b.getString(Bluetooth.DEVICE_UUID));
        mDeviceUUID = UUID.fromString(b.getString(Bluetooth.DEVICE_UUID));
        mMaxChars = b.getInt(Bluetooth.BUFFER_SIZE);
        btAdapter = BluetoothAdapter.getDefaultAdapter();

        try {
            if (mBTSocket == null || !mIsBluetoothConnected) {
                mBTSocket = mDevice.createInsecureRfcommSocketToServiceRecord(mDeviceUUID);
                mBTSocket.connect();
            }
        } catch (IOException e) {
// Unable to connect to device
            e.printStackTrace();
            mConnectSuccessful = false;
        }

        if (!mConnectSuccessful) {
            mHandler.post(new DisplayToast(this, "無法連線至設備，請重新確認藍芽狀態"));
            stopSelf();
        } else {
            mHandler.post(new DisplayToast(this, "Link Start"));
            mIsBluetoothConnected = true;
        }


        InputStream inputStream;
        int j = 0;
        int k = 0;
        try {
            inputStream = mBTSocket.getInputStream();
            while (!bStop) {
                byte[] buffer = new byte[256];
                if (inputStream.available() > 0) {
                    inputStream.read(buffer);
                    int i = 0;
                    /*
                     * This is needed because new String(buffer) is taking the entire buffer i.e. 256 chars on Android 2.3.4 http://stackoverflow.com/a/8843462/1287554
                     */
                    for (i = 0; i < buffer.length && buffer[i] != 0; i++) {
                    }
                    final String strInput = new String(buffer, 0, i);

                    ReadModelPitch dataUpload = new ReadModelPitch(mAccount, strInput);
//                    Log.d(TAG , strInput);
                    Upload(dataUpload);
                    /*
                     * If checked then receive text, better design would probably be to stop thread if unchecked and free resources, but this is a quick fix
                     */
                    //lbs data up load
                    try {
                        double doubleInput = ((Math.abs(Double.parseDouble(strInput)) * 2.5) + 8.25);
                        if (j < 20) {
                            if (doubleInput <= 15) {
                                lbs12++;
                            } else if (doubleInput <= 30) {
                                lbs27++;
                            } else if (doubleInput <= 45) {
                                lbs40++;
                            } else if (doubleInput <= 60) {
                                lbs49++;
                            } else {
                                lbs60++;
                            }
                            if (j == 7 || j == 14 || j == 19) {
                                if (k <= 0) {
                                    if (lbs40 + lbs49 + lbs60 > 7) {
                                        Notification();
//                                        AlertDialogFunction();
                                        k++;
                                    }
                                }
                            }
                            j++;
                        } else {
                            Lbsupload lbsUpload = new Lbsupload(mAccount, lbs12, lbs27, lbs40, lbs49, lbs60);
                            LbsUpload(lbsUpload);
                            j = k = lbs27 = lbs12 = lbs40 = lbs49 = lbs60 = 0;
                        }
                    } catch (NumberFormatException e) {
                        Log.d(TAG, "OnFailure" + e.toString());
                    }
                    //FHP data up load
                    try {
                        double doubleInput = Math.abs(Double.parseDouble(strInput));
                        if (j < 20) {
                            if (doubleInput <= 23.51) {
                                NoFHP++;
                            } else if (doubleInput <= 35.66) {
                                sFHP++;
                            } else {
                                mFHP++;
                            }
                            j++;
                        } else {
                            FHPupload FHPupload = new FHPupload(mAccount, NoFHP, sFHP, mFHP);
                            FHPUpload(FHPupload);
                            j = NoFHP = mFHP = sFHP = 0;
                        }
                    } catch (NumberFormatException e) {
                        Log.d(TAG, "OnFailure" + e.toString());
                    }
                }
                Thread.sleep(3000);
            }
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        Log.i("DemoLog", "TestService -> onCreate, Thread ID: " + Thread.currentThread().getId());
        super.onCreate();
        Log.d(TAG, "BTtoDBservice start");

        FirebaseApp firebaseApp = FirebaseApp.initializeApp(getApplicationContext());
        if (firebaseApp != null) {
            firebaseFirestore = FirebaseFirestore.getInstance(firebaseApp);
            firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
//            sync();
        }

        /**檢查手機版本是否支援通知；若支援則新增"頻道"*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "DemoCode", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel);
        }
        //firebase UID
        userID = firebaseAuth.getUid();
        //CurrentUser's email
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                mAccount = task.getResult().getString("email");
                Log.d(TAG, "email:" + mAccount);
            }
        });
    }

    @Override
    public void onDestroy() {
        Log.i("DemoLog", "TestService -> onDestroy, Thread ID: " + Thread.currentThread().getId());
        super.onDestroy();
        Log.d(TAG, "stop BTservice");
        bStop = false;
        mIsBluetoothConnected = false;
        try {
            mBTSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Upload(ReadModelPitch dataUpload) {
        Date date = new Date();
        //進行轉換
        String dateString = sdf.format(date);
        //進行轉換
        String dateString4 = sdfonlydate.format(date);
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID).collection(dateString4).document(dateString);
        documentReference.set(dataUpload).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess");
            }
        });
        documentReference.set(dataUpload).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "OnFailure" + e.toString());
            }
        });
    }

    private void LbsUpload(Lbsupload lbsupload) {
        Date date = new Date();
        //進行轉換
        String dateString3 = sdfhavehm.format(date);

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID).collection("pieGraph").document(dateString3);
        documentReference.set(lbsupload).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess");
            }
        });
        documentReference.set(lbsupload).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "OnFailure" + e.toString());
            }
        });
    }

    private void FHPUpload(FHPupload FHPupload) {
        Date date = new Date();
        //進行轉換
        String dateString3 = sdfhavehm.format(date);

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID).collection("barGraph").document(dateString3);
        documentReference.set(FHPupload).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess");
            }
        });
        documentReference.set(FHPupload).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "OnFailure" + e.toString());
            }
        });
    }

    private void Notification() {
        NotificationCompat.Builder builder
                = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("坐姿不良！")
                .setContentText("提醒你記得調整姿勢喔～")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);
        /**發出通知*/
        NotificationManagerCompat notificationManagerCompat
                = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, builder.build());
    }

    private void AlertDialogFunction() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("調整頸部姿勢")
                .setMessage("雪莉？你剛攻擊我的頸部？我的《Healthy Body》頸部？")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY);
        alertDialog.show();
    }

    //https://stackoverflow.com/questions/5346980/intentservice-wont-show-toast
    public class DisplayToast implements Runnable {
        private final Context mContext;
        String mText;

        public DisplayToast(Context mContext, String text) {
            this.mContext = mContext;
            mText = text;
        }

        public void run() {
            Toast.makeText(mContext, mText, Toast.LENGTH_SHORT).show();
        }
    }

    public static void sendRecalibrate() throws IOException {
        String msg = "Recalibrate";
        byte[] bytes = msg.getBytes();
        Log.d(TAG, "msg" + bytes);
        outputStream = mBTSocket.getOutputStream();
        outputStream.write(bytes);
    }
}
