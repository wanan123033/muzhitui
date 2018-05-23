package base;

import android.os.Vibrator;

import com.nevermore.muzhitui.R;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.QRCodeView;

public class ScanQRActivity extends BaseActivityTwoV implements QRCodeView.Delegate {
    @BindView(R.id.zxingview)
    QRCodeView mQRCodeView;

    @Override
    public void init() {
        mQRCodeView.startSpot();
        mQRCodeView.setResultHandler(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_scan_qr;
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(final String result) {
        showTest("groupId = " + result);
        vibrate();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        showTest("打开相机出错");
    }
}
