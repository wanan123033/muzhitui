package base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.nevermore.muzhitui.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import base.util.PhotoUtil;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by gk on 2016/1/29.
 */
public class ImageUtil {

    /**
     * 根据计算的inSampleSize，得到压缩后图片
     *
     * @param pathName
     * @param reqWidth
     * @param reqHeight
     * @return
     */

    private static ImageUtil mInstance;

    public static ImageUtil getInstance() {
        if (mInstance == null) {
            synchronized (ImageUtil.class) {
                if (mInstance == null) {
                    mInstance = new ImageUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 根据计算的inSampleSize，得到压缩后图片
     *
     * @param
     * @param reqWidth
     * @param reqHeight
     * @return
     */
 /*   public Bitmap decodeSampledBitmapFromResource(String pathName,
                                                  int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        Logger.i("options.inSampleSize = " + options.inSampleSize);
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);

        return cQuality(bitmap);
    }

*/
    public void decodeBitmapFromBitmap(Bitmap bitmapSrc, int reqWidth, int reqHeight, FileOutputStream fileOutputStream) {
        if (reqHeight == 0) {
            reqHeight = bitmapSrc.getHeight();
        }
        int inSampleSize = calculateRadio(bitmapSrc.getWidth(), bitmapSrc.getHeight(), reqWidth, reqHeight);
        bitmapSrc = bitmapSrc.createScaledBitmap(bitmapSrc, bitmapSrc.getWidth() / inSampleSize, bitmapSrc.getHeight() / inSampleSize, false);
        cQuality(bitmapSrc, fileOutputStream);
//        return bitmapSrc;
    }

    /**
     * 计算inSampleSize，用于压缩图片
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        // 源图片的宽度
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;

      /*  if (width > reqWidth && height > reqHeight) {
            // 计算出实际宽度和目标宽度的比率
            int widthRatio = Math.round((float) width / (float) reqWidth);
            int heightRatio = Math.round((float) height / (float) reqHeight);
            inSampleSize = Math.max(widthRatio, heightRatio);
        }*/
        inSampleSize = calculateRadio(width, height, reqWidth, reqHeight);

        return inSampleSize;
    }

    private int calculateRadio(int width, int height, int reqWidth, int reqHeight) {
        int inSampleSize = 1;
        if (width >= height && width > reqWidth) {//如果宽度大的话根据宽度固定大小缩放
            inSampleSize = (int) (width / reqWidth);
        } else if (width < height && height > reqHeight) {//如果高度高的话根据宽度固定大小缩放
            inSampleSize = (int) (height / reqHeight);
        }
        return inSampleSize;
    }

/*
    public boolean saveBitmap2file(Bitmap bmp, String filename) {
        int quality = 100;
        OutputStream stream = null;
        Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
        try {
            stream = new FileOutputStream(UIUtils.getExternalCacheDir() + File.separator + filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (!(filename.endsWith(".png") || filename.endsWith(".PNG"))) {
            format = Bitmap.CompressFormat.JPEG;
            bmp = cQuality(bmp);
        }
        return bmp.compress(format, quality, stream);
    }*/

    /**
     * 根据bitmap压缩图片质量
     *
     * @param bitmap 未压缩的bitmap
     * @return 压缩后的bitmap
     */
    public void cQuality(Bitmap bitmap, FileOutputStream fileOutputStream) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        int beginRate = 100;
        //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
        while (bOut.size() / 1024 > 90) {  //如果压缩后大于100Kb，则提高压缩率，重新压缩   如果是png会进入死循环吗
            beginRate -= 10;
            bOut.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, beginRate, bOut);
        }
        try {
            fileOutputStream.write(bOut.toByteArray());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


      /*  Logger.i("==================" + beginRate);
        ByteArrayInputStream bInt = new ByteArrayInputStream(bOut.toByteArray());
        Bitmap newBitmap = BitmapFactory.decodeStream(bInt);
        if (newBitmap != null) {
            return newBitmap;
        } else {
            return bitmap;
        }*/
    }


    final int REQUEST_CODE_GALLERY = 1001;

    public void chooseImage(String text,GalleryFinal.OnHanlderResultCallback onHanlderResultCallback, int size) {
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(false)
                .setEnableEdit(false)
                .setEnableCrop(false)
                .setEnableRotate(false)
                .setCropSquare(false)
                .setBtnText(text)
                .setEnablePreview(true).setMutiSelectMaxSize(size)
                .build();
        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, onHanlderResultCallback);
    }

    public void openCamera(GalleryFinal.OnHanlderResultCallback onHanlderResultCallback) {
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(false)
                .setEnableCrop(true)
                .setEnableRotate(false)
                .setCropSquare(true)
                .setEnablePreview(false)
                .build();
        GalleryFinal.openCamera(REQUEST_CODE_GALLERY, functionConfig, onHanlderResultCallback);
    }

    public Map<String, RequestBody> wrapUploadImgRequest(File imgFile) {
        Map<String, RequestBody> map = new HashMap<>();
        if (imgFile != null) {
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), imgFile);
            map.put("image\"; filename=\"" + imgFile.getName() + "", fileBody);

        }

        return map;
    }

    public DisplayImageOptions getCircleDisplayOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.ic_default_img) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_default_img)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.ic_default_img)  //设置图片加载解码过程中错误时候显示的图片
                .showImageOnLoading(R.mipmap.ic_default_img)
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true).displayer(new CircleBitmapDisplayer()).build();//设置下载的图片是否缓存在SD卡中
        return options;
    }


    public DisplayImageOptions getBaseDisplayOption() {
        //R.drawable.ic_gf_default_photo
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.mipmap.login_muzhitui)
                .showImageForEmptyUri(R.mipmap.login_muzhitui)
                .showImageOnLoading(R.mipmap.login_muzhitui).cacheInMemory(true).cacheOnDisk(true).build();
        return options;
    }

    /**
     * 根据图片路径生成bitmap 并压缩，防止内存溢出
     *
     * @param srcPath
     * @return
     */
    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    public static Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 将bitmap 压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 将bitmap 压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image, int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.size() / 1024 > size) {    //
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        MyLogger.kLog().e("图片压缩后大小："+baos.toByteArray().length);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        MyLogger.kLog().e("图片压缩后大小："+bitmap.getByteCount());
        return bitmap;
    }

    /**
     * 创建文件
     *
     * @return
     * @throws IOException
     */
    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
        return image;
    }

    /***
     * 根据图片路径生成文件并压缩
     * @param path
     * @return
     */
    public static File scal(String path) {
        //String path = fileUri.getPath();
        File outputFile = new File(path);
        long fileSize = outputFile.length();

        final long fileMaxSize = 200 * 1024;
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int height = options.outHeight;
            int width = options.outWidth;

            double scale = Math.sqrt((float) fileSize / fileMaxSize);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            outputFile = new File(PhotoUtil.createImageFile().getPath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.d("", "sss ok " + outputFile.length());
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            } else {
                File tempFile = outputFile;
                outputFile = new File(PhotoUtil.createImageFile().getPath());
                PhotoUtil.copyFileUsingFileChannels(tempFile, outputFile);
            }

        }
        return outputFile;
    }
    public static File scal(String path,int size) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        bitmap = compressImage(bitmap,size);
        FileOutputStream fos = null;
        try {
            File file = new File(PhotoUtil.createImageFile().getPath());
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            bitmap.recycle();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /***
     * 根据uri生成文件并压缩
     * @param fileUri
     * @return
     */
    public static File scal(Uri fileUri) {
        String path = fileUri.getPath();
        File outputFile = new File(path);
        long fileSize = outputFile.length();
        final long fileMaxSize = 200 * 1024;
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int height = options.outHeight;
            int width = options.outWidth;

            double scale = Math.sqrt((float) fileSize / fileMaxSize);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            outputFile = new File(PhotoUtil.createImageFile().getPath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch bloc v
                e.printStackTrace();
            }
            Log.d("", "sss ok " + outputFile.length());
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            } else {
                File tempFile = outputFile;
                outputFile = new File(PhotoUtil.createImageFile().getPath());
                PhotoUtil.copyFileUsingFileChannels(tempFile, outputFile);
            }

        }
        return outputFile;

    }

    public static String saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            bmp.recycle();
            bmp = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        MediaScannerConnection.scanFile(context,new String[] {Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath()+ "/"+ file.getParentFile().getAbsolutePath() }, null,null);
        return file.getAbsolutePath();
    }
    public static String saveImageToGallery(Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            if (!file.exists())
                file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            bmp.recycle();
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 清除App缓存
     */
    public static void clearApp(){
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        deleteDirWihtFile(appDir);
        SPUtils.clear();
        File appPage = Environment.getDataDirectory();
        deleteDirWihtFile(appPage);
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }
}
