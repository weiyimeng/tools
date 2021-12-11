代码结构
com.haoke91.a91edu
    adapter: 适配器
    db:数据库
    entities:实体类
    fragment:fragment模块界面
        main：首页
            HomeFragment:首页
            StudyFragment:学习
            FoundFragment:发现
            MineFragment:我的
    model：mvp层数据请求
    net：网络架构
    presenter：mvp逻辑层
    ui：界面
        adapter：
        business.alipay:业务支付
        course：选课
            SearchActivity:搜索页面
            SerialCourseActivity:系列课程
            CourseDetailActivity:课程详情
            SpecialClassActivity 专题课
        learn: 学习
            CalendarCourseActivity： 课程日历
            DailyWorkActivity     每日任务
            CourseOrderActivity  课次详情
            WrongExamBookActivity 错题本
        homework： 作业
             UpLoadHomeworkActivity 作业上传
             WrongExamActivity: 错题集
             CorrectExamActivity: 订正错题
        im：im
        liveRoom：
           AgoraActivity 声网
           VideoPlayerActivity 阿里
        myCollect:收藏夹
            MyCollectActivity 我的收藏
        order：订单
             OrderCenterActivity 订单界面
             AllOrderFragment
             ShoppingCartActivity 购物车
             AvailableCouponsActivity 可用优惠券
             PaySuccessActivity  支付成功
        user：
             SettingActivity 设置
             UserInfoActivity  用户信息
             UserImgActivity  头像编辑
        address:
              AddressManagerActivity  地址管理
              EditAddressActivity   新建地址/编辑地址
        login
             LoginActivity     登录

    utils：工具
    view：mvp用户交互层
    weight：
    widget：自定义控件
    wxApi：

    MainActivity：首页



    // 三方控件
    Calendar：https://github.com/huanghaibin-dev/CalendarView


