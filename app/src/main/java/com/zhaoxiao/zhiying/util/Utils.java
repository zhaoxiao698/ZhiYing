//package com.zhaoxiao.zhiying.util;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.graphics.drawable.ColorDrawable;
//import android.graphics.drawable.Drawable;
//import android.util.LruCache;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.flexbox.FlexDirection;
//import com.google.android.flexbox.FlexWrap;
//import com.google.android.flexbox.FlexboxLayoutManager;
//import com.google.android.flexbox.JustifyContent;
//import com.just.agentweb.core.AgentWeb;
//import com.just.agentweb.core.client.DefaultWebClient;
//import com.luck.picture.lib.PictureSelectionModel;
//import com.luck.picture.lib.PictureSelector;
//import com.luck.picture.lib.config.PictureConfig;
//import com.luck.picture.lib.config.PictureMimeType;
//import com.xuexiang.xui.XUI;
//import com.xuexiang.xui.utils.DrawableUtils;
//import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
//import com.xuexiang.xui.widget.imageview.preview.PreviewBuilder;
//import com.xuexiang.xupdate.XUpdate;
//import com.xuexiang.xutil.XUtil;
//import com.xuexiang.xutil.app.ActivityUtils;
//import com.xuexiang.xutil.common.StringUtils;
//import com.xuexiang.xutil.data.DateUtils;
//import com.xuexiang.xutil.file.FileIOUtils;
//import com.xuexiang.xutil.file.FileUtils;
//import com.zhaoxiao.zhiying.R;
//import com.zhaoxiao.zhiying.entity.community.ImageViewInfo;
//
//import java.io.File;
//
//
///**
// * @author XUE
// * @since 2019/4/1 11:25
// */
//public final class Utils {
//
//    public final static String UPDATE_URL = "https://gitee.com/xuexiangjys/XUI/raw/master/jsonapi/update_api.json";
//
//    private Utils() {
//        throw new UnsupportedOperationException("u can't instantiate me...");
//    }
//
//    //==========图片选择===========//
//
//    /**
//     * 获取图片选择的配置
//     *
//     * @param fragment
//     * @return
//     */
//    public static PictureSelectionModel getPictureSelector(Fragment fragment) {
//        return PictureSelector.create(fragment)
//                .openGallery(PictureMimeType.ofImage())
//                .theme(SettingSPUtils.getInstance().isUseCustomTheme() ? R.style.XUIPictureStyle_Custom : R.style.XUIPictureStyle)
//                .maxSelectNum(8)
//                .minSelectNum(1)
//                .selectionMode(PictureConfig.MULTIPLE)
//                .previewImage(true)
//                .isCamera(true)
//                .enableCrop(false)
//                .compress(true)
//                .previewEggs(true);
//    }
//
//    public static PictureSelectionModel getPictureSelector(Activity activity) {
//        return PictureSelector.create(activity)
//                .openGallery(PictureMimeType.ofImage())
//                .theme(SettingSPUtils.getInstance().isUseCustomTheme() ? R.style.XUIPictureStyle_Custom : R.style.XUIPictureStyle)
//                .maxSelectNum(8)
//                .minSelectNum(1)
//                .selectionMode(PictureConfig.MULTIPLE)
//                .previewImage(true)
//                .isCamera(true)
//                .enableCrop(false)
//                .compress(true)
//                .previewEggs(true);
//    }
//
//    //==========图片预览===========//
//
//    /**
//     * 大图预览
//     *
//     * @param fragment
//     * @param url      图片资源
//     * @param view     小图加载控件
//     */
//    public static void previewPicture(Fragment fragment, String url, View view) {
//        if (fragment == null || StringUtils.isEmpty(url)) {
//            return;
//        }
//        Rect bounds = new Rect();
//        if (view != null) {
//            view.getGlobalVisibleRect(bounds);
//        }
//        PreviewBuilder.from(fragment)
//                .setImgs(ImageViewInfo.newInstance(url, bounds))
//                .setCurrentIndex(0)
//                .setSingleFling(true)
//                .setProgressColor(R.color.xui_config_color_main_theme)
//                .setType(PreviewBuilder.IndicatorType.Number)
//                .start();
//    }
//
//
//    //==========拍照===========//
//
//    public static final String JPEG = ".jpeg";
//
//    /**
//     * 处理拍照的回调
//     *
//     * @param data
//     * @return
//     */
//    public static String handleOnPictureTaken(byte[] data) {
//        return handleOnPictureTaken(data, JPEG);
//    }
//
//    /**
//     * 处理拍照的回调
//     *
//     * @param data
//     * @return
//     */
//    public static String handleOnPictureTaken(byte[] data, String fileSuffix) {
//        String picPath = FileUtils.getDiskCacheDir() + "/images/" + DateUtils.getNowMills() + fileSuffix;
//        boolean result = FileIOUtils.writeFileFromBytesByStream(picPath, data);
//        return result ? picPath : "";
//    }
//
//    public static String getImageSavePath() {
//        return FileUtils.getDiskCacheDir("images") + File.separator + DateUtils.getNowMills() + JPEG;
//    }
//}
