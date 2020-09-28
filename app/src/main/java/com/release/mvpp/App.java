package com.release.mvpp;

import android.content.Context;

import androidx.annotation.Nullable;

import com.facebook.stetho.Stetho;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.release.base.base.BaseApplication;
import com.release.base.utils.CommonUtil;
import com.release.base.utils.CrashHandler;
import com.release.base.utils.SPUtil;
import com.release.mvpp.dao.DaoMaster;
import com.release.mvpp.dao.DaoSession;
import com.release.mvpp.dao.NewsTypeDao;
import com.scwang.smartrefresh.header.TaurusHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.greendao.database.Database;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public class App extends BaseApplication {

    public DaoSession mDaoSession;
    private static App mContext;

    public static App getInstance() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initConfig();
        initLitePal();
        initLeakCanary();
        initBugly();
    }

    private void initBugly() {
        Context context = getApplicationContext();
// 获取当前包名
        String packageName = context.getPackageName();
// 获取当前进程名
        String processName = CommonUtil.getProcessName(android.os.Process.myPid());
// 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
// 初始化Bugly
        CrashReport.initCrashReport(context, "71c4c6e997", BuildConfig.DEBUG, strategy);
    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                //指定为经典Header，默认是 贝塞尔雷达Header
                return new TaurusHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
                //指定为经典Footer，默认是 BallPulseFooter
                //new ClassicsFooter(context).setDrawableSize(20);
                return new BallPulseFooter(context).setSpinnerStyle(SpinnerStyle.Scale);
            }
        });
    }

    private void initLeakCanary() {
        //判断是否是调试模式，如果是则开启Stetho、LeakCanary
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
//            if (LeakCanary.isInAnalyzerProcess(app)) {
//                //此过程专用于LeakCanary进行堆分析。在此过程中不应初始化应用程序。
//                return;
//            }
//            LeakCanary.install(app);
        }
    }

    private void initLitePal() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "dao_db");
        Database database = helper.getWritableDb();
        mDaoSession = new DaoMaster(database).newSession();
        NewsTypeDao.updateLocalData(this, mDaoSession);
    }

    private void initConfig() {
        SPUtil.getInstance(this);
//        CrashHandler.getInstance().init(this);

        PrettyFormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // 隐藏线程信息 默认：显示
                .methodCount(0)         // 决定打印多少行（每一行代表一个方法）默认：2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("cyc")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return com.release.base.BuildConfig.DEBUG;
            }
        });
    }

}
