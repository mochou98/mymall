package org.mochou.mymall.core.system;

import org.mochou.mymall.core.util.SystemInfoPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 系统启动服务，用于检查系统状态及打印系统信息
 */
@Component
class SystemInistService {
    private SystemInistService systemInistService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private Environment environment;

    @PostConstruct
    private void inist() {
        systemInistService = this;

        try {
            SystemInfoPrinter.printInfo("Mymall 初始化信息", getSystemInfo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> getSystemInfo() {

        Map<String, String> infos = new LinkedHashMap<>();

        infos.put(SystemInfoPrinter.CREATE_PART_COPPER + 0, "系统信息");
        // 测试获取application-db.yml配置信息
        infos.put("服务器端口", environment.getProperty("server.port"));
        infos.put("数据库USER", environment.getProperty("spring.datasource.druid.username"));
        infos.put("数据库地址", environment.getProperty("spring.datasource.druid.url"));
        infos.put("调试级别", environment.getProperty("logging.level.org.mochou.mymall.wx"));

        // 测试获取application-core.yml配置信息
        infos.put(SystemInfoPrinter.CREATE_PART_COPPER + 1, "模块状态");
        infos.put("邮件", environment.getProperty("mymall.notify.mail.enable"));
        infos.put("短信", environment.getProperty("mymall.notify.sms.enable"));
        infos.put("模版消息", environment.getProperty("mymall.notify.wx.enable"));
        infos.put("快递信息", environment.getProperty("mymall.express.enable"));
        infos.put("快递鸟ID", environment.getProperty("mymall.express.appId"));
        infos.put("对象存储", environment.getProperty("mymall.storage.active"));
        infos.put("本地对象存储路径", environment.getProperty("mymall.storage.local.storagePath"));
        infos.put("本地对象访问地址", environment.getProperty("mymall.storage.local.address"));
        infos.put("本地对象访问端口", environment.getProperty("mymall.storage.local.port"));

        // 微信相关信息
        infos.put(SystemInfoPrinter.CREATE_PART_COPPER + 2, "微信相关");
        infos.put("微信APP KEY", environment.getProperty("mymall.wx.app-id"));
        infos.put("微信APP-SECRET", environment.getProperty("mymall.wx.app-secret"));
        infos.put("微信支付MCH-ID", environment.getProperty("mymall.wx.mch-id"));
        infos.put("微信支付MCH-KEY", environment.getProperty("mymall.wx.mch-key"));
        infos.put("微信支付通知地址", environment.getProperty("mymall.wx.notify-url"));

        //测试获取System表配置信息
        infos.put(SystemInfoPrinter.CREATE_PART_COPPER + 3, "系统设置");
        infos.put("自动创建朋友圈分享图", Boolean.toString(SystemConfig.isAutoCreateShareImage()));
        infos.put("商场名称", SystemConfig.getMallName());
        infos.put("首页显示记录数：NEW,HOT,BRAND,TOPIC,CatlogList,CatlogMore", SystemConfig.getNewLimit() + "," + SystemConfig.getHotLimit() + "," + SystemConfig.getBrandLimit() + "," + SystemConfig.getTopicLimit() + "," + SystemConfig.getCatlogListLimit() + "," + SystemConfig.getCatlogMoreLimit());

        return infos;
    }
}
